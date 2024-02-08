# Equipment Repair Management System

## Overview

Welcome to the Equipment Repair Management System! This application is designed to assist small equipment repair businesses in managing customer information, equipment details, inventory, jobs, expenses, and invoicing. With a user-friendly interface, this system streamlines your business operations and enhances your efficiency.

## Features

### 1. Customer Management

Easily create and maintain a database of your customers. Store their personal information, contact details, and any additional notes that may be relevant.

### 2. Equipment Management

Keep track of the equipment associated with each customer. Create entries for different machines, store specifications, and link them to the respective customers.

### 3. Inventory Management

Efficiently manage your inventory by storing information about available parts and their compatibility with different machines. This feature helps in quick reference and ensures accurate stock levels.

### 4. Job Tracking

Create and manage jobs seamlessly. Log details such as job description, date, and associated equipment. Record any expenses incurred during the job and add notes for reference.

### 5. Invoicing

Generate professional invoices for completed jobs directly from the system. This feature simplifies the bookkeeping process, making it easy to track income and expenses.

## Getting Started

### Installation

1. Create a MongoDB database and create a database in accordance with the "Create database section"
2. Add the connction string for the database to the DatabaseConnection class as the "uri" String  at src/main/jav/com/book/Controllers/DatabaseController.java
3. Compile the jar file by running mvn package
4. Install the latest jdk from the Oracle website
5. Run the .jar file and begin using the software
   

### Create Database
1. Start by creating a MongoDB database one pulic example is [Atlas](https://www.mongodb.com/cloud/atlas)
2. Then Create a database called CustomerBook
3. Then inside create the following collections [Customers, Equipment, Inventory, Jobs, Machines, Reports]
4. Once done get the connection string for the database to connect from the program and you are all set
