package com.fiap.fastfood.order.common.dto.response;

import com.fiap.fastfood.order.core.entity.OrderPaymentStatus;
import lombok.*;

@Builder
@Getter
@AllArgsConstructor
public class GetOrderPaymentStatusResponse {
    private OrderPaymentStatus paymentStatus;
}
