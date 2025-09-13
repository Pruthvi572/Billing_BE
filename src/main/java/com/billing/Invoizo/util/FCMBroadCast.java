package com.billing.Invoizo.util;


import com.billing.Invoizo.util.dto.FCMDTO;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Slf4j
@Component("fcmBroadCast")
public class FCMBroadCast {

    private static final String FIREBASE_URL_API = "https://fcm.googleapis.com/fcm/send";
    private static final String FIREBASE_SERVER_KEY =
            "AAAAtOpgSgU:APA91bGfl9lbw43jD3nNP8jIOc35Z8xgY0hNeU8KfAAuY0ppZ7wrc5_Z9MmXaSGC9ksAsF965nErMk0Ye0UP5KaLi0qXMs8jmFlU-" +
                    "_isWGcQU4-xnYmRhEf4qvbYsQfink9oIV6ku4xL";

    @SuppressWarnings("unchecked")
    @Async
    public String sendFCMBroadCastNotification(FCMDTO fcmdto) throws InterruptedException, ExecutionException {

        RestTemplate restTemplate = new RestTemplate();

        JSONObject body = new JSONObject();
        body.put("registration_ids", fcmdto.getRegIds());
        body.put("priority", "high");

        JSONObject notification = new JSONObject();
        notification.put("body", fcmdto.getBody());
        notification.put("title", fcmdto.getTitle());
        notification.put("moduleName", fcmdto.getModuleName());
        notification.put("action", fcmdto.getAction());
        notification.put("winCode", fcmdto.getWinCode());
        notification.put("referenceNumber", fcmdto.getReferenceNo());

        JSONObject data = new JSONObject();
        data.put("title", fcmdto.getBody());
        data.put("body", fcmdto.getTitle());
        data.put("moduleName", fcmdto.getModuleName());
        data.put("action", fcmdto.getAction());
        data.put("winCode", fcmdto.getWinCode());
        data.put("referenceNumber", fcmdto.getReferenceNo());

        if (!StringUtil.isNullOrEmpty(fcmdto.getImageUrl())) {
            notification.put("image", fcmdto.getImageUrl());
            data.put("image", fcmdto.getImageUrl());
        }

        body.put("notification", notification);
        body.put("data", data);
        HttpEntity<String> request = new HttpEntity<>(body.toString());

        ArrayList<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();
        interceptors.add(new HeaderRequestInterceptor("Authorization", "key=" + FIREBASE_SERVER_KEY));
        interceptors.add(new HeaderRequestInterceptor("Content-Type", "application/json"));
        restTemplate.setInterceptors(interceptors);

        String firebaseResponse = restTemplate.postForObject(FIREBASE_URL_API, request, String.class);
        CompletableFuture<String> completableFuture = CompletableFuture.completedFuture(firebaseResponse);
        CompletableFuture.allOf(completableFuture).join();
        return completableFuture.get();
    }

}
