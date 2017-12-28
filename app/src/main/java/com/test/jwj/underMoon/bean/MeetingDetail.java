package com.test.jwj.underMoon.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/7/20.
 */

public class MeetingDetail implements Serializable{
    public static final long serialVersionUID =1L;
    public int meetingId;
    public int id;
    public String city;
    public String summary;
    public String date;
    public boolean read;
    public boolean approve;

    public MeetingDetail(int meetingId, int id, String city, String summary, String date, boolean read, boolean approve) {
        this.meetingId = meetingId;
        this.id = id;
        this.city = city;
        this.summary = summary;
        this.date = date;
        this.read = read;
        this.approve = approve;
    }
}
