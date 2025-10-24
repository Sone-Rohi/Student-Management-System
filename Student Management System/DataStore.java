package sms;

import java.util.*;
import java.util.stream.Collectors;

public class DataStore {
    private static final Map<String, Student> students = new LinkedHashMap<>();
    private static final Map<String, Course> courses = new LinkedHashMap<>();
    private static final List<Enrollment> enrollments = new ArrayList<>();

    public static void seed() {
        // Courses
        addCourse(new Course("CS101", "Intro to CS"));
        addCourse(new Course("MATH201", "Discrete Math"));
        addCourse(new Course("ENG110", "Academic Writing"));
        // Students
        addStudent(new Student("S001", "Ada Lovelace", "ada@uni.edu"));
        addStudent(new Student("S002", "Alan Turing", "alan@uni.edu"));
    }

    // Students
    public static synchronized boolean addStudent(Student s) {
        if (students.containsKey(s.getId())) return false;
        students.put(s.getId(), s);
        return true;
    }

    public static synchronized boolean updateStudent(Student s) {
        if (!students.containsKey(s.getId())) return false;
        students.put(s.getId(), s);
        return true;
    }

    public static synchronized List<Student> getStudents() {
        return new ArrayList<>(students.values());
    }

    public static synchronized Optional<Student> findStudent(String id) {
        return Optional.ofNullable(students.get(id));
    }

    // Courses
    public static synchronized boolean addCourse(Course c) {
        if (courses.containsKey(c.getCode())) return false;
        courses.put(c.getCode(), c);
        return true;
    }

    public static synchronized List<Course> getCourses() {
        return new ArrayList<>(courses.values());
    }

    public static synchronized Optional<Course> findCourse(String code) {
        return Optional.ofNullable(courses.get(code));
    }

    // Enrollment
    public static synchronized boolean enroll(String studentId, String courseCode) {
        if (!students.containsKey(studentId) || !courses.containsKey(courseCode)) return false;
        boolean exists = enrollments.stream().anyMatch(e -> e.getStudentId().equals(studentId) && e.getCourseCode().equals(courseCode));
        if (exists) return false;
        enrollments.add(new Enrollment(studentId, courseCode, null));
        return true;
    }

    public static synchronized List<Enrollment> getEnrollments() {
        return new ArrayList<>(enrollments);
    }

    public static synchronized List<Enrollment> getEnrollmentsForStudent(String studentId) {
        return enrollments.stream().filter(e -> e.getStudentId().equals(studentId)).collect(Collectors.toList());
    }

    public static synchronized boolean assignGrade(String studentId, String courseCode, String grade) {
        for (Enrollment e : enrollments) {
            if (e.getStudentId().equals(studentId) && e.getCourseCode().equals(courseCode)) {
                e.setGrade(grade);
                return true;
            }
        }
        return false;
    }
}
