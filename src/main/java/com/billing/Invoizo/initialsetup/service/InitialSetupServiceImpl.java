package com.billing.Invoizo.initialsetup.service;


import com.billing.Invoizo.InvoizoProperties;
import com.billing.Invoizo.constants.EnumConstants;
import com.billing.Invoizo.initialsetup.dto.InitialSetupDTO;
import com.billing.Invoizo.masters.designations.repository.DesignationRepository;
import com.billing.Invoizo.masters.designations.service.DesignationService;
import com.billing.Invoizo.masters.modules.entity.ModulesEntity;
import com.billing.Invoizo.masters.modules.repository.ModulesRepository;
import com.billing.Invoizo.masters.ulb.entity.ULBEntity;
import com.billing.Invoizo.masters.ulb.repository.ULBRepository;
import com.billing.Invoizo.user.entity.EmployeeEntity;
import com.billing.Invoizo.user.entity.EmployeeULBDesignationEntity;
import com.billing.Invoizo.user.repository.EmployeeRepository;
import com.billing.Invoizo.user.repository.EmployeeULBDesignationRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.*;

@Service
@Slf4j
public class InitialSetupServiceImpl implements InitialSetupService {

    private final InvoizoProperties invoizoProperties;

    private final RestTemplateBuilder restTemplateBuilder;

    private final ULBRepository ulbRepository;


    private final ModulesRepository modulesRepository;

    private final PasswordEncoder passwordEncoder;

    private final EmployeeRepository employeeRepository;

    private final EmployeeULBDesignationRepository employeeULBDesignationRepository;

//    private final UserManagementService userManagementService;

    private final EntityManager entityManager;

//    private final ValidationUtils validationUtils;

    private final DesignationRepository designationRepository;

    private final DesignationService designationService;

    @Autowired
    public InitialSetupServiceImpl(InvoizoProperties invoizoProperties, RestTemplateBuilder restTemplateBuilder,
                                   ULBRepository ulbRepository, ModulesRepository modulesRepository,
                                   PasswordEncoder passwordEncoder, EmployeeRepository employeeRepository,
                                   EmployeeULBDesignationRepository employeeULBDesignationRepository,
                                   EntityManager entityManager,
                                   DesignationRepository designationRepository, DesignationService designationService) {
        this.invoizoProperties = invoizoProperties;
        this.restTemplateBuilder = restTemplateBuilder;
        this.ulbRepository = ulbRepository;
        this.modulesRepository = modulesRepository;
        this.passwordEncoder = passwordEncoder;
        this.employeeRepository = employeeRepository;
        this.employeeULBDesignationRepository = employeeULBDesignationRepository;
//        this.userManagementService = userManagementService;
        this.entityManager = entityManager;
//        this.validationUtils = validationUtils;
        this.designationRepository = designationRepository;
        this.designationService = designationService;
    }


    @Override
    public JSONObject checkLicenseKey(String licenseId, String subClientUsername) {
        String theUrl = invoizoProperties.getSecurityCheckURL() + "/web/security/check/?licenseId=" + licenseId + "&subClientUsername="
                + subClientUsername;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<Object> entity = new HttpEntity<>(headers);
        ResponseEntity<JSONObject> response = restTemplateBuilder.build().exchange(theUrl,
                HttpMethod.POST, entity, JSONObject.class);
        return response.getBody();
    }

