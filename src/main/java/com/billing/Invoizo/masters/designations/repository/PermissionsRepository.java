package com.billing.Invoizo.masters.designations.repository;

import com.billing.Invoizo.masters.designations.entity.PermissionsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface PermissionsRepository extends JpaRepository<PermissionsEntity, String> {
    @Query("SELECT DISTINCT p FROM PermissionsEntity p")
    List<PermissionsEntity> getAllPermissions();

    @Query("SELECT p FROM PermissionsEntity p WHERE p.isEnabled = true ORDER BY p.permissionOrder ASC")
    List<PermissionsEntity> getAllPermissionsForSave();

    @Query(value = "SELECT EXISTS (SELECT 1 FROM INFORMATION_SCHEMA.TABLES WHERE " +
            "TABLE_SCHEMA = 'lms' AND TABLE_NAME = 'setup_permissions')", nativeQuery = true)
    List<Boolean> checkTableExists();

    @Query("SELECT name FROM PermissionsEntity")
    List<String> getPermissionNames();
}
