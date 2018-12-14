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
                        <c:if test="${statusEvent == 21}">
                            <ul class="details-title">
                                <li class="font-we">定损信息</li>
                            </ul>
                            <ul class="details-li">
                                <li>定损人员：<span id="empName2"></span></li>
                                <li>联系电话：<span id="empTel2"></span></li>
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
                        <c:if test="${statusEvent == 22 || statusEvent == 31 || statusEvent == 51}">
                            <ul class="details-title">
                                <li class="font-we">定损报告</li>
                            </ul>
                            <ul class="details-li">
                                <li>定损人员：<span id="empName"></span></li>
                                <li>定损价格：<span id="amtAssert"></span></li>
                                <c:if test="${statusEvent == 31 || statusEvent==51}">
                                    <li>结算价格：<span id="amtBusiness"></span></li>
                                </c:if>
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
                        </c:if>
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
                            <c:if test="${statusEvent == 21}">
                                <li>商家接单时间：<span id="timeReceiveOrder"></span></li>
                            </c:if>
                            <c:if test="${statusEvent == 22}">
                                <li>提交定损时间：<span id="createAt"></span></li>
                            </c:if>
                            <c:if test="${statusEvent == 31}">
                                <li>完成定损时间：<span id="timeAssert"></span></li>
                            </c:if>
                            <c:if test="${statusEvent == 51}">
                                <li>交车时间：<span id="timePay"></span></li>
                            </c:if>
                        </ul>
                        <div class="check-btn">
                            <button type="button" class="btn btn-primary" onclick="invalidOrder()">废弃订单</button>
                            <c:if test="${statusEvent == 22}">
                                <button type="button" class="btn btn-primary" onclick="success()">去确认定损</button>
                            </c:if>
                            <c:if test="${statusEvent == 31}">
                                <button type="button" class="btn btn-primary" onclick="success()">修改定损信息</button>
                            </c:if>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<!-- 照片弹窗 -->
