package com.fiap.fastfood.order.common.beans;

import com.fiap.fastfood.order.common.interfaces.datasources.SpringDataMongoOrderRepository;
import com.fiap.fastfood.order.common.interfaces.gateway.OrderGateway;
import com.fiap.fastfood.order.communication.gateway.OrderGatewayImpl;
import com.fiap.fastfood.order.external.feign.PaymentClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;


@ExtendWith(MockitoExtension.class)
class GatewayBeanDeclarationTest {

    @Mock
    private SpringDataMongoOrderRepository repository;

    @Mock
    private PaymentClient paymentClient;

    @Test
    void testOrderGatewayBean() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.registerBean(SpringDataMongoOrderRepository.class, () -> repository);
        context.registerBean(PaymentClient.class, () -> paymentClient);
        context.register(GatewayBeanDeclaration.class);
        context.refresh();

        OrderGateway orderGateway = context.getBean(OrderGateway.class);

        assertNotNull(orderGateway);
        assertTrue(orderGateway instanceof OrderGatewayImpl);
    }
}