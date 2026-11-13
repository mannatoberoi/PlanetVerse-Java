
package model;

public class Instructor extends User {

    private String specialization;

    // Constructor
    public Instructor(int userId, String name, String email,
                      String specialization) {

        super(userId, name, email);
        setSpecialization(specialization);
    }

    // Setter
    public void setSpecialization(String specialization) {

        if (specialization == null || specialization.trim().isEmpty()) {
            throw new IllegalArgumentException(
                "Specialization cannot be empty"
            );
        }

        this.specialization = specialization.trim();
    }

    // Getter
    public String getSpecialization() {
        return specialization;
    }

    // Display instructor profile
    @Override
    public String displayProfile() {

        StringBuilder profile = new StringBuilder();

        profile.append(super.displayProfile()).append("\n");
        profile.append("Specialization: ").append(specialization);

        return profile.toString();
    }
}
