package com.webapp.foodwastereductionplatform.repositories;

import com.webapp.foodwastereductionplatform.model.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.*;

import java.util.*;

@Repository
public interface FoodRepository extends JpaRepository<Food, Integer> {
    List<Food> findByUserId(Integer userId);

    @Query("SELECT f FROM Food f WHERE f.expiryDate <= CURRENT_DATE AND f.status <> 'expired'")
    List<Food> findExpiredListings();
}