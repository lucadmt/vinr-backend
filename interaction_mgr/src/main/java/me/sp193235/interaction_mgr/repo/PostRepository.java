package me.sp193235.interaction_mgr.repo;

import me.sp193235.interaction_mgr.model.Circle;
import me.sp193235.interfaces.UserProxy;
import me.sp193235.interfaces.Vinr;
import org.springframework.data.repository.CrudRepository;

import me.sp193235.interaction_mgr.model.Post;

import java.util.List;

public interface PostRepository extends CrudRepository<Post, Long> {

    List<Post> findBytoRecipients(UserProxy u);

    List<Post> findBytoRecipientsAndMarkdownContentContainsIgnoreCase(UserProxy u, String query);

    List<Post> findDistinctByToRecipientsOrderByDateDesc(UserProxy user);

    List<Post> findDistinctByToRecipientsAndMarkdownContentContainsIgnoreCaseOrderByDateDesc(UserProxy user, String query);

    List<Post> findDistinctByFromUserAndToRecipientsOrderByDateDesc(Vinr user, UserProxy toRecipient);

    List<Post> findDistinctByFromUserAndToCirclesOrderByDateDesc(Vinr user, Circle toCircle);

    List<Post> findByToCircles(Circle c);

    List<Post> findBytoCirclesAndMarkdownContentContainsIgnoreCase(Circle c, String query);
}
