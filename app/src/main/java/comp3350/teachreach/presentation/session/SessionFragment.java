package comp3350.teachreach.presentation.session;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.slidingpanelayout.widget.SlidingPaneLayout;

import com.google.android.material.tabs.TabLayout;

import java.util.List;
import java.util.concurrent.ConcurrentMap;

import comp3350.teachreach.databinding.FragmentSessionsBinding;
import comp3350.teachreach.logic.interfaces.ISessionHandler;
import comp3350.teachreach.logic.profile.UserProfileFetcher;
import comp3350.teachreach.objects.interfaces.ISession;
import comp3350.teachreach.objects.interfaces.IStudent;
import comp3350.teachreach.objects.interfaces.ITutor;
import comp3350.teachreach.presentation.utils.TRViewModel;


public
class SessionFragment extends Fragment
{
    private static ISessionHandler         sessionHandler;
    private        FragmentSessionsBinding binding;
    private        SlidingPaneLayout       slidingPaneLayout;
    private        TabLayout               tabLayout;
    private        TRViewModel             viewModel;
    private        SessionViewModel        sessionViewModel;
    private        boolean                 isTutor;
    private        IStudent                student;
    private        ITutor                  tutor;
    private        Configuration           config;
    private        boolean                 isLarge, isLandscape;

    public
    SessionFragment()
    {
    }

    @Override
    public
    void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        viewModel        = new ViewModelProvider(requireActivity()).get(TRViewModel.class);
        sessionViewModel = new ViewModelProvider(requireActivity()).get(SessionViewModel.class);
        isTutor          = Boolean.TRUE.equals(viewModel.getIsTutor().getValue());
        if (isTutor) {
            tutor = viewModel.getTutor().getValue();
        } else {
            student = viewModel.getStudent().getValue();
        }
    }

    @Override
    public
    View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        binding           = FragmentSessionsBinding.inflate(inflater, container, false);
        slidingPaneLayout = binding.sessionsFragment;
        tabLayout         = binding.sessionTabs;
        config            = getResources().getConfiguration();
        isLarge           = config.isLayoutSizeAtLeast(Configuration.SCREENLAYOUT_SIZE_LARGE);
        isLandscape       = config.orientation == Configuration.ORIENTATION_LANDSCAPE;
        setUpRecycler();
        setUpTabBar();
        return binding.getRoot();
    }

    @Override
    public
    void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public
    void onResume()
    {
        super.onResume();
        if (isTutor) {
            sessionViewModel.setSessionsTutor(SessionViewModel.SessionType.pending, tutor);
        } else {
            sessionViewModel.setSessionsStudent(SessionViewModel.SessionType.pending, student);
        }
    }

    private
    void setUpRecycler()
    {
        RecyclerView recyclerView = binding.rvSessionResult;
        sessionHandler = sessionViewModel.getSessionHandler().getValue();
        List<ISession> sessionList = isTutor ?
                                     sessionHandler.getPendingSessions(tutor) :
                                     sessionHandler.getPendingSessions(student);
        ConcurrentMap<Integer, IStudent> studMap  = sessionViewModel.getStudents().getValue();
        ConcurrentMap<Integer, ITutor>   tutorMap = sessionViewModel.getTutors().getValue();
        SessionRecyclerAdapter<?> adapter = isTutor ?
                                            new SessionRecyclerAdapter<IStudent>(new UserProfileFetcher<>(),
                                                                                 studMap,
                                                                                 tutorMap,
                                                                                 sessionList,
                                                                                 isTutor,
                                                                                 s -> {
                                                                                     sessionHandler.updateSession(s);
                                                                                     this.onResume();
                                                                                 }) :
                                            new SessionRecyclerAdapter<ITutor>(new UserProfileFetcher<>(),
                                                                               studMap,
                                                                               tutorMap,
                                                                               sessionList,
                                                                               isTutor,
                                                                               s -> {
                                                                                   sessionHandler.updateSession(s);
                                                                                   this.onResume();
                                                                               });
        recyclerView.setAdapter(adapter);
        sessionViewModel.getSessionsBeingViewed().observe(getViewLifecycleOwner(), adapter::updateData);

        int                        spanCount     = isLarge && isLandscape ? 3 : isLarge || isLandscape ? 2 : 1;
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(requireContext(), spanCount);
        recyclerView.setLayoutManager(layoutManager);
    }

    private
    void setUpTabBar()
    {
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener()
        {
            @Override
            public
            void onTabSelected(TabLayout.Tab tab)
            {
                switch (tab.getPosition()) {
                    case 0 -> {
                        if (isTutor) {
                            sessionViewModel.setSessionsTutor(SessionViewModel.SessionType.pending, tutor);
                        } else {
                            sessionViewModel.setSessionsStudent(SessionViewModel.SessionType.pending, student);
                        }
                    }
                    case 1 -> {
                        if (isTutor) {
                            sessionViewModel.setSessionsTutor(SessionViewModel.SessionType.accepted, tutor);
                        } else {
                            sessionViewModel.setSessionsStudent(SessionViewModel.SessionType.accepted, student);
                        }
                    }
                    case 2 -> {
                        if (isTutor) {
                            sessionViewModel.setSessionsTutor(SessionViewModel.SessionType.rejected, tutor);
                        } else {
                            sessionViewModel.setSessionsStudent(SessionViewModel.SessionType.rejected, student);
                        }
                    }
                    default -> {
                        if (isTutor) {
                            sessionViewModel.setSessionsTutor(SessionViewModel.SessionType.others, tutor);
                        } else {
                            sessionViewModel.setSessionsStudent(SessionViewModel.SessionType.others, student);
                        }
                    }
                }
            }

            @Override
            public
            void onTabUnselected(TabLayout.Tab tab)
            {
            }

            @Override
            public
            void onTabReselected(TabLayout.Tab tab)
            {
            }
        });
    }
}
