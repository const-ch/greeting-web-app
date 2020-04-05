package com.greeting.endpoint;

import com.greeting.data.Account;
import com.greeting.data.BusinessType;
import com.greeting.service.GreetingService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;

import static com.greeting.util.Constants.PATH_NOT_EXISTS_MESSAGE;
import static com.greeting.util.Constants.PATH_NOT_IMPLEMENTED_YET_MESSAGE;
import static com.greeting.util.EndpointConstants.BUSINESS_ACCOUNT_PATH;
import static com.greeting.util.EndpointConstants.GREETING_PATH;
import static com.greeting.util.EndpointConstants.PERSONAL_ACCOUNT_PATH;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(GreetingEndpoint.class)
@ActiveProfiles("test")
public class GreetingEndpointMvcTest {

    public static final String GREET_BUSINESS = GREETING_PATH + BUSINESS_ACCOUNT_PATH;
    public static final String GREET_PERSON = GREETING_PATH + PERSONAL_ACCOUNT_PATH;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GreetingService greetingApiServiceMock;

    @Test
    public void greetPersonalAccount_successful() throws Exception {
        String expectedGreeting = "Hi, userId 1";
        when(greetingApiServiceMock.greetPersonalClient(Account.personal, 1)).thenReturn(expectedGreeting);
        // When: requesting the personal account, id is correct then status 200 is returned
        String responseBody = mockMvc.perform(get(GREET_PERSON, Account.personal.name(), 1))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        // And: the response body contains the expected string
        assertEquals(responseBody, expectedGreeting);
        // And: the mock was called once as expected
        verify(greetingApiServiceMock, times(1)).greetPersonalClient(Account.personal, 1);
    }

    @Test
    public void greetBigBusinessAccount_successful() throws Exception {
        String expectedGreeting = "Welcome, business user!";
        when(greetingApiServiceMock.greetBusinessClient(Account.business, BusinessType.big)).thenReturn("Welcome, business user!");
        // When: requesting the business account, type is big is correct then status 200 is returned
        String responseBody = mockMvc.perform(get(GREET_BUSINESS, Account.business.name(), BusinessType.big))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        // And: the response body contains the expected string
        assertEquals(expectedGreeting, responseBody);
        // And: the mock was called once as expected
        verify(greetingApiServiceMock, times(1)).greetBusinessClient(Account.business, BusinessType.big);
    }

    @Test
    public void greetSmallBusinessAccount_error_501_not_implemented() throws Exception {
        String expectedMessage = "Path is not implemented yet";
        when(greetingApiServiceMock.greetBusinessClient(Account.business, BusinessType.small))
                .thenThrow(new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED, PATH_NOT_IMPLEMENTED_YET_MESSAGE));
        // When: requesting the business account, type is small, then status 501 is returned
        String responseBody = mockMvc.perform(get(GREET_BUSINESS, Account.business.name(), BusinessType.small))
                .andExpect(status().is(501)).andReturn().getResponse().getErrorMessage();
        // And: the response contains the expected string
        assertEquals(expectedMessage, responseBody);
        // And: the mock was called once as expected
        verify(greetingApiServiceMock, times(1)).greetBusinessClient(Account.business, BusinessType.small);
    }

    @Test
    public void greetAccountTypeInvalid_error400() throws Exception {
        String expectedMessage = "The value wrongAccount is not correct. Possible values are [personal, business]";
        // When: requesting invalid account type, then status 400 and validation error is returned
        String responseBody = mockMvc.perform(get(GREET_BUSINESS, "wrongAccount", BusinessType.big))
                .andExpect(status().is(400)).andReturn().getResponse().getErrorMessage();
        // And: the response contains the expected string
        assertEquals(expectedMessage, responseBody);
        // And: the mock was not called as expected
        verify(greetingApiServiceMock, never()).greetBusinessClient(any(), any());
    }

    @Test
    public void greetBusinessIdInvalidBusinessType_error400() throws Exception {
        String expectedMessage = "The value invalidBusinessType is not correct. Possible values are [big, small]";
        // When: requesting invalid business type, then status 400 and validation error is returned
        String responseBody = mockMvc.perform(get(GREET_BUSINESS, Account.business.name(), "invalidBusinessType"))
                .andExpect(status().is(400)).andReturn().getResponse().getErrorMessage();
        // And: the response contains the expected string
        assertEquals(expectedMessage, responseBody);
        // And: the mock was not called as expected
        verify(greetingApiServiceMock, never()).greetBusinessClient(any(), any());
    }

    @Test
    public void wrongPathBusinessWithId_error404() throws Exception {
        String expectedMessage = "Path is not exist";
        when(greetingApiServiceMock.greetPersonalClient(Account.business, 1))
                .thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, PATH_NOT_EXISTS_MESSAGE));
        // When: requesting invalid path, then status 404 is returned
        String response = mockMvc.perform(get(GREETING_PATH + "/account/{account}/id/{id}", Account.business.name(), 1))
                .andExpect(status().is(404)).andReturn().getResponse().getErrorMessage();
        // And: the response contains the expected string
        assertEquals(expectedMessage, response);
        // And: the mock was called as expected
        verify(greetingApiServiceMock, times(1)).greetPersonalClient(any(), any());
    }

    @Test
    public void wrongPathPersonalAccountWithBusinessInput_error404() throws Exception {
        String expectedMessage = "Path is not exist";
        when(greetingApiServiceMock.greetBusinessClient(Account.personal, BusinessType.big))
                .thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, PATH_NOT_EXISTS_MESSAGE));
        // When: requesting invalid path, then status 404 is returned
        String response = mockMvc.perform(get(GREETING_PATH + "/account/{account}/business/{id}", Account.personal.name(), BusinessType.big))
                .andExpect(status().is(404)).andReturn().getResponse().getErrorMessage();
        // And: the response contains the expected string
        assertEquals(expectedMessage, response);
        // And: the mock was called as expected
        verify(greetingApiServiceMock, times(1)).greetBusinessClient(Account.personal, BusinessType.big);
    }

    @Test
    public void greetPersonalAccountIdNotPositive_error400() throws Exception {
        String expectedMessage = "getPersonalGreeting.id: must be greater than 0";
        // When: requesting invalid id, then status 400 and validation error is returned
        String responseBody = mockMvc.perform(get(GREET_PERSON, Account.personal.name(), -1))
                .andExpect(status().is(400)).andReturn().getResponse().getErrorMessage();
        // And: the response contains the expected string
        assertEquals(expectedMessage, responseBody);
        // And: the mock was not called as expected
        verify(greetingApiServiceMock, never()).greetPersonalClient(any(), anyInt());
    }

    @Test
    public void greetPersonalAccountIdString_error400() throws Exception {
        String expectedMessage = "Numeric parameter is incorrect: For input string: \"one\"";
        // When: requesting invalid id type, then status 400 and validation error is returned
        String responseBody = mockMvc.perform(get(GREET_PERSON, Account.personal.name(), "one"))
                .andExpect(status().is(400)).andReturn().getResponse().getErrorMessage();
        // And: the response contains the expected string
        assertEquals(expectedMessage, responseBody);
        // And: the mock was not called as expected
        verify(greetingApiServiceMock, never()).greetPersonalClient(any(), anyInt());
    }

}
