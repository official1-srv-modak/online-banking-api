package com.onlinebanking;

import com.fasterxml.jackson.datatype.jsr310.ser.OffsetDateTimeSerializer;
import com.onlinebanking.api.TransactionsApi;
import com.onlinebanking.model.AccountsIdGet200Response;
import com.onlinebanking.model.TransactionsHistoryGet200ResponseInner;
import com.onlinebanking.model.TransactionsTransferPostRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class TransactionsApiImpl implements TransactionsApi {

    private final Map<String, AccountsIdGet200Response> accounts;
    private final List<TransactionsHistoryGet200ResponseInner> transactions = new ArrayList<>();

    public TransactionsApiImpl(AccountsApiImpl accountsApi) {
        this.accounts = accountsApi.accounts;
    }

    @Override
    public ResponseEntity<List<TransactionsHistoryGet200ResponseInner>> transactionsHistoryGet() {
        return ResponseEntity.ok(transactions);
    }

    @Override
    public ResponseEntity<Void> transactionsTransferPost(TransactionsTransferPostRequest request) {
        AccountsIdGet200Response fromAccount = accounts.get(request.getFromAccount());
        AccountsIdGet200Response toAccount = accounts.get(request.getToAccount());

        if (fromAccount == null || toAccount == null) {
            return ResponseEntity.badRequest().build();
        }

        if (fromAccount.getBalance() < request.getAmount()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        fromAccount.setBalance(fromAccount.getBalance() - request.getAmount());
        toAccount.setBalance(toAccount.getBalance() + request.getAmount());

        TransactionsHistoryGet200ResponseInner transaction = new TransactionsHistoryGet200ResponseInner();
        transaction.setTransactionId(UUID.randomUUID().toString());
        transaction.setAmount(request.getAmount());
        transaction.setType(TransactionsHistoryGet200ResponseInner.TypeEnum.DEBIT);
        transaction.setTimestamp(OffsetDateTime.now());
        transactions.add(transaction);

        return ResponseEntity.ok().build();
    }
}