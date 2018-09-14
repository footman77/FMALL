package cn.footman.sso.service;

import cn.footman.common.utils.FResult;
import cn.footman.pojo.TbUser;

public interface RegisterService {

    FResult checkData(String param,int type);

    FResult saveUser(TbUser tbUser);
}
