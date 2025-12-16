package Project;

public class PrivateChatRoom extends ChatRoom{

    public PrivateChatRoom(){
        super();
    }


    public void addMembers(String newParticipant ){

        if(contacts.size() >= 2){
            System.out.println("Private chat can't have more than two participants");
            return;
        }
        if(contacts.contains(newParticipant)){
            System.out.println("Already present in chat");

        }



    }

    @Override
    public void addMessage(Message message){
        messages.add(message);
    }


}
