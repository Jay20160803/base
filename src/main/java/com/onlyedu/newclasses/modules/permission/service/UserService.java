package com.onlyedu.newclasses.modules.permission.service;

import com.github.pagehelper.PageInfo;
import com.onlyedu.newclasses.model.User;

/**
 * @author Andy
 * @date 2018/11/21 9:24
 */
public interface UserService {

    int addUser(User user);

    PageInfo<User> findAllUser(int pageNum, int pageSize);
}
