package com.billing.Invoizo.user.repository;

import com.billing.Invoizo.user.entity.UserLoginHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface UserLoginHistoryRepository extends JpaRepository<UserLoginHistory, Date> {

    @Query("SELECT MAX(loginTime) FROM UserLoginHistory WHERE employeeId=:username")
    Date getLastLoginTime(String username);
}
