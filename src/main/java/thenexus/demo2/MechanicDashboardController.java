package thenexus.demo2;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import thenexus.demo2.dao.AppointmentDAO;
import thenexus.demo2.dao.AppointmentServiceDAO;
import thenexus.demo2.dao.InventoryDAO;
import thenexus.demo2.model.Appointment;
import thenexus.demo2.model.AppointmentService;
import thenexus.demo2.model.Inventory;
import thenexus.demo2.model.User;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class MechanicDashboardController {

    @FXML
    private AnchorPane rootPane;

    @FXML
    private TableView<Object> tableView;

    private int currentMechanicId;

    @FXML
    public void initialize() {
        // Do not automatically load appointments on login.
    }

    public void setCurrentMechanicId(int id) {
        this.currentMechanicId = id;
    }

    // -------------------- APPOINTMENT FUNCTIONALITIES --------------------

    @FXML
    private void handleGetAppointment() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Get Appointment");
        dialog.setHeaderText("Enter Appointment ID");
        dialog.setContentText("Appointment ID:");
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent() && !result.get().trim().isEmpty()) {
            try {
                Long appointmentId = Long.parseLong(result.get().trim());
                AppointmentDAO appointmentDAO = new AppointmentDAO();
                Appointment appointment = appointmentDAO.getByAppointmentId(appointmentId);
                if (appointment != null) {
                    User assignedMechanic = appointment.getAssignedMechanic();
                    if (assignedMechanic != null && assignedMechanic.getId() == currentMechanicId) {
                        configureAppointmentTable();
                        ObservableList<Object> data = FXCollections.observableArrayList();
                        data.add(appointment);
                        tableView.setItems(data);
                    } else {
                        showAlert(Alert.AlertType.INFORMATION, "Not Authorized", "This appointment is not assigned to you.");
                    }
                } else {
                    showAlert(Alert.AlertType.INFORMATION, "Not Found", "Appointment with ID " + appointmentId + " not found.");
                }
            } catch (NumberFormatException e) {
                showAlert(Alert.AlertType.ERROR, "Invalid Input", "Appointment ID must be numeric.");
            }
        } else {
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Appointment ID is required.");
        }
    }

    @FXML
    private void handleAllAppointments() {
        AppointmentDAO appointmentDAO = new AppointmentDAO();
        List<Appointment> appointments = appointmentDAO.getAppointmentsByMechanic(currentMechanicId);
        if (appointments == null || appointments.isEmpty()) {
            showAlert(Alert.AlertType.INFORMATION, "No Appointments", "No appointments assigned to you.");
            tableView.getItems().clear();
        } else {
            configureAppointmentTable();
            ObservableList<Object> data = FXCollections.observableArrayList();
            data.addAll(appointments);
            tableView.setItems(data);
        }
    }

    @FXML
    private void handleUpdateAppointmentService() {
        // Prompt for Appointment ID
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

        // Check if the appointment is assigned to this mechanic
        AppointmentDAO appointmentDAO = new AppointmentDAO();
        Appointment appointment = appointmentDAO.getByAppointmentId(appointmentId);
        if (appointment == null) {
            showAlert(Alert.AlertType.INFORMATION, "Not Found", "Appointment with ID " + appointmentId + " not found.");
            return;
        }
        User assignedMechanic = appointment.getAssignedMechanic();
        if (assignedMechanic == null || assignedMechanic.getId() != currentMechanicId) {
            showAlert(Alert.AlertType.INFORMATION, "Not Authorized", "This appointment is not assigned to you.");
            return;
        }

        // Prompt for Service ID
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

        // Get the current AppointmentService
        AppointmentServiceDAO asDAO = new AppointmentServiceDAO();
        AppointmentService currentAS = asDAO.getByAppointmentServiceId(appointmentId, serviceId);
        if (currentAS == null) {
            showAlert(Alert.AlertType.INFORMATION, "Not Found", "Appointment Service not found for Appointment ID " + appointmentId + " and Service ID " + serviceId);
            return;
        }
        Long currentItemId = currentAS.getInventory().getItemId();

        // Prompt for new Quantity Used
        TextInputDialog quantityDialog = new TextInputDialog(String.valueOf(currentAS.getQuantityUsed()));
        quantityDialog.setTitle("Update Appointment Service");
        quantityDialog.setHeaderText("Enter new Quantity Used");
        quantityDialog.setContentText("Quantity:");
        Optional<String> quantityResult = quantityDialog.showAndWait();
        if (!quantityResult.isPresent() || quantityResult.get().trim().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Quantity is required.");
            return;
        }
        int newQuantity;
        try {
            newQuantity = Integer.parseInt(quantityResult.get().trim());
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Quantity must be numeric.");
            return;
        }

        // Update with the same itemId and new quantity
        asDAO.updateAppointmentService(appointmentId, serviceId, currentItemId, newQuantity);
        showAlert(Alert.AlertType.INFORMATION, "Success", "Appointment Service quantity updated successfully!");
    }

    @FXML
    private void handleAllAppointmentServices() {
        AppointmentDAO appointmentDAO = new AppointmentDAO();
        List<Appointment> appointments = appointmentDAO.getAppointmentsByMechanic(currentMechanicId);
        if (appointments == null || appointments.isEmpty()) {
            showAlert(Alert.AlertType.INFORMATION, "No Appointment Services", "No appointment services found for your assignments.");
            tableView.getItems().clear();
            return;
        }

        List<Long> appointmentIds = appointments.stream()
                .map(Appointment::getAppointmentId)
                .collect(Collectors.toList());

        AppointmentServiceDAO asDAO = new AppointmentServiceDAO();
        List<AppointmentService> allAsList = asDAO.getAllAppointmentServices();
        List<AppointmentService> filteredAsList = allAsList.stream()
                .filter(as -> appointmentIds.contains(as.getAppointmentId()))
                .collect(Collectors.toList());

        if (filteredAsList.isEmpty()) {
            showAlert(Alert.AlertType.INFORMATION, "No Appointment Services", "No appointment services found for your assignments.");
            tableView.getItems().clear();
        } else {
            configureAppointmentServiceTable();
            ObservableList<Object> data = FXCollections.observableArrayList(filteredAsList);
            tableView.setItems(data);
        }
    }

    @FXML
    private void handleGetSpecificAppointmentService() {
        TextInputDialog apptDialog = new TextInputDialog();
        apptDialog.setTitle("Get Specific Appointment Service");
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
        serviceDialog.setTitle("Get Specific Appointment Service");
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

        AppointmentServiceDAO asDAO = new AppointmentServiceDAO();
        AppointmentService appointmentService = asDAO.getByAppointmentServiceId(appointmentId, serviceId);
        if (appointmentService != null) {
            configureAppointmentServiceTable();
            ObservableList<Object> data = FXCollections.observableArrayList();
            data.add(appointmentService);
            tableView.setItems(data);
        } else {
            showAlert(Alert.AlertType.INFORMATION, "Not Found", "No appointment service found for Appointment ID " + appointmentId + " and Service ID " + serviceId);
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

    // -------------------- INVENTORY FUNCTIONALITIES --------------------

    @FXML
    private void handleUpdateItem() {
        TextInputDialog idDialog = new TextInputDialog();
        idDialog.setTitle("Update Inventory Item");
        idDialog.setHeaderText("Enter Item ID to update");
        idDialog.setContentText("Item ID:");
        Optional<String> idResult = idDialog.showAndWait();
        if (!idResult.isPresent() || idResult.get().trim().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Item ID is required.");
            return;
        }
        Long itemId;
        try {
            itemId = Long.parseLong(idResult.get().trim());
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Item ID must be numeric.");
            return;
        }
        InventoryDAO inventoryDAO = new InventoryDAO();
        Inventory item = inventoryDAO.getByInventoryId(itemId);
        if (item == null) {
            showAlert(Alert.AlertType.INFORMATION, "Not Found", "Inventory item with ID " + itemId + " not found.");
            return;
        }
        String currentName = item.getName();
        double currentPrice = item.getPrice();

        TextInputDialog quantityDialog = new TextInputDialog(item.getQuantity().toString());
        quantityDialog.setTitle("Update Inventory Item");
        quantityDialog.setHeaderText("Enter new Quantity");
        quantityDialog.setContentText("Quantity:");
        Optional<String> quantityResult = quantityDialog.showAndWait();
        if (!quantityResult.isPresent() || quantityResult.get().trim().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Quantity is required.");
            return;
        }
        Long newQuantity;
        try {
            newQuantity = Long.parseLong(quantityResult.get().trim());
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Quantity must be numeric.");
            return;
        }
        inventoryDAO.updateInventory(itemId, currentName, newQuantity, currentPrice);
        showAlert(Alert.AlertType.INFORMATION, "Success", "Inventory quantity updated successfully!");
    }

    @FXML
    private void handleAllItems() {
        InventoryDAO inventoryDAO = new InventoryDAO();
        List<Inventory> items = inventoryDAO.getAllInventory();
        if (items == null || items.isEmpty()) {
            showAlert(Alert.AlertType.INFORMATION, "No Items", "No inventory items found.");
            tableView.getItems().clear();
        } else {
            configureInventoryTable();
            ObservableList<Object> data = FXCollections.observableArrayList();
            data.addAll(items);
            tableView.setItems(data);
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

    // -------------------- LOGOUT FUNCTIONALITY --------------------

    @FXML
    private void handleLogout() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/thenexus/demo2/hello-view.fxml"));
            AnchorPane loginPane = loader.load();
            Scene loginScene = new Scene(loginPane);
            Stage currentStage = (Stage) rootPane.getScene().getWindow();
            currentStage.setMinHeight(600);
            currentStage.setMinWidth(1200);
            currentStage.setScene(loginScene);
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Could not load the login screen.");
        }
    }

    @FXML
    private void handleGetSpecificInventory() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Get Specific Inventory Item");
        dialog.setHeaderText("Enter Inventory Item ID");
        dialog.setContentText("Item ID:");
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent() && !result.get().trim().isEmpty()) {
            try {
                Long itemId = Long.parseLong(result.get().trim());
                InventoryDAO inventoryDAO = new InventoryDAO();
                Inventory item = inventoryDAO.getByInventoryId(itemId);
                if (item != null) {
                    configureInventoryTable();
                    ObservableList<Inventory> data = FXCollections.observableArrayList();
                    data.add(item);
                    tableView.setItems(FXCollections.observableArrayList(data));
                } else {
                    showAlert(Alert.AlertType.INFORMATION, "Not Found", "Inventory item with ID " + itemId + " not found.");
                }
            } catch (NumberFormatException e) {
                showAlert(Alert.AlertType.ERROR, "Invalid Input", "Item ID must be numeric.");
            }
        } else {
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Item ID is required.");
        }
    }

    // -------------------- HELPER METHOD --------------------

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}