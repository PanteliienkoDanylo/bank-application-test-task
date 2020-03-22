package cn.ua.bank.application.test.task.configuration;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class TokenProvider {

    private static Map<String, AuthenticatedUser> tokenToUserMap = new HashMap<>();

    public void put(String token, AuthenticatedUser user) {
        tokenToUserMap.put(token, user);
    }

    public AuthenticatedUser get(String token) {
        if (token != null) {
            return tokenToUserMap.get(token);
        }
        return null;
    }

    public String newToken(){
        return UUID.randomUUID().toString();
    }
}
