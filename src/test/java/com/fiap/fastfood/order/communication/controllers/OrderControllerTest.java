package com.fiap.fastfood.order.communication.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fiap.fastfood.order.common.dto.request.CreateOrderRequest;
import com.fiap.fastfood.order.common.interfaces.usecase.OrderUseCase;
import com.fiap.fastfood.order.core.entity.Item;
import com.fiap.fastfood.order.core.entity.Order;
import com.fiap.fastfood.order.core.entity.OrderPaymentStatus;
import com.fiap.fastfood.order.core.entity.OrderStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@ExtendWith(MockitoExtension.class)
class OrderControllerTest {

    @Mock
    private OrderUseCase orderUseCase;

    @InjectMocks
    private OrderController orderController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = standaloneSetup(orderController).build();
    }

    @Test
    void getOrders() throws Exception {
        var order = Order.builder()
                .items(Collections.singletonList(new Item("ABC", 20, BigDecimal.TEN)))
                .build();
        var orders = Collections.singletonList(order);

        when(orderUseCase.listOrder()).thenReturn(orders);

        mockMvc.perform(get("/orders"))
                .andExpect(status().isOk());

        verify(orderUseCase).listOrder();
    }

    @Test
    void getOrderPaymentStatus() throws Exception {
        var orderId = "123ABC";
        var order = Order.builder()
                .id(orderId)
                .paymentStatus(OrderPaymentStatus.APPROVED)
                .build();

        when(orderUseCase.getOrderById(orderId)).thenReturn(order);

        mockMvc.perform(get("/orders/{orderId}/payment-status", orderId))
                .andExpect(status().isOk());

        verify(orderUseCase).getOrderById(orderId);
    }

    @Test
    void createOrder() throws Exception {
        var order = createSampleOrder();
        var request = new CreateOrderRequest(order.getItems());

        when(orderUseCase.createOrder(any(Order.class))).thenReturn(order);

        mockMvc.perform(post("/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(orderUseCase, times(1)).createOrder(any(Order.class));
    }

    @Test
    void performPayment() throws Exception {
        var orderId = "1";
        var order = createSampleOrder();
        when(orderUseCase.performPayment(orderId)).thenReturn(order);

        mockMvc.perform(post("/orders/{orderId}/perform-payment", orderId))
                .andExpect(status().isOk());

        verify(orderUseCase, times(1)).performPayment(orderId);
    }

    private Order createSampleOrder() {
        Item item = new Item("1", 2, BigDecimal.ONE);
        return Order.builder()
                .id("1")
                .items(Collections.singletonList(item))
                .totalValue(item.getTotalItemValue())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .status(OrderStatus.RECEIVED)
                .paymentStatus(OrderPaymentStatus.PENDING)
                .nsuPayment("120374981273409")
                .build();
    }
}