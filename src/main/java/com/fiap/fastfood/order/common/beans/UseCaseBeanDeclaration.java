package com.fiap.fastfood.order.common.beans;

import com.fiap.fastfood.order.common.interfaces.gateway.OrderGateway;
import com.fiap.fastfood.order.common.interfaces.usecase.OrderUseCase;
import com.fiap.fastfood.order.core.usecase.OrderUseCaseImpl;
import com.fiap.fastfood.order.external.feign.PaymentClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseBeanDeclaration {

    @Bean
    public OrderUseCase orderUseCase(OrderGateway orderGateway, PaymentClient paymentClient) {
        return new OrderUseCaseImpl(orderGateway, paymentClient);
    }
}
