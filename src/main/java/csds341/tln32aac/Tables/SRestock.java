package csds341.tln32aac.Tables;

import java.sql.*;

public class SRestock {
    public Integer id;
    public Integer supplierID;
    public String status;
    public Date orderDate;
    public Date confirmDate;
    public Date deliveryDate;
    public Date restockDate;
    public Integer additionalCost;

    public SRestock(
        Integer id,
        Integer supplierID,
        String status,
        Date orderDate,
        Date confirmDate,
        Date deliveryDate,
        Date restockDate,
        Integer additionalCost
    ) {
        this.id = id;
        this.supplierID = supplierID;
        this.status = status;
        this.orderDate = orderDate;
        this.confirmDate = confirmDate;
        this.deliveryDate = deliveryDate;
        this.restockDate = restockDate;
        this.additionalCost = additionalCost;
    }

    public static SRestock getRestock (Integer saleID, Connection conn) {
        try {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM restock WHERE id = ?");
            stmt.setInt(1, saleID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new SRestock(
                    rs.getInt("id"),
                    rs.getInt("supplierID"),
                    rs.getString("status"),
                    rs.getDate("orderDate"),
                    rs.getDate("confirmDate"),
                    rs.getDate("deliveryDate"),
                    rs.getDate("restockDate"),
                    rs.getInt("additionalCost")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}