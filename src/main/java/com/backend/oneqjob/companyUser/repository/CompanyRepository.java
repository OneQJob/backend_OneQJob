package com.backend.oneqjob.companyUser.repository;

import com.backend.oneqjob.entity.CompanyUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository extends JpaRepository<CompanyUser, Long> {
}
