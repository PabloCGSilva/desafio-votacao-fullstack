# Cooperative Voting System

## Project Overview

This fullstack application implements a voting system for cooperatives, where each associate has one vote and decisions are made through assemblies. The system allows for the creation of agenda items, opening voting sessions, collecting votes, and displaying results.

### Key Features

- **Agenda Management**: Create and manage voting agenda items
- **Voting Sessions**: Open sessions with configurable durations (default: 1 minute)
- **Secure Voting**: Each associate can vote only once per agenda with identity verification
- **Results Calculation**: Real-time tabulation of votes with visual representation
- **CPF Validation**: Integration with a validation service to verify voter eligibility

## Live Demo

The application is running and can be accessed at:
- Frontend: http://localhost
- Backend API: http://localhost:8080/api/v1

## Directory Structure

```
cooperative-voting-system/
├── backend/              # Spring Boot backend application
│   ├── src/              # Source code
│   │   ├── main/         # Main application code
│   │   │   ├── java/     # Java classes
│   │   │   └── resources/ # Configuration files
│   │   └── test/         # Test code
│   └── pom.xml           # Maven configuration
├── frontend/             # React frontend application
│   ├── public/           # Static assets
│   ├── src/              # Source code
│   │   ├── components/   # React components
│   │   ├── pages/        # Page components
│   │   └── services/     # API services
│   └── package.json      # npm configuration
├── docker-compose.yml    # Docker composition for the full stack
└── README.md             # This file
```

## Setup Instructions

### Prerequisites

- Java 17+
- Node.js 16+
- npm 8+
- Maven 3.6+
- Docker and Docker Compose (for containerized deployment)

### Option 1: Running with Docker (Recommended)

The simplest way to run the entire application is using Docker Compose:

1. Make sure Docker and Docker Compose are installed

2. From the project root, build and start the containers:
   ```bash
   docker-compose up --build
   ```

3. Access the application:
   - Frontend: http://localhost
   - Backend API: http://localhost:8080/api/v1

4. To stop the containers:
   ```bash
   docker-compose down
   ```

### Option 2: Running Locally (Development)

#### Backend Setup

1. Navigate to the backend directory:
   ```bash
   cd backend
   ```

2. Build the application:
   ```bash
   mvn clean install
   ```

3. Run the application:
   ```bash
   mvn spring-boot:run
   ```

   The API will be available at http://localhost:8080/api/v1

#### Frontend Setup

1. Navigate to the frontend directory:
   ```bash
   cd frontend
   ```

2. Install dependencies:
   ```bash
   npm install
   ```

3. Run the development server:
   ```bash
   npm start
   ```

   The application will be available at http://localhost:3000

## Using the Application

### 1. Home Screen

The home screen provides navigation to the main features:
- View existing agendas
- Create new agendas

### 2. Creating an Agenda

1. Click on "Create Agenda" button
2. Fill in the required fields:
   - Title: The main topic of the agenda
   - Description: Detailed information about the agenda
3. Submit the form to create a new agenda item

### 3. Managing Voting Sessions

From the Agendas page, you can:
1. View all available agendas
2. Open a new voting session for an agenda by clicking "Open Voting"
   - Specify the duration in minutes (default: 1 minute)
3. Vote on existing sessions by clicking "Vote Now" on agendas with active sessions

### 4. Casting a Vote

The voting process follows a two-step verification for security:

1. **Identity Verification**
   - Enter your Associate ID (unique identifier)
   - Enter your CPF number (11 digits)
   - Click "Validate Identity"

2. **Vote Casting**
   - After successful validation, vote options appear
   - Select "Yes" or "No"
   - Click "Cast Vote" to register your decision

### 5. Viewing Results

Results are displayed after a voting session closes:
- Visual progress bars show Yes/No vote percentages
- Total vote count is displayed
- Final outcome (Approved/Rejected) is shown

## API Endpoints

### Agenda Management

- **Create a new agenda**
  - `POST /api/v1/agendas`
  - Request body:
    ```json
    {
      "title": "Annual Budget Approval",
      "description": "Vote on the cooperative's annual budget for 2025"
    }
    ```

- **Get all agendas**
  - `GET /api/v1/agendas`

- **Get an agenda by ID**
  - `GET /api/v1/agendas/{id}`

### Voting Session Management

- **Open a new voting session**
  - `POST /api/v1/sessions`
  - Request body:
    ```json
    {
      "agendaId": 1,
      "durationInMinutes": 5
    }
    ```

- **Get active voting sessions**
  - `GET /api/v1/sessions/active`

- **Get voting session results**
  - `GET /api/v1/sessions/{id}/results`

### Votes

- **Validate voter eligibility**
  - `GET /api/v1/votes/validate?cpf={cpf}&associateId={associateId}&sessionId={sessionId}`

- **Cast a vote**
  - `POST /api/v1/votes`
  - Request body:
    ```json
    {
      "sessionId": 1,
      "associateId": "123456",
      "voteOption": true,
      "cpf": "12345678901"
    }
    ```

## Testing

### Valid Test CPFs

For testing, you can use these valid CPF numbers:
- 529.982.247-25
- 111.444.777-35
- 123.456.789-09

Note: The system randomly determines if a CPF holder can vote, so you may need to try different CPFs.

### Special Testing Notes

1. **Voting Session Duration**: Sessions close automatically after the configured duration
2. **Duplicate Votes**: The system prevents associates from voting more than once per session
3. **Invalid CPFs**: The system validates CPF numbers mathematically and rejects invalid ones
4. **Inactive Sessions**: Votes are only accepted during open sessions

## Bonus Features

1. [x] **CPF Validation**: Implements a client that validates CPF numbers and randomly determines voting eligibility
2. [x] **Performance Optimization**: Includes database connection pooling, caching, and proper indexing for high-volume scenarios
3. [x] **API Versioning**: Implements URL path versioning (/api/v1/) to support future API evolution

## Troubleshooting

### Common Issues

1. **API Connection Problems**
   - **Symptom**: Frontend can't connect to backend
   - **Solution**: Ensure backend is running and CORS is properly configured

2. **Docker Networking Issues**
   - **Symptom**: Containers can't communicate
   - **Solution**: Check docker-compose network configuration

3. **Database Connection**
   - **Symptom**: Backend can't connect to database
   - **Solution**: Verify database credentials and connection string

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Acknowledgments

- This project was created as a solution to the DB Server fullstack challenge
- Based on the requirements specified in [desafio-votacao-fullstack](https://github.com/dbserver/desafio-votacao-fullstack)
