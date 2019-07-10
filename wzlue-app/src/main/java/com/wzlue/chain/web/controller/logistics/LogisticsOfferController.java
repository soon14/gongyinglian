
package com.wzlue.chain.web.controller.logistics;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.wzlue.chain.logistics.entity.LogisticsOfferAddressEntity;
import com.wzlue.chain.logistics.service.LogisticsOfferAddressService;
import com.wzlue.chain.sys.entity.SysUserEntity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.wzlue.chain.web.controller.sys.AbstractController;

import com.wzlue.chain.logistics.entity.LogisticsOfferEntity;
import com.wzlue.chain.logistics.service.LogisticsOfferService;
import com.wzlue.chain.common.utils.PageUtils;
import com.wzlue.chain.common.base.Query;
import com.wzlue.chain.common.base.R;


/**
 * 商品库物流报盘
 *
 * @author pengyong
 * @email sunlightcs@gmail.com
 * @date 2018-08-16 10:19:13
 */
@RestController
@RequestMapping("/logistics/logisticsoffer")
@Api("物流报盘")
public class LogisticsOfferController extends AbstractController {
    @Autowired
    private LogisticsOfferService logisticsOfferService;
    @Autowired
    private LogisticsOfferAddressService logisticsOfferAddressService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params) {
        //查询列表数据
        Query query = new Query(params);

        List<LogisticsOfferEntity> logisticsOfferList = logisticsOfferService.queryList(query);
        int total = logisticsOfferService.queryTotal(query);

        return R.page(logisticsOfferList, total);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("logistics:logisticsoffer:info")
    public R info(@PathVariable("id") Long id) {
        LogisticsOfferEntity logisticsOffer = logisticsOfferService.queryObject(id);
        List<LogisticsOfferAddressEntity> logisticsOfferAddressList = logisticsOfferAddressService.queryByLogisticsId(Math.toIntExact(logisticsOffer.getId()));

        R r = R.ok();
        r.put("logisticsOffer", logisticsOffer);
        r.put("logisticsOfferAddressList", logisticsOfferAddressList);
        return r;
    }

    /**
     * 保存
     */
    @PostMapping("/save")
//    @RequiresPermissions("logistics:logisticsoffer:save")
    @ApiOperation(value="物流报盘新增")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "token", value = "token", required = true, dataType = "string")
    })
    public R save(@RequestBody LogisticsOfferEntity logisticsOffer) {

        SysUserEntity user = getUser();
        Date date =new Date();
        logisticsOffer.setDeptId(null);//部门id
        logisticsOffer.setDelFlag(0);
        logisticsOffer.setCreateBy(Math.toIntExact(getUserId()));
        logisticsOffer.setUpdateBy(Math.toIntExact(getUserId()));
        logisticsOffer.setCreatDate(date);
        logisticsOffer.setUpdateDate(date);
        logisticsOfferService.save(logisticsOffer);
        //循环展示路线集合列表
        List<LogisticsOfferAddressEntity> logisticsOfferAddressEntityList = logisticsOffer.getLogisticsOfferAddressList();

        for (LogisticsOfferAddressEntity logisticsOfferAddressEntity : logisticsOfferAddressEntityList) {
            logisticsOfferAddressEntity.setLogisticsId(Math.toIntExact(logisticsOffer.getId()));
        }
        //判断运输线路是否传入
        if (logisticsOfferAddressEntityList.size() > 0) {
            logisticsOfferAddressService.saveList(logisticsOfferAddressEntityList);
        }

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("logistics:logisticsoffer:update")
    public R update(@RequestBody LogisticsOfferEntity logisticsOffer) {

        logisticsOfferService.update(logisticsOffer);

        List<LogisticsOfferAddressEntity> logisticsOfferAddressEntityList = logisticsOffer.getLogisticsOfferAddressList();
        //先全部删掉
        logisticsOfferAddressService.deleteBatchs(Math.toIntExact(logisticsOffer.getId()));
        //再判断有无重新添加线路
        if (logisticsOfferAddressEntityList != null && logisticsOfferAddressEntityList.size() != 0) {
            for (LogisticsOfferAddressEntity logisticsOfferAddressEntity : logisticsOfferAddressEntityList) {
                logisticsOfferAddressEntity.setLogisticsId(Math.toIntExact(logisticsOffer.getId()));
            }
            logisticsOfferAddressService.saveList(logisticsOfferAddressEntityList);
        }

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("logistics:logisticsoffer:delete")
    public R delete(@RequestBody Long[] ids) {
        logisticsOfferService.deleteBatch(ids);

        return R.ok();
    }

    /**
     * 物流订单中调用此查询接口
     */
    @RequestMapping("/queryByCompanyId")
    public R queryByCompanyId(@RequestParam Map<String, Object> params){
        List<LogisticsOfferEntity> list = logisticsOfferService.queryList(params);

        return R.ok().put("list",list);
    }

    /**
     * 上架
     */
    @RequestMapping("/up")
    @RequiresPermissions("logistics:logisticsoffer:update")
    public R up(@RequestBody Long[] ids) {
        Map<String, Object> map = new HashMap<>(16);
        map.put("status", 0);
        map.put("ids", ids);
        logisticsOfferService.updateStatus(map);

        return R.ok();
    }

    /**
     * 下架
     */
    @RequestMapping("/down")
    @RequiresPermissions("logistics:logisticsoffer:update")
    public R down(@RequestBody Long[] ids) {
        Map<String, Object> map = new HashMap<>(16);
        map.put("status", 1);
        map.put("ids", ids);
        logisticsOfferService.updateStatus(map);

        return R.ok();
    }


}