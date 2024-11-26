package csds341.tln32aac.Tables;

import java.sql.*;

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

    public static SRestockItem getRestockItem (Integer saleID, Connection conn) {
        try {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM restock WHERE id = ?");
            stmt.setInt(1, saleID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new SRestockItem(
                    rs.getInt("id"),
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
}
