package me.sp193235.interaction_mgr.model;

import com.google.gson.Gson;
import me.sp193235.interfaces.UserProxy;
import me.sp193235.interfaces.Vinr;

import javax.persistence.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "posts")
public class Post {

    @Id
    @GeneratedValue //(strategy = GenerationType.IDENTITY)
    private Long postId;

    @OneToOne
    private Vinr fromUser;

    @Column(length=5000000)
    private String markdownContent;

    @Column
    private Long date;

    @ManyToMany
    private final Set<Circle> toCircles;

    @ManyToMany
    private final Set<UserProxy> toRecipients;

    public Post() {
        this.toCircles = new HashSet<>();
        this.toRecipients = new HashSet<>();
    }

    public Post(Long postId, Vinr fromUser, String markdownContent, Long date) {
        this.postId = postId;
        this.fromUser = fromUser;
        this.markdownContent = markdownContent;
        this.date = date;
        this.toCircles = new HashSet<>();
        this.toRecipients = new HashSet<>();
    }

    public Post(Long postId, Vinr fromUser, String markdownContent, Long date, Collection<Circle> toCircles, Collection<UserProxy> toRecipients) {
        this.postId = postId;
        this.fromUser = fromUser;
        this.markdownContent = markdownContent;
        this.date = date;
        this.toCircles = new HashSet<>();
        this.toCircles.addAll(toCircles);
        this.toRecipients = new HashSet<>();
        this.toRecipients.addAll(toRecipients);
    }

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public Vinr getFromUser() {
        return fromUser;
    }

    public void setFromUser(Vinr fromUser) {
        this.fromUser = fromUser;
    }

    public String getMarkdownContent() {
        return markdownContent;
    }

    public void setMarkdownContent(String markdownContent) {
        this.markdownContent = markdownContent;
    }

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    public Set<Circle> getToCircles() {
        return toCircles;
    }

    public void addCircle(Circle c) {
        toCircles.add(c);
    }

    public void removeCircle(Circle c) {
        toCircles.remove(c);
    }

    public Set<UserProxy> getToRecipients() {
        return toRecipients;
    }

    public void addRecipient(UserProxy c) {
        toRecipients.add(c);
    }

    public void removeRecipient(UserProxy c) {
        toRecipients.remove(c);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Post)) return false;
        Post post = (Post) o;
        return getPostId().equals(post.getPostId()) && getFromUser().equals(post.getFromUser()) && Objects.equals(getMarkdownContent(), post.getMarkdownContent());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPostId(), getFromUser(), getMarkdownContent());
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
