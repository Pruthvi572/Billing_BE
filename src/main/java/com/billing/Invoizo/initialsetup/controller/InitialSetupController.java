package com.billing.Invoizo.initialsetup.controller;


import com.billing.Invoizo.InvoizoProperties;
import com.billing.Invoizo.initialsetup.dto.InitialSetupDTO;
import com.billing.Invoizo.initialsetup.dto.LicenseCheckDTO;
import com.billing.Invoizo.initialsetup.dto.SetupFlagDTO;
import com.billing.Invoizo.initialsetup.service.InitialSetupService;
import com.billing.Invoizo.masters.designations.entity.DesignationsEntity;
import com.billing.Invoizo.masters.designations.repository.DesignationRepository;
import com.billing.Invoizo.masters.modules.entity.ModulesEntity;
import com.billing.Invoizo.masters.ulb.repository.ULBRepository;
import com.billing.Invoizo.util.ImageHandler;
import com.billing.Invoizo.util.WebResponseEntity;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("initial/setup")
public class InitialSetupController {
    private final ULBRepository ulbRepository;

    private final InitialSetupService initialSetupService;

    private final DesignationRepository designationRepository;

    private final ImageHandler imageHandler;

    private final InvoizoProperties invoizoProperties;

    private final HttpServletRequest httpServletRequest;


    @Autowired
    public InitialSetupController(ULBRepository ulbRepository, InitialSetupService initialSetupService,
                                  DesignationRepository designationRepository, ImageHandler imageHandler,
                                  InvoizoProperties invoizoProperties, HttpServletRequest httpServletRequest) {
        this.ulbRepository = ulbRepository;
        this.initialSetupService = initialSetupService;
        this.designationRepository = designationRepository;
        this.imageHandler = imageHandler;
        this.invoizoProperties = invoizoProperties;
        this.httpServletRequest = httpServletRequest;
    }

    /**
     * used on startup to determine if the user should
     * be directed to the login page or the setup flow.
     */
    @GetMapping(value = {"/setupflag"})
    public ResponseEntity<SetupFlagDTO> setupFlag() {
        try {
            boolean isSetupCompleted = false;
            if (ulbRepository.count() > 0) {
                isSetupCompleted = true;
            }
            SetupFlagDTO setupFlagDTO = new SetupFlagDTO(isSetupCompleted);
            return new ResponseEntity<>(setupFlagDTO, HttpStatus.OK);

        } catch (Exception e) {
            log.error("error validation setupflag ::", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "check/license")
    public ResponseEntity<LicenseCheckDTO> checkLicenseKey(@RequestParam(name = "licenseId") String licenseId,
                                                           @RequestParam(name = "subClientUsername") String subClientUsername) {
        log.info("Checking License Key Valid or Not ::");
        try {
            LicenseCheckDTO licenseCheckDTO = new LicenseCheckDTO(initialSetupService.checkLicenseKey(licenseId, subClientUsername));
            return new ResponseEntity<>(licenseCheckDTO, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error While Checking License Key Valid or Not ::", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "designations")
    public ResponseEntity<List<DesignationsEntity>> designationList() {
        log.info("Getting Designation List ::");
        try {
            List<DesignationsEntity> designationEntityList = designationRepository.findAll();
            return new ResponseEntity<>(designationEntityList, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error Getting DesignationList ::", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = "save", consumes = "application/json")
    public ResponseEntity<WebResponseEntity<String>> saveAllDetails(
            @RequestBody InitialSetupDTO initialSetupDTO) {
        log.info("Saving Details");

        try {
            initialSetupService.saveAllDetails(initialSetupDTO);
            WebResponseEntity<String> response = new WebResponseEntity<>(1, true, "Created Successfully");
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            log.error("Error in Saving Details", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    /**
     * Gets the running URL.
     * This API is used to get the URL of project.
     *
     * @return the running URL
     */
    @PostMapping(value = "url")
    public ResponseEntity<String> getRunningURL() {
        log.info("Starting Services..");
        try {
            ServletRequest sr = httpServletRequest;
            String domainName = sr.getScheme() + "://" + sr.getServerName();
            if (sr.getServerPort() != 0) {
                domainName += ":" + sr.getServerPort();
            }
            domainName += httpServletRequest.getContextPath();
            return new ResponseEntity<>(domainName, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error in Importing Running URL {}", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Gets the list of existing data.
     * This API is used to get list of few existing data which are used to do the setup
     *
     * @return the list of existing data
     */
    @GetMapping(value = "listofexistingdata")
    public ResponseEntity<WebResponseEntity> getListOfExistingData() {
        log.info("List of Existing Data");

        try {
            List<ModulesEntity> mapResponse = initialSetupService.getListOfExistingData();
            WebResponseEntity<List<ModulesEntity>> response = new WebResponseEntity<>(true, mapResponse);
            return new ResponseEntity<WebResponseEntity>(response, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error in Getting List Of Existing Data", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Import ulb.
     * This API is used to upload list of ULBs from excel.
     *
     * @param excelFile the excel file
     * @return the response entity
     */
//    @PostMapping(value = "import/ulb")
//    public ResponseEntity<JSONObject> importUlb(@RequestPart(name = "excelFile") MultipartFile excelFile) {
//        log.info("Finance Import Excel");
//        try {
//            JSONObject response = initialSetupService.importUlb(excelFile);
//            return new ResponseEntity<>(response, HttpStatus.OK);
//        } catch (Exception e) {
//            log.error("Error in Importing ULB Details ", e);
//            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }

    /**
     * Check for master data.
     * This API is used to check whether all master data is entered or anything is missed.
     * It will return list of unentered master data.
     *
     * @return the response entity
     */
    @GetMapping(value = "check/masterData")
    public ResponseEntity<JSONObject> checkForMasterData() {
        log.info("Checking Master Data Availability :");
        try {
            return new ResponseEntity<>(initialSetupService.checkForMasterData(), HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error in Checking Master Data Availability :",
                    e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
