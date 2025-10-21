package comp3350.teachreach.presentation.utils;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import comp3350.teachreach.objects.interfaces.IAccount;
import comp3350.teachreach.objects.interfaces.IStudent;
import comp3350.teachreach.objects.interfaces.ITutor;

public
class TRViewModel extends ViewModel
{
    private final MutableLiveData<IAccount> account = new MutableLiveData<>();
    private final MutableLiveData<IStudent> student = new MutableLiveData<>();
    private final MutableLiveData<ITutor>   tutor   = new MutableLiveData<>();

    private final MutableLiveData<Boolean> isTutor = new MutableLiveData<>(false);

    public
    LiveData<IAccount> getAccount()
    {
        return account;
    }

    public
    void setAccount(IAccount account)
    {
        this.account.setValue(account);
    }

    public
    LiveData<IStudent> getStudent()
    {
        return student;
    }

    public
    void setStudent(IStudent student)
    {
        this.student.postValue(student);
    }

    public
    LiveData<ITutor> getTutor()
    {
        return tutor;
    }

    public
    void setTutor(ITutor tutor)
    {
        this.tutor.setValue(tutor);
    }

    public
    void setIsTutor()
    {
        this.isTutor.setValue(true);
    }

    public
    LiveData<Boolean> getIsTutor()
    {
        return isTutor;
    }
}
