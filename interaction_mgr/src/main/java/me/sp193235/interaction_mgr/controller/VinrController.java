package me.sp193235.interaction_mgr.controller;

import com.google.gson.Gson;
import me.sp193235.interfaces.UserProxy;
import me.sp193235.interfaces.Vinr;
import me.sp193235.interaction_mgr.repo.VinrRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


@CrossOrigin("*")
@RestController
@RequestMapping("/api")
public class VinrController {

    private final Gson gson = new Gson();

    @Autowired
    VinrRepository vinrRepo;

    @PutMapping("/vinr/{username}")
    public ResponseEntity<String> addVinr(@PathVariable String username, @RequestBody UserProxy user) {
        Optional<Vinr> maybeVinr = vinrRepo.findById(username);
        if (maybeVinr.isPresent()) {
            Vinr newVinr = maybeVinr.get();
            newVinr.addVinr(user);
            vinrRepo.save(newVinr);
            return new ResponseEntity<>("vinr-request-success", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("vinr-request-error-nosuch-user", HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/vinr/{username}")
    public ResponseEntity<String> delVinr(@PathVariable String username, @RequestBody UserProxy user) {
        Optional<Vinr> maybeVinr = vinrRepo.findById(username);
        if (maybeVinr.isPresent()) {
            Vinr receiver = maybeVinr.get();
            receiver.removeVinr(user);
            vinrRepo.save(receiver);
            return new ResponseEntity<>("vinr-deletion-success", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("vinr-request-error-nosuch-user", HttpStatus.NOT_FOUND);
        }
    }

    // Get ALL vinrs for user
    @GetMapping("/vinr/{username}")
    public ResponseEntity<String> getVinrs(@PathVariable String username) {
        Optional<Vinr> maybeVinr = vinrRepo.findById(username);
        if (maybeVinr.isPresent()) {
            Vinr receiver = maybeVinr.get();
            return new ResponseEntity<>(gson.toJson(receiver.getVinrs()), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("vinr-request-error-nosuch-user", HttpStatus.NOT_FOUND);
        }

    }
}