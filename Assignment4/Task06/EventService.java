
import java.sql.*;
import java.util.ArrayList;

public class EventService {

    private final String url =
        "jdbc:mysql://localhost:3306/campus_events";
    private final String user = "root";
    private final String password = "YOUR_MYSQL_PASSWORD";

    private Connection connect() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }

    public void register(String id, String name,
                         String department, String event)
                         throws SQLException {

        String sql = "INSERT INTO registrations " +
            "(student_id, student_name, department, event_name) " +
            "VALUES (?, ?, ?, ?)";

        try (Connection con = connect();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, id);
            ps.setString(2, name);
            ps.setString(3, department);
            ps.setString(4, event);

            ps.executeUpdate();
        }
    }

    public ArrayList<String[]> getRegistrations(String event)
            throws SQLException {

        ArrayList<String[]> records = new ArrayList<>();

        String sql = "SELECT student_id, student_name, " +
            "department, event_name, registration_date " +
            "FROM registrations";

        if (!event.equals("All Events")) {
            sql += " WHERE event_name = ?";
        }

        sql += " ORDER BY registration_date DESC";

        try (Connection con = connect();
             PreparedStatement ps = con.prepareStatement(sql)) {

            if (!event.equals("All Events")) {
                ps.setString(1, event);
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    records.add(new String[]{
                        rs.getString("student_id"),
                        rs.getString("student_name"),
                        rs.getString("department"),
                        rs.getString("event_name"),
                        rs.getString("registration_date")
                    });
                }
            }
        }

        return records;
    }
}
