package com.zccbh.demand.service.weChat;

import com.google.gson.JsonObject;

import com.zccbh.util.base.Base64;
import com.zccbh.util.base.DateUtils;
import com.zccbh.util.collect.Constant;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class PushTemplate {
    @Autowired
    private WeiXinUtils weiXinUtils;

    /**
     * 互助申请和理赔订单审核通过通知模板消息
     *
     * @return JSONObject
     * @throws Exception
     * @see
     */
    public JSONObject auditNotification(Map<String, String> parameter) throws Exception {
/*
        广告已通过审核
        审核结果：审核通过
        通过时间：2016年6月25日 18:36
        点击查看详情！*/
        // 模板id
        String template_id = Constant.toReadPro("auditNotification");
//        String template_id = "jdjPXoYJJSHVO05wMdAynG3iNmIBYN-CxDp-OF-PBwM";
        // 模板的主题颜色
        String topcolor = "#008000";
        // 用户昵称
        Map<String, String> stringStringMap = weiXinUtils.getNickname(parameter.get("openid"));
        String username = stringStringMap.get("nickname");
        if (Constant.toEmpty(username)) {
            username = Base64.getFromBase64(username);
        }
        // 构造json包
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        Map<String, Object> data = new LinkedHashMap<String, Object>();
        LinkedHashMap<String, String> first = new LinkedHashMap<String, String>();
//        first.put("value", "尊敬的" + username + "：\n\n您好，恭喜您!"+parameter.get("content"));
        first.put("value", "主人：恭喜！您爱车" + parameter.get("licensePlateNumber") + "的" + parameter.get("theme") + "审核已通过！请尽快填写接车信息吧。");
        first.put("color", "#fe3636");
        data.put("first", first);

        LinkedHashMap<String, String> keyword1 = new LinkedHashMap<String, String>();
        keyword1.put("value", parameter.get("keyword1"));
//        keyword1.put("value", parameter.get("licensePlateNumber"));
        keyword1.put("color", "#50a6ff");
        data.put("keyword1", keyword1);

        LinkedHashMap<String, String> keyword2 = new LinkedHashMap<String, String>();
        keyword2.put("value", parameter.get("keyword2"));
//        keyword2.put("value", "已通过");
        keyword2.put("color", "#50a6ff");
        data.put("keyword2", keyword2);

        LinkedHashMap<String, String> remark = new LinkedHashMap<String, String>();
        remark.put("value", "点击查看详情");
        remark.put("color", "#fe3636");
        data.put("remark", remark);

        map.put("touser", parameter.get("openid"));
        map.put("template_id", template_id);
        map.put("url", parameter.get("url"));
        map.put("topcolor", topcolor);
        map.put("data", data);
        JSONObject jsonObject = JSONObject.fromObject(map);
        return jsonObject;
    }

    /**
     * 订单支付通知模板消息
     *
     * @return JSONObject
     * @throws Exception
     * @see
     */
    public JSONObject paymentNotice(Map<String, String> parameter) throws Exception {
/*      你有一笔未支付订单，请支付成功后再使用共享单车
        交易金额：￥100
        交易时间：2012-02-02 10:11:00
        点击“详情”，立即支付*/
        // 模板id
        String template_id = Constant.toReadPro("paymentNotice");
//        String template_id = "IoYV6OwcIa1BVwRXkE-bn9Amz4yQbKi0l0lKiSpZnPU";
        // 模板的主题颜色
        String topcolor = "#008000";
        // 用户昵称
        Map<String, String> stringStringMap = weiXinUtils.getNickname(parameter.get("openid"));
        String username = stringStringMap.get("nickname");
        if (Constant.toEmpty(username)) {
            username = Base64.getFromBase64(username);
        }
        // 构造json包
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        Map<String, Object> data = new LinkedHashMap<String, Object>();

        LinkedHashMap<String, String> first = new LinkedHashMap<String, String>();
//        first.put("value", "尊敬的" + username + "：\n\n您好，您爱车"+parameter.get("licensePlateNumber")+"的车辆定损已通过.");
        first.put("value", "主人：您爱车" + parameter.get("licensePlateNumber") + "的定损已通过，请及时支付，以便维修中心为您安排维修。");
        first.put("color", "#fe3636");
        data.put("first", first);

        LinkedHashMap<String, String> keyword1 = new LinkedHashMap<String, String>();
        keyword1.put("value", parameter.get("money"));
        keyword1.put("color", "#50a6ff");
        data.put("keyword1", keyword1);

        LinkedHashMap<String, String> keyword2 = new LinkedHashMap<String, String>();
        keyword2.put("value", parameter.get("keyword2"));
        keyword2.put("color", "#50a6ff");
        data.put("keyword2", keyword2);

        LinkedHashMap<String, String> remark = new LinkedHashMap<String, String>();
        remark.put("value", "请及时支付，以便维修中心为您安排维修。");
        remark.put("color", "#fe3636");
        data.put("remark", remark);

        map.put("touser", parameter.get("openid"));
        map.put("template_id", template_id);
        map.put("url", Constant.toReadPro("realURL") + "hfive/view/order_detail.html?id=" + parameter.get("eventNo"));
        map.put("topcolor", topcolor);
        map.put("data", data);

        JSONObject jsonObject = JSONObject.fromObject(map);
        return jsonObject;
    }

    /**
     * 维修完成取车通知模板消息
     *
     * @return JSONObject
     * @throws Exception
     * @see
     */
    public JSONObject maintenanceNotification(Map<String, String> parameter) throws Exception {
/*        订单号为xxx的事故维修订单维修完毕啦，请火速前往xxx汽修厂提取您的爱车。
        订单号：24894498746578454
        维修车辆：鲁AJ0001
        提车地点：济南市历下区历山路1898号
        联系方式：18899999999
        点击查看订单详情*/
        // 模板id
        String template_id = Constant.toReadPro("maintenanceNotification");
//        String template_id = "1YcC3pctIrnj-Zv8k-3mbTB-dR8hEanlng7aKSMMDT0";
        // 模板的主题颜色
        String topcolor = "#008000";
        // 用户昵称
        Map<String, String> stringStringMap = weiXinUtils.getNickname(parameter.get("openid"));
        String username = stringStringMap.get("nickname");
        if (Constant.toEmpty(username)) {
            username = Base64.getFromBase64(username);
        }
        // 构造json包
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        Map<String, Object> data = new LinkedHashMap<String, Object>();

        LinkedHashMap<String, String> first = new LinkedHashMap<String, String>();
//        first.put("value", "尊敬的" + username + "：\n\n您好，您爱车"+parameter.get("licensePlateNumber")+"已维修完成.");
        first.put("value", "主人：您爱车" + parameter.get("licensePlateNumber") + "已维修完成，请与维修中心确认交车时间和地点，点击查看订单详情！");
        first.put("color", "#fe3636");
        data.put("first", first);

        LinkedHashMap<String, String> keyword1 = new LinkedHashMap<String, String>();
        keyword1.put("value", parameter.get("eventNo"));
        keyword1.put("color", "#50a6ff");
        data.put("keyword1", keyword1);

        LinkedHashMap<String, String> keyword2 = new LinkedHashMap<String, String>();
        keyword2.put("value", parameter.get("licensePlateNumber"));
        keyword2.put("color", "#50a6ff");
        data.put("keyword2", keyword2);

        LinkedHashMap<String, String> keyword3 = new LinkedHashMap<String, String>();
        keyword3.put("value", parameter.get("address"));
        keyword3.put("color", "#50a6ff");
        data.put("keyword3", keyword3);

        LinkedHashMap<String, String> keyword4 = new LinkedHashMap<String, String>();
        keyword4.put("value", parameter.get("tel"));
        keyword4.put("color", "#50a6ff");
        data.put("keyword4", keyword4);

        LinkedHashMap<String, String> remark = new LinkedHashMap<String, String>();
        remark.put("value", "请及时前往维修中心接车，点击查看订单详情！");
        remark.put("color", "#fe3636");
        data.put("remark", remark);

        map.put("touser", parameter.get("openid"));
        map.put("template_id", template_id);
        String s = parameter.get("eventNo");
        if (s.length() == 21) {
            map.put("url", Constant.toReadPro("realURL") + "hfive/view/baoxian_order_detail.html?id=" + s);
        } else {
            map.put("url", Constant.toReadPro("realURL") + "hfive/view/order_detail.html?id=" + s);
        }
        map.put("topcolor", topcolor);
        map.put("data", data);
        JSONObject jsonObject = JSONObject.fromObject(map);
        return jsonObject;
    }

    /**
     * 余额不足通知模板消息
     *
     * @return JSONObject
     * @throws Exception
     * @see
     */
    public JSONObject lackBalance(Map<String, String> parameter) throws Exception {
/*        因新一期互助事件扣款，导致当前余额过低，为了延续您的保障，建议充值至9元以上，延续您的保障
        被保障人：王大伟
        互助计划：综合意外互助计划
        当前余额：不足3元，下次扣款后保障可能失效（余额低于1元将自动失去保障）
        点此充值*/
        // 模板id
        String template_id = Constant.toReadPro("lackBalance");
//        String template_id = "ktCw29lJvcjf19WLQ-WswFXIwnD9SJFjNEtKBIzFb-M";
        // 模板的主题颜色
        String topcolor = "#008000";
        // 用户昵称
        Map<String, String> stringStringMap = weiXinUtils.getNickname(parameter.get("openid"));
        String username = stringStringMap.get("nickname");
        if (Constant.toEmpty(username)) {
            username = Base64.getFromBase64(username);
        }
        // 构造json包
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        Map<String, Object> data = new LinkedHashMap<String, Object>();

        LinkedHashMap<String, String> first = new LinkedHashMap<String, String>();
//        first.put("value", "尊敬的" + username + "：\n\n您好，您爱车"+parameter.get("content"));
//        first.put("value", "主人：您爱车" + parameter.get("licensePlateNumber") + "账户互助金低于" + parameter.get("money") + "元，" + parameter.get("notice") + "为保障您的互助权益，请尽快为账户充值吧！车V互助：我为人人，人人为我。");
        first.put("value", "主人：您爱车" + parameter.get("licensePlateNumber") + "账户互助金低于" + parameter.get("money") + "元，" + "预计"+parameter.get("num")+"天后退出保障，若账户金额低于0元，您将退出保障，再次加入后将重新进入30天观察期。车V互助：我为人人，人人为我。");
        first.put("color", "#fe3636");
        data.put("first", first);

        LinkedHashMap<String, String> keyword1 = new LinkedHashMap<String, String>();
        keyword1.put("value", parameter.get("name"));
        keyword1.put("color", "#50a6ff");
//        keyword1.put("value", parameter.get("licensePlateNumber"));
//        keyword1.put("color", "#50a6ff");
        data.put("keyword1", keyword1);

        LinkedHashMap<String, String> keyword2 = new LinkedHashMap<String, String>();
        keyword2.put("value", "车V互助计划");
        keyword2.put("color", "#50a6ff");
        data.put("keyword2", keyword2);

        LinkedHashMap<String, String> keyword3 = new LinkedHashMap<String, String>();
        keyword3.put("value", "低于" + parameter.get("money") + "元");
//        keyword3.put("value", parameter.get("money") + "元");
        keyword3.put("color", "#50a6ff");
        data.put("keyword3", keyword3);

        LinkedHashMap<String, String> remark = new LinkedHashMap<String, String>();
        remark.put("value", "为保障您的互助权益，请尽快为账户充值吧！");
        remark.put("color", "#fe3636");
        data.put("remark", remark);

        map.put("touser", parameter.get("openid"));
        map.put("template_id", template_id);
        map.put("url", Constant.toReadPro("realURL") + "hfive/view/recharge.html?id=" + parameter.get("carId"));
        map.put("topcolor", topcolor);
        map.put("data", data);
        JSONObject jsonObject = JSONObject.fromObject(map);
        return jsonObject;
    }

    /**
     * 分摊推送通知模板消息
     *
     * @return JSONObject
     * @throws Exception
     * @see
     */
    public JSONObject shareMoney(Map<String, String> parameter) throws Exception {
/*        中老年抗癌计划发生首起互助事件，经公示、扣款后，互助金已汇集至患病会员
        互助会员：王晓晓
        互助计划：中青年癌症互助计划
        汇集互助金：30，000元
        分摊金额：每人0.03元
        请及时关注账户余额*/
        // 模板id
        String template_id = Constant.toReadPro("shareMoney"); //真实
//        String template_id = "hWFWAUkwGjD4mWj2cgYgA-7rptSmEkHzEpMD8GzLDrM"; //真实
        // String template_id = "HSplg09-ZbLP7QvPPV4UC92oKX6NixPT5rZ2ha8PioA";  //测试环境
        // 模板的主题颜色
        String topcolor = "#008000";
        // 用户昵称
        Map<String, String> stringStringMap = weiXinUtils.getNickname(parameter.get("openid"));
        String username = stringStringMap.get("nickname");
        if (Constant.toEmpty(username)) {
            username = Base64.getFromBase64(username);
        }
        // 构造json包
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        Map<String, Object> data = new LinkedHashMap<String, Object>();
        String number = parameter.get("number");
        LinkedHashMap<String, String> first = new LinkedHashMap<String, String>();
        first.put("value", "尊敬的" + username + "：\n\n您好，本周共发生" + number + "起救助事件，您帮助了" + parameter.get("number1") + "人，感谢您对车V互助的支持与厚爱！");
        first.put("color", "#fe3636");
        data.put("first", first);

        LinkedHashMap<String, String> keyword1 = new LinkedHashMap<String, String>();
        keyword1.put("value", parameter.get("names"));
        keyword1.put("color", "#50a6ff");
        data.put("keyword1", keyword1);

        LinkedHashMap<String, String> keyword2 = new LinkedHashMap<String, String>();
        keyword2.put("value", "车V互助计划");
        keyword2.put("color", "#50a6ff");
        data.put("keyword2", keyword2);

        LinkedHashMap<String, String> keyword3 = new LinkedHashMap<String, String>();
        keyword3.put("value", parameter.get("totalAmount") + "元");
        keyword3.put("color", "#50a6ff");
        data.put("keyword3", keyword3);

        LinkedHashMap<String, String> keyword4 = new LinkedHashMap<String, String>();
        keyword4.put("value", parameter.get("money") + "元");
        keyword4.put("color", "#50a6ff");
        data.put("keyword4", keyword4);

        LinkedHashMap<String, String> remark = new LinkedHashMap<String, String>();
        remark.put("value", "请及时关注账户余额！");
        remark.put("color", "#fe3636");
        data.put("remark", remark);

        map.put("touser", parameter.get("openid"));
        map.put("template_id", template_id);
        map.put("url", Constant.toReadPro("realURL") + "hfive/view/wallet.html");
        map.put("topcolor", topcolor);
        map.put("data", data);
        JSONObject jsonObject = JSONObject.fromObject(map);
        return jsonObject;
    }


    /**
     * 活动推送通知模板消息
     *
     * @return JSONObject
     * @throws Exception
     * @see
     */
    public JSONObject activitiesPush(Map<String, String> parameter) throws Exception {
/*      白玉芬扫描成功
        活动主题：街道社区义务巡逻
        发起单位：江岸区19支志愿队
        活动地址：武汉市江岸区工农兵路
        时间：2015-06-05  14
        活动详情*/
        // 模板id
        String template_id = Constant.toReadPro("activitiesPush");
//        String template_id = "oE8x4uPQA7ORk24qkg0lm5H15u2DteHU2wvMPL8Mrfw";
        // 模板的主题颜色
        String topcolor = "#008000";
        // 用户昵称
        Map<String, String> stringStringMap = weiXinUtils.getNickname(parameter.get("openid"));
        String username = stringStringMap.get("nickname");
        if (Constant.toEmpty(username)) {
            username = Base64.getFromBase64(username);
        }
        // 构造json包
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        Map<String, Object> data = new LinkedHashMap<String, Object>();

        LinkedHashMap<String, String> first = new LinkedHashMap<String, String>();
        first.put("value", "尊敬的" + username + "：\n\n您好，" + parameter.get("activitiesName"));
        first.put("color", "#fe3636");
        data.put("first", first);

        LinkedHashMap<String, String> keyword1 = new LinkedHashMap<String, String>();
        keyword1.put("value", parameter.get("themeName"));
        keyword1.put("color", "#50a6ff");
        data.put("keyword1", keyword1);

        LinkedHashMap<String, String> keyword2 = new LinkedHashMap<String, String>();
        keyword2.put("value", parameter.get("unitName"));
        keyword2.put("color", "#50a6ff");
        data.put("keyword2", keyword2);

        LinkedHashMap<String, String> keyword3 = new LinkedHashMap<String, String>();
        keyword3.put("value", parameter.get("address"));
        keyword3.put("color", "#50a6ff");
        data.put("keyword3", keyword3);

        LinkedHashMap<String, String> keyword4 = new LinkedHashMap<String, String>();
        keyword4.put("value", parameter.get("date"));
        keyword4.put("color", "#50a6ff");
        data.put("keyword4", keyword4);

        LinkedHashMap<String, String> remark = new LinkedHashMap<String, String>();
        remark.put("value", "点击查看活动详情！");
        remark.put("color", "#fe3636");
        data.put("remark", remark);

        map.put("touser", parameter.get("openid"));
        map.put("template_id", template_id);
        map.put("url", Constant.toReadPro("realURL") + "hfive/view/index.html");
        map.put("topcolor", topcolor);
        map.put("data", data);
        JSONObject jsonObject = JSONObject.fromObject(map);
        return jsonObject;
    }

    /**
     * 加入互助支付通知模板消息(未支付通知提醒)
     *
     * @return JSONObject
     * @throws Exception
     * @see
     */
    public JSONObject paymentHelp(Map<String, String> parameter) throws Exception {
/*      你有一笔未支付订单，请支付成功后再使用共享单车
        交易金额：￥100
        交易时间：2012-02-02 10:11:00
        点击“详情”，立即支付*/
        // 模板id
        String template_id = Constant.toReadPro("paymentHelp");
//        String template_id = "IoYV6OwcIa1BVwRXkE-bn9Amz4yQbKi0l0lKiSpZnPU";
        // 模板的主题颜色
        String topcolor = "#008000";
        // 用户昵称
        Map<String, String> stringStringMap = weiXinUtils.getNickname(parameter.get("openid"));
        String username = stringStringMap.get("nickname");
        if (Constant.toEmpty(username)) {
            username = Base64.getFromBase64(username);
        }
        // 构造json包
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        Map<String, Object> data = new LinkedHashMap<String, Object>();

        LinkedHashMap<String, String> first = new LinkedHashMap<String, String>();
//        first.put("value", "尊敬的" + username + "：\n\n您好，亲! 您爱车"+parameter.get("licensePlateNumber")+"还没有支付呢.");
        first.put("value", "主人，我想找您说个事，如果您一年没得一次擦挂我就把我的电瓶车吃了！赌不赌？ 小擦挂不走保险走车V互助才是正确姿势，首充9元立享1000元擦挂保障。不要问我为什么这么便宜？因为没有中间商赚差价！");
        first.put("color", "#fe3636");
        data.put("first", first);

        LinkedHashMap<String, String> keyword1 = new LinkedHashMap<String, String>();
        keyword1.put("value", parameter.get("money") + "元");
        keyword1.put("color", "#50a6ff");
        data.put("keyword1", keyword1);

        LinkedHashMap<String, String> keyword2 = new LinkedHashMap<String, String>();
        keyword2.put("value", parameter.get("keyword2"));
        keyword2.put("color", "#50a6ff");
        data.put("keyword2", keyword2);

        LinkedHashMap<String, String> remark = new LinkedHashMap<String, String>();
        remark.put("value", "猛戳  >>");
        remark.put("color", "#fe3636");
        data.put("remark", remark);

        map.put("touser", parameter.get("openid"));
        map.put("template_id", template_id);
        map.put("url", Constant.toReadPro("realURL") + "hfive/view/car_add_pay.html?id=" + parameter.get("carId"));
        map.put("topcolor", topcolor);
        map.put("data", data);

        JSONObject jsonObject = JSONObject.fromObject(map);
        return jsonObject;
    }

    /**
     * 添加完好车辆照片通知模板消息
     *
     * @return JSONObject
     * @throws Exception
     * @see
     */
    public JSONObject addCarImg(Map<String, String> parameter) throws Exception {
/*      您好，请按要求补传相应照片
        理赔进度：核损中
        缺少照片类型：身份证正面
        点击进行上传*/
        // 模板id
        String template_id = Constant.toReadPro("addCarImg");
//        String template_id = "Njg_beymSsmb8QbZIvc-d5LdQYn7yYPcuK5ixqUybck";
        // 模板的主题颜色
        String topcolor = "#008000";
        // 用户昵称
        Map<String, String> stringStringMap = weiXinUtils.getNickname(parameter.get("openid"));
        String username = stringStringMap.get("nickname");
        if (Constant.toEmpty(username)) {
            username = Base64.getFromBase64(username);
        }
        // 构造json包
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        Map<String, Object> data = new LinkedHashMap<String, Object>();

        LinkedHashMap<String, String> first = new LinkedHashMap<String, String>();
        first.put("value", "尊敬的" + username + "：\n\n您好，恭喜您! 您爱车" + parameter.get("licensePlateNumber") + "已经支付" + parameter.get("money") + "元.");
        first.put("color", "#fe3636");
        data.put("first", first);

        LinkedHashMap<String, String> keyword1 = new LinkedHashMap<String, String>();
        keyword1.put("value", "审核中");
        keyword1.put("color", "#50a6ff");
        data.put("keyword1", keyword1);

        LinkedHashMap<String, String> keyword2 = new LinkedHashMap<String, String>();
        keyword2.put("value", "车辆照片");
        keyword2.put("color", "#50a6ff");
        data.put("keyword2", keyword2);

        LinkedHashMap<String, String> remark = new LinkedHashMap<String, String>();
        remark.put("value", "还差最后一步哟! 拍摄车辆无码照，审核通过后1000元互助救助额度立马到手！");
        remark.put("color", "#fe3636");
        data.put("remark", remark);

        map.put("touser", parameter.get("openid"));
        map.put("template_id", template_id);
        map.put("url", Constant.toReadPro("realURL") + "hfive/view/car_add2.html?id=" + parameter.get("carId"));
        map.put("topcolor", topcolor);
        map.put("data", data);

        JSONObject jsonObject = JSONObject.fromObject(map);
        return jsonObject;
    }

    /**
     * 审核未通过通知模板消息
     *
     * @return JSONObject
     * @throws Exception
     * @see
     */
    public JSONObject examineFail(Map<String, String> parameter) throws Exception {
/*      抱歉，您申请的分期未通过审核。
        申请项目：果酸换肤
        申请状态：申请失败
        失败原因：信用评分不足
        感谢您的使用。*/
        // 模板id
        String template_id = Constant.toReadPro("examineFail");
//        String template_id = "gstZecCl-Zbg-Ni9QV7hgpQkC7eQml7ezLsbopdagb4";
        // 模板的主题颜色
        String topcolor = "#008000";
        // 用户昵称
        Map<String, String> stringStringMap = weiXinUtils.getNickname(parameter.get("openid"));
        String username = stringStringMap.get("nickname");
        if (Constant.toEmpty(username)) {
            username = Base64.getFromBase64(username);
        }
        // 构造json包
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        Map<String, Object> data = new LinkedHashMap<String, Object>();
        LinkedHashMap<String, String> first = new LinkedHashMap<String, String>();
//        first.put("value", "尊敬的" + username + "：\n\n很抱歉！您爱车"+parameter.get("licensePlateNumber")+"审核未通过.");
        first.put("value", "主人：很抱歉！您爱车" + parameter.get("licensePlateNumber") + "的" + parameter.get("theme") + "审核未通过，请点击详情查看原因。");
        first.put("color", "#fe3636");
        data.put("first", first);

        LinkedHashMap<String, String> keyword1 = new LinkedHashMap<String, String>();
        keyword1.put("value", parameter.get("keyword1"));
        keyword1.put("color", "#50a6ff");
        data.put("keyword1", keyword1);

        LinkedHashMap<String, String> keyword2 = new LinkedHashMap<String, String>();
        keyword2.put("value", "未通过");
        keyword2.put("color", "#50a6ff");
        data.put("keyword2", keyword2);

        LinkedHashMap<String, String> keyword3 = new LinkedHashMap<String, String>();
        keyword3.put("value", parameter.get("remark"));
        keyword3.put("color", "#50a6ff");
        data.put("keyword3", keyword3);

        LinkedHashMap<String, String> remark = new LinkedHashMap<String, String>();
        remark.put("value", "点击查看详情");
        remark.put("color", "#fe3636");
        data.put("remark", remark);

        map.put("touser", parameter.get("openid"));
        map.put("template_id", template_id);
        String type = parameter.get("type");
        map.put("url", parameter.get("url"));
        map.put("topcolor", topcolor);
        map.put("data", data);
        JSONObject jsonObject = JSONObject.fromObject(map);
        return jsonObject;
    }

    /**
     * 派单给维修中心通知模板消息
     *
     * @return JSONObject
     * @throws Exception
     * @see
     */
    public JSONObject orderUpdate(Map<String, String> parameter) throws Exception {
/*     尊敬的kant:
       订单编号：1130927196009757
       订单状态： 已收货*/
        // 模板id
        String template_id = Constant.toReadPro("orderUpdate");
//        String template_id = "6nK7TsbPHQ76e9Mw9TJQoiiInThjyZacYk-cdXu5xxo";
        // 模板的主题颜色
        String topcolor = "#008000";
        // 用户昵称
        Map<String, String> stringStringMap = weiXinUtils.getNickname(parameter.get("openid"));
        String username = stringStringMap.get("nickname");
        if (Constant.toEmpty(username)) {
            username = Base64.getFromBase64(username);
        }
        // 构造json包
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        Map<String, Object> data = new LinkedHashMap<String, Object>();
        LinkedHashMap<String, String> first = new LinkedHashMap<String, String>();
//        first.put("value", "尊敬的" + username + "：\n\n您爱车"+parameter.get("licensePlateNumber")+"系统已经派单给维修中心");
//        first.put("value", "尊敬的" + username + "：\n\n很抱歉！您爱车"+parameter.get("licensePlateNumber")+" "+parameter.get("model")+"系统已经派单给维修中心");
        first.put("value", "主人：您爱车" + parameter.get("licensePlateNumber") + "维修订单，系统已经派单给维修中心，请点击查看订单详情。");
        first.put("color", "#fe3636");
        data.put("first", first);

        LinkedHashMap<String, String> keyword1 = new LinkedHashMap<String, String>();
        keyword1.put("value", parameter.get("keyword1"));
        keyword1.put("color", "#50a6ff");
        data.put("OrderSn", keyword1);

        LinkedHashMap<String, String> keyword2 = new LinkedHashMap<String, String>();
        keyword2.put("value", parameter.get("keyword2"));
        keyword2.put("color", "#50a6ff");
        data.put("OrderStatus", keyword2);

        LinkedHashMap<String, String> remark = new LinkedHashMap<String, String>();
        remark.put("value", "点击查看详情");
        remark.put("color", "#fe3636");
        data.put("remark", remark);

        map.put("touser", parameter.get("openid"));
        map.put("template_id", template_id);
        String type = parameter.get("type");
        if ("order".equals(parameter.get("orderType"))) {
            map.put("url", Constant.toReadPro("realURL") + "hfive/view/baoxian_order_detail.html?id=" + parameter.get("keyword1"));
        } else {
            map.put("url", Constant.toReadPro("realURL") + "hfive/view/order_detail.html?id=" + parameter.get("keyword1"));
        }
        map.put("topcolor", topcolor);
        map.put("data", data);
        JSONObject jsonObject = JSONObject.fromObject(map);
        return jsonObject;
    }

    /**
     * 车辆进入观察期通知模板消息
     *
     * @return JSONObject
     * @throws Exception
     * @see
     */
    public JSONObject carObservationInform(Map<String, String> parameter) throws Exception {
    /*
    恭喜！您的爱车川A88888已通过车V互助平台审核,现已进入观察期!观察期结束后车辆自动进入保障中，车V互助与您一起共建共享温暖互助社群！
    车牌号码：川A88888
    观察天数：7天
    通过时间：2018年6月18日 18:36:00
    感谢您的加入,点击查看详情。
    */
        // 模板id
        String template_id = Constant.toReadPro("carObservationInform");//真实
        //String template_id = "zpnKjYLENIm1JrnBSv_Ubh4XSLAVR1-0BRHwUslcSSE";
        // 模板的主题颜色
        String topcolor = "#008000";
        // 用户昵称
        Map<String, String> stringStringMap = weiXinUtils.getNickname(parameter.get("openid"));
        String username = stringStringMap.get("nickname");
        if (Constant.toEmpty(username)) {
            username = Base64.getFromBase64(username);
        }
        // 构造json包
        Map<String, Object> map = new LinkedHashMap<>();
        Map<String, Object> data = new LinkedHashMap<>();
        LinkedHashMap<String, String> first = new LinkedHashMap<>();
//        first.put("value", "尊敬的" + username + "：\n\n恭喜！您的爱车"+parameter.get("licensePlateNumber")+"已通过审核进入观察期，"+parameter.get("day")+"天后将进入保障中，车V互助与您一起共建共享温暖互助社群！");
        first.put("value", "主人，您爱车" + parameter.get("licensePlateNumber") + "已充值" + parameter.get("money") + "元互助金（用于分摊），现已进入观察期，观察期结束后可发起救助，救助额度" + parameter.get("amtCompensation") + "元。");
        first.put("color", "#fe3636");
        data.put("first", first);

        LinkedHashMap<String, String> keyword1 = new LinkedHashMap<>();
        keyword1.put("value", parameter.get("licensePlateNumber"));
        keyword1.put("color", "#50a6ff");
        data.put("keyword1", keyword1);

        LinkedHashMap<String, String> keyword2 = new LinkedHashMap<>();
        keyword2.put("value", parameter.get("day") + "天");
        keyword2.put("color", "#50a6ff");
        data.put("keyword2", keyword2);

        LinkedHashMap<String, String> keyword3 = new LinkedHashMap<String, String>();
        if (Constant.toEmpty(parameter.get("dayTime"))) {
            keyword3.put("value", parameter.get("dayTime"));
        } else {
            keyword3.put("value", DateUtils.getStringDateTime(new Date(), DateUtils.FORMAT_DATE_CN1));
        }
        keyword3.put("color", "#50a6ff");
        data.put("keyword3", keyword3);

        LinkedHashMap<String, String> remark = new LinkedHashMap<>();
        remark.put("value", "会员特权升级：邀请好友免费加入! 猛戳>>");
        remark.put("color", "#fe3636");
        data.put("remark", remark);

        map.put("touser", parameter.get("openid"));
        map.put("template_id", template_id);
        String type = parameter.get("type");
        map.put("url", Constant.toReadPro("realURL") + "hfive/view/car.html?type=2");
        map.put("topcolor", topcolor);
        map.put("data", data);
        JSONObject jsonObject = JSONObject.fromObject(map);
        return jsonObject;
    }

    /**
     * 账单生成通知！
     *
     * @return JSONObject
     * @throws Exception
     * @see
     */
    public JSONObject billNotifications(Map<String, String> parameter) throws Exception {
    /*
    您的本月账单已经生成
    账单月份：8月份
    账单类型：历史欠费
    您的服务费欠费已生成，请核实并缴费
    */
        // 模板id
        String template_id = Constant.toReadPro("billNotifications");   //真实
//        String template_id = "x64ngIAFv8DBVt3_YH_nW3dQ7Ss-nkCeAb3IjxlMORA";   //真实
        //String template_id = "zhe2pXNbQl8APGEalN0Y21N0IXjGJ95Rb91ovLiqU4E";   //测试
        // 模板的主题颜色
        String topcolor = "#008000";
        // 用户昵称
        Map<String, String> stringStringMap = weiXinUtils.getNickname(parameter.get("openid"));
        String username = stringStringMap.get("nickname");
        if (Constant.toEmpty(username)) {
            username = Base64.getFromBase64(username);
        }
        // 构造json包
        Map<String, Object> map = new LinkedHashMap<>();
        Map<String, Object> data = new LinkedHashMap<>();
        LinkedHashMap<String, String> first = new LinkedHashMap<>();
        first.put("value", "尊敬的" + username + "：\n\n您参与的车V互助，本周已互助小擦刮事故" + parameter.get("number") + "起，车V互助感恩有你！一起共建共享温暖社群~");
        first.put("color", "#fe3636");
        data.put("first", first);

        Date date = new Date();
        String stringDateTime = DateUtils.getStringDateTime(date, DateUtils.FORMAT_DATE);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int weekOfMonth = calendar.get(Calendar.WEEK_OF_MONTH);

        LinkedHashMap<String, String> keyword1 = new LinkedHashMap<>();
        keyword1.put("value", stringDateTime + "第" + weekOfMonth + "周");
        keyword1.put("color", "#50a6ff");
        data.put("keyword1", keyword1);

        LinkedHashMap<String, String> keyword2 = new LinkedHashMap<>();
        keyword2.put("value", "互助" + parameter.get("number") + "辆车");
        keyword2.put("color", "#50a6ff");
        data.put("keyword2", keyword2);

        LinkedHashMap<String, String> remark = new LinkedHashMap<>();
        remark.put("value", "点击查看");
        remark.put("color", "#fe3636");
        data.put("remark", remark);

        map.put("touser", parameter.get("openid"));
        map.put("template_id", template_id);
        String type = parameter.get("type");
        map.put("url", Constant.toReadPro("realURL") + "hfive/view/publicity.html");
        map.put("topcolor", topcolor);
        map.put("data", data);
        JSONObject jsonObject = JSONObject.fromObject(map);
        return jsonObject;
    }

    /**
     * 车辆保障中通知
     *
     * @param parameter
     * @return
     */
    public JSONObject carGuaranteeNotice(Map<String, String> parameter) throws Exception {
        System.out.println("进行车辆保障通知的模板发送,参数为: " + parameter);
        // 模板id
        String template_id = Constant.toReadPro("carGuaranteeNotice");
        // 模板的主题颜色
        String topcolor = "#008000";

        // 计算观察的天数(将秒转化成天数) 60 * 60 * 24 = 86400
        Long observationTime = Long.valueOf(parameter.get("observationTime"));
        Long observationDays = observationTime / (86400L * 1000);

        // 构造json包
        Map<String, Object> map = new LinkedHashMap<>();
        Map<String, Object> data = new LinkedHashMap<>();
        LinkedHashMap<String, String> first = new LinkedHashMap<>();
        first.put("value", "主人,您爱车" + parameter.get("licensePlateNumber") + "已通过" + observationDays + "天观察期，现已是互助保障中车辆。");
        first.put("color", "#fe3636");
        data.put("first", first);

        LinkedHashMap<String, String> keyword1 = new LinkedHashMap<>();
        keyword1.put("value", parameter.get("licensePlateNumber"));
        keyword1.put("color", "#50a6ff");
        data.put("keyword1", keyword1);

        LinkedHashMap<String, String> keyword2 = new LinkedHashMap<>();
        keyword2.put("value", "保障中");
        keyword2.put("color", "#50a6ff");
        data.put("keyword2", keyword2);

        LinkedHashMap<String, String> remark = new LinkedHashMap<>();
        remark.put("value", "会员特权升级：邀请好友免费加入! 猛戳>>");
        remark.put("color", "#fe3636");
        data.put("remark", remark);

        map.put("touser", parameter.get("openid"));
        map.put("template_id", template_id);
        map.put("url", Constant.toReadPro("realURL") + "hfive/view/rule_photo2.html?from=wx");
        map.put("topcolor", topcolor);
        map.put("data", data);
        JSONObject jsonObject = JSONObject.fromObject(map);
        return jsonObject;
    }

    /**
     * @author xiaowuge
     * @date 2018年9月6日
     * @version 1.0
     * 退出互助计划通知
     */
    public JSONObject quitRescue(Map<String, String> parameter) throws Exception {
        System.out.println("退出互助计划通知传入的参数为：" + parameter);
        //模板id
        String template_id = Constant.toReadPro("quitRescue");
        // 模板的主题颜色
        String topcolor = "#008000";
        // 用户昵称
        Map<String, String> stringStringMap = weiXinUtils.getNickname(parameter.get("openid"));
        String username = stringStringMap.get("nickname");
        if (Constant.toEmpty(username)) {
            username = Base64.getFromBase64(username);
        }

        // 构造json包
        Map<String, Object> map = new LinkedHashMap<>();
        Map<String, Object> data = new LinkedHashMap<>();

        LinkedHashMap<String, String> first = new LinkedHashMap<>();
        first.put("value", "主人：抱歉通知！您爱车" + parameter.get("licensePlateNumber") + "因互助金额小于等于零，已被强制退出互助计划，车V互助：我为人人，人人为我。");
        first.put("color", "#fe3636");
        data.put("first", first);

        LinkedHashMap<String, String> keyword1 = new LinkedHashMap<>();
        keyword1.put("value", parameter.get("licensePlateNumber"));
        keyword1.put("color", "#50a6ff");
        data.put("keyword1", keyword1);

        LinkedHashMap<String, String> keyword2 = new LinkedHashMap<>();
        keyword2.put("value", "车V互助计划");
        keyword2.put("color", "#50a6ff");
        data.put("keyword2", keyword2);

        LinkedHashMap<String, String> keyword3 = new LinkedHashMap<>();
        keyword3.put("value", "退出");
        keyword2.put("color", "#50a6ff");
        data.put("keyword3", keyword3);

        LinkedHashMap<String, String> remark = new LinkedHashMap<>();
        remark.put("value", "点击这里，重新加入车V互助。");
        remark.put("color", "#fe3636");
        data.put("remark", remark);

        map.put("touser", parameter.get("openid"));
        map.put("template_id", template_id);
        map.put("url", Constant.toReadPro("realURL") + "hfive/view/car_add.html?id=" + parameter.get("carId"));
        map.put("topcolor", topcolor);
        map.put("data", data);
        JSONObject jsonObject = JSONObject.fromObject(map);
        return jsonObject;
    }

    /**
     * 报告生成通知
     *
     * @return JSONObject
     * @throws Exception
     * @see
     */
    public JSONObject createNotice(Map<String, String> parameter) throws Exception {

        // 模板id
        String template_id = Constant.toReadPro("createNotice"); //真实
        // 模板的主题颜色
        String topcolor = "#008000";
        // 用户昵称
        Map<String, String> stringStringMap = weiXinUtils.getNickname(parameter.get("openid"));
        String username = stringStringMap.get("nickname");
        if (Constant.toEmpty(username)) {
            username = Base64.getFromBase64(username);
        }
        // 构造json包
        Map<String, Object> map = new LinkedHashMap<>();
        Map<String, Object> data = new LinkedHashMap<>();
        String number = parameter.get("number");

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(calendar.YEAR);
        int month = calendar.get(calendar.MONTH);
        int day = calendar.get(calendar.DATE);
        String date = year + "年" + (month + 1) + "月" + day + "日";

        Map<String, String> first = new LinkedHashMap<>();
//        first.put("value", username + "，收到报告，说明您持续享有车V互助会员权益");
        first.put("value", "主人，请查收互助报告，收到报告说明您正在享受车V互助会员权益。");
        first.put("color", "#fe3636");
        data.put("first", first);

        LinkedHashMap<String, String> keyword1 = new LinkedHashMap<>();
        keyword1.put("value", "互助报告");
        keyword1.put("color", "#50a6ff");
        data.put("keyword1", keyword1);

        LinkedHashMap<String, String> keyword2 = new LinkedHashMap<>();
        keyword2.put("value", date);
        keyword2.put("color", "#50a6ff");
        data.put("keyword2", keyword2);

        LinkedHashMap<String, String> remark = new LinkedHashMap<>();
        remark.put("value", "请点击查看 “救助记录”与“账户余额” >>");
        remark.put("color", "#fe3636");
        data.put("remark", remark);

        map.put("touser", parameter.get("openid"));
        map.put("template_id", template_id);
        map.put("url", Constant.toReadPro("realURL") + "hfive/view/wallet.html");
        map.put("topcolor", topcolor);
        map.put("data", data);
        JSONObject jsonObject = JSONObject.fromObject(map);
        return jsonObject;
    }

    /**
     * 互助额度不足500
     *
     * @return JSONObject
     * @throws Exception
     * @see
     */
    public JSONObject lackAmount(Map<String, String> parameter) throws Exception {

        // 模板id
        String template_id = Constant.toReadPro("lackAmount"); //真实
        // 模板的主题颜色
        String topcolor = "#008000";
        // 用户昵称
        Map<String, String> stringStringMap = weiXinUtils.getNickname(parameter.get("openid"));
        String username = stringStringMap.get("nickname");
        if (Constant.toEmpty(username)) {
            username = Base64.getFromBase64(username);
        }
        // 构造json包
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        Map<String, Object> data = new LinkedHashMap<String, Object>();
        String number = parameter.get("number");

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(calendar.YEAR);
        int month = calendar.get(calendar.MONTH);
        int day = calendar.get(calendar.DATE);
        String date = year + "年" + (month + 1) + "月" + day + "日";

        LinkedHashMap<String, String> first = new LinkedHashMap<String, String>();
//        first.put("value", username + "，收到报告，说明您持续享有车V互助会员权益");
        first.put("value", "主人，您爱车" + parameter.get("licensePlateNumber") + "擦挂救助额度已经低于500元，为保障您的互助权益，平台已开通会员额度提升计划，邀请好友加入即可提升擦挂额度，赶快邀请好友加入吧！");
        first.put("color", "#fe3636");
        data.put("first", first);

        LinkedHashMap<String, String> keyword1 = new LinkedHashMap<String, String>();
        keyword1.put("value", username);
        keyword1.put("color", "#50a6ff");
        data.put("keyword1", keyword1);

        LinkedHashMap<String, String> keyword2 = new LinkedHashMap<String, String>();
        keyword2.put("value", parameter.get("amtCompensation"));
        keyword2.put("color", "#50a6ff");
        data.put("keyword2", keyword2);

        LinkedHashMap<String, String> keyword3 = new LinkedHashMap<String, String>();
        keyword3.put("value", date);
        keyword3.put("color", "#50a6ff");
        data.put("keyword3", keyword3);

        LinkedHashMap<String, String> remark = new LinkedHashMap<String, String>();
        remark.put("value", "车V互助：我为人人，人人为我。去分享吧  >>");
        remark.put("color", "#fe3636");
        data.put("remark", remark);

        map.put("touser", parameter.get("openid"));
        map.put("template_id", template_id);
        map.put("url", Constant.toReadPro("realURL") + "hfive/view/share.html");
        map.put("topcolor", topcolor);
        map.put("data", data);
        JSONObject jsonObject = JSONObject.fromObject(map);
        return jsonObject;
    }


    /**
     * [您已为川A12345充值成功.
     * 充值金额：￥99.00元
     * 充值时间：2018-11-25 18:35:34
     * 当前余额：￥103.10元
     * 点击查看详情>>]
     * 充值成功提醒
     *
     * @return JSONObject
     * @throws Exception
     * @see
     */
    public JSONObject rechargeSuccess(Map<String, String> parameter) throws Exception {

        // 模板id
        String template_id = Constant.toReadPro("rechargeSuccess"); //真实
        // 模板的主题颜色
        String topcolor = "#008000";
        // 用户昵称
        Map<String, String> stringStringMap = weiXinUtils.getNickname(parameter.get("openid"));
        String username = stringStringMap.get("nickname");
        if (Constant.toEmpty(username)) {
            username = Base64.getFromBase64(username);
        }
        // 构造json包
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        Map<String, Object> data = new LinkedHashMap<String, Object>();
        String number = parameter.get("number");

        String date = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(new Date());

//        Calendar calendar = Calendar.getInstance();
//        int year = calendar.get(calendar.YEAR);
//        int month = calendar.get(calendar.MONTH);
//        int day = calendar.get(calendar.DATE);
//        String date = year + "年" + (month+1) + "月" + day + "日";

        LinkedHashMap<String, String> first = new LinkedHashMap<String, String>();
//        first.put("value", username + "，收到报告，说明您持续享有车V互助会员权益");
        first.put("value", "主人，您已为" + parameter.get("licensePlateNumber") + "充值成功");
        first.put("color", "#fe3636");
        data.put("first", first);

        LinkedHashMap<String, String> keyword1 = new LinkedHashMap<String, String>();
        keyword1.put("value", parameter.get("amt"));
        keyword1.put("color", "#50a6ff");
        data.put("keyword1", keyword1);

        LinkedHashMap<String, String> keyword2 = new LinkedHashMap<String, String>();
        keyword2.put("value", date);
        keyword2.put("color", "#50a6ff");
        data.put("keyword2", keyword2);

        LinkedHashMap<String, String> keyword3 = new LinkedHashMap<String, String>();
        keyword3.put("value", parameter.get("amtCooperation"));
        keyword3.put("color", "#50a6ff");
        data.put("keyword3", keyword3);

        LinkedHashMap<String, String> remark = new LinkedHashMap<String, String>();
        remark.put("value", "点击查看详情 >>");
        remark.put("color", "#fe3636");
        data.put("remark", remark);

        map.put("touser", parameter.get("openid"));
        map.put("template_id", template_id);
        map.put("url", Constant.toReadPro("realURL") + "hfive/view/wallet.html");
        map.put("topcolor", topcolor);
        map.put("data", data);
        JSONObject jsonObject = JSONObject.fromObject(map);
        return jsonObject;
    }


    /**
     * [亲爱的小主，您2017年5月份的月账单数据已生成，请过目！
     * 账户编号：1234567890
     * 账单月份：2017年05月
     * 请点击详细查看，感谢您对我们的的支持！]
     *
     * @return JSONObject
     * @throws Exception
     * @see
     */
    public JSONObject monthBill(Map<String, String> parameter) throws Exception {

        // 模板id
        String template_id = Constant.toReadPro("monthBill"); //真实
        // 模板的主题颜色
        String topcolor = "#008000";
        // 用户昵称
        Map<String, String> stringStringMap = weiXinUtils.getNickname(parameter.get("openid"));
        String username = stringStringMap.get("nickname");
        if (Constant.toEmpty(username)) {
            username = Base64.getFromBase64(username);
        }
        // 构造json包
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        Map<String, Object> data = new LinkedHashMap<String, Object>();
        String number = parameter.get("number");

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(calendar.YEAR);
        int month = calendar.get(calendar.MONTH);
        String date = year + "年" + month + "月";

//        String date = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS")).format(new Date());

        LinkedHashMap<String, String> first = new LinkedHashMap<String, String>();
//        first.put("value", username + "，收到报告，说明您持续享有车V互助会员权益");
        first.put("value", (month+1)+"月了，您是否很忙碌？\n别忘了保重身体，注意行车安全哦，您"+ date +"份的月账单如下");
        first.put("color", "#fe3636");
        data.put("first", first);

        LinkedHashMap<String, String> keyword1 = new LinkedHashMap<String, String>();
        keyword1.put("value", parameter.get("customerPN"));
        keyword1.put("color", "#50a6ff");
        data.put("keyword1", keyword1);

        LinkedHashMap<String, String> keyword2 = new LinkedHashMap<String, String>();
        keyword2.put("value", date);
        keyword2.put("color", "#50a6ff");
        data.put("keyword2", keyword2);

        LinkedHashMap<String, String> remark = new LinkedHashMap<String, String>();
        remark.put("value", "每次和您发生不超过#一毛钱的关系#");
        remark.put("color", "#fe3636");
        data.put("remark", remark);

        map.put("touser", parameter.get("openid"));
        map.put("template_id", template_id);
        map.put("url", Constant.toReadPro("realURL") + "hfive/view/wallet.html");
        map.put("topcolor", topcolor);
        map.put("data", data);
        JSONObject jsonObject = JSONObject.fromObject(map);
        return jsonObject;
    }

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public JSONObject invitationSuccess(Map<String, String> parameter) throws Exception {
        logger.info("邀请成功推送消息>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>{}", parameter);
        // 模板id
        String template_id = Constant.toReadPro("invitationSuccess");
        // 模板的主题颜色
        String topcolor = "#008000";

        // 构造json包
        Map<String, Object> map = new LinkedHashMap<>();
        Map<String, Object> data = new LinkedHashMap<>();

        Map<String, String> first = new HashMap<>();
        //恭喜您！已升级成V2会员，救助额度提升至1125元。
//        first.put("value", "恭喜您！已升级成V2会员，救助额度提升至" + parameter.get("amtCompensation") + "元");
        first.put("value", parameter.get("first"));
        first.put("color", "#fe3636");
        data.put("first", first);

        Map<String, String> keyword1 = new HashMap<>();
        keyword1.put("value", "V2会员");
        keyword1.put("color", "#50a6ff");
        data.put("keyword1", keyword1);

        Map<String, String> keyword2 = new HashMap<>();
        keyword2.put("value", "升级成功");
        keyword2.put("color", "#50a6ff");
        data.put("keyword2", keyword2);

        Map<String, String> remark = new HashMap<>();
//        remark.put("value", "亲，再邀请" + parameter.get("count") + "个人就可将救助额度提升至1500元。");
        remark.put("value", parameter.get("remark"));
        remark.put("color", "#fe3636");
        data.put("remark", remark);

        map.put("touser", parameter.get("openid"));
        map.put("template_id", template_id);
        map.put("url", parameter.get("url"));
        map.put("topcolor", topcolor);
        map.put("data", data);
        JSONObject jsonObject = JSONObject.fromObject(map);
        logger.info("发送内容>>>>>>>{}", jsonObject);
        return jsonObject;
    }

}