✈️ Airline Portal - Microservices
A Spring Boot Microservices-based Airline Management System built using
Java, Spring Boot, Spring Cloud, Spring Security, JWT, JPA, MySQL, OpenFeign,
Resilience4j, Apache Kafka, Razorpay Payment Links, JasperReports,
Elasticsearch, Docker and AI-service integration.
---
📁 Project Structure
```text
airlineportal/
│
├── services/
│   ├── UserService/
│   ├── LocationService/
│   ├── AirlineService/
│   ├── FlightService/
│   ├── BookingService/
│   ├── PaymentService/
│   └── AIService/                  # In development
│
├── common/
│   └── Shared DTOs, Feign Clients,
│       Exceptions, JWT utilities
│
├── ApiGateway/
│
├── EurekaServer/
│
├── ConfigServer/
│
├── docker/
│
├── pom.xml
└── README.md
```
---
🧩 Microservices
1. User Service
Handles user management, authentication and authorization.
Responsibilities
User registration and login
JWT authentication
Role management
User profile management
Internal user APIs
JWT token generation and validation
JasperReports-based user reports
```text
UserService
├── Authentication
├── Authorization
├── JWT
├── Roles
├── User Management
├── Internal APIs
└── User Reports
```
---
2. Location Service
Manages geographical and airport information.
```text
LocationService
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
Reports
Airport Management Report
City Management Report
---
3. Airline Service
Manages airlines and aircraft information.
```text
AirlineService
├── Airline
│   ├── Airline Information
│   ├── IATA / ICAO
│   ├── Country
│   ├── Alliance
│   └── Headquarters
│
└── Aircraft
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
Reports
Airline Management Report
Aircraft Management Report
---
4. Flight Service
Handles flights, schedules, flight instances and flight searching.
```text
FlightService
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
Features
Flight management
Flight schedules
Flight instances
Flight search
Route-based search
Date-based search
Upcoming flights
Flight status management
Elasticsearch-based flight searching
Seat availability management
Atomic seat reservation and release
Reports
Flight Management Report
Flight Schedule Report
Flight Instance Report
---
5. Booking Service
Handles flight bookings and passenger management.
```text
BookingService
├── Booking
│   ├── PNR
│   ├── User
│   ├── Flight
│   ├── Flight Instance
│   ├── Total Amount
│   ├── Booking Status
│   ├── Payment Status
│   ├── Booking Time
│   ├── Confirmation Time
│   └── Cancellation Information
│
└── Passenger
    ├── First Name
    ├── Last Name
    ├── Gender
    ├── Passport Number
    └── Seat Number
```
Responsibilities
Create booking
Generate unique PNR
Validate passengers
Verify users through UserService
Verify flights through FlightService
Verify flight instances
Reserve flight-instance seats
Release seats during cancellation
Retrieve booking by ID
Retrieve booking by PNR
Retrieve bookings by user
Retrieve bookings by flight
Retrieve bookings by flight instance
Cancel booking
Event Integration
BookingService publishes Kafka events:
```text
booking.created.v1
booking.cancelled.v1
```
---
6. Payment Service
Handles booking payments using Razorpay Payment Links.
```text
PaymentService
├── Payment
│   ├── Booking
│   ├── User
│   ├── PNR
│   ├── Amount
│   ├── Payment Status
│   ├── Payment Method
│   ├── Transaction ID
│   ├── Razorpay Payment Link
│   ├── Razorpay Payment ID
│   ├── Paid At
│   └── Created At
│
├── Razorpay Integration
├── Payment Link Creation
├── Razorpay Callback
├── Razorpay Webhook
└── Payment Reports
```
Payment Flow
```text
BookingService
      │
      │ booking.created.v1
      ▼
PaymentService
      │
      ▼
Create Razorpay Payment Link
      │
      ▼
Razorpay Hosted Payment Page
      │
      ├── Payment Successful
      │
      └── Payment Failed
      │
      ▼
PaymentService
      │
      │ payment.completed.v1
      │ payment.failed.v1
      ▼
