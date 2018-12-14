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
                        <c:if test="${statusEvent == 11}">
                            <ul class="details-title">
                                <li class="font-we">维修商家信息</li>
                            </ul>
                            <ul class="details-li">
                                <li>商家名称：<span id="shopName2"></span></li>
                                <li>联系电话：<span id="tel"></span></li>
                                <li>地址：<span id="address"></span></li>
                            </ul>
                        </c:if>
                        <ul class="details-title">
                            <li class="font-we">联系人信息</li>
                        </ul>
                        <ul class="details-li">
                            <li>联系人姓名：<span id="receiveManName"></span></li>
                            <li>联系电话：<span id="telCarOwner"></span></li>
                            <li>地址：<span id="place"></span></li>
                        </ul>
                        <c:if test="${statusEvent == 12}">
                            <ul class="details-title">
                                <li class="font-we">放弃接单信息</li>
                            </ul>
                            <ul class="details-li">
                                <li>商家名称：<span id="shopName"></span></li>
                                <li>撤单原因：<span id="cancellationsReason">未填写撤单原因</span></li>
                                <li>撤单说明：<span id="cancellationsInstructions">未填写撤单说明</span></li>
                            </ul>
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
                        <c:if test="${statusEvent == 10}">
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
                            <c:if test="${statusEvent == 10}">
                                <li>申请分单时间：<span id="createAt"></span></li>
                            </c:if>
                            <c:if test="${statusEvent == 11 || statusEvent == 12}">
                                <li>分单时间：<span id="createAt2"></span></li>
                            </c:if>
                        </ul>
                        <div class="check-btn">
                            <button type="button" class="btn btn-primary" onclick="invalidOrder()">废弃订单</button>
                            <c:if test="${statusEvent == 10 || statusEvent == 12}">
                                <button type="button" class="btn btn-primary" onclick="distribution()">立即分单</button>
                            </c:if>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<!--重新分配弹窗-->
<div id="ad-layer">
    <div class="map-address">起始位置：<span id="carAddress"></span></div>
    <div class="map-address">终点位置：<span id="shopAddress" data-value=""></span></div>
    <%--地图--%>
    <div id="mapContainer" style="width: 76%;height: 85%;top:80px;left: 2%;"></div>
    <%--定位 描述--%>
    <div id="mapContainerDes" style="" class="pre-default-timeline"></div>
