package com.billing.Invoizo.security.services;


import com.billing.Invoizo.security.execption.TokenRefreshException;
import com.billing.Invoizo.security.models.RefreshToken;
import com.billing.Invoizo.security.repository.RefreshTokenRepository;
import com.billing.Invoizo.security.repository.UserRepository;
import com.billing.Invoizo.user.entity.EmployeeEntity;
import com.billing.Invoizo.user.repository.EmployeeRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {
    @Value("${billing.invoizo.jwtRefreshExpirationMs}")
    private Long refreshTokenDurationMs;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    @Transactional
    public RefreshToken createRefreshToken(String employeeId) {
        RefreshToken refreshToken = new RefreshToken();
        EmployeeEntity employee = employeeRepository.findByEmployeeId(employeeId).get();
        Optional<RefreshToken> result = refreshTokenRepository.findByEmployee(employee);
        if (result.isPresent()) {
            result.get().setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
            refreshTokenRepository.save(result.get());
            refreshToken = result.get();
        } else {

            refreshToken.setEmployee(employee);
            refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
            refreshToken.setToken(UUID.randomUUID().toString());
            refreshToken = refreshTokenRepository.save(refreshToken);
        }
        return refreshToken;
    }

    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token);
            throw new TokenRefreshException(token.getToken(), "Refresh token was expired. Please make a new signin request");
        }
        return token;
    }

    @Transactional
    public void deleteByUserId(String employeeId) {
        refreshTokenRepository.deleteByEmployee(employeeRepository.findByEmployeeId(employeeId).get());
    }
}
