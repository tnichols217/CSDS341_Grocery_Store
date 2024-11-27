import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class GroceryStoreApp {

    // Database connection details
    // SQLServerDriver.register();
    private static final String DB_URL = 
        "jdbc:sqlserver://172.20.15.194;" + //
        "databaseName=Grocery;" + //
        "encrypt=false";

    private static final String DB_USERNAME = "sa";
    private static final String DB_PASSWORD = "SuperSecurePassword1";

    private JFrame frame;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GroceryStoreApp app = new GroceryStoreApp();
            app.showLoginPage();
        });
    }

    // Login Page
    public void showLoginPage() {
        frame = new JFrame("Grocery Store Database");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1600, 1000);
        frame.setLayout(new GridBagLayout());

        GridBagConstraints g = new GridBagConstraints();
        g.gridx = 0;
        g.gridy = 1;

        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        // Create the title label
        JLabel titleLabel = new JLabel("Grocery Store");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 36));
        // Add the title label to the top of the frame
        titlePanel.add(titleLabel);
        frame.add(titlePanel, g);

        JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        centerPanel.setLocation(0, 10 );
        JLabel lblID = new JLabel("Employee ID:");
        lblID.setFont(new Font("Arial", Font.PLAIN, 20));
        JTextField txtID = new JTextField(50);
        JButton btnLogin = new JButton("Login");
        btnLogin.setPreferredSize(new Dimension(100, 40));
        btnLogin.setFont(new Font("Arial", Font.ITALIC, 20));
        

        centerPanel.add(lblID);
        centerPanel.add(txtID);
        centerPanel.add(btnLogin);

        g.gridy = 3;
        // Add the panel to the center of the frame
        frame.add(centerPanel, g);
        
        btnLogin.addActionListener(e -> {
            String employeeID = txtID.getText().trim();
            if (validateEmployeeID(employeeID)) {
                showMainMenu();
            } else {
                JOptionPane.showMessageDialog(frame, "Invalid Employee ID", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        frame.setVisible(true);
    }

    //Validate Employee ID
    private boolean validateEmployeeID(String employeeID) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
            String query = "SELECT * FROM employee WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, employeeID);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Main Menu
    public void showMainMenu() {
        frame.getContentPane().removeAll();
        frame.setTitle("Main Menu");

        GridBagConstraints g = new GridBagConstraints();
        g.gridx = 0;
        g.gridy = 0;
        g.insets = new Insets(10, 10, 0, 0); 
        g.anchor = GridBagConstraints.NORTHWEST;

        JButton backbutton = new JButton("Back");
        backbutton.setFont(new Font("Arial", Font.PLAIN, 20));
        backbutton.setPreferredSize(new Dimension(100, 40));

        frame.add(backbutton, g);

        JPanel panel = new JPanel(new GridLayout(4, 1, 10, 10));
        JButton btnAddItem = new JButton("Add Item");
        btnAddItem.setPreferredSize(new Dimension(400, 100));
        btnAddItem.setFont(new Font("Arial", Font.BOLD, 26));
        JButton btnSale = new JButton("Make Sale");
        btnSale.setPreferredSize(new Dimension(400, 100));
        btnSale.setFont(new Font("Arial", Font.BOLD, 26));
        JButton btnRestock = new JButton("Restock");
        btnRestock.setFont(new Font("Arial", Font.BOLD, 26));
        JButton btnCheckStatus = new JButton("Check Store Status");
        btnCheckStatus.setFont(new Font("Arial", Font.BOLD, 26));

        panel.add(btnAddItem);
        panel.add(btnSale);
        panel.add(btnRestock);
        panel.add(btnCheckStatus);

        g.gridx = 0; // Center column
        g.gridy = 1; // Center row
        g.weightx = 1; // Allow stretching
        g.weighty = 1; // Allow stretching
        g.insets = new Insets(0, 0, 0, 0); // No margins
        g.anchor = GridBagConstraints.CENTER; // Center in the cell
        g.fill = GridBagConstraints.NONE; // Do not resize buttons
        frame.add(panel, g);
    
        btnAddItem.addActionListener(e -> showAddItemPage());
        btnSale.addActionListener(e -> showSalePage());
        btnRestock.addActionListener(e -> showRestockPage());
        btnCheckStatus.addActionListener(e -> showStoreStatusPage());
        backbutton.addActionListener(e -> showLoginPage());

        frame.setVisible(true);
        frame.revalidate();
        frame.repaint();
    
    }

    public void showAddItemPage() {
        frame.getContentPane().removeAll();
        frame.setTitle("Add Item");
    }

    // Sale Page
    public void showSalePage() {
        frame.getContentPane().removeAll();
        frame.setTitle("Make Sale");

        GridBagConstraints g = new GridBagConstraints();
        g.gridx = 0;
        g.gridy = 0;
        g.insets = new Insets(10, 10, 0, 30); 
        g.anchor = GridBagConstraints.NORTHWEST;

        JButton backbutton = new JButton("Back");
        backbutton.setFont(new Font("Arial", Font.PLAIN, 20));
        backbutton.setPreferredSize(new Dimension(100, 40));

        frame.add(backbutton, g);

        JPanel panel = new JPanel(new GridBagLayout());

        DefaultListModel<String> itemList = new DefaultListModel<>();
        JList<String> itemDisplay = new JList<>(itemList);
        itemDisplay.setPreferredSize(new Dimension(1400, 800));

        JTextField txtBarcode = new JTextField(30);
        txtBarcode.setPreferredSize(new Dimension(100, 50));

        JButton btnAdd = new JButton("Add by Barcode");
        btnAdd.setFont(new Font("Arial", Font.PLAIN, 26));
        btnAdd.setPreferredSize(new Dimension(320, 50));

        JButton btnSearch = new JButton("Search for Item");
        btnSearch.setFont(new Font("Arial", Font.PLAIN, 26));
        btnSearch.setPreferredSize(new Dimension(320, 50));

        JPanel inputPanel = new JPanel(new GridBagLayout());
        g.gridx = 1;
        g.gridy = 1;
        g.insets = new Insets(0, 0, 0, 0);
        inputPanel.add(txtBarcode, g);
        g.gridx = 2;
        inputPanel.add(btnAdd, g);
        g.gridx = 3;
        inputPanel.add(btnSearch, g);

        JScrollPane scrollPane = new JScrollPane(itemDisplay);
        scrollPane.setPreferredSize(new Dimension(1000, 500));

        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        JButton btnCheckout = new JButton("Checkout");
        btnCheckout.setFont(new Font("Arial", Font.PLAIN, 26));
        btnCheckout.setPreferredSize(new Dimension(500, 100));

        g.gridx = 0; // Center column
        g.gridy = 1; // Center row
        g.weightx = 1; // Allow stretching
        g.weighty = 1; // Allow stretching
        g.insets = new Insets(0, 0, 0, 0); // No margins
        g.anchor = GridBagConstraints.CENTER; // Center in the cell
        g.fill = GridBagConstraints.NONE; // Do not resize buttons
        
        panel.add(inputPanel, g);
        g.gridy = 2;
        panel.add(scrollPane, g);
        g.gridy = 3;
        panel.add(btnCheckout, g);

        btnAdd.addActionListener(e -> {
            String barcode = txtBarcode.getText().trim();
            if (!barcode.isEmpty()) {
                String item = getItemByBarcode(barcode);
                if (item != null) {
                    itemList.addElement(item);
                } else {
                    JOptionPane.showMessageDialog(frame, "Item not found!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        backbutton.addActionListener(e -> showMainMenu());

        btnSearch.addActionListener(e -> showItemSearchDialog(itemList));

        btnCheckout.addActionListener(e -> {
            JOptionPane.showMessageDialog(frame, "Receipt printed!", "Success", JOptionPane.INFORMATION_MESSAGE);
            itemList.clear();
        });

        frame.add(panel, g);
        frame.setVisible(true);
        frame.revalidate();
        frame.repaint();
    }

    // Get Item by Barcode
    private String getItemByBarcode(String barcode) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
            String query = "SELECT name FROM inventory WHERE barcode = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, barcode);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("name");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Item Search Dialog
    private void showItemSearchDialog(DefaultListModel<String> itemList) {
        JDialog dialog = new JDialog(frame, "Item Search", true);
        dialog.setSize(400, 300);
        dialog.setLayout(new BorderLayout());

        JTextField txtSearch = new JTextField();
        JButton btnSearch = new JButton("Search");
        DefaultListModel<String> searchResults = new DefaultListModel<>();
        JList<String> searchDisplay = new JList<>(searchResults);
        JButton btnAdd = new JButton("Add Selected");
        JButton backbutton = new JButton("Back");

        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.add(txtSearch, BorderLayout.CENTER);
        inputPanel.add(btnSearch, BorderLayout.EAST);

        // btnSearch.addActionListener(e -> {
        //     searchResults.clear();
        //     String query = txtSearch.getText().trim();
        //     ArrayList<String> results = searchItems(query);
        //     for (String item : results) {
        //         searchResults.addElement(item);
        //     }
        // });

        btnAdd.addActionListener(e -> {
            String selectedItem = searchDisplay.getSelectedValue();
            if (selectedItem != null) {
                itemList.addElement(selectedItem);
                dialog.dispose();
            }
        });

        dialog.add(inputPanel, BorderLayout.NORTH);
        dialog.add(new JScrollPane(searchDisplay), BorderLayout.CENTER);
        dialog.add(btnAdd, BorderLayout.SOUTH);
        dialog.add(backbutton);

        dialog.setVisible(true);
    }

    // Search Items in Database
    // private ArrayList<String> searchItems(String query) {
    //     ArrayList<String> items = new ArrayList<>();
    //     try (Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
    //         String sql = "SELECT name FROM inventory WHERE name LIKE ?";
    //         PreparedStatement stmt = conn.prepareStatement(sql);
    //         stmt.setString(1, "%" + query + "%");
    //         ResultSet rs = stmt.executeQuery();
    //         while (rs.next()) {
    //             items.add(rs.getString("name"));
    //         }
    //     } catch (SQLException e) {
    //         e.printStackTrace();
    //     }
    //     return items;
    // }

    // Restock Page
    public void showRestockPage() {
        frame.getContentPane().removeAll();
        frame.setTitle("Restock");
        JButton backbutton = new JButton("Back");
        frame.add(backbutton);
        
        // Implement similar logic for displaying restock details and updating statuses
        // ...

        frame.revalidate();
        frame.repaint();
    }

    // Store Status Page
    public void showStoreStatusPage() {
        frame.getContentPane().removeAll();
        frame.setTitle("Store Status");
        // Implement similar logic for displaying inventory details
        // ...

        frame.revalidate();
        frame.repaint();
    }
}
