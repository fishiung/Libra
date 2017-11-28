package cn.libra.load;

import client.ESClient;
import com.alibaba.fastjson.JSON;
import cn.libra.model.Field;
import cn.libra.model.MappingBean;
import cn.libra.model.TableDescBean;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpHost;
import org.apache.http.StatusLine;
import org.apache.http.entity.StringEntity;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.transport.TransportClient;
import cn.libra.source.MySQLSource;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;


/**
 * Created by fishiung on 2017-11-24
 */
public class LoadToES {

    private static TransportClient client = null;


    static {
        client = ESClient.genClient();
    }


    public static void loadFromMySQL(MySQLSource source, String... args) {
        IndexResponse indexResponse = null;
        String indexName = source.getDatabaseName();

        RestClient client = getRestClient();


        if (args.length >= 1) {
            //client.prepareIndex(args[0],args[1]).setSource().get();
//            indexName = args[0].toLowerCase();
//                client.admin().indices().prepareCreate(indexName).execute().actionGet();
        }
        //TODO if else
//        if (args.length >= 3) {
//            PutMappingRequest mapping = Requests.putMappingRequest(indexName).type(args[2]).source(builderSource(source.getTableDesc(args[2])));
//            client.admin().indices().putMapping(mapping).actionGet();
//        } else {
//            String[] tables = source.getTables();
//            for (String table : tables) {
//                PutMappingRequest mapping = Requests.putMappingRequest(indexName).type(table).source(builderSource(source.getTableDesc(table)));
//                client.admin().indices().putMapping(mapping).actionGet();
//            }
//        }


        //添加数据

        // Requests.putMappingRequest(indexName).


    }

    public static Map<String,String> builderSource(TableDescBean tableDesc){
        Map<String,String> source = new HashMap<>();
        String[] fields = tableDesc.getFields();
        String[] types = tableDesc.getTypes();
        for (int i = 0; i < fields.length; i++) {
            source.put(fields[i], types[i]);
        }
        return source;
    }


    //TODO  Class Field should rename
    public static String builderMappingJson(TableDescBean tableDesc) {
        String[] fields = tableDesc.getFields();
        String[] types = tableDesc.getTypes();
        MappingBean mappingBean = new MappingBean();
        Map<String, Field> tmp = new LinkedHashMap<>();
        for (int i = 0; i < fields.length; i++) {
            tmp.put(fields[i], new Field(types[i]));
        }
        mappingBean.setProperties(tmp);
        return JSON.toJSONString(mappingBean);
    }


    public static void main(String[] args) throws IOException,SQLException{
        MySQLSource source = new MySQLSource("jdbc:mysql://localhost:3306/hippo","root","root");
        StringEntity  mapping = new StringEntity(builderMappingJson(source.getTableDesc("data_import_record")),"utf-8");
        mapping.setContentType("application/json");
        mapping.setContentEncoding("UTF-8");
        RestClient client = getRestClient();
//        Response response = client.performRequest("PUT", "/test/d/_mapping", Collections.emptyMap(),mapping);
//        StatusLine line = response.getStatusLine();
//        int statusCode = line.getStatusCode();
//        System.out.println(statusCode);
        Response response = bulkAdd(client,"","",source.getAll());
        int statusCode1 = response.getStatusLine().getStatusCode();
        System.out.println(statusCode1);
        client.close();
    }

    public static RestClient getRestClient(){
        return RestClient.builder(new HttpHost("local",9200)).build();
    }

    public static Response bulkAdd(RestClient client,String index, String type, String[] datas) throws IOException{
        //{ "index": {}}
        //{ "price" : 1000, "color" : "红色", "brand" : "长虹", "sold_date" : "2016-10-28" }
        String res = "";
        String addCommand = "{ \"index\": {}}\r\n";

        for (String data : datas) {
            res+=addCommand+data+"\r\n";
        }
        StringEntity  mapping = new StringEntity(res,"utf-8");
        mapping.setContentType("application/x-ndjson");
        mapping.setContentEncoding("UTF-8");
        return  client.performRequest("PUT", "/test/d/_bulk", Collections.emptyMap(),mapping);
    }


    //TODO 创建index
    //TODO 创建mapping
    //TODO bulk add  buik API





}
