package com.billing.Invoizo.masters.businessProfile.service;

import com.billing.Invoizo.masters.businessProfile.dto.BusinessProfileDTO;

public interface BusinessProfileService {

    void saveBusinessDetails(BusinessProfileDTO businessProfileDTO);

    <T> T getList(Integer pageNumber, Integer pageSize, String searchValue);
}
