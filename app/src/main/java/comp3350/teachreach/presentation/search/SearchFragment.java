package comp3350.teachreach.presentation.search;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.slidingpanelayout.widget.SlidingPaneLayout;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.materialswitch.MaterialSwitch;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import comp3350.teachreach.R;
import comp3350.teachreach.databinding.DialogFiltersBinding;
import comp3350.teachreach.databinding.FragmentSearchBinding;
import comp3350.teachreach.objects.interfaces.ITutor;
import comp3350.teachreach.presentation.profile.tutor.StringRecyclerAdapter;
import comp3350.teachreach.presentation.utils.TRViewModel;

public class SearchFragment extends Fragment {
    private EditText              maxPrice;
    private EditText              minPrice;
    private CheckBox              priceMaxSwitch;
    private CheckBox              priceMinSwitch;
    private SlidingPaneLayout     slidingPaneLayout;
    private FragmentSearchBinding binding;
    private TRViewModel           viewModel;
    private List<ITutor>          tutorList;
    private List<String>          courseList;
    private SearchViewModel       searchViewModel;

    public SearchFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel       = new ViewModelProvider(requireActivity()).get(TRViewModel.class);
        searchViewModel = new ViewModelProvider(requireActivity()).get(SearchViewModel.class);
        tutorList       = searchViewModel.getTutors();
        courseList      = searchViewModel.getCourses();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSearchBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        slidingPaneLayout = binding.searchFragment;
        setUpRecyclerView();
        setUpSearchBar();
        searchViewModel.getErr().observe(getViewLifecycleOwner(), err -> {
            if (err == null) return;
            searchViewModel.resetErr();
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        searchViewModel.resetFilter();
    }

    private void setUpSearchBar() {
        EditText searchEditText = binding.searchField;
        Button   filterButton   = binding.btnFilter;
        Button   searchButton   = binding.btnSearch;

        searchEditText.setOnEditorActionListener((v, actionId, keyEvent) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE ||
                    (keyEvent.getAction() == KeyEvent.ACTION_DOWN &&
                            keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                return onSearchClick(searchEditText);
            }
            return false;
        });
        filterButton.setOnClickListener(v -> onFilterButtonClick(searchEditText));
        searchButton.setOnClickListener(v -> onSearchClick(searchEditText));
    }

    private void onFilterButtonClick(EditText searchEditText) {
        DialogFiltersBinding filtersBinding = DialogFiltersBinding.inflate(
                this.getLayoutInflater());
        View filterDialogView = filtersBinding.getRoot();

        setUpCourseCodeField(filtersBinding);
        setUpReviewFilterField(filtersBinding);
        setUpPriceFilterField(filtersBinding);
        setUpSortSwitches(filtersBinding);

        new MaterialAlertDialogBuilder(requireContext()).setTitle("Apply Filters")
                                                        .setMessage("Long press on field to reset")
                                                        .setView(filterDialogView)
                                                        .setPositiveButton("Apply", (d, i) -> {
                                                            onSearchClick(searchEditText);
                                                        })
                                                        .setNeutralButton("Reset", (d, i) -> {
                                                            searchViewModel.resetFilter();
                                                        })
                                                        .setNegativeButton("Cancel",
                                                                           (d, i) -> d.dismiss())
                                                        .create()
                                                        .show();
    }

    private void setUpReviewFilterField(DialogFiltersBinding b) {
        TextInputLayout tilMinReviews = b.tilMinReviews;
        List<Integer>   intList       = List.of(1, 2, 3, 4);
        ArrayAdapter<Integer> arrayAdapter = new ArrayAdapter<>(requireActivity(),
                                                                android.R.layout.simple_list_item_1,
                                                                intList);
        AutoCompleteTextView reviewsField  = b.starField;
        CheckBox             reviewsSwitch = b.reviewsSwitch;
        reviewsField.setAdapter(arrayAdapter);
        reviewsField.setThreshold(1);
        reviewsField.setLongClickable(true);
        reviewsField.setOnLongClickListener(v -> {
            searchViewModel.clearMinimumAvgRating();
            return true;
        });
        reviewsField.setOnItemClickListener((p, v, pos, id) -> {
            reviewsField.clearFocus();
            searchViewModel.setMinimumAvgRating(intList.get(pos));
        });
        reviewsSwitch.setOnCheckedChangeListener((d, checked) -> {
            tilMinReviews.setEnabled(checked);
            if (!checked) searchViewModel.clearMinimumAvgRating();
        });
        searchViewModel.getFilteringByRatings().observe(getViewLifecycleOwner(), checked -> {
            reviewsSwitch.setChecked(checked);
            tilMinReviews.setEnabled(checked);
        });
        searchViewModel.getSelectedReview().observe(getViewLifecycleOwner(), r -> {
            if (r != null) reviewsField.setText("⭐️".repeat(r.intValue()));
            else reviewsField.setText("");
        });
    }

