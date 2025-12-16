package Project;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;


import javax.swing.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Optional;

public class MainScene extends Application implements MessageListener {

    User user;
    String contactChat;
    ArrayList<Contacts> contactList = new ArrayList<>();
    ScrollPane messagePane;
    VBox vBox2;
    VBox contactPane;


    String currentActiveChatID = null;
    boolean verifyContact= false;

    String tempContactName;
    String tempContactID;

    Stage globalStage;
    Scene globalScene;


    Image finalDefaultProfileImage;
    VBox messageBox;

    //New user login Label used in onMessageReceived method
    Label newUserLoginLabel;
    //Old user login Label for onMessageReceived Method
    Label loginLabel;

    //Old Login Scene
    Scene oldLoginScene;

    //Messages Array
    ArrayList<Message> messageArrayList = new ArrayList<>();


    @Override
    public void start(Stage PrimaryStage){
        globalStage = PrimaryStage;



        BorderPane borderPane = new BorderPane();
        SplitPane splitPane = new SplitPane();
        VBox vBox1 = new VBox();
        vBox2 = new VBox();
        vBox2.setAlignment(Pos.CENTER);
        Label StartChatting = new Label("Start New Chat");
        StartChatting.setFont(new Font(24));
        vBox2.getChildren().add(StartChatting);

        splitPane.getItems().addAll(vBox1,vBox2);
        borderPane.setCenter(splitPane);
        globalScene = new Scene(borderPane,700,600);
        globalStage.setTitle("Messenger");

        splitPane.setDividerPositions(0.25);




        Button addContact = new Button("+");
        addContact.setLayoutX(10);
        HBox hbox = new HBox(10);
        TextField search = new TextField();
        search.setPromptText("Search");

        // Default Profile picture
        Image defaultProfileImage = null;
        //ImageView profileImageView = null;


        try{
            FileInputStream defaultProfile = new FileInputStream("resources/defaultProfilePic.png");

            defaultProfileImage = new Image(defaultProfile);
            //  profileImageView = new ImageView(defaultProfileImage);
            // profileImageView.setFitWidth(30);
            //  profileImageView.setFitHeight(30);


        }
        catch(IOException i){
            System.out.println(i);
        }
        finalDefaultProfileImage = defaultProfileImage;


        //ScrollPane for Contact
        ScrollPane contactScrollPane = new ScrollPane();
        contactScrollPane.setFitToWidth(true);
        contactScrollPane.setFitToHeight(true);
        VBox.setVgrow(contactScrollPane, Priority.ALWAYS);

        contactPane = new VBox();
        contactScrollPane.setContent(contactPane);
        contactScrollPane.setFitToWidth(true);





        //Scroll pane for Message

        messagePane = new ScrollPane();
        messagePane.setFitToHeight(true);
        messagePane.setFitToWidth(true);
        VBox.setVgrow(messagePane, Priority.ALWAYS);





        //----------------------------------- New User Login-----------------------------------

        BorderPane newUserBorderPane = new BorderPane();
        borderPane.setPrefSize(800, 500);

        AnchorPane newUserAnchorPane = new AnchorPane();
        newUserAnchorPane.setPrefSize(808, 500);
        newUserAnchorPane.setStyle("-fx-background-color: #202020;");

        // Welcome Label
        Label newUserWelcomeLabel = new Label("Welcome");
        newUserWelcomeLabel.setLayoutX(309);
        newUserWelcomeLabel.setLayoutY(102);
        newUserWelcomeLabel.setPrefSize(182, 46);
        newUserWelcomeLabel.setTextFill(Paint.valueOf("#faf4f4"));
        newUserWelcomeLabel.setFont(new Font(26));

        // Username Label
        Label newUsernameLabel = new Label("User Name");
        newUsernameLabel.setLayoutX(250);
        newUsernameLabel.setLayoutY(193);
        newUsernameLabel.setPrefSize(74, 31);
        newUsernameLabel.setTextFill(Color.WHITE);
        newUsernameLabel.setFont(new Font(15));

        // User ID Label
        Label newUserIdLabel = new Label("User ID");
        newUserIdLabel.setLayoutX(250);
        newUserIdLabel.setLayoutY(237);
        newUserIdLabel.setPrefSize(72, 26);
        newUserIdLabel.setTextFill(Color.WHITE);
        newUserIdLabel.setFont(new Font(15));

        // Password Label
        Label newUserPasswordLabel = new Label("Password");
        newUserPasswordLabel.setLayoutX(250);
        newUserPasswordLabel.setLayoutY(276);
        newUserPasswordLabel.setPrefSize(71, 27);
        newUserPasswordLabel.setTextFill(Color.WHITE);
        newUserPasswordLabel.setFont(new Font(16));

        // Username TextField
        TextField newUsernameField = new TextField();
        newUsernameField.setLayoutX(435);
        newUsernameField.setLayoutY(196);
        newUsernameField.setPromptText("Username");

        // User ID TextField
        TextField newUserIdField = new TextField();
        newUserIdField.setLayoutX(436);
        newUserIdField.setLayoutY(238);
        newUserIdField.setPromptText("UserID");

        // PasswordField
        PasswordField newUserPasswordField = new PasswordField();
        newUserPasswordField.setLayoutX(436);
        newUserPasswordField.setLayoutY(277);
        newUserPasswordField.setPromptText("Password");

        // Login Button
        Button newUserLoginButton = new Button("Login");
        newUserLoginButton.setLayoutX(249);
        newUserLoginButton.setLayoutY(340);
        newUserLoginButton.setPrefSize(150, 26);

        // Close Button
        Button newUserCloseButton = new Button("Close");
        newUserCloseButton.setLayoutX(650);
        newUserCloseButton.setLayoutY(446);
        newUserCloseButton.setPrefSize(134, 26);

        // Login Label for errors
        newUserLoginLabel = new Label();
        newUserLoginLabel.setLayoutX(279);
        newUserLoginLabel.setLayoutY(161);
        newUserLoginLabel.setPrefSize(253, 18);
        newUserLoginLabel.setTextFill(Color.web("#fa0808"));
        newUserLoginLabel.setAlignment(Pos.CENTER);

        // Add all nodes to AnchorPane
        newUserAnchorPane.getChildren().addAll(
                newUserWelcomeLabel,
                newUsernameLabel,
                newUserIdLabel,
                newUserPasswordLabel,
                newUsernameField,
                newUserIdField,
                newUserPasswordField,
                newUserLoginButton,
                newUserCloseButton,
                newUserLoginLabel
        );

        newUserBorderPane.setLeft(newUserAnchorPane);

        Scene newUserLoginScene = new Scene(newUserBorderPane);



        //new user login button

        newUserLoginButton.setOnAction((e)->{


            if(newUserIdField.getText().isBlank()||newUsernameField.getText().isBlank()||newUserPasswordField.getText().isBlank())
                newUserLoginLabel.setText("Fill all fields");



            user = new User(newUserIdField.getText());
            user.setListener(this);
            user.newAuthentication(newUsernameField.getText(),newUserIdField.getText(),newUserPasswordField.getText());



     /*

            if()) {
                newUserLoginLabel.setText("Welcome");
                globalStage.setScene(globalScene);
            }

            else

                newUserLoginLabel.setText("Wrong Credentials");
*/
//            controllerOldLogin.loginButtonOnAction(userIDField,passwordField,loginLabel);



        });









        //----------------------------------- Old User Login-----------------------------------
        BorderPane borderPaneLogin = new BorderPane();
        borderPaneLogin.setPrefSize(800, 500);

        AnchorPane anchorPane = new AnchorPane();
        anchorPane.setPrefSize(808, 500);
        anchorPane.setStyle("-fx-background-color: #202020;");

        Label welcomeLabel = new Label("Welcome");
        welcomeLabel.setLayoutX(309);
        welcomeLabel.setLayoutY(102);
        welcomeLabel.setPrefSize(182, 46);
        welcomeLabel.setTextFill(Paint.valueOf("#faf4f4"));
        welcomeLabel.setFont(new Font(26));

        Label userIdLabel = new Label("User ID");
        userIdLabel.setLayoutX(250);
        userIdLabel.setLayoutY(195);
        userIdLabel.setPrefSize(72, 26);
        userIdLabel.setTextFill(Color.WHITE);
        userIdLabel.setFont(new Font(15));

        Label passwordLabel = new Label("Password");
        passwordLabel.setLayoutX(250);
        passwordLabel.setLayoutY(236);
        passwordLabel.setPrefSize(71, 27);
        passwordLabel.setTextFill(Color.WHITE);
        passwordLabel.setFont(new Font(16));

        TextField userIDField = new TextField();
        userIDField.setLayoutX(436);
        userIDField.setLayoutY(196);
        userIDField.setPromptText("UserID");

        PasswordField passwordField = new PasswordField();
        passwordField.setLayoutX(436);
        passwordField.setLayoutY(237);
        passwordField.setPromptText("Password");

        Button loginButton = new Button("Login");
        loginButton.setLayoutX(435);
        loginButton.setLayoutY(287);
        loginButton.setPrefSize(150, 26);

        Label noAccountLabel = new Label("Don't have an account? Create one");
        noAccountLabel.setLayoutX(250);
        noAccountLabel.setLayoutY(336);
        noAccountLabel.setPrefSize(200, 20);
        noAccountLabel.setTextFill(Color.web("#e1d5d5"));

        Button newAccountButton = new Button("New");
        newAccountButton.setLayoutX(462);
        newAccountButton.setLayoutY(333);
        newAccountButton.setPrefSize(122, 26);

        loginLabel = new Label();
        loginLabel.setLayoutX(320);
        loginLabel.setLayoutY(158);
        loginLabel.setPrefSize(169, 18);
        loginLabel.setTextFill(Color.RED);

        anchorPane.getChildren().addAll(
                welcomeLabel,
                userIdLabel,
                passwordLabel,
                userIDField,
                passwordField,
                loginButton,
                noAccountLabel,
                newAccountButton,
                loginLabel
        );

        borderPaneLogin.setCenter(anchorPane);

        oldLoginScene = new Scene(borderPaneLogin,700,600);
        globalStage.setScene(oldLoginScene);
        globalStage.setTitle("Login Form");
        globalStage.show();

        //login button on action

  //      ControllerOldLogin controllerOldLogin = new ControllerOldLogin();


        loginButton.setOnAction((e)->{
            if(userIDField.getText().isBlank()||passwordField.getText().isBlank())
                 loginLabel.setText("Fill all fields");
            user = new User(userIDField.getText());

/*
                loginLabel.setText("Welcome");

                for(int i = 0; i < user.contact.size(); i++){
                    Contacts currentContact = user.contact.get(i);
                    ContactBox(finalDefaultProfileImage, user.contact.get(0).getName(), currentContact);
                }
*/
            user.setListener(this);

            user.oldAuthentication(userIDField.getText(),passwordField.getText());






//            controllerOldLogin.loginButtonOnAction(userIDField,passwordField,loginLabel);

        });

        //New login button on Action


        newAccountButton.setOnAction((e)->globalStage.setScene(newUserLoginScene));







        //new Contact scene
        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        Scene addContactScene  = new Scene(gridPane,700,600);
        Label newContactTitle = new Label("New Contact");
        newContactTitle.setFont(new Font(20));


        //Nodes
        Label newContactName = new Label("Name");
        Label newContactId = new Label("ID");
        TextField newContactNameField = new TextField();
        TextField newContactIdField = new TextField();

        newContactNameField.setPrefWidth(200);
        newContactIdField.setPrefWidth(200);


        Label newContactResult = new Label("");

        Button backToMain = new Button("Back");
        backToMain.setOnAction((e)->globalStage.setScene(globalScene));
        backToMain.setAlignment(Pos.BOTTOM_RIGHT);
        Button add = new Button("Add");
        add.setAlignment(Pos.BOTTOM_CENTER);

        HBox buttonBox = new HBox();
        buttonBox.setPadding(new Insets(10));
        buttonBox.getChildren().addAll(add,backToMain);



       //------------------ sending data to verify contact from server---------------------

        add.setOnAction((e)->{



            tempContactName = newContactNameField.getText();
            tempContactID = newContactIdField.getText();

            user.verifyContact(tempContactID,tempContactName);


        });


        gridPane.add(newContactTitle,0,0,2,1);
        GridPane.setHalignment(newContactTitle, HPos.CENTER);
        gridPane.add(newContactName, 0, 1);
        gridPane.add(newContactNameField, 1, 1);

        gridPane.add(newContactId, 0, 2);
        gridPane.add(newContactIdField, 1, 2);

        gridPane.add(buttonBox, 1, 4);


  //      gridPane.add(add,2,4);
//        gridPane.add(newContactResult,3,0);


        //add button on action

        addContact.setOnAction((e)->{
            newContactResult.setText("");
            newContactIdField.setText("");
            newContactNameField.setText("");

            globalStage.setScene(addContactScene);

        });


        hbox.getChildren().addAll(addContact,search);
        hbox.setPadding(new Insets(10));
        vBox1.getChildren().addAll(hbox,contactPane);


        globalStage.show();



    }


