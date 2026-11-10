
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;

public class CampusEventApp extends JFrame {

    JTextField idField, nameField, departmentField;
    JComboBox<String> eventBox, filterBox;
    JTable table;
    DefaultTableModel model;

    JButton registerButton, clearButton, viewButton, exportButton;
    EventService service = new EventService();

    String[] events = {
        "Cultural Fest",
        "Music Competition",
        "Dance Competition",
        "Technical Symposium"
    };

    public CampusEventApp() {

        setTitle("Campus Event Registration");
        setLayout(new BorderLayout(10, 10));

        // Registration panel
        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));

        idField = new JTextField();
        nameField = new JTextField();
        departmentField = new JTextField();

        eventBox = new JComboBox<>(events);

        registerButton = new JButton("Register");
        clearButton = new JButton("Clear");

        panel.add(new JLabel("Student ID:"));
        panel.add(idField);

        panel.add(new JLabel("Student Name:"));
        panel.add(nameField);

        panel.add(new JLabel("Department:"));
        panel.add(departmentField);

        panel.add(new JLabel("Select Event:"));
        panel.add(eventBox);

        panel.add(registerButton);
        panel.add(clearButton);

        add(panel, BorderLayout.NORTH);

        // Committee table
        model = new DefaultTableModel(
            new String[]{"Student ID", "Name", "Department",
                         "Event", "Registration Date"}, 0
        );

        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // Filter and export panel
        JPanel bottom = new JPanel();

        filterBox = new JComboBox<>(new String[]{
            "All Events", "Cultural Fest",
            "Music Competition", "Dance Competition",
            "Technical Symposium"
        });

        viewButton = new JButton("View Registrations");
        exportButton = new JButton("Export CSV");

        bottom.add(new JLabel("Filter Event:"));
        bottom.add(filterBox);
        bottom.add(viewButton);
        bottom.add(exportButton);

        add(bottom, BorderLayout.SOUTH);

        // Menu bar
        JMenuBar bar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");
        JMenu studentMenu = new JMenu("Student");
        JMenu committeeMenu = new JMenu("Committee");
        JMenu helpMenu = new JMenu("Help");

        JMenuItem exit = new JMenuItem("Exit");
        JMenuItem clear = new JMenuItem("Clear");
        JMenuItem view = new JMenuItem("View Registrations");
        JMenuItem about = new JMenuItem("About");

        fileMenu.add(exit);
        studentMenu.add(clear);
        committeeMenu.add(view);
        helpMenu.add(about);

        bar.add(fileMenu);
        bar.add(studentMenu);
        bar.add(committeeMenu);
        bar.add(helpMenu);

        setJMenuBar(bar);

        // Event handling
        registerButton.addActionListener(e -> registerStudent());
        clearButton.addActionListener(e -> clearFields());
        clear.addActionListener(e -> clearFields());
        viewButton.addActionListener(e -> loadRecords());
        view.addActionListener(e -> loadRecords());
        filterBox.addActionListener(e -> loadRecords());
        exportButton.addActionListener(e -> exportCSV());

        exit.addActionListener(e -> confirmExit());

        about.addActionListener(e ->
            JOptionPane.showMessageDialog(
                this, "Campus Event Registration System"
            )
        );

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                confirmExit();
            }
        });

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setSize(850, 500);
        setLocationRelativeTo(null);

        loadRecords();
    }

    void registerStudent() {

        String id = idField.getText().trim();
        String name = nameField.getText().trim();
        String department = departmentField.getText().trim();
        String event = (String) eventBox.getSelectedItem();

        if (id.isEmpty() || name.isEmpty()
                || department.isEmpty()) {
            JOptionPane.showMessageDialog(
                this, "Please fill all fields"
            );
            return;
        }

        registerButton.setEnabled(false);

        SwingWorker<Void, Void> worker =
            new SwingWorker<Void, Void>() {

            protected Void doInBackground() throws Exception {
                service.register(id, name, department, event);
                return null;
            }

            protected void done() {
                registerButton.setEnabled(true);

                try {
                    get();

                    JOptionPane.showMessageDialog(
                        CampusEventApp.this,
                        "Registration Successful!"
                    );

                    clearFields();
                    loadRecords();

                } catch (Exception ex) {
                    Throwable cause = ex.getCause();

                    if (cause instanceof java.sql.SQLException
                            && ((java.sql.SQLException) cause)
                               .getSQLState().startsWith("23")) {
                        JOptionPane.showMessageDialog(
                            CampusEventApp.this,
                            "Duplicate registration not allowed"
                        );
                    } else {
                        JOptionPane.showMessageDialog(
                            CampusEventApp.this,
                            "Database error: " + ex.getMessage()
                        );
                    }
                }
            }
        };

        worker.execute();
    }

    void loadRecords() {

        String selected =
            (String) filterBox.getSelectedItem();

        viewButton.setEnabled(false);

        SwingWorker<ArrayList<String[]>, Void> worker =
            new SwingWorker<ArrayList<String[]>, Void>() {

            protected ArrayList<String[]> doInBackground()
                    throws Exception {
                return service.getRegistrations(selected);
            }

            protected void done() {
                viewButton.setEnabled(true);

                try {
                    model.setRowCount(0);

                    for (String[] row : get()) {
                        model.addRow(row);
                    }

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(
                        CampusEventApp.this,
                        "Unable to load registrations: "
                        + ex.getMessage()
                    );
                }
            }
        };

        worker.execute();
    }

    void clearFields() {
        idField.setText("");
        nameField.setText("");
        departmentField.setText("");
        eventBox.setSelectedIndex(0);
    }

    void exportCSV() {

        JFileChooser chooser = new JFileChooser();
        chooser.setSelectedFile(
            new File("CampusRegistrations.csv")
        );

        if (chooser.showSaveDialog(this)
                != JFileChooser.APPROVE_OPTION) {
            return;
        }

        File file = chooser.getSelectedFile();

        try (PrintWriter out = new PrintWriter(file)) {

            out.println(
                "Student ID,Name,Department,Event,Registration Date"
            );

            for (int i = 0; i < model.getRowCount(); i++) {
                for (int j = 0; j < model.getColumnCount(); j++) {

                    String value =
                        String.valueOf(model.getValueAt(i, j));

                    value = "\"" +
                        value.replace("\"", "\"\"") + "\"";

                    out.print(value);

                    if (j < model.getColumnCount() - 1) {
                        out.print(",");
                    }
                }

                out.println();
            }

            JOptionPane.showMessageDialog(
                this, "CSV exported successfully!"
            );

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                this, "Export failed: " + ex.getMessage()
            );
        }
    }

    void confirmExit() {
        int choice = JOptionPane.showConfirmDialog(
            this,
            "Do you want to exit?",
            "Confirm Exit",
            JOptionPane.YES_NO_OPTION
        );

        if (choice == JOptionPane.YES_OPTION) {
            dispose();
        }
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() ->
            new CampusEventApp().setVisible(true)
        );
    }
}
