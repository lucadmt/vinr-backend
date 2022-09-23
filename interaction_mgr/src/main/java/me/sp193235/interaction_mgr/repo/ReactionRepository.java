package me.sp193235.interaction_mgr.repo;

import me.sp193235.interaction_mgr.model.Post;
import me.sp193235.interaction_mgr.model.Reaction;
import me.sp193235.interaction_mgr.model.ReactionId;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ReactionRepository extends CrudRepository<Reaction, ReactionId> {

    Integer countByPost(Post p);

    List<Reaction> findBypost(Post p);
}