</div>
<!-- 照片弹窗 -->
<jsp:include page="/pages/branch/imagePopup.jsp"/>
<script type="text/javascript" src="https://webapi.amap.com/demos/js/liteToolbar.js"></script>
<script type="text/javascript">
    var receiveDetail;
    var shopData;

    $(document).ready(function (){
        $.ajax({
            type : "post",
            dataType : "json",
            headers: {rqSide: ex.pc()},
            data : JSON.stringify({"eventNo":"${eventNo}"}),
            contentType : "application/json;charset=utf-8",
            url: "${ctx}/event/distributionDetail",
            success : function(datas){
                if(datas.code == "0"){
                    receiveDetail = datas.data;
                    var statusEventInt = ex.judgeEmpty(receiveDetail.statusEvent);
                    // 基本信息
                    $("#nameCarOwner").html(ex.judgeEmpty(receiveDetail.nameCarOwner));
                    $("#licensePlateNumber").html(ex.judgeEmpty(receiveDetail.licensePlateNumber));
                    $("#model").html(ex.judgeEmpty(receiveDetail.model));
                    $("#customerPN").html(ex.judgeEmpty(receiveDetail.customerPN));
                    $("#amtCompensation").html(ex.judgeEmptyOr0(receiveDetail.amtCompensation)+"元");
                    $("#statusEvent").html(ex.statusEvent(receiveDetail.statusEvent));
                    //维修商家信息
                    if(statusEventInt==11){
                        $("#shopName2").html(ex.judgeEmpty(receiveDetail.shopName));
                        if(ex.judgeEmpty(receiveDetail.tel)){
                            $("#tel").html(receiveDetail.tel);
                        }
                        if(ex.judgeEmpty(receiveDetail.address)){
                            $("#address").html(receiveDetail.address);
                        }
                    }
                    //联系人信息
                    $("#receiveManName").html(ex.judgeEmpty(receiveDetail.receiveManName));
                    $("#telCarOwner").html(ex.judgeEmpty(receiveDetail.telCarOwner));
                    $("#place").html(ex.judgeEmpty(receiveDetail.place));
                    //放弃接单信息
                    if(statusEventInt==12){
                        $("#shopName").html(ex.judgeEmpty(receiveDetail.shopName));
                        if(ex.judgeEmpty(receiveDetail.cancellationsInstructions)){
                            $("#cancellationsInstructions").html(receiveDetail.cancellationsInstructions);
                        }
                        if(ex.judgeEmpty(receiveDetail.cancellationsReason)){
                            $("#cancellationsReason").html(receiveDetail.cancellationsReason);
                        }
                    }
                    // 车损事故报告
                    if(ex.judgeEmpty(receiveDetail.applySuccess)){
                        $("#applySuccess").html(receiveDetail.applySuccess);
                    }
                    if(ex.judgeEmpty(receiveDetail.accidentDescription)){
                        $("#accidentDescription").html(receiveDetail.accidentDescription);
                    }
                    $("#drivingLicense").attr('src',ex.judgeEmpty(receiveDetail.drivingLicense));
                    var accidentImg = ex.judgeEmpty(receiveDetail.accidentImg);
                    var accidentImgHtml = "";
                    if(accidentImg){
                        for (var i=0;i < accidentImg.length;i++){
                            accidentImgHtml += "<li><a class='card-img' onclick='scaleImg(this)'>";
                            accidentImgHtml += "<img src='"+ accidentImg[i] +"' alt='' onload='resetImg(this)'>";
                        }
                    }
                    $("#accidentImg").html(accidentImgHtml);
                    //分享估价二维码
                    if(statusEventInt==10 && ex.judgeEmpty(receiveDetail.eventQrcode)){
                        $("#eventQrcode").attr('src',receiveDetail.eventQrcode[0]);
                    }
                    //事故前照片
                    if(ex.judgeEmpty(receiveDetail.carPhotos)){
                        $("#carTitle").html("<li class='font-we'>车辆事故前信息</li>");
                        var carPhotos = JSON.parse(receiveDetail.carPhotos);
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
                    $("#eventNo").html(ex.judgeEmpty(receiveDetail.eventNo));
                    $("#timeApply").html(ex.judgeEmpty(receiveDetail.timeApply)?ex.reTime("s",receiveDetail.timeApply.time):"");
                    if(statusEventInt==10){
                        $("#createAt").html(ex.judgeEmpty(receiveDetail.createAt)?ex.reTime("s",receiveDetail.createAt.time):"");
                    }
                    if(statusEventInt==11 || statusEventInt==12){
                        $("#createAt2").html(ex.judgeEmpty(receiveDetail.createAt)?ex.reTime("s",receiveDetail.createAt.time):"");
                    }
                    //请求维修厂数据并在地图上标记维修厂位置
                    $.ajax({
                        type : "post",
                        dataType : "json",
                        headers: {rqSide: ex.pc()},
                        data : JSON.stringify({"longitude":receiveDetail.longitude,"latitude":receiveDetail.latitude}),
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
                }else{
                    alertBtn("初始化数据失败!请重试或联系管理员!");
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

    //分配维修商家弹窗
    function distribution(){
        var repeatFlag = false;
        //回显接车地址
        $("#carAddress").html(receiveDetail.place);
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
            content: $('#ad-layer'),
            yes:function(index, layero){
                // 处理多次点击
                if (repeatFlag) {
                    console.log("你重复点击了分单按钮!");
                    return ;
                }
                repeatFlag = true;
                var maintenanceshopId = $("#shopAddress").attr("data-value");
                if (!maintenanceshopId) {
                    alert("请选择维修厂");
                    return;
                }
                var strJson = {"eventNo":"${eventNo}","maintenanceshopId":maintenanceshopId,"licensePlateNumber":receiveDetail.licensePlateNumber,"model":receiveDetail.model};
                $.ajax({
                    type : "post",
                    dataType : "json",
                    headers: {rqSide: ex.pc()},
                    data : JSON.stringify(strJson),
                    contentType : "application/json;charset=utf-8",
                    url: "${ctx}/event/distribution",
                    success : function(datas){
                        repeatFlag = false;
                        if(datas.code != "0"){
                            alert(datas.message)
                        }else{
                            self.location.href = "${ctx}/event/list.html";
                        }
                    },
                    error : function (XMLHttpRequest, textStatus, errorThrown) {
                        repeatFlag = false;
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
        if(receiveDetail.longitude && receiveDetail.latitude){
            var marker =new AMap.Marker({
                position:[receiveDetail.longitude/1000000,receiveDetail.latitude/1000000],
                map:map,
                icon: new AMap.Icon({
                    size: new AMap.Size(30,40),
                    image: "${ctx}/cite/images/icon_yh.png",
                    imageOffset: new AMap.Pixel(0,0),
                    imageSize: new AMap.Size(30,40)
                })
            });
            var info = '<div><div class="amap-adcontent-body">' +
                '<div class="info-title">姓&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;名：'+ receiveDetail.nameCarOwner +'</div>' +
                '<div class="info-content">电&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;话：'+ receiveDetail.telCarOwner +'<br>' +
                '</div>' +
                '<div class="amap-info-combo"></div>' +
                '</div>' +
                '<div class="amap-combo-sharp"></div>' +
                '<a class="amap-adcombo-close" href="javascript: void(0)"></a>';
            marker.content=info;
            marker.on('click',markerClick);
            changeMapCenter(receiveDetail.longitude/1000000,receiveDetail.latitude/1000000,info);
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
</script>
</body>
</html>