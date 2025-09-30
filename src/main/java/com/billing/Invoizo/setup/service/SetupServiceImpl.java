package com.billing.Invoizo.setup.service;


import com.billing.Invoizo.constants.SetupConstants;
import com.billing.Invoizo.masters.designation_permission.entity.DesignationPermissionEntity;
import com.billing.Invoizo.masters.designation_permission.repository.DesignationPermissionRepository;
import com.billing.Invoizo.masters.designations.entity.DesignationsEntity;
import com.billing.Invoizo.masters.designations.entity.PermissionsEntity;
import com.billing.Invoizo.masters.designations.repository.DesignationRepository;
import com.billing.Invoizo.masters.designations.repository.PermissionsRepository;
import com.billing.Invoizo.masters.designations.service.DesignationService;
import com.billing.Invoizo.masters.modules.entity.ModulesEntity;
import com.billing.Invoizo.masters.modules.repository.ModulesRepository;
import com.billing.Invoizo.sequence.service.SequenceService;
import com.billing.Invoizo.util.SequenceNamesUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class SetupServiceImpl implements SetupService {


    @Autowired
    private PermissionsRepository permissionsRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private DesignationRepository designationRepository;

    @Autowired
    private SequenceService sequenceService;

    @Autowired
    private DesignationPermissionRepository designationPermissionRepository;

    @Autowired
    private ModulesRepository modulesRepository;


    @Autowired
    private DesignationService designationService;


    @Override
    @Transactional
    public void insertDefaultValues() {

        //First we are checking the tables got created in the Database or not
        //If Tables got created, Then only we are loading all the json data and default designations and sequences
//        List<Boolean> checkIfTablesAreCreated = permissionsRepository.checkTableExists();
//        if (checkIfTablesAreCreated != null && checkIfTablesAreCreated.get(0)) {

        loadSequences();


//        loadDefaultPermissions();

//            loadEmployeeCategories();

        loadSetupModules();

        loadDefaultDesignations();

//            loadDefaultLabels();

//            loadNotificationActions();

//            loadHierarchy();

//        }
    }


    /**
     * Loading Notification Actions from notifications.json by which we can enable and disable notifications
     */
//    @SneakyThrows
//    private void loadNotificationActions() {
//        ObjectMapper objectMapper = new ObjectMapper();
//        ClassPathResource notificationActions = new ClassPathResource("jsonScripts/notifications.json");
//        List<Map<String, String>> notificationActionsList = objectMapper.readValue(notificationActions.getFile(), List.class);
//        List<String> existingNotification = notificationActionsRepository.getModuleAbbrevationList();
//        for (Map<String, String> notificationAction : notificationActionsList) {
//            if (!existingNotification.contains(notificationAction.get("moduleAbbrevation"))) {
//                NotificationActionsEntity notificationActionsEntity = new NotificationActionsEntity();
//                notificationActionsEntity.setModuleName(notificationAction.get("moduleName"));
//                notificationActionsEntity.setModuleAbbrevation(notificationAction.get("moduleAbbrevation"));
//                notificationActionsEntity.setModuleConstant(notificationAction.get("moduleConstant"));
//                notificationActionsEntity.setAction(notificationAction.get("action"));
//                notificationActionsEntity.setActionConstant(notificationAction.get("actionConstant"));
//                notificationActionsEntity.setHasFCM(Boolean.parseBoolean(notificationAction.get("hasFCM")));
//                notificationActionsEntity.setHasEmail(Boolean.parseBoolean(notificationAction.get("hasEmail")));
//                notificationActionsEntity.setHasSMS(Boolean.parseBoolean(notificationAction.get("hasSMS")));
//                notificationActionsRepository.save(notificationActionsEntity);
//            }
//        }
//    }

    /**
     * Loding Module Names from setupModules.json
     * By using these we can enable and disable the module as well as approval flow of an module
     * In this setupModules.json id and approvalType will be Unique
     */
    @SneakyThrows
    private void loadSetupModules() {
        ObjectMapper objectMapper = new ObjectMapper();
        ClassPathResource setupModulesFile = new ClassPathResource("jsonScripts/setupModules.json");
        List<Map<String, String>> setupModulesArrays = objectMapper.readValue(setupModulesFile.getFile(), List.class);
        List<Integer> existingModules = modulesRepository.getModuleIds();
        for (Map<String, String> setupModule : setupModulesArrays) {
            if (!existingModules.contains(Integer.parseInt(setupModule.get("id")))) {
                ModulesEntity modulesEntity = new ModulesEntity();
                modulesEntity.setId(Integer.parseInt(setupModule.get("id")));
                modulesEntity.setModuleName(setupModule.get("moduleName"));
                modulesEntity.setConstant(setupModule.get("constant"));
                modulesEntity.setDisabled(Boolean.parseBoolean(setupModule.get("isDisabled")));
                modulesEntity.setCanBeDisabled(Boolean.parseBoolean(setupModule.get("canBeDisabled")));
                modulesEntity.setModuleDescription(setupModule.get("moduleDescription"));
                modulesEntity.setModuleImage(setupModule.get("moduleImage"));
                modulesEntity.setHasApprovalFlow(Boolean.parseBoolean(setupModule.get("hasApprovalFlow")));
                modulesEntity.setCanBeDisabledFlow(Boolean.parseBoolean(setupModule.get("canBeDisabledFlow")));
                modulesEntity.setApprovalFlowEnabled(Boolean.parseBoolean(setupModule.get("isApprovalFlowEnabled")));
                modulesRepository.save(modulesEntity);
            }
        }
    }

    /**
     * Loading the labels from labels.json
     * In labels.json sNo will be Unique
     */
//    @SneakyThrows
//    private void loadDefaultLabels() {
//        ObjectMapper objectMapper = new ObjectMapper();
//        ClassPathResource labelsFile = new ClassPathResource("jsonScripts/labels.json");
//        List<Map<String, String>> labelDetails = objectMapper.readValue(labelsFile.getFile(), List.class);
//        List<Integer> existingLabels = masterLabelsRepository.getLebelIds();
//        for (Map<String, String> label : labelDetails) {
//            if (!existingLabels.contains(Integer.parseInt(label.get("sNo")))) {
//                MasterLabelsEntity masterLabels = new MasterLabelsEntity();
//                masterLabels.setSNo(Integer.parseInt(label.get("sNo")));
//                masterLabels.setName(label.get("name"));
//                masterLabels.setValue(label.get("value"));
//                masterLabels.setLabel(label.get("label"));
//                masterLabelsRepository.save(masterLabels);
//            }
//        }
//    }

    /**
     * Loading the permissions from permissions.json which are going to be assigned for respective Designations
     * In permissions.json moduleName and permissionOrderwill be same for respective modules and name will be Unique for every permission
     * If any permissions are added after doing setup those will be assigned for all existing Designations directly
     */
    @SneakyThrows
    private void loadDefaultPermissions() {
        ObjectMapper objectMapper = new ObjectMapper();
        ClassPathResource permissionsFile = new ClassPathResource("jsonScripts/permissions.json");
        List<Map<String, String>> permissionsArrays = objectMapper.readValue(permissionsFile.getFile(), List.class);
        List<String> existingPermissions = permissionsRepository.getPermissionNames();
        for (Map<String, String> permission : permissionsArrays) {
            if (!existingPermissions.contains(permission.get("name"))) {
                PermissionsEntity permissionsEntity = new PermissionsEntity();
                permissionsEntity.setDescription(permission.get("description"));
                permissionsEntity.setEnabled(Boolean.parseBoolean(permission.get("isEnabled")));
                permissionsEntity.setModule(permission.get("module"));
                permissionsEntity.setModuleName(permission.get("moduleName"));
                permissionsEntity.setName(permission.get("name"));
                permissionsEntity.setPermissionOrder(Integer.parseInt(permission.get("permissionOrder")));
                permissionsEntity.setSerialNo(Integer.parseInt(permission.get("serialNo")));
                permissionsEntity.setTitle(permission.get("title"));
                permissionsEntity.setType(Integer.parseInt(permission.get("type")));
                permissionsRepository.save(permissionsEntity);

                List<Integer> existingDesignations = designationRepository.getDesignationId();
                for (Integer desinationId : existingDesignations) {
                    DesignationPermissionEntity designationPermissionEntity = new DesignationPermissionEntity();
                    designationPermissionEntity.setAssigned(true);
                    designationPermissionEntity.setDescription(permissionsEntity.getDescription());
                    designationPermissionEntity.setDesignationId(desinationId);
                    designationPermissionEntity.setEnabled(permissionsEntity.isEnabled());
                    designationPermissionEntity.setModule(permissionsEntity.getModule());
                    designationPermissionEntity.setModuleName(permissionsEntity.getModuleName());
                    designationPermissionEntity.setPermissionOrder(permissionsEntity.getPermissionOrder());
                    designationPermissionEntity.setPermissions(permissionsEntity.getName());
                    designationPermissionEntity.setTitle(permissionsEntity.getTitle());
                    designationPermissionEntity.setType(permissionsEntity.getType());
                    designationPermissionRepository.save(designationPermissionEntity);
                }
            }
        }
    }

    /**
     * Loading the sequences if they are not exists in DB
     * For Designation Sequence only we are setting the start value of sequence as 0
     * For remaining Sequences the start value will be 1
     */
    private void loadSequences() {
        Set<SequenceNamesUtil.Sequences> sequences = EnumSet.allOf(SequenceNamesUtil.Sequences.class);
        for (SequenceNamesUtil.Sequences sequence : sequences) {
            int start = 1;
            if (sequence.equals(SequenceNamesUtil.Sequences.MASTER_DESIGNATION_SEQUENCE)) {
                start = 0;
            }
            String sql = "CREATE SEQUENCE IF NOT EXISTS " + sequence +
                    " INCREMENT 1 START " + start + " MINVALUE 0 MAXVALUE 9999999999 CACHE 1";
            entityManager.createNativeQuery(sql).executeUpdate();
        }
    }

    /**
     * Loading Employee Categories from employeeCategory.json which are going to be assigned for an employee in masters
     * In employeeCategory.json categoryId will be Unique
     */
//    @SneakyThrows
//    private void loadEmployeeCategories() {
//        ObjectMapper objectMapper = new ObjectMapper();
//        ClassPathResource employeeCategoryJson = new ClassPathResource("jsonScripts/employeeCategory.json");
//        List<Map<String, String>> empCategory = objectMapper.readValue(employeeCategoryJson.getFile(), List.class);
//        List<Integer> existingCategories = employeeCategoryRepository.getCategoryIdList();
//        for (Map<String, String> labels : empCategory) {
//            if (!existingCategories.contains(Integer.parseInt(labels.get("categoryId")))) {
//                EmployeeCategory employeeCategory = new EmployeeCategory();
//                employeeCategory.setCategoryId(Integer.parseInt(labels.get("categoryId")));
//                employeeCategory.setCategoryName(labels.get("categoryName"));
//                employeeCategoryRepository.save(employeeCategory);
//            }
//        }
//    }

    /**
     * We are going to load 2 default designations
     * i.e., 0 --> Super Admin and 1 --> Citizen
     * For Super Admin we are going to assign all the permissions
     */
    private void loadDefaultDesignations() {
        Set<SetupConstants.Designations> designations = EnumSet.allOf(SetupConstants.Designations.class);
        List<String> existingDesignations = designationRepository.getDesignationName();
        for (SetupConstants.Designations designation : designations) {
            //Checking the default designation exist or not
            //If not exists then we are saving default designation and assigning permissions
            if (!existingDesignations.contains(designation.getValue())) {
                DesignationsEntity designationsEntity = new DesignationsEntity();
                designationsEntity.setId(sequenceService.getCurrentValueOfSequence(SequenceNamesUtil.Sequences.MASTER_DESIGNATION_SEQUENCE.getValue()));
                designationsEntity.setAbbreviation(designation.name());
                designationsEntity.setName(designation.getValue());
                designationRepository.save(designationsEntity);
                designationService.saveDesignationPermission(designationsEntity.getId());
            }
        }
    }



}
