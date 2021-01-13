drop database PoisePMS;
create database if not exists PoisePMS;

use PoisePMS;


create table erf(
physical_address varchar (100) NOT NULL,
ERF_number varchar(100),
primary key (physical_address)
);

drop table if exists architects;
create table architects(
architect_ID int NOT NULL,
architect_name varchar(50),
architect_number varchar(50),
architect_email varchar(100),
architect_address varchar(100),
primary key(architect_ID)
);

drop table if exists contractors;
create table contractors(
contractor_ID int NOT NULL,
contractor_name varchar(50),
contractor_number varchar(50),
contractor_email varchar(100),
contractor_address varchar(100),
primary key (contractor_ID)
);

drop table if exists customers;
create table customers(
customer_ID int NOT NULL,
customer_name varchar(50),
customer_number varchar(50),
customer_email varchar(100),
customer_address varchar(100),
primary key (customer_ID)
);

drop table if exists projects;
create table projects(
project_number int NOT NULL,
project_name varchar(50),
building_type varchar(50),
physical_address varchar(100),
project_total_fee float,
project_total_paid float,
project_architect_ID int,
project_contractor_ID int,
project_customer_ID int,
project_deadline date,
project_status varchar(50),
project_completion_date date,
primary key (project_number),
foreign key (physical_address) references erf (physical_address),
foreign key (project_architect_ID) references architects (architect_ID),
foreign key (project_contractor_ID) references contractors (contractor_ID),
foreign key (project_customer_ID) references customers (customer_ID)
);

insert into architects
	VALUES
	(1, "Nadia Botha", "0797854477", "nadiabotha@gmail.com", "78 Bayview Rd, Somerset West"),
	(2, "John Smith", "0834567788", "johnsmith@gmail.com", "47 Seaview Ave, Cape Town"),
	(3, "Brian Gaurino", "0876781247", "briangaurino@live.com", "95 Glen Street, Centurion");

insert into contractors
	VALUES
	(1, "Shaun Welsh", "0824786674", "shaunwelsh@live.co.za", "4191 Highland View Drive, Durban"),
	(2, "Rabia Holt", "0847627794", "rabiaholt@gmail.com", "47 Blair Court, Cape Town"),
	(3, "Debra Catoe", "0845227611", "debracatoe@live.com", "741 Kennedy Court, Hermanus");

insert into customers
	VALUES
	(1, "Stephanie Berg", "0876934411", "stepahnieberh@live.co.za", "81 Jerry Dove Drive, Bellville"),
	(2, "Jake Randle", "0795231178", "jakerandle@gmail.com", "47 Seaview Ave, Cape Town"),
	(3, "Rhoda Wright", "0842567122", "rhodawright@live.com", "88 Westville Ave, Stellenbosch");

insert into ERF
	VALUES
	("593 Terra Street, Johannesburg", "7854EE4574B"),
	("3787 Cantebury Drive, Witbank", "7855AB7864F"),
	("285 Fron Street, Worchester", "6872TR4584X" );

insert into projects
	VALUES
	(1, "House Randle", "House", "593 Terra Street, Johannesburg", 78500.60, 5500, 1, 1, 2, "2021-08-15", "Incomplete", NULL),
	(2, "Investmech Office", "Office", "3787 Cantebury Drive, Witbank", 250500.55, 10500.30, 3, 3, 1, "2020-12-01", "Incomplete", NULL),
	(3, "Corner Shop", "Shop", "285 Fron Street, Worchester", 120200.10, 25350.63, 2, 2, 3, "2021-03-23", "Finalized", "2021-01-08");

	
	

select * from architects;
select * from contractors;
select * from customers;
select * from erf;
select * from projects;