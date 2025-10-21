package comp3350.teachreach.tests.integrationTests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import comp3350.teachreach.data.hsqldb.MessageHSQLDB;
import comp3350.teachreach.data.interfaces.IMessagePersistence;
import comp3350.teachreach.logic.DAOs.AccessMessages;
import comp3350.teachreach.objects.interfaces.IMessage;
import comp3350.teachreach.objects.interfaces.IStudent;
import comp3350.teachreach.objects.interfaces.ITutor;
import comp3350.teachreach.tests.utils.TestUtils;

public class AccessMessagesIT {
    private AccessMessages accessMessages;
    private IStudent testStudent;
    private ITutor testTutor;
    private File tempDB;


    @Before
    public void setUp() throws IOException {
        this.tempDB = TestUtils.copyDB();
        final IMessagePersistence persistence = new
                MessageHSQLDB(this.tempDB
                .getAbsolutePath()
                .replace(
                        ".script",
                        ""));
        this.accessMessages = new AccessMessages(persistence);
    }

    @Test
    public void testCreateGroup() {
        int newGroupID = accessMessages.createGroup(1, 1);
        assertEquals(1, newGroupID);
    }

    @Test
    public void testStoreMessage() {
        int groupID = accessMessages.createGroup(1, 1);
        assertEquals(1, groupID);
        List<IMessage> messages = accessMessages.retrieveAllMessageByGroupID(groupID);
        assertEquals(0, messages.size());
        int message_ID = accessMessages.storeMessage(groupID, 1, "HELLO WORLD!");
        assertEquals(1, message_ID);
    }

    @Test
    public void testSearchGroupByIDs() {
        accessMessages.createGroup(1, 1);
        int testGroupID1 = accessMessages.searchGroupByIDs(1, 1);
        assertEquals(1, testGroupID1);
        accessMessages.createGroup(2, 1);
        int testGroupID2 = accessMessages.searchGroupByIDs(2, 1);
        assertEquals(2, testGroupID2);
        int testGroupID3 = accessMessages.searchGroupByIDs(3, 4);
        assertEquals(-1, testGroupID3);
    }

    @Test
    public void testSearchIDsByGroupID() {
        int groupID = accessMessages.createGroup(2, 1);
        Map<String, Integer> IDs;
        IDs = accessMessages.searchIDsByGroupID(groupID);
        assertEquals(2, Objects.requireNonNull(IDs.get("StudentID")).intValue());
        assertEquals(1, Objects.requireNonNull(IDs.get("TutorID")).intValue());
    }

    @Test
    public void testRetrieveAllMessageByGroupID() {
        int groupID = accessMessages.createGroup(1, 1);
        List<IMessage> testMessages = accessMessages.retrieveAllMessageByGroupID(groupID);
        assertEquals(0, testMessages.size());
        accessMessages.storeMessage(groupID, 1, "GOOD MORNING!");
        testMessages = accessMessages.retrieveAllMessageByGroupID(groupID);
        assertEquals(1, testMessages.size());
        assertEquals(1, testMessages.get(0).getSenderID());
        accessMessages.storeMessage(groupID, 3, "I'M FINE THANK YOU!");
        testMessages = accessMessages.retrieveAllMessageByGroupID(groupID);
        assertEquals(2, testMessages.size());
        assertEquals(3, testMessages.get(1).getSenderID());
    }

    @Test
    public void testRetrieveAllGroupsByTutorID() {
        int tutorID = 1;
        int groupID1 = accessMessages.createGroup(1, tutorID);
        int groupID2 = accessMessages.createGroup(2, tutorID);

        List<Integer> groups = accessMessages.retrieveAllGroupsByTutorID(tutorID);

        assertEquals(2, groups.size());
        assertTrue(groups.contains(groupID1));
        assertTrue(groups.contains(groupID2));
    }

    @Test
    public void testRetrieveAllGroupsByStudentID() {
        int studentID = 1;
        int groupID1 = accessMessages.createGroup(studentID, 1);
        int groupID2 = accessMessages.createGroup(studentID, 2);

        List<Integer> groups = accessMessages.retrieveAllGroupsByStudentID(studentID);

        assertEquals(2, groups.size());
        assertTrue(groups.contains(groupID1));
        assertTrue(groups.contains(groupID2));
    }

    @Test
    public void testTimeStamp() {
        accessMessages.createGroup(1, 1);
        accessMessages.storeMessage(1, 1, "GOOD MORNING!");
        List<IMessage> testMessages = accessMessages.retrieveAllMessageByGroupID(1);
        Timestamp time = testMessages.get(0).getTime();
        assertNotNull(time);
    }

    @After
    public void tearDown() {
        this.tempDB.delete();
    }
}
