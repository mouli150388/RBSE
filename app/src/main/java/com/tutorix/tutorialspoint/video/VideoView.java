package com.tutorix.tutorialspoint.video;

import org.json.JSONObject;

public interface VideoView {
    public void showLoading();
    public void hideLoading();
    public void showMessage(String msh,boolean hasToFinish);
    public void parseResultData(JSONObject obj);
    public void play();
}
