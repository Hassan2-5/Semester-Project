package Project;



public class GroupChatRoom extends ChatRoom{

    public GroupChatRoom(){
        super();
    }

    public void addMembers(String newParticipant ){
        if(contacts.contains(newParticipant)){
            System.out.println("Already present in chat");

        }



    }

    @Override
    public void addMessage(Message message){
        messages.add(message);
    }


}
