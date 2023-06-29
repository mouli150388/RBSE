package com.tutorix.tutorialspoint.toc;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tutorix.tutorialspoint.R;
import com.tutorix.tutorialspoint.utility.Constants;
import com.tutorix.tutorialspoint.video.VideoPlayerActivity;

import java.util.ArrayList;

public class TOCLecturesAdapter extends RecyclerView.Adapter<TOCLecturesAdapter.MyViewHolder> {
    ArrayList<TOCLecture> tocSectionList;
    String section_id;
    String section_name;

    public TOCLecturesAdapter(String section_id, String section_name) {
        tocSectionList = new ArrayList<>();
        this.section_id = section_id;
        this.section_name = section_name;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View
                itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.toc_item_layout, parent, false);

        return new TOCLecturesAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        TOCLecture tocSection = tocSectionList.get(position);
        holder.textView.setText(tocSection.getLectureName());
        holder.description.setText(holder.textView.getContext().getString(R.string.duration) + " " + tocSection.getLectureDuration());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(), VideoPlayerActivity.class);
                i.setFlags(FLAG_ACTIVITY_NEW_TASK);
                i.putExtra(Constants.lectureVideoUrl, "");
                i.putExtra(Constants.sectionName, section_name);
                i.putExtra("hindiUrl", false);

                i.putExtra(Constants.lectureId, tocSection.getLectureId());
                i.putExtra(Constants.subjectId, "0");
                i.putExtra(Constants.classId, "0");
                i.putExtra(Constants.userId, "0");
                i.putExtra(Constants.sectionId, section_id);
                i.putExtra(Constants.lectureName, tocSection.getLectureName());
                i.putExtra("selected_user_id", "0");
                i.putExtra("selected_class_id", "");
                i.putExtra("lecture_video_url", tocSection.getLectureVideoUrl());

                view.getContext().startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return tocSectionList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        TextView description;


        MyViewHolder(View view) {
            super(view);
            textView = view.findViewById(R.id.chapter_name);
            description = view.findViewById(R.id.chapter_desc);

        }
    }


    public void setList(ArrayList<TOCLecture> tocSectionLists) {

        tocSectionList.clear();
        tocSectionList.addAll(tocSectionLists);
        notifyDataSetChanged();
    }
}
