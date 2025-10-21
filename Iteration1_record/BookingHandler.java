import java.util.ArrayList;

"1. 
Check dates and time for availability from user(tutor), object(Booking, already exist)
Send to tutors the newly booked session (BookingUI)
2. Accept/Reject Student Request
Accept/Reject
3. 
Location 

High Priority: 
As a student, I want to book a session under my tutor’s availability (2 days)
Medium Priority:
As a tutor, I want to review and either accept or reject tutoring requests. (2 days)
Low Priority:
As a tutor, I want to set a list of preferred tutoring locations for students to choose from. (2 days)
"
public class BookingHandler{

    private BookingDatabase dataAccessBooking;
    private TutorDatabase dataAccessTutor;
    private ArrayList<tutor> ListofTutor;
    private ArrayList<Session> ListofSession;
   

    public BookingHandler()
    {
        //assume we have service handler
        dataAccessBooking = Services.getBookingDatabase();
        dataAccessTutor = Services.getTutorDatabase();
        ListofTutor = dataAccessTutor.getstubTutor();
        ListofSession = dataAccessBooking.getstubSession();
    }

    public boolean[][] tutorAvailability(Tutor Tutor)
    //scenario: student click on tutor profile->check availability on tutor
    {
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

    public void storeStudentRequests(Student student, Tutor tutor, int day, int month, int year, int hour, String location)
    //scenario: student click on request ( with dates, time, location), then send to the booking object to 
    {
        //1. receive student request from UI
        //2. send condition:pending request to tutor (database)
        Session newSession = new Session(student, tutor, day, month, year, hour, location);
        //3. set unavailability on tutor
        tutor.setAvailability(day,  hour, false);
    }


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
/* 
    public void retrieveAcceptedStudentRequests(Tutor tutor)
    //scenario: tutor on upcoming request UI would need to continuious show the student requests
    {
        //1. retrieve data from dataset
        ArrayList<Session> ListofSession = dataAccessBooking.getstubSession();
        //2. return all pending booking
        ArrayList<Session> Searched;
        for (int i = 0; i < ListofSession.size(); i++)
        {
            if(ListofSession[i].getTutor().equals(tutor))
            {
                if(ListofSession[i].accepted == true)
                Searched.add(ListofSession[i]);
            }
        }
        
        return Searched;

    }
*/
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
        //2.Send it to database, like confimation
    

    //lower function
    //all get set


}
//make a handler
//with KT: he make the location showing on profile
// with aidan: change dataset : booking condiition + pending


