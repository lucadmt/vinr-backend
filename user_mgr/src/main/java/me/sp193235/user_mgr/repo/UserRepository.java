package me.sp193235.user_mgr.repo;

import org.springframework.data.repository.CrudRepository;

import me.sp193235.user_mgr.model.User;

import java.util.List;

public interface UserRepository extends CrudRepository<User, String> {
    List<User> findByUsernameContains(String query);
}
