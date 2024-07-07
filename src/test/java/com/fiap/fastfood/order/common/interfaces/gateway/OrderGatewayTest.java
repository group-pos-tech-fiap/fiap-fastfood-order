package com.fiap.fastfood.order.common.interfaces.gateway;

import com.fiap.fastfood.order.common.builder.OrderBuilder;
import com.fiap.fastfood.order.common.exceptions.custom.EntityNotFoundException;
import com.fiap.fastfood.order.common.interfaces.datasources.SpringDataMongoOrderRepository;
import com.fiap.fastfood.order.communication.gateway.OrderGatewayImpl;
import com.fiap.fastfood.order.core.entity.Item;
import com.fiap.fastfood.order.core.entity.Order;
import com.fiap.fastfood.order.core.entity.OrderPaymentStatus;
import com.fiap.fastfood.order.core.entity.OrderStatus;
import com.fiap.fastfood.order.external.feign.PaymentClient;
import com.fiap.fastfood.order.external.feign.models.PaymentRequest;
import com.fiap.fastfood.order.external.feign.models.PaymentResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderGatewayTest {

    @Mock
    private SpringDataMongoOrderRepository repository;

    @Mock
    private PaymentClient paymentClient;

    @InjectMocks
    private OrderGatewayImpl orderGateway;

    private Order sampleOrder;

    @BeforeEach
    void setUp() {
        sampleOrder = createSampleOrder();
    }

    @Test
    void saveOrder() {
        when(repository.save(any())).thenReturn(OrderBuilder.fromDomainToOrm(sampleOrder));

        var savedOrder = orderGateway.saveOrder(sampleOrder);

        assertEquals(sampleOrder.getId(), savedOrder.getId());
        assertEquals(sampleOrder.getItems().get(0).getItemValue(), savedOrder.getItems().get(0).getItemValue());
        assertEquals(sampleOrder.getItems().get(0).getQuantity(), savedOrder.getItems().get(0).getQuantity());
        assertEquals(sampleOrder.getItems().get(0).getTotalItemValue(), savedOrder.getItems().get(0).getTotalItemValue());
        verify(repository, times(1)).save(any());
    }

    @Test
    void getOrderById() throws EntityNotFoundException {
        when(repository.findById(anyString())).thenReturn(Optional.of(OrderBuilder.fromDomainToOrm(sampleOrder)));

        var retrievedOrder = orderGateway.getOrderById(sampleOrder.getId());

        assertEquals(sampleOrder.getId(), retrievedOrder.getId());
        verify(repository, times(1)).findById(anyString());
    }

    @Test
    void getOrderById_EntityNotFoundException() {
        when(repository.findById(anyString())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> orderGateway.getOrderById(sampleOrder.getId()));
        verify(repository, times(1)).findById(anyString());
    }

    @Test
    void performPayment_Success() throws EntityNotFoundException {
        var nsuPayment = "10893741098234";
        var paymentResponse = new PaymentResponse();
        paymentResponse.setNsu(nsuPayment);
        sampleOrder.setPaymentStatus(OrderPaymentStatus.APPROVED);
        sampleOrder.setNsuPayment(nsuPayment);

        when(repository.findById(anyString())).thenReturn(Optional.of(OrderBuilder.fromDomainToOrm(sampleOrder)));
        when(paymentClient.performPayment(any(PaymentRequest.class))).thenReturn(paymentResponse);
        when(repository.save(any())).thenReturn(OrderBuilder.fromDomainToOrm(sampleOrder));

        var order = orderGateway.performPayment(sampleOrder.getId());

        assertEquals(OrderPaymentStatus.APPROVED, order.getPaymentStatus());
        verify(paymentClient, times(1)).performPayment(any(PaymentRequest.class));
        verify(repository, times(1)).save(any());
    }

    @Test
    void performPayment_Failure() throws EntityNotFoundException {
        sampleOrder.setPaymentStatus(OrderPaymentStatus.REJECTED);
        when(repository.findById(anyString())).thenReturn(Optional.of(OrderBuilder.fromDomainToOrm(sampleOrder)));
        when(repository.save(any())).thenReturn(OrderBuilder.fromDomainToOrm(sampleOrder));
        when(paymentClient.performPayment(any(PaymentRequest.class))).thenThrow(new RuntimeException());

        var paidOrder = orderGateway.performPayment(sampleOrder.getId());

        assertEquals(OrderPaymentStatus.REJECTED, paidOrder.getPaymentStatus());
        verify(paymentClient, times(1)).performPayment(any(PaymentRequest.class));
        verify(repository, times(1)).save(any());
    }

    @Test
    void listOrder() {
        var orders = List.of(sampleOrder);
        when(repository.findAll(any(Sort.class))).thenReturn(orders.stream().map(OrderBuilder::fromDomainToOrm).toList());

        List<Order> retrievedOrders = orderGateway.listOrder();

        assertEquals(orders.size(), retrievedOrders.size());
        verify(repository, times(1)).findAll(any(Sort.class));
    }

    @Test
    void testGetOrderStatusPriority() {
        assertEquals(1, orderGateway.getOrderStatusPriority(OrderStatus.READY));
        assertEquals(2, orderGateway.getOrderStatusPriority(OrderStatus.IN_PREPARATION));
        assertEquals(3, orderGateway.getOrderStatusPriority(OrderStatus.RECEIVED));
        assertEquals(0, orderGateway.getOrderStatusPriority(OrderStatus.COMPLETED));
    }

    private Order createSampleOrder() {
        Item item = new Item("1", 2, BigDecimal.valueOf(99.00));
        return Order.builder()
                .id("1")
                .items(List.of(item))
                .totalValue(item.getTotalItemValue())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .status(OrderStatus.RECEIVED)
                .paymentStatus(OrderPaymentStatus.PENDING)
                .nsuPayment("12345")
                .build();
    }
}