package com.fiap.fastfood.order.core.usecase;

import com.fiap.fastfood.order.common.exceptions.custom.EntityNotFoundException;
import com.fiap.fastfood.order.common.interfaces.gateway.OrderGateway;
import com.fiap.fastfood.order.common.interfaces.usecase.OrderUseCase;
import com.fiap.fastfood.order.core.entity.Order;
import com.fiap.fastfood.order.core.entity.OrderPaymentStatus;
import com.fiap.fastfood.order.core.entity.OrderStatus;
import com.fiap.fastfood.order.external.feign.PaymentClient;
import com.fiap.fastfood.order.external.feign.models.PaymentRequest;

import java.util.List;

public class OrderUseCaseImpl implements OrderUseCase {

    private final OrderGateway orderGateway;
    private final PaymentClient paymentClient;

    public OrderUseCaseImpl(OrderGateway orderGateway, PaymentClient paymentClient) {
        this.orderGateway = orderGateway;
        this.paymentClient = paymentClient;
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
        var order = getOrderById(orderId);
        var paymentRequest = new PaymentRequest(order.getId(), order.getTotalValue());
        try {
            var response = paymentClient.performPayment(paymentRequest);
            if (response != null && response.getNsu() != null) {
                order.setNsuPayment(response.getNsu());
                order.setPaymentStatus(OrderPaymentStatus.APPROVED);
            } else {
                order.setPaymentStatus(OrderPaymentStatus.REJECTED);
            }
        } catch (Exception e) {
            order.setPaymentStatus(OrderPaymentStatus.REJECTED);
        }
        return orderGateway.saveOrder(order);
    }

    @Override
    public Order updateOrderStatus(String orderId, OrderStatus status) throws EntityNotFoundException {
        var order = getOrderById(orderId);
        order.setStatus(status);
        return orderGateway.saveOrder(order);
    }
}
