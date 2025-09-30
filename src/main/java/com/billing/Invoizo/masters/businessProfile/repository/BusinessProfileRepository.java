package com.billing.Invoizo.masters.businessProfile.repository;

import com.billing.Invoizo.masters.businessProfile.entity.BusinessProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BusinessProfileRepository extends JpaRepository<BusinessProfileEntity, String> {
}
