package me.sp193235.notification_mgr.repo;

import me.sp193235.notification_mgr.controller.NotificationController;
import me.sp193235.notification_mgr.model.Notification;
import me.sp193235.interfaces.UserProxy;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface NotificationRepository extends CrudRepository<Notification, Long> {
    List<Notification> findDistinctByofUser(UserProxy user);
    List<Notification> findDistinctByofUserAndStatus(UserProxy user, String status);
}