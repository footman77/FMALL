package cn.footman.sso.service;

import cn.footman.common.utils.FResult;

public interface TokenService {

    public FResult getUserByToken(String token);

}
