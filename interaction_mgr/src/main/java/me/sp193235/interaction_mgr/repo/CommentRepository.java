package me.sp193235.interaction_mgr.repo;

import me.sp193235.interaction_mgr.model.Circle;
import me.sp193235.interaction_mgr.model.Post;
import me.sp193235.interfaces.UserProxy;
import me.sp193235.interfaces.Vinr;
import org.springframework.data.repository.CrudRepository;

import me.sp193235.interaction_mgr.model.Comment;

import java.util.List;

public interface CommentRepository extends CrudRepository<Comment, Long> {
    List<Comment> findDistinctByToPost(Post post);

    List<Comment> findByToPostAndToRecipients(Post post, UserProxy u);

    List<Comment> findByToPostAndToRecipientsAndMarkdownContentContainsIgnoreCase(Post p, UserProxy u, String query);

    List<Comment> findDistinctByToPostAndToRecipientsOrderByDateDesc(Post p, UserProxy user);

    List<Comment> findDistinctByToPostAndToRecipientsAndMarkdownContentContainsIgnoreCaseOrderByDateDesc(Post p, UserProxy user, String query);

    List<Comment> findDistinctByToPostAndFromUserAndToRecipientsOrderByDateDesc(Post p, Vinr user, UserProxy toRecipient);

    List<Comment> findDistinctByToPostAndFromUserAndToCirclesOrderByDateDesc(Post p, Vinr user, Circle toCircle);

    List<Comment> findByToPostAndToCircles(Post p, Circle c);

    List<Comment> findByToPostAndToCirclesAndMarkdownContentContainsIgnoreCase(Post p, Circle c, String query);
}
