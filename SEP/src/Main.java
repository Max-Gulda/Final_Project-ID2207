import com.sep.system.gui.SEPSystemGUI;
import com.sep.system.util.SessionManager;
import com.sep.system.auth.AuthenticationService;
import com.sep.system.util.DataManager;

public class Main {
    public static void main(String[] args) {
        // Initialize the session manager
        SessionManager sessionManager = new SessionManager();

        // Initialize the data manager for saving/loading user data
        DataManager dataManager = new DataManager();

        // Save data on shutdown
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            sessionManager.saveOnExit();  // Save session-specific data
            dataManager.saveUsers(sessionManager.getAuthService().getUsers());  // Save user data
            System.out.println("Data saved before exit.");
        }));

        // Start the GUI
        AuthenticationService authService = sessionManager.getAuthService();
        SEPSystemGUI gui = new SEPSystemGUI(authService, dataManager);  // Pass both authService and dataManager
        gui.createAndShowGUI();
    }
}
