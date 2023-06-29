package com.tutorix.tutorialspoint.adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tutorix.tutorialspoint.R;
import com.tutorix.tutorialspoint.SharedPref;
import com.tutorix.tutorialspoint.activities.SubscribePrePage;
import com.tutorix.tutorialspoint.models.MockTestModel;
import com.tutorix.tutorialspoint.quiz.QuizMockExamActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MockTestListAdapter extends RecyclerView.Adapter<MockTestListAdapter.MoCktestViewHolder> {

    QuizMockExamActivity activity;
    List<MockTestModel> listData;
    int background_drawable;
    int text_color;
   /* public MockTestListAdapter(QuizMockExamActivity activity) {
        this.activity = activity;
        listData = new ArrayList<>();
    }*/

   boolean isActiveExpired;
    public MockTestListAdapter(QuizMockExamActivity activity, int background_drawable,int text_color) {
        this.activity = activity;
        listData = new ArrayList<>();
        this.background_drawable=background_drawable;
        this.text_color=text_color;

        SharedPref sh=new SharedPref();
        if(sh.isExpired(activity))
        {
            isActiveExpired=true;
        }
    }

    @NonNull
    @Override
    public MoCktestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MoCktestViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.mocktest_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MoCktestViewHolder holder, int position) {
        holder.txtTitle.setText(activity.getString(R.string.mock_tests)+" "+(position+1));
        holder.txtTitle.setBackgroundResource(background_drawable);
        holder.txtTitle.setTextColor(text_color);
        holder.txtTitle.setTag(position);
        holder.txtTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    int tagpos= (int) v.getTag();
                if (isActiveExpired&&(tagpos>=1)) {
                    Intent i = new Intent(activity, SubscribePrePage.class);
                    i.putExtra("flag", "M");
                    activity.startActivity(i);
                    return;
                }
                activity.selectedTest(tagpos,listData.get(tagpos).title);
            }
        });

    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public void addMock(MockTestModel model){
        listData.add(model);
        notifyDataSetChanged();
    }

    class MoCktestViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.txt_title)
        TextView txtTitle;
        public MoCktestViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


}
