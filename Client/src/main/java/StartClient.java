import controllers.LoginController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import services.IService;

public class StartClient extends Application {
    public static void main(String[] args) {
        launch(args);
    }
    @Override
    public void start(Stage primaryStage) throws Exception {
        try {
            ApplicationContext factory = new ClassPathXmlApplicationContext("classpath:spring-client.xml");
            IService server=(IService)factory.getBean("service");
            System.out.println("Obtained a reference to remote papers server");
            FXMLLoader loader=new FXMLLoader();
            loader.setLocation(getClass().getResource("views/loginView.fxml"));
            Parent root=loader.load();

            LoginController loginController=loader.getController();
            loginController.setService(server);
            Stage stage = new Stage();
            stage.setTitle("LOGIN");
            stage.setScene(new Scene(root, 600, 350));
            stage.show();

        } catch (Exception e) {
            System.err.println("Words Initialization  exception:"+e);
            e.printStackTrace();
        }
    }
}
