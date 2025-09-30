package com.billing.Invoizo.user.controller;

import com.billing.Invoizo.user.dto.EmployeeDTO;
import com.billing.Invoizo.user.dto.EmployeeListDTO;
import com.billing.Invoizo.user.dto.EmployeeViewDTO;
import com.billing.Invoizo.user.repository.EmployeeRepository;
import com.billing.Invoizo.user.service.EmployeeService;
import com.billing.Invoizo.util.PaginationResponse;
import com.billing.Invoizo.util.WebResponseEntity;
import com.billing.Invoizo.util.dto.UniqueCheckResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.billing.Invoizo.constants.EndpointConstants.*;

@Slf4j
@RestController("employeeController")
public class EmployeeController {


    private final EmployeeService employeeService;
    private final EmployeeRepository employeeRepository;

    @Autowired
    public EmployeeController(EmployeeService employeeService, EmployeeRepository employeeRepository) {
        this.employeeService = employeeService;
        this.employeeRepository = employeeRepository;
    }

    @GetMapping(EMPLOYEE_UNIQUECHECK)
    public ResponseEntity<WebResponseEntity<UniqueCheckResponseDTO>> employeeUniqueCheck(
            @RequestParam(name = "employeeId") String employeeId) {
        log.info("Unique Check Of EmployeeId ::");
        try {
            Long employeeCount = employeeRepository.isEmployeeExist(employeeId);
            UniqueCheckResponseDTO checkResponseDTO = new UniqueCheckResponseDTO(employeeCount != null && employeeCount > 0);
            WebResponseEntity<UniqueCheckResponseDTO> response = new WebResponseEntity<>(0, true,
                    checkResponseDTO);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error While Unique Check Of EmployeeId ::", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PostMapping(USER_EMPLOYEES_API)
    public ResponseEntity<WebResponseEntity<String>> saveEmployee(
            @RequestBody EmployeeDTO employeeDTO

    ) {
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

    @GetMapping(USER_EMPLOYEES_API)
    public ResponseEntity<WebResponseEntity<PaginationResponse<EmployeeListDTO>>> employeesList(
            @RequestParam(required = false, name = "pageNumber") Integer pageNumber,
            @RequestParam(required = false, name = "pageSize") Integer pageSize,
            @RequestParam(required = false, name = "searchValue") String searchValue,
            @RequestParam(required = false, name = "areaValue") Integer areaValue) {
        log.info("Getting Employees List ::");
        try {
            PaginationResponse<EmployeeListDTO> paginatedData =
                    employeeService.getAllEmployees(pageNumber, pageSize, searchValue, areaValue);
            WebResponseEntity<PaginationResponse<EmployeeListDTO>> response =
                    new WebResponseEntity<>(0, true, paginatedData);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error While Getting Employees List ::", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(EMPLOYEE_VIEW)
    public ResponseEntity<WebResponseEntity<List<EmployeeViewDTO>>> getEmployeeView(@RequestParam String employeeId) {
        log.info("Getting Employee View ::");
        try {
            WebResponseEntity<List<EmployeeViewDTO>> response = new WebResponseEntity<>(0, true,
                    employeeService.getEmployeeView(employeeId));
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error While Getting Employee View ::", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
