
package model;

public class User {

    // Private data members
    private int userId;
    private String name;
    private String email;

    // Constructor
    public User(int userId, String name, String email) {
        this.userId = userId;
        setName(name);
        setEmail(email);
    }

    // Setters
    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }
        this.name = name.trim();
    }

    public void setEmail(String email) {
        if (email == null || !email.contains("@") || !email.contains(".")) {
            throw new IllegalArgumentException("Invalid email address");
        }
        this.email = email.trim().toLowerCase();
    }

    // Getters
    public int getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    // Display user profile using StringBuilder
    public String displayProfile() {
        StringBuilder profile = new StringBuilder();

        profile.append("User ID: ").append(userId).append("\n");
        profile.append("Name: ").append(name).append("\n");
        profile.append("Email: ").append(email);

        return profile.toString();
    }
}
