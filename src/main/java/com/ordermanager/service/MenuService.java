package com.ordermanager.service;

import com.ordermanager.entity.Menu;
import com.ordermanager.mapper.MenuMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MenuService {

    private final MenuMapper menuMapper;

    public MenuService(MenuMapper menuMapper) {
        this.menuMapper = menuMapper;
    }

    public List<Menu> tree(String code, String label, String route, String type) {
        return menuMapper.selectList(code, label, route, type);
    }

    public Menu getById(Long id) {
        return menuMapper.selectById(id);
    }

    public int create(Menu menu) {
        menu.setCreateTime(LocalDateTime.now());
        return menuMapper.insert(menu);
    }

    public int update(Menu menu) {
        return menuMapper.update(menu);
    }

    public int delete(Long id) {
        Menu menu = menuMapper.selectById(id);
        if (menu == null) return 0;
        List<Menu> children = menuMapper.selectChildren(menu.getCode());
        if (!children.isEmpty()) {
            return -1; // has children
        }
        return menuMapper.deleteById(id);
    }

    public List<Menu> getAll() {
        return menuMapper.selectAll();
    }
}
