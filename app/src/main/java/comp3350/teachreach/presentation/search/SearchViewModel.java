package comp3350.teachreach.presentation.search;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.common.util.concurrent.Callables;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import comp3350.teachreach.application.Server;
import comp3350.teachreach.logic.TutorFilter;
import comp3350.teachreach.logic.interfaces.ITutorFilter;
import comp3350.teachreach.logic.interfaces.ITutorProfileHandler;
import comp3350.teachreach.logic.interfaces.IUserProfileHandler;
import comp3350.teachreach.logic.profile.TutorProfileHandler;
import comp3350.teachreach.logic.profile.UserProfileFetcher;
import comp3350.teachreach.objects.interfaces.ITutor;

public class SearchViewModel extends ViewModel {
    private final ExecutorService          threadPool = Executors.newCachedThreadPool();
    private final ListeningExecutorService service    = MoreExecutors.listeningDecorator(
            threadPool);

    private final IUserProfileHandler<ITutor> userFetcher         = new UserProfileFetcher<>();
    private final ITutorProfileHandler        tutorFetcher        = new TutorProfileHandler();
    private final ITutorFilter                tutorFilter         = new TutorFilter(userFetcher,
                                                                                    tutorFetcher);
    private final MutableLiveData<Boolean>    sortingByPrice      = new MutableLiveData<>(
            tutorFilter.getSortByPriceState());
    private final MutableLiveData<Boolean>    sortingByReviews    = new MutableLiveData<>(
            tutorFilter.getSortByReviewsState());
    private final MutableLiveData<Boolean>    filteringByMinPrice = new MutableLiveData<>(
            tutorFilter.getMinimumHourlyRateState());
    private final MutableLiveData<Boolean>    filteringByMaxPrice = new MutableLiveData<>(
            tutorFilter.getMaximumHourlyRateState());
    private final MutableLiveData<Boolean>    filteringByRatings  = new MutableLiveData<>(
            tutorFilter.getMinimumAvgRatingState());
    private final MutableLiveData<Boolean>    filteringByCourse   = new MutableLiveData<>(
            tutorFilter.getCourseCodeState());

    private final MutableLiveData<String> err = new MutableLiveData<>(null);

    private final MutableLiveData<Double> prevMaxPrice   = new MutableLiveData<>(null);
    private final MutableLiveData<Double> prevMinPrice   = new MutableLiveData<>(null);
    private final MutableLiveData<Double> selectedReview = new MutableLiveData<>(null);
    private final MutableLiveData<String> selectedCourse = new MutableLiveData<>(null);

    private final List<ITutor> tutors  = new CopyOnWriteArrayList<>(
            Server.getTutorDataAccess().getTutors().values());
    private final List<String> courses = new CopyOnWriteArrayList<>(
            Server.getCourseDataAccess().getCourses().keySet());

    private final MutableLiveData<List<ITutor>> tutorsFiltered = new MutableLiveData<>();

    public LiveData<Boolean> getSortingByPrice() {
        return sortingByPrice;
    }

    public LiveData<Boolean> getSortingByReviews() {
        return sortingByReviews;
    }

    public LiveData<Boolean> getFilteringByCourse() {
        return filteringByCourse;
    }

    public LiveData<Boolean> getFilteringByMaxPrice() {
        return filteringByMaxPrice;
    }

    public LiveData<Boolean> getFilteringByMinPrice() {
        return filteringByMinPrice;
    }

    public LiveData<Boolean> getFilteringByRatings() {
        return filteringByRatings;
    }

    public LiveData<List<ITutor>> getTutorsFiltered() {
        return tutorsFiltered;
    }

    public void resetFilter() {
        tutorFilter.Reset();
        applyFilters();

        prevMinPrice.postValue(null);
        prevMaxPrice.postValue(null);
        selectedCourse.postValue(null);
        selectedReview.postValue(null);

        sortingByReviews.postValue(tutorFilter.getSortByReviewsState());
        sortingByPrice.postValue(tutorFilter.getSortByPriceState());
        filteringByMaxPrice.postValue(tutorFilter.getMaximumHourlyRateState());
        filteringByMinPrice.postValue(tutorFilter.getMinimumHourlyRateState());
        filteringByRatings.postValue(tutorFilter.getMinimumAvgRatingState());
        filteringByCourse.postValue(tutorFilter.getCourseCodeState());
    }

