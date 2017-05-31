package com.mgilangjanuar.dev.sceleapp.Fragments.ForumDetail;

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

import com.mgilangjanuar.dev.sceleapp.Adapters.ForumDetailCommentAdapter;
import com.mgilangjanuar.dev.sceleapp.Presenters.ForumDetailPresenter;
import com.mgilangjanuar.dev.sceleapp.R;

public class Comments extends Fragment implements ForumDetailPresenter.ForumDetailServicePresenter {

    ForumDetailPresenter forumDetailPresenter;

    public Comments() {
        // Required empty public constructor
    }

    public static Comments newInstance(ForumDetailPresenter forumDetailPresenter) {
        Comments fragment = new Comments();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        fragment.forumDetailPresenter = forumDetailPresenter;
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
        return inflater.inflate(R.layout.fragment_forum_comments, container, false);
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        (new Thread(new Runnable() {
            @Override
            public void run() {
                setupForumDetail(view);
            }
        })).start();
    }

    @Override
    public void setupForumDetail(View view) {
        final ForumDetailCommentAdapter adapter = forumDetailPresenter.buildCommentAdapter();
        final RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_forum_detail);
        final TextView status = (TextView) view.findViewById(R.id.text_status_forum_comments);

        if (getActivity() == null) { return; }
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(adapter);
                if(adapter.getItemCount() == 0) {
                    status.setText(getActivity().getResources().getString(R.string.empty_text));
                    status.setTextColor(getActivity().getResources().getColor(R.color.color_accent));
                } else {
                    status.setVisibility(TextView.GONE);
                }
            }
        });

        final SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_forum_detail);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        (new Thread(new Runnable() {
                            @Override
                            public void run() {
                                forumDetailPresenter.clear();
                                final ForumDetailCommentAdapter adapter = forumDetailPresenter.buildCommentAdapter();
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
