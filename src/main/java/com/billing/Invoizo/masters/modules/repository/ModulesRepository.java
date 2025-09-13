package com.billing.Invoizo.masters.modules.repository;

import com.billing.Invoizo.masters.modules.entity.ModulesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ModulesRepository extends JpaRepository<ModulesEntity, Integer> {

    @Query("SELECT id FROM ModulesEntity")
    List<Integer> getModuleIds();

    @Query("SELECT moduleName FROM ModulesEntity WHERE isDisabled=:isDisabled")
    List<String> getEnabledModules(boolean isDisabled);
}
