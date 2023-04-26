package com.maoatao.cas.core.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.maoatao.cas.core.bean.param.clientuser.ClientUserQueryParam;
import com.maoatao.cas.core.bean.param.clientuser.ClientUserSaveParam;
import com.maoatao.cas.core.bean.param.clientuser.ClientUserUpdateParam;
import com.maoatao.cas.core.bean.vo.ClientUserVO;
import com.maoatao.cas.core.bean.entity.ClientUserEntity;
import com.maoatao.cas.security.bean.ClientDetails;
import com.maoatao.daedalus.data.service.DaedalusService;
import java.util.Map;
import java.util.Set;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * 用户
 *
 * @author MaoAtao
 * @date 2022-03-11 16:13:35
 */
public interface ClientUserService extends DaedalusService<ClientUserEntity> {

    /**
     * 分页
     *
     * @param param 参数
     * @return 分页
     */
    Page<ClientUserVO> page(ClientUserQueryParam param);

    /**
     * 通过id查询
     *
     * @param id CAS 客户端用户id
     * @return CAS 客户端用户
     */
    ClientUserVO details(Long id);

    /**
     * 通过用户名和用户详细信息
     * <p>
     * 与{@link org.springframework.security.core.userdetails.UserDetailsService#loadUserByUsername}不同,需要客户端id
     *
     * @param username      用户名
     * @param clientDetails 客户端作用域
     * @return 返回用户信息
     * @throws UsernameNotFoundException 如果找不到用户或用户没有授权权限
     */
    UserDetails getUserDetails(String username, ClientDetails clientDetails) throws UsernameNotFoundException;

    /**
     * 新增
     *
     * @param param 参数
     * @return 新增成功返回主键 id
     */
    long save(ClientUserSaveParam param);

    /**
     * 更新
     *
     * @param param 参数
     * @return 更新成功返回true
     */
    boolean update(ClientUserUpdateParam param);

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
    ClientUserEntity getByNameAndClient(String name, String clientId);

    /**
     * 构建权限
     *
     * @param openId        用户开放id
     * @param clientDetails 请求作用域
     * @return 权限
     */
    Map<String, Set<String>> buildPermissions(String openId, ClientDetails clientDetails);
}
