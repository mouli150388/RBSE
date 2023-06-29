package com.tutorix.tutorialspoint.subjects;

import com.tutorix.tutorialspoint.models.Chapters;

import java.util.List;

public interface SubjectView {
    public void loadData(List<Chapters> listData,String BaseURL);
    public void showMessage(String msg);
    public void showNoSdcard();
    public void requestPermissionForStorage();
    public void shoaLoding();
    public void cloaseLoding();
}
