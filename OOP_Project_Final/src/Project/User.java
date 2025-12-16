package Project;

import java.util.ArrayList;
import java.util.Scanner;

public class User {

    String userName;
    MessageType mt;
    private String userid;
    String password;
    ArrayList<Contacts> contact = new ArrayList<>();
    Scanner sc = new Scanner(System.in);
    int port = 5001;
    String ip = "LocalHost";
    Client client;





    public User(String userid){
        client = new Client(ip,port);
        this.userid = userid;
/*
		this.userName = userName;
		this.userid = userid;
		this.password = password;
	
		boolean b = false;
		while(b == false){

			b = authentication(userName, password);
			if(b == true)
				System.out.println("Welcome "+userName);

			else{
				System.out.println("Try Again");

			}

		}


		char choice = 'y';
		while(choice == 'y'){

			mainMenu();	

			System.out.println("Do you want to add another contact?(y/n)");
			choice = sc.next().charAt(0);


		}



*/

    }

    public String getUserid() {
        return userid;
    }

    //login authentication
    public void oldAuthentication(String user_id,String password){
        String contact_id;
        String contact_name;

         client.authenticate(user_id,password);

         /*
        String contactList = client.contactList;
        String[] temp = contactList.split(",");

        for(int i = 0; i < temp.length; i++){
            String[] temp2 = temp[i].split(":",2);
            contact_id = temp2[0].trim();
            contact_name = temp2[1].trim();
            Contacts newcontact = new Contacts(contact_name,contact_id);
            contact.add(newcontact);
        }
*/

    }

    public void newAuthentication(String username,String user_id,String password){
        System.out.println(user_id+username+password);
        client.newLogin(username,user_id,password);


    }
    //Verify for contacts

    public void verifyContact(String contactID,String contact_name){

        client.verifyContact(contactID,contact_name);


    }


    public void setListener(MessageListener listener) {
        client.setMessageListener(listener);
    }

    public void sendPic(String picAddress, String receiverId){
            client.sendPic(picAddress,receiverId);

    }




//senging messages

    public void sendMessage(String receiver,String msg){
        client.sendMessage(receiver,msg);
    }


/*

//Search Method

    public Contacts[] Search(){
        System.out.println("Search here: ");
        String searchWord = sc.next();
        int counter = 0;

        for(int i = 0; i < contact.length; i++){
            if(contact[i].name.equals(searchWord)){
                counter++;
            }

        }

        if(counter == 0){
            System.out.println("No match found!");
            return null;
        }

        Contacts[] updatedContact = new Contacts[counter];
        int newCounter = 0;

        for(int i = 0; i < contact.length; i++){
            if(contact[i].equals(searchWord)){
                updatedContact[newCounter] = contact[i];
                newCounter++;
            }

        }

        return updatedContact;

    }

//Add function

    public String add(){
        System.out.print("Enter Name:");
        String addName = sc.nextLine();
        System.out.println();
        System.out.print("Enter User Id:");
        String addId  = sc.next();
        System.out.println();

        for(int i = 0; i < contact.length; i++){
            if(contact[i] == null){
                contact[i] = new Contacts(addName,addId);
                break;
            }
        }

        return "Contact has been saved!";

    }

//Remove Method
	/*
	public String Remove(){
		System.out.print("Enter Id: ");
		String givenId = sc.next();

		for(int i = 0; i < contact.length; i++)
			if(contact[i].id.equals(givenId))
				

	}
	
	*/

// main panel
/*
    public void mainMenu(){
        System.out.println("1) Add Contact");
        System.out.println("2) Show Contacts");
        System.out.println("3) Exit");
        int choice = sc.nextInt();
        sc.nextLine();


        if(choice == 1){
            add();

        }



        else if(choice == 2){

            int num = 1;
            for(int i = 0; i < contact.length; i++){
                if(contact[i] == null)
                    continue;

                System.out.println(num +") "+contact[i].name);
                num++;

            }

        }


    }

    */
}