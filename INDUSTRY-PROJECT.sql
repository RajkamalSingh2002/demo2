-- Drop triggers
DROP TRIGGER get_service_price;
DROP TRIGGER handle_stock_notifications;
DROP TRIGGER users_insert_trigger;
DROP TRIGGER users_update_trigger;
DROP TRIGGER customers_insert_trigger;
DROP TRIGGER customers_update_trigger;
DROP TRIGGER customers_delete_trigger;
DROP TRIGGER vehicles_insert_trigger;
DROP TRIGGER vehicles_update_trigger;
DROP TRIGGER vehicles_delete_trigger;
DROP TRIGGER appointments_insert_trigger;
DROP TRIGGER appointments_update_trigger;
DROP TRIGGER appointments_delete_trigger;
DROP TRIGGER services_insert_trigger;
DROP TRIGGER services_update_trigger;
DROP TRIGGER services_delete_trigger;
DROP TRIGGER appointmentservices_insert_trigger;
DROP TRIGGER appointmentservices_update_trigger;
DROP TRIGGER appointmentservices_delete_trigger;
DROP TRIGGER inventory_insert_trigger;
DROP TRIGGER inventory_update_trigger;
DROP TRIGGER inventory_delete_trigger;
DROP TRIGGER invoices_insert_trigger;
DROP TRIGGER invoices_update_trigger;
DROP TRIGGER invoices_delete_trigger;

-- Drop procedures and function
DROP PROCEDURE insert_user;
DROP PROCEDURE update_user;
DROP PROCEDURE insert_customer;
DROP PROCEDURE update_customer;
DROP PROCEDURE delete_customer;
DROP PROCEDURE insert_vehicle;
DROP PROCEDURE update_vehicle;
DROP PROCEDURE delete_vehicle;
DROP PROCEDURE insert_appointment;
DROP PROCEDURE update_appointment;
DROP PROCEDURE delete_appointment;
DROP PROCEDURE insert_service;
DROP PROCEDURE update_service;
DROP PROCEDURE delete_service;
DROP PROCEDURE insert_appointment_service;
DROP PROCEDURE update_appointment_service;
DROP PROCEDURE delete_appointment_service;
DROP PROCEDURE insert_inventory;
DROP PROCEDURE update_inventory;
DROP PROCEDURE delete_inventory;
DROP PROCEDURE insert_invoice;
DROP PROCEDURE update_invoice;
DROP PROCEDURE delete_invoice;
DROP FUNCTION get_total_price;

-- Drop sequences
DROP SEQUENCE users_seq;
DROP SEQUENCE customers_seq;
DROP SEQUENCE appointments_seq;
DROP SEQUENCE services_seq;
DROP SEQUENCE inventory_seq;
DROP SEQUENCE invoices_seq;
DROP SEQUENCE notification_seq;
DROP SEQUENCE audit_seq;

-- Drop tables in order to respect foreign key constraints
DROP TABLE auditlog;
DROP TABLE low_stock_notifications;
DROP TABLE appointmentservices;
DROP TABLE invoices;
DROP TABLE appointments;
DROP TABLE customers;
DROP TABLE vehicles;
DROP TABLE services;
DROP TABLE inventory;
DROP TABLE users;
DROP TABLE roles;


CREATE TABLE users(
userid NUMBER(5),
name VARCHAR2(20),
password VARCHAR2(255),
roleid NUMBER(5),
CONSTRAINT pk_user_id PRIMARY KEY (userid),
CONSTRAINT fk_role_id FOREIGN KEY (roleid)
REFERENCES roles(roleid)
);



CREATE SEQUENCE users_seq
 START WITH 1
 INCREMENT BY 1
 NOCYCLE
 CACHE 20;
 
 
 
CREATE TABLE roles(
roleid NUMBER(5),
rolename VARCHAR2(20),
CONSTRAINT pk_role_id PRIMARY KEY (roleid)
);



CREATE OR REPLACE PROCEDURE insert_user(
v_name IN users.name%TYPE,
v_password IN users.password%TYPE,
v_role_id IN users.roleid%TYPE,
v_active IN users.active%TYPE
)
IS
v_count NUMBER;
BEGIN
SELECT COUNT(*)
INTO v_count
FROM roles
WHERE roleid = v_role_id;
IF v_count = 0 THEN
RAISE_APPLICATION_ERROR(-20009, 'Invalid roleid, no such role exists.');
END IF;
INSERT INTO users(userid, name, password, roleid,active)
VALUES (users_seq.NEXTVAL, v_name, v_password, v_role_id,v_active);
COMMIT;
DBMS_OUTPUT.PUT_LINE('User ' || v_name || ' has been inserted successfully.');
EXCEPTION
WHEN NO_DATA_FOUND THEN
DBMS_OUTPUT.PUT_LINE('Error: No data found for the given roleid.');
WHEN OTHERS THEN
DBMS_OUTPUT.PUT_LINE('An unexpected error occurred: ' || SQLERRM);
END insert_user;
/



CREATE OR REPLACE PROCEDURE update_user(p_user_id IN users.userid%TYPE,
p_name IN users.name%TYPE,p_password IN users.password%TYPE ,
p_role_id users.roleid%TYPE,p_active IN users.active%TYPE)
IS 
v_name users.name%TYPE;
v_password users.password%TYPE;
v_roleid users.roleid%TYPE;
v_count NUMBER;
v_active users.active%TYPE;
BEGIN
SELECT name,password,roleid,active INTO v_name,v_password,v_roleid,v_active FROM users WHERE userid=p_user_id;
IF p_name != v_name THEN
UPDATE users SET name=p_name WHERE userid=p_user_id;
END IF;
IF p_password != v_password THEN
UPDATE users SET password=p_password WHERE userid=p_user_id;
END IF;
SELECT COUNT(*) INTO v_count FROM roles WHERE roleid=p_role_id;
IF v_count = 0 THEN
RAISE_APPLICATION_ERROR(-20008,'Invalid roleid. This roleid does not exist');
END IF;
IF v_roleid != p_role_id THEN
UPDATE users SET roleid=p_role_id WHERE userid=p_user_id;
END IF;
IF v_active != p_active THEN
UPDATE users SET active=p_active WHERE userid=p_user_id;
END IF;
COMMIT;
DBMS_OUTPUT.PUT_LINE('successfully updated');
EXCEPTION
WHEN NO_DATA_FOUND THEN
DBMS_OUTPUT.PUT_LINE('User with the given id not found.');
WHEN OTHERS THEN
DBMS_OUTPUT.PUT_LINE('Error: ' || SQLERRM);
END update_user;
/
SET SERVEROUTPUT ON;
BEGIN
update_user(1,'div','123',3,'yes');

END;
/




CREATE TABLE customers(
customerid NUMBER(5),
name VARCHAR2(50),
email VARCHAR2(100),
contact NUMBER(20),
vin NUMBER(30),
address VARCHAR2(255),
CONSTRAINT pk_customer_id PRIMARY KEY (customerid),
CONSTRAINT fk_vin FOREIGN KEY (vin)
REFERENCES vehicles(vin)
);



ALTER TABLE customers DROP CONSTRAINT fk_vin;
ALTER TABLE customers ADD CONSTRAINT fk_vin FOREIGN KEY (vin) REFERENCES vehicles(vin) ON DELETE CASCADE;



CREATE SEQUENCE customers_seq
 START WITH 1
 INCREMENT BY 1
 NOCYCLE
 CACHE 20;
 
 
 
CREATE OR REPLACE PROCEDURE insert_customer(
v_name IN customers.name%TYPE,
v_email IN customers.email%TYPE,
v_contact IN customers.contact%TYPE,
v_vin IN customers.vin%TYPE,
v_address IN customers.address%TYPE
)
IS 
v_count NUMBER;
BEGIN 
-- Check if the VIN exists in vehicles table before inserting
SELECT COUNT(*) INTO v_count 
FROM vehicles 
WHERE vin = v_vin;
IF v_count = 0 THEN
RAISE_APPLICATION_ERROR(-20010, 'VIN does not exist in the vehicles table');
END IF;
-- Insert customer data
INSERT INTO customers(customerid, name, email, contact, vin, address)
VALUES(customers_seq.NEXTVAL, v_name, v_email, v_contact, v_vin, v_address);
DBMS_OUTPUT.PUT_LINE('Record inserted successfully');
COMMIT;
EXCEPTION 
WHEN OTHERS THEN
DBMS_OUTPUT.PUT_LINE('ERROR: ' || SQLERRM);
END insert_customer;
/

BEGIN
insert_customer('Rajkamal','r@gmail.com',123456789,123,'brampton');
END;
/


CREATE OR REPLACE PROCEDURE update_customer(p_id IN customers.customerid%TYPE,p_name IN customers.name%TYPE,
p_email IN customers.email%TYPE,
p_contact IN customers.contact%TYPE,
p_vin IN customers.vin%TYPE,
p_address IN customers.address%TYPE)
IS 
v_count NUMBER;
v_name customers.name%TYPE;
v_email customers.email%TYPE;
v_contact customers.contact%TYPE;
v_address customers.address%TYPE;
v_vin customers.vin%TYPE;
BEGIN
-- Check if the VIN exists in vehicles table before inserting
SELECT COUNT(*) INTO v_count 
FROM vehicles 
WHERE vin = p_vin;
IF v_count = 0 THEN
RAISE_APPLICATION_ERROR(-20011, 'VIN does not exist in the vehicles table');
END IF;
SELECT name,email,contact,vin,address INTO v_name,v_email,v_contact,v_vin,v_address FROM customers WHERE customerid=p_id;
IF v_name != p_name THEN
UPDATE customers SET name = p_name WHERE customerid=p_id;
END IF;
IF v_email != p_email THEN
UPDATE customers SET email = p_email WHERE customerid=p_id;
END IF;
IF v_contact != p_contact THEN
UPDATE customers SET contact = p_contact WHERE customerid=p_id;
END IF;
IF v_vin != p_vin THEN
UPDATE customers SET vin=p_vin WHERE customerid=p_id;
END IF;
IF v_address != p_address THEN
UPDATE customers SET address = p_address WHERE customerid=p_id;
END IF;
DBMS_OUTPUT.PUT_LINE('Updated Successfully');
COMMIT;
EXCEPTION
WHEN NO_DATA_FOUND THEN
DBMS_OUTPUT.PUT_LINE('No record found with the given id');
WHEN OTHERS THEN
DBMS_OUTPUT.PUT_LINE('ERROR:' || SQLERRM);
END update_customer;
/



