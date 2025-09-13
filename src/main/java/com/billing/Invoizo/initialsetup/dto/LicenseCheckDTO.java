package com.billing.Invoizo.initialsetup.dto;


import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.json.simple.JSONObject;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
public class LicenseCheckDTO implements Serializable {
    private JSONObject response;

}
