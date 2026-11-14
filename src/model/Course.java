
package model;

public class Course implements Eligible {

    private int courseId;
    private String courseName;
    private Double minimumCgpa;
    private int capacity;

    // Constructor
    public Course(int courseId, String courseName,
                  Double minimumCgpa, int capacity) {

        this.courseId = courseId;
        setCourseName(courseName);
        setMinimumCgpa(minimumCgpa);
        setCapacity(capacity);
    }

    // Course name validation
    public void setCourseName(String courseName) {

        if (courseName == null || courseName.trim().isEmpty()) {
            throw new IllegalArgumentException(
                "Course name cannot be empty"
            );
        }

        this.courseName = courseName.trim();
    }

    // CGPA validation
    public void setMinimumCgpa(Double minimumCgpa) {

        if (minimumCgpa == null ||
            minimumCgpa < 0 || minimumCgpa > 10) {

            throw new IllegalArgumentException(
                "Minimum CGPA must be between 0 and 10"
            );
        }

        this.minimumCgpa = minimumCgpa;
    }

    // Capacity validation
    public void setCapacity(int capacity) {

        if (capacity <= 0) {
            throw new IllegalArgumentException(
                "Capacity must be greater than zero"
            );
        }

        this.capacity = capacity;
    }

    // Getters
    public int getCourseId() {
        return courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public Double getMinimumCgpa() {
        return minimumCgpa;
    }

    public int getCapacity() {
        return capacity;
    }

    // Implementing Eligible interface
    @Override
    public boolean isEligible(Student student) {

        return student != null &&
               student.getCgpa() >= minimumCgpa;
    }

    // Display course details
    public String displayCourse() {

        StringBuilder details = new StringBuilder();

        details.append("Course ID: ").append(courseId).append("\n");
        details.append("Course Name: ").append(courseName).append("\n");
        details.append("Minimum CGPA: ").append(minimumCgpa).append("\n");
        details.append("Capacity: ").append(capacity);

        return details.toString();
    }
}