    private void setUpSortSwitches(DialogFiltersBinding b) {
        MaterialSwitch sortByPriceSwitch   = b.sortByPriceSwitch;
        MaterialSwitch sortByRatingsSwitch = b.sortByReviewsSwitch;
        sortByPriceSwitch.setOnCheckedChangeListener((c, checked) -> {
            if (checked) searchViewModel.setSortByPrice();
            else searchViewModel.resetSortByPrice();
        });
        sortByRatingsSwitch.setOnCheckedChangeListener((c, checked) -> {
            if (checked) searchViewModel.setSortByReviews();
            else searchViewModel.resetSortByReviews();
        });
        searchViewModel.getSortingByPrice()
                       .observe(getViewLifecycleOwner(), sortByPriceSwitch::setChecked);
        searchViewModel.getSortingByReviews()
                       .observe(getViewLifecycleOwner(), sortByRatingsSwitch::setChecked);
    }

    private void setUpPriceFilterField(DialogFiltersBinding b) {
        TextInputLayout tilMinPrice = b.tilMinPrice;
        TextInputLayout tilMaxPrice = b.tilMaxPrice;
        minPrice       = tilMinPrice.getEditText();
        maxPrice       = tilMaxPrice.getEditText();
        priceMinSwitch = b.priceMinSwitch;
        priceMaxSwitch = b.priceMaxSwitch;
        searchViewModel.getFilteringByMaxPrice().observe(getViewLifecycleOwner(), enabled -> {
            tilMaxPrice.setEnabled(enabled);
            priceMaxSwitch.setChecked(enabled);
        });
        searchViewModel.getFilteringByMinPrice().observe(getViewLifecycleOwner(), enabled -> {
            tilMinPrice.setEnabled(enabled);
            priceMinSwitch.setChecked(enabled);
        });
        priceMinSwitch.setOnCheckedChangeListener((d, checked) -> tilMinPrice.setEnabled(checked));
        priceMaxSwitch.setOnCheckedChangeListener((d, checked) -> tilMaxPrice.setEnabled(checked));
        searchViewModel.getPrevMinPrice().observe(getViewLifecycleOwner(), min -> {
            if (min != null) minPrice.setText(String.format(Locale.getDefault(), "%.2f", min));
        });
        searchViewModel.getPrevMaxPrice().observe(getViewLifecycleOwner(), max -> {
            if (max != null) maxPrice.setText(String.format(Locale.getDefault(), "%.2f", max));
        });
    }

    private void setUpCourseCodeField(DialogFiltersBinding b) {
        TextInputLayout tilCourseCodes = b.tilCourseCode;
        ArrayAdapter<String> coursesArrayAdapter = new ArrayAdapter<>(requireActivity(),
                                                                      android.R.layout.simple_list_item_1,
                                                                      courseList);
        AutoCompleteTextView courseCodes  = b.courseCodeField;
        CheckBox             courseSwitch = b.courseSwitch;
        courseCodes.setAdapter(coursesArrayAdapter);
        courseCodes.setThreshold(1);
        courseCodes.setLongClickable(true);
        courseCodes.setOnLongClickListener(v -> {
            searchViewModel.clearCourseCode();
            return true;
        });
        courseCodes.setOnItemClickListener((p, v, pos, id) -> {
            courseCodes.clearFocus();
            searchViewModel.setCourseCode(courseList.get(pos));
        });
        courseSwitch.setOnCheckedChangeListener((d, checked) -> {
            tilCourseCodes.setEnabled(checked);
            if (!checked) searchViewModel.clearCourseCode();
        });
        searchViewModel.getFilteringByCourse().observe(getViewLifecycleOwner(), checked -> {
            tilCourseCodes.setEnabled(checked);
            courseSwitch.setChecked(checked);
        });
        searchViewModel.getSelectedCourse().observe(getViewLifecycleOwner(), c -> {
            if (c != null) courseCodes.setText(c);
            else courseCodes.setText("");
        });
    }

    private boolean onSearchClick(EditText searchEditText) {
        String searchString = searchEditText.getText().toString().trim();
        if (priceMinSwitch != null && priceMinSwitch.isChecked()) {
            String s = minPrice.getText().toString().trim();
            if (!s.isEmpty()) searchViewModel.setMinimumHourlyRate(Double.parseDouble(s));
        } else searchViewModel.clearMinimumHourlyRate();

        if (priceMaxSwitch != null && priceMaxSwitch.isChecked()) {
            String s = maxPrice.getText().toString().trim();
            if (!s.isEmpty()) searchViewModel.setMaximumHourlyRate(Double.parseDouble(s));
        } else searchViewModel.clearMaximumHourlyRate();

        if (searchString.isEmpty()) searchViewModel.resetSearchString();
        else searchViewModel.setSearchFilter(searchString);
        return true;
    }

    private void setUpRecyclerView() {
        RecyclerView recyclerView = binding.rvSearchResult;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new StringRecyclerAdapter(new ArrayList<>()));
        SearchTutorRecyclerAdapter adapter = new SearchTutorRecyclerAdapter(
                searchViewModel.getUserFetcher(), searchViewModel.getTutorFetcher(), tutorList,
                this::openDetails);
        searchViewModel.getTutorsFiltered().observe(getViewLifecycleOwner(), adapter::updateData);
        recyclerView.setAdapter(adapter);
    }

    private void openDetails(ITutor t) {
        viewModel.setTutor(t);
        FragmentManager fm              = getChildFragmentManager();
        NavHostFragment navHostFragment = (NavHostFragment) fm.findFragmentById(R.id.rightSide);
        assert navHostFragment != null;
        slidingPaneLayout.open();
        NavController nc = navHostFragment.getNavController();
        nc.navigate(R.id.actionToTutorProfileViewFragment);
    }
}