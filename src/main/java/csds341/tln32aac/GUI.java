package csds341.tln32aac;

import javax.swing.*;

import csds341.tln32aac.Tables.SEmployee;
import csds341.tln32aac.Tables.SItem;
import csds341.tln32aac.Tables.SSale;
import csds341.tln32aac.Tables.SSaleItem;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Consumer;

public class GUI {
    private JFrame frame;
    private SQLAdapter dbAdapter;
    private SEmployee employee;

    public GUI(SQLAdapter dbAdapter) {
        this.dbAdapter = dbAdapter;
    }

    /**
     * Shows the login page for the grocery store database
     */
    public void showLoginPage() {
        frame = new JFrame("Grocery Store Database");
        frame.getContentPane().removeAll();
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

        btnLogin.addActionListener(e -> showMainMenu());
        
        btnLogin.addActionListener(e -> {
            Integer employeeID = Integer.parseInt(txtID.getText().trim());
            if (dbAdapter.validateEmployeeID(employeeID)) {
                employee = dbAdapter.getEmployeeByID(employeeID);
                showMainMenu();
            } else {
                JOptionPane.showMessageDialog(frame, "Invalid Employee ID", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        frame.setVisible(true);
    }

    /**
     * Show the main menu for the grocery store database
     */
    public void showMainMenu() {
        frame.getContentPane().removeAll();
        frame.setTitle("Main Menu");

        GridBagConstraints g = new GridBagConstraints();
        g.gridx = 0;
        g.gridy = 0;


        JPanel panel = new JPanel(new GridLayout(4, 1, 10, 10));
        JButton btnAddItem = new JButton("Add Item to Grocery Store");
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
    
        g.anchor = GridBagConstraints.SOUTH;
        g.insets = new Insets(0, 0, 100, 0);
        JButton btnLogout = new JButton("Log out");
        btnLogout.setFont(new Font("Arial", Font.ITALIC, 26));
        btnLogout.setPreferredSize(new Dimension(200, 50));
        frame.add(btnLogout, g);

        btnAddItem.addActionListener(e -> showAddItemPage());
        btnSale.addActionListener(e -> showSalePage());
        btnRestock.addActionListener(e -> showRestockPage());
        btnCheckStatus.addActionListener(e -> showStoreStatusPage());
        btnLogout.addActionListener(e -> showLoginPage());

        frame.setVisible(true);
        frame.revalidate();
        frame.repaint();
    
    }

    /**
     * Shows the add item page
     */
    public void showAddItemPage() {
        frame.getContentPane().removeAll();
        frame.setTitle("Add Item");

        GridBagConstraints g = new GridBagConstraints();
        g.gridx = 0;
        g.gridy = 0;
        g.insets = new Insets(10, 10, 0, 30); 
        g.anchor = GridBagConstraints.NORTHWEST;

        JButton backbutton = new JButton("Back");
        backbutton.setFont(new Font("Arial", Font.PLAIN, 20));
        backbutton.setPreferredSize(new Dimension(100, 40));

        frame.add(backbutton, g);

        backbutton.addActionListener(e -> showMainMenu());

        JLabel itemName = new JLabel("Item name: ");
        itemName.setFont(new Font("Arial", Font.PLAIN, 20));
        JLabel itemCurrentPrice = new JLabel("Item current price: ");
        itemCurrentPrice.setFont(new Font("Arial", Font.PLAIN, 20));
        JLabel itemSupplier = new JLabel("Item supplier phone number: ");
        itemSupplier.setFont(new Font("Arial", Font.PLAIN, 20));
        JLabel itemUnitType = new JLabel("Item unit type (item, weight, volume): ");
        itemUnitType.setFont(new Font("Arial", Font.PLAIN, 20));
        JLabel itemDiscount = new JLabel("Item discount percentage: ");
        itemDiscount.setFont(new Font("Arial", Font.PLAIN, 20));
        JLabel itemStock = new JLabel("Item stock amount: ");
        itemStock.setFont(new Font("Arial", Font.PLAIN, 20));

        JTextField itemNameTxt = new JTextField(50);
        itemNameTxt.setPreferredSize(new Dimension(100, 50));
        JTextField itemCurrentPriceTxt = new JTextField(50);
        itemCurrentPriceTxt.setPreferredSize(new Dimension(100, 50));
        JTextField itemSupplierTxt = new JTextField(50);
        itemSupplierTxt.setPreferredSize(new Dimension(100, 50));
        JTextField itemUnitTypeTxt = new JTextField(50);
        itemUnitTypeTxt.setPreferredSize(new Dimension(100, 50));
        JTextField itemDiscountTxt = new JTextField(50);
        itemDiscountTxt.setPreferredSize(new Dimension(100, 50));
        JTextField itemStockTxt = new JTextField(50);
        itemStockTxt.setPreferredSize(new Dimension(100, 50));

        JButton btnAddItem = new JButton("Add Item");
        btnAddItem.setFont(new Font("Arial", Font.PLAIN, 20));
        btnAddItem.setPreferredSize(new Dimension(200, 50));

        JPanel panel = new JPanel(new GridLayout(6, 2, 2, 10));

        panel.add(itemName);
        panel.add(itemNameTxt);
        panel.add(itemCurrentPrice);
        panel.add(itemCurrentPriceTxt);
        panel.add(itemSupplier);
        panel.add(itemSupplierTxt);
        panel.add(itemUnitType);
        panel.add(itemUnitTypeTxt);
        panel.add(itemDiscount);
        panel.add(itemDiscountTxt);
        panel.add(itemStock);
        panel.add(itemStockTxt);
        

        g.gridx = 0; // Center column
        g.gridy = 0; // Center row
        g.weightx = 1; // Allow stretching
        g.weighty = 1; // Allow stretching
        g.insets = new Insets(0, 0, 0, 0); // No margins
        g.anchor = GridBagConstraints.CENTER; // Center in the cell
        g.fill = GridBagConstraints.NONE; // Do not resize buttons
        frame.add(panel, g);
        g.anchor = GridBagConstraints.SOUTH;
        g.insets = new Insets(0, 0, 100, 0); // No margins
        frame.add(btnAddItem, g);

        btnAddItem.addActionListener(e -> {
            String item = itemNameTxt.getText().trim();
            Integer currentPrice = Integer.parseInt(itemCurrentPriceTxt.getText().trim());
            Integer supplier = Integer.parseInt(itemSupplierTxt.getText().trim());
            String unitType = itemUnitTypeTxt.getText().trim();
            Integer discount = Integer.parseInt(itemDiscount.getText().trim());
            Integer stock = Integer.parseInt(itemStock.getText().trim());

            
            if (dbAdapter.addItem(item, currentPrice, supplier, unitType, discount, stock)) {
                        JOptionPane.showMessageDialog(frame, "Item successfully added!");
            } 

        });
        frame.setVisible(true);
        frame.revalidate();
        frame.repaint();

    }

    /**
     * shows item quantity page
     */
    private void askItemQuantity(Consumer<Integer> cb) {
        JFrame frame = new JFrame("Quantity");
        frame.setSize(400, 300);
        frame.setLayout(new GridBagLayout());


        JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel quantity = new JLabel("Quantity:");
        quantity.setFont(new Font("Arial", Font.PLAIN, 20));
        JTextField quantityText = new JTextField(10);
        JButton btnSave = new JButton("Save");
        btnSave.setPreferredSize(new Dimension(100, 40));
        btnSave.setFont(new Font("Arial", Font.ITALIC, 20));
        

        centerPanel.add(quantity);
        centerPanel.add(quantityText);
        centerPanel.add(btnSave);
        
        btnSave.addActionListener(e -> {
            cb.accept(Integer.parseInt(quantityText.getText().trim()));
        });

        btnSave.addActionListener(e -> frame.dispose());

        GridBagConstraints g = new GridBagConstraints();
        g.gridx = 0;
        g.gridy = 0;
        g.anchor = GridBagConstraints.CENTER;
        // Add the panel to the center of the frame
        frame.add(centerPanel, g);
        
        frame.setVisible(true);
    }

    /**
     * Shows the create a sale page
     */
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

        ArrayList<SItem> items = new ArrayList<SItem>();
        ArrayList<Integer> quantities = new ArrayList<Integer>();
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
            askItemQuantity(q -> {
                String barcode = txtBarcode.getText().trim();
                if (!barcode.isEmpty()) {
                    Integer itemID = dbAdapter.getItemByBarcode(barcode);
                    SItem item = dbAdapter.getItemByID(itemID);
                    if (item != null) {
                        items.add(item);
                        quantities.add(q);
                        itemList.addElement(item.name + " - " + q);
                    } else {
                        JOptionPane.showMessageDialog(frame, "Item not found!", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                };
            });
        });

        backbutton.addActionListener(e -> showMainMenu());

        btnAdd.addActionListener(e -> {
            showItemSearchDialog(item -> {
                String barcode = txtBarcode.getText().trim();
                if (!barcode.isEmpty()) {
                    Integer itemID = dbAdapter.getItemByBarcode(barcode);
                    SItem item = dbAdapter.getItemByID(itemID);
                    if (item != null) {
                        items.add(item);
                        quantities.add(q);
                        itemList.addElement(item.name + " - " + q);
                    } else {
                        JOptionPane.showMessageDialog(frame, "Item not found!", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                };
            }); 
        }

        );

        btnCheckout.addActionListener(e -> {
            Integer saleID = dbAdapter.createSale(employee.id, items, quantities, 0);
            if (saleID != null) {
                SSale sale = dbAdapter.getSaleByID(saleID);
                ArrayList<SSaleItem> saleItems = dbAdapter.getFullSale(saleID);
                String receipt = "Sale number: " + sale.id.toString() + "\n" + 
                    String.format("%20s %5s %5s", "Name", "Num", "Total") +
                    saleItems.stream()
                        .map(s -> "" + String.format("%20s", s.item.name) + " " + String.format("%5d", s.quantity) + " " + String.format("%5d", s.totalCost))
                        .reduce("", (a, b) -> a + "\n" + b) +
                    String.format("\nTotal cost: %d", sale.total);
                JOptionPane.showMessageDialog(frame, receipt, "Reciept", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(frame, "Failed to create sale", "Failiure", JOptionPane.INFORMATION_MESSAGE);
            }
            itemList.clear();
        });

        frame.add(panel, g);
        frame.setVisible(true);
        frame.revalidate();
        frame.repaint();
    }

    /**
     * Opens a window to search for items
     * @param itemList The list of items to populate with the searched items
     * DefaultListModel<String> itemList
     * 
     * ok for show item search dialog this is how it goes
     * type in item id, click search
     * item name will pop up
     * click add and it will ask for item quantity and once u type quantity and hit save
     * everything will show up together
     */
    private void showItemSearchDialog(Consumer<Integer> cb) {
        JDialog dialog = new JDialog(frame, "Item Search", true);
        dialog.setSize(400, 300);
        dialog.setLayout(new BorderLayout());

        JTextField txtSearch = new JTextField();
        JButton btnSearch = new JButton("Search");
        btnSearch.setFont(new Font("Arial", Font.PLAIN, 16));
        DefaultListModel<String> searchResults = new DefaultListModel<>();
        JList<String> searchDisplay = new JList<>(searchResults);
        JButton btnAdd = new JButton("Add");
        btnAdd.setFont(new Font("Arial", Font.PLAIN, 16));

        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.add(txtSearch, BorderLayout.CENTER);
        inputPanel.add(btnSearch, BorderLayout.EAST);

        btnSearch.addActionListener(e -> {
            searchResults.clear();
            Integer name = Integer.parseInt(txtSearch.getText().trim());
            //String result = dbAdapter.searchItems(name).name;
            //searchResults.addElement(result);
        });

        btnAdd.addActionListener(e -> {
            cb.accept(Integer.parseInt(searchResults.get(0)));
        });

        // btnAdd.addActionListener(e -> {
        //     askItemQuantity(q -> { 
        //         String itemName = searchResults.get(0).trim();
        //         if (!itemName.isEmpty()) {
        //             Integer itemID = dbAdapter.getItemByBarcode(barcode);
        //             SItem item = dbAdapter.getItemByID(itemID);
        //             if (item != null) {
        //                 showSalePage.items.add(item);
        //                 quantities.add(q);
        //                 itemList.addElement(item.name + " - " + q);
        //             } else {
        //                 JOptionPane.showMessageDialog(frame, "Item not found!", "Error", JOptionPane.ERROR_MESSAGE);
        //             }
        //         };
        //     }); 

        // });

        // btnAdd.addActionListener(e -> {
        //     String selectedItem = searchDisplay.getSelectedValue();
        //     if (selectedItem != null) {
        //         itemList.addElement(selectedItem);
        //         dialog.dispose();
        //     }
        // });

        dialog.add(inputPanel, BorderLayout.NORTH);
        dialog.add(new JScrollPane(searchDisplay), BorderLayout.CENTER);
        dialog.add(btnAdd, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }

    /**
     * Shows the restock page for creating restock requests
     */
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

    /**
     * Shows the store status page for displaying inventory details and current statuses
     */
    public void showStoreStatusPage() {
        frame.getContentPane().removeAll();
        frame.setTitle("Store Status");
        // Implement similar logic for displaying inventory details
        // ...

        frame.revalidate();
        frame.repaint();
    }
}
