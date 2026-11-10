
import java.io.*;

public class Task03 {

    public static void main(String[] args) {

        String[] files = {
            "store1.txt",
            "store2.txt",
            "store3.txt"
        };

        int totalSales = 0;

        try {

            for (String fileName : files) {

                try (BufferedReader br =
                     new BufferedReader(new FileReader(fileName))) {

                    String data = br.readLine();
                    int sales = Integer.parseInt(data);

                    totalSales += sales;

                    System.out.println(fileName +
                                       " Sales: " + sales);
                }
            }

            try (BufferedWriter bw =
                 new BufferedWriter(
                     new FileWriter("summary.txt"))) {

                bw.write("Total Sales: " + totalSales);
            }

            System.out.println("Summary report generated");
            System.out.println("Total Sales: " + totalSales);

        } catch (IOException e) {

            System.out.println("File error: " + e.getMessage());

        } catch (NumberFormatException e) {

            System.out.println("Invalid sales data");

        } finally {

            System.out.println("Resource management completed");
        }
    }
}
