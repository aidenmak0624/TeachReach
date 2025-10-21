package comp3350.teachreach.presentation.communication.Groups;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;


import java.util.List;

import comp3350.teachreach.R;
import comp3350.teachreach.databinding.CardChatUserBinding;
import comp3350.teachreach.logic.communication.MessageHandler;
import comp3350.teachreach.logic.interfaces.IMessageHandler;
import comp3350.teachreach.objects.interfaces.IAccount;
import comp3350.teachreach.presentation.communication.IndividualChat.MessageModel;
import comp3350.teachreach.presentation.utils.TRViewModel;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UserViewHolder> {
    private List<IAccount> users;
    private CardChatUserBinding binding;
    private ISelectAccountListener listener;

    private TRViewModel vm;

    private GroupModel gm;

    public UsersAdapter(List<IAccount> users, ISelectAccountListener listener, TRViewModel vm, GroupModel gm) {
        this.users = users;
        this.listener = listener;
        this.vm = vm;
        this.gm = gm;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        CardChatUserBinding cardChatUserBinding = CardChatUserBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);

        return new UserViewHolder(cardChatUserBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder userViewHolder, int position) {

        userViewHolder.setUserData(users.get(position));


        userViewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int clickedPosition = userViewHolder.getAdapterPosition();
                if (clickedPosition != RecyclerView.NO_POSITION) {
                    listener.onItemClicked(users.get(clickedPosition));
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        IMessageHandler messageHandler = new MessageHandler();

        return messageHandler.retrieveAllChatAccountsByAccountID(vm.getAccount().getValue().getAccountID()).size();
    }


    class UserViewHolder extends RecyclerView.ViewHolder {
        public CardView cardView;

        UserViewHolder(CardChatUserBinding cardChatUserBinding) {
            super(cardChatUserBinding.getRoot());
            binding = cardChatUserBinding;
            cardView = binding.userCardView;

        }

        void setUserData(IAccount user) {
            binding.tvName.setText(user.getUserName());
            binding.tvMajorField.setText(user.getUserMajor());
            binding.emailView.setText(user.getAccountEmail());
        }
    }

}
