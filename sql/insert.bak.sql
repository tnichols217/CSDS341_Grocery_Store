INSERT INTO Grocery.dbo.barcode (manufacturer,gsPrefix,productCode,itemID,barcode) VALUES
	 (2345,401,67890,1,401234567890);
INSERT INTO Grocery.dbo.employee (id,fName,lName,contact,SSN,currentWage) VALUES
	 (1,N'Trevor',N'Nichols',12345,123,12),
	 (2,N'Ashley',N'Chen',12345,123,12);
INSERT INTO Grocery.dbo.item (id,name,currentPrice,supplier,unitType,discount,cachedCurrentStock,targetAmount) VALUES
	 (1,N'Oreos',5,1,N'quantity',0,-6,100),
	 (5,N'Banana',2,1,N'quantity',0,0,100),
	 (6,N'Tomato',2,2,N'quantity',0,0,100);
INSERT INTO Grocery.dbo.manufacturer (id,name,contact) VALUES
	 (100,N'Trevor',12345),
	 (2345,NULL,NULL);
INSERT INTO Grocery.dbo.restock (id,supplierID,status,orderDate,confirmDate,deliveryDate,restockDate,additionalCost) VALUES
	 (2,1,N'created','2024-11-27',NULL,NULL,NULL,10);
INSERT INTO Grocery.dbo.restockItem (restockID,itemID,quantity,expiryDate,unitCost,totalCost) VALUES
	 (2,1,100,'2025-01-27',5,0);
INSERT INTO Grocery.dbo.sale (id,employeeID,[timestamp],tip,paymentID) VALUES
	 (46,1,'2024-11-27 21:22:00.0',10,NULL),
	 (47,1,'2024-11-27 22:26:00.0',10,NULL),
	 (48,1,'2024-11-27 23:42:00.0',10,NULL);
INSERT INTO Grocery.dbo.saleItem (saleID,itemID,quantity,unitCost,discount,totalCost) VALUES
	 (46,1,2,5,0,0),
	 (47,1,2,5,0,0),
	 (48,1,2,5,0,0);
INSERT INTO Grocery.dbo.shift (employeeID,startTime,endTime,wage,paymentID,duration,totalWage) VALUES
	 (1,'2024-11-27 21:08:00.0','2024-11-27 21:08:00.0',12,NULL,0,0),
	 (1,'2024-11-27 21:13:00.0','2024-11-27 21:13:00.0',12,NULL,0,0),
	 (1,'2024-11-27 21:22:00.0','2024-11-27 21:22:00.0',12,NULL,0,0),
	 (1,'2024-11-27 21:34:00.0','2024-11-27 21:34:00.0',12,NULL,0,0),
	 (1,'2024-11-27 21:41:00.0','2024-11-27 21:41:00.0',12,NULL,0,0),
	 (1,'2024-11-27 22:14:00.0','2024-11-27 22:14:00.0',12,NULL,0,0),
	 (1,'2024-11-27 22:15:00.0','2024-11-27 22:15:00.0',12,NULL,0,0),
	 (1,'2024-11-27 22:26:00.0','2024-11-27 22:26:00.0',12,NULL,0,0),
	 (1,'2024-11-27 23:41:00.0','2024-11-27 23:43:00.0',12,NULL,2,0),
	 (1,'2024-11-27 23:49:00.0','2024-11-27 23:53:00.0',12,NULL,4,0);
INSERT INTO Grocery.dbo.supplier (id,name,contact) VALUES
	 (1,N'Trevor',12345),
	 (2,N'Trevor',12345);