    public void setSortByReviews() {
        sortingByReviews.postValue(tutorFilter.setSortByReviews().getSortByReviewsState());
        applyFilters();
    }

    public void resetSortByReviews() {
        sortingByReviews.postValue(tutorFilter.clearSortByReviews().getSortByReviewsState());
        applyFilters();
    }

    public void setSortByPrice() {
        sortingByPrice.postValue(tutorFilter.setSortByPrice().getSortByPriceState());
        applyFilters();
    }

    public void resetSortByPrice() {
        sortingByPrice.postValue(tutorFilter.clearSortByPrice().getSortByPriceState());
        applyFilters();
    }

    public void setMinimumAvgRating(double minimumAvgRating) {
        selectedReview.postValue(minimumAvgRating);
        filteringByRatings.postValue(
                tutorFilter.setMinimumAvgRating(minimumAvgRating).getMinimumAvgRatingState());
    }

    public void setMinimumHourlyRate(double desiredHourlyRate) {
        prevMinPrice.postValue(desiredHourlyRate);
        filteringByMinPrice.postValue(
                tutorFilter.setMinimumHourlyRate(desiredHourlyRate).getMinimumHourlyRateState());
    }

    public void setMaximumHourlyRate(double desiredHourlyRate) {
        prevMaxPrice.postValue(desiredHourlyRate);
        filteringByMaxPrice.postValue(
                tutorFilter.setMaximumHourlyRate(desiredHourlyRate).getMaximumHourlyRateState());
    }

    public void setCourseCode(@NonNull String courseCode) {
        selectedCourse.postValue(courseCode);
        filteringByCourse.postValue(tutorFilter.setCourseCode(courseCode).getCourseCodeState());
    }

    public void setSearchFilter(@NonNull String searchString) {
        tutorFilter.setSearchFilter(searchString);
        applyFilters();
    }

    public void resetSearchString() {
        tutorFilter.resetSearchString().filterFunc().apply(tutors);
        applyFilters();
    }

    public void clearMinimumAvgRating() {
        selectedReview.postValue(null);
        filteringByRatings.postValue(
                tutorFilter.clearMinimumAvgRating().getMinimumAvgRatingState());
    }

    public void clearMinimumHourlyRate() {
        prevMinPrice.postValue(null);
        filteringByMinPrice.postValue(
                tutorFilter.clearMinimumHourlyRate().getMinimumHourlyRateState());
    }

    public void clearMaximumHourlyRate() {
        prevMaxPrice.postValue(null);
        filteringByMaxPrice.postValue(
                tutorFilter.clearMaximumHourlyRate().getMaximumHourlyRateState());
    }

    public void clearCourseCode() {
        selectedCourse.postValue(null);
        filteringByCourse.postValue(tutorFilter.clearCourseCode().getCourseCodeState());
    }

    public LiveData<Double> getPrevMaxPrice() {
        return prevMaxPrice;
    }

    public LiveData<Double> getPrevMinPrice() {
        return prevMinPrice;
    }

    public LiveData<Double> getSelectedReview() {
        return selectedReview;
    }

    public LiveData<String> getSelectedCourse() {
        return selectedCourse;
    }

    public List<String> getCourses() {
        return courses;
    }

    public List<ITutor> getTutors() {
        return tutors;
    }

    public IUserProfileHandler<ITutor> getUserFetcher() {
        return userFetcher;
    }

    public ITutorProfileHandler getTutorFetcher() {
        return tutorFetcher;
    }

    public void resetErr() {
        err.postValue(null);
    }

    public LiveData<String> getErr() {
        return err;
    }

    private void applyFilters() {
        ListenableFuture<List<ITutor>> futureList = Futures.submitAsync(
                Callables.asAsyncCallable(() -> tutorFilter.filterFunc().apply(tutors), service),
                service);
        Futures.addCallback(futureList, new FutureCallback<>() {
            @Override
            public void onSuccess(List<ITutor> result) {
                tutorsFiltered.postValue(result);
            }

            @Override
            public void onFailure(@NonNull Throwable t) {
                err.postValue(t.getMessage());
            }
        }, service);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        threadPool.shutdown();
        service.shutdown();
    }
}
