# MediGen — AI-Powered Clinical Triage & Administrative Assistant

## Problem Statement

Healthcare professionals often spend excessive time on manual note-taking, administrative tasks, and patient triage, leading to burnout and inefficiencies. MediGen aims to **automate clinical documentation** and **initial triage recommendation** by converting raw patient input into structured medical summaries using Generative AI.

##  Application Goals

1. Convert **raw patient narratives** into structured **SOAP/H&P medical notes** using AI.
2. Provide **initial triage recommendations** (Low, Medium, High risk).
3. Handle **secure consultation payments** using Razorpay sandbox.
4. Enable doctors to **review and edit AI-generated notes** before final submission.

##  Tech Stack

| Layer                      | Technology                                             |
| -------------------------- | ------------------------------------------------------ |
| **Backend**                | Java 17, Spring Boot, Spring Cloud, Spring Security    |
| **Frontend**               | React.js (basic UI for form and dashboard)             |
| **Database**               | MySQL (patients, doctors, payments), MongoDB (AI logs) |
| **AI Integration**         | OpenAI API (text-davinci/gpt-4 endpoint)               |
| **Build & Tools**          | Gradle, Docker, Postman, GitHub                        |
| **Deployment (prototype)** | Render / Railway / Free-tier EC2                       |

##  Architecture Overview

```
Frontend (React)
     ↓
API Gateway (Spring Cloud Gateway)
     ↓
-------------------------------------------------
| Auth Service | Patient Service | AI Service | Payment Service |
-------------------------------------------------
     ↓
MySQL (structured data)
MongoDB (AI logs)
OpenAI API (external call)
```

### Microservice Responsibilities

- **Auth Service**: Doctor registration, login, JWT token validation
- **Patient Service**: Intake form submissions, patient records management
- **AI Service**: OpenAI API integration for SOAP note generation
- **Payment Service**: Razorpay sandbox integration for payments
- **Gateway Service**: Central API entry point and routing
- **Discovery Server**: Service registration and discovery (Eureka)

##  Quick Start

### Prerequisites
- Java 17+
- Node.js 16+
- MySQL 8.0+
- MongoDB 4.4+
- Docker (optional)

### Backend Setup
```bash
cd backend
./gradlew build
./gradlew bootRun
```

### Frontend Setup
```bash
cd frontend
npm install
npm start
```

### Docker Setup
```bash
docker-compose up -d
```

## 📁 Project Structure

```
MediGen/
 ├── backend/
 │    ├── auth-service/
 │    ├── patient-service/
 │    ├── ai-service/
 │    ├── payment-service/
 │    ├── gateway-service/
 │    └── discovery-server/
 ├── frontend/
 │    ├── src/
 │    │    ├── components/
 │    │    ├── pages/
 │    │    └── services/
 │    └── package.json
 └── README.md
```



> **MediGen — AI-Powered Clinical Triage & Admin Assistant**
> Solo project using **Java Spring Boot, React, and OpenAI API** to streamline clinical workflows by generating structured medical notes and triage recommendations from patient data.
>
> * Designed modular **microservice architecture** with Auth, Patient, AI, and Payment services using **Spring Cloud**.
> * Integrated **OpenAI API** for automated SOAP note generation and triage logic.
> * Implemented **JWT-based authentication** and **MySQL persistence**.
> * Deployed prototype using **Docker + Render Cloud**.
> * Focused on **scalability, modularity, and real-world AI application in healthcare.**

