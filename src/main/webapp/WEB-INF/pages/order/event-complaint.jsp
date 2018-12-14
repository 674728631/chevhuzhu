<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"></c:set>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>车V互助后台管理系统</title>
    <jsp:include page="/pages/branch/head.jsp"/>
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
                <%--<li><a href="${ctx}/main.html"><i class="fa fa-dashboard"></i> 首页</a></li>--%>
                <li><i class="fa fa-dashboard"></i> 订单</li>
                <li>互助理赔</li>
                <li>详情</li>
            </ul>
        </div>
        <div class="row margin-top-ele">
            <div class="col-lg-12 col-xs-12">
                <div class="panel panel-info">
                    <div class="panel-heading">
                        <h3 class="panel-title">订单详情</h3>
                    </div>
                    <div class="panel-body">
                        <ul class="details-li">
                            <li>车主姓名：<span id="nameCarOwner"></span></li>
                            <li>车牌号：<span id="licensePlateNumber"></span></li>
                            <li><span id="statusEvent"></span></li>
                        </ul>
                        <ul class="details-li">
                            <li>品牌型号：<span id="model"></span></li>
                            <li>手机号码：<span id="customerPN"></span></li>
                            <li>剩余理赔额度：<span id="amtCompensation"></span></li>
                        </ul>
                        <ul class="details-title">
                            <li class="font-we">维修商家信息</li>
                        </ul>
                        <ul class="details-li">
                            <li>商家名称：<span id="shopName"></span></li>
                            <li>联系电话：<span id="tel"></span></li>
                            <li>地址：<span id="address"></span></li>
                        </ul>
                        <ul class="details-title">
                            <li class="font-we">联系人信息</li>
                        </ul>
                        <ul class="details-li">
                            <li>联系人姓名：<span id="receiveManName"></span></li>
                            <li>联系电话：<span id="telCarOwner"></span></li>
                            <li>地址：<span id="place"></span></li>
                        </ul>
                        <ul class="details-title">
                            <li class="font-we">投诉信息</li>
                        </ul>
                        <ul class="details-li">
                            <li>投诉内容：<span id="content"></span></li>
                        </ul>
                        <ul class="details-title">
                            <li class="font-we">维修报告</li>
                        </ul>
                        <ul class="details-li">
                            <li>维修人员：<span id="repairmanName"></span></li>
                            <li>联系电话：<span id="reparimanTel"></span></li>
                        </ul>
                        <ul class="details-li">
                            <li>维修描述：<span id="repDescription"></span></li>
                        </ul>
                        <div class="card-info clearH">
                            <ul id="img" class="img-info"></ul>
                        </div>
                        <ul class="details-title">
                            <li class="font-we">定损报告</li>
                        </ul>
                        <ul class="details-li">
                            <li>定损人员：<span id="empName"></span></li>
                            <li>定损价格：<span id="amtAssert"></span></li>
                            <li>结算价格：<span id="amtBusiness"></span></li>
                            <li>联系电话：<span id="empTel"></span></li>
                            <li>受损程度：<span id="damageExtent"></span></li>
                        </ul>
                        <ul class="details-li">
                            <li>受损部位：<span id="damagePosition"></span></li>
                        </ul>
                        <ul class="details-li">
                            <li>定损描述：<span id="description"></span></li>
                        </ul>
                        <div class="card-info clearH">
                            <ul id="assertImg" class="img-info"></ul>
                        </div>
                        <ul class="details-title">
                            <li class="font-we">车损事故报告</li>
                        </ul>
                        <ul class="details-li">
                            <li>平台审核说明：<span id="applySuccess">未填写审核说明</span></li>
                        </ul>
                        <ul class="details-li">
                            <li>事故描述：<span id="accidentDescription">未填写事故描述</span></li>
                        </ul>
                        <div class="card-info clearH">
                            <ul class="img-info">
                                <li>
                                    <a class='card-img' onclick='scaleImg(this)'>
                                        <img id="drivingLicense" src='${ctx}/cite/images/no_pic.png' alt='' onload='resetImg(this)'>
                                    </a>
                                </li>
                            </ul>
                            <ul id="accidentImg" class="img-info"></ul>
                        </div>
                        <ul class="details-title" id="carTitle"></ul>
                        <div class="card-info" id="carPhotos"></div>
                        <ul class="details-li">
                            <li>订单编号：<span id="eventNo"></span></li>
                            <li>申请理赔时间：<span id="timeApply"></span></li>
                            <li>投诉时间：<span id="timeComplaint"></span></li>
                        </ul>
                        <div class="check-btn">
                            <button type="button" class="btn btn-primary" onclick="invalidOrder()">废弃订单</button>
                            <button type="button" class="btn btn-primary" onclick="success()">投诉已解决</button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<!-- 照片弹窗 -->