BookingService
```
Kafka Events
```text
payment.completed.v1
payment.failed.v1
```
Reports
Payment Report
---
🤖 AI Service
The AIService is being developed as a separate microservice for
AI-powered airline assistance.
```text
AIService
├── Controller
├── Service
├── PromptService
├── AI Provider Integration
├── Flight Feign Client
├── Booking Feign Client
└── Payment Feign Client
```
Planned capabilities
AI airline chatbot
Flight recommendations
Flight search assistance
Booking assistance
Payment assistance
Context-aware airline responses
AIService will consume information from existing services rather than
duplicating their business data.
---
🌐 API Gateway
The API Gateway acts as the entry point for client requests.
```text
Client
   │
   ▼
API Gateway :5000
   │
   ├── UserService
   ├── LocationService
   ├── AirlineService
   ├── FlightService
   ├── BookingService
   ├── PaymentService
   └── AIService
```
Responsibilities
Request routing
Service discovery integration
Central API entry point
Gateway-level request processing
---
🔎 Eureka Server
Eureka provides service registration and discovery.
```text
                       Eureka Server
                           :8761
                              │
        ┌─────────────────────┼─────────────────────┐
        ▼                     ▼                     ▼
   UserService          FlightService        BookingService
        │                     │                     │
        ▼                     ▼                     ▼
 LocationService        AirlineService        PaymentService
```
---
⚙️ Config Server
Spring Cloud Config Server provides centralized configuration for the
microservices.
```text
                    Config Server :8888
                           │
        ┌──────────────────┼──────────────────┐
        ▼                  ▼                  ▼
   UserService       FlightService      BookingService
        │                  │                  │
        └──────────────────┼──────────────────┘
                           ▼
                    Centralized Config
```
Sensitive configuration such as database credentials, JWT secrets and
payment credentials should be supplied through external/local configuration
and should not be committed to Git.
---
🔐 Security
The project uses Spring Security with JWT-based authentication.
```text
Login
  ↓
UserService
  ↓
JWT Token
  ↓
Client
  ↓
Authorization: Bearer <token>
  ↓
API Gateway / Services
```
JWT contains role information:
```json
{
  "sub": "user@example.com",
  "roles": [
    "ROLE_USER"
  ]
}
```
Shared JWT functionality is maintained in the common module.
---
🔗 Inter-Service Communication
The project uses OpenFeign for synchronous communication between
microservices.
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
The common module contains reusable Feign clients and shared DTOs.
---
📨 Apache Kafka
Kafka is used for asynchronous event-driven communication.
```text
BookingService
      │
      ├── booking.created.v1
      └── booking.cancelled.v1
              │
              ▼
         Kafka Broker
             :9092
              │
              ▼
        PaymentService
              │
              ├── payment.completed.v1
              └── payment.failed.v1
