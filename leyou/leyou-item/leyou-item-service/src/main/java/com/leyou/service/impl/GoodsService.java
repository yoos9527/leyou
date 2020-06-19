package com.leyou.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.leyou.common.pojo.PageResult;
import com.leyou.item.bo.SpuBo;
import com.leyou.item.pojo.Sku;
import com.leyou.item.pojo.Spu;
import com.leyou.item.pojo.SpuDetail;
import com.leyou.item.pojo.Stock;
import com.leyou.mapper.*;
import com.leyou.service.ICategoryService;
import com.leyou.service.IGoodsService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
public class GoodsService implements IGoodsService {
    @Autowired
    private SpuMapper spuMapper;
    @Autowired
    private ICategoryService categoryService;
    @Autowired
    private BrandMapper brandMapper;
    @Autowired
    private SkuMapper skuMapper;
    @Autowired
    StockMapper stockMapper;
    @Autowired
    private SpuDetailMapper spuDetailMapper;
    @Override
    public PageResult<SpuBo> querySpuBoByPage(String key, Boolean saleable, Integer page, Integer rows) {
        //搜索条件
        Example exampl = new Example(Spu.class);
        Example.Criteria criteria = exampl.createCriteria();
        if(StringUtils.isNotBlank(key)){
            //根据标题模糊查询
            criteria.andLike("title","%"+key+"%");
        }
        if (saleable != null){
            //根据商品状态查询
            criteria.andEqualTo("saleable",saleable);
        }
        //分页条件
        PageHelper.startPage(page,rows);
        //执行查询
        List<Spu> spus = spuMapper.selectByExample(exampl);
        PageInfo<Spu> pageInfo = new PageInfo<Spu>();

        List<SpuBo> spuBos = new ArrayList<SpuBo>();
        spus.forEach(spu -> {
            SpuBo spuBo = new SpuBo();
            //copy共同属性的值到新的对象
            BeanUtils.copyProperties(spu,spuBo);
            //查询分类名称
            List<String> names = this.categoryService.queryNamesByIds(Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3()));
            spuBo.setCname(StringUtils.join(names, "--"));
            // 查询品牌的名称
            spuBo.setBname(this.brandMapper.selectByPrimaryKey(spu.getBrandId()).getName());
            spuBos.add(spuBo);
            }
        );
        return new PageResult<>(pageInfo.getTotal(),spuBos);
    }

    @Override
    @Transactional
    public void saveGoods(SpuBo spuBo) {
        spuBo.setId(null);
        spuBo.setSaleable(true);
        spuBo.setValid(true);
        spuBo.setCreateTime(new Date());
        spuBo.setLastUpdateTime(spuBo.getCreateTime());
        //保存信息到spu表
        this.spuMapper.insertSelective(spuBo);
        //新增spuDetail
        SpuDetail spuDetail = new SpuDetail();
        spuDetail.setSpuId(spuBo.getId());
        saveSkuAndStock(spuBo);
    }

    @Override
    public List<Sku> querySkuBySpuId(Long spuId) {
        Sku recode = new Sku();
        recode.setSpuId(spuId);
        return this.skuMapper.select(recode);
    }

    @Override
    public SpuDetail querySpuDetailBySpuId(Long id) {
        return this.spuDetailMapper.selectByPrimaryKey(id);
    }


    private void saveSkuAndStock(SpuBo spuBo){
        spuBo.getSkus().forEach(sku -> {
            //新增sku
            sku.setSpuId(spuBo.getId());
            sku.setCreateTime(new Date());
            sku.setLastUpdateTime(sku.getCreateTime());
            this.skuMapper.insertSelective(sku);
            // 新增库存
            Stock stock = new Stock();
            stock.setSkuId(sku.getId());
            stock.setStock(sku.getStock());
            this.stockMapper.insertSelective(stock);
        });
    }
}
