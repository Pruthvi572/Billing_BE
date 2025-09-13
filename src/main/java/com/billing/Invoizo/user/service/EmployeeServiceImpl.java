package com.billing.Invoizo.user.service;

import com.billing.Invoizo.InvoizoProperties;
import com.billing.Invoizo.base.service.BaseServiceImpl;
import com.billing.Invoizo.constants.EnumConstants;
import com.billing.Invoizo.masters.designations.entity.DesignationsEntity;
import com.billing.Invoizo.user.dto.EmployeeBankDetailsDTO;
import com.billing.Invoizo.user.dto.EmployeeDTO;
import com.billing.Invoizo.user.dto.EmployeeListDTO;
import com.billing.Invoizo.user.dto.EmployeeViewDTO;
import com.billing.Invoizo.user.entity.EmployeeBankDetailsEntity;
import com.billing.Invoizo.user.entity.EmployeeEntity;
import com.billing.Invoizo.user.entity.EmployeeULBDesignationEntity;
import com.billing.Invoizo.user.entity.UserLoginHistory;
import com.billing.Invoizo.user.repository.EmployeeBankDetailsRepository;
import com.billing.Invoizo.user.repository.EmployeeRepository;
import com.billing.Invoizo.user.repository.EmployeeULBDesignationRepository;
import com.billing.Invoizo.user.repository.UserLoginHistoryRepository;
import com.billing.Invoizo.util.EmailBroadCast;
import com.billing.Invoizo.util.OTPGenerate;
import com.billing.Invoizo.util.SMSBroadcast;
import com.billing.Invoizo.util.StringUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.criteria.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class EmployeeServiceImpl extends BaseServiceImpl implements EmployeeService {


    private final UserLoginHistoryRepository loginHistoryRepository;
    private final EmployeeRepository employeeRepository;
    private final InvoizoProperties properties;
    private final OTPGenerate otpGenerate;
    private final PasswordEncoder passwordEncoder;
    private final EmployeeULBDesignationRepository employeeULBDesignationRepository;
    private final EntityManager entityManager;
    private final EmployeeBankDetailsRepository employeeBankDetailsRepository;


    @Autowired
    public EmployeeServiceImpl(UserLoginHistoryRepository loginHistoryRepository, EmployeeRepository employeeRepository,
                               RestTemplateBuilder restTemplateBuilder,
                               InvoizoProperties properties, EmailBroadCast emailBroadCast,
                               SMSBroadcast smsBroadcast, OTPGenerate otpGenerate,
                               PasswordEncoder passwordEncoder,
                               EmployeeULBDesignationRepository employeeULBDesignationRepository,
                               EntityManager entityManager, EmployeeBankDetailsRepository employeeBankDetailsRepository) {
        this.loginHistoryRepository = loginHistoryRepository;
        this.employeeRepository = employeeRepository;
        this.properties = properties;
        this.otpGenerate = otpGenerate;
        this.passwordEncoder = passwordEncoder;
        this.employeeULBDesignationRepository = employeeULBDesignationRepository;
        this.entityManager = entityManager;
        this.employeeBankDetailsRepository = employeeBankDetailsRepository;
    }


    @Override
    @Transactional
    public void saveLoginHistory(String username) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                .getRequest();
        UserLoginHistory userLoginHistory = new UserLoginHistory();
        userLoginHistory.setLoginTime(new Date());
        userLoginHistory.setEmployeeId(username);
        userLoginHistory.setIpAddress(request.getRemoteAddr());
        userLoginHistory.setIsFrom(EnumConstants.FromType.CLOUD);
        loginHistoryRepository.save(userLoginHistory);
    }

    @Override
    @Transactional
    public void handleWrongPasswordAttempt(String username) {
        Optional<EmployeeEntity> result = employeeRepository.findByEmployeeId(username);
        result.ifPresent(employeeEntity -> {
            if (employeeEntity.getFailedAttempts() == 3 &&
                    (employeeEntity.getLockedTime().getTime() + properties.getAccountLockTime()) < System.currentTimeMillis()) {
                employeeEntity.setFailedAttempts(1);
            } else if (employeeEntity.getFailedAttempts() < 3 && employeeEntity.getLockedTime() == null) {
                int totalFailedAttempts = employeeEntity.getFailedAttempts() + 1;
                employeeEntity.setFailedAttempts(totalFailedAttempts);
                if (totalFailedAttempts == 3) {
                    employeeEntity.setLockedTime(new Date());
                    employeeEntity.setAccountNonLocked(false);
                }
            }
            employeeRepository.save(employeeEntity);
        });
    }

    @Override
    public Integer saveEmployee(EmployeeDTO employeeDTO, String employeeDesignationId,
                                String districtId, Integer designationId) {
        Date date = new Date();
        EmployeeEntity employeeEntity = new EmployeeEntity();
        employeeEntity.setCreatedAt(date);
        employeeEntity.setModifiedAt(date);
        employeeEntity.setCreatedBy(employeeDesignationId);
        employeeEntity.setModifiedBy(employeeDesignationId);
        employeeEntity.setDeleted(false);
        employeeEntity.setDesignation(employeeDTO.getDesignation());
        employeeEntity.setPermissions(employeeDTO.getPermissions());
        employeeEntity.setProfilePicture(employeeDTO.getProfilePicture());
        employeeEntity.setDesignationId(employeeDTO.getDesignationId());
        employeeEntity.setName(employeeDTO.getName());
        employeeEntity.setMobileNumber(employeeDTO.getMobileNumber());
        employeeEntity.setGender(employeeDTO.getGender());
        employeeEntity.setEmail(employeeDTO.getEmail());
        employeeEntity.setPan(employeeDTO.getPan());
        employeeEntity.setPhotoPath(employeeDTO.getPhotoPath());
        employeeEntity.setEnabled(true);
        employeeEntity.setTransferred(false);
        employeeEntity.setHasWritePermission(true);
        employeeEntity.setIpAddress(employeeDTO.getIpAddress());
        employeeEntity.setCloudLastLog(employeeDTO.getCloudLastLog());
        employeeEntity.setMobileLastLog(employeeDTO.getMobileLastLog());
        employeeEntity.setAccountNonExpired(true);
        employeeEntity.setAccountNonLocked(true);
        employeeEntity.setCredentialsNonExpired(true);
        employeeEntity.setHasAgreedTerms(true);
        employeeEntity.setActive(false);
        employeeEntity.setFailedAttempts(0);
        employeeEntity.setHierarchy(employeeDTO.getHierarchy());
        employeeEntity.setEligibleForIncentives(employeeDTO.isEligibleForIncentives());
        employeeEntity.setMemberCode(employeeDTO.getMemberCode());
        String password = String.valueOf(otpGenerate.generate());
        employeeEntity.setPassword(passwordEncoder.encode(password));
        employeeEntity.setEmployeeId(employeeDTO.getEmployeeId());
        employeeRepository.save(employeeEntity);
        saveEmployeeULBDesignations(employeeEntity);
        if (employeeDTO.getIfscCode() != null && !employeeDTO.getIfscCode().isEmpty()) {
            saveEmployeeBankDetails(employeeDTO, date, employeeDesignationId);
        }
//        sendEmail(employeeEntity, password);
//        sendSMS(employeeEntity, password);
        return 0;
    }

    private void saveEmployeeULBDesignations(EmployeeEntity employeeEntity) {
        EmployeeULBDesignationEntity employeeULBDesignation = new EmployeeULBDesignationEntity();
        employeeULBDesignation.setEmployeeDesignationId(employeeEntity.getEmployeeId() + employeeEntity.getDesignationId());
        employeeULBDesignation.setEmployeeId(employeeEntity.getEmployeeId());
        employeeULBDesignation.setActiveStatus(true);
        employeeULBDesignation.setDesignationId(employeeEntity.getDesignationId());
        employeeULBDesignationRepository.save(employeeULBDesignation);
    }

    public void saveEmployeeBankDetails(EmployeeDTO employeeDTO, Date date, String employeeDesignationId) {
        EmployeeBankDetailsEntity employeeBankDetailsEntity = new EmployeeBankDetailsEntity();
        employeeBankDetailsEntity.setBankName(employeeDTO.getBankName());
        employeeBankDetailsEntity.setEmployeeId(employeeDTO.getEmployeeId());
        employeeBankDetailsEntity.setIfscCode(employeeDTO.getIfscCode());
        employeeBankDetailsEntity.setAccountNumber(employeeDTO.getAccountNumber());
        employeeBankDetailsEntity.setBranchName(employeeDTO.getBranchName());
        employeeBankDetailsEntity.setCreatedAt(date);
        employeeBankDetailsEntity.setModifiedAt(date);
        employeeBankDetailsEntity.setCreatedBy(employeeDesignationId);
        employeeBankDetailsEntity.setModifiedBy(employeeDesignationId);
        employeeBankDetailsEntity.setBankCode(employeeDTO.getBankCode());
        employeeBankDetailsEntity.setBankBranchCode(employeeDTO.getBankBranchCode());
        employeeBankDetailsEntity.setAccountOpenDate(employeeDTO.getAccountOpenDate());
        employeeBankDetailsRepository.save(employeeBankDetailsEntity);
    }

    @Override
    @Transactional
    public <T> T getAllEmployees(Integer pageNumber, Integer pageSize, String searchValue, Integer areaValue) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<EmployeeListDTO> criteriaQuery = criteriaBuilder.createQuery(EmployeeListDTO.class);
        Root<EmployeeEntity> queryRoot = criteriaQuery.from(EmployeeEntity.class);
        Join<EmployeeEntity, DesignationsEntity> designationJoin = queryRoot.join("designationsEntity", JoinType.LEFT);
        CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
        Root<EmployeeEntity> countRoot = countQuery.from(EmployeeEntity.class);
        Join<EmployeeEntity, DesignationsEntity> designationCountJoin = countRoot.join("designationsEntity", JoinType.LEFT);
        List<Predicate> dataPredicates = new ArrayList<>();
        List<Predicate> countPredicates = new ArrayList<>();

        if (!StringUtil.isNullOrEmpty(searchValue)) {
            searchValue = searchValue.toLowerCase();
            dataPredicates.add(criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(queryRoot.get("name")), '%' + searchValue + '%'),
                    criteriaBuilder.like(criteriaBuilder.lower(queryRoot.get("mobileNumber")), '%' + searchValue + '%'),
                    criteriaBuilder.like(criteriaBuilder.lower(queryRoot.get("employeeId")), '%' + searchValue + '%')
            ));
            countPredicates.add(criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(countRoot.get("name")), '%' + searchValue + '%'),
                    criteriaBuilder.like(criteriaBuilder.lower(countRoot.get("mobileNumber")), '%' + searchValue + '%'),
                    criteriaBuilder.like(criteriaBuilder.lower(countRoot.get("employeeId")), '%' + searchValue + '%')
            ));
        }

        dataPredicates.add(criteriaBuilder.notEqual(queryRoot.get("designationId"), EnumConstants.Designations.SHG_MEMBER.getValue()));
        countPredicates.add(criteriaBuilder.notEqual(countRoot.get("designationId"), EnumConstants.Designations.SHG_MEMBER.getValue()));

        criteriaQuery.select(criteriaBuilder.construct(
                        EmployeeListDTO.class,
                        queryRoot.get("employeeId"),
                        queryRoot.get("name"),
                        queryRoot.get("mobileNumber"),
                        queryRoot.get("gender"),
                        designationJoin.get("name") // maps to DesignationName
                )).where(dataPredicates.toArray(new Predicate[]{}))
                .orderBy(criteriaBuilder.desc(queryRoot.get("modifiedAt")));
        Query dataQuery = entityManager.createQuery(criteriaQuery);
        countQuery.select(criteriaBuilder.count(countRoot));
        countQuery.where(countPredicates.toArray(new Predicate[]{}));
        Query count = entityManager.createQuery(countQuery);
        return paginationResponse(pageNumber, pageSize, dataQuery, count);
    }


