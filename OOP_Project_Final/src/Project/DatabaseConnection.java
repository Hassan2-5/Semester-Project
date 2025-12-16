package Project;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConnection {

    public Connection databaseLink;

    public Connection getConnection(){
        String databaseName = "useraccounts";
        String databaseUser = "root";
        String databasePassword = "GIqv@2205727";
        String url = "jdbc:mysql://localhost:3306/" + databaseName;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            databaseLink = DriverManager.getConnection(url,databaseUser,databasePassword);
        }
        catch(Exception e){
            System.out.println(e);
        }


        return databaseLink;
    }

}