    @Override
    @Transactional
    public void saveAllDetails(InitialSetupDTO initialSetupDTO) throws IOException {

        List<EmployeeEntity> employeeEntities = new ArrayList<>();
        ULBEntity ulbEntity = initialSetupDTO.getUlbEntity();
        int id = 1;
        String ulb = "ULB";
        String superAdminUlbCode = null;
        List<ULBEntity> ulbEntities = initialSetupDTO.getMultiple();
        ULBEntity ulbEntity1 = new ULBEntity();
//        for (ULBEntity ulbEntity1 : ulbEntities) {
//            ulbEntity1.setLicenseId(ulbEntity.getLicenseId());
//            ulbEntity1.setSubClientId(ulbEntity.getSubClientId());
        if (ulbEntity1.getUlbCode() == null) {
            ulbEntity1.setUlbCode(ulb + id);
//                id++;
        }
        ulbRepository.saveAndFlush(ulbEntity1);

        // Adding Admins for All ULB's
        EmployeeEntity adminEmployeeEntity = new EmployeeEntity();
        adminEmployeeEntity.setName(ulbEntity1.getName() + " Admin ");
        adminEmployeeEntity.setEmployeeId(adminEmployeeEntity.getName().toLowerCase().replaceAll(" ", ""));
        adminEmployeeEntity.setDesignationId(EnumConstants.Designations.ADMIN.getValue());
        adminEmployeeEntity.setHasAgreedTerms(true);
        adminEmployeeEntity.setUlbCode(ulbEntity1.getUlbCode());
        employeeEntities.add(adminEmployeeEntity);
        superAdminUlbCode = ulbEntity1.getUlbCode();
//        }

        //Saving Designations
        designationService.designationSave();

        // Saving Modules
//        updateModules(initialSetupDTO);

        // Adding Super Admin
        EmployeeEntity superAdminemployeeEntity = new EmployeeEntity();
        superAdminemployeeEntity.setEmployeeId("superadmin");
        superAdminemployeeEntity.setName("Super Admin");
        superAdminemployeeEntity.setDesignationId(EnumConstants.Designations.SUPER_ADMIN.getValue());
        superAdminemployeeEntity.setHasAgreedTerms(true);
        superAdminemployeeEntity.setUlbCode(superAdminUlbCode);
        employeeEntities.add(superAdminemployeeEntity);

        // adding admins for multiple ulbs
//        if (initialSetupDTO.getAdmins() != null) {
//            employeeEntities.addAll(initialSetupDTO.getAdmins());
//        }
//        if (initialSetupDTO.getEmployeeEntities() != null && !initialSetupDTO.getEmployeeEntities().isEmpty()) {
//            employeeEntities.addAll(initialSetupDTO.getEmployeeEntities());
//        }

        // saving Employees
        saveEmployees(employeeEntities);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("employeeEntities", employeeEntities);
        jsonObject.put("ulbEntity", ulbEntity);

//        userManagementService.checkBefore(jsonObject);
    }

    @Override
    public List<ModulesEntity> getListOfExistingData() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<ModulesEntity> cq = cb.createQuery(ModulesEntity.class);
        Root<ModulesEntity> root = cq.from(ModulesEntity.class);
        cq.select(root).distinct(true);
        List<ModulesEntity> results = entityManager.createQuery(cq).getResultList();
        return results;
    }

    /**
     * Update modules.
     * This method is used to update Setup modules in ModulesEntity.
     *
     * @param initialSetupDTO the initial setup DTO
     */
    private void updateModules(InitialSetupDTO initialSetupDTO) {
        for (ModulesEntity moduleEntity : initialSetupDTO.getModuleEntities()) {
            modulesRepository.save(moduleEntity);
        }
    }

    /**
     * Save employees.
     * This method is used save employee details into EmployeeEntity and EmployeeULBDesignationEntity with all details
     *
     * @param employeeEntities the employee entities
     */
    private void saveEmployees(List<EmployeeEntity> employeeEntities) {
        for (EmployeeEntity employeeEntity : employeeEntities) {
            employeeEntity.setAccountNonExpired(true);
            employeeEntity.setAccountNonLocked(true);
            employeeEntity.setAccountNonExpired(true);
            employeeEntity.setCredentialsNonExpired(true);
            employeeEntity.setPassword(passwordEncoder.encode(employeeEntity.getEmployeeId()));
            employeeEntity.setCreatedAt(new Date());
            employeeEntity.setEnabled(true);
            employeeRepository.saveAndFlush(employeeEntity);

            EmployeeULBDesignationEntity designationEntity = new EmployeeULBDesignationEntity();
            designationEntity.setActiveStatus(true);
            designationEntity.setDesignationId(employeeEntity.getDesignationId());
            designationEntity.setEmployeeId(employeeEntity.getEmployeeId());
            designationEntity.setUlbCode(employeeEntity.getUlbCode());
            designationEntity.setEmployeeDesignationId(designationEntity.getEmployeeId()
                    + designationEntity.getDesignationId() + designationEntity.getUlbCode());
            designationEntity.setBaseDesignation(true);
            employeeULBDesignationRepository.saveAndFlush(designationEntity);
        }
    }

    /**
     * This method is used to read the list of ULB information which are uploaded by excel sheet.
     * This method will validate the data and return the error messages to the user.
     */
    @SuppressWarnings("unchecked")
