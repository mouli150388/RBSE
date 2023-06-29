package com.tutorix.tutorialspoint.adapters;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.viewpager.widget.PagerAdapter;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.tutorix.tutorialspoint.AppController;
import com.tutorix.tutorialspoint.R;
import com.tutorix.tutorialspoint.models.MockTestReviewModel;
import com.tutorix.tutorialspoint.quiz.MockTestPreviousActivity;
import com.tutorix.tutorialspoint.quiz.ReviewQuiz;
import com.tutorix.tutorialspoint.utility.CommonUtils;
import com.tutorix.tutorialspoint.utility.Constants;
import com.tutorix.tutorialspoint.views.CircularProgressBarThumb;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MockTestReviewAdapter extends PagerAdapter {
    List<MockTestReviewModel> listData;
    MockTestPreviousActivity activity;
    MockTestReviewModel model;
    String userid;
    int color_theme;
    int color_theme_card;
    int background_drawable;
    int progressDrawable;
    Resources res;
    DecimalFormat df = new DecimalFormat("#.##");



    public MockTestReviewAdapter(MockTestPreviousActivity activity, String userid, int color_theme, int background_drawable, int color_theme_card, int progressDrawable) {
        listData = new ArrayList<>();
        this.activity = activity;
        this.userid=userid;
        this.color_theme=color_theme;
        this.background_drawable=background_drawable;
        this.color_theme_card=color_theme_card;
        this.progressDrawable=progressDrawable;
        res=activity.getResources();
    }

    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.mock_review_item, null);
        //ButterKnife.bind(activity,view);
        model=listData.get(position);

        TextView txt_sectionname = view.findViewById(R.id.txt_sectionname);
        TextView txt_mocktest = view.findViewById(R.id.txt_mocktest);
        TextView txt_test_time = view.findViewById(R.id.txt_test_time);
        TextView txt_chapter_name = view.findViewById(R.id.txt_chapter_name);
        TextView txt_score = view.findViewById(R.id.txt_score);
        TextView txt_score_total = view.findViewById(R.id.txt_score_total);
        TextView txt_score_slash = view.findViewById(R.id.txt_score_slash);
       // TextView txt_speed = view.findViewById(R.id.txt_speed);
        TextView txt_accuracy = view.findViewById(R.id.txt_accuracy);
        CircularProgressBarThumb progress_score =view. findViewById(R.id.progress_score);
        CircularProgressBarThumb progress_time =view. findViewById(R.id.progress_time);
        CircularProgressBarThumb progress_accuracy =view. findViewById(R.id.progress_accuracy);
        //ProgressBar progress_speed =view. findViewById(R.id.progress_speed);
        TextView wrong = view.findViewById(R.id.wrong);
        Button Review = view.findViewById(R.id.Review);

        TextView time = view.findViewById(R.id.time);
        TextView selected = view.findViewById(R.id.selected);
        EditText edit_suggestion = view.findViewById(R.id.edit_suggestion);
        //TextView txt_grade = view.findViewById(R.id.txt_grade);
        String selectedAnswer = String.format("%02d",model.attempted_questions) ;
        String unselected = String.valueOf(model.total_questions-model.attempted_questions) ;
        selected.setText(activity.getString(R.string.attemped)+" : "+selectedAnswer);
        TextView correct = view.findViewById(R.id.correct);
        CardView lnr_title_header = view.findViewById(R.id.lnr_title_header);

        lnr_title_header.setCardBackgroundColor(res.getColor(color_theme_card));
        txt_score_slash.setTextColor(res.getColor(color_theme));
        txt_score_slash.setBackgroundResource((color_theme));
        txt_score_total.setTextColor(res.getColor(color_theme));
        txt_score.setTextColor(res.getColor(color_theme));
       // txt_sectionname.setTextColor(Color.parseColor("#6E6E70"));
        //txt_mocktest.setTextColor(res.getColor(color_theme));
        //Review.setTextColor(res.getColor(color_theme));
       Review.setBackgroundResource(background_drawable);

        // ImageView img_resultgif = view.findViewById(R.id.img_resultgif);
        //Glide.with(getApplicationContext()).load(R.drawable.grad_result).into(img_resultgif);


        txt_sectionname.setText(activity.section_name);
        txt_chapter_name.setVisibility(View.GONE);
        txt_mocktest.setVisibility(View.VISIBLE);
        txt_test_time.setVisibility(View.VISIBLE);
        txt_mocktest.setText(model.mock_test.replaceAll("M",activity.getString(R.string.mock_tests)+" "));


        String correctAnswer = String.format("%02d",model.total_correct) ;
        //txt_score.setText(correctAnswer);
        //txt_score_total.setText(" / "+model.total_questions);

        correct.setText(activity.getString(R.string.correct_answeres)+": "+correctAnswer);
        String wrongAnswer = String.format("%02d",model.total_wrong) ;
        wrong.setText(activity.getString(R.string.wrong_answers)+": "+wrongAnswer);


        txt_test_time.setText(activity.getString(R.string.attempted_on)+CommonUtils.getstringDateAndTime(model.created_dtm));

        float c= ((float)model.total_correct/model.total_questions);
        c=c*100;
        float w=100-(c);


        progress_score.setProgressColor(res.getColor(color_theme));

           // showRecommanded(lnr_recommand_items);

        //progress_score.setMax(100);
        progress_score.setProgress(1);


        //progress_accuracy.setMax(100);
        progress_accuracy.setProgress(((float)model.total_correct/model.total_questions));
        //setAccuracy(pie_accuracy,c,w);


        DateFormat sdf = new SimpleDateFormat("hh:mm:ss");
        int Minute=0;
        int Sec=0;
        try {
            //Log.v("model.quiz_duration ","model.quiz_duration "+model.quiz_duration);
            Date date=sdf.parse(model.quiz_duration);
            Minute= date.getMinutes();
            Sec= date.getSeconds();
           // Log.v("model.quiz_duration ","model.quiz_duration "+Minute+" "+Sec);
        } catch (ParseException e) {
            e.printStackTrace();
        }



        float mnts=Minute+((float)Sec/60);
        int TOTAL_TIME;
        if(model.class_id.equals("4"))
        {
            if(model.total_questions%2==0)
                TOTAL_TIME= (int) (model.total_questions*1.5);
            else  TOTAL_TIME= (int)( (model.total_questions*1.5)+0.5);
        }else
        {
            TOTAL_TIME= model.total_questions*1;
        }
        float totalTimeInSec=Minute+((float)Sec/60);
        //Log.v("model.quiz_duration ","totalTimeInSec "+totalTimeInSec);
        /*float speed;
        if(model.attempted_questions==0)
            speed=0;
        else speed=(totalTimeInSec/model.attempted_questions);*/

        //progress_time.setMax(TOTAL_TIME*60);
        progress_time.setProgress((int) (totalTimeInSec/TOTAL_TIME));



       /* progress_speed.setMax(10*100);
        try {
            progress_speed.setProgress((int) (speed*100));
        }catch (Exception e)
        {
            progress_speed.setProgress((int)(speed*100));
        }*/

        //txt_accuracy.setText(speed+"/min.");
        // mProgressView2.setText(speed+"/ Minutes");
        try{
            // mProgressView2.setProgress(Integer.parseInt(speed));
        }catch (Exception e)
        {
            // mProgressView2.setProgress(0);
        }

      //  String timeTodis = String.format(format, Minute)  + String.format(format, Sec);

        txt_score.setText(correctAnswer);
        txt_score_total.setText(""+model.total_questions);
        time.setText((df.format(mnts)+"/"+TOTAL_TIME));
       // txt_speed.setText(df.format(speed)+"/qtn.");
        txt_accuracy.setText((int)c+"%");

        container.addView(view);
        Review.setTag(model);
        Review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppController.getInstance().playAudio(R.raw.button_click);
                MockTestReviewModel model= (MockTestReviewModel) v.getTag();
                Intent i = new Intent(activity, ReviewQuiz.class);
                i.putExtra(Constants.userId, userid);
                i.putExtra("quiz_id", model.quiz_id);
                i.putExtra("lecture_time", model.created_dtm);
                i.putExtra("subject_id", model.subject_id);
                i.putExtra("mock_test", "M");

                activity.startActivity(i);
                activity.overridePendingTransition(R.anim.right_in, R.anim.left_out);
            }
        });
        return view;
    }


    @Override
    public boolean isViewFromObject(View view, Object obj) {
        return view == obj;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View view = (View) object;
        container.removeView(view);
    }

    public void addMockReview(MockTestReviewModel model)
    {
        listData.add(model);
        notifyDataSetChanged();
    }
    private void setAccuracy(PieChart piechart,float ansP,float ansW)
    {
        piechart.getDescription().setText("");
        piechart.setHoleRadius(70);
        piechart.setEntryLabelTextSize(8);
        Typeface tf=Typeface.createFromAsset(activity.getAssets(),"opensans_regular.ttf");
        ArrayList<PieEntry> yValues = new ArrayList<>();
        yValues.add(new PieEntry(ansP, "Correct"));
        yValues.add(new PieEntry(ansW, "Wrong"));
        PieDataSet pidataSet = new PieDataSet(yValues, "");
        pidataSet.setColors(new int[]{R.color.bio_background,R.color.red},activity);
        pidataSet.setSliceSpace(1f);
        pidataSet.setValueTextSize(8f);
        pidataSet.setValueTextColor(activity.getResources().getColor(R.color.white));

        //pidataSet.setValueTextColors(Arrays.asList(new Integer[]{R.color.white,R.color.white}));
        pidataSet.setSelectionShift(1f);
        pidataSet.setValueLineColor(activity.getResources().getColor(R.color.white));
        pidataSet.setValueTypeface(tf);
        // pidataSet.getEntriesForXValue(ansP).setLabel("Answered");
        // pidataSet.getEntriesForXValue(100-ansP).get(0).setLabel("Wrong");


        PieData pidata = new PieData(pidataSet);
        pidata.setValueFormatter(new PercentFormatter());


        piechart.setDrawEntryLabels(false);
        piechart.setEntryLabelColor(activity.getResources().getColor(R.color.white));
        piechart.setNoDataTextTypeface(tf);
        piechart.setNoDataTextTypeface(tf);
        piechart.setEntryLabelTypeface(tf);
        piechart.setCenterTextColor(activity.getResources().getColor(R.color.white));


        piechart.setData(pidata);
    }
}
