package comp3350.teachreach.presentation.booking;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import org.threeten.bp.format.DateTimeFormatter;

import java.util.List;

import comp3350.teachreach.R;
import comp3350.teachreach.objects.interfaces.ITimeSlice;

public
class TimeSlotRecyclerAdapter extends RecyclerView.Adapter<TimeSlotRecyclerAdapter.ViewHolder> {
    private static List<ITimeSlice>         timeSlots;
    private static IOnTimeSlotClickListener listener;
    private        int                      selectedPosition = -1;

    public TimeSlotRecyclerAdapter(List<ITimeSlice> timeSlots, IOnTimeSlotClickListener listener) {
        TimeSlotRecyclerAdapter.timeSlots = timeSlots;
        TimeSlotRecyclerAdapter.listener  = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.time_slot_recycler_view_row, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (position == RecyclerView.NO_POSITION) {
            return;
        }
        ITimeSlice ts       = timeSlots.get(position);
        CardView   cardView = holder.getCardView();
        cardView.setCardBackgroundColor(position == selectedPosition
                                                ?
                                                cardView.getContext()
                                                        .getColor(
                                                                R.color.TIME_CLOT_CARD_BACKGROUND_PRESSED)
                                                :
                                                        cardView.getContext()
                                                                .getColor(
                                                                        R.color.TIME_CLOT_CARD_BACKGROUND_DEFAULT));
        cardView.setOnClickListener(v -> setSelectedPosition(ts, position));
        String dateStr = ts.getStartTime().format(DateTimeFormatter.ofPattern("h:mm a"));
        holder.getTimeField().setText(dateStr);
    }

    @Override
    public int getItemCount() {
        return timeSlots.size();
    }

    public void setSelectedPosition(ITimeSlice timeSlice, int position) {
        notifyItemChanged(selectedPosition);
        selectedPosition = position;
        notifyItemChanged(position);
        listener.onTimeSlotClick(timeSlice);
    }

    public static
    class ViewHolder extends RecyclerView.ViewHolder {
        private final CardView cardView;
        private final TextView timeField;

        public ViewHolder(View view) {
            super(view);
            cardView  = view.findViewById(R.id.timeSlotCard);
            timeField = view.findViewById(R.id.timeSlotText);
        }

        public CardView getCardView() {
            return cardView;
        }

        public TextView getTimeField() {
            return timeField;
        }
    }
}
