package Project;

/*
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MainScreenController {
    MainScreen ms = new MainScreen();


    public void confirmContact(){
        DatabaseConnection dbConnection = new DatabaseConnection();
        Connection connect = dbConnection.getConnection();

        String verifyLogin = "Select count(1) from accounts WHERE user_id = '" + ms.addSearch.getText() +"' ";

        try {
            Statement statement = connect.createStatement();
            ResultSet queryResult = statement.executeQuery(verifyLogin);

            while (queryResult.next()) {
                if (queryResult.getInt(1) == 1) {

                    ms.addSearchResult.setText("User ID Exists");

                } else {
                    System.out.println("No such ");

                }
            }
        }
        catch(SQLException e){
            System.out.println(e);
        }
    }
}
*/