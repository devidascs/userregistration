package com.seto.userregistration.service.image;

public interface SecurityService {
    String findLoggedInUsername();

    void autoLogin(String username, String password);
}
