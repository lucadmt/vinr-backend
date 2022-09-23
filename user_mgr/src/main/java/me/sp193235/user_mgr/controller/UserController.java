package me.sp193235.user_mgr.controller;

import java.util.*;

import com.google.gson.Gson;

import me.sp193235.interfaces.RabbitMesg;
import me.sp193235.interfaces.Vinr;
import me.sp193235.user_mgr.UserMgrApplication;
import me.sp193235.user_mgr.model.User;
import me.sp193235.user_mgr.repo.UserRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;


@CrossOrigin("*")
@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private RabbitTemplate template;

    @Autowired
    UserRepository repository;

    @PutMapping("/user")
    public ResponseEntity<String> newUser(@RequestBody User u) {
        Optional<User> existing = repository.findById(u.getUsername());
        if (existing.isPresent()) {
            return new ResponseEntity<>("signup-error-user-exists", HttpStatus.CONFLICT);
        } else {
            System.out.println(u);
            u.setPassword(u.hashPw());
            repository.save(u);
            RabbitMesg<Vinr> mesg = new RabbitMesg<>(RabbitMesg.TYPE_CREATE, new Vinr(u.getUsername()));
            template.convertAndSend(UserMgrApplication.exchangeName,"", mesg);
            System.out.println("Message sent: " + mesg.getObj());
            return new ResponseEntity<>("signup-success", HttpStatus.OK);
        }
    }

    @PostMapping("/user")
    public ResponseEntity<String> loginUser(@RequestBody User u) {
        Optional<User> existing = repository.findById(u.getUsername());
        if (existing.isEmpty()) {
            return new ResponseEntity<>("user-not-registered", HttpStatus.NOT_FOUND);
        } else {
            PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
            if (passwordEncoder.matches(u.getPassword(), existing.get().getPassword())) {
                return new ResponseEntity<>("signin-success", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("signin-error", HttpStatus.UNAUTHORIZED);
            }
        }
    }

    @PatchMapping("/user")
    public ResponseEntity<String> update(@RequestBody User u) {
        Optional<User> existing = repository.findById(u.getUsername());
        if (existing.isEmpty()) {
            return new ResponseEntity<>("user-not-registered", HttpStatus.NOT_FOUND);
        } else {
            User patching = existing.get();
            if (u.getPassword() != null) {
                patching.setPassword(u.hashPw());
            }
            patching.setBio(u.getBio());
            patching.setProfilePic(u.getProfilePic());
            repository.save(patching);
            return new ResponseEntity<>("update-user-success", HttpStatus.OK);
        }
    }


    // Get ALL users
    @GetMapping("/user")
    public ResponseEntity<Set<User>> getUsers() {
        Set<User> users = new HashSet<>();
        repository.findAll().forEach(users::add);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/user/{username}")
    public ResponseEntity<String> getUser(@PathVariable String username) {
        return new ResponseEntity<>(new Gson().toJson(repository.findById(username).orElse(null)), HttpStatus.OK);
    }

    @GetMapping("/user/query/{query}")
    public ResponseEntity<String> queryUser(@PathVariable String query) {
        return new ResponseEntity<>(new Gson().toJson(repository.findByUsernameContains(query)), HttpStatus.OK);
    }

    @RequestMapping(value = "/user/{username}", method = RequestMethod.HEAD)
    public ResponseEntity<String> exists(@PathVariable String username) {
        if (repository.findById(username).isPresent()) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/user/{username}")
    public ResponseEntity<String> deleteUser(@PathVariable String username) {
        repository.delete(Objects.requireNonNull(repository.findById(username).orElse(null)));
        return new ResponseEntity<String>("user-delete-success", HttpStatus.OK);
    }

    @DeleteMapping("/user/all")
    public ResponseEntity<String> deleteAll() {
        repository.findAll().forEach((i) -> repository.delete(i));
        return new ResponseEntity<String>("Users Deleted!", HttpStatus.OK);
    }
}