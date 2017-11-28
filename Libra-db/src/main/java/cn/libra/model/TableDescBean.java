package cn.libra.model;

/**
 * Created by fishiung on 2017-11-27
 */
public class TableDescBean {

    private String tableName;

    private String [] fields;

    private String [] types;

    public TableDescBean() {
    }

    public TableDescBean(String tableName, String[] fields, String[] types) {
        this.tableName = tableName;
        this.fields = fields;
        this.types = types;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String[] getFields() {
        return fields;
    }

    public void setFields(String[] fields) {
        this.fields = fields;
    }

    public String[] getTypes() {
        return types;
    }

    public void setTypes(String[] types) {
        this.types = types;
    }
}