    public HBox ContactBox(Image defaultProfileImage, String name,String ContactId,Contacts contact){
        HBox newContact = new HBox(10);
        Label addname = new Label(name);
        newContact.setStyle("-fx-border-color: black; ");

          ImageView profileImageView = new ImageView(defaultProfileImage);
          profileImageView.setFitWidth(30);
          profileImageView.setFitHeight(30);
        
        newContact.getChildren().addAll(profileImageView,addname);
        newContact.setPrefWidth(30);
        newContact.setPrefHeight(50);
        contactPane.getChildren().addAll(newContact);


        ContextMenu contextMenu = new ContextMenu();
        MenuItem deleteOption = new MenuItem("Delete");
        MenuItem modifyContect = new MenuItem("Modify Contect");

        deleteOption.setOnAction((e)->{
            contactPane.getChildren().removeAll(newContact);
            vBox2.getChildren().clear();
            Label StartChatting = new Label("Start New Chat");
            StartChatting.setFont(new Font(24));
            vBox2.getChildren().add(StartChatting);

        });

        modifyContect.setOnAction((e)->{
            TextInputDialog dialog = new TextInputDialog(); 
            dialog.setTitle("Edit Contact");
            dialog.setHeaderText("Rename Contact");
            dialog.setContentText("Enter new name:");

            Optional<String> result = dialog.showAndWait();

            result.ifPresent(newName ->{
                addname.setText(newName);
            });
        });


        contextMenu.getItems().addAll(deleteOption,modifyContect);


        newContact.setOnContextMenuRequested(event->{
            contextMenu.show(newContact, event.getScreenX(), event.getScreenY());

        });


        newContact.setOnMouseClicked((e)->{
            openChat(messagePane,vBox2,name,contact);

            for(Message temp : messageArrayList){

                System.out.println(messageArrayList.size());

                    if(temp.getReceiverId().equals(ContactId) && temp.getSenderId().equals(user.getUserid())){
                        addMyMessage(temp.getMsg(),messageBox);
                    }

                    else if(temp.getSenderId().equals(ContactId) && temp.getReceiverId().equals(user.getUserid())){

                        Label label = new Label(temp.getMsg());
                        // Gray color for received messages, White text
                        label.setStyle("-fx-background-color: #EAEAEA; -fx-text-fill: black; -fx-padding: 10; -fx-background-radius: 10;");
                        label.setWrapText(true);
                        label.setMaxWidth(250); // Prevent it from being too wide

                        HBox box = new HBox(label);
                        box.setAlignment(Pos.CENTER_LEFT);
                        box.setPadding(new Insets(5));

                        messageBox.getChildren().add(box);

                    }
                    else{

                    }
                System.out.println(temp.getReceiverId()+","+ContactId+","+temp.getSenderId()+","+user.getUserid());
                System.out.println(temp.getSenderId()+","+ContactId+","+temp.getReceiverId()+","+user.getUserid());

            }


        });


        return newContact;

    }

