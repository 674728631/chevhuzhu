package com.zccbh.demand.service.customer;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zccbh.demand.mapper.customer.MessageMapper;
import com.zccbh.demand.mapper.customer.UserCustomerMapper;
import com.zccbh.demand.service.weChat.WeiXinUtils;
import com.zccbh.util.base.LocalThreadPool;
import com.zccbh.util.collect.Constant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class MessageService {
    @Autowired
    private MessageMapper messageMapper;

    @Autowired
    UserCustomerMapper userCustomerMapper;

    @Autowired
    private WeiXinUtils weiXinUtils;

    private Logger logger = LoggerFactory.getLogger(MessageService.class);

    /**
     * 创建消息
     *
     * @param paramModelMap 消息信息
     * @return 添加结果信息
     * @throws Exception
     */
    @Transactional
    public String saveMessage(Map<String, Object> paramModelMap) throws Exception {
        messageMapper.saveSingle(paramModelMap);
        return "0";
    }

    public String updateModel(Map<String, Object> paramModelMap) throws Exception {
        messageMapper.updateModel(paramModelMap);
        return "0";
    }

    public String updateRead(Map<String, Object> paramModelMap) throws Exception {
        messageMapper.updateRead(paramModelMap);
        return "0";
    }

    public PageInfo<Map<String, Object>> findMessageList(Map<String, Object> paramModelMap) throws Exception {
        int pageNo = Constant.toEmpty(paramModelMap.get("pageNo")) ? Integer.parseInt(paramModelMap.get("pageNo").toString()) : 1;
        int pageSize = Constant.toEmpty(paramModelMap.get("pageSize")) ? Integer.parseInt(paramModelMap.get("pageSize").toString()) : 10;
        PageHelper.startPage(pageNo, pageSize);
        List<Map<String, Object>> messageList = messageMapper.findMore(paramModelMap);
        PageInfo<Map<String, Object>> carInfo = new PageInfo<>(messageList);
        return carInfo;
    }


    public void pushInvitationSuccessTemplate(String customerId) {
        logger.info("发送邀请成功消息>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>{}", customerId);
        LocalThreadPool.executorService.execute(() -> {
            try {
                // 获取邀请人
                Map<String, Object> rs = userCustomerMapper.getInvitationSuccessTempParam(customerId);
                if (CollectionUtils.isEmpty(rs)) {
                    throw new RuntimeException("获取用户失败！");
                }
                Map<String, String> param = new HashMap<>();

                param.put("openid", rs.get("openid").toString());
                int num = Integer.valueOf(rs.get("num").toString());
                //
                param.put("remark", "亲，您已获得1元购买原价30元上门洗车服务资格（限购2次）。");
                param.put("url", Constant.toReadPro("realURL") + "hfive/view/washcar.html");
                if (1 == num) {
                    param.put("url", Constant.toReadPro("realURL") + "hfive/view/wallet.html");
                    param.put("first", "恭喜您！已升级成V2会员，救助额度提升至1125元。");
//                    param.put("remark", "亲，再邀请3个人就可将救助额度提升至1500元。");
                    param.put("remark", "亲，再邀请1个人就可获得1元购买原价30元上门洗车服务资格。");
                } else if (2 == num) {
                    param.put("first", "恭喜您！救助额度提升至1250元。");
//                    param.put("remark", "亲，再邀请2个人就可将救助额度提升至1500元。");
                } else if (3 == num) {
                    param.put("first", "恭喜您！救助额度提升至1375元。");
//                    param.put("remark", "亲，再邀请1个人就可将救助额度提升至1500元。");
                } else if (4 == num)
                    param.put("first", "恭喜您！救助额度提升至1500元。");
                if (num < 5)
                    weiXinUtils.sendTemplate(19, param);
            } catch (Exception e) {
                logger.error("", e);
            }
        });
    }

    public static void main(String[] args) {
        BigDecimal amt = new BigDecimal("1250");
        BigDecimal j = amt.subtract(Constant.oneQuota);
        System.out.println(j);
//        BigDecimal amt = new BigDecimal("1250");
//        BigDecimal amt = new BigDecimal("1250");
        BigDecimal i = amt.subtract(new BigDecimal("1000")).divide(Constant.oneQuota);

        BigDecimal num = new BigDecimal("2");
        BigDecimal jj = num.multiply(Constant.oneQuota);
        System.out.println(jj.toString());
    }
}


