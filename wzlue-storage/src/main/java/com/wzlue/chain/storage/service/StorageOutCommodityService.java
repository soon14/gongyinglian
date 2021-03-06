package com.wzlue.chain.storage.service;

import com.wzlue.chain.storage.entity.StorageOutCommodityEntity;

import java.util.List;
import java.util.Map;

/**
 * 仓储出库商品表
 * 
 * @author 
 * @email 
 * @date 2018-09-15 11:17:18
 */
public interface StorageOutCommodityService {
	
	StorageOutCommodityEntity queryObject(Long id);
	
	List<StorageOutCommodityEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(StorageOutCommodityEntity storageOutCommodity);
	
	void update(StorageOutCommodityEntity storageOutCommodity);
	
	void delete(Long id);
	
	void deleteBatch(Long[] ids);

    Long queryOrderCommodityId(Long id);
}
