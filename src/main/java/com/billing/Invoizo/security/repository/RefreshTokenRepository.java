package com.billing.Invoizo.security.repository;


import com.billing.Invoizo.security.models.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);

//    @Modifying
//    void deleteByEmployee(EmployeeEntity employee);

//    Optional<RefreshToken> findByEmployee(EmployeeEntity employee);

}
