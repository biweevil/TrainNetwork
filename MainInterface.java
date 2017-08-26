package sample;


import com.sun.javafx.geom.Vec2d;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;


import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Jordan on 30/07/2017.
 */
public class MainInterface {

    private Controller controller;
    private Sidemenu sidemenu;
    private Node sidemenuNode;
    private ArrayList<MapObject> mapObjects;

    private Point2D currentOffset;
    private double zoomScale;
    private SelectableMapObject currentSelectedMapObject;


    MainInterface(Controller controller){
        this.controller = controller;
        controller.setMainInterface(this);
        this.mapObjects = new ArrayList<MapObject>();

        //temp canvas location
        this.currentOffset = new Point2D(0,0);
        this.zoomScale = 10;

        createCreatorMenu();





        //test mapobject ****************************************************************
        Point2D testLocation = new Point2D(10,10);
        Vec2d testSize = new Vec2d(40,40);
        SelectableMapObject test = new SelectableMapObject("test",testLocation, this, testSize, 10){
            @Override
            public void draw(GraphicsContext gc) {
                gc.setFill(Color.GREEN);
                Point2D localLocation = this.canvasLocation();
                gc.fillRect(localLocation.getX(), localLocation.getY(), testSize.x*this.getMainInterface().getZoomScale(), testSize.y*this.getMainInterface().getZoomScale());

            }
        };
        mapObjects.add(test);

        Point2D testLocation2 = new Point2D(20,20);
        Vec2d testSize2 = new Vec2d(20,20);
        SelectableMapObject test2 = new SelectableMapObject("test2", testLocation2, this, testSize2, 20){
            @Override
            public void draw(GraphicsContext gc) {
                gc.setFill(Color.BLUE);
                Point2D localLocation = this.canvasLocation();
                gc.fillRect(localLocation.getX(), localLocation.getY(), 20*this.getMainInterface().getZoomScale(), 20*this.getMainInterface().getZoomScale());

            }
        };
        mapObjects.add(test2);

        Point2D testLocation3 = new Point2D(-20,-20);
        Vec2d testSize3 = new Vec2d(5,5);
        SelectableMapObject test3 = new SelectableMapObject("test3", testLocation3, this, testSize3, 30){
            @Override
            public void draw(GraphicsContext gc) {
                gc.setFill(Color.RED);
                Point2D localLocation = this.canvasLocation();
                gc.fillRect(localLocation.getX(), localLocation.getY(), 5*this.getMainInterface().getZoomScale(), 5*this.getMainInterface().getZoomScale());

            }
        };
        mapObjects.add(test3);

        mapObjects.add(new TrackNode(new Point2D(49,45), this));
        mapObjects.add(new TrackNode(new Point2D(80,49), this));
        mapObjects.add(new TrackNode(new Point2D(80,50), this));
        //********************************************************************************************

        controller.setMapObjects(mapObjects);

    }

    public Point2D getCanvasOffset(){
        return currentOffset;
    }

    public Vec2d getCanvasSize() {
        return controller.getCanvasSize();
    }

    public double getZoomScale(){
        return zoomScale;
    }

    public void sendClickEvent(MouseEvent mEvent, Cursor cursor){
        if(cursor == Cursor.CROSSHAIR)
            selectMapObject(mEvent);

    }

    public void setZoomScale(double zoomScale){
        this.zoomScale = zoomScale;
        System.out.println(zoomScale);
    }

    public void adjustCurrentOffset(double x, double y){
        this.currentOffset = this.currentOffset.add(x/getZoomScale(), y/getZoomScale());
    }

    public void setCurrentOffset(int x, int y){
        this.currentOffset = new Point2D(x, y);
    }

    public SelectableMapObject getCurrentSelectedMapObject(){
        return currentSelectedMapObject;
    }

    private boolean selectMapObject(MouseEvent mEvent){
        boolean objectSelected = false;
        ArrayList<SelectableMapObject> objectsInBounds = new ArrayList<SelectableMapObject>();
        for(MapObject mapObject: mapObjects){
            if(mapObject.selectable.Value) {
                SelectableMapObject selectableMapObject = (SelectableMapObject) mapObject;
                if(selectableMapObject.isTouched(XYtoPoint((int) mEvent.getX(), (int) mEvent.getY()))){
                    objectsInBounds.add(selectableMapObject);
                    objectSelected = true;
                }
            }
        }

        if(objectSelected) {
            SelectableMapObject smallest = objectsInBounds.get(0);
            for(SelectableMapObject currentObject: objectsInBounds){
                if(vectorMagnitude(currentObject.size.Value) < vectorMagnitude(smallest.size.Value)){
                    smallest = currentObject;
                }
            }
            currentSelectedMapObject = smallest;
        }else{
            currentSelectedMapObject = null;
        }
        updateSideMenu(currentSelectedMapObject);
        return objectSelected;
    }

    public static double vectorMagnitude (Vec2d vector){
        return Math.sqrt(vector.x * vector.x + vector.y * vector.y);
    }

    private Point2D XYtoPoint(int x, int y){
        Point2D newPoint = new Point2D(x, y);
        return newPoint;
    }

    private void updateSideMenu(MapObject target){
        if(this.currentSelectedMapObject == null){
            controller.setSideListNodes(new Node[]{sidemenuNode});
        }
        else
        {
            controller.setSideListNodes(ControlCreator.CreateNode(target.getProperties()));
        }

    }

    private void createCreatorMenu(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("sidemenu.fxml"));
            sidemenuNode =  loader.load();
            sidemenu = loader.getController();
            sidemenu.setMainInterface(this);
        }catch (IOException e){

        }
    }

    public void addNewMapObject(MapObject mapObject){
        this.mapObjects.add(mapObject);
        controller.setMapObjects(mapObjects);
    }

    public void removeMapObject(MapObject mapObject){
        this.mapObjects.remove(mapObject);
        controller.setMapObjects(mapObjects);
    }


}
