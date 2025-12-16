package Project;

import java.time.LocalDateTime;
import java.util.UUID;
public class Message {
    private  final String messageId;
    private String senderId;
    private String receiverId;
    private LocalDateTime timpestamp;
    private String msg;




    public Message(String senderId, String receiverId, String text){
        this.senderId = senderId;
        this.receiverId = receiverId;

        this.msg = text;


        this.messageId = UUID.randomUUID().toString();

    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public LocalDateTime getTimpestamp() {
        return timpestamp;
    }

    public void setTimpestamp(LocalDateTime timpestamp) {
        this.timpestamp = timpestamp;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }



    public String toString(){
        return getSenderId()+":"+getMsg();
    }

}
