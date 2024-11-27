
DROP DATABASE Grocery;
CREATE DATABASE Grocery;

USE Grocery;

CREATE TABLE manufacturer (
    id INT PRIMARY KEY,
    name VARCHAR(255),
    contact INT
);

CREATE TABLE supplier (
    id INT IDENTITY(1,1) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    contact INT NOT NULL
);

CREATE TABLE item (
    id INT IDENTITY(1,1) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    currentPrice INT NOT NULL,
    supplier INT NOT NULL REFERENCES supplier(id),
    unitType VARCHAR(10) NOT NULL,
    discount INT DEFAULT 0,
    cachedCurrentStock INT DEFAULT 0,
    targetAmount INT DEFAULT 100,
    CHECK(unitType IN ('quantity', 'weight', 'volume'))
);

CREATE TABLE barcode (
    manufacturer INT REFERENCES manufacturer(id),
    gsPrefix INT,
    productCode INT,
    itemID INT NOT NULL REFERENCES item(id),
    barcode AS (
        CONVERT(BIGINT, CONCAT(gsPrefix, manufacturer, productCode))
    ),
    PRIMARY KEY (manufacturer, gsPrefix, productCode)
);

CREATE TABLE employee (
    id INT IDENTITY(1,1) PRIMARY KEY,
    fName VARCHAR(100) NOT NULL,
    lName VARCHAR(100) NOT NULL,
    contact INT NOT NULL,
    SSN INT NOT NULL,
    currentWage INT NOT NULL
);

CREATE TABLE payment (
    id INT IDENTITY(1,1) PRIMARY KEY,
    timestamp SMALLDATETIME DEFAULT CURRENT_TIMESTAMP,
    chequeNumber INT NOT NULL
);

CREATE TABLE sale (
    id INT IDENTITY(1,1) PRIMARY KEY,
    employeeID INT NOT NULL REFERENCES employee(id),
    timestamp SMALLDATETIME DEFAULT CURRENT_TIMESTAMP,
    tip INT DEFAULT 0,
    paymentID INT REFERENCES payment(id)
);

CREATE TABLE shift (
    employeeID INT REFERENCES employee(id),
    startTime SMALLDATETIME DEFAULT CURRENT_TIMESTAMP,
    endTime SMALLDATETIME DEFAULT CURRENT_TIMESTAMP,
    wage INT NOT NULL,
    paymentID INT REFERENCES payment(id),
    duration AS (
        DATEDIFF(MINUTE, startTime, endTime)
    ),
    totalWage AS (
        wage * DATEDIFF(MINUTE, startTime, endTime) / 60
    ),
    PRIMARY KEY (employeeID, startTime)
);

CREATE TABLE saleItem (
    saleID INT REFERENCES sale(id),
    itemID INT REFERENCES item(id),
    quantity INT NOT NULL,
    unitCost INT NOT NULL,
    discount INT NOT NULL,
    totalCost AS (
        quantity * unitCost * (100 - discount) / 100
    ),
    PRIMARY KEY (saleID, itemID)
);

CREATE TABLE restock (
    id INT IDENTITY(1,1) PRIMARY KEY,
    supplierID INT NOT NULL REFERENCES supplier(id),
    status VARCHAR(10) NOT NULL,
    orderDate DATE DEFAULT GETDATE(),
    confirmDate DATE,
    deliveryDate DATE,
    restockDate DATE,
    additionalCost INT NOT NULL,
    CHECK (status IN ('created', 'confirmed', 'delivered', 'restocked'))
);

CREATE TABLE restockItem (
    restockID INT REFERENCES restock(id),
    itemID INT REFERENCES item(id),
    quantity INT NOT NULL,
    expiryDate DATE NOT NULL,
    unitCost INT NOT NULL,
    totalCost AS (
        unitCost * quantity
    ),
    PRIMARY KEY (restockID, itemID)
);