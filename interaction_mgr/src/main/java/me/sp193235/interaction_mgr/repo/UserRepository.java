package me.sp193235.interaction_mgr.repo;

import me.sp193235.interfaces.UserProxy;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<UserProxy, String> {
}
