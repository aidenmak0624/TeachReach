package comp3350.teachreach.logic.DAOs;

import java.util.List;
import java.util.Map;

import comp3350.teachreach.application.Server;
import comp3350.teachreach.data.interfaces.IMessagePersistence;
import comp3350.teachreach.logic.exceptions.DataAccessException;
import comp3350.teachreach.objects.interfaces.IMessage;

public class AccessMessages {

    private static IMessagePersistence messagePersistence;

    public AccessMessages() {
        AccessMessages.messagePersistence = Server.getMessageDataAccess();
    }

    public AccessMessages(IMessagePersistence messageDataAccess) {
        AccessMessages.messagePersistence = messageDataAccess;

    }

    public int createGroup(int studentID, int tutorID){
        try {
            return messagePersistence.createGroup(studentID, tutorID);
        } catch (final Exception e) {
            throw new DataAccessException("Group might've already exist!", e);
        }
    }

    public int storeMessage(int groupID, int senderAccountID, String message){
        try{
            return messagePersistence.storeMessage(groupID,senderAccountID,message);
        }
        catch (final Exception e) {
            throw new DataAccessException("Exception on storing Message!", e);
        }
    }
    public int searchGroupByIDs(int studentAccountID, int tutorAccountID){
            return messagePersistence.searchGroupByIDs(studentAccountID,  tutorAccountID);
    }

    public  Map<String, Integer> searchIDsByGroupID(int groupID){
        try{
            return messagePersistence.searchIDsByGroupID(groupID);
        }
        catch (final Exception e) {
            throw new DataAccessException("Exception on searching IDs By GroupID!", e);
        }
    }


    public List<IMessage> retrieveAllMessageByGroupID(int groupID){
        try{
            return messagePersistence.retrieveAllMessageByGroupID(groupID);
        }
        catch (final Exception e) {
            throw new DataAccessException("Exception on searching Group By IDs!", e);
        }
    }

    public List<Integer> retrieveAllGroupsByTutorID(int tutorID){
        try{
            return messagePersistence.retrieveAllGroupsByTutorID(tutorID);
        }
        catch (final Exception e) {
            throw new DataAccessException("Exception on searching Group By tutor IDs!", e);
        }
    }

    public List<Integer> retrieveAllGroupsByStudentID(int studentID){
        try{
            return messagePersistence.retrieveAllGroupsByStudentID(studentID);
        }
        catch (final Exception e) {
            throw new DataAccessException("Exception on searching Group By student ID!", e);
        }
    }

    public List<Integer> retrieveAllTutorIDsByStudentID(int studentID){
        try{
            return messagePersistence.retrieveAllTutorIDsByStudentID(studentID);
        }
        catch (final Exception e) {
            throw new DataAccessException("Exception on searching Tutors By student ID!", e);
        }
    }


    public List<Integer> retrieveAllStudentIDsByTutorID(int tutorID){
        try{
            return messagePersistence.retrieveAllStudentIDsByTutorID(tutorID);
        }
        catch (final Exception e) {
            throw new DataAccessException("Exception on searching Students By student ID!", e);
        }
    }

}
