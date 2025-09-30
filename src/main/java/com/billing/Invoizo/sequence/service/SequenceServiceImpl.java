package com.billing.Invoizo.sequence.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SequenceServiceImpl implements SequenceService {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public Integer getCurrentValueOfSequence(String sequenceName) {
        String sql = "SELECT nextval('" + sequenceName + "')";
        Query query = entityManager.createNativeQuery(sql);
        return ((Long) query.getSingleResult()).intValue();
    }
}