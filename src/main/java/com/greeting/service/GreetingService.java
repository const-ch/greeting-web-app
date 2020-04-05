package com.greeting.service;

import com.greeting.data.Account;
import com.greeting.data.BusinessType;

public interface GreetingService {
    String greetPersonalClient(Account account, Integer userId);

    String greetBusinessClient(Account account, BusinessType businessType);
}
