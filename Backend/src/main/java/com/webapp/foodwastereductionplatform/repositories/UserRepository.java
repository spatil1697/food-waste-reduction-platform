package com.webapp.foodwastereductionplatform.repositories;

import com.webapp.foodwastereductionplatform.model.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.*;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    User findByEmail(String email);

    User findAllById(Integer userId);
}
