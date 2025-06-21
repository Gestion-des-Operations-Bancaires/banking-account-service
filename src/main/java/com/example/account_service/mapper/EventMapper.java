package com.example.account_service.mapper;

import com.example.account_service.entity.Account;
import com.example.account_service.event.AccountEvent;

public class EventMapper {

    public static AccountEvent mapToAccountCreateEvent(Account account) {
        AccountEvent accountEvent = new AccountEvent();
        accountEvent.setEventType("Account-Creation");
        accountEvent.setAccountId(account.getId());
        accountEvent.setAccountNumber(account.getAccountNumber());
        accountEvent.setCustomerId(account.getCustomerId());
        accountEvent.setAccountType(account.getAccountType().getDisplayName());
        accountEvent.setBalance(account.getBalance());

        return accountEvent;
    }

}
