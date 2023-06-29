package com.tutorix.tutorialspoint.login;

public interface LoginView {
    public void showLoading();
    public void showMessage(String msg,int code);
    public void navigateScreen(int screen);

}
