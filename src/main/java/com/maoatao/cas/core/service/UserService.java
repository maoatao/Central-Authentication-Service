package com.maoatao.cas.core.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.maoatao.cas.core.entity.UserEntity;
import com.maoatao.cas.core.param.UserParam;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * 用户
 *
 * @author MaoAtao
 * @date 2022-03-11 16:13:35
 */
public interface UserService extends IService<UserEntity> {


    /**
     * 通过用户名和用户详细信息
     *
     * @param username 用户名
     * @param clientId 客户端 Id
     * @return 返回用户信息
     * @throws UsernameNotFoundException 如果找不到用户或用户没有授权权限
     */
    UserDetails getUserDetails(String username, String clientId) throws UsernameNotFoundException;

    /**
     * 分页
     *
     * @param param 参数
     * @return 分页
     */
    Page<UserEntity> getPage(UserParam param);

    /**
     * 新增
     *
     * @param param 参数
     * @return 新增成功返回true
     */
    long save(UserParam param);

    /**
     * 更新
     *
     * @param param 参数
     * @return 更新成功返回true
     */
    boolean update(UserParam param);

    /**
     * 删除
     *
     * @param id 主键id
     * @return 删除成功返回true
     */
    boolean remove(Long id);

    /**
     * 删除用户
     *
     * @param username 用户名
     * @param clientId 客户端 Id
     * @return 删除成功返回true
     */
    boolean remove(String username, String clientId);

    /**
     * 修改密码
     *
     * @param username    用户名
     * @param clientId    客户端 Id
     * @param oldPassword 旧密码(未加密)
     * @param newPassword 新密码(加密的密码,该方法将自动加密)
     * @return 修改成功返回true
     */
    boolean changePassword(String username, String clientId, String oldPassword, String newPassword);

    /**
     * 存在
     *
     * @param username 用户名
     * @param clientId 客户端 Id
     * @return 存在用户返回true
     */
    boolean exists(String username, String clientId);

    /**
     * 按用户名称和客户端 id 查询用户
     *
     * @param name     名称
     * @param clientId 客户端 id
     * @return 用户不存在返回 null
     */
    UserEntity getByNameAndClient(String name, String clientId);
}