    public void openChat(ScrollPane messagePane,VBox vBox2,String name,Contacts contact ){
        vBox2.getChildren().clear();
        TextField writeMessage = new TextField();
        //writeMessage.setPrefWidth(100);
        writeMessage.setPrefSize(300,20);
        currentActiveChatID = contact.getId();
        Button picSend = new Button("Pic");


        messageBox = new VBox();
       // VBox.setVgrow(messageBox,Priority.ALWAYS);
        messagePane.setContent(messageBox);
        messagePane.setFitToWidth(true);
        Label chatTitle = new Label(name);
        chatTitle.setAlignment(Pos.TOP_RIGHT);

        HBox chatTitleBox = new HBox(10);
        chatTitleBox.getChildren().addAll(chatTitle);
        chatTitleBox.setAlignment(Pos.TOP_CENTER);




        Button send = new Button(">");
        HBox hbox2 = new HBox(10);
        hbox2.getChildren().addAll(picSend,writeMessage,send);
        hbox2.setAlignment(Pos.BOTTOM_CENTER);
        hbox2.setPadding(new Insets(10));

        vBox2.getChildren().addAll(chatTitleBox,messagePane,hbox2);


        PrivateChatRoom privateChatRoom = new PrivateChatRoom();

        send.setOnAction((e)->{
            if(writeMessage.getText().isBlank()){
                System.out.println("no text detected");
            }
            else{
                //privateChatRoom.addMembers(user.getUserid());
                //privateChatRoom.addMembers(contact.getId());
                messageArrayList.add(new Message(user.getUserid(),contact.getId(), writeMessage.getText()));

                //Message message = new Message(user.getUserid(),contact.getId(), writeMessage.getText());

                user.sendMessage(contact.getId(),writeMessage.getText());
              //  privateChatRoom.addMessage(message);
                addMyMessage(writeMessage.getText(),messageBox );
                writeMessage.clear();

            }


        });

        picSend.setOnAction((e)->{
            FileChooser fileChooser = new FileChooser();
            File file = fileChooser.showOpenDialog(globalStage);
            String filePath = file.getAbsolutePath();

            if (file != null) {
                System.out.println("File path: " + file.getAbsolutePath());

                user.sendPic(filePath,contact.getId());

            }

        });

    }
    public void addMyMessage(String text,VBox messageBox) {


        Label label = new Label(text);
        label.setStyle("-fx-background-color: lightblue; -fx-padding: 10; -fx-background-radius: 10;");
        label.setWrapText(true); // Allow long text to wrap

        HBox box = new HBox(label);
        box.setAlignment(Pos.CENTER_RIGHT);

        messageBox.getChildren().add(box);


    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void onMessageReceived(String rawMessage) {
        // Must run on JavaFX Application Thread
        Platform.runLater(() -> {


            if (rawMessage.startsWith("VERIFY_RESULT:")) {
                String result = rawMessage.split(":")[1];
                if ("true".equals(result)) {
                    System.out.println("Contact Verified and Added!");

                    contactList.add(new Contacts(tempContactName, tempContactID));
                    Contacts newContact = new Contacts(tempContactName, tempContactID);

                    // Add to List
                    contactList.add(newContact);

                    // Add to UI (Using the global image)
                    ContactBox(finalDefaultProfileImage, tempContactName,tempContactID, newContact);

                    // Switch Scene back to Main
                    globalStage.setScene(globalScene);



                }

                else {
                System.out.println("Contact ID not found.");
                }
            return;
            }
            else if (rawMessage.startsWith("VERIFY_AUTHEN:")) {
                String[] result = rawMessage.split(":");

                if(result[1].equals("true")){
                    globalStage.setScene(globalScene);

                }
                else {
                    loginLabel.setText("Wrong Credentials");
                }


            }
            /*
            else if (rawMessage.startsWith("VERIFY_AUTHEN_CONTACT:")) {



                String[] temp = rawMessage.split(",");

                String contact_id;
                String contact_name;

                for(int i = 0; i < temp.length; i++){
                    String[] temp2 = temp[i].split(":",2);
                    contact_id = temp2[0].trim();
                    contact_name = temp2[1].trim();
                    Contacts newcontact = new Contacts(contact_name,contact_id);
                    user.contact.add(newcontact);
                }

                //Adding contact to main scene
                for(int i = 0; i < user.contact.size(); i++){
                    Contacts currentContact = user.contact.get(i);
                    ContactBox(finalDefaultProfileImage, user.contact.get(0).getName(), currentContact);
                }


            }
            */


            else if (rawMessage.startsWith("REGULAR:")) {

                try {


                    String[] parts = rawMessage.split(":", 3);

                    if (parts.length < 3) return; // Safety check

                    String senderID = parts[1].trim();
                    String content = parts[2];

                    messageArrayList.add(new Message(senderID,user.getUserid(),content));

                    if (currentActiveChatID != null && currentActiveChatID.equals(senderID)) {

                        Label label = new Label(content);
                        // Gray color for received messages, White text
                        label.setStyle("-fx-background-color: #EAEAEA; -fx-text-fill: black; -fx-padding: 10; -fx-background-radius: 10;");
                        label.setWrapText(true);
                        label.setMaxWidth(250); // Prevent it from being too wide

                        HBox box = new HBox(label);
                        box.setAlignment(Pos.CENTER_LEFT);
                        box.setPadding(new Insets(5));

                        messageBox.getChildren().add(box);



                    } else {

                        System.out.println("Notification: Message from " + senderID);

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }



            }
            else if (rawMessage.startsWith("LOGIN_RESULT")) {
                user.client.stopListening();
                String[] result = rawMessage.split(":",2);

                if("true".equals(result[1])){
                    globalStage.setScene(oldLoginScene);

                }
                else{
                    newUserLoginLabel.setText("Username Exists");
                }




            }
            else if(rawMessage.startsWith("PIC")){


                String[] result = rawMessage.split(":");
                String picInString = result[1];

                LocalDateTime now = LocalDateTime.now();

                // 2. Define the desired format
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH-mm-ss");
                String fileName = now.format(formatter) + ".jpeg";



                byte[] imageInByte = Base64.getDecoder().decode(picInString);

                try {

                    FileOutputStream fileOutputStream = new FileOutputStream(fileName);
                    fileOutputStream.write(imageInByte);
                    fileOutputStream.close();
                }
                catch(IOException e){
                    System.out.println(e);
                }

            }

            else {

            }
        });
    }
}
