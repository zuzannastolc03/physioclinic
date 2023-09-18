package com.example.physioclinic.dao;

import com.example.physioclinic.entity.User;

public interface AppDAO {
    User findUserByUsername(String username);
}
