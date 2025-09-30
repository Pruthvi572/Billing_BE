package com.billing.Invoizo.config;


import com.billing.Invoizo.InvoizoProperties;
import com.billing.Invoizo.setup.service.SetupService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DBInit {
    @Autowired
    private InvoizoProperties invoizoProperties;

    @Autowired
    private SetupService setupService;

    /**
     * Running Default Scripts for inserting Json data and Load Sequence
     * and Default Designations before doing the setup
     */
    @PostConstruct
    private void postConstruct() {
        if (invoizoProperties.isRunDefaultScripts()) {
            setupService.insertDefaultValues();
        }
    }

}
