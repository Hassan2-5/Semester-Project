package Project;

import java.util.ArrayList;
import java.util.UUID;

public abstract class ChatRoom {

   // private final String chatId;
    ArrayList<String> contacts = new ArrayList<>();
    ArrayList<Message> messages = new ArrayList<>();

    public ChatRoom(){
        //this.chatId = UUID.randomUUID().toString();

    }
/*
    public String getChatId() {
        return chatId;
    }
*/
    public abstract void addMessage(Message message);

    public abstract void addMembers(String newParticipant);


}
