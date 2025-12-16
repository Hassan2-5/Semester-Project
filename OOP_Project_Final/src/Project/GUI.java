package Project;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;

import java.io.IOException;


public class GUI extends Application{

    @Override
    public void start(Stage primaryStage){

        try{
            Parent root = FXMLLoader.load(getClass().getResource("OldUserLogin.fxml"));
            primaryStage.setTitle("Messenger App");
            primaryStage.setScene(new Scene(root,800,500));
            primaryStage.show();
        }
        catch(IOException i){
            System.out.println(i);
            return;
        }

    }

}
