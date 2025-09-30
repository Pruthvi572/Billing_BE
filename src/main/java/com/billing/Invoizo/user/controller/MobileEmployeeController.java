package com.billing.Invoizo.user.controller;


import com.billing.Invoizo.user.dto.EmployeeDTO;
import com.billing.Invoizo.user.dto.EmployeeViewDTO;
import com.billing.Invoizo.user.service.EmployeeService;
import com.billing.Invoizo.util.WebResponseEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.billing.Invoizo.constants.EndpointConstants.MOBILE_EMPLOYEE_VIEW;
import static com.billing.Invoizo.constants.EndpointConstants.MOBILE_MASTER_EMPLOYEES_API;

@RestController("mobileEmployeeController")
@Slf4j
public class MobileEmployeeController {


    private final EmployeeService employeeService;

    @Autowired
    public MobileEmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }


    @PostMapping(MOBILE_MASTER_EMPLOYEES_API)
    public ResponseEntity<WebResponseEntity<String>> saveMember(
            @RequestBody EmployeeDTO employeeDTO) {
        log.info("Saving Employee Details :");
        try {
            Integer result = null;
            result = employeeService.saveEmployee(employeeDTO, null, null, null);
            if (result != null) {
                if (result == 0) {
                    return new ResponseEntity<>(
                            new WebResponseEntity<>(1, true,
                                    "Employee created Successfully"),
                            HttpStatus.CREATED);
                } else if (result == 1) {
                    return new ResponseEntity<>(new WebResponseEntity<>(5,
                            "Employees Limit reached"),
                            HttpStatus.OK);
                } else if (result == 2) {
                    return new ResponseEntity<>(new WebResponseEntity<>(5, "Employee already exists"),
                            HttpStatus.OK);
                }
            } else {
                return new ResponseEntity<>(
                        new WebResponseEntity<>(5, "Null value in Request"),
                        HttpStatus.OK);
            }
        } catch (Exception e) {
            log.error("Error While Saving Employee Details :", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return null;
    }

    @GetMapping(MOBILE_EMPLOYEE_VIEW)
    public ResponseEntity<WebResponseEntity<List<EmployeeViewDTO>>> getMobileEmployeeView(@RequestParam String employeeId) {
        log.info("Getting Mobile Employee View ::");
        try {
            WebResponseEntity<List<EmployeeViewDTO>> response = new WebResponseEntity<>(0, true,
                    employeeService.getEmployeeView(employeeId));
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error While Getting Mobile Employee View ::", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
