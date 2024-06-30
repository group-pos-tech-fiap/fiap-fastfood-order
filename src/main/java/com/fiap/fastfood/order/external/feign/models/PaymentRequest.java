package com.fiap.fastfood.order.external.feign.models;

import java.math.BigDecimal;

public record PaymentRequest (String orderId, BigDecimal value) {
}
