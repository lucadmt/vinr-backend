package me.sp193235.notification_mgr.controller;

import com.google.gson.Gson;
import me.sp193235.interfaces.Vinr;
import me.sp193235.notification_mgr.model.Notification;
import me.sp193235.notification_mgr.model.NotificationStatus;
import me.sp193235.notification_mgr.repo.NotificationRepository;
import me.sp193235.notification_mgr.repo.VinrRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@CrossOrigin("*")
@RestController
@RequestMapping("/api")
public class NotificationController {

    private final Gson gson = new Gson();

    @Autowired
    NotificationRepository notificationRepo;

    @Autowired
    VinrRepository vinrRepo;

    // Create new Notification
    @PutMapping("/notification")
    public ResponseEntity<String> putNotification(@RequestBody Notification notification) {
        Optional<Vinr> ofUser = vinrRepo.findById(notification.getOfUser().getUsername());
        if (ofUser.isPresent()) {
            notificationRepo.save(notification);
            return new ResponseEntity<>(gson.toJson(notification), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("notification-add-failed-nosuch-user", HttpStatus.NOT_FOUND);
        }
    }

    // Create new Notification
    @PatchMapping("/notification")
    public ResponseEntity<String> patchNotification(@RequestBody Notification notification) {
        Optional<Vinr> ofUser = vinrRepo.findById(notification.getOfUser().getUsername());
        Optional<Notification> notification_ex = notificationRepo.findById(notification.getNotificationId());
        if (ofUser.isPresent() && notification_ex.isPresent()) {
            Notification actual = notification_ex.get();
            actual.setTitle(notification.getTitle());
            actual.setContent(notification.getContent());
            actual.setStatus(notification.getStatus());
            notificationRepo.save(actual);
            return new ResponseEntity<>(gson.toJson(actual), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("notification-patch-failed-nosuch-args", HttpStatus.NOT_FOUND);
        }
    }

    // Delete a Notification
    @DeleteMapping("/notification/{id}")
    public ResponseEntity<String> delNotification(@PathVariable long id) {
        Optional<Notification> n = notificationRepo.findById(id);
        if (n.isPresent()) {
            notificationRepo.delete(n.get());
            return new ResponseEntity<>(gson.toJson(n.get()), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("notification-delete-failed-nosuch-notification", HttpStatus.NOT_FOUND);
        }
    }

    // Get ALL notifications for user
    @GetMapping("/notification/{username}")
    public ResponseEntity<String> getNotifications(@PathVariable String username) {
        Optional<Vinr> existingUser = vinrRepo.findById(username);
        return existingUser.map(
                        vinr ->
                                new ResponseEntity<>(gson.toJson(notificationRepo.findDistinctByofUser(vinr.toUserProxy())), HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>("notifications-get-nosuch-user", HttpStatus.NOT_FOUND));

    }

    // Get ALL notifications for user
    @GetMapping("/notification/{username}/check")
    public ResponseEntity<String> checkNotifications(@PathVariable String username) {
        Optional<Vinr> existingUser = vinrRepo.findById(username);
        return existingUser.map(
                        vinr -> {
                            List<Notification> notifications = notificationRepo.findDistinctByofUserAndStatus(vinr.toUserProxy(), NotificationStatus.STATUS_UNREAD);
                            return new ResponseEntity<>(gson.toJson(notifications.size() > 0), HttpStatus.OK);
                        })
                .orElseGet(() -> new ResponseEntity<>("notifications-check-nosuch-user", HttpStatus.NOT_FOUND));

    }
}