```
Current Topics
```text
booking.created.v1
booking.cancelled.v1
payment.completed.v1
payment.failed.v1
```
Kafka is used to decouple booking and payment processing and to support
event-driven workflows.
---
🛡️ Resilience4j
Inter-service communication uses Resilience4j.
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
Implemented patterns include:
Retry
Circuit Breaker
Fallback handling
---
📊 JasperReports
JasperReports is used for PDF report generation.
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
Current Reports
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

BookingService
└── Booking / Passenger Reports

PaymentService
└── Payment Report
```
---
🔍 Elasticsearch
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
The project maintains separate JPA and Elasticsearch search endpoints:
```text
JPA Search
GET /api/flights/search

Elasticsearch Search
GET /api/flights/elasticsearch/search
```
---
🏗️ Service Architecture
Business services follow a layered architecture.
```text
Controller
    ↓
Service
    ↓
Helper
    ↓
Repository
```
For inter-service communication:
```text
ServiceImpl
    ↓
ExternalService
    ↓
Feign Client
    ↓
Remote Microservice
```
Responsibilities
```text
Controller
    → Handles HTTP requests and responses

Service
    → Application and business orchestration

Helper
    → Validation, lookup and business-support logic

Mapper
    → Entity ↔ DTO conversion

Repository
    → Database operations

ExternalService
    → Inter-service communication abstraction

Feign Client
    → Remote service communication

Exception Handler
    → Centralized exception handling
```
---
💳 Booking and Payment Architecture
```text
                  Booking Request
                        │
                        ▼
                ┌───────────────┐
                │ BookingService│
                └───────┬───────┘
                        │
                Validate Flight
                        │
                        ▼
                 Reserve Seats
                        │
                        ▼
                Save Booking
                        │
                        ▼
              booking.created.v1
                        │
                        ▼
                ┌───────────────┐
                │ PaymentService│
                └───────┬───────┘
                        │
                        ▼
              Razorpay Payment Link
                        │
                        ▼
                Payment Completed
                        │
                        ▼
             payment.completed.v1
                        │
                        ▼
                Booking Confirmation
```
---
🧰 Technology Stack
Technology	Usage
Java 17	Backend development
Spring Boot 3.5.3	Microservices
Spring Cloud	Microservices ecosystem
Spring Data JPA	Persistence
Hibernate	ORM
MySQL	Relational database
Spring Security	Authentication & Authorization
JWT	Token-based authentication
Eureka	Service discovery
Spring Cloud Gateway	API Gateway
Spring Cloud Config	Centralized configuration
OpenFeign	Inter-service communication
Resilience4j	Retry & Circuit Breaker
Apache Kafka	Event-driven communication
Razorpay	Payment processing
JasperReports	PDF reporting
Elasticsearch	Flight search
Maven	Build & dependency management
Lombok	Boilerplate reduction
Bean Validation	Request validation
Docker	Containerization
Postman	API testing
---
🔄 High-Level System Architecture
```text
                         ┌─────────────────┐
                         │     Client      │
                         └────────┬────────┘
                                  │
                                  ▼
                         ┌─────────────────┐
                         │   API Gateway   │
                         │      :5000      │
                         └────────┬────────┘
                                  │
       ┌──────────────────────────┼──────────────────────────┐
       ▼                          ▼                          ▼
 UserService              LocationService              AirlineService
    :5001                       :5002                       :5003
       │                          │                          │
       │                          └── City / Airport         └── Airline / Aircraft
       │
       └── JWT / Users
                                  │
                                  ▼
                         ┌─────────────────┐
                         │  FlightService  │
                         │      :5005      │
                         └────────┬────────┘
                                  │
                    ┌─────────────┼─────────────┐
                    ▼             ▼             ▼
                 Flight       Schedule       Instance
                                                 │
                                                 ▼
                                          Seat Availability
                                                 │
                                                 ▼
                         ┌────────────────────────────┐
                         │      BookingService        │
                         │          :5008             │
                         └─────────────┬──────────────┘
                                       │
                                       │ Kafka
                                       ▼
                         ┌────────────────────────────┐
                         │      PaymentService        │
                         │          :5009             │
                         └─────────────┬──────────────┘
                                       │
                                       ▼
                              Razorpay Payment

                         ┌────────────────────────────┐
                         │        AIService            │
                         │       In Development        │
                         └─────────────┬──────────────┘
                                       │
                              Feign / Service APIs
                                       │
                        ┌──────────────┼──────────────┐
                        ▼              ▼              ▼
                     Flight         Booking        Payment
                     Service        Service        Service
```
---
📌 Current Development Status
✅ Implemented
User Service
JWT authentication
Role-based authorization
Location Service
City and Airport management
Airline management
Aircraft management
Flight management
Flight schedules
Flight instances
Flight search
Elasticsearch integration
OpenFeign inter-service communication
Resilience4j Retry / Circuit Breaker
JasperReports PDF generation
Booking and Passenger domain
Booking validation architecture
Atomic flight-instance seat reservation/release
Booking cancellation and seat release
Apache Kafka integration
Booking Kafka events
Payment Kafka events
PaymentService
Razorpay Payment Links
Razorpay callback/webhook handling
Payment reporting
🚧 In Development
AIService
PromptService
AI provider integration
AI-powered flight and booking assistance
Further end-to-end booking/payment improvements
---


🚀 Future Enhancements
Potential future modules include:
- NotificationService
- Seat selection service
- Baggage management
- Check-in service
- Fare management
- Loyalty/rewards
- Coupon and discount management
- Distributed tracing
- Centralized logging
- Prometheus and Grafana monitoring
These are planned enhancements and are not currently represented as
implemented microservices.


