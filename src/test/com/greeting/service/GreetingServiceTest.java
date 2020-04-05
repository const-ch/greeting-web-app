package com.greeting.service;

import com.greeting.data.Account;
import com.greeting.data.BusinessType;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.server.ResponseStatusException;

import static com.greeting.util.Constants.PATH_NOT_EXISTS_MESSAGE;
import static com.greeting.util.Constants.PATH_NOT_IMPLEMENTED_YET_MESSAGE;
import static org.junit.Assert.assertEquals;

public class GreetingServiceTest {

    @Autowired
    GreetingService greetingService = new GreetingServiceImpl();

    @Rule
    public ExpectedException expectedEx = ExpectedException.none();


    @Test
    public void testPersonalClient_success(){
        Integer clientId = 1;
        String actualResult = greetingService.greetPersonalClient(Account.personal,clientId);
        String expectedResult = "Hi, userId 1";
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testBusinessBigClient_success(){

        String actualResult = greetingService.greetBusinessClient(Account.business,BusinessType.big);
        String expectedResult = "Welcome, business user!";
        assertEquals(expectedResult, actualResult);
    }

    @Test(expected = ResponseStatusException.class)
    public void testBusinessSmallClient_exception() {
        greetingService.greetBusinessClient(Account.personal,BusinessType.small);
        expectedEx.expectMessage(PATH_NOT_IMPLEMENTED_YET_MESSAGE);
    }

    @Test(expected = ResponseStatusException.class)
    public void testGreetPersonalClientAccountBusiness_exception() {
        greetingService.greetPersonalClient(Account.business,1);
        expectedEx.expectMessage(PATH_NOT_EXISTS_MESSAGE);
    }

    @Test(expected = ResponseStatusException.class)
    public void testGreetBusinessClientAccountPersonal_exception() {
        greetingService.greetBusinessClient(Account.personal,BusinessType.big);
        expectedEx.expectMessage(PATH_NOT_EXISTS_MESSAGE);
    }
}
