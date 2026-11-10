
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;

public class Task03 extends JFrame {

    JTextField searchField;
    JComboBox<String> category;
    JTable table;
    DefaultTableModel model;
    JButton searchButton;

    String[][] books = {
        {"101", "Java Programming", "James Gosling", "Programming", "Available"},
        {"102", "Python Basics", "Guido Rossum", "Programming", "Available"},
        {"103", "Data Science", "John Smith", "AI", "Issued"},
        {"104", "Machine Learning", "Tom Mitchell", "AI", "Available"},
        {"105", "Database Systems", "Raghu Ramakrishnan", "Database", "Available"}
    };

    public Task03() {

        setTitle("Campus Library Search Screen");
        setLayout(new BorderLayout(10, 10));

        JPanel searchPanel = new JPanel();

        searchField = new JTextField(15);

        category = new JComboBox<>(new String[]{
            "Title", "Author", "Category"
        });

        searchButton = new JButton("Search");

        searchPanel.add(new JLabel("Search:"));
        searchPanel.add(searchField);
        searchPanel.add(category);
        searchPanel.add(searchButton);

        add(searchPanel, BorderLayout.NORTH);

        model = new DefaultTableModel(
            new String[]{"Book ID", "Title", "Author",
                         "Category", "Status"}, 0
        );

        table = new JTable(model);

        add(new JScrollPane(table), BorderLayout.CENTER);

        searchButton.addActionListener(e -> searchBooks());

        searchField.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    searchBooks();
                }
            }
        });

        displayBooks(books);

        setSize(700, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    void displayBooks(String[][] results) {

        model.setRowCount(0);

        for (String[] book : results) {
            model.addRow(book);
        }
    }

    void searchBooks() {

        String keyword = searchField.getText()
                                    .trim()
                                    .toLowerCase();

        String selected = (String) category.getSelectedItem();

        if (keyword.isEmpty()) {
            JOptionPane.showMessageDialog(
                this, "Please enter a search keyword"
            );
            displayBooks(books);
            return;
        }

        model.setRowCount(0);

        for (String[] book : books) {

            String value = "";

            if (selected.equals("Title")) {
                value = book[1];
            } else if (selected.equals("Author")) {
                value = book[2];
            } else if (selected.equals("Category")) {
                value = book[3];
            }

            if (value.toLowerCase().contains(keyword)) {
                model.addRow(book);
            }
        }

        if (model.getRowCount() == 0) {
            JOptionPane.showMessageDialog(
                this, "No books found"
            );
        }
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() ->
            new Task03().setVisible(true)
        );
    }
}
