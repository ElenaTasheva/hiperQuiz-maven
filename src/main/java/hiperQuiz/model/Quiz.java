package hiperQuiz.model;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "quizez")
public class Quiz extends BaseEntity<Long, Quiz> {

    //todo add method to add questions
    private String title; // string 2 to 80 characters long
    // todo check if only Admin can create quiz
    private User author; // the User that created the Quiz;
    private String description; //string 20 - 250 characters long, supporting Markdown syntax;
    private List<Question> questions  = new ArrayList<>(); // list of Question entities (containing the answers with their scores too);
    private int expectedDuration; // integer number in minutes;
    private String URL; //picture (optional) - best representing the Quiz, valid URL to a picture, if missing a placeholder picture should be used;
    private String tags = ""; //string including comma separated tags, allowing to find the Quizes by quick search;


    public Quiz() {

    }

    public Quiz(String title, String description, int expectedDuration) {
        this.title = title;
        this.description = description;
        this.expectedDuration = expectedDuration;
    }

    public Quiz(String title, String description) {
        this.title = title;
        this.description = description;
    }

    @Column(nullable = false)
    @Size(min = 2, max = 80)
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    @Column
    @Size(min = 20, max = 250)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @OneToMany(mappedBy = "quiz")
    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    @Column
    @Size(min = 1)
    public int getExpectedDuration() {
        return expectedDuration;
    }

    public void setExpectedDuration(int expectedDuration) {
        this.expectedDuration = expectedDuration;
    }

    @Column
    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    @Column
    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Quiz{");
        sb.append("created=").append(getCreated());
        sb.append(", id=").append(getId());
        sb.append(", title='").append(title).append('\'');
        sb.append(", author=").append(author);
        sb.append(", description='").append(description).append('\'');
        sb.append(", questions=").append(questions);
        sb.append(", expectedDuration=").append(expectedDuration);
        sb.append(", URL='").append(URL).append('\'');
        sb.append(", tags='").append(tags).append('\'');
        sb.append('}');
        return sb.toString();
    }

    public void addQuestion(Question question){
        questions.add(question);
    }



}
