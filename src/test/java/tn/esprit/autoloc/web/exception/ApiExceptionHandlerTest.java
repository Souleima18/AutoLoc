package tn.esprit.autoloc.web.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.mock.web.MockHttpServletRequest;
import tn.esprit.autoloc.exception.ResourceConflictException;

class ApiExceptionHandlerTest {

    private final ApiExceptionHandler handler = new ApiExceptionHandler();

    @Test
    void conflictResponseContainsClientMessageWithoutStackTrace() {
        MockHttpServletRequest request = new MockHttpServletRequest("DELETE", "/api/agences/1");

        var response = handler.handleConflict(
                new ResourceConflictException("Agence encore utilisée."),
                request);

        ProblemDetail body = response.getBody();
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Agence encore utilisée.", body.getDetail());
        assertEquals("/api/agences/1", body.getInstance().toString());
    }
}
