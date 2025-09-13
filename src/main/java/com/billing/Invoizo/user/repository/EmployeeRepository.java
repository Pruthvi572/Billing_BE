package com.billing.Invoizo.user.repository;

import com.billing.Invoizo.user.entity.EmployeeEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.Optional;

public interface EmployeeRepository  extends JpaRepository<EmployeeEntity, String> {

    @Query("SELECT COUNT(*) FROM EmployeeEntity WHERE employeeId=:employeeId")
    Long isEmployeeExist(String employeeId);

    @Query("SELECT Count(*) FROM EmployeeEntity WHERE designationId=:designationId")
    Long isDesignationExist(String designationId);


    Optional<EmployeeEntity> findByEmployeeId(String employeeId);

//    @Query(value = "SELECT EUD.employeeid AS employeeId, DES.name AS designationName, " +
//            "EUD.designationId AS designationId, EUD.employeeDesignationId AS employeeDesignationId " +
//            "FROM lms.user_employee_ulb_designation EUD " +
//            "LEFT JOIN lms.master_designations DES ON EUD.designationid = DES.id " +
//            "WHERE EUD.employeeid = :employeeId", nativeQuery = true)
//    List<EmployeeDesignationProjections> getEmployeeDesignationDetails(String employeeId);

    @Transactional
    @Modifying
    @Query("UPDATE EmployeeEntity SET mobileLoginStatus =:mobileLoginStatus, mobileLastLog =:mobileLastLog WHERE employeeId =:employeeId")
    void updateEmployeeMobileStatus(@Param("employeeId") String employeeId,
                                    @Param("mobileLoginStatus") byte mobileLoginStatus,
                                    @Param("mobileLastLog") Date mobileLastLog);

    @Transactional
    @Modifying
    @Query("UPDATE EmployeeEntity set mobileLoginStatus=:mobileLoginStatus WHERE employeeId =:employeeId")
    void logout(@Param("employeeId") String employeeId, @Param("mobileLoginStatus") byte mobileLoginStatus);

}
