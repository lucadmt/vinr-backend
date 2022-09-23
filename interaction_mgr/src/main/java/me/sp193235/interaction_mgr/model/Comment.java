package me.sp193235.interaction_mgr.model;

import com.google.gson.Gson;
import me.sp193235.interfaces.Vinr;

import javax.persistence.*;

@Entity
public class Comment extends Post {

    @OneToOne
    private Post toPost;

    public Comment() {
        super();
    }

    public Comment(long postId, Vinr fromUser, String markdownContent, Long date, Post toPost) {
        super(postId, fromUser, markdownContent, date);
        this.toPost = toPost;
    }

    public Post getToPost() {
        return toPost;
    }

    public void setToPost(Post toPost) {
        this.toPost = toPost;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
