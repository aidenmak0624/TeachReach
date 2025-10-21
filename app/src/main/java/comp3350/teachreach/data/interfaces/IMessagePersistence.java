package comp3350.teachreach.data.interfaces;

import java.util.List;
import java.util.Map;

import comp3350.teachreach.objects.interfaces.IMessage;

public interface IMessagePersistence {
    int createGroup(int studentID, int tutorID);

    int storeMessage(int groupID, int senderAccountID, String message);

    int searchGroupByIDs(int studentID, int tutorID);

    Map<String, Integer> searchIDsByGroupID(int groupID);

    List<IMessage> retrieveAllMessageByGroupID(int groupID);

    List<Integer> retrieveAllGroupsByStudentID(int studentID);

    List<Integer> retrieveAllGroupsByTutorID(int tutorID);
    List<Integer> retrieveAllTutorIDsByStudentID(int studentID);
    List<Integer> retrieveAllStudentIDsByTutorID(int tutorID);

}
