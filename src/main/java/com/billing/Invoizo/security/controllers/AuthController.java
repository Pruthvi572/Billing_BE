package com.billing.Invoizo.security.controllers;


import com.billing.Invoizo.masters.designation_permission.repository.DesignationPermissionRepository;
import com.billing.Invoizo.masters.modules.repository.ModulesRepository;
import com.billing.Invoizo.mobile.dto.Encryption;
import com.billing.Invoizo.security.execption.TokenRefreshException;
import com.billing.Invoizo.security.jwt.ActiveTokenManager;
import com.billing.Invoizo.security.jwt.JwtUtils;
import com.billing.Invoizo.security.models.RefreshToken;
import com.billing.Invoizo.security.models.User;
import com.billing.Invoizo.security.payload.request.LoginRequest;
import com.billing.Invoizo.security.payload.request.SignupRequest;
import com.billing.Invoizo.security.payload.request.TokenRefreshRequest;
import com.billing.Invoizo.security.payload.response.LoginResponse;
import com.billing.Invoizo.security.payload.response.MessageResponse;
import com.billing.Invoizo.security.repository.UserRepository;
import com.billing.Invoizo.security.services.RefreshTokenService;
import com.billing.Invoizo.user.dto.EmployeeDesignationProjections;
import com.billing.Invoizo.user.entity.EmployeeEntity;
import com.billing.Invoizo.user.repository.EmployeeRepository;
import com.billing.Invoizo.user.repository.UserLoginHistoryRepository;
import com.billing.Invoizo.user.service.EmployeeService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

import static com.billing.Invoizo.constants.EnumConstants.PERMISSION_TYPE.WEB;


@Slf4j
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;


    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    RefreshTokenService refreshTokenService;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private ModulesRepository modulesRepository;

    @Autowired
    private DesignationPermissionRepository permissionsRepository;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private UserLoginHistoryRepository loginHistoryRepository;

    @Autowired
    private ActiveTokenManager activeTokenManager;


    @PostMapping("/signin")
    public ResponseEntity<LoginResponse> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        LoginResponse loginResponse = new LoginResponse();
        try {
            String loginUserName = Encryption.decrypt(loginRequest.getUsername());
            String loginPassword = Encryption.decrypt(loginRequest.getPassword());
            loginRequest.setUsername(loginUserName);
            loginRequest.setPassword(loginPassword);
            loginResponse.setEmployeeId(loginUserName);
            Authentication authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            EmployeeEntity employeeEntity = (EmployeeEntity) authentication.getPrincipal();
            loginResponse.setAccessToken(jwtUtils.generateJwtToken(employeeEntity));
            RefreshToken refreshToken = refreshTokenService.createRefreshToken(employeeEntity.getUsername());
            loginResponse.setRefreshToken(refreshToken.getToken());
            loginResponse.setModules(modulesRepository.getEnabledModules(false));
            setEmployeeDetails(loginRequest.getUsername(), loginResponse);
            loginResponse.setPermissions(getPermissions(loginResponse.getDesignationId(), WEB.getType()));
            employeeEntity.setFailedAttempts(0);
            activeTokenManager.generateAndManageTokenForUser(employeeEntity.getUsername(), loginResponse.getAccessToken());
            employeeRepository.save(employeeEntity);
            loginResponse.setLastLoggedInAt(loginHistoryRepository.getLastLoginTime(loginRequest.getUsername()));
            employeeService.saveLoginHistory(loginRequest.getUsername());
            return ResponseEntity.ok(loginResponse);
        } catch (BadCredentialsException exception) {
            log.error("Invalid Credentials {}", exception.getMessage());
            loginResponse.setMessage(exception.getMessage());
            employeeService.handleWrongPasswordAttempt(loginRequest.getUsername());
            return ResponseEntity.ok(loginResponse);
        } catch (Exception exception) {
            log.error("Error While Logging in {}", exception.getMessage());
            loginResponse.setMessage(exception.getMessage());
            return ResponseEntity.ok(loginResponse);
        }
    }

    public List<String> getPermissions(int designationId, int type) {
        return permissionsRepository.getAssignedPermissions(designationId, true, type);
    }

    private LoginResponse setEmployeeDetails(String employeeId, LoginResponse loginResponse) {
        List<EmployeeDesignationProjections> list = employeeRepository.getEmployeeDesignationDetails(employeeId);
        Optional<EmployeeEntity> employeeEntity = employeeRepository.findByEmployeeId(employeeId);
        if (employeeEntity.isPresent()) {
            loginResponse.setEmployeeId(employeeEntity.get().getEmployeeId());
            loginResponse.setName(employeeEntity.get().getName());
            loginResponse.setDesignationId(employeeEntity.get().getDesignationId());
            loginResponse.setAddress(employeeEntity.get().getAddress());
        }
        if (list.size() > 0) {
            EmployeeDesignationProjections designationEntity = list.get(0);
            loginResponse.setDesignationId(designationEntity.getDesignationId());
            loginResponse.setDesignationName(designationEntity.getDesignationName());
            loginResponse.setEmployeeDesignationId(designationEntity.getEmployeeDesignationId());
        }
        return loginResponse;
    }


    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
        }

        User user = new User(signUpRequest.getUsername(), signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()));

        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }


    @PostMapping("/refreshtoken")
    public ResponseEntity<?> refreshtoken(@Valid @RequestBody TokenRefreshRequest request) {
        String requestRefreshToken = request.getRefreshToken();

        return refreshTokenService.findByToken(requestRefreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getEmployee)
                .map(employee -> {
                    String token = jwtUtils.generateTokenFromUsername(employee.getUsername());
                    return ResponseEntity.ok(new TokenRefreshException(token, requestRefreshToken));
                })
                .orElseThrow(() -> new TokenRefreshException(requestRefreshToken,
                        "Refresh token is not in database!"));
    }

    @PostMapping("/signout")
    public ResponseEntity<?> logoutUser() {
        Object principle = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!principle.toString().equals("anonymousUser")) {
            String employeeId = ((UserDetails) principle).getUsername();
            refreshTokenService.deleteByUserId(employeeId);
            activeTokenManager.removeTokensForUser(employeeId);
        }
        return ResponseEntity.ok(new MessageResponse("Log out successful!"));
    }
}
