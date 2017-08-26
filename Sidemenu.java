package sample;

import com.sun.javafx.geom.Vec2d;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;

import java.util.ArrayList;
import java.util.Collection;

public class Sidemenu {

    @FXML
    private ListView<MapObject> list;

    @FXML
    private Button addButton;

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {



        list.getItems().add(new SelectableMapObject("Square", Point2D.ZERO, mainInterface, new Vec2d(0,0), Layer.BACKGROUND ){
            @Override
            public void draw(GraphicsContext gc) throws Exception {
                super.draw(gc);
            }
        });
        list.getItems().add(new TrackNode(new Point2D(0,0), mainInterface));
        list.getItems().add(new TrackNode(new Point2D(0,0), mainInterface));
        list.getItems().add(new TrackNode(new Point2D(0,0), mainInterface));
        list.getItems().add(new TrackNode(new Point2D(0,0), mainInterface));

        addButton.setOnMousePressed(mEvent -> {

        mainInterface.addNewMapObject(list.getSelectionModel().getSelectedItem().createMapObject(this.mainInterface));



            //mainInterface.getCanvasOffset();
        });
    }


    private MainInterface mainInterface;

    public void setMainInterface(MainInterface mainInterface){
        this.mainInterface = mainInterface;
    }

}