<jsp:include page="/pages/branch/imagePopup.jsp"/>
<!--审核成功弹窗-->
<div id="ad-layer">
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
<script type="text/javascript">
    var assertDetail
    $(document).ready(function (){
        $.ajax({
            type : "post",
            dataType : "json",
            headers: {rqSide: ex.pc()},
            data : JSON.stringify({"eventNo":"${eventNo}"}),
            contentType : "application/json;charset=utf-8",
            url: "${ctx}/event/assertdetail",
            success : function(datas){
                if(datas.code == "0"){
                    assertDetail = datas.data;
                    var statusEventInt = ex.judgeEmpty(assertDetail.statusEvent);
                    // 基本信息
                    $("#nameCarOwner").html(ex.judgeEmpty(assertDetail.nameCarOwner));
                    $("#licensePlateNumber").html(ex.judgeEmpty(assertDetail.licensePlateNumber));
                    $("#model").html(ex.judgeEmpty(assertDetail.model));
                    $("#customerPN").html(ex.judgeEmpty(assertDetail.customerPN));
                    $("#amtCompensation").html(ex.judgeEmptyOr0(assertDetail.amtCompensation)+"元");
                    $("#statusEvent").html(ex.statusEvent(assertDetail.statusEvent));
                    // 定损信息
                    if(statusEventInt==21) {
                        $("#empName2").html(ex.judgeEmpty(assertDetail.empName));
                        $("#empTel2").html(ex.judgeEmpty(assertDetail.empTel));
                    }
                    //维修商家信息
                    $("#shopName").html(ex.judgeEmpty(assertDetail.shopName));
                    $("#tel").html(ex.judgeEmpty(assertDetail.tel));
                    $("#address").html(ex.judgeEmpty(assertDetail.address));
                    //联系人信息
                    $("#receiveManName").html(ex.judgeEmpty(assertDetail.receiveManName));
                    $("#telCarOwner").html(ex.judgeEmpty(assertDetail.telCarOwner));
                    $("#place").html(ex.judgeEmpty(assertDetail.place));
                    //定损报告
                    if(statusEventInt==22 || statusEventInt==31 || statusEventInt==51){
                        $("#empName").html(ex.judgeEmpty(assertDetail.empName));
                        $("#amtAssert").html(ex.judgeEmptyOr0(assertDetail.amtAssert)+"元");
                        if(statusEventInt==31 || statusEventInt==51){
                            $("#amtBusiness").html(ex.judgeEmptyOr0(assertDetail.amtBusiness)+"元");
                        }
                        $("#empTel").html(ex.judgeEmpty(assertDetail.empTel));
                        $("#damageExtent").html(ex.judgeEmpty(assertDetail.damageExtent));
                        $("#damagePosition").html(ex.judgeEmpty(assertDetail.damagePosition));
                        $("#description").html(ex.judgeEmpty(assertDetail.description));
                        var assertImg = ex.judgeEmpty(assertDetail.assertImg);
                        var assertImgHtml = "";
                        if(assertImg){
                            for (var i=0;i < assertImg.length;i++){
                                assertImgHtml += "<li><a class='card-img' onclick='scaleImg(this)'>";
                                assertImgHtml += "<img src='"+ assertImg[i] +"' alt='' onload='resetImg(this)'>";
                            }
                        }
                        $("#assertImg").html(assertImgHtml);
                    }
                    // 车损事故报告
                    if(ex.judgeEmpty(assertDetail.applySuccess)){
                        $("#applySuccess").html(ex.judgeEmpty(assertDetail.applySuccess));
                    }
                    $("#accidentDescription").html(ex.judgeEmpty(assertDetail.accidentDescription));
                    $("#drivingLicense").attr('src',ex.judgeEmpty(assertDetail.drivingLicense));
                    var accidentImg = ex.judgeEmpty(assertDetail.accidentImg);
                    var accidentImgHtml = "";
                    if(accidentImg){
                        for (var i=0;i < accidentImg.length;i++){
                            accidentImgHtml += "<li><a class='card-img' onclick='scaleImg(this)'>";
                            accidentImgHtml += "<img src='"+ accidentImg[i] +"' alt='' onload='resetImg(this)'>";
                        }
                    }
                    $("#accidentImg").html(accidentImgHtml);
                    //事故前照片
                    if(ex.judgeEmpty(assertDetail.carPhotos)){
                        $("#carTitle").html("<li class='font-we'>车辆事故前信息</li>");
                        var carPhotos = JSON.parse(assertDetail.carPhotos);
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
                    $("#eventNo").html(ex.judgeEmpty(assertDetail.eventNo));
                    $("#timeApply").html(ex.judgeEmpty(assertDetail.timeApply)?ex.reTime("s",assertDetail.timeApply.time):"");
                    if(statusEventInt==21) {
                        $("#timeReceiveOrder").html(ex.judgeEmpty(assertDetail.timeReceiveOrder) ? ex.reTime("s", assertDetail.timeReceiveOrder.time) : "");
                    }
                    if(statusEventInt==22){
                        $("#createAt").html(ex.judgeEmpty(assertDetail.createAt) ? ex.reTime("s", assertDetail.createAt.time) : "");
                    }
                    if(statusEventInt==31){
                        $("#timeAssert").html(ex.judgeEmpty(assertDetail.timeAssert) ? ex.reTime("s", assertDetail.timeAssert.time) : "");
                    }
                    if(statusEventInt==51){
                        $("#timePay").html(ex.judgeEmpty(assertDetail.timePay) ? ex.reTime("s", assertDetail.timePay.time) : "");
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
    function success(){
        $(".in_1").val(ex.judgeEmptyOr0(assertDetail.amtAssert));
        var amtBusiness = ex.judgeEmptyOr0(assertDetail.amtAssert)*(ex.judgeEmptyOr0(assertDetail.ratio));
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
            content: $('#ad-layer'),
            yes:function(index, layero){
                var amtAssert = $(".in_1").val();
                amtBusiness = $(".in_2").val();
                var reasonAssert = $(".in_3").val();
                if (!(amtAssert&&amtBusiness)) {
                    alert("请正确填写定损金额");
                    return;
                }
                var strJson = {"eventNo":"${eventNo}","amtAssert":amtAssert,"amtBusiness":amtBusiness,"licensePlateNumber":assertDetail.licensePlateNumber,"reasonAssert":reasonAssert};
                $.ajax({
                    type : "post",
                    dataType : "json",
                    headers: {rqSide: ex.pc()},
                    data : JSON.stringify(strJson),
                    contentType : "application/json;charset=utf-8",
                    url: "${ctx}/event/assertsuccess",
                    success : function(datas){
                        if(datas.code != "0"){
                            alert(datas.message)
                        }else{
                            self.location.href = "${ctx}/event/list.html";
                        }
                    }
                });
                layer.close(index);
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

    function changeAmtBusiness() {
        var amtBusiness = $(".in_1").val()*(assertDetail.ratio);
        amtBusiness = parseFloat(amtBusiness.toFixed(2));
        $(".in_2").val(amtBusiness);
    }
</script>
</body>
</html>