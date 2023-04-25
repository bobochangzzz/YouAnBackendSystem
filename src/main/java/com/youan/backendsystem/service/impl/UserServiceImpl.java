package com.youan.backendsystem.service.impl;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.youan.backendsystem.common.ErrorCode;
import com.youan.backendsystem.exception.BusinessException;
import com.youan.backendsystem.listener.UserListener;
import com.youan.backendsystem.mapper.UserMapper;
import com.youan.backendsystem.model.dto.user.UserQueryRequest;
import com.youan.backendsystem.model.entity.Department;
import com.youan.backendsystem.model.entity.User;
import com.youan.backendsystem.model.entity.UserDepartment;
import com.youan.backendsystem.model.enums.UserRoleEnum;
import com.youan.backendsystem.model.vo.LoginUserVO;
import com.youan.backendsystem.model.vo.UserExcelVO;
import com.youan.backendsystem.model.vo.UserVO;
import com.youan.backendsystem.service.DepartmentService;
import com.youan.backendsystem.service.UserDepartmentService;
import com.youan.backendsystem.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.youan.backendsystem.constant.UserConstant.USER_LOGIN_STATE;

/**
 * 用户服务实现
 *
 * @author bobochang
 * @from <a href="https://blog.bobochang.work">bobochang's BLOG</a>
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Resource
    private UserDepartmentService userDepartmentService;
    @Resource
    private DepartmentService departmentService;


    /**
     * 盐值，混淆密码
     */
    private static final String SALT = "bobochang";

    @Override
    public long userRegister(String userAccount, String userPassword, String checkPassword) {
        // 1. 校验
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数为空");
        }
        if (userAccount.length() < 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户账号过短");
        }
        if (userPassword.length() < 8 || checkPassword.length() < 8) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户密码过短");
        }
        // 密码和校验密码相同
        if (!userPassword.equals(checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "两次输入的密码不一致");
        }
        synchronized (userAccount.intern()) {
            // 账户不能重复
            QueryWrapper<User> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("userAccount", userAccount);
            long count = this.baseMapper.selectCount(queryWrapper);
            if (count > 0) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号重复");
            }
            // 2. 加密
            String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
            // 3. 插入数据
            User user = new User();
            user.setUserAccount(userAccount);
            user.setUserPassword(encryptPassword);
            boolean saveResult = this.save(user);
            if (!saveResult) {
                throw new BusinessException(ErrorCode.SYSTEM_ERROR, "注册失败，数据库错误");
            }
            return user.getId();
        }
    }

    @Override
    public LoginUserVO userLogin(String userAccount, String userPassword, HttpServletRequest request) {
        // 1. 校验
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数为空");
        }
        if (userAccount.length() < 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号错误");
        }
        if (userPassword.length() < 8) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "密码错误");
        }
        // 2. 加密
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
        // 查询用户是否存在
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        queryWrapper.eq("userPassword", encryptPassword);
        User user = this.baseMapper.selectOne(queryWrapper);
        // 用户不存在
        if (user == null) {
            log.info("user login failed, userAccount cannot match userPassword");
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户不存在或密码错误");
        }
        // 3. 记录用户的登录态
        request.getSession().setAttribute(USER_LOGIN_STATE, user);
        return this.getLoginUserVO(user);
    }

    /**
     * 获取当前登录用户
     *
     * @param request
     * @return
     */
    @Override
    public User getLoginUser(HttpServletRequest request) {
        // 先判断是否已登录
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User currentUser = (User) userObj;
        if (currentUser == null || currentUser.getId() == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        // 从数据库查询（追求性能的话可以注释，直接走缓存）
        long userId = currentUser.getId();
        currentUser = this.getById(userId);
        if (currentUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        return currentUser;
    }

    /**
     * 获取当前登录用户（允许未登录）
     *
     * @param request
     * @return
     */
    @Override
    public User getLoginUserPermitNull(HttpServletRequest request) {
        // 先判断是否已登录
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User currentUser = (User) userObj;
        if (currentUser == null || currentUser.getId() == null) {
            return null;
        }
        // 从数据库查询（追求性能的话可以注释，直接走缓存）
        long userId = currentUser.getId();
        return this.getById(userId);
    }

    /**
     * 是否为管理员
     *
     * @param request
     * @return
     */
    @Override
    public boolean isAdmin(HttpServletRequest request) {
        // 仅管理员可查询
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User user = (User) userObj;
        return isAdmin(user);
    }

    @Override
    public boolean isAdmin(User user) {
        return user != null && UserRoleEnum.ADMIN.getValue().equals(user.getUserRoleName());
    }

    /**
     * 用户注销
     *
     * @param request
     */
    @Override
    public boolean userLogout(HttpServletRequest request) {
        if (request.getSession().getAttribute(USER_LOGIN_STATE) == null) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "未登录");
        }
        // 移除登录态
        request.getSession().removeAttribute(USER_LOGIN_STATE);
        return true;
    }

    @Override
    public LoginUserVO getLoginUserVO(User user) {
        if (user == null) {
            return null;
        }
        LoginUserVO loginUserVO = new LoginUserVO();
        BeanUtils.copyProperties(user, loginUserVO);
        return loginUserVO;
    }

    @Override
    public UserVO getUserVO(User user) {
        if (user == null) {
            return null;
        }
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        QueryWrapper<Department> departmentQueryWrapper = new QueryWrapper<Department>();
        userVO.setDepartmentName(
                departmentService.getOne(
                                departmentQueryWrapper.eq(user.getDepartmentId() != 0L, "id", user.getDepartmentId()))
                        .getDepartmentName());
        return userVO;
    }

    @Override
    public List<UserVO> getUserVO(List<User> userList) {
        if (CollectionUtils.isEmpty(userList)) {
            return new ArrayList<>();
        }
        return userList.stream().map(this::getUserVO).collect(Collectors.toList());
    }

    @Override
    public QueryWrapper<User> getQueryWrapper(UserQueryRequest userQueryRequest) {
        if (userQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数为空");
        }
        Long id = userQueryRequest.getId();
        String userName = userQueryRequest.getUserName();
        String phone = userQueryRequest.getPhone();
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(id != null, "id", id);
        queryWrapper.eq(StringUtils.isNotBlank(phone), "phone", phone);
        queryWrapper.like(StringUtils.isNotBlank(userName), "userName", userName);
        return queryWrapper;
    }

    @Override
    public boolean saveUserInfo(User user) {
        String userAccount = user.getUserAccount();
        String userPassword = user.getUserPassword();
        // 1. 校验
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数为空");
        }
        if (userAccount.length() < 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户账号过短");
        }
        if (userPassword.length() < 8) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户密码过短");
        }
        synchronized (userAccount.intern()) {
            if (checkAccount(userAccount)) {
                // 2. 加密
                String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
                // 3. 插入数据
                user.setUserPassword(encryptPassword);
                boolean result1 = save(user);
                if (!result1) {
                    throw new BusinessException(ErrorCode.SYSTEM_ERROR, "添加失败，数据库错误");
                }
                // 保存用户到用户部门关系表中
                Long userId = user.getId();
                Long departmentId = user.getDepartmentId();
                UserDepartment userDepartment = new UserDepartment();
                userDepartment.setUserId(userId);
                userDepartment.setDepartmentId(departmentId);
                boolean result2 = userDepartmentService.save(userDepartment);
                return result2;
            }
            return false;
        }
    }

    /**
     * 校验账号是否重复
     *
     * @param userAccount
     */
    private boolean checkAccount(String userAccount) {
        // 账户不能重复
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        long count = this.baseMapper.selectCount(queryWrapper);
        if (count > 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号重复");
        }
        return true;
    }

    @Override
    public boolean updateUserInfo(User user) {
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("id", user.getId());
        User oldUser = getOne(userQueryWrapper);
        // 更新用户部门关系表
        // 判断当前用户所属部门是否发生改变
        boolean result1 = true;
        QueryWrapper<UserDepartment> userDepartmentQueryWrapper = new QueryWrapper<>();
        if (user.getDepartmentId() != oldUser.getDepartmentId()) {
            userDepartmentQueryWrapper.eq("userId", oldUser.getId());
            userDepartmentQueryWrapper.eq("departmentId", oldUser.getDepartmentId());
            UserDepartment oldUserDepartment = userDepartmentService.getOne(userDepartmentQueryWrapper);
            oldUserDepartment.setDepartmentId(user.getDepartmentId());
            result1 = userDepartmentService.updateById(oldUserDepartment);
        }
        boolean result2 = updateById(user);
        return result1 && result2;
    }

    @Override
    public boolean updateUserPassword(User user) {
        // 拿到需要新加密的密码 进行加密存放数据库
        user.setUserPassword(DigestUtils.md5DigestAsHex((SALT + user.getUserPassword()).getBytes()));
        return updateById(user);
    }

    @Override
    public boolean exportUserData(HttpServletResponse response) {
        // 设置下载信息
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        String fileName = "user";
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");
        // 查询数据库
        List<UserExcelVO> userExcelVOList = list().stream()
                // todo 性能优化：减少数据库查询次数 批量先查询出所有部门id 再获取部门名称 将信息以key-value存入Set中
                .map(user -> {
                    // 将user数据复制到userExcelVO中
                    UserExcelVO userExcelVO = new UserExcelVO();
                    BeanUtils.copyProperties(user, userExcelVO);
                    // 根据部门id获取部门名称
                    userExcelVO.setDepartmentName(departmentService.getById(user.getDepartmentId()).getDepartmentName());
                    return userExcelVO;
                })
                .collect(Collectors.toList());
        // 调用EasyExcel方法进行写操作
        try {
            EasyExcel.write(response.getOutputStream(), UserExcelVO.class).sheet("用户数据").doWrite(userExcelVOList);
        } catch (IOException e) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR);
        }
        return true;
    }

    @Override
    public boolean importUserData(MultipartFile file) {
        try {
            EasyExcel.read(file.getInputStream(), UserExcelVO.class, new UserListener(baseMapper, departmentService)).sheet().doRead();
        } catch (IOException e) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR);
        }
        return false;
    }


}
