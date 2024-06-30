package com.fiap.fastfood.order.common.interfaces.usecase;

import com.fiap.fastfood.order.common.exceptions.custom.EntityNotFoundException;
import com.fiap.fastfood.order.core.entity.Order;

import java.util.List;

public interface OrderUseCase {

    Order createOrder(Order order);

    List<Order> listOrder();

    Order getOrderById(String id) throws EntityNotFoundException;

    Order performPayment(String orderId) throws EntityNotFoundException;
}
