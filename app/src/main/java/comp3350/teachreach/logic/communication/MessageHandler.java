package comp3350.teachreach.logic.communication;

import androidx.annotation.NonNull;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import comp3350.teachreach.logic.DAOs.AccessAccounts;
import comp3350.teachreach.logic.DAOs.AccessMessages;
import comp3350.teachreach.logic.DAOs.AccessStudents;
import comp3350.teachreach.logic.DAOs.AccessTutors;
import comp3350.teachreach.logic.exceptions.DataAccessException;
import comp3350.teachreach.logic.exceptions.MessageHandleException;
import comp3350.teachreach.logic.interfaces.IMessageHandler;
import comp3350.teachreach.objects.interfaces.IAccount;
import comp3350.teachreach.objects.interfaces.IMessage;
import comp3350.teachreach.objects.interfaces.IStudent;
import comp3350.teachreach.objects.interfaces.ITutor;

public class MessageHandler implements IMessageHandler {
    private final AccessMessages accessMessages;

    private final AccessStudents accessStudents;

    private final AccessTutors accessTutors;

    private AccessAccounts accessAccounts = null;

    public MessageHandler() {
        accessMessages = new AccessMessages();
        accessStudents = new AccessStudents();
        accessTutors = new AccessTutors();
        accessAccounts = new AccessAccounts();

    }

    public MessageHandler(AccessMessages accessMessages, AccessStudents accessStudents, AccessTutors accessTutors) {
        this.accessMessages = accessMessages;
        this.accessStudents = accessStudents;
        this.accessTutors = accessTutors;

    }

    public MessageHandler(AccessMessages accessMessages, AccessStudents accessStudents, AccessTutors accessTutors, AccessAccounts accessAccounts) {
        this.accessMessages = accessMessages;
        this.accessStudents = accessStudents;
        this.accessTutors = accessTutors;
        this.accessAccounts = accessAccounts;

    }

    private boolean validateSentMessage(int groupID, int senderAccountID, String message) {
        boolean valid = true;
        boolean account = false;
        int findStudentID = 0;
        int findTutorID = 0;

        Map<String, Integer> resultIDs;
        resultIDs = accessMessages.searchIDsByGroupID(groupID);

        try {
            IStudent findStudent = accessStudents.getStudentByAccountID(senderAccountID);
            findStudentID = findStudent.getStudentID();
            if (findStudentID > 0)
                account = true;
        } catch (DataAccessException ignored) {
        }

        try {
            ITutor findTutor = accessTutors.getTutorByAccountID(senderAccountID);
            findTutorID = findTutor.getTutorID();
            if (findTutorID > 0)
                account = true;
        } catch (DataAccessException ignored) {
        }


        if (Objects.requireNonNull(resultIDs.get("StudentID")) != findStudentID && Objects.requireNonNull(resultIDs.get("TutorID")) != findTutorID) {
            account = false;
        }
        if (message == null) {
            valid = false;
        }
        return account && valid;
    }


    private boolean checkExistGroup(int studentID, int tutorID) {
        return (accessMessages.searchGroupByIDs(studentID, tutorID) > 0);
    }


    @Override
    public int createGroup(int studentID, int tutorID) {

        return !checkExistGroup(studentID, tutorID) ? accessMessages.createGroup(studentID, tutorID) : -1;
    }

    @Override
    public int storeMessage(int groupID, int senderAccountID, String message) throws MessageHandleException {
        try {
            if (validateSentMessage(groupID, senderAccountID, message)) {
                return accessMessages.storeMessage(groupID, senderAccountID, message);
            }
        } catch (final Exception e) {
            throw new MessageHandleException("Invalid Message.", e);
        }
        return -1;
    }

    @Override
    public int searchGroupByIDs(int studentID, int tutorID) {
        return accessMessages.searchGroupByIDs(studentID, tutorID);
    }

    @Override
    public List<IMessage> retrieveAllMessageByGroupID(int groupID) {
        return accessMessages.retrieveAllMessageByGroupID(groupID);
    }

    @Override
    public String timeStampConverter(@NonNull Timestamp timestamp) {
        LocalDateTime localDateTime = LocalDateTime.ofInstant(timestamp.toInstant(), ZoneId.systemDefault());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return localDateTime.format(formatter);
    }

    @Override
    public List<Integer> retrieveAllGroupsByTutorID(int tutorID) {
        return accessMessages.retrieveAllGroupsByTutorID(tutorID);
    }

    @Override
    public List<Integer> retrieveAllGroupsByStudentID(int studentID) {
        return accessMessages.retrieveAllGroupsByStudentID(studentID);
    }

    @Override
    public List<IAccount> retrieveAllChatAccountsByAccountID(int accountID) {
        List<IAccount> users = new ArrayList<>();

        IStudent findStudent = null;
        ITutor findTutor = null;

        try {
            findStudent = accessStudents.getStudentByAccountID(accountID);
        } catch (DataAccessException ignored) {
        }

        try {
            findTutor = accessTutors.getTutorByAccountID(accountID);
        } catch (DataAccessException ignored) {
        }

        if (findStudent != null && findStudent.getAccountID() == accountID) {
            List<Integer> tutorIDs = accessMessages.retrieveAllTutorIDsByStudentID(findStudent.getStudentID());
            for (int tutorID : tutorIDs) {

                ITutor tutor = accessTutors.getTutorByTutorID(tutorID);
                int tutorAccountID = tutor.getAccountID();
                accessAccounts.getAccountByAccountID(tutorAccountID).ifPresent(users::add);
            }
        } else if (findTutor != null && findTutor.getAccountID() == accountID) {
            List<Integer> studentIDs = accessMessages.retrieveAllStudentIDsByTutorID(findTutor.getTutorID());
            for (int studentID : studentIDs) {

                IStudent student = accessStudents.getStudentByStudentID(studentID);
                int studentAccountID = student.getAccountID();
                accessAccounts.getAccountByAccountID(studentAccountID).ifPresent(users::add);
            }
        }

        return users;
    }

}
