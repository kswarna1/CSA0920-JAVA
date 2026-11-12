
import java.io.*;
import java.nio.file.*;
import java.util.*;

public class PassStore {

    static final String FILE = "passes.txt";

    static boolean valid(String[] data) {
        if (data.length != 5) return false;

        try {
            Integer.parseInt(data[3]);
            Double.parseDouble(data[4]);

            return !data[0].isEmpty()
                && !data[1].isEmpty()
                && !data[2].isEmpty();
        } catch (NumberFormatException e) {
            return false;
        }
    }

    // Add a pass
    static void addPass(String record) throws IOException {
        String[] data = record.split(",", -1);

        if (!valid(data)) {
            System.out.println("Invalid record");
            return;
        }

        try (BufferedWriter bw =
                 new BufferedWriter(new FileWriter(FILE, true))) {
            bw.write(record);
            bw.newLine();
        }

        System.out.println("Pass added successfully");
    }

    // Find pass by ID
    static String[] findById(String id) throws IOException {
        try (BufferedReader br =
                 new BufferedReader(new FileReader(FILE))) {

            String line;

            while ((line = br.readLine()) != null) {
                String[] data = line.split(",", -1);

                if (!valid(data)) continue;

                if (data[0].equals(id)) {
                    return data;
                }
            }
        }

        return null;
    }

    // List passes by route
    static void listByRoute(int route) throws IOException {
        try (BufferedReader br =
                 new BufferedReader(new FileReader(FILE))) {

            String line;

            while ((line = br.readLine()) != null) {
                String[] data = line.split(",", -1);

                if (!valid(data)) continue;

                if (Integer.parseInt(data[3]) == route) {
                    System.out.println(Arrays.toString(data));
                }
            }
        }
    }

    // Count lines using buffered reading
    static long countBuffered() throws IOException {
        long count = 0;

        try (BufferedReader br =
                 new BufferedReader(new FileReader(FILE))) {

            while (br.readLine() != null) {
                count++;
            }
        }

        return count;
    }

    // Count lines using byte-by-byte reading
    static long countUnbuffered() throws IOException {
        long count = 0;
        int ch;
        int last = -1;

        try (FileInputStream fis = new FileInputStream(FILE)) {
            while ((ch = fis.read()) != -1) {
                if (ch == '\n') count++;
                last = ch;
            }
        }

        if (last != -1 && last != '\n') count++;

        return count;
    }

    // Generate a test file for performance measurements
    static void generateFile(int records) throws IOException {
        try (BufferedWriter bw =
                 new BufferedWriter(new FileWriter(FILE))) {

            for (int i = 1; i <= records; i++) {
                bw.write("P" + i + ",R" + i + ",Student" + i
                    + "," + ((i % 10) + 1) + ",2500.0");
                bw.newLine();
            }
        }
    }

    static void measureReading() throws IOException {
        long start = System.nanoTime();
        long bufferedCount = countBuffered();
        long bufferedTime = System.nanoTime() - start;

        start = System.nanoTime();
        long unbufferedCount = countUnbuffered();
        long unbufferedTime = System.nanoTime() - start;

        System.out.println("Buffered lines: " + bufferedCount);
        System.out.println("Buffered time: "
            + bufferedTime / 1_000_000.0 + " ms");

        System.out.println("Unbuffered lines: " + unbufferedCount);
        System.out.println("Unbuffered time: "
            + unbufferedTime / 1_000_000.0 + " ms");
    }

    public static void main(String[] args) throws IOException {

        if (!Files.exists(Paths.get(FILE))) {
            Files.write(Paths.get(FILE), Arrays.asList(
                "P1001,192311001,Arun,5,2500.0",
                "P1002,192311002,Meena,7",
                "P1003,192311003,Divya,7,3800.0"
            ));
        }

        System.out.println("Find P1003:");
        System.out.println(Arrays.toString(findById("P1003")));

        System.out.println("\nFind missing ID:");
        System.out.println(Arrays.toString(findById("P9999")));

        System.out.println("\nRoute 7 passes:");
        listByRoute(7);

        System.out.println("\nLine count comparison:");
        measureReading();

        System.out.println("\nAdding new pass:");
        addPass("P1004,192311004,Ravi,5,2800.0");
    }
}
