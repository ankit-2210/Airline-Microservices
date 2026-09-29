# ✈️ Airline Portal - Microservices

A **Spring Boot Microservices-based Airline Management System** built using Java, Spring Boot, Spring Cloud, JPA, MySQL, JWT, OpenFeign, Resilience4j, JasperReports, Elasticsearch, and React-based client applications.

---

## 📁 Project Structure

```text
airlineportal/
│
├── services/
│   │
│   ├── UserService/
│   │   ├── src/main/java/
│   │   │   └── com.airlineportal/
│   │   │       ├── client/
│   │   │       ├── config/
│   │   │       ├── controller/
│   │   │       │   ├── AuthController.java
│   │   │       │   ├── UserController.java
│   │   │       │   ├── InternalUserController.java
│   │   │       │   └── UserReportController.java
│   │   │       ├── dto/
│   │   │       ├── exception/
│   │   │       ├── helper/
│   │   │       ├── mapper/
│   │   │       ├── model/
│   │   │       │   └── User.java
│   │   │       ├── repository/
│   │   │       ├── security/
│   │   │       │   └── jwt/
│   │   │       │       └── JwtUtils.java
│   │   │       └── service/
│   │   │           └── impl/
│   │   │
│   │   └── src/main/resources/
│   │       ├── reports/
│   │       │   └── users.jrxml
│   │       └── application.properties
│   │
│   │
│   ├── LocationService/
│   │   ├── src/main/java/
│   │   │   └── com.locationservice/
│   │   │       ├── controller/
│   │   │       ├── dto/
│   │   │       ├── exception/
│   │   │       ├── helper/
│   │   │       ├── mapper/
│   │   │       ├── model/
│   │   │       │   ├── Airport.java
│   │   │       │   └── City.java
│   │   │       ├── repository/
│   │   │       └── service/
│   │   │           └── impl/
│   │   │
│   │   └── src/main/resources/
│   │       ├── reports/
│   │       │   ├── airports.jrxml
│   │       │   └── cities.jrxml
│   │       └── application.properties
│   │
│   │
│   ├── AirlineService/
│   │   ├── src/main/java/
│   │   │   └── com.airlineservice/
│   │   │       ├── client/
│   │   │       ├── controller/
│   │   │       ├── dto/
│   │   │       ├── exception/
│   │   │       ├── external/
│   │   │       ├── helper/
│   │   │       ├── mapper/
│   │   │       ├── model/
│   │   │       │   ├── Airline.java
│   │   │       │   └── Aircraft.java
│   │   │       ├── repository/
│   │   │       └── service/
│   │   │           └── impl/
│   │   │
│   │   └── src/main/resources/
│   │       ├── reports/
│   │       │   ├── airlines.jrxml
│   │       │   └── aircrafts.jrxml
│   │       └── application.properties
│   │
│   │
│   ├── FlightService/
│   │   ├── src/main/java/
│   │   │   └── com.flightservice/
│   │   │       ├── client/
│   │   │       ├── controller/
│   │   │       │   ├── FlightController.java
│   │   │       │   ├── FlightScheduleController.java
│   │   │       │   ├── FlightInstanceController.java
│   │   │       │   ├── FlightSearchController.java
│   │   │       │   └── FlightReportController.java
│   │   │       ├── dto/
│   │   │       ├── exception/
│   │   │       ├── external/
│   │   │       ├── helper/
│   │   │       ├── mapper/
│   │   │       ├── model/
│   │   │       │   ├── Flight.java
│   │   │       │   ├── FlightSchedule.java
│   │   │       │   └── FlightInstance.java
│   │   │       ├── repository/
│   │   │       └── service/
│   │   │           └── impl/
│   │   │
│   │   └── src/main/resources/
│   │       ├── reports/
│   │       │   ├── flights.jrxml
│   │       │   ├── flight_schedules.jrxml
│   │       │   └── flight_instances.jrxml
│   │       └── application.properties
│   │
│   │
│   ├── BookingService/
│   │   ├── src/main/java/
│   │   │   └── com.bookingservice/
│   │   │       ├── controller/
│   │   │       ├── dto/
│   │   │       │   ├── request/
│   │   │       │   └── response/
│   │   │       ├── exception/
│   │   │       ├── external/
│   │   │       │   ├── ExternalService.java
│   │   │       │   └── ExternalServiceImpl.java
│   │   │       ├── helper/
│   │   │       │   └── BookingHelper.java
│   │   │       ├── mapper/
│   │   │       ├── model/
│   │   │       │   ├── Booking.java
│   │   │       │   └── Passenger.java
│   │   │       ├── repository/
│   │   │       └── service/
│   │   │           └── impl/
│   │   │
│   │   └── src/main/resources/
│   │       └── application.properties
│   │
│   │
│   └── PaymentService/
│       └── ...
│
│
├── common/
│   └── src/main/java/
│       └── com.airlineportal/
│           ├── client/
│           │   ├── UserFeignClient.java
│           │   ├── LocationFeignClient.java
│           │   ├── AirlineFeignClient.java
│           │   ├── AircraftFeignClient.java
│           │   ├── FlightFeignClient.java
│           │   └── FlightInstanceFeignClient.java
│           │
│           ├── dto/
│           │
│           ├── exception/
│           │
│           ├── payload/
│           │   ├── request/
│           │   └── response/
│           │
│           ├── security/
│           │   └── jwt/
│           │       └── JwtUtils.java
│           │
│           └── utils/
│               ├── Airline/
│               ├── Aircraft/
│               ├── Booking/
│               └── Flight/
│
│
├── ApiGateway/
│   └── src/
│       └── main/
│           ├── java/
│           │   └── com.apigateway/
│           └── resources/
│               └── application.properties
│
│
├── EurekaServer/
│   └── src/
│       └── main/
│           ├── java/
│           │   └── com.eureka/
│           └── resources/
│               └── application.properties
│
│
├── docker/
│   └── ...
│
├── pom.xml
└── README.md
```

