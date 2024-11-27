package csds341.tln32aac;

import javax.swing.SwingUtilities;

public class GroceryStoreApp {

    private static String DB_URL = 
        "jdbc:sqlserver://localhost;" + //
        "databaseName=Grocery;" + //
        "encrypt=false";

    private static String DB_USERNAME = "sa";
    private static String DB_PASSWORD = "SuperSecurePassword1";

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GUI app = new GUI(
                null
            );
            app.showLoginPage();
        });
    }
}
