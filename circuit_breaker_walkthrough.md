# Walkthrough: Circuit Breaker Test Endpoint

This document provides step-by-step instructions to verify the Resilience4j Circuit Breaker implementation.

## 1. System Components

* **Controller**: `com.belajar.task_api.presentation.web.controllers.CircuitBreakerTestController`
  Exposes the endpoint: `GET /api/v1/test-circuit-breaker?fail=true/false`
* **Configuration**: `src/main/resources/application-local.properties`
  Configures the circuit breaker named `backendA`:
  ```properties
  resilience4j.circuitbreaker.instances.backendA.sliding-window-size=5
  resilience4j.circuitbreaker.instances.backendA.failure-rate-threshold=50
  resilience4j.circuitbreaker.instances.backendA.wait-duration-in-open-state=10s
  resilience4j.circuitbreaker.instances.backendA.permitted-number-of-calls-in-half-open-state=3
  resilience4j.circuitbreaker.instances.backendA.automatic-transition-from-open-to-half-open-enabled=true
  ```

---

## 2. Verification Steps

### Step 1: Start the Application
1. Open the project in IntelliJ IDE.
2. Rebuild the project (`Build` -> `Rebuild Project`).
3. Run the application.

### Step 2: Test Success Case (CLOSED state)
Call the endpoint with `fail=false`:
```bash
curl --location 'http://localhost:9000/api/v1/test-circuit-breaker?fail=false'
```
* **Expected Response**: `200 OK`
  ```json
  {
    "success": true,
    "message": "Operation successful",
    "data": "Success response from simulated downstream API"
  }
  ```

### Step 3: Trigger Failures (Trip the Circuit)
Call the endpoint with `fail=true` to simulate downstream dependency failures. Send the request **3 times** consecutively:
```bash
curl --location 'http://localhost:9000/api/v1/test-circuit-breaker?fail=true'
```
* **Expected Response**: `503 Service Unavailable` each time
  ```json
  {
    "success": false,
    "message": "Layanan tidak tersedia (Circuit Breaker Aktif). Error: Downstream service is currently unavailable",
    "data": null
  }
  ```
* On the 3rd consecutive failure, the failure rate reaches 60% (3 out of 5 requests in the sliding window), which exceeds the 50% threshold. The circuit trips to **OPEN**.

### Step 4: Verify Fast-Failing (OPEN state)
With the circuit now **OPEN**, try calling a successful request:
```bash
curl --location 'http://localhost:9000/api/v1/test-circuit-breaker?fail=false'
```
* **Expected Behavior**: The request fails immediately with `503 Service Unavailable` without executing the controller log. In your application logs, you will see:
  ```text
  [CircuitBreaker] Fallback triggered. Reason: CallNotPermittedException: CircuitBreaker 'backendA' is OPEN and does not permit further calls
  ```

### Step 5: Test Auto-Recovery (HALF-OPEN transition)
1. Wait **10 seconds** (the configured wait duration in open state).
2. Call the success endpoint again:
   ```bash
   curl --location 'http://localhost:9000/api/v1/test-circuit-breaker?fail=false'
   ```
3. **Expected Behavior**: The request succeeds (`200 OK`) because the circuit transitioned to **HALF-OPEN** and permitted the request to pass to verify health. Once the health is verified, the circuit transitions back to **CLOSED**.
