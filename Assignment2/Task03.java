
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

class Student {

    private final String regNo;
    private final String name;

    Student(String regNo, String name) {
        this.regNo = regNo;
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o)
            return true;

        if (!(o instanceof Student))
            return false;

        Student s = (Student) o;
        return regNo.equals(s.regNo);
    }

    // Correct hashCode implementation
    @Override
    public int hashCode() {
        return Objects.hash(regNo);
    }

    @Override
    public String toString() {
        return regNo + " " + name;
    }
}

public class Task03 {

    public static void main(String[] args) {

        Set<Student> present = new HashSet<>();

        // Test 1
        present.add(new Student("192311001", "Arun"));
        present.add(new Student("192311002", "Divya"));

        System.out.println("Test 1: " + present.size());

        // Test 2
        present.add(new Student("192311001", "Arun"));

        System.out.println("Test 2: " + present.size());

        // Test 3
        present.add(new Student("192311001", "Kumar"));

        System.out.println("Test 3: " + present.size());

        System.out.println("Unique Students:");

        for (Student s : present) {
            System.out.println(s);
        }
    }
}
