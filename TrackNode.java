package sample;

import com.sun.javafx.geom.CubicCurve2D;
import com.sun.javafx.geom.Vec2d;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.BlendMode;
import javafx.scene.paint.Color;
import javafx.scene.transform.Rotate;


/**
 * Created by Jordan on 31/07/2017.
 */
public class TrackNode extends SelectableMapObject implements Connectable {

    private TrackPath TrackA;
    private TrackPath TrackB;

    TrackNode(Point2D location, MainInterface mainInterface){
        super("Track Node",location, mainInterface, new Vec2d(2,2), Layer.TRACKNODE);
        angle.Value = 45.0;
        angle.setEditible(true);
        size.setEditible(false);
        objectname.setEditible(false);
    }

    public void draw(GraphicsContext gc) {

        gc.setGlobalBlendMode(BlendMode.DIFFERENCE);
        gc.setFill(Color.color(0.2078, 0.2078, 0.2157));
        gc.fillOval(canvasLocation().getX(), canvasLocation().getY(), 2*this.getMainInterface().getZoomScale(), 2*this.getMainInterface().getZoomScale());
        gc.setFill(Color.color(0.8392, 0.8157, 0.8157));
        gc.fillOval(canvasLocation(0.25,0.25).getX(), canvasLocation(0.25, 0.25).getY(), 1.5*this.getMainInterface().getZoomScale(), 1.5*this.getMainInterface().getZoomScale());
        gc.save();
        Rotate r = new Rotate(angle.Value, canvasLocation(0.85,0.85).getX(), canvasLocation(0.85,0.85).getY());
        gc.setTransform(r.getMxx(), r.getMyx(), r.getMxy(), r.getMyy(), r.getTx(), r.getTy());
        gc.setFill(Color.YELLOW);
        gc.fillRect(canvasLocation(0,0.85).getX(), canvasLocation(0, 0.85).getY(), 2*this.getMainInterface().getZoomScale(), 0.3*this.getMainInterface().getZoomScale());
        gc.restore();
        gc.setGlobalBlendMode(BlendMode.SRC_OVER);
    }

    public MapObject createMapObject(MainInterface mainInterface){
        return new TrackNode(mainInterface.getCanvasOffset(), mainInterface);
    }

    @Override
    public void connect(Connectable other) {
        TrackPath newPath = new TrackPath( this, (TrackNode) other);

        other.connect(newPath);
    }

    @Override
    public void connect(MapObject newConnection) {
        this.TrackB = (TrackPath) newConnection;
        mainInterface.addNewMapObject(newConnection);
    }

    @Override
    public void disconnect(Connectable other) {

    }

    @Override
    public Connectable getOther() {
        return null;
    }
}
