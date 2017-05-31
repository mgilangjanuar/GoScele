package com.mgilangjanuar.dev.sceleapp.Fragments.CourseDetail;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mgilangjanuar.dev.sceleapp.Adapters.CourseDetailAdapter;
import com.mgilangjanuar.dev.sceleapp.Presenters.CourseDetailPresenter;
import com.mgilangjanuar.dev.sceleapp.R;

public class DashboardFragment extends Fragment implements CourseDetailPresenter.CourseDetailServicePresenter {

    RecyclerView recyclerView;
    CourseDetailPresenter courseDetailPresenter;

    public DashboardFragment() {
        // Required empty public constructor
    }

    public static DashboardFragment newInstance(CourseDetailPresenter courseDetailPresenter) {
        DashboardFragment fragment = new DashboardFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        fragment.courseDetailPresenter = courseDetailPresenter;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_course_detail_dashboard, container, false);
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        (new Thread(new Runnable() {
            @Override
            public void run() {
                setupCourseDetail(view);
            }
        })).start();
    }

    @Override
    public void setupCourseDetail(final View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_course_detail);
        final CourseDetailAdapter adapter = courseDetailPresenter.buildDashboardAdapter();
        final TextView status = (TextView) view.findViewById(R.id.text_status_course_dashboard);

        if (getActivity() == null) { return; }
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                if (recyclerView.getAdapter() == null || ! adapter.equals(recyclerView.getAdapter())) {
                    recyclerView.setAdapter(adapter);
                }
                if (adapter.getItemCount() == 0) {
                    status.setText(getActivity().getResources().getString(R.string.empty_text));
                    status.setTextColor(getActivity().getResources().getColor(R.color.color_accent));
                } else {
                    status.setVisibility(TextView.GONE);
                }
            }
        });

        final SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_course_detail);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        (new Thread(new Runnable() {
                            @Override
                            public void run() {
                                courseDetailPresenter.clearDashboard();
                                final CourseDetailAdapter adapter = courseDetailPresenter.buildDashboardAdapter();
                                if (getActivity() == null) { return; }
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        recyclerView.setAdapter(adapter);
                                        swipeRefreshLayout.setRefreshing(false);
                                    }
                                });
                            }
                        })).start();
                    }
                }, 1000);
            }
        });
    }
}
