<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"></c:set>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>车V互助后台管理系统</title>
    <jsp:include page="/pages/branch/head.jsp"/>
    <link href="${ctx}/cite/css/datapicker2.css" rel="stylesheet">
</head>
<body>
<div id="wrapper">
    <!-- Sidebar -->
    <nav class="navbar navbar-fixed-top navbar-inverse main-nav" role="navigation">
        <jsp:include page="/pages/branch/mobile.jsp"/>
        <jsp:include page="/pages/branch/left.jsp"/>
    </nav>
    <!--主体内容-->
    <div id="page-wrapper">
        <div class="position-row">
            <ul class="breadcrumb index-bread">
                <li><a href="${ctx}/main.html"><i class="fa fa-dashboard"></i> 首页</a></li>
                <li>成员</li>
                <li><a href="${ctx}/maintenanceshop/list.html">渠道名单</a></li>
                <li class="active"><i class="fa fa-file-alt"></i></li>
            </ul>
        </div>
        <div class="row margin-top-ele">
            <div class="col-lg-12 col-xs-12">
                <div class="panel panel-info">
                    <div class="panel-heading">
                        <h3 class="panel-title">新建商家</h3>
                    </div>
                    <div class="panel-body">
                        <ul class="details-li" id="text-info">
                            <li class="details-tt">基本信息</li>
                            <li class="details-input"><span class="pull-left">商家名称</span><div class="pull-left"><input class="form-control" type="text" placeholder="店铺名称" maxlength="100"></div></li>
                            <li class="details-input"><span class="pull-left">负责人员</span><div class="pull-left"><input class="form-control" type="text" placeholder="店长姓名" maxlength="50"></div></li>
                            <li class="details-input"><span class="pull-left">手机号码</span><div class="pull-left"><input class="form-control" type="tel" placeholder="手机号码" maxlength="11"></div></li>
                            <li class="details-input"><span class="pull-left">营业时间</span><div class="pull-left sm-input"><input class="form-control" type="text" placeholder="开始时间"></div><div class="pull-left sm-input"><input class="form-control" type="text" placeholder="结束时间"></div></li>
                            <li class="details-input"><span class="pull-left">接单半径</span><div class="pull-left"><input class="form-control" type="tel" placeholder="接单半径（公里数）" maxlength="10"></div></li>
                            <li class="details-input"><span class="pull-left">月接单量</span><div class="pull-left"><input class="form-control" type="tel" placeholder="月接单量（单/月）" maxlength="10"></div></li>
                            <li class="details-input dropdown-box"><span class="pull-left">定价级别</span><div class="dropdown pull-left"><button class="btn btn-default dropdown-toggle" type="button" id="dropdownMenu1" data-toggle="dropdown" aria-haspopup="true" aria-expanded="true"><span class="dropdown-value">A</span> <span class="caret"></span></button>
                            <ul class="dropdown-menu" aria-labelledby="dropdownMenu1">
                              <li><a class="dropdown-item" href="javascript:;">A</a></li>
                              <li><a class="dropdown-item" href="javascript:;">B</a></li>
                              <li><a class="dropdown-item" href="javascript:;">C</a></li>
                            </ul></div><div class="pull-left"><button type="button" class="btn btn-info">去编辑</button></div></li>
                            <li class="details-input dropdown-box"><span class="pull-left">结算级别</span><div class="dropdown pull-left"><button class="btn btn-default dropdown-toggle" type="button" id="dropdownMenu2" data-toggle="dropdown" aria-haspopup="true" aria-expanded="true"><span class="dropdown-value">A</span> <span class="caret"></span></button>
                            <ul class="dropdown-menu" aria-labelledby="dropdownMenu2">
                              <li><a class="dropdown-item" href="javascript:;">A</a></li>
                              <li><a class="dropdown-item" href="javascript:;">B</a></li>
                              <li><a class="dropdown-item" href="javascript:;">C</a></li>
                            </ul></div><div class="pull-left"><button type="button" class="btn btn-info">去编辑</button></div></li>
                            <li class="details-input full-input"><span class="pull-left">商家地址</span><div class="pull-left xl-input"><input id="area-select" class="form-control form-readonly" type="text" placeholder="省-市-区/县" maxlength="100" readonly></div></li>
                            <li class="details-input full-input"><span class="pull-left">　　　　</span><div class="pull-left xl-input"><input class="form-control" type="text" placeholder="详细地址" maxlength="100"></div></li>
                            <li class="details-input full-input"><span class="pull-left">店铺描述<br>（选填）</span><div class="pull-left"><textarea class="form-control" placeholder="认真填写店铺描述，有助于别人了解商家店铺哦"></textarea></div></li>
                            <li class="details-tt tt2">图片信息（选填）</li>
                        </ul>
                        
                        <div class="card-info">
                            <ul class="img-info">
                                <li>
                                    <a class="card-img upload-img" href="javascript:;">
                                        <input id="fileupload-yyzz" class="fileupload" type="file" accept="image/jpg,image/jpeg,image/png,image/bmp,image/gif">
                                        <span class="upload-cel"></span>
                                    </a>
                                    <p>营业执照</p>
                                </li>
                                <li>
                                    <a class="card-img upload-img" href="javascript:;">
                                        <input id="fileupload-logo" class="fileupload" type="file" accept="image/jpg,image/jpeg,image/png,image/bmp,image/gif">
                                        <span class="upload-cel"></span>
                                    </a>
                                    <p>店铺logo</p>
                                </li>
                                <li>
                                    <a class="card-img upload-img upload-add" href="javascript:;">
                                        <input id="fileupload3" class="fileupload-m" type="file" accept="image/jpg,image/jpeg,image/png,image/bmp,image/gif" multiple>
                                    </a>
                                    <p>添加门店照片</p>
                                </li>
                            </ul>
                        </div>
                        <div class="check-btn">
                            <button type="button" class="btn btn-primary" onclick="addShop()">　提交　</button>
                        </div>
                        <%--微信推送按钮--%>
                        <%--<div id="btn"></div>--%>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<script type="text/javascript" src="${ctx}/cite/plugins/city_area.js"></script>
