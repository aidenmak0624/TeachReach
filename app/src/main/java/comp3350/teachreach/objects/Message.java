package comp3350.teachreach.objects;

import java.time.Instant;
import java.sql.Timestamp;

public class Message implements comp3350.teachreach.objects.interfaces.IMessage {

    private int senderID;

    private Timestamp time;

    private String message;

    public Message(int senderAccountID,
                   Timestamp time,
                   String message) {
        this.senderID = senderAccountID;
        this.time = time;
        this.message = message;
    }

    @Override
    public int getSenderID() {
        return senderID;
    }


    @Override
    public Timestamp getTime() {
        return time;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public void setSenderID(int senderID) {
        this.senderID = senderID;
    }

    @Override
    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public void setTime(Timestamp time) {
        this.time = time;
    }
}


