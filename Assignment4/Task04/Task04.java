
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Task04 extends JFrame {

    JButton button, clearButton;
    JLabel message;
    JTextField input;

    public Task04() {

        setTitle("Event Handling Forensics");
        setLayout(new FlowLayout());

        input = new JTextField(15);
        button = new JButton("Submit");
        clearButton = new JButton("Clear");
        message = new JLabel("Enter your name");

        add(input);
        add(button);
        add(clearButton);
        add(message);

        // ActionListener for Submit button
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                String name = input.getText().trim();

                if (name.isEmpty()) {
                    message.setText("Please enter your name");
                } else {
                    message.setText("Welcome " + name);
                }
            }
        });

        // ActionListener using lambda expression
        clearButton.addActionListener(e -> {
            input.setText("");
            message.setText("Fields cleared");
        });

        // MouseAdapter example
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                message.setText("Mouse entered Submit button");
            }

            public void mouseExited(MouseEvent e) {
                message.setText("Mouse left Submit button");
            }
        });

        // WindowAdapter example
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                int choice = JOptionPane.showConfirmDialog(
                    Task04.this,
                    "Do you want to exit?",
                    "Confirm Exit",
                    JOptionPane.YES_NO_OPTION
                );

                if (choice == JOptionPane.YES_OPTION) {
                    dispose();
                }
            }
        });

        setSize(450, 200);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() ->
            new Task04().setVisible(true)
        );
    }
}
