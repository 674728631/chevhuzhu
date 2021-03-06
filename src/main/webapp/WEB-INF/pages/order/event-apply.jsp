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
                        <ul class="details-li">
                            <li>理赔次数：<span id="compensateNum"></span></li>
                        </ul>
                        <c:if test="${statusEvent == 2}">
                            <ul class="details-li">
                                <li>未通过原因：<span id="applyFailure">未填写未通过原因</span></li>
                            </ul>
                        </c:if>
                        <ul class="details-title">
                            <li class="font-we">车损事故报告</li>
                        </ul>
                        <c:if test="${statusEvent == 3}">
                            <ul class="details-li">
                                <li>平台审核说明：<span id="applySuccess">未填写审核说明</span></li>
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
                        <c:if test="${statusEvent == 3}">
                            <div class="card-info clearH">
                                <ul class="img-info">
                                    <li>
                                        <a class='card-img' onclick='scaleImg(this)'>
                                            <img id="eventQrcode" src='${ctx}/cite/images/no_pic.png' alt='' onload='resetImg(this)'>
                                        </a>
                                        <p>分享估价二维码</p>
                                    </li>
                                </ul>
                            </div>
                        </c:if>
                        <ul class="details-li">
                            <li>订单编号：<span id="eventNo"></span></li>
                            <li>申请理赔时间：<span id="timeApply"></span></li>
                            <c:if test="${statusEvent == 2 || statusEvent == 3}">
                                <li>审核时间：<span id="timeExamine"></span></li>
                            </c:if>
                        </ul>
                        <div class="check-btn">
                        	<button id="historyBut" type="button" class="btn btn-primary" onclick="showHistory()" style="display:none;">查看历史</button>
                            <button type="button" class="btn btn-primary" onclick="invalidOrder()">废弃订单</button>
                            <c:if test="${statusEvent == 1}">
                                <button type="button" class="btn btn-danger" onclick="fail()">不通过</button>
                                <button type="button" class="btn btn-primary" onclick="success()">通过</button>
                            </c:if>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>


<div class="row margin-top-ele" id="historyInfo" style="display: none">
            <div class="col-lg-12 col-xs-12">
                <div class="panel panel-info">
                    <div class="panel-heading">
                        <h3 class="panel-title">历史不通过记录</h3>
                    </div>
                    <div class="panel-body">
                        <ul class="details-li">
                            <li>车主姓名：<span id="hnameCarOwner"></span></li>
                            <li>车牌号：<span id="hlicensePlateNumber"></span></li>
                            <li><span id="statusEvent"></span></li>
                        </ul>
                        <ul class="details-li">
                            <li>品牌型号：<span id="hmodel"></span></li>
                            <li>手机号码：<span id="hcustomerPN"></span></li>
                            <li>剩余理赔额度：<span id="hamtCompensation"></span></li>
                        </ul>
                      
                        <c:if test="${statusEvent == 2}">
                            <ul class="details-li">
                                <li>未通过原因：<span id="happlyFailure">未填写未通过原因</span></li>
                            </ul>
                        </c:if>
                        <ul class="details-title">
                            <li class="font-we">车损事故报告</li>
                        </ul>
                        <c:if test="${statusEvent == 3}">
                            <ul class="details-li">
                                <li>平台审核说明：<span id="happlySuccess">未填写审核说明</span></li>
                            </ul>
                        </c:if>
                        <ul class="details-li">
                            <li>事故描述：<span id="haccidentDescription"></span></li>
                        </ul>
                        <div class="card-info clearH">
                          <%--   <ul class="img-info">
                                <li>
                                    <a class='card-img' onclick='scaleImg(this)'>
                                        <img id="drivingLicense" src='${ctx}/cite/images/no_pic.png' alt='' onload='resetImg(this)'>
                                    </a>
                                </li>
                            </ul> --%>
                            <ul id="HaccidentImg" class="img-info"></ul>
                        </div>
                        <ul class="details-title" id="HcarTitle"></ul>
                        <div class="card-info" id="hcarPhotos"></div>
                        <ul class="details-li">
                            <li>订单编号：<span id="heventNo"></span></li>
                            <li>申请理赔时间：<span id="htimeApply"></span></li>
                            <c:if test="${statusEvent == 2 || statusEvent == 3}">
                                <li>审核时间：<span id="htimeExamine"></span></li>
                            </c:if>
                        </ul>
                    </div>
                </div>
            </div>
        </div>
