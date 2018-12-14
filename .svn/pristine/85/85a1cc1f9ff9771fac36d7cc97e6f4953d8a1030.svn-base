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
                <li>互助车辆</li>
                <li>详情</li>
            </ul>
        </div>
        <div class="row margin-top-ele">
            <div class="col-lg-12 col-xs-12">
                <div class="panel panel-info">
                    <div class="panel-heading">
                        <h3 class="panel-title">车辆详情</h3>
                    </div>
                    <div class="panel-body">
                        <ul class="details-li" id="text-info">
                            <li>车牌号：<span id="licensePlateNumber"></span></li>
                            <li>手机号码：<span id="customerPN"></span></li>
                            <li><span id="carStatus"></span></li>
                            <li>行驶城市：<span id="drvingCity"></span></li>
                            <li>来源：<span id="shopName"></span></li>
                            <li>互助金余额：<span id="amtCooperation"></span></li>
                            <li>理赔余额：<span id="amtCompensation"></span></li>
                            <li>加入时长：<span id="joinDay">0天</span></li>
                            <c:if test="${status == 20 || status == 30}">
                                <li id="timeEnd"></li>
                            </c:if>
                            <c:if test="${status == 13}">
                                <li>观察期还剩：<span id="observationTime"></span></li>
                            </c:if>
                            <c:if test="${status == 30}">
                                <li>退出计划原因：<span id="reasonOut"></span></li>
                            </c:if>
                        </ul>
                        <div class="card-info" id="carPhotos"></div>
                        <ul class="details-li">
                            <c:if test="${status == 1}">
                                <li>填写车牌时间：<span id="createAt"></span></li>
                            </c:if>
                            <c:if test="${status == 13}">
                                <li>完成支付时间：<span id="payTime"></span></li>
                            </c:if>
                            <c:if test="${status == 20}">
                                <li>加入互助时间：<span id="timeBegin"></span></li>
                            </c:if>
                            <c:if test="${status == 30}">
                                <li>退出计划时间：<span id="timeSignout"></span></li>
                            </c:if>
                        </ul>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<!-- 照片弹窗 -->
<jsp:include page="/pages/branch/imagePopup.jsp"/>

<script type="text/javascript">
    $(document).ready(function (){
        $.ajax({
            type : "post",
            dataType : "json",
            headers: {rqSide: ex.pc()},
            data : JSON.stringify({"id":"${carId}"}),
            contentType : "application/json;charset=utf-8",
            url: "${ctx}/car/carDetail",
            success : function(datas){
                if(datas.code == "0"){
                    var carDetail = datas.data;
                    var statusInt = ex.judgeEmpty(carDetail.status);
                    var shopName = ex.judgeEmpty(carDetail.shopName)?carDetail.shopName:"自然用户";
                    //基本信息
                    $("#licensePlateNumber").html(ex.judgeEmpty(carDetail.licensePlateNumber));
                    $("#customerPN").html(ex.judgeEmpty(carDetail.customerPN));
                    $("#carStatus").html(ex.carStatus(carDetail.status));
                    $("#drvingCity").html(ex.judgeEmpty(carDetail.drvingCity));
                    $("#shopName").html(shopName);
                    $("#amtCooperation").html(ex.judgeEmptyOr0(carDetail.amtCooperation)+"元");
                    $("#amtCompensation").html(ex.judgeEmptyOr0(carDetail.amtCompensation)+"元");
                    /* 保障中车辆 加入时长  */
                    console.log(carDetail.status);
                    console.log(carDetail.timeBegin);
                    console.log(carDetail.timeSignout);
                    if(carDetail.status=='20' && ex.judgeEmpty(carDetail.timeBegin)){
                        $("#joinDay").html(ex.calcDays(carDetail.timeBegin.time)+"天");
                    /* 保障中车辆 （包年）加入时长  */
                  /*   }else if(ex.judgeEmpty(carDetail.timeBegin) && ex.judgeEmpty(carDetail.timeEnd)){
                    	$("#joinDay").html(ex.calcDayBeginToEnd(carDetail.timeBegin.time,carDetail.timeEnd.time) + "天"); */
                    	/* 观察期，待支付  加入时长  */
                    }else if(!ex.judgeEmpty(carDetail.timeBegin) && !ex.judgeEmpty(carDetail.timeEnd)){
                    	$("#joinDay").html("0天");
                    }else if(carDetail.status=='30' && ex.judgeEmpty(carDetail.timeBegin) && ex.judgeEmpty(carDetail.timeSignout)){
                    	$("#joinDay").html(ex.calcDayBeginToEnd(carDetail.timeBegin.time, carDetail.timeSignout.time) + '天');
                    }
                    if(statusInt==13){
                        if(ex.judgeEmpty(carDetail.observationEndTime)){
                            $("#observationTime").html(ex.calcDayBeginToEnd(new Date().getTime(),carDetail.observationEndTime.time)+"天");
                        }
                    }
                    if((statusInt==20||statusInt==30) && carDetail.typeGuarantee==2){
                        var timeEnd = ex.judgeEmpty(carDetail.timeEnd)?ex.reTime("s",carDetail.timeEnd.time):"";
                        $("#timeEnd").html("保障期至：<span>"+ timeEnd +"</span>");
                    }
                    if(statusInt==30){
                        if(carDetail.typeGuarantee==2){
                            $("#reasonOut").html("保障期已结束");
                        }else {
                            $("#reasonOut").html("互助金余额不足");
                        }
                    }
                    if(ex.judgeEmpty(carDetail.carPhotos)){
                        var carPhotos = JSON.parse(carDetail.carPhotos);
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
                    if(statusInt==1){
                        $("#createAt").html(ex.judgeEmpty(carDetail.createAt) ? ex.reTime("s", carDetail.createAt.time) : "");
                    }else if(statusInt==13){
                        $("#payTime").html(ex.judgeEmpty(carDetail.payTime) ? ex.reTime("s", carDetail.payTime.time) : "");
                    }else if(statusInt==20){
                        $("#timeBegin").html(ex.judgeEmpty(carDetail.timeBegin) ? ex.reTime("s", carDetail.timeBegin.time) : "");
                    }else if(statusInt==30){
                        $("#timeSignout").html(ex.judgeEmpty(carDetail.timeSignout) ? ex.reTime("s", carDetail.timeSignout.time) : "");
                    }
                }else{
                    alertBtn(datas.message);
                }
            },
            error:function (e) {
                alertBtn("请求失败!请检查网络或联系管理员!");
            }
        });
    });

    //消息提示 弹窗
    function alertBtn(info){
        layer.alert(info, function(index){
            layer.close(index);
        });
    }
</script>
</body>
</html>