package me.sp193235.interaction_mgr.controller;

import com.google.gson.Gson;
import me.sp193235.interaction_mgr.model.Comment;
import me.sp193235.interaction_mgr.model.Post;
import me.sp193235.interaction_mgr.repo.*;
import me.sp193235.interfaces.Vinr;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


@CrossOrigin("*")
@RestController
@RequestMapping("/api")
public class CommentController {

    @Autowired
    CommentRepository repository;

    @Autowired
    PostRepository postRepo;

    @Autowired
    ReactionRepository reactionRepo;

    @Autowired
    VinrRepository vinrRepo;

    private final Gson gson = new Gson();

    @PutMapping("/comment")
    public ResponseEntity<String> newComment(@RequestBody Comment c) {
        // Posts have an auto-generated Id, proceed to save it.
        System.out.println(c);
        Optional<Post> maybePost = postRepo.findById(c.getToPost().getPostId());
        Optional<Vinr> maybeUser = vinrRepo.findById(c.getFromUser().getUsername());
        System.out.println("User is present: " + maybeUser.isPresent() + " / Post is present: " + maybePost.isPresent());
        if (maybeUser.isPresent() && maybePost.isPresent()) {
            repository.save(c);
            return new ResponseEntity<>(gson.toJson(c), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("comment-add-fail-nosuchargs", HttpStatus.NOT_FOUND);
        }
    }

    @PatchMapping("/comment")
    public ResponseEntity<String> patchComment(@RequestBody Comment c) {
        Optional<Comment> existing = repository.findById(c.getPostId());
        if (existing.isPresent()) {
            Comment newPost = existing.get();
            newPost.setMarkdownContent(c.getMarkdownContent());
            System.out.println("Updated Comment: " + existing.get());
            repository.save(newPost);
            return new ResponseEntity<>("comment-edit-success", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("comment-inexistent", HttpStatus.NOT_FOUND);
        }
    }

    // Get ALL comments for a certain post
    @GetMapping("/comment/{postId}")
    public ResponseEntity<String> getPosts(@PathVariable Long postId) {
        Optional<Post> maybePost = postRepo.findById(postId);
        return maybePost.map(
                post -> new ResponseEntity<>(gson.toJson(repository.findDistinctByToPost(post)), HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>("commentsforpost-fail-nosuch-post", HttpStatus.NOT_FOUND));
    }

    // Delete a certain comment
    @DeleteMapping("/comment")
    public ResponseEntity<String> deletePost(@RequestBody Comment c) {
        // Check if comment exists
        Optional<Comment> maybeComment = repository.findById(c.getPostId());
        if (maybeComment.isPresent()) {
            reactionRepo.findBypost(maybeComment.get()).forEach(i -> reactionRepo.delete(i));
            repository.findDistinctByToPost(c).forEach(
                    i -> repository.delete(i)
            );
            repository.delete(maybeComment.get());
            return new ResponseEntity<>("comment-delete-success", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("comment-delete-failure", HttpStatus.NOT_FOUND);
        }
    }
}