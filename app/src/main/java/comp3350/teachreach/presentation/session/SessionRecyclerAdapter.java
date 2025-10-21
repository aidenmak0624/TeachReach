package comp3350.teachreach.presentation.session;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import org.threeten.bp.format.DateTimeFormatter;

import java.util.List;
import java.util.Locale;
import java.util.concurrent.ConcurrentMap;

import comp3350.teachreach.R;
import comp3350.teachreach.logic.interfaces.IUserProfileHandler;
import comp3350.teachreach.objects.SessionStatus;
import comp3350.teachreach.objects.interfaces.ISession;
import comp3350.teachreach.objects.interfaces.IStudent;
import comp3350.teachreach.objects.interfaces.ITimeSlice;
import comp3350.teachreach.objects.interfaces.ITutor;

public
class SessionRecyclerAdapter<T> extends RecyclerView.Adapter<SessionRecyclerAdapter.ViewHolder>
{
    private static final DateTimeFormatter                timeFormatter = DateTimeFormatter.ofPattern("h:mm a, d/M/yy");
    private static       List<ISession>                   sessionList;
    private static       IOnSessionClickListener          listener;
    private static       ConcurrentMap<Integer, IStudent> students;
    private static       ConcurrentMap<Integer, ITutor>   tutors;
    private final        IUserProfileHandler<T>           profileHandler;
    private final        boolean                          isTutor;

    public
    SessionRecyclerAdapter(IUserProfileHandler<T> profileHandler,
                           ConcurrentMap<Integer, IStudent> studentMap,
                           ConcurrentMap<Integer, ITutor> tutorMap,
                           List<ISession> sessions,
                           boolean isTutor,
                           IOnSessionClickListener listener)
    {
        SessionRecyclerAdapter.sessionList = sessions;
        SessionRecyclerAdapter.listener    = listener;
        students                           = studentMap;
        tutors                             = tutorMap;
        this.profileHandler                = profileHandler;
        this.isTutor                       = isTutor;
    }

    @NonNull
    @Override
    public
    ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_session, parent, false);
        return new SessionRecyclerAdapter.ViewHolder(view);
    }

    @Override
    public
    void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
        if (position == RecyclerView.NO_POSITION) {
            return;
        }

        ISession session        = sessionList.get(position);
        TextView withWhom       = holder.getWithWhom();
        TextView startTime      = holder.getStartTime();
        TextView endTime        = holder.getEndTime();
        TextView duration       = holder.getDuration();
        TextView location       = holder.getLocation();
        TextView price          = holder.getPrice();
        TextView accepted       = holder.getAccepted();
        Button   acceptedButton = holder.getAcceptedButton();

        acceptedButton.setOnClickListener(v -> listener.onAcceptedClick(session.approve()));

        String withUser = isTutor ?
                          profileHandler.getUserName((T) students.get(session.getSessionStudentID())) :
                          profileHandler.getUserName((T) tutors.get(session.getSessionTutorID()));

        withWhom.setText(withUser);
        location.setText(session.getSessionLocation());

        ITimeSlice sessionTime = session.getTimeRange();
        startTime.setText(sessionTime.getStartTime().format(timeFormatter));
        endTime.setText(sessionTime.getEndTime().format(timeFormatter));
        duration.setText(String.format(Locale.getDefault(), "%d minutes", sessionTime.getDuration().toMinutes()));
        price.setText(String.format(Locale.getDefault(), "$%.2f", session.getSessionCost()));

        boolean      pen    = session.getStatus() == SessionStatus.PENDING;
        boolean      acc    = session.getStatus() == SessionStatus.ACCEPTED;
        boolean      rej    = session.getStatus() == SessionStatus.REJECTED;
        CharSequence reject = "Reject";
        CharSequence status = "Status: ";

        if (isTutor && pen) {
            accepted.setText(reject);
            accepted.setOnClickListener(v -> listener.onAcceptedClick(session.reject()));
        } else {
            accepted.setText(status);
        }
        acceptedButton.setEnabled(isTutor && pen);
        acceptedButton.setText(pen && isTutor ? "Accept" : acc ? "Accepted!" : rej ? "Rejected" : "Pending");
    }

    @Override
    public
    int getItemCount()
    {
        return sessionList.size();
    }

    public
    void updateData(List<ISession> newList)
    {
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new DiffUtil.Callback()
        {
            @Override
            public
            int getOldListSize()
            {
                return sessionList.size();
            }

            @Override
            public
            int getNewListSize()
            {
                return newList.size();
            }

            @Override
            public
            boolean areItemsTheSame(int oldItemPosition, int newItemPosition)
            {
                return sessionList.get(oldItemPosition).getSessionID() == newList.get(newItemPosition).getSessionID();
            }

            @Override
            public
            boolean areContentsTheSame(int oldItemPosition, int newItemPosition)
            {
                ISession old = sessionList.get(oldItemPosition);
                ISession niu = newList.get(newItemPosition);
                return old.getSessionStudentID() == niu.getSessionStudentID() &&
                       old.getSessionTutorID() == niu.getSessionTutorID() &&
                       old.getSessionLocation().equals(niu.getSessionLocation());
            }
        });
        sessionList = newList;
        diffResult.dispatchUpdatesTo(this);
    }

    public static
    class ViewHolder extends RecyclerView.ViewHolder
    {
        private final TextView withWhom, startTime, endTime, duration, price, accepted, location;
        private final Button acceptedButton;

        public
        ViewHolder(View view)
        {
            super(view);
            withWhom       = view.requireViewById(R.id.sessionWithWhom);
            startTime      = view.requireViewById(R.id.sessionStartDate);
            endTime        = view.requireViewById(R.id.sessionEndDate);
            duration       = view.requireViewById(R.id.sessionDuration);
            location       = view.requireViewById(R.id.sessionLocation);
            price          = view.requireViewById(R.id.sessionPrice);
            accepted       = view.requireViewById(R.id.sessionStatusString);
            acceptedButton = view.requireViewById(R.id.sessionStatusButton);
        }

        public
        TextView getWithWhom()
        {
            return withWhom;
        }

        public
        TextView getStartTime()
        {
            return startTime;
        }

        public
        TextView getEndTime()
        {
            return endTime;
        }

        public
        TextView getDuration()
        {
            return duration;
        }

        public
        TextView getPrice()
        {
            return price;
        }

        public
        TextView getLocation()
        {
            return location;
        }

        public
        TextView getAccepted()
        {
            return accepted;
        }

        public
        Button getAcceptedButton()
        {
            return acceptedButton;
        }
    }
}
