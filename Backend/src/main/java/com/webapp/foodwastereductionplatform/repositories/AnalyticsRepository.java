package com.webapp.foodwastereductionplatform.repositories;

import com.webapp.foodwastereductionplatform.model.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.*;

@Repository
public interface AnalyticsRepository extends JpaRepository<Analytics, Integer> {
}

