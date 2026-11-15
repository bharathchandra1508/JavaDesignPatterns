# Java Design Patterns

- [Strategy Pattern](#Strategy Pattern)

# Strategy Pattern

A small Spring Boot service that demonstrates the **Strategy pattern** for payment processing. Each payment method is its own class, and the right one is chosen at runtime from the `paymentType` field of the request. Adding a new payment method means adding one class, with no changes to existing code.

---

## Table of Contents

- [Project Structure](#project-structure)
- [How It Works](#how-it-works)
- [Startup Flow](#startup-flow)
- [Request Flow](#request-flow)
- [Running the Application](#running-the-application)
- [API Reference](#api-reference)
- [Adding a New Payment Method](#adding-a-new-payment-method)

---

## Project Structure

```
src/main/java/com/designpatterns/samples/strategypattern/
├── JavaDesignPatternsApplication.java    # Spring Boot entry point
├── api/
│   ├── PaymentController.java            # REST endpoint: POST /payments
│   ├── PaymentRequest.java               # Request body (record)
│   └── GlobalExceptionHandler.java       # Maps errors to HTTP responses
├── service/
│   └── PaymentService.java               # Orchestrates a payment
├── strategy/
│   ├── PaymentStrategy.java              # Strategy interface
│   └── PaymentStrategyResolver.java      # Finds a strategy by type
└── strategies/
    ├── ApplePayPayment.java              # type = "applepay"
    ├── UpiPayment.java                   # type = "upi"
    └── ...                               # one class per payment method
```

---

## How It Works

Three roles make up the pattern:

| Role | Class | Responsibility |
|------|-------|----------------|
| Strategy interface | `PaymentStrategy` | Defines `type()` and `pay(amount)` |
| Concrete strategies | `ApplePayPayment`, `UpiPayment`, ... | One implementation per payment method |
| Resolver | `PaymentStrategyResolver` | Maps a type string to its strategy |

Spring collects every `@Component` that implements `PaymentStrategy` and passes them into the resolver. The resolver builds a `Map<String, PaymentStrategy>` once at startup, so each request is a constant-time lookup.

---

## Startup Flow

What happens from `main()` until the server accepts requests:

```
 java -jar / IDE Run
        │
        ▼
┌─────────────────────────────────────┐
│ JavaDesignPatternsApplication.main  │
│   SpringApplication.run(...)        │
└──────────────┬──────────────────────┘
               │
               ▼
┌─────────────────────────────────────┐
│ @SpringBootApplication              │
│   enables component scanning from   │
│   the root package downward         │
└──────────────┬──────────────────────┘
               │
               ▼
┌─────────────────────────────────────┐
│ Component scan finds:               │
│   @Component  ApplePayPayment       │
│   @Component  UpiPayment            │
│   @Component  ...  (all strategies) │
│   @Service    PaymentService        │
│   @Component  PaymentStrategyResolver│
│   @RestController PaymentController │
│   @RestControllerAdvice GlobalExceptionHandler │
└──────────────┬──────────────────────┘
               │
               ▼
┌─────────────────────────────────────┐
│ Bean creation (dependency injection)│
│   1. Create every PaymentStrategy   │
│      bean (no dependencies)         │
│   2. Create PaymentStrategyResolver │
│      and inject List<PaymentStrategy>│
│   3. Create PaymentService and      │
│      inject the resolver            │
│   4. Create PaymentController and   │
│      inject the service             │
└──────────────┬──────────────────────┘
               │
               ▼
┌─────────────────────────────────────┐
│ Resolver constructor builds the map │
│   "applepay" → ApplePayPayment      │
│   "upi"      → UpiPayment           │
│   ...                               │
│   Duplicate type() values fail here │
└──────────────┬──────────────────────┘
               │
               ▼
┌─────────────────────────────────────┐
│ Embedded Tomcat starts on port 8080 │
│ Ready to accept requests            │
└─────────────────────────────────────┘
```

**Key point:** the map is built once. If two strategies return the same `type()`, `Collectors.toUnmodifiableMap` throws during startup, so the app never runs with an ambiguous configuration.

---

## Request Flow

What happens when a client calls `POST /payments`:

```
 Client
   │  POST /payments
   │  {"paymentType":"applepay","amount":1499}
   ▼
┌─────────────────────────────────────┐
│ Tomcat → DispatcherServlet          │
│   matches URL and HTTP method       │
└──────────────┬──────────────────────┘
               │
               ▼
┌─────────────────────────────────────┐
│ Jackson deserializes JSON into      │
│ PaymentRequest(paymentType, amount) │
└──────────────┬──────────────────────┘
               │
               ▼
┌─────────────────────────────────────┐
│ PaymentController.pay(request)      │
│   calls paymentService.process()    │
└──────────────┬──────────────────────┘
               │
               ▼
┌─────────────────────────────────────┐
│ PaymentService.process(request)     │
│   strategy = resolver.resolve(type) │
└──────────────┬──────────────────────┘
               │
               ▼
┌─────────────────────────────────────┐
│ PaymentStrategyResolver.resolve()   │
│   map.get("applepay")               │
│   found  → return ApplePayPayment   │
│   absent → throw IllegalArgument-   │
│            Exception                │
└──────────────┬──────────────────────┘
               │
       ┌───────┴────────┐
       │ found          │ not found
       ▼                ▼
┌──────────────────┐  ┌─────────────────────────────┐
│ strategy.pay()   │  │ GlobalExceptionHandler      │
│ prints the       │  │ returns 400 Bad Request     │
│ payment          │  │ {"error":"Unsupported       │
└────────┬─────────┘  │  payment type: ..."}        │
         │            └──────────────┬──────────────┘
         ▼                           │
┌──────────────────┐                 │
│ 200 OK           │                 │
└────────┬─────────┘                 │
         │                           │
         └─────────────┬─────────────┘
                       ▼
                   Client receives response
```

---

## Running the Application

**Prerequisites:** JDK 17+, Maven 3.8+ (or the IntelliJ bundled Maven).

From IntelliJ:
1. Right-click `pom.xml` → **Maven → Reload Project**.
2. Run `JavaDesignPatternsApplication`.

From the command line:

```bash
mvn spring-boot:run
```

---

## API Reference

### `POST /payments`

**Request body**

| Field | Type | Required | Example |
|-------|------|----------|---------|
| `paymentType` | string | yes | `"applepay"` |
| `amount` | number | yes | `1499` |

**Responses**

| Status | When | Body |
|--------|------|------|
| `200 OK` | Payment type is supported | empty |
| `400 Bad Request` | Unknown `paymentType` | `{"error": "Unsupported payment type: bitcoin"}` |

**Example**

```bash
curl -X POST http://localhost:8080/payments \
  -H "Content-Type: application/json" \
  -d '{"paymentType":"applepay","amount":1499}'
```

Console output:

```
Paid 1499.00 via Apple Pay
```

---

## Adding a New Payment Method

1. Create a class in `strategies/` annotated with `@Component`.
2. Implement `PaymentStrategy`, returning a unique `type()` value.
3. Implement `pay()`.

No other file changes. Spring registers the new bean and the resolver picks it up at startup.

```java
@Component
public class WalletPayment implements PaymentStrategy {

    @Override
    public String type() {
        return "wallet";
    }

    @Override
    public void pay(double amount) {
        System.out.printf("Paid %.2f via Wallet%n", amount);
    }
}
```