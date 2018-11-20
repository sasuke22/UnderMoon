package com.test.jwj.underMoon.bean;

import java.io.Serializable;
import java.sql.Date;

/**
 * Created by Administrator on 2017/7/20.
 */

public class MeetingDetail implements Serializable{
    public static final long serialVersionUID =1L;
    public int               meetingId;
    public int               id;//meeting的人的ID？
    public String            city;
    public String            summary;
    public Date              date;
    public boolean           read;
    public boolean           approve;
    public String            type;
    public String            loveType;
    public int               age;
    public int               marry;
    public int               height;
    public String            job;
    public String            figure;
    public String            xingzuo;
    public String            content;
    public String            registId;//报名人的ID
    public String            enlistersName;
    public int               score;

    public MeetingDetail(){}

    public MeetingDetail(int meetingId, int id, String city, String summary, Date date, boolean read, boolean approve, String type,
                         String loveType, int age, int marry, int height, String job, String figure, String xingzuo, String content) {
        this.meetingId = meetingId;
        this.id = id;
        this.city = city;
        this.summary = summary;
        this.date = date;
        this.read = read;
        this.approve = approve;
        this.type = type;
        this.loveType = loveType;
        this.age = age;
        this.marry = marry;
        this.height = height;
        this.job = job;
        this.figure = figure;
        this.xingzuo = xingzuo;
        this.content = content;
    }

}
