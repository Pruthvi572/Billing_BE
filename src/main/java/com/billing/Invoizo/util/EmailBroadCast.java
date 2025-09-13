package com.billing.Invoizo.util;


import lombok.extern.slf4j.Slf4j;
import sendinblue.ApiClient;
import sendinblue.ApiException;
import sendinblue.Configuration;
import sendinblue.auth.ApiKeyAuth;
import sibApi.TransactionalEmailsApi;
import sibModel.*;

import java.util.ArrayList;
import java.util.List;

@Slf4j
//@Component("emailBroadCast")
public class EmailBroadCast {

    //    @Value("${email.key}")
    private String emailKey;

    //    @Value("${db.url}")
    private String jdbcUrl;

    //    @Value("${emailSenderName}")
    private String emailSenderName;

    public String sendEmail(String subject, String content, String email) {

        log.info("In Email BroadCast - Send Email Method");
        ApiClient apiClient = Configuration.getDefaultApiClient();
        ApiKeyAuth apiKeyAuth = (ApiKeyAuth) apiClient.getAuthentication("api-key");
        apiKeyAuth.setApiKey(emailKey);

        String dataBaseUrl = jdbcUrl;
        String[] arr = dataBaseUrl.split("/");
        String dbName = arr[arr.length - 1];
        String[] ipAddress = arr[2].split(":");

        TransactionalEmailsApi api = new TransactionalEmailsApi();
        SendSmtpEmail sendSmtpEmail = new SendSmtpEmail();
        sendSmtpEmail.subject(subject);
        sendSmtpEmail.textContent(content);
        SendSmtpEmailSender emailSender = new SendSmtpEmailSender();
        emailSender.name(emailSenderName);
        emailSender.email("noreply@gpms.org");
        List<String> tags = new ArrayList<>();
        tags.add("GPMS");
        tags.add("Government Project Monitoring System");
        tags.add("LeadWinner Corp");
        tags.add(dbName);
        tags.add(ipAddress[0]);
        sendSmtpEmail.sender(emailSender);
        sendSmtpEmail.tags(tags);
        List<SendSmtpEmailTo> sendSmtpEmailToList = new ArrayList<>();
        SendSmtpEmailTo sendSmtpEmailTo = new SendSmtpEmailTo();
        sendSmtpEmailTo.email(email);
        sendSmtpEmailToList.add(sendSmtpEmailTo);
        sendSmtpEmail.to(sendSmtpEmailToList);
        try {
            CreateSmtpEmail createSmtpEmail = api.sendTransacEmail(sendSmtpEmail);
            return createSmtpEmail.getMessageId();
        } catch (Exception e) {
            log.error("Error in Sending Email:", e);
            return null;
        }
    }

    public List<GetEmailEventReportEvents> getNotificationStatus(String emailMessageId) throws ApiException {

        ApiClient apiClient = Configuration.getDefaultApiClient();
        ApiKeyAuth apiKeyAuth = (ApiKeyAuth) apiClient.getAuthentication("api-key");
        apiKeyAuth.setApiKey(emailKey);
        TransactionalEmailsApi api = new TransactionalEmailsApi();
        GetEmailEventReport emailEventReport;
        if (emailMessageId != null) {
            emailEventReport = api.getEmailEventReport(null, null, null, null, null, null, null, null, emailMessageId,
                    null, null);
        } else {
            emailEventReport = api.getEmailEventReport(null, null, null, null, 30L, null,
                    null, null, null, null, null);
        }
        return emailEventReport.getEvents();
    }

    public GetAggregatedReport getNotificationCounts() throws ApiException {
        ApiClient apiClient = Configuration.getDefaultApiClient();
        ApiKeyAuth apiKeyAuth = (ApiKeyAuth) apiClient.getAuthentication("api-key");
        apiKeyAuth.setApiKey(emailKey);
        String dataBaseUrl = jdbcUrl;
        String[] arr = dataBaseUrl.split("/");
        String dbName = arr[arr.length - 1];
        TransactionalEmailsApi api = new TransactionalEmailsApi();
        return api.getAggregatedSmtpReport(null, null, 30L, dbName);
    }
}
