package com.fiap.fastfood.order.communication.controllers;

import com.fiap.fastfood.order.common.builder.OrderBuilder;
import com.fiap.fastfood.order.common.dto.request.CreateOrderRequest;
import com.fiap.fastfood.order.common.dto.request.UpdateOrderStatusRequest;
import com.fiap.fastfood.order.common.dto.response.GetOrderPaymentStatusResponse;
import com.fiap.fastfood.order.common.dto.response.GetOrderResponse;
import com.fiap.fastfood.order.common.exceptions.custom.EntityNotFoundException;
import com.fiap.fastfood.order.common.interfaces.usecase.OrderUseCase;
import com.fiap.fastfood.order.core.entity.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderUseCase useCase;

    public OrderController(OrderUseCase orderUseCase) {
        this.useCase = orderUseCase;
    }

    @GetMapping
    public ResponseEntity<List<GetOrderResponse>> getOrders() {
        final var result = useCase.listOrder();

        return ResponseEntity.ok(result.stream()
                .map(OrderBuilder::fromDomainToResponse)
                .collect(Collectors.toList()));
    }

    @GetMapping(value="/{orderId}/payment-status")
    public ResponseEntity<GetOrderPaymentStatusResponse> getOrderPaymentStatus(@PathVariable String orderId) throws EntityNotFoundException {
        return ResponseEntity.ok(GetOrderPaymentStatusResponse.builder()
                .paymentStatus(useCase.getOrderById(orderId).getPaymentStatus())
                .build());
    }

    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody CreateOrderRequest request) {
        return ResponseEntity.ok(useCase.createOrder(OrderBuilder.fromRequestToDomain(request)));
    }

    @PostMapping(value="/{orderId}/perform-payment")
    public ResponseEntity<Order> performPayment(@PathVariable String orderId) throws EntityNotFoundException {
        return ResponseEntity.ok(useCase.performPayment(orderId));
    }

    @PutMapping(value = "/status")
    public ResponseEntity<Order> updateOrderStatus(@RequestBody UpdateOrderStatusRequest request) throws EntityNotFoundException {
        return ResponseEntity.ok(useCase.updateOrderStatus(request.orderId(), request.status()));
    }
}
