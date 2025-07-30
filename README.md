# 🐾 PetShop-API & E-commerce Platform

![Java](https://img.shields.io/badge/Java-17-blue)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.x-brightgreen)
![JavaScript](https://img.shields.io/badge/JavaScript-ES6+-yellow)
![HTML5](https://img.shields.io/badge/HTML-5-orange)
![CSS3](https://img.shields.io/badge/CSS-3-blueviolet)

---

# EN - US

## 📖 About the Project

This project is a **full-stack solution** for managing a PetShop, developed as a portfolio item to demonstrate and practice skills in **back-end architecture using Java/Spring Boot** and interactive **front-end development with plain JavaScript**.

The application simulates a real e-commerce environment, with features ranging from product and service listing to a complete and secure payment flow, integrated with the **Mercado Pago API**. The system also implements a robust **role-based access control (RBAC)** to differentiate actions between Customers and Employees.

---

## ✨ Main Features

- **Authentication & Security:** Full login system with **JWT (JSON Web Token)** authentication and authorization managed by **Spring Security**.

- **Role-Based Access Control (RBAC):**
  - **Customers:** Can view products, services and make purchases.
  - **Employees (EMPLOYEE):** Access to admin functionalities (product registration, view sales, etc.).

- **Dynamic Catalog:** Front-end that consumes the API to dynamically list products and services.

- **Shopping Cart:** Complete shopping cart logic implemented on the front-end using **pure JavaScript**, allowing items to be added, viewed, and managed.

- **Complete Payment Flow:** End-to-end integration with **Mercado Pago**, from creating the payment preference to redirecting the customer.

- **Asynchronous Payment Confirmation:** Use of **Webhooks** to receive notifications from Mercado Pago, update the status in the database, and automatically reduce stock.

- **Robust RESTful API:** Back-end built following REST API design principles.

---

## 🛠️ Technologies Used

### 🖥️ Back-end
- Java 21  
- Spring Boot 3  
- Spring Security (authentication and authorization with JWT)  
- Spring Data JPA & Hibernate (data persistence)  
- Maven (dependency management)  
- H2 Database (for development environment) *(Postgres in the future)*  
- Mercado Pago SDK for Java  

### 🎨 Front-end
- HTML5  
- CSS3 (using Flexbox and CSS Grid for responsive layouts)  
- JavaScript (ES6+) – Vanilla JS for DOM manipulation and interactivity  

### 🧰 Development Tools
- IntelliJ IDEA  
- Postman (for API testing)  
- localtunnel (for testing webhooks in local environment)  
- Git & GitHub  

---

## 🚀 Getting Started

Follow the instructions below to run the project in your local environment.

### ✅ Prerequisites

You’ll need the following tools installed:

- JDK 17 or higher  
- Apache Maven  
- Git  

---

### ⚙️ Installation and Execution

**Clone the repository:**

```bash
git clone https://github.com/lucaslp25/PetShop-API.git
cd PetShop-API
```

### Configure the Back-end:

* -> Navigate to the Spring Boot root project.*

*Open the application.properties (and test version) and fill it with your own information — mainly your Mercado Pago AccessToken and the base URL for testing.*

#### Run the Spring Boot Application

The front-end is served directly by the Spring Boot application. Open your browser and access:

http://localhost:8080  <--

*🔐 Environment Configuration*
To make the application work, you need to configure the following properties in your application.properties file:

→ I configured the properties to allow choosing between environment variables or fallback defaults.

→ You will need to configure your Mercado Pago token by accessing their API and placing the token in the property like this:
mercadopago.access_token=${TOKEN_MERCADO_PAGO:token-only-execute}

→ Again, I left a default token for execution, but you must replace these tokens for Mercado Pago to work.

*Front-end base URL for payment redirection (use localtunnel to generate the URL and test):*
app.base-url=https://your-tunnel-url.loca.lt

✒️ Author
Lucas Lopes

💼 LinkedIn -> https://www.linkedin.com/in/lucas-lopes-lp-devskt2025/

💻 GitHub -> https://github.com/lucaslp25

---

# PT - BR


## 📖 Sobre o Projeto

Este projeto é uma solução **full-stack completa** para a gestão de um PetShop, desenvolvida como um item de portfólio para demonstrar e treinar habilidades em arquitetura de **back-end com Java/Spring Boot** e desenvolvimento de **front-end interativo com JavaScript puro**.

A aplicação simula um ambiente de e-commerce real, com funcionalidades que vão desde a listagem de produtos e serviços até um fluxo de pagamento completo e seguro, integrado com a API do **Mercado Pago**. O sistema também implementa um robusto **controle de acesso baseado em papéis (RBAC)** para diferenciar as ações de Clientes e Funcionários.

---

## ✨ Features Principais

- **Autenticação e Segurança:** Sistema de login completo com autenticação via **JWT** (JSON Web Tokens) e autorização gerenciada pelo **Spring Security**.

- **Controle de Acesso por Papel (RBAC):**
  - **Clientes:** Podem ver produtos, serviços e realizar compras.
  - **Funcionários (EMPLOYEE):** Acesso a funcionalidades administrativas (cadastro de produtos, visualização de vendas, etc.).

- **Catálogo Dinâmico:** Front-end que consome a API para listar produtos e serviços de forma dinâmica.

- **Carrinho de Compras:** Lógica completa de carrinho implementada no front-end com **JavaScript puro**, permitindo adicionar, visualizar e gerenciar itens.

- **Fluxo de Pagamento Completo:** Integração ponta a ponta com o **Mercado Pago**, desde a criação da preferência de pagamento até o redirecionamento do cliente.

- **Confirmação de Pagamento Assíncrona:** Uso de **Webhooks** para receber notificações do Mercado Pago, atualizar o status no banco de dados e dar baixa no estoque automaticamente.

- **API RESTful Robusta:** Back-end construído seguindo os princípios de design de APIs REST.

---

## 🛠️ Tecnologias Utilizadas

### 🖥️ Back-end
- Java 21
- Spring Boot 3  
- Spring Security (autenticação e autorização com JWT)  
- Spring Data JPA & Hibernate (persistência de dados)  
- Maven (gerenciamento de dependências)  
- Banco de Dados H2 (ambiente de desenvolvimento) (Futuramente o postgres)  
- Mercado Pago SDK for Java  

### 🎨 Front-end
- HTML5  
- CSS3 (com Flexbox e CSS Grid para layouts responsivos)  
- JavaScript (ES6+) – Vanilla JS para manipulação do DOM e interatividade  

### 🧰 Ferramentas de Desenvolvimento
- IntelliJ IDEA  
- Postman (para testes de API)  
- localtunnel (para testes de webhook em ambiente local)  
- Git & GitHub  

---

## 🚀 Começando

Siga as instruções abaixo para executar o projeto em seu ambiente local.

### ✅ Pré-requisitos

Você vai precisar ter as seguintes ferramentas instaladas:

- JDK 17 ou superior  
- Apache Maven  
- Git  

---

### ⚙️ Instalação e Execução

**Clone o repositório:**

```bash
git clone https://github.com/lucaslp25/PetShop-API.git
cd PetShop-API
```

**Configure o Back-end:**

Navegue até a raiz do projeto Spring Boot.

Abra o application.properties (e de test) e preencha com suas informações, principalmente seu AccessToken do Mercado Pago e a URL base para testes.

*Execute a Aplicação Spring Boot*

O front-end é servido diretamente pela aplicação Spring Boot. Abra seu navegador e acesse:

http://localhost:8080

🔐 Configuração do Ambiente

Para que a aplicação funcione, você precisa configurar as seguintes propriedades no seu arquivo application.properties:

-> Coloquei a opção na properties de ou escolher a variável de ambiente, ou escolher uma pré-definida

-> Você terá que configurar o token do mercado pago, acessando a API deles e coloando o token nas properties onde diz: mercadopago.access_token=${TOKEN_MERCADO_PAGO:token-only-execute}

-> Novamente coloquei meu token como variável de ambiente e deixei outro apenas para rodar: (NÃO FUNCIONARA O MERCADO PAGO SE NÃO SUBSTITUIR OS TOKENS QUE ESTÃO ALI)

*URL base do front-end para os redirects de pagamento (use o localtunnel para gerar URL e testes)*
app.base-url=https://seu-tunnel-url.loca.lt

✒️ Autor
Lucas Lopes

💼 LinkedIn -> https://www.linkedin.com/in/lucas-lopes-lp-devskt2025/

💻 GitHub -> https://github.com/lucaslp25

