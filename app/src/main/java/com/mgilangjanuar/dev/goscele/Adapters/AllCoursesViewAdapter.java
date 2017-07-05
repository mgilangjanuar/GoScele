package com.mgilangjanuar.dev.goscele.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mgilangjanuar.dev.goscele.CourseDetailActivity;
import com.mgilangjanuar.dev.goscele.Models.CourseModel;
import com.mgilangjanuar.dev.goscele.Presenters.CoursePresenter;
import com.mgilangjanuar.dev.goscele.R;

import java.util.List;

/**
 * Created by muhammadgilangjanuar on 5/16/17.
 */

public class AllCoursesViewAdapter extends RecyclerView.Adapter<AllCoursesViewAdapter.AllCoursesViewHolder> {

    List<CourseModel> list;
    Context context;
    CoursePresenter coursePresenter;

    public class AllCoursesViewHolder extends RecyclerView.ViewHolder {

        public RelativeLayout relativeLayout;
        public TextView title;
        public Button buttonAction;

        public AllCoursesViewHolder(final View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title_course_name);
            buttonAction = (Button) itemView.findViewById(R.id.button_course);
            relativeLayout = (RelativeLayout) itemView.findViewById(R.id.layout_course_list);
        }

        public void enableButton() {
            buttonAction.getBackground().setColorFilter(context.getResources().getColor(android.R.color.holo_green_dark), PorterDuff.Mode.MULTIPLY);
            buttonAction.setEnabled(true);
        }

        public void disableButton() {
            buttonAction.getBackground().setColorFilter(context.getResources().getColor(android.R.color.darker_gray), PorterDuff.Mode.MULTIPLY);
            buttonAction.setEnabled(false);
        }
    }

    public AllCoursesViewAdapter(Context context, CoursePresenter coursePresenter, List<CourseModel> list) {
        this.context = context;
        this.coursePresenter = coursePresenter;
        this.list = list;
    }

    @Override
    public AllCoursesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new AllCoursesViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_course, parent, false));
    }

    @Override
    public void onBindViewHolder(final AllCoursesViewHolder holder, int position) {
        final CourseModel courseModel = list.get(position);
        holder.title.setText(courseModel.name);
        holder.buttonAction.setText("Add To Current");
        holder.buttonAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                coursePresenter.addToCurrent(courseModel);
                holder.disableButton();
                coursePresenter.isDataCurrentCoursesViewAdapterChanged = true;
            }
        });

        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity((new Intent(context, CourseDetailActivity.class)).putExtra("url", courseModel.url));
            }
        });

        if (coursePresenter.alreadyExistInCurrent(courseModel)) {
            holder.disableButton();
        } else {
            holder.enableButton();
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onViewRecycled(final AllCoursesViewHolder holder) {
        super.onViewRecycled(holder);

        if (coursePresenter.alreadyExistInCurrent(new CourseModel() {{
            name = holder.title.getText().toString();
        }})) {
            holder.disableButton();
        } else {
            holder.enableButton();
        }
    }


}
