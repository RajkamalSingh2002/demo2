--Login and Display Main Dashboard Menu
SET SERVEROUTPUT ON;
ACCEPT username PROMPT 'Enter your username: '
ACCEPT password PROMPT 'Enter your password: '

DECLARE
    v_username users.name%TYPE     := '&username';
    v_password users.password%TYPE   := '&password';
    v_roleid   users.roleid%TYPE;
BEGIN
    -- Validate the user credentials.
    SELECT roleid 
      INTO v_roleid
      FROM users
     WHERE name = v_username
       AND password = v_password;
    -- Check the role and show the main dashboard menu if admin.
    IF v_roleid = 1 THEN
     DBMS_OUTPUT.PUT_LINE('Welcome, Admin. Opening Admin Dashboard...');
    ELSIF v_roleid = 2 THEN
       DBMS_OUTPUT.PUT_LINE('Welcome, Sales Rep. Opening Sales Rep Dashboard...');
    ELSIF v_roleid = 3 THEN
       DBMS_OUTPUT.PUT_LINE('Welcome, Mechanic. Opening Mechanic Dashboard...');
    ELSE
       DBMS_OUTPUT.PUT_LINE('Role not recognized. Access Denied.');
    END IF;
EXCEPTION
    WHEN NO_DATA_FOUND THEN
       DBMS_OUTPUT.PUT_LINE('Invalid credentials. Please try again.');
    WHEN OTHERS THEN
       DBMS_OUTPUT.PUT_LINE('An unexpected error occurred: ' || SQLERRM);
END;
/

--Process Main Dashboard Option (For Admin Only)
PROMPT Main Dashboard Options:
PROMPT   1. Customer Management
PROMPT   2. Vehicle Management
PROMPT   3. Appointments Management
PROMPT   4. Appointment Services Management
PROMPT   5. Services Management
PROMPT   6. Inventory Management
PROMPT   7. Invoice Management
PROMPT   8. Employee Management
PROMPT   9. Notifications
PROMPT   10. Logout
PROMPT
ACCEPT admin_main_option PROMPT 'Enter your main dashboard option (1-10): '

DECLARE
    v_main_option NUMBER := &admin_main_option;
BEGIN
    CASE v_main_option
      WHEN 1 THEN 
          DBMS_OUTPUT.PUT_LINE('You selected Customer Management.');
      WHEN 2 THEN 
          DBMS_OUTPUT.PUT_LINE('You selected Vehicle Management.');
      WHEN 3 THEN 
          DBMS_OUTPUT.PUT_LINE('You selected Appointments Management.');
      WHEN 4 THEN 
          DBMS_OUTPUT.PUT_LINE('You selected Appointment Services Management.');
      WHEN 5 THEN 
          DBMS_OUTPUT.PUT_LINE('You selected Services Management.');
      WHEN 6 THEN 
          DBMS_OUTPUT.PUT_LINE('You selected Inventory Management.');
      WHEN 7 THEN 
          DBMS_OUTPUT.PUT_LINE('You selected Invoice Management.');
      WHEN 8 THEN 
          DBMS_OUTPUT.PUT_LINE('You selected Employee Management.');
      WHEN 9 THEN 
          DBMS_OUTPUT.PUT_LINE('You selected Notifications.');
      WHEN 10 THEN 
          DBMS_OUTPUT.PUT_LINE('Logging out...');
      ELSE 
          DBMS_OUTPUT.PUT_LINE('Invalid main option selected.');
    END CASE;
EXCEPTION
    WHEN OTHERS THEN 
        DBMS_OUTPUT.PUT_LINE('An error occurred: ' || SQLERRM);
END;
/

--OPTION 1 (CUSTOMERS MANAGEMENT SELECTED)
SET SERVEROUTPUT ON;
PROMPT
PROMPT Customer Management Options:
PROMPT   1. Get Specific Customer
PROMPT   2. Update Customer
PROMPT   3. Delete Customer
PROMPT   4. Add Customer
PROMPT   5. List All Customers
PROMPT
ACCEPT customer_option PROMPT 'Enter your option: '

DECLARE
    v_option NUMBER := &customer_option;
BEGIN
    CASE v_option
        WHEN 1 THEN 
            DBMS_OUTPUT.PUT_LINE('Option 1 selected: Get Specific Customer.');
        WHEN 2 THEN 
            DBMS_OUTPUT.PUT_LINE('Option 2 selected: Update Customer.');
        WHEN 3 THEN 
            DBMS_OUTPUT.PUT_LINE('Option 3 selected: Delete Customer.');
        WHEN 4 THEN 
            DBMS_OUTPUT.PUT_LINE('Option 4 selected: Add Customer.');
        WHEN 5 THEN 
            DBMS_OUTPUT.PUT_LINE('Option 5 selected: List All Customers.');
        ELSE 
            DBMS_OUTPUT.PUT_LINE('Invalid option selected.');
    END CASE;
END;
/

--OPTION 1 (GET SPECIFIC CUSTOMER)
ACCEPT cust_id PROMPT 'Enter the Customer ID to retrieve: '

DECLARE
    v_cust_id NUMBER := &cust_id;
BEGIN
    get_customer(v_cust_id);
END;
/

--OPTION 2 (UPDATE CUSTOMER)
ACCEPT cust_id PROMPT 'Enter the Customer ID to update: '
ACCEPT new_name PROMPT 'Enter new name: '
ACCEPT new_email PROMPT 'Enter new email: '
ACCEPT new_contact PROMPT 'Enter new contact (numeric): '
ACCEPT new_vin PROMPT 'Enter new VIN: '
ACCEPT new_address PROMPT 'Enter new address: '

DECLARE
    v_id      NUMBER := &cust_id;
    v_name    customers.name%TYPE := '&new_name';
    v_email   customers.email%TYPE := '&new_email';
    v_contact customers.contact%TYPE := &new_contact;
    v_vin     customers.vin%TYPE := &new_vin;
    v_address customers.address%TYPE := '&new_address';
BEGIN
    update_customer(v_id, v_name, v_email, v_contact, v_vin, v_address);
END;
/

--OPTION 3 (DELETE CUSTOMER)
ACCEPT del_cust_id PROMPT 'Enter the Customer ID to delete: '

DECLARE
    v_del_id NUMBER := &del_cust_id;
BEGIN
    delete_customer(v_del_id);
END;
/

--OPTION 4 (ADD CUSTOMER)
ACCEPT add_name PROMPT 'Enter Customer Name: '
ACCEPT add_email PROMPT 'Enter Customer Email: '
ACCEPT add_contact PROMPT 'Enter Customer Contact (numeric): '
ACCEPT add_vin PROMPT 'Enter VIN: '
ACCEPT add_address PROMPT 'Enter Customer Address: '

DECLARE
    v_name    customers.name%TYPE := '&add_name';
    v_email   customers.email%TYPE := '&add_email';
    v_contact customers.contact%TYPE := &add_contact;
    v_vin     customers.vin%TYPE := &add_vin;
    v_address customers.address%TYPE := '&add_address';
BEGIN
    insert_customer(v_name, v_email, v_contact, v_vin, v_address);
END;
/

--OPTION 5 (All CUSTOMERS).
DECLARE
BEGIN
    get_all_customers;
END;
/

-- OPTION 2 (VEHICLES MANAGEMENT SELECETED)
PROMPT
PROMPT Vehicles Management Options:
PROMPT   1. Get Specific Vehicle
PROMPT   2. Update Vehicle
PROMPT   3. Delete Vehicle
PROMPT   4. Add Vehicle
PROMPT   5. List All Vehicles
PROMPT

