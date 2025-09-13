package com.billing.Invoizo.user.repository;

import com.billing.Invoizo.user.entity.EmployeeULBDesignationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeULBDesignationRepository extends JpaRepository<EmployeeULBDesignationEntity, String> {
}
