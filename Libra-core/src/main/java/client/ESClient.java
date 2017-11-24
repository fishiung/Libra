package client;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import java.net.InetAddress;
import java.net.InetSocketAddress;

/**
 * Created by fishiung on 2017-11-24
 */
public class ESClient {

    public static TransportClient genClient(){
        TransportClient client = null;
        try {
            Settings settings = Settings.builder().put("cluster.name","Libra").put("client.transport.sniff",true).build();
            client = new PreBuiltTransportClient(settings).addTransportAddress(new InetSocketTransportAddress(new InetSocketAddress("localhost",9300)));
        }catch(Exception e){
            e.printStackTrace();
        }
        return client;
    }




}
