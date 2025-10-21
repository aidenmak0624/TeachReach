package comp3350.teachreach.presentation.communication.IndividualChat;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.MaterialToolbar;

import java.util.List;

import comp3350.teachreach.R;
import comp3350.teachreach.databinding.FragmentIndividualChatBinding;
import comp3350.teachreach.logic.communication.MessageHandler;
import comp3350.teachreach.logic.exceptions.MessageHandleException;
import comp3350.teachreach.logic.interfaces.IMessageHandler;
import comp3350.teachreach.objects.interfaces.IMessage;
import comp3350.teachreach.presentation.utils.TRViewModel;

public class IndividualChatFragment extends Fragment {

    private static final String TAG = "IndividualChatFragment";
    private IMessageHandler messageHandler;
    private TRViewModel     vm;
    private MessageModel                  mm;
    private FragmentIndividualChatBinding binding;
    private FullMessageAdaptor            fullMessageAdaptor;
    private EditText inputMessage;
    private Button   sendButton;

    public IndividualChatFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        messageHandler = new MessageHandler();
        vm             = new ViewModelProvider(requireActivity()).get(
                TRViewModel.class);
        mm             = new ViewModelProvider(requireActivity()).get(
                MessageModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentIndividualChatBinding.inflate(inflater,
                                                        container,
                                                        false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUpRecyclerView();
        setUpTopBar();
        setUpSendButton();


        LiveData<List<IMessage>> messagesLiveData
                = mm.getMessageList();
        messagesLiveData.observe(getViewLifecycleOwner(),
                                 messageList -> fullMessageAdaptor.setMessages());
    }

    private void setUpTopBar() {
        MaterialToolbar materialToolbar = binding.topAppBar;
        materialToolbar.setTitle(mm.getOtherUser().getValue().getUserName());

        materialToolbar.setNavigationOnClickListener(view -> {
            NavController navController
                    = NavHostFragment.findNavController(this);
            navController.navigate(R.id.actionToGroupFragment);
        });

    }

    private void setUpRecyclerView() {

        RecyclerView recyclerView = binding.IndividualChatsRecycleViewFragment;
        fullMessageAdaptor = new FullMessageAdaptor(getContext(),
                                                    messageHandler.retrieveAllMessageByGroupID(
                                                            mm.getGroupID().getValue()),

                                                    vm, mm);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(fullMessageAdaptor);
    }

    private void setUpSendButton() {
        inputMessage = binding.inputMessage;
        sendButton   = binding.layoutSend;
        sendButton.setOnClickListener(view -> {
            sendMessage();
        });
    }

    private void sendMessage() {
        try {
            String messages = inputMessage.getText().toString();

            Integer groupIDInteger = mm.getGroupID().getValue();
            if (groupIDInteger != null) {
                int groupID = groupIDInteger;

                int senderAccountID = vm.getAccount().getValue().getAccountID();
                messageHandler.storeMessage(groupID, senderAccountID, messages);
                updateMessage(groupID);
            } else {
                Log.e(TAG, "GroupID is null");
                Toast.makeText(getContext(), "GroupID is null", Toast.LENGTH_SHORT).show();
            }

        } catch (MessageHandleException e) {
            Toast
                    .makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT)
                    .show();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    private void updateMessage(Integer groupID) {
        mm.setMessageList(messageHandler.retrieveAllMessageByGroupID(groupID));
    }


}