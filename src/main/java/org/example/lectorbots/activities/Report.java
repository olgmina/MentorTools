package org.example.lectorbots.activities;

import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;

public class Report {
    private String telegramName;
    private SimpleLongProperty userID;

    private Answer respose; // другой тип для ответов теста
    private Integer idSlide;
    private SimpleIntegerProperty rating;
    private String question;

    public Report(String telegramName, int idSlide) {
        this.telegramName = telegramName;
        this.idSlide = idSlide;
        respose =new Answer("");
    }

    public Report(String telegramName, long userID, String respose, int idSlide, int rating, String question) {
        this.telegramName = telegramName;
        this.userID = new SimpleLongProperty(userID);
        this.respose =new Answer(respose);
        this.idSlide = idSlide;
        this.rating = new SimpleIntegerProperty(rating);
        this.question = question;
    }

    public String getTelegramName() {
        return telegramName;
    }

    public void setTelegramName(String telegramName) {
        this.telegramName = telegramName;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public SimpleIntegerProperty getratingProperty() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating.set(rating);
    }

    public int getRating() {
        return rating.get();
    }

    public Integer getIdSlide() {
        return idSlide;
    }

    public void setIdSlide(int idSlide) {
        this.idSlide = idSlide;
    }

    public String getRespose() {
        return respose.getAnswer();
    }

    public void setRespose(String respose) {
        this.respose.set(respose);
    }

    public Long getuserID() {
        return userID.get();
    }

    public void setuserID(long userID) {
        this.userID.set(userID);
    }

    public SimpleLongProperty userIDProperty() {
        return userID;
    }

}
