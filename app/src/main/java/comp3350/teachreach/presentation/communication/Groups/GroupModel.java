package comp3350.teachreach.presentation.communication.Groups;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import comp3350.teachreach.objects.interfaces.IAccount;

public
class GroupModel extends ViewModel
{
    private final MutableLiveData<List<IAccount>> contactList = new MutableLiveData<>(new ArrayList<>());


    public
    MutableLiveData<List<IAccount>> getContactList()
    {
        return contactList;
    }

    public
    void addAccountToContactList(IAccount newAccount)
    {
        List<IAccount> currentList = contactList.getValue();
        if (currentList == null) {
            currentList = new ArrayList<>();
        }
        currentList.add(newAccount);
        contactList.postValue(currentList);
    }
}

