"use strict";!function(){var e,o,i,t,n=!1,r=checkLogin(!0),a=getUrlPar("id"),s=getUrlPar("oid"),d="";function l(e){$("#d-owner").val(e.owner),$("#d-vin").val(e.vin),$("#d-model").val(e.model),$("#d-engine").val(e.engine_num);var o=toDate(e.register_date);o?$("#d-register").val(o.formatDate("yyyy-MM-dd")):$("#d-register").val("");var i=toDate(e.issue_date);i?$("#d-issue").val(i.formatDate("yyyy-MM-dd")):$("#d-issue").val(""),e.owner&&e.model?($("#d-owner2").text(e.owner),$("#d-model2").text(e.model),$("#driver-info").addClass("hide"),$("#driver-info2").removeClass("hide")):($("#driver-info").removeClass("hide"),$("#driver-info2").addClass("hide"))}function c(i){$(".container").html(juicer($("#main-tpl").html(),i)),_config.is_wx&&!_config.wx_config&&configWXJSSDK(["chooseImage","uploadImage"].concat(_config.wx_share),{success:function(){}}),i&&(d=(e=i).licensePlateNumber,$("#d-plate2").text(d),!a&&e.carId&&(a=e.carId),i.drivingLicense&&(o={src:i.drivingLicense,id:""},$("#img1").insertImg(i.drivingLicense,"full").closest(".btn-upload").removeClass("default"),$(".reupload-box").removeClass("hide"),l({owner:i.nameCarOwner,model:i.model,plate_num:i.licensePlateNumber,vin:i.VIN,engine_num:i.engineNum,register_date:i.registerDate,issue_date:i.issueDate}))),$("#btn-edit").on("click",function(){$("#driver-info").removeClass("hide"),$("#driver-info2").addClass("hide")}),$("#upload-car").on("click",function(){if(_config.is_wx_mobile)if(_config.wx_config){var e=$(this);wx.chooseImage({count:1,sizeType:["compressed"],sourceType:["album","camera"],success:function(i){var t=i.localIds;wxUploadPhoto({localId:t[0],success:function(i){var r,a=i.serverId;e.removeClass("default").find(".upload-img").html('<img src="'+t[0]+'" onload="resetImg(this, \'full\')" data-value="'+a+'">'),r={src:t[0],id:a},n||(showLoad({str:"图片识别中"}),n=!0,goAPI({url:_api.recognition_driver,data:{image:r.id,side:"face"},success:function(e){o=r,l(e.data),$(".reupload-box").removeClass("hide"),$window.scrollTop(0)},error:function(e){showConfirm({str:e,btn_yes:{event_click:function(){o?$("#img1").html('<img src="'+o.src+'" onload="resetImg(this, \'full\')" data-value="'+o.id+'">'):($("#img1").empty(),$(".btn-upload").addClass("default"))}}})},complete:function(){hideLoad(),n=!1}})),$(".reupload-box").removeClass("hide")}})}})}else showAlert("正在调用摄像头...请稍后再点击");else showAlert("请在手机微信客户端中上传照片")}),$("#btn-pay").on("click",function(){var e,o=$("#img1 img"),i=$("#d-owner").val().trim(),t=$("#d-model").val().trim(),l=$("#d-vin").val().trim(),c=$("#d-engine").val().trim(),u=$("#d-register").val().trim(),m=$("#d-issue").val().trim();0!==o.length?0!==i.length?0!==t.length?(e={carId:a,orderNo:s,token:r.token,base:/^http/.test(o.eq(0).attr("src"))?"":o.eq(0).data("value"),nameCarOwner:i,VIN:l,model:t,engineNum:c,registerDate:u?toDate(u).formatDate("yyyy/MM/dd"):"",issueDate:m?toDate(m).formatDate("yyyy/MM/dd"):""},n||(showLoad(),n=!0,goAPI({url:_api.add_license,data:e,success:function(e){showConfirm({class:"success",full:!0,title:"您的"+d+"车辆信息\n提交成功！",str:"我们将在一个工作日内完成理赔审核，请留意信息，车V互助为您的爱车保驾护航！",btn_yes:{str:"我知道了",href:"/hfive/view/baoxian_order.html"}})},error:function(e,o,i){showConfirm({class:"fail",full:!0,title:"您的"+d+"车辆信息提交失败！\n请重新提交",str:"失败原因："+e,btn_yes:{str:"我知道了"}})},complete:function(){hideLoad(),n=!1}}))):showAlert("行驶证品牌型号不能为空"):showAlert("行驶证所有人不能为空"):showAlert("请上传行驶证照片")})}r&&(a||s?s?function e(o,i){n||(showLoad(),n=!0,goAPI({url:_api.baoxian_order_detail,data:o,success:function(e){$.isFunction(i)&&i(e.data)},error:function(t){showConfirm({str:t,btn_yes:{str:"重新加载",event_click:function(){e(o,i)}}})},complete:function(){hideLoad(),n=!1}}))}({token:r.token,orderNo:s},c):(i={token:r.token,id:a},t=c,n||(showLoad(),n=!0,goAPI({url:_api.car_detail,data:i,success:function(e){$.isFunction(t)&&t(e.data)},error:function(e){showConfirm({str:e})},complete:function(){hideLoad(),n=!1}}))):showConfirm({str:"缺少车辆ID",btn_yes:{str:"返回上一页",href:"javascript:history.go(-1);"},btn_no:{str:"回到首页",href:"/hfive/view/index.html"}}))}();