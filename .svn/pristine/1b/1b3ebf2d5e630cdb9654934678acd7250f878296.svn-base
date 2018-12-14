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
                <li><i class="fa fa-dashboard"></i> 成员</li>
                <li>渠道名单</li>
                <li>详情</li>
            </ul>
        </div>
        <div class="row margin-top-ele">
            <div class="col-lg-12 col-xs-12">
                <div class="panel panel-info">
                    <div class="panel-heading">
                        <h3 class="panel-title">商家详情</h3>
                    </div>
                    <div class="panel-body">
                        <ul class="details-title">
                            <li class="font-we">基本信息</li>
                        </ul>
                        <ul class="details-li">
                            <li>商家名称：<span id="name"></span></li>
                            <li>负责人员：<span id="linkman"></span></li>
                            <li><span id="status"></span></li>
                        </ul>
                        <ul class="details-li">
                            <li>手机号码：<span id="tel"></span></li>
                            <li>地址：<span id="address"></span></li>
                            <li>营业时间：<span id="businessHours"></span></li>
                        </ul>
                        <ul class="details-li">
                            <li>接单量：<span id="orderMonth"></span></li>
                            <li>接单范围：<span id="ordersRadius"></span></li>
                            <li>销售额：<span id="totalAmt"></span></li>
                        </ul>
                        <ul class="details-li">
                            <li>订单量：<span id="orderNum"></span></li>
                            <li>服务分：<span id="servicePoints"></span>&ensp;&ensp;&ensp;&ensp;<button type="button" class="btn" onclick="showServicePoints()">查看</button></li>
                            <li>微信关注数：<span id="attentionNum"></span></li>
                        </ul>
                        <ul class="details-li">
                            <li>注册数：<span id="registerNum"></span></li>
                            <li>员工人数：<span id="empNum">0</span>&ensp;&ensp;&ensp;<button type="button" class="btn" onclick="showEmployee()">查看</button></li>
                            <li>定价级别：<span id="levelShare"></span></li>
                        </ul>
                        <ul class="details-li">
                            <li>结算级别：<span id="levelSettlement"></span></li>
                            <li>店铺描述：<span id="shopDescribe"></span></li>
                        </ul>
                        <ul class="details-li">
                            <li>营销补贴：<span id="amtCoupon"></span></li>
                            <li>申请理赔数量：<span id="eventApplyNum"></span></li>
                            <li>理赔数量：<span id="eventNum"></span></li>
                            <li>理赔支出：<span id="expenses"></span></li>
                        </ul>
                        <ul class="details-li">
                            <li>添加车牌：<span id="addLicensePlateNumber"></span></li>
                            <li>支付：<span id="payNumber"></span></li>
                            <li>未添加照片：<span id="noAddPhotoNumber"></span></li>
                            <li>观察期：<span id="observationNumber"></span></li>
                            <li>保障中：<span id="securityNumber"></span></li>
                            <li>理赔数量：<span id="exitNumber"></span></li>
                        </ul>
                        <ul class="details-title">
                            <li class="font-we">图片信息</li>
                        </ul>
                        <div class="card-info clearH">
                            <ul class="img-info">
                                <li>
                                    <a class='card-img' onclick='scaleImg(this)'>
                                        <img id="qrcode" src='${ctx}/cite/images/no_pic.png' alt='' onload='resetImg(this)'>
                                    </a>
                                    <p>二维码</p>
                                </li>
                                <li>
                                    <a class='card-img' onclick='scaleImg(this)'>
                                        <img id="businessLicenseImg" src='${ctx}/cite/images/no_pic.png' alt='' onload='resetImg(this)'>
                                    </a>
                                    <p>营业执照</p>
                                </li>
                                <li>
                                    <a class='card-img' onclick='scaleImg(this)'>
                                        <img id="logo" src='${ctx}/cite/images/no_pic.png' alt='' onload='resetImg(this)'>
                                    </a>
                                    <p>店铺logo</p>
                                </li>
                            </ul>
                        </div>
                        <div class="card-info clearH">
                            <ul id="img" class="img-info"></ul>
                        </div>
                        <ul class="details-li">
                            <li>新建商家时间：<span id="timeJoin"></span></li>
                        </ul>
                        <div class="check-btn">
                        <c:if test="${status == 1}">
                            <button type="button" class="btn btn-primary" onclick="freeze()">冻结</button>
                        </c:if>
                        <c:if test="${status == 2}">
                            <button type="button" class="btn btn-primary" onclick="unfreeze()">解冻</button>
                        </c:if>
                            <button type="button" class="btn btn-primary" onclick="editShopPage()">编辑</button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<%--服务分弹窗--%>
