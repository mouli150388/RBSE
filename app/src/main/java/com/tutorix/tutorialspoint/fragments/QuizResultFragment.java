package com.tutorix.tutorialspoint.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tutorix.tutorialspoint.R;
import com.tutorix.tutorialspoint.models.QUizResultModel;
import com.tutorix.tutorialspoint.utility.CommonUtils;

import java.util.Calendar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {link QuizResultFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {link QuizResultFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class QuizResultFragment extends DialogFragment {


    int selectedAns;
    int wrongAnswers;
    int score;
    String userid;
    String quiz_id;
    String currentTime;
    int Minute;
    int Sec;
    public QuizResultFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle b=getArguments();
        QUizResultModel model=b.getParcelable("data");
         score =model.score;
        selectedAns =model.selectedAns;
        wrongAnswers =model.wrongAnswers;
        userid =model.userid;
        currentTime =model.currentTime;
        quiz_id =model.quiz_id;
        Minute =model.Minute;
        Sec =model.Sec;



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_quiz_result, container, false);
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

      /*  AlertDialog.Builder  alertDialog = new AlertDialog.Builder(getActivity(), R.style.DialogTheme_notrans);
        alertDialog.setCancelable(false);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.alert_label_editor, null);
        //alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        // alertDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        alertDialog.setView(dialogView);
        TextView abc = dialogView.findViewById(R.id.abc);
        ProgressBar progressBar = dialogView.findViewById(R.id.progressBar);

        //View lnr_container = dialogView.findViewById(R.id.lnr_container);
        //lnr_container.setBackgroundColor(getResources().getColor(color_theme));


        // PieChart pieChart = dialogView.findViewById(R.id.piechart);
        final TextView time = dialogView.findViewById(R.id.time);
        ObjectAnimator scaleDown = ObjectAnimator.ofPropertyValuesHolder(time,
                PropertyValuesHolder.ofFloat("scaleX", 1.2f),
                PropertyValuesHolder.ofFloat("scaleY", 1.2f));
        scaleDown.setDuration(300);
        //pieChart.setContentDescription("");

        scaleDown.setRepeatCount(ObjectAnimator.INFINITE);
        scaleDown.setRepeatMode(ObjectAnimator.REVERSE);
        scaleDown.start();
        TextView selected = dialogView.findViewById(R.id.selected);
        TextView txt_grade = dialogView.findViewById(R.id.txt_grade);
        String selectedAnswer = String.valueOf(selectedAns) ;
        selected.setText(selectedAnswer);
        TextView correct = dialogView.findViewById(R.id.correct);

        TextView wrong = dialogView.findViewById(R.id.wrong);
        Button Review = dialogView.findViewById(R.id.Review);
        Button Quit = dialogView.findViewById(R.id.Quit);
        Button Retake = dialogView.findViewById(R.id.Retake);

        String correctAnswer = String.valueOf(score) ;
        correct.setText(correctAnswer);
        String wrongAnswer = String.valueOf(wrongAnswers) ;
        wrong.setText(wrongAnswer);
        try {
            int num = (100 * score) / QuizRulesActivity.getQuesList().length();

            abc.setText(String.valueOf(num)+"%");
            if(num>=90)
                txt_grade.setText("A");
            else if(num>=80)
                txt_grade.setText("B");
            else if(num>=60)
                txt_grade.setText("c");
            else  if(num>=40)
                txt_grade.setText("D");
            else txt_grade.setText("0");

            progressBar.setMax(100);
            progressBar.setProgress(num);
            progressBar.setSecondaryProgress(100-num);
        }catch (Exception e)
        {
            abc.setText("0");
            e.printStackTrace();
        }
        String format = "%1$02d";
        String timeTodis = String.format(format, Minute) + "min " + String.format(format, Sec) + "sec";
        time.setText(timeTodis);
        Review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), ReviewQuiz.class);
                i.putExtra(Constants.userId, userid);
                i.putExtra("quiz_id", quiz_id);
                i.putExtra("lecture_time", getDateTime());

                startActivity(i);
                getActivity().overridePendingTransition(R.anim.right_in, R.anim.left_out);

            }
        });
        Quit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //quesIndex = 0;

                getActivity().finish();
            }
        });
        Retake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();

            }
        });

*/
        return null;
    }
    private String getDateTime() {


        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(Long.valueOf(currentTime));
        return CommonUtils.format.format(calendar.getTime());

    }
}
