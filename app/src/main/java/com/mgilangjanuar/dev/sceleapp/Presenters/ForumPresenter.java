package com.mgilangjanuar.dev.sceleapp.Presenters;

import android.app.Activity;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.mgilangjanuar.dev.sceleapp.Adapters.ForumAdapter;
import com.mgilangjanuar.dev.sceleapp.Models.ForumModel;
import com.mgilangjanuar.dev.sceleapp.Models.ListForumModel;
import com.mgilangjanuar.dev.sceleapp.R;
import com.mgilangjanuar.dev.sceleapp.Services.ForumService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

/**
 * Created by muhammadgilangjanuar on 5/31/17.
 */

public class ForumPresenter {
    Activity activity;

    ListForumModel listForumModel;
    ForumService forumService;

    public String url;

    public interface ForumServicePresenter {
        void setupForum();
    }

    public ForumPresenter(Activity activity, String url) {
        this.activity = activity;
        this.url = url;
        listForumModel = new ListForumModel(activity);
        forumService = new ForumService(url);
    }

    public ForumAdapter buildAdapter() {
        if (listForumModel.getSavedUrl() == null
                || !listForumModel.getSavedUrl().equals(url)
                || listForumModel.getSavedForumModelList() == null) {
            buildModel();
        }
        return new ForumAdapter(activity, listForumModel.getSavedForumModelList());
    }

    public void buildModel() {
        try {
            listForumModel.clear();
            listForumModel.url = url;
            listForumModel.forumModelList = new ArrayList<>();
            for (Map<String, String> e: forumService.getForums()) {
                ForumModel model = new ForumModel();
                model.url = e.get("url");
                model.title = e.get("title");
                model.author = e.get("author");
                model.lastUpdate = e.get("lastUpdate");
                model.repliesNumber = e.get("repliesNumber");
                listForumModel.forumModelList.add(model);
            }
            listForumModel.save();
        } catch (IOException e) {
            Log.e("ForumPresenter", e.getMessage());
        }
    }

    public String getTitle() {
        try {
            return forumService.getTitle();
        } catch (IOException e) {
            Log.e("ForumPresenter", e.getMessage());
        }
        return activity.getResources().getString(R.string.app_name);
    }

    public void clear() {
        listForumModel.clear();
    }
}