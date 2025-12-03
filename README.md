# EMI Calculator (Full-stack) — Backend (Spring Boot) + Frontend (Angular)

This repository contains a small full-stack EMI calculator assignment demonstrating:
- Java Spring Boot backend API to calculate EMI
- Minimal Angular frontend to call the API and display results

## Repo layout
emi-calculator/
├── backend/
│   ├── pom.xml
│   └── src/
│       └── main/
│           └── java/com/emicalculator/
│               ├── EmiCalculatorBackendApplication.java
│               ├── controller/EmiController.java
│               ├── dto/request/EmiRequest.java
│               ├── dto/response/EmiResponse.java
│               ├── service/EmiService.java
│               └── exception/EmiExceptionHandler.java
│       └── test/
│           └── java/com/example/emicalculator/EmiCalculatorBackendApplicationTests.java
├── frontend/
│   ├── package.json
│   ├── angular.json
│   └── src/
│       ├── index.html
│       ├── main.ts
│       └── app/
|           |──service
|               └──app.service.ts
│           ├── app.module.ts
│           ├── app.component.ts
│           ├── app.component.html
│           ├── app.component.css
│           └── emi.service.ts
├── .gitignore
└── README.md

---

## Prerequisites

- Java 17+ and Maven (for backend)
- Node 18+ and npm, Angular CLI (for frontend)
- (Optional) IDE like IntelliJ / VSCode

---

## Backend — run locally

1. Open terminal, go to `backend/`:
        cd backend
        mvn clean install
        mvn spring-boot:run

Backend will start on http://localhost:8080.

API : POST /api/emi

## Fronend — run locally

Frontend — run locally
	1.	In a new terminal, go to `frontend/`:
        cd frontend
        npm install
        npm start    
	2.	The Angular dev server runs on http://localhost:4200 by default.
	3.	Make sure the backend is running on http://localhost:8080 (or update emi.service.ts with the correct URL).
