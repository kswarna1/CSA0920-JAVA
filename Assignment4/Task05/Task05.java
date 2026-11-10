
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Task05 extends JFrame {

    JTextField number1, number2;
    JLabel result;

    public Task05() {

        setTitle("Different Event Handling Styles");
        setLayout(new FlowLayout());

        number1 = new JTextField(8);
        number2 = new JTextField(8);

        JButton anonymousButton = new JButton("Anonymous");
        JButton lambdaButton = new JButton("Lambda");
        JButton separateButton = new JButton("Separate Class");

        result = new JLabel("Enter two numbers");

        add(new JLabel("Number 1:"));
        add(number1);

        add(new JLabel("Number 2:"));
        add(number2);

        add(anonymousButton);
        add(lambdaButton);
        add(separateButton);
        add(result);

        // 1. Anonymous Inner Class
        anonymousButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                calculate();
            }
        });

        // 2. Lambda Expression
        lambdaButton.addActionListener(e -> calculate());

        // 3. Separate Listener Class
        separateButton.addActionListener(
            new SeparateListener(this)
        );

        setSize(550, 220);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    void calculate() {

        try {
            int a = Integer.parseInt(number1.getText());
            int b = Integer.parseInt(number2.getText());

            result.setText("Addition: " + (a + b));

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(
                this, "Please enter valid numbers"
            );
        }
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() ->
            new Task05().setVisible(true)
        );
    }
}

// Separate Listener Class
class SeparateListener implements ActionListener {

    Task05 window;

    SeparateListener(Task05 window) {
        this.window = window;
    }

    public void actionPerformed(ActionEvent e) {
        window.calculate();
    }
}
