
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Task02 extends JFrame {

    JTextField patientId;
    JComboBox<String> department, doctor, timeSlot;
    JButton book;
    JLabel status;
    boolean bookingInProgress = false;
    SwingWorker<String, Void> worker;

    public Task02() {
        setTitle("Hospital Appointment Desk");
        setLayout(new GridLayout(6, 2, 10, 10));

        patientId = new JTextField();

        department = new JComboBox<>(new String[]{
            "Cardiology", "Neurology", "Orthopedics"
        });

        doctor = new JComboBox<>();
        timeSlot = new JComboBox<>(new String[]{
            "10:00 AM", "11:00 AM", "02:00 PM", "04:00 PM"
        });

        book = new JButton("Book Appointment");
        status = new JLabel("Enter patient details");

        add(new JLabel("Patient ID:"));
        add(patientId);

        add(new JLabel("Department:"));
        add(department);

        add(new JLabel("Doctor:"));
        add(doctor);

        add(new JLabel("Time Slot:"));
        add(timeSlot);

        add(book);
        add(status);

        department.addActionListener(e -> updateDoctors());

        book.addActionListener(e -> bookAppointment());

        updateDoctors();

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                if (bookingInProgress) {
                    int choice = JOptionPane.showConfirmDialog(
                        Task02.this,
                        "Booking is in progress. Do you want to exit?",
                        "Confirm Exit",
                        JOptionPane.YES_NO_OPTION
                    );

                    if (choice == JOptionPane.YES_OPTION) {
                        worker.cancel(true);
                        dispose();
                    }
                } else {
                    int choice = JOptionPane.showConfirmDialog(
                        Task02.this,
                        "Are you sure you want to close?",
                        "Confirm Exit",
                        JOptionPane.YES_NO_OPTION
                    );

                    if (choice == JOptionPane.YES_OPTION) {
                        dispose();
                    }
                }
            }
        });

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setSize(450, 300);
        setLocationRelativeTo(null);
    }

    void updateDoctors() {
        doctor.removeAllItems();

        String selected = (String) department.getSelectedItem();

        if (selected.equals("Cardiology")) {
            doctor.addItem("Dr. Ravi");
            doctor.addItem("Dr. Kumar");
        } else if (selected.equals("Neurology")) {
            doctor.addItem("Dr. Priya");
            doctor.addItem("Dr. Anil");
        } else {
            doctor.addItem("Dr. Suresh");
            doctor.addItem("Dr. Meena");
        }
    }

    void bookAppointment() {
        String id = patientId.getText().trim();

        if (id.isEmpty()) {
            JOptionPane.showMessageDialog(
                this, "Please enter Patient ID"
            );
            return;
        }

        if (bookingInProgress) {
            return;
        }

        String dept = (String) department.getSelectedItem();
        String doc = (String) doctor.getSelectedItem();
        String time = (String) timeSlot.getSelectedItem();

        bookingInProgress = true;
        book.setEnabled(false);
        status.setText("Booking in progress...");

        worker = new SwingWorker<String, Void>() {
            protected String doInBackground() throws Exception {
                Thread.sleep(1500);
                return "Appointment Booked Successfully!\n"
                    + "Patient ID: " + id
                    + "\nDepartment: " + dept
                    + "\nDoctor: " + doc
                    + "\nTime: " + time;
            }

            protected void done() {
                bookingInProgress = false;
                book.setEnabled(true);

                if (!isDisplayable()) {
                    return;
                }

                try {
                    JOptionPane.showMessageDialog(
                        Task02.this, get()
                    );
                    status.setText("Appointment Confirmed");
                } catch (Exception ex) {
                    status.setText("Booking failed");
                    JOptionPane.showMessageDialog(
                        Task02.this, "Unable to complete booking"
                    );
                }
            }
        };

        worker.execute();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() ->
            new Task02().setVisible(true)
        );
    }
}
