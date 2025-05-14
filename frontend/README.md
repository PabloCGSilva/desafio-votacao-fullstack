# Cooperative Voting System - Frontend

## Overview

The Cooperative Voting System frontend provides an intuitive interface for cooperative members to participate in democratic decision-making processes. Built with React and Material UI, it offers a responsive user experience across devices for managing agendas, voting sessions, and viewing results.

![Cooperative Voting System](https://example.com/screenshot.png)

## Features

- Create and view cooperative agenda items
- Open voting sessions with customizable durations
- Two-step secure voting process with identity verification
- Real-time voting results with visual indicators
- Responsive design for desktop and mobile

## Tech Stack

- **Framework**: React 18
- **UI Library**: Material UI
- **HTTP Client**: Axios
- **Routing**: React Router

## Getting Started

### Local Development Setup

1. **Clone the repository**
   ```bash
   git clone https://github.com/yourusername/cooperative-voting-frontend.git
   cd cooperative-voting-frontend
   ```

2. **Install dependencies**
   ```bash
   npm install
   ```

3. **Configure API endpoint**
   
   Edit `src/services/api.js` to point to your backend:
   ```javascript
   const API_BASE_URL = 'http://localhost:8080/api/v1';
   ```

4. **Start the development server**
   ```bash
   npm start
   ```
   
   The application will be available at http://localhost:3000

### Docker Deployment

1. **Build and run with Docker**
   ```bash
   # Build the Docker image
   docker build -t voting-frontend .
   
   # Run the container
   docker run -p 3000:3000 -it voting-frontend
   ```

2. **Access the containerized application**
   
   Open your browser and navigate to http://localhost:3000

## User Guide: Screens and Functionality

### Home Screen

The home screen serves as a central hub with clear navigation to key features.

**Features:**
- Quick access to view existing agendas
- Direct link to create new agendas
- Clean, intuitive layout with descriptive cards

**Usage:**
- Click "View Agendas" to see all available agenda items
- Click "Create Agenda" to start a new voting topic

### Agenda Management Screen

This screen displays all available agendas and provides actions for each.

**Features:**
- List of all agendas with titles and descriptions
- Creation dates for reference
- Action buttons for each agenda

**Usage:**
- View existing agendas with their details
- Click "Open Voting" on an agenda to start a new voting session
- Click "Vote Now" on agendas with active voting sessions
- Click "Create Agenda" to add a new agenda item

### Create Agenda Screen

This form allows users to create new agenda topics for voting.

**Features:**
- Simple form with validation
- Clear input fields for title and description
- Responsive design for various screen sizes

**Usage:**
1. Enter a descriptive title for the agenda
2. Provide a detailed description explaining the voting topic
3. Click "Create Agenda" to submit

### Voting Screen

The voting process follows a two-step verification approach for security.

**Features:**
- Two-step voting process for security
- Progress indicator showing the voting steps
- Countdown timer showing remaining session time
- Form validation for inputs

**Step 1: Identity Verification**
1. Enter Associate ID (unique identifier)
2. Enter CPF number (11 digits)
3. Click "Validate Identity"

**Step 2: Vote Casting**
1. After successful validation, vote options appear
2. Select "Yes" or "No"
3. Click "Cast Vote" to register your decision

### Results Screen

This screen displays voting outcomes with visual indicators.

**Features:**
- Visual progress bars for Yes/No vote percentages
- Total vote count display
- Final outcome indicator (Approved/Rejected)
- Automatic refresh while voting is in progress

**States:**
- **In Progress**: Shows a waiting indicator with automatic refresh
- **Completed**: Displays final results with outcome

## Testing the Application

### Manual Testing

1. **Create an agenda**
   - Navigate to Create Agenda screen
   - Fill in title and description
   - Submit the form
   - Verify the agenda appears in the list

2. **Open a voting session**
   - Go to Agendas list
   - Click "Open Voting" on an agenda
   - Set duration (e.g., 5 minutes)
   - Verify "Vote Now" button appears

3. **Cast a vote**
   - Click "Vote Now"
   - Enter test Associate ID (e.g., "123456")
   - Enter valid CPF (e.g., "12345678909")
   - Validate identity
   - Select "Yes" or "No"
   - Cast vote
   - Verify success message

4. **View results**
   - After voting, you'll be redirected to results
   - If session is still open, verify waiting message
   - Wait for session to close
   - Verify results appear with proper statistics

### Test CPF Numbers

Use these valid CPF numbers for testing:
- 529.982.247-25
- 111.444.777-35
- 123.456.789-09

### Automated Testing (for Developers)

Run the test suite with:
```bash
npm test
```

## Advanced Deployment

### Production Build

Create an optimized production build:
```bash
npm run build
```

This creates a `build` folder with production-ready files.

### Nginx Configuration

For production with Nginx, create a `nginx.conf` file:
```nginx
server {
    listen 80;
    root /usr/share/nginx/html;
    index index.html;

    # Handle React routing
    location / {
        try_files $uri $uri/ /index.html;
    }

    # Proxy API requests
    location /api/ {
        proxy_pass http://backend:8080/api/;
        proxy_http_version 1.1;
        proxy_set_header Upgrade $http_upgrade;
        proxy_set_header Connection 'upgrade';
        proxy_set_header Host $host;
        proxy_cache_bypass $http_upgrade;
    }
}
```

### Docker Compose Setup

For a complete environment with frontend, backend, and database:

```yaml
version: '3.8'

services:
  frontend:
    build: ./cooperative-voting-frontend
    ports:
      - "80:80"
    depends_on:
      - backend
    networks:
      - voting-network

  backend:
    build: ./cooperative-voting-system
    ports:
      - "8080:8080"
    environment:
      - SPRING_PROFILES_ACTIVE=prod
      - SPRING_DATASOURCE_URL=jdbc:postgresql://db:5432/voting_db
      - SPRING_DATASOURCE_USERNAME=postgres
      - SPRING_DATASOURCE_PASSWORD=postgres
    depends_on:
      - db
    networks:
      - voting-network

  db:
    image: postgres:14
    ports:
      - "5433:5432"
    environment:
      - POSTGRES_DB=voting_db
      - POSTGRES_USER=postgres
      - POSTGRES_PASSWORD=postgres
    volumes:
      - postgres_data:/var/lib/postgresql/data
    networks:
      - voting-network

networks:
  voting-network:

volumes:
  postgres_data:
```

Run with:
```bash
docker-compose up --build
```

## Troubleshooting

### Common Issues

1. **API Connection Problems**
   - **Symptom**: "Failed to load agendas" or similar errors
   - **Solution**: Verify backend API is running at the configured URL

2. **Voting Validation Errors**
   - **Symptom**: "Associate is not able to vote" message
   - **Solution**: Try different CPF numbers, as the backend randomly determines eligibility

3. **Results Not Showing**
   - **Symptom**: Perpetual loading or waiting message
   - **Solution**: Voting session might still be open; wait for the configured duration to pass

4. **Docker Connectivity Issues**
   - **Symptom**: Frontend can't reach backend in Docker
   - **Solution**: Ensure services are on the same network and environment variables are correctly set

## Browser Compatibility

The application has been tested and works on:
- Chrome 90+
- Firefox 88+
- Safari 14+
- Edge 90+

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## License

This project is licensed under the MIT License - see the LICENSE file for details.

---

This frontend application is designed to work with the [Cooperative Voting System Backend](https://github.com/yourusername/cooperative-voting-system). Please refer to the backend documentation for API details and server setup instructions.