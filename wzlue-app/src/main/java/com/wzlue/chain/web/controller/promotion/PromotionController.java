
package com.wzlue.chain.web.controller.promotion;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.wzlue.chain.promotion.entity.PromotionEntity;
import com.wzlue.chain.promotion.service.PromotionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import jdk.nashorn.internal.ir.annotations.Ignore;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.wzlue.chain.web.controller.sys.AbstractController;


import com.wzlue.chain.common.utils.PageUtils;
import com.wzlue.chain.common.base.Query;
import com.wzlue.chain.common.base.R;
import springfox.documentation.annotations.ApiIgnore;


/**
 * 促销表
 * 
 * @author 
 * @email 
 * @date 2018-08-27 16:01:49
 */
@RestController
@RequestMapping("/promotion/promotion")
@Api(value = "促销管理")
public class PromotionController extends AbstractController{
	@Autowired
	private PromotionService promotionService;
	
	/**
	 * 列表
	 */
	@GetMapping("/list")
    @ApiOperation(value = "促销列表接口")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "token", value = "token", required = true, dataType = "string"),
            @ApiImplicitParam(paramType = "query", name = "page", value = "页码", defaultValue = "1", dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "limit", value = "一页几条", defaultValue = "10", dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "name", value = "促销标题", dataType = "string")
    })
	public R list(@ApiIgnore @RequestParam Map<String, Object> params){
		//查询列表数据
        Query query = new Query(params);

		List<PromotionEntity> promotionList = promotionService.queryList(query);
		int total = promotionService.queryTotal(query);
		
		return R.page(promotionList,total);
	}
	
	
	/**
	 * 信息
	 */
	@GetMapping("/info/{id}")
//	@RequiresPermissions("promotion:promotion:info")
    @ApiOperation(value = "促销详情接口")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "token", value = "token", required = true, dataType = "string")
    })
	public R info(@PathVariable("id") Integer id){
		PromotionEntity promotion = promotionService.queryObject(id);
		
		return R.ok().put("promotion", promotion);
	}
	
	/**
	 * 保存
	 */
	@PostMapping("/save")
//	@RequiresPermissions("promotion:promotion:save")
    @ApiOperation(value = "促销新增接口")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "token", value = "token", required = true, dataType = "string")
    })
	public R save(@RequestBody PromotionEntity promotion){
        Date currentTime = new Date();
        promotion.setStatus(1);
        promotion.setCreateBy(getUserId().intValue());
        promotion.setCreatedTime(currentTime);
        promotion.setUpdateBy(getUserId());
        promotion.setUpdateTime(currentTime);
        promotionService.save(promotion);
		
		return R.ok();
	}
	
	/**
	 * 修改
	 */
	@PostMapping("/update")
//	@RequiresPermissions("promotion:promotion:update")
    @ApiOperation(value = "促销修改接口")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "token", value = "token", required = true, dataType = "string")
    })
	public R update(@RequestBody PromotionEntity promotion){
		promotion.setUpdateBy(getUserId());
		promotion.setUpdateTime(new Date());
		promotionService.update(promotion);
		
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@PostMapping("/delete")
//	@RequiresPermissions("promotion:promotion:delete")
    @ApiOperation(value = "促销删除接口")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "token", value = "token", required = true, dataType = "string")
    })
	public R delete(@RequestBody Integer[] ids){
		promotionService.deleteBatch(ids);
		
		return R.ok();
	}

    /**
     * 上架商品onSale
     */
    @PostMapping("/onSale")
//    @RequiresPermissions("promotion:promotion:update")
    @ApiOperation(value = "促销上架接口")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "token", value = "token", required = true, dataType = "string")
    })
    public R onSale(@RequestBody Integer[] ids){
        Map<String, Object> map = new HashMap<>();
        map.put("status",0);
        map.put("ids",ids);
        map.put("userId",getUserId());
        promotionService.onSale(map);
        return R.ok();
    }

    /**
     * 下架商品noSale
     */
    @PostMapping("/noSale")
//    @RequiresPermissions("promotion:promotion:update")
    @ApiOperation(value = "促销下架接口")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "token", value = "token", required = true, dataType = "string")
    })
    public R noSale(@RequestBody Integer[] ids){
        Map<String, Object> map = new HashMap<>();
        map.put("status",1);
        map.put("ids",ids);
        map.put("userId",getUserId());
        promotionService.noSale(map);
        return R.ok();
    }
	
}
