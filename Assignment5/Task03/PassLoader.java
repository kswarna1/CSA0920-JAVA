
import java.sql.*;
import java.util.*;

public class PassLoader {

    static final String URL =
        "jdbc:mysql://localhost:3306/assignment5";
    static final String USER = "root";
    static final String PASSWORD = "your_password";

    static class Pass {
        String id, regNo, name;
        int route;
        double amount;

        Pass(String id, String regNo, String name,
             int route, double amount) {
            this.id = id;
            this.regNo = regNo;
            this.name = name;
            this.route = route;
            this.amount = amount;
        }
    }

    static List<Pass> generate(int n) {
        List<Pass> list = new ArrayList<>();

        for (int i = 1; i <= n; i++) {
            list.add(new Pass(
                "P" + i,
                "R" + i,
                "Student" + i,
                (i % 10) + 1,
                2500.0
            ));
        }

        return list;
    }

    static void clearTable(Connection con) throws SQLException {
        try (Statement st = con.createStatement()) {
            st.executeUpdate("DELETE FROM bus_pass");
        }
    }

    // Statement insertion
    static void insertStatement(Connection con, List<Pass> list)
            throws SQLException {

        try (Statement st = con.createStatement()) {
            for (Pass p : list) {
                String sql = "INSERT INTO bus_pass VALUES ('"
                    + p.id + "','" + p.regNo + "','" + p.name
                    + "'," + p.route + "," + p.amount + ")";

                st.executeUpdate(sql);
            }
        }
    }

    // PreparedStatement insertion
    static void insertPrepared(Connection con, List<Pass> list)
            throws SQLException {

        String sql = "INSERT INTO bus_pass VALUES (?,?,?,?,?)";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            for (Pass p : list) {
                ps.setString(1, p.id);
                ps.setString(2, p.regNo);
                ps.setString(3, p.name);
                ps.setInt(4, p.route);
                ps.setDouble(5, p.amount);

                ps.executeUpdate();
            }
        }
    }

    // Batch insertion, executing every 1000 records
    static int loadBatch(Connection con, List<Pass> list)
            throws SQLException {

        String sql = "INSERT INTO bus_pass VALUES (?,?,?,?,?)";

        boolean oldAutoCommit = con.getAutoCommit();
        int inserted = 0;

        try {
            con.setAutoCommit(false);

            try (PreparedStatement ps = con.prepareStatement(sql)) {

                int count = 0;

                for (Pass p : list) {
                    ps.setString(1, p.id);
                    ps.setString(2, p.regNo);
                    ps.setString(3, p.name);
                    ps.setInt(4, p.route);
                    ps.setDouble(5, p.amount);

                    ps.addBatch();
                    count++;

                    if (count % 1000 == 0) {
                        ps.executeBatch();
                        ps.clearBatch();
                    }
                }

                if (count % 1000 != 0) {
                    ps.executeBatch();
                }
            }

            con.commit();
            inserted = list.size();

        } catch (SQLException e) {
            con.rollback();
            throw e;

        } finally {
            con.setAutoCommit(oldAutoCommit);
        }

        return inserted;
    }

    static long measureStatement(Connection con, List<Pass> list)
            throws SQLException {

        clearTable(con);
        long start = System.nanoTime();

        insertStatement(con, list);

        return (System.nanoTime() - start) / 1_000_000;
    }

    static long measurePrepared(Connection con, List<Pass> list)
            throws SQLException {

        clearTable(con);
        long start = System.nanoTime();

        insertPrepared(con, list);

        return (System.nanoTime() - start) / 1_000_000;
    }

    static long measureBatch(Connection con, List<Pass> list)
            throws SQLException {

        clearTable(con);
        long start = System.nanoTime();

        loadBatch(con, list);

        return (System.nanoTime() - start) / 1_000_000;
    }

    // Verify records and display column information
    static void verify(Connection con) throws SQLException {

        String sql = "SELECT * FROM bus_pass";

        try (Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            ResultSetMetaData md = rs.getMetaData();

            System.out.println("\nTable column information:");

            for (int i = 1; i <= md.getColumnCount(); i++) {
                System.out.println(
                    md.getColumnName(i) + " : "
                    + md.getColumnTypeName(i)
                );
            }

            int count = 0;

            while (rs.next()) {
                count++;
            }

            System.out.println("Total records: " + count);
        }

        DatabaseMetaData db = con.getMetaData();

        System.out.println("Database: "
            + db.getDatabaseProductName());

        System.out.println("Database version: "
            + db.getDatabaseProductVersion());

        System.out.println("Driver: " + db.getDriverName());

        System.out.println("Driver version: "
            + db.getDriverVersion());
    }

    // Test rollback with an invalid amount
    static void testRollback(Connection con) throws SQLException {

        clearTable(con);

        List<Pass> test = new ArrayList<>();

        test.add(new Pass("P1", "R1", "Arun", 5, 2500));
        test.add(new Pass("P2", "R2", "Meena", 7, 3000));
        test.add(new Pass("P3", "R3", "Divya", 8, -3500));

        System.out.println("\nRollback experiment:");

        try {
            loadBatch(con, test);
            System.out.println("Insertion successful");

        } catch (SQLException e) {
            System.out.println("Insertion failed: "
                + e.getMessage());

            System.out.println("Transaction rolled back");
        }

        try (Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(
                 "SELECT COUNT(*) FROM bus_pass")) {

            if (rs.next()) {
                System.out.println(
                    "Records after rollback: " + rs.getInt(1)
                );
            }
        }
    }

    public static void main(String[] args) {

        try (Connection con =
                 DriverManager.getConnection(URL, USER, PASSWORD)) {

            System.out.println("MySQL connection successful");

            int[] sizes = {1000, 10000, 100000};

            for (int n : sizes) {
                List<Pass> list = generate(n);

                System.out.println("\nRecords: " + n);

                System.out.println("Statement: "
                    + measureStatement(con, list) + " ms");

                System.out.println("PreparedStatement: "
                    + measurePrepared(con, list) + " ms");

                System.out.println("Batch: "
                    + measureBatch(con, list) + " ms");
            }

            verify(con);
            testRollback(con);

        } catch (SQLException e) {
            System.out.println("Database error: "
                + e.getMessage());
        }
    }
}
