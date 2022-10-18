package com.nju.edu.erp.service;

import com.nju.edu.erp.model.vo.user.UserClockInVO;
import com.nju.edu.erp.model.vo.user.UserInfoVO;
import com.nju.edu.erp.model.vo.user.UserVO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface UserService {

    /**
     * 用户登录
     * @param userVO
     * @return
     */
    Map<String, String> login(UserVO userVO);

    /**
     * 用户注册
     * @param userVO
     */
    void register(UserVO userVO);

    /**
     * 用户认证
     * @param token
     */
    UserVO auth(String token);

    void clockIn(UserVO userVO);

    List<UserClockInVO> getClockInRecord(UserVO userVO);

    UserInfoVO getUserInfo(String username);

    int getAbsentDay(UserVO userVO);

    UserVO getUserByName(String username);

    List<UserVO> getAll();

    void createInfo(UserInfoVO userInfoVO);

    List<UserInfoVO> getAllInfo();
}
