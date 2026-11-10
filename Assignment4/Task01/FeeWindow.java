import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class FeeWindow extends JFrame {

    JTextField regNo = new JTextField(15);
    JTextField amount = new JTextField(15);

    JButton pay = new JButton("Pay");
    JButton clear = new JButton("Clear");
    JButton receipt = new JButton("Receipt");

    JLabel status = new JLabel("Enter payment details");

    FeeService service = new FeeService();

    String lastReceipt = "";
    boolean processing = false;

    public FeeWindow() {

        setTitle("Student Fee Payment");
        setLayout(new GridLayout(5, 2, 10, 10));

        add(new JLabel("Registration Number:"));
        add(regNo);

        add(new JLabel("Amount:"));
        add(amount);

        add(pay);
        add(clear);

        add(receipt);
        add(status);

        pay.addActionListener(e -> makePayment());

        clear.addActionListener(e -> {
            if (!processing) {
                regNo.setText("");
                amount.setText("");
                status.setText("Fields cleared");
                lastReceipt = "";
            }
        });

        receipt.addActionListener(e -> {
            if (lastReceipt.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "No receipt available");
            } else {
                JOptionPane.showMessageDialog(this, lastReceipt);
            }
        });

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (processing) {
                    JOptionPane.showMessageDialog(
                            FeeWindow.this,
                            "Payment is in progress. Please wait.");
                } else {
                    dispose();
                }
            }
        });

        setSize(450, 250);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
    }

    private void makePayment() {

        if (processing) {
            return;
        }

        processing = true;
        pay.setEnabled(false);
        status.setText("Processing payment...");

        SwingWorker<String, Void> worker =
                new SwingWorker<String, Void>() {

            @Override
            protected String doInBackground() {
                return service.pay(
                        regNo.getText(),
                        amount.getText());
            }

            @Override
            protected void done() {
                processing = false;
                pay.setEnabled(true);

                try {
                    lastReceipt = get();
                    status.setText("Payment Successful");

                    JOptionPane.showMessageDialog(
                            FeeWindow.this, lastReceipt);

                } catch (Exception ex) {
                    lastReceipt = "";
                    status.setText("Payment Failed");

                    JOptionPane.showMessageDialog(
                            FeeWindow.this,
                            ex.getCause() != null
                                    ? ex.getCause().getMessage()
                                    : ex.getMessage(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        };

        worker.execute();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() ->
                new FeeWindow().setVisible(true));
    }
}