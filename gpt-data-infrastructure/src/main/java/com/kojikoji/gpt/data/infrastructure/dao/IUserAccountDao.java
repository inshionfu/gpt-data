package com.kojikoji.gpt.data.infrastructure.dao;

import com.kojikoji.gpt.data.infrastructure.po.UserAccountPO;
import org.apache.ibatis.annotations.Mapper;

/**
 * @ClassName IUserAccountDao
 * @Description
 * @Author kojikoji 1310402980@qq.com
 * @Date 2024/12/18 20:37
 * @Version
 */

@Mapper
public interface IUserAccountDao {

    int subAccountQuota(String openid);

    int addAccountQuota(UserAccountPO plusAccountReq);

    UserAccountPO queryUserAccount(String openid);

    void insertAccount(UserAccountPO plusAccountReq);
}
