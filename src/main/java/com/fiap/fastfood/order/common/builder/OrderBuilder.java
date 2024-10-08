package com.fiap.fastfood.order.common.builder;

import com.fiap.fastfood.order.common.dto.request.CreateOrderRequest;
import com.fiap.fastfood.order.common.dto.response.GetOrderResponse;
import com.fiap.fastfood.order.core.entity.Order;
import com.fiap.fastfood.order.external.orm.OrderORM;

import java.util.stream.Collectors;

public class OrderBuilder {

    public static GetOrderResponse fromDomainToResponse(Order order) {
        return GetOrderResponse.builder()
                .id(order.getId())
                .createdAt(order.getCreatedAt())
                .updatedAt(order.getUpdatedAt())
                .totalValue(order.getTotalValue())
                .items(order.getItems())
                .status(order.getStatus())
                .paymentStatus(order.getPaymentStatus())
                .build();
    }

    public static Order fromRequestToDomain(CreateOrderRequest request) {
        return Order.builder()
                .items(request.getItems())
                .build();
    }

    public static Order fromOrmToDomain(OrderORM orm) {
        return Order.builder()
                .id(orm.getId())
                .createdAt(orm.getCreatedAt())
                .updatedAt(orm.getUpdatedAt())
                .totalValue(orm.getTotalValue())
                .status(orm.getStatus())
                .paymentStatus(orm.getPaymentStatus())
                .items(orm.getItems().stream().map(ItemBuilder::fromOrmToDomain).collect(Collectors.toList()))
                .build();
    }

    public static OrderORM fromDomainToOrm(Order order) {
        return OrderORM.builder()
                .id(order.getId())
                .createdAt(order.getCreatedAt())
                .updatedAt(order.getUpdatedAt())
                .totalValue(order.getTotalValue())
                .status(order.getStatus())
                .paymentStatus(order.getPaymentStatus())
                .items(order.getItems().stream().map(ItemBuilder::fromDomainToOrm).collect(Collectors.toList()))
                .build();
    }
}
