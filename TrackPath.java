package sample;


import com.sun.javafx.geom.Vec2d;
import javafx.scene.canvas.GraphicsContext;
import org.jetbrains.annotations.Contract;

import java.awt.geom.CubicCurve2D;
import java.awt.geom.PathIterator;


/**
 * Created by Jordan on 31/07/2017.
 */
public class TrackPath extends MapObject{

    TrackPath(TrackNode tracknodeA, TrackNode tracknodeB){
        super("TrackPath",tracknodeA.location.Value, tracknodeA.getMainInterface(), new Vec2d(0,0), Layer.TRACK);
        this.tracknodeA = tracknodeA;
        this.tracknodeB = tracknodeB;
        this.mainInterface = tracknodeA.getMainInterface();
        updateCurve();
    }

    private MainInterface mainInterface;
    private CubicCurve2D.Double trackcurve;
    private TrackNode tracknodeA;
    private TrackNode tracknodeB;


    public void updateCurve(){
        double ax = tracknodeA.canvasLocation().getX();
        double ay = tracknodeA.canvasLocation().getY();
        double bx = tracknodeB.canvasLocation().getX();
        double by = tracknodeB.canvasLocation().getY();
        double midpoint = tracknodeA.canvasLocation().distance(tracknodeB.canvasLocation())/2;
        double acx = midpoint*Math.acos(tracknodeA.angle.Value);
        double acy = midpoint*Math.asin(tracknodeA.angle.Value);
        double bcx = midpoint*Math.acos(tracknodeB.angle.Value+180);
        double bcy = midpoint*Math.asin(tracknodeB.angle.Value+180);

        this.trackcurve = new CubicCurve2D.Double(ax, ay, acx, acy, bcx, bcy, bx, by);
    }



    public void draw(GraphicsContext gc){
        java.awt.geom.PathIterator pi =  trackcurve.getPathIterator(null);
        while (!pi.isDone()){
            doLine(gc, pi);
            pi.next();
        }
    }

    private void doLine(GraphicsContext gc, PathIterator pi){

        double[] coordinates = new double[6];
        int type = pi.currentSegment(coordinates);
        for(int i = 0; i < 6; i+=2) {
            coordinates[i] = (coordinates[i] + mainInterface.getCanvasOffset().getX()) * mainInterface.getZoomScale();
            coordinates[i+1] = (coordinates[i+1] + mainInterface.getCanvasOffset().getY()) * mainInterface.getZoomScale();
        }


        switch (type){
            case PathIterator.SEG_MOVETO:
                gc.moveTo(coordinates[0],coordinates[1]);

                break;
            case PathIterator.SEG_LINETO:
                gc.lineTo(coordinates[0],coordinates[1]);

                break;
            case PathIterator.SEG_CLOSE:
                break;
            case PathIterator.SEG_CUBICTO:
                gc.bezierCurveTo(coordinates[0],coordinates[1],coordinates[2],coordinates[3],coordinates[4],coordinates[5]);
                break;
            default:

                break;
        }
        gc.stroke();
    }


}
