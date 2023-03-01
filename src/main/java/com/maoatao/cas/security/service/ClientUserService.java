package com.maoatao.cas.security.service;

import com.maoatao.cas.security.bean.CustomUserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * 自定义用户信息接口 UserDetailsService
 * <p>
 * Customized by {@link org.springframework.security.core.userdetails.UserDetailsService}
 *
 * @author MaoAtao
 * @date 2023-02-28 21:59:35
 */
public interface ClientUserService {

    /**
     * 通过用户名和用户详细信息
     *
     * @param username 用户名
     * @param details  详细信息(客户端 Id)
     * @return 返回用户信息
     * @throws UsernameNotFoundException 如果找不到用户或用户没有授权权限
     */
    CustomUserDetails getUser(String username, Object details) throws UsernameNotFoundException;

    /**
     * 创建用户
     *
     * @param userDetails 用户详情
     * @return 创建成功返回true
     */
    long createUser(CustomUserDetails userDetails);

    /**
     * 更新用户
     *
     * @param userDetails 用户详情
     * @return 更新成功返回true
     */
    boolean updateUser(CustomUserDetails userDetails);

    /**
     * 删除用户
     *
     * @param username 用户名
     * @return 删除成功返回true
     */
    boolean deleteUser(String username);

    /**
     * 修改密码
     *
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     * @return 修改成功返回true
     */
    boolean changePassword(String oldPassword, String newPassword);

    /**
     * 存在用户
     *
     * @param username 用户名
     * @param clientId 客户端 Id
     * @return 存在用户返回true
     */
    boolean userExists(String username, String clientId);
}
