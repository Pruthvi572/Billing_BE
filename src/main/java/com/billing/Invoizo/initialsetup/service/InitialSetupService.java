package com.billing.Invoizo.initialsetup.service;

import com.billing.Invoizo.initialsetup.dto.InitialSetupDTO;
import com.billing.Invoizo.masters.modules.entity.ModulesEntity;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.util.List;

public interface InitialSetupService {

    JSONObject checkLicenseKey(String licenseId, String subClientUsername);

    void saveAllDetails(InitialSetupDTO initialSetupDTO) throws IOException;

    List<ModulesEntity> getListOfExistingData();

    JSONObject checkForMasterData();

}
