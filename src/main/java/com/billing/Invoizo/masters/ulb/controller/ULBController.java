package com.billing.Invoizo.masters.ulb.controller;

import com.billing.Invoizo.masters.ulb.entity.ULBEntity;
import com.billing.Invoizo.masters.ulb.service.ULBService;
import com.billing.Invoizo.util.RandomNumberGenerator;
import com.billing.Invoizo.util.WebResponseEntity;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class ULBController {


    private final RandomNumberGenerator randomNumberGenerator;
    private final ULBService ulbService;

    @Autowired
    public ULBController(RandomNumberGenerator randomNumberGenerator, ULBService ulbService) {
        this.randomNumberGenerator = randomNumberGenerator;
        this.ulbService = ulbService;
    }


//    @PostMapping()
//    public ResponseEntity<WebResponseEntity<JSONObject>> saveULB(@RequestBody ULBEntity ulbEntity,
//                                                                 @RequestHeader String employeeDesignationId,
//                                                                 @RequestHeader Integer designationId,
//                                                                 @RequestHeader String ulbCode) {
//        log.info("Saving ULB Details :");
//        try {
//            ulbEntity.setUlbCode(randomNumberGenerator.generateRandomNumber() + "");
//            ulbEntity.setCreatedBy(employeeDesignationId);
//            WebResponseEntity<JSONObject> response = new WebResponseEntity<>(1, true, "Created Successfully"
//                    , masterDataService.saveUlb(ulbEntity, designationId, ulbCode));
//            return new ResponseEntity<>(response, HttpStatus.CREATED);
//        } catch (Exception e) {
//            log.error("Error in Saving ULB Details:", e);
//            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }
}
