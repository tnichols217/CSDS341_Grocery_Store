package csds341.tln32aac.Tables;

import java.sql.*;

public class SItem {
    public Integer id;
    public String name;
    public Integer currentPrice;
    public Integer supplier;
    public String unitType;
    public Integer discount;
    public Integer stock;
    public Integer targetAmount;

    public SItem(
        Integer id,
        String name,
        Integer currentPrice,
        Integer supplier,
        String unitType,
        Integer discount,
        Integer stock,
        Integer targetAmount
    ) {
        this.id = id;
        this.name = name;
        this.currentPrice = currentPrice;
        this.supplier = supplier;
        this.unitType = unitType;
        this.discount = discount;
        this.stock = stock;
        this.targetAmount = targetAmount;
    }

    public static SItem getItem(Integer itemID, Connection conn) {
        try {
            PreparedStatement stmt = conn.prepareStatement("EXEC GetItemByID @itemID = ?");
            stmt.setInt(1, itemID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new SItem(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getInt("currentPrice"),
                    rs.getInt("supplier"),
                    rs.getString("unitType"),
                    rs.getInt("discount"),
                    rs.getInt("cachedCurrentStock"),
                    rs.getInt("targetAmount")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