CREATE OR REPLACE PROCEDURE delete_customer(v_id IN customers.customerid%TYPE)
IS 
BEGIN 
DELETE FROM customers WHERE customerid=v_id;
DBMS_OUTPUT.PUT_LINE('Deleted Successfully');
COMMIT;
EXCEPTION
WHEN NO_DATA_FOUND THEN
DBMS_OUTPUT.PUT_LINE('No record found with the given id');
WHEN OTHERS THEN
DBMS_OUTPUT.PUT_LINE('ERROR:' || SQLERRM);
END delete_customer;
/



CREATE TABLE vehicles(
vin NUMBER(20),
model VARCHAR2(30),
year NUMBER(10),
licenseplate VARCHAR2(30),
CONSTRAINT pk_vin PRIMARY KEY (vin)
);


CREATE OR REPLACE PROCEDURE insert_vehicle(v_vin IN vehicles.vin%TYPE,v_model IN vehicles.model%TYPE,v_year
IN vehicles.year%TYPE,v_licenseplate IN vehicles.licenseplate%TYPE)
IS 
BEGIN 
INSERT INTO vehicles(vin,model,year,licenseplate) VALUES(v_vin,v_model,v_year,v_licenseplate);
DBMS_OUTPUT.PUT_LINE('Record inserted successfully');
COMMIT;
EXCEPTION 
WHEN OTHERS THEN
DBMS_OUTPUT.PUT_LINE('ERROR:' || SQLERRM);
END insert_vehicle;
/



CREATE OR REPLACE PROCEDURE update_vehicle(p_vin IN vehicles.vin%TYPE,p_model IN vehicles.model%TYPE,
p_year IN vehicles.year%TYPE,
p_licenseplate IN vehicles.licenseplate%TYPE)
IS 
v_model vehicles.model%TYPE;
v_year vehicles.year%TYPE;
v_licenseplate vehicles.licenseplate%TYPE;
BEGIN 
SELECT model,year,licenseplate INTO v_model,v_year,v_licenseplate FROM vehicles WHERE vin=p_vin;
IF v_model != p_model THEN
UPDATE vehicles SET model = p_model WHERE vin=p_vin;
END IF;
IF v_year != p_year THEN
UPDATE vehicles SET year = p_year WHERE vin=p_vin;
END IF;
IF v_licenseplate != p_licenseplate THEN
UPDATE vehicles SET licenseplate = p_licenseplate WHERE vin=p_vin;
END IF;
DBMS_OUTPUT.PUT_LINE('Updated Successfully');
COMMIT;
EXCEPTION
WHEN NO_DATA_FOUND THEN
DBMS_OUTPUT.PUT_LINE('No record found with the given id');
WHEN OTHERS THEN
DBMS_OUTPUT.PUT_LINE('ERROR:' || SQLERRM);
END update_vehicle;
/


CREATE OR REPLACE PROCEDURE delete_vehicle(v_vin IN vehicles.vin%TYPE)
IS 
BEGIN 
DELETE FROM vehicles WHERE vin=v_vin;
DBMS_OUTPUT.PUT_LINE('Deleted Successfully');
COMMIT;
EXCEPTION
WHEN NO_DATA_FOUND THEN
DBMS_OUTPUT.PUT_LINE('No record found with the given id');
WHEN OTHERS THEN
DBMS_OUTPUT.PUT_LINE('ERROR:' || SQLERRM);
END delete_vehicle;
/



CREATE TABLE appointments (
    appointmentid NUMBER(10),
    customerid NUMBER(5),
    status VARCHAR2(50),
    appointmentdate DATE,
    assignedmechanic NUMBER(5),
    CONSTRAINT pk_appointment_id PRIMARY KEY (appointmentid),
    CONSTRAINT fk_customer_id FOREIGN KEY (customerid)
        REFERENCES customers(customerid),
    CONSTRAINT fk_assigned_mechanic FOREIGN KEY (assignedmechanic)
        REFERENCES users(userid)
);


ALTER TABLE appointments DROP COLUMN appointmentdate;
ALTER TABLE appointments ADD appointmentdate DATE DEFAULT SYSDATE;
ALTER TABLE appointments DROP CONSTRAINT fk_customer_id;
ALTER TABLE appointments DROP CONSTRAINT fk_assigned_mechanic;
ALTER TABLE appointments ADD CONSTRAINT fk_customer_id FOREIGN KEY (customerid)
REFERENCES customers(customerid) ON DELETE CASCADE;
ALTER TABLE appointments ADD CONSTRAINT fk_assigned_mechanic FOREIGN KEY (assignedmechanic)
REFERENCES users(userid)ON DELETE CASCADE;


CREATE SEQUENCE appointments_seq
START WITH 1
INCREMENT BY 1
NOCYCLE
CACHE 20;

CREATE OR REPLACE PROCEDURE insert_appointment(
v_customerid IN appointments.customerid%TYPE,
v_status IN appointments.status%TYPE,
v_assignedmechanic IN appointments.assignedmechanic%TYPE
)
IS
v_count NUMBER;
BEGIN
-- Check if customer exists
SELECT COUNT(*) INTO v_count FROM customers WHERE customerid = v_customerid;
IF v_count = 0 THEN
RAISE_APPLICATION_ERROR(-20001, 'Invalid customer ID, no such customer exists.');
END IF;
-- Check if assigned mechanic exists
SELECT COUNT(*) INTO v_count FROM users WHERE userid = v_assignedmechanic;
IF v_count = 0 THEN
RAISE_APPLICATION_ERROR(-20002, 'Invalid mechanic ID, no such user exists.');
END IF;
-- Insert appointment
INSERT INTO appointments(appointmentid, customerid, status, appointmentdate, assignedmechanic)
VALUES (appointments_seq.NEXTVAL, v_customerid, v_status, SYSDATE, v_assignedmechanic);    
COMMIT;
DBMS_OUTPUT.PUT_LINE('Appointment has been inserted successfully.');
EXCEPTION
WHEN OTHERS THEN
DBMS_OUTPUT.PUT_LINE('Error: ' || SQLERRM);
END insert_appointment;
/


CREATE OR REPLACE PROCEDURE update_appointment(
p_appointmentid IN appointments.appointmentid%TYPE,
p_status IN appointments.status%TYPE,
p_assignedmechanic IN appointments.assignedmechanic%TYPE
)
IS
v_status appointments.status%TYPE;
v_assignedmechanic appointments.assignedmechanic%TYPE;
v_count NUMBER;
BEGIN
-- Fetch current values
SELECT status, assignedmechanic INTO v_status, v_assignedmechanic FROM appointments 
WHERE appointmentid = p_appointmentid;
-- Update status if changed
IF p_status != v_status THEN
UPDATE appointments SET status = p_status WHERE appointmentid = p_appointmentid;
END IF;
-- Update assigned mechanic if changed
IF p_assignedmechanic != v_assignedmechanic THEN
SELECT COUNT(*) INTO v_count FROM users WHERE userid = p_assignedmechanic;
IF v_count = 0 THEN
RAISE_APPLICATION_ERROR(-20003, 'Invalid mechanic ID. This user does not exist.');
END IF;
UPDATE appointments SET assignedmechanic = p_assignedmechanic WHERE appointmentid = p_appointmentid;
END IF;
COMMIT;
DBMS_OUTPUT.PUT_LINE('Appointment updated successfully.');
EXCEPTION
WHEN NO_DATA_FOUND THEN
DBMS_OUTPUT.PUT_LINE('No appointment found with the given ID.');
WHEN OTHERS THEN
DBMS_OUTPUT.PUT_LINE('Error: ' || SQLERRM);
END update_appointment;
/



CREATE OR REPLACE PROCEDURE delete_appointment(v_appointmentid IN appointments.appointmentid%TYPE)
IS
BEGIN
DELETE FROM appointments WHERE appointmentid=v_appointmentid;
COMMIT;
DBMS_OUTPUT.PUT_LINE('Recored with this id deleted successfully');
EXCEPTION
WHEN NO_DATA_FOUND THEN
DBMS_OUTPUT.PUT_LINE('No appointment found with the given ID.');
WHEN OTHERS THEN
DBMS_OUTPUT.PUT_LINE('Error: ' || SQLERRM);
END delete_appointment;
/



CREATE TABLE services(
serviceid NUMBER(5),
name VARCHAR2(20),
description VARCHAR2(100),
price NUMBER(10),
CONSTRAINT pk_service_id PRIMARY KEY (serviceid)
);



ALTER TABLE services DROP COLUMN price;



CREATE SEQUENCE services_seq
START WITH 1
INCREMENT BY 1
NOCYCLE
CACHE 20;


CREATE OR REPLACE PROCEDURE insert_service(
v_name IN services.name%TYPE,
v_description IN services.description%TYPE
)
IS
BEGIN
INSERT INTO services(serviceid, name, description)
VALUES (services_seq.NEXTVAL, v_name, v_description);
COMMIT;
DBMS_OUTPUT.PUT_LINE('Service has been inserted successfully.');
EXCEPTION
WHEN OTHERS THEN
DBMS_OUTPUT.PUT_LINE('Error: ' || SQLERRM);
END insert_service;
/


CREATE OR REPLACE PROCEDURE update_service(
p_serviceid IN services.serviceid%TYPE,
p_name IN services.name%TYPE,
p_description IN services.description%TYPE
)
IS
v_name services.name%TYPE;
v_description services.description%TYPE;
BEGIN
-- Fetch current values
SELECT name, description INTO v_name, v_description FROM services WHERE serviceid = p_serviceid;
-- Update name if changed
IF p_name != v_name THEN
UPDATE services SET name = p_name WHERE serviceid = p_serviceid;
END IF;
-- Update description if changed
IF p_description != v_description THEN
UPDATE services SET description = p_description WHERE serviceid = p_serviceid;
END IF;    
COMMIT;
DBMS_OUTPUT.PUT_LINE('Service updated successfully.');
EXCEPTION
WHEN NO_DATA_FOUND THEN
DBMS_OUTPUT.PUT_LINE('No service found with the given ID.');
WHEN OTHERS THEN
DBMS_OUTPUT.PUT_LINE('Error: ' || SQLERRM);
END update_service;
/


CREATE OR REPLACE PROCEDURE delete_service(p_serviceid IN services.serviceid%TYPE)
IS
BEGIN 
DELETE FROM services WHERE serviceid = p_serviceid;
COMMIT;
DBMS_OUTPUT.PUT_LINE('Service has been deleted.');
EXCEPTION
WHEN NO_DATA_FOUND THEN
DBMS_OUTPUT.PUT_LINE('No service found with the given ID.');
WHEN OTHERS THEN
DBMS_OUTPUT.PUT_LINE('Error: ' || SQLERRM);
END delete_service;
/



