package com.onlinebanking;

import com.onlinebanking.api.AccountsApi;
import com.onlinebanking.model.AccountsIdGet200Response;
import com.onlinebanking.model.AccountsIdPutRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AccountsApiImpl implements AccountsApi {

    public final Map<String, AccountsIdGet200Response> accounts = new HashMap<>();

    public AccountsApiImpl() {
        // Initialize sample accounts
        AccountsIdGet200Response account1 = new AccountsIdGet200Response();
        account1.setAccountId("acc_123");
        account1.setBalance(1000.0f); // Note the 'f' for float
        account1.setAccountType("Checking");
        accounts.put("acc_123", account1);

        AccountsIdGet200Response account2 = new AccountsIdGet200Response();
        account2.setAccountId("acc_456");
        account2.setBalance(500.0f); // Note the 'f' for float
        account2.setAccountType("Savings");
        accounts.put("acc_456", account2);
    }

    @Override
    public ResponseEntity<AccountsIdGet200Response> accountsIdGet(String id) {
        AccountsIdGet200Response account = accounts.get(id);
        if (account != null) {
            return ResponseEntity.ok(account);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Override
    public ResponseEntity<Void> accountsIdPut(String id, AccountsIdPutRequest accountsIdPutRequest) {
        if (accounts.containsKey(id)) {
            AccountsIdGet200Response existingAccount = accounts.get(id);
            // Update email and phone if they are provided
            if(accountsIdPutRequest.getEmail() != null) {
                existingAccount.setEmail(accountsIdPutRequest.getEmail());
            }
            if(accountsIdPutRequest.getPhone() != null) {
                existingAccount.setPhone(accountsIdPutRequest.getPhone());
            }
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().build();
        }
    }
}