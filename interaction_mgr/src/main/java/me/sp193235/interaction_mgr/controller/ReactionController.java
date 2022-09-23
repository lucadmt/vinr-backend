package me.sp193235.interaction_mgr.controller;

import me.sp193235.interaction_mgr.model.Post;
import me.sp193235.interaction_mgr.model.Reaction;
import me.sp193235.interaction_mgr.model.ReactionId;
import me.sp193235.interfaces.Vinr;
import me.sp193235.interaction_mgr.repo.PostRepository;
import me.sp193235.interaction_mgr.repo.ReactionRepository;
import me.sp193235.interaction_mgr.repo.VinrRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


@CrossOrigin("*")
@RestController
@RequestMapping("/api")
public class ReactionController {

    @Autowired
    PostRepository repository;

    @Autowired
    ReactionRepository reactionRepository;

    @Autowired
    VinrRepository vinrRepo;

    @GetMapping("/reaction/{postId}")
    public ResponseEntity<String> getReactions(@PathVariable Long postId) {
        Optional<Post> maybePost = repository.findById(postId);
        if (maybePost.isPresent()) {
            Integer maybeCount = reactionRepository.countByPost(maybePost.get());
            int count = maybeCount == null ? 0 : maybeCount;

            return new ResponseEntity<>(Integer.toString(count), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("reactions-get-error-nosuch-post", HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/reaction")
    public ResponseEntity<String> putReaction(@RequestBody Reaction r) {
        Optional<Post> existing = repository.findById(r.getPost().getPostId());
        Optional<Vinr> existingUser = vinrRepo.findById(r.getFromUser().getUsername());
        System.out.println("Trying to add reaction: " + r);
        System.out.println("Existing post is present: " + existing.isPresent() + "; User is present: " + existingUser.isPresent());
        if (existing.isPresent() && existingUser.isPresent()) {
            Post p = existing.get();
            System.out.println("p equals reaction.post: " + p.equals(r.getPost()) + "; fromUser exists: " + r.getFromUser().equals(existingUser.get()));
            if (reactionRepository.findById(new ReactionId(r.getFromUser().getUsername(), r.getPost().getPostId())).isPresent()) {
                System.out.println("Reaction Already Present: Deleting it");
                reactionRepository.delete(r);
            } else {
                System.out.println("New Reaction! Saving it");
                reactionRepository.save(r);
            }
            String newCount = reactionRepository.countByPost(r.getPost()).toString();
            return new ResponseEntity<>(newCount, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("reaction-add-fail-post-user-inexistent", HttpStatus.NOT_FOUND);
        }
    }
}