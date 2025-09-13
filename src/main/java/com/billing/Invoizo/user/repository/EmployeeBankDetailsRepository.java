package com.billing.Invoizo.user.repository;

import com.billing.Invoizo.user.entity.EmployeeBankDetailsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeBankDetailsRepository extends JpaRepository<EmployeeBankDetailsEntity, String> {
}
