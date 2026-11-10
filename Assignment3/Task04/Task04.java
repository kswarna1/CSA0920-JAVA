
class ScannerStation implements Runnable {

    private String stationName;

    ScannerStation(String name) {
        stationName = name;
    }

    public void run() {

        for (int i = 1; i <= 3; i++) {

            System.out.println(stationName +
                               " scanned script " + i);

            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                System.out.println("Thread interrupted");
                Thread.currentThread().interrupt();
                return;
            }
        }
    }
}

public class Task04 {

    public static void main(String[] args) {

        ScannerStation s1 = new ScannerStation("Station 1");
        ScannerStation s2 = new ScannerStation("Station 2");
        ScannerStation s3 = new ScannerStation("Station 3");
        ScannerStation s4 = new ScannerStation("Station 4");

        Thread t1 = new Thread(s1);
        Thread t2 = new Thread(s2);
        Thread t3 = new Thread(s3);
        Thread t4 = new Thread(s4);

        t1.start();
        t2.start();
        t3.start();
        t4.start();

        System.out.println("Station 1 is alive: " +
                           t1.isAlive());

        try {
            t1.join();
            t2.join();
            t3.join();
            t4.join();
        } catch (InterruptedException e) {
            System.out.println("Main thread interrupted");
            Thread.currentThread().interrupt();
            return;
        }

        System.out.println("All scanning completed");
        System.out.println("Total scripts scanned: 12");
    }
}
