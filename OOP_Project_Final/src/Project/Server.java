package Project;

import java.io.*;
import java.net.*;
import java.sql.*;
import java.util.*;

public class Server {

    ServerSocket server = null;


    public Server(int port) {

        try {

            server = new ServerSocket(port);
            server.setReuseAddress(true);

            while (true) {

                Socket client = server.accept();



                System.out.println("New client connected: " + client.getInetAddress().getHostAddress());

                ClientHandler clientSock = new ClientHandler(client);
                new Thread(clientSock).start();
            }

        }
        catch (IOException e) {
            System.out.println("Server Error: " + e);
        }

    }

    private static class ClientHandler implements Runnable {

        private final Socket clientSocket;
        private String userid = null;

        private DataOutputStream out;
        private BufferedReader in;

        private static final HashMap<String, DataOutputStream> map = new HashMap<>();

        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
        }

        @Override
        public void run() {



            try {
                out = new DataOutputStream(clientSocket.getOutputStream());
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
/*
                // First message from client = user ID
                userid = in.readLine();
                System.out.println("User Added: " + userid);

                synchronized (map) {
                    map.put(userid, out);
                }
*/
                while(true) {
                    System.out.println("IN server");

                    String messageType;
                    String restMessage;
                    String line = in.readLine();

                  //  System.out.println(line);
                    if(line == null) break;
                    String[] type = line.split(":", 2);
                    messageType = type[0].trim();
                    System.out.println(messageType);
                    restMessage = type[1].trim();

                    switch (messageType) {

                        case "AUTHENTICATE":
                            String result;



                            String[] piece = restMessage.split(",", 2);

                            result = authentication(piece[0], piece[1]);
                            out.writeBytes("VERIFY_AUTHEN:"+result + "\n");
                            System.out.println("verify authen sended");
                            out.flush();
/*
                            ArrayList<String> contactList = getContactsFromDatabase(piece[0]);
                            String contactListString = String.join(",",contactList);
                            out.writeBytes("VERIFY_AUTHEN_CONTACT:"+contactListString+"\n");
                            out.flush();
*/

                            if("true".equals(result)){
                                userid = piece[0].trim();
                                synchronized (map) {
                                    map.put(this.userid, out);
                                    System.out.println(map.get(userid));
                                }
                            }
                            else{
                                System.out.println("Bad DATA");
                            }

                            break;


                        case "REGULAR":
                            // Split receiver and message
                            String[] parts = restMessage.split(":", 2);

                            if (parts.length < 2) continue;
                            System.out.println("Rest message-----"+restMessage);
                            String receiverID = parts[0];
                            String msg = parts[1];

                            DataOutputStream receiverOut;

                            synchronized (map) {
                                receiverOut = map.get(receiverID);
                                System.out.println(receiverOut);

                            }

                            if (receiverOut != null) {
                                receiverOut.writeBytes("REGULAR"+":"+userid + ":" + msg + "\n");
                                receiverOut.flush();
                            } else {
                                System.out.println("Receiver " + receiverID + " is not connected.");
                            }
                            //System.out.println(receiverID + ": " + msg);
                            break;

                        case "LOGIN":
                            String[] half = restMessage.split(",", 3);
                            String login_result = newAuthentication(half[0], half[1], half[2]);




                            out.writeBytes("LOGIN_RESULT:"+login_result + "\n");
                            out.flush();
                            break;



                        case "VERIFY_CONTACT":
                            String[] broke = restMessage.split(":", 2);


                            out.writeBytes("VERIFY_RESULT:"+checkForContact(broke[0])+"\n");
                            addContactToDataBase(userid,broke[0],broke[1]);
                            break;

                        case "PIC":

                            String[] picMessage = restMessage.split(":");
                            String ReceiverID = picMessage[0];
                            System.out.println(ReceiverID+"in Pic section");
                            String imageString = picMessage[1];

                            DataOutputStream ReceiverOut = map.get(ReceiverID);

                            ReceiverOut.writeBytes("PIC:"+imageString+"\n");

                            break;

                        default:
                            System.out.println("no type");


                    }
                }



                //Checking for authentication





            }
            catch (IOException e) {
                System.out.println(e);

            }




            finally {
                System.out.println("User Disconnected: " + userid);

                synchronized (map) {

                    if (userid != null)
                        map.remove(userid);

                }
                    try{
                        if (out != null) out.close();
                        if (in != null) in.close();

                        clientSocket.close();

                    }
                    catch(IOException i){
                        System.out.println(i);
                    }



            }
        }
    }
    public static String checkForContact(String contact_id){
        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getConnection();

        String verifyLogin = "Select count(1) from accounts WHERE user_id = '" + contact_id+"' ";


        try {
            Statement statement = connectDB.createStatement();
            ResultSet queryResult = statement.executeQuery(verifyLogin);

            if (queryResult.next()){
                if(queryResult.getInt(1) == 1){

                    return "true";
                    //newContactLabel.setText("Contact has been added");
                    //ContactBox(defaultProfileImage,name,contactPane);

                }
                else{

                    return "false";
                    //Insert query
                    //newContactLabel.setText("No such ID exist");

                }

            }
        }
        catch(SQLIntegrityConstraintViolationException e){
            return "taken";

            //newContactLabel.setText("User ID is already taken.");

        }
        catch(SQLException e){
            System.out.println(e);
        }

        return "false";
    }


    public static String authentication(String user_id, String password){
        System.out.println("in authentication");
        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getConnection();

        System.out.println("going to send query ");
        String verifyLogin = "Select count(1) from accounts WHERE user_id = '"+user_id+"' and password = '"+password+"'";
        System.out.println("Sended query");

        try {
            Statement statement = connectDB.createStatement();
            ResultSet queryResult = statement.executeQuery(verifyLogin);


            if(queryResult.next()) {
                if (queryResult.getInt(1) == 1) {


                    return "true";

                } else {
                    //Insert query
                    return "false";
                    //LoginLabel.setText("Wrong Credentials");
                    /*
                    String insertEntry = "INSERT INTO accounts(user_id,user_name,password) VALUES(?,?,?)";


                    PreparedStatement ps = connectDB.prepareStatement(insertEntry);
                    ps.setString(1,UserIDField.getText());
                    ps.setString(2,UsernameField.getText());
                    ps.setString(3,PasswordField.getText());

                    ps.executeUpdate();
                    LoginLabel.setText("Welcome");
                    */
                }

            }


            } catch(SQLIntegrityConstraintViolationException e) {
                //LoginLabel.setText("User ID is already taken.");

            } catch(SQLException e) {
                System.out.println(e);
            }
        return "false";

    }

    public static String newAuthentication(String username,String user_id,String password){

        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getConnection();

        System.out.println("New Authentication"+username+user_id+password);
        String verifyLogin = "Select count(1) from accounts WHERE user_id = '"+user_id+"' ";


        try {
            Statement statement = connectDB.createStatement();
            ResultSet queryResult = statement.executeQuery(verifyLogin);

            if(queryResult.next()){
                if(queryResult.getInt(1) >= 1){

                    //loginLabel.setText("User ID is already taken.");
                    return "false";
                }
                else{
                    //Insert query

                    String insertEntry = "INSERT INTO accounts(user_id,user_name,password) VALUES(?,?,?)";


                    PreparedStatement ps = connectDB.prepareStatement(insertEntry);
                    ps.setString(1,user_id);
                    ps.setString(2,username);
                    ps.setString(3,password);

                    ps.executeUpdate();
                  //  loginLabel.setText("Welcome");
                    return "true";
                }

            }
        }
        catch(SQLIntegrityConstraintViolationException e){
            //loginLabel.setText("User ID is already taken.");

        }
        catch(SQLException e){
            System.out.println(e);
        }

        return "false";
    }
    public static void addContactToDataBase(String user_id,String contact_id,String contact_name){
        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getConnection();

       // String query = "INSERT INTO contacts (user_id, contact_id) VALUES ('" + user + "', '" + contact + "')";

        try {


            String insertEntry = "INSERT INTO contacts(user_id,contact_id,contact_name) VALUES(?,?,?)";


            PreparedStatement ps = connectDB.prepareStatement(insertEntry);
            ps.setString(1,user_id);
            ps.setString(2,contact_id);
            ps.setString(3,contact_name);

            ps.executeUpdate();
            //LoginLabel.setText("Welcome");



        } catch(SQLIntegrityConstraintViolationException e) {
            //LoginLabel.setText("User ID is already taken.");

        } catch(SQLException e) {
            System.out.println(e);
        }


    }


     public static ArrayList<String> getContactsFromDatabase(String user_id) {

        ArrayList<String> contacts = new ArrayList<>();

        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getConnection();

         String query = "SELECT contact_id, contact_name FROM contacts WHERE user_id = ?";

        try {
            PreparedStatement ps = connectDB.prepareStatement(query);
            ps.setString(1, user_id);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String id = rs.getString("contact_id");
                String name = rs.getString("contact_name");

                // Format: id:name
                contacts.add(id + ":" + name);             }

        } catch (SQLException e) {
            System.out.println("Error loading contacts: " + e.getMessage());
        }

        return contacts;
    }



}
