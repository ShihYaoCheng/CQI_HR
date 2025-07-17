# CQI HR Management System

An internal web-based Human Resource Management System (HRMS) developed for CQI Games, providing attendance tracking, leave request processing, and backend reporting tools. The system is built with Java and Spring Framework, and deployed via Dockerized Tomcat.

## 🌟 Features

- 👤 **User Authentication**: Secure login system with session management
- 📆 **Leave Management**: Submit, track, and approve employee leave requests
- 🕒 **Attendance Tracking**: Daily check-in/out, working hour statistics
- 📈 **Report Generation**: Export Excel reports for HR analysis using Apache POI
- 🔔 **LINE Bot Integration**: Notify employees of leave approvals via LINE messages
- 🔌 **RESTful API**: Exposes endpoints for mobile clients and third-party integration
- 📊 **Role-Based Access**: Different permission levels for HR, managers, and staff

## 💻 Tech Stack

- **Language & Frameworks**: Java 8, Spring Framework (Web MVC, Context, ORM, TX), Hibernate
- **Database**: MySQL
- **View Layer**: JSP, JSTL
- **Build Tool**: Maven
- **Deployment**: WAR on Tomcat, Docker
- **Libraries**:
  - Apache POI (Excel report export)
  - Jackson / json-lib (JSON handling)
  - Quartz (Scheduled tasks)
  - LINE Bot SDK
  - SLF4J + Log4j (Logging)

## 🧑‍💻 My Role

- Built major modules: leave processing, attendance logics, and report exports
- Developed RESTful APIs for frontend integration and automation
- Integrated LINE Bot SDK for sending leave notifications to staff
- Designed Docker-based deployment flow using Maven + docker-maven-plugin
- Maintained system logs and monitoring infrastructure

## 📂 Repository Notes

This repo contains non-confidential parts of the original system, with simulated data and simplified business logic for demo purposes only.

