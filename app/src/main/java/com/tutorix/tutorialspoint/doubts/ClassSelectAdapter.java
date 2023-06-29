package com.tutorix.tutorialspoint.doubts;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;

import com.tutorix.tutorialspoint.R;
import com.tutorix.tutorialspoint.models.SelectClassModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ClassSelectAdapter extends RecyclerView.Adapter<ClassSelectAdapter.ViewHOlder> {

    List<SelectClassModel> listClass;
    String selectedClass="";
    LatestDoubtsActivity activity;
    public ClassSelectAdapter(LatestDoubtsActivity activity) {
        listClass = new ArrayList<>();
        this.activity=activity;
        addClasses();
    }

    @NonNull
    @Override
    public ViewHOlder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHOlder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_classs_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHOlder holder, int position) {
        SelectClassModel classModel=listClass.get(position);

            holder.radioClass.setText(classModel.name);
            //holder.txt_img.setText(classModel.img);
        holder.radioClass.setChecked(classModel.isSelect);
        holder.radioClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedClass=listClass.get(position).id;
                activity.selectedClass(selectedClass);
                //setSelected(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listClass.size();
    }

    class ViewHOlder extends RecyclerView.ViewHolder {
        @BindView(R.id.radio_class)
        CheckedTextView radioClass;
        //@BindView(R.id.txt_img)
       // TextView txt_img;
        public ViewHOlder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

public void setSelected(int pos)
{
    for(int k=0;k<listClass.size();k++)
        listClass.get(k).isSelect=false;
    listClass.get(pos).isSelect=true;
    notifyDataSetChanged();
}

    public void addClasses()
    {
        try {
            InputStream is=activity.getAssets().open("class.json");
            StringBuffer s=new StringBuffer();
            byte[]b=new byte[is.available()];
            is.read(b);
            JSONArray array=new JSONArray(new String(b));

            JSONObject obj;
            SelectClassModel model;
            for(int k=0;k<array.length();k++)
            {

                obj=array.getJSONObject(k);
                model=new SelectClassModel();
                model.isSelect=false;

                model.name=obj.getString("name");
                model.id=obj.getString("id");
                model.img=obj.getString("img");
                listClass.add(model);
            }


        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

}
