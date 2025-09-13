package com.billing.Invoizo.base.service;

import com.billing.Invoizo.util.PaginationResponse;
import jakarta.persistence.Query;
import org.springframework.transaction.annotation.Transactional;

public class BaseServiceImpl implements BaseService {

    @Override
    @Transactional
    public <T> T paginationResponse(Integer pageNumber, Integer pageSize, Query criteria, Query countCriteria) {
        PaginationResponse paginationResponse = new PaginationResponse();
        if (pageSize != null && pageNumber != null) {
            criteria.setMaxResults(pageSize);
            criteria.setFirstResult((pageNumber) * pageSize);
            paginationResponse.setTotalRecords((Long) countCriteria.getSingleResult());
        }
        paginationResponse.setData(criteria.getResultList());
        return (T) paginationResponse;
    }

}
