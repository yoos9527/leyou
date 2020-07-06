package com.leyou.elasticsearch;

import com.leyou.common.pojo.PageResult;
import com.leyou.item.bo.SpuBo;
import com.leyou.search.client.GoodsClient;
import com.leyou.search.pojo.Goods;
import com.leyou.search.pojo.SearchRequest;
import com.leyou.search.pojo.SearchResult;
import com.leyou.search.repository.GoodsRepository;
import com.leyou.search.service.SearchService;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.test.context.junit4.SpringRunner;

import javax.sound.midi.SoundbankResource;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ElasicsearchTest {
    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;
    @Autowired
    private GoodsRepository goodsRepository;
    @Autowired
    private SearchService searchService;
    @Autowired
    private GoodsClient goodsClient;

    @Test
    public void test(){
        this.elasticsearchTemplate.createIndex(Goods.class);
        this.elasticsearchTemplate.putMapping(Goods.class);
        Integer page = 1;
        Integer rows = 100;
        do {
            System.out.println("循环的次数");
            //分页查询spu获取分页结果集
            PageResult<SpuBo> result = this.goodsClient.querySpuBoByPage(null, null, page, rows);
            //获取当前数据
            List<SpuBo> items = result.getItems();
            //处理list<spuBo> ==> List<Goods>
            List<Goods> goodsList = items.stream().map(spuBo -> {
                try {
                    return this.searchService.buildGoods(spuBo);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }).collect(Collectors.toList());
            //执行新增数据方法
            System.out.println(goodsList.toString());
            goodsRepository.saveAll(goodsList);
            rows = items.size();
            page++;
        }while (rows == 100);
    }
    @Test
    public void test1(){
        SearchRequest request = new SearchRequest();
        request.setKey("电脑");
        int lo = 326;
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
        QueryBuilder basicBuild = QueryBuilders.matchQuery("all", request.getKey()).operator(Operator.AND);
        List<Map<String, Object>> paramAggResult = this.searchService.getParamAggResult((long) 76, basicBuild);
        paramAggResult.forEach(map ->{
            String s = map.toString();
            System.out.println(s);
        });
    }

}
