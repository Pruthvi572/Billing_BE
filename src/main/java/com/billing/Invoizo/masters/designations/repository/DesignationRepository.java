package com.billing.Invoizo.masters.designations.repository;

import com.billing.Invoizo.masters.designations.entity.DesignationsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public interface DesignationRepository extends JpaRepository<DesignationsEntity, String> {
    @Query("SELECT name FROM DesignationsEntity")
    List<String> getDesignationName();

    @Query("SELECT id FROM DesignationsEntity")
    List<Integer> getDesignationId();

    @Query("SELECT COUNT(d) FROM DesignationsEntity d WHERE d.name = :name AND d.abbreviation = :abbreviation")
    long getDesignationCount(@Param("name") String name, @Param("abbreviation") String abbreviation);

    @Query("SELECT new map(d.id AS id, d.name AS designationName, d.abbreviation AS designationAbbreviation) " +
            "FROM DesignationsEntity d WHERE d.id = :designationId")
    Map<String, Object> getDesignationInfo(@Param("designationId") int designationId);

    @Query("SELECT COUNT(d) FROM DesignationsEntity d " +
            "WHERE (:type = 1 AND LOWER(d.name) = LOWER(:name)) " +
            "OR (:type != 1 AND LOWER(d.abbreviation) = LOWER(:name))")
    Long designationCheck(@Param("name") String name, @Param("type") int type);

    @Query("SELECT id FROM DesignationsEntity WHERE id = :designationId")
    Optional<Integer> getSingleId(@Param("designationId") int designationId);
}
