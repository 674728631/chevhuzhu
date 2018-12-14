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
                        <c:if test="${statusEvent == 52}">
                            <ul class="details-title">
                                <li class="font-we">维修信息</li>
                            </ul>
                            <ul class="details-li">
                                <li>维修人员：<span id="repairmanName2"></span></li>
                                <li>联系电话：<span id="reparimanTel2"></span></li>
                            </ul>
                        </c:if>
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
                        <c:if test="${statusEvent == 61 || statusEvent == 71}">
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
                        </c:if>
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
                            <c:if test="${statusEvent == 52}">
                                <li>开始维修时间：<span id="timeBegin"></span></li>
                            </c:if>
                            <c:if test="${statusEvent == 61}">
                                <li>维修完成时间：<span id="timeEnd"></span></li>
                            </c:if>
                            <c:if test="${statusEvent == 71}">
                                <li>交车时间：<span id="timeReceiveEnd"></span></li>
                            </c:if>
                        </ul>
                        <div class="check-btn">
                            <button type="button" class="btn btn-primary" onclick="invalidOrder()">废弃订单</button>
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
    $(document).ready(function (){
        $.ajax({
            type : "post",
            dataType : "json",
            headers: {rqSide: ex.pc()},
            data : JSON.stringify({"eventNo":"${eventNo}"}),
            contentType : "application/json;charset=utf-8",
            url: "${ctx}/event/repairdetail",
            success : function(datas){
                if(datas.code == "0"){
                    var repairDetail = datas.data;
                    var statusEventInt = ex.judgeEmpty(repairDetail.statusEvent);
                    // 基本信息
                    $("#nameCarOwner").html(ex.judgeEmpty(repairDetail.nameCarOwner));
                    $("#licensePlateNumber").html(ex.judgeEmpty(repairDetail.licensePlateNumber));
                    $("#model").html(ex.judgeEmpty(repairDetail.model));
                    $("#customerPN").html(ex.judgeEmpty(repairDetail.customerPN));
                    $("#amtCompensation").html(ex.judgeEmptyOr0(repairDetail.amtCompensation)+"元");
                    $("#statusEvent").html(ex.statusEvent(repairDetail.statusEvent));
                    // 维修信息
                    if(statusEventInt==52) {
                        $("#repairmanName2").html(ex.judgeEmpty(repairDetail.repairmanName));
                        $("#reparimanTel2").html(ex.judgeEmpty(repairDetail.reparimanTel));
                    }
                    //维修商家信息
                    $("#shopName").html(ex.judgeEmpty(repairDetail.shopName));
                    $("#tel").html(ex.judgeEmpty(repairDetail.tel));
                    $("#address").html(ex.judgeEmpty(repairDetail.address));
                    //联系人信息
                    $("#receiveManName").html(ex.judgeEmpty(repairDetail.receiveManName));
                    $("#telCarOwner").html(ex.judgeEmpty(repairDetail.telCarOwner));
                    $("#place").html(ex.judgeEmpty(repairDetail.place));
                    //维修报告
                    if(statusEventInt==61 || statusEventInt == 71){
                        $("#repairmanName").html(ex.judgeEmpty(repairDetail.repairmanName));
                        $("#reparimanTel").html(ex.judgeEmpty(repairDetail.reparimanTel));
                        $("#repDescription").html(ex.judgeEmpty(repairDetail.repDescription));
                        var img = ex.judgeEmpty(repairDetail.img);
                        var imgHtml = "";
                        if(img){
                            for (var i=0;i < img.length;i++){
                                imgHtml += "<li><a class='card-img' onclick='scaleImg(this)'>";
                                imgHtml += "<img src='"+ img[i] +"' alt='' onload='resetImg(this)'>";
                            }
                        }
                        $("#img").html(imgHtml);
                    }
                    //定损报告
                    $("#empName").html(ex.judgeEmpty(repairDetail.empName));
                    $("#amtAssert").html(ex.judgeEmptyOr0(repairDetail.amtAssert)+"元");
                    $("#amtBusiness").html(ex.judgeEmptyOr0(repairDetail.amtBusiness)+"元");
                    $("#empTel").html(ex.judgeEmpty(repairDetail.empTel));
                    $("#damageExtent").html(ex.judgeEmpty(repairDetail.damageExtent));
                    $("#damagePosition").html(ex.judgeEmpty(repairDetail.damagePosition));
                    $("#description").html(ex.judgeEmpty(repairDetail.description));
                    var assertImg = ex.judgeEmpty(repairDetail.assertImg);
                    var assertImgHtml = "";
                    if(assertImg){
                        for (var i=0;i < assertImg.length;i++){
                            assertImgHtml += "<li><a class='card-img' onclick='scaleImg(this)'>";
                            assertImgHtml += "<img src='"+ assertImg[i] +"' alt='' onload='resetImg(this)'>";
                        }
                    }
                    $("#assertImg").html(assertImgHtml);
                    // 车损事故报告
                    if(ex.judgeEmpty(repairDetail.applySuccess)){
                        $("#applySuccess").html(ex.judgeEmpty(repairDetail.applySuccess));
                    }
                    $("#accidentDescription").html(ex.judgeEmpty(repairDetail.accidentDescription));
                    $("#drivingLicense").attr('src',ex.judgeEmpty(repairDetail.drivingLicense));
                    var accidentImg = ex.judgeEmpty(repairDetail.accidentImg);
                    var accidentImgHtml = "";
                    if(accidentImg){
                        for (var i=0;i < accidentImg.length;i++){
                            accidentImgHtml += "<li><a class='card-img' onclick='scaleImg(this)'>";
                            accidentImgHtml += "<img src='"+ accidentImg[i] +"' alt='' onload='resetImg(this)'>";
                        }
                    }
                    $("#accidentImg").html(accidentImgHtml);
                    //事故前照片
                    if(ex.judgeEmpty(repairDetail.carPhotos)){
                        $("#carTitle").html("<li class='font-we'>车辆事故前信息</li>");
                        var carPhotos = JSON.parse(repairDetail.carPhotos);
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
                    $("#eventNo").html(ex.judgeEmpty(repairDetail.eventNo));
                    $("#timeApply").html(ex.judgeEmpty(repairDetail.timeApply)?ex.reTime("s",repairDetail.timeApply.time):"");
                    if(statusEventInt==52) {
                        $("#timeBegin").html(ex.judgeEmpty(repairDetail.timeBegin) ? ex.reTime("s", repairDetail.timeBegin.time) : "");
                    }
                    if(statusEventInt==61) {
                        $("#timeEnd").html(ex.judgeEmpty(repairDetail.timeEnd) ? ex.reTime("s", repairDetail.timeEnd.time) : "");
                    }
                    if(statusEventInt==71) {
                        $("#timeReceiveEnd").html(ex.judgeEmpty(repairDetail.timeReceiveEnd) ? ex.reTime("s", repairDetail.timeReceiveEnd.time) : "");
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