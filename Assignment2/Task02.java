
import java.util.*;

public class Task02 {

    static void test(int n) {

        ArrayList<Integer> a = new ArrayList<>();
        LinkedList<Integer> l = new LinkedList<>();

        long start, end;

        // Add elements
        start = System.nanoTime();
        for (int i = 0; i < n; i++)
            a.add(i);
        end = System.nanoTime();
        System.out.println("ArrayList add: " + (end-start) + " ns");

        start = System.nanoTime();
        for (int i = 0; i < n; i++)
            l.add(i);
        end = System.nanoTime();
        System.out.println("LinkedList add: " + (end-start) + " ns");

        // Insert in middle
        start = System.nanoTime();
        a.add(n / 2, -1);
        end = System.nanoTime();
        System.out.println("ArrayList middle insert: " + (end-start) + " ns");

        start = System.nanoTime();
        l.add(n / 2, -1);
        end = System.nanoTime();
        System.out.println("LinkedList middle insert: " + (end-start) + " ns");

        // Random access
        start = System.nanoTime();
        int x = a.get(n / 2);
        end = System.nanoTime();
        System.out.println("ArrayList access: " + (end-start) + " ns");

        start = System.nanoTime();
        x = l.get(n / 2);
        end = System.nanoTime();
        System.out.println("LinkedList access: " + (end-start) + " ns");

        // Iteration
        start = System.nanoTime();
        for (int value : a) x += value;
        end = System.nanoTime();
        System.out.println("ArrayList iteration: " + (end-start) + " ns");

        start = System.nanoTime();
        for (int value : l) x += value;
        end = System.nanoTime();
        System.out.println("LinkedList iteration: " + (end-start) + " ns");

        // Remove front
        start = System.nanoTime();
        a.remove(0);
        end = System.nanoTime();
        System.out.println("ArrayList front remove: " + (end-start) + " ns");

        start = System.nanoTime();
        l.remove(0);
        end = System.nanoTime();
        System.out.println("LinkedList front remove: " + (end-start) + " ns");
    }

    public static void main(String[] args) {

        int[] sizes = {1000, 10000, 50000};

        for (int n : sizes) {
            System.out.println("\nPlaylist size: " + n);
            test(n);
        }

        // Up Next queue using ArrayDeque
        ArrayDeque<String> queue = new ArrayDeque<>();

        queue.addLast("Song A");
        queue.addLast("Song B");
        queue.addFirst("Song C");

        System.out.println("\nUp Next Queue: " + queue);
        System.out.println("Playing: " + queue.removeFirst());
    }
}
