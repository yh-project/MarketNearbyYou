package com.example.mny.Controller;

public interface Control {

    void changePage(String pageName);
    void startToast(String msg);
    void onBackPressed();
    void makeNotice(String type, String msg);

}
