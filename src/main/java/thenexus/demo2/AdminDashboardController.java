package thenexus.demo2;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Image;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import thenexus.demo2.dao.*;
import thenexus.demo2.model.*;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.Optional;
import javafx.event.ActionEvent;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.properties.UnitValue;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.borders.Border;






import java.util.List;
import java.util.Optional;

public class AdminDashboardController {

    @FXML
    private AnchorPane rootPane; // Reference to the root pane

    // Single TableView used for displaying various entity types.
    @FXML
    private TableView<Object> tableView;

    @FXML
    public void initialize() {
        // Default view is set to Customers.
        configureCustomerTable();
    }

    // ---------------------- CUSTOMER FUNCTIONALITIES ---------------------- //

    @FXML
    private void handleGetSpecificCustomer() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Get Specific Customer");
        dialog.setHeaderText("Enter Customer ID");
        dialog.setContentText("Customer ID:");
        dialog.getDialogPane().getStylesheets().add(getClass().getResource("/thenexus/demo2/alert.css").toExternalForm());

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent() && !result.get().trim().isEmpty()) {
            try {
                Long customerId = Long.parseLong(result.get().trim());
                CustomerDAO customerDAO = new CustomerDAO();
                Customer customer = customerDAO.getById(customerId);
                if (customer != null) {
                    configureCustomerTable();
                    ObservableList<Customer> data = FXCollections.observableArrayList();
                    data.add(customer);
                    tableView.setItems(FXCollections.observableArrayList(data));
                } else {
                    showAlert(Alert.AlertType.INFORMATION, "Not Found", "Customer with ID " + customerId + " not found.");
                }
            } catch (NumberFormatException e) {
                showAlert(Alert.AlertType.ERROR, "Invalid Input", "Please enter a valid numeric Customer ID.");
            }
        } else {
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Customer ID is required.");
        }
    }

    @FXML
    private void handleAddCustomer() {
        // Prompt for Customer Name
        TextInputDialog nameDialog = new TextInputDialog();
        nameDialog.setTitle("Add New Customer");
        nameDialog.setHeaderText("Enter Customer Name");
        nameDialog.setContentText("Name:");
        nameDialog.getDialogPane().getStylesheets().add(getClass().getResource("/thenexus/demo2/style.css").toExternalForm());
        Optional<String> nameResult = nameDialog.showAndWait();
        if (!nameResult.isPresent() || nameResult.get().trim().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Name is required.");
            return;
        }
        String name = nameResult.get().trim();

        // Prompt for Customer Email
        TextInputDialog emailDialog = new TextInputDialog();
        emailDialog.setTitle("Add New Customer");
        emailDialog.setHeaderText("Enter Customer Email");
        emailDialog.setContentText("Email:");
        emailDialog.getDialogPane().getStylesheets().add(getClass().getResource("/thenexus/demo2/style.css").toExternalForm());
        Optional<String> emailResult = emailDialog.showAndWait();
        if (!emailResult.isPresent() || emailResult.get().trim().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Email is required.");
            return;
        }
        String email = emailResult.get().trim();
        if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Please enter a valid email address.");
            return;
        }

        // Prompt for Customer Contact
        TextInputDialog contactDialog = new TextInputDialog();
        contactDialog.setTitle("Add New Customer");
        contactDialog.setHeaderText("Enter Customer Contact");
        contactDialog.setContentText("Contact:");
        contactDialog.getDialogPane().getStylesheets().add(getClass().getResource("/thenexus/demo2/style.css").toExternalForm());
        Optional<String> contactResult = contactDialog.showAndWait();
        if (!contactResult.isPresent() || contactResult.get().trim().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Contact is required.");
            return;
        }
        Long contact;
        try {
            contact = Long.parseLong(contactResult.get().trim());
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Contact must be numeric.");
            return;
        }

        // Prompt for Vehicle VIN
        TextInputDialog vinDialog = new TextInputDialog();
        vinDialog.setTitle("Add New Customer");
        vinDialog.setHeaderText("Enter Vehicle Identification Number (VIN)");
        vinDialog.setContentText("VIN:");
        vinDialog.getDialogPane().getStylesheets().add(getClass().getResource("/thenexus/demo2/style.css").toExternalForm());
        Optional<String> vinResult = vinDialog.showAndWait();
        if (!vinResult.isPresent() || vinResult.get().trim().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "VIN is required.");
            return;
        }
        Long vin;
        try {
            vin = Long.parseLong(vinResult.get().trim());
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "VIN must be numeric.");
            return;
        }

        // Prompt for Customer Address (optional)
        TextInputDialog addressDialog = new TextInputDialog();
        addressDialog.setTitle("Add New Customer");
        addressDialog.setHeaderText("Enter Customer Address");
        addressDialog.setContentText("Address:");
        addressDialog.getDialogPane().getStylesheets().add(getClass().getResource("/thenexus/demo2/style.css").toExternalForm());
        Optional<String> addressResult = addressDialog.showAndWait();
        String address = addressResult.orElse("").trim();

        CustomerDAO customerDAO = new CustomerDAO();
        customerDAO.insertCustomer(name, email, contact, vin, address);
        showAlert(Alert.AlertType.INFORMATION, "Success", "Customer added successfully!");
    }

    @FXML
    private void handleDeleteCustomer() {
        TextInputDialog idDialog = new TextInputDialog();
        idDialog.setTitle("Delete Customer");
        idDialog.setHeaderText("Enter Customer ID to delete");
        idDialog.setContentText("Customer ID:");
        idDialog.getDialogPane().getStylesheets().add(getClass().getResource("/thenexus/demo2/alert.css").toExternalForm());
        Optional<String> idResult = idDialog.showAndWait();

        if (!idResult.isPresent() || idResult.get().trim().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Customer ID is required.");
            return;
        }
        Long customerId;
        try {
            customerId = Long.parseLong(idResult.get().trim());
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Customer ID must be numeric.");
            return;
        }
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirm Delete");
        confirmAlert.setHeaderText(null);
        confirmAlert.setContentText("Are you sure you want to delete customer with ID " + customerId + "?");
        confirmAlert.getDialogPane().getStylesheets().add(getClass().getResource("/thenexus/demo2/alert.css").toExternalForm());
        Optional<ButtonType> confirmResult = confirmAlert.showAndWait();
        if (confirmResult.isPresent() && confirmResult.get() == ButtonType.OK) {
            CustomerDAO customerDAO = new CustomerDAO();
            customerDAO.deleteCustomer(customerId);
            showAlert(Alert.AlertType.INFORMATION, "Deleted", "Customer deleted successfully!");
        }
    }

    @FXML
    private void handleUpdateCustomer() {
        TextInputDialog idDialog = new TextInputDialog();
        idDialog.setTitle("Update Customer");
        idDialog.setHeaderText("Enter Customer ID to update");
        idDialog.setContentText("Customer ID:");
        idDialog.getDialogPane().getStylesheets().add(getClass().getResource("/thenexus/demo2/alert.css").toExternalForm());
        Optional<String> idResult = idDialog.showAndWait();

        if (!idResult.isPresent() || idResult.get().trim().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Customer ID is required.");
            return;
        }
        Long customerId;
        try {
            customerId = Long.parseLong(idResult.get().trim());
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Customer ID must be numeric.");
            return;
        }

        CustomerDAO customerDAO = new CustomerDAO();
        Customer customer = customerDAO.getById(customerId);
        if (customer == null) {
            showAlert(Alert.AlertType.INFORMATION, "Not Found", "Customer with ID " + customerId + " not found.");
            return;
        }

        TextInputDialog nameDialog = new TextInputDialog(customer.getName());
        nameDialog.setTitle("Update Customer");
        nameDialog.setHeaderText("Enter new Customer Name");
        nameDialog.setContentText("Name:");
        nameDialog.getDialogPane().getStylesheets().add(getClass().getResource("/thenexus/demo2/style.css").toExternalForm());
        Optional<String> nameResult = nameDialog.showAndWait();
        if (!nameResult.isPresent() || nameResult.get().trim().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Name is required.");
            return;
        }
        String name = nameResult.get().trim();

        TextInputDialog emailDialog = new TextInputDialog(customer.getEmail());
        emailDialog.setTitle("Update Customer");
        emailDialog.setHeaderText("Enter new Customer Email");
        emailDialog.setContentText("Email:");
        emailDialog.getDialogPane().getStylesheets().add(getClass().getResource("/thenexus/demo2/style.css").toExternalForm());
        Optional<String> emailResult = emailDialog.showAndWait();
        if (!emailResult.isPresent() || emailResult.get().trim().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Email is required.");
            return;
        }
        String email = emailResult.get().trim();
        if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Please enter a valid email address.");
            return;
        }

        TextInputDialog contactDialog = new TextInputDialog(customer.getContact().toString());
        contactDialog.setTitle("Update Customer");
        contactDialog.setHeaderText("Enter new Customer Contact");
        contactDialog.setContentText("Contact:");
        contactDialog.getDialogPane().getStylesheets().add(getClass().getResource("/thenexus/demo2/style.css").toExternalForm());
        Optional<String> contactResult = contactDialog.showAndWait();
        if (!contactResult.isPresent() || contactResult.get().trim().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Contact is required.");
            return;
        }
        Long contact;
        try {
            contact = Long.parseLong(contactResult.get().trim());
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Contact must be numeric.");
            return;
        }

        String defaultVin = "";
        if (customer.getVehicle() != null) {
            defaultVin = customer.getVehicle().getVin().toString();
        }
        TextInputDialog vinDialog = new TextInputDialog(defaultVin);
        vinDialog.setTitle("Update Customer");
        vinDialog.setHeaderText("Enter new Vehicle Identification Number (VIN)");
        vinDialog.setContentText("VIN:");
        vinDialog.getDialogPane().getStylesheets().add(getClass().getResource("/thenexus/demo2/style.css").toExternalForm());
        Optional<String> vinResult = vinDialog.showAndWait();
        if (!vinResult.isPresent() || vinResult.get().trim().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "VIN is required.");
            return;
        }
        Long vin;
        try {
            vin = Long.parseLong(vinResult.get().trim());
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "VIN must be numeric.");
            return;
        }

        TextInputDialog addressDialog = new TextInputDialog(customer.getAddress());
        addressDialog.setTitle("Update Customer");
        addressDialog.setHeaderText("Enter new Customer Address");
        addressDialog.setContentText("Address:");
        addressDialog.getDialogPane().getStylesheets().add(getClass().getResource("/thenexus/demo2/style.css").toExternalForm());
        Optional<String> addressResult = addressDialog.showAndWait();
        String address = addressResult.orElse("").trim();

        customerDAO.updateCustomer(customerId, name, email, contact, vin, address);
        showAlert(Alert.AlertType.INFORMATION, "Success", "Customer updated successfully!");
    }

    @FXML
    private void handleAllCustomers() {
        CustomerDAO customerDAO = new CustomerDAO();
        List<Customer> customers = customerDAO.getAllCustomers();
        if (customers == null || customers.isEmpty()) {
            showAlert(Alert.AlertType.INFORMATION, "No Customers", "No customers found.");
            tableView.getItems().clear();
        } else {
            configureCustomerTable();
            ObservableList<Customer> data = FXCollections.observableArrayList(customers);
            tableView.setItems(FXCollections.observableArrayList(data));
        }
    }

    // ---------------------- VEHICLE FUNCTIONALITIES ---------------------- //

    @FXML
    private void handleGetSpecificVehicle() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Get Specific Vehicle");
        dialog.setHeaderText("Enter Vehicle VIN");
        dialog.setContentText("VIN:");
        dialog.getDialogPane().getStylesheets().add(getClass().getResource("/thenexus/demo2/alert.css").toExternalForm());

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent() && !result.get().trim().isEmpty()) {
            try {
                Long vin = Long.parseLong(result.get().trim());
                VehicleDAO vehicleDAO = new VehicleDAO();
                Vehicle vehicle = vehicleDAO.getByVin(vin);
                if (vehicle != null) {
                    configureVehicleTable();
                    ObservableList<Vehicle> data = FXCollections.observableArrayList();
                    data.add(vehicle);
                    tableView.setItems(FXCollections.observableArrayList(data));
                } else {
                    showAlert(Alert.AlertType.INFORMATION, "Not Found", "Vehicle with VIN " + vin + " not found.");
                }
            } catch (NumberFormatException e) {
                showAlert(Alert.AlertType.ERROR, "Invalid Input", "VIN must be numeric.");
            }
        } else {
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "VIN is required.");
        }
    }

    @FXML
    private void handleAddVehicle() {
        // Prompt for VIN
        TextInputDialog vinDialog = new TextInputDialog();
        vinDialog.setTitle("Add New Vehicle");
        vinDialog.setHeaderText("Enter Vehicle VIN");
        vinDialog.setContentText("VIN:");
        vinDialog.getDialogPane().getStylesheets().add(getClass().getResource("/thenexus/demo2/style.css").toExternalForm());
        Optional<String> vinResult = vinDialog.showAndWait();
        if (!vinResult.isPresent() || vinResult.get().trim().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "VIN is required.");
            return;
        }
        Long vin;
        try {
            vin = Long.parseLong(vinResult.get().trim());
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "VIN must be numeric.");
            return;
        }

        // Prompt for Model
        TextInputDialog modelDialog = new TextInputDialog();
        modelDialog.setTitle("Add New Vehicle");
        modelDialog.setHeaderText("Enter Vehicle Model");
        modelDialog.setContentText("Model:");
        modelDialog.getDialogPane().getStylesheets().add(getClass().getResource("/thenexus/demo2/style.css").toExternalForm());
        Optional<String> modelResult = modelDialog.showAndWait();
        if (!modelResult.isPresent() || modelResult.get().trim().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Model is required.");
            return;
        }
        String model = modelResult.get().trim();

        // Prompt for Year
        TextInputDialog yearDialog = new TextInputDialog();
        yearDialog.setTitle("Add New Vehicle");
        yearDialog.setHeaderText("Enter Vehicle Year");
        yearDialog.setContentText("Year:");
        yearDialog.getDialogPane().getStylesheets().add(getClass().getResource("/thenexus/demo2/style.css").toExternalForm());
        Optional<String> yearResult = yearDialog.showAndWait();
        if (!yearResult.isPresent() || yearResult.get().trim().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Year is required.");
            return;
        }
        Integer year;
        try {
            year = Integer.parseInt(yearResult.get().trim());
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Year must be numeric.");
            return;
        }

        // Prompt for License Plate
        TextInputDialog licenseDialog = new TextInputDialog();
        licenseDialog.setTitle("Add New Vehicle");
        licenseDialog.setHeaderText("Enter Vehicle License Plate");
        licenseDialog.setContentText("License Plate:");
        licenseDialog.getDialogPane().getStylesheets().add(getClass().getResource("/thenexus/demo2/style.css").toExternalForm());
        Optional<String> licenseResult = licenseDialog.showAndWait();
        if (!licenseResult.isPresent() || licenseResult.get().trim().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "License Plate is required.");
            return;
        }
        String licensePlate = licenseResult.get().trim();

        VehicleDAO vehicleDAO = new VehicleDAO();
        vehicleDAO.insertVehicle(vin, model, year, licensePlate);
        showAlert(Alert.AlertType.INFORMATION, "Success", "Vehicle added successfully!");
    }

    @FXML
    private void handleDeleteVehicle() {
        TextInputDialog vinDialog = new TextInputDialog();
        vinDialog.setTitle("Delete Vehicle");
        vinDialog.setHeaderText("Enter Vehicle VIN to delete");
        vinDialog.setContentText("VIN:");
        vinDialog.getDialogPane().getStylesheets().add(getClass().getResource("/thenexus/demo2/alert.css").toExternalForm());
        Optional<String> vinResult = vinDialog.showAndWait();
        if (!vinResult.isPresent() || vinResult.get().trim().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "VIN is required.");
            return;
        }
        Long vin;
        try {
            vin = Long.parseLong(vinResult.get().trim());
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "VIN must be numeric.");
            return;
        }
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirm Delete");
        confirmAlert.setHeaderText(null);
        confirmAlert.setContentText("Are you sure you want to delete vehicle with VIN " + vin + "?");
        confirmAlert.getDialogPane().getStylesheets().add(getClass().getResource("/thenexus/demo2/alert.css").toExternalForm());
        Optional<ButtonType> confirmResult = confirmAlert.showAndWait();
        if (confirmResult.isPresent() && confirmResult.get() == ButtonType.OK) {
            VehicleDAO vehicleDAO = new VehicleDAO();
            vehicleDAO.deleteVehicle(vin);
            showAlert(Alert.AlertType.INFORMATION, "Deleted", "Vehicle deleted successfully!");
        }
    }

    @FXML
    private void handleUpdateVehicle() {
        TextInputDialog vinDialog = new TextInputDialog();
        vinDialog.setTitle("Update Vehicle");
        vinDialog.setHeaderText("Enter Vehicle VIN to update");
        vinDialog.setContentText("VIN:");
        vinDialog.getDialogPane().getStylesheets().add(getClass().getResource("/thenexus/demo2/alert.css").toExternalForm());
        Optional<String> vinResult = vinDialog.showAndWait();
        if (!vinResult.isPresent() || vinResult.get().trim().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "VIN is required.");
            return;
        }
        Long vin;
        try {
            vin = Long.parseLong(vinResult.get().trim());
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "VIN must be numeric.");
            return;
        }
        VehicleDAO vehicleDAO = new VehicleDAO();
        Vehicle vehicle = vehicleDAO.getByVin(vin);
        if (vehicle == null) {
            showAlert(Alert.AlertType.INFORMATION, "Not Found", "Vehicle with VIN " + vin + " not found.");
            return;
        }
        TextInputDialog vinInputDialog = new TextInputDialog(vehicle.getVin().toString());
        vinInputDialog.setTitle("Update Vehicle");
        vinInputDialog.setHeaderText("Vehicle VIN (cannot be changed)");
        vinInputDialog.setContentText("VIN:");
        vinInputDialog.getDialogPane().getStylesheets().add(getClass().getResource("/thenexus/demo2/alert.css").toExternalForm());
        Optional<String> vinInputResult = vinInputDialog.showAndWait();
        if (vinInputResult.isPresent()) {
            String newVinStr = vinInputResult.get().trim();
            if (!newVinStr.equals(vehicle.getVin().toString())) {
                showAlert(Alert.AlertType.ERROR, "Invalid Input", "VIN cannot be changed.");
                return;
            }
        }
        TextInputDialog modelDialog = new TextInputDialog(vehicle.getModel());
        modelDialog.setTitle("Update Vehicle");
        modelDialog.setHeaderText("Enter new Vehicle Model");
        modelDialog.setContentText("Model:");
        modelDialog.getDialogPane().getStylesheets().add(getClass().getResource("/thenexus/demo2/style.css").toExternalForm());
        Optional<String> modelResult = modelDialog.showAndWait();
        if (!modelResult.isPresent() || modelResult.get().trim().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Model is required.");
            return;
        }
        String model = modelResult.get().trim();
        TextInputDialog yearDialog = new TextInputDialog(vehicle.getYear().toString());
        yearDialog.setTitle("Update Vehicle");
        yearDialog.setHeaderText("Enter new Vehicle Year");
        yearDialog.setContentText("Year:");
        yearDialog.getDialogPane().getStylesheets().add(getClass().getResource("/thenexus/demo2/style.css").toExternalForm());
        Optional<String> yearResult = yearDialog.showAndWait();
        if (!yearResult.isPresent() || yearResult.get().trim().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Year is required.");
            return;
        }
        Integer year;
        try {
            year = Integer.parseInt(yearResult.get().trim());
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Year must be numeric.");
            return;
        }
        TextInputDialog licenseDialog = new TextInputDialog(vehicle.getLicensePlate());
        licenseDialog.setTitle("Update Vehicle");
        licenseDialog.setHeaderText("Enter new Vehicle License Plate");
        licenseDialog.setContentText("License Plate:");
        licenseDialog.getDialogPane().getStylesheets().add(getClass().getResource("/thenexus/demo2/style.css").toExternalForm());
        Optional<String> licenseResult = licenseDialog.showAndWait();
        if (!licenseResult.isPresent() || licenseResult.get().trim().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "License Plate is required.");
            return;
        }
        String licensePlate = licenseResult.get().trim();
        vehicleDAO.updateVehicle(vin, model, year, licensePlate);
        showAlert(Alert.AlertType.INFORMATION, "Success", "Vehicle updated successfully!");
    }

    @FXML
    private void handleAllVehicles() {
        VehicleDAO vehicleDAO = new VehicleDAO();
        List<Vehicle> vehicles = vehicleDAO.getAllVehicles();
        if (vehicles == null || vehicles.isEmpty()) {
            showAlert(Alert.AlertType.INFORMATION, "No Vehicles", "No vehicles found.");
            tableView.getItems().clear();
        } else {
            configureVehicleTable();
            ObservableList<Vehicle> data = FXCollections.observableArrayList(vehicles);
            tableView.setItems(FXCollections.observableArrayList(data));
        }
    }

    // ---------------------- APPOINTMENT FUNCTIONALITIES ---------------------- //

    @FXML
    private void handleGetSpecificAppointment() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Get Specific Appointment");
        dialog.setHeaderText("Enter Appointment ID");
        dialog.setContentText("Appointment ID:");
        dialog.getDialogPane().getStylesheets().add(getClass().getResource("/thenexus/demo2/alert.css").toExternalForm());

        Optional<String> result = dialog.showAndWait();
        if(result.isPresent() && !result.get().trim().isEmpty()){
            try {
                Long appointmentId = Long.parseLong(result.get().trim());
                AppointmentDAO appointmentDAO = new AppointmentDAO();
                Appointment appointment = appointmentDAO.getByAppointmentId(appointmentId);
                if(appointment != null){
                    configureAppointmentTable();
                    ObservableList<Appointment> data = FXCollections.observableArrayList();
                    data.add(appointment);
                    tableView.setItems(FXCollections.observableArrayList(data));
                } else {
                    showAlert(Alert.AlertType.INFORMATION, "Not Found", "Appointment with ID " + appointmentId + " not found.");
                }
            } catch(NumberFormatException e){
                showAlert(Alert.AlertType.ERROR, "Invalid Input", "Appointment ID must be numeric.");
            }
        } else {
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Appointment ID is required.");
        }
    }

    @FXML
    private void handleAddAppointment() {
        // Prompt for Customer ID
        TextInputDialog customerDialog = new TextInputDialog();
        customerDialog.setTitle("Add New Appointment");
        customerDialog.setHeaderText("Enter Customer ID");
        customerDialog.setContentText("Customer ID:");
        Optional<String> custResult = customerDialog.showAndWait();
        if(!custResult.isPresent() || custResult.get().trim().isEmpty()){
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Customer ID is required.");
            return;
        }
        Long customerId;
        try {
            customerId = Long.parseLong(custResult.get().trim());
        } catch(NumberFormatException e){
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Customer ID must be numeric.");
            return;
        }

        // Prompt for Status
        TextInputDialog statusDialog = new TextInputDialog();
        statusDialog.setTitle("Add New Appointment");
        statusDialog.setHeaderText("Enter Appointment Status");
        statusDialog.setContentText("Status:");
        Optional<String> statusResult = statusDialog.showAndWait();
        if(!statusResult.isPresent() || statusResult.get().trim().isEmpty()){
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Status is required.");
            return;
        }
        String status = statusResult.get().trim();

        // Prompt for Assigned Mechanic ID
        TextInputDialog mechDialog = new TextInputDialog();
        mechDialog.setTitle("Add New Appointment");
        mechDialog.setHeaderText("Enter Assigned Mechanic ID");
        mechDialog.setContentText("Mechanic ID:");
        Optional<String> mechResult = mechDialog.showAndWait();
        if(!mechResult.isPresent() || mechResult.get().trim().isEmpty()){
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Assigned Mechanic ID is required.");
            return;
        }
        Long mechanicId;
        try {
            mechanicId = Long.parseLong(mechResult.get().trim());
        } catch(NumberFormatException e){
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Mechanic ID must be numeric.");
            return;
        }

        AppointmentDAO appointmentDAO = new AppointmentDAO();
        appointmentDAO.insertAppointment(customerId, status, mechanicId);
        showAlert(Alert.AlertType.INFORMATION, "Success", "Appointment added successfully!");
    }

    @FXML
    private void handleUpdateAppointment() {
        TextInputDialog idDialog = new TextInputDialog();
        idDialog.setTitle("Update Appointment");
        idDialog.setHeaderText("Enter Appointment ID to update");
        idDialog.setContentText("Appointment ID:");
        Optional<String> idResult = idDialog.showAndWait();
        if(!idResult.isPresent() || idResult.get().trim().isEmpty()){
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Appointment ID is required.");
            return;
        }
        Long appointmentId;
        try {
            appointmentId = Long.parseLong(idResult.get().trim());
        } catch(NumberFormatException e){
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Appointment ID must be numeric.");
            return;
        }
        AppointmentDAO appointmentDAO = new AppointmentDAO();
        Appointment appointment = appointmentDAO.getByAppointmentId(appointmentId);
        if(appointment == null){
            showAlert(Alert.AlertType.INFORMATION, "Not Found", "Appointment with ID " + appointmentId + " not found.");
            return;
        }

        // Prompt for new Status
        TextInputDialog statusDialog = new TextInputDialog(appointment.getStatus());
        statusDialog.setTitle("Update Appointment");
        statusDialog.setHeaderText("Enter new Status");
        statusDialog.setContentText("Status:");
        Optional<String> statusResult = statusDialog.showAndWait();
        if(!statusResult.isPresent() || statusResult.get().trim().isEmpty()){
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Status is required.");
            return;
        }
        String status = statusResult.get().trim();

        // Prompt for new Assigned Mechanic ID
        TextInputDialog mechDialog = new TextInputDialog();
        mechDialog.setTitle("Update Appointment");
        mechDialog.setHeaderText("Enter new Assigned Mechanic ID");
        mechDialog.setContentText("Mechanic ID:");
        Optional<String> mechResult = mechDialog.showAndWait();
        if(!mechResult.isPresent() || mechResult.get().trim().isEmpty()){
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Mechanic ID is required.");
            return;
        }
        Long mechanicId;
        try {
            mechanicId = Long.parseLong(mechResult.get().trim());
        } catch(NumberFormatException e){
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Mechanic ID must be numeric.");
            return;
        }

        appointmentDAO.updateAppointment(appointmentId, status, mechanicId);
        showAlert(Alert.AlertType.INFORMATION, "Success", "Appointment updated successfully!");
    }

    @FXML
    private void handleDeleteAppointment() {
        TextInputDialog idDialog = new TextInputDialog();
        idDialog.setTitle("Delete Appointment");
        idDialog.setHeaderText("Enter Appointment ID to delete");
        idDialog.setContentText("Appointment ID:");
        Optional<String> idResult = idDialog.showAndWait();
        if(!idResult.isPresent() || idResult.get().trim().isEmpty()){
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Appointment ID is required.");
            return;
        }
        Long appointmentId;
        try {
            appointmentId = Long.parseLong(idResult.get().trim());
        } catch(NumberFormatException e){
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Appointment ID must be numeric.");
            return;
        }
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirm Delete");
        confirmAlert.setHeaderText(null);
        confirmAlert.setContentText("Are you sure you want to delete appointment with ID " + appointmentId + "?");
        Optional<ButtonType> confirmResult = confirmAlert.showAndWait();
        if(confirmResult.isPresent() && confirmResult.get() == ButtonType.OK){
            AppointmentDAO appointmentDAO = new AppointmentDAO();
            appointmentDAO.deleteAppointment(appointmentId);
            showAlert(Alert.AlertType.INFORMATION, "Deleted", "Appointment deleted successfully!");
        }
    }

    @FXML
    private void handleAllAppointments() {
        AppointmentDAO appointmentDAO = new AppointmentDAO();
        List<Appointment> appointments = appointmentDAO.getAllAppointments();
        if(appointments == null || appointments.isEmpty()){
            showAlert(Alert.AlertType.INFORMATION, "No Appointments", "No appointments found.");
            tableView.getItems().clear();
        } else {
            configureAppointmentTable();
            ObservableList<Appointment> data = FXCollections.observableArrayList(appointments);
            tableView.setItems(FXCollections.observableArrayList(data));
        }
    }

    private void configureAppointmentTable() {
        tableView.getColumns().clear();
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Object, Long> appointmentIdCol = new TableColumn<>("Appointment ID");
        appointmentIdCol.setCellValueFactory(new PropertyValueFactory<>("appointmentId"));

        TableColumn<Object, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));

        TableColumn<Object, Object> dateCol = new TableColumn<>("Appointment Date");
        dateCol.setCellValueFactory(new PropertyValueFactory<>("appointmentDate"));

        TableColumn<Object, Object> customerCol = new TableColumn<>("Customer");
        customerCol.setCellValueFactory(new PropertyValueFactory<>("customer"));

        TableColumn<Object, Object> mechanicCol = new TableColumn<>("Assigned Mechanic");
        mechanicCol.setCellValueFactory(new PropertyValueFactory<>("assignedMechanic"));

        tableView.getColumns().addAll(appointmentIdCol, statusCol, dateCol, customerCol, mechanicCol);
    }

    // ---------------------- APPOINTMENT SERVICE FUNCTIONALITIES ---------------------- //

    @FXML
    private void handleGetSpecificAppointmentService() {
        // Prompt for Appointment ID
        TextInputDialog apptDialog = new TextInputDialog();
        apptDialog.setTitle("Get Specific Appointment Service");
        apptDialog.setHeaderText("Enter Appointment ID");
        apptDialog.setContentText("Appointment ID:");
        Optional<String> apptResult = apptDialog.showAndWait();
        if(!apptResult.isPresent() || apptResult.get().trim().isEmpty()){
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Appointment ID is required.");
            return;
        }
        Long appointmentId;
        try {
            appointmentId = Long.parseLong(apptResult.get().trim());
        } catch(NumberFormatException e){
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Appointment ID must be numeric.");
            return;
        }

        // Prompt for Service ID
        TextInputDialog serviceDialog = new TextInputDialog();
        serviceDialog.setTitle("Get Specific Appointment Service");
        serviceDialog.setHeaderText("Enter Service ID");
        serviceDialog.setContentText("Service ID:");
        Optional<String> serviceResult = serviceDialog.showAndWait();
        if(!serviceResult.isPresent() || serviceResult.get().trim().isEmpty()){
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Service ID is required.");
            return;
        }
        int serviceId;
        try {
            serviceId = Integer.parseInt(serviceResult.get().trim());
        } catch(NumberFormatException e){
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Service ID must be numeric.");
            return;
        }

        AppointmentServiceDAO asDAO = new AppointmentServiceDAO();
        // Assume getById method exists taking appointmentId and serviceId
        AppointmentService appointmentService = asDAO.getByAppointmentServiceId(appointmentId, serviceId);
        if(appointmentService != null){
            configureAppointmentServiceTable();
            ObservableList<AppointmentService> data = FXCollections.observableArrayList();
            data.add(appointmentService);
            tableView.setItems(FXCollections.observableArrayList(data));
        } else {
            showAlert(Alert.AlertType.INFORMATION, "Not Found", "No appointment service found for Appointment ID " + appointmentId + " and Service ID " + serviceId);
        }
    }

    @FXML
    private void handleAddAppointmentService() {
        // (Already implemented earlier)
        TextInputDialog apptIdDialog = new TextInputDialog();
        apptIdDialog.setTitle("Add Appointment Service");
        apptIdDialog.setHeaderText("Enter Appointment ID");
        apptIdDialog.setContentText("Appointment ID:");
        Optional<String> apptIdResult = apptIdDialog.showAndWait();
        if (!apptIdResult.isPresent() || apptIdResult.get().trim().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Appointment ID is required.");
            return;
        }
        Long appointmentId;
        try {
            appointmentId = Long.parseLong(apptIdResult.get().trim());
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Appointment ID must be numeric.");
            return;
        }

        TextInputDialog serviceIdDialog = new TextInputDialog();
        serviceIdDialog.setTitle("Add Appointment Service");
        serviceIdDialog.setHeaderText("Enter Service ID");
        serviceIdDialog.setContentText("Service ID:");
        Optional<String> serviceIdResult = serviceIdDialog.showAndWait();
        if (!serviceIdResult.isPresent() || serviceIdResult.get().trim().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Service ID is required.");
            return;
        }
        int serviceId;
        try {
            serviceId = Integer.parseInt(serviceIdResult.get().trim());
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Service ID must be numeric.");
            return;
        }

        TextInputDialog itemIdDialog = new TextInputDialog();
        itemIdDialog.setTitle("Add Appointment Service");
        itemIdDialog.setHeaderText("Enter Inventory Item ID");
        itemIdDialog.setContentText("Item ID:");
        Optional<String> itemIdResult = itemIdDialog.showAndWait();
        if (!itemIdResult.isPresent() || itemIdResult.get().trim().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Item ID is required.");
            return;
        }
        Long itemId;
        try {
            itemId = Long.parseLong(itemIdResult.get().trim());
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Item ID must be numeric.");
            return;
        }

        TextInputDialog quantityDialog = new TextInputDialog();
        quantityDialog.setTitle("Add Appointment Service");
        quantityDialog.setHeaderText("Enter Quantity Used");
        quantityDialog.setContentText("Quantity:");
        Optional<String> quantityResult = quantityDialog.showAndWait();
        if (!quantityResult.isPresent() || quantityResult.get().trim().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Quantity is required.");
            return;
        }
        int quantity;
        try {
            quantity = Integer.parseInt(quantityResult.get().trim());
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Quantity must be numeric.");
            return;
        }

        AppointmentServiceDAO asDAO = new AppointmentServiceDAO();
        asDAO.insertAppointmentService(appointmentId, serviceId, itemId, quantity);
        showAlert(Alert.AlertType.INFORMATION, "Success", "Appointment Service added successfully!");
    }

    @FXML
    private void handleUpdateAppointmentService() {
        // Prompt for Appointment ID and Service ID to identify the record
        TextInputDialog apptDialog = new TextInputDialog();
        apptDialog.setTitle("Update Appointment Service");
        apptDialog.setHeaderText("Enter Appointment ID");
        apptDialog.setContentText("Appointment ID:");
        Optional<String> apptResult = apptDialog.showAndWait();
        if (!apptResult.isPresent() || apptResult.get().trim().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Appointment ID is required.");
            return;
        }
        Long appointmentId;
        try {
            appointmentId = Long.parseLong(apptResult.get().trim());
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Appointment ID must be numeric.");
            return;
        }

        TextInputDialog serviceDialog = new TextInputDialog();
        serviceDialog.setTitle("Update Appointment Service");
        serviceDialog.setHeaderText("Enter Service ID");
        serviceDialog.setContentText("Service ID:");
        Optional<String> serviceResult = serviceDialog.showAndWait();
        if (!serviceResult.isPresent() || serviceResult.get().trim().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Service ID is required.");
            return;
        }
        int serviceId;
        try {
            serviceId = Integer.parseInt(serviceResult.get().trim());
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Service ID must be numeric.");
            return;
        }

        // Prompt for new Inventory Item ID
        TextInputDialog itemIdDialog = new TextInputDialog();
        itemIdDialog.setTitle("Update Appointment Service");
        itemIdDialog.setHeaderText("Enter new Inventory Item ID");
        itemIdDialog.setContentText("Item ID:");
        Optional<String> itemIdResult = itemIdDialog.showAndWait();
        if (!itemIdResult.isPresent() || itemIdResult.get().trim().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Item ID is required.");
            return;
        }
        Long itemId;
        try {
            itemId = Long.parseLong(itemIdResult.get().trim());
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Item ID must be numeric.");
            return;
        }

        // Prompt for new Quantity Used
        TextInputDialog quantityDialog = new TextInputDialog();
        quantityDialog.setTitle("Update Appointment Service");
        quantityDialog.setHeaderText("Enter new Quantity Used");
        quantityDialog.setContentText("Quantity:");
        Optional<String> quantityResult = quantityDialog.showAndWait();
        if (!quantityResult.isPresent() || quantityResult.get().trim().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Quantity is required.");
            return;
        }
        int quantity;
        try {
            quantity = Integer.parseInt(quantityResult.get().trim());
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Quantity must be numeric.");
            return;
        }

        AppointmentServiceDAO asDAO = new AppointmentServiceDAO();
        asDAO.updateAppointmentService(appointmentId, serviceId, itemId, quantity);
        showAlert(Alert.AlertType.INFORMATION, "Success", "Appointment Service updated successfully!");
    }

    @FXML
    private void handleDeleteAppointmentService() {
        // Prompt for Appointment ID and Service ID
        TextInputDialog apptDialog = new TextInputDialog();
        apptDialog.setTitle("Delete Appointment Service");
        apptDialog.setHeaderText("Enter Appointment ID");
        apptDialog.setContentText("Appointment ID:");
        Optional<String> apptResult = apptDialog.showAndWait();
        if (!apptResult.isPresent() || apptResult.get().trim().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Appointment ID is required.");
            return;
        }
        Long appointmentId;
        try {
            appointmentId = Long.parseLong(apptResult.get().trim());
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Appointment ID must be numeric.");
            return;
        }

        TextInputDialog serviceDialog = new TextInputDialog();
        serviceDialog.setTitle("Delete Appointment Service");
        serviceDialog.setHeaderText("Enter Service ID");
        serviceDialog.setContentText("Service ID:");
        Optional<String> serviceResult = serviceDialog.showAndWait();
        if (!serviceResult.isPresent() || serviceResult.get().trim().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Service ID is required.");
            return;
        }
        int serviceId;
        try {
            serviceId = Integer.parseInt(serviceResult.get().trim());
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Service ID must be numeric.");
            return;
        }

        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirm Delete");
        confirmAlert.setHeaderText(null);
        confirmAlert.setContentText("Are you sure you want to delete the appointment service record for Appointment ID " + appointmentId + " and Service ID " + serviceId + "?");
        Optional<ButtonType> confirmResult = confirmAlert.showAndWait();
        if(confirmResult.isPresent() && confirmResult.get() == ButtonType.OK){
            AppointmentServiceDAO asDAO = new AppointmentServiceDAO();
            asDAO.deleteAppointmentService(appointmentId, serviceId);
            showAlert(Alert.AlertType.INFORMATION, "Deleted", "Appointment Service record deleted successfully!");
        }
    }

    @FXML
    private void handleAllAppointmentServices() {
        AppointmentServiceDAO asDAO = new AppointmentServiceDAO();
        List<AppointmentService> asList = asDAO.getAllAppointmentServices();
        if(asList == null || asList.isEmpty()){
            showAlert(Alert.AlertType.INFORMATION, "No Records", "No appointment services found.");
            tableView.getItems().clear();
        } else {
            configureAppointmentServiceTable();
            ObservableList<AppointmentService> data = FXCollections.observableArrayList(asList);
            tableView.setItems(FXCollections.observableArrayList(data));
        }
    }

    private void configureAppointmentServiceTable() {
        tableView.getColumns().clear();
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Appointment ID
        TableColumn<Object, Long> appointmentIdCol = new TableColumn<>("Appointment ID");
        appointmentIdCol.setCellValueFactory(cellData -> {
            Object rowValue = cellData.getValue();
            if (rowValue instanceof AppointmentService as) {
                return new ReadOnlyObjectWrapper<>(as.getAppointmentId());
            }
            return new ReadOnlyObjectWrapper<>(null);
        });

        // Service ID
        TableColumn<Object, Integer> serviceIdCol = new TableColumn<>("Service ID");
        serviceIdCol.setCellValueFactory(cellData -> {
            Object rowValue = cellData.getValue();
            if (rowValue instanceof AppointmentService as) {
                return new ReadOnlyObjectWrapper<>(as.getServiceId());
            }
            return new ReadOnlyObjectWrapper<>(null);
        });

        // Service Name (custom cell value factory)
        TableColumn<Object, String> serviceNameCol = new TableColumn<>("Service Name");
        serviceNameCol.setCellValueFactory(cellData -> {
            Object rowValue = cellData.getValue();
            if (rowValue instanceof AppointmentService as && as.getService() != null) {
                return new ReadOnlyStringWrapper(as.getService().getName());
            }
            return new ReadOnlyStringWrapper("");
        });

        // Item ID
        TableColumn<Object, Long> itemIdCol = new TableColumn<>("Item ID");
        itemIdCol.setCellValueFactory(cellData -> {
            Object rowValue = cellData.getValue();
            if (rowValue instanceof AppointmentService as) {
                return new ReadOnlyObjectWrapper<>(as.getItemId());
            }
            return new ReadOnlyObjectWrapper<>(null);
        });

        // Item Name (custom cell value factory)
        TableColumn<Object, String> itemNameCol = new TableColumn<>("Item Name");
        itemNameCol.setCellValueFactory(cellData -> {
            Object rowValue = cellData.getValue();
            if (rowValue instanceof AppointmentService as && as.getInventory() != null) {
                // Make sure Inventory has getName(), or adjust this if it's getItemName()
                return new ReadOnlyStringWrapper(as.getInventory().getName());
            }
            return new ReadOnlyStringWrapper("");
        });

        // Quantity Used
        TableColumn<Object, Integer> quantityCol = new TableColumn<>("Quantity Used");
        quantityCol.setCellValueFactory(cellData -> {
            Object rowValue = cellData.getValue();
            if (rowValue instanceof AppointmentService as) {
                return new ReadOnlyObjectWrapper<>(as.getQuantityUsed());
            }
            return new ReadOnlyObjectWrapper<>(0);
        });

        // Price
        TableColumn<Object, Double> priceCol = new TableColumn<>("Price");
        priceCol.setCellValueFactory(cellData -> {
            Object rowValue = cellData.getValue();
            if (rowValue instanceof AppointmentService as) {
                return new ReadOnlyObjectWrapper<>(as.getPrice());
            }
            return new ReadOnlyObjectWrapper<>(0.0);
        });

        // Add all columns to the table
        tableView.getColumns().addAll(
                appointmentIdCol,
                serviceIdCol,
                serviceNameCol,
                itemIdCol,
                itemNameCol,
                quantityCol,
                priceCol
        );
    }


    // ---------------------- SERVICE FUNCTIONALITIES ---------------------- //

    @FXML
    private void handleGetSpecificService() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Get Specific Service");
        dialog.setHeaderText("Enter Service ID");
        dialog.setContentText("Service ID:");
        dialog.getDialogPane().getStylesheets().add(getClass().getResource("/thenexus/demo2/alert.css").toExternalForm());
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent() && !result.get().trim().isEmpty()) {
            try {
                int serviceId = Integer.parseInt(result.get().trim());
                ServiceDAO serviceDAO = new ServiceDAO();
                Service service = serviceDAO.getByServiceId(serviceId);
                if (service != null) {
                    configureServiceTable();
                    ObservableList<Service> data = FXCollections.observableArrayList();
                    data.add(service);
                    tableView.setItems(FXCollections.observableArrayList(data));
                } else {
                    showAlert(Alert.AlertType.INFORMATION, "Not Found", "Service with ID " + serviceId + " not found.");
                }
            } catch (NumberFormatException e) {
                showAlert(Alert.AlertType.ERROR, "Invalid Input", "Service ID must be numeric.");
            }
        } else {
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Service ID is required.");
        }
    }

    @FXML
    private void handleAddService() {
        TextInputDialog nameDialog = new TextInputDialog();
        nameDialog.setTitle("Add New Service");
        nameDialog.setHeaderText("Enter Service Name");
        nameDialog.setContentText("Name:");
        Optional<String> nameResult = nameDialog.showAndWait();
        if (!nameResult.isPresent() || nameResult.get().trim().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Service name is required.");
            return;
        }
        String name = nameResult.get().trim();

        TextInputDialog descDialog = new TextInputDialog();
        descDialog.setTitle("Add New Service");
        descDialog.setHeaderText("Enter Service Description");
        descDialog.setContentText("Description:");
        Optional<String> descResult = descDialog.showAndWait();
        String description = descResult.orElse("").trim();

        ServiceDAO serviceDAO = new ServiceDAO();
        serviceDAO.insertService(name, description);
        showAlert(Alert.AlertType.INFORMATION, "Success", "Service added successfully!");
    }

    @FXML
    private void handleUpdateService() {
        TextInputDialog idDialog = new TextInputDialog();
        idDialog.setTitle("Update Service");
        idDialog.setHeaderText("Enter Service ID to update");
        idDialog.setContentText("Service ID:");
        Optional<String> idResult = idDialog.showAndWait();
        if (!idResult.isPresent() || idResult.get().trim().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Service ID is required.");
            return;
        }
        int serviceId;
        try {
            serviceId = Integer.parseInt(idResult.get().trim());
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Service ID must be numeric.");
            return;
        }
        ServiceDAO serviceDAO = new ServiceDAO();
        Service service = serviceDAO.getByServiceId(serviceId);
        if (service == null) {
            showAlert(Alert.AlertType.INFORMATION, "Not Found", "Service with ID " + serviceId + " not found.");
            return;
        }
        TextInputDialog nameDialog = new TextInputDialog(service.getName());
        nameDialog.setTitle("Update Service");
        nameDialog.setHeaderText("Enter new Service Name");
        nameDialog.setContentText("Name:");
        Optional<String> nameResult = nameDialog.showAndWait();
        if (!nameResult.isPresent() || nameResult.get().trim().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Service name is required.");
            return;
        }
        String name = nameResult.get().trim();

        TextInputDialog descDialog = new TextInputDialog(service.getDescription());
        descDialog.setTitle("Update Service");
        descDialog.setHeaderText("Enter new Service Description");
        descDialog.setContentText("Description:");
        Optional<String> descResult = descDialog.showAndWait();
        String description = descResult.orElse("").trim();

        serviceDAO.updateService(serviceId, name, description);
        showAlert(Alert.AlertType.INFORMATION, "Success", "Service updated successfully!");
    }

    @FXML
    private void handleDeleteService() {
        TextInputDialog idDialog = new TextInputDialog();
        idDialog.setTitle("Delete Service");
        idDialog.setHeaderText("Enter Service ID to delete");
        idDialog.setContentText("Service ID:");
        Optional<String> idResult = idDialog.showAndWait();
        if (!idResult.isPresent() || idResult.get().trim().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Service ID is required.");
            return;
        }
        int serviceId;
        try {
            serviceId = Integer.parseInt(idResult.get().trim());
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Service ID must be numeric.");
            return;
        }
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirm Delete");
        confirmAlert.setHeaderText(null);
        confirmAlert.setContentText("Are you sure you want to delete service with ID " + serviceId + "?");
        Optional<ButtonType> confirmResult = confirmAlert.showAndWait();
        if (confirmResult.isPresent() && confirmResult.get() == ButtonType.OK) {
            ServiceDAO serviceDAO = new ServiceDAO();
            serviceDAO.deleteService(serviceId);
            showAlert(Alert.AlertType.INFORMATION, "Deleted", "Service deleted successfully!");
        }
    }

    @FXML
    private void handleAllServices() {
        ServiceDAO serviceDAO = new ServiceDAO();
        List<Service> services = serviceDAO.getAllServices();
        if (services == null || services.isEmpty()) {
            showAlert(Alert.AlertType.INFORMATION, "No Services", "No services found.");
            tableView.getItems().clear();
        } else {
            configureServiceTable();
            ObservableList<Service> data = FXCollections.observableArrayList(services);
            tableView.setItems(FXCollections.observableArrayList(data));
        }
    }

    private void configureServiceTable() {
        tableView.getColumns().clear();
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Object, Integer> serviceIdCol = new TableColumn<>("Service ID");
        serviceIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Object, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Object, String> descCol = new TableColumn<>("Description");
        descCol.setCellValueFactory(new PropertyValueFactory<>("description"));

        tableView.getColumns().addAll(serviceIdCol, nameCol, descCol);
    }

    // ---------------------- INVENTORY FUNCTIONALITIES ---------------------- //

    @FXML
    private void handleGetSpecificInventory() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Get Specific Inventory Item");
        dialog.setHeaderText("Enter Inventory Item ID");
        dialog.setContentText("Item ID:");
        Optional<String> result = dialog.showAndWait();
        if(result.isPresent() && !result.get().trim().isEmpty()){
            try {
                Long itemId = Long.parseLong(result.get().trim());
                InventoryDAO inventoryDAO = new InventoryDAO();
                Inventory item = inventoryDAO.getByInventoryId(itemId);
                if(item != null){
                    configureInventoryTable();
                    ObservableList<Inventory> data = FXCollections.observableArrayList();
                    data.add(item);
                    tableView.setItems(FXCollections.observableArrayList(data));
                } else {
                    showAlert(Alert.AlertType.INFORMATION, "Not Found", "Inventory item with ID " + itemId + " not found.");
                }
            } catch(NumberFormatException e){
                showAlert(Alert.AlertType.ERROR, "Invalid Input", "Item ID must be numeric.");
            }
        } else {
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Item ID is required.");
        }
    }

    @FXML
    private void handleAddInventory() {
        TextInputDialog nameDialog = new TextInputDialog();
        nameDialog.setTitle("Add Inventory Item");
        nameDialog.setHeaderText("Enter Item Name");
        nameDialog.setContentText("Name:");
        Optional<String> nameResult = nameDialog.showAndWait();
        if(!nameResult.isPresent() || nameResult.get().trim().isEmpty()){
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Item name is required.");
            return;
        }
        String name = nameResult.get().trim();

        TextInputDialog quantityDialog = new TextInputDialog();
        quantityDialog.setTitle("Add Inventory Item");
        quantityDialog.setHeaderText("Enter Quantity");
        quantityDialog.setContentText("Quantity:");
        Optional<String> quantityResult = quantityDialog.showAndWait();
        if(!quantityResult.isPresent() || quantityResult.get().trim().isEmpty()){
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Quantity is required.");
            return;
        }
        Long quantity;
        try {
            quantity = Long.parseLong(quantityResult.get().trim());
        } catch(NumberFormatException e){
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Quantity must be numeric.");
            return;
        }

        TextInputDialog priceDialog = new TextInputDialog();
        priceDialog.setTitle("Add Inventory Item");
        priceDialog.setHeaderText("Enter Price");
        priceDialog.setContentText("Price:");
        Optional<String> priceResult = priceDialog.showAndWait();
        if(!priceResult.isPresent() || priceResult.get().trim().isEmpty()){
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Price is required.");
            return;
        }
        double price;
        try {
            price = Double.parseDouble(priceResult.get().trim());
        } catch(NumberFormatException e){
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Price must be numeric.");
            return;
        }

        InventoryDAO inventoryDAO = new InventoryDAO();
        inventoryDAO.insertInventory(name, quantity, price);
        showAlert(Alert.AlertType.INFORMATION, "Success", "Inventory item added successfully!");
    }

    @FXML
    private void handleUpdateInventory() {
        TextInputDialog idDialog = new TextInputDialog();
        idDialog.setTitle("Update Inventory Item");
        idDialog.setHeaderText("Enter Item ID to update");
        idDialog.setContentText("Item ID:");
        Optional<String> idResult = idDialog.showAndWait();
        if(!idResult.isPresent() || idResult.get().trim().isEmpty()){
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Item ID is required.");
            return;
        }
        Long itemId;
        try {
            itemId = Long.parseLong(idResult.get().trim());
        } catch(NumberFormatException e){
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Item ID must be numeric.");
            return;
        }
        InventoryDAO inventoryDAO = new InventoryDAO();
        Inventory item = inventoryDAO.getByInventoryId(itemId);
        if(item == null){
            showAlert(Alert.AlertType.INFORMATION, "Not Found", "Inventory item with ID " + itemId + " not found.");
            return;
        }
        TextInputDialog nameDialog = new TextInputDialog(item.getName());
        nameDialog.setTitle("Update Inventory Item");
        nameDialog.setHeaderText("Enter new Item Name");
        nameDialog.setContentText("Name:");
        Optional<String> nameResult = nameDialog.showAndWait();
        if(!nameResult.isPresent() || nameResult.get().trim().isEmpty()){
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Item name is required.");
            return;
        }
        String name = nameResult.get().trim();

        TextInputDialog quantityDialog = new TextInputDialog(item.getQuantity().toString());
        quantityDialog.setTitle("Update Inventory Item");
        quantityDialog.setHeaderText("Enter new Quantity");
        quantityDialog.setContentText("Quantity:");
        Optional<String> quantityResult = quantityDialog.showAndWait();
        if(!quantityResult.isPresent() || quantityResult.get().trim().isEmpty()){
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Quantity is required.");
            return;
        }
        Long quantity;
        try {
            quantity = Long.parseLong(quantityResult.get().trim());
        } catch(NumberFormatException e){
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Quantity must be numeric.");
            return;
        }

        TextInputDialog priceDialog = new TextInputDialog(String.valueOf(item.getPrice()));
        priceDialog.setTitle("Update Inventory Item");
        priceDialog.setHeaderText("Enter new Price");
        priceDialog.setContentText("Price:");
        Optional<String> priceResult = priceDialog.showAndWait();
        if(!priceResult.isPresent() || priceResult.get().trim().isEmpty()){
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Price is required.");
            return;
        }
        double price;
        try {
            price = Double.parseDouble(priceResult.get().trim());
        } catch(NumberFormatException e){
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Price must be numeric.");
            return;
        }

        inventoryDAO.updateInventory(itemId, name, quantity, price);
        showAlert(Alert.AlertType.INFORMATION, "Success", "Inventory item updated successfully!");
    }

    @FXML
    private void handleDeleteInventory() {
        TextInputDialog idDialog = new TextInputDialog();
        idDialog.setTitle("Delete Inventory Item");
        idDialog.setHeaderText("Enter Item ID to delete");
        idDialog.setContentText("Item ID:");
        Optional<String> idResult = idDialog.showAndWait();
        if(!idResult.isPresent() || idResult.get().trim().isEmpty()){
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Item ID is required.");
            return;
        }
        Long itemId;
        try {
            itemId = Long.parseLong(idResult.get().trim());
        } catch(NumberFormatException e){
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Item ID must be numeric.");
            return;
        }
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirm Delete");
        confirmAlert.setHeaderText(null);
        confirmAlert.setContentText("Are you sure you want to delete inventory item with ID " + itemId + "?");
        Optional<ButtonType> confirmResult = confirmAlert.showAndWait();
        if(confirmResult.isPresent() && confirmResult.get() == ButtonType.OK){
            InventoryDAO inventoryDAO = new InventoryDAO();
            inventoryDAO.deleteInventory(itemId);
            showAlert(Alert.AlertType.INFORMATION, "Deleted", "Inventory item deleted successfully!");
        }
    }

    @FXML
    private void handleAllInventory() {
        InventoryDAO inventoryDAO = new InventoryDAO();
        List<Inventory> items = inventoryDAO.getAllInventory();
        if(items == null || items.isEmpty()){
            showAlert(Alert.AlertType.INFORMATION, "No Items", "No inventory items found.");
            tableView.getItems().clear();
        } else {
            configureInventoryTable();
            ObservableList<Inventory> data = FXCollections.observableArrayList(items);
            tableView.setItems(FXCollections.observableArrayList(data));
        }
    }

    private void configureInventoryTable() {
        tableView.getColumns().clear();
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Object, Long> itemIdCol = new TableColumn<>("Item ID");
        itemIdCol.setCellValueFactory(new PropertyValueFactory<>("itemId"));

        TableColumn<Object, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Object, Long> quantityCol = new TableColumn<>("Quantity");
        quantityCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        TableColumn<Object, Double> priceCol = new TableColumn<>("Price");
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        TableColumn<Object, String> availabilityCol = new TableColumn<>("Availability");
        availabilityCol.setCellValueFactory(new PropertyValueFactory<>("availability"));

        tableView.getColumns().addAll(itemIdCol, nameCol, quantityCol, priceCol, availabilityCol);
    }

    // ---------------------- INVOICE FUNCTIONALITIES ---------------------- //

    @FXML
    private void handleGetSpecificInvoice() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Get Specific Invoice");
        dialog.setHeaderText("Enter Invoice ID");
        dialog.setContentText("Invoice ID:");
        Optional<String> result = dialog.showAndWait();
        if(result.isPresent() && !result.get().trim().isEmpty()){
            try {
                Long invoiceId = Long.parseLong(result.get().trim());
                InvoiceDAO invoiceDAO = new InvoiceDAO();
                Invoice invoice = invoiceDAO.getByInvoiceId(invoiceId);
                if(invoice != null){
                    configureInvoiceTable();
                    ObservableList<Invoice> data = FXCollections.observableArrayList();
                    data.add(invoice);
                    tableView.setItems(FXCollections.observableArrayList(data));
                } else {
                    showAlert(Alert.AlertType.INFORMATION, "Not Found", "Invoice with ID " + invoiceId + " not found.");
                }
            } catch(NumberFormatException e){
                showAlert(Alert.AlertType.ERROR, "Invalid Input", "Invoice ID must be numeric.");
            }
        } else {
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Invoice ID is required.");
        }
    }

    @FXML
    private void handleAddInvoice() {
        // Prompt for Generated By (User ID)
        TextInputDialog genDialog = new TextInputDialog();
        genDialog.setTitle("Add Invoice");
        genDialog.setHeaderText("Enter Generated By (User ID)");
        genDialog.setContentText("User ID:");
        Optional<String> genResult = genDialog.showAndWait();
        if(!genResult.isPresent() || genResult.get().trim().isEmpty()){
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "User ID is required.");
            return;
        }
        Long userId;
        try {
            userId = Long.parseLong(genResult.get().trim());
        } catch(NumberFormatException e){
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "User ID must be numeric.");
            return;
        }

        // Prompt for Appointment ID
        TextInputDialog apptDialog = new TextInputDialog();
        apptDialog.setTitle("Add Invoice");
        apptDialog.setHeaderText("Enter Appointment ID");
        apptDialog.setContentText("Appointment ID:");
        Optional<String> apptResult = apptDialog.showAndWait();
        if(!apptResult.isPresent() || apptResult.get().trim().isEmpty()){
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Appointment ID is required.");
            return;
        }
        Long appointmentId;
        try {
            appointmentId = Long.parseLong(apptResult.get().trim());
        } catch(NumberFormatException e){
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Appointment ID must be numeric.");
            return;
        }

        // Prompt for Paid Or Not
        TextInputDialog paidDialog = new TextInputDialog();
        paidDialog.setTitle("Add Invoice");
        paidDialog.setHeaderText("Enter Payment Status (e.g., Paid/Not Paid)");
        paidDialog.setContentText("Paid Or Not:");
        Optional<String> paidResult = paidDialog.showAndWait();
        if(!paidResult.isPresent() || paidResult.get().trim().isEmpty()){
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Payment status is required.");
            return;
        }
        String paidOrNot = paidResult.get().trim();

        InvoiceDAO invoiceDAO = new InvoiceDAO();
        invoiceDAO.insertInvoice(userId, appointmentId, paidOrNot);
        showAlert(Alert.AlertType.INFORMATION, "Success", "Invoice added successfully!");
    }

    @FXML
    private void handleUpdateInvoice() {
        TextInputDialog idDialog = new TextInputDialog();
        idDialog.setTitle("Update Invoice");
        idDialog.setHeaderText("Enter Invoice ID to update");
        idDialog.setContentText("Invoice ID:");
        Optional<String> idResult = idDialog.showAndWait();
        if(!idResult.isPresent() || idResult.get().trim().isEmpty()){
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Invoice ID is required.");
            return;
        }
        Long invoiceId;
        try {
            invoiceId = Long.parseLong(idResult.get().trim());
        } catch(NumberFormatException e){
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Invoice ID must be numeric.");
            return;
        }
        InvoiceDAO invoiceDAO = new InvoiceDAO();
        Invoice invoice = invoiceDAO.getByInvoiceId(invoiceId);
        if(invoice == null){
            showAlert(Alert.AlertType.INFORMATION, "Not Found", "Invoice with ID " + invoiceId + " not found.");
            return;
        }
        // Prompt for new total price
        TextInputDialog priceDialog = new TextInputDialog(String.valueOf(invoice.getTotalPrice()));
        priceDialog.setTitle("Update Invoice");
        priceDialog.setHeaderText("Enter new Total Price");
        priceDialog.setContentText("Total Price:");
        Optional<String> priceResult = priceDialog.showAndWait();
        if(!priceResult.isPresent() || priceResult.get().trim().isEmpty()){
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Total Price is required.");
            return;
        }
        double totalPrice;
        try {
            totalPrice = Double.parseDouble(priceResult.get().trim());
        } catch(NumberFormatException e){
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Total Price must be numeric.");
            return;
        }

        // Prompt for new Generated By (User ID)
        TextInputDialog genDialog = new TextInputDialog(String.valueOf(invoice.getGeneratedBy().getId()));
        genDialog.setTitle("Update Invoice");
        genDialog.setHeaderText("Enter new Generated By (User ID)");
        genDialog.setContentText("User ID:");
        Optional<String> genResult = genDialog.showAndWait();
        if(!genResult.isPresent() || genResult.get().trim().isEmpty()){
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "User ID is required.");
            return;
        }
        Long userId;
        try {
            userId = Long.parseLong(genResult.get().trim());
        } catch(NumberFormatException e){
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "User ID must be numeric.");
            return;
        }

        // Prompt for new Appointment ID
        TextInputDialog apptDialog = new TextInputDialog(String.valueOf(invoice.getAppointment().getAppointmentId()));
        apptDialog.setTitle("Update Invoice");
        apptDialog.setHeaderText("Enter new Appointment ID");
        apptDialog.setContentText("Appointment ID:");
        Optional<String> apptResult = apptDialog.showAndWait();
        if(!apptResult.isPresent() || apptResult.get().trim().isEmpty()){
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Appointment ID is required.");
            return;
        }
        Long appointmentId;
        try {
            appointmentId = Long.parseLong(apptResult.get().trim());
        } catch(NumberFormatException e){
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Appointment ID must be numeric.");
            return;
        }

        // Prompt for new Payment Status
        TextInputDialog paidDialog = new TextInputDialog(invoice.getPaidOrNot());
        paidDialog.setTitle("Update Invoice");
        paidDialog.setHeaderText("Enter new Payment Status (e.g., Paid/Not Paid)");
        paidDialog.setContentText("Paid Or Not:");
        Optional<String> paidResult = paidDialog.showAndWait();
        if(!paidResult.isPresent() || paidResult.get().trim().isEmpty()){
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Payment status is required.");
            return;
        }
        String paidOrNot = paidResult.get().trim();

        invoiceDAO.updateInvoice(invoiceId, totalPrice, userId, appointmentId, paidOrNot);
        showAlert(Alert.AlertType.INFORMATION, "Success", "Invoice updated successfully!");
    }

    @FXML
    private void handleDeleteInvoice() {
        TextInputDialog idDialog = new TextInputDialog();
        idDialog.setTitle("Delete Invoice");
        idDialog.setHeaderText("Enter Invoice ID to delete");
        idDialog.setContentText("Invoice ID:");
        Optional<String> idResult = idDialog.showAndWait();
        if(!idResult.isPresent() || idResult.get().trim().isEmpty()){
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Invoice ID is required.");
            return;
        }
        Long invoiceId;
        try {
            invoiceId = Long.parseLong(idResult.get().trim());
        } catch(NumberFormatException e){
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Invoice ID must be numeric.");
            return;
        }
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirm Delete");
        confirmAlert.setHeaderText(null);
        confirmAlert.setContentText("Are you sure you want to delete invoice with ID " + invoiceId + "?");
        Optional<ButtonType> confirmResult = confirmAlert.showAndWait();
        if(confirmResult.isPresent() && confirmResult.get() == ButtonType.OK){
            InvoiceDAO invoiceDAO = new InvoiceDAO();
            invoiceDAO.deleteInvoice(invoiceId);
            showAlert(Alert.AlertType.INFORMATION, "Deleted", "Invoice deleted successfully!");
        }
    }

    @FXML
    private void handleAllInvoices() {
        InvoiceDAO invoiceDAO = new InvoiceDAO();
        List<Invoice> invoices = invoiceDAO.getAllInvoices();
        if(invoices == null || invoices.isEmpty()){
            showAlert(Alert.AlertType.INFORMATION, "No Invoices", "No invoices found.");
            tableView.getItems().clear();
        } else {
            configureInvoiceTable();
            ObservableList<Invoice> data = FXCollections.observableArrayList(invoices);
            tableView.setItems(FXCollections.observableArrayList(data));
        }
    }

    private void configureInvoiceTable() {
        tableView.getColumns().clear();
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Object, Long> invoiceIdCol = new TableColumn<>("Invoice ID");
        invoiceIdCol.setCellValueFactory(new PropertyValueFactory<>("invoiceId"));

        TableColumn<Object, Double> totalPriceCol = new TableColumn<>("Total Price");
        totalPriceCol.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));

        TableColumn<Object, Object> dateCol = new TableColumn<>("Invoice Date");
        dateCol.setCellValueFactory(new PropertyValueFactory<>("invoiceDate"));

        TableColumn<Object, Object> generatedByCol = new TableColumn<>("Generated By");
        generatedByCol.setCellValueFactory(new PropertyValueFactory<>("generatedBy"));

        TableColumn<Object, Object> appointmentCol = new TableColumn<>("Appointment");
        appointmentCol.setCellValueFactory(new PropertyValueFactory<>("appointment"));

        TableColumn<Object, String> paidCol = new TableColumn<>("Paid/Not");
        paidCol.setCellValueFactory(new PropertyValueFactory<>("paidOrNot"));

        tableView.getColumns().addAll(invoiceIdCol, totalPriceCol, dateCol, generatedByCol, appointmentCol, paidCol);
    }

    // ---------------------- USER FUNCTIONALITIES ---------------------- //

    @FXML
    private void handleGetSpecificUser() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Get Specific User");
        dialog.setHeaderText("Enter User ID");
        dialog.setContentText("User ID:");
        dialog.getDialogPane().getStylesheets().add(getClass().getResource("/thenexus/demo2/alert.css").toExternalForm());

        Optional<String> result = dialog.showAndWait();
        if(result.isPresent() && !result.get().trim().isEmpty()){
            try {
                Long userId = Long.parseLong(result.get().trim());
                UserDAO userDAO = new UserDAO();
                User user = userDAO.getByUserId(userId);
                if(user != null){
                    configureUserTable();
                    ObservableList<User> data = FXCollections.observableArrayList();
                    data.add(user);
                    tableView.setItems(FXCollections.observableArrayList(data));
                } else {
                    showAlert(Alert.AlertType.INFORMATION, "Not Found", "User with ID " + userId + " not found.");
                }
            } catch(NumberFormatException e){
                showAlert(Alert.AlertType.ERROR, "Invalid Input", "User ID must be numeric.");
            }
        } else {
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "User ID is required.");
        }
    }

    @FXML
    private void handleAddUser() {
        // Prompt for Username
        TextInputDialog usernameDialog = new TextInputDialog();
        usernameDialog.setTitle("Add New User");
        usernameDialog.setHeaderText("Enter Username");
        usernameDialog.setContentText("Username:");
        usernameDialog.getDialogPane().getStylesheets().add(getClass().getResource("/thenexus/demo2/style.css").toExternalForm());
        Optional<String> usernameResult = usernameDialog.showAndWait();
        if(!usernameResult.isPresent() || usernameResult.get().trim().isEmpty()){
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Username is required.");
            return;
        }
        String username = usernameResult.get().trim();

        // Prompt for Password
        TextInputDialog passwordDialog = new TextInputDialog();
        passwordDialog.setTitle("Add New User");
        passwordDialog.setHeaderText("Enter Password");
        passwordDialog.setContentText("Password:");
        passwordDialog.getDialogPane().getStylesheets().add(getClass().getResource("/thenexus/demo2/style.css").toExternalForm());
        Optional<String> passwordResult = passwordDialog.showAndWait();
        if(!passwordResult.isPresent() || passwordResult.get().trim().isEmpty()){
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Password is required.");
            return;
        }
        String password = passwordResult.get().trim();

        // Prompt for Role ID (assuming you use an integer for roleId)
        TextInputDialog roleDialog = new TextInputDialog();
        roleDialog.setTitle("Add New User");
        roleDialog.setHeaderText("Enter Role ID");
        roleDialog.setContentText("Role ID:");
        roleDialog.getDialogPane().getStylesheets().add(getClass().getResource("/thenexus/demo2/style.css").toExternalForm());
        Optional<String> roleResult = roleDialog.showAndWait();
        if(!roleResult.isPresent() || roleResult.get().trim().isEmpty()){
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Role ID is required.");
            return;
        }
        int roleId;
        try {
            roleId = Integer.parseInt(roleResult.get().trim());
        } catch(NumberFormatException e){
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Role ID must be numeric.");
            return;
        }

        // Prompt for Active (e.g., "Y" or "N")
        TextInputDialog activeDialog = new TextInputDialog();
        activeDialog.setTitle("Add New User");
        activeDialog.setHeaderText("Enter Active Status");
        activeDialog.setContentText("Active (Y/N):");
        activeDialog.getDialogPane().getStylesheets().add(getClass().getResource("/thenexus/demo2/style.css").toExternalForm());
        Optional<String> activeResult = activeDialog.showAndWait();
        if(!activeResult.isPresent() || activeResult.get().trim().isEmpty()){
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Active status is required.");
            return;
        }
        String active = activeResult.get().trim();

        UserDAO userDAO = new UserDAO();
        userDAO.insertUser(username, password, roleId, active);
        showAlert(Alert.AlertType.INFORMATION, "Success", "User added successfully!");
    }

    @FXML
    private void handleUpdateUser() {
        // Prompt for User ID
        TextInputDialog idDialog = new TextInputDialog();
        idDialog.setTitle("Update User");
        idDialog.setHeaderText("Enter User ID to update");
        idDialog.setContentText("User ID:");
        idDialog.getDialogPane().getStylesheets().add(getClass().getResource("/thenexus/demo2/alert.css").toExternalForm());
        Optional<String> idResult = idDialog.showAndWait();
        if(!idResult.isPresent() || idResult.get().trim().isEmpty()){
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "User ID is required.");
            return;
        }
        Long userId;
        try {
            userId = Long.parseLong(idResult.get().trim());
        } catch(NumberFormatException e){
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "User ID must be numeric.");
            return;
        }

        UserDAO userDAO = new UserDAO();
        User user = userDAO.getByUserId(userId);
        if(user == null){
            showAlert(Alert.AlertType.INFORMATION, "Not Found", "User with ID " + userId + " not found.");
            return;
        }

        // Prompt for new Username
        TextInputDialog usernameDialog = new TextInputDialog(user.getUsername());
        usernameDialog.setTitle("Update User");
        usernameDialog.setHeaderText("Enter new Username");
        usernameDialog.setContentText("Username:");
        Optional<String> usernameResult = usernameDialog.showAndWait();
        if(!usernameResult.isPresent() || usernameResult.get().trim().isEmpty()){
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Username is required.");
            return;
        }
        String username = usernameResult.get().trim();

        // Prompt for new Password
        TextInputDialog passwordDialog = new TextInputDialog(user.getPassword());
        passwordDialog.setTitle("Update User");
        passwordDialog.setHeaderText("Enter new Password");
        passwordDialog.setContentText("Password:");
        Optional<String> passwordResult = passwordDialog.showAndWait();
        if(!passwordResult.isPresent() || passwordResult.get().trim().isEmpty()){
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Password is required.");
            return;
        }
        String password = passwordResult.get().trim();

        // Prompt for new Role ID
        TextInputDialog roleDialog = new TextInputDialog(String.valueOf(user.getRole().getId()));
        roleDialog.setTitle("Update User");
        roleDialog.setHeaderText("Enter new Role ID");
        roleDialog.setContentText("Role ID:");
        Optional<String> roleResult = roleDialog.showAndWait();
        if(!roleResult.isPresent() || roleResult.get().trim().isEmpty()){
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Role ID is required.");
            return;
        }
        int roleId;
        try {
            roleId = Integer.parseInt(roleResult.get().trim());
        } catch(NumberFormatException e){
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Role ID must be numeric.");
            return;
        }

        // Prompt for new Active status
        TextInputDialog activeDialog = new TextInputDialog(user.getActive());
        activeDialog.setTitle("Update User");
        activeDialog.setHeaderText("Enter new Active Status");
        activeDialog.setContentText("Active (Y/N):");
        Optional<String> activeResult = activeDialog.showAndWait();
        if(!activeResult.isPresent() || activeResult.get().trim().isEmpty()){
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Active status is required.");
            return;
        }
        String active = activeResult.get().trim();

        userDAO.updateUser(userId, username, password, roleId, active);
        showAlert(Alert.AlertType.INFORMATION, "Success", "User updated successfully!");
    }

    @FXML
    private void handleAllUsers() {
        UserDAO userDAO = new UserDAO();
        List<User> users = userDAO.getAllUsers();
        if(users == null || users.isEmpty()){
            showAlert(Alert.AlertType.INFORMATION, "No Users", "No users found.");
            tableView.getItems().clear();
        } else {
            configureUserTable();
            ObservableList<User> data = FXCollections.observableArrayList(users);
            tableView.setItems(FXCollections.observableArrayList(data));
        }
    }

    // Configure the TableView columns for displaying Users
    private void configureUserTable() {
        tableView.getColumns().clear();
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Object, Integer> userIdCol = new TableColumn<>("User ID");
        userIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Object, String> usernameCol = new TableColumn<>("Username");
        usernameCol.setCellValueFactory(new PropertyValueFactory<>("username"));

        // Optionally, you may not want to display passwords for security reasons
        // TableColumn<Object, String> passwordCol = new TableColumn<>("Password");
        // passwordCol.setCellValueFactory(new PropertyValueFactory<>("password"));

        TableColumn<Object, String> activeCol = new TableColumn<>("Active");
        activeCol.setCellValueFactory(new PropertyValueFactory<>("active"));

        TableColumn<Object, Object> roleCol = new TableColumn<>("Role");
        roleCol.setCellValueFactory(new PropertyValueFactory<>("role"));

        tableView.getColumns().addAll(userIdCol, usernameCol, activeCol, roleCol);
    }


    // ---------------------- LOGOUT FUNCTIONALITY ---------------------- //

    @FXML
    private void handleLogOut() {
        System.out.println("Logout");
        try {
            FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("/thenexus/demo2/hello-view.fxml"));
            AnchorPane loginPane = loader.load();
            Stage currentStage = (Stage) rootPane.getScene().getWindow();
            currentStage.setScene(new Scene(loginPane));
            currentStage.setMinWidth(1200);
            currentStage.setMinHeight(600);
            currentStage.setTitle("Login");
            currentStage.show();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error loading login screen!");
        }
    }

    // ---------------------- LOW STOCK NOTIFICATION FUNCTIONALITIES ---------------------- //

    @FXML
    private void handleAllLowStockNotifications() {
        LowStockNotificationDAO notificationDAO = new LowStockNotificationDAO();
        List<LowStockNotification> notifications = notificationDAO.getAllNotifications();
        if (notifications == null || notifications.isEmpty()) {
            showAlert(Alert.AlertType.INFORMATION, "No Notifications", "No low stock notifications found.");
            tableView.getItems().clear();
        } else {
            configureLowStockNotificationTable();
            ObservableList<LowStockNotification> data = FXCollections.observableArrayList(notifications);
            tableView.setItems(FXCollections.observableArrayList(data));
        }
    }

    private void configureLowStockNotificationTable() {
        tableView.getColumns().clear();
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Object, Integer> notificationIdCol = new TableColumn<>("Notification ID");
        notificationIdCol.setCellValueFactory(new PropertyValueFactory<>("notificationId"));

        TableColumn<Object, Integer> itemIdCol = new TableColumn<>("Item ID");
        itemIdCol.setCellValueFactory(new PropertyValueFactory<>("itemId"));

        TableColumn<Object, Integer> quantityCol = new TableColumn<>("Quantity");
        quantityCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        TableColumn<Object, String> dateCol = new TableColumn<>("Notification Date");
        // If you need a custom date format, you can set a cell factory to format the Date as a String.
        dateCol.setCellValueFactory(new PropertyValueFactory<>("notificationDate"));

        tableView.getColumns().addAll(notificationIdCol, itemIdCol, quantityCol, dateCol);
    }

    @FXML
    private void handleGenerateRevenueReport() {
        try {
            // 1) Fetch all invoices
            InvoiceDAO invoiceDAO = new InvoiceDAO();
            List<Invoice> invoiceList = invoiceDAO.getAllInvoices();

            if (invoiceList == null || invoiceList.isEmpty()) {
                showAlert(Alert.AlertType.INFORMATION, "No Invoices", "No invoices found to generate a revenue report.");
                return;
            }

            // 2) Define PDF destination in the user's Downloads folder
            String dest = System.getProperty("user.home") + File.separator + "Downloads" +
                    File.separator + "RevenueReport.pdf";

            // 3) Initialize PDF writer and document with A4 size and margins
            PdfWriter writer = new PdfWriter(dest);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf, PageSize.A4);
            document.setMargins(20, 20, 20, 20);

            // 4) Header: Logo and Company Info
            try {
                ImageData data = ImageDataFactory.create("1.jpg");
                Image logo = new Image(data).scaleToFit(80, 40);
                logo.setMarginBottom(5);
                document.add(logo);
            } catch (Exception e) {
                System.err.println("Logo not found, proceeding without it.");
            }

            Paragraph companyName = new Paragraph("Zodiac")
                    .setFontSize(28)
                    .setBold()
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFontColor(ColorConstants.RED)
                    .setMarginBottom(5);
            document.add(companyName);

            Paragraph companyDetails = new Paragraph("123 Main Street, City, State, ZIP | Phone: (123) 456-7890 | Email: info@zodiac.com")
                    .setFontSize(10)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFontColor(ColorConstants.GRAY)
                    .setMarginBottom(15);
            document.add(companyDetails);

            // 5) Main heading: Revenue Report
            Paragraph mainHeading = new Paragraph("Revenue Report")
                    .setFontSize(24)
                    .setBold()
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginBottom(10);
            document.add(mainHeading);

            // Add some spacing
            document.add(new Paragraph("\n"));

            // 6) Create a table with defined column widths (using percentages for consistency)
            Table table = new Table(UnitValue.createPercentArray(new float[]{20, 20, 20, 20, 20}));
            table.setWidth(UnitValue.createPercentValue(100));

            // 7) Build a styled header row with dark gray background and white bold text
            Cell h1 = new Cell().add(new Paragraph("Invoice ID").setBold().setFontColor(ColorConstants.WHITE))
                    .setBackgroundColor(ColorConstants.DARK_GRAY)
                    .setBorder(new SolidBorder(ColorConstants.BLACK, 1))
                    .setPadding(5);
            Cell h2 = new Cell().add(new Paragraph("Date").setBold().setFontColor(ColorConstants.WHITE))
                    .setBackgroundColor(ColorConstants.DARK_GRAY)
                    .setBorder(new SolidBorder(ColorConstants.BLACK, 1))
                    .setPadding(5);
            Cell h3 = new Cell().add(new Paragraph("Total Price").setBold().setFontColor(ColorConstants.WHITE))
                    .setBackgroundColor(ColorConstants.DARK_GRAY)
                    .setBorder(new SolidBorder(ColorConstants.BLACK, 1))
                    .setPadding(5);
            Cell h4 = new Cell().add(new Paragraph("Paid/Not").setBold().setFontColor(ColorConstants.WHITE))
                    .setBackgroundColor(ColorConstants.DARK_GRAY)
                    .setBorder(new SolidBorder(ColorConstants.BLACK, 1))
                    .setPadding(5);
            Cell h5 = new Cell().add(new Paragraph("Customer").setBold().setFontColor(ColorConstants.WHITE))
                    .setBackgroundColor(ColorConstants.DARK_GRAY)
                    .setBorder(new SolidBorder(ColorConstants.BLACK, 1))
                    .setPadding(5);

            table.addHeaderCell(h1);
            table.addHeaderCell(h2);
            table.addHeaderCell(h3);
            table.addHeaderCell(h4);
            table.addHeaderCell(h5);

            // 8) Track total revenue
            double totalRevenue = 0.0;

            // 9) Populate table rows with invoice data
            for (Invoice invoice : invoiceList) {

                // Invoice ID cell
                table.addCell(new Cell()
                        .add(new Paragraph(String.valueOf(invoice.getInvoiceId())))
                        .setBorder(new SolidBorder(ColorConstants.BLACK, 1))
                        .setPadding(5));

                // Invoice Date cell (formatting can be applied if needed)
                String dateStr = invoice.getInvoiceDate().toString();
                table.addCell(new Cell()
                        .add(new Paragraph(dateStr))
                        .setBorder(new SolidBorder(ColorConstants.BLACK, 1))
                        .setPadding(5));

                // Total Price cell with formatting to two decimals
                table.addCell(new Cell()
                        .add(new Paragraph(String.format("$%.2f", invoice.getTotalPrice())))
                        .setBorder(new SolidBorder(ColorConstants.BLACK, 1))
                        .setPadding(5));

                // Paid/Not cell, defaulting to "N/A" if null
                String paidStatus = (invoice.getPaidOrNot() != null) ? invoice.getPaidOrNot() : "N/A";
                table.addCell(new Cell()
                        .add(new Paragraph(paidStatus))
                        .setBorder(new SolidBorder(ColorConstants.BLACK, 1))
                        .setPadding(5));

                // Customer cell, with a safe check for null values
                String customerName = "";
                if (invoice.getAppointment() != null && invoice.getAppointment().getCustomer() != null) {
                    customerName = invoice.getAppointment().getCustomer().getName();
                }
                table.addCell(new Cell()
                        .add(new Paragraph(customerName))
                        .setBorder(new SolidBorder(ColorConstants.BLACK, 1))
                        .setPadding(5));

                // Accumulate the total revenue from each invoice
                totalRevenue += invoice.getTotalPrice();
            }

            // 10) Add the table to the document
            document.add(table);

            // 11) Add a summary section for total revenue
            Table summaryTable = new Table(UnitValue.createPercentArray(new float[]{80, 20}));
            summaryTable.setWidth(UnitValue.createPercentValue(50));
            summaryTable.setHorizontalAlignment(com.itextpdf.layout.properties.HorizontalAlignment.RIGHT);

            summaryTable.addCell(new Cell().add(new Paragraph("Total Revenue").setBold()));
            summaryTable.addCell(new Cell().add(new Paragraph(String.format("$%.2f", totalRevenue)))
                    .setBold());
            document.add(new Paragraph("\n"));
            document.add(summaryTable);

            // 12) (Optional) Footer notes
            Paragraph notes = new Paragraph("Revenue report generated by Zodiac.")
                    .setFontSize(10)
                    .setFontColor(ColorConstants.GRAY)
                    .setTextAlignment(TextAlignment.LEFT);
            document.add(notes);

            // 13) Close the document
            document.close();

            // 14) Notify success and open the PDF if the Desktop API is supported
            showAlert(Alert.AlertType.INFORMATION, "PDF Generated", "Revenue report PDF saved to: " + dest);
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().open(new File(dest));
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to generate revenue report: " + e.getMessage());
        }
    }




    @FXML
    private void handleGeneratePDFInvoice(ActionEvent event) {
        // Prompt for Appointment ID
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Generate PDF Invoice");
        dialog.setHeaderText("Enter Appointment ID for Invoice");
        dialog.setContentText("Appointment ID:");
        Optional<String> result = dialog.showAndWait();
        if (!result.isPresent() || result.get().trim().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Appointment ID is required.");
            return;
        }

        Long appointmentId;
        try {
            appointmentId = Long.parseLong(result.get().trim());
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Appointment ID must be numeric.");
            return;
        }

        // Retrieve invoice details
        InvoiceDAO invoiceDAO = new InvoiceDAO();
        Invoice invoice = invoiceDAO.getLatestInvoiceByAppointmentId(appointmentId);
        if (invoice == null) {
            showAlert(Alert.AlertType.INFORMATION, "Not Found", "No invoice found for Appointment ID " + appointmentId);
            return;
        }

        // Define PDF destination in the Downloads folder
        String dest = System.getProperty("user.home") + File.separator + "Downloads" +
                File.separator + "invoice_appointment_" + appointmentId + ".pdf";

        try {
            // Initialize PDF
            PdfWriter writer = new PdfWriter(dest);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf, PageSize.A4);
            document.setMargins(20, 20, 20, 20);

            // Header: Logo and Company Info
            try {
                ImageData data = ImageDataFactory.create("1.jpg");
                Image logo = new Image(data).scaleToFit(80, 40);
                logo.setMarginBottom(5);
                document.add(logo);
            } catch (Exception e) {
                System.err.println("Logo not found, proceeding without it.");
            }

            Paragraph companyName = new Paragraph("Zodiac")
                    .setFontSize(28)
                    .setBold()
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFontColor(ColorConstants.RED)
                    .setMarginBottom(5);
            document.add(companyName);

            Paragraph companyDetails = new Paragraph("123 Main Street, City, State, ZIP | Phone: (123) 456-7890 | Email: info@zodiac.com")
                    .setFontSize(10)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFontColor(ColorConstants.GRAY)
                    .setMarginBottom(15);
            document.add(companyDetails);

            // Details Table for Appointment and Customer Information
            Table detailsTable = new Table(UnitValue.createPercentArray(new float[]{50, 50}));
            detailsTable.setWidth(UnitValue.createPercentValue(100));

            // First row with top border
            Cell billToCell = new Cell().add(new Paragraph("Bill To:").setBold().setFontColor(ColorConstants.RED))
                    .setPadding(5)
                    .setBorderTop(new SolidBorder(ColorConstants.RED, 2));
            detailsTable.addCell(billToCell);

            Cell appointmentDetailsCell = new Cell().add(new Paragraph("Appointment Details:").setBold().setFontColor(ColorConstants.RED))
                    .setPadding(5)
                    .setBorderTop(new SolidBorder(ColorConstants.RED, 2));
            detailsTable.addCell(appointmentDetailsCell);

            // Subsequent rows
            detailsTable.addCell(new Cell().add(new Paragraph(invoice.getAppointment().getCustomer().getName())));
            detailsTable.addCell(new Cell().add(new Paragraph("ID: " + invoice.getAppointment().getAppointmentId())));

            detailsTable.addCell(new Cell().add(new Paragraph(invoice.getAppointment().getCustomer().getEmail())));
            detailsTable.addCell(new Cell().add(new Paragraph("Date: " + invoice.getAppointment().getAppointmentDate().toString())));

            detailsTable.addCell(new Cell().add(new Paragraph("Contact: " + invoice.getAppointment().getCustomer().getContact())));
            detailsTable.addCell(new Cell().add(new Paragraph("Status: " + invoice.getAppointment().getStatus())));

            document.add(detailsTable);

            // Spacing
            document.add(new Paragraph("\n"));

            // Items Table (Using a single line item here for demonstration)
            // If you don't require an itemized breakdown, this table can be modified or removed.
            Table itemsTable = new Table(UnitValue.createPercentArray(new float[]{40, 20, 20, 20}));
            itemsTable.setWidth(UnitValue.createPercentValue(100));
            itemsTable.setBackgroundColor(ColorConstants.LIGHT_GRAY);

            itemsTable.addHeaderCell(new Cell().add(new Paragraph("Description").setBold()).setPadding(5));
            itemsTable.addHeaderCell(new Cell().add(new Paragraph("Quantity").setBold()).setPadding(5));
            itemsTable.addHeaderCell(new Cell().add(new Paragraph("Unit Price").setBold()).setPadding(5));
            itemsTable.addHeaderCell(new Cell().add(new Paragraph("Total").setBold()).setPadding(5));

            // Here we're using a placeholder line item where the unit price and total are the same,
            // since the entire invoice amount is stored in invoice.getTotalPrice().
            itemsTable.addCell(new Cell().add(new Paragraph("Service Charges")).setPadding(5));
            itemsTable.addCell(new Cell().add(new Paragraph("1")).setPadding(5));
            itemsTable.addCell(new Cell().add(new Paragraph(String.format("$%.2f", invoice.getTotalPrice()))).setPadding(5));
            itemsTable.addCell(new Cell().add(new Paragraph(String.format("$%.2f", invoice.getTotalPrice()))).setPadding(5));

            document.add(itemsTable);

            // Spacing
            document.add(new Paragraph("\n"));

            // Summary Section using the dynamic invoice total
            double invoiceTotal = invoice.getTotalPrice();
            Table summaryTable = new Table(UnitValue.createPercentArray(new float[]{80, 20}));
            summaryTable.setWidth(UnitValue.createPercentValue(50));
            summaryTable.setHorizontalAlignment(com.itextpdf.layout.properties.HorizontalAlignment.RIGHT);

            summaryTable.addCell(new Cell().add(new Paragraph("Total").setBold().setFontColor(ColorConstants.RED))
                    .setBackgroundColor(ColorConstants.LIGHT_GRAY));
            summaryTable.addCell(new Cell().add(new Paragraph(String.format("$%.2f", invoiceTotal)).setFontColor(ColorConstants.RED))
                    .setBackgroundColor(ColorConstants.LIGHT_GRAY));

            document.add(summaryTable);

            // Spacing
            document.add(new Paragraph("\n"));

            // Footer: Payment Terms and Notes
            Paragraph paymentTerms = new Paragraph("Payment Terms: Due within 30 days")
                    .setFontSize(10)
                    .setFontColor(ColorConstants.GRAY)
                    .setTextAlignment(TextAlignment.LEFT);
            document.add(paymentTerms);

            Paragraph notes = new Paragraph("Thank you for your business! Contact us at info@zodiac.com for any questions.")
                    .setFontSize(10)
                    .setFontColor(ColorConstants.GRAY)
                    .setTextAlignment(TextAlignment.LEFT);
            document.add(notes);

            // Close document
            document.close();

            // Notify success and open the PDF if Desktop support is available
            showAlert(Alert.AlertType.INFORMATION, "PDF Generated",  dest);
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().open(new File(dest));
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to generate PDF: " + e.getMessage());
        }
    }



    /**
     * Helper method to show an alert dialog with applied CSS.
     */
    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.getDialogPane().getStylesheets().add(getClass().getResource("/thenexus/demo2/alert.css").toExternalForm());
        alert.showAndWait();
    }





    // ---------------------- DYNAMIC TABLE CONFIGURATION ---------------------- //

    private void configureCustomerTable() {
        tableView.getColumns().clear();
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Object, Long> customerIdCol = new TableColumn<>("Customer ID");
        customerIdCol.setCellValueFactory(new PropertyValueFactory<>("customerId"));

        TableColumn<Object, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Object, String> emailCol = new TableColumn<>("Email");
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));

        TableColumn<Object, Long> contactCol = new TableColumn<>("Contact");
        contactCol.setCellValueFactory(new PropertyValueFactory<>("contact"));

        TableColumn<Object, String> vinCol = new TableColumn<>("VIN");
        vinCol.setCellValueFactory(cellData -> {
            Object row = cellData.getValue();
            if (row instanceof Customer) {
                Customer customer = (Customer) row;
                if (customer.getVehicle() != null && customer.getVehicle().getVin() != null) {
                    return new SimpleStringProperty(customer.getVehicle().getVin().toString());
                }
            }
            return new SimpleStringProperty("N/A");
        });

        TableColumn<Object, String> addressCol = new TableColumn<>("Address");
        addressCol.setCellValueFactory(new PropertyValueFactory<>("address"));

        tableView.getColumns().addAll(customerIdCol, nameCol, emailCol, contactCol,vinCol, addressCol);
    }

    private void configureVehicleTable() {
        tableView.getColumns().clear();
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Object, Long> vinCol = new TableColumn<>("VIN");
        vinCol.setCellValueFactory(new PropertyValueFactory<>("vin"));

        TableColumn<Object, String> modelCol = new TableColumn<>("Model");
        modelCol.setCellValueFactory(new PropertyValueFactory<>("model"));

        TableColumn<Object, Integer> yearCol = new TableColumn<>("Year");
        yearCol.setCellValueFactory(new PropertyValueFactory<>("year"));

        TableColumn<Object, String> licensePlateCol = new TableColumn<>("License Plate");
        licensePlateCol.setCellValueFactory(new PropertyValueFactory<>("licensePlate"));

        tableView.getColumns().addAll(vinCol, modelCol, yearCol, licensePlateCol);
    }
}