CREATE TABLE appointmentservices (
    appointmentid NUMBER(10),
    serviceid NUMBER(5),
    itemid NUMBER(10),
    quantityused NUMBER(10),
    CONSTRAINT pk_appointmentservices PRIMARY KEY (appointmentid, serviceid),
    CONSTRAINT fk_appointment FOREIGN KEY (appointmentid)
        REFERENCES appointments(appointmentid) ON DELETE CASCADE,
    CONSTRAINT fk_service FOREIGN KEY (serviceid)
        REFERENCES services(serviceid) ON DELETE CASCADE,
    CONSTRAINT fk_items FOREIGN KEY (itemid)
    REFERENCES inventory(itemid)
);


ALTER TABLE appointmentservices ADD price NUMBER(10);
ALTER TABLE appointmentservices DROP CONSTRAINT fk_items;
ALTER TABLE appointmentservices ADD CONSTRAINT fk_items FOREIGN KEY (itemid)
REFERENCES inventory(itemid) ON DELETE CASCADE;


CREATE OR REPLACE TRIGGER get_service_price
BEFORE INSERT OR UPDATE ON appointmentservices
FOR EACH ROW
DECLARE
    v_item_price NUMBER(30);
BEGIN
    SELECT price
    INTO v_item_price
    FROM inventory
    WHERE itemid = :NEW.itemid;
    :NEW.price := v_item_price * 2 * :NEW.quantityused;
END;
/




CREATE OR REPLACE PROCEDURE insert_appointment_service(
v_appointmentid IN appointmentservices.appointmentid%TYPE,
v_serviceid IN appointmentservices.serviceid%TYPE,
v_itemid IN appointmentservices.itemid%TYPE,
v_quantityused IN appointmentservices.quantityused%TYPE
)
IS
BEGIN
INSERT INTO appointmentservices(appointmentid, serviceid, itemid, quantityused)
VALUES (v_appointmentid, v_serviceid, v_itemid, v_quantityused);
COMMIT;
DBMS_OUTPUT.PUT_LINE('Appointment service has been inserted successfully.');
EXCEPTION
WHEN NO_DATA_FOUND THEN
DBMS_OUTPUT.PUT_LINE('Error: Item ID does not exist.');
WHEN OTHERS THEN
DBMS_OUTPUT.PUT_LINE('Error: ' || SQLERRM);
END insert_appointment_service;
/



CREATE OR REPLACE PROCEDURE update_appointment_service(
p_appointmentid IN appointmentservices.appointmentid%TYPE,
p_serviceid IN appointmentservices.serviceid%TYPE,
p_itemid IN appointmentservices.itemid%TYPE,
p_quantityused IN appointmentservices.quantityused%TYPE
)
IS
v_count NUMBER;
BEGIN
-- Check if the record exists
SELECT COUNT(*) INTO v_count FROM appointmentservices 
WHERE appointmentid = p_appointmentid AND serviceid = p_serviceid;
IF v_count = 0 THEN
RAISE_APPLICATION_ERROR(-20004, 'No record found with the given appointment ID and service ID.');
END IF;
-- Update the record (price will be auto-calculated by the trigger)
UPDATE appointmentservices 
SET itemid = p_itemid, quantityused = p_quantityused
WHERE appointmentid = p_appointmentid AND serviceid = p_serviceid;
COMMIT;
DBMS_OUTPUT.PUT_LINE('Appointment service updated successfully.');
EXCEPTION
WHEN OTHERS THEN
DBMS_OUTPUT.PUT_LINE('Error: ' || SQLERRM);
END update_appointment_service;
/


CREATE OR REPLACE PROCEDURE delete_appointment_service(
p_appointmentid IN appointmentservices.appointmentid%TYPE,
p_serviceid IN appointmentservices.serviceid%TYPE
)
IS
v_count NUMBER;
BEGIN
-- Check if the record exists
SELECT COUNT(*) INTO v_count FROM appointmentservices 
WHERE appointmentid = p_appointmentid AND serviceid = p_serviceid;
IF v_count = 0 THEN
RAISE_APPLICATION_ERROR(-20005, 'No record found with the given appointment ID and service ID.');
END IF;
-- Delete the record
DELETE FROM appointmentservices 
WHERE appointmentid = p_appointmentid AND serviceid = p_serviceid;
COMMIT;
DBMS_OUTPUT.PUT_LINE('Appointment service deleted successfully.');
EXCEPTION
WHEN OTHERS THEN
DBMS_OUTPUT.PUT_LINE('Error: ' || SQLERRM);
END delete_appointment_service;
/



CREATE TABLE inventory(
itemid NUMBER(10),
name VARCHAR2(50),
quantity NUMBER(20),
price NUMBER(30),
CONSTRAINT pk_item_id PRIMARY KEY (itemid)
);



ALTER TABLE inventory ADD availablity VARCHAR2(10);


CREATE SEQUENCE inventory_seq
START WITH 1
INCREMENT BY 1
NOCYCLE
CACHE 20;



CREATE OR REPLACE PROCEDURE insert_inventory(
p_name IN inventory.name%TYPE,
p_quantity IN inventory.quantity%TYPE,
p_price IN inventory.price%TYPE
)
IS
BEGIN
INSERT INTO inventory(itemid, name, quantity, price, availablity)
VALUES (inventory_seq.NEXTVAL, p_name, p_quantity, p_price, 
CASE WHEN p_quantity > 0 THEN 'Available' ELSE 'Out of Stock' END);
COMMIT;
DBMS_OUTPUT.PUT_LINE('Inventory item "' || p_name || '" added successfully.');
EXCEPTION
WHEN OTHERS THEN
DBMS_OUTPUT.PUT_LINE('Error inserting inventory item: ' || SQLERRM);
END insert_inventory;
/

SET SERVEROUTPUT ON;
BEGIN
update_inventory(1,'Tire',0,10);
END;
/

CREATE OR REPLACE PROCEDURE update_inventory(
p_itemid IN inventory.itemid%TYPE,
p_name IN inventory.name%TYPE,
p_quantity IN inventory.quantity%TYPE,
p_price IN inventory.price%TYPE
)
IS
v_name inventory.name%TYPE;
v_quantity inventory.quantity%TYPE;
v_price inventory.price%TYPE;
v_availablity inventory.availablity%TYPE;
v_count NUMBER;
BEGIN
-- Check if the item exists
SELECT COUNT(*) INTO v_count FROM inventory WHERE itemid = p_itemid;    
IF v_count = 0 THEN
RAISE_APPLICATION_ERROR(-20006, 'No item found with the given ID.');
END IF;
-- Get current values
SELECT name, quantity, price, availablity
INTO v_name, v_quantity, v_price, v_availablity
FROM inventory WHERE itemid = p_itemid;
-- Update only if values are different
IF p_name != v_name THEN
UPDATE inventory SET name = p_name WHERE itemid = p_itemid;
END IF;
IF p_quantity != v_quantity THEN
UPDATE inventory SET quantity = p_quantity WHERE itemid = p_itemid;
END IF;
IF p_price != v_price THEN
UPDATE inventory SET price = p_price WHERE itemid = p_itemid;
END IF;
-- Update availability based on quantity
IF p_quantity != v_quantity OR v_availablity IS NULL THEN
UPDATE inventory 
SET availablity = CASE WHEN p_quantity > 0 THEN 'Available' ELSE 'Out of Stock' END
WHERE itemid = p_itemid;
END IF;
COMMIT;
DBMS_OUTPUT.PUT_LINE('Inventory item updated successfully.');
EXCEPTION
WHEN NO_DATA_FOUND THEN
DBMS_OUTPUT.PUT_LINE('Error: Inventory item not found.');
WHEN OTHERS THEN
DBMS_OUTPUT.PUT_LINE('Error updating inventory item: ' || SQLERRM);
END update_inventory;
/


CREATE OR REPLACE PROCEDURE delete_inventory(
p_itemid IN inventory.itemid%TYPE
)
IS
v_count NUMBER;
BEGIN
-- Check if the item exists
SELECT COUNT(*) INTO v_count FROM inventory WHERE itemid = p_itemid;
IF v_count = 0 THEN
RAISE_APPLICATION_ERROR(-20007, 'No inventory item found with the given ID.');
END IF;
-- Delete the item
DELETE FROM inventory WHERE itemid = p_itemid;
COMMIT;
DBMS_OUTPUT.PUT_LINE('Inventory item deleted successfully.');
EXCEPTION
WHEN OTHERS THEN
DBMS_OUTPUT.PUT_LINE('Error deleting inventory item: ' || SQLERRM);
END delete_inventory;
/







CREATE TABLE invoices (
invoiceid NUMBER(10),
totalprice NUMBER(20),
invoicedate DATE,
generatedby NUMBER(5),
appointmentid NUMBER(10),
CONSTRAINT pk_invoice_id PRIMARY KEY (invoiceid),
CONSTRAINT fk_appointment_id FOREIGN KEY (appointmentid)
REFERENCES appointments(appointmentid),
CONSTRAINT fk_generatedby FOREIGN KEY (generatedby)
REFERENCES users(userid)
);


ALTER TABLE invoices DROP COLUMN invoicedate;
ALTER TABLE invoices ADD invoicedate DATE DEFAULT SYSDATE;
ALTER TABLE invoices ADD paidornot VARCHAR2(10);
ALTER TABLE invoices DROP CONSTRAINT fk_appointment_id;
ALTER TABLE invoices DROP CONSTRAINT fk_generatedby;
ALTER TABLE invoices ADD CONSTRAINT fk_appointment_id FOREIGN KEY (appointmentid)
REFERENCES appointments(appointmentid) ON DELETE CASCADE;
ALTER TABLE invoices ADD CONSTRAINT fk_generatedby FOREIGN KEY (generatedby)
REFERENCES users(userid) ON DELETE CASCADE;


CREATE SEQUENCE invoices_seq
START WITH 1
INCREMENT BY 1
NOCYCLE
CACHE 20;

CREATE OR REPLACE FUNCTION get_total_price(p_appointment_id IN appointmentservices.appointmentid%TYPE)
RETURN NUMBER
IS
v_total_price NUMBER;
BEGIN
SELECT SUM(price) INTO v_total_price FROM appointmentservices WHERE appointmentid=p_appointment_id;
RETURN v_total_price;
EXCEPTION 
WHEN NO_DATA_FOUND THEN
RETURN 0;  
WHEN OTHERS THEN
RETURN NULL;  
END get_total_price;
/

