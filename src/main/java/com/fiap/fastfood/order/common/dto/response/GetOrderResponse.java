package com.fiap.fastfood.order.common.dto.response;

import com.fiap.fastfood.order.core.entity.Item;
import com.fiap.fastfood.order.core.entity.OrderPaymentStatus;
import com.fiap.fastfood.order.core.entity.OrderStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
@AllArgsConstructor
public class GetOrderResponse {
    private String id;
    private List<Item> items;
    private BigDecimal totalValue;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private OrderStatus status;
    private OrderPaymentStatus paymentStatus;
}
