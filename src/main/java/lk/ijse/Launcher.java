package lk.ijse;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lk.ijse.comfit.FactoryConfiguration;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.io.IOException;

public class Launcher extends Application {
    public static void main(String[] args)  {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Session session;
        try {
            session = FactoryConfiguration.getInstance().getSession();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Transaction transaction = session.beginTransaction();
        transaction.commit();
        session.close();


        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/login_form.fxml"));
        Scene scene = new Scene(loader.load());

        stage.setScene(scene);
        stage.setTitle("Login Form");
        stage.centerOnScreen();
        stage.show();
    }}