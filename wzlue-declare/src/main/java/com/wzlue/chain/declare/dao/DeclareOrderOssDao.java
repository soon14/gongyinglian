package com.wzlue.chain.declare.dao;

import com.wzlue.chain.declare.entity.DeclareOrderOssEntity;
import com.wzlue.chain.common.base.BaseDao;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 报关文件上传
 * 
 * @author 
 * @email 
 * @date 2018-09-05 18:06:05
 */
@Mapper
public interface DeclareOrderOssDao extends BaseDao<DeclareOrderOssEntity> {

    List<DeclareOrderOssEntity> getListById(Integer orderId);

    void deleteByOrderId(Integer orderId);
}