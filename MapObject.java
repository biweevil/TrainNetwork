package sample;

import com.sun.javafx.geom.Vec2d;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;

/**
 * Created by Jordan on 30/07/2017.
 */
public class MapObject {

    protected ObjectProperty<Point2D> location;
    protected ObjectProperty<Vec2d> size;
    protected MainInterface mainInterface;
    protected ObjectProperty<Boolean> selectable;
    protected ObjectProperty<Integer> layer;
    protected ObjectProperty<String> objectname;
    protected ArrayList<ObjectProperty> Properties = new ArrayList<ObjectProperty>();

    MapObject(String objectname, Point2D location, MainInterface mainInterface, Vec2d size, int layer){
        this.mainInterface = mainInterface;

        this.objectname = new ObjectProperty<String>(objectname, "ObjectName", true);
        this.location = new ObjectProperty<Point2D>(location, "Location", true);
        this.size = new ObjectProperty<Vec2d>(size, "Size", true);
        this.selectable = new ObjectProperty<Boolean>(false, "Selectable", false);
        this.layer = new ObjectProperty<Integer>(layer, "Layer", true);
        Properties.add(this.objectname);
        Properties.add(this.location);
        Properties.add(this.size);
        Properties.add(this.selectable);
        Properties.add(this.layer);
    }

    public void draw(GraphicsContext gc) throws Exception {
        //throw new Exception("Method must be overidden");
        System.err.println("Method must be overidden");
        gc.fillRect(this.canvasLocation().getX(), this.canvasLocation().getY(), this.size.Value.x*this.getMainInterface().getZoomScale(), this.size.Value.y*this.getMainInterface().getZoomScale());
    }



    public MainInterface getMainInterface(){
        return mainInterface;
    }

    public Point2D canvasLocation(){
        Point2D canvaslocation = location.Value.add(this.mainInterface.getCanvasOffset());;
        return canvaslocation.multiply(this.mainInterface.getZoomScale());
    }

    public Point2D canvasLocation(double x, double y){
        Point2D canvaslocation = location.Value.add(this.mainInterface.getCanvasOffset().add(x,y));;
        return canvaslocation.multiply(this.mainInterface.getZoomScale());
    }

    public Vec2d canvasSize(){
        Vec2d returnSize = new Vec2d(this.size.Value.x*this.getMainInterface().getZoomScale(), this.size.Value.y*this.getMainInterface().getZoomScale());
        return returnSize;
    }

    public ArrayList<ObjectProperty> getProperties() {
        return Properties;
    }


    public String toString(){
        return this.getClass().getName().split("\\.")[1];
    }

    public MapObject createMapObject(MainInterface mainInterface){
        return new MapObject("MapObject", mainInterface.getCanvasOffset(), mainInterface, new Vec2d(20.0,20.0), Layer.BACKGROUND);
    }
}
