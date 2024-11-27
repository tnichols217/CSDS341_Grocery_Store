package csds341.tln32aac;

import java.sql.*;
import java.util.ArrayList;

import com.microsoft.sqlserver.jdbc.SQLServerDriver;

import csds341.tln32aac.Tables.*;

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

    public Timestamp startShift(SEmployee employee) {
        try {
            PreparedStatement statement = conn.prepareStatement("INSERT INTO shift (employeeID, wage) OUTPUT Inserted.startTime VALUES (?, ?);");
            statement.setInt(1, employee.id);
            statement.setInt(2, employee.currentWage);
            
            ResultSet res = statement.executeQuery();

            res.next();
            
            // Get the last inserted sale ID
            Timestamp shiftID = res.getTimestamp(1);
            conn.commit();
            return shiftID;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean endShift(Timestamp shiftID) {
        try {
            PreparedStatement statement = conn.prepareStatement("UPDATE shift SET endTime = CURRENT_TIMESTAMP WHERE startTime = ?");
            statement.setTimestamp(1, shiftID);
            statement.executeUpdate();
            conn.commit();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean addItem(String name, Integer currentPrice, Integer supplier, String unitType, Integer discount) {
        try {
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO item (name, currentPrice, supplier, unitType, discount) VALUES (?, ?, ?, ?, ?);");
            stmt.setString(1, name);
            stmt.setInt(2, currentPrice);
            stmt.setInt(3, supplier);
            stmt.setString(4, unitType);
            stmt.setInt(5, discount);
            stmt.execute();
            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;

    }
    public ArrayList<SItem> searchItems(String query) {
        return searchItems(query, 100);
    }

    public ArrayList<SItem> searchItems(String query, int quantity) {
        ArrayList<SItem> items = new ArrayList<>();
        try {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM item WHERE name LIKE ?");
            stmt.setString(1, "%" + query + "%");
            ResultSet rs = stmt.executeQuery();
            for (int i = 0; i < quantity && rs.next(); i++) {
                items.add(new SItem(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getInt("currentPrice"),
                    rs.getInt("supplier"),
                    rs.getString("unitType"),
                    rs.getInt("discount"),
                    rs.getInt("cachedCurrentStock"),
                    rs.getInt("targetAmount")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return items;
    }

    public ArrayList<SRestock> getRestocks(int quantity) {
        ArrayList<SRestock> items = new ArrayList<>();
        try {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM v_restock");
            ResultSet rs = stmt.executeQuery();
            for (int i = 0; i < quantity && rs.next(); i++) {
                items.add(new SRestock(
                    rs.getInt("id"),
                    rs.getInt("supplierID"),
                    rs.getString("status"),
                    rs.getDate("orderDate"),
                    rs.getDate("confirmDate"),
                    rs.getDate("deliveryDate"),
                    rs.getDate("restockDate"),
                    rs.getInt("additionalCost"),
                    rs.getInt("totalCost")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return items;
    }

    public ArrayList<SItem> getItems(int quantity) {
        ArrayList<SItem> items = new ArrayList<>();
        try {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM item");
            ResultSet rs = stmt.executeQuery();
            for (int i = 0; i < quantity && rs.next(); i++) {
                items.add(new SItem(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getInt("currentPrice"),
                    rs.getInt("supplier"),
                    rs.getString("unitType"),
                    rs.getInt("discount"),
                    rs.getInt("cachedCurrentStock"),
                    rs.getInt("targetAmount")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return items;
    }

    public Integer getItemByBarcode(String barcode) {
        try {
            PreparedStatement stmt = conn.prepareStatement("SELECT itemID FROM barcode WHERE barcode = ?");
            stmt.setString(1, barcode);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("itemID");
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

    public Integer createSale(Integer employeeID, ArrayList<SItem> saleItems, ArrayList<Integer> quantity, Integer tip) {
        try {

            // Insert into sales table
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO sale (employeeID, tip) VALUES (?, ?); SELECT SCOPE_IDENTITY();");
            stmt.setInt(1, employeeID);
            stmt.setInt(2, tip);
            
            ResultSet res = stmt.executeQuery();

            res.next();
            
            // Get the last inserted sale ID
            int saleID = res.getInt(1);

            // Insert into sales_items table
            for (int i = 0; i < saleItems.size(); i++) {
                stmt = conn.prepareStatement("INSERT INTO saleItem (saleID, itemID, quantity, unitCost, discount) VALUES (?,?,?,?,?);");
                stmt.setInt(1, saleID);
                stmt.setInt(2, saleItems.get(i).id);
                stmt.setInt(3, quantity.get(i));
                stmt.setInt(4, saleItems.get(i).currentPrice);
                stmt.setInt(5, saleItems.get(i).discount);
                stmt.executeUpdate();
            }
            conn.commit();
            return saleID;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Table CRUD operations overloads

    public SEmployee getEmployeeByID(Integer employeeID) {
        return SEmployee.getEmployee(employeeID, conn);
    }

    public SItem getItemByID(Integer itemID) {
        return SItem.getItem(itemID, conn);
    }

    public SRestock getRestockByID(Integer restockID) {
        return SRestock.getRestock(restockID, conn);
    }

    public SRestockItem getRestockItemByID(Integer restockID, Integer itemID) {
        return SRestockItem.getRestockItem(restockID, itemID, conn);
    }

    public ArrayList<SRestockItem> getFullRestock(Integer restockID) {
        return SRestockItem.getRestock(restockID, conn);
    }

    public SSale getSaleByID(Integer saleID) {
        return SSale.getSale(saleID, conn);
    }

    public SSaleItem getSaleItemByID(Integer saleID, Integer itemID) {
        return SSaleItem.getSaleItem(saleID, itemID, conn);
    }

    public ArrayList<SSaleItem> getFullSale(Integer saleID) {
        return SSaleItem.getSale(saleID, conn);
    }
}
