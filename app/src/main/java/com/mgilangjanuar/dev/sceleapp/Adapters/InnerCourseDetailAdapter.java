package com.mgilangjanuar.dev.sceleapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mgilangjanuar.dev.sceleapp.Helpers.HtmlHandlerHelper;
import com.mgilangjanuar.dev.sceleapp.InAppBrowser;
import com.mgilangjanuar.dev.sceleapp.Models.InnerCoursePostModel;
import com.mgilangjanuar.dev.sceleapp.R;

import java.util.List;

/**
 * Created by muhammadgilangjanuar on 5/21/17.
 */

public class InnerCourseDetailAdapter extends RecyclerView.Adapter<InnerCourseDetailAdapter.InnerCourseDetailViewHolder> {

    Context context;
    List<InnerCoursePostModel> list;

    public InnerCourseDetailAdapter(Context context, List<InnerCoursePostModel> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public InnerCourseDetailViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new InnerCourseDetailViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_inner_course_detail, parent, false));
    }

    @Override
    public void onBindViewHolder(InnerCourseDetailViewHolder holder, int position) {
        final InnerCoursePostModel model = list.get(position);

        if (! model.title.equals("")) {
            holder.title.setVisibility(TextView.VISIBLE);
            holder.title.setText(model.title);
            holder.title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.startActivity((new Intent(context, InAppBrowser.class)).putExtra("url", model.url));
                }
            });
        }

        if (! model.comment.equals("")) {
            holder.comment.setVisibility(TextView.VISIBLE);
            HtmlHandlerHelper helper = new HtmlHandlerHelper(context, model.comment);
            helper.setTextViewHTML(holder.comment);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class InnerCourseDetailViewHolder extends RecyclerView.ViewHolder {

        public TextView title;
        public TextView comment;

        public InnerCourseDetailViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title_inner_course_post);
            comment = (TextView) itemView.findViewById(R.id.comment_inner_course_post);
        }
    }
}
