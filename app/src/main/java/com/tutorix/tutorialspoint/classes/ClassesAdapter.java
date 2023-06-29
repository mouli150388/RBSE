package com.tutorix.tutorialspoint.classes;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tutorix.tutorialspoint.R;
import com.tutorix.tutorialspoint.classes.model.StudentBatch;
import com.tutorix.tutorialspoint.classes.model.StudentBranch;
import com.tutorix.tutorialspoint.classes.model.StudentCity;
import com.tutorix.tutorialspoint.classes.model.StudentClass;

import java.util.ArrayList;

public class ClassesAdapter extends BaseAdapter {
    ArrayList<Object>list;
  public  ClassesAdapter(){
      list=new ArrayList<>();
    }

    public void setList(ArrayList<Object>list)
    {
        this.list.clear();
        this.list.addAll(list);
      //  this.list=list;
        notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
      View v= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.student_btach_list_item,null);
      TextView textview= v.findViewById(R.id.textview);
      Object obj=list.get(i);
      if(obj instanceof StudentClass)
        textview.setText(((StudentClass)list.get(i)).title);
      else if(obj instanceof StudentCity)
          textview.setText(((StudentCity)list.get(i)).title);
      else if(obj instanceof StudentBranch)
          textview.setText(((StudentBranch)list.get(i)).title);
      else if(obj instanceof StudentBatch)
          textview.setText(((StudentBatch)list.get(i)).title);
      return v;
    }


}
