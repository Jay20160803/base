package com.onlyedu.newclasses.modules.permission.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.onlyedu.newclasses.model.User;
import com.onlyedu.newclasses.modules.permission.dao.UserDao;
import com.onlyedu.newclasses.modules.permission.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Andy
 * @date 2018/11/21 9:35
 */
@Service(value = "userService")
public class UserServiceImpl implements UserService {


    @Autowired
    private UserDao userDao;

    @Override
    public int addUser(User user) {
        return userDao.insert(user);
    }

    @Override
    public PageInfo<User> findAllUser(int pageNum, int pageSize) {

        PageHelper.startPage(pageNum,pageSize);
        List<User> list = userDao.selectAllUser();
        PageInfo<User> result = new PageInfo<>(list);
        return result;
    }
}
