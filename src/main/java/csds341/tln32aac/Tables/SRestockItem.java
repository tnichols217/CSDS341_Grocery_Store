package csds341.tln32aac.Tables;

import java.sql.*;
import java.util.ArrayList;

public class SRestockItem {
    public Integer restockID;
    public Integer itemID;
    public Integer quantity;
    public Date expiryDate;
    public Integer unitCost;
    public Integer totalCost;

    public SRestockItem(
        Integer restockID,
        Integer itemID,
        Integer quantity,
        Date expiryDate,
        Integer unitCost,
        Integer totalCost
    ) {
        this.restockID = restockID;
        this.itemID = itemID;
        this.quantity = quantity;
        this.expiryDate = expiryDate;
        this.unitCost = unitCost;
        this.totalCost = totalCost;
    }

    public static SRestockItem getRestockItem (Integer restockID, Integer itemID, Connection conn) {
        try {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM restockItem WHERE restockID = ? AND itemID = ?");
            stmt.setInt(1, restockID);
            stmt.setInt(2, itemID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new SRestockItem(
                    rs.getInt("restockID"),
                    rs.getInt("itemID"),
                    rs.getInt("quantity"),
                    rs.getDate("expiryDate"),
                    rs.getInt("unitCost"),
                    rs.getInt("totalCost")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ArrayList<SRestockItem> getRestock (Integer restockID, Connection conn) {
        ArrayList<SRestockItem> restockItems = new ArrayList<>();
        try {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM restockItem WHERE restockID = ?");
            stmt.setInt(1, restockID);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                restockItems.add(
                    new SRestockItem(
                        rs.getInt("restockID"),
                        rs.getInt("itemID"),
                        rs.getInt("quantity"),
                        rs.getDate("expiryDate"),
                        rs.getInt("unitCost"),
                        rs.getInt("totalCost")
                    )
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return restockItems;
    }
}
