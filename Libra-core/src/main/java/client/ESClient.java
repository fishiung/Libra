package client;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import java.net.InetSocketAddress;

/**
 * Created by fishiung on 2017-11-24
 */
public class ESClient {

    public static TransportClient genClient(){
        TransportClient client = null;
        try {
            Settings settings = Settings.builder().put("cluster.name","Libra").put("node.name","node-1").put("client.transport.sniff",true).build();

            client = new PreBuiltTransportClient(settings).addTransportAddress(new TransportAddress(new InetSocketAddress("127.0.0.1",9300)));
        }catch(Exception e){
            e.printStackTrace();
        }
        return client;
    }




}
