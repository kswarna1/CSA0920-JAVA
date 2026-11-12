
import java.io.*;
import java.sql.*;

public class SQLInjectionTest {

    static final String URL =
        "jdbc:mysql://localhost:3306/assignment5";

    static final String USER = "root";
    static final String PASSWORD = "your_password";

    // Unsafe login
    static boolean unsafeLogin(
            Connection con, String id, String password)
            throws SQLException {

        String sql = "SELECT * FROM users WHERE id = '"
            + id + "' AND pwd = '" + password + "'";

        try (Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            return rs.next();
        }
    }

    // Safe login
    static boolean safeLogin(
            Connection con, String id, String password)
            throws SQLException {

        String sql =
            "SELECT * FROM users WHERE id = ? AND pwd = ?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, id);
            ps.setString(2, password);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    // Check damaged pass records
    static void checkPassFile() throws IOException {

        try (BufferedReader br =
                 new BufferedReader(new FileReader("passes.txt"))) {

            String line;

            while ((line = br.readLine()) != null) {

                String[] data = line.split(",", -1);

                try {
                    if (data.length != 5) {
                        throw new Exception("Wrong field count");
                    }

                    if (data[0].isEmpty()
                            || data[1].isEmpty()
                            || data[2].isEmpty()) {
                        throw new Exception("Empty field");
                    }

                    Integer.parseInt(data[3]);
                    double amount = Double.parseDouble(data[4]);

                    if (amount < 0) {
                        throw new Exception("Negative amount");
                    }

                    System.out.println("Valid: " + line);

                } catch (Exception e) {
                    System.out.println(
                        "Damaged record skipped: " + line);
                }
            }
        }
    }

    public static void main(String[] args) {

        System.out.println("Damaged File Testing:");

        try {
            checkPassFile();
        } catch (IOException e) {
            System.out.println("File error: " + e.getMessage());
        }

        System.out.println("\nSQL Injection Testing:");

        try (Connection con =
                 DriverManager.getConnection(URL, USER, PASSWORD)) {

            String attackId = "admin' -- ";
            String wrongPassword = "wrongpassword";

            System.out.println("Unsafe login result: "
                + unsafeLogin(con, attackId, wrongPassword));

            System.out.println("Safe login result: "
                + safeLogin(con, attackId, wrongPassword));

        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }
}
