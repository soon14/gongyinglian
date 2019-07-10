package com.wzlue.chain.traceback.service;

import com.wzlue.chain.common.base.Query;
import com.wzlue.chain.traceback.entity.TraceBackEntity;

import java.util.List;
import java.util.Map;

/**
 * @author Administrator
 */
public interface TraceBackService {
    /**
     * 查询所有合同
     *
     * @param params
     * @return 所有合同
     */
    List<TraceBackEntity> queryList(Map<String, Object> params);

    /**
     * 获取总数量
     * @param params
     * @return
     */
    Integer queryTotal(Map<String, Object> params);

    /**
     * 查询追溯详情
     * @param params
     * @return
     */
    Object queryInfo(Map<String, Object> params);

    /**
     * 获取当前合同
     * @param params
     * @return
     */
    Map<String,Object> queryContract(Map<String, Object> params);

    Map<String,Object> querySourceOrder(Map<String, Object> params);

    Map<String,Object> queryFatherSourceOrder(Map<String, Object> params);

}