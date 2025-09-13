package com.billing.Invoizo.masters.designations.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.billing.Invoizo.masters.designation_permission.entity.DesignationPermissionEntity;
import com.billing.Invoizo.masters.designation_permission.repository.DesignationPermissionRepository;
import com.billing.Invoizo.masters.designations.dto.DesignationDTO;
import com.billing.Invoizo.masters.designations.dto.DesignationViewDTO;
import com.billing.Invoizo.masters.designations.entity.DesignationsEntity;
import com.billing.Invoizo.masters.designations.entity.PermissionsEntity;
import com.billing.Invoizo.masters.designations.repository.DesignationRepository;
import com.billing.Invoizo.masters.designations.repository.PermissionsRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.billing.Invoizo.constants.Constants.SUPERADMIN;
import static com.billing.Invoizo.constants.EnumConstants.Designations.ADMIN;

@Service
public class DesignationServiceImpl implements DesignationService {

    private final DesignationRepository designationRepository;
    private final DesignationPermissionRepository designationPermissionRepository;
    private final EntityManager entityManager;
    private final PermissionsRepository permissionsRepository;

    @Autowired
    public DesignationServiceImpl(DesignationRepository designationRepository, DesignationPermissionRepository designationPermissionRepository,
                                  EntityManager entityManager, PermissionsRepository permissionsRepository) {
        this.designationRepository = designationRepository;
        this.designationPermissionRepository = designationPermissionRepository;
        this.entityManager = entityManager;
        this.permissionsRepository = permissionsRepository;
    }


    @Override
    @Transactional
    public List<DesignationDTO> getAlldesignations(Integer hierarchy, Integer designationId) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<DesignationDTO> cq = cb.createQuery(DesignationDTO.class);
        Root<DesignationsEntity> root = cq.from(DesignationsEntity.class);

        // 🔹 Prepare dynamic predicate list
        List<Predicate> predicates = new ArrayList<>();

        if (hierarchy != null) {
            predicates.add(cb.equal(root.get("hierarchy"), hierarchy));
        }

        if (designationId != null) {
            predicates.add(cb.equal(root.get("id"), designationId));
        }
        predicates.add(cb.notEqual(root.get("id"), SUPERADMIN));
        predicates.add(cb.notEqual(root.get("id"), ADMIN));
        cq.select(cb.construct(
                DesignationDTO.class,
                root.get("id"),
                root.get("name"),
                root.get("abbreviation"),
                root.get("hierarchy"),
                root.get("designationOrder")));
        cq.where(cb.and(predicates.toArray(new Predicate[0])));
        return entityManager.createQuery(cq).getResultList();
    }


    /**
     * Save a new Designation along with its permissions.
     *
     * @param designationDTO An object containing the designation information and associated permissions.
     * @return An integer code indicating the result:
     * 0: Designation saved successfully with permissions.
     * 1: Designation with the same name and abbreviation already exists.
     * 2: No permissions assigned for the designation.
     */
    @Override
    @Transactional
    public void designationSave() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        ClassPathResource setupDesignationsFile = new ClassPathResource("jsonScripts/setupdesignations.json");
        List<DesignationDTO> setupDesignationsDTOList = objectMapper.readValue(
                setupDesignationsFile.getFile(),
                new TypeReference<List<DesignationDTO>>() {
                }
        );
        List<DesignationsEntity> setupDesignationsEntities = new ArrayList<>();
        for (DesignationDTO designationDTO1 : setupDesignationsDTOList) {
            boolean exists = designationRepository.getSingleId(designationDTO1.getId()).isPresent();
            if (exists) {
                continue;
            }
            DesignationsEntity entity = new DesignationsEntity();
            BeanUtils.copyProperties(designationDTO1, entity);
            if (designationDTO1.getName() != null) {
                entity.setName(designationDTO1.getName().toUpperCase());
            }
            if (designationDTO1.getAbbreviation() != null) {
                entity.setAbbreviation(designationDTO1.getAbbreviation().toUpperCase());
            }
            setupDesignationsEntities.add(entity);
        }
        designationRepository.saveAll(setupDesignationsEntities);
        for (DesignationsEntity designationsEntity : setupDesignationsEntities) {
            saveDesignationPermission(designationsEntity.getId());
        }

    }

    @Override
    @Transactional
    public DesignationViewDTO getAllPermissionsRelatedToDesignation(int designationId) {
        // Retrieve designation information and permissions.
        Map<String, Object> designations = designationRepository.getDesignationInfo(designationId);
        List<Map<String, Object>> designationPermissionEntityList = designationPermissionRepository.getDesignationPermissions(designationId);

        // Separate permissions into mobile and cloud categories.
        List<Map<String, Object>> mobilePermissions = filterPermissionsByType(designationPermissionEntityList, 0);
        List<Map<String, Object>> cloudPermissions = filterPermissionsByType(designationPermissionEntityList, 1);

        // Populate the DesignationDTO with permissions and designations.
        DesignationViewDTO designationViewDTO = new DesignationViewDTO();
        designationViewDTO.setMobilePermissions(mobilePermissions);
        designationViewDTO.setCloudPermissions(cloudPermissions);
        designationViewDTO.setDesignations(designations);

        return designationViewDTO;
    }

    private List<Map<String, Object>> filterPermissionsByType(List<Map<String, Object>> permissions, int type) {
        return permissions.stream()
                .filter(p -> p.get("type").equals(type))
                .collect(Collectors.toList());
    }




    @Transactional
    @Override
    public void saveDesignationPermission(int designationId) {
        List<PermissionsEntity> permissions = permissionsRepository.findAll();
        List<DesignationPermissionEntity> designationPermissionEntityList = new ArrayList<>();
        for (PermissionsEntity permissionsEntity : permissions) {
            DesignationPermissionEntity designationPermissionEntity = new DesignationPermissionEntity();
            designationPermissionEntity.setAssigned(true);
            designationPermissionEntity.setDescription(permissionsEntity.getDescription());
            designationPermissionEntity.setDesignationId(designationId);
            designationPermissionEntity.setEnabled(permissionsEntity.isEnabled());
            designationPermissionEntity.setModule(permissionsEntity.getModule());
            designationPermissionEntity.setModuleName(permissionsEntity.getModuleName());
            designationPermissionEntity.setPermissionOrder(permissionsEntity.getPermissionOrder());
            designationPermissionEntity.setPermissions(permissionsEntity.getName());
            designationPermissionEntity.setTitle(permissionsEntity.getTitle());
            designationPermissionEntity.setType(permissionsEntity.getType());
            designationPermissionEntityList.add(designationPermissionEntity);
        }
        designationPermissionRepository.saveAll(designationPermissionEntityList);
    }
}
