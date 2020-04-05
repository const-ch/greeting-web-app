package com.greeting.endpoint;

import com.greeting.data.Account;
import com.greeting.data.BusinessType;
import com.greeting.service.GreetingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import static com.greeting.util.EndpointConstants.ACCOUNT_PARAM;
import static com.greeting.util.EndpointConstants.BUSINESS_ACCOUNT_PATH;
import static com.greeting.util.EndpointConstants.BUSINESS_TYPE_PARAM;
import static com.greeting.util.EndpointConstants.ID_PARAM;
import static com.greeting.util.EndpointConstants.PERSONAL_ACCOUNT_PATH;

@RestController
@RequestMapping("/greeting")
@Validated
public class GreetingEndpoint {

    GreetingService greetingService;

    @Autowired
    public GreetingEndpoint(GreetingService greetingService) {
        this.greetingService = greetingService;
    }

    @GetMapping(value = PERSONAL_ACCOUNT_PATH)
    public String getPersonalGreeting(@PathVariable(ACCOUNT_PARAM) Account account,
                                      @PathVariable(ID_PARAM) @NotNull @Positive Integer id) {
        return greetingService.greetPersonalClient(account, id);
    }

    @GetMapping(value = BUSINESS_ACCOUNT_PATH)
    public String getBusinessClientGreeting(@PathVariable(ACCOUNT_PARAM) Account account,
                                            @PathVariable(BUSINESS_TYPE_PARAM) @NotNull BusinessType businessType) {
        return greetingService.greetBusinessClient(account, businessType);
    }
}
