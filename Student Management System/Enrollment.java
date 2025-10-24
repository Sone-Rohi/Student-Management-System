package sms;

public class Enrollment {
    private String studentId;
    private String courseCode;
    private String grade; // e.g., A, B+, etc.

    public Enrollment(String studentId, String courseCode, String grade) {
        this.studentId = studentId;
        this.courseCode = courseCode;
        this.grade = grade;
    }

    public String getStudentId() { return studentId; }
    public String getCourseCode() { return courseCode; }
    public String getGrade() { return grade; }
    public void setGrade(String grade) { this.grade = grade; }
}
