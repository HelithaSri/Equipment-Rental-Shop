
  _    _      _ _ _   _              _____      _ 
 | |  | |    | (_) | | |            / ____|    (_)
 | |__| | ___| |_| |_| |__   __ _  | (___  _ __ _ 
 |  __  |/ _ \ | | __| '_ \ / _` |  \___ \| '__| |
 | |  | |  __/ | | |_| | | | (_| |  ____) | |  | |
 |_|  |_|\___|_|_|\__|_| |_|\__,_| |_____/|_|  |_|

  ______ ______ ______ ______ ______ ______ ______ 
 |______|______|______|______|______|______|______|
 

DROP DATABASE IF EXISTS Rent_System;
CREATE DATABASE IF NOT EXISTS Rent_System;
SHOW DATABASES ;
USE Rent_System;

DROP TABLE IF EXISTS Customer;
CREATE TABLE IF NOT EXISTS Customer(
   CustId VARCHAR(20),
   CustName VARCHAR(30),
   CustAddress VARCHAR(30),
   PhoneNumber VARCHAR(10),
   Nic VARCHAR(12),
   CONSTRAINT PRIMARY KEY (CustId)
);

DROP TABLE IF EXISTS Rent;
CREATE TABLE IF NOT EXISTS Rent(
	RentId VARCHAR(10),
	RentDate DATE,
	CusId VARCHAR(20),
	TOtal DECIMAL(10,2),
	CONSTRAINT PRIMARY KEY (RentId),
	CONSTRAINT FOREIGN KEY (CusId) REFERENCES Customer(CustId) ON DELETE CASCADE ON UPDATE CASCADE
);

DROP TABLE IF EXISTS Category;
CREATE TABLE IF NOT EXISTS Category(
	CategoryId VARCHAR(10),
	CategoryName VARCHAR(30),
	CONSTRAINT PRIMARY KEY (CategoryId)
);

DROP TABLE IF EXISTS BrandModel;
CREATE TABLE IF NOT EXISTS BrandModel(
	ModelId VARCHAR(10),
	ModelName VARCHAR(30),
	CONSTRAINT PRIMARY KEY (ModelId)
);

DROP TABLE IF EXISTS Rent_Item;
CREATE TABLE IF NOT EXISTS Rent_Item(
	SerialNumber VARCHAR(20),
	Description VARCHAR(50),
	RentPrice DECIMAL(10,2),
	Status VARCHAR(15),
	ModelId VARCHAR(10),
	CategoryId VARCHAR(10),
	CONSTRAINT PRIMARY KEY (SerialNumber),
	CONSTRAINT FOREIGN KEY (ModelId) REFERENCES BrandModel(ModelId) ON DELETE CASCADE ON UPDATE CASCADE,
	CONSTRAINT FOREIGN KEY (CategoryId) REFERENCES Category(CategoryId) ON DELETE CASCADE ON UPDATE CASCADE
);

DROP TABLE IF EXISTS RentDetail;
CREATE TABLE IF NOT EXISTS RentDetail(
	SerialNumber VARCHAR(20),
	RentId VARCHAR(10),
	RentDate DATE,
	ReturnDate DATE,
	Total DECIMAL(10,2),
	Status VARCHAR(15),
	CONSTRAINT PRIMARY KEY (SerialNumber,RentId),
	CONSTRAINT FOREIGN KEY (SerialNumber) REFERENCES Rent_Item(SerialNumber) ON DELETE CASCADE ON UPDATE CASCADE,
	CONSTRAINT FOREIGN KEY (RentId) REFERENCES Rent(RentId) ON DELETE CASCADE ON UPDATE CASCADE
);

DROP TABLE IF EXISTS Orders;
CREATE TABLE IF NOT EXISTS Orders(
	OrderId VARCHAR(10),
	OrderDate DATE,
	Total DECIMAL(10,2),
	CONSTRAINT PRIMARY KEY (OrderId)
);

DROP TABLE IF EXISTS Item;
CREATE TABLE IF NOT EXISTS Item(
	ItemId VARCHAR(10),
	Description VARCHAR(50),
	UnitPrice DECIMAL(10,2),
	QtyOnHand INT,
	ModelId VARCHAR(10),
	CategoryId VARCHAR(10),
	Discount DECIMAL(10,2),
	CONSTRAINT PRIMARY KEY (ItemId),
	CONSTRAINT FOREIGN KEY (ModelId) REFERENCES BrandModel(ModelId) ON DELETE CASCADE ON UPDATE CASCADE,
	CONSTRAINT FOREIGN KEY (CategoryId) REFERENCES Category(CategoryId) ON DELETE CASCADE ON UPDATE CASCADE
);

DROP TABLE IF EXISTS OrderDetail;
CREATE TABLE IF NOT EXISTS OrderDetail(
   OrderId VARCHAR(10),
   ItemId VARCHAR(10),
   OrderQty INT(11),
   Discount DECIMAL(10,2),
   Total DECIMAL(10,2),
   CONSTRAINT PRIMARY KEY (OrderId, ItemId),
   CONSTRAINT FOREIGN KEY (ItemId) REFERENCES Item(ItemId) ON DELETE CASCADE ON UPDATE CASCADE,
   CONSTRAINT FOREIGN KEY (OrderId) REFERENCES Orders(OrderId) ON DELETE CASCADE ON UPDATE CASCADE
);

DROP TABLE IF EXISTS `user`;
CREATE TABLE IF NOT EXISTS `user`(
	userName VARCHAR(10),
	password VARCHAR(100),
	name VARCHAR(50),
	address VARCHAR(50),
	dob DATE,
	role VARCHAR(5),
	CONSTRAINT PRIMARY KEY (userName)
);
