package csds341.tln32aac;

import java.sql.*;
import java.util.ArrayList;

import com.microsoft.sqlserver.jdbc.SQLServerDriver;

public class SQLAdapter {
    private String DB_URL;
    private String DB_USERNAME;
    private String DB_PASSWORD;

    private Connection conn;

    public SQLAdapter(String DB_URL, String DB_USERNAME, String DB_PASSWORD) {
        this.DB_URL = DB_URL;
        this.DB_USERNAME = DB_USERNAME;
        this.DB_PASSWORD = DB_PASSWORD;
        try {
            SQLServerDriver.register();
            this.conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
            conn.setAutoCommit(false);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void closeConnection() {
        try {
            if (conn!= null &&!conn.isClosed()) {
                conn.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean reconnect() {
        try {
            conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public boolean getStatus() {
        try {
            return !conn.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }

    public boolean validateEmployeeID(Integer employeeID) {
        try {
            PreparedStatement statement = conn.prepareStatement("SELECT * FROM employee WHERE id = ?");
            statement.setInt(1, employeeID);
            ResultSet rs = statement.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            return false;
        }
    }

    public ArrayList<String> searchItems(String query) {
        return searchItems(query, 100);
    }

    public ArrayList<String> searchItems(String query, int quantity) {
        ArrayList<String> items = new ArrayList<>();
        try {
            PreparedStatement stmt = conn.prepareStatement("SELECT name FROM item WHERE name LIKE ?");
            stmt.setString(1, "%" + query + "%");
            ResultSet rs = stmt.executeQuery();
            for (int i = 0; i < quantity && rs.next(); i++) {
                items.add(rs.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return items;
    }

    public String getItemByBarcode(String barcode) {
        try {
            PreparedStatement stmt = conn.prepareStatement("SELECT barcode FROM barcode WHERE barcode = ?");
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

    public boolean insertBarcode(String barcode, Integer itemID) {
        try {
            PreparedStatement stmt = conn.prepareStatement("EXEC InsertBarcode @ean13 = ?, @itemID = ?;");
            stmt.setString(1, barcode);
            stmt.setInt(2, itemID);
            stmt.execute();
            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean createSale(Integer employeeID, ArrayList<Integer> itemID, ArrayList<Integer> quantity, Integer tip) {
        try {

            // Insert into sales table
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO sale (employeeID, tip) VALUES (?, ?);");
            stmt.setInt(1, employeeID);
            stmt.setInt(2, tip);
            stmt.executeUpdate();

            // Get the last inserted sale ID
            int saleID = conn.prepareStatement("SELECT SCOPE_IDENTITY();").executeQuery().getInt(1);

            // Insert into sales_items table
            for (int i = 0; i < itemID.size(); i++) {
                stmt = conn.prepareStatement("INSERT INTO sales_items (saleID, itemID, quantity) VALUES (?,?,?);");
                stmt.setInt(1, saleID);
                stmt.setInt(2, itemID.get(i));
                stmt.setInt(3, quantity.get(i));
                stmt.executeUpdate();
            }
            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
