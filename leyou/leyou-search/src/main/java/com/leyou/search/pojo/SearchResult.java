package com.leyou.search.pojo;

import com.leyou.common.pojo.PageResult;
import com.leyou.item.pojo.Brand;

import java.util.List;
import java.util.Map;

public class SearchResult extends PageResult<Goods> {
    //分类
    private List<Map<String,Object>> category;
    //品牌
    private List<Brand> brands;
    //规格参数
    private List<Map<String,Object>> spec;

    public SearchResult() {
    }

    public SearchResult(List<Map<String, Object>> category, List<Brand> brands, List<Map<String, Object>> spec) {
        this.category = category;
        this.brands = brands;
        this.spec = spec;
    }

    public SearchResult(Long total, List<Goods> items, List<Map<String, Object>> category, List<Brand> brands, List<Map<String, Object>> spec) {
        super(total, items);
        this.category = category;
        this.brands = brands;
        this.spec = spec;
    }

    public SearchResult(Long total, Integer totalPage, List<Goods> items, List<Map<String, Object>> category, List<Brand> brands, List<Map<String, Object>> spec) {
        super(total, totalPage, items);
        this.category = category;
        this.brands = brands;
        this.spec = spec;
    }

    public List<Map<String, Object>> getSpec() {
        return spec;
    }

    public void setSpec(List<Map<String, Object>> spec) {
        this.spec = spec;
    }

    public List<Map<String, Object>> getCategory() {
        return category;
    }

    public void setCategory(List<Map<String, Object>> category) {
        this.category = category;
    }

    public List<Brand> getBrands() {
        return brands;
    }

    public void setBrands(List<Brand> brands) {
        this.brands = brands;
    }
}