ACCEPT vehicle_option PROMPT 'Enter your vehicle management option (1-5): '

DECLARE
    v_option NUMBER := &vehicle_option;
BEGIN
    CASE v_option
        WHEN 1 THEN DBMS_OUTPUT.PUT_LINE('Option 1 selected: Get Specific Vehicle.');
        WHEN 2 THEN DBMS_OUTPUT.PUT_LINE('Option 2 selected: Update Vehicle.');
        WHEN 3 THEN DBMS_OUTPUT.PUT_LINE('Option 3 selected: Delete Vehicle.');
        WHEN 4 THEN DBMS_OUTPUT.PUT_LINE('Option 4 selected: Add Vehicle.');
        WHEN 5 THEN DBMS_OUTPUT.PUT_LINE('Option 5 selected: List All Vehicles.');
        ELSE DBMS_OUTPUT.PUT_LINE('Invalid option selected.');
    END CASE;
END;
/

-- Option 1: Get Specific Vehicle
ACCEPT vehicle_vin PROMPT 'Enter the Vehicle VIN to retrieve: '

DECLARE
    v_vin vehicles.vin%TYPE := &vehicle_vin;
BEGIN
    get_vehicle(v_vin);
END;
/

-- Option 2: Update Vehicle
ACCEPT up_vin PROMPT 'Enter the Vehicle VIN to update: '
ACCEPT up_model PROMPT 'Enter new model: '
ACCEPT up_year PROMPT 'Enter new year: '
ACCEPT up_license PROMPT 'Enter new license plate: '

DECLARE
    v_vin vehicles.vin%TYPE := &up_vin;
    v_model vehicles.model%TYPE := '&up_model';
    v_year vehicles.year%TYPE := &up_year;
    v_license vehicles.licenseplate%TYPE := '&up_license';
BEGIN
    update_vehicle(v_vin, v_model, v_year, v_license);
END;
/

-- Option 3: Delete Vehicle
ACCEPT del_vin PROMPT 'Enter the Vehicle VIN to delete: '

DECLARE
    v_vin vehicles.vin%TYPE := &del_vin;
BEGIN
    delete_vehicle(v_vin);
END;
/

-- Option 4: Add Vehicle
ACCEPT add_vin PROMPT 'Enter Vehicle VIN: '
ACCEPT add_model PROMPT 'Enter Vehicle Model: '
ACCEPT add_year PROMPT 'Enter Vehicle Year: '
ACCEPT add_license PROMPT 'Enter License Plate: '

DECLARE
    v_vin vehicles.vin%TYPE := &add_vin;
    v_model vehicles.model%TYPE := '&add_model';
    v_year vehicles.year%TYPE := &add_year;
    v_license vehicles.licenseplate%TYPE := '&add_license';
BEGIN
    insert_vehicle(v_vin, v_model, v_year, v_license);
END;
/

-- Option 5: List All Vehicles
DECLARE
BEGIN
    get_all_vehicles;
END;
/

-- OPTION 3 (Appointments Management Sub-Menu)
PROMPT
PROMPT Appointments Management Options:
PROMPT   1. Get Specific Appointment
PROMPT   2. Update Appointment
PROMPT   3. Delete Appointment
PROMPT   4. Add Appointment
PROMPT   5. List All Appointments
PROMPT

ACCEPT appointment_option PROMPT 'Enter your appointment management option (1-5): '

DECLARE
    v_option NUMBER := &appointment_option;
BEGIN
    CASE v_option
        WHEN 1 THEN DBMS_OUTPUT.PUT_LINE('Option 1 selected: Get Specific Appointment.');
        WHEN 2 THEN DBMS_OUTPUT.PUT_LINE('Option 2 selected: Update Appointment.');
        WHEN 3 THEN DBMS_OUTPUT.PUT_LINE('Option 3 selected: Delete Appointment.');
        WHEN 4 THEN DBMS_OUTPUT.PUT_LINE('Option 4 selected: Add Appointment.');
        WHEN 5 THEN DBMS_OUTPUT.PUT_LINE('Option 5 selected: List All Appointments.');
        ELSE DBMS_OUTPUT.PUT_LINE('Invalid option selected.');
    END CASE;
END;
/

-- Option 1: Get Specific Appointment
ACCEPT app_id PROMPT 'Enter the Appointment ID to retrieve: '

DECLARE
    v_app_id appointments.appointmentid%TYPE := &app_id;
BEGIN
    get_appointment(v_app_id);
END;
/

-- Option 2: Update Appointment
ACCEPT app_id PROMPT 'Enter the Appointment ID to update: '
ACCEPT new_status PROMPT 'Enter new status: '
ACCEPT new_mechanic PROMPT 'Enter new assigned mechanic ID: '

DECLARE
    v_app_id appointments.appointmentid%TYPE := &app_id;
    v_status appointments.status%TYPE := '&new_status';
    v_mechanic appointments.assignedmechanic%TYPE := &new_mechanic;
BEGIN
    update_appointment(v_app_id, v_status, v_mechanic);
END;
/

-- Option 3: Delete Appointment
ACCEPT del_app_id PROMPT 'Enter the Appointment ID to delete: '

DECLARE
    v_app_id appointments.appointmentid%TYPE := &del_app_id;
BEGIN
    delete_appointment(v_app_id);
END;
/

-- Option 4: Add Appointment
ACCEPT app_customer PROMPT 'Enter Customer ID for the appointment: '
ACCEPT app_status PROMPT 'Enter appointment status: '
ACCEPT app_mechanic PROMPT 'Enter assigned mechanic ID: '

DECLARE
    v_customer appointments.customerid%TYPE := &app_customer;
    v_status appointments.status%TYPE := '&app_status';
    v_mechanic appointments.assignedmechanic%TYPE := &app_mechanic;
BEGIN
    insert_appointment(v_customer, v_status, v_mechanic);
END;
/

-- Option 5: List All Appointments
DECLARE
BEGIN
    get_all_appointments;
END;
/

-- OPTION 4 (Appointment Services Management Sub-Menu)
PROMPT
PROMPT Appointment Services Management Options:
PROMPT   1. Get Specific Appointment Service
PROMPT   2. Update Appointment Service
PROMPT   3. Delete Appointment Service
PROMPT   4. Add Appointment Service
PROMPT   5. List All Appointment Services
PROMPT

ACCEPT apptserv_option PROMPT 'Enter your appointment services option (1-5): '

DECLARE
    v_option NUMBER := &apptserv_option;
BEGIN
    CASE v_option
        WHEN 1 THEN DBMS_OUTPUT.PUT_LINE('Option 1 selected: Get Specific Appointment Service.');
        WHEN 2 THEN DBMS_OUTPUT.PUT_LINE('Option 2 selected: Update Appointment Service.');
        WHEN 3 THEN DBMS_OUTPUT.PUT_LINE('Option 3 selected: Delete Appointment Service.');
        WHEN 4 THEN DBMS_OUTPUT.PUT_LINE('Option 4 selected: Add Appointment Service.');
        WHEN 5 THEN DBMS_OUTPUT.PUT_LINE('Option 5 selected: List All Appointment Services.');
        ELSE DBMS_OUTPUT.PUT_LINE('Invalid option selected.');
    END CASE;
END;
/

-- Option 1: Get Specific Appointment Service
ACCEPT as_app_id PROMPT 'Enter the Appointment ID: '
ACCEPT as_service_id PROMPT 'Enter the Service ID: '

DECLARE
    v_app_id appointmentservices.appointmentid%TYPE := &as_app_id;
    v_service_id appointmentservices.serviceid%TYPE := &as_service_id;
