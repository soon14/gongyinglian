package com.wzlue.chain.goods.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import com.wzlue.chain.goods.dao.RecommendGoodsDao;
import com.wzlue.chain.goods.entity.RecommendGoodsEntity;
import com.wzlue.chain.goods.service.RecommendGoodsService;



@Service("recommendGoodsService")
public class RecommendGoodsServiceImpl implements RecommendGoodsService {
	@Autowired
	private RecommendGoodsDao recommendGoodsDao;
	
	@Override
	public RecommendGoodsEntity queryObject(Integer id){
		return recommendGoodsDao.queryObject(id);
	}
	
	@Override
	public List<RecommendGoodsEntity> queryList(Map<String, Object> map){
		return recommendGoodsDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return recommendGoodsDao.queryTotal(map);
	}
	
	@Override
	public void save(RecommendGoodsEntity recommendGoods){
		recommendGoodsDao.save(recommendGoods);
	}
	
	@Override
	public void update(RecommendGoodsEntity recommendGoods){
		recommendGoodsDao.update(recommendGoods);
	}
	
	@Override
	public void delete(Integer id){
		recommendGoodsDao.delete(id);
	}

	@Override
	public void deleteBatch(Integer[] ids){
		recommendGoodsDao.deleteBatch(ids);
	}

	@Override
	public void updateStatus(RecommendGoodsEntity recommendGoods) {
		recommendGoodsDao.updateStatus(recommendGoods);
	}

	@Override
	public int getCount(Map<String, Object> map) {
		return recommendGoodsDao.getCount(map);
	}

	@Override
	public void upper(Integer[] ids){
		recommendGoodsDao.upper(ids);
	}
    @Override
    public void lower(Integer[] ids){
        recommendGoodsDao.lower(ids);
    }
}