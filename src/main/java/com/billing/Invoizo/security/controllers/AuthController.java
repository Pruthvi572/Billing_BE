package com.billing.Invoizo.security.controllers;


import com.billing.Invoizo.security.jwt.ActiveTokenManager;
import com.billing.Invoizo.security.jwt.JwtUtils;
import com.billing.Invoizo.security.repository.UserRepository;
import com.billing.Invoizo.security.services.RefreshTokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


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

//    @Autowired
//    private EmployeeRepository employeeRepository;

//    @Autowired
//    private ModulesRepository modulesRepository;
//
//    @Autowired
//    private DesignationPermissionRepository permissionsRepository;
//
//    @Autowired
//    private MasterLabelsRepository labelsRepository;
//
//    @Autowired
//    private EmployeeService employeeService;
//
//    @Autowired
//    private UserLoginHistoryRepository loginHistoryRepository;

    @Autowired
    private ActiveTokenManager activeTokenManager;

//    @PostMapping("/signin")
//    public ResponseEntity<LoginResponse> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
//        LoginResponse loginResponse = new LoginResponse();
//        try {
//            String loginUserName = Encryption.decrypt(loginRequest.getUsername());
//            String loginPassword = Encryption.decrypt(loginRequest.getPassword());
//            loginRequest.setUsername(loginUserName);
//            loginRequest.setPassword(loginPassword);
//            loginResponse.setEmployeeId(loginUserName);
//            Authentication authentication = authenticationManager
//                    .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
//            SecurityContextHolder.getContext().setAuthentication(authentication);
//            EmployeeEntity employeeEntity = (EmployeeEntity) authentication.getPrincipal();
//            loginResponse.setAccessToken(jwtUtils.generateJwtToken(employeeEntity));
//            RefreshToken refreshToken = refreshTokenService.createRefreshToken(employeeEntity.getUsername());
//            loginResponse.setRefreshToken(refreshToken.getToken());
//            loginResponse.setModules(modulesRepository.getEnabledModules(false));
//            setEmployeeDetails(loginRequest.getUsername(), loginResponse);
//            loginResponse.setPermissions(getPermissions(loginResponse.getDesignationId(), WEB.getType()));
//            loginResponse.setMasterLabels(labelsRepository.findAll());
//            employeeEntity.setFailedAttempts(0);
//            activeTokenManager.generateAndManageTokenForUser(employeeEntity.getUsername(), loginResponse.getAccessToken());
//            employeeRepository.save(employeeEntity);
//            loginResponse.setLastLoggedInAt(loginHistoryRepository.getLastLoginTime(loginRequest.getUsername()));
//            employeeService.saveLoginHistory(loginRequest.getUsername());
//            return ResponseEntity.ok(loginResponse);
//        } catch (BadCredentialsException exception) {
//            log.error("Invalid Credentials {}", exception.getMessage());
//            loginResponse.setMessage(exception.getMessage());
//            employeeService.handleWrongPasswordAttempt(loginRequest.getUsername());
//            return ResponseEntity.ok(loginResponse);
//        } catch (Exception exception) {
//            log.error("Error While Logging in {}", exception.getMessage());
//            loginResponse.setMessage(exception.getMessage());
//            return ResponseEntity.ok(loginResponse);
//        }
//    }
//
//    public List<String> getPermissions(int designationId, int type) {
//        return permissionsRepository.getAssignedPermissions(designationId, true, type);
//    }
//
//    private LoginResponse setEmployeeDetails(String employeeId, LoginResponse loginResponse) {
//        List<EmployeeDesignationProjections> list = employeeRepository.getEmployeeDesignationDetails(employeeId);
//        Optional<EmployeeEntity> employeeEntity = employeeRepository.findByEmployeeId(employeeId);
//        if (employeeEntity.isPresent()) {
//            loginResponse.setEmployeeId(employeeEntity.get().getEmployeeId());
//            loginResponse.setName(employeeEntity.get().getName());
//            loginResponse.setCategoryId(employeeEntity.get().getCategoryId());
//            loginResponse.setPoliceStation(employeeEntity.get().getPoliceStation());
//            loginResponse.setCircles(employeeEntity.get().getCircles());
//            loginResponse.setDesignationId(employeeEntity.get().getDesignationId());
//            loginResponse.setDistrictName(employeeEntity.get().getDistrictName());
//            loginResponse.setAddress(employeeEntity.get().getAddress());
//        }
//        if (list.size() > 0) {
//            EmployeeDesignationProjections designationEntity = list.get(0);
//            loginResponse.setDistrictId(designationEntity.getDistrictId());
//            loginResponse.setDesignationId(designationEntity.getDesignationId());
//            loginResponse.setDesignationName(designationEntity.getDesignationName());
//            loginResponse.setEmployeeDesignationId(designationEntity.getEmployeeDesignationId());
//        }
//        return loginResponse;
//    }
//

//    @PostMapping("/signup")
//    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
//        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
//            return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
//        }
//
//        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
//            return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
//        }

        // Create new user's account
//        User user = new User(signUpRequest.getUsername(), signUpRequest.getEmail(),
//                encoder.encode(signUpRequest.getPassword()));

//        Set<String> strRoles = signUpRequest.getRole();
//        Set<Role> roles = new HashSet<>();

//        if (strRoles == null) {
//            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
//                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
//            roles.add(userRole);
//        } else {
//            strRoles.forEach(role -> {
//                switch (role) {
//                    case "admin":
//                        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
//                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
//                        roles.add(adminRole);
//
//                        break;
//                    case "mod":
//                        Role modRole = roleRepository.findByName(ERole.ROLE_MODERATOR)
//                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
//                        roles.add(modRole);
//
//                        break;
//                    default:
//                        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
//                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
//                        roles.add(userRole);
//                }
//            });
//        }
//
//        user.setRoles(roles);
//        userRepository.save(user);
//
//        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
//    }


//    @PostMapping("/refreshtoken")
//    public ResponseEntity<?> refreshtoken(@Valid @RequestBody TokenRefreshRequest request) {
//        String requestRefreshToken = request.getRefreshToken();
//
//        return refreshTokenService.findByToken(requestRefreshToken)
//                .map(refreshTokenService::verifyExpiration)
//                .map(RefreshToken::getEmployee)
//                .map(employee -> {
//                    String token = jwtUtils.generateTokenFromUsername(employee.getUsername());
//                    return ResponseEntity.ok(new TokenRefreshResponse(token, requestRefreshToken));
//                })
//                .orElseThrow(() -> new TokenRefreshException(requestRefreshToken,
//                        "Refresh token is not in database!"));
//    }

//    @PostMapping("/signout")
//    public ResponseEntity<?> logoutUser() {
//        Object principle = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        if (!principle.toString().equals("anonymousUser")) {
//            String employeeId = ((UserDetails) principle).getUsername();
//            refreshTokenService.deleteByUserId(employeeId);
//            activeTokenManager.removeTokensForUser(employeeId);
//        }
//        return ResponseEntity.ok(new MessageResponse("Log out successful!"));
//    }
}
