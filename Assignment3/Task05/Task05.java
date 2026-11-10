
class Cinema {

    boolean[] seats = new boolean[10];

    synchronized void book(int seat) {

        if (!seats[seat]) {

            seats[seat] = true;

        }
    }

    int getTotalSeatsSold() {

        int count = 0;

        for (boolean seat : seats) {

            if (seat) {
                count++;
            }
        }

        return count;
    }
}

public class Task05 {

    public static void main(String[] args) {

        Cinema cinema = new Cinema();

        Thread[] customers = new Thread[100];

        for (int i = 0; i < 100; i++) {

            final int seat = i % 10;

            customers[i] = new Thread(() -> {
                cinema.book(seat);
            });

            customers[i].start();
        }

        try {

            for (Thread t : customers) {
                t.join();
            }

        } catch (InterruptedException e) {

            System.out.println("Booking interrupted");
            Thread.currentThread().interrupt();
            return;
        }

        System.out.println("Total customers: 100");
        System.out.println("Available seats: 10");
        System.out.println("Seats sold: " +
                           cinema.getTotalSeatsSold());
    }
}
