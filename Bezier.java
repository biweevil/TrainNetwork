package sample;

import com.sun.javafx.geom.Vec2d;
import static java.lang.Math.pow;

/**
 * Created by Jordan on 21/08/2017.
 */


public class Bezier {

    private Vec2d Start;
    private Vec2d Control1;
    private Vec2d Control2;
    private Vec2d End;

    public Bezier(Vec2d Start, Vec2d Control1, Vec2d Control2, Vec2d End) {
        this.Start = Start;
        this.Control1 = Control1;
        this.Control2 = Control2;
        this.End = End;
    }

    public Vec2d getPoint(double t){
        double x = StartCalc(t).x + Control1Calc(t).x + Control2Calc(t).x + EndCalc(t).x;
        double y = StartCalc(t).y + Control1Calc(t).y + Control2Calc(t).y + EndCalc(t).y;
        return new Vec2d(x, y);
    }

    private Vec2d StartCalc(double t){
        return new Vec2d(Start.x*Math.pow((1-t),3), Start.y*Math.pow((1-t),3));
    }

    private Vec2d Control1Calc(double t){
        return new Vec2d(3*Math.pow((1-t),2)*t*Start.x, 3*Math.pow((1-t),2)*t*Start.y);
    }

    private Vec2d Control2Calc(double t) {
        return new Vec2d(3*(1-t)*t*t*Start.x, 3*(1-t)*t*t*Start.y);
    }

    private Vec2d EndCalc(double t) {
        return new Vec2d(t*t*t*Start.x, t*t*t*Start.y);
    }
}
