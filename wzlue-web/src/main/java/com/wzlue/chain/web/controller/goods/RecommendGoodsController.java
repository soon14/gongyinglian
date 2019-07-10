
package com.wzlue.chain.web.controller.goods;

import com.wzlue.chain.common.base.Query;
import com.wzlue.chain.common.base.R;
import com.wzlue.chain.goods.entity.ItemCategoryEntity;
import com.wzlue.chain.goods.entity.RecommendGoodsEntity;
import com.wzlue.chain.goods.service.ItemCategoryService;
import com.wzlue.chain.goods.service.RecommendGoodsService;
import com.wzlue.chain.offer.entity.GoodsBuyingEntity;
import com.wzlue.chain.offer.entity.GoodsOfferEntity;
import com.wzlue.chain.offer.service.GoodsBuyingService;
import com.wzlue.chain.offer.service.GoodsOfferService;
import com.wzlue.chain.web.controller.sys.AbstractController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 推荐商品表
 *
 * @author
 * @email
 * @date 2018-11-19 19:05:20
 */
@RestController
@RequestMapping("/goods/recommendgoods")
public class RecommendGoodsController extends AbstractController {
    @Autowired
    private RecommendGoodsService recommendGoodsService;

    @Autowired
    private ItemCategoryService itemCategoryService;

    @Autowired
    private GoodsBuyingService goodsBuyingService;

    @Autowired
    private GoodsOfferService goodsOfferService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params) {
        //查询列表数据
        Query query = new Query(params);

        List<RecommendGoodsEntity> recommendGoodsList = recommendGoodsService.queryList(query);
        int total = recommendGoodsService.queryTotal(query);
        for (RecommendGoodsEntity recommend : recommendGoodsList) {
            if (recommend.getPosition() == 0 || recommend.getPosition() == 1) { //期货现货报盘
                if(recommend.getGoodsOfferNo() != null){
                    GoodsOfferEntity goodsOffer = goodsOfferService.queryOfferByNumber(recommend.getGoodsOfferNo());
                    if(goodsOffer != null){
                        recommend.setProductName(goodsOffer.getGoodsName());
                    }
                }
            }else {
                recommend.setProductName(recommend.getGoodsOfferNo());
            }
        }

        return R.page(recommendGoodsList, total);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
//    @RequiresPermissions("goods:recommendgoods:info")
    public R info(@PathVariable("id") Integer id) {
        RecommendGoodsEntity recommendGoods = recommendGoodsService.queryObject(id);

        return R.ok().put("recommendGoods", recommendGoods);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
//    @RequiresPermissions("goods:recommendgoods:save")
    public R save(@RequestBody RecommendGoodsEntity recommendGoods) {
		/*if(recommendGoods.getType() != null){
			//将当前分类的其他推荐置为失效
			recommendGoodsService.updateStatus(recommendGoods);
		}*/
        recommendGoodsService.save(recommendGoods);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
//    @RequiresPermissions("goods:recommendgoods:update")
    public R update(@RequestBody RecommendGoodsEntity recommendGoods) {
        recommendGoodsService.update(recommendGoods);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
//    @RequiresPermissions("goods:recommendgoods:delete")
    public R delete(@RequestBody Integer[] ids) {
        recommendGoodsService.deleteBatch(ids);

        return R.ok();
    }

    /**
     * 查询分类
     */
    @RequestMapping("/getCategory")
    public R getCategory() {
        Map<String, Object> params = new HashMap<String, Object>();
        List<ItemCategoryEntity> itemCategoryList = itemCategoryService.getItemCategory(params);
        return R.ok().put("list", itemCategoryList);
    }

    /**
     * 获取求购报盘列表
     *
     * @return
     */
    @RequestMapping("/getBuyingOffer")
    public R getBuyingOffer() {
        Map<String, Object> params = new HashMap<String, Object>();
        //查询列表数据
        Query query = new Query(params);
        List<GoodsBuyingEntity> goodsBuyingList = goodsBuyingService.getBuyingOffer(query);
        return R.page(goodsBuyingList, goodsBuyingList.size());
    }

    /**
     * 查询同一位置推荐数量
     *
     * @param position
     * @return
     */
    @RequestMapping("/getCountByPosition")
    public R getCountByPosition(String position, String type) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("position", position);
        map.put("type", type);
        int count = recommendGoodsService.getCount(map);
        return R.ok().put("count", count);
    }


    /**
     * 上架
     */
    @RequestMapping("/upper")
    public R upper(@RequestBody Integer[] ids) {
        recommendGoodsService.upper(ids);

        return R.ok();
    }

    /**
     * 下架
     */
    @RequestMapping("/lower")
    public R lower(@RequestBody Integer[] ids) {
        recommendGoodsService.lower(ids);

        return R.ok();
    }


}