CREATE OR REPLACE PROCEDURE insert_invoice(
v_generatedby IN invoices.generatedby%TYPE,
v_appointmentid IN invoices.appointmentid%TYPE,
v_paidornot IN invoices.paidornot%TYPE
)
IS 
v_total_price NUMBER(30);
v_count_appointment NUMBER;
v_count_generatedby NUMBER;
BEGIN
-- Check if the appointment exists
SELECT COUNT(*) INTO v_count_appointment 
FROM appointments 
WHERE appointmentid = v_appointmentid;
IF v_count_appointment = 0 THEN
RAISE_APPLICATION_ERROR(-20012, 'Appointment with the given ID does not exist.');
END IF;
-- Check if the generatedby (user) exists
SELECT COUNT(*) INTO v_count_generatedby 
FROM users 
WHERE userid = v_generatedby;
IF v_count_generatedby = 0 THEN
RAISE_APPLICATION_ERROR(-20013, 'User with the given generatedby ID does not exist.');
END IF;
-- Get total price from appointmentservices
v_total_price := get_total_price(v_appointmentid);
-- Insert invoice data
INSERT INTO invoices(invoiceid, totalprice, generatedby, appointmentid, paidornot)
VALUES(invoices_seq.NEXTVAL, v_total_price, v_generatedby, v_appointmentid, v_paidornot);
DBMS_OUTPUT.PUT_LINE('Inserted Successfully');
COMMIT;
EXCEPTION 
WHEN OTHERS THEN
DBMS_OUTPUT.PUT_LINE('ERROR: ' || SQLERRM);
END insert_invoice;
/


CREATE OR REPLACE PROCEDURE update_invoice(
p_invoiceid IN invoices.invoiceid%TYPE,
p_totalprice IN invoices.totalprice%TYPE,
p_generatedby IN invoices.generatedby%TYPE,
p_appointmentid IN invoices.appointmentid%TYPE,
p_paidornot IN invoices.paidornot%TYPE
)
IS
v_totalprice invoices.totalprice%TYPE;
v_generatedby invoices.generatedby%TYPE;
v_appointmentid invoices.appointmentid%TYPE;
v_paidornot invoices.paidornot%TYPE;
v_count_invoice NUMBER;
v_count_appointment NUMBER;
v_count_generatedby NUMBER;
BEGIN
-- Check if the invoice exists
SELECT COUNT(*) INTO v_count_invoice 
FROM invoices 
WHERE invoiceid = p_invoiceid;
IF v_count_invoice = 0 THEN
RAISE_APPLICATION_ERROR(-20014, 'Invoice with the given ID does not exist.');
END IF;
-- Check if the appointment exists
SELECT COUNT(*) INTO v_count_appointment 
FROM appointments 
WHERE appointmentid = p_appointmentid;
IF v_count_appointment = 0 THEN
RAISE_APPLICATION_ERROR(-20015, 'Appointment with the given ID does not exist.');
END IF;
-- Check if the generatedby (user) exists
SELECT COUNT(*) INTO v_count_generatedby 
FROM users 
WHERE userid = p_generatedby;
IF v_count_generatedby = 0 THEN
RAISE_APPLICATION_ERROR(-20016, 'User with the given generatedby ID does not exist.');
END IF;
-- Get current data for comparison
SELECT totalprice, generatedby, appointmentid, paidornot 
INTO v_totalprice, v_generatedby, v_appointmentid, v_paidornot 
FROM invoices 
WHERE invoiceid = p_invoiceid;
-- Update only if there's a change
IF v_totalprice != p_totalprice THEN
UPDATE invoices SET totalprice = p_totalprice WHERE invoiceid = p_invoiceid;
END IF;
IF v_generatedby != p_generatedby THEN
UPDATE invoices SET generatedby = p_generatedby WHERE invoiceid = p_invoiceid;
END IF;
IF v_appointmentid != p_appointmentid THEN
UPDATE invoices SET appointmentid = p_appointmentid WHERE invoiceid = p_invoiceid;
END IF;
IF v_paidornot != p_paidornot THEN
UPDATE invoices SET paidornot = p_paidornot WHERE invoiceid = p_invoiceid;
END IF;
DBMS_OUTPUT.PUT_LINE('Updated Successfully');
COMMIT;
EXCEPTION
WHEN NO_DATA_FOUND THEN
DBMS_OUTPUT.PUT_LINE('No record found with the given ID');
WHEN OTHERS THEN
DBMS_OUTPUT.PUT_LINE('ERROR: ' || SQLERRM);
END update_invoice;
/


CREATE OR REPLACE PROCEDURE delete_invoice(v_invoiceid IN invoices.invoiceid%TYPE)
IS 
BEGIN
DELETE FROM invoices WHERE invoiceid=v_invoiceid;
DBMS_OUTPUT.PUT_LINE('Deleted Successfully');
COMMIT;
EXCEPTION
WHEN NO_DATA_FOUND THEN
DBMS_OUTPUT.PUT_LINE('No record found with the given id');
WHEN OTHERS THEN
DBMS_OUTPUT.PUT_LINE('ERROR:' || SQLERRM);
END delete_invoice;
/




CREATE TABLE low_stock_notifications (
    notification_id NUMBER(10),
    itemid NUMBER(10),
    quantity NUMBER(20),
    notification_date DATE DEFAULT SYSDATE,
    CONSTRAINT pk_notification_id PRIMARY KEY(notification_id),
    CONSTRAINT fk_itme_id FOREIGN KEY(itemid)
    REFERENCES inventory(itemid) ON DELETE CASCADE
);

CREATE SEQUENCE notification_seq
START WITH 1
INCREMENT BY 1
NOCYCLE
CACHE 20;


CREATE OR REPLACE TRIGGER handle_stock_notifications
AFTER UPDATE ON inventory
FOR EACH ROW
BEGIN
-- If the quantity drops below 50 and was above or equal to 50, insert a low stock notification
IF :NEW.quantity < 50 AND :OLD.quantity >= 50 THEN
-- Insert notification record for low stock
INSERT INTO low_stock_notifications (notification_id, itemid,quantity, notification_date)
VALUES (notification_seq.NEXTVAL, :NEW.itemid, :NEW.quantity, SYSDATE);        
-- Output message
DBMS_OUTPUT.PUT_LINE('Low stock notification inserted for item id ' || :NEW.itemid || ' with quantity ' || :NEW.quantity);    
-- If the quantity goes above 50 and was below 50, delete the corresponding notification record
ELSIF :NEW.quantity >= 50 AND :OLD.quantity < 50 THEN
-- Delete the notification record for the item if the stock is now above 50
DELETE FROM low_stock_notifications
WHERE itemid = :NEW.itemid;        
-- Output message
DBMS_OUTPUT.PUT_LINE('Low stock notification deleted for item id ' || :NEW.itemid);
END IF;
END;
/

CREATE TABLE auditlog (
    logid NUMBER(38),         
    tablename VARCHAR2(100),         
    operation VARCHAR2(10),            
    recordid NUMBER,                   
    old_value CLOB,                   
    new_value CLOB,                    
    changedby NUMBER(5),          
    changedat DATE,
    CONSTRAINT pk_log_id PRIMARY KEY(logid),
    CONSTRAINT fk_changed_by FOREIGN KEY (changedby)
    REFERENCES users(userid)
);

ALTER TABLE auditlog DROP COLUMN changedby;
ALTER TABLE auditlog DROP CONSTRAINT fk_changed_by;
ALTER TABLE auditlog ADD CONSTRAINT fk_changed_by FOREIGN KEY (changedby)
REFERENCES users(userid) ON DELETE CASCADE;



CREATE SEQUENCE audit_seq
START WITH 1
INCREMENT BY 1
NOCYCLE
CACHE 20;



CREATE OR REPLACE TRIGGER users_insert_trigger
AFTER INSERT ON users
FOR EACH ROW
BEGIN
  INSERT INTO auditlog (logid, tablename, operation, recordid, old_value, new_value, changedat)
  VALUES (
    audit_seq.NEXTVAL, 
    'users', 
    'INSERT', 
    :NEW.userid, 
    NULL,  -- No old value for INSERT
    'Name: ' || :NEW.name || ', Role ID: ' || :NEW.roleid || ', Active: ' || :NEW.active,  -- new values
    SYSDATE
  );
END;
/




CREATE OR REPLACE TRIGGER users_update_trigger
AFTER UPDATE ON users
FOR EACH ROW
DECLARE
  v_changes VARCHAR2(4000);
BEGIN
  v_changes := 'Changes: ';

  -- Check if name was updated
  IF :OLD.name != :NEW.name THEN
    v_changes := v_changes || 'Name changed from ' || :OLD.name || ' to ' || :NEW.name || '. ';
  END IF;

  -- Check if password was updated
  IF :OLD.password != :NEW.password THEN
    v_changes := v_changes || 'Password changed. ';
  END IF;

  -- Check if role was updated
  IF :OLD.roleid != :NEW.roleid THEN
    v_changes := v_changes || 'Role ID changed from ' || :OLD.roleid || ' to ' || :NEW.roleid || '. ';
  END IF;

  -- Check if active status was updated
  IF :OLD.active != :NEW.active THEN
    v_changes := v_changes || 'Active status changed from ' || :OLD.active || ' to ' || :NEW.active || '. ';
  END IF;

  -- Only insert into auditlog if changes exist
  IF v_changes != 'Changes: ' THEN
    INSERT INTO auditlog (logid, tablename, operation, recordid, old_value, new_value, changedat)
    VALUES (
      audit_seq.NEXTVAL, 
      'users', 
      'UPDATE', 
      :NEW.userid, 
      'Name: ' || :OLD.name || ', Role ID: ' || :OLD.roleid || ', Active: ' || :OLD.active,  -- old values
      v_changes,  -- new values
      SYSDATE
    );
  END IF;
END;
/


CREATE OR REPLACE TRIGGER customers_insert_trigger
AFTER INSERT ON customers
FOR EACH ROW
BEGIN
  INSERT INTO auditlog (logid, tablename, operation, recordid, old_value, new_value, changedat)
  VALUES (
    audit_seq.NEXTVAL, 
    'customers', 
    'INSERT', 
    :NEW.customerid, 
    NULL,  -- No old value for INSERT
    'Name: ' || :NEW.name || ', Email: ' || :NEW.email || ', Contact: ' || :NEW.contact || ', VIN: ' || :NEW.vin || ', Address: ' || :NEW.address,  -- new values
    SYSDATE
  );
END;
/








CREATE OR REPLACE TRIGGER customers_update_trigger
AFTER UPDATE ON customers
FOR EACH ROW
DECLARE
  v_changes CLOB; -- Declare CLOB to store concatenated new values
