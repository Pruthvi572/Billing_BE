package com.billing.Invoizo.security.services;


import com.billing.Invoizo.InvoizoProperties;
import com.billing.Invoizo.user.entity.EmployeeEntity;
import com.billing.Invoizo.user.repository.EmployeeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Slf4j
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private InvoizoProperties properties;

    @Autowired
    private RestTemplateBuilder restTemplateBuilder;


    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<EmployeeEntity> result = employeeRepository.findByEmployeeId(username);
        EmployeeEntity employeeEntity;
        if (result.isPresent()) {
            employeeEntity = result.get();
            if (!isAccountNonLocked(employeeEntity)) {
                throw new LockedException("Account locked. Please try again after : " +
                        (((employeeEntity.getLockedTime().getTime() + properties.getAccountLockTime())
                                - System.currentTimeMillis()) / 1000) + " Seconds");
            }
//            if (!checkBeforeLogin()) {
//                throw new AuthenticationCredentialsNotFoundException("Access Denied, Contact Support");
//            }
            if (!employeeEntity.isEnabled()) {
                throw new AuthenticationCredentialsNotFoundException("Employee Disabled");
            }
        } else {
            throw new UsernameNotFoundException("Invalid Username");
        }
        return employeeEntity;
    }

//    private boolean checkBeforeLogin() {
//        try {
//            HttpHeaders headers = new HttpHeaders();
//            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//            HttpEntity<Object> entity = new HttpEntity<>(headers);
//            DistrictProjections districtProjections = districtRepository.getLicenseDetails();
//            ResponseEntity<JSONObject> response = restTemplateBuilder
//                    .build().exchange(
//                            properties.getSecurityCheckURL() + "/web/security/check/?licenseId=" + districtProjections.getLicenseId()
//                                    + "&subClientId=" + districtProjections.getSubClientId(),
//                            HttpMethod.POST, entity, JSONObject.class);
//
//            return (boolean) Objects.requireNonNull(response.getBody()).get("flag");
//        } catch (Exception e) {
//            log.error("Error in Checking with License Module :", e);
//        }
//        return false;
//    }

    private boolean isAccountNonLocked(EmployeeEntity employeeEntity) {
        if (!employeeEntity.isAccountNonLocked() && employeeEntity.getFailedAttempts() == 3) {
            long lockedTime = employeeEntity.getLockedTime().getTime();
            long currentTime = System.currentTimeMillis();
            if (lockedTime + properties.getAccountLockTime() < currentTime) {
                employeeEntity.setFailedAttempts(0);
                employeeEntity.setLockedTime(null);
                employeeEntity.setAccountNonLocked(true);
                employeeRepository.save(employeeEntity);
                return true;
            }
        }
        return employeeEntity.isAccountNonLocked();
    }

}
