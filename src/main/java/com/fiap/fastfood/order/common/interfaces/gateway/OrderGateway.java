package com.fiap.fastfood.order.common.interfaces.gateway;

import com.fiap.fastfood.order.common.exceptions.custom.EntityNotFoundException;
import com.fiap.fastfood.order.core.entity.Order;

import java.util.List;

public interface OrderGateway {
    List<Order> listOrder();
    Order saveOrder(Order order);
    Order getOrderById(String id) throws EntityNotFoundException;
}
