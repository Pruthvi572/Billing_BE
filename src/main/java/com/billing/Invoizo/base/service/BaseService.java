package com.billing.Invoizo.base.service;

import jakarta.persistence.Query;

public interface BaseService {

    <T> T paginationResponse(Integer offset, Integer pageSize, Query criteria, Query countCriteria);

}
