package com.wzlue.chain.web.controller.sys;

import cn.hutool.core.util.ReUtil;
import com.wzlue.chain.chatMsg.service.ChatMessageService;
import com.wzlue.chain.common.annotation.SysLog;
import com.wzlue.chain.common.base.Query;
import com.wzlue.chain.common.base.R;
import com.wzlue.chain.common.utils.Constant;
import com.wzlue.chain.common.utils.PageUtils;
import com.wzlue.chain.common.validator.Assert;
import com.wzlue.chain.common.validator.ValidatorUtils;
import com.wzlue.chain.common.validator.group.AddGroup;
import com.wzlue.chain.common.validator.group.UpdateGroup;
import com.wzlue.chain.company.entity.MerchantCompanyEntity;
import com.wzlue.chain.company.service.MerchantCompanyService;
import com.wzlue.chain.sys.MSdx.ApiDemo4Java;
import com.wzlue.chain.sys.entity.SysUserEntity;
import com.wzlue.chain.sys.form.PasswordForm;
import com.wzlue.chain.sys.service.SysUserRoleService;
import com.wzlue.chain.sys.service.SysUserService;
import org.apache.commons.lang.ArrayUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 系统用户
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2016年10月31日 上午10:40:10
 */
@RestController
@RequestMapping("/sys/user")
public class SysUserController extends AbstractController {
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private SysUserRoleService sysUserRoleService;
    @Autowired
    private ChatMessageService chatMessageService;
    @Autowired
    private MerchantCompanyService merchantCompanyService;

    /**
     * 所有用户列表
     */
    @RequestMapping("/list")
//    @RequiresPermissions("sys:user:list")
    public R list(@RequestParam Map<String, Object> params) {
        //只有超级管理员，才能查看所有管理员列表
        if (getUserId() != Constant.SUPER_ADMIN) {
            params.put("createUserId", getUserId());
        }

        //查询列表数据
        Query query = new Query(params);
        List<SysUserEntity> userList = sysUserService.queryList(query);
        int total = sysUserService.queryTotal(query);

        PageUtils pageUtil = new PageUtils(userList, total, query.getLimit(), query.getPage());

        return R.ok().put("page", pageUtil);
    }

    /**
     * 所有用户列表
     */
    @RequestMapping("/listAllByParams")
//    @RequiresPermissions("sys:user:list")
    public R listAllByParams(@RequestParam Map<String, Object> params) {
        //查询列表数据
        Query query = new Query(params);
        List<SysUserEntity> userList = sysUserService.queryList(query);
        int total = sysUserService.queryTotal(query);

        PageUtils pageUtil = new PageUtils(userList, total, query.getLimit(), query.getPage());

        return R.ok().put("page", pageUtil);
    }

    /**
     * 获取登录的用户信息
     */
    @RequestMapping("/info")
    public R info() {
        SysUserEntity user = getUser();
        //查询公司名称
        if (null != user) {
            if (user.getUserId() == 1) {
                user.setCompanyName("admin");
            }else {
                MerchantCompanyEntity entity = merchantCompanyService.getInfoByUser(user.getUserId());
                if(null != entity)
                    user.setCompanyName(entity.getCompanyName());
                else
                    user.setCompanyName(user.getUsername());

            }

            return R.ok().put("user", user);
        } else {
            return R.error("获取用户信息错误");
        }
    }

    /**
     * 修改登录用户密码
     */
    @SysLog("修改密码")
    @RequestMapping("/password")
    public R password(@RequestBody PasswordForm form) {
        Assert.isBlank(form.getNewPassword(), "新密码不为能空");

        //sha256加密
        String password = new Sha256Hash(form.getPassword(), getUser().getSalt()).toHex();
        //sha256加密
        String newPassword = new Sha256Hash(form.getNewPassword(), getUser().getSalt()).toHex();

        //更新密码
        int count = sysUserService.updatePassword(getUserId(), password, newPassword);
        if (count == 0) {
            return R.error("原密码不正确");
        }

        return R.ok();
    }

    /**
     * 用户信息
     */
    @RequestMapping("/info/{userId}")
//    @RequiresPermissions("sys:user:info")
    public R info(@PathVariable("userId") Long userId) {
        SysUserEntity user = sysUserService.queryObject(userId);

        //获取用户所属的角色列表
        List<Long> roleIdList = sysUserRoleService.queryRoleIdList(userId);
        user.setRoleIdList(roleIdList);

        return R.ok().put("user", user);
    }

    /**
     * 保存用户
     */
    @SysLog("保存用户")
    @RequestMapping("/save")
//    @RequiresPermissions("sys:user:save")
    public R save(@RequestBody SysUserEntity user) {
        ValidatorUtils.validateEntity(user, AddGroup.class);

        user.setCreateUserId(getUserId());
        sysUserService.save(user);
        chatMessageService.createNewIMUser(user.getUsername(),user.getNikename());
        return R.ok();
    }

    /**
     * 注册
     */
    @SysLog("注册")
    @RequestMapping("/register")
    public R register(@RequestBody SysUserEntity user, String smsCode) {
        ValidatorUtils.validateEntity(user, AddGroup.class);

        // 验证短信码
        if(!ApiDemo4Java.yz(user.getMobile(), smsCode))
            return R.error("短信码错误!");
        sysUserService.save(user);
        chatMessageService.createNewIMUser(user.getUsername(),user.getNikename());

        return R.ok();
    }

    @SysLog("注册-发送短信验证码")
    @PostMapping("/sendSMS")
    public R sendSMS(@RequestBody String mobile) {

        String smsCode = null;
        String regex = "^((13[0-9])|(14[5,7,9])|(15([0-3]|[5-9]))|(166)|(17[0,1,3,5,6,7,8])|(18[0-9])|(19[8|9]))\\d{8}$";
        if (!"".equals(mobile) && ReUtil.contains(regex, mobile)) {
            SysUserEntity userEntity = sysUserService.queryByUserName(mobile);
            if (null != userEntity)
                return R.error("手机号码已被注册！");

            ApiDemo4Java.send(mobile);

            return R.ok();
        }

        return R.error();
    }


    /**
     * 修改用户
     */
    @SysLog("修改用户")
    @RequestMapping("/update")
//    @RequiresPermissions("sys:user:update")
    public R update(@RequestBody SysUserEntity user) {
        ValidatorUtils.validateEntity(user, UpdateGroup.class);
        chatMessageService.updateNikeName(user.getUsername(),user.getNikename());
        user.setCreateUserId(getUserId());
        sysUserService.update(user);
        sysUserService.updateCustomerServiceById(user);
        return R.ok();
    }

    /**
     * 删除用户
     */
    @SysLog("删除用户")
    @RequestMapping("/delete")
//    @RequiresPermissions("sys:user:delete")
    public R delete(@RequestBody Long[] userIds) {
        if (ArrayUtils.contains(userIds, 1L)) {
            return R.error("系统管理员不能删除");
        }

        if (ArrayUtils.contains(userIds, getUserId())) {
            return R.error("当前用户不能删除");
        }

        sysUserService.deleteBatch(userIds);

        return R.ok();
    }
}
