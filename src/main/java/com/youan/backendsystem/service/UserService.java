package com.youan.backendsystem.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.youan.backendsystem.model.dto.user.UserQueryRequest;
import com.youan.backendsystem.model.entity.User;
import com.youan.backendsystem.model.vo.LoginUserVO;
import com.youan.backendsystem.model.vo.UserVO;
import org.apache.http.HttpResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 用户服务
 *
 * @author bobochang
 * @from <a href="https://blog.bobochang.work">bobochang's BLOG</a>
 */
public interface UserService extends IService<User> {

    /**
     * 用户注册
     *
     * @param userAccount   用户账户
     * @param userPassword  用户密码
     * @param checkPassword 校验密码
     * @return 新用户 id
     */
    long userRegister(String userAccount, String userPassword, String checkPassword);

    /**
     * 用户登录
     *
     * @param userAccount  用户账户
     * @param userPassword 用户密码
     * @param request
     * @return 脱敏后的用户信息
     */
    LoginUserVO userLogin(String userAccount, String userPassword, HttpServletRequest request);


    /**
     * 获取当前登录用户
     *
     * @param request
     * @return
     */
    User getLoginUser(HttpServletRequest request);

    /**
     * 获取当前登录用户（允许未登录）
     *
     * @param request
     * @return
     */
    User getLoginUserPermitNull(HttpServletRequest request);

    /**
     * 是否为管理员
     *
     * @param request
     * @return
     */
    boolean isAdmin(HttpServletRequest request);

    /**
     * 是否为管理员
     *
     * @param user
     * @return
     */
    boolean isAdmin(User user);

    /**
     * 用户注销
     *
     * @param request
     * @return
     */
    boolean userLogout(HttpServletRequest request);

    /**
     * 获取脱敏的已登录用户信息
     *
     * @return
     */
    LoginUserVO getLoginUserVO(User user);

    /**
     * 获取脱敏的用户信息
     *
     * @param user
     * @return
     */
    UserVO getUserVO(User user);

    /**
     * 获取脱敏的用户信息
     *
     * @param userList
     * @return
     */
    List<UserVO> getUserVO(List<User> userList);

    /**
     * 获取查询条件
     *
     * @param userQueryRequest
     * @return
     */
    QueryWrapper<User> getQueryWrapper(UserQueryRequest userQueryRequest);

    /**
     * 新建用户
     *
     * @param user
     * @return
     */
    boolean saveUserInfo(User user);

    /**
     * 更新用户
     *
     * @param user
     * @return
     */
    boolean updateUserInfo(User user);

    /**
     * 修改用户密码
     *
     * @param user
     * @return
     */
    boolean updateUserPassword(User user);

    /**
     * 导出用户数据
     *
     * @param response
     * @return
     */
    boolean exportUserData(HttpServletResponse response);

    /**
     * 导入用户数据
     *
     * @param file
     * @return
     */
    boolean importUserData(MultipartFile file);
}
