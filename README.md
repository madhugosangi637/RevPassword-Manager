🔐 Password Manager – Console Application
📌 Project Overview

The Password Manager is a secure console-based Java application built using Maven and Oracle (PL/SQL).
It allows users to securely store and manage passwords for multiple online accounts using a protected master account.

The application follows a modular layered architecture and implements security mechanisms like encryption, security questions, and verification logic.

🏗️ Project Structure
project
│
├── src/main/java
│   ├── com.revpm.dao
│   │     ├── UserDAO.java
│   │     ├── PasswordVaultDao.java
│   │     └── SecurityQuestionDao.java
│   │
│   ├── com.revpm.model
│   │     ├── User.java
│   │     ├── PasswordEntry.java
│   │     └── SecurityQuestion.java
│   │
│   ├── com.revpm.util
│   │     ├── DBUtil.java
│   │     ├── PasswordGenerator.java
│   │     ├── PasswordUtil.java
│   │     ├── OptUtil.java
│   │     └── TestDB.java
│   │
│   └── com.revpm.main
│         └── PasswordManagerApp.java
│
├── src/main/resources
│     └── log4j2.xml
│
├── docs
│     ├── erd
│     └── architecture
│
├── logs
│     └── app.log
│
├── pom.xml
└── README.md

🏛️ Application Architecture

The project follows a Layered Architecture:

Console (PasswordManagerApp)
        ↓
DAO Layer (UserDAO, PasswordVaultDao, SecurityQuestionDao)
        ↓
Oracle Database (PL/SQL)

🔹 Layer Responsibilities
1️⃣ Main Layer

Entry point: PasswordManagerApp.java

Handles menu navigation and user interaction

2️⃣ DAO Layer

UserDAO – Handles user registration, login, updates

PasswordVaultDao – Handles password CRUD operations

SecurityQuestionDao – Handles account recovery logic

3️⃣ Utility Layer

DBUtil – Oracle DB connection handling

PasswordGenerator – Generates strong passwords

PasswordUtil – Encryption / password validation

OptUtil – Input/output handling

TestDB – Database testing support

4️⃣ Database Layer

Oracle Database

PL/SQL procedures (if implemented)

🗄️ Database (Oracle – PL/SQL)
Main Tables:

USER

PASSWORD_VAULT

SECURITY_QUESTION

VERIFICATION_CODE (if implemented)

Relationships:

One User → Many Password Entries

One User → Many Security Questions

📂 ERD Diagram: docs/erd
📂 Architecture Diagram: docs/architecture

🔐 Features
👤 User Features

Register new account

Login with master password

Update profile details

Change master password

Forgot password using security questions

🔑 Password Vault Features

Add account credentials

View stored passwords

Update passwords

Delete passwords

Search by account name

🔒 Security Features

Encrypted password storage

Strong random password generation (via PasswordGenerator)

Logging using Log4j2

Secure DB connection via DBUtil

🛠️ Technologies Used

Java 8

Maven

Oracle Database

PL/SQL

JDBC

Log4j2

Eclipse IDE

▶️ How to Run the Project

Clone the repository

Open in Eclipse as Existing Maven Project

Configure Oracle DB connection in DBUtil.java

Ensure required tables are created in Oracle

Run PasswordManagerApp.java

📑 Logging

Application logs are stored in:

logs/app.log


Configured using:

src/main/resources/log4j2.xml

🎯 Definition of Done

✔ Working console-based application
✔ Maven project structure
✔ Oracle DB integration
✔ ERD Diagram included
✔ Architecture Diagram included
✔ Logging implemented
✔ Code pushed to GitHub

👨‍💻 Author

Madhu
Java | Oracle | Maven Project | Jdbc | Log4J | JUnit