BEGIN
  v_changes := TO_CLOB('Changes: ');  -- Initialize changes string

  -- Check if name was updated
  IF :OLD.name != :NEW.name THEN
    v_changes := v_changes || 'Name changed from ' || :OLD.name || ' to ' || :NEW.name || '. ';
  END IF;

  -- Check if email was updated
  IF :OLD.email != :NEW.email THEN
    v_changes := v_changes || 'Email changed from ' || :OLD.email || ' to ' || :NEW.email || '. ';
  END IF;

  -- Check if contact was updated
  IF :OLD.contact != :NEW.contact THEN
    v_changes := v_changes || 'Contact changed from ' || :OLD.contact || ' to ' || :NEW.contact || '. ';
  END IF;

  -- Check if vin was updated
  IF :OLD.vin != :NEW.vin THEN
    v_changes := v_changes || 'VIN changed from ' || :OLD.vin || ' to ' || :NEW.vin || '. ';
  END IF;

  -- Check if address was updated
  IF :OLD.address != :NEW.address THEN
    v_changes := v_changes || 'Address changed from ' || :OLD.address || ' to ' || :NEW.address || '. ';
  END IF;

  -- Only insert into auditlog if changes exist
  IF v_changes != TO_CLOB('Changes: ') THEN  -- Make sure there are actual changes
    INSERT INTO auditlog (
      logid, 
      tablename, 
      operation, 
      recordid, 
      old_value, 
      new_value, 
      changedat
    )
    VALUES (
      audit_seq.NEXTVAL, 
      'customers', 
      'UPDATE', 
      :NEW.customerid, 
      'Name: ' || :OLD.name || ', Email: ' || :OLD.email || ', Contact: ' || :OLD.contact || ', VIN: ' || :OLD.vin || ', Address: ' || :OLD.address,  -- old values
      v_changes,  -- concatenated new values
      SYSDATE
    );
  END IF;
END;
/





CREATE OR REPLACE TRIGGER customers_delete_trigger
AFTER DELETE ON customers
FOR EACH ROW
BEGIN
  INSERT INTO auditlog (logid, tablename, operation, recordid, old_value, new_value, changedat)
  VALUES (
    audit_seq.NEXTVAL, 
    'customers', 
    'DELETE', 
    :OLD.customerid, 
    'Name: ' || :OLD.name || ', Email: ' || :OLD.email || ', Contact: ' || :OLD.contact || ', VIN: ' || :OLD.vin || ', Address: ' || :OLD.address,  -- old values (before deletion)
    NULL,  -- No new value for DELETE
    SYSDATE
  );
END;
/


CREATE OR REPLACE TRIGGER vehicles_insert_trigger
AFTER INSERT ON vehicles
FOR EACH ROW
BEGIN
  INSERT INTO auditlog (logid, tablename, operation, recordid, old_value, new_value, changedat)
  VALUES (
    audit_seq.NEXTVAL, 
    'vehicles', 
    'INSERT', 
    :NEW.vin, 
    NULL,  -- No old value for INSERT
    'Model: ' || :NEW.model || ', Year: ' || :NEW.year || ', License Plate: ' || :NEW.licenseplate,  -- new values
    SYSDATE
  );
END;
/




CREATE OR REPLACE TRIGGER vehicles_update_trigger
AFTER UPDATE ON vehicles
FOR EACH ROW
DECLARE
  v_changes VARCHAR2(4000);
BEGIN
  v_changes := 'Changes: ';

  -- Check if model was updated
  IF :OLD.model != :NEW.model THEN
    v_changes := v_changes || 'Model changed from ' || :OLD.model || ' to ' || :NEW.model || '. ';
  END IF;

  -- Check if year was updated
  IF :OLD.year != :NEW.year THEN
    v_changes := v_changes || 'Year changed from ' || :OLD.year || ' to ' || :NEW.year || '. ';
  END IF;

  -- Check if license plate was updated
  IF :OLD.licenseplate != :NEW.licenseplate THEN
    v_changes := v_changes || 'License Plate changed from ' || :OLD.licenseplate || ' to ' || :NEW.licenseplate || '. ';
  END IF;

  -- Only insert into auditlog if changes exist
  IF v_changes != 'Changes: ' THEN
    INSERT INTO auditlog (logid, tablename, operation, recordid, old_value, new_value, changedat)
    VALUES (
      audit_seq.NEXTVAL, 
      'vehicles', 
      'UPDATE', 
      :NEW.vin, 
      'Model: ' || :OLD.model || ', Year: ' || :OLD.year || ', License Plate: ' || :OLD.licenseplate,  -- old values
      v_changes,  -- new values
      SYSDATE
    );
  END IF;
END;
/


CREATE OR REPLACE TRIGGER vehicles_delete_trigger
AFTER DELETE ON vehicles
FOR EACH ROW
BEGIN
  INSERT INTO auditlog (logid, tablename, operation, recordid, old_value, new_value, changedat)
  VALUES (
    audit_seq.NEXTVAL, 
    'vehicles', 
    'DELETE', 
    :OLD.vin, 
    'Model: ' || :OLD.model || ', Year: ' || :OLD.year || ', License Plate: ' || :OLD.licenseplate,  -- old values (before deletion)
    NULL,  -- No new value for DELETE
    SYSDATE
  );
END;
/


-- Trigger for INSERT operation on appointments table
CREATE OR REPLACE TRIGGER appointments_insert_trigger
AFTER INSERT ON appointments
FOR EACH ROW
BEGIN
  INSERT INTO auditlog (logid, tablename, operation, recordid, old_value, new_value, changedat)
  VALUES (
    audit_seq.NEXTVAL, 
    'appointments', 
    'INSERT', 
    :NEW.appointmentid, 
    NULL,  -- No old value for INSERT
    'Customer ID: ' || :NEW.customerid || ', Status: ' || :NEW.status || ', Appointment Date: ' || :NEW.appointmentdate || ', Assigned Mechanic: ' || :NEW.assignedmechanic,  -- new values
    SYSDATE
  );
END;
/

-- Trigger for UPDATE operation on appointments table
CREATE OR REPLACE TRIGGER appointments_update_trigger
AFTER UPDATE ON appointments
FOR EACH ROW
DECLARE
  v_changes VARCHAR2(4000);
BEGIN
  v_changes := 'Changes: ';

  -- Check if customer id was updated
  IF :OLD.customerid != :NEW.customerid THEN
    v_changes := v_changes || 'Customer ID changed from ' || :OLD.customerid || ' to ' || :NEW.customerid || '. ';
  END IF;

  -- Check if status was updated
  IF :OLD.status != :NEW.status THEN
    v_changes := v_changes || 'Status changed from ' || :OLD.status || ' to ' || :NEW.status || '. ';
  END IF;

  -- Check if appointment date was updated
  IF :OLD.appointmentdate != :NEW.appointmentdate THEN
    v_changes := v_changes || 'Appointment Date changed from ' || :OLD.appointmentdate || ' to ' || :NEW.appointmentdate || '. ';
  END IF;

  -- Check if assigned mechanic was updated
  IF :OLD.assignedmechanic != :NEW.assignedmechanic THEN
    v_changes := v_changes || 'Assigned Mechanic changed from ' || :OLD.assignedmechanic || ' to ' || :NEW.assignedmechanic || '. ';
  END IF;

  -- Only insert into auditlog if changes exist
  IF v_changes != 'Changes: ' THEN
    INSERT INTO auditlog (logid, tablename, operation, recordid, old_value, new_value, changedat)
    VALUES (
      audit_seq.NEXTVAL, 
      'appointments', 
      'UPDATE', 
      :NEW.appointmentid, 
      'Customer ID: ' || :OLD.customerid || ', Status: ' || :OLD.status || ', Appointment Date: ' || :OLD.appointmentdate || ', Assigned Mechanic: ' || :OLD.assignedmechanic,  -- old values
      v_changes,  -- new values
      SYSDATE
    );
  END IF;
END;
/

-- Trigger for DELETE operation on appointments table
CREATE OR REPLACE TRIGGER appointments_delete_trigger
AFTER DELETE ON appointments
FOR EACH ROW
BEGIN
  INSERT INTO auditlog (logid, tablename, operation, recordid, old_value, new_value, changedat)
  VALUES (
    audit_seq.NEXTVAL, 
    'appointments', 
    'DELETE', 
    :OLD.appointmentid, 
    'Customer ID: ' || :OLD.customerid || ', Status: ' || :OLD.status || ', Appointment Date: ' || :OLD.appointmentdate || ', Assigned Mechanic: ' || :OLD.assignedmechanic,  -- old values (before deletion)
    NULL,  -- No new value for DELETE
    SYSDATE
  );
END;
/










-- Trigger for INSERT operation on services table
CREATE OR REPLACE TRIGGER services_insert_trigger
AFTER INSERT ON services
FOR EACH ROW
BEGIN
  INSERT INTO auditlog (logid, tablename, operation, recordid, old_value, new_value, changedat)
  VALUES (
    audit_seq.NEXTVAL, 
    'services', 
    'INSERT', 
    :NEW.serviceid, 
    NULL,  -- No old value for INSERT
    'Name: ' || :NEW.name || ', Description: ' || :NEW.description,  -- new values
    SYSDATE
  );
END;
/

-- Trigger for UPDATE operation on services table
CREATE OR REPLACE TRIGGER services_update_trigger
AFTER UPDATE ON services
FOR EACH ROW
DECLARE
  v_changes VARCHAR2(4000);
BEGIN
  v_changes := 'Changes: ';

  -- Check if name was updated
  IF :OLD.name != :NEW.name THEN
    v_changes := v_changes || 'Name changed from ' || :OLD.name || ' to ' || :NEW.name || '. ';
  END IF;

  -- Check if description was updated
  IF :OLD.description != :NEW.description THEN
    v_changes := v_changes || 'Description changed from ' || :OLD.description || ' to ' || :NEW.description || '. ';
  END IF;

  -- Only insert into auditlog if changes exist
  IF v_changes != 'Changes: ' THEN
    INSERT INTO auditlog (logid, tablename, operation, recordid, old_value, new_value, changedat)
    VALUES (
      audit_seq.NEXTVAL, 
      'services', 
      'UPDATE', 
      :NEW.serviceid, 
      'Name: ' || :OLD.name || ', Description: ' || :OLD.description,  -- old values
      v_changes,  -- new values
      SYSDATE
    );
  END IF;
END;
/

-- Trigger for DELETE operation on services table
CREATE OR REPLACE TRIGGER services_delete_trigger
AFTER DELETE ON services
FOR EACH ROW
BEGIN
  INSERT INTO auditlog (logid, tablename, operation, recordid, old_value, new_value, changedat)
  VALUES (
    audit_seq.NEXTVAL, 
    'services', 
    'DELETE', 
    :OLD.serviceid, 
    'Name: ' || :OLD.name || ', Description: ' || :OLD.description,  -- old values (before deletion)
    NULL,  -- No new value for DELETE
    SYSDATE
  );
