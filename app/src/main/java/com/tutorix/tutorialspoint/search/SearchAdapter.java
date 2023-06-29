package com.tutorix.tutorialspoint.search;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tutorix.tutorialspoint.AppConfig;
import com.tutorix.tutorialspoint.R;
import com.tutorix.tutorialspoint.SharedPref;
import com.tutorix.tutorialspoint.models.SubChapters;
import com.tutorix.tutorialspoint.utility.Constants;
import com.tutorix.tutorialspoint.video.VideoPlayerActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<SubChapters> listSubChapters;
    private static final int ITEM = 0;
    private static final int LOADING = 1;


    private boolean isLoadingAdded = false;
    Activity activity;
    SearchFragment searchFragment;


    public SearchAdapter(Activity activity, SearchFragment searchFragment) {
        listSubChapters = new ArrayList<>();
        this.activity = activity;
        this.searchFragment = searchFragment;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case ITEM:
                viewHolder = new SearchViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_layout, parent, false));
                break;
            case LOADING:
                View v2 = inflater.inflate(R.layout.item_progress, parent, false);
                viewHolder = new LoadingVH(v2);
                break;
        }
        return viewHolder;
    }

    SubChapters subChapters;

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        switch (getItemViewType(position)) {
            case ITEM:
                subChapters = listSubChapters.get(position);
                holder.itemView.setTag(position);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            InputMethodManager inputMethodManager =
                                    (InputMethodManager) activity.getSystemService(
                                            Activity.INPUT_METHOD_SERVICE);
                            if (inputMethodManager != null && activity.getCurrentFocus() != null && activity.getCurrentFocus().getWindowToken() != null) {
                                inputMethodManager.hideSoftInputFromWindow(Objects.requireNonNull(activity.getCurrentFocus()).getWindowToken(), 0);
                            }
                            v.clearFocus();
                        } catch (Exception e) {

                        }
                        if (new SharedPref().isExpired(activity)) {

                            if (searchFragment != null)
                                searchFragment.showExpieryAlert();
                            return;
                        }
                        SubChapters subChapters = listSubChapters.get((Integer) v.getTag());
                        Intent i = new Intent(v.getContext(), VideoPlayerActivity.class);
                        i.putExtra("hindiUrl", false);
                        i.setFlags(FLAG_ACTIVITY_NEW_TASK);
                        i.putExtra(Constants.lectureVideoUrl, "");
                        i.putExtra(Constants.sectionName, subChapters.txt);

                        i.putExtra(Constants.lectureId, subChapters.lecture_id);
                        i.putExtra(Constants.subjectId, subChapters.subjectid);
                        i.putExtra(Constants.classId, subChapters.classid);
                        i.putExtra(Constants.userId, subChapters.userid);
                        i.putExtra(Constants.sectionId, subChapters.section_id);
                        i.putExtra(Constants.lectureName, subChapters.txt);
                        v.getContext().startActivity(i);
                    }
                });

                ((SearchViewHolder) holder).chapterName.setText(subChapters.txt);
                ((SearchViewHolder) holder).chapterDesc.setText((AppConfig.getClassNameDisplayClass(subChapters.classid)).replaceAll("-", "") + "th" + ", " + AppConfig.getSubjectNameCapital(subChapters.subjectid));
                break;

            case LOADING:
//                Do nothing
                break;
        }

    }

    @Override
    public int getItemCount() {
        return listSubChapters.size();
    }

    public void add(List<SubChapters> listSubChapters) {
        listSubChapters.clear();
        listSubChapters.addAll(listSubChapters);
        notifyDataSetChanged();
    }


    @Override
    public int getItemViewType(int position) {
        return (position == listSubChapters.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
    }

    /*
   Helpers
   _________________________________________________________________________________________________
    */

    public void add(SubChapters mc) {
        try {


            listSubChapters.add(mc);
            notifyItemInserted(listSubChapters.size() - 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addAll(List<SubChapters> mcList) {
        listSubChapters.clear();
        for (SubChapters mc : mcList) {
            add(mc);
        }

    }

    public void remove(SubChapters city) {
        try {
            int position = listSubChapters.indexOf(city);
            if (position > -1 && listSubChapters.size() > position) {
                listSubChapters.remove(position);
                notifyItemRemoved(position);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void clear() {
        isLoadingAdded = false;
        while (getItemCount() > 0) {
            remove(getItem(0));
        }
    }

    public boolean isEmpty() {
        return getItemCount() == 0;
    }


    public void addLoadingFooter() {
        isLoadingAdded = true;
        add(new SubChapters());
    }

    public SubChapters getItem(int position) {
        return listSubChapters.get(position);
    }

    public void removeLoadingFooter() {
        try {
            if (!isLoadingAdded) {
                return;
            }
            isLoadingAdded = false;

            int position = listSubChapters.size() - 1;
            SubChapters item = getItem(position);

            if (item != null) {
                listSubChapters.remove(position);
                notifyItemRemoved(position);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class SearchViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.logo)
        ImageView logo;
        @BindView(R.id.chapter_name)
        TextView chapterName;
        @BindView(R.id.chapter_desc)
        TextView chapterDesc;

        public SearchViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    protected class LoadingVH extends RecyclerView.ViewHolder {

        public LoadingVH(View itemView) {
            super(itemView);
        }
    }

}
