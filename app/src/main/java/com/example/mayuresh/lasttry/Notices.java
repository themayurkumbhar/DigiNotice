package com.example.mayuresh.lasttry;

import java.sql.Time;

/**
 * Created by Mayuresh on 19-Nov-15.
 */
public class Notices {
    public Notices() {
    }

    int id;
    String title;
    String msg;
    String time;
    public Notices(int id, String title, String msg) {

        this.id = id;
        this.title = title;
        this.msg = msg;
    }
    public Notices(String title, String msg) {
        this.title = title;
        this.msg = msg;
    }



    public int getId() {

        return id;
    }

    public  String getTime()
    {
        return time;
    }
    public void setTime(String time)
    {
        this.time=time;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
