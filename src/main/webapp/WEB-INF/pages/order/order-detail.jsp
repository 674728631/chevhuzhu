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
    <link rel="stylesheet" href="https://cache.amap.com/lbs/static/main.css"/>
    <script type="text/javascript"
            src="https://webapi.amap.com/maps?v=1.4.2&key=aa654029339dfdfce8e12ebf759d305eֵ"></script>
    <style type="text/css">
        body {
            font-size: 12px;
        }
        .amap-info-close{
            top:10px;
        }
    </style>
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
                <li>保险理赔</li>
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
                            <li><span id="status"></span></li>
                        </ul>
                        <ul class="details-li">
                            <li>品牌型号：<span id="model"></span></li>
                            <li>手机号码：<span id="customerPN"></span></li>
                        </ul>
                        <c:if test="${status == 2}">
                            <ul class="details-li">
                                <li>未通过原因：<span id="examineExplanation1"></span></li>
                            </ul>
                        </c:if>
                        <c:if test="${status == 11 || status == 12 || status == 21 || status == 31 || status == 32 || status == 41 || status == 42 || status == 51 || status == 61 || status == 71 || status == 100}">
                            <ul class="details-title">
                                <c:if test="${status != 12}">
                                    <li class="font-we">维修商家信息</li>
                                </c:if>
                                <c:if test="${status == 12}">
                                    <li class="font-we">放弃接单信息</li>
                                </c:if>
                            </ul>
                            <ul class="details-li">
                                <li>商家名称：<span id="shopName"></span></li>
                                <c:if test="${status != 12}">
                                    <li>联系电话：<span id="tel"></span></li>
                                    <li>地址：<span id="address"></span></li>
                                </c:if>
                                <c:if test="${status == 12}">
                                    <li>原因：<span id="reasonCancellations"></span></li>
                                    <li>说明：<span id="explanationCancellations"></span></li>
                                </c:if>
                            </ul>
                        </c:if>
                        <c:if test="${status == 10 || status == 11 || status == 12 || status == 21 || status == 31 || status == 32 || status == 41 || status == 42 || status == 51 || status == 61 || status == 71 || status == 100}">
                            <ul class="details-title">
                                <li class="font-we">联系人信息</li>
                            </ul>
                            <ul class="details-li">
                                <li>联系人姓名：<span id="carOwnerName"></span></li>
                                <li>联系电话：<span id="carOwnerTel"></span></li>
                                <li>地址：<span id="deliverPlace"></span></li>
                                <li>最迟交车时间：<span id="reciveCarTime"></span></li>
                            </ul>
                        </c:if>
                        <c:if test="${status == 71}">
                            <ul class="details-title">
                                <li class="font-we">投诉信息</li>
                            </ul>
                            <ul class="details-li">
                                <li>投诉内容：<span id="complaintContent"></span></li>
                            </ul>
                        </c:if>
                        <c:if test="${status == 42 || status == 51 || status == 61 || status == 71 || status == 100}">
                            <ul class="details-title">
                                <c:if test="${status == 42}">
                                    <li class="font-we">维修信息</li>
                                </c:if>
                                <c:if test="${status != 42}">
                                    <li class="font-we">维修报告</li>
                                </c:if>
                            </ul>
                            <ul class="details-li">
                                <li>维修人员：<span id="repairmanName"></span></li>
                                <li>联系电话：<span id="repairmanTel"></span></li>
                            </ul>
                            <c:if test="${status != 42}">
                                <ul class="details-li">
                                    <li>维修描述：<span id="explanationRepair"></span></li>
                                </ul>
                                <div class="card-info clearH">
                                    <ul id="repairImg" class="img-info"></ul>
                                </div>
                            </c:if>
                        </c:if>
                        <c:if test="${status == 21 || status == 31 || status == 32 || status == 41 || status == 42 || status == 51 || status == 61 || status == 71 || status == 100}">
                            <ul class="details-title">
                                <c:if test="${status == 21 || status == 31}">
                                    <li class="font-we">定损信息</li>
                                </c:if>
                                <c:if test="${status != 21 && status != 31}">
                                    <li class="font-we">定损报告</li>
                                </c:if>
                            </ul>
                            <ul class="details-li">
                                <li>定损人员：<span id="assertmanName"></span></li>
                                <li>联系电话：<span id="assertmanTel"></span></li>
                                <c:if test="${status == 32 || status == 41 || status == 42 || status == 51 || status == 61 || status == 71 || status == 100}">
                                    <li>定损价格：<span id="amtAssert"></span></li>
                                    <c:if test="${status != 32}">
                                        <li>结算价格：<span id="amtBusiness"></span></li>
                                    </c:if>
                                    <li>受损程度：<span id="damageExtent"></span></li>
                                </c:if>
                            </ul>
                            <c:if test="${status == 32 || status == 41 || status == 42 || status == 51 || status == 61 || status == 71 || status == 100}">
                                <ul class="details-li">
                                    <li>受损部位：<span id="damagePosition"></span></li>
                                </ul>
                                <ul class="details-li">
                                    <li>定损描述：<span id="assertDescription"></span></li>
                                </ul>
                                <div class="card-info clearH">
                                    <ul id="assertImg" class="img-info"></ul>
                                </div>
                            </c:if>
                        </c:if>
                        <ul class="details-title">
                            <li class="font-we">车损事故报告</li>
                        </ul>
                        <c:if test="${status != 1 && status != 2}">
                            <ul class="details-li">
                                <li>平台审核说明：<span id="examineExplanation2">未填写审核说明</span></li>
                            </ul>
                        </c:if>
                        <ul class="details-li">
                            <li>事故描述：<span id="accidentDescription"></span></li>
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
                        <c:if test="${status == 100}">
                            <ul class="details-title">
                                <li class="font-we">车主评价</li>
                            </ul>
                            <ul class="details-li">
                                <li id="commentScore"></li>
                            </ul>
                            <ul class="details-li">
                                <li id="labelContent"></li>
                            </ul>
                            <ul class="details-li">
                                <li><span id="commentContent"></span></li>
                            </ul>
                        </c:if>
                        <ul class="details-li">
                            <li>订单编号：<span id="orderNo"></span></li>
                            <li>申请理赔时间：<span id="applyTime"></span></li>
                            <c:if test="${status == 2 || status == 3}">
                                <li>审核时间：<span id="examineTime"></span></li>
                            </c:if>
                            <c:if test="${status == 10}">
                                <li>申请分单时间：<span id="applyDistributionTime"></span></li>
                            </c:if>
                            <c:if test="${status == 11}">
                                <li>分单时间：<span id="distributionTime"></span></li>
                            </c:if>
                            <c:if test="${status == 12}">
                                <li>放弃接单时间：<span id="failReceiveOrderTime"></span></li>
                            </c:if>
                            <c:if test="${status == 21}">
                                <li>商家接单时间：<span id="receiveOrderTime"></span></li>
                            </c:if>
                            <c:if test="${status == 31}">
                                <li>商家接车时间：<span id="deliverCarTime"></span></li>
                            </c:if>
                            <c:if test="${status == 32}">
                                <li>提交定损时间：<span id="assertTime"></span></li>
                            </c:if>
                            <c:if test="${status == 41}">
                                <li>确认定损时间：<span id="comfirmAssertTime"></span></li>
                            </c:if>
                            <c:if test="${status == 42}">
                                <li>开始维修时间：<span id="beginRepairTime"></span></li>
                            </c:if>
                            <c:if test="${status == 51}">
                                <li>维修完成时间：<span id="endRepairTime"></span></li>
                            </c:if>
                            <c:if test="${status == 61}">
                                <li>交车时间：<span id="takeCarTime"></span></li>
                            </c:if>
                            <c:if test="${status == 71}">
                                <li>投诉时间：<span id="complaintTime"></span></li>
                            </c:if>
                            <c:if test="${status == 100}">
                                <li>评价时间：<span id="completeTime"></span></li>
                            </c:if>
                        </ul>
                        <div class="check-btn">
                            <button type="button" class="btn btn-primary" onclick="invalidOrder()">废弃订单</button>
                            <c:if test="${status == 1}">
                                <button type="button" class="btn btn-danger" onclick="applyFail()">不通过</button>
                                <button type="button" class="btn btn-primary" onclick="applySuccess()">通过</button>
                            </c:if>
                            <c:if test="${status == 10 || status == 12}">
                                <button type="button" class="btn btn-primary" onclick="distribution()">立即分单</button>
                            </c:if>
                            <c:if test="${status == 32}">
                                <button type="button" class="btn btn-primary" onclick="comfirmAssert()">去确认定损</button>
                            </c:if>
                            <c:if test="${status == 71}">
                                <button type="button" class="btn btn-primary" onclick="solveComplaint()">投诉已解决</button>
                            </c:if>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<!--审核失败弹窗-->
