
public class Task01 {

    static String[] fields(int n) {
        String[] data = new String[n];

        for (int i = 0; i < n; i++) {
            data[i] = "Field" + i + "=Railway;";
        }

        return data;
    }

    static long usingString(int tickets, int n) {
        String[] data = fields(n);
        long total = 0;

        for (int t = 0; t < tickets; t++) {
            String ticket = "";

            for (int i = 0; i < n; i++) {
                ticket = ticket + data[i];
            }

            total += ticket.length();
        }
        return total;
    }

    static long usingBuilder(int tickets, int n) {
        String[] data = fields(n);
        long total = 0;

        for (int t = 0; t < tickets; t++) {
            StringBuilder ticket = new StringBuilder();

            for (int i = 0; i < n; i++) {
                ticket.append(data[i]);
            }

            total += ticket.length();
        }
        return total;
    }

    static long usingBuffer(int tickets, int n) {
        String[] data = fields(n);
        long total = 0;

        for (int t = 0; t < tickets; t++) {
            StringBuffer ticket = new StringBuffer();

            for (int i = 0; i < n; i++) {
                ticket.append(data[i]);
            }

            total += ticket.length();
        }
        return total;
    }

    static void test(String name, int tickets, int n,
                     java.util.function.LongSupplier task) {

        long totalTime = 0;
        long result = 0;

        task.getAsLong(); // Warm-up

        for (int i = 0; i < 3; i++) {
            long start = System.nanoTime();
            result = task.getAsLong();
            long end = System.nanoTime();

            totalTime += end - start;
        }

        System.out.println(name);
        System.out.println("Average time: "
            + totalTime / 3 / 1000000.0 + " ms");
        System.out.println("Characters: " + result);
    }

    public static void main(String[] args) {

        System.out.println("WORKLOAD 1");
        test("String", 1, 6, () -> usingString(1, 6));
        test("StringBuilder", 1, 6, () -> usingBuilder(1, 6));
        test("StringBuffer", 1, 6, () -> usingBuffer(1, 6));

        System.out.println("\nWORKLOAD 2");
        test("String", 10000, 50,
            () -> usingString(10000, 50));
        test("StringBuilder", 10000, 50,
            () -> usingBuilder(10000, 50));
        test("StringBuffer", 10000, 50,
            () -> usingBuffer(10000, 50));

        System.out.println("\nWORKLOAD 3");
        test("String", 2000, 100,
            () -> usingString(2000, 100));
        test("StringBuilder", 2000, 100,
            () -> usingBuilder(2000, 100));
        test("StringBuffer", 2000, 100,
            () -> usingBuffer(2000, 100));
    }
}
