package comp3350.teachreach.presentation.search;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Locale;

import comp3350.teachreach.R;
import comp3350.teachreach.logic.interfaces.ITutorProfileHandler;
import comp3350.teachreach.logic.interfaces.IUserProfileHandler;
import comp3350.teachreach.objects.interfaces.ITutor;

public class SearchTutorRecyclerAdapter extends
        RecyclerView.Adapter<SearchTutorRecyclerAdapter.ViewHolder> {
    private static List<ITutor>                tutorList;
    private static IOnTutorClickListener       listener;
    private static IUserProfileHandler<ITutor> profileHandler;
    private static ITutorProfileHandler        tutorProfileHandler;

    public SearchTutorRecyclerAdapter(IUserProfileHandler<ITutor> profileHandler,
                                      ITutorProfileHandler tutorProfileHandler, List<ITutor> tutors,
                                      IOnTutorClickListener listener) {
        SearchTutorRecyclerAdapter.tutorList           = tutors;
        SearchTutorRecyclerAdapter.listener            = listener;
        SearchTutorRecyclerAdapter.profileHandler      = profileHandler;
        SearchTutorRecyclerAdapter.tutorProfileHandler = tutorProfileHandler;
    }

    @NonNull
    @Override
    public SearchTutorRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                                    int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                                  .inflate(R.layout.card_row_tutor, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (position == RecyclerView.NO_POSITION) {
            return;
        }
        ITutor tutor = tutorList.get(position);

        CardView tutorCard = holder.getCardView();
        TextView tvName    = holder.getTvName();
        TextView tvMajor   = holder.getTvMajor();
        TextView tvRatings = holder.getTvRatings();
        TextView tvPrice   = holder.getTvHourlyRate();

        tutorCard.setOnClickListener(v -> listener.onTutorClick(tutor));
        tvName.setText(profileHandler.getUserName(tutor));
        tvMajor.setText(profileHandler.getUserMajor(tutor));
        tvRatings.setText(String.format(Locale.getDefault(), "%.2f ⭐️",
                                        tutorProfileHandler.getAvgReview(tutor)));
        tvPrice.setText(String.format(Locale.getDefault(), "$%.2f/h", tutor.getHourlyRate()));
    }

    @Override
    public int getItemCount() {
        return tutorList.size();
    }

    public void updateData(List<ITutor> newList) {
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new DiffUtil.Callback() {
            @Override
            public int getOldListSize() {
                return tutorList.size();
            }

            @Override
            public int getNewListSize() {
                return newList.size();
            }

            @Override
            public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                return tutorList.get(oldItemPosition).getTutorID() ==
                        newList.get(newItemPosition).getTutorID();
            }

            @Override
            public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                ITutor oldTutor = tutorList.get(oldItemPosition);
                ITutor newTutor = newList.get(newItemPosition);
                return oldTutor.getHourlyRate() == newTutor.getHourlyRate() &&
                        oldTutor.getReviewSum() == newTutor.getReviewSum() &&
                        oldTutor.getReviewCount() == newTutor.getReviewCount();
            }
        });
        tutorList = newList;
        diffResult.dispatchUpdatesTo(this);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final CardView cardView;
        private final TextView tvName, tvMajor, tvRatings, tvHourlyRate;

        public ViewHolder(View view) {
            super(view);
            cardView     = view.findViewById(R.id.cardTutor);
            tvName       = view.findViewById(R.id.tvNameField);
            tvMajor      = view.findViewById(R.id.tvMajorField);
            tvRatings    = view.findViewById(R.id.tvRatingsField);
            tvHourlyRate = view.findViewById(R.id.tvHourlyRateField);
        }

        public CardView getCardView() {
            return cardView;
        }

        public TextView getTvName() {
            return tvName;
        }

        public TextView getTvMajor() {
            return tvMajor;
        }

        public TextView getTvRatings() {
            return tvRatings;
        }

        public TextView getTvHourlyRate() {
            return tvHourlyRate;
        }
    }
}