<!-- 照片弹窗 -->
<jsp:include page="/pages/branch/imagePopup.jsp"/>
<!--审核失败弹窗-->
<div id="defeated-layer">
    <div class="input-reason">
        <h3>未通过原因</h3>
        <input type="radio" name="reasonFailureNew" value="上传的车辆照片不符合要求">上传的车辆照片不符合要求<br>
        <input type="radio" name="reasonFailureNew" value="您的车辆不属于互助申请范围内">您的车辆不属于互助申请范围内<br>
        <input type="radio" name="reasonFailureNew" value="车辆信息有误">车辆信息有误<br>
        <input type="radio" name="reasonFailureNew" value="车辆有旧伤">车辆有旧伤<br>
        <input type="radio" name="reasonFailureNew" value="其他原因">其他原因<br>
        <input type="text" id="reason-other" placeholder="请填写具体原因" style="width:100%;margin-top:6px;display:none;">
    </div>
</div>
<div id="defeated-layer2">
    <div class="input-reason">
        <h3>请填写通过原因</h3>
        <textarea id="reasonSuccessNew" onkeyup="value=value.replace(/\s/g,'')"></textarea>
    </div>
</div>
<script type="text/javascript">
    $(document).ready(function (){
        $.ajax({
            type : "post",
            dataType : "json",
            headers: {rqSide: ex.pc()},
            data : JSON.stringify({"eventNo":"${eventNo}"}),
            contentType : "application/json;charset=utf-8",
            url: "${ctx}/event/applydetail",
            success : function(datas){
                if(datas.code == "0"){
                    var applyDetail = datas.data;
                    var statusEventInt = ex.judgeEmpty(applyDetail.statusEvent);
                    // 基本信息
                    $("#nameCarOwner").html(ex.judgeEmpty(applyDetail.nameCarOwner));
                    $("#licensePlateNumber").html(ex.judgeEmpty(applyDetail.licensePlateNumber));
                    $("#model").html(ex.judgeEmpty(applyDetail.model));
                    $("#customerPN").html(ex.judgeEmpty(applyDetail.customerPN));
                    $("#amtCompensation").html(ex.judgeEmptyOr0(applyDetail.amtCompensation)+"元");
                    $("#statusEvent").html(ex.statusEvent(applyDetail.statusEvent));
                    $("#compensateNum").html(ex.judgeEmptyOr0(applyDetail.compensateNum)+"次");
                    //未通过原因
                    if(statusEventInt==2){
                        if(ex.judgeEmpty(applyDetail.applyFailure)){
                            $("#applyFailure").html(ex.judgeEmpty(applyDetail.applyFailure));
                        }
                    }
                    //查询历史定损不通过
                    console.log(applyDetail.enentFailFlag);
                    if(applyDetail.enentFailFlag){
                    	document.getElementById('historyBut').style.display = "inline-block";
                    }
                    // 车损事故报告
                    if(statusEventInt==2 || statusEventInt==3){
                        $("#timeExamine").html(ex.judgeEmpty(applyDetail.timeExamine)?ex.reTime("s",applyDetail.timeExamine.time):"");
                        if(ex.judgeEmpty(applyDetail.applySuccess)){
                            $("#applySuccess").html(ex.judgeEmpty(applyDetail.applySuccess));
                        }
                    }
                    $("#accidentDescription").html(ex.judgeEmpty(applyDetail.accidentDescription));
                    $("#drivingLicense").attr('src',ex.judgeEmpty(applyDetail.drivingLicense));
                    var accidentImg = ex.judgeEmpty(applyDetail.accidentImg);
                    var accidentImgHtml = "";
                    if(accidentImg){
                        for (var i=0;i < accidentImg.length;i++){
                            accidentImgHtml += "<li><a class='card-img' onclick='scaleImg(this)'>";
                            accidentImgHtml += "<img src='"+ accidentImg[i] +"' alt='' onload='resetImg(this)'>";
                        }
                    }
                    $("#accidentImg").html(accidentImgHtml);
                    //分享估价二维码
                    if(statusEventInt==3 && ex.judgeEmpty(applyDetail.eventQrcode)){
                        $("#eventQrcode").attr('src',applyDetail.eventQrcode[0]);
                    }
                    //事故前照片
                    if(ex.judgeEmpty(applyDetail.carPhotos)){
                        $("#carTitle").html("<li class='font-we'>车辆事故前信息</li>");
                        var carPhotos = JSON.parse(applyDetail.carPhotos);
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
                    $("#eventNo").html(ex.judgeEmpty(applyDetail.eventNo));
                    $("#timeApply").html(ex.judgeEmpty(applyDetail.timeApply)?ex.reTime("s",applyDetail.timeApply.time):"");
                }else{
                    alertBtn("初始化数据失败!请重试或联系管理员!");
                }
            },
            error:function (e) {
                alertBtn("请求失败!请检查网络或联系管理员!");
            }
        });
    });
    
    //加载历史未通过
    $(document).ready(function (){
        $.ajax({
            type : "post",
            dataType : "json",
            headers: {rqSide: ex.pc()},
            data : JSON.stringify({"eventNo":"${eventNo}"}),
            contentType : "application/json;charset=utf-8",
            url: "${ctx}/event/applyFailDetail",
            success : function(datas){
                if(datas.code == "0"){
                    var applyDetail = datas.data.eventApplyFail;
                    // 基本信息
                    $("#hnameCarOwner").html(ex.judgeEmpty(applyDetail.nameCarOwner));
                    $("#hlicensePlateNumber").html(ex.judgeEmpty(applyDetail.licensePlateNumber));
                    $("#hmodel").html(ex.judgeEmpty(applyDetail.model));
                    $("#hcustomerPN").html(ex.judgeEmpty(applyDetail.customerPN));
                    $("#hamtCompensation").html(ex.judgeEmptyOr0(applyDetail.amtCompensation)+"元");
                    //未通过原因
                    if(ex.judgeEmpty(applyDetail.applyFailure)){
                       $("#happlyFailure").html(ex.judgeEmpty(applyDetail.reasonFailure));
                    }
                    // 车损事故报告
                    $("#htimeExamine").html(ex.judgeEmpty(applyDetail.timeExamine)?ex.reTime("s",applyDetail.timeExamine.time):"");
                    $("#haccidentDescription").html(ex.judgeEmpty(applyDetail.accidentDescription));
                    var accidentImg = ex.judgeEmpty(applyDetail.accidentImg);
                    var accidentImgHtml = "";
                    if(accidentImg){
                        for (var i=0;i < accidentImg.length;i++){
                            accidentImgHtml += "<li><a class='card-img' onclick='scaleImg(this)'>";
                            accidentImgHtml += "<img src='"+ accidentImg[i] +"' alt='' onload='resetImg(this)'>";
                        }
                    }
                    $("#HaccidentImg").html(accidentImgHtml);
                    //尾部时间
                    $("#heventNo").html(ex.judgeEmpty(applyDetail.eventNo));
                    $("#htimeApply").html(ex.judgeEmpty(applyDetail.timeApply)?ex.reTime("s",applyDetail.timeApply.time):"");
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
                var reasonSuccess = $("#reasonSuccessNew").val();
                var strJson = {"eventNo":"${eventNo}","timeExamine":ex.reTime("s",null),"reasonSuccess":reasonSuccess};
                $.ajax({
                    type : "post",
                    dataType : "json",
                    headers: {rqSide: ex.pc()},
                    data : JSON.stringify(strJson),
                    contentType : "application/json;charset=utf-8",
                    url: "${ctx}/event/applysuccess",
                    success : function(datas){
                        if(datas.code != "0"){
                            alert(datas.message)
                        }else{
                            self.location.href = "${ctx}/event/list.html";
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
    function fail(){
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
                var reasonFailure = $("input:radio[name='reasonFailureNew']:checked").val();
                if(reasonFailure=="其他原因"){
                    reasonFailure = $("#reason-other").val();
                }
                if (!reasonFailure) {
                    alertBtn("请选择失败原因");
                    return;
                }
                var strJson = {"eventNo":"${eventNo}","timeExamine":ex.reTime("s",null),"reasonFailure":reasonFailure};
                $.ajax({
                    type : "post",
                    dataType : "json",
                    headers: {rqSide: ex.pc()},
                    data : JSON.stringify(strJson),
                    contentType : "application/json;charset=utf-8",
                    url: "${ctx}/event/applyfail",
                    success : function(datas){
                        if(datas.code != "0"){
                            alert(datas.message)
                        }else{
                            self.location.href = "${ctx}/event/list.html";
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

    //显示不通过历史
    function showHistory(){
    	layer.open({
    		type: 1,
    		skin: 'layui-layer-rim', //加上边框
    		maxmin: true,
    		area: ['800px', '600px'], //宽高
    		content: $("#historyInfo").html()
    	});
    }
    
    //消息提示 弹窗
    function alertBtn(info){
        layer.alert(info, function(index){
            layer.close(index);
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
</script>
</body>
</html>