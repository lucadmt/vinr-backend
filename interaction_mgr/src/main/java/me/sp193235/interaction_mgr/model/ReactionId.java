package me.sp193235.interaction_mgr.model;

import com.google.gson.Gson;

import java.io.Serializable;
import java.util.Objects;

public class ReactionId implements Serializable {

    private String fromUser;
    private Long post;

    public ReactionId() {
    }

    public ReactionId(String fromUser, Long post) {
        this.fromUser = fromUser;
        this.post = post;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ReactionId)) return false;
        ReactionId that = (ReactionId) o;
        return fromUser.equals(that.fromUser) && post.equals(that.post);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fromUser, post);
    }

    public String getFromUser() {
        return fromUser;
    }

    public void setFromUser(String fromUser) {
        this.fromUser = fromUser;
    }

    public Long getPost() {
        return post;
    }

    public void setPost(Long post) {
        this.post = post;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
