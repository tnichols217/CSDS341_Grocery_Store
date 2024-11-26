package csds341.tln32aac.Tables;

import java.sql.*;

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

    public static SSaleItem getSaleItem(Integer saleID, Connection conn) {
        try {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM saleItem WHERE id = ?");
            stmt.setInt(1, saleID);
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
}
