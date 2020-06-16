package com.leyou.service;

import com.leyou.item.pojo.SpecGroup;
import com.leyou.item.pojo.SpecParam;

import java.util.List;

public interface ISpecificationService {
    //根据组分类d查询信息
    List<SpecGroup> querySepcGroup(Long cid);
    //根据组id查询规格参数
    List<SpecParam> querySepcParams(Long gid,Long cid,Boolean generic,Boolean searching);
}
