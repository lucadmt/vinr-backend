package me.sp193235.interaction_mgr.controller;

import com.google.gson.Gson;
import me.sp193235.interaction_mgr.model.Circle;
import me.sp193235.interaction_mgr.repo.CircleRepository;
import me.sp193235.interaction_mgr.repo.VinrRepository;
import me.sp193235.interfaces.Vinr;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Optional;


@CrossOrigin("*")
@RestController
@RequestMapping("/api")
public class CircleController {

    private final Gson gson = new Gson();

    @Autowired
    CircleRepository circleRepo;

    @Autowired
    VinrRepository vinrRepo;

    // Create new Circle
    @PostMapping("/circle")
    public ResponseEntity<String> newCircle(@RequestBody Circle circle) {
        Optional<Vinr> requestingUser = vinrRepo.findById(circle.getOfUser().getUsername());
        if (requestingUser.isPresent()) {
            circleRepo.save(circle);
            return new ResponseEntity<>(gson.toJson(circle), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("circle-add-failed-nosuch-user", HttpStatus.NOT_FOUND);
        }
    }

    // Update circle
    @PatchMapping("/circle/{circleId}")
    public ResponseEntity<String> putUserInCircle(@RequestBody Circle c) {
        Optional<Vinr> existingUser = vinrRepo.findById(c.getOfUser().getUsername());
        Optional<Circle> existingCircle = circleRepo.findById(c.getCircleId());
        if (existingUser.isPresent() && existingCircle.isPresent()) {
            Circle circle = existingCircle.get();
            circle.setName(c.getName());
            circle.setUsers(c.getUsers());
            circleRepo.save(circle);
            return new ResponseEntity<>("circle-update-success", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("circle-update-nosuchargs", HttpStatus.NOT_FOUND);
        }
    }

    // Delete Circle
    @DeleteMapping("/circle")
    public ResponseEntity<String> delCircle(@RequestBody Circle circle) {
        Optional<Vinr> requestingUser = vinrRepo.findById(circle.getOfUser().getUsername());
        if (requestingUser.isPresent()) {
            circleRepo.delete(circle);
            return new ResponseEntity<>("circle-del-success", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("circle-del-failed-nosuch-user", HttpStatus.NOT_FOUND);
        }
    }

    // Get ALL circles for user
    @GetMapping("/circle/{username}")
    public ResponseEntity<String> getVinrs(@PathVariable String username) {
        Optional<Vinr> existingUser = vinrRepo.findById(username);
        return existingUser.map(
                vinr ->
                        new ResponseEntity<>(gson.toJson(circleRepo.findByofUser(vinr.toUserProxy())), HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>("circle-get-nosuch-user", HttpStatus.NOT_FOUND));

    }
}