//    @Override
//    public JSONObject importUlb(MultipartFile excelFile) {
//        JSONObject jsonObject = new JSONObject();
//        List<String> messages = new ArrayList<>();
//        Set<String> codes = new HashSet<>();
//        Map<String, String> ulbCodeAndNames = new HashMap<>();
//        try {
//            FileOutputStream fos;
//            File convFile = new File(loanManagementProperties.getExcelImportTempPath() + File.separator + excelFile.getOriginalFilename());
//            boolean isFileCreated = convFile.createNewFile();
//            if (isFileCreated) {
//                log.info("File created successfully with filename {}", convFile.getName());
//            }
//            fos = new FileOutputStream(convFile);
//            fos.write(excelFile.getBytes());
//            fos.close();
//            String fileName = excelFile.getOriginalFilename();
//            FileInputStream fileInputStream = new FileInputStream(convFile);
//            List<ULBEntity> list = new ArrayList<>();
//            Workbook workbook;
//            if (fileName.toLowerCase().endsWith("xlsm")) {
//                workbook = new XSSFWorkbook(fileInputStream);
//            } else if (fileName.toLowerCase().endsWith("xls")) {
//                workbook = new HSSFWorkbook(fileInputStream);
//            } else if (fileName.toLowerCase().endsWith("xlsx")) {
//                workbook = new XSSFWorkbook(fileInputStream);
//            } else {
//                fileInputStream.close();
//                jsonObject.put("messages", "Please check for file format");
//                return jsonObject;
//            }
//            Sheet sheet = workbook.getSheetAt(0);
//            Iterator<Row> rowIterator = sheet.iterator();
//            DataFormatter dataFormatter = new DataFormatter();
//            ULBEntity ulbEntity;
//            if (sheet.getRow(1).getCell(EnumConstants.ULB_COLUMN_INDEX.SERIALNO.getValue(), Row.MissingCellPolicy.RETURN_BLANK_AS_NULL) == null) {
//                messages.add("Details should start from Row 2 in " + sheet.getSheetName() + " sheet");
//            }
//            rowIterator.next();
//            while (rowIterator.hasNext()) {
//                Row row = rowIterator.next();
//                Cell sNoCell = row.getCell(EnumConstants.ULB_COLUMN_INDEX.SERIALNO.getValue(), Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
//                if (sNoCell == null || StringUtil.isNullOrEmpty(dataFormatter.formatCellValue(sNoCell))) {
//                    if (messages.isEmpty()) {
//                        jsonObject.put("flag", false);
//                        jsonObject.put("ulbDetails", list);
//                    } else {
//                        jsonObject.put("messages", messages);
//                        jsonObject.put("flag", false);
//                        jsonObject.put("status", 4);
//                    }
//                    jsonObject.put("codes", codes);
//
//                    jsonObject.put("ulbCodeAndNames", ulbCodeAndNames);
//                    return jsonObject;
//                } else {
//                    Cell cell = row.getCell(EnumConstants.EMP_COLUMN_INDEX.SERIALNO.getValue());
//                    String sNo = dataFormatter.formatCellValue(cell);
//                    if (!validationUtils.validateNumbers(sNo)) {
//                        messages.add("Serial Number should be Numeric only  At row " + (row.getRowNum() + 1) + " in "
//                                + sheet.getSheetName() + " Sheet");
//                    }
//                }
//
//                ulbEntity = new ULBEntity();
//                // Reading Code
//                codeCell(row, dataFormatter, codes, ulbEntity, messages, sheet);
//
//                // Reading Name
//                extractedNameCell(row, dataFormatter, ulbEntity, ulbCodeAndNames, messages, sheet);
//
//                // Reading gstNumber
//                gstNumberCell(row, dataFormatter, ulbEntity, messages, sheet);
//
//                // Reading Sector
//                readingSectorCell(row, dataFormatter, ulbEntity, messages, sheet);
//
//                // Reading District Name
//                distrciNameCell(row, dataFormatter, ulbEntity, messages, sheet);
//
//                // Reading Lattitude
//                lattitudeCell(row, dataFormatter, ulbEntity, messages, sheet);
//
//                // Reading Longitude
//                longitudeCell(row, dataFormatter, ulbEntity, messages, sheet);
//                // Reading NorthLngLat
//                StringBuilder boundaryLatLng = new StringBuilder();
//                northLngLatCell(row, dataFormatter, messages, sheet, boundaryLatLng);
//                // Reading EastLngLat
//                eastLatLngCell(row, dataFormatter, messages, sheet, boundaryLatLng);
//                // Reading SothLatlng
//                southLatLngCell(row, dataFormatter, messages, sheet, boundaryLatLng);
//                // Reading westLatLng
//                westLatLngCell(row, dataFormatter, messages, sheet, boundaryLatLng);
//                if (!boundaryLatLng.toString().isEmpty()) {
//                    ulbEntity.setBoundaryLatLng(boundaryLatLng.toString());
//                }
//                list.add(ulbEntity);
//            }
//            if (messages.isEmpty()) {
//                jsonObject.put("ulbDetails", list);
//            } else {
//                jsonObject.put("messages", messages);
//                jsonObject.put("flag", false);
//                jsonObject.put("status", 4);
//            }
//        } catch (IOException e) {
//            log.error(ExceptionUtils.getFullStackTrace(e));
//            jsonObject.put("flag", false);
//            jsonObject.put("messages", "Check for File Permissions");
//            jsonObject.put("status", 4);
//        }
//        return jsonObject;
//
//    }
//
//    private void codeCell(Row row, DataFormatter dataFormatter, Set<String> codes, ULBEntity ulbEntity, List<String> messages, Sheet sheet) {
//        Cell codeCell = row.getCell(EnumConstants.ULB_COLUMN_INDEX.AREA_CODE.getValue(), Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
//        if (codeCell != null) {
//            Cell cell = row.getCell(EnumConstants.ULB_COLUMN_INDEX.AREA_CODE.getValue());
//            String code = dataFormatter.formatCellValue(cell);
//            if (code.length() <= 20 && code.length() >= 1) {
//                if (validationUtils.validateCode(code)) {
//                    boolean addStatus = codes.add(code);
//                    if (addStatus) {
//                        ulbEntity.setUlbCode(code);
//                    } else {
//                        messages.add("ULB Code should not be duplicated at row number " + (row.getRowNum() + 1)
//                                + " in " + sheet.getSheetName() + " sheet");
//                    }
//                } else {
//                    messages.add("ULB Code accepts only alphanumeric and (-) and (/) only  at row number "
//                            + (row.getRowNum() + 1) + " in " + sheet.getSheetName() + " sheet");
//                }
//            } else {
//                messages.add("ULB Code should not exceed more than 20 and not less than 2 at row number "
//                        + (row.getRowNum() + 1) + " in " + sheet.getSheetName() + " sheet");
//            }
//        } else {
//            messages.add("ULB Code should not be empty at row number " + (row.getRowNum() + 1) + " in "
//                    + sheet.getSheetName() + " sheet");
//        }
//    }
//
//    private void westLatLngCell(Row row, DataFormatter dataFormatter, List<String> messages, Sheet sheet, StringBuilder boundaryLatLng) {
//        Cell westLatLngCell = row.getCell(EnumConstants.ULB_COLUMN_INDEX.EASTLATLNG.getValue(), Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
//        String westLatLngCellValue;
//        if (westLatLngCell != null) {
//            Cell cell = row.getCell(EnumConstants.ULB_COLUMN_INDEX.EASTLATLNG.getValue());
//            westLatLngCellValue = dataFormatter.formatCellValue(cell);
//            if (westLatLngCellValue.contains(":")) {
//                String[] latlng = westLatLngCellValue.split(":");
//                if (!validationUtils.validateLatitude(latlng[0])) {
//                    messages.add("West Latitude is not in format " + (row.getRowNum() + 1) + " in "
//                            + sheet.getSheetName() + " sheet");
//                } else if (!validationUtils.validateLongitude(latlng[1])) {
//                    messages.add("West Longitude is not in format " + (row.getRowNum() + 1) + " in "
//                            + sheet.getSheetName() + " sheet");
//                } else {
//                    boundaryLatLng.append(",{").append(westLatLngCellValue).append("}");
//                }
//            }
//        }
//    }
//
//    private void southLatLngCell(Row row, DataFormatter dataFormatter, List<String> messages, Sheet sheet, StringBuilder boundaryLatLng) {
//        Cell southLatLngCell = row.getCell(EnumConstants.ULB_COLUMN_INDEX.EASTLATLNG.getValue(), Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
//        String southLatLngCellValue;
//        if (southLatLngCell != null) {
//            Cell cell = row.getCell(EnumConstants.ULB_COLUMN_INDEX.EASTLATLNG.getValue());
//            southLatLngCellValue = dataFormatter.formatCellValue(cell);
//            if (southLatLngCellValue.contains(":")) {
//                String[] latlng = southLatLngCellValue.split(":");
//                if (!validationUtils.validateLatitude(latlng[0])) {
//                    messages.add("South Latitude is not in format " + (row.getRowNum() + 1) + " in "
//                            + sheet.getSheetName() + " sheet");
//                } else if (!validationUtils.validateLongitude(latlng[1])) {
//                    messages.add("South Longitude is not in format " + (row.getRowNum() + 1) + " in "
//                            + sheet.getSheetName() + " sheet");
//                } else {
//                    boundaryLatLng.append(",{").append(southLatLngCellValue).append("}");
//                }
//            }
//        }
//    }
//
//    private void eastLatLngCell(Row row, DataFormatter dataFormatter, List<String> messages, Sheet sheet, StringBuilder boundaryLatLng) {
//        Cell eastLatLngCell = row.getCell(EnumConstants.ULB_COLUMN_INDEX.EASTLATLNG.getValue(), Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
//        String eastlatLngCellValue;
//        if (eastLatLngCell != null) {
//            Cell cell = row.getCell(EnumConstants.ULB_COLUMN_INDEX.EASTLATLNG.getValue());
//            eastlatLngCellValue = dataFormatter.formatCellValue(cell);
//            if (eastlatLngCellValue.contains(":")) {
//                String[] latlng = eastlatLngCellValue.split(":");
//                if (!validationUtils.validateLatitude(latlng[0])) {
//                    messages.add("East Latitude is not in format " + (row.getRowNum() + 1) + " in "
//                            + sheet.getSheetName() + " sheet");
//                } else if (!validationUtils.validateLongitude(latlng[1])) {
//                    messages.add("East Longitude is not in format " + (row.getRowNum() + 1) + " in "
//                            + sheet.getSheetName() + " sheet");
//                } else {
//                    boundaryLatLng.append(",{").append(eastlatLngCellValue).append("}");
//                }
//            }
//        }
//    }
//
//    private void northLngLatCell(Row row, DataFormatter dataFormatter, List<String> messages, Sheet sheet, StringBuilder boundaryLatLng) {
//        Cell northLatLngCell = row.getCell(EnumConstants.ULB_COLUMN_INDEX.NORTHLATLNG.getValue(), Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
//        String northlatLngCellValue;
//        if (northLatLngCell != null) {
//            Cell cell = row.getCell(EnumConstants.ULB_COLUMN_INDEX.NORTHLATLNG.getValue());
//            northlatLngCellValue = dataFormatter.formatCellValue(cell);
//            if (northlatLngCellValue.contains(":")) {
//                String[] latlng = northlatLngCellValue.split(":");
//                if (!validationUtils.validateLatitude(latlng[0])) {
//                    messages.add("North Latitude is not in format " + (row.getRowNum() + 1) + " in "
//                            + sheet.getSheetName() + " sheet");
//                } else if (!validationUtils.validateLongitude(latlng[1])) {
//                    messages.add("North Longitude is not in format " + (row.getRowNum() + 1) + " in "
//                            + sheet.getSheetName() + " sheet");
//                } else {
//                    boundaryLatLng.append("[{").append(northlatLngCellValue).append("}");
//                }
//            }
//        }
//    }
//
//    private void longitudeCell(Row row, DataFormatter dataFormatter, ULBEntity ulbEntity, List<String> messages, Sheet sheet) {
//        Cell longitudeCell = row.getCell(EnumConstants.ULB_COLUMN_INDEX.LONGITUDE.getValue(), Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
//        if (longitudeCell != null) {
//            Cell cell = row.getCell(EnumConstants.ULB_COLUMN_INDEX.LONGITUDE.getValue());
//            String longitudeCellValue = dataFormatter.formatCellValue(cell);
//            if (validationUtils.validateLongitude(longitudeCellValue)) {
//                ulbEntity.setLongitude(longitudeCellValue);
//            } else {
//                messages.add("Longitude should not exceed more than 20 and less than 3 at row number "
//                        + (row.getRowNum() + 1) + " in " + sheet.getSheetName() + " sheet");
//            }
//        } else {
//            messages.add("Longitude should not be empty at row " + (row.getRowNum() + 1) + " in "
//                    + sheet.getSheetName() + " sheet");
//        }
//    }
//
//    private void lattitudeCell(Row row, DataFormatter dataFormatter, ULBEntity ulbEntity, List<String> messages, Sheet sheet) {
//        Cell latitudeCell = row.getCell(EnumConstants.ULB_COLUMN_INDEX.LATITUDE.getValue(), Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
//        if (latitudeCell != null) {
//            Cell cell = row.getCell(EnumConstants.ULB_COLUMN_INDEX.LATITUDE.getValue());
//            String latitudeCellValue = dataFormatter.formatCellValue(cell);
//            if (validationUtils.validateLatitude(latitudeCellValue)) {
//                ulbEntity.setLatitude(latitudeCellValue);
//            } else {
//                messages.add("Latitude should not exceed more than 20 and less than 3 at row number "
//                        + (row.getRowNum() + 1) + " in " + sheet.getSheetName() + " sheet");
//            }
//        } else {
//            messages.add("Latitude should not be empty at row " + (row.getRowNum() + 1) + " in "
//                    + sheet.getSheetName() + " sheet");
//        }
//    }
//
//    private void distrciNameCell(Row row, DataFormatter dataFormatter, ULBEntity ulbEntity, List<String> messages, Sheet sheet) {
//        Cell districtNameCell = row.getCell(EnumConstants.ULB_COLUMN_INDEX.DISTRICT_NAME.getValue(),
//                Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
//        if (districtNameCell != null) {
//            Cell cell = row.getCell(EnumConstants.ULB_COLUMN_INDEX.DISTRICT_NAME.getValue());
//            String districtNameCellValue = dataFormatter.formatCellValue(cell);
//            if (districtNameCellValue.length() < 50 && districtNameCellValue.length() >= 3
//                    && validationUtils.validateNames(districtNameCellValue)) {
//                ulbEntity.setDistrictName(districtNameCellValue);
//            } else {
//                messages.add(
//                        "District Name should not exceed more than 50 and less than 3 at row number and it only accepts Alphabets  At row "
//                                + (row.getRowNum() + 1) + " in " + sheet.getSheetName() + " sheet");
//            }
//        } else {
//            messages.add("District Name should not be empty at row " + (row.getRowNum() + 1) + " in "
//                    + sheet.getSheetName() + " sheet");
//        }
//    }
//
//    private static void readingSectorCell(Row row, DataFormatter dataFormatter, ULBEntity ulbEntity, List<String> messages, Sheet sheet) {
//        Cell magnitudeCell = row.getCell(EnumConstants.ULB_COLUMN_INDEX.SECTOR.getValue(), Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
//        if (magnitudeCell != null) {
//            Cell cell = row.getCell(EnumConstants.ULB_COLUMN_INDEX.SECTOR.getValue());
//            String magnitudeCellValue = dataFormatter.formatCellValue(cell);
//            if (magnitudeCellValue.length() <= 50 && magnitudeCellValue.length() >= 2) {
//                ulbEntity.setMagnitude(magnitudeCellValue);
//            } else {
//                messages.add("Sector should not exceed more than 50 and less than 2 at row number "
//                        + (row.getRowNum() + 1) + " in " + sheet.getSheetName() + " sheet");
//            }
//        } else {
//            messages.add("Sector should not be empty at row number " + (row.getRowNum() + 1) + " in "
//                    + sheet.getSheetName() + " sheet");
//        }
//    }
//
//    private static void gstNumberCell(Row row, DataFormatter dataFormatter, ULBEntity ulbEntity, List<String> messages, Sheet sheet) {
//        Cell gstNumber = row.getCell(EnumConstants.ULB_COLUMN_INDEX.GSTNUMBER.getValue(), Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
//        if (gstNumber != null) {
//            Cell cell = row.getCell(EnumConstants.ULB_COLUMN_INDEX.GSTNUMBER.getValue());
//            String gstNumberValue = dataFormatter.formatCellValue(cell);
//            if (gstNumberValue.length() <= 15 && gstNumberValue.length() > 1) {
//                ulbEntity.setDepartmentGSTNumber(gstNumberValue);
//            } else {
//                messages.add("GST Number should be 15 characters long at row number " + (row.getRowNum() + 1)
//                        + " in " + sheet.getSheetName() + " sheet");
//            }
//        } else {
//            messages.add("GST Number should not be empty at row number " + (row.getRowNum() + 1) + " in "
//                    + sheet.getSheetName() + " sheet");
//        }
//    }
//
//    private void extractedNameCell(Row row, DataFormatter dataFormatter, ULBEntity ulbEntity, Map<String,
//            String> ulbCodeAndNames, List<String> messages, Sheet sheet) {
//        Cell nameCell = row.getCell(EnumConstants.ULB_COLUMN_INDEX.AREA_NAME.getValue(), Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
//        if (nameCell != null) {
//            Cell cell = row.getCell(EnumConstants.ULB_COLUMN_INDEX.AREA_NAME.getValue());
//            String name = dataFormatter.formatCellValue(cell);
//            if (name.length() < 50 && name.length() >= 3) {
//                if (validationUtils.validateNames(name)) {
//                    ulbEntity.setName(name);
//                    if (!StringUtil.isNullOrEmpty(ulbEntity.getUlbCode())) {
//                        ulbCodeAndNames.put(ulbEntity.getUlbCode(), ulbEntity.getName());
//                    }
//                } else {
//                    messages.add("Name accepts only alphabets at row number " + (row.getRowNum() + 1) + " in "
//                            + sheet.getSheetName() + " sheet");
//                }
//            } else {
//                messages.add("Name should not exceed more than 50 and not less than 3 at row number "
//                        + (row.getRowNum() + 1) + " in " + sheet.getSheetName() + " sheet");
//            }
//        } else {
//            messages.add("Name should not be empty at row number " + (row.getRowNum() + 1) + " in "
//                    + sheet.getSheetName() + " sheet");
//        }
//    }

    @Override
    @Transactional
    public JSONObject checkForMasterData() {
        JSONObject jsonObject = new JSONObject();
        boolean flag = false;
        List<String> list = new ArrayList<>();

        boolean designationsFlag = designationRepository.count() > 0;
        if (designationsFlag) {
            list.add("Designations Details");
            flag = true;
        }

        jsonObject.put("flag", flag);
        if (flag) {
            jsonObject.put("dataList", list);
        }
        return jsonObject;
    }


}
