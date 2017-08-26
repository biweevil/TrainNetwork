package sample;

import java.util.ArrayList;

/**
 * Created by Jordan on 13/08/2017.
 */
public interface Connectable {
    ArrayList<Connectable> others = new ArrayList<Connectable>();
     void connect(Connectable other);
    void connect(MapObject newConnection);
     void disconnect(Connectable other);
    Connectable getOther();
}
