"use strict";!function(){var e=getUrlPar("sid")||"138",r=getUrlPar("cid")||"20180510152803F82H09";_config.is_wx?($("#container").html(juicer($("#main-tpl").html(),{})),configWXJSSDK(_config.wx_share,{success:function(){shareWX({title:"@老司机 1000元私家车维修金请及时查收",desc:"4.20-4.30，时间过了就么得了哈",imgUrl:"http://www.chevhuzhu.com/hfive/img/share_event_cdcyh.jpg",link:window.location.href.replace(/#.*$/,"")})}})):showConfirm({str:"请在微信客户端中打开此页面",btn_yes:{str:null}}),$("#btn-confirm").on("click",function(){var t=$("#phone").val().trim();0!==t.length?validatePhone(t,!0)?(showLoad(),!0,goAPI({url:_api.event,data:{shopId:e,couponNo:r,mobileNumber:t},success:function(e){showConfirm({str:'<font class="txt-red">恭喜您！获得1000元擦刮维修补贴！</font><br>请长按识别二维码关注<br>完成最后一步注册即可认领成功',cover_close:!0,node:'<img src="/hfive/img/chevhuzhu_qr_s.jpg" style="width:3rem;height:3rem;">',btn_close:!0,btn_yes:{str:null}})},error:function(e,r){501==r?showConfirm({str:"您已领取过啦~<br>请识别二维码关注<br>补充车辆信息",cover_close:!0,node:'<img src="/hfive/img/chevhuzhu_qr_s.jpg" style="width:3rem;height:3rem;">',btn_close:!0,btn_yes:{str:null}}):502==r?showConfirm({str:"亲，您已加入了保障计划，请下次再来~"}):showConfirm({str:e})},complete:function(){hideLoad(),!1}})):showAlert("手机号码有误"):showAlert("请输入手机号码")})}();