BEGIN
    get_appointment_service(v_app_id, v_service_id);
END;
/

-- Option 2: Update Appointment Service
ACCEPT uas_app_id PROMPT 'Enter the Appointment ID to update: '
ACCEPT uas_service_id PROMPT 'Enter the Service ID to update: '
ACCEPT new_item_id PROMPT 'Enter new Item ID: '
ACCEPT new_quantity PROMPT 'Enter new Quantity Used: '

DECLARE
    v_app_id appointmentservices.appointmentid%TYPE := &uas_app_id;
    v_service_id appointmentservices.serviceid%TYPE := &uas_service_id;
    v_item_id appointmentservices.itemid%TYPE := &new_item_id;
    v_quantity appointmentservices.quantityused%TYPE := &new_quantity;
BEGIN
    update_appointment_service(v_app_id, v_service_id, v_item_id, v_quantity);
END;
/

-- Option 3: Delete Appointment Service
ACCEPT das_app_id PROMPT 'Enter the Appointment ID to delete: '
ACCEPT das_service_id PROMPT 'Enter the Service ID to delete: '

DECLARE
    v_app_id appointmentservices.appointmentid%TYPE := &das_app_id;
    v_service_id appointmentservices.serviceid%TYPE := &das_service_id;
BEGIN
    delete_appointment_service(v_app_id, v_service_id);
END;
/

-- Option 4: Add Appointment Service
ACCEPT aa_app_id PROMPT 'Enter the Appointment ID: '
ACCEPT aa_service_id PROMPT 'Enter the Service ID: '
ACCEPT aa_item_id PROMPT 'Enter the Item ID: '
ACCEPT aa_quantity PROMPT 'Enter Quantity Used: '

DECLARE
    v_app_id appointmentservices.appointmentid%TYPE := &aa_app_id;
    v_service_id appointmentservices.serviceid%TYPE := &aa_service_id;
    v_item_id appointmentservices.itemid%TYPE := &aa_item_id;
    v_quantity appointmentservices.quantityused%TYPE := &aa_quantity;
BEGIN
    insert_appointment_service(v_app_id, v_service_id, v_item_id, v_quantity);
END;
/

-- Option 5: List All Appointment Services
DECLARE
BEGIN
    get_all_appointment_services;
END;
/


-- OPTION 5 (Services Management Sub-Menu)
PROMPT
PROMPT Services Management Options:
PROMPT   1. Get Specific Service
PROMPT   2. Update Service
PROMPT   3. Delete Service
PROMPT   4. Add Service
PROMPT   5. List All Services
PROMPT

ACCEPT service_option PROMPT 'Enter your service management option (1-5): '

DECLARE
    v_option NUMBER := &service_option;
BEGIN
    CASE v_option
        WHEN 1 THEN DBMS_OUTPUT.PUT_LINE('Option 1 selected: Get Specific Service.');
        WHEN 2 THEN DBMS_OUTPUT.PUT_LINE('Option 2 selected: Update Service.');
        WHEN 3 THEN DBMS_OUTPUT.PUT_LINE('Option 3 selected: Delete Service.');
        WHEN 4 THEN DBMS_OUTPUT.PUT_LINE('Option 4 selected: Add Service.');
        WHEN 5 THEN DBMS_OUTPUT.PUT_LINE('Option 5 selected: List All Services.');
        ELSE DBMS_OUTPUT.PUT_LINE('Invalid option selected.');
    END CASE;
END;
/

-- Option 1: Get Specific Service
ACCEPT service_id PROMPT 'Enter the Service ID to retrieve: '

DECLARE
    v_service_id services.serviceid%TYPE := &service_id;
BEGIN
    get_service(v_service_id);
END;
/

-- Option 2: Update Service
ACCEPT upd_service_id PROMPT 'Enter the Service ID to update: '
ACCEPT upd_name PROMPT 'Enter new Service Name: '
ACCEPT upd_description PROMPT 'Enter new Service Description: '

DECLARE
    v_service_id services.serviceid%TYPE := &upd_service_id;
    v_name services.name%TYPE := '&upd_name';
    v_description services.description%TYPE := '&upd_description';
BEGIN
    update_service(v_service_id, v_name, v_description);
END;
/

-- Option 3: Delete Service
ACCEPT del_service_id PROMPT 'Enter the Service ID to delete: '

DECLARE
    v_service_id services.serviceid%TYPE := &del_service_id;
BEGIN
    delete_service(v_service_id);
END;
/

-- Option 4: Add Service
ACCEPT add_service_name PROMPT 'Enter Service Name: '
ACCEPT add_service_desc PROMPT 'Enter Service Description: '

DECLARE
    v_name services.name%TYPE := '&add_service_name';
    v_description services.description%TYPE := '&add_service_desc';
BEGIN
    insert_service(v_name, v_description);
END;
/

-- Option 5: List All Services
DECLARE
BEGIN
    get_all_services;
END;
/

-- OPTION 6 (Inventory Management Sub-Menu)
PROMPT
PROMPT Inventory Management Options:
PROMPT   1. Get Specific Inventory Item
PROMPT   2. Update Inventory Item
PROMPT   3. Delete Inventory Item
PROMPT   4. Add Inventory Item
PROMPT   5. List All Inventory Items
PROMPT

ACCEPT inventory_option PROMPT 'Enter your inventory management option (1-5): '

DECLARE
    v_option NUMBER := &inventory_option;
BEGIN
    CASE v_option
        WHEN 1 THEN DBMS_OUTPUT.PUT_LINE('Option 1 selected: Get Specific Inventory Item.');
        WHEN 2 THEN DBMS_OUTPUT.PUT_LINE('Option 2 selected: Update Inventory Item.');
        WHEN 3 THEN DBMS_OUTPUT.PUT_LINE('Option 3 selected: Delete Inventory Item.');
        WHEN 4 THEN DBMS_OUTPUT.PUT_LINE('Option 4 selected: Add Inventory Item.');
        WHEN 5 THEN DBMS_OUTPUT.PUT_LINE('Option 5 selected: List All Inventory Items.');
        ELSE DBMS_OUTPUT.PUT_LINE('Invalid option selected.');
    END CASE;
END;
/

-- Option 1: Get Specific Inventory Item
ACCEPT item_id PROMPT 'Enter the Inventory Item ID to retrieve: '

DECLARE
    v_itemid inventory.itemid%TYPE := &item_id;
BEGIN
    get_inventory_item(v_itemid);
END;
/

-- Option 2: Update Inventory Item
ACCEPT upd_item_id PROMPT 'Enter the Inventory Item ID to update: '
ACCEPT upd_item_name PROMPT 'Enter new Item Name: '
ACCEPT upd_quantity PROMPT 'Enter new Quantity: '
ACCEPT upd_price PROMPT 'Enter new Price: '

DECLARE
    v_itemid inventory.itemid%TYPE := &upd_item_id;
    v_name inventory.name%TYPE := '&upd_item_name';
    v_quantity inventory.quantity%TYPE := &upd_quantity;
    v_price inventory.price%TYPE := &upd_price;
BEGIN
    update_inventory(v_itemid, v_name, v_quantity, v_price);
END;
/

-- Option 3: Delete Inventory Item
ACCEPT del_item_id PROMPT 'Enter the Inventory Item ID to delete: '

DECLARE
    v_itemid inventory.itemid%TYPE := &del_item_id;
BEGIN
    delete_inventory(v_itemid);
END;
/

