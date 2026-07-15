# AI Resume Builder

A simple web application that generates professional resumes using rule-based text generation. Users enter their details through a clean web interface, and the system generates formatted resume sections including summary, skills, education, experience, and projects.

## 🚀 Features

- **Clean Web Interface**: Simple, responsive HTML/CSS/JS frontend
- **Professional Resume Generation**: Rule-based resume generation with formatted sections
- **No Database Required**: Stateless application, no data storage
- **Easy Setup**: Minimal configuration needed to get started

## 📁 Project Structure

```
AiResumeBuilder/
├── frontend/
│   ├── index.html          # Home page
│   ├── form.html           # User input form
│   ├── preview.html        # Resume preview page
│   ├── style.css           # Styling
│   └── script.js           # Frontend logic
│
└── backend/
    ├── pom.xml             # Maven dependencies
    ├── src/
    │   └── main/
    │       ├── java/com/example/resume/
    │       │   ├── ResumeBuilderApplication.java
    │       │   ├── controller/
    │       │   │   └── ResumeController.java
    │       │   ├── service/
    │       │   │   └── ResumeService.java
    │       │   └── model/
    │       │       ├── ResumeRequest.java
    │       │       └── ResumeResponse.java
    │       └── resources/
    │           └── application.properties
    └── README.md
```

## 🛠️ Prerequisites

- **Java 17** or higher
- **Maven 3.6+** (or use Maven wrapper)
- **Web Browser** (Chrome, Firefox, Edge, etc.)
- **Text Editor** or IDE (optional, for viewing code)

## 📦 Setup Instructions

### Backend Setup (Spring Boot)

1. **Navigate to backend directory:**
   ```bash
   cd backend
   ```

2. **Build the project:**
   ```bash
   mvn clean install
   ```
   
   Or if you have Maven wrapper:
   ```bash
   ./mvnw clean install    # Linux/Mac
   mvnw.cmd clean install  # Windows
   ```

3. **Run the Spring Boot application:**
   ```bash
   mvn spring-boot:run
   ```
   
   Or:
   ```bash
   ./mvnw spring-boot:run    # Linux/Mac
   mvnw.cmd spring-boot:run  # Windows
   ```

4. **Verify backend is running:**
   - Open browser and go to: `http://localhost:8080/api/health`
   - You should see: "Resume Builder API is running!"

### Frontend Setup

1. **Navigate to frontend directory:**
   ```bash
   cd frontend
   ```

2. **Open the application:**
   - **Option 1: Simple HTTP Server (Recommended)**
     ```bash
     # Using Python 3
     python -m http.server 8000
     
     # Using Python 2
     python -m SimpleHTTPServer 8000
     
     # Using Node.js (if you have http-server installed)
     npx http-server -p 8000
     ```
   
   - **Option 2: Open directly in browser**
     - Simply open `index.html` in your web browser
     - Note: Some browsers may block CORS requests when opening files directly
     - If you encounter CORS issues, use Option 1 instead

3. **Access the application:**
   - If using HTTP server: `http://localhost:8000`
   - If opening directly: Navigate to the `index.html` file

## 🎯 Usage

1. **Start the backend server** (as described above)
2. **Open the frontend** in your browser
3. **Click "Create Your Resume"** on the home page
4. **Fill in the form** with your details:
   - Name, Email, Phone (required)
   - Skills (comma-separated)
   - Education details
   - Work experience
   - Projects (optional)
   - Career objective (optional)
5. **Click "Generate Resume"** and wait for processing
6. **Review your resume** on the preview page
7. **Print or save as PDF** using browser's print function

## 🔧 API Endpoints

### POST `/api/generate-resume`

Generates a resume from user input.

**Request Body:**
```json
{
  "name": "John Doe",
  "email": "john.doe@example.com",
  "phone": "+1 (555) 123-4567",
  "skills": "Java, Spring Boot, SQL, Git",
  "education": "Bachelor of Science in Computer Science, University Name, 2020-2024",
  "experience": "Software Developer at Tech Corp (2022-2024)",
  "projects": "E-commerce Platform - Built using Spring Boot",
  "careerObjective": "To leverage my technical skills..."
}
```

**Response:**
```json
{
  "name": "John Doe",
  "email": "john.doe@example.com",
  "phone": "+1 (555) 123-4567",
  "summary": "Professional summary text...",
  "skillsSection": "Technical Skills: Java, Spring Boot...",
  "educationSection": "• Bachelor of Science...",
  "experienceSection": "• Software Developer...",
  "projectsSection": "• E-commerce Platform..."
}
```

### GET `/api/health`

Health check endpoint.

**Response:** `"Resume Builder API is running!"`

## 🎨 Customization

### Changing Backend Port

Edit `backend/src/main/resources/application.properties`:
```properties
server.port=8080
```

### Changing Frontend API URL

Edit `frontend/script.js`:
```javascript
const response = await fetch('http://localhost:8080/api/generate-resume', {
    // ... rest of the code
});
```

## 🐛 Troubleshooting

### Backend Issues

- **Port 8080 already in use:**
  - Change the port in `application.properties` or stop the process using port 8080

- **Maven build fails:**
  - Ensure Java 17+ is installed: `java -version`
  - Ensure Maven is installed: `mvn -version`
  - Try cleaning and rebuilding: `mvn clean install`

### Frontend Issues

- **CORS errors:**
  - Make sure backend is running on `http://localhost:8080`
  - Use an HTTP server instead of opening HTML files directly
  - Check browser console for specific error messages

- **API call fails:**
  - Verify backend is running: visit `http://localhost:8080/api/health`
  - Check browser console for error details
  - Ensure the API URL in `script.js` matches your backend port

## 📝 Notes

- This application uses **rule-based generation**, not AI models
- No data is stored - all processing is stateless
- The resume generation uses simple templating and formatting rules
- Perfect for learning Spring Boot and frontend development

## 🚀 Future Enhancements (Optional)

- Add PDF export functionality
- Support for multiple resume templates
- Save/load resume drafts
- Export to Word/PDF formats
- More sophisticated text generation rules

## 📄 License

This project is provided as-is for educational purposes.

---

**Happy Resume Building! 🎉**