<div id="defeated-layer">
    <div class="input-reason">
        <h3>未通过原因</h3>
        <input type="radio" name="reasonFailure" value="上传的车辆照片不符合要求">上传的车辆照片不符合要求<br>
        <input type="radio" name="reasonFailure" value="您的车辆不属于互助申请范围内">您的车辆不属于互助申请范围内<br>
        <input type="radio" name="reasonFailure" value="车辆信息有误">车辆信息有误<br>
        <input type="radio" name="reasonFailure" value="车辆有旧伤">车辆有旧伤<br>
        <input type="radio" name="reasonFailure" value="其他原因">其他原因<br>
        <input type="text" id="reason-other" placeholder="请填写具体原因" style="width:100%;margin-top:6px;display:none;">
    </div>
</div>
<div id="defeated-layer2">
    <div class="input-reason">
        <h3>请填写通过原因</h3>
        <textarea id="reasonSuccess" onkeyup="value=value.replace(/\s/g,'')"></textarea>
    </div>
</div>
<!--重新分配弹窗-->
<div id="distribution-layer" class="ad-layer">
    <div class="map-address">起始位置：<span id="carAddress"></span></div>
    <div class="map-address">终点位置：<span id="shopAddress" data-value=""></span></div>
    <%--地图--%>
    <div id="mapContainer" style="width: 76%;height: 85%;top:80px;left: 2%;"></div>
    <%--定位 描述--%>
    <div id="mapContainerDes" style="" class="pre-default-timeline"></div>
