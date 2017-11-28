package cn.libra.source;

/**
 * Created by fishiung on 2017-11-24
 */
public interface Source {
    String type();

    default boolean isDatabase() {
        return false;
    }

    default boolean isTable(){
        return true;
    }

    default String[] getFields(String tableName){
        return null;
    }

    default String[] getType(String tableName){
        return null;
    }
}
