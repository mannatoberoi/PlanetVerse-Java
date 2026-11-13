
package model;

public class Student extends User {

    private String department;
    private Double cgpa;

    // Constructor
    public Student(int userId, String name, String email,
                   String department, Double cgpa) {

        super(userId, name, email);

        setDepartment(department);
        setCgpa(cgpa);
    }

    // Department setter
    public void setDepartment(String department) {
        if (department == null || department.trim().isEmpty()) {
            throw new IllegalArgumentException("Department cannot be empty");
        }

        this.department = department.trim().toUpperCase();
    }

    // CGPA setter with validation
    public void setCgpa(Double cgpa) {
        if (cgpa == null || cgpa < 0 || cgpa > 10) {
            throw new IllegalArgumentException("CGPA must be between 0 and 10");
        }

        this.cgpa = cgpa;
    }

    // Getters
    public String getDepartment() {
        return department;
    }

    public Double getCgpa() {
        return cgpa;
    }

    // Display student profile
    @Override
    public String displayProfile() {
        StringBuilder profile = new StringBuilder();

        profile.append(super.displayProfile()).append("\n");
        profile.append("Department: ").append(department).append("\n");
        profile.append("CGPA: ").append(cgpa);

        return profile.toString();
    }
}
