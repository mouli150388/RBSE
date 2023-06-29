package com.tutorix.tutorialspoint.adapters;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tutorix.tutorialspoint.AppConfig;
import com.tutorix.tutorialspoint.R;
import com.tutorix.tutorialspoint.Security;
import com.tutorix.tutorialspoint.activities.ImagePreviewActivity;
import com.tutorix.tutorialspoint.doubts.DoubtsViewActivity;
import com.tutorix.tutorialspoint.models.AnswerModel;
import com.tutorix.tutorialspoint.utility.CommonUtils;
import com.tutorix.tutorialspoint.utility.Constants;
import com.tutorix.tutorialspoint.views.CustomWebview;

import org.apache.commons.codec.binary.Base64;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class DoubtDetailsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    int VIEW_TYPE_NORMAL = 1;
    int VIEW_TYPE_FOOTER = 2;
    int VIEW_TYPE_HEADER = 0;
    String user_id;
    List<AnswerModel> listData;
    Activity activity;
    boolean footerShow;
    boolean own_doubt;
    String assets= "file:///android_asset";

    public DoubtDetailsAdapter(String user_id, Activity activity, boolean footerShow, boolean own_doubt) {
        listData = new ArrayList<>();
        this.user_id=user_id;
        this.activity=activity;
        this.footerShow=footerShow;
        this.own_doubt=own_doubt;
        if(own_doubt)
            footerShow=false;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_HEADER)
            return new FootViewHeader(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_doubt_header, parent, false));
        else if (viewType == VIEW_TYPE_NORMAL)
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.lauout_doubt_normal, parent, false));
        else if (viewType == VIEW_TYPE_FOOTER)
            return new FootViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_doubt_footer, parent, false));
        else
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.lauout_doubt_normal, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        AnswerModel model=listData.get(position);

        if (getItemViewType(position) == VIEW_TYPE_HEADER) {

            ((FootViewHeader)holder).txtQ.clearCache(true);
            ((FootViewHeader)holder).txtQ.clearHistory();
            ((FootViewHeader)holder).txtQ.clearFormData();
            ((FootViewHeader)holder).txtQ.getSettings().setUseWideViewPort(false);
            ((FootViewHeader)holder).txtQ.getSettings().setSupportZoom(true);
            ((FootViewHeader)holder).txtQ.getSettings().setAllowFileAccess(true);
            ((FootViewHeader)holder).txtQ.getSettings().setBuiltInZoomControls(true);
           // ((FootViewHeader)holder).txtQ.getSettings().setDisplayZoomControls(true);
            ((FootViewHeader)holder).txtQ.getSettings().setJavaScriptEnabled(true);
            ((FootViewHeader)holder).txtQ.loadDataWithBaseURL("",model.question, "text/html", null,"");

            ((FootViewHeader)holder).txtQTime.setText(CommonUtils.getstringDate(model.question_asked_time));
            ((FootViewHeader)holder).txtQUsrName.setText(model.q_user_name);
            if (model.q_user_image != null && !model.q_user_image.isEmpty()&&!model.q_user_image.equals("null")) {
                Glide.with(activity).load(Constants.IMAGE_PATH + model.q_user_id + "/" + model.q_user_image).placeholder(R.drawable.ic_doubt_profile_user).into(((FootViewHeader)holder).img_qtn_img);
            }
            ((FootViewHeader)holder).txtView.setText( model.question_view_count + " Views");

            String sub="";


            if(model.question_board!=null&&(!model.question_board.isEmpty()))
                sub=sub+ model.question_board;

            if(model.question_marks>0)
                sub=sub+", "+model.question_marks+" Mark(s)";
            if(sub.trim().isEmpty())
            {
                ((FootViewHeader)holder).txt_marks.setVisibility(View.GONE);
            }else
            {
                ((FootViewHeader)holder).txt_marks.setVisibility(View.VISIBLE);
                ((FootViewHeader)holder).txt_marks.setText( sub);
            }


            ((FootViewHeader)holder).txtClass.setText(AppConfig.getClassNameDisplayClass( model.q_class_id+"")+"th".replaceAll("-","")+", ");
            ((FootViewHeader)holder).txtSubject.setText(AppConfig.getSubjectNameCapital( model.subject_id+""));
            if(listData.size()>1) {
                ((FootViewHeader) holder).lnr_notanswer.setVisibility(View.GONE);
                ((FootViewHeader) holder).txt_solutions.setVisibility(View.VISIBLE);
            }
            else {
                ((FootViewHeader) holder).txt_solutions.setVisibility(View.GONE);
                ((FootViewHeader) holder).lnr_notanswer.setVisibility(View.VISIBLE);
            }

        }
        else if (getItemViewType(position) == VIEW_TYPE_NORMAL) {
            /*if(model.answer_image!=null&&model.answer_image.length()>0)
            {
                ((ViewHolder)holder).txtQImage.setVisibility(View.VISIBLE);
            }else
            {*/
                ((ViewHolder)holder).txtQImage.setVisibility(View.GONE);
            //}
            ((ViewHolder)holder).txt_web.clearCache(true);
            ((ViewHolder)holder).txt_web.clearHistory();
            ((ViewHolder)holder).txt_web.clearFormData();
            ((ViewHolder)holder).txtQImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), ImagePreviewActivity.class);
                    intent.putExtra("uri", Constants.IMAGE_REQUAET_URL + model.answer_user_id + "/" + model.answer_image);
                    v.getContext().startActivity(intent);
                }
            });
            String s=model.answer_user_name;
            ((ViewHolder)holder).txtQUsrName.setText(s);
            if(((ViewHolder)holder).txt_qtn_img!=null&&s!=null&&s.length()>0)
            ((ViewHolder)holder).txt_qtn_img.setText(s.charAt(0)+"".toUpperCase());
            if(model.user_photo!=null&&!model.user_photo.equalsIgnoreCase("null"))
            {
                Glide.with(activity).load(Constants.IMAGE_PATH + model.answer_user_id + "/" +model.user_photo).placeholder(R.drawable.ic_doubt_profile_user).into(((ViewHolder)holder).img_user);
            }
            ((ViewHolder)holder).txtQ.setVisibility(View.GONE);
            ((ViewHolder)holder).txt_like.setText(""+model.answer_like_count);
            ((ViewHolder)holder).txt_like.setChecked(model.user_like_answer==1);
          if(FavouriteEnabled)
            ((ViewHolder)holder).txt_like.setVisibility(View.VISIBLE);
          else
            ((ViewHolder)holder).txt_like.setVisibility(View.INVISIBLE);

            ((ViewHolder)holder).txt_like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(activity!=null&&activity instanceof  DoubtsViewActivity)
                    {

                        ((DoubtsViewActivity)activity).setAnswerLike(model.answer_id,position);
                    }
                }
            });
            //((ViewHolder)holder).txt_web.clearCache(true);
            ((ViewHolder)holder).txt_web.getSettings().setUseWideViewPort(false);
            ((ViewHolder)holder).txt_web.getSettings().setSupportZoom(true);
            //((ViewHolder)holder).txt_web.getSettings().setAllowFileAccess(true);
            ((ViewHolder)holder).txt_web.getSettings().setBuiltInZoomControls(true);
            //((ViewHolder)holder).txt_web.getSettings().setDisplayZoomControls(true);
            ((ViewHolder)holder).txt_web.getSettings().setJavaScriptEnabled(true);
            ((ViewHolder)holder).txt_web.setWebViewClient(new WebViewClient(){
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {

                    try{
                        String s=url.split("qi=")[1];


                        /* byte[]decode= Base64.decodeBase64((s).getBytes());
                        String text=new String(decode);*/

                      /*  String q_id= Security.getDecryptKey2(text,AppConfig.ENC_KEY);
                        Log.v("q_id","q_id "+q_id);*/
                        ((DoubtsViewActivity)activity).callPublishDoubts(Security.getDecryptKey2(new String(Base64.decodeBase64((s).getBytes())),AppConfig.ENC_KEY));

                    }catch (Exception e)
                    {
                        e.printStackTrace();
                    }



                    return true;
                }

            });
            ((ViewHolder)holder).txt_web.loadDataWithBaseURL("",model.answer,"text/html","utf-8","");
             if(model.answer_given_time!=null)
            ((ViewHolder)holder).txtQTime.setText(CommonUtils.getstringDateAndTime(model.answer_given_time));

            ((ViewHolder)holder).img_more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showOptions(v,position);
                }
            });
        } else if (getItemViewType(position) ==VIEW_TYPE_FOOTER)
        {

            if(own_doubt)
                    ((FootViewHolder)holder).txt_reply.setText("Continue with conversation");
            else
                    ((FootViewHolder)holder).txt_reply.setText("Answer");
            ((FootViewHolder)holder).txt_close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //((FootViewHolder)holder).card_rating.setVisibility(View.VISIBLE);
                    if(activity!=null&&activity instanceof  DoubtsViewActivity)
                    {
                        ((DoubtsViewActivity)activity).closeDoubt();
                    }


                }
            });

            ((FootViewHolder)holder).txt_reply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(activity!=null&&activity instanceof  DoubtsViewActivity)
                    {
                        ((DoubtsViewActivity)activity).replayDoubt();
                    }


                }
            });


        }
    }
    PopupWindow popupWindow;
    private void showOptions(View v,int position)
    {
        if(popupWindow==null) {
            View view = LayoutInflater.from(v.getContext()).inflate(R.layout.popup_menu, null);

            TextView txt_phy = view.findViewById(R.id.txt_phy);
            TextView txt_che = view.findViewById(R.id.txt_che);
            TextView txt_bio = view.findViewById(R.id.txt_bio);
            TextView txt_math = view.findViewById(R.id.txt_math);
            TextView txt_all = view.findViewById(R.id.txt_all);

            TextView txt_o= view.findViewById(R.id.txt_o);
            TextView txt_oo= view.findViewById(R.id.txt_oo);
            TextView txt_ooo= view.findViewById(R.id.txt_ooo);
            TextView txt_oooo= view.findViewById(R.id.txt_oooo);

            txt_o.setVisibility(View.GONE);
            txt_oo.setVisibility(View.GONE);
            txt_ooo.setVisibility(View.GONE);
            txt_oooo.setVisibility(View.GONE);
            txt_che.setVisibility(View.GONE);
            txt_bio.setVisibility(View.GONE);
            txt_math.setVisibility(View.GONE);
            txt_all.setVisibility(View.GONE);
            txt_phy.setText("Report");
            popupWindow = new PopupWindow(
                    view,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);

            popupWindow.setBackgroundDrawable(new BitmapDrawable());
            popupWindow.setOutsideTouchable(true);
            txt_phy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    popupWindow.dismiss();
                    try {
                        if(activity!=null&&activity instanceof  DoubtsViewActivity)
                        {
                            ((DoubtsViewActivity)activity).setReport(listData.get(position).answer_id);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        popupWindow.showAsDropDown(v);
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0)
            return VIEW_TYPE_HEADER;
        else if (footerShow&&position == listData.size() - 1)
            return VIEW_TYPE_FOOTER;
        else return VIEW_TYPE_NORMAL;

    }


    public void addData(List<AnswerModel> listDatas) {
        listData.clear();
        listData.addAll(listDatas);
        notifyDataSetChanged();
    }

    public void setfooterShow(boolean footerShow)
    {
        this.footerShow=footerShow;
        if(own_doubt)
            this.footerShow=false;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public void clear() {
        if(listData!=null)
            listData.clear();
        notifyDataSetChanged();
    }

    public void updateLikeStatus(int position,int value) {
        if(listData.size()>position)
        {
            listData.get(position).answer_like_count=value;
            if(listData.get(position).user_like_answer==0)
            listData.get(position).user_like_answer=1;
            else
            listData.get(position).user_like_answer=0;
            notifyItemChanged(position);
        }
    }
boolean FavouriteEnabled=true;
    public void setEnableFavourite(boolean FavouriteEnabled) {
        this.FavouriteEnabled=FavouriteEnabled;
    }

    static
    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.txt_q_usr_name)
        TextView txtQUsrName;
        @BindView(R.id.txt_q_time)
        TextView txtQTime;
        @BindView(R.id.txt_q)
        TextView txtQ;
        @BindView(R.id.txt_q_image)
        TextView txtQImage;
        @BindView(R.id.txt_qtn_img)
        TextView txt_qtn_img;
        @BindView(R.id.txt_web)
        CustomWebview txt_web;
        @BindView(R.id.img_more)
        LinearLayout img_more;
        @BindView(R.id.txt_like)
        CheckedTextView txt_like;
        @BindView(R.id.img_user)
        ImageView img_user;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class FootViewHolder extends RecyclerView.ViewHolder {

       // SmileRating mSmileRating;
        TextView txt_reply;
        TextView txt_close;
        //CardView card_rating;
        public FootViewHolder(@NonNull View itemView) {
            super(itemView);
           //mSmileRating =  itemView.findViewById(R.id.ratingView);
            txt_reply =  itemView.findViewById(R.id.txt_reply);
            txt_close =  itemView.findViewById(R.id.txt_close);
           // card_rating =  itemView.findViewById(R.id.card_rating);
        }
    }
    class FootViewHeader extends RecyclerView.ViewHolder {

        @BindView(R.id.txt_q_usr_name)
        TextView txtQUsrName;
        @BindView(R.id.txt_q_time)
        TextView txtQTime;
        @BindView(R.id.txt_q)
        CustomWebview txtQ; @BindView(R.id.txt_marks)
        TextView txt_marks;

        @BindView(R.id.img_qtn_img)
        ImageView img_qtn_img;
        @BindView(R.id.txt_class)
        TextView txtClass;
        @BindView(R.id.txt_subject)
        TextView txtSubject;
        @BindView(R.id.txt_view)
        TextView txtView;
        @BindView(R.id.txt_solutions)
        TextView txt_solutions;
        @BindView(R.id.lnr_notanswer)
        LinearLayout lnr_notanswer;
        //CardView card_rating;
        public FootViewHeader(@NonNull View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }


}
