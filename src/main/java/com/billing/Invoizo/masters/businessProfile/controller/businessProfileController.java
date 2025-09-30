package com.billing.Invoizo.masters.businessProfile.controller;


import com.billing.Invoizo.masters.businessProfile.dto.BusinessProfileDTO;
import com.billing.Invoizo.masters.businessProfile.repository.BusinessProfileRepository;
import com.billing.Invoizo.masters.businessProfile.service.BusinessProfileService;
import com.billing.Invoizo.util.WebResponseEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.billing.Invoizo.constants.EndpointConstants.BUSINESS_DETAILS_API;

@RestController
@Slf4j
public class businessProfileController {


    private final BusinessProfileService businessProfileService;
    private final BusinessProfileRepository businessProfileRepository;


    @Autowired
    public businessProfileController(BusinessProfileService businessProfileService, BusinessProfileRepository businessProfileRepository) {
        this.businessProfileService = businessProfileService;
        this.businessProfileRepository = businessProfileRepository;
    }


    @PostMapping(BUSINESS_DETAILS_API)
    public ResponseEntity<WebResponseEntity<String>> saveBusiness(@RequestBody BusinessProfileDTO businessProfileDTO,
                                                                  @RequestHeader(name = "employeeId") String employeeId) {
        log.info("Saving Business Profile Details");
        try {
            businessProfileService.saveBusinessDetails(businessProfileDTO);
            return new ResponseEntity<>(
                    new WebResponseEntity<>(1, true, "Created Successfully."), HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error While updating EMI Payment flag : ", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }

    @GetMapping(BUSINESS_DETAILS_API)
    public <T> ResponseEntity<WebResponseEntity<T>> getLoanList(
            @RequestParam(required = false) Integer pageNumber,
            @RequestParam(required = false) Integer pageSize,
            @RequestParam(required = false) String searchValue) {
        log.info("Fetching Business Profile List Details");
        try {

            T loanList = businessProfileService.getList(pageNumber, pageSize, searchValue);
            return new ResponseEntity<>(new WebResponseEntity<>(true, loanList), HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error in Fetching Business Profile List Details", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }


}
