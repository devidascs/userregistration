package com.seto.userregistration.service.user;

import com.seto.userregistration.model.User;

public interface UserService {
    void save(User user);

    User findByUsername(String username);
}
