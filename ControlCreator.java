package sample;


import com.sun.javafx.geom.Vec2d;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

import java.util.ArrayList;

/**
 * Created by Jordan on 01/08/2017.
 */
public class ControlCreator {


    //RETURNS NODE(S)
    public static Node[] CreateNode(ArrayList<ObjectProperty> properties){
        Pane propertyPane = new Pane();

        /* ALT overload each case. */
        for (ObjectProperty property: properties) {
            switch (property.getType()) {
                case "Integer":
                    propertyPane.getChildren().add(PropertyLabel(property));
                    propertyPane.getChildren().add(createIntegerBox(property));
                    break;
                case "Double":
                    propertyPane.getChildren().add(PropertyLabel(property));
                    propertyPane.getChildren().add(createDoubleBox(property));
                    break;
                case "Point2D":
                    propertyPane.getChildren().add(PropertyLabel(property));
                    propertyPane.getChildren().add(createPoint2DBox(property));
                    break;
                case "Vec2d":
                    propertyPane.getChildren().add(PropertyLabel(property, " \nX\t\tY\t"));
                    propertyPane.getChildren().add(createVectorBox(property));
                    break;
                case "String":
                    propertyPane.getChildren().add(PropertyLabel(property));
                    propertyPane.getChildren().add(createStringBox(property));
                    break;
                case "Boolean":
                    propertyPane.getChildren().add(PropertyLabel(property));
                    propertyPane.getChildren().add(createBoolean(property));
                    break;
            }
        }
        return (Node[]) (propertyPane.getChildren().toArray(new Node[]{}));
    }

    private static Label PropertyLabel(ObjectProperty property){
        Label propertyName = new Label(property.getName());
        propertyName.setPadding(new Insets(4));
        return propertyName;
    }

    private static Label PropertyLabel(ObjectProperty property, String extra){
        Label propertyName = new Label(property.getName()+extra);
        propertyName.setPadding(new Insets(4));
        return propertyName;
    }

    private static Label PropertyLabel(String property){
        Label propertyName = new Label(property);
        propertyName.setPadding(new Insets(4));
        return propertyName;
    }

    private static CheckBox createBoolean(ObjectProperty property){
        CheckBox BooleanCheckBox = new CheckBox("Enable");
        BooleanCheckBox.setDisable(!property.getEditible());
        BooleanCheckBox.setPadding(new Insets(4));
        BooleanCheckBox.setSelected((Boolean) property.Value);
        BooleanCheckBox.onMousePressedProperty().set(mEvent -> {
            property.Value = BooleanCheckBox.selectedProperty().getValue();
        });
        return BooleanCheckBox;
    }

    private static TextField createStringBox(ObjectProperty property){
        TextField StringBox = new TextField();
        StringBox.setDisable(!property.getEditible());
        StringBox.setPrefWidth(100);
        StringBox.setPadding(new Insets(4));
        StringBox.setText((String) property.Value);
        StringBox.setOnKeyReleased(kEvent -> {
            property.Value = StringBox.getText();
        });

        return StringBox;
    }

    private static HBox createVectorBox(ObjectProperty property){
        HBox HBoxContainer = new HBox(1);
        TextField VecX = new TextField();
        TextField VecY = new TextField();
        VecX.setDisable(!property.getEditible());
        VecY.setDisable(!property.getEditible());
        VecX.setPrefWidth(50);
        VecY.setPrefWidth(50);
        VecX.setPadding(new Insets(4));
        VecY.setPadding(new Insets(4));
        VecX.setText(Double.toString(((Vec2d)property.Value).x));
        VecY.setText(Double.toString(((Vec2d)property.Value).y));
        VecX.setOnKeyReleased(kEvent -> {
            try {
                ((Vec2d) property.Value).x = Double.valueOf(VecX.getText());
                VecX.setStyle("-fx-text-fill: #000000;");
            }catch (NumberFormatException e){
                VecX.setText(Double.toString(((Vec2d)property.Value).x));
                VecX.setStyle("-fx-text-fill: #a40017;");
            }
        });
        VecY.setOnKeyReleased(kEvent -> {
            try {
                ((Vec2d) property.Value).y = Double.valueOf(VecY.getText());
                VecY.setStyle("-fx-text-fill: #000000;");
            }catch (NumberFormatException e){
                VecY.setText(Double.toString(((Vec2d)property.Value).y));
                VecY.setStyle("-fx-text-fill: #a40017;");
            }
        });
        HBoxContainer.getChildren().add(VecX);
        HBoxContainer.getChildren().add(VecY);
        return HBoxContainer;
    }

