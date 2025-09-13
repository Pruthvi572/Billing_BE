//package com.leadwinner.loanmanagement.mobile.service;
//
//import com.leadwinner.loanmanagement.constants.EnumConstants;
//
//import com.leadwinner.loanmanagement.mobile.dto.Encryption;
//import com.leadwinner.loanmanagement.mobile.dto.MobileLoginResponseEntity;
//import com.leadwinner.loanmanagement.security.controllers.AuthController;
//import com.leadwinner.loanmanagement.security.jwt.JwtUtils;
//import com.leadwinner.loanmanagement.security.payload.request.MobileLoginRequest;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import javax.crypto.BadPaddingException;
//import javax.crypto.IllegalBlockSizeException;
//import javax.crypto.NoSuchPaddingException;
//import java.io.UnsupportedEncodingException;
//import java.security.InvalidAlgorithmParameterException;
//import java.security.InvalidKeyException;
//import java.security.NoSuchAlgorithmException;
//import java.security.spec.InvalidKeySpecException;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//import java.util.Optional;
//
//
//@Service("mobileCredentialsServiceImpl")
//@Slf4j
//public class MobileCredentialsServiceImpl implements MobileCredentialsService {
////    @Autowired
////    AuthenticationManager authenticationManager;
////    @Autowired
////    private MasterLabelsRepository labelsRepository;
////    @Autowired
////    private EmployeeRepository employeeRepository;
////    @Autowired
////    private PasswordEncoder passwordEncoder;
////    @Autowired
////    private AuthController authController;
////    @Autowired
////    @Qualifier("userServiceImpl")
////    private UserServiceImpl userServiceImpl;
////    @Autowired
////    private ModulesRepository modulesRepository;
////    @Autowired
////    JwtUtils jwtUtils;
////    @Override
////    @Transactional
//    public MobileLoginResponseEntity isAuthorized(final MobileLoginRequest loginRequest)
//            throws InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException,
//            NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException,
//            BadPaddingException, UnsupportedEncodingException {
//
//        MobileLoginResponseEntity entity = new MobileLoginResponseEntity();
//        boolean enabled;
//        String employeeId;
//        String user = loginRequest.getUsername();
//        Optional<EmployeeEntity> response = employeeRepository.findByEmployeeId(user);
//        if (response.isEmpty() || !passwordEncoder.matches(loginRequest.getPassword(), response.get().getPassword())) {
//            log.info("Invalid User");
//            entity.setStatus(EnumConstants.MobileStatus.INVALID_USER.getValue());
//            return entity;
//        } else if (!userServiceImpl.checkBeforeLogin(loginRequest.getUsername()) && response.get().getCategoryId() != CITIZEN_ID) {
//            log.info("Access Denied ");
//            entity.setStatus(EnumConstants.MobileStatus.ACCESS_DENIED.getValue());
//            return entity;
//        } else {
//            if (response.get().isDeleted()) {
//                log.info("Deleted");
//                entity.setStatus(EnumConstants.MobileStatus.DELETED.getValue());
//                return entity;
//            } else {
//                enabled = response.get().isEnabled();
//                employeeId = response.get().getEmployeeId();
//            }
//        }
//        if (!enabled) {
//            log.info("Account Locked");
//            entity.setStatus(EnumConstants.MobileStatus.ACCOUNT_LOCKED.getValue());
//            return entity;
//        } else {
//            final List<String> employeeIds = new ArrayList<>();
//            List<EmployeeDesignationProjections> assignedULBDTOs = employeeRepository.getEmployeeDesignationDetails(employeeId);
//            assignedULBDTOs.forEach(t -> employeeIds.add(t.getEmployeeDesignationId()));
//            entity.setPermissions(authController.getPermissions(response.get().getDesignationId(), MOBILE.getType()));
//            entity.setUlbList(assignedULBDTOs);
//            entity.setEmployeeDesignationIds(employeeIds);
//            entity.setPassword(Encryption.encrypt(loginRequest.getPassword()));
//            Authentication authentication = authenticationManager
//                    .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
//            SecurityContextHolder.getContext().setAuthentication(authentication);
//            EmployeeEntity userDetails = (EmployeeEntity) authentication.getPrincipal();
//            entity.setToken(jwtUtils.generateJwtToken(userDetails));
//            entity.setLabels(labelsRepository.findAll());
//
//            entity.setModules(modulesRepository.getEnabledModules(false));
//            response.get().setRegId(loginRequest.getRegId());
//            response.get().setDeviceModel(loginRequest.getDeviceModel());
//            response.get().setMobileSessionId(loginRequest.getMobileSessionId());
//            response.get().setMobileVersion(loginRequest.getMobileVersion());
//            if (addMobileInfo(response) == EnumConstants.MobileStatus.LOGIN_SUCCESS.getValue()) {
//                entity.setEmployeeName(response.get().getName());
//                entity.setEmail(response.get().getEmail());
//                entity.setMobileNumber(response.get().getMobileNumber());
//                entity.setAddress(response.get().getAddress());
//                entity.setPhotoPath(response.get().getPhotoPath());
//                entity.setEmployeeId(response.get().getEmployeeId());
//                entity.setCategoryid(response.get().getCategoryId());
//                entity.setCircles(response.get().getCircles());
//                entity.setPoliceStation(response.get().getPoliceStation());
//                entity.setCategoryName(getCategoryName(response.get().getCategoryId()));
//                entity.setDesignationId(response.get().getDesignationId());
//                entity.setDistrictId(response.get().getDistrictId());
//                entity.setStatus(EnumConstants.MobileStatus.LOGIN_SUCCESS.getValue());
//                log.info("Login Success");
//                return entity;
//            } else {
//                log.info("Already Success");
//                entity.setStatus(EnumConstants.MobileStatus.LOGIN_LOGGEDIN.getValue());
//                return entity;
//            }
//        }
//    }
//
//
//    private byte addMobileInfo(Optional<EmployeeEntity> employeeEntity) {
//        if (employeeEntity.get().getMobileLoginStatus() != EnumConstants.MobileStatus.LOGIN_SUCCESS.getValue()) {
//            employeeRepository.updateEmployeeMobileStatus(employeeEntity.get().getEmployeeId(),
//                    EnumConstants.MobileStatus.LOGIN_SUCCESS.getValue(), new Date());
//            return EnumConstants.MobileStatus.LOGIN_SUCCESS.getValue();
//        } else {
//            return EnumConstants.MobileStatus.LOGIN_LOGGEDIN.getValue();
//        }
//    }
//
//    @Override
//    @Transactional
//    public void logout(String employeeId) {
//        employeeRepository.logout(employeeId, EnumConstants.MobileStatus.LOGIN_FAILURE.getValue(), "");
//    }
//
//    private String getCategoryName(int categoryId) {
//        return employeeRepository.getCategoryName(categoryId);
//    }
//
//}
