package model;

/**
 * Created by fishiung on 2017-11-24
 */
public class Field {


    private String type;

    private boolean store;

   // private String analyzer;

    public Field(String type) {
        this.type = type;
    }

    public Field() {
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isStore() {
        return store;
    }

    public void setStore(boolean store) {
        this.store = store;
    }

//    public String getAnalyzer() {
//        return analyzer;
//    }
//
//    public void setAnalyzer(String analyzer) {
//        this.analyzer = analyzer;
//    }
}
