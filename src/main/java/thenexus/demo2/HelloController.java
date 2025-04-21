package thenexus.demo2;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.layout.AnchorPane;
import org.hibernate.Session;
import org.hibernate.query.Query;
import thenexus.demo2.model.User;
import thenexus.demo2.util.HibernateUtil;

public class HelloController {
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Button loginButton;

    @FXML
    protected void handleLogin() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        if (username.isEmpty() || password.isEmpty()) {
            showAlert("Error", "Username or Password cannot be empty", Alert.AlertType.ERROR);
            return;
        }

        // Use Hibernate to authenticate
        User user = authenticateUser(username, password);

        if (user != null && user.getActive().toLowerCase().equals("yes")) {
            loadDashboard(user);
        } else {
            showAlert("Login Failed", "Invalid username/password or user inactive", Alert.AlertType.ERROR);
        }
    }

    private User authenticateUser(String username, String password) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "FROM User u WHERE u.username = :username AND u.password = :password";
            Query<User> query = session.createQuery(hql, User.class);
            query.setParameter("username", username);
            query.setParameter("password", password);
            return query.uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void loadDashboard(User user) {
        String role = user.getRole().getRoleName().toLowerCase();
        String fxmlFile;
        switch (role) {
            case "admin":
                fxmlFile = "/thenexus/demo2/admin-dashboard.fxml";
                break;
            case "sales rep":
                fxmlFile = "/thenexus/demo2/sales-dashboard.fxml";
                break;
            case "mechanic":
                fxmlFile = "/thenexus/demo2/mechanic-dashboard.fxml";
                break;
            default:
                showAlert("Error", "Unknown role: " + role, Alert.AlertType.ERROR);
                return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource(fxmlFile));
            AnchorPane pane = loader.load();
            if (role.equals("mechanic")) {
                MechanicDashboardController controller = loader.getController();
                controller.setCurrentMechanicId(user.getId());
            }
            Stage stage = (Stage) loginButton.getScene().getWindow();
            stage.setScene(new Scene(pane));
            stage.setTitle(role + " Dashboard");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Could not load dashboard for role: " + role, Alert.AlertType.ERROR);
        }
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(message);

        // Get the stylesheet
        String stylesheet = getClass().getResource("/thenexus/demo2/alert.css") != null
                ? getClass().getResource("/thenexus/demo2/alert.css").toExternalForm()
                : null;

        // Apply the stylesheet if it's found
        if (stylesheet != null) {
            alert.getDialogPane().getStylesheets().add(stylesheet);
        } else {
            System.err.println("CSS file not found!");
        }

        alert.showAndWait();
    }
}