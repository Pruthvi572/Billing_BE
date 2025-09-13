package com.billing.Invoizo.security.jwt;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ActiveTokenManager {

    private final Map<String, List<String>> activeTokens = new ConcurrentHashMap<>();

    public void generateAndManageTokenForUser(String username, String newToken) {
        List<String> userTokens = activeTokens.computeIfAbsent(username, k -> new ArrayList<>());
        userTokens.clear();
        userTokens.add(newToken);
        activeTokens.put(username, userTokens);
    }

    public boolean isTokenValidForUser(String username, String token) {
        List<String> userTokens = activeTokens.getOrDefault(username, new ArrayList<>());
        return userTokens.contains(token);
    }

    public void removeTokensForUser(String username) {
        activeTokens.remove(username);
    }
}
