package me.sp193235.notification_mgr.model;

import com.google.gson.Gson;
import me.sp193235.interfaces.UserProxy;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "notifications")
public class Notification {

    @Id
    @GeneratedValue
    private long notificationId;

    @OneToOne
    private UserProxy ofUser;

    @Column
    private String title;

    @Column
    private String status;

    @Column String content;

    public Notification() {

    }

    public Notification(long notificationId, UserProxy ofUser, String title, String content, String status) {
        this.title = title;
        this.content = content;
        this.notificationId = notificationId;
        this.ofUser = ofUser;
        this.status = status;
    }

    public long getNotificationId() {
        return notificationId;
    }

    public UserProxy getOfUser() {
        return ofUser;
    }

    public void setOfUser(UserProxy ofUser) {
        this.ofUser = ofUser;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Notification)) return false;
        Notification that = (Notification) o;
        return getNotificationId() == that.getNotificationId() && getOfUser().equals(that.getOfUser()) && Objects.equals(getTitle(), that.getTitle()) && Objects.equals(getContent(), that.getContent());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getNotificationId(), getOfUser(), getTitle(), getContent());
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
