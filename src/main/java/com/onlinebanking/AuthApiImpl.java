package com.onlinebanking.impl;

import com.onlinebanking.api.AuthApi;
import com.onlinebanking.JwtTokenProvider;
import com.onlinebanking.model.AuthLoginPost200Response;
import com.onlinebanking.model.AuthLoginPostRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AuthApiImpl implements AuthApi {

    private final JwtTokenProvider jwtTokenProvider;
    private final Map<String, String> users = new HashMap<>();

    @Autowired
    public AuthApiImpl(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
        //Initialize users. In a real application, you would use a database.
        users.put("user1", "pass1");
        users.put("user2", "pass2");
    }

    @Override
    public ResponseEntity<AuthLoginPost200Response> authLoginPost(AuthLoginPostRequest authLoginPostRequest) {
        if (users.containsKey(authLoginPostRequest.getUsername()) &&
                users.get(authLoginPostRequest.getUsername()).equals(authLoginPostRequest.getPassword())) {
            String token = jwtTokenProvider.generateToken(authLoginPostRequest.getUsername());
            AuthLoginPost200Response response = new AuthLoginPost200Response();
            response.setToken(token);
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(401).build();
        }
    }

    @Override
    public ResponseEntity<Void> authLogoutPost() {
        // In a real application, you might invalidate the token or store it in a blacklist.
        return ResponseEntity.ok().build();
    }
}