<script type="text/javascript" src="${ctx}/cite/plugins/datapicker2.js"></script>
<script type="text/javascript">
var datapick_area = datapicker2({
  data: area_json,
  active_level: 3,
  event_ok: function(data) {
    area = [data[0].value, data[1].value, data[2].value];
    $('#area-select').val(data[0].name + '-' + data[1].name + '-' + data[2].name);
  }
});  // 地区选择器
var area = [];
var uploadimg_max = 4;  // 门店照片最大上传张数
var uploadimg_cur = 0;  // 门店照片当前上传张数
var img_yyzz = null;  // 营业执照照片上传路径
var img_logo = null;  // 店铺logo图片上传路径
var img_mendian = [];  // 门店照片上传路径
$('#area-select').on('click', function(){
  datapick_area.show(area);
});
$('.card-info').on('click', '.upload-cel', function(){
  var upload = $(this).siblings('input').val('');
  $(this).parent().removeClass('uploaded');
  $(this).siblings('img').remove();
  if (upload.attr('id') === 'fileupload-yyzz') {
    img_yyzz = null;
  } else if (upload.attr('id') === 'fileupload-logo') {
    img_logo = null;
  }
}).on('click', '.upload-del', function(){
  var item = $(this).closest('li');
  var index = item.index() - 2;
  item.remove();
  uploadimg_cur -= 1;
  img_mendian.splice(index, 1);
  $('.fileupload-m').closest('li').show();
  $('.card-info .mendian').each(function(index, item){
    $(item).text('门店照片' + (index + 1));
  })
})
$('.fileupload').on('change', function(){
  var fileList = this.files;
  if (fileList.length <= 0) {
    return;
  }
  var imgUrlStr = '';
  if (window.createObjectURL != undefined) {
      imgUrlStr = window.createObjectURL(fileList[0]);
  } else if (window.URL != undefined) {
      imgUrlStr = window.URL.createObjectURL(fileList[0]);
  } else if (window.webkitURL != undefined) {
      imgUrlStr = window.webkitURL.createObjectURL(fileList[0]);
  }
  if (imgUrlStr) {
    $(this).parent().addClass('uploaded');
    $(this).siblings('img').remove();
    $('<img src="' + imgUrlStr + '" onload="resetImg(this)">').insertAfter(this);
    if ($(this).attr('id') === 'fileupload-yyzz') {
      img_yyzz = fileList[0];
    } else if ($(this).attr('id') === 'fileupload-logo') {
      img_logo = fileList[0];
    }
  }
})
$('.fileupload-m').on('change', function(){
  var fileList = this.files;
  if (fileList.length <= 0) {
    return;
  }
  for (var i = 0, j = fileList.length; i < j; i++) {
    if (uploadimg_cur >= uploadimg_max) {
      break;
    }
    var imgUrlStr = '';
    if (window.createObjectURL != undefined) {
        imgUrlStr = window.createObjectURL(fileList[i]);
    } else if (window.URL != undefined) {
        imgUrlStr = window.URL.createObjectURL(fileList[i]);
    } else if (window.webkitURL != undefined) {
        imgUrlStr = window.webkitURL.createObjectURL(fileList[i]);
    }
    if (imgUrlStr) {uploadimg_cur += 1;
      if (uploadimg_cur >= uploadimg_max) {
        $('.fileupload-m').closest('li').hide();
      }
      img_mendian.push(fileList[i]);
      $('<li><a class="card-img upload-img uploaded" href="javascript:;"><img src="' + imgUrlStr + '" onload="resetImg(this)"><span class="upload-del"></span></a><p class="mendian">门店照片' + uploadimg_cur + '</p></li>').insertBefore($(this).closest('li'));
    }
  }
  $(this).val('');
})
$('.dropdown-box').on('click', '.dropdown-item', function(){
  $(this).closest('.dropdown-box').find('.dropdown-value').text($(this).text());
})
function addShop() {
  var formData = new FormData();
  formData.append("营业执照", img_yyzz);
  formData.append("店铺logo", img_logo);
  for(var i = 0, j = img_mendian.length; i < j; i++){
    formData.append("门店照片", img_mendian[i]);
  }
  $.ajax({
    type: 'post',
    url: '接口地址',
    data: formData,
    success:function(data){
      
    },
    error:function(e){
      
    }
  });
}
</script>
</body>
</html>