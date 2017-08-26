/**
 * Sample Skeleton for 'sample.fxml' Controller Class
 */

package sample;



import com.sun.javafx.geom.Vec2d;
import javafx.animation.AnimationTimer;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.net.URL;
import java.util.*;




public class Controller {

    private enum cursorTypes{
        CROSS, MOVE
    }

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="topbar"
    private ToolBar topbar; // Value injected by FXMLLoader

    @FXML // fx:id="connect"
    private Button connect; // Value injected by FXMLLoader

    @FXML // fx:id="disconnect"
    private Button disconnect; // Value injected by FXMLLoader

    @FXML // fx:id="sidelist"
    private VBox sidelist; // Value injected by FXMLLoader

    @FXML // fx:id="canvas"
    private Canvas canvas; // Value injected by FXMLLoader

    @FXML // fx:id="toolbar"
    private VBox toolbar; // Value injected by FXMLLoader

    @FXML // fx:id="zoomslider"
    private Slider zoomslider; // Value injected by FXMLLoader

    @FXML // fx:id="zoomin"
    private Button zoomin; // Value injected by FXMLLoader

    @FXML // fx:id="zoomout"
    private Button zoomout; // Value injected by FXMLLoader

    @FXML // fx:id="changeCursor"
    private ImageView changeCursor; // Value injected by FXMLLoader


    private ArrayList<MapObject> mapObjects;
    private MainInterface mainInterface;
    private cursorTypes cursormode = cursorTypes.CROSS;
    private Point2D lastMouseDragPos;
    private boolean movingObject = false;

    private boolean connectingObject = false;
    private Connectable first;

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert topbar != null : "fx:id=\"topbar\" was not injected: check your FXML file 'sample.fxml'.";
        assert connect != null : "fx:id=\"testbutton\" was not injected: check your FXML file 'sample.fxml'.";
        assert disconnect != null : "fx:id=\"testbutton\" was not injected: check your FXML file 'sample.fxml'.";
        assert sidelist != null : "fx:id=\"sidelist\" was not injected: check your FXML file 'sample.fxml'.";
        assert canvas != null : "fx:id=\"canvas\" was not injected: check your FXML file 'sample.fxml'.";
        assert toolbar != null : "fx:id=\"toolbar\" was not injected: check your FXML file 'sample.fxml'.";
        assert zoomslider != null : "fx:id=\"zoomslider\" was not injected: check your FXML file 'sample.fxml'.";
        assert zoomin != null : "fx:id=\"zoomin\" was not injected: check your FXML file 'sample.fxml'.";
        assert zoomout != null : "fx:id=\"zoomout\" was not injected: check your FXML file 'sample.fxml'.";
        zoomslider.setMin(10);
        zoomslider.setMax(100);
        sidelist.setPadding(new Insets(10));
        changeCursor.setImage(new Image("sample/cursor move.png"));

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                GraphicsContext gc = canvas.getGraphicsContext2D();
                gc.clearRect(0,0,canvas.getWidth(), canvas.getHeight());
                drawMapObjects(gc);
                highlight(mainInterface.getCurrentSelectedMapObject(), gc);
            }
        };

        timer.start();




        //Image image = new Image("sample/track.png");
        //canvas.getGraphicsContext2D().drawImage(image, 10, 10);

        connect.setOnAction(event -> {
            if (!connectingObject && mainInterface.getCurrentSelectedMapObject() != null) {
                first = (Connectable) mainInterface.getCurrentSelectedMapObject();
                connect.setText("To");
                connectingObject = true;
            } else if(first != mainInterface.getCurrentSelectedMapObject()){
                first.connect((Connectable) mainInterface.getCurrentSelectedMapObject());
                connect.setText("Connect");
                connectingObject = false;

            }
            else{
                connect.setText("Failed");
                connectingObject = false;
            }


        });

        zoomin.setOnAction(event -> {
            zoomslider.setValue(zoomslider.getValue()*1.05+1);
            mainInterface.setZoomScale(0.05+(5000/500-0.05)*Math.log10(zoomslider.getValue()/10));
        });

        zoomout.setOnAction(event -> {
            zoomslider.setValue(zoomslider.getValue()*0.95-1);
            mainInterface.setZoomScale(0.05+(5000/500-0.05)*Math.log10(zoomslider.getValue()/10));
        });

        canvas.setOnMousePressed(mEvent -> {
            mainInterface.sendClickEvent(mEvent, canvas.getCursor());
            lastMouseDragPos = new Point2D(mEvent.getX(),mEvent.getY());
        });

        canvas.setOnMouseDragged(mEvent -> {
            SelectableMapObject selected = mainInterface.getCurrentSelectedMapObject();
            double disX =  (mEvent.getX() - lastMouseDragPos.getX());
            double disY =  (mEvent.getY() - lastMouseDragPos.getY());
            if(canvas.getCursor() == Cursor.MOVE) {
                mainInterface.adjustCurrentOffset(disX, disY);
            }
            try {
                if ((selected.isTouched(new Point2D(mEvent.getX(), mEvent.getY()))&& mEvent.getClickCount() > 1) || movingObject) {
                    canvas.setCursor(Cursor.CLOSED_HAND);
                    movingObject = true;
                    if (!mEvent.isShiftDown())
                        selected.move(disX, disY);
                    else{
                        selected.rotate(mEvent.getX(),mEvent.getY());
                    }
                }
            }catch (NullPointerException e){
                movingObject = false;
            }
            lastMouseDragPos = new Point2D(mEvent.getX(), mEvent.getY());
        });

        canvas.setOnMouseReleased(mEvent -> {
            if(movingObject){
                movingObject = false;
                canvas.setCursor(Cursor.CROSSHAIR);
            }
        });

        zoomslider.setOnMouseReleased(event -> {
            mainInterface.setZoomScale(0.05+(5000/500-0.05)*Math.log10(zoomslider.getValue()/10));
        });

        changeCursor.setOnMouseReleased(event -> {
            if(cursormode == cursorTypes.CROSS) {
                Image MoveCursor = new Image("sample/cursor cross.png");
                changeCursor.setImage(MoveCursor);
                cursormode = cursorTypes.MOVE;
                canvas.setCursor(Cursor.MOVE);
            }else{
                Image CrossCursor = new Image("sample/cursor move.png");
                changeCursor.setImage(CrossCursor);
                cursormode = cursorTypes.CROSS;
                canvas.setCursor(Cursor.CROSSHAIR);
            }
        });
    }

    private void highlight(MapObject target, GraphicsContext gc) {
        try {
            gc.setStroke(Color.YELLOW);
            gc.setLineWidth(mainInterface.getZoomScale()/5);
            gc.strokeRect(target.canvasLocation().getX(), target.canvasLocation().getY(), target.canvasSize().x, target.canvasSize().y);
        }catch (NullPointerException e){

        }
    }

    private void drawMapObjects(GraphicsContext gc){
        mapObjects.sort(Comparator.comparingInt(mapObject -> mapObject.layer.Value));
        for (MapObject mapObject : mapObjects) {
            try {
                mapObject.draw(gc);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void setSideListNodes(Node[] list){
        sidelist.getChildren().clear();
        sidelist.getChildren().addAll(list);
    }

    public void setMapObjects(ArrayList<MapObject> mapObjects){
        this.mapObjects = mapObjects;
    }

    public void setMainInterface(MainInterface mainInterface){
        this.mainInterface = mainInterface;
    }

    public Vec2d getCanvasSize() {
        return new Vec2d(this.canvas.getWidth(), this.canvas.getHeight());
    }
}
