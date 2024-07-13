
# 🚀 FIAP : Challenge Pós-Tech Software Architecture
## 🍔 Microserviço fiap-fastfood-order 

Microserviço responsável pelos pedidos dos clientes, faz parte da Fase 4 da Pós-Graduação de Arquitetura de Sistemas da FIAP.

### 👨‍🏫 Grupo

Integrantes:
- Diego S. Silveira (RM352891)
- Kelvin Vieira (RM352728)
- Wellington Vieira (RM352970)

### 💻 Tecnologias

Tecnologias utilizadas:

* Java 17
* Spring Framework
* Gradle
* MongoDB
* Docker
* Swagger
* Cloud AWS
* Kubernetes

### 👓 Serviços Utilizados

* Github
* Postman
* Docker Desktop
* MongoDB Compass
* k9s
* Minikube
* AWS CLI

### 🔌 Integracao dos microserviçoes
A integração dos microservicos é realizada através do FeignClient.

    > Pagamento/Payment: com.fiap.fastfood.order.external.feign.PaymentClient

### 💿 Cobertura de teste
![](misc/test-coverage.png)

## Version

1.0.0.0