<div class="update-lay-box" id="servicePointsLay">
    <div role="tabpanel" class="tab-pane active white-table">
        <table class="table table-bordered table-hover clear-border" id="servicePointsTable">
            <thead><tr><th class='col-xs-1'>描述</th><th class='col-xs-1'>分数</th><th class='col-xs-1'>时间</th></tr></thead>
            <tbody>
                <tr>
                    <td>呵呵</td>
                    <td>-5</td>
                    <td>2018.10.1</td>
                </tr>
            </tbody>
        </table>
    </div>
</div>
<%--员工弹窗--%>
<div class="update-lay-box" id="employeeLay">
    <div role="tabpanel" class="tab-pane active white-table">
        <table class="table table-bordered table-hover clear-border" id="employeeTable"></table>
    </div>
</div>
<!-- 照片弹窗 -->
<jsp:include page="/pages/branch/imagePopup.jsp"/>
<script type="text/javascript">
    $(document).ready(function () {
        $.ajax({
            type: "post",
            dataType: "json",
            headers: {rqSide: ex.pc()},
            data: JSON.stringify({"shopId": ${maintenanceshopId}}),
            contentType: "application/json;charset=utf-8",
            url: "${ctx}/maintenanceshop/shopDetail",
            success: function (datas) {
                if (datas.code == "0") {
                    var shopDetail = datas.data;
                    $("#name").html(ex.judgeEmpty(shopDetail.name));
                    $("#linkman").html(ex.judgeEmpty(shopDetail.linkman));
                    $("#status").html(ex.judgeEmpty(shopDetail.status)==1?"正常":ex.judgeEmpty(shopDetail.status)==2?"冻结":"未知");
                    $("#tel").html(ex.judgeEmpty(shopDetail.tel));
                    $("#address").html(ex.judgeEmpty(shopDetail.address));
                    $("#businessHours").html(ex.judgeEmpty(shopDetail.businessHours));
                    $("#orderMonth").html(ex.judgeEmptyOr0(shopDetail.orderMonth)+"单/月");
                    $("#ordersRadius").html(ex.judgeEmptyOr0(shopDetail.ordersRadius)+"公里");
                    $("#totalAmt").html(ex.judgeEmpty(shopDetail.totalAmt));
                    $("#orderNum").html(ex.judgeEmpty(shopDetail.orderNum));
                    $("#servicePoints").html(ex.judgeEmpty(shopDetail.servicePoints));
                    $("#attentionNum").html(ex.judgeEmpty(shopDetail.attentionNum));
                    $("#registerNum").html(ex.judgeEmpty(shopDetail.registerNum));
                    $("#amtCoupon").html(ex.judgeEmptyOr0(shopDetail.amtCoupon));
                    $("#eventApplyNum").html(ex.judgeEmptyOr0(shopDetail.eventApplyNum));
                    $("#eventNum").html(ex.judgeEmptyOr0(shopDetail.eventNum));
                    $("#expenses").html(ex.judgeEmptyOr0(shopDetail.expenses));
                    $("#addLicensePlateNumber").html(ex.judgeEmptyOr0(shopDetail.addLicensePlateNumber));
                    $("#payNumber").html(ex.judgeEmptyOr0(shopDetail.payNumber));
                    $("#noAddPhotoNumber").html(ex.judgeEmptyOr0(shopDetail.noAddPhotoNumber));
                    $("#observationNumber").html(ex.judgeEmptyOr0(shopDetail.observationNumber));
                    $("#securityNumber").html(ex.judgeEmptyOr0(shopDetail.securityNumber));
                    $("#exitNumber").html(ex.judgeEmptyOr0(shopDetail.exitNumber));
                    if(ex.judgeEmpty(shopDetail.empNum)){
                        $("#empNum").html(shopDetail.empNum);
                    }
                    $("#levelShare").html(ex.judgeEmpty(shopDetail.levelShare));
                    $("#levelSettlement").html(ex.judgeEmpty(shopDetail.levelSettlement));
                    $("#shopDescribe").html(ex.judgeEmpty(shopDetail.shopDescribe));
                    if(ex.judgeEmpty(shopDetail.qrcode)){
                        $("#qrcode").attr('src',shopDetail.qrcode);
                    }
                    if(ex.judgeEmpty(shopDetail.businessLicenseImg)){
                        $("#businessLicenseImg").attr('src',shopDetail.businessLicenseImg);
                    }
                    if(ex.judgeEmpty(shopDetail.logo)){
                        $("#logo").attr('src',shopDetail.logo);
                    }
                    var img = ex.judgeEmpty(shopDetail.img);
                    var imgHtml = "";
                    if(img){
                        for (var i=0;i < img.length;i++){
                            imgHtml += "<li><a class='card-img' onclick='scaleImg(this)'>";
                            imgHtml += "<img src='"+ img[i] +"' alt='' onload='resetImg(this)'>";
                            imgHtml += "</a><p>门店照片</p></li>";
                        }
                    }
                    $("#img").html(imgHtml);
                    $("#timeJoin").html(ex.judgeEmpty(shopDetail.timeJoin) ? ex.reTime("s", shopDetail.timeJoin.time) : "");
                } else {
                    alertBtn("初始化数据失败!请重试或联系管理员!");
                }
            },
            error: function (e) {
                alertBtn("请求失败!请检查网络或联系管理员!");
            }
        });
    });

    //冻结商家
    function freeze() {
        $.ajax({
            type : "post",
            dataType : "json",
            headers: {rqSide: ex.pc()},
            data : JSON.stringify({"id":${maintenanceshopId}}),
            contentType : "application/json;charset=utf-8",
            url: "${ctx}/maintenanceshop/freeze",
            success : function(datas){
                if(datas.code != "0"){
                    alert(datas.message)
                }else{
                    self.location.href = "${ctx}/maintenanceshop/detail.html?maintenanceshopId=" + ${maintenanceshopId} + "&status=2";
                }
            }
        });
    }

    //解冻商家
    function unfreeze() {
        $.ajax({
            type : "post",
            dataType : "json",
            headers: {rqSide: ex.pc()},
            data : JSON.stringify({"id":${maintenanceshopId}}),
            contentType : "application/json;charset=utf-8",
            url: "${ctx}/maintenanceshop/unfreeze",
            success : function(datas){
                if(datas.code != "0"){
                    alert(datas.message)
                }else{
                    self.location.href = "${ctx}/maintenanceshop/detail.html?maintenanceshopId=" + ${maintenanceshopId} + "&status=1";
                }
            }
        });
    }

    //编辑商家页面
    function editShopPage() {
        self.location.href = "${ctx}/maintenanceshop/editShop.html?maintenanceshopId=" + ${maintenanceshopId};
    }

    //展示商家员工
    function showEmployee(){
        $.ajax({
            type : "post",
            dataType : "json",
            headers: {rqSide: ex.pc()},
            data : JSON.stringify({"maintenanceshopId":${maintenanceshopId}}),
            contentType : "application/json;charset=utf-8",
            url: "${ctx}/maintenanceshop/employee",
            success : function(datas){
                if(datas.code != "0"){
                    alert(datas.message)
                }else{
                    var repData = datas.data;
                    var dataHtml = "<thead><tr><th class='col-xs-1'>姓名</th><th class='col-xs-1'>联系电话</th></tr></thead>";
                    dataHtml += "<tbody>";
                    for(var i=0;i < repData.length;i++){
                        var repDateChild = repData[i];
                        dataHtml += "<tr>" +
                            "<td>" + ex.judgeEmpty(repDateChild.name) + "</td>" +
                            "<td>" + ex.judgeEmpty(repDateChild.tel) + "</td>" +
                            "</tr>";
                    }
                    dataHtml += "</tbody>";
                    $("#employeeTable").html(dataHtml);
                    layer.open({
                        title:'商家员工',
                        closeBtn:'1',
                        type:1,
                        anim: 5,
                        area:["400px","300px"],
                        shadeClose: false, //点击遮罩关闭
                        content: $('#employeeLay'),
                    });
                }
            }
        });
    }

    //展示商家服务分
    function showServicePoints(){
        $.ajax({
            type : "post",
            dataType : "json",
            headers: {rqSide: ex.pc()},
            data : JSON.stringify({"maintenanceshopId":${maintenanceshopId}}),
            contentType : "application/json;charset=utf-8",
            url: "${ctx}/maintenanceshop/servicePoints",
            success : function(datas){
                if(datas.code != "0"){
                    alert(datas.message)
                }else{
                    var repData = datas.data;
                    var dataHtml = "<thead><tr><th>描述</th><th>分数</th><th>时间</th></tr></thead>";
                    dataHtml += "<tbody>";
                    for(var i=0;i < repData.length;i++){
                        var repDateChild = repData[i];
                        var score = 0;
                        if(repDateChild.type==11){
                            score = "-"+ ex.judgeEmptyOr0(repDateChild.score)
                        }
                        if(repDateChild.type==12){
                            score = "+"+ ex.judgeEmptyOr0(repDateChild.score)
                        }
                        var createAt = ex.judgeEmpty(repDateChild.createAt) ? ex.reTime("s", repDateChild.createAt.time) : "";
                        dataHtml += "<tr>" +
                            "<td>" + ex.judgeEmpty(repDateChild.content) + "</td>" +
                            "<td>" + score + "</td>" +
                            "<td>" + createAt + "</td>" +
                            "</tr>";
                    }
                    dataHtml += "</tbody>";
                    $("#servicePointsTable").html(dataHtml);
                    layer.open({
                        title:'服务分记录',
                        closeBtn:'1',
                        type:1,
                        anim: 5,
                        area:["800px","500px"],
                        shadeClose: false, //点击遮罩关闭
                        content: $('#servicePointsLay'),
                    });
                }
            }
        });
    }

    //消息提示 弹窗
    function alertBtn(info) {
        layer.alert(info, function (index) {
            layer.close(index);
        });
    }
</script>
</body>
</html>