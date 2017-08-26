package sample;

import com.sun.javafx.geom.Vec2d;
import javafx.geometry.Point2D;

/**
 * Created by Jordan on 31/07/2017.
 */
public class SelectableMapObject extends MapObject{


    protected ObjectProperty<Double> angle;

    SelectableMapObject(String objectname, Point2D location, MainInterface mainInterface, Vec2d size, int depth){
        super(objectname, location, mainInterface, size, depth);
        this.selectable.Value = true;
        angle = new ObjectProperty<Double>(0.0, "Angle", false);
        Properties.add(this.angle);
    }

    public boolean isTouched(Point2D clickLocation){
        Point2D canvasLocation = canvasLocation();
        int east = (int) canvasLocation.getX();
        int west = east +  (int)(this.size.Value.x*this.getMainInterface().getZoomScale());
        int north = (int) canvasLocation.getY();
        int south = north + (int)(this.size.Value.y*this.getMainInterface().getZoomScale());
        int clickX = (int) clickLocation.getX();
        int clickY = (int) clickLocation.getY();
        if(east <= clickX && clickX <= west && north <= clickY && clickY <= south){
            return true;
        }
            return false;
    }

    public void move(double x, double y){
        this.location.Value = this.location.Value.add(x/mainInterface.getZoomScale(), y/mainInterface.getZoomScale());
    }

    public void rotate(double x, double y){
        System.out.println((x-this.canvasLocation().getX())+" "+(y-this.canvasLocation().getY()));
        System.out.println(Math.toDegrees(Math.atan2(x-this.canvasLocation().getX(), y-this.canvasLocation().getY())));
        this.angle.Value = -(Math.toDegrees(Math.atan2(x-this.canvasLocation().getX(), y-this.canvasLocation().getY())));
    }

    public MapObject createMapObject(MainInterface mainInterface){
        return new SelectableMapObject("MapObject", mainInterface.getCanvasOffset(), mainInterface, new Vec2d(5,5), Layer.BACKGROUND);
    }

}
