
import java.util.*;

public class Task04 {

    public static void main(String[] args) {

        HashMap<String, Integer> hash =
            new HashMap<>();

        TreeMap<String, Integer> tree =
            new TreeMap<>();

        LinkedHashMap<String, Integer> linked =
            new LinkedHashMap<>();

        String[] codes = {
            "M103", "M101", "M105", "M102", "M104"
        };

        int[] stocks = {50, 100, 30, 80, 60};

        for (int i = 0; i < codes.length; i++) {

            hash.put(codes[i], stocks[i]);
            tree.put(codes[i], stocks[i]);
            linked.put(codes[i], stocks[i]);
        }

        // HashMap lookup
        System.out.println("Stock Lookup:");

        System.out.println("M101: " + hash.get("M101"));

        // Code order report
        System.out.println("\nTreeMap - Code Order:");

        for (Map.Entry<String, Integer> e : tree.entrySet()) {
            System.out.println(e.getKey() + " = " + e.getValue());
        }

        // Insertion order report
        System.out.println("\nLinkedHashMap - Insertion Order:");

        for (Map.Entry<String, Integer> e : linked.entrySet()) {
            System.out.println(e.getKey() + " = " + e.getValue());
        }

        // Performance test
        int n = 100000;

        for (int i = 0; i < n; i++) {
            hash.put("X" + i, i);
            tree.put("X" + i, i);
            linked.put("X" + i, i);
        }

        long start, end;

        start = System.nanoTime();

        for (int i = 0; i < n; i++) {
            hash.get("X" + i);
        }

        end = System.nanoTime();

        System.out.println("\nHashMap lookup: "
            + (end - start) + " ns");

        start = System.nanoTime();

        for (int i = 0; i < n; i++) {
            tree.get("X" + i);
        }

        end = System.nanoTime();

        System.out.println("TreeMap lookup: "
            + (end - start) + " ns");

        start = System.nanoTime();

        for (int i = 0; i < n; i++) {
            linked.get("X" + i);
        }

        end = System.nanoTime();

        System.out.println("LinkedHashMap lookup: "
            + (end - start) + " ns");
    }
}