//    private void sendSMS(EmployeeEntity employeeEntity, String password) {
//        SMSContentFieldsDTO smsContentFieldsDTO = new SMSContentFieldsDTO();
//        smsContentFieldsDTO.setMobileNumber(employeeEntity.getMobileNumber());
//        smsContentFieldsDTO.setEmployeeId(employeeEntity.getEmployeeId());
//        smsContentFieldsDTO.setPassword(password);
//        smsContentFieldsDTO.setConstant(String.valueOf(EMPLOYEE_REGISTRATION));
//        notificationService.setMessageContent(smsContentFieldsDTO, EMPLOYEE_REGISTRATION.getValue());
//    }
//
//    private void sendEmail(EmployeeEntity employeeEntity, String password) {
//        StringBuilder emailContent = new StringBuilder();
//        emailContent.append("Thank you for registering with REACH. We're excited to have you on board." +
//                        " Your account has been successfully created, and here are your login credentials:")
//                .append("\n")
//                .append("\n")
//                .append("User ID : ").append(employeeEntity.getEmployeeId())
//                .append("\n").append("Password : ").append(password)
//                .append("\n");
//        if (!StringUtil.isNullOrEmpty(employeeEntity.getEmail())) {
//            emailContent.append("Email : ").append(employeeEntity.getEmail()).append("\n");
//        }
//        emailContent.append("\n").append("Please make sure to keep your login credentials secure and do not share them with anyone." +
//                        " For security reasons, we recommend that you change your password after your first login.")
//                .append("\n")
//                .append("\n")
//                .append("We are here to provide you with a seamless filing experience." +
//                        " Thank you for choosing REACH. We look forward to serving you.")
//                .append("\n")
//                .append("\n")
//                .append("Best regards,")
//                .append("\n")
//                .append("Reach team.");
//        emailBroadCast.sendEmail("Reach Username and Password Created", emailContent.toString(),
//                employeeEntity.getEmail(), null);
//    }


    @Override
    @Transactional
    public EmployeeViewDTO getEmployeeView(String employeeId) {
        Optional<EmployeeEntity> employeeEntity = employeeRepository.findByEmployeeId(employeeId);
        if (employeeEntity.isPresent()) {
            EmployeeViewDTO employeeViewDTO = new EmployeeViewDTO();
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<EmployeeDTO> cq = cb.createQuery(EmployeeDTO.class);
            Root<EmployeeEntity> root = cq.from(EmployeeEntity.class);
            Join<EmployeeEntity, DesignationsEntity> designationsEntityJoin = root.join("designationsEntity",
                    JoinType.LEFT);
            Join<EmployeeEntity, EmployeeBankDetailsEntity> employeeBankDetailsEntityJoin = root.join("employeeBankDetailsEntity",
                    JoinType.LEFT);
            cq.select(cb.construct(EmployeeDTO.class,
                            root.get("employeeId"),
                            root.get("designationId"),
                            root.get("name"),
                            root.get("password"),
                            root.get("mobileNumber"),
                            root.get("gender"),
                            root.get("email"),
                            root.get("pan"),
                            root.get("photoPath"),
                            root.get("createdBy"),
                            root.get("createdAt"),
                            root.get("modifiedBy"),
                            root.get("modifiedAt"),
                            root.get("enabled"),
                            root.get("isDeleted"),
                            root.get("isTransferred"),
                            root.get("hasWritePermission"),
                            root.get("ipAddress"),
                            root.get("cloudLastLog"),
                            root.get("mobileLastLog"),
                            root.get("accountNonExpired"),
                            root.get("accountNonLocked"),
                            root.get("credentialsNonExpired"),
                            root.get("otp"),
                            root.get("otpMobileNumber"),
                            root.get("otpVerifiedAt"),
                            root.get("otpExpiresAt"),
                            root.get("hasAgreedTerms"),
                            root.get("regId"),
                            root.get("deviceModel"),
                            root.get("mobileVersion"),
                            root.get("mobileSessionId"),
                            root.get("mobileLoginStatus"),
                            root.get("aadharNumber"),
                            root.get("address"),
                            root.get("ulbCode"),
                            root.get("mobileIpAddress"),
                            root.get("mobileLastLocation"),
                            root.get("lastSyncTime"),
                            root.get("transferOtp"),
                            root.get("transferOtpExpiresAt"),
                            root.get("lockedTime"),
                            root.get("lastFailed"),
                            root.get("failedAttempts"),
                            root.get("isActive"),
                            root.get("memberCode"),
                            root.get("hierarchy"),
                            root.get("isEligibleForIncentives"),
                            root.get("shgCode"),
                            designationsEntityJoin.get("name").alias("designationName"),
                            designationsEntityJoin.get("abbreviation").alias("designationAbbreviation"),
                            employeeBankDetailsEntityJoin.get("accountNumber"),
                            employeeBankDetailsEntityJoin.get("ifscCode"),
                            employeeBankDetailsEntityJoin.get("branchName"),
                            employeeBankDetailsEntityJoin.get("bankName")))
                    .where(cb.equal(root.get("employeeId"), employeeId));
            EmployeeDTO employeeDTO = entityManager.createQuery(cq).getSingleResult();
            employeeViewDTO.setEmployeeDTO(employeeDTO);
            return employeeViewDTO;
        }
        return null;
    }

    @Override
    public List<EmployeeBankDetailsDTO> getShgBankDetails(String employeeId) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<EmployeeBankDetailsDTO> query = criteriaBuilder.createQuery(EmployeeBankDetailsDTO.class);
        Root<EmployeeBankDetailsEntity> root = query.from(EmployeeBankDetailsEntity.class);
        List<Predicate> predicates = new ArrayList<>();
        if (employeeId != null && !employeeId.isEmpty()) {
            predicates.add(criteriaBuilder.equal(root.get("employeeId"), employeeId));
        }
        query.select(criteriaBuilder.construct(EmployeeBankDetailsDTO.class,
                root.get("id").alias("bankdetailsid"),
                root.get("bankName"),
                root.get("ifscCode"),
                root.get("branchName"),
                root.get("accountNumber"),
                root.get("bankBranchCode"),
                root.get("bankCode"),
                root.get("accountOpenDate")));
        query.where(criteriaBuilder.and(predicates.toArray(new Predicate[0])));
        return entityManager.createQuery(query).getResultList();
    }
}