</div>
<!--定损审核成功弹窗-->
<div id="assertSuccess-layer" class="ad-layer">
    <ul>
        <li>
            <span>定损价</span>
            <input type="text" class="ad-input in_1 " maxlength="20" onkeyup="value=value.replace(/\s/g,''),changeAmtBusiness()" >
        </li>
        <li>
            <span>结算价</span>
            <input type="text" class="ad-input in_2" maxlength="20" onkeyup="value=value.replace(/\s/g,'')">
        </li>
        <li>
            <span>审核说明（选填）</span>
            <textarea type="text" class="form-control in_3" placeholder="" style="height: 100px" maxlength="200" onkeyup="value=value.replace(/\s/g,'')"></textarea>
        </li>
    </ul>
</div>
<!-- 照片弹窗 -->
<jsp:include page="/pages/branch/imagePopup.jsp"/>
<script type="text/javascript" src="https://webapi.amap.com/demos/js/liteToolbar.js"></script>
<script type="text/javascript">
    var detail;
    var shopData;

    $(document).ready(function (){
        $.ajax({
            type : "post",
            dataType : "json",
            headers: {rqSide: ex.pc()},
            data : JSON.stringify({"orderNo":"${orderNo}"}),
            contentType : "application/json;charset=utf-8",
            url: "${ctx}/order/detail",
            success : function(datas){
                if(datas.code == "0"){
                    detail = datas.data;
                    var statusInt = ex.judgeEmpty(detail.status);
                    // 基本信息
                    $("#nameCarOwner").html(ex.judgeEmpty(detail.nameCarOwner));
                    $("#licensePlateNumber").html(ex.judgeEmpty(detail.licensePlateNumber));
                    $("#status").html(ex.orderStatus(detail.status));
                    $("#model").html(ex.judgeEmpty(detail.model));
                    $("#customerPN").html(ex.judgeEmpty(detail.customerPN));
                    if(statusInt==2){
                        if(ex.judgeEmpty(detail.examineExplanation)){
                            $("#examineExplanation1").html(detail.examineExplanation);
                        }
                    }
                    //维修商家信息
                    if(statusInt==11 || statusInt==12 || statusInt==21 || statusInt==31 || statusInt==32 || statusInt==41 || statusInt==42 || statusInt==51 || statusInt==61 || statusInt==71 || statusInt==100) {
                        $("#shopName").html(ex.judgeEmpty(detail.shopName));
                        if(statusInt==12){
                            $("#reasonCancellations").html(ex.judgeEmpty(detail.reasonCancellations));
                            $("#explanationCancellations").html(ex.judgeEmpty(detail.explanationCancellations));
                        }else {
                            $("#tel").html(ex.judgeEmpty(detail.tel));
                            $("#address").html(ex.judgeEmpty(detail.address));
                        }
                    }
                    //联系人信息
                    if(statusInt==10 || statusInt==11 || statusInt==12 || statusInt==21 || statusInt==31 || statusInt==32 || statusInt==41 || statusInt==42 || statusInt==51 || statusInt==61 || statusInt==71 || statusInt==100) {
                        $("#carOwnerName").html(ex.judgeEmpty(detail.carOwnerName));
                        $("#carOwnerTel").html(ex.judgeEmpty(detail.carOwnerTel));
                        $("#deliverPlace").html(ex.judgeEmpty(detail.deliverPlace));
                        $("#reciveCarTime").html(ex.judgeEmpty(detail.reciveCarTime) ? ex.reTime("s", detail.reciveCarTime.time) : "");
                    }
                    //车损事故报告
                    if(statusInt!=1 && statusInt!=2){
                        if(ex.judgeEmpty(detail.examineExplanation)){
                            $("#examineExplanation2").html(detail.examineExplanation);
                        }
                    }
                    $("#accidentDescription").html(ex.judgeEmpty(detail.accidentDescription));
                    $("#drivingLicense").attr('src',ex.judgeEmpty(detail.drivingLicense));
                    var accidentImg = ex.judgeEmpty(detail.accidentImg);
                    var accidentImgHtml = "";
                    if (accidentImg) {
                        for (var i = 0; i < accidentImg.length; i++) {
                            accidentImgHtml += "<li><a class='card-img' onclick='scaleImg(this)'>";
                            accidentImgHtml += "<img src='" + accidentImg[i] + "' alt='' onload='resetImg(this)'>";
                        }
                    }
                    $("#accidentImg").html(accidentImgHtml);
                    //投诉信息
                    if(statusInt==71) {
                        $("#complaintContent").html(ex.judgeEmpty(detail.complaintContent));
                    }
                    //维修报告
                    if(statusInt==42 || statusInt==51 || statusInt==61 || statusInt==71 || statusInt==100) {
                        $("#repairmanName").html(ex.judgeEmpty(detail.repairmanName));
                        $("#repairmanTel").html(ex.judgeEmpty(detail.repairmanTel));
                        if(statusInt!=42){
                            $("#explanationRepair").html(ex.judgeEmpty(detail.explanationRepair));
                            var repairImg = ex.judgeEmpty(detail.repairImg);
                            var repairImgHtml = "";
                            if (repairImg) {
                                for (var i = 0; i < repairImg.length; i++) {
                                    repairImgHtml += "<li><a class='card-img' onclick='scaleImg(this)'>";
                                    repairImgHtml += "<img src='" + repairImg[i] + "' alt='' onload='resetImg(this)'>";
                                }
                            }
                            $("#repairImg").html(repairImgHtml);
                        }
                    }
                    //定损报告
                    if(statusInt==21 || statusInt==31 || statusInt==32 || statusInt==41 || statusInt==42 || statusInt==51 || statusInt==61 || statusInt==71 || statusInt==100) {
                        $("#assertmanName").html(ex.judgeEmpty(detail.assertmanName));
                        $("#assertmanTel").html(ex.judgeEmpty(detail.assertmanTel));
                        if(statusInt==32 || statusInt==41 || statusInt==42 || statusInt==51 || statusInt==61 || statusInt==71 || statusInt==100) {
                            $("#amtAssert").html(ex.judgeEmptyOr0(detail.amtAssert) + "元");
                            if(statusInt!=32) {
                                $("#amtBusiness").html(ex.judgeEmptyOr0(detail.amtBusiness) + "元");
                            }
                            $("#damageExtent").html(ex.judgeEmpty(detail.damageExtent));
                            $("#damagePosition").html(ex.judgeEmpty(detail.damagePosition));
                            $("#assertDescription").html(ex.judgeEmpty(detail.assertDescription));
                            var assertImg = ex.judgeEmpty(detail.assertImg);
                            var assertImgHtml = "";
                            if (assertImg) {
                                for (var i = 0; i < assertImg.length; i++) {
                                    assertImgHtml += "<li><a class='card-img' onclick='scaleImg(this)'>";
                                    assertImgHtml += "<img src='" + assertImg[i] + "' alt='' onload='resetImg(this)'>";
                                }
                            }
                            $("#assertImg").html(assertImgHtml);
                        }
                    }
                    //车主评价
                    if(statusInt==100){
                        var score_html = '';
                        for (var i = 1; i < 6; i++) {
                            if (i > ex.judgeEmptyOr0(detail.commentScore)) {
                                score_html += '<span class="i-star empty"></span>';
                            } else {
                                score_html += '<span class="i-star"></span>';
                            }
                        }
                        $("#commentScore").html(score_html);
                        $("#labelContent").html($.map(ex.judgeEmpty(detail.labelContent).split('_'), function(item){
                            return '<span class="score-tag">' + item + '</span>';
                        }).join(''));
                        $("#commentContent").html(ex.judgeEmpty(detail.commentContent));
                    }
                    //事故前照片
                    if(ex.judgeEmpty(detail.carPhotos)){
                        $("#carTitle").html("<li class='font-we'>车辆事故前信息</li>");
                        var carPhotos = JSON.parse(detail.carPhotos);
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
                    $("#orderNo").html(ex.judgeEmpty(detail.orderNo));
                    $("#applyTime").html(ex.judgeEmpty(detail.applyTime)?ex.reTime("s",detail.applyTime.time):"");
                    if(statusInt==2 || statusInt==3){
                        $("#examineTime").html(ex.judgeEmpty(detail.examineTime)?ex.reTime("s",detail.examineTime.time):"");
                    }
                    if(statusInt==10){
                        $("#applyDistributionTime").html(ex.judgeEmpty(detail.applyDistributionTime)?ex.reTime("s",detail.applyDistributionTime.time):"");
                    }
                    if(statusInt==11){
                        $("#distributionTime").html(ex.judgeEmpty(detail.distributionTime)?ex.reTime("s",detail.distributionTime.time):"");
                    }
                    if(statusInt==12){
                        $("#failReceiveOrderTime").html(ex.judgeEmpty(detail.failReceiveOrderTime)?ex.reTime("s",detail.failReceiveOrderTime.time):"");
                    }
                    if(statusInt==21){
                        $("#receiveOrderTime").html(ex.judgeEmpty(detail.receiveOrderTime)?ex.reTime("s",detail.receiveOrderTime.time):"");
                    }
                    if(statusInt==31){
                        $("#deliverCarTime").html(ex.judgeEmpty(detail.deliverCarTime)?ex.reTime("s",detail.deliverCarTime.time):"");
                    }
                    if(statusInt==32){
                        $("#assertTime").html(ex.judgeEmpty(detail.assertTime)?ex.reTime("s",detail.assertTime.time):"");
                    }
                    if(statusInt==41){
                        $("#comfirmAssertTime").html(ex.judgeEmpty(detail.comfirmAssertTime)?ex.reTime("s",detail.comfirmAssertTime.time):"");
                    }
                    if(statusInt==42){
                        $("#beginRepairTime").html(ex.judgeEmpty(detail.beginRepairTime)?ex.reTime("s",detail.beginRepairTime.time):"");
                    }
                    if(statusInt==51){
                        $("#endRepairTime").html(ex.judgeEmpty(detail.endRepairTime)?ex.reTime("s",detail.endRepairTime.time):"");
                    }
                    if(statusInt==61){
                        $("#takeCarTime").html(ex.judgeEmpty(detail.takeCarTime)?ex.reTime("s",detail.takeCarTime.time):"");
                    }
                    if(statusInt==71){
                        $("#complaintTime").html(ex.judgeEmpty(detail.complaintTime)?ex.reTime("s",detail.complaintTime.time):"");
                    }
                    if(statusInt==100){
                        $("#completeTime").html(ex.judgeEmpty(detail.completeTime)?ex.reTime("s",detail.completeTime.time):"");
                    }
                    //请求维修厂数据并在地图上标记维修厂位置
                    if(statusInt==10 || statusInt==12){
                        $.ajax({
                            type : "post",
                            dataType : "json",
                            headers: {rqSide: ex.pc()},
                            data : JSON.stringify({"longitude":detail.deliverLongitude,"latitude":detail.deliverLatitude}),
                            contentType : "application/json;charset=utf-8",
                            url: "${ctx}/maintenanceshop/canDistribution",
                            success : function(datas){
                                shopData = datas;
                                if(shopData.code == "0"){
                                    var repData = shopData.data;
                                    for(var i= 0,marker;i<repData.length;i++){
                                        var repDateChild = repData[i];
                                        if(repDateChild.longitude && repDateChild.latitude){
                                            var marker =new AMap.Marker({
                                                position:[repDateChild.longitude/1000000,repDateChild.latitude/1000000],
                                                map:map,
                                                icon: new AMap.Icon({
                                                    size: new AMap.Size(30,40),
                                                    image: "${ctx}/cite/images/icon_wx.png",
                                                    imageOffset: new AMap.Pixel(0,0),
                                                    imageSize: new AMap.Size(30,40)
                                                })
                                            });
                                            var adress = '<div><div class="amap-adcontent-body">' +
                                                '<div class="info-title">'+ repDateChild.name +'</div>' +
                                                '<div class="info-content">地&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;址：'+ repDateChild.address +'<br>' +
                                                '电&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;话：'+ repDateChild.tel +'<br>' +
                                                '店&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;长：'+ repDateChild.linkman +'<br>' +
                                                '<button type="button" class="btn btn-success" onclick=showShopAddress('+ repDateChild.id +',"' + repDateChild.address + '")>选择商家</button>' +
                                                '</div>' +
                                                '<div class="amap-info-combo"></div>' +
                                                '</div>' +
                                                '<div class="amap-combo-sharp"></div>' +
                                                '<a class="amap-adcombo-close" href="javascript: void(0)"></a>';
                                            marker.content=adress;
                                            marker.on('click',markerClick);
                                        }
                                    }
                                }
                            }
                        });
                    }
                }else{
                    alertBtn("初始化数据失败!请重试或联系管理员!");
                }
            },
            error:function (e) {
                alertBtn("请求失败!请检查网络或联系管理员!");
            }
        });
    });

    //审核成功弹窗
    function applySuccess(){
        layer.open({
            type: 1,
            title: '审核成功',
            closeBtn: 2,
            area: '360px;',
            shade: 0.5,
            btn: ['确定','取消'],
            btnAlign: 'c',
            moveType: 1, //拖拽模式，0或者1
            content: $("#defeated-layer2"),
            yes: function(index, layero){
                var examineExplanation = $("#reasonSuccess").val();
                var strJson = {"orderNo":"${orderNo}","carId":detail.carId,"customerId":detail.customerId,"examineExplanation":examineExplanation};
                $.ajax({
                    type : "post",
                    dataType : "json",
                    headers: {rqSide: ex.pc()},
                    data : JSON.stringify(strJson),
                    contentType : "application/json;charset=utf-8",
                    url: "${ctx}/order/applysuccess",
                    success : function(datas){
                        if(datas.code != "0"){
                            alert(datas.message)
                        }else{
                            self.location.href = "${ctx}/order/list.html";
                        }
                    }
                });
                $("#defeated-layer2").css("display","none");
                layer.close(index); //如果设定了yes回调，需进行手工关闭
            }
            ,btn1:function(){
                $("#defeated-layer2").css("display","none");
            }
            ,btn2:function(){
                $("#defeated-layer2").css("display","none");
            }
        });
    }

    //审核失败弹窗
    function applyFail(){
        layer.open({
            type: 1,
            title: '审核失败',
            closeBtn: 2,
            area: '360px;',
            shade: 0.5,
            btn: ['确定','取消'],
            btnAlign: 'c',
            moveType: 1, //拖拽模式，0或者1
            content: $("#defeated-layer"),
            yes: function(index, layero){
                var examineExplanation = $("input:radio[name='reasonFailure']:checked").val();
                if(examineExplanation=="其他原因"){
                    examineExplanation = $("#reason-other").val();
                }
                if (!examineExplanation) {
                    alertBtn("请选择失败原因");
                    return;
                }
                var strJson = {"orderNo":"${orderNo}","carId":detail.carId,"customerId":detail.customerId,"examineExplanation":examineExplanation};
                $.ajax({
                    type : "post",
                    dataType : "json",
                    headers: {rqSide: ex.pc()},
                    data : JSON.stringify(strJson),
                    contentType : "application/json;charset=utf-8",
                    url: "${ctx}/order/applyfail",
                    success : function(datas){
                        if(datas.code != "0"){
                            alert(datas.message)
                        }else{
                            self.location.href = "${ctx}/order/list.html";
                        }
                    }
                });
                $("#defeated-layer").css("display","none");
                layer.close(index); //如果设定了yes回调，需进行手工关闭
            }
            ,btn1:function(){
                $("#defeated-layer").css("display","none");
            }
            ,btn2:function(){
                $("#defeated-layer").css("display","none");
            }
        });
    }

    //审核失败（其他原因）
    $('#defeated-layer').on('change', 'input:radio', function(e){
        if (e.target.value === '其他原因') {
            $('#reason-other').show();
        } else {
            $('#reason-other').hide();
        }
    })

    //定损确认弹窗
    function comfirmAssert(){
        $(".in_1").val(ex.judgeEmptyOr0(detail.amtAssert));
        var amtBusiness = ex.judgeEmptyOr0(detail.amtAssert)*(ex.judgeEmptyOr0(detail.ratio));
        amtBusiness = parseFloat(amtBusiness.toFixed(2));
        $(".in_2").val(amtBusiness);
        layer.open({
            title:"确认定损",
            type: 1,
            btn:['提交','取消'],
            btnAlign: 'r',
            area: ['400px','400px'],
            offset: 'auto',
            shadeClose: false, //点击遮罩关闭
            content: $('#assertSuccess-layer'),
            yes:function(index, layero){
                var amtAssert = $(".in_1").val();
                amtBusiness = $(".in_2").val();
                var explanationAssert = $(".in_3").val();
                if (!(amtAssert&&amtBusiness)) {
                    alert("请正确填写定损金额");
                    return;
                }
                var strJson = {"orderNo":"${orderNo}","amtAssert":amtAssert,"amtBusiness":amtBusiness,"explanationAssert":explanationAssert};
                $.ajax({
                    type : "post",
                    dataType : "json",
                    headers: {rqSide: ex.pc()},
                    data : JSON.stringify(strJson),
                    contentType : "application/json;charset=utf-8",
                    url: "${ctx}/order/assertsuccess",
                    success : function(datas){
                        if(datas.code != "0"){
                            alert(datas.message)
                        }else{
                            self.location.href = "${ctx}/order/list.html";
                        }
                    }
                });
                layer.close(index);
            }
        });
    }

    function changeAmtBusiness() {
        var amtBusiness = $(".in_1").val()*(detail.ratio);
        amtBusiness = parseFloat(amtBusiness.toFixed(2));
        $(".in_2").val(amtBusiness);
    }

    //处理投诉弹窗
    function solveComplaint(){
        layer.open({
            title:'信息提示',
            type: 1,
            btn:['确定','取消'],
            btnAlign: 'r',
            area: ['280px'],
            shadeClose: false, //点击遮罩关闭
            content: '<p class="is-layer">您确定该投诉已处理完毕吗？</p>',
            yes:function(layero){
                var strJson = {"orderNo":"${orderNo}"};
                $.ajax({
                    type : "post",
                    dataType : "json",
                    headers: {rqSide: ex.pc()},
                    data : JSON.stringify(strJson),
                    contentType : "application/json;charset=utf-8",
                    url: "${ctx}/order/complaintsuccess",
                    success: function (datas) {
                        if(datas.code != "0"){
                            alert(datas.message)
                        }else{
                            self.location.href = "${ctx}/order/list.html";
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
                var strJson = {"orderNo":"${orderNo}"};
                $.ajax({
                    type : "post",
                    dataType : "json",
                    headers: {rqSide: ex.pc()},
                    data : JSON.stringify(strJson),
                    contentType : "application/json;charset=utf-8",
                    url: "${ctx}/order/invalidOrder",
                    success: function (datas) {
                        if(datas.code != "0"){
                            alert(datas.message)
                        }else{
                            self.location.href = "${ctx}/order/list.html";
                        }
                    }
                });
                layer.close();
            }
        });
    }

    //分配维修商家弹窗
    function distribution(){
        //回显接车地址
        $("#carAddress").html(detail.deliverPlace);
        carMarker();
        shopList();
        layer.open({
            title:"分配订单",
            type: 1,
            btn:['提交','取消'],
            btnAlign: 'r',
            scrollbar: false,
            area: ['60%','80%'],
            offset: 'auto',
            shadeClose: false, //点击遮罩关闭
            content: $('#distribution-layer'),
            yes:function(index, layero){
                var maintenanceshopId = $("#shopAddress").attr("data-value");
                if (!maintenanceshopId) {
                    alert("请选择维修厂");
                    return;
                }
                var strJson = {"orderNo":"${orderNo}","maintenanceshopId":maintenanceshopId,"carId":detail.carId};
                $.ajax({
                    type : "post",
                    dataType : "json",
                    headers: {rqSide: ex.pc()},
                    data : JSON.stringify(strJson),
                    contentType : "application/json;charset=utf-8",
                    url: "${ctx}/order/distribution",
                    success : function(datas){
                        if(datas.code != "0"){
                            alert(datas.message)
                        }else{
                            self.location.href = "${ctx}/order/list.html";
                        }
                    }
                });
            }
        });
    }

    //加载高德地图
    var map = new AMap.Map("mapContainer", {
        resizeEnable: false,
        zoom: 12
    });
    var infoWindow = new AMap.InfoWindow({
        offset:new AMap.Pixel(0,-30)
    });
    function markerClick(e){
        infoWindow.setContent(e.target.content);
        infoWindow.open(map, e.target.getPosition());
    }

    //弹窗显示选择的维修厂地址
    function showShopAddress(shopId,shopAddress){
        $("#shopAddress").html(shopAddress);
        $("#shopAddress").attr("data-value", shopId);
    }

    //地图标记接车人，并将接车人位置放在地图中心
    function carMarker(){
        if(detail.deliverLongitude && detail.deliverLatitude){
            var marker =new AMap.Marker({
                position:[detail.deliverLongitude/1000000,detail.deliverLatitude/1000000],
                map:map,
                icon: new AMap.Icon({
                    size: new AMap.Size(30,40),
                    image: "${ctx}/cite/images/icon_yh.png",
                    imageOffset: new AMap.Pixel(0,0),
                    imageSize: new AMap.Size(30,40)
                })
            });
            var info = '<div><div class="amap-adcontent-body">' +
                '<div class="info-title">姓&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;名：'+ detail.carOwnerName +'</div>' +
                '<div class="info-content">电&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;话：'+ detail.carOwnerTel +'<br>' +
                '</div>' +
                '<div class="amap-info-combo"></div>' +
                '</div>' +
                '<div class="amap-combo-sharp"></div>' +
                '<a class="amap-adcombo-close" href="javascript: void(0)"></a>';
            marker.content=info;
            marker.on('click',markerClick);
            changeMapCenter(detail.deliverLongitude/1000000,detail.deliverLatitude/1000000,info);
        }
    }
    function shopList(){
        if(shopData.code == "0"){
            var repData = shopData.data;
            var dataHtml = "";
            for(var i= 0;i<repData.length;i++){
                var repDateChild = repData[i];
                if(repDateChild.longitude && repDateChild.latitude){
                    dataHtml += "<div class='map-des' onclick=showShopAddress("+ repDateChild.id +",'" + repDateChild.address + "'),changeMapCenter('"+ repDateChild.longitude/1000000 +"','"+ repDateChild.latitude/1000000 +"')>";
                    dataHtml += "<h3>"+ repDateChild.name +"</h3>";
                    dataHtml += "<span>"+ parseFloat((repDateChild.distance/1000).toFixed(1)) +"公里</span>";
                    dataHtml += "<p>"+ repDateChild.address +"</p>";
                    dataHtml += "</div>";
                }
            }
            $("#mapContainerDes").html(dataHtml);
        }
    }
    function changeMapCenter(longitude,latitude){
        map.setZoomAndCenter(15,[longitude,latitude]);
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