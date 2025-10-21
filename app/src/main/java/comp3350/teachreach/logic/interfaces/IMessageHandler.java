package comp3350.teachreach.logic.interfaces;

import androidx.annotation.NonNull;

import java.sql.Timestamp;
import java.util.List;

import comp3350.teachreach.objects.interfaces.IAccount;
import comp3350.teachreach.objects.interfaces.IMessage;

public interface IMessageHandler
{
    int createGroup(int studentID, int tutorID) throws Exception;

    int storeMessage(int groupID, int senderAccountID, String message) throws Exception;

    int searchGroupByIDs(int studentID, int tutorID);

    List<IMessage> retrieveAllMessageByGroupID(int groupID);

    List<Integer> retrieveAllGroupsByTutorID(int tutorID);

    List<Integer> retrieveAllGroupsByStudentID(int studentID);

    List<IAccount> retrieveAllChatAccountsByAccountID(int accountID);

    String timeStampConverter(@NonNull Timestamp timestamp);
}
