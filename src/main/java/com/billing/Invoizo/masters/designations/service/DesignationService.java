package com.billing.Invoizo.masters.designations.service;

import com.billing.Invoizo.masters.designations.dto.DesignationDTO;
import com.billing.Invoizo.masters.designations.dto.DesignationViewDTO;

import java.io.IOException;
import java.util.List;

public interface DesignationService {

    void designationSave() throws IOException;

    List<DesignationDTO> getAlldesignations(Integer hierarchy, Integer designationId);

    DesignationViewDTO getAllPermissionsRelatedToDesignation(int designationId);

    void saveDesignationPermission(int designationId);

}
