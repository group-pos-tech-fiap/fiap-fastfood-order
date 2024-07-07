package com.fiap.fastfood.order.common.exceptions;

import com.fiap.fastfood.order.common.exceptions.custom.EntityNotFoundException;
import com.fiap.fastfood.order.common.exceptions.model.ExceptionDetails;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.WebRequest;

import static org.junit.jupiter.api.Assertions.*;

class ExceptionControllerHandlerTest {

    @Mock
    private WebRequest webRequest;

    private ExceptionControllerHandler exceptionControllerHandler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        exceptionControllerHandler = new ExceptionControllerHandler();
    }

    @Test
    void testResourceException() {
        EntityNotFoundException ex = new EntityNotFoundException("ERROR_CODE", "Resource not found");

        ResponseEntity<ExceptionDetails> response = exceptionControllerHandler.resourceException(ex, webRequest);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        ExceptionDetails body = response.getBody();
        assertNotNull(body);
        assertEquals("https://developer.mozilla.org/en-US/docs/Web/HTTP/Status/404", body.getType());
        assertEquals("The requested resource was not found.", body.getTitle());
        assertEquals("ERROR_CODE", body.getCode());
        assertEquals("Resource not found", body.getDetail());
        assertEquals(HttpStatus.NOT_FOUND.value(), body.getStatus());
        assertNotNull(body.getDate());
    }
}