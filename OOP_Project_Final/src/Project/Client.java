package Project;

import javafx.application.Platform;

import java.io.*;
import java.net.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;

public class Client {

    Socket s = null;
    DataOutputStream dos = null;
    BufferedReader br = null;



    String from;
    String result;
    String to;
    String contactList;
    boolean isAuthenticated = false;
    private MessageListener messageListener;
    private ReceiveThread receiveThread;

    private boolean isListening = false;



    public Client(String ip, int port) {

        try {
            s = new Socket(ip, port);

            dos = new DataOutputStream(s.getOutputStream());
            br = new BufferedReader(new InputStreamReader(s.getInputStream()));

            //startListening();
        }
        catch (IOException i) {
            System.out.println(i);
        }
    }

    public void stopListening() {
        if (isListening && receiveThread != null) {
            receiveThread.interrupt();  // This will break out of readLine() safely
            receiveThread = null;
            isListening = false;
            System.out.println("Listening thread stopped.");
        }
    }


    public void authenticate(String user_id, String password) {
        String authenticate = "";

        try {
            // 1. Format the string
            authenticate = "AUTHENTICATE" + ":" + user_id + "," + password;

            dos.writeBytes(authenticate + "\n");
            dos.flush();
           // contactList = br.readLine();
            isAuthenticated = true;
           startListening();
            from = user_id;



        } catch (IOException e) {
            System.out.println("Authentication Error: " + e.getMessage());

        }
    }

    public void newLogin(String user_name, String user_id, String password){
        String login="";

        if (dos == null || s.isClosed()) {
            System.out.println("Error: Not connected to server. Cannot send Login.");
            return;
        }


        try {
            login = String.format("LOGIN:"+user_name+","+user_id+","+password);

            startListening();
            dos.writeBytes(login+"\n");
            dos.flush();



        //    new ReceiveThread().start();


        }
        catch(IOException e){
            System.out.println(e);
        }

    }

    public void setMessageListener(MessageListener listener) {
        this.messageListener = listener;
    }

    private void startListening() {
        if (!isListening) {
            new ReceiveThread().start();
            isListening = true;
            System.out.println("Listening thread started.");
        }
    }






    //Subclass for receiving thread

    public class ReceiveThread extends Thread {
        public void run() {
            try {
                String incoming;

                while ((incoming = br.readLine()) != null) {

                    String msg = incoming;

                    System.out.println("msg received by client");

                      if(messageListener != null){
                        Platform.runLater(() ->{
                            messageListener.onMessageReceived(msg);
                        });
                    }

                    System.out.println(incoming);
                }

            }
            catch (IOException e) {
                System.out.println(e);
            }
        }
    }



    public void cleanup(){
        try {
            s.close();
            br.close();
            dos.flush();
            dos.close();
        }
        catch(IOException e){
            System.out.println(e);
        }
    }


    public void sendPic(String picAddress,String receiverId){

        try {

            byte[] imageData = Files.readAllBytes(Paths.get(picAddress));

            String imageString = Base64.getEncoder().encodeToString(imageData);



            dos.writeBytes("PIC:"+receiverId+":"+imageString+"\n");
            dos.flush();

        }
        catch (IOException e){
            System.out.println(e);
        }


    }




    public void verifyContact(String contactID, String contact_name){

        String verifyStatement;
        try {

            verifyStatement = String.format("VERIFY_CONTACT"+":"+contactID+":"+contact_name);
            dos.writeBytes(verifyStatement+"\n");


        }
        catch(IOException e){
            System.out.println(e);
        }


    }

    public void sendMessage(String receiver,String msg){

        try {

            String outgoing;


                if(s.isClosed())
                    return;

                dos.writeBytes("REGULAR"+":"+receiver + ":" + msg + "\n");
                dos.flush();


        }
        catch (IOException i) {
            System.out.println(i);
        }



    }

}
