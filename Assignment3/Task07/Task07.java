
import java.util.*;
import java.util.concurrent.*;
import java.io.*;

public class Task07 {

    static class AnswerTask implements Callable<Integer> {

        private int fileNumber;

        AnswerTask(int fileNumber) {
            this.fileNumber = fileNumber;
        }

        public Integer call() throws Exception {

            if (fileNumber % 100 == 0) {
                throw new FileNotFoundException(
                    "Answer file missing");
            }

            if (fileNumber % 100 == 1) {
                throw new IOException(
                    "Answer file corrupted");
            }

            return 1;
        }
    }

    static void processFiles(int poolSize)
            throws InterruptedException {

        ExecutorService executor =
            Executors.newFixedThreadPool(poolSize);

        ConcurrentLinkedQueue<Integer> successful =
            new ConcurrentLinkedQueue<>();

        ConcurrentLinkedQueue<Integer> missing =
            new ConcurrentLinkedQueue<>();

        ConcurrentLinkedQueue<Integer> corrupted =
            new ConcurrentLinkedQueue<>();

        List<Future<Integer>> results = new ArrayList<>();

        long startTime = System.currentTimeMillis();

        try {

            for (int i = 1; i <= 10000; i++) {
                results.add(executor.submit(
                    new AnswerTask(i)));
            }

            for (Future<Integer> future : results) {

                try {
                    future.get();
                    successful.add(1);

                } catch (ExecutionException e) {

                    Throwable cause = e.getCause();

                    if (cause instanceof FileNotFoundException) {
                        missing.add(1);
                    } else {
                        corrupted.add(1);
                    }
                }
            }

        } finally {
            executor.shutdown();
        }

        long endTime = System.currentTimeMillis();

        System.out.println("Thread Pool Size: " + poolSize);
        System.out.println("Successful Files: " +
                           successful.size());
        System.out.println("Missing Files: " +
                           missing.size());
        System.out.println("Corrupted Files: " +
                           corrupted.size());
        System.out.println("Total Processed: " +
            (successful.size() + missing.size()
             + corrupted.size()));
        System.out.println("Time Taken: " +
                           (endTime - startTime) + " ms");
        System.out.println("--------------------------------");
    }

    public static void main(String[] args) {

        int[] poolSizes = {1, 2, 4, 8};

        try {

            for (int size : poolSizes) {
                processFiles(size);
            }

            System.out.println("All processing completed");

        } catch (InterruptedException e) {
            System.out.println("Processing interrupted");
            Thread.currentThread().interrupt();
        }
    }
}
