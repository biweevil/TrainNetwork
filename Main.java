package sample;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Main extends Application {

    protected Controller controller;
    protected MainInterface maininterface;

    @Override
    public void start(Stage primaryStage) throws Exception{

        //load fxml
        FXMLLoader loader = new FXMLLoader(getClass().getResource("sample.fxml"));
        Parent root = (Parent) loader.load();
        controller = loader.getController();

        //set up stage
        primaryStage.setTitle("Train Network");
        primaryStage.setResizable(false);
        primaryStage.setScene(new Scene(root, 1000, 580));
        Image icon = new Image("sample/track.png");
        primaryStage.getIcons().add(icon);
        primaryStage.show();

        //create interface
        maininterface = new MainInterface(controller);

    }


    public static void main(String[] args) {
        launch(args);
    }
}
