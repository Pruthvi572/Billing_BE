package com.billing.Invoizo.masters.designation_permission.repository;

import com.billing.Invoizo.masters.designation_permission.entity.DesignationPermissionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;

public interface DesignationPermissionRepository extends JpaRepository<DesignationPermissionEntity, String> {

    @Query("SELECT new map(" +
            "dp.designationId AS designationId, " +
            "dp.type AS type, " +
            "dp.permissions AS permissions, " +
            "dp.title AS title, " +
            "dp.module AS module, " +
            "dp.moduleName AS moduleName, " +
            "dp.isAssigned AS isAssigned, " +
            "dp.description AS description, " +
            "dp.isEnabled AS isEnabled, " +
            "dp.permissionOrder AS permissionOrder) " +
            "FROM DesignationPermissionEntity dp " +
            "WHERE dp.designationId = :designationId " +
            "AND dp.isEnabled = true " +
            "ORDER BY dp.permissionOrder ASC")
    List<Map<String, Object>> getDesignationPermissions(@Param("designationId") int designationId);

    @Query("SELECT DISTINCT(permissions) FROM DesignationPermissionEntity WHERE designationId=:designationId AND isAssigned=:isAssigned " +
            "AND type=:type")
    List<String> getAssignedPermissions(int designationId, boolean isAssigned, int type);

}
