package comp3350.teachreach.logic;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import comp3350.teachreach.logic.interfaces.ITutorFilter;
import comp3350.teachreach.logic.interfaces.ITutorProfileHandler;
import comp3350.teachreach.logic.interfaces.IUserProfileHandler;
import comp3350.teachreach.logic.profile.TutorProfileHandler;
import comp3350.teachreach.logic.profile.UserProfileFetcher;
import comp3350.teachreach.objects.interfaces.ITutor;

public class TutorFilter implements ITutorFilter {
    Map<ConditionFilterTag, Predicate<ITutor>> cond            = new HashMap<>();
    List<SortConditionTag>                     sortCondSet     = new ArrayList<>();
    Predicate<ITutor>                          searchCondition = t -> true;

    IUserProfileHandler<ITutor> userHandler;
    ITutorProfileHandler        profileHandler;

    public TutorFilter(IUserProfileHandler<ITutor> userInfoFetcher,
                       ITutorProfileHandler tutorInfoFetcher) {
        userHandler    = userInfoFetcher;
        profileHandler = tutorInfoFetcher;
    }

    public static TutorFilter New() {
        return new TutorFilter(new UserProfileFetcher<>(), new TutorProfileHandler());
    }

    @Override
    public TutorFilter Reset() {
        cond.clear();
        sortCondSet.clear();
        searchCondition = t -> true;
        return this;
    }

    @Override
    public TutorFilter resetSearchString() {
        searchCondition = t -> true;
        return this;
    }

    @Override
    public boolean getMinimumAvgRatingState() {
        return cond.containsKey(ConditionFilterTag.minReview);
    }

    @Override
    public boolean getMinimumHourlyRateState() {
        return cond.containsKey(ConditionFilterTag.minPrice);
    }

    @Override
    public boolean getMaximumHourlyRateState() {
        return cond.containsKey(ConditionFilterTag.maxPrice);
    }

    @Override
    public boolean getCourseCodeState() {
        return cond.containsKey(ConditionFilterTag.courseCode);
    }

    @Override
    public boolean getSortByPriceState() {
        return sortCondSet.contains(SortConditionTag.byPrice);
    }

    @Override
    public boolean getSortByReviewsState() {
        return sortCondSet.contains(SortConditionTag.byReviews);
    }

    @Override
    public TutorFilter clearMinimumAvgRating() {
        cond.remove(ConditionFilterTag.minReview);
        return this;
    }

    @Override
    public TutorFilter clearMinimumHourlyRate() {
        cond.remove(ConditionFilterTag.minPrice);
        return this;
    }

    @Override
    public TutorFilter clearMaximumHourlyRate() {
        cond.remove(ConditionFilterTag.maxPrice);
        return this;
    }

    @Override
    public TutorFilter clearCourseCode() {
        cond.remove(ConditionFilterTag.courseCode);
        return this;
    }

    @Override
    public TutorFilter setMinimumAvgRating(double minimumAvgRating) {
        cond.put(ConditionFilterTag.minReview,
                 t -> profileHandler.getAvgReview(t) >= minimumAvgRating);
        return this;
    }

    @Override
    public TutorFilter setMinimumHourlyRate(double desiredHourlyRate) {
        cond.put(ConditionFilterTag.minPrice, t -> t.getHourlyRate() >= desiredHourlyRate);
        return this;
    }

    @Override
    public TutorFilter setMaximumHourlyRate(double desiredHourlyRate) {
        cond.put(ConditionFilterTag.maxPrice, t -> t.getHourlyRate() <= desiredHourlyRate);
        return this;
    }

    @Override
    public TutorFilter setCourseCode(String courseCode) {
        cond.put(ConditionFilterTag.courseCode,
                 t -> profileHandler.getCourseCodeList(t).contains(courseCode));
        return this;
    }

    @Override
    public TutorFilter setSearchFilter(String searchString) {
        final String s = searchString.trim().toLowerCase();
        searchCondition = t -> profileHandler.getCourseCodeList(t)
                                             .stream()
                                             .anyMatch(code -> code.toLowerCase().contains(s))

                || profileHandler.getCourseDescriptionList(t)
                                 .stream()
                                 .anyMatch(desc -> desc.toLowerCase().contains(s))

                || profileHandler.getPreferredLocations(t)
                                 .stream()
                                 .anyMatch(l -> l.toLowerCase().contains(s))

                || userHandler.getUserName(t).toLowerCase().contains(s)

                || userHandler.getUserMajor(t).toLowerCase().contains(s);
        return this;
    }

    @Override
    public TutorFilter setSortByPrice() {
        if (sortCondSet.contains(SortConditionTag.byPrice)) return this;
        sortCondSet.add(SortConditionTag.byPrice);
        return this;
    }

    @Override
    public TutorFilter setSortByReviews() {
        if (sortCondSet.contains(SortConditionTag.byReviews)) return this;
        sortCondSet.add(SortConditionTag.byReviews);
        return this;
    }

    @Override
    public TutorFilter clearSortByPrice() {
        sortCondSet.remove(SortConditionTag.byPrice);
        return this;
    }

    @Override
    public TutorFilter clearSortByReviews() {
        sortCondSet.remove(SortConditionTag.byReviews);
        return this;
    }

    @Override
    public Function<List<ITutor>, List<ITutor>> filterFunc() {
        return t -> t.stream().filter(condition()).sorted(sortBy()).collect(Collectors.toList());
    }

    @Override
    public Function<List<ITutor>, List<ITutor>> filterFunc(Predicate<ITutor> p) {
        return t -> t.stream().filter(p).sorted(sortBy()).collect(Collectors.toList());
    }

    private Predicate<ITutor> condition() {
        Predicate<ITutor> filterConditions = cond.values()
                                                 .stream()
                                                 .reduce(Predicate::and)
                                                 .orElse(t -> true);
        return filterConditions.and(searchCondition);
    }

    private Comparator<ITutor> sortBy() {
        Comparator<ITutor> result    = null;
        Comparator<ITutor> byPrice   = Comparator.comparingDouble(ITutor::getHourlyRate);
        Comparator<ITutor> byReviews = Comparator.comparingDouble(profileHandler::getAvgReview);
        byReviews = byReviews.reversed();

        for (SortConditionTag t : sortCondSet) {
            switch (t) {
                case byPrice -> result = (result == null) ? byPrice : result.thenComparing(byPrice);
                case byReviews -> result = (result == null)
                        ? byReviews
                        : result.thenComparing(byReviews);
            }
        }

        return result == null ? Comparator.comparingInt(ITutor::getTutorID) : result;
    }

    private enum ConditionFilterTag {
        minReview,
        minPrice,
        maxPrice,
        courseCode
    }

    private enum SortConditionTag {
        byPrice,
        byReviews
    }
}
