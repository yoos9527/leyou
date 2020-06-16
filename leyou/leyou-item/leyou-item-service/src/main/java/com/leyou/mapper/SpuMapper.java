package com.leyou.mapper;

import com.leyou.item.pojo.Spu;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface SpuMapper extends Mapper<Spu> {
    @Select("select * from tb_spu where title like '%',#{key},'%'")
    List<Spu> querySpuByPage(String key);
}
