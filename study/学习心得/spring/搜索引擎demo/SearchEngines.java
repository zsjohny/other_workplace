package com.newman.task.engines;

import com.newman.dto.PowerPurchaserProduct;
import com.newman.puglin.mybatis.util.MybatisOperaterUtil;
import com.newman.puglin.mybatis.util.MybatisSqlWhereBuild;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequestBuilder;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingResponse;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequestBuilder;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

@Component
public class SearchEngines {


    @PostConstruct
    public void init() throws IOException {

        //创建客户端
        TransportClient client = new PreBuiltTransportClient(Settings.EMPTY).addTransportAddresses(
                new TransportAddress(InetAddress.getByName("api.kuayet.com"), 9300));

        List<PowerPurchaserProduct> list = MybatisOperaterUtil.getInstance().finAll(new PowerPurchaserProduct(),
                new MybatisSqlWhereBuild(PowerPurchaserProduct.class));

        //批量插入
        BulkRequestBuilder bulkRequest = client.prepareBulk();

        int len = list.size();
        for (int i = 0; i < len; i++) {
            IndexRequestBuilder ir = client.prepareIndex()
                    .setIndex(PowerPurchaserProduct.class.getCanonicalName().toLowerCase())
                    .setType(PowerPurchaserProduct.class.getSimpleName().toLowerCase())
                    .setId("" + i)
                    .setSource(javabean2json(list.get(i)));
            bulkRequest.add(ir);
        }

        BulkResponse bulkResponse = bulkRequest.execute().actionGet();
        if (!bulkResponse.hasFailures()) {
            System.out.println("全部插入成功");
        }

    }

    private XContentBuilder javabean2json(PowerPurchaserProduct product) throws IOException {
        XContentBuilder xContentBuilder = XContentFactory.jsonBuilder().startObject().field("id", product.getId())
                .field("name", product.getName()).field("source", product.getSource())
                .field("price", product.getPrice()).field("salesVolume", product.getSalesVolume()).
                        field("imageUrl", product.getImageUrl()).field("type", product.getType())
                .field("typeId", product.getTypeId()).field("deduction", product.getDeduction())
                .field("qLink", product.getQLink()).field("goodId", product.getGoodId()).
                        field("detailUrl", product.getDetailUrl()).field("largerUrl", product.getLargerUrl())
                .field("expireTime", product.getExpireTime()).field("category", product.getCategory())
                .field("channel", product.getChannel()).endObject();
        return xContentBuilder;
    }

