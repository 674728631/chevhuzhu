"use strict";!function(){var t,e,n,i,a,r,s=!1,c=checkLogin(!0),o=getUrlPar("id"),l="",u=area_json[51].name,h="510100",d="成都市",f=u+d,b=area_json[51].value,v={};function m(i){$(".container").html(juicer($("#main-tpl").html(),i)),i?(i,i.drvingCity&&(f=i.drvingCity),t=i.licensePlateNumber.substr(0,1).toUpperCase(),e=i.licensePlateNumber.substr(1,1).toUpperCase(),l=i.licensePlateNumber.substr(2).toUpperCase()):(t="川",e="A"),n=vkeyboard({type:"keyboard",init:l,input:$("#cnum-keyboard"),max_length:10}),$("#city").val(f),$("#cnum1").val(t+e),$("#btn-help").on("click",function(){showConfirm({str:"因维修覆盖范围，目前互助只针对大成都（都江堰 、简阳 、眉山 、资阳除外）范围车主",btn_yes:{str:"我知道了"}})}),$("#city-picker").on("click",function(){a.show([h])}),$("#cnum-picker").on("click",function(){r.show([t,e])}),$("#btn-pay").on("click",function(){l=n.val().trim(),0!==f.length?0!==l.length?validateCar(t+e+l)?showConfirm({title:"重要提示",str:"请再次确认您的车牌是否为"+t+e+l,btn_close:!0,btn_cancel:{str:"重填"},btn_yes:{str:"下一步",event_click:function(){showConfirm({title:"重要提示",str:'<div class="fsize-14"><span class="txt-lv1" style="line-height:2">A 加入条件</span><br>（1）长期行驶地在大成都范围私家车<br>（2）8年以内私家车<br>（3）上年度保险理赔不超过3次<br>（4）面包车不能加入<br><br><span class="txt-lv1" style="line-height:2">B 擦刮救助范围</span><br>（1）碰撞、擦刮、玻璃破碎、爆胎、倒车镜损坏均可互助<br>（2）旧伤车可加入但旧伤不赔，划痕不赔<br><br><span class="txt-lv1" style="line-height:2">C 会员账户</span><br>（1）首充9元起成为会员，享1000元/年擦刮救助额度<br>（2）每次事故分摊不超过0.1元<br>（3）账户余额不低于0元</duv>',color:"#999",class:"type3 tl",btn_close:!0,btn_yes:{str:"下一步"},event_close:function(){var n;n={id:o,token:c.token,licensePlateNumber:(t+e+l).toUpperCase(),drvingCity:f},s||(showLoad(),s=!0,goAPI({url:_api.add_car1,data:n,success:function(n){1==n.data.noNeedPay?showConfirm({class:"success",title:"您的"+(t+e+l)+"车辆信息\n提交成功！",str:"恭喜您！您的爱车进入30天观察期，观察期后正式生效",btn_yes:{str:"我知道了",href:"/hfive/view/car.html"}}):window.location.href="/hfive/view/car_add_pay.html?id="+n.data.id+"&rejoin="+getUrlPar("rejoin")},error:function(n,i,a){489==i?showConfirm({class:"fail",title:"您的"+(t+e+l)+"车辆信息提交失败！\n请重新提交",str:"失败原因："+n,btn_yes:{str:"我知道了",href:"/hfive/view/car.html"}}):showConfirm({class:"fail",title:"您的"+(t+e+l)+"车辆信息提交失败！\n请重新提交",str:"失败原因："+n,btn_yes:{str:"我知道了"}})},complete:function(){hideLoad(),s=!1}}))}})}}}):showAlert("车牌号格式不正确"):showAlert("请输入车牌号其余位数"):showAlert("请选择城市")})}i=car_number,c&&checkToken(c,function(){o?function(t,e){if(s)return;showLoad(),s=!0,goAPI({url:_api.car_detail,data:t,success:function(t){$.isFunction(e)&&e(t)},error:function(t){showConfirm({str:t})},complete:function(){hideLoad(),s=!1}})}({token:c.token,id:o},function(t){m(t.data)}):m()}),v[510100]={name:b[510100].name,value:null},a=datapicker2({data:v,event_ok:function(t){h=t[0].value,d=t[0].name,f=u+t[0].name,$("#city").val(f)}}),r=datapicker2({data:i,active_level:2,class:"carpick",event_ok:function(n){t=n[0].value,e=n[1].value,$("#cnum1").val(t+e)}})}();