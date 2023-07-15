package com.tutorix.tutorialspoint.adapters;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.tutorix.tutorialspoint.AppConfig;
import com.tutorix.tutorialspoint.AppController;
import com.tutorix.tutorialspoint.R;
import com.tutorix.tutorialspoint.Security;
import com.tutorix.tutorialspoint.SessionManager;
import com.tutorix.tutorialspoint.SharedPref;
import com.tutorix.tutorialspoint.activities.EditNotesActivity;
import com.tutorix.tutorialspoint.activities.LecturesActivity;
import com.tutorix.tutorialspoint.activities.NotesActivity;
import com.tutorix.tutorialspoint.activities.SubscribePrePage;
import com.tutorix.tutorialspoint.activities.TrackMainActivity;
import com.tutorix.tutorialspoint.database.MyDatabase;
import com.tutorix.tutorialspoint.doubts.DoubtsActivity;
import com.tutorix.tutorialspoint.models.ActivationDetails;
import com.tutorix.tutorialspoint.models.SubChapters;
import com.tutorix.tutorialspoint.ncert.NCERTListActivity;
import com.tutorix.tutorialspoint.quiz.QuizMockExamActivity;
import com.tutorix.tutorialspoint.quiz.QuizRulesActivity;
import com.tutorix.tutorialspoint.utility.AppStatus;
import com.tutorix.tutorialspoint.utility.CommonUtils;
import com.tutorix.tutorialspoint.utility.Constants;
import com.tutorix.tutorialspoint.video.VideoActivityMVP;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class SubAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int FOOTER_VIEW = 1;
    private final Activity context;
    private final ArrayList<SubChapters> data;
    //private Activity mContext;
    private MyDatabase dbHelper;
    private String loginType;
    private String access_token;
    private boolean footerShow;
    private boolean hasDemo=true;
    float offset1;
    float offset2;
    float offset3;
    float offset4;
    boolean isBookmarks;
    int chapter_text_color;
    int chapter_selected_text_color;
    String section_name;
    String subject_id="";
    int background_drawable;
    int bk_background_drawable=R.drawable.ic_default_bg;
    boolean isActiveExpired=false;
    String subject_name;
    boolean isallVideos=true;
    int current_class_id=-1;
    public SubAdapter(ArrayList<SubChapters> data,String section_name, Activity context, boolean footerShow,boolean isBookmarks,boolean isallVideos) {
        this.data = data;
        this.section_name = section_name;
        this.context = context;
        this.footerShow = footerShow;

        dbHelper = MyDatabase.getDatabase(context);
        this.isBookmarks=isBookmarks;

        this.isallVideos=isallVideos;
        String[] userInfo = SessionManager.getUserInfo(context);
        loginType = userInfo[2];
        loginType = userInfo[2];
        access_token = userInfo[1];
        chapter_text_color=context.getResources().getColor(R.color.chapter_text_color);
        chapter_selected_text_color=context.getResources().getColor(R.color.chapter_selected_text_color);
      String  classId = userInfo[4];
        current_class_id= Integer.parseInt(classId);


        MyDatabase db = MyDatabase.getDatabase(context);
        ActivationDetails aDetails = db.activationDAO().getActivationDetails(userInfo[0], userInfo[4]);

        SharedPref sh=new SharedPref();
        if(sh.isExpired(context))
        {
            isActiveExpired=true;
        }
       /* if (aDetails != null && aDetails.activation_start_date != null && aDetails.activation_start_date.trim().length() > 0) {

            boolean date = CommonUtils.checkActivation(aDetails.activation_start_date, aDetails.activation_end_date);
            if (!date ) {
                isActiveExpired=true;

                //return;
            }
        }*/
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == FOOTER_VIEW) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.footer, parent, false);
            return new FooterHolder(itemView);
        }
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.subchapters, parent, false);
        return new MyViewHolder(itemView);
    }

    private MyViewHolder previousHolder = null;

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MyViewHolder) {
            final MyViewHolder vh = (MyViewHolder) holder;

            subject_id=data.get(holder.getAdapterPosition()).subjectid;

            vh.itemView.setVisibility(View.VISIBLE);
            if(subject_id.equals("1"))
            {
                background_drawable=R.drawable.ic_phy_notes;
                bk_background_drawable=R.drawable.ic_default_bg_phy;

                //vh.playButton.setImageResource(R.drawable.ic_phy_play);
                vh.playButton.setBackgroundResource(R.drawable.circle_color_phy);
                chapter_text_color=context.getResources().getColor(R.color.phy_background);
                subject_name=context.getString(R.string.physics);
            }else if(subject_id.equals("2"))
            {
                background_drawable=R.drawable.ic_che_notes;
                bk_background_drawable=R.drawable.ic_default_bg_che;
               /* vh.lectureNotes.setBackgroundResource(R.drawable.ic_che_notes);
                vh.quiz.setBackgroundResource(R.drawable.ic_che_notes);
                vh.track.setBackgroundResource(R.drawable.ic_che_notes);*/
                chapter_text_color=context.getResources().getColor(R.color.che_background);
                //vh.playButton.setImageResource(R.drawable.ic_che_play);
                vh.playButton.setBackgroundResource(R.drawable.circle_color_che);
                subject_name=context.getString(R.string.chemistry);
            }else if(subject_id.equals("3"))
            {
                background_drawable=R.drawable.ic_bio_notes;
                bk_background_drawable=R.drawable.ic_default_bg_bio;
               /* vh.lectureNotes.setBackgroundResource(R.drawable.ic_bio_notes);
                vh.quiz.setBackgroundResource(R.drawable.ic_bio_notes);
                vh.track.setBackgroundResource(R.drawable.ic_bio_notes);*/
                //vh.playButton.setImageResource(R.drawable.ic_bio_play);
                vh.playButton.setBackgroundResource(R.drawable.circle_color_bio);
                chapter_text_color=context.getResources().getColor(R.color.bio_background);
                subject_name=context.getString(R.string.biology);
            }else if(subject_id.equals("4"))
            {
                background_drawable=R.drawable.ic_math_notes;
                bk_background_drawable=R.drawable.ic_default_bg_math;
               /* vh.lectureNotes.setBackgroundResource(R.drawable.ic_math_notes);
                vh.quiz.setBackgroundResource(R.drawable.ic_math_notes);
                vh.track.setBackgroundResource(R.drawable.ic_math_notes);*/
               // vh.playButton.setImageResource(R.drawable.ic_math_play);
                vh.playButton.setBackgroundResource(R.drawable.circle_color_math);
                chapter_text_color=context.getResources().getColor(R.color.math_background);
                subject_name=context.getString(R.string.physics);
            }
            vh.lectureNotes.setBackgroundResource(background_drawable);
            vh.quiz.setBackgroundResource(background_drawable);
            vh.track.setBackgroundResource(background_drawable);

            vh.chapterName.setTextColor(chapter_text_color);
            if (data.get(holder.getAdapterPosition()).lecture_bookmark &&
                    data.get(holder.getAdapterPosition()).lecture_completed ) {
                vh.lectureStatus.setImageResource(R.drawable.ic_fav_completed_bg);
            }else  if (data.get(holder.getAdapterPosition()).lecture_bookmark ) {
                vh.lectureStatus.setImageResource(R.drawable.ic_fav_bg);
            }else if (data.get(holder.getAdapterPosition()).lecture_completed) {
                vh.lectureStatus.setImageResource(R.drawable.ic_completed_bg);
            }
            else {
                vh.lectureStatus.setImageResource(bk_background_drawable);
            }

           /* if(data.get(holder.getAdapterPosition()).is_notes)
                vh.notes_availe.setVisibility(View.VISIBLE);
            else*/
                vh.notes_availe.setVisibility(View.INVISIBLE);

            if ((isActiveExpired)&&!data.get(position).is_demo) {
                vh.lnr_demo.setVisibility(View.VISIBLE);
                hasDemo = false;
            } else {

                vh.lnr_demo.setVisibility(View.GONE);
                vh.lnr_play.setVisibility(View.VISIBLE);
                if(!data.get(position).status.equalsIgnoreCase("P"))
                {
                    vh.lnr_demo.setVisibility(View.VISIBLE);
                    vh.img_lock.setVisibility(View.GONE);
                    vh.txt_subscribe.setVisibility(View.VISIBLE);
                    vh.txt_subscribe.setText("Coming Soon!");
                    vh.lnr_play.setVisibility(View.INVISIBLE);
                }
            }

            vh.txt_subscribe.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);

            vh.lnr_options.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    vh.lnr_options.getViewTreeObserver().removeOnPreDrawListener(this);
                    offset1 = vh.showLayout.getX()-vh.completed.getX() ;
                    vh.completed.setTranslationX(offset1);
                    offset2 = vh.showLayout.getX()- vh.favourite.getX();
                    vh.favourite.setTranslationX(offset2);
                    offset3 = vh.showLayout.getX() - vh.notes.getX();
                    vh.notes.setTranslationX(offset3);
                    offset4 = vh.showLayout.getX()-vh.doubts.getX();
                    vh.doubts.setTranslationX(offset4);
                    return true;
                }
            });

           /* vh.completed.clearAnimation();
            vh.favourite.clearAnimation();
            vh.doubts.clearAnimation();
            vh.notes.clearAnimation();*/

         /*   switch (data.get(0).subjectid)
            {
                case "1":
                    vh.lnr_demo.setBackgroundColor(context.getResources().getColor(R.color.phy_background_status_trns));
                    break;
                case "2":
                    vh.lnr_demo.setBackgroundColor(context.getResources().getColor(R.color.che_background_status_trns));
                    break;
                case "3":
                    vh.lnr_demo.setBackgroundColor(context.getResources().getColor(R.color.bio_background_status_trns));
                    break;
                case "4":
                    vh.lnr_demo.setBackgroundColor(context.getResources().getColor(R.color.math_background_status_trns));;
                    break;

            }*/

            vh.lnr_demo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AppController.getInstance().playAudio(R.raw.button_click);
                    if(!data.get(position).status.equalsIgnoreCase("P"))
                    {
                        CommonUtils.showToast(context,"Coming Soon!");
                        return;
                    }
                    Intent i = new Intent(context, SubscribePrePage.class);
                    i.putExtra("flag", "M");
                    context.startActivity(i);
                }
            });
            vh.lectureStatus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AppController.getInstance().playAudio(R.raw.bookmark_sound);
                    if(!data.get(position).status.equalsIgnoreCase("P"))
                    {
                        CommonUtils.showToast(context,"Coming Soon!");
                        return;
                    }
                    if ((isActiveExpired)&&!data.get(position).is_demo) {
                        Intent i = new Intent(context, SubscribePrePage.class);
                        i.putExtra("flag", "M");
                        context.startActivity(i);
                        return;
                    }


                    if (vh.notes.getVisibility() == View.VISIBLE) {
                        hideViewWithAnimation(vh);
                        //((MyViewHolder) holder).bodyLayout.setAlpha(1f);
                        previousHolder = null;
                    } else {
                        if (previousHolder != null) {
                            hideViewWithAnimation(previousHolder);
                            // previousHolder.bodyLayout.setAlpha(1f);
                        }
                        showViewWithAnimation(vh);
                        //((MyViewHolder) holder).bodyLayout.setAlpha(0.2f);
                        previousHolder = (MyViewHolder) holder;
                    }
                }
            });

            vh.completed.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /*vh.layoutMessage.animate()
                            .translationY(vh.layoutMessage.getHeight())
                            .alpha(0.1f)
                            .setDuration(3000)
                            .setListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    super.onAnimationEnd(animation);
                                    vh.layoutMessage.setVisibility(View.VISIBLE);
                                }
                            });*/
                    if(!data.get(vh.getAdapterPosition()).status.equalsIgnoreCase("P"))
                    {
                        CommonUtils.showToast(context,"Coming Soon!");
                        return;
                    }
                    hideViewWithAnimation(vh);
                    completedClick(vh);
                }
            });

            vh.favourite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!data.get(vh.getAdapterPosition()).status.equalsIgnoreCase("P"))
                    {
                        CommonUtils.showToast(context,"Coming Soon!");
                        return;
                    }
                    hideViewWithAnimation(vh);
                    favouriteClick(vh);
                }
            });

            vh.notes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!data.get(vh.getAdapterPosition()).status.equalsIgnoreCase("P"))
                    {
                        CommonUtils.showToast(context,"Coming Soon!");
                        return;
                    }
                    //Log.v("onactivityResult","onactivity Result calling 1 ");
                    AppController.getInstance().playAudio(R.raw.button_click);
                    if(context instanceof LecturesActivity)
                    {
                        ((LecturesActivity)context).NotesAactivityCall(data.get(holder.getAdapterPosition()));
                       // Log.v("onactivityResult","onactivity Result calling 2 ");
                        return;
                    }
                    Intent i = new Intent(context, EditNotesActivity.class);
                    i.putExtra(Constants.userId, data.get(holder.getAdapterPosition()).userid);
                    i.putExtra(Constants.lectureId, data.get(holder.getAdapterPosition()).lecture_id);
                    i.putExtra(Constants.classId, data.get(holder.getAdapterPosition()).classid);
                    i.putExtra(Constants.subjectId, data.get(holder.getAdapterPosition()).subjectid);
                    i.putExtra(Constants.sectionId, data.get(holder.getAdapterPosition()).section_id);
                    i.putExtra(Constants.lectureName, data.get(holder.getAdapterPosition()).txt);
                    i.putExtra("isNotes", false);
                    //i.putExtra("subchapter",data.get(holder.getAdapterPosition()));
                    i.addFlags(FLAG_ACTIVITY_NEW_TASK);
                    context.startActivityForResult(i,200);
                    //mContext = (Activity) vh.itemView.getContext();
                    context.overridePendingTransition(R.anim.right_in, R.anim.left_out);
                }
            });

            vh.doubts.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(!data.get(vh.getAdapterPosition()).status.equalsIgnoreCase("P"))
                    {
                        CommonUtils.showToast(context,"Coming Soon!");
                        return;
                    }
                    AppController.getInstance().playAudio(R.raw.button_click);
                    if (!AppStatus.getInstance(context).isOnline()) {
                        CommonUtils.showToast(context, context.getString(R.string.no_internet));
                        return;
                    }
                   /* if (loginType.isEmpty())
                    {
                        Dialog dialog=new Dialog(context);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                        dialog.setContentView(R.layout.dialog_alert);
                        TextView txt_msg=dialog.findViewById(R.id.txt_msg);
                        txt_msg.setText(context.getResources().getString(R.string.service_availabile));
                        TextView txt_cancel=dialog.findViewById(R.id.txt_cancel);
                        TextView txt_ok=dialog.findViewById(R.id.txt_ok);
                        txt_ok.setText("Ok");
                        txt_cancel.setVisibility(View.GONE);
                        txt_cancel.setText("Cancel");
                        txt_cancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });
                        txt_ok.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();

                            }
                        });
                        dialog.show();
                        return;
                    }*/

                    Intent i = new Intent(context, DoubtsActivity.class);
                    i.putExtra(Constants.userId, data.get(holder.getAdapterPosition()).userid);
                    i.putExtra(Constants.subjectId, data.get(holder.getAdapterPosition()).subjectid);
                    i.addFlags(FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(i);
                    //mContext = (Activity) vh.itemView.getContext();
                    context.overridePendingTransition(R.anim.right_in, R.anim.left_out);
                }
            });

            vh.chapterName.setText(data.get(holder.getAdapterPosition()).txt);
            String duration=data.get(holder.getAdapterPosition()).lecture_duration;
            if(duration.startsWith("00:"))
                duration=duration.replace("00:","");
             duration = context.getResources().getString(R.string.duration) + " - " + duration;
            if(!footerShow)
            vh.durationTVID.setText(subject_name+", "+duration);
            else
            vh.durationTVID.setText(duration);

            if (data.get(holder.getAdapterPosition()).selectedlecture_id != null) {
                if (data.get(holder.getAdapterPosition()).lecture_id.equalsIgnoreCase(data.get(holder.getAdapterPosition()).selectedlecture_id)) {
                    vh.chapterName.setTextColor(chapter_selected_text_color);
                    vh.playButton.setVisibility(View.GONE);
                } else {
                    vh.chapterName.setTextColor(chapter_text_color);
                    vh.playButton.setVisibility(View.VISIBLE);
                }
            }

            vh.chapterName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    videoChapter(holder.getAdapterPosition());
                }
            }); vh.durationTVID.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    videoChapter(holder.getAdapterPosition());
                }
            });
            vh.lnr_play.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    videoChapter(holder.getAdapterPosition());
                }
            });
            vh.playButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    videoChapter(holder.getAdapterPosition());
                }
            });
            vh.bodyLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    videoChapter(holder.getAdapterPosition());
                }
            });

            vh.quiz.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(!data.get(holder.getAdapterPosition()).status.equalsIgnoreCase("P"))
                    {
                        CommonUtils.showToast(context,"Coming Soon!");
                        return;
                    }
                    AppController.getInstance().playAudio(R.raw.button_click);
                   /* if(data.get(holder.getAdapterPosition()).lecture_quiz.isEmpty())
                    {
                        if(context instanceof VideoActivity)
                            ((VideoActivity)context).showSnackbar();
                        else  if(context instanceof InternalChapterActivity)
                            ((InternalChapterActivity)context).showSnackbar();
                        return;
                    }*/


                    Intent i = new Intent(context, QuizRulesActivity.class);
                    i.setFlags(FLAG_ACTIVITY_NEW_TASK);
                    i.putExtra(Constants.lectureId, data.get(holder.getAdapterPosition()).lecture_id);
                    i.putExtra(Constants.sectionName, section_name);
                    i.putExtra(Constants.subjectId, data.get(holder.getAdapterPosition()).subjectid);
                    i.putExtra(Constants.classId, data.get(holder.getAdapterPosition()).classid);
                    i.putExtra(Constants.userId, data.get(holder.getAdapterPosition()).userid);
                    i.putExtra(Constants.sectionId, data.get(holder.getAdapterPosition()).section_id);
                    i.putExtra(Constants.lectureName, data.get(holder.getAdapterPosition()).txt);
                    context.startActivity(i);
                   // mContext = (Activity) vh.itemView.getContext();
                    context.overridePendingTransition(R.anim.right_in, R.anim.left_out);


                }


            });

            vh.lectureNotes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(!data.get(holder.getAdapterPosition()).status.equalsIgnoreCase("P"))
                    {
                        CommonUtils.showToast(context,"Coming Soon!");
                        return;
                    }
                    AppController.getInstance().playAudio(R.raw.button_click);
                    Intent i = new Intent(context, NotesActivity.class);
                    i.setFlags(FLAG_ACTIVITY_NEW_TASK);
                    i.putExtra(Constants.lectureId, data.get(holder.getAdapterPosition()).lecture_id);

                    i.putExtra(Constants.subjectId, data.get(holder.getAdapterPosition()).subjectid);
                    i.putExtra(Constants.classId, data.get(holder.getAdapterPosition()).classid);
                    i.putExtra(Constants.userId, data.get(holder.getAdapterPosition()).userid);
                    i.putExtra(Constants.sectionId, data.get(holder.getAdapterPosition()).section_id);
                    i.putExtra(Constants.lectureName, data.get(holder.getAdapterPosition()).txt);
                    context.startActivity(i);
                    //mContext = (Activity) vh.itemView.getContext();
                    context.overridePendingTransition(R.anim.right_in, R.anim.left_out);
                }
            });

            vh.track.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(!data.get(holder.getAdapterPosition()).status.equalsIgnoreCase("P"))
                    {
                        CommonUtils.showToast(context,"Coming Soon!");
                        return;
                    }
                    AppController.getInstance().playAudio(R.raw.button_click);
                    Intent t = new Intent(context, TrackMainActivity.class);
                    t.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    t.putExtra(Constants.lectureId, data.get(holder.getAdapterPosition()).lecture_id);
                    t.putExtra(Constants.subjectId, data.get(holder.getAdapterPosition()).subjectid);
                    t.putExtra(Constants.classId, data.get(holder.getAdapterPosition()).classid);
                    t.putExtra(Constants.userId, data.get(holder.getAdapterPosition()).userid);
                    t.putExtra(Constants.sectionId, data.get(holder.getAdapterPosition()).section_id);
                    t.putExtra(Constants.intentType, Constants.lecture);
                    t.putExtra(Constants.lectureName, data.get(holder.getAdapterPosition()).txt);
                    context.startActivity(t);
                    //mContext = (Activity) vh.itemView.getContext();
                    context.overridePendingTransition(R.anim.right_in, R.anim.left_out);
                }
            });


           /* switch (data.get(position).subjectid) {
                case "1":
                    vh.playButton.setImageResource(R.drawable.ic_play_phy);
                    vh.img_lock.setColorFilter(ContextCompat.getColor(context, R.color.phy_background), PorterDuff.Mode.SRC_IN);
                    vh.track.setBackgroundResource(R.drawable.notes_phy);
                    vh.lectureNotes.setBackgroundResource(R.drawable.notes_phy);
                    vh.quiz.setBackgroundResource(R.drawable.notes_phy);
                    break;
                case "2":
                    vh.img_lock.setColorFilter(ContextCompat.getColor(context, R.color.che_background), PorterDuff.Mode.SRC_IN);
                    vh.playButton.setImageResource(R.drawable.ic_play);
                    vh.track.setBackgroundResource(R.drawable.notes);
                    vh.lectureNotes.setBackgroundResource(R.drawable.notes);
                    vh.quiz.setBackgroundResource(R.drawable.notes);
                    break;
                case "3":
                    vh.playButton.setImageResource(R.drawable.ic_play_bio);
                    vh.img_lock.setColorFilter(ContextCompat.getColor(context, R.color.bio_background), PorterDuff.Mode.SRC_IN);
                    vh.track.setBackgroundResource(R.drawable.notes_bio);
                    vh.lectureNotes.setBackgroundResource(R.drawable.notes_bio);
                    vh.quiz.setBackgroundResource(R.drawable.notes_bio);
                    break;
                case "4":
                    vh.img_lock.setColorFilter(ContextCompat.getColor(context, R.color.math_background), PorterDuff.Mode.SRC_IN);
                    vh.playButton.setImageResource(R.drawable.ic_play_math);
                    vh.track.setBackgroundResource(R.drawable.notes_math);
                    vh.lectureNotes.setBackgroundResource(R.drawable.notes_math);
                    vh.quiz.setBackgroundResource(R.drawable.notes_math);
                    break;
            }*/
        } else if (holder instanceof FooterHolder) {
            FooterHolder vh = (FooterHolder) holder;
            vh.fab.setEnabled(true);
            if(!isallVideos)
            {
                vh.fab.setVisibility(View.GONE);
                vh.lnr_footer_coming.setVisibility(View.VISIBLE);
            }else
            {
                if(data.size()==0)
                {
                    vh.fab.setVisibility(View.GONE);
                    vh.lnr_footer_coming.setVisibility(View.VISIBLE);
                }else {
                    vh.fab.setVisibility(View.VISIBLE);
                    vh.lnr_footer_coming.setVisibility(View.GONE);
                }
            }
            /*if (hasDemo) {
                vh.fab.setEnabled(true);
            }*/

          /*  if (data.size() > 0) {
                switch (data.get(0).subjectid) {
                    case "1":
                        vh.fab.setBackgroundResource(R.drawable.button_selector_phy);

                        break;
                    case "2":
                        vh.fab.setBackgroundResource(R.drawable.button_selector);
                        break;
                    case "3":
                        vh.fab.setBackgroundResource(R.drawable.button_selector_bio);
                        break;
                    case "4":
                        vh.fab.setBackgroundResource(R.drawable.button_selector_math);
                        break;

                }
            }*/

            vh.fab.setBackgroundResource( background_drawable);
            vh.ncert.setBackgroundResource( background_drawable);
            vh.fab.setTextColor(chapter_text_color);
            vh.ncert.setTextColor(chapter_text_color);
            vh.fab.setTag(holder.getAdapterPosition());
            vh.ncert.setTag(holder.getAdapterPosition());
        }
    }
    private Animator createCollapseAnimator(View view, float offset) {
        return ObjectAnimator.ofFloat(view, "translationX", 0, offset)
                .setDuration(300);
    }
    private Animator createExpandAnimator(View view, float offset) {
        return ObjectAnimator.ofFloat(view, "translationX", offset, 0)
                .setDuration(300);
    }
    private void showViewWithAnimation(final MyViewHolder viewHolder) {
        //int currentAnim = Android.Resource.Animation.SlideInLeft;
       /* viewHolder.completed.clearAnimation();
        viewHolder.favourite.clearAnimation();
        viewHolder.doubts.clearAnimation();
        viewHolder.notes.clearAnimation();*/

        viewHolder.completed.setVisibility(View.VISIBLE);
        viewHolder.favourite.setVisibility(View.VISIBLE);
        viewHolder.notes.setVisibility(View.VISIBLE);
        //viewHolder.doubts.setVisibility(View.VISIBLE);

       final AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(createExpandAnimator(viewHolder.completed, offset1),
                createExpandAnimator(viewHolder.favourite, offset2),
                createExpandAnimator(viewHolder.notes, offset3),
                createExpandAnimator(viewHolder.doubts, offset3));
        animatorSet.setDuration(300);
        /*viewHolder.lnr_options.postDelayed(new Runnable() {
            @Override
            public void run() {
                viewHolder.completed.clearAnimation();
                viewHolder.favourite.clearAnimation();
                viewHolder.doubts.clearAnimation();
                viewHolder.notes.clearAnimation();

            }
        },300);*/
        animatorSet.start();






    }

    private void hideViewWithAnimation(final MyViewHolder viewHolder) {
/*
        viewHolder.completed.clearAnimation();
        viewHolder.favourite.clearAnimation();
        viewHolder.doubts.clearAnimation();
        viewHolder.notes.clearAnimation();*/

       final AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(createCollapseAnimator(viewHolder.completed, offset1),
                createCollapseAnimator(viewHolder.favourite, offset2),
                createCollapseAnimator(viewHolder.notes, offset3),
                createCollapseAnimator(viewHolder.doubts, offset3));
        animatorSet.setDuration(250);
        animatorSet.start();
        viewHolder.lnr_options.postDelayed(new Runnable() {
            @Override
            public void run() {
                viewHolder.completed.setVisibility(View.INVISIBLE);
                viewHolder.favourite.setVisibility(View.INVISIBLE);
                viewHolder.notes.setVisibility(View.INVISIBLE);
                //viewHolder.doubts.setVisibility(View.INVISIBLE);
               /* viewHolder.completed.clearAnimation();
                viewHolder.favourite.clearAnimation();
                viewHolder.doubts.clearAnimation();
                viewHolder.notes.clearAnimation();*/

            }
        },220);

    }

    private void favouriteClick(final MyViewHolder holder) {


        SubChapters d=data.get(holder.getAdapterPosition());
        if(/*AppConfig.checkSDCardEnabled(context,d.userid,d.classid)&&*/AppConfig.checkSdcard(d.classid,context))
        {

            boolean completed = data.get(holder.getAdapterPosition()).lecture_bookmark;

            data.get(holder.getAdapterPosition()).lecture_bookmark = !completed;
            dbHelper = MyDatabase.getDatabase(context);
            data.get(holder.getAdapterPosition()).createdDtm = getDateTime();
            if (AppStatus.getInstance(context).isOnline()) {

                action(data.get(holder.getAdapterPosition()));
                data.get(holder.getAdapterPosition()).is_sync = true;
                if (!data.get(holder.getAdapterPosition()).lecture_bookmark && !data.get(holder.getAdapterPosition()).lecture_completed&&!data.get(holder.getAdapterPosition()).is_notes) {
                    SubChapters subChapters = data.get(holder.getAdapterPosition());
                    dbHelper.subjectChapterDAO().deleteBookmark(subChapters.userid, subChapters.classid, subChapters.section_id, subChapters.lecture_id, subChapters.subjectid);
                } else {
                    SubChapters subChapters = data.get(holder.getAdapterPosition());
                    SubChapters subChapter = dbHelper.subjectChapterDAO().getBookmarkorData(
                            subChapters.userid, subChapters.classid,
                            subChapters.section_id, subChapters.lecture_id
                            , subChapters.subjectid
                    );
                    if(subChapter==null) {
                        data.get(holder.getAdapterPosition()).lecture_notes="";
                        dbHelper.subjectChapterDAO().addBookMark(data.get(holder.getAdapterPosition()));
                    }
                    else
                        dbHelper.subjectChapterDAO().updateBookmarkCompleted(subChapters.userid, subChapters.classid, subChapters.section_id, subChapters.lecture_id, subChapters.subjectid,subChapters.is_sync,subChapters.lecture_bookmark,subChapters.lecture_completed,subChapters.is_notes);

                }
            } else {
                data.get(holder.getAdapterPosition()).is_sync = false;
                if (!data.get(holder.getAdapterPosition()).lecture_bookmark && !data.get(holder.getAdapterPosition()).lecture_completed) {
                    SubChapters subChapters = data.get(holder.getAdapterPosition());
                    dbHelper.subjectChapterDAO().deleteBookmark(subChapters.userid, subChapters.classid, subChapters.section_id, subChapters.lecture_id, subChapters.subjectid);
                } else {
                    SubChapters subChapters = data.get(holder.getAdapterPosition());
                    SubChapters subChapter = dbHelper.subjectChapterDAO().getBookmarkorData(
                            subChapters.userid, subChapters.classid,
                            subChapters.section_id, subChapters.lecture_id
                            , subChapters.subjectid
                    );
                    if(subChapter==null) {
                        data.get(holder.getAdapterPosition()).lecture_notes="";
                        dbHelper.subjectChapterDAO().addBookMark(data.get(holder.getAdapterPosition()));
                    }
                    else
                        dbHelper.subjectChapterDAO().updateBookmarkCompleted(subChapters.userid, subChapters.classid, subChapters.section_id, subChapters.lecture_id, subChapters.subjectid,subChapters.is_sync,subChapters.lecture_bookmark,subChapters.lecture_completed,subChapters.is_notes);

                }
            }
        }else
        {
            if (AppStatus.getInstance(context).isOnline()) {
                data.get(holder.getAdapterPosition()).lecture_bookmark = !data.get(holder.getAdapterPosition()).lecture_bookmark;
                action(data.get(holder.getAdapterPosition()));
            } else {
                CommonUtils.showToast(context, context.getString(R.string.no_internet));
                // Toasty.info(context, "There is no internet.", Toast.LENGTH_SHORT, true).show();
                return;
            }
        }
       /* if(loginType.isEmpty())
        {
            if (AppStatus.getInstance(context).isOnline()) {
                data.get(holder.getAdapterPosition()).lecture_bookmark = !data.get(holder.getAdapterPosition()).lecture_bookmark;
                action(data.get(holder.getAdapterPosition()));
            } else {
                CommonUtils.showToast(context, context.getString(R.string.no_internet));
                // Toasty.info(context, "There is no internet.", Toast.LENGTH_SHORT, true).show();
                return;
            }
        }else if (loginType.equalsIgnoreCase("O")){

            if(AppConfig.checkSDCardEnabled(context,d.userid,d.classid)&&AppConfig.checkSdcard(d.classid,context))
            {

                boolean completed = data.get(holder.getAdapterPosition()).lecture_bookmark;

                data.get(holder.getAdapterPosition()).lecture_bookmark = !completed;
                dbHelper = MyDatabase.getDatabase(context);
                data.get(holder.getAdapterPosition()).createdDtm = getDateTime();
                if (AppStatus.getInstance(context).isOnline()) {

                    action(data.get(holder.getAdapterPosition()));
                    data.get(holder.getAdapterPosition()).is_sync = true;
                    if (!data.get(holder.getAdapterPosition()).lecture_bookmark && !data.get(holder.getAdapterPosition()).lecture_completed&&!data.get(holder.getAdapterPosition()).is_notes) {
                        SubChapters subChapters = data.get(holder.getAdapterPosition());
                        dbHelper.subjectChapterDAO().deleteBookmark(subChapters.userid, subChapters.classid, subChapters.section_id, subChapters.lecture_id, subChapters.subjectid);
                    } else {
                        SubChapters subChapters = data.get(holder.getAdapterPosition());
                        SubChapters subChapter = dbHelper.subjectChapterDAO().getBookmarkorData(
                                subChapters.userid, subChapters.classid,
                                subChapters.section_id, subChapters.lecture_id
                                , subChapters.subjectid
                        );
                        if(subChapter==null) {
                            data.get(holder.getAdapterPosition()).lecture_notes="";
                            dbHelper.subjectChapterDAO().addBookMark(data.get(holder.getAdapterPosition()));
                        }
                        else
                            dbHelper.subjectChapterDAO().updateBookmarkCompleted(subChapters.userid, subChapters.classid, subChapters.section_id, subChapters.lecture_id, subChapters.subjectid,subChapters.is_sync,subChapters.lecture_bookmark,subChapters.lecture_completed,subChapters.is_notes);

                    }
                } else {
                    data.get(holder.getAdapterPosition()).is_sync = false;
                    if (!data.get(holder.getAdapterPosition()).lecture_bookmark && !data.get(holder.getAdapterPosition()).lecture_completed) {
                        SubChapters subChapters = data.get(holder.getAdapterPosition());
                        dbHelper.subjectChapterDAO().deleteBookmark(subChapters.userid, subChapters.classid, subChapters.section_id, subChapters.lecture_id, subChapters.subjectid);
                    } else {
                        SubChapters subChapters = data.get(holder.getAdapterPosition());
                        SubChapters subChapter = dbHelper.subjectChapterDAO().getBookmarkorData(
                                subChapters.userid, subChapters.classid,
                                subChapters.section_id, subChapters.lecture_id
                                , subChapters.subjectid
                        );
                        if(subChapter==null) {
                            data.get(holder.getAdapterPosition()).lecture_notes="";
                            dbHelper.subjectChapterDAO().addBookMark(data.get(holder.getAdapterPosition()));
                        }
                        else
                            dbHelper.subjectChapterDAO().updateBookmarkCompleted(subChapters.userid, subChapters.classid, subChapters.section_id, subChapters.lecture_id, subChapters.subjectid,subChapters.is_sync,subChapters.lecture_bookmark,subChapters.lecture_completed,subChapters.is_notes);

                    }
                }
            }else
            {
                if (AppStatus.getInstance(context).isOnline()) {
                    data.get(holder.getAdapterPosition()).lecture_bookmark = !data.get(holder.getAdapterPosition()).lecture_bookmark;
                    action(data.get(holder.getAdapterPosition()));
                } else {
                    CommonUtils.showToast(context, context.getString(R.string.no_internet));
                    // Toasty.info(context, "There is no internet.", Toast.LENGTH_SHORT, true).show();
                    return;
                }
            }
        }else
        {

            boolean completed = data.get(holder.getAdapterPosition()).lecture_bookmark;

            data.get(holder.getAdapterPosition()).lecture_bookmark = !completed;
            dbHelper = MyDatabase.getDatabase(context);
            data.get(holder.getAdapterPosition()).createdDtm = getDateTime();
            if (AppStatus.getInstance(context).isOnline()) {

                action(data.get(holder.getAdapterPosition()));
                data.get(holder.getAdapterPosition()).is_sync = true;
                if (!data.get(holder.getAdapterPosition()).lecture_bookmark && !data.get(holder.getAdapterPosition()).lecture_completed&&!data.get(holder.getAdapterPosition()).is_notes) {
                    SubChapters subChapters = data.get(holder.getAdapterPosition());
                    dbHelper.subjectChapterDAO().deleteBookmark(subChapters.userid, subChapters.classid, subChapters.section_id, subChapters.lecture_id, subChapters.subjectid);
                } else {
                    SubChapters subChapters = data.get(holder.getAdapterPosition());
                    SubChapters subChapter = dbHelper.subjectChapterDAO().getBookmarkorData(
                            subChapters.userid, subChapters.classid,
                            subChapters.section_id, subChapters.lecture_id
                            , subChapters.subjectid
                    );
                    if(subChapter==null) {
                        data.get(holder.getAdapterPosition()).lecture_notes="";
                        dbHelper.subjectChapterDAO().addBookMark(data.get(holder.getAdapterPosition()));
                    }
                    else
                        dbHelper.subjectChapterDAO().updateBookmarkCompleted(subChapters.userid, subChapters.classid, subChapters.section_id, subChapters.lecture_id, subChapters.subjectid,subChapters.is_sync,subChapters.lecture_bookmark,subChapters.lecture_completed,subChapters.is_notes);

                }
            } else {
                data.get(holder.getAdapterPosition()).is_sync = false;
                if (!data.get(holder.getAdapterPosition()).lecture_bookmark && !data.get(holder.getAdapterPosition()).lecture_completed) {
                    SubChapters subChapters = data.get(holder.getAdapterPosition());
                    dbHelper.subjectChapterDAO().deleteBookmark(subChapters.userid, subChapters.classid, subChapters.section_id, subChapters.lecture_id, subChapters.subjectid);
                } else {
                    SubChapters subChapters = data.get(holder.getAdapterPosition());
                    SubChapters subChapter = dbHelper.subjectChapterDAO().getBookmarkorData(
                            subChapters.userid, subChapters.classid,
                            subChapters.section_id, subChapters.lecture_id
                            , subChapters.subjectid
                    );
                    if(subChapter==null) {
                        data.get(holder.getAdapterPosition()).lecture_notes="";
                        dbHelper.subjectChapterDAO().addBookMark(data.get(holder.getAdapterPosition()));
                    }
                    else
                        dbHelper.subjectChapterDAO().updateBookmarkCompleted(subChapters.userid, subChapters.classid, subChapters.section_id, subChapters.lecture_id, subChapters.subjectid,subChapters.is_sync,subChapters.lecture_bookmark,subChapters.lecture_completed,subChapters.is_notes);

                }
            }
        }*/


        //hideViewWithAnimation(holder);
        holder.favourite.postDelayed(new Runnable() {
            @Override
            public void run() {
                /*if(isBookmarks)
                {
                    if (!data.get(holder.getAdapterPosition()).lecture_bookmark && !data.get(holder.getAdapterPosition()).lecture_completed) {
                        data.remove(holder.getAdapterPosition());
                    }

                    notifyDataSetChanged();

                }else
                {*/
               /* holder.completed.setVisibility(View.INVISIBLE);
                holder.favourite.setVisibility(View.INVISIBLE);
                holder.notes.setVisibility(View.INVISIBLE);
                holder.doubts.setVisibility(View.INVISIBLE);*/
                    notifyItemChanged(holder.getAdapterPosition());
                //}

            }
        },0);
        //notifyItemChanged(holder.getAdapterPosition());
        //showStatus(previousHolder,holder.getAdapterPosition(),"Favourite Updated");

    }




    private void completedClick(final MyViewHolder holder) {

SubChapters d= data.get(holder.getAdapterPosition());
        data.get(holder.getAdapterPosition()).createdDtm = getDateTime();
        if(/*AppConfig.checkSDCardEnabled(context,d.userid,d.classid)&&*/AppConfig.checkSdcard(d.classid,context))
        {

            boolean completed = data.get(holder.getAdapterPosition()).lecture_completed;
            data.get(holder.getAdapterPosition()).lecture_completed = !completed;
            dbHelper = MyDatabase.getDatabase(context);
            if (AppStatus.getInstance(context).isOnline()) {
                action(data.get(holder.getAdapterPosition()));
                data.get(holder.getAdapterPosition()).is_sync = true;
                if (!data.get(holder.getAdapterPosition()).lecture_bookmark && !data.get(holder.getAdapterPosition()).lecture_completed&&!data.get(holder.getAdapterPosition()).is_notes) {
                    SubChapters subChapters = data.get(holder.getAdapterPosition());
                    dbHelper.subjectChapterDAO().deleteBookmark(subChapters.userid, subChapters.classid, subChapters.section_id, subChapters.lecture_id, subChapters.subjectid);
                } else {
                    SubChapters subChapters = data.get(holder.getAdapterPosition());
                    SubChapters subChapter = dbHelper.subjectChapterDAO().getBookmarkorData(
                            subChapters.userid, subChapters.classid,
                            subChapters.section_id, subChapters.lecture_id
                            , subChapters.subjectid
                    );
                    if(subChapter==null) {
                        data.get(holder.getAdapterPosition()).lecture_notes="";
                        data.get(holder.getAdapterPosition()).is_sync=true;
                        dbHelper.subjectChapterDAO().addBookMark(data.get(holder.getAdapterPosition()));
                    }
                    else
                        dbHelper.subjectChapterDAO().updateBookmarkCompleted(subChapters.userid, subChapters.classid, subChapters.section_id, subChapters.lecture_id, subChapters.subjectid,subChapters.is_sync,subChapters.lecture_bookmark,subChapters.lecture_completed,subChapters.is_notes);

                }
            } else {
                data.get(holder.getAdapterPosition()).is_sync = false;
                if (!data.get(holder.getAdapterPosition()).lecture_bookmark && !data.get(holder.getAdapterPosition()).lecture_completed) {
                    SubChapters subChapters = data.get(holder.getAdapterPosition());
                    dbHelper.subjectChapterDAO().deleteBookmark(subChapters.userid, subChapters.classid, subChapters.section_id, subChapters.lecture_id, subChapters.subjectid);
                } else {
                    SubChapters subChapters = data.get(holder.getAdapterPosition());
                    SubChapters subChapter = dbHelper.subjectChapterDAO().getBookmarkorData(
                            subChapters.userid, subChapters.classid,
                            subChapters.section_id, subChapters.lecture_id
                            , subChapters.subjectid
                    );
                    if(subChapter==null) {
                        data.get(holder.getAdapterPosition()).is_sync=true;
                        data.get(holder.getAdapterPosition()).lecture_notes="";
                        dbHelper.subjectChapterDAO().addBookMark(data.get(holder.getAdapterPosition()));
                    }
                    else
                        dbHelper.subjectChapterDAO().updateBookmarkCompleted(subChapters.userid, subChapters.classid, subChapters.section_id, subChapters.lecture_id, subChapters.subjectid,subChapters.is_sync,subChapters.lecture_bookmark,subChapters.lecture_completed,subChapters.is_notes);
                }
            }
        }else
        {
            if (AppStatus.getInstance(context).isOnline()) {
                data.get(holder.getAdapterPosition()).lecture_completed = !data.get(holder.getAdapterPosition()).lecture_completed;
                action(data.get(holder.getAdapterPosition()));
            } else {
                CommonUtils.showToast(context, context.getString(R.string.no_internet));
                return;
            }
        }
       /* if(loginType.isEmpty())
        {
            if (AppStatus.getInstance(context).isOnline()) {
                data.get(holder.getAdapterPosition()).lecture_completed = !data.get(holder.getAdapterPosition()).lecture_completed;
                action(data.get(holder.getAdapterPosition()));
            } else {
                CommonUtils.showToast(context, context.getString(R.string.no_internet));
                return;
            }
        }else if (loginType.equalsIgnoreCase("O")){

            if(AppConfig.checkSDCardEnabled(context,d.userid,d.classid)&&AppConfig.checkSdcard(d.classid,context))
            {

                boolean completed = data.get(holder.getAdapterPosition()).lecture_completed;
                data.get(holder.getAdapterPosition()).lecture_completed = !completed;
                dbHelper = MyDatabase.getDatabase(context);
                if (AppStatus.getInstance(context).isOnline()) {
                    action(data.get(holder.getAdapterPosition()));
                    data.get(holder.getAdapterPosition()).is_sync = true;
                    if (!data.get(holder.getAdapterPosition()).lecture_bookmark && !data.get(holder.getAdapterPosition()).lecture_completed&&!data.get(holder.getAdapterPosition()).is_notes) {
                        SubChapters subChapters = data.get(holder.getAdapterPosition());
                        dbHelper.subjectChapterDAO().deleteBookmark(subChapters.userid, subChapters.classid, subChapters.section_id, subChapters.lecture_id, subChapters.subjectid);
                    } else {
                        SubChapters subChapters = data.get(holder.getAdapterPosition());
                        SubChapters subChapter = dbHelper.subjectChapterDAO().getBookmarkorData(
                                subChapters.userid, subChapters.classid,
                                subChapters.section_id, subChapters.lecture_id
                                , subChapters.subjectid
                        );
                        if(subChapter==null) {
                            data.get(holder.getAdapterPosition()).lecture_notes="";
                            data.get(holder.getAdapterPosition()).is_sync=true;
                            dbHelper.subjectChapterDAO().addBookMark(data.get(holder.getAdapterPosition()));
                        }
                        else
                            dbHelper.subjectChapterDAO().updateBookmarkCompleted(subChapters.userid, subChapters.classid, subChapters.section_id, subChapters.lecture_id, subChapters.subjectid,subChapters.is_sync,subChapters.lecture_bookmark,subChapters.lecture_completed,subChapters.is_notes);

                    }
                } else {
                    data.get(holder.getAdapterPosition()).is_sync = false;
                    if (!data.get(holder.getAdapterPosition()).lecture_bookmark && !data.get(holder.getAdapterPosition()).lecture_completed) {
                        SubChapters subChapters = data.get(holder.getAdapterPosition());
                        dbHelper.subjectChapterDAO().deleteBookmark(subChapters.userid, subChapters.classid, subChapters.section_id, subChapters.lecture_id, subChapters.subjectid);
                    } else {
                        SubChapters subChapters = data.get(holder.getAdapterPosition());
                        SubChapters subChapter = dbHelper.subjectChapterDAO().getBookmarkorData(
                                subChapters.userid, subChapters.classid,
                                subChapters.section_id, subChapters.lecture_id
                                , subChapters.subjectid
                        );
                        if(subChapter==null) {
                            data.get(holder.getAdapterPosition()).is_sync=true;
                            data.get(holder.getAdapterPosition()).lecture_notes="";
                            dbHelper.subjectChapterDAO().addBookMark(data.get(holder.getAdapterPosition()));
                        }
                        else
                            dbHelper.subjectChapterDAO().updateBookmarkCompleted(subChapters.userid, subChapters.classid, subChapters.section_id, subChapters.lecture_id, subChapters.subjectid,subChapters.is_sync,subChapters.lecture_bookmark,subChapters.lecture_completed,subChapters.is_notes);
                    }
                }
            }else
            {
                if (AppStatus.getInstance(context).isOnline()) {
                    data.get(holder.getAdapterPosition()).lecture_completed = !data.get(holder.getAdapterPosition()).lecture_completed;
                    action(data.get(holder.getAdapterPosition()));
                } else {
                    CommonUtils.showToast(context, context.getString(R.string.no_internet));
                    return;
                }
            }
        }else
        {

            boolean completed = data.get(holder.getAdapterPosition()).lecture_completed;
            data.get(holder.getAdapterPosition()).lecture_completed = !completed;
            dbHelper = MyDatabase.getDatabase(context);
            if (AppStatus.getInstance(context).isOnline()) {
                action(data.get(holder.getAdapterPosition()));
                data.get(holder.getAdapterPosition()).is_sync = true;
                if (!data.get(holder.getAdapterPosition()).lecture_bookmark && !data.get(holder.getAdapterPosition()).lecture_completed&&!data.get(holder.getAdapterPosition()).is_notes) {
                    SubChapters subChapters = data.get(holder.getAdapterPosition());
                    dbHelper.subjectChapterDAO().deleteBookmark(subChapters.userid, subChapters.classid, subChapters.section_id, subChapters.lecture_id, subChapters.subjectid);
                } else {
                    SubChapters subChapters = data.get(holder.getAdapterPosition());
                    SubChapters subChapter = dbHelper.subjectChapterDAO().getBookmarkorData(
                            subChapters.userid, subChapters.classid,
                            subChapters.section_id, subChapters.lecture_id
                            , subChapters.subjectid
                    );
                    if(subChapter==null) {
                        data.get(holder.getAdapterPosition()).lecture_notes="";
                        data.get(holder.getAdapterPosition()).is_sync=true;
                        dbHelper.subjectChapterDAO().addBookMark(data.get(holder.getAdapterPosition()));
                    }
                    else
                        dbHelper.subjectChapterDAO().updateBookmarkCompleted(subChapters.userid, subChapters.classid, subChapters.section_id, subChapters.lecture_id, subChapters.subjectid,subChapters.is_sync,subChapters.lecture_bookmark,subChapters.lecture_completed,subChapters.is_notes);

                }
            } else {
                data.get(holder.getAdapterPosition()).is_sync = false;
                if (!data.get(holder.getAdapterPosition()).lecture_bookmark && !data.get(holder.getAdapterPosition()).lecture_completed) {
                    SubChapters subChapters = data.get(holder.getAdapterPosition());
                    dbHelper.subjectChapterDAO().deleteBookmark(subChapters.userid, subChapters.classid, subChapters.section_id, subChapters.lecture_id, subChapters.subjectid);
                } else {
                    SubChapters subChapters = data.get(holder.getAdapterPosition());
                    SubChapters subChapter = dbHelper.subjectChapterDAO().getBookmarkorData(
                            subChapters.userid, subChapters.classid,
                            subChapters.section_id, subChapters.lecture_id
                            , subChapters.subjectid
                    );
                    if(subChapter==null) {
                        data.get(holder.getAdapterPosition()).is_sync=true;
                        data.get(holder.getAdapterPosition()).lecture_notes="";
                        dbHelper.subjectChapterDAO().addBookMark(data.get(holder.getAdapterPosition()));
                    }
                    else
                        dbHelper.subjectChapterDAO().updateBookmarkCompleted(subChapters.userid, subChapters.classid, subChapters.section_id, subChapters.lecture_id, subChapters.subjectid,subChapters.is_sync,subChapters.lecture_bookmark,subChapters.lecture_completed,subChapters.is_notes);
                }
            }
        }*/



        holder.completed.postDelayed(new Runnable() {
            @Override
            public void run() {
               /* if(isBookmarks)
                {
                    if (!data.get(holder.getAdapterPosition()).lecture_bookmark && !data.get(holder.getAdapterPosition()).lecture_completed) {
                        data.remove(holder.getAdapterPosition());
                    }
                    notifyDataSetChanged();

                }else {*/
               /* holder.completed.setVisibility(View.INVISIBLE);
                holder.favourite.setVisibility(View.INVISIBLE);
                holder.notes.setVisibility(View.INVISIBLE);
                holder.doubts.setVisibility(View.INVISIBLE);*/
                    notifyItemChanged(holder.getAdapterPosition());
                //}
            }
        },0);

        //showStatus(previousHolder,holder.getAdapterPosition(),"Complted Updated");
    }

    private void action(SubChapters data) {
        final JSONObject fjson = new JSONObject();

        try {
            fjson.put(Constants.userId, data.userid);
            fjson.put(Constants.lectureId, data.lecture_id);
            fjson.put(Constants.sectionId, data.section_id);
            fjson.put(Constants.subjectId, data.subjectid);
            fjson.put(Constants.classId, data.classid);
            fjson.put(Constants.lectureName, data.txt);
            fjson.put(Constants.lectureDuration, data.lecture_duration);
            fjson.put(Constants.lectureVideoUrl, data.lecture_video_url);
            if(data.lecture_notes!=null&&data.lecture_notes.length()>0&&!data.lecture_notes.contains("notes.htm"))
                fjson.put(Constants.lectureNotes, data.lecture_notes);
            fjson.put(Constants.lectureVideoThumb, data.lecture_video_thumb);
            fjson.put(Constants.lectureSRTUrl, data.video_srt);
            fjson.put(Constants.completedFlag, (data.lecture_completed) ? "Y" : "N");
            fjson.put(Constants.bookmarkFlag, (data.lecture_bookmark) ? "Y" : "N");

            //fjson.put(Constants.notes_flag, (data.is_notes) ? "Y" : "N");
            fjson.put(Constants.accessToken, access_token);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        String tag_string_req = Constants.reqRegister;
        StringRequest strReq = new StringRequest(Request.Method.POST,
                Constants.LECTURE_ACTIONS,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jObj = new JSONObject(response);
                            //Log.d(Constants.response, response);

                            boolean error = jObj.getBoolean(Constants.errorFlag);
                            if (error) {
                                String errorMsg = jObj.getString(Constants.message);
                                CommonUtils.showToast(context,errorMsg);
                                //Toasty.warning(context, errorMsg, Toast.LENGTH_SHORT, true).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            CommonUtils.showToast(context,context.getString(R.string.json_error)+e.getMessage());
                            //Toasty.warning(context, "Json error: " + e.getMessage(), Toast.LENGTH_SHORT, true).show();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                //Toasty.warning(context, error.getMessage(), Toast.LENGTH_SHORT, true).show();
                CommonUtils.showToast(context, error.getMessage());
            }
        }) {
            final String Key = AppConfig.ENC_KEY;
            final String message = fjson.toString();
            final String encryption = Security.encrypt(message, Key);


            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(AppConfig.JSON_DATA, encryption);
                return params;
            }

        };
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    @Override
    public int getItemViewType(int position) {
        // This is where we'll add footer.
        return position == data.size() ? FOOTER_VIEW : super.getItemViewType(position);
    }

    private void videoChapter(int adapterPosition) {

        if(!data.get(adapterPosition).status.equalsIgnoreCase("P"))
        {
            CommonUtils.showToast(context,"Coming Soon!");
            return;
        }
        if ((isActiveExpired)&&!data.get(adapterPosition).is_demo) {
            Intent i = new Intent(context, SubscribePrePage.class);
            i.putExtra("flag", "M");
            context.startActivity(i);
            return;
        }
       /* if (isActiveExpired ) {
                CommonUtils.showToast(context, "Your Activation for this class has been expired, Please renewal it again");
                return;
            }*/
        SubChapters sub=data.get(adapterPosition);
       /* String lectureVideoUrl = sub.lecture_video_url;
        String lectureId = sub.lecture_id;
        String subjectId = sub.subjectid;
        String classId = sub.classid;
        String userId = sub.userid;
        String sectionId = sub.section_id;
        String lectureName = sub.txt;*/
       // String selectedLectureId = sub.selectedlecture_id;



       /* if (selectedLectureId != null) {
            //((VideoActivityMVP) context).ReloadData(lectureId);
        } else {*/
            Intent i = new Intent(context, VideoActivityMVP.class);

            i.putExtra(Constants.lectureVideoUrl, sub.lecture_video_url);
            i.putExtra(Constants.sectionName, section_name);

            i.putExtra(Constants.lectureId, sub.lecture_id);
            i.putExtra(Constants.subjectId, sub.subjectid);
            i.putExtra(Constants.classId, sub.classid);
            i.putExtra(Constants.userId, sub.userid);
            i.putExtra(Constants.sectionId, sub.section_id);
            i.putExtra(Constants.lectureName, sub.txt);
            if (footerShow ||
                    sub.lecture_bookmark ||
                    sub.lecture_completed)
                i.putExtra("data", data);
            i.setFlags(FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
            context.overridePendingTransition(R.anim.right_in, R.anim.left_out);
        //}
       /* new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                AppController.getInstance().playAudio(R.raw.qz_next);
            }
        },100);*/

    }

    @Override
    public int getItemCount() {
        if (data == null) {
            return 0;
        }
        if (data.size() == 0) {
            //Return 1 here to normalShow nothing
            return 1;
        }

        // Add extra view to normalShow the footer view
        return data.size() + 1;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout bodyLayout;
        TextView chapterName, durationTVID;
        TextView quiz, lectureNotes, track, txt_subscribe;
        ImageView playButton;
        LinearLayout lnr_play;
        ImageView lectureStatus;
        ImageView notes_availe;
        View showLayout;
        ImageView img_lock;
        View lnr_options;
        LinearLayout lnr_bookmark;
        LinearLayout lnr_demo;
        ImageView favourite, completed, notes, doubts;


        private MyViewHolder(View itemView) {
            super(itemView);
            bodyLayout = itemView.findViewById(R.id.bodyLayout);
            chapterName = itemView.findViewById(R.id.chapter_name);

            quiz = itemView.findViewById(R.id.quiz);
            lectureNotes = itemView.findViewById(R.id.lectureNotes);
            track = itemView.findViewById(R.id.Quit);
            txt_subscribe = itemView.findViewById(R.id.txt_subscribe);


            lectureStatus = itemView.findViewById(R.id.lectureStatus);

            showLayout = itemView.findViewById(R.id.showLayout);
            img_lock = itemView.findViewById(R.id.img_lock);
            lnr_options = itemView.findViewById(R.id.lnr_options);
            lnr_bookmark = itemView.findViewById(R.id.lnr_bookmark);
            lnr_demo = itemView.findViewById(R.id.lnr_demo);


            notes_availe = itemView.findViewById(R.id.notes_availe);
            completed = itemView.findViewById(R.id.completed);
            favourite = itemView.findViewById(R.id.favourite);
            notes = itemView.findViewById(R.id.notes);
            doubts = itemView.findViewById(R.id.doubts);

            durationTVID = itemView.findViewById(R.id.durationTVID);

            playButton = itemView.findViewById(R.id.playButton);
            lnr_play = itemView.findViewById(R.id.lnr_play);

            lnr_play.setSoundEffectsEnabled(false);
            playButton.setSoundEffectsEnabled(false);
            notes.setSoundEffectsEnabled(false);
            doubts.setSoundEffectsEnabled(false);
            lnr_demo.setSoundEffectsEnabled(false);
            lectureNotes.setSoundEffectsEnabled(false);
            track.setSoundEffectsEnabled(false);
            quiz.setSoundEffectsEnabled(false);
        }
    }

    class FooterHolder extends RecyclerView.ViewHolder {
        Button fab;
        Button ncert;
        View lnr_mock;
        View lnr_ncert;
        View lnr_footer_coming;
        View card_footer;
TextView txt_coming_footer;
        FooterHolder(View itemView) {
            super(itemView);
            txt_coming_footer = itemView.findViewById(R.id.txt_coming_footer);
            fab = itemView.findViewById(R.id.fab);
            ncert = itemView.findViewById(R.id.ncert);
            lnr_mock = itemView.findViewById(R.id.lnr_mock);
            lnr_ncert = itemView.findViewById(R.id.lnr_ncert);
            lnr_footer_coming = itemView.findViewById(R.id.lnr_footer_coming);
            card_footer = itemView.findViewById(R.id.card_footer);
            fab.setSoundEffectsEnabled(false);
            txt_coming_footer.setTextColor(chapter_text_color);
            lnr_mock.setSoundEffectsEnabled(false);
            if (!footerShow) {
                fab.setVisibility(View.GONE);
                card_footer.setVisibility(View.GONE);
            }
            if(current_class_id<=5)
                lnr_ncert.setVisibility(View.VISIBLE);
            else
                lnr_ncert.setVisibility(View.GONE);
            ncert.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AppController.getInstance().playAudio(R.raw.button_click);
                   /* if (!hasDemo) {
                        Intent i = new Intent(context, SubscribeActivity.class);
                        i.putExtra("flag", "M");
                        context.startActivity(i);
                        return;
                    }*/
                    int position = getAdapterPosition() - 1;
                    Intent i = new Intent(context, NCERTListActivity.class);
                    i.setFlags(FLAG_ACTIVITY_NEW_TASK);
                    i.putExtra(Constants.lectureId, "0");
                    i.putExtra(Constants.subjectId, data.get(position).subjectid);
                    i.putExtra(Constants.classId, data.get(position).classid);
                    i.putExtra(Constants.userId, data.get(position).userid);
                    i.putExtra(Constants.sectionId, data.get(position).section_id);
                    i.putExtra(Constants.sectionName, section_name);
                    i.putExtra(Constants.lectureName, "");
                    i.putExtra(Constants.subjectName, subject_name);

                    //Log.v("On Quize", "onQuize " + data.get(position).section_id);
                    context.startActivity(i);
                }
            });
            lnr_ncert.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AppController.getInstance().playAudio(R.raw.button_click);
                   /* if (!hasDemo) {
                        Intent i = new Intent(context, SubscribeActivity.class);
                        i.putExtra("flag", "M");
                        context.startActivity(i);
                        return;
                    }*/
                    int position = getAdapterPosition() - 1;
                    Intent i = new Intent(context, NCERTListActivity.class);
                    i.setFlags(FLAG_ACTIVITY_NEW_TASK);
                    i.putExtra(Constants.lectureId, "0");
                    i.putExtra(Constants.subjectId, data.get(position).subjectid);
                    i.putExtra(Constants.classId, data.get(position).classid);
                    i.putExtra(Constants.userId, data.get(position).userid);
                    i.putExtra(Constants.sectionId, data.get(position).section_id);
                    i.putExtra(Constants.sectionName, section_name);
                    i.putExtra(Constants.lectureName, "");
                    i.putExtra(Constants.subjectName, subject_name);

                    //Log.v("On Quize", "onQuize " + data.get(position).section_id);
                    context.startActivity(i);
                }
            });
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AppController.getInstance().playAudio(R.raw.button_click);
                   /* if (!hasDemo) {
                        Intent i = new Intent(context, SubscribeActivity.class);
                        i.putExtra("flag", "M");
                        context.startActivity(i);
                        return;
                    }*/
                    int position = getAdapterPosition() - 1;
                    Intent i = new Intent(context, QuizMockExamActivity.class);
                    i.setFlags(FLAG_ACTIVITY_NEW_TASK);
                    i.putExtra(Constants.lectureId, "0");
                    i.putExtra(Constants.subjectId, data.get(position).subjectid);
                    i.putExtra(Constants.classId, data.get(position).classid);
                    i.putExtra(Constants.userId, data.get(position).userid);
                    i.putExtra(Constants.sectionId, data.get(position).section_id);
                    i.putExtra(Constants.sectionName, section_name);
                    i.putExtra(Constants.lectureName, "");
                    //Log.v("On Quize", "onQuize " + data.get(position).section_id);
                    context.startActivity(i);
                }
            });
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AppController.getInstance().playAudio(R.raw.button_click);
                  /*  if (!hasDemo) {
                        Intent i = new Intent(context, SubscribePrePage.class);
                        i.putExtra("flag", "M");
                        context.startActivity(i);
                        return;
                    }*/
                    int position = getAdapterPosition() - 1;
                    Intent i = new Intent(context, QuizMockExamActivity.class);
                    i.setFlags(FLAG_ACTIVITY_NEW_TASK);
                    i.putExtra(Constants.lectureId, "0");
                    i.putExtra(Constants.subjectId, data.get(position).subjectid);
                    i.putExtra(Constants.classId, data.get(position).classid);
                    i.putExtra(Constants.userId, data.get(position).userid);
                    i.putExtra(Constants.sectionId, data.get(position).section_id);
                    i.putExtra(Constants.sectionName, section_name);
                    i.putExtra(Constants.lectureName, "");
                    //Log.v("On Quize", "onQuize " + data.get(position).section_id);
                    context.startActivity(i);
                }
            });
        }
    }

    private String getDateTime() {

        Date date = new Date();
        return CommonUtils.format.format(date);
    }
}
