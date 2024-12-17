CREATE DATABASE logistics;
USE logistics;

CREATE TABLE Products (
    product_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    category VARCHAR(255),
    weight DECIMAL(10, 2),
    volume DECIMAL(10, 2),
    location VARCHAR(255)
);

CREATE TABLE Warehouse (
    warehouse_id INT AUTO_INCREMENT PRIMARY KEY,
    location VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL
);


CREATE TABLE ProductOperations (
    operation_id INT AUTO_INCREMENT PRIMARY KEY,
    product_id INT,
    warehouse_id INT,
    operation_type ENUM('приём', 'перемещение', 'списание') NOT NULL,
    quantity INT NOT NULL,
    operation_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (product_id) REFERENCES Products(product_id),
    FOREIGN KEY (warehouse_id) REFERENCES Warehouse(warehouse_id)
);

CREATE TABLE Users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role ENUM('ADMIN', 'USER') NOT NULL
);

CREATE TABLE LogMessages(
    log_id int AUTO_INCREMENT PRIMARY KEY,
    message VARCHAR(255) NOT NULL
);
