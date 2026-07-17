package com.ordermanager.service;

import com.ordermanager.common.PageResult;
import com.ordermanager.entity.User;
import com.ordermanager.mapper.UserMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserService {

    private final UserMapper userMapper;

    public UserService(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public PageResult<User> page(String code, String name, String role, String dept, String org,
                                  Integer active, int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        List<User> list = userMapper.selectList(code, name, role, dept, org, active, offset, pageSize);
        long total = userMapper.selectCount(code, name, role, dept, org, active);
        return new PageResult<>(list, total);
    }

    public User getById(Long id) {
        return userMapper.selectById(id);
    }

    public User getByName(String name) {
        return userMapper.selectByName(name);
    }

    public int create(User user) {
        user.setCreateTime(LocalDateTime.now());
        return userMapper.insert(user);
    }

    public int update(User user) {
        return userMapper.update(user);
    }

    public int delete(Long id) {
        return userMapper.deleteById(id);
    }

    public int toggleLock(Long id, boolean locked) {
        return userMapper.updateLocked(id, locked ? 1 : 0);
    }

    public int resetPassword(Long id, String encodedPwd) {
        userMapper.updatePassword(id, encodedPwd);
        return userMapper.updateFirstLogin(id, 1);
    }

    public int changePassword(Long id, String encodedPwd) {
        userMapper.updatePassword(id, encodedPwd);
        return userMapper.updateFirstLogin(id, 0);
    }

    public List<User> getAll() {
        return userMapper.selectAll();
    }
}
