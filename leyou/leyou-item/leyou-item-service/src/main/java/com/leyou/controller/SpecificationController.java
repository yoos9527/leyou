package com.leyou.controller;

import com.leyou.item.pojo.SpecGroup;
import com.leyou.item.pojo.SpecParam;
import com.leyou.service.ISpecificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("spec")
public class SpecificationController {
    @Autowired
    private ISpecificationService specificationService;

    /**
     * 根据分类id查询组信息
     * @param cid
     * @return
     */
    @GetMapping("groups/{cid}")
    public ResponseEntity<List<SpecGroup>> queryGroupsByCid(@PathVariable("cid")Long cid){
        List<SpecGroup> specGroups = this.specificationService.querySepcGroup(cid);
        if(CollectionUtils.isEmpty(specGroups)){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(specGroups);
    }
    /**
     * 根据条件查询规格参数
     */
    @GetMapping("params")
    public ResponseEntity<List<SpecParam>> queryParams
    (@RequestParam(value = "gid",required = false)Long gid,
     @RequestParam(value = "cid",required = false)Long cid,
     @RequestParam(value = "generic",required = false)Boolean generic,
     @RequestParam(value = "searching",required = false)Boolean searching
    ){
        List<SpecParam> params = this.specificationService.querySepcParams(gid,cid,generic,searching);
        if (CollectionUtils.isEmpty(params)){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(params);
    }
}
