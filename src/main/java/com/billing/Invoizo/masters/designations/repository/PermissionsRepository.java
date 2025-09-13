package com.billing.Invoizo.masters.designations.repository;

import com.billing.Invoizo.masters.designations.entity.PermissionsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface PermissionsRepository extends JpaRepository<PermissionsEntity, String> {
}
