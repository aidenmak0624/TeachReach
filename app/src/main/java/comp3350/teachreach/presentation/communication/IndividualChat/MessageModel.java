package comp3350.teachreach.presentation.communication.IndividualChat;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import comp3350.teachreach.logic.communication.MessageHandler;
import comp3350.teachreach.logic.interfaces.IMessageHandler;
import comp3350.teachreach.objects.interfaces.IAccount;
import comp3350.teachreach.objects.interfaces.IMessage;

public
class MessageModel extends ViewModel
{
    private final LiveData<IMessageHandler> messageHandler = new MutableLiveData<>(new MessageHandler());

    private final MutableLiveData<List<IMessage>> messageList = new MutableLiveData<>();
    private final MutableLiveData<Integer> groupID = new MutableLiveData<>();
    private final MutableLiveData<IAccount> otherUser = new MutableLiveData<>();

    public
    MutableLiveData<List<IMessage>> getMessageList()
    {
        return messageList;
    }

    public
    void setMessageList(List<IMessage> messageList)
    {
        this.messageList.setValue(messageList);
    }

    public
    MutableLiveData<IAccount> getOtherUser()
    {
        return otherUser;
    }

    public
    void setOtherUser(IAccount otherUser)
    {
        this.otherUser.setValue(otherUser);
    }

    public
    MutableLiveData<Integer> getGroupID()
    {
        return groupID;
    }

    public
    void setGroupID(Integer groupID)
    {
        this.groupID.setValue(groupID);
    }

    public
    LiveData<IMessageHandler> getMessageHandler()
    {
        return messageHandler;
    }
}
