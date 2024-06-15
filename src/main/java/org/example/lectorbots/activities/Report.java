package org.example.lectorbots.activities;

public class Report {
    private String telegramName;
    private long userID;

    private Answer respose; // другой тип для ответов теста
    private int idSlide;
    private int rating;
    private String question;

    public Report(String telegramName, int idSlide) {
        this.telegramName = telegramName;
        this.idSlide = idSlide;
        respose =new Answer("");
    }

    public Report(String telegramName, long userID, String respose, int idSlide, int rating, String question) {
        this.telegramName = telegramName;
        this.userID = userID;
        this.respose =new Answer(respose);
        this.idSlide = idSlide;
        this.rating = rating;
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

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public int getIdSlide() {
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

    public long getUserID() {
        return userID;
    }

    public void setUserID(long userID) {
        this.userID = userID;
    }
}
