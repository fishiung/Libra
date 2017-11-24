package load;

import client.ESClient;
import com.alibaba.fastjson.JSON;
import model.Field;
import model.MappingBean;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.Requests;
import org.elasticsearch.client.transport.TransportClient;
import source.MySQLSource;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by fishiung on 2017-11-24
 */
public class LoadToES {

    private static TransportClient client = null;


    static {
        client = ESClient.genClient();
    }



    public static void loadFromMySQL(MySQLSource source,String ...args){
        IndexResponse indexResponse =null;
        String indexName = source.getDatabaseName();
        if(args.length>=1) {
            //client.prepareIndex(args[0],args[1]).setSource().get();
//            indexName = args[0].toLowerCase();
//                client.admin().indices().prepareCreate(indexName).execute().actionGet();
        }
        //TODO if else
        if(args.length>=3){
            PutMappingRequest mapping = Requests.putMappingRequest(indexName).type(args[2]).source(builderMappingJson(source.getTableDesc(args[2])));
            client.admin().indices().putMapping(mapping).actionGet();
        }else{
            String[] tables = source.getTables();
            for(String table:tables){
                PutMappingRequest mapping = Requests.putMappingRequest(indexName).type(table).source(builderMappingJson(source.getTableDesc(table)));
                client.admin().indices().putMapping(mapping).actionGet();
            }
        }





        //添加数据

       // Requests.putMappingRequest(indexName).


    }




    //TODO  Class Field should rename
    public static String builderMappingJson(Object[] tableDesc){
        Object[] fields =tableDesc[0];
        Object[] types = (String[]) tableDesc[1];
        MappingBean mappingBean = new MappingBean();
        Map<String, Field> tmp = new HashMap<>();
        for(int i=0;i<fields.length;i++){
            tmp.put(fields[i],new Field(types[i]));
        }
        mappingBean.setProperties(tmp);
        return JSON.toJSONString(mappingBean);
    }


    public static void main(String[] args) {

        //loadFromMySQL(new MySQLSource(),"AA","BB");
        loadFromMySQL(new MySQLSource("jdbc:mysql://localhost:3306/hippo", "root", "root"),"BB");


    }

}
