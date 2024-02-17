package com.backend.oneqjob.companyUser.repository;

import com.backend.oneqjob.entity.CompanyUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyRepository extends JpaRepository<CompanyUser, Long> {
}
