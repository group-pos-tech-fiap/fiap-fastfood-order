package com.fiap.fastfood.order;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@EnableFeignClients
@EnableMongoAuditing
@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "Fast Food ORDER - FIAP", description = "Microservico responsavel pelos pedidos do cliente", version = "v1"))
public class FiapFastFoodOrderApplication {

    public static void main(String[] args) {
        SpringApplication.run(FiapFastFoodOrderApplication.class, args);
    }

}
