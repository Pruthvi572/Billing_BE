package com.billing.Invoizo.masters.businessProfile.service;

import com.billing.Invoizo.base.service.BaseServiceImpl;
import com.billing.Invoizo.masters.businessProfile.dto.BusinessProfileDTO;
import com.billing.Invoizo.masters.businessProfile.entity.BusinessProfileEntity;
import com.billing.Invoizo.masters.businessProfile.repository.BusinessProfileRepository;
import com.billing.Invoizo.util.StringUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class BusinessProfileServiceImpl extends BaseServiceImpl implements BusinessProfileService {


    private final BusinessProfileRepository businessProfileRepository;
    private final EntityManager entityManager;

    @Autowired
    public BusinessProfileServiceImpl(BusinessProfileRepository businessProfileRepository, EntityManager entityManager) {
        this.businessProfileRepository = businessProfileRepository;
        this.entityManager = entityManager;
    }


    @Override
    public void saveBusinessDetails(BusinessProfileDTO businessProfileDTO) {
        BusinessProfileEntity businessProfileEntity = new BusinessProfileEntity();
        if (businessProfileDTO.getId().isEmpty()) {
            Optional<BusinessProfileEntity> optional = businessProfileRepository.findById(businessProfileDTO.getId());
            if (optional.isPresent()) {
                businessProfileEntity.setBusinessName(businessProfileDTO.getBusinessName());
                businessProfileEntity.setOwnerName(businessProfileDTO.getOwnerName());
                businessProfileEntity.setEmail(businessProfileDTO.getEmail());
                businessProfileEntity.setPhoneNumber(businessProfileDTO.getPhoneNumber());
                businessProfileEntity.setAlternatePhone(businessProfileDTO.getAlternatePhone());
                businessProfileEntity.setAddress(businessProfileDTO.getAddress());
                businessProfileEntity.setCity(businessProfileDTO.getCity());
                businessProfileEntity.setState(businessProfileDTO.getState());
                businessProfileEntity.setPinCode(businessProfileDTO.getPinCode());
                businessProfileEntity.setGstNumber(businessProfileDTO.getGstNumber());
                businessProfileEntity.setPanNumber(businessProfileDTO.getPanNumber());
                businessProfileEntity.setBankName(businessProfileDTO.getBankName());
                businessProfileEntity.setAccountNumber(businessProfileDTO.getAccountNumber());
                businessProfileEntity.setIfscCode(businessProfileDTO.getIfscCode());
                businessProfileEntity.setBranchName(businessProfileDTO.getBranchName());
            }
        } else {
            businessProfileEntity.setBusinessName(businessProfileDTO.getBusinessName());
            businessProfileEntity.setOwnerName(businessProfileDTO.getOwnerName());
            businessProfileEntity.setEmail(businessProfileDTO.getEmail());
            businessProfileEntity.setPhoneNumber(businessProfileDTO.getPhoneNumber());
            businessProfileEntity.setAlternatePhone(businessProfileDTO.getAlternatePhone());
            businessProfileEntity.setAddress(businessProfileDTO.getAddress());
            businessProfileEntity.setCity(businessProfileDTO.getCity());
            businessProfileEntity.setState(businessProfileDTO.getState());
            businessProfileEntity.setPinCode(businessProfileDTO.getPinCode());
            businessProfileEntity.setGstNumber(businessProfileDTO.getGstNumber());
            businessProfileEntity.setPanNumber(businessProfileDTO.getPanNumber());
            businessProfileEntity.setBankName(businessProfileDTO.getBankName());
            businessProfileEntity.setAccountNumber(businessProfileDTO.getAccountNumber());
            businessProfileEntity.setIfscCode(businessProfileDTO.getIfscCode());
            businessProfileEntity.setBranchName(businessProfileDTO.getBranchName());
        }
    }

    @Override
    public <T> T getList(Integer pageNumber, Integer pageSize, String searchValue) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<BusinessProfileDTO> projectionQuery = criteriaBuilder.createQuery(BusinessProfileDTO.class);
        Root<BusinessProfileEntity> root = projectionQuery.from(BusinessProfileEntity.class);

        CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
        Root<BusinessProfileEntity> countRoot = countQuery.from(BusinessProfileEntity.class);

        List<Predicate> predicates = new ArrayList<>();
        List<Predicate> countPredicates = new ArrayList<>();


        if (!StringUtil.isNullOrEmpty(searchValue)) {
            searchValue = searchValue.toLowerCase();
            predicates.add(criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), '%' + searchValue + '%'),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("ownerName")), '%' + searchValue + '%')
            ));
            countPredicates.add(criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(countRoot.get("name")), '%' + searchValue + '%'),
                    criteriaBuilder.like(criteriaBuilder.lower(countRoot.get("applicantId")), '%' + searchValue + '%')
                    ));
        }
        projectionQuery.select(criteriaBuilder.construct(BusinessProfileDTO.class,
                        root.get("id"),
                        root.get("name"),
                        root.get("businessName"),
                        root.get("loanPurpose"),
                        root.get("shgCode"),
                        root.get("shgName"),
                        root.get("mobileNumber"),
                        root.get("amount"),
                        root.get("isDefaulter"),
                        root.get("interestRate"),
                        root.get("tenure"),
                        root.get("status"),
                        root.get("isKYCDone").alias("isKYCDone"),
                        root.get("isFieldVerificationDone").alias("isFieldVerificationDone"),
                        root.get("isRejected"),
                        root.get("stateName"),
                        root.get("districtName"),
                        root.get("blockName"),
                        root.get("panchayatName"),
                        root.get("villageName"),
                        root.get("createdAt"),
                        root.get("paymentStatus"),
                        root.get("clfName"),
                        root.get("voName")))
                .where(predicates.toArray(new Predicate[]{}))
                .orderBy(criteriaBuilder.desc(root.get("modifiedAt")));

        Query dataQuery = entityManager.createQuery(projectionQuery);
        Query count = entityManager.createQuery(countQuery.select(criteriaBuilder.count(countRoot))
                .where(countPredicates.toArray(new Predicate[]{})));

        return paginationResponse(pageNumber, pageSize, dataQuery, count);
    }
}
