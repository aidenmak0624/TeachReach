package comp3350.teachreach.presentation.communication.IndividualChat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import comp3350.teachreach.databinding.FragmentOtherGuyMessageBinding;
import comp3350.teachreach.databinding.SingleMessageBinding;
import comp3350.teachreach.logic.communication.MessageHandler;
import comp3350.teachreach.logic.interfaces.IMessageHandler;
import comp3350.teachreach.objects.interfaces.IMessage;
import comp3350.teachreach.presentation.utils.TRViewModel;

public class FullMessageAdaptor
        extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_TYPE_SENT_MESSAGE     = 1;
    private static final int VIEW_TYPE_RECEIVED_MESSAGE = 2;

    private Context context;


    private TRViewModel  vm;
    private MessageModel mm;


    private List<IMessage> messageList;

    public FullMessageAdaptor(Context context,
                              List<IMessage> messageList,

                              TRViewModel vm, MessageModel mm) {
        this.context     = context;
        this.messageList = messageList;
        this.vm          = vm;
        this.mm          = mm;

        extractMessages();
    }

    private void extractMessages() {
        messageList = mm.getMessageList().getValue();

    }

    public void setMessages() {

        extractMessages();
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                      int viewType) {
        if (viewType == VIEW_TYPE_SENT_MESSAGE) {
            return new SendMessageViewHolder(SingleMessageBinding.inflate(
                    LayoutInflater.from(parent.getContext()),
                    parent,
                    false));
        } else {
            return new ReceivedMessageViewHolder(FragmentOtherGuyMessageBinding.inflate(
                    LayoutInflater.from(parent.getContext()),
                    parent,
                    false
            ));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder,
                                 int position) {
        switch (holder.getItemViewType()) {
            case VIEW_TYPE_SENT_MESSAGE:
                ((SendMessageViewHolder) holder).setMessageData(
                        messageList.get(position));
                break;
            case VIEW_TYPE_RECEIVED_MESSAGE:
                ((ReceivedMessageViewHolder) holder).setMessageData(
                        messageList.get(position));
                break;
        }
    }

    @Override
    public int getItemCount() {
        IMessageHandler messageHandler = new MessageHandler();

        return messageHandler.retrieveAllMessageByGroupID(mm.getGroupID().getValue()).size();

    }

    @Override
    public int getItemViewType(int position) {
        if (messageList.get(position).getSenderID() ==
                mm.getOtherUser().getValue().getAccountID()) {
            return VIEW_TYPE_RECEIVED_MESSAGE;
        } else {
            return VIEW_TYPE_SENT_MESSAGE;
        }
    }

    static class SendMessageViewHolder extends RecyclerView.ViewHolder {
        private SingleMessageBinding binding;
        private IMessageHandler      messageHandler;

        SendMessageViewHolder(SingleMessageBinding singleMessageBinding) {
            super(singleMessageBinding.getRoot());
            binding        = singleMessageBinding;
            messageHandler = new MessageHandler();
        }

        void setMessageData(IMessage sentMessage) {
            binding.messageView.setText(sentMessage.getMessage());
            binding.timeTextView.setText
                                        (messageHandler.timeStampConverter(sentMessage.getTime()));

        }
    }

    class ReceivedMessageViewHolder extends RecyclerView.ViewHolder {

        private FragmentOtherGuyMessageBinding binding;
        private IMessageHandler                messageHandler;

        ReceivedMessageViewHolder(FragmentOtherGuyMessageBinding otherGuyMessageBinding) {
            super(otherGuyMessageBinding.getRoot());
            binding        = otherGuyMessageBinding;
            messageHandler = new MessageHandler();
        }

        void setMessageData(IMessage receivedMessage
        ) {
            binding.otherGuyNameView.setText(mm.getOtherUser().getValue().getUserName());
            binding.otherGuyRealMessageView.setText(receivedMessage.getMessage());
            binding.otherGuyTimeView.setText(
                    messageHandler.timeStampConverter(receivedMessage.getTime())
            );

        }
    }
}
