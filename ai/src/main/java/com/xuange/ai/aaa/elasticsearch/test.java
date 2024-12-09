package com.xuange.ai.aaa.elasticsearch;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.IndexRequest;
import co.elastic.clients.elasticsearch.indices.CreateIndexRequest;
import co.elastic.clients.transport.rest_client.RestClientTransport;

import java.io.IOException;

import static com.xuange.ai.aaa.elasticsearch.se.*;
//xuange
public class test {
    public void add(){
        RestClientTransport transPort = getTransPort();

        ElasticsearchClient client = createElsticSearch(transPort);
        CreateIndexRequest wwww = new CreateIndexRequest.Builder().index("wwww").build();
        try {
            if (client != null) {
                client.indices().create(wwww);
                String json = "{ \"name\": \"John Doe\", \"age\": 30, \"city\": \"New York\" }";

                IndexRequest<Object> build = new IndexRequest.Builder<>()
                        .index("wwww")
                        .id("1")
                        .document(json)
                        .build();
                client.index(build);
                closeElasticSearch(transPort);

            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
//Database（数据库）	Index（索引）	在关系型数据库中，Database 是存储数据的整体结构，而在 Elasticsearch 中，这对应于一个 Index。一个 Index 中包含了数据的逻辑分组，但不像数据库那么严格。
//Table（表）	Type（类型）（已弃用）	在早期版本的 Elasticsearch 中，一个 Index 中可以有多个 Type，类似于数据库中的表。但是从 6.x 开始，Type 被弃用，现在每个 Index 只能包含一种文档类型。
//Row（行）	Document（文档）	在数据库表中，一行代表一条数据记录；在 Elasticsearch 中，这对应一个 Document。一个 Document 使用 JSON 格式来存储数据。
//Column（列）	Field（字段）	在数据库的表中，每行都有相同的列结构；在 Elasticsearch 中，Field 是文档的属性。不同文档可以有不同的字段。
//Primary Key（主键）	_id（文档ID）	每条数据库记录都有一个主键来唯一标识，而在 Elasticsearch 中，每个 Document 也有一个 _id 字段，用于唯一标识文档。可以通过手动指定 _id 或由 Elasticsearch 自动生成。
//Schema（模式）	Mapping（映射）	数据库中的 Schema 定义表结构；在 Elasticsearch 中，Mapping 定义了 Index 的字段类型及索引规则。
//Index（索引）	Inverted Index（倒排索引）	在数据库中，Index 是为了加速查询而创建的索引结构；在 Elasticsearch 中，每个 Field 默认都创建了倒排索引，用于支持高效的全文检索。
//Query（查询）	Query DSL（查询 DSL）	数据库使用 SQL 语言查询数据，而 Elasticsearch 使用专门的 Query DSL（Domain Specific Language），以 JSON 格式编写查询请求。
//JOIN 操作	嵌套对象、父子关系	数据库支持 JOIN 操作来跨表查询关联数据；Elasticsearch 通过