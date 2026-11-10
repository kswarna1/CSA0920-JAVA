
class Wallet {

    int id;
    int balance;

    Wallet(int id, int balance) {
        this.id = id;
        this.balance = balance;
    }

    static void transfer(Wallet a, Wallet b, int amount) {

        Wallet first = a.id < b.id ? a : b;
        Wallet second = a.id < b.id ? b : a;

        synchronized (first) {
            synchronized (second) {

                a.balance -= amount;
                b.balance += amount;

            }
        }
    }
}

class TokenCounter {

    private int[] buffer = new int[5];
    private int front = 0;
    private int rear = 0;
    private int count = 0;

    synchronized void put(int value)
            throws InterruptedException {

        while (count == buffer.length) {
            wait();
        }

        buffer[rear] = value;
        rear = (rear + 1) % buffer.length;
        count++;

        notifyAll();
    }

    synchronized int take()
            throws InterruptedException {

        while (count == 0) {
            wait();
        }

        int value = buffer[front];
        front = (front + 1) % buffer.length;
        count--;

        notifyAll();

        return value;
    }
}

public class Task06 {

    public static void main(String[] args) {

        Wallet w1 = new Wallet(1, 1000);
        Wallet w2 = new Wallet(2, 1000);

        Thread t1 = new Thread(() ->
            Wallet.transfer(w1, w2, 100));

        Thread t2 = new Thread(() ->
            Wallet.transfer(w2, w1, 200));

        t1.start();
        t2.start();

        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            System.out.println("Transaction interrupted");
            Thread.currentThread().interrupt();
            return;
        }

        System.out.println("Wallet 1 Balance: " + w1.balance);
        System.out.println("Wallet 2 Balance: " + w2.balance);

        TokenCounter buffer = new TokenCounter();
        final int[] sum = {0};

        Thread producer = new Thread(() -> {

            try {
                for (int i = 1; i <= 20; i++) {
                    buffer.put(i);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        Thread consumer = new Thread(() -> {

            try {
                for (int i = 1; i <= 20; i++) {
                    sum[0] += buffer.take();
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        producer.start();
        consumer.start();

        try {
            producer.join();
            consumer.join();
        } catch (InterruptedException e) {
            System.out.println("Processing interrupted");
            Thread.currentThread().interrupt();
            return;
        }

        System.out.println("Producer-Consumer Sum: " + sum[0]);
        System.out.println("Processing completed");
    }
}
