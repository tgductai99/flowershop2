USE master;

CREATE DATABASE AsmJava5;
GO

USE AsmJava5;
GO

CREATE TABLE Categories (
    Id CHAR(4) PRIMARY KEY,
    Name NVARCHAR(50) NOT NULL
);

CREATE TABLE Accounts (
    Username NVARCHAR(50) PRIMARY KEY,
    Password NVARCHAR(50) NOT NULL,
    Fullname NVARCHAR(50),
    Email NVARCHAR(50),
    Photo NVARCHAR(50),
    Address NVARCHAR(255),
    Phone NVARCHAR(10),
    Activated BIT NOT NULL DEFAULT 1,
    Admin BIT NOT NULL DEFAULT 0
);

CREATE TABLE Products (
    Id INT IDENTITY(1,1) PRIMARY KEY,
    Name NVARCHAR(50) NOT NULL,
    Image NVARCHAR(50),
    Price FLOAT NOT NULL,
    CreateDate DATE DEFAULT GETDATE(),
    Available BIT NOT NULL DEFAULT 1,
    CategoryId CHAR(4) NOT NULL,
    Amount INT,

    CONSTRAINT FK_Products_Categories
        FOREIGN KEY (CategoryId)
        REFERENCES Categories(Id)
);

CREATE TABLE Orders (
    Id BIGINT IDENTITY(1,1) PRIMARY KEY,
    Username NVARCHAR(50) NOT NULL,
    CreateDate DATETIME DEFAULT GETDATE(),
    Address NVARCHAR(100),

    CONSTRAINT FK_Orders_Accounts
        FOREIGN KEY (Username)
        REFERENCES Accounts(Username)
);

CREATE TABLE OrderDetails (
    Id BIGINT IDENTITY(1,1) PRIMARY KEY,
    OrderId BIGINT NOT NULL,
    ProductId INT NOT NULL,
    Price FLOAT NOT NULL,
    Quantity INT NOT NULL,

    CONSTRAINT FK_OrderDetails_Orders
        FOREIGN KEY (OrderId)
        REFERENCES Orders(Id),

    CONSTRAINT FK_OrderDetails_Products
        FOREIGN KEY (ProductId)
        REFERENCES Products(Id)
);

CREATE TABLE Discounts (
    Id INT IDENTITY(1,1) PRIMARY KEY,
    ProductId INT NOT NULL,
    DiscountType NVARCHAR(20) NOT NULL,  -- 'PERCENT' or 'AMOUNT'
    DiscountValue FLOAT NOT NULL,         -- 10 = 10% or 10 currency units
    StartDate DATE NOT NULL,
    EndDate DATE NOT NULL,
    Active BIT NOT NULL DEFAULT 1,

    CONSTRAINT FK_Discounts_Products
        FOREIGN KEY (ProductId)
        REFERENCES Products(Id)
);


ALTER TABLE Orders
ADD Status VARCHAR(20) NOT NULL DEFAULT 'PENDING';

