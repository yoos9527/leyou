package com.leyou.controller;

import com.leyou.item.pojo.Category;
import com.leyou.service.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("category")
public class CategoryController {
    @Autowired
    private ICategoryService categoryService;
    @GetMapping("list")
    public ResponseEntity<List<Category>> queryByParentId(@RequestParam(value = "pid",defaultValue = "0")long pid){
        if( pid < 0){
            //400参数不合法
            return ResponseEntity.badRequest().build();
        }
        List<Category> categories = categoryService.selectByPid(pid);
        if(CollectionUtils.isEmpty(categories)){
            //404资源未找到
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(categories);
    }
    @GetMapping
    public ResponseEntity<List<String>> queryNameByIds(@RequestParam("ids")List<Long> ids){
        List<String> strings = this.categoryService.queryNamesByIds(ids);
        if(CollectionUtils.isEmpty(strings)){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(strings);
    }
}
