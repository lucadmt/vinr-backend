package me.sp193235.interaction_mgr.controller;

import java.util.*;

import com.google.gson.Gson;

import me.sp193235.interaction_mgr.model.Circle;
import me.sp193235.interaction_mgr.model.Comment;
import me.sp193235.interaction_mgr.model.Post;
import me.sp193235.interaction_mgr.model.Reaction;
import me.sp193235.interaction_mgr.repo.*;
import me.sp193235.interfaces.UserProxy;
import me.sp193235.interfaces.Vinr;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@CrossOrigin("*")
@RestController
@RequestMapping("/api")
public class PostController {

    @Autowired
    PostRepository repository;

    @Autowired
    VinrRepository vinrRepo;

    @Autowired
    CircleRepository circleRepo;

    @Autowired
    ReactionRepository reactionRepo;

    @Autowired
    CommentRepository commentRepo;

    private Set<Post> filterOnlyPosts(Set<Post> input) {
        System.out.println(input);
        final Set<Post> posts = new HashSet<>();
        input.forEach(i -> {
            System.out.println("Filtering:");
            Optional<Comment> maybeComment = commentRepo.findById(i.getPostId());
            if (maybeComment.isEmpty()) {
                posts.add(i);
            }
        });
        return posts;
    }

    @PutMapping("/post")
    public ResponseEntity<String> newPost(@RequestBody Post p) {
        // Posts have an auto-generated Id, proceed to save it.
        if (p.getToRecipients().size() == 0 && p.getToCircles().size() == 0)
            p.addRecipient(new UserProxy("everyone"));
        repository.save(p);
        return new ResponseEntity<>(p.getPostId().toString(), HttpStatus.OK);
    }

