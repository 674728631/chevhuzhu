package com.zccbh.util.base;



import com.alibaba.fastjson.JSON;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.QuerySendDetailsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.QuerySendDetailsResponse;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.aliyuncs.push.model.v20160801.PushRequest;
import com.aliyuncs.push.model.v20160801.PushResponse;
import com.aliyuncs.utils.ParameterHelper;
import com.zccbh.util.collect.Constant;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class SmsDemo {
    //产品名称:云通信短信API产品,开发者无需替换
    static final String product="Dysmsapi";
    //产品域名,开发者无需替换
    static final String domain = "dysmsapi.aliyuncs.com";
    // TODO 此处需要替换成开发者自己的AK(在阿里云访问控制台寻找)，下面举个例子
    static final String accessKeyId =  Constant.toReadPro("accessKeyId");
    static final String accessKeySecret = Constant.toReadPro("accessKeySecret");
//    static final String accessKeyId = "LTAIocJnw9YCKcBy";
//    static final String accessKeySecret = "AW2mZEPHpLcCLOgskdgZKTfeD5DCcq";


    public static SendSmsResponse sendSms(int type,String phone,String code) throws ClientException{
        //可自助调整超时时间
        System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
        System.setProperty("sun.net.client.defaultReadTimeout", "10000");
        //初始化acsClient,暂不支持region化
        IClientProfile profile=DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);
        DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
        IAcsClient acsClient=new DefaultAcsClient(profile);
        //组装请求对象-具体描述见控制台-文档部分内容
        SendSmsRequest request=new SendSmsRequest();
        //必填:待发送手机号
        request.setPhoneNumbers(phone);
        //必填:短信签名-可在短信控制台中找到举个例子
        request.setSignName("车V互助");
        //必填:短信模板id-可在短信控制台中找到，是id不是名字，举个例子
        // 1  登录 11互助申请和理赔订单审核通过
        // 21 互助申请和理赔订单审核未通过  31接单完成提醒
        // 41 接单提示 51 投诉
        // 61 后台通知 81 放弃接单
        // 91 活动推送
        // 2 余额不足提醒
        // 22 车妈妈激活提醒
        switch (type){
            case 1:
                try {
                    request.setTemplateCode("SMS_115750252");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 11:
                try {
/*                  SMS_129747802
                    恭喜您爱车${code}审核已通过，请尽快处理吧。
                    恭喜您爱车川A12345的互助申请审核已通过，请尽快处理吧*/
                    request.setTemplateCode("SMS_129747802");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 21:
                try {
/*                  SMS_129742850
                    很抱歉，您爱车${code}审核未通过，请查看原因吧。
                    很抱歉，您爱车川A12345的互助申请审核未通过，请查看原因吧。*/
                    request.setTemplateCode("SMS_129742850");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 31:
                try {
/*                  SMS_144150264
                    好消息，您爱车${code}的擦刮救助订单维修完成，请尽快验收吧。
                    好消息，您爱车川A12345的擦刮救助订单维修完成，请尽快验收吧。*/
                    request.setTemplateCode("SMS_144150264");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 41:
                try {
/*                  SMS_129747805
                    订单来啦！车牌${code}待接单，快去接单吧。
                    订单来啦！车牌川A1234待接单，快去接单吧*/
                    request.setTemplateCode("SMS_129747805");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 51:
                try {
/*                  SMS_129757780
                    ${code}的投诉订单，快去处理吧。
                    主人，您有一条川A1234的投诉订单，快去处理吧*/
                    request.setTemplateCode("SMS_129757780");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 61:
                try {
/*                  SMS_129747809
                    主人，车牌${code}已提交，快去审核吧。
                    主人，车牌川A12345的互助申请已提交，快去审核吧。*/
                    request.setTemplateCode("SMS_129747809");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 71:
                try {
/*                  SMS_144150270
                   恭喜您爱车${code}的擦刮救助订单定损确认完毕，请尽快支付。
                    恭喜您爱车川A12345的擦刮救助订单定损确认完毕，请尽快支付。*/
                    request.setTemplateCode("SMS_144150270");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 72:
                try {
/*                  SMS_144150260
                    主人，您的爱车${code}的订单还没有支付，将可能错过1000元爱车互助额度哦！赶快去支付吧。
                    主人，您的爱车川A123456的订单还没有支付，将可能错过1000元爱车互助额度哦！赶快去支付吧。
                    */
                    request.setTemplateCode("SMS_144150260");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 81:
                try {
/*                  SMS_129759554
                    主人，车牌${code}的订单被放弃接单，赶快去重新分单吧。
                    主人，车牌川A12345的订单被放弃接单，赶快去重新分单吧。*/
                    request.setTemplateCode("SMS_129759554");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 91:
                try {
/*                  SMS_132990306
                    @老司机，恭喜你获得由${code}送出的1000元私家车维修金，请前往“车V互助”公众号完善车辆信息后立即生效。*/
                    request.setTemplateCode("SMS_132990306");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 2:
//            	SMS_150737927
//            	余额不足提醒
//            	亲，你的余额已不足1.0元，为保障你的车V会员权益，请及时前往车V互助公众号充值。
				try {
					request.setTemplateCode("SMS_150737927");
				} catch (Exception e) {
					e.printStackTrace();
				}
            	break;
            case 22:
			try {
				//SMS_150737931
				//恭喜。。。车妈妈送出的...前往车V互助公众号认领。
				request.setTemplateCode("SMS_150737931");
			} catch (Exception e) {
				e.printStackTrace();
			}
            	break;
            default:
        }
        //可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为--必填，与模板相对应
        //request.setTemplateParam("{\"name\":\"Tom\", \"code\":\"123\"}");
        request.setTemplateParam("{\"code\":\""+code+"\"}");
        //选填-上行短信扩展码(无特殊需求用户请忽略此字段)
        //request.setSmsUpExtendCode("90997");
        //可选:outId为提供给业务方扩展字段,最终在短信回执消息中将此值带回给调用者
        //request.setOutId("yourOutId");
        //hint 此处可能会抛出异常，注意catch
        SendSmsResponse sendSmsResponse=acsClient.getAcsResponse(request);
        return sendSmsResponse;
    }

    public static QuerySendDetailsResponse querySendDetails(String bizId) throws ServerException, ClientException{
        //可自助调整超时时间
        System.setProperty("sun.net.client.defaultConnectTimeout", "60000");
        System.setProperty("sun.net.client.defaultReadTimeout", "60000");

        //初始化acsClient,暂不支持region化
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);
        DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
        IAcsClient acsClient = new DefaultAcsClient(profile);

        //组装请求对象
        QuerySendDetailsRequest request = new QuerySendDetailsRequest();
        //必填-号码
        request.setPhoneNumber("");
        //可选-流水号
        request.setBizId(bizId);
        //必填-发送日期 支持30天内记录查询，格式yyyyMMdd
        SimpleDateFormat ft = new SimpleDateFormat("yyyyMMdd");
        request.setSendDate(ft.format(new Date()));
        //必填-页大小
        request.setPageSize(10L);
        //必填-当前页码从1开始计数
        request.setCurrentPage(1L);

        //hint 此处可能会抛出异常，注意catch
        QuerySendDetailsResponse querySendDetailsResponse = acsClient.getAcsResponse(request);
        return querySendDetailsResponse;
    }

    /**
     *
     * @param system        系统类型10为苹果 20为安卓系统
     * @param type          消息的标题类型
     * @param content       文本消息
     * @param deviceId      deviceId
     * @param parameterMap  隐藏参数类型 消息类型key type, 订单号key orderNo  /订单状态key orderStatus
     * @return
     * @throws ServerException
     * @throws ClientException
     */
    public static Boolean  mobilePushMessage(int system,int type,String deviceId,String content,Map<String, Object> parameterMap) throws ServerException, ClientException{
        try {
            Long appKey =null;
            //10为苹果 其他为安卓系统
            if (system==10){
                appKey =Long.valueOf(Constant.toReadPro("iOSAppKey"));
            }else{
                appKey =Long.valueOf(Constant.toReadPro("AndroidAppKey"));
            }
            IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);
            DefaultAcsClient client = new DefaultAcsClient(profile);
            PushRequest pushRequest = new PushRequest();
            // 推送目标
            pushRequest.setAppKey(appKey);
            pushRequest.setTarget("DEVICE"); //推送目标: DEVICE:按设备推送 ALIAS : 按别名推送 ACCOUNT:按帐号推送  TAG:按标签推送; ALL: 广播推送
            pushRequest.setTargetValue(deviceId); //根据Target来设定，如Target=DEVICE, 则对应的值为 设备id1,设备id2. 多个值使用逗号分隔.(帐号与设备有一次最多100个的限制)
            //        pushRequest.setTarget("ALL"); //推送目标: DEVICE:推送给设备; ACCOUNT:推送给指定帐号,TAG:推送给自定义标签; ALL: 推送给全部
            //        pushRequest.setTargetValue("ALL"); //根据Target来设定，如Target=DEVICE, 则对应的值为 设备id1,设备id2. 多个值使用逗号分隔.(帐号与设备有一次最多100个的限制)
            pushRequest.setPushType("NOTICE"); // 消息类型 MESSAGE NOTICE
            pushRequest.setDeviceType("ALL"); // 设备类型 ANDROID iOS ALL.
            // 推送配置
            switch (type){
                case 1:
                    pushRequest.setTitle("系统消息"); // 消息的标题-系统消息
                    break;
                case 3:
                    pushRequest.setTitle("总额变动"); // 消息的标题-总额变动
                    break;
                case 5:
                    pushRequest.setTitle("互助订单"); // 消息的标题-车险订单
                    break;
                case 21:
                    pushRequest.setTitle("互助接单"); // 消息的标题-车险订单
                    break;
                default:
            }
            pushRequest.setBody(content); // 消息的内容
            // 推送配置: iOS
            pushRequest.setIOSBadge(1); // iOS应用图标右上角角标
            pushRequest.setIOSMusic("default"); // iOS通知声音
            //pushRequest.setIOSSubtitle("iOS10 subtitle");//iOS10通知副标题的内容
            pushRequest.setIOSNotificationCategory("iOS10 Notification Category");//指定iOS10通知Category
            pushRequest.setIOSMutableContent(true);//是否允许扩展iOS通知内容
            //pushRequest.setIOSApnsEnv("DEV");//iOS的通知是通过APNs中心来发送的，需要填写对应的环境信息。"DEV" : 表示开发环境 "PRODUCT" : 表示生产环境
            pushRequest.setIOSApnsEnv("PRODUCT");//iOS的通知是通过APNs中心来发送的，需要填写对应的环境信息。"DEV" : 表示开发环境 "PRODUCT" : 表示生产环境
            pushRequest.setIOSRemind(true); // 消息推送时设备不在线（既与移动推送的服务端的长连接通道不通），则这条推送会做为通知，通过苹果的APNs通道送达一次。注意：离线消息转通知仅适用于生产环境
            pushRequest.setIOSRemindBody("iOSRemindBody");//iOS消息转通知时使用的iOS通知内容，仅当iOSApnsEnv=PRODUCT && iOSRemind为true时有效
            String string = JSON.toJSONString(parameterMap);
            System.out.println("string = " + string);
            pushRequest.setIOSExtParameters(""+string+""); //通知的扩展属性(注意 : 该参数要以json map的格式传入,否则会解析出错)
            //        pushRequest.setIOSExtParameters("{\"_ENV_\":\"DEV\",\"k2\":\"v2\"}"); //通知的扩展属性(注意 : 该参数要以json map的格式传入,否则会解析出错)
            // 推送配置: Android
            pushRequest.setAndroidNotifyType("NONE");//通知的提醒方式 "VIBRATE" : 震动 "SOUND" : 声音 "BOTH" : 声音和震动 NONE : 静音
            pushRequest.setAndroidNotificationBarType(1);//通知栏自定义样式0-100
            pushRequest.setAndroidNotificationBarPriority(1);//通知栏自定义样式0-100
            //        pushRequest.setAndroidOpenType("URL"); //点击通知后动作 "APPLICATION" : 打开应用 "ACTIVITY" : 打开AndroidActivity "URL" : 打开URL "NONE" : 无跳转
            //        pushRequest.setAndroidOpenUrl("http://www.aliyun.com"); //Android收到推送后打开对应的url,仅当AndroidOpenType="URL"有效
            //        pushRequest.setAndroidActivity("com.alibaba.push2.demo.XiaoMiPushActivity"); // 设定通知打开的activity，仅当AndroidOpenType="Activity"有效
            pushRequest.setAndroidMusic("default"); // Android通知音乐
            pushRequest.setAndroidPopupActivity("com.ali.demo.PopupActivity");//设置该参数后启动辅助弹窗功能, 此处指定通知点击后跳转的Activity（辅助弹窗的前提条件：1. 集成第三方辅助通道；2. StoreOffline参数设为true）
            pushRequest.setAndroidPopupTitle("Popup Title");
            pushRequest.setAndroidPopupBody("Popup Body");
            pushRequest.setAndroidExtParameters(""+string+""); //设定通知的扩展属性。(注意 : 该参数要以 json map 的格式传入,否则会解析出错)
            //        pushRequest.setAndroidExtParameters("{\"k1\":\"android\",\"k2\":\"v2\"}"); //设定通知的扩展属性。(注意 : 该参数要以 json map 的格式传入,否则会解析出错)
            // 推送控制
            Date pushDate = new Date(System.currentTimeMillis()) ; // 30秒之间的时间点, 也可以设置成你指定固定时间
            String pushTime = ParameterHelper.getISO8601Time(pushDate);
            pushRequest.setPushTime(pushTime); // 延后推送。可选，如果不设置表示立即推送
            String expireTime = ParameterHelper.getISO8601Time(new Date(System.currentTimeMillis() + 12 * 3600 * 1000)); // 12小时后消息失效, 不会再发送
            pushRequest.setExpireTime(expireTime);
            pushRequest.setStoreOffline(true); // 离线消息是否保存,若保存, 在推送时候，用户即使不在线，下一次上线则会收到
            PushResponse pushResponse = null;
            pushResponse = client.getAcsResponse(pushRequest);
            System.out.printf("RequestId: %s, MessageID: %s\n",
                    pushResponse.getRequestId(), pushResponse.getMessageId());
            return true;
        } catch (ClientException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     *  给车v互助的 管理端推送消息
     * @param system        系统类型10为苹果 20为安卓系统
     * @param title          消息的标题
     * @param content       文本消息
     * @param deviceId      deviceId
     * @param parameterMap  隐藏参数类型 消息类型key type, 订单号key orderNo  /订单状态key orderStatus
     * @return
     * @throws ServerException
     * @throws ClientException
     */
    public static Boolean  mobilePushMessageForManager(int system,String title,String deviceId,String content,Map<String, Object> parameterMap) throws ServerException, ClientException{
        try {
            Long appKey =null;
            //10为苹果 其他为安卓系统
            if (system==10){
                appKey =Long.valueOf(Constant.toReadPro("iOSAppManagerKey"));
            }else{
                appKey =Long.valueOf(Constant.toReadPro("AndroidAppManagerKey"));
            }
            IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);
            DefaultAcsClient client = new DefaultAcsClient(profile);
            PushRequest pushRequest = new PushRequest();
            // 推送目标
            pushRequest.setAppKey(appKey);
            pushRequest.setTarget("DEVICE"); //推送目标: DEVICE:按设备推送 ALIAS : 按别名推送 ACCOUNT:按帐号推送  TAG:按标签推送; ALL: 广播推送
            pushRequest.setTargetValue(deviceId); //根据Target来设定，如Target=DEVICE, 则对应的值为 设备id1,设备id2. 多个值使用逗号分隔.(帐号与设备有一次最多100个的限制)
            //        pushRequest.setTarget("ALL"); //推送目标: DEVICE:推送给设备; ACCOUNT:推送给指定帐号,TAG:推送给自定义标签; ALL: 推送给全部
            //        pushRequest.setTargetValue("ALL"); //根据Target来设定，如Target=DEVICE, 则对应的值为 设备id1,设备id2. 多个值使用逗号分隔.(帐号与设备有一次最多100个的限制)
            pushRequest.setPushType("NOTICE"); // 消息类型 MESSAGE NOTICE
            pushRequest.setDeviceType("ALL"); // 设备类型 ANDROID iOS ALL.
            // 推送配置的title
            pushRequest.setTitle(title); // 消息的标题-系统消息
            pushRequest.setBody(content); // 消息的内容
            // 推送配置: iOS
            pushRequest.setIOSBadge(1); // iOS应用图标右上角角标
            pushRequest.setIOSMusic("default"); // iOS通知声音
            //pushRequest.setIOSSubtitle("iOS10 subtitle");//iOS10通知副标题的内容
            pushRequest.setIOSNotificationCategory("iOS10 Notification Category");//指定iOS10通知Category
            pushRequest.setIOSMutableContent(true);//是否允许扩展iOS通知内容
            pushRequest.setIOSApnsEnv(Constant.toReadPro("iosEnvironment"));//iOS的通知是通过APNs中心来发送的，需要填写对应的环境信息。"DEV" : 表示开发环境 "PRODUCT" : 表示生产环境
//            pushRequest.setIOSApnsEnv("PRODUCT");//iOS的通知是通过APNs中心来发送的，需要填写对应的环境信息。"DEV" : 表示开发环境 "PRODUCT" : 表示生产环境
            pushRequest.setIOSRemind(true); // 消息推送时设备不在线（既与移动推送的服务端的长连接通道不通），则这条推送会做为通知，通过苹果的APNs通道送达一次。注意：离线消息转通知仅适用于生产环境
            pushRequest.setIOSRemindBody("iOSRemindBody");//iOS消息转通知时使用的iOS通知内容，仅当iOSApnsEnv=PRODUCT && iOSRemind为true时有效
            String string = JSON.toJSONString(parameterMap);
            System.out.println("string = " + string);
            pushRequest.setIOSExtParameters(""+string+""); //通知的扩展属性(注意 : 该参数要以json map的格式传入,否则会解析出错)
            //        pushRequest.setIOSExtParameters("{\"_ENV_\":\"DEV\",\"k2\":\"v2\"}"); //通知的扩展属性(注意 : 该参数要以json map的格式传入,否则会解析出错)
            // 推送配置: Android
            pushRequest.setAndroidNotifyType("NONE");//通知的提醒方式 "VIBRATE" : 震动 "SOUND" : 声音 "BOTH" : 声音和震动 NONE : 静音
            pushRequest.setAndroidNotificationBarType(1);//通知栏自定义样式0-100
            pushRequest.setAndroidNotificationBarPriority(1);//通知栏自定义样式0-100
            //        pushRequest.setAndroidOpenType("URL"); //点击通知后动作 "APPLICATION" : 打开应用 "ACTIVITY" : 打开AndroidActivity "URL" : 打开URL "NONE" : 无跳转
            //        pushRequest.setAndroidOpenUrl("http://www.aliyun.com"); //Android收到推送后打开对应的url,仅当AndroidOpenType="URL"有效
            //        pushRequest.setAndroidActivity("com.alibaba.push2.demo.XiaoMiPushActivity"); // 设定通知打开的activity，仅当AndroidOpenType="Activity"有效
            pushRequest.setAndroidMusic("default"); // Android通知音乐
            pushRequest.setAndroidPopupActivity("com.ali.demo.PopupActivity");//设置该参数后启动辅助弹窗功能, 此处指定通知点击后跳转的Activity（辅助弹窗的前提条件：1. 集成第三方辅助通道；2. StoreOffline参数设为true）
            pushRequest.setAndroidPopupTitle("Popup Title");
            pushRequest.setAndroidPopupBody("Popup Body");
            pushRequest.setAndroidExtParameters(""+string+""); //设定通知的扩展属性。(注意 : 该参数要以 json map 的格式传入,否则会解析出错)
            //        pushRequest.setAndroidExtParameters("{\"k1\":\"android\",\"k2\":\"v2\"}"); //设定通知的扩展属性。(注意 : 该参数要以 json map 的格式传入,否则会解析出错)
            // 推送控制
            Date pushDate = new Date(System.currentTimeMillis()) ; // 30秒之间的时间点, 也可以设置成你指定固定时间
            String pushTime = ParameterHelper.getISO8601Time(pushDate);
            pushRequest.setPushTime(pushTime); // 延后推送。可选，如果不设置表示立即推送
            String expireTime = ParameterHelper.getISO8601Time(new Date(System.currentTimeMillis() + 12 * 3600 * 1000)); // 12小时后消息失效, 不会再发送
            pushRequest.setExpireTime(expireTime);
            pushRequest.setStoreOffline(true); // 离线消息是否保存,若保存, 在推送时候，用户即使不在线，下一次上线则会收到
            PushResponse pushResponse = null;
            pushResponse = client.getAcsResponse(pushRequest);
            System.out.printf("RequestId: %s, MessageID: %s\n",
                    pushResponse.getRequestId(), pushResponse.getMessageId());
            return true;
        } catch (ClientException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void main(String[] args) throws ClientException, InterruptedException {

        //发短信
        SendSmsResponse response = sendSms(1,"13881352548","13881352548");
        System.out.println("response = " + response);
        System.out.println("response = " + response.getMessage());
        System.out.println("response = " + response.getCode());
        Thread.sleep(3000L);

//        //查明细
////        if(response.getCode() != null && response.getCode().equals("OK")) {
//        System.out.println("请求成功");
//        QuerySendDetailsResponse querySendDetailsResponse = querySendDetails(response.getBizId());
//        System.out.println("短信明细查询接口返回数据----------------");
//        System.out.println("Code=" + querySendDetailsResponse.getCode());
//        System.out.println("Message=" + querySendDetailsResponse.getMessage());
//        int i = 0;
//        for(QuerySendDetailsResponse.SmsSendDetailDTO smsSendDetailDTO : querySendDetailsResponse.getSmsSendDetailDTOs())
//        {
//            System.out.println("SmsSendDetailDTO["+i+"]:");
//            System.out.println("Content=" + smsSendDetailDTO.getContent());
//            System.out.println("ErrCode=" + smsSendDetailDTO.getErrCode());
//            System.out.println("OutId=" + smsSendDetailDTO.getOutId());
//            System.out.println("PhoneNum=" + smsSendDetailDTO.getPhoneNum());
//            System.out.println("ReceiveDate=" + smsSendDetailDTO.getReceiveDate());
//            System.out.println("SendDate=" + smsSendDetailDTO.getSendDate());
//            System.out.println("SendStatus=" + smsSendDetailDTO.getSendStatus());
//            System.out.println("Template=" + smsSendDetailDTO.getTemplateCode());
//        }
//        System.out.println("TotalCount=" + querySendDetailsResponse.getTotalCount());
//        System.out.println("RequestId=" + querySendDetailsResponse.getRequestId());
//        double d=5/2;
//        System.out.println("d = " + d);
    }

}