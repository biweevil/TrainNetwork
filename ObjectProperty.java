package sample;

import javax.swing.text.StyledEditorKit;

/**
 * Created by Jordan on 09/08/2017.
 */
public class ObjectProperty<Type> {

    public Type Value;
    private String name;
    private Boolean editible;

    ObjectProperty(Type Value, String name, Boolean editible){
        this.Value = Value;
        this.name = name;
        this.editible = editible;
    }


    public String getType(){
        String type;
        try {
            String wholePath = Value.getClass().getTypeName();
            String path[] = wholePath.split("\\.");
            int length = path.length;
            type = path[length-1];
        }catch (NullPointerException e){
            type = "null";
        }catch (ArrayIndexOutOfBoundsException r){
            type = "null";
        }
        return type;
    }

    public String getName(){
        return this.name;
    }

    public Boolean getEditible(){
        return this.editible;
    }

    public void setEditible(Boolean editible){
        this.editible = editible;
    }
}