    @PatchMapping("/post")
    public ResponseEntity<String> patchPost(@RequestBody Post p) {
        Optional<Post> existing = repository.findById(p.getPostId());
        if (existing.isPresent()) {
            Post newPost = existing.get();
            newPost.setFromUser(p.getFromUser());
            newPost.setMarkdownContent(p.getMarkdownContent());
            System.out.println("Updated Post: " + existing.get());
            repository.save(newPost);
            return new ResponseEntity<>("post-edit-success", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("post-inexistent", HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/post/{postid}/recipients/{recipient}")
    public ResponseEntity<String> delRecipient(@PathVariable Long postid, @RequestBody String recipient) {
        Optional<Post> existing = repository.findById(postid);
        Optional<Vinr> existingRecipient = vinrRepo.findById(recipient);
        if (existing.isPresent()) {
            Post p = existing.get();
            if (existingRecipient.isPresent()) {
                p.removeRecipient(existingRecipient.get().toUserProxy());
                return new ResponseEntity<>("post-edit-success", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("no-such-recipient", HttpStatus.NOT_FOUND);
            }
        } else {
            return new ResponseEntity<>("post-inexistent", HttpStatus.NOT_FOUND);
        }
    }

    // Get ALL posts
    @GetMapping("/post")
    public ResponseEntity<Set<Post>> getPosts() {
        final HashSet<Post> posts = new HashSet<>();
        repository.findAll().forEach(posts::add);
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }

    // Get ALL posts for a specific user
    @GetMapping("/post/for-user/{username}")
    public ResponseEntity<Set<Post>> getPostsForUser(@PathVariable String username) {
        System.out.println("Trying to get posts for user: "+username);
        Optional<Vinr> maybeUser = vinrRepo.findById(username);
        Set<Post> postsForEveryone = new HashSet<>(repository.findDistinctByToRecipientsOrderByDateDesc(new UserProxy("everyone")));
        final HashSet<Post> posts = new HashSet<>(filterOnlyPosts(postsForEveryone));
        System.out.println("posts for everyone: "+postsForEveryone);
        if (maybeUser.isPresent()) {

            // Fetch Posts for all {username} circles
            Set<Post> postsForCircle = new HashSet<>();

            for (Circle c : circleRepo.findByofUser(maybeUser.get().toUserProxy()))
                postsForCircle.addAll(repository.findByToCircles(c));

            posts.addAll(filterOnlyPosts(postsForCircle));

            // Initialize posts with direct post to user
            posts.addAll(filterOnlyPosts(new HashSet<>(repository.findBytoRecipients(maybeUser.get().toUserProxy()))));
        }
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }

    // Get ALL posts for a specific user
    @GetMapping("/post/for-user/{requesting}/of-user/{username}")
    public ResponseEntity<Set<Post>> getPostsByUserForUser(@PathVariable String username, @PathVariable String requesting) {
        System.out.println("Trying to get posts for user: "+username+ "; of user: "+requesting);
        final HashSet<Post> posts;
        Set<Post> postsForRequesting;
        Set<Post> postsForRequestingInCircles = new HashSet<>();

        Optional<Vinr> maybeUser = vinrRepo.findById(username);
        Optional<Vinr> maybeRequestingUser = vinrRepo.findById(requesting);

        if (maybeUser.isPresent() && maybeRequestingUser.isPresent()) {

            // Fetch Posts of {username} for {requesting} such that {requesting} is in the recipients
            postsForRequesting = new HashSet<Post>(repository.findDistinctByFromUserAndToRecipientsOrderByDateDesc(
                    maybeUser.get(), new UserProxy("everyone")
            ));
            postsForRequesting.addAll(repository.findDistinctByFromUserAndToRecipientsOrderByDateDesc(
                    maybeUser.get(), maybeRequestingUser.get().toUserProxy()
            ));

            posts = new HashSet<>(filterOnlyPosts(postsForRequesting));

            // Fetch Posts of {username} for all {username} circles such that {requesting} is in them
            Set<Circle> commonCircles = new HashSet<>(circleRepo.findByOfUserAndUsersContains(maybeUser.get().toUserProxy(), new UserProxy("everyone")));
            commonCircles.addAll(circleRepo.findByOfUserAndUsersContains(maybeUser.get().toUserProxy(), new UserProxy(requesting)));
            for (Circle c : commonCircles) {
                postsForRequestingInCircles.addAll(
                        repository.findDistinctByFromUserAndToCirclesOrderByDateDesc(
                                maybeUser.get(),
                                c
                        )
                );
            }

            posts.addAll(filterOnlyPosts(postsForRequestingInCircles));
        } else {
            posts = new HashSet<>(
                    filterOnlyPosts(new HashSet<>(repository.findDistinctByToRecipientsOrderByDateDesc(
                            new UserProxy("everyone")
                    )))
            );
        }
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }

    // Get ALL posts for a specific user by query
    @GetMapping("/post/for-user/{username}/{query}")
    public ResponseEntity<Set<Post>> getPostsForUser(@PathVariable String username, @PathVariable String query) {
        final HashSet<Post> posts;
        Optional<Vinr> maybeUser = vinrRepo.findById(username);
        Set<Post> postsForEveryone =
                new HashSet<>(
                        repository.findDistinctByToRecipientsAndMarkdownContentContainsIgnoreCaseOrderByDateDesc(
                                new UserProxy("everyone"), query
                        )
                );
        if (maybeUser.isPresent()) {

            // Fetch Posts for all {username} circles
            Set<Post> postsForCircle = new HashSet<>();

            for (Circle c : circleRepo.findByofUser(maybeUser.get().toUserProxy()))
                postsForCircle.addAll(repository.findBytoCirclesAndMarkdownContentContainsIgnoreCase(c, query));

            // Initialize posts with direct post to user
            posts =
                    new HashSet<>(
                            repository.findBytoRecipientsAndMarkdownContentContainsIgnoreCase(maybeUser.get().toUserProxy(), query)
                    );
            posts.addAll(postsForCircle);
            posts.addAll(postsForEveryone);
        } else {
            posts = new HashSet<>(postsForEveryone);
        }
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }

    @GetMapping("/post/{postid}")
    public ResponseEntity<String> getPost(@PathVariable Long postid) {
        return new ResponseEntity<>(new Gson().toJson(repository.findById(postid).orElse(null)), HttpStatus.OK);
    }

    public void recursiveDelete(Post p) {
        if (commentRepo.findDistinctByToPost(p).size() == 0) {
            System.out.println("deleting: "+p);
            reactionRepo.findBypost(p).forEach(r -> reactionRepo.delete(r));
            repository.delete(p);
        } else {
            for (Post p1 : commentRepo.findDistinctByToPost(p)) {
                recursiveDelete(p1);
            }
            repository.delete(p);
        }
    }

    @DeleteMapping("/post/{postid}")
    public ResponseEntity<String> deletePost(@PathVariable Long postid) {
        // Check if post exists
        Optional<Post> p = repository.findById(postid);
        if (p.isPresent()) {
            Post pex = p.get();
            recursiveDelete(pex);
            return new ResponseEntity<String>("post-delete-success", HttpStatus.OK);
        } else {
            return new ResponseEntity<String>("post-delete-failure", HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/post/all")
    public ResponseEntity<String> deleteAll() {
        repository.findAll().forEach((i) -> repository.delete(i));
        return new ResponseEntity<String>("all-posts-delete-success", HttpStatus.OK);
    }
}