END;
/






-- Trigger for INSERT operation on appointmentservices table
CREATE OR REPLACE TRIGGER appointmentservices_insert_trigger
AFTER INSERT ON appointmentservices
FOR EACH ROW
BEGIN
  INSERT INTO auditlog (logid, tablename, operation, recordid, old_value, new_value, changedat)
  VALUES (
    audit_seq.NEXTVAL, 
    'appointmentservices', 
    'INSERT', 
    :NEW.appointmentid, 
    NULL,  -- No old value for INSERT
    'Service ID: ' || :NEW.serviceid || ', Item ID: ' || :NEW.itemid || ', Quantity Used: ' || :NEW.quantityused || ', Price: ' || :NEW.price,  -- new values
    SYSDATE
  );
END;
/

-- Trigger for UPDATE operation on appointmentservices table
CREATE OR REPLACE TRIGGER appointmentservices_update_trigger
AFTER UPDATE ON appointmentservices
FOR EACH ROW
DECLARE
  v_changes VARCHAR2(4000);
BEGIN
  v_changes := 'Changes: ';

  -- Check if quantityused was updated
  IF :OLD.quantityused != :NEW.quantityused THEN
    v_changes := v_changes || 'Quantity Used changed from ' || :OLD.quantityused || ' to ' || :NEW.quantityused || '. ';
  END IF;

  -- Check if price was updated
  IF :OLD.price != :NEW.price THEN
    v_changes := v_changes || 'Price changed from ' || :OLD.price || ' to ' || :NEW.price || '. ';
  END IF;

  -- Only insert into auditlog if changes exist
  IF v_changes != 'Changes: ' THEN
    INSERT INTO auditlog (logid, tablename, operation, recordid, old_value, new_value, changedat)
    VALUES (
      audit_seq.NEXTVAL, 
      'appointmentservices', 
      'UPDATE', 
      :NEW.appointmentid, 
      'Service ID: ' || :OLD.serviceid || ', Item ID: ' || :OLD.itemid || ', Quantity Used: ' || :OLD.quantityused || ', Price: ' || :OLD.price,  -- old values
      v_changes,  -- new values
      SYSDATE
    );
  END IF;
END;
/

-- Trigger for DELETE operation on appointmentservices table
CREATE OR REPLACE TRIGGER appointmentservices_delete_trigger
AFTER DELETE ON appointmentservices
FOR EACH ROW
BEGIN
  INSERT INTO auditlog (logid, tablename, operation, recordid, old_value, new_value, changedat)
  VALUES (
    audit_seq.NEXTVAL, 
    'appointmentservices', 
    'DELETE', 
    :OLD.appointmentid, 
    'Service ID: ' || :OLD.serviceid || ', Item ID: ' || :OLD.itemid || ', Quantity Used: ' || :OLD.quantityused || ', Price: ' || :OLD.price,  -- old values (before deletion)
    NULL,  -- No new value for DELETE
    SYSDATE
  );
END;
/






-- Trigger for INSERT operation on inventory table
CREATE OR REPLACE TRIGGER inventory_insert_trigger
AFTER INSERT ON inventory
FOR EACH ROW
BEGIN
  INSERT INTO auditlog (logid, tablename, operation, recordid, old_value, new_value, changedat)
  VALUES (
    audit_seq.NEXTVAL, 
    'inventory', 
    'INSERT', 
    :NEW.itemid, 
    NULL,  -- No old value for INSERT
    'Name: ' || :NEW.name || ', Quantity: ' || :NEW.quantity || ', Price: ' || :NEW.price || ', Availability: ' || :NEW.availablity,  -- new values
    SYSDATE
  );
END;
/

-- Trigger for UPDATE operation on inventory table
CREATE OR REPLACE TRIGGER inventory_update_trigger
AFTER UPDATE ON inventory
FOR EACH ROW
DECLARE
  v_changes VARCHAR2(4000);
BEGIN
  v_changes := 'Changes: ';

  -- Check if quantity was updated
  IF :OLD.quantity != :NEW.quantity THEN
    v_changes := v_changes || 'Quantity changed from ' || :OLD.quantity || ' to ' || :NEW.quantity || '. ';
  END IF;

  -- Check if price was updated
  IF :OLD.price != :NEW.price THEN
    v_changes := v_changes || 'Price changed from ' || :OLD.price || ' to ' || :NEW.price || '. ';
  END IF;

  -- Check if availability was updated
  IF :OLD.availablity != :NEW.availablity THEN
    v_changes := v_changes || 'Availability changed from ' || :OLD.availablity || ' to ' || :NEW.availablity || '. ';
  END IF;

  -- Only insert into auditlog if changes exist
  IF v_changes != 'Changes: ' THEN
    INSERT INTO auditlog (logid, tablename, operation, recordid, old_value, new_value, changedat)
    VALUES (
      audit_seq.NEXTVAL, 
      'inventory', 
      'UPDATE', 
      :NEW.itemid, 
      'Name: ' || :OLD.name || ', Quantity: ' || :OLD.quantity || ', Price: ' || :OLD.price || ', Availability: ' || :OLD.availablity,  -- old values
      v_changes,  -- new values
      SYSDATE
    );
  END IF;
END;
/

-- Trigger for DELETE operation on inventory table
CREATE OR REPLACE TRIGGER inventory_delete_trigger
AFTER DELETE ON inventory
FOR EACH ROW
BEGIN
  INSERT INTO auditlog (logid, tablename, operation, recordid, old_value, new_value, changedat)
  VALUES (
    audit_seq.NEXTVAL, 
    'inventory', 
    'DELETE', 
    :OLD.itemid, 
    'Name: ' || :OLD.name || ', Quantity: ' || :OLD.quantity || ', Price: ' || :OLD.price || ', Availability: ' || :OLD.availablity,  -- old values (before deletion)
    NULL,  -- No new value for DELETE
    SYSDATE
  );
END;
/





-- Trigger for INSERT operation on invoices table
CREATE OR REPLACE TRIGGER invoices_insert_trigger
AFTER INSERT ON invoices
FOR EACH ROW
BEGIN
  INSERT INTO auditlog (logid, tablename, operation, recordid, old_value, new_value, changedat)
  VALUES (
    audit_seq.NEXTVAL, 
    'invoices', 
    'INSERT', 
    :NEW.invoiceid, 
    NULL,  -- No old value for INSERT
    'TotalPrice: ' || :NEW.totalprice || ', InvoiceDate: ' || :NEW.invoicedate || ', PaidOrNot: ' || :NEW.paidornot,  -- new values
    SYSDATE
  );
END;
/

-- Trigger for UPDATE operation on invoices table
CREATE OR REPLACE TRIGGER invoices_update_trigger
AFTER UPDATE ON invoices
FOR EACH ROW
DECLARE
  v_changes VARCHAR2(4000);
BEGIN
  v_changes := 'Changes: ';

  -- Check if totalprice was updated
  IF :OLD.totalprice != :NEW.totalprice THEN
    v_changes := v_changes || 'TotalPrice changed from ' || :OLD.totalprice || ' to ' || :NEW.totalprice || '. ';
  END IF;

  -- Check if paidornot was updated
  IF :OLD.paidornot != :NEW.paidornot THEN
    v_changes := v_changes || 'PaidOrNot changed from ' || :OLD.paidornot || ' to ' || :NEW.paidornot || '. ';
  END IF;

  -- Only insert into auditlog if changes exist
  IF v_changes != 'Changes: ' THEN
    INSERT INTO auditlog (logid, tablename, operation, recordid, old_value, new_value, changedat)
    VALUES (
      audit_seq.NEXTVAL, 
      'invoices', 
      'UPDATE', 
      :NEW.invoiceid, 
      'TotalPrice: ' || :OLD.totalprice || ', InvoiceDate: ' || :OLD.invoicedate || ', PaidOrNot: ' || :OLD.paidornot,  -- old values
      v_changes,  -- new values
      SYSDATE
    );
  END IF;
END;
/

-- Trigger for DELETE operation on invoices table
CREATE OR REPLACE TRIGGER invoices_delete_trigger
AFTER DELETE ON invoices
FOR EACH ROW
BEGIN
  INSERT INTO auditlog (logid, tablename, operation, recordid, old_value, new_value, changedat)
  VALUES (
    audit_seq.NEXTVAL, 
    'invoices', 
    'DELETE', 
    :OLD.invoiceid, 
    'TotalPrice: ' || :OLD.totalprice || ', InvoiceDate: ' || :OLD.invoicedate || ', PaidOrNot: ' || :OLD.paidornot,  -- old values (before deletion)
    NULL,  -- No new value for DELETE
    SYSDATE
  );
END;
/

COMMIT;
 

 
 
 
 -- Procedure to get details for a specific customer.
CREATE OR REPLACE PROCEDURE get_customer(
    v_id IN customers.customerid%TYPE
) IS
    v_name    customers.name%TYPE;
    v_email   customers.email%TYPE;
    v_contact customers.contact%TYPE;
    v_vin     customers.vin%TYPE;
    v_address customers.address%TYPE;
BEGIN
    SELECT name, email, contact, vin, address
      INTO v_name, v_email, v_contact, v_vin, v_address
      FROM customers
     WHERE customerid = v_id;
    DBMS_OUTPUT.PUT_LINE('Customer ID: ' || v_id);
    DBMS_OUTPUT.PUT_LINE('Name: ' || v_name);
    DBMS_OUTPUT.PUT_LINE('Email: ' || v_email);
    DBMS_OUTPUT.PUT_LINE('Contact: ' || v_contact);
    DBMS_OUTPUT.PUT_LINE('VIN: ' || v_vin);
    DBMS_OUTPUT.PUT_LINE('Address: ' || v_address);
EXCEPTION
    WHEN NO_DATA_FOUND THEN
        DBMS_OUTPUT.PUT_LINE('No customer found with ID ' || v_id);
    WHEN OTHERS THEN
        DBMS_OUTPUT.PUT_LINE('Error: ' || SQLERRM);
END;
/
 
-- Procedure to list all customers.
CREATE OR REPLACE PROCEDURE get_all_customers IS
BEGIN
    FOR rec IN (SELECT customerid, name, email, contact, vin, address FROM customers) LOOP
        DBMS_OUTPUT.PUT_LINE('Customer ID: ' || rec.customerid || ' | Name: ' || rec.name ||
                              ' | Email: ' || rec.email || ' | Contact: ' || rec.contact ||
                              ' | VIN: ' || rec.vin || ' | Address: ' || rec.address);
    END LOOP;
    IF SQL%ROWCOUNT = 0 THEN
        DBMS_OUTPUT.PUT_LINE('No customers found.');
    END IF;
