package com.billing.Invoizo.initialsetup.service;


import com.billing.Invoizo.InvoizoProperties;
import com.billing.Invoizo.constants.EnumConstants;
import com.billing.Invoizo.initialsetup.dto.InitialSetupDTO;
import com.billing.Invoizo.masters.designations.repository.DesignationRepository;
import com.billing.Invoizo.masters.designations.service.DesignationService;
import com.billing.Invoizo.masters.modules.entity.ModulesEntity;
import com.billing.Invoizo.masters.modules.repository.ModulesRepository;
import com.billing.Invoizo.masters.ulb.entity.ULBEntity;
import com.billing.Invoizo.masters.ulb.repository.ULBRepository;
import com.billing.Invoizo.user.entity.EmployeeEntity;
import com.billing.Invoizo.user.entity.EmployeeULBDesignationEntity;
import com.billing.Invoizo.user.repository.EmployeeRepository;
import com.billing.Invoizo.user.repository.EmployeeULBDesignationRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.*;

@Service
@Slf4j
public class InitialSetupServiceImpl implements InitialSetupService {

    private final InvoizoProperties invoizoProperties;

    private final RestTemplateBuilder restTemplateBuilder;

    private final ULBRepository ulbRepository;


    private final ModulesRepository modulesRepository;

    private final PasswordEncoder passwordEncoder;

    private final EmployeeRepository employeeRepository;

    private final EmployeeULBDesignationRepository employeeULBDesignationRepository;

    private final EntityManager entityManager;

    private final DesignationRepository designationRepository;

    private final DesignationService designationService;

    @Autowired
    public InitialSetupServiceImpl(InvoizoProperties invoizoProperties, RestTemplateBuilder restTemplateBuilder,
                                   ULBRepository ulbRepository, ModulesRepository modulesRepository,
                                   PasswordEncoder passwordEncoder, EmployeeRepository employeeRepository,
                                   EmployeeULBDesignationRepository employeeULBDesignationRepository,
                                   EntityManager entityManager,
                                   DesignationRepository designationRepository, DesignationService designationService) {
        this.invoizoProperties = invoizoProperties;
        this.restTemplateBuilder = restTemplateBuilder;
        this.ulbRepository = ulbRepository;
        this.modulesRepository = modulesRepository;
        this.passwordEncoder = passwordEncoder;
        this.employeeRepository = employeeRepository;
        this.employeeULBDesignationRepository = employeeULBDesignationRepository;
//        this.userManagementService = userManagementService;
        this.entityManager = entityManager;
//        this.validationUtils = validationUtils;
        this.designationRepository = designationRepository;
        this.designationService = designationService;
    }


    @Override
    public JSONObject checkLicenseKey(String licenseId, String subClientUsername) {
        String theUrl = invoizoProperties.getSecurityCheckURL() + "/web/security/check/?licenseId=" + licenseId + "&subClientUsername="
                + subClientUsername;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<Object> entity = new HttpEntity<>(headers);
        ResponseEntity<JSONObject> response = restTemplateBuilder.build().exchange(theUrl,
                HttpMethod.POST, entity, JSONObject.class);
        return response.getBody();
    }