-- Option 4: Add Inventory Item
ACCEPT add_item_name PROMPT 'Enter Item Name: '
ACCEPT add_quantity PROMPT 'Enter Quantity: '
ACCEPT add_price PROMPT 'Enter Price: '

DECLARE
    v_name inventory.name%TYPE := '&add_item_name';
    v_quantity inventory.quantity%TYPE := &add_quantity;
    v_price inventory.price%TYPE := &add_price;
BEGIN
    insert_inventory(v_name, v_quantity, v_price);
END;
/

-- Option 5: List All Inventory Items
DECLARE
BEGIN
    get_all_inventory;
END;
/

--OPTION 7 (Invoice Management Sub-Menu)
PROMPT
PROMPT Invoice Management Options:
PROMPT   1. Get Specific Invoice
PROMPT   2. Update Invoice
PROMPT   3. Delete Invoice
PROMPT   4. Add Invoice
PROMPT   5. List All Invoices
PROMPT

ACCEPT invoice_option PROMPT 'Enter your invoice management option (1-5): '

DECLARE
    v_option NUMBER := &invoice_option;
BEGIN
    CASE v_option
        WHEN 1 THEN DBMS_OUTPUT.PUT_LINE('Option 1 selected: Get Specific Invoice.');
        WHEN 2 THEN DBMS_OUTPUT.PUT_LINE('Option 2 selected: Update Invoice.');
        WHEN 3 THEN DBMS_OUTPUT.PUT_LINE('Option 3 selected: Delete Invoice.');
        WHEN 4 THEN DBMS_OUTPUT.PUT_LINE('Option 4 selected: Add Invoice.');
        WHEN 5 THEN DBMS_OUTPUT.PUT_LINE('Option 5 selected: List All Invoices.');
        ELSE DBMS_OUTPUT.PUT_LINE('Invalid option selected.');
    END CASE;
END;
/

-- Option 1: Get Specific Invoice
ACCEPT inv_id PROMPT 'Enter the Invoice ID to retrieve: '

DECLARE
    v_inv_id invoices.invoiceid%TYPE := &inv_id;
BEGIN
    get_invoice(v_inv_id);
END;
/

-- Option 2: Update Invoice
ACCEPT upd_inv_id PROMPT 'Enter the Invoice ID to update: '
ACCEPT upd_total PROMPT 'Enter new Total Price: '
ACCEPT upd_generated_by PROMPT 'Enter new Generated By (User ID): '
ACCEPT upd_app_id PROMPT 'Enter new Appointment ID: '
ACCEPT upd_paid PROMPT 'Enter new Paid/Not status: '

DECLARE
    v_inv_id invoices.invoiceid%TYPE := &upd_inv_id;
    v_total invoices.totalprice%TYPE := &upd_total;
    v_gen invoices.generatedby%TYPE := &upd_generated_by;
    v_app invoices.appointmentid%TYPE := &upd_app_id;
    v_paid invoices.paidornot%TYPE := '&upd_paid';
BEGIN
    update_invoice(v_inv_id, v_total, v_gen, v_app, v_paid);
END;
/

-- Option 3: Delete Invoice
ACCEPT del_inv_id PROMPT 'Enter the Invoice ID to delete: '

DECLARE
    v_inv_id invoices.invoiceid%TYPE := &del_inv_id;
BEGIN
    delete_invoice(v_inv_id);
END;
/

-- Option 4: Add Invoice
ACCEPT inv_generated_by PROMPT 'Enter Generated By (User ID): '
ACCEPT inv_app_id PROMPT 'Enter Appointment ID: '
ACCEPT inv_paid PROMPT 'Enter Paid/Not status: '

DECLARE
    v_gen invoices.generatedby%TYPE := &inv_generated_by;
    v_app invoices.appointmentid%TYPE := &inv_app_id;
    v_paid invoices.paidornot%TYPE := '&inv_paid';
BEGIN
    insert_invoice(v_gen, v_app, v_paid);
END;
/

-- Option 5: List All Invoices
DECLARE
BEGIN
    get_all_invoices;
END;
/

-- OPTION 8 (Employee Management Sub-Menu)
PROMPT
PROMPT Employee Management Options:
PROMPT   1. Get Specific Employee
PROMPT   2. Update Employee
PROMPT   3. Add Employee
PROMPT   4. List All Employees
PROMPT
ACCEPT employee_option PROMPT 'Enter your employee management option (1-4): '

DECLARE
    v_option NUMBER := &employee_option;
BEGIN
    CASE v_option
        WHEN 1 THEN DBMS_OUTPUT.PUT_LINE('Option 1 selected: Get Specific Employee.');
        WHEN 2 THEN DBMS_OUTPUT.PUT_LINE('Option 2 selected: Update Employee.');
        WHEN 3 THEN DBMS_OUTPUT.PUT_LINE('Option 3 selected: Add Employee.');
        WHEN 4 THEN DBMS_OUTPUT.PUT_LINE('Option 4 selected: List All Employees.');
        ELSE DBMS_OUTPUT.PUT_LINE('Invalid option selected.');
    END CASE;
END;
/
 
-- Option 1: Get Specific Employee
ACCEPT emp_id PROMPT 'Enter the Employee ID to retrieve: '

DECLARE
    v_emp_id users.userid%TYPE := &emp_id;
BEGIN
    get_employee(v_emp_id);
END;
/
 
-- Option 2: Update Employee
ACCEPT emp_id PROMPT 'Enter the Employee ID to update: '
ACCEPT new_emp_name PROMPT 'Enter new Employee Name: '
ACCEPT new_emp_password PROMPT 'Enter new Employee Password: '
ACCEPT new_emp_role PROMPT 'Enter new Role ID: '
ACCEPT new_emp_active PROMPT 'Enter new Active Status (e.g., 1 for active, 0 for inactive): '

DECLARE
    v_emp_id  users.userid%TYPE := &emp_id;
    v_name    users.name%TYPE := '&new_emp_name';
    v_password users.password%TYPE := '&new_emp_password';
    v_role    users.roleid%TYPE := &new_emp_role;
    v_active  users.active%TYPE := &new_emp_active;
BEGIN
    update_user(v_emp_id, v_name, v_password, v_role, v_active);
END;
/
 
-- Option 3: Add Employee
ACCEPT emp_name PROMPT 'Enter Employee Name: '
ACCEPT emp_password PROMPT 'Enter Employee Password: '
ACCEPT emp_role PROMPT 'Enter Role ID: '
ACCEPT emp_active PROMPT 'Enter Active Status (e.g., 1 for active, 0 for inactive): '

DECLARE
    v_name     users.name%TYPE := '&emp_name';
    v_password users.password%TYPE := '&emp_password';
    v_role     users.roleid%TYPE := &emp_role;
    v_active   users.active%TYPE := &emp_active;
BEGIN
    insert_user(v_name, v_password, v_role, v_active);
END;
/
 
-- Option 4: List All Employees
DECLARE
BEGIN
    get_all_employees;
END;
/

