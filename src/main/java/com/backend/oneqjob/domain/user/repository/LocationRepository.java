package com.backend.oneqjob.domain.user.repository;

import com.backend.oneqjob.entity.LocationEntity;
import com.backend.oneqjob.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationRepository extends JpaRepository<LocationEntity, Long> {
}
