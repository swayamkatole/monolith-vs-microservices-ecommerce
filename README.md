# Monolith vs Microservices: E-Commerce Performance Study

This repository contains the source code and k6 load-testing scripts for an empirical comparison of monolithic and microservices architectures for an e-commerce application.

📄 **Research paper:** [An Empirical Comparison of Monolithic and Microservices Architectures for an E-Commerce Application](https://arxiv.org/abs/2608.15668)  
📌 **arXiv:** [arXiv:2608.15668 [cs.SE]](https://arxiv.org/abs/2608.15668)

## Research paper

**Title:** An Empirical Comparison of Monolithic and Microservices Architectures for an E-Commerce Application

**Author:** Swayam Amol Katole

**Status:** Publicly available as an arXiv preprint.

This study evaluates equivalent monolithic and microservices implementations under HTTP load at 50 and 100 virtual users (VUs). The measurements include throughput, average latency, p95 latency, and error rate.

## Technology stack

- Java 21
- Spring Boot
- Maven
- PostgreSQL
- k6

## Repository structure

```text
.
├── monolith/                    # Single Spring Boot application
├── microservices/               # Independent service implementations
│   ├── catalog-service/
│   ├── order-service/
│   └── cart-service/
├── load-tests/                  # k6 load-test scripts
├── monolith-load.js             # Monolithic-system test entry point
└── microservices-load.js        # Microservices-system test entry point
```

## Architecture

### Monolithic application

The `monolith/` directory contains one Spring Boot application providing:

- Product catalog operations
- Order creation and retrieval
- Shopping-cart operations

### Microservices application

The `microservices/` directory separates the application into three independently runnable Spring Boot services:

- `catalog-service` — manages product data
- `order-service` — manages orders
- `cart-service` — manages shopping carts

All implementations are designed to use PostgreSQL.

## Running locally

### Prerequisites

Install:

- Java 21
- Maven
- PostgreSQL
- k6

### Configure the database

Create a PostgreSQL database and configure each application or service with local credentials.

Do not commit passwords, API keys, `.env` files, or local configuration files to GitHub. Use a local `application.properties` file or environment variables.

### Run the monolith

```bash
cd monolith
./mvnw spring-boot:run
```

On Windows PowerShell:

```powershell
cd monolith
.\mvnw.cmd spring-boot:run
```

### Run the microservices

Open a separate terminal for each service:

```bash
cd microservices/catalog-service
mvn spring-boot:run
```

```bash
cd microservices/order-service
mvn spring-boot:run
```

```bash
cd microservices/cart-service
mvn spring-boot:run
```

## Load testing

Run a monolith test:

```bash
k6 run monolith-load.js
```

Run a microservices test:

```bash
k6 run microservices-load.js
```

The test scenarios exercise product-catalog retrieval, product-detail retrieval, order creation, cart updates, and cart retrieval.

## Results summary

At 50 VUs, both architectures completed the benchmark without errors.

At 100 VUs, the microservices implementation achieved:

- 5.4% higher throughput
- 25% lower average latency
- 39% lower p95 latency
- A lower median error rate than the monolithic implementation

Results depend on the workload, machine configuration, JVM settings, database configuration, and deployment environment.

## Reproducibility

To reproduce or extend the study:

1. Configure PostgreSQL for the monolith and all three microservices.
2. Start the architecture being tested.
3. Run the appropriate k6 script at the desired virtual-user level.
4. Repeat each configuration multiple times.
5. Compare median throughput, latency, and error-rate measurements.

## Citation

If you use this repository, please cite:

```text
Swayam Amol Katole. An Empirical Comparison of Monolithic and Microservices Architectures for an E-Commerce Application. arXiv:2608.15668 [cs.SE], 2026.
```

Paper: [https://arxiv.org/abs/2608.15668](https://arxiv.org/abs/2608.15668)

## License

This project is released under the MIT License. See [LICENSE](LICENSE).
