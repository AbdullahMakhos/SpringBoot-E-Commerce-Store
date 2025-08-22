🛍️ SpringBoot E-Commerce Store
A full-featured, minimalistic e-commerce web application built with Spring Boot. This project demonstrates core web development concepts including user authentication, database management, session handling, and a dynamic frontend.

https://img.shields.io/badge/Java-17+-blue?style=flat&logo=openjdk
https://img.shields.io/badge/Spring%2520Boot-3.2-brightgreen?style=flat&logo=springboot
https://img.shields.io/badge/MySQL-8.0-blue?style=flat&logo=mysql
https://img.shields.io/badge/Thymeleaf-3.1-green?style=flat&logo=thymeleaf

✨ Features
User Authentication

Secure user registration and login

Password hashing with BCrypt

Session management

Login/logout functionality

Product Management

Browse product catalog

Full CRUD operations (Create, Read, Update, Delete)

Product validation and error handling

Shopping Cart

Add/remove items from cart

Persistent cart across sessions

Checkout process

Purchase history

Frontend Interface

Clean, responsive Thymeleaf templates

Custom CSS styling

Dynamic content rendering

User-friendly navigation

🛠️ Technology Stack
Backend: Java 17+, Spring Boot 3.2, Spring Data JPA, Spring Security

Database: MySQL 8.0

Frontend: Thymeleaf, HTML5, CSS3, JavaScript (Vanilla)

Build Tool: Maven

Security: BCrypt password encoding, session authentication

📦 Project Structure
text
src/
├── main/
│   ├── java/com/ecommerce/store/
│   │   ├── controller/     # MVC Controllers
│   │   ├── model/          # Entity classes
│   │   ├── repository/     # Data access layer
│   │   ├── service/        # Business logic
│   │   └── dto/            # Data Transfer Objects
│   ├── resources/
│   │   ├── static/         # CSS, JS files
│   │   ├── templates/      # Thymeleaf HTML files
│   │   └── application.properties
└── test/                   # Unit tests
🚀 Installation & Setup
Prerequisites
Java 17 or higher

MySQL 8.0+

Maven 3.6+

Steps
Clone the repository

bash
git clone https://github.com/your-username/springboot-ecommerce-store.git
cd springboot-ecommerce-store
Configure Database

Create a MySQL database named ecommerce_store

Update src/main/resources/application.properties with your database credentials:

properties
spring.datasource.url=jdbc:mysql://localhost:3306/ecommerce_store
spring.datasource.username=your_username
spring.datasource.password=your_password
Build and Run

bash
mvn clean install
mvn spring-boot:run
Access the Application

Open your browser and go to http://localhost:8080

Register a new account or use existing credentials

📖 Usage
Register/Login: Create an account or login with existing credentials

Browse Products: View available products on the homepage

Manage Cart: Add products to cart, view cart contents, remove items

Checkout: Complete purchases and view order history

Admin Features: Manage products (add, edit, delete) - if implemented

🧪 Testing
Run the test suite with:

bash
mvn test
The project includes unit tests for:

Service layer logic

Controller endpoints

Repository operations

🌟 Key Technical Achievements
Implemented secure authentication with session management

Designed normalized database schema with JPA relationships

Used DTO pattern to prevent JSON serialization issues

Created responsive UI with custom CSS

Implemented global exception handling

Followed MVC architecture and separation of concerns

📝 License
This project is open source and available under the MIT License.

🤝 Contributing
Contributions, issues, and feature requests are welcome! Feel free to check issues page.

Note: This is a portfolio project demonstrating full-stack development skills with Spring Boot. Not intended for production use without additional security and performance enhancements.
