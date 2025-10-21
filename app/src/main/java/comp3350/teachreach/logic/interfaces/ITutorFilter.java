package comp3350.teachreach.logic.interfaces;

import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

import comp3350.teachreach.objects.interfaces.ITutor;

public interface ITutorFilter {
    ITutorFilter Reset();

    boolean getMinimumAvgRatingState();

    boolean getMinimumHourlyRateState();

    boolean getMaximumHourlyRateState();

    boolean getCourseCodeState();

    boolean getSortByPriceState();

    boolean getSortByReviewsState();

    ITutorFilter resetSearchString();

    ITutorFilter clearMinimumAvgRating();

    ITutorFilter clearMinimumHourlyRate();

    ITutorFilter clearMaximumHourlyRate();

    ITutorFilter clearCourseCode();

    ITutorFilter setMinimumAvgRating(double minimumAvgRating);

    ITutorFilter setMinimumHourlyRate(double desiredHourlyRate);

    ITutorFilter setMaximumHourlyRate(double desiredHourlyRate);

    ITutorFilter setCourseCode(String courseCode);

    ITutorFilter setSearchFilter(String searchString);

    ITutorFilter setSortByPrice();

    ITutorFilter setSortByReviews();

    ITutorFilter clearSortByPrice();

    ITutorFilter clearSortByReviews();

    Function<List<ITutor>, List<ITutor>> filterFunc();

    Function<List<ITutor>, List<ITutor>> filterFunc(Predicate<ITutor> p);
}
