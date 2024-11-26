package csds341.tln32aac.Tables;

import java.sql.*;

public class SSale {
    public Integer id;
    public Integer employeeID;
    public Timestamp timestamp;
    public Integer tip;
    public Integer paymentID;

    public SSale(
        Integer id,
        Integer employeeID,
        Timestamp timestamp,
        Integer tip,
        Integer paymentID
    ) {
        this.id = id;
        this.employeeID = employeeID;
        this.timestamp = timestamp;
        this.tip = tip;
        this.paymentID = paymentID;
    }

    public static SSale getSale(Integer saleID, Connection conn) {
        try {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM sale WHERE id = ?");
            stmt.setInt(1, saleID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new SSale(
                    rs.getInt("id"),
                    rs.getInt("employee_id"),
                    rs.getTimestamp("timestamp"),
                    rs.getInt("tip"),
                    rs.getInt("paymentID")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
