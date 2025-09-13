package com.billing.Invoizo.user.service;


import com.billing.Invoizo.InvoizoProperties;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserManagementServiceImpl implements UserManagementService {

    private final RestTemplateBuilder restTemplate;

    private final InvoizoProperties invoizoProperties;


    @Autowired
    public UserManagementServiceImpl(RestTemplateBuilder restTemplate, InvoizoProperties invoizoProperties) {
        this.restTemplate = restTemplate;
        this.invoizoProperties = invoizoProperties;
    }

    @Override
    public boolean checkBefore(JSONObject jsonObject) {
        try {
            String url = invoizoProperties.getSecurityCheckURL() + "/web/security/check/usercreate1";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Object> entity = new HttpEntity<>(jsonObject, headers);
            ResponseEntity<Object> response = restTemplate.build().exchange(url, HttpMethod.POST, entity, Object.class);
            if (response.getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR) {
                throw new Exception();
            }
            JSONObject jsonObject1 = (JSONObject) response.getBody();
            return (boolean) jsonObject1.get("flag");
        } catch (Exception e) {
            log.error("Error in Checking with License Module :", e);
        }
        return false;
    }
}
