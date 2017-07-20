package com.test.jwj.underMoon;

/**
 * Created by Administrator on 2017/7/20.
 */

public class MeetingDetail {
    public String city;
    public String summary;
    public String date;
    public boolean read;
    public boolean approve;

    public MeetingDetail(String city, String summary, String date, boolean read, boolean approve) {
        this.city = city;
        this.summary = summary;
        this.date = date;
        this.read = read;
        this.approve = approve;
    }
}
