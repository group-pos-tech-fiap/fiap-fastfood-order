package com.fiap.fastfood.order.core.usecase;

import com.fiap.fastfood.order.common.exceptions.custom.EntityNotFoundException;
import com.fiap.fastfood.order.common.interfaces.gateway.OrderGateway;
import com.fiap.fastfood.order.common.interfaces.usecase.OrderUseCase;
import com.fiap.fastfood.order.core.entity.Order;
import com.fiap.fastfood.order.core.entity.OrderPaymentStatus;
import com.fiap.fastfood.order.core.entity.OrderStatus;

import java.util.List;

public class OrderUseCaseImpl implements OrderUseCase {

    private final OrderGateway orderGateway;

    public OrderUseCaseImpl(OrderGateway orderGateway) {
        this.orderGateway = orderGateway;
    }

    @Override
    public Order createOrder(Order order) {
        order.setStatus(OrderStatus.RECEIVED);
        order.setPaymentStatus(OrderPaymentStatus.PENDING);
        return orderGateway.saveOrder(order);
    }

    @Override
    public List<Order> listOrder() {
        return orderGateway.listOrder();
    }

    @Override
    public Order getOrderById(String id) throws EntityNotFoundException {
        return orderGateway.getOrderById(id);
    }

    @Override
    public Order performPayment(String orderId) throws EntityNotFoundException {
        return orderGateway.performPayment(orderId);
    }
}
