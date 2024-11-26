package csds341.tln32aac.Tables;

import java.sql.*;
import java.util.ArrayList;

public class SSaleItem {
    public Integer saleID;
    public Integer itemID;
    public Integer quantity;
    public Integer unitCost;
    public Integer discount;
    public Integer totalCost;

    public SSaleItem(
        Integer saleID,
        Integer itemID,
        Integer quantity,
        Integer unitCost,
        Integer discount,
        Integer totalCost
    ) {
        this.saleID = saleID;
        this.itemID = itemID;
        this.quantity = quantity;
        this.unitCost = unitCost;
        this.discount = discount;
        this.totalCost = totalCost;
    }

    public static SSaleItem getSaleItem(Integer saleID, Integer itemID, Connection conn) {
        try {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM saleItem WHERE saleID = ? AND itemID = ?");
            stmt.setInt(1, saleID);
            stmt.setInt(2, itemID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new SSaleItem(
                    rs.getInt("saleID"),
                    rs.getInt("itemID"),
                    rs.getInt("quantity"),
                    rs.getInt("unitCost"),
                    rs.getInt("discount"),
                    rs.getInt("totalCost")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ArrayList<SSaleItem> getSale(Integer saleID, Connection conn) {
        ArrayList<SSaleItem> saleItems = new ArrayList<>();
        try {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM saleItem WHERE saleID =?");
            stmt.setInt(1, saleID);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                saleItems.add(
                    new SSaleItem(
                        rs.getInt("saleID"),
                        rs.getInt("itemID"),
                        rs.getInt("quantity"),
                        rs.getInt("unitCost"),
                        rs.getInt("discount"),
                        rs.getInt("totalCost")
                    )
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return saleItems;
    }
}
