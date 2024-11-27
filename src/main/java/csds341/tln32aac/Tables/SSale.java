package csds341.tln32aac.Tables;

import java.sql.*;

public class SSale {
    public Integer id;
    public Integer employeeID;
    public Timestamp timestamp;
    public Integer tip;
    public Integer paymentID;
    public Integer total;
    public Integer tipAmount;

    public SSale(
        Integer id,
        Integer employeeID,
        Timestamp timestamp,
        Integer tip,
        Integer paymentID,
        Integer total,
        Integer tipAmount
    ) {
        this.id = id;
        this.employeeID = employeeID;
        this.timestamp = timestamp;
        this.tip = tip;
        this.paymentID = paymentID;
        this.total = total;
        this.tipAmount = tipAmount;
    }

    public static SSale getSale(Integer saleID, Connection conn) {
        try {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM v_sale WHERE id = ?");
            stmt.setInt(1, saleID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new SSale(
                    rs.getInt("id"),
                    rs.getInt("employeeID"),
                    rs.getTimestamp("timestamp"),
                    rs.getInt("tip"),
                    rs.getInt("paymentID"),
                    rs.getInt("total"),
                    rs.getInt("tipAmount")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
