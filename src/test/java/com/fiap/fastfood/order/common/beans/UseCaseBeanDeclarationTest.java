package com.fiap.fastfood.order.common.beans;

import com.fiap.fastfood.order.common.interfaces.gateway.OrderGateway;
import com.fiap.fastfood.order.common.interfaces.usecase.OrderUseCase;
import com.fiap.fastfood.order.core.usecase.OrderUseCaseImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UseCaseBeanDeclarationTest {

    @Mock
    private OrderGateway orderGateway;

    @Test
    void testOrderUseCaseBean() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.registerBean(OrderGateway.class, () -> orderGateway);
        context.register(UseCaseBeanDeclaration.class);
        context.refresh();

        OrderUseCase orderUseCase = context.getBean(OrderUseCase.class);

        assertNotNull(orderUseCase);
        assertTrue(orderUseCase instanceof OrderUseCaseImpl);
    }
}