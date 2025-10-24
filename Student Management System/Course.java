package sms;

public class Course {
    private String code;
    private String title;

    public Course(String code, String title) {
        this.code = code.trim();
        this.title = title.trim();
    }

    public String getCode() { return code; }
    public String getTitle() { return title; }

    @Override
    public String toString() {
        return code + " - " + title;
    }
}