---

## 🧩 Microservices

### 1. User Service

Handles user management and authentication.

**Responsibilities:**

* User registration and login
* JWT authentication
* Role management
* User profile management
* Internal user APIs for other microservices
* JasperReports-based user reports

```text
User
 ├── Authentication
 ├── Authorization
 ├── JWT
 ├── Roles
 ├── User Management
 └── User Reports
```

---

### 2. Location Service

Manages geographical and airport information.

```text
Location
 ├── City
 │   ├── Name
 │   ├── City Code
 │   ├── Country
 │   └── Region
 │
 └── Airport
     ├── IATA Code
     ├── Name
     ├── Address
     ├── GeoCode
     ├── Timezone
     └── City
```

**Reports:**

* Airport Management Report
* City Management Report

---

### 3. Airline Service

Manages airlines and aircraft.

```text
Airline
 ├── Airline Information
 ├── IATA / ICAO
 ├── Country
 ├── Alliance
 ├── Headquarters
 └── Aircraft

Aircraft
 ├── Aircraft Code
 ├── Model
 ├── Manufacturer
 ├── Seating Configuration
 ├── Range
 ├── Cruising Speed
 ├── Maintenance
 ├── Status
 └── Current Airport
```

**Reports:**

* Airline Management Report
* Aircraft Management Report

---

### 4. Flight Service

Handles flight operations and scheduling.

```text
Flight
 ├── Flight
 │   ├── Flight Number
 │   ├── Airline
 │   ├── Aircraft
 │   ├── Departure Airport
 │   ├── Arrival Airport
 │   ├── Scheduled Departure
 │   ├── Scheduled Arrival
 │   └── Flight Status
 │
 ├── Flight Schedule
 │   ├── Departure Time
 │   ├── Arrival Time
 │   ├── Start Date
 │   ├── End Date
 │   └── Operating Days
 │
 └── Flight Instance
     ├── Departure DateTime
     ├── Arrival DateTime
     ├── Total Seats
     ├── Available Seats
     ├── Booking Window
     ├── Flight Status
     └── Active
```

**Additional functionality:**

* Flight search
* Route-based search
* Date-based search
* Upcoming flights
* Flight status management
* Elasticsearch-based flight searching
* Seat availability management
* Atomic seat reservation/release