    @Override
    @Transactional
    public void saveAllDetails(InitialSetupDTO initialSetupDTO) throws IOException {

        List<EmployeeEntity> employeeEntities = new ArrayList<>();
        ULBEntity ulbEntity = initialSetupDTO.getUlbEntity();
        int id = 1;
        String ulb = "ULB";
        String superAdminUlbCode = null;
        List<ULBEntity> ulbEntities = initialSetupDTO.getMultiple();
        ULBEntity ulbEntity1 = new ULBEntity();
//        for (ULBEntity ulbEntity1 : ulbEntities) {
//            ulbEntity1.setLicenseId(ulbEntity.getLicenseId());
//            ulbEntity1.setSubClientId(ulbEntity.getSubClientId());
        if (ulbEntity1.getUlbCode() == null) {
            ulbEntity1.setUlbCode(ulb + id);
//                id++;
        }
        ulbRepository.saveAndFlush(ulbEntity1);

        // Adding Admins for All ULB's
        EmployeeEntity adminEmployeeEntity = new EmployeeEntity();
        adminEmployeeEntity.setName(ulbEntity1.getName() + " Admin ");
        adminEmployeeEntity.setEmployeeId(adminEmployeeEntity.getName().toLowerCase().replaceAll(" ", ""));
        adminEmployeeEntity.setDesignationId(EnumConstants.Designations.ADMIN.getValue());
        adminEmployeeEntity.setHasAgreedTerms(true);
        adminEmployeeEntity.setUlbCode(ulbEntity1.getUlbCode());
        employeeEntities.add(adminEmployeeEntity);
        superAdminUlbCode = ulbEntity1.getUlbCode();
//        }

        //Saving Designations
        designationService.designationSave();

        // Saving Modules
//        updateModules(initialSetupDTO);

        // Adding Super Admin
        EmployeeEntity superAdminemployeeEntity = new EmployeeEntity();
        superAdminemployeeEntity.setEmployeeId("superadmin");
        superAdminemployeeEntity.setName("Super Admin");
        superAdminemployeeEntity.setDesignationId(EnumConstants.Designations.SUPER_ADMIN.getValue());
        superAdminemployeeEntity.setHasAgreedTerms(true);
        superAdminemployeeEntity.setUlbCode(superAdminUlbCode);
        employeeEntities.add(superAdminemployeeEntity);

        // adding admins for multiple ulbs
//        if (initialSetupDTO.getAdmins() != null) {
//            employeeEntities.addAll(initialSetupDTO.getAdmins());
//        }
//        if (initialSetupDTO.getEmployeeEntities() != null && !initialSetupDTO.getEmployeeEntities().isEmpty()) {
//            employeeEntities.addAll(initialSetupDTO.getEmployeeEntities());
//        }

        // saving Employees
        saveEmployees(employeeEntities);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("employeeEntities", employeeEntities);
        jsonObject.put("ulbEntity", ulbEntity);

//        userManagementService.checkBefore(jsonObject);
    }

    @Override
    public List<ModulesEntity> getListOfExistingData() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<ModulesEntity> cq = cb.createQuery(ModulesEntity.class);
        Root<ModulesEntity> root = cq.from(ModulesEntity.class);
        cq.select(root).distinct(true);
        List<ModulesEntity> results = entityManager.createQuery(cq).getResultList();
        return results;
    }

    /**
     * Update modules.
     * This method is used to update Setup modules in ModulesEntity.
     *
     * @param initialSetupDTO the initial setup DTO
     */
    private void updateModules(InitialSetupDTO initialSetupDTO) {
        for (ModulesEntity moduleEntity : initialSetupDTO.getModuleEntities()) {
            modulesRepository.save(moduleEntity);
        }
    }

    /**
     * Save employees.
     * This method is used save employee details into EmployeeEntity and EmployeeULBDesignationEntity with all details
     *
     * @param employeeEntities the employee entities
     */
    private void saveEmployees(List<EmployeeEntity> employeeEntities) {
        for (EmployeeEntity employeeEntity : employeeEntities) {
            employeeEntity.setAccountNonExpired(true);
            employeeEntity.setAccountNonLocked(true);
            employeeEntity.setAccountNonExpired(true);
            employeeEntity.setCredentialsNonExpired(true);
            employeeEntity.setPassword(passwordEncoder.encode(employeeEntity.getEmployeeId()));
            employeeEntity.setCreatedAt(new Date());
            employeeEntity.setEnabled(true);
            employeeRepository.saveAndFlush(employeeEntity);

            EmployeeULBDesignationEntity designationEntity = new EmployeeULBDesignationEntity();
            designationEntity.setActiveStatus(true);
            designationEntity.setDesignationId(employeeEntity.getDesignationId());
            designationEntity.setEmployeeId(employeeEntity.getEmployeeId());
            designationEntity.setUlbCode(employeeEntity.getUlbCode());
            designationEntity.setEmployeeDesignationId(designationEntity.getEmployeeId()
                    + designationEntity.getDesignationId() + designationEntity.getUlbCode());
            designationEntity.setBaseDesignation(true);
            employeeULBDesignationRepository.saveAndFlush(designationEntity);
        }
    }


    @Override
    @Transactional
    public JSONObject checkForMasterData() {
        JSONObject jsonObject = new JSONObject();
        boolean flag = false;
        List<String> list = new ArrayList<>();

        boolean designationsFlag = designationRepository.count() > 0;
        if (designationsFlag) {
            list.add("Designations Details");
            flag = true;
        }

        jsonObject.put("flag", flag);
        if (flag) {
            jsonObject.put("dataList", list);
        }
        return jsonObject;
    }


}
