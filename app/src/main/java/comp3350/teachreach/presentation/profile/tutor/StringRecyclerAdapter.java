package comp3350.teachreach.presentation.profile.tutor;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import comp3350.teachreach.R;

public class StringRecyclerAdapter extends RecyclerView.Adapter<StringRecyclerAdapter.ViewHolder> {

    private List<String> stringList;

    public StringRecyclerAdapter(List<String> s) {
        stringList = s;
    }

    @NonNull
    @Override
    public StringRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                               int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                                  .inflate(R.layout.list_row_string, parent, false);

        return new StringRecyclerAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StringRecyclerAdapter.ViewHolder holder, int position) {
        holder.getTvString().setText(stringList.get(position));
    }

    @Override
    public int getItemCount() {
        return stringList.size();
    }

    public void updateData(List<String> newList) {
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new DiffUtil.Callback() {
            @Override
            public int getOldListSize() {
                return stringList.size();
            }

            @Override
            public int getNewListSize() {
                return newList.size();
            }

            @Override
            public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                return stringList.get(oldItemPosition).equals(newList.get(newItemPosition));
            }

            @Override
            public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                return areItemsTheSame(oldItemPosition, newItemPosition);
            }
        });
        stringList = newList;
        diffResult.dispatchUpdatesTo(this);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvString;

        public ViewHolder(View view) {
            super(view);
            tvString = view.findViewById(R.id.tvStringField);
        }

        public TextView getTvString() {
            return tvString;
        }
    }
}

