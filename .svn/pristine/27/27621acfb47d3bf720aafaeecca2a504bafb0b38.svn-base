package com.zccbh.demand.mapper.merchants;

import com.zccbh.demand.pojo.merchants.CbhAccount;
import com.zccbh.util.base.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

public interface CbhAccountMapper extends BaseMapper<CbhAccount> {
    CbhAccount selectByToken(@Param(value = "toKen")String toKen, @Param(value = "accountPW")String accountPW);
}