EXCEPTION
    WHEN OTHERS THEN
        DBMS_OUTPUT.PUT_LINE('Error: ' || SQLERRM);
END;
/







-- For Vehicles Management: Get Specific and List All Vehicles

CREATE OR REPLACE PROCEDURE get_vehicle(
  v_vin IN vehicles.vin%TYPE
)
IS 
  v_model vehicles.model%TYPE;
  v_year vehicles.year%TYPE;
  v_license vehicles.licenseplate%TYPE;
BEGIN
  SELECT model, year, licenseplate
    INTO v_model, v_year, v_license
    FROM vehicles
    WHERE vin = v_vin;
  DBMS_OUTPUT.PUT_LINE('VIN: ' || v_vin || ' | Model: ' || v_model ||
                        ' | Year: ' || v_year || ' | License Plate: ' || v_license);
EXCEPTION
  WHEN NO_DATA_FOUND THEN
    DBMS_OUTPUT.PUT_LINE('No vehicle found with VIN ' || v_vin);
  WHEN OTHERS THEN
    DBMS_OUTPUT.PUT_LINE('Error in get_vehicle: ' || SQLERRM);
END;
/

CREATE OR REPLACE PROCEDURE get_all_vehicles IS
BEGIN
  FOR rec IN (SELECT vin, model, year, licenseplate FROM vehicles) LOOP
    DBMS_OUTPUT.PUT_LINE('VIN: ' || rec.vin || ' | Model: ' || rec.model ||
                         ' | Year: ' || rec.year || ' | License Plate: ' || rec.licenseplate);
  END LOOP;
  IF SQL%ROWCOUNT = 0 THEN
    DBMS_OUTPUT.PUT_LINE('No vehicles found.');
  END IF;
EXCEPTION
  WHEN OTHERS THEN
    DBMS_OUTPUT.PUT_LINE('Error in get_all_vehicles: ' || SQLERRM);
END;
/

-- For Appointments Management: Get Specific and List All Appointments

CREATE OR REPLACE PROCEDURE get_appointment(
  p_appointmentid IN appointments.appointmentid%TYPE
)
IS 
  v_customer appointments.customerid%TYPE;
  v_status appointments.status%TYPE;
  v_date appointments.appointmentdate%TYPE;
  v_mechanic appointments.assignedmechanic%TYPE;
BEGIN
  SELECT customerid, status, appointmentdate, assignedmechanic
    INTO v_customer, v_status, v_date, v_mechanic
    FROM appointments
    WHERE appointmentid = p_appointmentid;
  DBMS_OUTPUT.PUT_LINE('Appointment ID: ' || p_appointmentid ||
                        ' | Customer ID: ' || v_customer ||
                        ' | Status: ' || v_status ||
                        ' | Date: ' || v_date ||
                        ' | Assigned Mechanic: ' || v_mechanic);
EXCEPTION
  WHEN NO_DATA_FOUND THEN
    DBMS_OUTPUT.PUT_LINE('No appointment found with ID ' || p_appointmentid);
  WHEN OTHERS THEN
    DBMS_OUTPUT.PUT_LINE('Error in get_appointment: ' || SQLERRM);
END;
/

CREATE OR REPLACE PROCEDURE get_all_appointments IS
BEGIN
  FOR rec IN (SELECT appointmentid, customerid, status, appointmentdate, assignedmechanic FROM appointments) LOOP
    DBMS_OUTPUT.PUT_LINE('Appointment ID: ' || rec.appointmentid ||
                         ' | Customer ID: ' || rec.customerid ||
                         ' | Status: ' || rec.status ||
                         ' | Date: ' || rec.appointmentdate ||
                         ' | Assigned Mechanic: ' || rec.assignedmechanic);
  END LOOP;
  IF SQL%ROWCOUNT = 0 THEN
    DBMS_OUTPUT.PUT_LINE('No appointments found.');
  END IF;
EXCEPTION
  WHEN OTHERS THEN
    DBMS_OUTPUT.PUT_LINE('Error in get_all_appointments: ' || SQLERRM);
END;
/

-- For Appointment Services Management: Get Specific and List All

CREATE OR REPLACE PROCEDURE get_appointment_service(
  p_appointmentid IN appointmentservices.appointmentid%TYPE,
  p_serviceid IN appointmentservices.serviceid%TYPE
)
IS 
  v_item appointmentservices.itemid%TYPE;
  v_quantity appointmentservices.quantityused%TYPE;
  v_price appointmentservices.price%TYPE;
BEGIN
  SELECT itemid, quantityused, price
    INTO v_item, v_quantity, v_price
    FROM appointmentservices
    WHERE appointmentid = p_appointmentid
      AND serviceid = p_serviceid;
  DBMS_OUTPUT.PUT_LINE('Appointment ID: ' || p_appointmentid ||
                        ' | Service ID: ' || p_serviceid ||
                        ' | Item ID: ' || v_item ||
                        ' | Quantity Used: ' || v_quantity ||
                        ' | Price: ' || v_price);
EXCEPTION
  WHEN NO_DATA_FOUND THEN
    DBMS_OUTPUT.PUT_LINE('No appointment service found for Appointment ID ' || p_appointmentid || ' and Service ID ' || p_serviceid);
  WHEN OTHERS THEN
    DBMS_OUTPUT.PUT_LINE('Error in get_appointment_service: ' || SQLERRM);
END;
/

CREATE OR REPLACE PROCEDURE get_all_appointment_services IS
BEGIN
  FOR rec IN (SELECT appointmentid, serviceid, itemid, quantityused, price FROM appointmentservices) LOOP
    DBMS_OUTPUT.PUT_LINE('Appointment ID: ' || rec.appointmentid ||
                         ' | Service ID: ' || rec.serviceid ||
                         ' | Item ID: ' || rec.itemid ||
                         ' | Quantity Used: ' || rec.quantityused ||
                         ' | Price: ' || rec.price);
  END LOOP;
  IF SQL%ROWCOUNT = 0 THEN
    DBMS_OUTPUT.PUT_LINE('No appointment services found.');
  END IF;
EXCEPTION
  WHEN OTHERS THEN
    DBMS_OUTPUT.PUT_LINE('Error in get_all_appointment_services: ' || SQLERRM);
END;
/

-- For Services Management: Get Specific and List All Services

CREATE OR REPLACE PROCEDURE get_service(
  p_serviceid IN services.serviceid%TYPE
)
IS 
  v_name services.name%TYPE;
  v_desc services.description%TYPE;
BEGIN
  SELECT name, description INTO v_name, v_desc
  FROM services
  WHERE serviceid = p_serviceid;
  DBMS_OUTPUT.PUT_LINE('Service ID: ' || p_serviceid ||
                       ' | Name: ' || v_name ||
                       ' | Description: ' || v_desc);
EXCEPTION
  WHEN NO_DATA_FOUND THEN
    DBMS_OUTPUT.PUT_LINE('No service found with ID ' || p_serviceid);
  WHEN OTHERS THEN
    DBMS_OUTPUT.PUT_LINE('Error in get_service: ' || SQLERRM);
END;
/

CREATE OR REPLACE PROCEDURE get_all_services IS
BEGIN
  FOR rec IN (SELECT serviceid, name, description FROM services) LOOP
    DBMS_OUTPUT.PUT_LINE('Service ID: ' || rec.serviceid ||
                         ' | Name: ' || rec.name ||
                         ' | Description: ' || rec.description);
  END LOOP;
  IF SQL%ROWCOUNT = 0 THEN
    DBMS_OUTPUT.PUT_LINE('No services found.');
  END IF;
EXCEPTION
  WHEN OTHERS THEN
    DBMS_OUTPUT.PUT_LINE('Error in get_all_services: ' || SQLERRM);
END;
/

-- For Inventory Management: Get Specific and List All Inventory Items

CREATE OR REPLACE PROCEDURE get_inventory_item(
  p_itemid IN inventory.itemid%TYPE
)
IS 
  v_name inventory.name%TYPE;
  v_quantity inventory.quantity%TYPE;
  v_price inventory.price%TYPE;
  v_avail inventory.availablity%TYPE;
BEGIN
  SELECT name, quantity, price, availablity
  INTO v_name, v_quantity, v_price, v_avail
  FROM inventory
  WHERE itemid = p_itemid;
  DBMS_OUTPUT.PUT_LINE('Item ID: ' || p_itemid ||
                       ' | Name: ' || v_name ||
                       ' | Quantity: ' || v_quantity ||
                       ' | Price: ' || v_price ||
                       ' | Availability: ' || v_avail);
EXCEPTION
  WHEN NO_DATA_FOUND THEN
    DBMS_OUTPUT.PUT_LINE('No inventory item found with ID ' || p_itemid);
  WHEN OTHERS THEN
    DBMS_OUTPUT.PUT_LINE('Error in get_inventory_item: ' || SQLERRM);
END;
/

CREATE OR REPLACE PROCEDURE get_all_inventory IS
BEGIN
  FOR rec IN (SELECT itemid, name, quantity, price, availablity FROM inventory) LOOP
    DBMS_OUTPUT.PUT_LINE('Item ID: ' || rec.itemid ||
                         ' | Name: ' || rec.name ||
                         ' | Quantity: ' || rec.quantity ||
                         ' | Price: ' || rec.price ||
                         ' | Availability: ' || rec.availablity);
  END LOOP;
  IF SQL%ROWCOUNT = 0 THEN
    DBMS_OUTPUT.PUT_LINE('No inventory items found.');
  END IF;
EXCEPTION
  WHEN OTHERS THEN
    DBMS_OUTPUT.PUT_LINE('Error in get_all_inventory: ' || SQLERRM);
END;
/

-- For Invoice Management: Get Specific and List All Invoices

CREATE OR REPLACE PROCEDURE get_invoice(
  p_invoiceid IN invoices.invoiceid%TYPE
)
IS 
  v_total invoices.totalprice%TYPE;
  v_gen invoices.generatedby%TYPE;
  v_app invoices.appointmentid%TYPE;
  v_paid invoices.paidornot%TYPE;
  v_date invoices.invoicedate%TYPE;