<jsp:include page="/pages/branch/imagePopup.jsp"/>
<script type="text/javascript">
    var complaintDetail;

    $(document).ready(function (){
        $.ajax({
            type : "post",
            dataType : "json",
            headers: {rqSide: ex.pc()},
            data : JSON.stringify({"eventNo":"${eventNo}"}),
            contentType : "application/json;charset=utf-8",
            url: "${ctx}/event/complaintdetail",
            success : function(datas){
                if(datas.code == "0"){
                    complaintDetail = datas.data;
                    // 基本信息
                    $("#nameCarOwner").html(ex.judgeEmpty(complaintDetail.nameCarOwner));
                    $("#licensePlateNumber").html(ex.judgeEmpty(complaintDetail.licensePlateNumber));
                    $("#model").html(ex.judgeEmpty(complaintDetail.model));
                    $("#customerPN").html(ex.judgeEmpty(complaintDetail.customerPN));
                    $("#amtCompensation").html(ex.judgeEmptyOr0(complaintDetail.amtCompensation)+"元");
                    $("#statusEvent").html(ex.statusEvent(complaintDetail.statusEvent));
                    //维修商家信息
                    $("#shopName").html(ex.judgeEmpty(complaintDetail.shopName));
                    $("#tel").html(ex.judgeEmpty(complaintDetail.tel));
                    $("#address").html(ex.judgeEmpty(complaintDetail.address));
                    //联系人信息
                    $("#receiveManName").html(ex.judgeEmpty(complaintDetail.receiveManName));
                    $("#telCarOwner").html(ex.judgeEmpty(complaintDetail.telCarOwner));
                    $("#place").html(ex.judgeEmpty(complaintDetail.place));
                    //投诉信息
                    $("#content").html(ex.judgeEmpty(complaintDetail.content));
                    //维修报告
                    $("#repairmanName").html(ex.judgeEmpty(complaintDetail.repairmanName));
                    $("#reparimanTel").html(ex.judgeEmpty(complaintDetail.reparimanTel));
                    $("#repDescription").html(ex.judgeEmpty(complaintDetail.repDescription));
                    var img = ex.judgeEmpty(complaintDetail.img);
                    var imgHtml = "";
                    if(img){
                        for (var i=0;i < img.length;i++){
                            imgHtml += "<li><a class='card-img' onclick='scaleImg(this)'>";
                            imgHtml += "<img src='"+ img[i] +"' alt='' onload='resetImg(this)'>";
                        }
                    }
                    $("#img").html(imgHtml);
                    //定损报告
                    $("#empName").html(ex.judgeEmpty(complaintDetail.empName));
                    $("#amtAssert").html(ex.judgeEmptyOr0(complaintDetail.amtAssert)+"元");
                    $("#amtBusiness").html(ex.judgeEmptyOr0(complaintDetail.amtBusiness)+"元");
                    $("#empTel").html(ex.judgeEmpty(complaintDetail.empTel));
                    $("#damageExtent").html(ex.judgeEmpty(complaintDetail.damageExtent));
                    $("#damagePosition").html(ex.judgeEmpty(complaintDetail.damagePosition));
                    $("#description").html(ex.judgeEmpty(complaintDetail.description));
                    var assertImg = ex.judgeEmpty(complaintDetail.assertImg);
                    var assertImgHtml = "";
                    if(assertImg){
                        for (var i=0;i < assertImg.length;i++){
                            assertImgHtml += "<li><a class='card-img' onclick='scaleImg(this)'>";
                            assertImgHtml += "<img src='"+ assertImg[i] +"' alt='' onload='resetImg(this)'>";
                        }
                    }
                    $("#assertImg").html(assertImgHtml);
                    // 车损事故报告
                    if(ex.judgeEmpty(complaintDetail.applySuccess)){
                        $("#applySuccess").html(ex.judgeEmpty(complaintDetail.applySuccess));
                    }
                    $("#accidentDescription").html(ex.judgeEmpty(complaintDetail.accidentDescription));
                    $("#drivingLicense").attr('src',ex.judgeEmpty(complaintDetail.drivingLicense));
                    var accidentImg = ex.judgeEmpty(complaintDetail.accidentImg);
                    var accidentImgHtml = "";
                    if(accidentImg){
                        for (var i=0;i < accidentImg.length;i++){
                            accidentImgHtml += "<li><a class='card-img' onclick='scaleImg(this)'>";
                            accidentImgHtml += "<img src='"+ accidentImg[i] +"' alt='' onload='resetImg(this)'>";
                        }
                    }
                    $("#accidentImg").html(accidentImgHtml);
                    //事故前照片
                    if(ex.judgeEmpty(complaintDetail.carPhotos)){
                        $("#carTitle").html("<li class='font-we'>车辆事故前信息</li>");
                        var carPhotos = JSON.parse(complaintDetail.carPhotos);
                        var carPhotosHtml = "<ul class='img-info'>";
                        if(ex.judgeEmpty(carPhotos.qd)){
                            carPhotosHtml += "<li><a class='card-img' onclick='scaleImg(this)'><img src='"+ carPhotos.qd +"' alt='' onload='resetImg(this)'></a><p>前挡</p></li>";
                        }
                        if(ex.judgeEmpty(carPhotos.zq)){
                            carPhotosHtml += "<li><a class='card-img' onclick='scaleImg(this)'><img src='"+ carPhotos.zq +"' alt='' onload='resetImg(this)'></a><p>左前</p></li>";
                        }
                        if(ex.judgeEmpty(carPhotos.yq)){
                            carPhotosHtml += "<li><a class='card-img' onclick='scaleImg(this)'><img src='"+ carPhotos.yq +"' alt='' onload='resetImg(this)'></a><p>右前</p></li>";
                        }
                        if(ex.judgeEmpty(carPhotos.zh)){
                            carPhotosHtml += "<li><a class='card-img' onclick='scaleImg(this)'><img src='"+ carPhotos.zh +"' alt='' onload='resetImg(this)'></a><p>左后</p></li>";
                        }
                        if(ex.judgeEmpty(carPhotos.yh)){
                            carPhotosHtml += "<li><a class='card-img' onclick='scaleImg(this)'><img src='"+ carPhotos.yh +"' alt='' onload='resetImg(this)'></a><p>右后</p></li>";
                        }
                        if(ex.judgeEmpty(carPhotos.zc)){
                            carPhotosHtml += "<li><a class='card-img' onclick='scaleImg(this)'><img src='"+ carPhotos.zc +"' alt='' onload='resetImg(this)'></a><p>左侧</p></li>";
                        }
                        if(ex.judgeEmpty(carPhotos.yc)){
                            carPhotosHtml += "<li><a class='card-img' onclick='scaleImg(this)'><img src='"+ carPhotos.yc +"' alt='' onload='resetImg(this)'></a><p>右侧</p></li>";
                        }
                        carPhotosHtml += "</ul>";
                        $("#carPhotos").html(carPhotosHtml);
                    }
                    //尾部时间
                    $("#eventNo").html(ex.judgeEmpty(complaintDetail.eventNo));
                    $("#timeApply").html(ex.judgeEmpty(complaintDetail.timeApply)?ex.reTime("s",complaintDetail.timeApply.time):"");
                    $("#timeComplaint").html(ex.judgeEmpty(complaintDetail.timeComplaint) ? ex.reTime("s", complaintDetail.timeComplaint.time) : "");
                }else{
                    alertBtn("初始化数据失败!请重试或联系管理员!");
                }
            },
            error:function (e) {
                alertBtn("请求失败!请检查网络或联系管理员!");
            }
        });
    });

    //处理投诉弹窗
    function success(){
        layer.open({
            title:'信息提示',
            type: 1,
            btn:['确定','取消'],
            btnAlign: 'r',
            area: ['280px'],
            shadeClose: false, //点击遮罩关闭
            content: '<p class="is-layer">您确定该投诉已处理完毕吗？</p>',
            yes:function(layero){
                var strJson = {"eventNo":"${eventNo}","nameCarOwner":complaintDetail.nameCarOwner,"customerPN":complaintDetail.customerPN,"maintenanceshopId":complaintDetail.maintenanceshopId,"customerId":complaintDetail.customerId};
                $.ajax({
                    type : "post",
                    dataType : "json",
                    headers: {rqSide: ex.pc()},
                    data : JSON.stringify(strJson),
                    contentType : "application/json;charset=utf-8",
                    url: "${ctx}/event/complaintsuccess",
                    success: function (datas) {
                        if(datas.code != "0"){
                            alert(datas.message)
                        }else{
                            self.location.href = "${ctx}/event/list.html";
                        }
                    }
                });
                layer.close();
            }
        });
    }

    //废弃订单弹窗
    function invalidOrder(){
        layer.open({
            title:'信息提示',
            type: 1,
            btn:['确定','取消'],
            btnAlign: 'r',
            area: ['280px'],
            shadeClose: false, //点击遮罩关闭
            content: '<p class="is-layer">您确定要废弃该订单吗？</p>',
            yes:function(layero){
                var strJson = {"eventNo":"${eventNo}"};
                $.ajax({
                    type : "post",
                    dataType : "json",
                    headers: {rqSide: ex.pc()},
                    data : JSON.stringify(strJson),
                    contentType : "application/json;charset=utf-8",
                    url: "${ctx}/event/invalidOrder",
                    success: function (datas) {
                        if(datas.code != "0"){
                            alert(datas.message)
                        }else{
                            self.location.href = "${ctx}/event/list.html";
                        }
                    }
                });
                layer.close();
            }
        });
    }

    //消息提示 弹窗
    function alertBtn(info){
        layer.alert(info, function(index){
            layer.close(index);
        });
    }
</script>
</body>
</html>