package com.xuange.ai.aaa.elasticsearch;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.IndexRequest;
import co.elastic.clients.elasticsearch.indices.CreateIndexRequest;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;
//xuange
public class se {
    static AtomicInteger Id = new AtomicInteger(0);

    public static ElasticsearchClient createElsticSearch(RestClientTransport restClientTransport) {
        try {


            return new ElasticsearchClient(restClientTransport);
        } catch
        (Exception e) {
            e.printStackTrace();

        }

        return null;
    }
    public static Boolean closeElasticSearch(RestClientTransport restClientTransport){
        try {
           restClientTransport.close();

        } catch
        (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    public static RestClientTransport getTransPort(){
        RestClient localhost = RestClient.builder(new HttpHost("localhost", 4897)).build();
        return new RestClientTransport(localhost, new JacksonJsonpMapper());

    }
    public void add(String index,String json){
        RestClientTransport transPort = getTransPort();

        ElasticsearchClient client = createElsticSearch(transPort);
        CreateIndexRequest wwww = new CreateIndexRequest.Builder().index(index).build();
        try {
            if (client != null) {
                client.indices().create(wwww);

                IndexRequest<Object> build = new IndexRequest.Builder<>()
                        .index("wwww")
                        .id(String.valueOf(Id))
                        .document(json)
                        .build();
                client.index(build);
                closeElasticSearch(transPort);
                Id.incrementAndGet();

            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
//    public void Search(){
//        RestClientTransport transPort = getTransPort();
//
//        ElasticsearchClient client = createElsticSearch(transPort);
//        new Sear
//    }
}
