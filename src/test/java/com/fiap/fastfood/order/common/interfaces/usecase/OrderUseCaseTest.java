package com.fiap.fastfood.order.common.interfaces.usecase;

import com.fiap.fastfood.order.common.exceptions.custom.EntityNotFoundException;
import com.fiap.fastfood.order.common.interfaces.gateway.OrderGateway;
import com.fiap.fastfood.order.core.entity.Item;
import com.fiap.fastfood.order.core.entity.Order;
import com.fiap.fastfood.order.core.entity.OrderPaymentStatus;
import com.fiap.fastfood.order.core.entity.OrderStatus;
import com.fiap.fastfood.order.core.usecase.OrderUseCaseImpl;
import com.fiap.fastfood.order.external.feign.PaymentClient;
import com.fiap.fastfood.order.external.feign.models.PaymentRequest;
import com.fiap.fastfood.order.external.feign.models.PaymentResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderUseCaseTest {

    @Mock
    private OrderGateway orderGateway;

    @Mock
    private PaymentClient paymentClient;

    @InjectMocks
    private OrderUseCaseImpl orderUseCase;

    private Order sampleOrder;

    @BeforeEach
    void setUp() {
        sampleOrder = createSampleOrder();
    }

    @Test
    void createOrder() {
        when(orderGateway.saveOrder(any(Order.class))).thenReturn(sampleOrder);

        var createdOrder = orderUseCase.createOrder(sampleOrder);

        assertEquals(OrderStatus.RECEIVED, createdOrder.getStatus());
        assertEquals(OrderPaymentStatus.PENDING, createdOrder.getPaymentStatus());
        verify(orderGateway, times(1)).saveOrder(any(Order.class));
    }

    @Test
    void listOrder() {
        var orders = List.of(sampleOrder);
        when(orderGateway.listOrder()).thenReturn(orders);

        List<Order> retrievedOrders = orderUseCase.listOrder();

        assertEquals(orders.size(), retrievedOrders.size());
        verify(orderGateway, times(1)).listOrder();
    }

    @Test
    void getOrderById() throws EntityNotFoundException {
        var orderId = "1";
        when(orderGateway.getOrderById(orderId)).thenReturn(sampleOrder);

        Order retrievedOrder = orderUseCase.getOrderById(orderId);

        assertEquals(sampleOrder.getId(), retrievedOrder.getId());
        verify(orderGateway, times(1)).getOrderById(orderId);
    }

    @Test
    void getOrderById_EntityNotFoundException() throws EntityNotFoundException {
        String orderId = "9080";
        when(orderGateway.getOrderById(orderId)).thenThrow(new EntityNotFoundException("ORDER-01", String.format("Order with ID %s not found", orderId)));

        assertThrows(EntityNotFoundException.class, () -> orderUseCase.getOrderById(orderId));
        verify(orderGateway, times(1)).getOrderById(orderId);
    }

    @Test
    void performPayment_Success() throws EntityNotFoundException {
        var orderId = "1010";
        var nsuPayment = "10893741098234";
        var paymentResponse = new PaymentResponse(nsuPayment);

        when(orderGateway.getOrderById(orderId)).thenReturn(sampleOrder);
        when(paymentClient.performPayment(any(PaymentRequest.class))).thenReturn(paymentResponse);
        when(orderGateway.saveOrder(any(Order.class))).thenReturn(sampleOrder);

        var order = orderUseCase.performPayment(sampleOrder.getId());

        assertEquals(OrderPaymentStatus.APPROVED, order.getPaymentStatus());
        verify(paymentClient, times(1)).performPayment(any(PaymentRequest.class));
        verify(orderGateway, times(1)).saveOrder(any(Order.class));
    }

    @Test
    void performPayment_Failure() throws EntityNotFoundException {
        var orderId = "1010";

        when(orderGateway.getOrderById(orderId)).thenReturn(sampleOrder);
        when(paymentClient.performPayment(any(PaymentRequest.class))).thenReturn(new PaymentResponse());
        when(orderGateway.saveOrder(any(Order.class))).thenReturn(sampleOrder);

        var order = orderUseCase.performPayment(sampleOrder.getId());

        assertEquals(OrderPaymentStatus.REJECTED, order.getPaymentStatus());
        verify(paymentClient, times(1)).performPayment(any(PaymentRequest.class));
        verify(orderGateway, times(1)).saveOrder(any(Order.class));
    }

    private Order createSampleOrder() {
        var item = new Item("1", 2, BigDecimal.TEN);
        return Order.builder()
                .id("1010")
                .items(List.of(item))
                .totalValue(item.getTotalItemValue())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .status(OrderStatus.RECEIVED)
                .paymentStatus(OrderPaymentStatus.PENDING)
                .nsuPayment("38924710289347")
                .build();
    }

}