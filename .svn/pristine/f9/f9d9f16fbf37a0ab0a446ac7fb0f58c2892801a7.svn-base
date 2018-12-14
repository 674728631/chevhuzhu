package com.zccbh.demand.service.business;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zccbh.demand.mapper.business.AccountMapper;
import com.zccbh.util.collect.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class AccountService {
    @Autowired
    private AccountMapper accountMapper;

    /**
     * 修改账户信息
     * @param paramModelMap 账户信息
     * @return
     * @throws Exception
     */
    @Transactional
    public String updateAccount(Map<String, Object> paramModelMap) throws Exception{
        accountMapper.updateModel(paramModelMap);
        return "0";
    }

    /**
     * 删除账户
     * @param accountId 账户id
     * @return
     * @throws Exception
     */
    @Transactional
    public String deleteAccount(Integer accountId) throws Exception{
        accountMapper.deleteModel(accountId);
        return "0";
    }

    /**
     * 查询账户数据
     * @param paramModelMap 查询条件
     * @return 账户信息
     * @throws Exception
     */
    public PageInfo<Map<String, Object>> findAccountList(Map<String, Object> paramModelMap) throws Exception {
        int pageNo = Constant.toEmpty(paramModelMap.get("pageNo"))?Integer.parseInt(paramModelMap.get("pageNo").toString()):1;
        int pageSize = Constant.toEmpty(paramModelMap.get("pageSize"))?Integer.parseInt(paramModelMap.get("pageSize").toString()):10;
        PageHelper.startPage(pageNo, pageSize);
        List<Map<String, Object>> accountList = accountMapper.findMore(paramModelMap);
        for (Map<String, Object> account:accountList) {
            String amtUnfreeze = Constant.toOr((String) account.get("amtUnfreeze"), Constant.toReadPro("orKey"), "decrypt");
            String amtFreeze = Constant.toOr((String) account.get("amtFreeze"), Constant.toReadPro("orKey"), "decrypt");
            account.put("amtTotal",new BigDecimal(amtUnfreeze).add(new BigDecimal(amtFreeze)));
            account.put("amtUnfreeze",amtUnfreeze);
            account.put("amtFreeze",amtFreeze);
        }
        PageInfo<Map<String, Object>> accountInfo = new PageInfo<>(accountList);
        return accountInfo;
    }
}


