package com.fiap.fastfood.order.common.beans;

import com.fiap.fastfood.order.common.interfaces.datasources.SpringDataMongoOrderRepository;
import com.fiap.fastfood.order.common.interfaces.gateway.OrderGateway;
import com.fiap.fastfood.order.communication.gateway.OrderGatewayImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayBeanDeclaration {

    @Bean
    public OrderGateway orderGateway(SpringDataMongoOrderRepository repository) {
        return new OrderGatewayImpl(repository);
    }
}
