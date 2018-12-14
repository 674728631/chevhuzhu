"use strict";!function(){var e,t,n,o,a,i=!1,c=getUrlPar("id"),s=getUrlPar("mid"),r=checkLogin(!0),l=preview({fixed:!0,background:"#000000",number:!0,point:!0,fill:"fix"}),d=null,f={ads:""};function u(e){i||(showLoad(),i=!0,goAPI({url:_api.order_pay,data:e,traditional:!0,success:function(e){wx.chooseWXPay({appId:e.data.appId,timestamp:e.data.timeStamp.toString(),nonceStr:e.data.nonceStr,package:e.data.package,signType:e.data.signType,paySign:e.data.paySign,success:function(e){showConfirm({str:"支付成功",btn_yes:{event_click:function(){window.location.reload()}}})}})},error:function(e){showConfirm({str:e})},complete:function(){hideLoad(),i=!1}}))}function p(e){i||(showLoad(),i=!0,goAPI({url:_api.order_report,data:e,success:function(e){showConfirm({str:"操作成功",btn_yes:{event_click:function(){window.location.reload()}}})},error:function(e){showAlert(e)},complete:function(){hideLoad(),i=!1}}))}juicer.register("build_stime",function(e){var t=Math.floor(e/3600);if(t>0){var n=Math.ceil(e%3600/60);return n>=60?t+1+"小时":0===n?t+"小时":t+"小时"+n+"分"}return Math.ceil(e/60)+"分"}),r&&(c?function m(){showLoad();goAPI({url:_api.order_detail,data:{token:r.token,eventNo:c,messageId:s},success:function(s){4!=(e=s.data).statusEvent||10==e.isInvalid?(_config.is_wx_mobile&&!_config.wx_config&&configWXJSSDK(["previewImage","chooseWXPay"].concat(_config.wx_share),{success:function(){d&&u(d)}}),e.commentLabelContent?e.commentLabelContent=e.commentLabelContent.split("_"):e.commentLabelContent=null,"string"===$.type(e.accidentImg)?e.accidentImg=e.accidentImg.split("_"):e.accidentImg||(e.accidentImg=[]),"string"===$.type(e.assertImg)?e.assertImg=e.assertImg.split("_"):e.assertImg||(e.assertImg=[]),"string"===$.type(e.repairImg)?e.repairImg=e.repairImg.split("_"):e.repairImg||(e.repairImg=[]),$(".container").html(juicer($("#main-tpl").html(),e)),10!=e.isInvalid&&function(e){var t=0;t=1==e||2==e?1:3==e||10==e||11==e||12==e||21==e||22==e||23==e?2:31==e?3:41==e||51==e||52==e?4:5;$(".step-box .point").each(function(e){e<t&&$(this).addClass("active")})}(e.statusEvent),$("#time").length>0&&function e(t){if(t>=0){var n,o=Math.floor(t/3600),a=Math.floor(t%3600/60),i=Math.floor(t%60);o>0?(n=o+"时",n+=a>9?a+"分":"0"+a+"分",n+=i>9?i+"秒":"0"+i+"秒"):a>0?(n=a+"分",n+=i>9?i+"秒":"0"+i+"秒"):n=i+"秒",$("#time").text(n),setTimeout(function(){e(t-1)},1e3)}else $("#time").text("已超时")}(e.interval),$(".photo-box").on("click",".photo-item",function(){var e=$(this),t=e.index(),n=[];e.closest(".photo-box").find(".photo-img img").each(function(){n.push($(this).attr("src"))}),_config.is_wx?_config.wx_config&&wx.previewImage({current:n[t],urls:n}):l.show({imgs:n,index:t+1})}),$("#map-confirm").on("click",function(){f.ads&&$("#caddress").val(f.ads),$("#map-layer").addClass("hide")}),$("#btn-pay").on("click",function(){!function(e){if(i)return;if(!_config.is_wx_mobile)return void showConfirm({str:"请使用微信移动客户端打开该页面再进行支付"});_config.wx_config?u(e):d=e}({token:r.token,eventNo:e.eventNo})}),$("#btn-report").on("click",function(){showConfirm({class:"shadow-blue",cover_background:"rgba(255,255,255,0.5)",cover_close:!0,str:"亲，维修中我们有很多不足地方需要改进，感谢对车V互助的支持，请认真填写您的投诉内容，方便后续工作人员与您联系。",node:'<div id="report-box"><textarea id="report" class="fsize-14" maxlength="200" placeholder="亲，请填写您的投诉内容，方便我们工作人员核实处理！"></textarea><p id="report-status" class="tr">0/200</p></div>',event_init:function(){$("#report").off().on("input",function(){$("#report-status").text($("#report").val().length+"/200")})},btn_yes:{click_close:!1,event_click:function(){var t=$("#report").val().trim();0!==t.length?p({token:r.token,eventNo:e.eventNo,content:t}):showAlert("请填写投诉内容")}},btn_cancel:{str:"取消"}})}),$("#btn-resolve").on("click",function(){showConfirm({str:"您确定投诉问题已经解决并且确认维修中心已经交车给您了？",class:"shadow-blue",cover_background:"rgba(255,255,255,0.5)",cover_close:!0,btn_yes:{event_click:function(){p({token:r.token,eventNo:e.eventNo})}},btn_cancel:{str:"取消"}})}),$("#btn-confirm").on("click",function(){showConfirm({title:"亲，您确定维修中心已经交车给您了？",str:"维修中心交车给您前，请您认真核实汽车维修的是否满意",color:"#3196fe",class:"shadow-blue",cover_background:"rgba(255,255,255,0.5)",cover_close:!0,btn_yes:{event_click:function(){!function(e){if(i)return;showLoad(),i=!0,goAPI({url:_api.order_confirm,data:e,success:function(e){showConfirm({str:"操作成功",btn_yes:{event_click:function(){window.location.reload()}},btn_no:{str:"去评价",event_click:function(){window.location.href="/hfive/view/score.html?id="+c}}})},error:function(e){showConfirm({str:e})},complete:function(){hideLoad(),i=!1}})}({token:r.token,eventNo:e.eventNo})}},btn_cancel:{str:"取消"}})}),$("#btn-apply").on("click",function(){showConfirm({class:"shadow-blue contact",cover_background:"rgba(255,255,255,0.5)",node:'<div class="citem fbox-ac"><p>接车地址</p><div id="btn-address" class="ibox fbox-ac fbox-f1 hover"><input id="caddress" type="text" maxlength="100" value="'+f.ads+'" placeholder="点击打开地图" readonly></div></div><div class="citem fbox-ac"><p>联系人　</p><div class="ibox fbox-ac fbox-f1"><input id="cname" type="text" maxlength="50" placeholder="请输入联系人姓名" value="'+e.nameCarOwner+'"><a class="fbox-cc cdel hover hide" href="javascript:;"><span></span></a></div></div><div class="citem fbox-ac"><p>手机号　</p><div class="ibox fbox-ac fbox-f1"><input id="cphone" type="tel" maxlength="11" placeholder="请输入联系人手机号" value="'+r.phone+'"><a class="fbox-cc cdel hover hide" href="javascript:;"><span></span></a></div></div><div class="citem fbox-ac"><p>接车时间</p><div class="ibox fbox-ac fbox-f1"><input id="cdate" class="hover" type="text" maxlength="50" placeholder="请选择接车时间" value="" readonly><a class="fbox-cc cdel hover hide" href="javascript:;"><span></span></a></div></div>',btn_yes:{click_close:!1,event_click:function(){var e=$("#cname").val().trim(),t=$("#cphone").val().trim(),n=$("#cdate").val().trim();f.ads?0!==e.length?0!==t.length?validatePhone(t,!0)?0!==n.length?function(e){if(i)return;showLoad(),i=!0,goAPI({url:_api.submit_contacter,data:e,success:function(e){showConfirm({str:"提交成功",btn_yes:{event_click:function(){window.location.reload()}}})},error:function(e){showAlert(e)},complete:function(){hideLoad(),i=!1}})}({token:r.token,eventNo:c,nameCarOwner:e,telCarOwner:t,reciveCarTime:n,place:f.ads,longitude:1e6*f.lng,latitude:1e6*f.lat}):showAlert("请选择接车时间"):showAlert("手机号格式有误"):showAlert("请填写手机号"):showAlert("请填写联系人"):showAlert("请选择接车地址")}},btn_cancel:{str:"取消"},event_init:function(){!function(){if(a)return;(a=new AMap.Map("map-main",{center:[104.065764,30.657462],zoom:14})).on("click",function(e){a.setCenter(e.lnglat),n&&n.getAddress(e.lnglat,function(t,n){"complete"===t&&(f={lat:e.lnglat.lat,lng:e.lnglat.lng,ads:n.regeocode.formattedAddress},$("#map-address").text(f.ads))}),o?o.setPosition(e.lnglat):o=new AMap.Marker({position:e.lnglat,map:a})}),a.plugin(["AMap.Geolocation","AMap.Geocoder","AMap.ToolBar","AMap.Scale"],function(){t=new AMap.Geolocation({enableHighAccuracy:!0,timeout:5e3,maximumAge:0,convert:!0,showButton:!0,buttonPosition:"LB",buttonOffset:new AMap.Pixel(10,20),showMarker:!0,showCircle:!0,panToLocation:!0,zoomToAccuracy:!0}),n=new AMap.Geocoder({batch:!1}),a.addControl(new AMap.ToolBar),a.addControl(new AMap.Scale),a.addControl(t),t.getCurrentPosition(),$("#map-address").text("自动定位中"),$("#caddress").val("自动定位中"),AMap.event.addListener(t,"complete",function(e){o?o.setPosition(e.position):o=new AMap.Marker({position:e.position,map:a}),f={lat:e.position.lat,lng:e.position.lng,ads:e.formattedAddress},$("#map-address").text(f.ads),$("#caddress").val(f.ads)}),AMap.event.addListener(t,"error",function(e){showAlert("定位失败，错误原因："+e.message),"自动定位中"===$("#map-address").text()&&$("#map-address").text("请选择当前位置"),$("#caddress").val("")})})}(),$("#btn-address").off().on("click",function(){$("#map-layer").removeClass("hide")}),$(".contact input").off().on("input",function(){var e=$(this),t=e.val().trim();t.length>0?e.siblings(".cdel").removeClass("hide"):e.siblings(".cdel").addClass("hide")}),$(".contact .cdel").off().on("click",function(){var e=$(this);e.addClass("hide").siblings("input").val("")}),$("#cdate").off().on("click",function(){var e={},t={},n={},o=[],a={},i={},c={},s=new Date,r=new Date(s.getFullYear(),s.getMonth(),s.getDate(),s.getHours()+3,s.getMinutes(),s.getSeconds()),l=new Date(s.getFullYear(),s.getMonth(),s.getDate(),12,0,0),d=new Date(s.getFullYear(),s.getMonth(),s.getDate(),17,59,59),f={},u={};r<l?r=l:r>d&&(r=null);for(var p=0;p<7;p++)if(0===p&&r||p>0){var m=new Date(s.getFullYear(),s.getMonth(),s.getDate()+p).formatDate("yyyy-MM-dd");a[p]={name:m}}for(var p=12;p<18;p++)i[p]={name:""+p};for(var p=0;p<60;p++)c[p]={name:p<10?"0"+p:""+p};if(r){for(var p=r.getHours();p<18;p++)f[p]={name:""+p};for(var p=r.getMinutes();p<60;p++)u[p]={name:p<10?"0"+p:""+p};o.push(a,f,u)}else o.push(a,i,c);var v=datapicker({class:"node",node:'<p class="time-title fbox-cc">预计接车时间</p><p class="time-txt fbox-cc"></p><div class="time-header fbox-ac"><p class="fbox-f1">日期<p/><p class="fbox-f1">时<p/><p class="fbox-f1">分</p></div>',event_scroll:function(e,t){r&&e[0].name===r.formatDate("yyyy-MM-dd")?(0===t?(o[1]=f,f[e[1].name]&&r.getHours()!=e[1].name?o[2]=c:o[2]=u):1===t&&(e[1].name==r.getHours()?o[2]=u:o[2]=c),v.show([e[0].value,e[1].value,e[2].value],o)):0===t?(o=[a,i,c],v.show([e[0].value,e[1].value,e[2].value],o)):$(".time-txt").text(e[0].name+"　"+e[1].name+":"+e[2].name)},event_show:function(o){e=o[0],t=o[1],n=o[2],$(".time-txt").text(o[0].name+"　"+o[1].name+":"+o[2].name)},event_ok:function(o){e=o[0],t=o[1],n=o[2],$("#cdate").val(e.name+" "+t.name+":"+n.name+":00")}});v.show([e.value,t.value,n.value],o)})}})})):window.location.href="/hfive/view/order_add2.html?oid="+c},error:function(e){showConfirm({str:e,btn_yes:{str:"重新加载",event_click:m}})},complete:function(){hideLoad()}})}():showConfirm({str:"缺少订单ID",btn_yes:{str:"返回上一页",href:"javascript:history.go(-1);"},btn_no:{str:"回到首页",href:"/hfive/view/index.html"}}))}();