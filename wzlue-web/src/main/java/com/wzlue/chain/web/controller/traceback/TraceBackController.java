package com.wzlue.chain.web.controller.traceback;

import com.wzlue.chain.agent.entity.AgentOrderEntity;
import com.wzlue.chain.common.base.Query;
import com.wzlue.chain.common.base.R;
import com.wzlue.chain.declare.entity.DeclareOrderEntity;
import com.wzlue.chain.logistics.entity.LogisticsOrderEntity;
import com.wzlue.chain.order.entity.GoodsOrderEntity;
import com.wzlue.chain.storage.entity.OrderEntity;
import com.wzlue.chain.traceback.entity.TraceBackEntity;
import com.wzlue.chain.traceback.service.TraceBackService;
import com.wzlue.chain.web.controller.sys.AbstractController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 追溯Controller
 *
 * @author Administrator
 * @date 2018/09/29
 */
@RestController
@RequestMapping("/traceBack")
public class TraceBackController extends AbstractController {
    @Autowired
    private TraceBackService traceBackService;

    @RequestMapping("/list")
    public R queryList(@RequestParam Map<String, Object> params) {
        params.put("companyId", getUser().getCompanyId());
        Query query = new Query(params);
        List<TraceBackEntity> list = traceBackService.queryList(query);
        return R.page(list, traceBackService.queryTotal(query));
    }

    @RequestMapping("/queryInfo")
    public R queryInfo(@RequestBody Map<String, Object> params) {
        R r = R.ok();
        Integer orderType = (Integer) params.get("orderType");
        //获取公司id
        params.put("companyId", getUser().getCompanyId());
        //根据当前订单号，查询该订单下面的所有来源是该订单号的订单详情
        Map<String, Object> fatherOrder = traceBackService.queryFatherSourceOrder(params);
        r.put("fatherOrder", fatherOrder);
        r.put("orderType", orderType);
        return r;
    }

    //报检号追溯
    @RequestMapping("/list2")
    public R queryList2(@RequestParam Map<String, Object> params) {
        params.put("companyId", getUser().getCompanyId());
        Query query = new Query(params);
        List<TraceBackEntity> list = traceBackService.queryList(query);
        return R.page(list, traceBackService.queryTotal(query));
    }

}
