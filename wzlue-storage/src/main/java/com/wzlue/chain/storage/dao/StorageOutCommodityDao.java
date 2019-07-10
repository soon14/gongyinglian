package com.wzlue.chain.storage.dao;

import com.wzlue.chain.storage.entity.StorageOutCommodityEntity;
import com.wzlue.chain.common.base.BaseDao;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 仓储出库商品表
 * 
 * @author 
 * @email 
 * @date 2018-09-15 11:17:18
 */
@Mapper
public interface StorageOutCommodityDao extends BaseDao<StorageOutCommodityEntity> {

    void deleteByOutId(Long outId);

    void saveList(List<StorageOutCommodityEntity> commodityEntityList);

    void updateList(List<StorageOutCommodityEntity> commodityEntityList);

    Long queryOrderCommodityId(Long id);
}