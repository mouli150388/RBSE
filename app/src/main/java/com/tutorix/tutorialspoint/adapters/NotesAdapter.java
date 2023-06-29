package com.tutorix.tutorialspoint.adapters;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tutorix.tutorialspoint.AppController;
import com.tutorix.tutorialspoint.R;
import com.tutorix.tutorialspoint.activities.EditNotesActivity;
import com.tutorix.tutorialspoint.models.SubChapters;
import com.tutorix.tutorialspoint.utility.Constants;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.SectionViewHolder> {
    ArrayList<SubChapters> data;
    Activity activity;
    String baseurl;
    Resources res;
    public int selected_position=-1;
    public NotesAdapter( Activity activit)
    {
        data=new ArrayList();
        activity=activit;
        res=activit.getResources();


    }
    @NonNull
    @Override
    public SectionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SectionViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.notes_item_adapter, parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull SectionViewHolder holder, int position) {

        SubChapters chapters=data.get(position);
        holder.chapterName.setText(chapters.txt);
        holder.txt_notes.setText(chapters.lecture_notes);
        switch (chapters.subjectid)
        {
            case "1":
                holder.txt_subject.setText(activity.getString(R.string.physics));
                holder.txt_subject.setTextColor(res.getColor(R.color.phy_background));
                holder.logo.setImageResource(R.drawable.physics_quiz);
                break;
            case "2":
                holder.txt_subject.setText(activity.getString(R.string.chemistry));
                holder.txt_subject.setTextColor(res.getColor(R.color.che_background));
                holder.logo.setImageResource(R.drawable.chemistry_quiz);
                break;
            case "3":
                holder.txt_subject.setText(activity.getString(R.string.biology));
                holder.txt_subject.setTextColor(res.getColor(R.color.bio_background));
                holder.logo.setImageResource(R.drawable.biolog_quiz);
                break;
            case "4":
                holder.txt_subject.setText(activity.getString(R.string.mathematics));
                holder.txt_subject.setTextColor(res.getColor(R.color.math_background));
                holder.logo.setImageResource(R.drawable.math_quiz);
                break;
        }

        holder.chapterName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.itemView.performClick();
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selected_position=holder.getAdapterPosition();
                Intent i = new Intent(activity, EditNotesActivity.class);
                i.putExtra(Constants.userId, data.get(holder.getAdapterPosition()).userid);
                i.putExtra(Constants.lectureId, data.get(holder.getAdapterPosition()).lecture_id);
                i.putExtra(Constants.classId, data.get(holder.getAdapterPosition()).classid);
                i.putExtra(Constants.subjectId, data.get(holder.getAdapterPosition()).subjectid);
                i.putExtra(Constants.sectionId, data.get(holder.getAdapterPosition()).section_id);
                i.putExtra(Constants.lectureName, data.get(holder.getAdapterPosition()).txt);
                i.putExtra("isNotes", true);
                //i.putExtra("subchapter",data.get(holder.getAdapterPosition()));
               // i.addFlags(FLAG_ACTIVITY_NEW_TASK);
                activity.startActivityForResult(i,200);
                //mContext = (Activity) vh.itemView.getContext();
                activity.overridePendingTransition(R.anim.right_in, R.anim.left_out);
                AppController.getInstance().playAudio(R.raw.qz_next);
            }
        });

    }

    public void updatedNotes(String text)
    {
        if(selected_position>-1&&text!=null&&!text.isEmpty())
        {
            data.get(selected_position).lecture_notes=text;
            //notifyItemChanged(selected_position);
            notifyDataSetChanged();
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void addData(List<SubChapters> datas) {
        data.clear();;
        data.addAll(datas);
        notifyDataSetChanged();
    }


    class SectionViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.chapter_name)
        TextView chapterName;
        @BindView(R.id.txt_subject)
        TextView txt_subject;


        @BindView(R.id.txt_notes)
        TextView txt_notes;
        @BindView(R.id.logo)
        ImageView logo;
        @BindView(R.id.lnr_card)
        LinearLayout lnrCard;
        public SectionViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }



    }
    public static float convertDpToPixel(float dp, Activity context){

        return dp * ((float) context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }
}
