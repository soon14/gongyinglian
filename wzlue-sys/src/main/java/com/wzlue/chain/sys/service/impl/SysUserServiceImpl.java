package com.wzlue.chain.sys.service.impl;


import com.wzlue.chain.common.exception.RRException;
import com.wzlue.chain.common.utils.Constant;
import com.wzlue.chain.sys.dao.SysDeptDao;
import com.wzlue.chain.sys.dao.SysUserDao;
import com.wzlue.chain.sys.entity.SysRoleEntity;
import com.wzlue.chain.sys.entity.SysUserEntity;
import com.wzlue.chain.sys.service.SysRoleService;

import com.wzlue.chain.sys.service.SysUserRoleService;
import com.wzlue.chain.sys.service.SysUserService;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;


/**
 * 系统用户
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2016年9月18日 上午9:46:09
 */
@Service("sysUserService")
public class SysUserServiceImpl implements SysUserService {
    @Autowired
    private SysUserDao sysUserDao;
    @Autowired
    private SysUserRoleService sysUserRoleService;
    @Autowired
    private SysRoleService sysRoleService;
    @Autowired
    private SysDeptDao sysDeptDao;


    @Override
    public List<String> queryAllPerms(Long userId) {
        return sysUserDao.queryAllPerms(userId);
    }

    @Override
    public List<Long> queryAllMenuId(Long userId) {
        return sysUserDao.queryAllMenuId(userId);
    }

    @Override
    public SysUserEntity queryByUserName(String username) {
        return sysUserDao.queryByUserName(username);
    }

    @Override
    public SysUserEntity queryObject(Long userId) {
        SysUserEntity user = sysUserDao.queryObject(userId);
        if (null != user.getDepartmentId() && null != sysDeptDao.queryObject(user.getDepartmentId())) {
            user.setDepartmentName(sysDeptDao.queryObject(user.getDepartmentId()).getName());
        }
        return user;
    }

    @Override
    public List<SysUserEntity> queryList(Map<String, Object> map) {
        return sysUserDao.queryList(map);
    }

    @Override
    public int queryTotal(Map<String, Object> map) {
        return sysUserDao.queryTotal(map);
    }

    @Override
    @Transactional
    public void save(SysUserEntity user) {
        user.setCreateTime(new Date());
        //sha256加密
        String salt = RandomStringUtils.randomAlphanumeric(20);
        user.setPassword(new Sha256Hash(user.getPassword(), salt).toHex());
        user.setSalt(salt);
        sysUserDao.save(user);

        //检查角色是否越权
//		checkRole(user);

        //保存用户与角色关系
        sysUserRoleService.saveOrUpdate(user.getUserId(), user.getRoleIdList());
    }

    @Override
    @Transactional
    public void update(SysUserEntity user) {
        if (StringUtils.isBlank(user.getPassword())) {
            user.setPassword(null);
        } else {
            user.setPassword(new Sha256Hash(user.getPassword(), user.getSalt()).toHex());
        }
        sysUserDao.update(user);

        //检查角色是否越权
        checkRole(user);

        //保存用户与角色关系
        sysUserRoleService.saveOrUpdate(user.getUserId(), user.getRoleIdList());
    }

    @Override
    @Transactional
    public void deleteBatch(Long[] userId) {
        sysUserDao.deleteBatch(userId);
    }

    @Override
    public int updatePassword(Long userId, String password, String newPassword) {
        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        map.put("password", password);
        map.put("newPassword", newPassword);
        return sysUserDao.updatePassword(map);
    }

    @Override
    public int updatePasswordByUserName(String userName, String salt, String newPassword) {
        Map<String, Object> map = new HashMap<>();
        map.put("userName", userName);
        map.put("salt", salt);
        map.put("newPassword", newPassword);
        return sysUserDao.updatePasswordByUserName(map);
    }

    @Override
    public SysUserEntity queryOne(String apply) {
        return sysUserDao.queryOne(apply);
    }

    @Override
    public List<SysUserEntity> findUserInOfficeId(Map map) {
        return sysUserDao.queryListNotPage(map);
    }


    /**
     * 检查角色是否越权
     */
    private void checkRole(SysUserEntity user) {
        if (user.getRoleIdList() == null || user.getRoleIdList().size() == 0) {
            return;
        }
        //如果不是超级管理员，则需要判断用户的角色是否自己创建
        if (user.getCreateUserId() == Constant.SUPER_ADMIN) {
            return;
        }

        //查询用户创建的角色列表
        List<Long> roleIdList = sysRoleService.queryRoleIdList(user.getCreateUserId());

        //判断是否越权
        if (!roleIdList.containsAll(user.getRoleIdList())) {
            throw new RRException("新增用户所选角色，不是本人创建");
        }
    }

    @Override
    public void updateDataAuthorizes(SysUserEntity user) {
        sysUserDao.updateDataAuthorizes(user);
    }

    @Override
    public void updateCustomerServiceById(SysUserEntity user) {
        sysUserDao.updateCustomerServiceById(user);
    }

    @Override
    public boolean updateHeader(SysUserEntity user) {
        return sysUserDao.updateHeader(user) > 0;
    }

    @Override
    public void updatePasswordByUserId(Long userId, String newPassword) {
        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        map.put("newPassword", newPassword);
        sysUserDao.updatePasswordByUserId(map);
    }
}