    private static HBox createPoint2DBox(ObjectProperty property){
        HBox HBoxContainer = new HBox(1);
        TextField PX = new TextField();
        TextField PY = new TextField();
        PX.setDisable(!property.getEditible());
        PY.setDisable(!property.getEditible());
        PX.setPrefWidth(50);
        PY.setPrefWidth(50);
        PX.setPadding(new Insets(4));
        PY.setPadding(new Insets(4));
        PX.setText(Double.toString(((Point2D)(property.Value)).getX()));
        PY.setText(Double.toString(((Point2D)(property.Value)).getY()));
        PX.setOnMouseDragged(mEvent ->{
            numberDrag(PX, mEvent , new Double(0));
            setPoint2DBox(property, PX, PY);
        });
        PY.setOnMouseDragged(mEvent -> {
            numberDrag(PY, mEvent, new Double(0));
            setPoint2DBox(property, PX, PY);
        });
        PX.setOnKeyReleased(kEvent -> {
            setPoint2DBox(property, PX, PY);
        });
        PY.setOnKeyReleased(kEvent -> {
            setPoint2DBox(property, PX, PY);
        });
        HBoxContainer.getChildren().add(PX);
        HBoxContainer.getChildren().add(PY);
        return HBoxContainer;
    }

    private static void setPoint2DBox(ObjectProperty property, TextField PX, TextField PY){
        try {
            property.Value = new Point2D(Double.valueOf(PX.getText()), ((Point2D) property.Value).getY());
            PX.setStyle("-fx-text-fill: #000000;");
        }catch (NumberFormatException e){
            PX.setText(Double.toString(((Point2D)property.Value).getX()));
            PX.setStyle("-fx-text-fill: #a40017;");
        }
        try {
            property.Value = new Point2D(((Point2D) property.Value).getX(), Double.valueOf(PY.getText()));
            PY.setStyle("-fx-text-fill: #000000;");
        }catch (NumberFormatException e){
            PY.setText(Double.toString(((Point2D)property.Value).getY()));
            PY.setStyle("-fx-text-fill: #a40017;");
        }

    }

    private static TextField createDoubleBox(ObjectProperty property){
        TextField DoubleBox = new TextField();
        DoubleBox.setDisable(!property.getEditible());
        DoubleBox.setPrefWidth(100);
        DoubleBox.setPadding(new Insets(4));
        DoubleBox.setText(Double.toString((Double)property.Value));
        DoubleBox.setOnMouseDragged(mEvent ->{
            numberDrag(DoubleBox, mEvent, new Double(0));
            setDoubleBox(property, DoubleBox);
        });
        DoubleBox.setOnKeyReleased(kEvent -> {
            setDoubleBox(property, DoubleBox);
        });
        return DoubleBox;
    }

    private static void setDoubleBox(ObjectProperty property, TextField DoubleBox){
        try {
            property.Value = Double.valueOf(DoubleBox.getText());
            DoubleBox.setStyle("-fx-text-fill: #000000;");
        }catch (NumberFormatException e){
            DoubleBox.setText(Double.toString((Double) property.Value));
            DoubleBox.setStyle("-fx-text-fill: #a40017;");
        }
    }

    private static TextField createIntegerBox(ObjectProperty property){
        TextField IntegerBox = new TextField();
        IntegerBox.setDisable(!property.getEditible());
        IntegerBox.setPrefWidth(100);
        IntegerBox.setPadding(new Insets(4));
        IntegerBox.setText(Integer.toString((Integer) property.Value));
        IntegerBox.setOnMouseDragged(mEvent ->{
            numberDrag(IntegerBox, mEvent, new Integer(0));
            setIntegerBox(property, IntegerBox);
        });
        IntegerBox.setOnKeyReleased(kEvent -> {
            setIntegerBox(property, IntegerBox);
        });
        return IntegerBox;
    }

    private static void setIntegerBox(ObjectProperty property, TextField IntegerBox){
        try {
            property.Value = Integer.valueOf(IntegerBox.getText());
            IntegerBox.setStyle("-fx-text-fill: #000000;");
        }catch (NumberFormatException e){
            IntegerBox.setText(Integer.toString((Integer) property.Value));
            IntegerBox.setStyle("-fx-text-fill: #a40017;");
        }
    }

    private static void numberDrag(TextField box, MouseEvent mouseEvent, Double type){

        Double orignal = Double.valueOf(box.getText());
        box.setText(Double.toString(orignal += Math.pow(((mouseEvent.getY()- (box.getHeight()/2)) /50), 3)));
    }

    private static void numberDrag(TextField box, MouseEvent mouseEvent, Integer type){

        Integer orignal = Integer.valueOf(box.getText());
        box.setText(Integer.toString(orignal += (int)Math.pow(((mouseEvent.getY()- (box.getHeight()/2)) /50), 3)));
    }


}

