package com.leyou.service.impl;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.leyou.common.pojo.PageResult;
import com.leyou.item.pojo.Brand;
import com.leyou.mapper.BrandMapper;
import com.leyou.service.IBrandService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class BrandServiceImpl implements IBrandService {
    @Autowired
    private BrandMapper brandMapper;

    /**
     * 根据查询条件分页并排序查询品牌信息
     * @param key
     * @param page
     * @param rows
     * @param sortBy
     * @param desc
     * @return
     */
    @Override
    public PageResult<Brand> queryBrandByPage(String key, Integer page, Integer rows, String sortBy, Boolean desc) {
        //初始化example对象
        Example example = new Example(Brand.class);
        Example.Criteria criteria = example.createCriteria();
        //根据name进行模糊查询  key
        if(StringUtils.isNotBlank(key)){
            criteria.andLike("name","%"+key+"%").orEqualTo("letter",key);
        }
        //添加分页条件
        PageHelper.startPage(page,rows);
        //添加排序条件
        if (StringUtils.isNotBlank(sortBy)) {
            example.setOrderByClause(sortBy+" "+(desc ?"desc":"asc"));
        }
        List<Brand> brands = this.brandMapper.selectByExample(example);
        System.out.println(brands.toString());
        //包装成pageInfo
        PageInfo<Brand> pageInfo = new PageInfo<Brand>();
        System.out.println(pageInfo.toString());
        PageResult<Brand> pageResult = new PageResult<Brand>();
        pageResult.setTotal(pageInfo.getTotal());
        pageResult.setItems(brands);
        return pageResult;
    }

    /**
     * 保存品牌数据并与分类数据建立关系
     * @param brand
     * @param cids
     */
    @Override
    public void saveBrand(Brand brand, List<Long> cids) {
        boolean falg = this.brandMapper.insertSelective(brand) == 1;
        System.out.println("======="+brand.toString());
        if (falg){
            for (Long cid : cids){
                this.brandMapper.insertCategoryBrand(cid,brand.getId());
            }
        }
    }

    /**
     * 根据分类cid查询品牌信息
     * @param cid
     * @return
     */
    @Override
    public List<Brand> queryBrandByCid(Long cid) {
        return this.brandMapper.queryBrandByCid(cid);
    }

    @Override
    public Brand queryBrandById(Long id) {
        return this.brandMapper.selectByPrimaryKey(id);
    }
}
