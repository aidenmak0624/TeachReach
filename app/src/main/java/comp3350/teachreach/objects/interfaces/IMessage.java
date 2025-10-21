package comp3350.teachreach.objects.interfaces;

import java.sql.Timestamp;

public interface IMessage {
    int getSenderID();

    Timestamp getTime();

    String getMessage();

    void setSenderID(int senderID);

    void setMessage(String message);

    void setTime(Timestamp time);
}
