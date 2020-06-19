package com.leyou.item.api;

import com.leyou.common.pojo.PageResult;
import com.leyou.item.pojo.Brand;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("brand")
public interface BrandApi {

    /**
     * 根据分类ID查询对应的品牌信息
     */
    @GetMapping("cid/{cid}")
    public List<Brand> queryBrandByCid(@PathVariable("cid")Long cid);

    /**
     * 根据查询条件分页并排序查询品牌信息
     *
     * @return
     */
    @GetMapping("page")
    public PageResult<Brand> queryBrandsByPage(
            @RequestParam(value = "key",required = false) String key,
            @RequestParam(value = "page",defaultValue = "1") Integer page,
            @RequestParam(value = "rows",defaultValue = "5") Integer rows,
            @RequestParam(value = "sortBy",required = false) String sortBy,
            @RequestParam(value = "desc",required = false) Boolean desc
    );


    /**
     * 根据id查询品牌信息
     */
    @GetMapping("{id}")
    public Brand queryBrandById(@PathVariable("id")Long id);
}
