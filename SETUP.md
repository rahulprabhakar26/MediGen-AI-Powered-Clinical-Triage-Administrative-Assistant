# MediGen - AI-Powered Clinical Triage & Administrative Assistant

## 🚀 Quick Start Guide

### Prerequisites
- Java 17+
- Node.js 16+
- MySQL 8.0+
- MongoDB 4.4+
- Docker (optional)

### Local Development Setup

1. **Clone and Setup**
   ```bash
   git clone <repository-url>
   cd MediGen
   cp .env.example .env
   # Edit .env with your API keys
   ```

2. **Start Databases**
   ```bash
   # MySQL
   docker run -d --name medigen-mysql -e MYSQL_ROOT_PASSWORD=password -p 3306:3306 mysql:8.0
   
   # MongoDB
   docker run -d --name medigen-mongodb -p 27017:27017 mongo:4.4
   ```

3. **Start Backend Services**
   ```bash
   cd backend
   
   # Start Discovery Server
   cd discovery-server && ./gradlew bootRun &
   
   # Start Gateway Service
   cd ../gateway-service && ./gradlew bootRun &
   
   # Start Auth Service
   cd ../auth-service && ./gradlew bootRun &
   
   # Start Patient Service
   cd ../patient-service && ./gradlew bootRun &
   
   # Start AI Service
   cd ../ai-service && ./gradlew bootRun &
   
   # Start Payment Service
   cd ../payment-service && ./gradlew bootRun &
   ```

4. **Start Frontend**
   ```bash
   cd frontend
   npm install
   npm start
   ```

### Docker Setup (Recommended)

```bash
# Copy environment variables
cp .env.example .env
# Edit .env with your API keys

# Start all services
docker-compose up -d

# View logs
docker-compose logs -f

# Stop services
docker-compose down
```

## 🔧 Configuration

### Required Environment Variables

Create a `.env` file with the following variables:

```bash
# OpenAI API Key (required for AI service)
OPENAI_API_KEY=your-openai-api-key-here

# Razorpay Sandbox Keys (required for payment service)
RAZORPAY_KEY_ID=rzp_test_1234567890
RAZORPAY_KEY_SECRET=your-razorpay-secret-key
```

### Service Ports

| Service | Port | Description |
|---------|------|-------------|
| Discovery Server | 8761 | Eureka Service Registry |
| Gateway | 8080 | API Gateway |
| Auth Service | 8081 | Authentication |
| Patient Service | 8082 | Patient Management |
| AI Service | 8083 | AI Integration |
| Payment Service | 8084 | Payment Processing |
| Frontend | 3000 | React Application |

## 📋 API Endpoints

### Authentication Service (8081)
- `POST /api/auth/register` - Doctor registration
- `POST /api/auth/login` - Doctor login
- `GET /api/auth/validate` - Token validation

### Patient Service (8082)
- `POST /api/patients/register` - Patient registration
- `POST /api/patients/records` - Create patient record
- `GET /api/patients/records` - Get all records
- `GET /api/patients/{id}/records` - Get patient records
- `PUT /api/patients/records/{id}` - Update record

### AI Service (8083)
- `POST /api/ai/generate-notes` - Generate clinical notes

### Payment Service (8084)
- `POST /api/payments/create-order` - Create payment order
- `POST /api/payments/verify` - Verify payment
- `GET /api/payments/patient/{id}` - Payment history

## 🧪 Testing the Application

1. **Register a Doctor**
   - Go to http://localhost:3000/doctor-register
   - Fill in doctor details
   - Login at http://localhost:3000/doctor-login

2. **Register a Patient**
   - Go to http://localhost:3000/
   - Fill in patient details
   - Note the patient ID

3. **Create Patient Record**
   - Go to http://localhost:3000/patient-intake
   - Enter patient ID and symptoms
   - Submit to generate AI clinical notes

4. **View Dashboard**
   - Login as doctor
   - Go to http://localhost:3000/doctor-dashboard
   - Review and edit AI-generated notes

## 🏗️ Architecture

```
┌─────────────────┐
│   React Frontend │
└─────────┬───────┘
          │
┌─────────▼───────┐
│  API Gateway   │
└─────────┬───────┘
          │
┌─────────▼─────────────────────────────────────┐
│  Microservices                                │
│  ┌─────────┐ ┌─────────┐ ┌─────────┐ ┌─────────┐│
│  │   Auth  │ │ Patient │ │   AI    │ │Payment ││
│  │ Service │ │ Service │ │ Service │ │ Service ││
│  └─────────┘ └─────────┘ └─────────┘ └─────────┘│
└─────────┬─────────────────────────────────────┘
          │
┌─────────▼─────────────────────────────────────┐
│  Data Layer                                   │
│  ┌─────────┐ ┌─────────┐ ┌─────────────────┐  │
│  │  MySQL  │ │ MongoDB │ │   OpenAI API    │  │
│  │(Structured)│(AI Logs)│ │  (AI Service)  │  │
│  └─────────┘ └─────────┘ └─────────────────┘  │
└───────────────────────────────────────────────┘
```

## 🔍 Troubleshooting

### Common Issues

1. **Services not starting**
   - Check if ports are available
   - Verify database connections
   - Check environment variables

2. **AI Service not working**
   - Verify OpenAI API key
   - Check API quota and billing

3. **Database connection issues**
   - Ensure MySQL and MongoDB are running
   - Check connection strings in application.yml

### Logs

```bash
# Docker logs
docker-compose logs -f [service-name]

# Local development logs
tail -f logs/application.log
```

## 📚 Development Notes

- All services use Spring Boot 3.2.0
- Frontend uses React 18 with React Router
- JWT tokens for authentication
- Feign client for inter-service communication
- MongoDB for AI service logs
- MySQL for structured data

## 🚀 Deployment

For production deployment:

1. Update environment variables
2. Use production databases
3. Configure proper security settings
4. Set up monitoring and logging
5. Use container orchestration (Kubernetes)

## 📄 License

MIT License - See LICENSE file for details
