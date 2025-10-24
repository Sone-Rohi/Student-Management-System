package sms;

public class Student {
    private String id;
    private String name;
    private String email;

    public Student(String id, String name, String email) {
        this.id = id.trim();
        this.name = name.trim();
        this.email = email.trim();
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }

    public void setName(String name) { this.name = name.trim(); }
    public void setEmail(String email) { this.email = email.trim(); }
}
