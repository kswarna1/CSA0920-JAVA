
import java.io.IOException;

class ExamException extends Exception {
    ExamException(String message) {
        super(message);
    }
}

class SubmissionStorageException extends ExamException {
    SubmissionStorageException(String message, Throwable cause) {
        super(message);
        initCause(cause);
    }
}

class Storage {

    void save(String fileName, int fileSize)
            throws SubmissionStorageException {

        try {
            if (fileSize > 500) {
                throw new IOException("Disk quota exceeded");
            }

            System.out.println("Saved " + fileName);

        } catch (IOException e) {
            throw new SubmissionStorageException(
                "Could not store " + fileName, e);
        }
    }
}

public class Task02 {

    public static void main(String[] args) {

        Storage storage = new Storage();

        try {
            storage.save("answer01.pdf", 120);
            storage.save("answer02.pdf", 800);

        } catch (ExamException e) {

            System.out.println("Error: " + e.getMessage());

            if (e.getCause() != null) {
                System.out.println("Cause: "
                                   + e.getCause().getMessage());
            }
        }
    }
}
