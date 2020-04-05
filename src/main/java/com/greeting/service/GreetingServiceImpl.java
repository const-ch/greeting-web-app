package com.greeting.service;

import com.greeting.data.Account;
import com.greeting.data.BusinessType;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import static com.greeting.util.Constants.GREET_BUSINESS;
import static com.greeting.util.Constants.GREET_PERSONAL;
import static com.greeting.util.Constants.PATH_NOT_EXISTS_MESSAGE;
import static com.greeting.util.Constants.PATH_NOT_IMPLEMENTED_YET_MESSAGE;

@Service
public class GreetingServiceImpl implements GreetingService {

    @Override
    public String greetPersonalClient(Account account, Integer userId) {
        validateAccountType(account, Account.personal);
        return String.format(GREET_PERSONAL, userId);
    }

    @Override
    public String greetBusinessClient(Account account, BusinessType businessType) {
        validateAccountType(account, Account.business);
        validateImplementationForBusinessType(businessType);
        return GREET_BUSINESS;
    }

    private void validateImplementationForBusinessType(BusinessType businessType) {
        if (BusinessType.small.equals(businessType)) {
            throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED, PATH_NOT_IMPLEMENTED_YET_MESSAGE);
        }
    }

    private void validateAccountType(Account enteredAccountType, Account allowedAccount) {
        if (!allowedAccount.equals(enteredAccountType)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, PATH_NOT_EXISTS_MESSAGE);
        }
    }
}