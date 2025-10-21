package Iteration1.tests.logic;
package Iteration1.tests.object;



import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;

import Iteration1.BookingHandler;
import SessionStub
import TutorStub


public class bookingHandlerTest {
    //Before: create tutor availability
    private BookingHandler BookingHandler;
    @Before
    public void setUp() {
        System.out.println("Starting test for BookingHandler");
        BookingHandler = new BookingHandler();
        //Add tutor into the database
    }


    
    //1. Test tutorAvailability()
    @Test
    public void testVerifyEmail(){
        System.out.println("\nStarting testUpdateRecipe");

        System.out.println("Finished testUpdateRecipe");
    }

    @Test
    public void testTutorAvailability(){

        //1. retrieve one tutor from database as input of the function
        //2. call test function with assert
    }

    public boolean[][] tutorAvailability(Tutor Tutor){
        // Check dates and time from tutor->profile->booking object->findout the tine and date
        //1. access data
        //ArrayList<tutor> ListofTutor = dataAccessTutor.getstubTutor();
        Tutor Searched;
        for (int i = 0; i < ListofTutor.size(); i++)
        {
            if(ListofTutor[i].getName().equals(Tutor.getName()))
            {
                Searched = ListofTutor[i];
            }
        }
        //2. retrieve data ( time, dates )
        boolean[][] TutorAvailability = Searched.getAvailabiliity();
        
        //3. return object(booking) (list of objects?) (confirm and pending)
        return TutorAvailability;
    }
    //2. Test SendStudentRequests()
    public void storeStudentRequests(Student student, Tutor tutor, int day, int month, int year, int hour, String location)
    //scenario: student click on request ( with dates, time, location), then send to the booking object to 
    {
        //1. receive student request from UI
        //2. send condition:pending request to tutor (database)
        Session newSession = new Session(student, tutor, day, month, year, hour, location);
        //3. set unavailability on tutor
        tutor.setAvailability(day,  hour, false);
    }

        //3. Test ShowStudentRequests()

    public void retrievePendingOrAcceptStudentRequests(Tutor tutor, Boolean stage)
    //scenario: tutor on upcoming request UI would need to continuious show the student requests
    {
        //1. retrieve data from dataset
        //ArrayList<Session> ListofSession = dataAccessBooking.getstubSession();
        //2. return all pending booking
        ArrayList<Session> Searched;
        for (int i = 0; i < ListofSession.size(); i++)
        {
            if(ListofSession[i].getTutor().equals(tutor))
            {
                if(ListofSession[i].accepted == stage)
                Searched.add(ListofSession[i]);
            }
        }
        
        return Searched;

    }


    public void retrieveAllStudentRequests(Tutor tutor)
    //scenario: tutor on upcoming request UI would need to continuious show the student requests
    {
    //1. retrieve data from dataset
    //ArrayList<Session>ListofSession = dataAccessBooking.getstubSession();
    //2. return all pending booking
     ArrayList<Session> Searched;
        for (int i = 0; i < ListofSession.size(); i++)
        {
         if(ListofSession[i].getTutor().equals(tutor))
         {
             Searched.add(ListofSession[i]);
         }
        }
    
     return Searched;

    }
    #Test
    public void testAllSessionForOneTutor(){

        System.out.println("\nStarting testCreateRecipeList");
        List<Recipe> actualRecipeList = recipeHandler.getAllRecipes();

        assertNotNull(actualRecipeList);
        assertEquals(4, actualRecipeList.size());

        System.out.println("Finished testCreateRecipeList");



    }
    

    //4. Test AnsweredRequest()


    public void AnsweredRequest(Tutor tutor, Session session, boolean decision)
    //scenario: tutor could answer request from student ( Accept/Reject )
    {

        //1.Receive answer from UI (object, or time, date, locaiton)
        if(decision == false)
        {
            int[] date = session.getDate();
            int hour = session.getHour();

            ListofSession.remove(session);
            //Reject, then available.
            tutor.setAvailability(date[0],hour,true);
        }
        else if(decision == true)
        {
            session.acceptSession();
        }
    }
}