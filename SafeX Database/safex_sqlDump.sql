-- Create the database
CREATE DATABASE IF NOT EXISTS safex;

-- Use the database
USE safex;

-- Create the clients table
CREATE TABLE IF NOT EXISTS clients (
    ID INT NOT NULL AUTO_INCREMENT,
    FirstName VARCHAR(255) NOT NULL,
    LastName VARCHAR(255) NOT NULL,
    SafeXAddress VARCHAR(255) NOT NULL UNIQUE,
    Password VARCHAR(255) NOT NULL,
    Date DATE NOT NULL,
    PRIMARY KEY (ID)
);

-- Create the accounts table with a foreign key referencing clients
CREATE TABLE IF NOT EXISTS accounts (
    account_id INT NOT NULL AUTO_INCREMENT,
    ID INT,
    checking_acc DOUBLE DEFAULT 5000,
    savings_acc DOUBLE DEFAULT 5000,
    PRIMARY KEY (account_id),
    CONSTRAINT fk_clients FOREIGN KEY (ID) REFERENCES clients(ID) ON DELETE CASCADE
);

-- Create the admin table
CREATE TABLE IF NOT EXISTS admin (
    id INT NOT NULL AUTO_INCREMENT,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
);

-- Optional: Insert a sample admin user
INSERT INTO admin (username, password)
VALUES ('admin', '123456');

-- Insert values into clients table (ID auto-increments)
INSERT INTO clients (FirstName, LastName, SafeXAddress, Password, Date)
VALUES
('Kaif',    'Nazir',   'kaif@safex',    '123456', '2025-04-26'),
('Sara',    'Khan',    'sara@safex',    '123456', '2025-04-26'),
('Tanzeel', 'Akhtar',  'tanzeel@safex', '123456', '2025-04-26'),
('Aviral',  'Jain',    'aviral@safex',  '123456', '2025-04-26'),
('Raj',     'Verma',   'raj@safex',     '123456', '2025-04-26'),
('Priya',   'Sharma',  'priya@safex',   '123456', '2025-04-26'),
('Rohan',   'Mehta',   'rohan@safex',   '123456', '2025-04-26'),
('Ananya',  'Patel',   'ananya@safex',  '123456', '2025-04-26'),
('Vikram',  'Singh',   'vikram@safex',  '123456', '2025-04-26'),
('Sneha',   'Iyer',    'sneha@safex',   '123456', '2025-04-26'),
('Zara',    'Ali',     'zara@safex',    '123456', '2025-04-26'),
('Omar',    'Sheikh',  'omar@safex',    '123456', '2025-04-26'),
('Nashit',  'Khan',    'nashit@safex',  '123456', '2025-04-26'),
('Mira',  'Sinha',    'mira@safex',  '123456', '2025-04-26');

-- Insert values into accounts table
INSERT INTO accounts (account_id, ID, checking_acc, savings_acc)
VALUES
(1001,  1, 11000, 5000),
(1002,  2,  5000, 5000),
(1003,  3,  5000, 5000),
(1004,  4,  5000, 5000),
(1005,  5,  5000, 5000),
(1006,  6,  5000, 5000),
(1007,  7,  5000, 5000),
(1008,  8,  5000, 5000),
(1009,  9,  5000, 5000),
(1010, 10,  5000, 5000),
(1011, 11,  5123, 5000),
(1012, 12,  5000, 5000),
(1013, 13,  5000, 5000),
(1014, 14,  5000, 5000);
ALTER TABLE accounts AUTO_INCREMENT = 1015;





