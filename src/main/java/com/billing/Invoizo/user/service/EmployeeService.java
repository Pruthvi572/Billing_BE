package com.billing.Invoizo.user.service;


import com.billing.Invoizo.user.dto.EmployeeBankDetailsDTO;
import com.billing.Invoizo.user.dto.EmployeeDTO;
import com.billing.Invoizo.user.dto.EmployeeViewDTO;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public interface EmployeeService {
    void saveLoginHistory(String username);

    void handleWrongPasswordAttempt(String username);

    Integer saveEmployee(EmployeeDTO employeeDTO, String employeeDesignationId, String districtId, Integer designationId);

    <T> T getAllEmployees(Integer pageNumber, Integer pageSize, String searchValue, Integer areaValue);

    EmployeeViewDTO getEmployeeView(String employeeId);


}
