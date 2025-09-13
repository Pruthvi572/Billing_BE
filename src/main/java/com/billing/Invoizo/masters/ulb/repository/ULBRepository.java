package com.billing.Invoizo.masters.ulb.repository;

import com.billing.Invoizo.masters.ulb.entity.ULBEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ULBRepository extends JpaRepository<ULBEntity, String> {
}