**Reports:**

* Flight Management Report
* Flight Schedule Report
* Flight Instance Report

---

### 5. Booking Service

Handles passenger bookings.

```text
Booking
 ├── PNR
 ├── User
 ├── Flight
 ├── Flight Instance
 ├── Total Amount
 ├── Booking Status
 ├── Payment Status
 ├── Booking Time
 ├── Confirmation Time
 └── Cancellation Information

Passenger
 ├── First Name
 ├── Last Name
 ├── Gender
 ├── Passport Number
 └── Seat Number
```

**Current responsibilities:**

* Create booking
* Generate unique PNR
* Validate passengers
* Verify users through UserService
* Verify flights through FlightService
* Verify FlightInstances through FlightService
* Reserve seats
* Release seats on cancellation
* Retrieve booking by ID / PNR
* Retrieve bookings by user / flight / flight instance
* Cancel booking

---

### 6. Payment Service

Planned service for handling booking payments.

```text
Payment
 ├── Booking
 ├── Amount
 ├── Payment Status
 ├── Transaction
 └── Payment Gateway
```

---

## 🌐 API Gateway

The API Gateway acts as the single entry point to the microservices.

```text
Client
   ↓
API Gateway : 5000
   ↓
 ┌─────────────────────────────────────┐
 │ User Service                        │
 │ Location Service                    │
 │ Airline Service                     │
 │ Flight Service                      │
 │ Booking Service                     │
 │ Payment Service                     │
 └─────────────────────────────────────┘
```

Responsibilities:

* Request routing
* Service discovery
* Central entry point
* Swagger aggregation
* Authentication-related gateway processing

---

## 🔎 Eureka Service Discovery

Eureka Server provides service registration and discovery.

```text
                 Eureka Server
                     :8761
                       │
        ┌──────────────┼──────────────┐
        ↓              ↓              ↓
   User Service   Flight Service   Booking Service
        ↓              ↓              ↓
 Location Service  Airline Service  Payment Service
```

---

## 🔐 Security

The project uses **JWT-based authentication**.

```text
Login
  ↓
User Service
  ↓
JWT Token
  ↓
Client
  ↓
Authorization: Bearer <token>
  ↓
API Gateway / Services
```

JWT contains role information as a list:

```json
{
  "sub": "user@example.com",
  "roles": [
    "ROLE_USER"
  ]
}
```

Common JWT functionality is maintained in the shared module.

---

## 🔗 Inter-Service Communication

Microservices communicate using **OpenFeign**.

```text
BookingService
     │
     ├── UserFeignClient
     │       ↓
     │   UserService
     │
     ├── FlightFeignClient
     │       ↓
     │   FlightService
     │
     └── FlightInstanceFeignClient
             ↓
         FlightService
```

External service calls are abstracted through:

```text
ServiceImpl
    ↓
ExternalService
    ↓
Feign Client
    ↓
Remote Microservice
```

---

## 🛡️ Resilience

Inter-service communication uses **Resilience4j**.

```text
Feign Request
     ↓
   Retry
     ↓
Circuit Breaker
     ↓
Remote Service
     ↓
Fallback
```

Configured patterns include:

* Retry
* Circuit Breaker
* Fallback methods

---

## 📊 JasperReports

The project uses **JasperReports** for PDF report generation.

```text
Database
    ↓
Repository
    ↓
Entity
    ↓
Report DTO
    ↓
Mapper
    ↓
JRBeanCollectionDataSource
    ↓
JRXML
    ↓
PDF
```

Implemented report areas:

```text
UserService
 └── Users Report

LocationService
 ├── Airports Report
 └── Cities Report

AirlineService
 ├── Airlines Report
 └── Aircraft Report

FlightService
 ├── Flights Report
 ├── Flight Schedules Report
 └── Flight Instances Report
```

---

## 🔍 Elasticsearch

Elasticsearch is used for flight searching.

```text
Client
  ↓
FlightSearchController
  ↓
Elasticsearch Repository
  ↓
Elasticsearch
```

The Elasticsearch search endpoint is separated from the JPA flight search endpoint:

```text
JPA Search
GET /api/flights/search

Elasticsearch Search
GET /api/flights/elasticsearch/search
```

---

## 🏗️ Common Service Architecture

Each business service follows a similar layered architecture:

```text
Controller
    ↓
Service
    ↓
Helper
    ↓
Repository

External Service Communication
    ↓
ExternalService
    ↓
Feign Client
```

### Responsibilities

```text
Controller
    → Handles HTTP requests/responses

Service
    → Contains application/business orchestration

Helper
    → Validation, lookup and business-support logic

Mapper
    → Entity ↔ DTO conversion

Repository
    → Database operations

ExternalService
    → Abstraction for inter-service communication

Feign Client
    → Remote service communication

Exception Handler
    → Centralized error handling
```

---

## 🧰 Technology Stack

| Technology           | Usage                          |
| -------------------- | ------------------------------ |
| Java                 | Backend development            |
| Spring Boot          | Microservices                  |
| Spring Data JPA      | Persistence                    |
| Hibernate            | ORM                            |
| MySQL                | Database                       |
| Spring Cloud         | Microservices ecosystem        |
| Eureka               | Service discovery              |
| Spring Cloud Gateway | API Gateway                    |
| OpenFeign            | Inter-service communication    |
| Resilience4j         | Retry & Circuit Breaker        |
| Spring Security      | Authentication & Authorization |
| JWT                  | Token-based authentication     |
| JasperReports        | PDF reporting                  |
| Elasticsearch        | Flight search                  |
| Maven                | Build & dependency management  |
| Lombok               | Boilerplate reduction          |
| Bean Validation      | Request validation             |
| Postman              | API testing                    |

---

## 🔄 High-Level System Architecture

```text
                         ┌─────────────────┐
                         │     Client      │
                         └────────┬────────┘
                                  │
                                  ↓
                         ┌─────────────────┐
                         │   API Gateway   │
                         │      :5000      │
                         └────────┬────────┘
                                  │
                    ┌─────────────┼─────────────┐
                    ↓             ↓             ↓
              User Service   Location Service  Airline Service
                    │             │             │
                    │             │             └──── Aircraft
                    │             │
                    │             └──── Airport / City
                    │
                    └──── JWT / Users

                                  │
                                  ↓
                           ┌──────────────┐
                           │ FlightService│
                           └──────┬───────┘
                                  │
                   ┌──────────────┼──────────────┐
                   ↓              ↓              ↓
                Flight        Schedule       Instance
                                                  │
                                                  ↓
                                            Seat Availability

                                  │
                                  ↓
                           ┌──────────────┐
                           │BookingService│
                           └──────┬───────┘
                                  │
                                  ↓
                              Passenger
                                  │
                                  ↓
                           Payment Service
```

---

## 📌 Current Development Status

### Implemented / Developed

* User Service
* JWT authentication
* Role-based authorization
* Location Service
* City and Airport management
* Airline management
* Aircraft management
* Flight management
* Flight schedules
* Flight instances
* Flight search
* Elasticsearch integration
* Inter-service communication with OpenFeign
* Resilience4j Retry / Circuit Breaker
* JasperReports PDF generation
* Booking and Passenger domain
* Booking validation architecture
* FlightInstance seat reservation/release architecture

### In Progress

* Complete BookingService integration
* PaymentService
* End-to-end booking + payment flow
* Additional production-level validations and transactional improvements

---

## 🚀 Overall Architecture

```text
                    ┌──────────────────────┐
                    │      API Gateway     │
                    └──────────┬───────────┘
                               │
        ┌──────────────────────┼────────────────────────┐
        │                      │                        │
        ↓                      ↓                        ↓
   User Service         Location Service         Airline Service
        │                      │                        │
        │                      │                        └── Aircraft
        │                      └── Airport / City
        │
        └── JWT

                               ↓
                        Flight Service
                               │
                 ┌─────────────┼─────────────┐
                 ↓             ↓             ↓
              Flight       Schedule       Instance
                                             │
                                             ↓
                                        Seat Management
                                             │
                                             ↓
                                      Booking Service
                                             │
                                             ↓
                                      Payment Service
```