    public static void main(String[] args) throws Exception {
        //
//        createMapping();

        //创建客户端
        TransportClient client = new PreBuiltTransportClient(Settings.EMPTY).addTransportAddresses(
                new TransportAddress(InetAddress.getByName("api.kuayet.com"), 9300));



        if (1 == 1) {
            Scanner scanner = new Scanner(System.in);

            while (scanner.hasNext()) {
                //搜索2
                QueryBuilder query = QueryBuilders.boolQuery()
//                .must(QueryBuilders.matchQuery("name", "22"));
                        .must(QueryBuilders.matchQuery("name", scanner.nextLine()));
//                .must(QueryBuilders.rangeQuery("age").lte(50));
                SearchResponse searchResponse = client.prepareSearch(PowerPurchaserProduct.class.getCanonicalName().toLowerCase())
                        .setQuery(query)
                        .setFrom(0).setSize(10)
                        .execute()
                        .actionGet();
                //SearchHits是SearchHit的复数形式，表示这个是一个列表
                SearchHits shs = searchResponse.getHits();
                for (SearchHit hit : shs) {
                    System.out.println(hit.getSourceAsString());

                }

            }


            return;
        }

        Map<String, String> map = new HashMap<>();
        map.put("name", "男士秋季牛仔裤男直筒男裤冬季大码宽松休闲2018新款男装加绒裤子");
        map.put("age", "花花公子贵宾男装男士打底衫秋冬款针织衫上衣修身年白色百搭毛衣");

        //新增
        IndexResponse indexResponse = client.prepareIndex()
                .setIndex(PowerPurchaserProduct.class.getCanonicalName().toLowerCase())
                .setType(PowerPurchaserProduct.class.getSimpleName().toLowerCase())
                .setSource(map)
                .setId("1")
                .execute()
                .actionGet();

        //查询特定
        GetResponse getResponse = client.prepareGet()
                .setIndex(PowerPurchaserProduct.class.getCanonicalName().toLowerCase())
                .setType(PowerPurchaserProduct.class.getSimpleName().toLowerCase())
                .setId("1")
                .execute()
                .actionGet();

        //搜素
        QueryBuilder query = QueryBuilders.queryStringQuery("");
        SearchResponse searchResponse = client.prepareSearch(PowerPurchaserProduct.class.getCanonicalName().toLowerCase())
                .setQuery(query)
                .setFrom(0).setSize(10)
                .execute()
                .actionGet();
        //SearchHits是SearchHit的复数形式，表示这个是一个列表
        SearchHits shs = searchResponse.getHits();
        for (SearchHit hit : shs) {
//            System.out.println(hit.getSourceAsString());
        }

        // 声明where条件
        BoolQueryBuilder qbs = QueryBuilders.boolQuery();

        /**此处使用模糊匹配查询 类比数据库中 like*/
        QueryBuilder qb1 = QueryBuilders.matchPhraseQuery("name", "22");
        BoolQueryBuilder bqb1 = QueryBuilders.boolQuery().must(qb1);
        qbs.must(bqb1);
        SearchRequestBuilder requestBuilder = client.prepareSearch(PowerPurchaserProduct.class.getCanonicalName().toLowerCase()).
                setTypes(PowerPurchaserProduct.class.getSimpleName().toLowerCase());
        requestBuilder.setQuery(qbs);

        SearchResponse response = requestBuilder.setFrom(0).setSize(10).execute().actionGet();
        SearchHits hits = response.getHits();
        for (SearchHit hit : hits) {

//            System.out.println(hit.getSourceAsString());
        }

        if (1 == 1) {
            return;
        }
        //删除
        DeleteResponse delResponse = client.prepareDelete()
                .setIndex(PowerPurchaserProduct.class.getCanonicalName().toLowerCase())
                .setType(PowerPurchaserProduct.class.getSimpleName().toLowerCase())
                .setId("1")
                .execute()
                .actionGet();
        //更新
        Map<String, Object> updateMap = new HashMap<String, Object>();
        updateMap.put("prodId", 2);
        updateMap.put("prodName", "iphone6s");
        updateMap.put("prodDesc", "手机");
        //updateMap.put("catId", 2);

        UpdateResponse updateResponse = client.prepareUpdate()
                .setIndex(PowerPurchaserProduct.class.getCanonicalName().toLowerCase())
                .setType(PowerPurchaserProduct.class.getTypeName().toLowerCase())
                .setDoc(updateMap)
                .setId("1")
                .execute()
                .actionGet();

        //批量插入
        BulkRequestBuilder bulkRequest = client.prepareBulk();

        for (int i = 0; i < 100; i++) {

            map = new HashMap<>();
            map.put("name", "3223");
            map.put("age", "666666");
            IndexRequestBuilder ir = client.prepareIndex()
                    .setIndex(PowerPurchaserProduct.class.getCanonicalName().toLowerCase())
                    .setType("bulk_user")
                    .setId("" + i)
                    .setSource(map);
            bulkRequest.add(ir);
        }

        BulkResponse bulkResponse = bulkRequest.execute().actionGet();
        if (!bulkResponse.hasFailures()) {
            System.out.println("全部插入成功");
        }
        //批量删除
        bulkRequest = client.prepareBulk();
        for (int i = 10; i < 100; i++) {
            DeleteRequestBuilder deleteRequestBuilder = client.prepareDelete()
                    .setIndex(PowerPurchaserProduct.class.getCanonicalName().toLowerCase())
                    .setType("bulk_user")
                    .setId("" + i);
            bulkRequest.add(deleteRequestBuilder);
        }

        bulkResponse = bulkRequest.execute().actionGet();
        if (!bulkResponse.hasFailures()) {
            System.out.println("全部删除成功");
        }
    }

    //创建mapping
    public static void createMapping() throws Exception {
        //创建客户端
        TransportClient client = new PreBuiltTransportClient(Settings.EMPTY).addTransportAddresses(
                new TransportAddress(InetAddress.getByName("api.kuayet.com"), 9300));

        //首先创建index
//        CreateIndexResponse createIndexResponse = client.admin().indices()
//                .prepareCreate(PowerPurchaserProduct.class.getCanonicalName().toLowerCase()).execute().actionGet();
//        System.out.println("createIndexResponse="+createIndexResponse.isAcknowledged());

        PutMappingRequestBuilder mappingRequest = client.admin().indices().preparePutMapping(PowerPurchaserProduct.class.getCanonicalName().toLowerCase())
                .setType(PowerPurchaserProduct.class.getSimpleName().toLowerCase())
                .setSource(createTestModelMapping());
        PutMappingResponse putMappingResponse = mappingRequest.execute().actionGet();
        System.out.println("putMappingResponse=" + putMappingResponse.isAcknowledged());

    }


    private static XContentBuilder createTestModelMapping() throws Exception {
        XContentBuilder mapping = XContentFactory.jsonBuilder()
                .startObject()
                .startObject(PowerPurchaserProduct.class.getSimpleName().toLowerCase())
                .startObject("properties")
                .startObject("id")
                .field("type", "long")
                .field("store", "yes")
                .endObject()
                .startObject("type")
                .field("type", "string")
                .field("index", "not_analyzed")
                .endObject()
                .startObject("catIds")
                .field("type", "integer")
                .endObject()
                .startObject("catName")
                .field("type", "string")
                .endObject()
                .endObject()
                .endObject()
                .endObject();
        return mapping;
    }


}
