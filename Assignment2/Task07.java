
import java.util.*;

class GapBuffer {

    String[] data;
    int gapStart;
    int gapEnd;

    GapBuffer(int n) {

        data = new String[2 * n + 100];

        for (int i = 0; i < n; i++) {
            data[i] = "Song" + i;
        }

        gapStart = n;
        gapEnd = data.length;
    }

    void moveGap(int index) {

        while (gapStart > index) {
            data[--gapEnd] = data[--gapStart];
        }

        while (gapStart < index) {
            data[gapStart++] = data[gapEnd++];
        }
    }

    void insert(int index, String song) {

        moveGap(index);

        if (gapStart == gapEnd) {
            throw new IllegalStateException("Gap is full");
        }

        data[gapStart++] = song;
    }

    String get(int index) {

        if (index < gapStart) {
            return data[index];
        }

        return data[index + (gapEnd - gapStart)];
    }

    int size() {
        return data.length - (gapEnd - gapStart);
    }
}

public class Task07 {

    public static void main(String[] args) {

        int n = 10000;
        int insertions = 5000;
        int middle = n / 2;

        ArrayList<String> list = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            list.add("Song" + i);
        }

        long start = System.nanoTime();

        for (int i = 0; i < insertions; i++) {
            list.add(middle, "NewSong" + i);
        }

        long arrayTime = System.nanoTime() - start;

        GapBuffer gap = new GapBuffer(n);

        start = System.nanoTime();

        for (int i = 0; i < insertions; i++) {
            gap.insert(middle, "NewSong" + i);
        }

        long gapTime = System.nanoTime() - start;

        System.out.println("Original ArrayList:");
        System.out.println("Total songs: " + list.size());
        System.out.println("Insertion time: " + arrayTime + " ns");

        System.out.println("\nRedesigned Gap Buffer:");
        System.out.println("Total songs: " + gap.size());
        System.out.println("Insertion time: " + gapTime + " ns");

        System.out.println("\nChecking correctness:");
        System.out.println("ArrayList: " + list.get(middle));
        System.out.println("Gap Buffer: " + gap.get(middle));
    }
}