BEGIN
  SELECT totalprice, generatedby, appointmentid, paidornot, invoicedate
  INTO v_total, v_gen, v_app, v_paid, v_date
  FROM invoices
  WHERE invoiceid = p_invoiceid;
  DBMS_OUTPUT.PUT_LINE('Invoice ID: ' || p_invoiceid ||
                       ' | Total Price: ' || v_total ||
                       ' | Generated By: ' || v_gen ||
                       ' | Appointment ID: ' || v_app ||
                       ' | Paid: ' || v_paid ||
                       ' | Date: ' || v_date);
EXCEPTION
  WHEN NO_DATA_FOUND THEN
    DBMS_OUTPUT.PUT_LINE('No invoice found with ID ' || p_invoiceid);
  WHEN OTHERS THEN
    DBMS_OUTPUT.PUT_LINE('Error in get_invoice: ' || SQLERRM);
END;
/

CREATE OR REPLACE PROCEDURE get_all_invoices IS
BEGIN
  FOR rec IN (SELECT invoiceid, totalprice, generatedby, appointmentid, paidornot, invoicedate FROM invoices) LOOP
    DBMS_OUTPUT.PUT_LINE('Invoice ID: ' || rec.invoiceid ||
                         ' | Total Price: ' || rec.totalprice ||
                         ' | Generated By: ' || rec.generatedby ||
                         ' | Appointment ID: ' || rec.appointmentid ||
                         ' | Paid: ' || rec.paidornot ||
                         ' | Date: ' || rec.invoicedate);
  END LOOP;
  IF SQL%ROWCOUNT = 0 THEN
    DBMS_OUTPUT.PUT_LINE('No invoices found.');
  END IF;
EXCEPTION
  WHEN OTHERS THEN
    DBMS_OUTPUT.PUT_LINE('Error in get_all_invoices: ' || SQLERRM);
END;
/


CREATE OR REPLACE PROCEDURE get_employee(
    v_emp_id IN users.userid%TYPE
)
IS 
    v_name    users.name%TYPE;
    v_password users.password%TYPE;
    v_role    users.roleid%TYPE;
    v_active  users.active%TYPE;
BEGIN
    SELECT name, password, roleid, active
      INTO v_name, v_password, v_role, v_active
      FROM users
     WHERE userid = v_emp_id;
    DBMS_OUTPUT.PUT_LINE('Employee ID: ' || v_emp_id ||
                         ' | Name: ' || v_name ||
                         ' | Password: ' || v_password ||
                         ' | Role ID: ' || v_role ||
                         ' | Active: ' || v_active);
EXCEPTION
    WHEN NO_DATA_FOUND THEN
        DBMS_OUTPUT.PUT_LINE('No employee found with ID ' || v_emp_id);
    WHEN OTHERS THEN
        DBMS_OUTPUT.PUT_LINE('Error in get_employee: ' || SQLERRM);
END;
/

CREATE OR REPLACE PROCEDURE get_all_employees IS
BEGIN
    FOR rec IN (SELECT userid, name, password, roleid, active FROM users) LOOP
        DBMS_OUTPUT.PUT_LINE('Employee ID: ' || rec.userid ||
                             ' | Name: ' || rec.name ||
                             ' | Password: ' || rec.password ||
                             ' | Role ID: ' || rec.roleid ||
                             ' | Active: ' || rec.active);
    END LOOP;
    IF SQL%ROWCOUNT = 0 THEN
        DBMS_OUTPUT.PUT_LINE('No employees found.');
    END IF;
EXCEPTION
    WHEN OTHERS THEN
        DBMS_OUTPUT.PUT_LINE('Error in get_all_employees: ' || SQLERRM);
END;
/



CREATE OR REPLACE PROCEDURE get_all_notifications IS
BEGIN
    FOR rec IN (SELECT notification_id, itemid, quantity, notification_date
                FROM low_stock_notifications
                ORDER BY notification_date DESC) LOOP
         DBMS_OUTPUT.PUT_LINE('Notification ID: ' || rec.notification_id ||
                              ' | Item ID: ' || rec.itemid ||
                              ' | Quantity: ' || rec.quantity ||
                              ' | Date: ' || rec.notification_date);
    END LOOP;
    IF SQL%ROWCOUNT = 0 THEN
         DBMS_OUTPUT.PUT_LINE('No low stock notifications found.');
    END IF;
EXCEPTION
    WHEN OTHERS THEN
         DBMS_OUTPUT.PUT_LINE('Error in get_all_notifications: ' || SQLERRM);
END;
/



CREATE OR REPLACE PROCEDURE get_mechanic_appointment(
    p_app_id      IN appointments.appointmentid%TYPE,
    p_mechanic_id IN appointments.assignedmechanic%TYPE
)
IS
    v_customer   appointments.customerid%TYPE;
    v_status     appointments.status%TYPE;
    v_date       appointments.appointmentdate%TYPE;
BEGIN
    SELECT customerid, status, appointmentdate
      INTO v_customer, v_status, v_date
      FROM appointments
     WHERE appointmentid = p_app_id
       AND assignedmechanic = p_mechanic_id;
       
    DBMS_OUTPUT.PUT_LINE('Appointment ID: ' || p_app_id ||
                         ' | Customer ID: ' || v_customer ||
                         ' | Status: ' || v_status ||
                         ' | Date: ' || v_date);
EXCEPTION
    WHEN NO_DATA_FOUND THEN
         DBMS_OUTPUT.PUT_LINE('No appointment found for Appointment ID ' 
              || p_app_id || ' assigned to Mechanic ' || p_mechanic_id);
    WHEN OTHERS THEN
         DBMS_OUTPUT.PUT_LINE('Error in get_mechanic_appointment: ' || SQLERRM);
END;
/

CREATE OR REPLACE PROCEDURE get_all_mechanic_appointments(
    p_mechanic_id IN appointments.assignedmechanic%TYPE
)
IS
BEGIN
    FOR rec IN (SELECT appointmentid, customerid, status, appointmentdate
                  FROM appointments
                 WHERE assignedmechanic = p_mechanic_id)
    LOOP
         DBMS_OUTPUT.PUT_LINE('Appointment ID: ' || rec.appointmentid ||
                              ' | Customer ID: ' || rec.customerid ||
                              ' | Status: ' || rec.status ||
                              ' | Date: ' || rec.appointmentdate);
    END LOOP;
    
    IF SQL%ROWCOUNT = 0 THEN
         DBMS_OUTPUT.PUT_LINE('No appointments assigned to Mechanic ' || p_mechanic_id);
    END IF;
EXCEPTION
    WHEN OTHERS THEN
         DBMS_OUTPUT.PUT_LINE('Error in get_all_mechanic_appointments: ' || SQLERRM);
END;
/

CREATE OR REPLACE PROCEDURE get_mechanic_appointment_service(
    p_app_id      IN appointmentservices.appointmentid%TYPE,
    p_serv_id     IN appointmentservices.serviceid%TYPE,
    p_mechanic_id IN appointments.assignedmechanic%TYPE
)
IS
    v_item     appointmentservices.itemid%TYPE;
    v_quantity appointmentservices.quantityused%TYPE;
    v_price    appointmentservices.price%TYPE;
BEGIN
    SELECT asv.itemid, asv.quantityused, asv.price
      INTO v_item, v_quantity, v_price
      FROM appointmentservices asv, appointments a
     WHERE asv.appointmentid = a.appointmentid
       AND a.assignedmechanic = p_mechanic_id
       AND asv.appointmentid = p_app_id
       AND asv.serviceid = p_serv_id;
       
    DBMS_OUTPUT.PUT_LINE('Appointment Service Details for Appointment ID: ' || p_app_id ||
                         ' | Service ID: ' || p_serv_id ||
                         ' | Item ID: ' || v_item ||
                         ' | Quantity Used: ' || v_quantity ||
                         ' | Price: ' || v_price);
EXCEPTION
    WHEN NO_DATA_FOUND THEN
         DBMS_OUTPUT.PUT_LINE('No appointment service found for Appointment ID: ' ||
             p_app_id || ', Service ID: ' || p_serv_id ||
             ' assigned to Mechanic ' || p_mechanic_id);
    WHEN OTHERS THEN
         DBMS_OUTPUT.PUT_LINE('Error in get_mechanic_appointment_service: ' || SQLERRM);
END;
/



CREATE OR REPLACE PROCEDURE get_all_mechanic_appointment_services(
    p_mechanic_id IN appointments.assignedmechanic%TYPE
)
IS
BEGIN
    FOR rec IN (
       SELECT asv.appointmentid, asv.serviceid, asv.itemid, asv.quantityused, asv.price
         FROM appointmentservices asv, appointments a
        WHERE asv.appointmentid = a.appointmentid
          AND a.assignedmechanic = p_mechanic_id
    )
    LOOP
         DBMS_OUTPUT.PUT_LINE('Appointment ID: ' || rec.appointmentid ||
                              ' | Service ID: ' || rec.serviceid ||
                              ' | Item ID: ' || rec.itemid ||
                              ' | Quantity Used: ' || rec.quantityused ||
                              ' | Price: ' || rec.price);
    END LOOP;
    
    IF SQL%ROWCOUNT = 0 THEN
         DBMS_OUTPUT.PUT_LINE('No appointment services found for Mechanic ' || p_mechanic_id);
    END IF;
EXCEPTION
    WHEN OTHERS THEN
         DBMS_OUTPUT.PUT_LINE('Error in get_all_mechanic_appointment_services: ' || SQLERRM);
END;
/


 

CREATE OR REPLACE PROCEDURE update_inventory_quantity(
    p_itemid      IN inventory.itemid%TYPE,
    p_new_quantity IN inventory.quantity%TYPE
)
IS
    v_current_quantity inventory.quantity%TYPE;
BEGIN
    SELECT quantity INTO v_current_quantity
      FROM inventory
     WHERE itemid = p_itemid;
     
    -- Ensure that we are reducing the quantity (i.e. p_new_quantity is less than current)
    IF p_new_quantity < v_current_quantity THEN
        UPDATE inventory
           SET quantity = p_new_quantity,
               availablity = CASE WHEN p_new_quantity > 0 THEN 'Available' ELSE 'Out of Stock' END
         WHERE itemid = p_itemid;
        COMMIT;
        DBMS_OUTPUT.PUT_LINE('Quantity updated successfully for item ' || p_itemid);
    ELSE
        DBMS_OUTPUT.PUT_LINE('Invalid update: New quantity (' || p_new_quantity ||
                             ') must be less than the current quantity (' || v_current_quantity || ').');
    END IF;
EXCEPTION
    WHEN NO_DATA_FOUND THEN
        DBMS_OUTPUT.PUT_LINE('No inventory item found with ID ' || p_itemid);
    WHEN OTHERS THEN
        DBMS_OUTPUT.PUT_LINE('Error updating quantity: ' || SQLERRM);
END;
/












