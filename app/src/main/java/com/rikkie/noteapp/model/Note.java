package com.rikkie.noteapp.model;

import java.io.Serializable;

/**
 * Created by tuyenpx on 26/04/2016.
 */
public class Note implements Serializable {


    private int idNote;
    private String titleNote;
    private String contentNote;
    private int colorNote;
    private String lastTimeModifyNote;
    private long alarmNote;
    private boolean isSetAlarm;
    private String pathImage;
    private String alarmTimeString;


    public Note(int idNote, String titleNote, String contentNote, int colorNote, String lastTimeModifyNote, long alarmNote, boolean isSetAlarm, String pathImage, String alarmTimeString) {
        this.idNote = idNote;
        this.titleNote = titleNote;
        this.contentNote = contentNote;
        this.colorNote = colorNote;
        this.lastTimeModifyNote = lastTimeModifyNote;
        this.alarmNote = alarmNote;
        this.isSetAlarm = isSetAlarm;
        this.pathImage = pathImage;
        this.alarmTimeString = alarmTimeString;
    }

    public Note() {

    }

    public Note(int idNote, long alarmNote) {
        this.idNote = idNote;
        this.alarmNote = alarmNote;
    }

    public int getIdNote() {
        return idNote;
    }

    public void setIdNote(int idNote) {
        this.idNote = idNote;
    }

    public String getTitleNote() {
        return titleNote;
    }

    public void setTitleNote(String titleNote) {
        this.titleNote = titleNote;
    }

    public String getContentNote() {
        return contentNote;
    }

    public void setContentNote(String contentNote) {
        this.contentNote = contentNote;
    }

    public int getColorNote() {
        return colorNote;
    }

    public void setColorNote(int colorNote) {
        this.colorNote = colorNote;
    }

    public String getLastTimeModifyNote() {
        return lastTimeModifyNote;
    }

    public void setLastTimeModifyNote(String lastTimeModifyNote) {
        this.lastTimeModifyNote = lastTimeModifyNote;
    }

    public long getAlarmNote() {
        return alarmNote;
    }

    public void setAlarmNote(long alarmNote) {
        this.alarmNote = alarmNote;
    }

    public boolean isSetAlarm() {
        return isSetAlarm;
    }

    public void setSetAlarm(boolean setAlarm) {
        isSetAlarm = setAlarm;
    }

    public String getPathImage() {
        return pathImage;
    }

    public void setPathImage(String pathImage) {
        this.pathImage = pathImage;
    }

    public String getAlarmTimeString() {
        return alarmTimeString;
    }

    public void setAlarmTimeString(String alarmTimeString) {
        this.alarmTimeString = alarmTimeString;
    }

    @Override
    public String toString() {
        return "[ idNote= " + idNote + " titleNote = " + titleNote + " contentNote = " + contentNote + " colorNote =" + colorNote + " lastTimeModifyNote = " + lastTimeModifyNote + " alarmNote = " + alarmNote + " isSetAlarm = " + isSetAlarm + " pathImage = " + pathImage + " alarmTimeString = " + alarmTimeString;
    }
}
