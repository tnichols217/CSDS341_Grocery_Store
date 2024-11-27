package csds341.tln32aac.Tables;

import java.sql.*;

public class SEmployee {
    public Integer id;
    public String fName;
    public String lName;
    public Integer contact;
    public Integer SSN;
    public Integer currentWage;

    public SEmployee(
        Integer id,
        String fName,
        String lName,
        Integer contact,
        Integer SSN,
        Integer currentWage
    ) {
        this.id = id;
        this.fName = fName;
        this.lName = lName;
        this.contact = contact;
        this.SSN = SSN;
        this.currentWage = currentWage;
    }

    public static SEmployee getEmployee(Integer employeeID, Connection conn) {
        try {
            PreparedStatement stmt = conn.prepareStatement("EXEC GetEmployeeByID @employeeID = ?;");
            stmt.setInt(1, employeeID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new SEmployee(
                    rs.getInt("id"),
                    rs.getString("fName"),
                    rs.getString("lName"),
                    rs.getInt("contact"),
                    rs.getInt("SSN"),
                    rs.getInt("currentWage")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
