package com.maoatao.cas.security.service;

import com.maoatao.cas.web.param.UserParam;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * 自定义用户信息接口 UserDetailsService
 * <p>
 * Customized by {@link org.springframework.security.core.userdetails.UserDetailsService}
 *
 * @author MaoAtao
 * @date 2023-02-28 21:59:35
 */
public interface SecurityUserService {

    /**
     * 通过用户名和用户详细信息
     *
     * @param username 用户名
     * @param details  详细信息(客户端 Id)
     * @return 返回用户信息
     * @throws UsernameNotFoundException 如果找不到用户或用户没有授权权限
     */
    UserDetails getUser(String username, Object details) throws UsernameNotFoundException;

    /**
     * 创建用户
     * <p>
     * 请传入未加密的密码,该方法将自动加密
     *
     * @param param 参数
     * @return 创建成功返回true
     */
    long createUser(UserParam param);

    /**
     * 更新用户
     * <p>
     * 此方法不允许修改密码,传入无效
     *
     * @param param 参数
     * @return 更新成功返回true
     */
    boolean updateUser(UserParam param);

    /**
     * 删除用户
     *
     * @param username 用户名
     * @return 删除成功返回true
     */
    boolean deleteUser(String username, String clientId);

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
     * 存在用户
     *
     * @param username 用户名
     * @param clientId 客户端 Id
     * @return 存在用户返回true
     */
    boolean userExists(String username, String clientId);
}
