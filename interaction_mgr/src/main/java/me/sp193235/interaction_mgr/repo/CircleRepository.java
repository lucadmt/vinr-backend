package me.sp193235.interaction_mgr.repo;

import me.sp193235.interaction_mgr.model.Circle;
import me.sp193235.interfaces.UserProxy;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CircleRepository extends CrudRepository<Circle, Long> {

    List<Circle> findByofUser(UserProxy u);

    List<Circle> findByOfUserAndUsersContains(UserProxy ofUser, UserProxy users);
}