--OPTION 9 (Notification Management Sub-Menu (For "Get All Notifications")
PROMPT
PROMPT Notification Management Options:
PROMPT   1. List All Notifications
PROMPT

ACCEPT notification_option PROMPT 'Enter your notification option (only option available is 1): '

DECLARE
    v_option NUMBER := &notification_option;
BEGIN
    CASE v_option
        WHEN 1 THEN 
            DBMS_OUTPUT.PUT_LINE('Listing all notifications...');
        ELSE 
            DBMS_OUTPUT.PUT_LINE('Invalid option selected.');
    END CASE;
END;
/

-- Call the procedure to display all notifications:
DECLARE
BEGIN
    get_all_notifications;
END;
/

--OPTION 10 LOGOUT
PROMPT
PROMPT Logout Option:
PROMPT   10. Logout
PROMPT
ACCEPT logout_option PROMPT 'Enter your logout option (10 to logout): '

DECLARE
    v_logout NUMBER := &logout_option;
BEGIN
    IF v_logout = 10 THEN
       DBMS_OUTPUT.PUT_LINE('Logging out...');
    ELSE
       DBMS_OUTPUT.PUT_LINE('Invalid logout option. Please enter 10 to logout.');
    END IF;
END;
/

-- Optionally, exit the SQL*Plus session:
EXIT;








--Process Main Dashboard Option (For Sales Rep Only)
PROMPT Main Dashboard Options:
PROMPT   1. Customer Management
PROMPT   2. Vehicle Management
PROMPT   3. Appointments Management
PROMPT   4. Appointment Services Management
PROMPT   5. Services Management
PROMPT   6. Inventory Management
PROMPT   7. Invoice Management
PROMPT   8. Log Out
PROMPT
ACCEPT admin_main_option PROMPT 'Enter your main dashboard option (1-8): '

DECLARE
    v_main_option NUMBER := &admin_main_option;
BEGIN
    CASE v_main_option
      WHEN 1 THEN 
          DBMS_OUTPUT.PUT_LINE('You selected Customer Management.');
      WHEN 2 THEN 
          DBMS_OUTPUT.PUT_LINE('You selected Vehicle Management.');
      WHEN 3 THEN 
          DBMS_OUTPUT.PUT_LINE('You selected Appointments Management.');
      WHEN 4 THEN 
          DBMS_OUTPUT.PUT_LINE('You selected Appointment Services Management.');
      WHEN 5 THEN 
          DBMS_OUTPUT.PUT_LINE('You selected Services Management.');
      WHEN 6 THEN 
          DBMS_OUTPUT.PUT_LINE('You selected Inventory Management.');
      WHEN 7 THEN 
          DBMS_OUTPUT.PUT_LINE('You selected Invoice Management.');
      WHEN 8 THEN 
          DBMS_OUTPUT.PUT_LINE('Log Out');
      ELSE 
          DBMS_OUTPUT.PUT_LINE('Invalid main option selected.');
    END CASE;
EXCEPTION
    WHEN OTHERS THEN 
        DBMS_OUTPUT.PUT_LINE('An error occurred: ' || SQLERRM);
END;
/

--OPTION 1 (CUSTOMERS MANAGEMENT SELECTED)
SET SERVEROUTPUT ON;
PROMPT
PROMPT Customer Management Options:
PROMPT   1. Get Specific Customer
PROMPT   2. Update Customer
PROMPT   3. Delete Customer
PROMPT   4. Add Customer
PROMPT   5. List All Customers
PROMPT
ACCEPT customer_option PROMPT 'Enter your option: '

DECLARE
    v_option NUMBER := &customer_option;
BEGIN
    CASE v_option
        WHEN 1 THEN 
            DBMS_OUTPUT.PUT_LINE('Option 1 selected: Get Specific Customer.');
        WHEN 2 THEN 
            DBMS_OUTPUT.PUT_LINE('Option 2 selected: Update Customer.');
        WHEN 3 THEN 
            DBMS_OUTPUT.PUT_LINE('Option 3 selected: Delete Customer.');
        WHEN 4 THEN 
            DBMS_OUTPUT.PUT_LINE('Option 4 selected: Add Customer.');
        WHEN 5 THEN 
            DBMS_OUTPUT.PUT_LINE('Option 5 selected: List All Customers.');
        ELSE 
            DBMS_OUTPUT.PUT_LINE('Invalid option selected.');
    END CASE;
END;
/

--OPTION 1 (GET SPECIFIC CUSTOMER)
ACCEPT cust_id PROMPT 'Enter the Customer ID to retrieve: '

DECLARE
    v_cust_id NUMBER := &cust_id;
BEGIN
    get_customer(v_cust_id);
END;
/

--OPTION 2 (UPDATE CUSTOMER)
ACCEPT cust_id PROMPT 'Enter the Customer ID to update: '
ACCEPT new_name PROMPT 'Enter new name: '
ACCEPT new_email PROMPT 'Enter new email: '
ACCEPT new_contact PROMPT 'Enter new contact (numeric): '
ACCEPT new_vin PROMPT 'Enter new VIN: '
ACCEPT new_address PROMPT 'Enter new address: '

DECLARE
    v_id      NUMBER := &cust_id;
    v_name    customers.name%TYPE := '&new_name';
    v_email   customers.email%TYPE := '&new_email';
    v_contact customers.contact%TYPE := &new_contact;
    v_vin     customers.vin%TYPE := &new_vin;
    v_address customers.address%TYPE := '&new_address';
BEGIN
    update_customer(v_id, v_name, v_email, v_contact, v_vin, v_address);
END;
/

--OPTION 3 (DELETE CUSTOMER)
ACCEPT del_cust_id PROMPT 'Enter the Customer ID to delete: '

DECLARE
    v_del_id NUMBER := &del_cust_id;
BEGIN
    delete_customer(v_del_id);
END;
/

--OPTION 4 (ADD CUSTOMER)
ACCEPT add_name PROMPT 'Enter Customer Name: '
ACCEPT add_email PROMPT 'Enter Customer Email: '
ACCEPT add_contact PROMPT 'Enter Customer Contact (numeric): '
ACCEPT add_vin PROMPT 'Enter VIN: '
ACCEPT add_address PROMPT 'Enter Customer Address: '

DECLARE
    v_name    customers.name%TYPE := '&add_name';
    v_email   customers.email%TYPE := '&add_email';
    v_contact customers.contact%TYPE := &add_contact;
    v_vin     customers.vin%TYPE := &add_vin;
    v_address customers.address%TYPE := '&add_address';
BEGIN
    insert_customer(v_name, v_email, v_contact, v_vin, v_address);
END;
/

--OPTION 5 (All CUSTOMERS).
DECLARE
BEGIN
    get_all_customers;
END;
/

-- OPTION 2 (VEHICLES MANAGEMENT SELECETED)
PROMPT
PROMPT Vehicles Management Options:
PROMPT   1. Get Specific Vehicle
PROMPT   2. Update Vehicle
PROMPT   3. Delete Vehicle
PROMPT   4. Add Vehicle
PROMPT   5. List All Vehicles
PROMPT

ACCEPT vehicle_option PROMPT 'Enter your vehicle management option (1-5): '

DECLARE
    v_option NUMBER := &vehicle_option;
BEGIN
    CASE v_option
        WHEN 1 THEN DBMS_OUTPUT.PUT_LINE('Option 1 selected: Get Specific Vehicle.');
        WHEN 2 THEN DBMS_OUTPUT.PUT_LINE('Option 2 selected: Update Vehicle.');
        WHEN 3 THEN DBMS_OUTPUT.PUT_LINE('Option 3 selected: Delete Vehicle.');
        WHEN 4 THEN DBMS_OUTPUT.PUT_LINE('Option 4 selected: Add Vehicle.');
        WHEN 5 THEN DBMS_OUTPUT.PUT_LINE('Option 5 selected: List All Vehicles.');
        ELSE DBMS_OUTPUT.PUT_LINE('Invalid option selected.');
    END CASE;
END;
/

-- Option 1: Get Specific Vehicle
ACCEPT vehicle_vin PROMPT 'Enter the Vehicle VIN to retrieve: '

DECLARE
    v_vin vehicles.vin%TYPE := &vehicle_vin;
BEGIN
    get_vehicle(v_vin);
END;
/

-- Option 2: Update Vehicle
ACCEPT up_vin PROMPT 'Enter the Vehicle VIN to update: '
ACCEPT up_model PROMPT 'Enter new model: '
ACCEPT up_year PROMPT 'Enter new year: '
ACCEPT up_license PROMPT 'Enter new license plate: '

DECLARE
    v_vin vehicles.vin%TYPE := &up_vin;
    v_model vehicles.model%TYPE := '&up_model';
    v_year vehicles.year%TYPE := &up_year;
    v_license vehicles.licenseplate%TYPE := '&up_license';
BEGIN
    update_vehicle(v_vin, v_model, v_year, v_license);
END;
/

-- Option 3: Delete Vehicle
ACCEPT del_vin PROMPT 'Enter the Vehicle VIN to delete: '

DECLARE
    v_vin vehicles.vin%TYPE := &del_vin;
BEGIN
    delete_vehicle(v_vin);
END;
/

-- Option 4: Add Vehicle
ACCEPT add_vin PROMPT 'Enter Vehicle VIN: '
ACCEPT add_model PROMPT 'Enter Vehicle Model: '
ACCEPT add_year PROMPT 'Enter Vehicle Year: '
ACCEPT add_license PROMPT 'Enter License Plate: '

DECLARE
    v_vin vehicles.vin%TYPE := &add_vin;
    v_model vehicles.model%TYPE := '&add_model';
    v_year vehicles.year%TYPE := &add_year;
    v_license vehicles.licenseplate%TYPE := '&add_license';
BEGIN
    insert_vehicle(v_vin, v_model, v_year, v_license);
END;
/

-- Option 5: List All Vehicles
DECLARE
BEGIN
    get_all_vehicles;
END;
/

-- OPTION 3 (Appointments Management Sub-Menu)
PROMPT
PROMPT Appointments Management Options:
PROMPT   1. Get Specific Appointment
PROMPT   2. Update Appointment
PROMPT   3. Delete Appointment
PROMPT   4. Add Appointment
PROMPT   5. List All Appointments
PROMPT

ACCEPT appointment_option PROMPT 'Enter your appointment management option (1-5): '

DECLARE
    v_option NUMBER := &appointment_option;
BEGIN
    CASE v_option
        WHEN 1 THEN DBMS_OUTPUT.PUT_LINE('Option 1 selected: Get Specific Appointment.');
        WHEN 2 THEN DBMS_OUTPUT.PUT_LINE('Option 2 selected: Update Appointment.');
        WHEN 3 THEN DBMS_OUTPUT.PUT_LINE('Option 3 selected: Delete Appointment.');
        WHEN 4 THEN DBMS_OUTPUT.PUT_LINE('Option 4 selected: Add Appointment.');
        WHEN 5 THEN DBMS_OUTPUT.PUT_LINE('Option 5 selected: List All Appointments.');
        ELSE DBMS_OUTPUT.PUT_LINE('Invalid option selected.');
    END CASE;
END;
/

-- Option 1: Get Specific Appointment
ACCEPT app_id PROMPT 'Enter the Appointment ID to retrieve: '

DECLARE
    v_app_id appointments.appointmentid%TYPE := &app_id;
BEGIN
    get_appointment(v_app_id);
END;
/

-- Option 2: Update Appointment
ACCEPT app_id PROMPT 'Enter the Appointment ID to update: '
ACCEPT new_status PROMPT 'Enter new status: '
ACCEPT new_mechanic PROMPT 'Enter new assigned mechanic ID: '

DECLARE
    v_app_id appointments.appointmentid%TYPE := &app_id;
    v_status appointments.status%TYPE := '&new_status';
    v_mechanic appointments.assignedmechanic%TYPE := &new_mechanic;
BEGIN
    update_appointment(v_app_id, v_status, v_mechanic);
END;
/

-- Option 3: Delete Appointment
ACCEPT del_app_id PROMPT 'Enter the Appointment ID to delete: '

DECLARE
    v_app_id appointments.appointmentid%TYPE := &del_app_id;
BEGIN
    delete_appointment(v_app_id);
END;
/

-- Option 4: Add Appointment
ACCEPT app_customer PROMPT 'Enter Customer ID for the appointment: '
ACCEPT app_status PROMPT 'Enter appointment status: '
ACCEPT app_mechanic PROMPT 'Enter assigned mechanic ID: '

DECLARE
    v_customer appointments.customerid%TYPE := &app_customer;
    v_status appointments.status%TYPE := '&app_status';
    v_mechanic appointments.assignedmechanic%TYPE := &app_mechanic;
BEGIN
    insert_appointment(v_customer, v_status, v_mechanic);
END;
/

-- Option 5: List All Appointments
DECLARE
BEGIN
    get_all_appointments;
END;
/

-- OPTION 4 (Appointment Services Management Sub-Menu)
PROMPT
PROMPT Appointment Services Management Options:
PROMPT   1. Get Specific Appointment Service
PROMPT   2. Update Appointment Service
PROMPT   3. Delete Appointment Service
PROMPT   4. Add Appointment Service
PROMPT   5. List All Appointment Services
PROMPT

ACCEPT apptserv_option PROMPT 'Enter your appointment services option (1-5): '

DECLARE
    v_option NUMBER := &apptserv_option;
BEGIN
    CASE v_option
        WHEN 1 THEN DBMS_OUTPUT.PUT_LINE('Option 1 selected: Get Specific Appointment Service.');
        WHEN 2 THEN DBMS_OUTPUT.PUT_LINE('Option 2 selected: Update Appointment Service.');
        WHEN 3 THEN DBMS_OUTPUT.PUT_LINE('Option 3 selected: Delete Appointment Service.');
        WHEN 4 THEN DBMS_OUTPUT.PUT_LINE('Option 4 selected: Add Appointment Service.');
        WHEN 5 THEN DBMS_OUTPUT.PUT_LINE('Option 5 selected: List All Appointment Services.');
        ELSE DBMS_OUTPUT.PUT_LINE('Invalid option selected.');
    END CASE;
END;
/

-- Option 1: Get Specific Appointment Service
ACCEPT as_app_id PROMPT 'Enter the Appointment ID: '
ACCEPT as_service_id PROMPT 'Enter the Service ID: '

DECLARE
    v_app_id appointmentservices.appointmentid%TYPE := &as_app_id;
    v_service_id appointmentservices.serviceid%TYPE := &as_service_id;
BEGIN
    get_appointment_service(v_app_id, v_service_id);
END;
/

-- Option 2: Update Appointment Service
ACCEPT uas_app_id PROMPT 'Enter the Appointment ID to update: '
ACCEPT uas_service_id PROMPT 'Enter the Service ID to update: '
ACCEPT new_item_id PROMPT 'Enter new Item ID: '
ACCEPT new_quantity PROMPT 'Enter new Quantity Used: '

DECLARE
    v_app_id appointmentservices.appointmentid%TYPE := &uas_app_id;
    v_service_id appointmentservices.serviceid%TYPE := &uas_service_id;
    v_item_id appointmentservices.itemid%TYPE := &new_item_id;
    v_quantity appointmentservices.quantityused%TYPE := &new_quantity;
BEGIN
    update_appointment_service(v_app_id, v_service_id, v_item_id, v_quantity);
END;
/

-- Option 3: Delete Appointment Service
ACCEPT das_app_id PROMPT 'Enter the Appointment ID to delete: '
ACCEPT das_service_id PROMPT 'Enter the Service ID to delete: '

DECLARE
    v_app_id appointmentservices.appointmentid%TYPE := &das_app_id;
    v_service_id appointmentservices.serviceid%TYPE := &das_service_id;
BEGIN
    delete_appointment_service(v_app_id, v_service_id);
END;
/

-- Option 4: Add Appointment Service
ACCEPT aa_app_id PROMPT 'Enter the Appointment ID: '
ACCEPT aa_service_id PROMPT 'Enter the Service ID: '
ACCEPT aa_item_id PROMPT 'Enter the Item ID: '
ACCEPT aa_quantity PROMPT 'Enter Quantity Used: '

DECLARE
    v_app_id appointmentservices.appointmentid%TYPE := &aa_app_id;
    v_service_id appointmentservices.serviceid%TYPE := &aa_service_id;
    v_item_id appointmentservices.itemid%TYPE := &aa_item_id;
    v_quantity appointmentservices.quantityused%TYPE := &aa_quantity;
BEGIN
    insert_appointment_service(v_app_id, v_service_id, v_item_id, v_quantity);
END;
/

-- Option 5: List All Appointment Services
DECLARE
BEGIN
    get_all_appointment_services;
END;
/


-- OPTION 5 (Services Management Sub-Menu)
PROMPT
PROMPT Services Management Options:
PROMPT   1. Get Specific Service
PROMPT   2. Update Service
PROMPT   3. Delete Service
PROMPT   4. Add Service
PROMPT   5. List All Services
PROMPT

ACCEPT service_option PROMPT 'Enter your service management option (1-5): '

DECLARE
    v_option NUMBER := &service_option;
BEGIN
    CASE v_option
        WHEN 1 THEN DBMS_OUTPUT.PUT_LINE('Option 1 selected: Get Specific Service.');
        WHEN 2 THEN DBMS_OUTPUT.PUT_LINE('Option 2 selected: Update Service.');
        WHEN 3 THEN DBMS_OUTPUT.PUT_LINE('Option 3 selected: Delete Service.');
        WHEN 4 THEN DBMS_OUTPUT.PUT_LINE('Option 4 selected: Add Service.');
        WHEN 5 THEN DBMS_OUTPUT.PUT_LINE('Option 5 selected: List All Services.');
        ELSE DBMS_OUTPUT.PUT_LINE('Invalid option selected.');
    END CASE;
END;
/

-- Option 1: Get Specific Service
ACCEPT service_id PROMPT 'Enter the Service ID to retrieve: '

DECLARE
    v_service_id services.serviceid%TYPE := &service_id;
BEGIN
    get_service(v_service_id);
END;
/

-- Option 2: Update Service
ACCEPT upd_service_id PROMPT 'Enter the Service ID to update: '
ACCEPT upd_name PROMPT 'Enter new Service Name: '
ACCEPT upd_description PROMPT 'Enter new Service Description: '

DECLARE
    v_service_id services.serviceid%TYPE := &upd_service_id;
    v_name services.name%TYPE := '&upd_name';
    v_description services.description%TYPE := '&upd_description';
BEGIN
    update_service(v_service_id, v_name, v_description);
END;
/

-- Option 3: Delete Service
ACCEPT del_service_id PROMPT 'Enter the Service ID to delete: '

DECLARE
    v_service_id services.serviceid%TYPE := &del_service_id;
BEGIN
    delete_service(v_service_id);
END;
/

-- Option 4: Add Service
ACCEPT add_service_name PROMPT 'Enter Service Name: '
ACCEPT add_service_desc PROMPT 'Enter Service Description: '

DECLARE
    v_name services.name%TYPE := '&add_service_name';
    v_description services.description%TYPE := '&add_service_desc';
BEGIN
    insert_service(v_name, v_description);
END;
/

-- Option 5: List All Services
DECLARE
BEGIN
    get_all_services;
END;
/

-- OPTION 6 (Inventory Management Sub-Menu)
PROMPT
PROMPT Inventory Management Options:
PROMPT   1. Get Specific Inventory Item
PROMPT   2. List All Inventory Items
PROMPT

ACCEPT inventory_option PROMPT 'Enter your inventory management option (1-2): '

DECLARE
    v_option NUMBER := &inventory_option;
BEGIN
    CASE v_option
        WHEN 1 THEN DBMS_OUTPUT.PUT_LINE('Option 1 selected: Get Specific Inventory Item.');
        WHEN 2 THEN DBMS_OUTPUT.PUT_LINE('Option 5 selected: List All Inventory Items.');
        ELSE DBMS_OUTPUT.PUT_LINE('Invalid option selected.');
    END CASE;
END;
/

-- Option 1: Get Specific Inventory Item
ACCEPT item_id PROMPT 'Enter the Inventory Item ID to retrieve: '

DECLARE
    v_itemid inventory.itemid%TYPE := &item_id;
BEGIN
    get_inventory_item(v_itemid);
END;
/

-- Option 2: List All Inventory Items
DECLARE
BEGIN
    get_all_inventory;
END;
/

--OPTION 7 (Invoice Management Sub-Menu)
PROMPT
PROMPT Invoice Management Options:
PROMPT   1. Get Specific Invoice
PROMPT   2. List All Invoices
PROMPT

ACCEPT invoice_option PROMPT 'Enter your invoice management option (1-2): '

DECLARE
    v_option NUMBER := &invoice_option;
BEGIN
    CASE v_option
        WHEN 1 THEN DBMS_OUTPUT.PUT_LINE('Option 1 selected: Get Specific Invoice.');
        WHEN 2 THEN DBMS_OUTPUT.PUT_LINE('Option 5 selected: List All Invoices.');
        ELSE DBMS_OUTPUT.PUT_LINE('Invalid option selected.');
    END CASE;
END;
/

-- Option 1: Get Specific Invoice
ACCEPT inv_id PROMPT 'Enter the Invoice ID to retrieve: '

DECLARE
    v_inv_id invoices.invoiceid%TYPE := &inv_id;
BEGIN
    get_invoice(v_inv_id);
END;
/

-- Option 2: List All Invoices
DECLARE
BEGIN
    get_all_invoices;
END;
/

--OPTION 8 LOGOUT
PROMPT
PROMPT Logout Option:
PROMPT   8. Logout
PROMPT
ACCEPT logout_option PROMPT 'Enter your logout option (10 to logout): '

DECLARE
    v_logout NUMBER := &logout_option;
BEGIN
    IF v_logout = 10 THEN
       DBMS_OUTPUT.PUT_LINE('Logging out...');
    ELSE
       DBMS_OUTPUT.PUT_LINE('Invalid logout option. Please enter 10 to logout.');
    END IF;
END;
/

-- Optionally, exit the SQL*Plus session:
EXIT;
















--======================================================
-- MECHANIC DASHBOARD - LOGIN
--======================================================
SET SERVEROUTPUT ON;
ACCEPT username PROMPT 'Enter your username: '
ACCEPT password PROMPT 'Enter your password: '

-- Login block: validate credentials and ensure role = 3.
DECLARE
    v_username users.name%TYPE   := '&username';
    v_password users.password%TYPE := '&password';
    v_roleid   users.roleid%TYPE;
    v_userid   users.userid%TYPE;
BEGIN
    SELECT userid, roleid
      INTO v_userid, v_roleid
      FROM users
     WHERE name = v_username
       AND password = v_password;
       
    IF v_roleid = 3 THEN
       DBMS_OUTPUT.PUT_LINE('Welcome, Mechanic. Opening Mechanic Dashboard...');
       DBMS_OUTPUT.PUT_LINE('Your Mechanic ID is: ' || v_userid);
    ELSE
       DBMS_OUTPUT.PUT_LINE('Access Denied: You are not a Mechanic.');
    END IF;
EXCEPTION
    WHEN NO_DATA_FOUND THEN
       DBMS_OUTPUT.PUT_LINE('Invalid credentials. Please try again.');
    WHEN OTHERS THEN
       DBMS_OUTPUT.PUT_LINE('An error occurred: ' || SQLERRM);
END;
/
   
--======================================================
-- MECHANIC MAIN DASHBOARD MENU
--======================================================
PROMPT
PROMPT Mechanic Main Dashboard Options:
PROMPT   1. Appointments Management (View Only)
PROMPT   2. Appointment Services Management (View Only)
PROMPT   3. Inventory Management (View Only)
PROMPT   4. Logout
PROMPT

ACCEPT mech_main_option PROMPT 'Enter your main dashboard option (1-4): '

DECLARE
    v_option NUMBER := &mech_main_option;
BEGIN
    CASE v_option
      WHEN 1 THEN DBMS_OUTPUT.PUT_LINE('You selected Appointments Management.');
      WHEN 2 THEN DBMS_OUTPUT.PUT_LINE('You selected Appointment Services Management.');
      WHEN 3 THEN DBMS_OUTPUT.PUT_LINE('You selected Inventory Management (View Only).');
      WHEN 4 THEN DBMS_OUTPUT.PUT_LINE('Logging out...');
      ELSE DBMS_OUTPUT.PUT_LINE('Invalid option selected.');
    END CASE;
END;
/
   
-- MODULE 1: APPOINTMENTS MANAGEMENT (View Only)
-- (Only GET specific and List All, filtered by mechanic)
PROMPT
PROMPT Appointments Management Options (Mechanic Only):
PROMPT   1. Get Specific Appointment (Assigned to you)
PROMPT   2. List All Appointments (Assigned to you)
PROMPT
ACCEPT mech_appt_option PROMPT 'Enter your appointment management option (1-2): '

DECLARE
    v_option NUMBER := &mech_appt_option;
BEGIN
    CASE v_option
        WHEN 1 THEN DBMS_OUTPUT.PUT_LINE('Option 1: Get Specific Appointment.');
        WHEN 2 THEN DBMS_OUTPUT.PUT_LINE('Option 2: List All Appointments.');
        ELSE DBMS_OUTPUT.PUT_LINE('Invalid option selected.');
    END CASE;
END;
/

-- For Option 1: Get Specific Appointment (Mechanic)
-- Prompt the mechanic to enter their ID and the appointment ID.
ACCEPT mech_id PROMPT 'Enter your Mechanic ID (from login): '
ACCEPT app_id PROMPT 'Enter the Appointment ID to retrieve: '

DECLARE
    v_mech_id users.userid%TYPE := &mech_id;
    v_app_id appointments.appointmentid%TYPE := &app_id;
BEGIN
    -- Call a mechanic-specific procedure that checks that the appointment is assigned to the given mechanic.
    get_mechanic_appointment(v_app_id, v_mech_id);
END;
/

-- For Option 2: List All Appointments (Mechanic)
ACCEPT mech_id PROMPT 'Enter your Mechanic ID (from login): '

DECLARE
    v_mech_id users.userid%TYPE := &mech_id;
BEGIN
    get_all_mechanic_appointments(v_mech_id);
END;
/

-- MODULE 2: APPOINTMENT SERVICES MANAGEMENT (View Only)
-- (Only GET specific and List All, filtered by the mechanic)
PROMPT
PROMPT Appointment Services Management Options (Mechanic Only):
PROMPT   1. Get Specific Appointment Service (for your appointment)
PROMPT   2. List All Appointment Services (for appointments assigned to you)
PROMPT

ACCEPT mech_apptserv_option PROMPT 'Enter your appointment services option (1-2): '

DECLARE
    v_option NUMBER := &mech_apptserv_option;
BEGIN
    CASE v_option
        WHEN 1 THEN DBMS_OUTPUT.PUT_LINE('Option 1: Get Specific Appointment Service.');
        WHEN 2 THEN DBMS_OUTPUT.PUT_LINE('Option 2: List All Appointment Services.');
        ELSE DBMS_OUTPUT.PUT_LINE('Invalid option selected.');
    END CASE;
END;
/

-- For Appointment Services Option 1: Get Specific Appointment Service (Mechanic)
ACCEPT mech_id PROMPT 'Enter your Mechanic ID (from login): '
ACCEPT app_id PROMPT 'Enter the Appointment ID (assigned to you): '
ACCEPT serv_id PROMPT 'Enter the Service ID for that appointment: '

DECLARE
    v_mech_id users.userid%TYPE := &mech_id;
    v_app_id appointmentservices.appointmentid%TYPE := &app_id;
    v_serv_id appointmentservices.serviceid%TYPE := &serv_id;
BEGIN
    get_mechanic_appointment_service(v_app_id, v_serv_id, v_mech_id);
END;
/

-- For Appointment Services Option 2: List All Appointment Services (Mechanic)
ACCEPT mech_id PROMPT 'Enter your Mechanic ID (from login): '

DECLARE
    v_mech_id users.userid%TYPE := &mech_id;
BEGIN
    get_all_mechanic_appointment_services(v_mech_id);
END;
/

-- MODULE 3: INVENTORY MANAGEMENT (View Only)
PROMPT Inventory Management Options (View Only):
PROMPT   1. Get Specific Inventory Item
PROMPT   2. List All Inventory Items
PROMPT   3. Update Item
ACCEPT mech_inv_option PROMPT 'Enter your inventory option (1-2): '

DECLARE
    v_option NUMBER := &mech_inv_option;
BEGIN
    CASE v_option
        WHEN 1 THEN DBMS_OUTPUT.PUT_LINE('Option 1: Get Specific Inventory Item.');
        WHEN 2 THEN DBMS_OUTPUT.PUT_LINE('Option 2: List All Inventory Items.');
        WHEN 3 THEN DBMS_OUTPUT.PUT_LINE('Option 3: Update Item.');
        ELSE DBMS_OUTPUT.PUT_LINE('Invalid option selected.');
    END CASE;
END;
/

-- Option 1: Get Specific Inventory Item (same as for Sales Rep)
ACCEPT item_id PROMPT 'Enter the Inventory Item ID to retrieve: '

DECLARE
    v_itemid inventory.itemid%TYPE := &item_id;
BEGIN
    get_inventory_item(v_itemid);
END;
/

-- Option 2: List All Inventory Items
DECLARE
BEGIN
    get_all_inventory;
END;
/

-- Option 3: Update Inventory Quantity (for service usage)
ACCEPT item_id PROMPT 'Enter the Inventory Item ID to update quantity: '
ACCEPT new_qty PROMPT 'Enter the new quantity (must be less than current): '

DECLARE
    v_itemid inventory.itemid%TYPE := &item_id;
    v_new_qty inventory.quantity%TYPE := &new_qty;
BEGIN
    update_inventory_quantity(v_itemid, v_new_qty);
END;
/

-- MODULE 4: LOGOUT
PROMPT
PROMPT Logout Option:
PROMPT   4. Logout
PROMPT
ACCEPT logout_option PROMPT 'Enter your logout option (4 to logout): '

DECLARE
    v_logout NUMBER := &logout_option;
BEGIN
    IF v_logout = 4 THEN
       DBMS_OUTPUT.PUT_LINE('Logging out...');
    ELSE
       DBMS_OUTPUT.PUT_LINE('Invalid logout option. Please enter 4 to logout.');
    END IF;
END;
/

EXIT;




