package hiperQuiz.model;

import hiperQuiz.model.enums.Gender;
import hiperQuiz.model.enums.Role;
import lombok.NonNull;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "users")
public class User extends BaseEntity<Long, User> {

    private String username;
    private String email;
    private String password;
    private Gender gender;
    private Role role = Role.PLAYER;
    private String picture;
    private String description;
    private String metadata;
    private boolean status;
    private List<Quiz> quizzes = new ArrayList<>();

    public User() {

    }

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public User(String username) {
        this.username = username;
    }

    @Column(name = "username", nullable = false, unique = true)
    @Size(min = 2, max = 15, message = "Username must be between 2 and 15 characters long.")
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Column(name = "email", nullable = false, unique = true)
    @Email
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Column(nullable = false)
    @Size(min = 8, max = 15,  message = "Password must be between 8 and 15 characters long.")
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*[!@#$%^&*()_+,.\\\\\\/;':\"-]).{8,}$")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Enumerated(EnumType.ORDINAL)
    @NonNull
    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    @Enumerated(EnumType.ORDINAL)
    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @Column
    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    @Column()
    @Size(min = 20, max = 250, message = "Description must be between 20 and 250 characters long.")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Column
    @Size(max = 512)
    public String getMetadata() {
        return metadata;
    }

    public void setMetadata(String metadata) {
        this.metadata = metadata;
    }

    @Column
    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    @OneToMany(mappedBy = "author")
    public List<Quiz> getQuizzes() {
        return quizzes;
    }

    public void setQuizzes(List<Quiz> quizzes) {
        this.quizzes = quizzes;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("User{");
        sb.append("created=").append(getCreated());
        sb.append(", id=").append(getId());
        sb.append(", username='").append(username).append('\'');
        sb.append(", email='").append(email).append('\'');
        sb.append(", password='").append(password).append('\'');
        sb.append(", gender=").append(gender);
        sb.append(", role=").append(role);
        sb.append(", picture='").append(picture).append('\'');
        sb.append(", description='").append(description).append('\'');
        sb.append(", metadata='").append(metadata).append('\'');
        sb.append(", status=").append(status);
        sb.append(", quizzes=").append(quizzes);
        sb.append('}');
        return sb.toString();
    }


    // only for presentation purpose (report)
//    @Override
//    public String toString() {
//      return username;
//    }
}
