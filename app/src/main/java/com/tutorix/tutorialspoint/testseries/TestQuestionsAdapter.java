package com.tutorix.tutorialspoint.testseries;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tutorix.tutorialspoint.R;
import com.tutorix.tutorialspoint.testseries.data.TestQuestions;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TestQuestionsAdapter extends RecyclerView.Adapter<TestQuestionsAdapter.TestQuestionViewHolder>{

    List<TestQuestions>listQuestions;
    TestQuestions testQuestions;
    QuestionsReviewFragment questionsReviewFragments;


    public TestQuestionsAdapter( QuestionsReviewFragment questionsReviewFragment) {
        listQuestions=new ArrayList<>();
        questionsReviewFragments=questionsReviewFragment;
    }

    @NonNull
    @Override
    public TestQuestionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TestQuestionViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.test_question_item,parent,false));
    }

    public void addData( List<TestQuestions>listQuestion)
    {
        listQuestions.clear();
        listQuestions.addAll(listQuestion);
        notifyDataSetChanged();
    }
    @Override
    public void onBindViewHolder(@NonNull TestQuestionViewHolder holder, int position) {

         testQuestions=listQuestions.get(position);
         holder.txt_q_no.setText(""+testQuestions.question_id);
        holder.txt_q_no.setTextColor(Color.parseColor("#ffffff"));
        switch (testQuestions.status)
        {
            case 2:
                holder.txt_q_no.setBackgroundColor(Color.parseColor("#04A80C"));
                break;
            case 1:
                holder.txt_q_no.setBackgroundColor(Color.parseColor("#E73E08"));
                break;
            case 3:
                holder.txt_q_no.setBackgroundColor(Color.parseColor("#F8E42A"));
                break;
            case 4:
                holder.txt_q_no.setBackgroundColor(Color.parseColor("#1F34A7"));
                break;
            case 0:
            case -1:
                holder.txt_q_no.setBackgroundColor(Color.parseColor("#FFFFFF"));
                holder.txt_q_no.setTextColor(Color.parseColor("#000000"));
                break;
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                questionsReviewFragments.selectedQuestion(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listQuestions.size();
    }

    class TestQuestionViewHolder extends RecyclerView.ViewHolder{

        TextView txt_q_no;
        public TestQuestionViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_q_no=itemView.findViewById(R.id.txt_q_no);
        }
    }
}
