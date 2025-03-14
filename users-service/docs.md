# User Service API Documentation

## Overview
The **User Service API** provides authentication, user management, and logging functionalities as an independent microservice. It follows industry best practices, including **RESTful API principles, JWT-based authentication, Role-Based Access Control (RBAC), security hardening, and observability**.

---
## API Endpoints

### **1. Authentication APIs**

#### **1.1 User Registration**
- **Endpoint:** `POST /api/auth/register`
- **Description:** Registers a new user with an inactive and locked account until email verification.
- **Request Body:**
```json
{
  "username": "johndoe",
  "email": "johndoe@example.com",
  "password": "StrongPassword123!",
  "phone": "1234567890",
  "firstName": "John",
  "lastName": "Doe",
  "dateOfBirth": "1990-01-01"
}
```
- **Response:**
```json
{
  "status": "success",
  "message": "Registration successful. Please verify your email."
}
```

#### **1.2 Email Verification**
- **Endpoint:** `GET /api/auth/verify-email/{token}`
- **Description:** Activates and unlocks the user's account after email verification.
- **Response:**
```json
{
  "status": "success",
  "message": "Email verified successfully. Your account is now active."
}
```

#### **1.3 User Login**
- **Endpoint:** `POST /api/auth/login`
- **Description:** Authenticates the user and returns a JWT token.
- **Request Body:**
```json
{
  "email": "johndoe@example.com",
  "password": "StrongPassword123!"
}
```
- **Response:**
```json
{
  "status": "success",
  "token": "jwt-token-here",
  "expiresIn": 3600
}
```

### **2. User Management APIs**

#### **2.1 Get User Profile**
- **Endpoint:** `GET /api/users/{userId}`
- **Authorization:** User or Admin
- **Response:**
```json
{
  "id": 123,
  "username": "johndoe",
  "email": "johndoe@example.com",
  "firstName": "John",
  "lastName": "Doe",
  "role": "USER",
  "isActive": true,
  "isLocked": false
}
```

#### **2.2 Update User Profile**
- **Endpoint:** `PUT /api/users/{userId}`
- **Authorization:** User
- **Request Body:**
```json
{
  "phone": "9876543210",
  "firstName": "John",
  "lastName": "Doe"
}
```
- **Response:**
```json
{
  "status": "success",
  "message": "Profile updated successfully."
}
```

### **3. Admin Management APIs**

#### **3.1 Lock/Unlock User**
- **Endpoint:** `PUT /api/admin/users/{userId}/lock`
- **Authorization:** Admin
- **Response:**
```json
{
  "status": "success",
  "message": "User locked successfully."
}
```

#### **3.2 Delete User Account**
- **Endpoint:** `DELETE /api/admin/users/{userId}`
- **Authorization:** Admin or Self
- **Response:**
```json
{
  "status": "success",
  "message": "User deleted successfully."
}
```

### **4. Logging & Monitoring APIs**

#### **4.1 Get User Activity Logs**
- **Endpoint:** `GET /api/logs/users/{userId}`
- **Authorization:** Admin
- **Response:**
```json
{
  "logs": [
    {
      "timestamp": "2025-03-14T12:34:56Z",
      "action": "User logged in",
      "ipAddress": "192.168.1.1"
    }
  ]
}
```

#### **4.2 Export User Data**
- **Endpoint:** `GET /api/admin/users/export`
- **Authorization:** Admin
- **Response:** CSV or JSON containing user data.

---
## **Security Best Practices**
✅ **JWT Authentication** for API security.  
✅ **RBAC (Role-Based Access Control)** for user/admin actions.  
✅ **Rate Limiting & IP Throttling** to prevent abuse.  
✅ **Password Hashing (BCrypt/Argon2)** to store secure credentials.  
✅ **2FA/MFA** support for additional security.

---
## **Observability & Monitoring**
✅ **API Health Check** (`GET /api/health`) for service status.  
✅ **User Activity Logs** stored in **ELK Stack / Grafana** for analysis.  
✅ **Error Logging** with **Sentry or Datadog** for real-time issue tracking.

---
## **Tech Stack Recommendations**
| Component       | Technology   |
|----------------|-------------|
| **Backend**    | Spring Boot (Java 21) |
| **Database**   | PostgreSQL / MySQL |
| **Cache**      | Redis |
| **Authentication** | JWT / OAuth2 |
| **Event Handling** | Kafka / RabbitMQ |
| **Logging**    | ELK Stack / Grafana |
| **Deployment** | Docker, Kubernetes |

---
## **Conclusion**
This **User Service API** is designed to be **secure, scalable, and reusable across multiple projects**. Let me know if you need further improvements! 🚀
