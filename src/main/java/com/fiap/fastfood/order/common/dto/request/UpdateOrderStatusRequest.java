package com.fiap.fastfood.order.common.dto.request;

import com.fiap.fastfood.order.core.entity.OrderStatus;

public record UpdateOrderStatusRequest(String orderId, OrderStatus status) {
}
