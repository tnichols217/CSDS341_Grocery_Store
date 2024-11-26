package csds341.tln32aac;

import javax.swing.SwingUtilities;

public class GroceryStoreApp {

    // Database connection details
    // SQLServerDriver.register();
    private static String DB_URL = 
        "jdbc:sqlserver://172.20.15.194;" + //
        "databaseName=Grocery;" + //
        "encrypt=false";

    private static String DB_USERNAME = "sa";
    private static String DB_PASSWORD = "SuperSecurePassword1";

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GUI app = new GUI(
                new SQLAdapter(DB_URL, DB_USERNAME, DB_PASSWORD)
            );
            app.showLoginPage();
        });
    }
}
