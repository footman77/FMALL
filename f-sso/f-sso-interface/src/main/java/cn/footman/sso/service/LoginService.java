package cn.footman.sso.service;

import cn.footman.common.utils.FResult;

public interface LoginService {

    FResult login(String username,String password);
}
