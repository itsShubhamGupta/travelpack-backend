# TravelPack Backend – Tour Package Management System

## 🚀 Overview
This is a Spring Boot backend for a Tour Package Booking System where users can browse and book travel packages, and admins can manage packages and itineraries.

---

## 🔥 Key Features
- JWT Authentication & Role-Based Authorization (ADMIN / USER)
- Booking system with slot management
- Package & itinerary management
- Image upload support
- Search & filter (location, price)
- Pagination support
- Global exception handling

---

## 🛠 Tech Stack
- Java 8+
- Spring Boot
- Spring Security (JWT)
- Spring Data JPA (Hibernate)
- MySQL

---

## 📦 API Endpoints

### 🔐 Auth
POST /auth/register  
POST /auth/login  

### 📦 Packages
GET /packages  
GET /package/{id}  
GET /packages?location=kashmir  

### 📘 Booking
POST /booking  
GET /booking/user/{id}  

### 🧑‍💼 Admin
POST /admin/package  


---

## 🔐 Security Flow
- User logs in → JWT token generated  
- Token sent in Authorization header  
- Role-based access control applied  

---

## ⚙️ Setup Instructions

```bash
git clone <repo-url>
cd travelpack-backend
