package com.nju.edu.erp.service.Impl;

import com.auth0.jwt.interfaces.Claim;
import com.nju.edu.erp.config.JwtConfig;
import com.nju.edu.erp.dao.UserClockInDao;
import com.nju.edu.erp.dao.UserDao;
import com.nju.edu.erp.enums.Role;
import com.nju.edu.erp.exception.MyServiceException;
import com.nju.edu.erp.model.po.User;
import com.nju.edu.erp.model.po.UserClockInPO;
import com.nju.edu.erp.model.po.UserInfoPO;
import com.nju.edu.erp.model.vo.user.UserClockInVO;
import com.nju.edu.erp.model.vo.user.UserInfoVO;
import com.nju.edu.erp.model.vo.user.UserVO;
import com.nju.edu.erp.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class UserServiceImpl implements UserService {

    private final UserDao userDao;

    private final JwtConfig jwtConfig;

    private final UserClockInDao clockInDao;

    @Autowired
    public UserServiceImpl(UserDao userDao, JwtConfig jwtConfig, UserClockInDao clockInDao) {
        this.userDao = userDao;
        this.jwtConfig = jwtConfig;
        this.clockInDao = clockInDao;
    }


    @Override
    public Map<String, String> login(UserVO userVO) {
        User user = userDao.findByUsernameAndPassword(userVO.getName(), userVO.getPassword());
        if (null == user ) {
            throw new MyServiceException("A0000", "用户名或密码错误");
        }
        Map<String, String> authToken = new HashMap<>();
        String token = jwtConfig.createJWT(user);
        authToken.put("token", token);
        return authToken;
    }

    @Override
    @Transactional
    public void register(UserVO userVO) {
        User user = userDao.findByUsername(userVO.getName());
        if (user != null) {
            throw new MyServiceException("A0001", "用户名已存在");
        }
        User userSave = new User();
        BeanUtils.copyProperties(userVO, userSave);
        userDao.createUser(userSave);
    }

    @Override
    public UserVO auth(String token) {
        Map<String, Claim> claims = jwtConfig.parseJwt(token);
        UserVO userVO = UserVO.builder()
                .name(claims.get("name").as(String.class))
                .role(Role.valueOf(claims.get("role").as(String.class)))
                .build();
        return userVO;
    }

    @Override
    @Transactional
    public void clockIn(UserVO userVO) {
        UserClockInPO clockInPO=new UserClockInPO();
        clockInPO.setDate(new Date());
        clockInPO.setUserName(userVO.getName());
        int insert = clockInDao.insert(clockInPO);
        if(insert==0){
            throw new RuntimeException("打卡失败");
        }
    }

    @Override
    public List<UserClockInVO> getClockInRecord(UserVO userVO) {
        List<UserClockInPO> pos = clockInDao.findByUserName(userVO.getName());
        List<UserClockInVO> vos=new ArrayList<>();
        for (UserClockInPO po : pos) {
            UserClockInVO vo=new UserClockInVO();
            BeanUtils.copyProperties(po,vo);
            vos.add(vo);
        }
        return vos;
    }

    @Override
    public UserInfoVO getUserInfo(String username) {
        UserInfoPO userInfo = userDao.getUserInfo(username);
        UserInfoVO userInfoVO=new UserInfoVO();
        BeanUtils.copyProperties(userInfo,userInfoVO);
        return userInfoVO;
    }

    @Override
    public int getAbsentDay(UserVO userVO) {
        List<UserClockInVO> clockInRecord = getClockInRecord(userVO);

        LocalDate localDate=LocalDate.now();
        boolean[] recordTable=new boolean[localDate.lengthOfMonth()];
        for (UserClockInVO userClockInVO : clockInRecord) {
            Date date = userClockInVO.getDate();
            Calendar calendar=Calendar.getInstance();
            calendar.setTime(date);
            if(calendar.get(Calendar.MONTH)+1==localDate.getMonth().getValue()){
                recordTable[calendar.get(Calendar.DATE)]=true;
            }
        }

        int absentDay=recordTable.length;
        for (boolean b : recordTable) {
            if(b){
                absentDay--;
            }
        }
        return absentDay;
    }

    @Override
    public UserVO getUserByName(String username) {
        User byUsername = userDao.findByUsername(username);
        UserVO userVO=new UserVO();
        BeanUtils.copyProperties(byUsername,userVO);
        return userVO;
    }


    @Override
    public List<UserVO> getAll() {
        List<User> all = userDao.getAll();
        List<UserVO> userVOS=new ArrayList<>();
        for (User user : all) {
            UserVO vo=new UserVO();
            BeanUtils.copyProperties(user,vo);
            userVOS.add(vo);
        }
        return userVOS;
    }

    @Override
    @Transactional
    public void createInfo(UserInfoVO userInfoVO) {
        Integer userId = userInfoVO.getUserId();
        String name = userInfoVO.getName();
        UserInfoPO userInfo = userDao.getUserInfo(name);
        if(userInfo!=null){
            throw new RuntimeException("已有信息");
        }
        User user = userDao.findByUsername(name);
        if(user==null){
            throw new RuntimeException("用户不存在");
        }
        UserInfoPO userInfoPO=new UserInfoPO();
        BeanUtils.copyProperties(userInfoVO,userInfoPO);
        int effect=userDao.insertInfo(userInfoPO);
        if(effect!=1){
            throw new RuntimeException("创建失败");
        }
    }

    @Override
    public List<UserInfoVO> getAllInfo() {
        List<UserInfoPO> allInfo = userDao.getAllInfo();
        return allInfo.stream().map(po->{
            UserInfoVO vo=new UserInfoVO();
            BeanUtils.copyProperties(po,vo);
            return vo;
        }).collect(Collectors.toList());
    }


}
