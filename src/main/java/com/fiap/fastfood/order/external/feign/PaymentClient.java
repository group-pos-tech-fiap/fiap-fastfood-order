package com.fiap.fastfood.order.external.feign;

import com.fiap.fastfood.order.external.feign.models.PaymentRequest;
import com.fiap.fastfood.order.external.feign.models.PaymentResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Component
@FeignClient(name = "PaymentClient", url = "${spring.cloud.openfeign.client.payment.uri}")
public interface PaymentClient {

    @PostMapping(value="/perform-payment")
    PaymentResponse performPayment(@RequestBody PaymentRequest paymentRequest);

}
