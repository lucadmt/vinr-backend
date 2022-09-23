package me.sp193235.interaction_mgr.model;

import com.google.gson.Gson;
import me.sp193235.interfaces.Vinr;

import javax.persistence.*;

@Entity
@IdClass(ReactionId.class)
@Table(name = "reactions")
public class Reaction {

    @Id
    @ManyToOne(optional = false)
    @JoinColumn(name = "from_user_username", nullable = false)
    private Vinr fromUser;

    @Id
    @ManyToOne(optional = false)
    @JoinColumn(name = "post_post_id", nullable = false)
    private Post post;

    public Vinr getFromUser() {
        return fromUser;
    }

    public void setFromUser(Vinr fromUser) {
        this.fromUser = fromUser;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
