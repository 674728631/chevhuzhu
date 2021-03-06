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
            <ul class="breadcrumb index-bread col-xs-12">
                <%--<li><a href="${ctx}/main.html"><i class="fa fa-dashboard"></i> 首页</a></li>--%>
                <li><i class="fa fa-dashboard"></i> 成员</li>
                <li>用户名单</li>
            </ul>
        </div>
        <div class="row margin-row-max">
            <div class="col-sm-4 new-search-btn">
                <input id="searchInfo" type="text" class="form-control" aria-describedby="inputGroupSuccess1Status" onkeyup="value=value.replace(/\s/g,'')" placeholder="请输入手机号搜索信息">
                <button type="button" class="btn def-btn btn-success" onclick="search()">搜索</button>
            </div>
        </div>
        <div class="row">
            <div class="col-lg-12 col-xs-12">
                <div>
                    <ul class="nav nav-tabs nav-bac" role="tablist">
                        <li role="presentation" class="active"><a id="totalNum" href="#home_1" aria-controls="home_1" role="tab" data-toggle="tab" onclick="customerListData(1)">全部</a></li>
                        <li role="presentation" class="flag1"><a id="normalNum" href="#home_1" aria-controls="home_1" role="tab" data-toggle="tab" onclick="customerListData(1,1)">正常</a></li>
                        <li role="presentation" id="flag1"><a id="freezeNum" href="#home_1" aria-controls="home_1" role="tab" data-toggle="tab" onclick="customerListData(1,2)">冻结</a></li>
                    </ul>
                    <!-- Tab panes -->
                    <div class="tab-content index-tab-content">
                        <div role="tabpanel" class="tab-pane active white-table" id="home_1">
                            <table class="table table-bordered table-hover clear-border" id="mainDataTable"></table>
                            <%-- 数据分页(会显示所有数据所分页数) --%>
                            <div id="blankMessage" style="text-align:center;line-height:160px;font-size:18px;font-weight: bold;"></div>
                            <div class="panel-footer">
                                <div id="kkpager" class='kkpager' style="text-align: right; margin:10px 3px; margin-left: 130px"></div>
                                <div class="clearfix"></div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<div id="defeated-layer">
    <div class="input-reason">
        <div style="margin-top:6px;overflow: hidden;line-height:32px;">
            <li style="float:left;"><img id="portrait" src='${ctx}/cite/images/no_pic.png' style='width:32px;height:32px;border-radius:50%;vertical-align: top;margin-right:4px;' alt=''><font id="nickname"></font></li>
            <li style="float:right;"><span id="customerStatus"></span></li>
        </div>
        <div style="margin-top:6px;overflow: hidden;">
            <li style="float:left;">手机号：<span id="customerPN"></span></li>
            <li style="float:right;">互助金：<span id="totalAmtCooperation"></span></li>
        </div>
        <div style="margin-top:6px;overflow: hidden;">
            <li style="float:left;">用户来源：<span id="source">自然用户</span></li>
            <li style="float:right;">加入天数：<span id="joinDay"></span></li>
        </div>
        <li style="margin-top:6px;"><span id="totalCarNum">添加的车辆（0）：</span><span id="totalCar">暂无车辆</span></li>
        <li style="margin-top:6px;"><span id="guaranteeCarNum">保障中车辆（0）：</span><span id="guaranteeCar">暂无车辆</span></li>
        <li style="margin:6px 0;">用户注册时间：<span id="timeJoin"></span></li>
    </div>
</div>
<script type="text/javascript">
    var pageSize=10;

    //加载用户统计
    function customerCount(searchInfo) {
        $.ajax({
            type : "post",
            dataType : "json",
            headers: {rqSide: ex.pc()},
            data : JSON.stringify({"customerPN":searchInfo}),
            contentType : "application/json;charset=utf-8",
            url: "${ctx}/customer/count",
            success : function(datas){
                if(datas.code == "0"){
                    var result = datas.data;
                    $("#totalNum").html("全部("+result.totalNum+")");
                    $("#normalNum").html("正常("+result.normalNum+")");
                    $("#freezeNum").html("冻结("+result.freezeNum+")");
                }
            }
        });
    }

    customerListData(1);
    //加载页面数据
    function customerListData(pageNo,status){
        var searchInfo = $("#searchInfo").val();
        customerCount(searchInfo);
        var strJson = {"pageNo":pageNo,"pageSize":pageSize,"customerPN":searchInfo,"status":status};
        $.ajax({
            type : "post",
            dataType : "json",
            headers: {rqSide: ex.pc()},
            data : JSON.stringify(strJson),
            contentType : "application/json;charset=utf-8",
            url: "${ctx}/customer/list",
            success : function(datas){
                if(datas.code == "0"){
                    var repData = datas.data.list;
                    var dataHtml = "<thead><tr><th>头像</th><th>用户昵称</th><th>手机号</th><th>添加的车辆</th><th>保障中车辆</th><th>理赔次数</th><th>渠道来源</th><th>状态</th><th>时间</th><th>操作</th></tr></thead>";
                    dataHtml += "<tbody>";
                    if (0 == repData.length) {
                        dataHtml += "</tbody>";
                        $("#mainDataTable").html(dataHtml);
                        $("#blankMessage").text("暂无名单信息");
                    } else {
                        for(var i=0;i < repData.length;i++){
                        var repDateChild = repData[i];
                        var customerStatus = ex.judgeEmpty(repDateChild.status)==1?"正常":ex.judgeEmpty(repDateChild.status)==2?"冻结":"未知";
                        var timeJoin = ex.judgeEmpty(repDateChild.timeJoin)?ex.reTime("s",repDateChild.timeJoin.time):"";
                        dataHtml += "<tr>";
                        if(ex.judgeEmpty(repDateChild.portrait)){
                            dataHtml += "<td><img src='"+ repDateChild.portrait +"' style='width:32px;height:32px;border-radius:50%;' alt=''></td>";
                        }else{
                            dataHtml += "<td><img src='${ctx}/cite/images/no_pic.png' style='width:32px;height:32px;border-radius:50%;' alt=''></td>";
                        }
                        var shopName = ex.judgeEmpty(repDateChild.shopName)?repDateChild.shopName:"自然用户";
                        dataHtml += "<td>" + ex.judgeEmpty(repDateChild.nickname) + "</td>" +
                            "<td>" + ex.judgeEmpty(repDateChild.customerPN) + "</td>" +
                            "<td>" + ex.judgeEmptyOr0(repDateChild.carNum) + "</td>" +
                            "<td>" + ex.judgeEmptyOr0(repDateChild.carInGuaranteeNum) + "</td>" +
                            "<td>" + ex.judgeEmptyOr0(repDateChild.eventNum) + "</td>" +
                            "<td>" + shopName + "</td>" +
                            "<td>" + customerStatus + "</td>" +
                            "<td>" + timeJoin + "</td>" +
                            "<td>" +
                            "<button type='button' class='btn btn-info' onclick='showDetail(" + repDateChild.id + ")'>查看</button>&nbsp;" +
                            "</td>" +
                            "</tr>";
                        }
                        dataHtml += "</tbody>";
                        $("#mainDataTable").html(dataHtml);
                    }
                    if(datas.data.total > -1){
                        kkpager.init({
                            pagerid : "kkpager",
                            pno : datas.data.pageNum,//当前页
                            total : datas.data.pages,//总页码
                            totalRecords : datas.data.total,//总数据条数
                            mode : 'click',//默认值是link，可选link或者click
                            click : function(n){
                                customerListData(n,status);
                                window.location.href="#top";
                            }
                        });
                        kkpager.generPageHtml();
                    }
                }
            }
        });
    }

    //用户详情弹窗
    function showDetail(customerId){
        $.ajax({
            type : "post",
            dataType : "json",
            headers: {rqSide: ex.pc()},
            data : JSON.stringify({"customerId":customerId}),
            contentType : "application/json;charset=utf-8",
            url: "${ctx}/customer/detail",
            success : function(datas) {
                if (datas.code == "0") {
                    var customerDetail = datas.data;
                    if(ex.judgeEmpty(customerDetail.portrait)){
                        $("#portrait").attr('src',ex.judgeEmpty(customerDetail.portrait));
                    }else{
                        $("#portrait").attr('src','${ctx}/cite/images/no_pic.png');
                    }
                    $("#nickname").html(ex.judgeEmpty(customerDetail.nickname));
                    var customerStatus = ex.judgeEmpty(customerDetail.status)==1?"正常":ex.judgeEmpty(customerDetail.status)==2?"冻结":"未知";
                    $("#customerStatus").html(customerStatus);
                    $("#customerPN").html(ex.judgeEmpty(customerDetail.customerPN));
                    $("#totalAmtCooperation").html(ex.judgeEmptyOr0(customerDetail.totalAmtCooperation)+"元");
                    if(ex.judgeEmpty(customerDetail.source)){
                        $("#source").html(customerDetail.source);
                    }else{
                        $("#source").html('自然用户');
                    }
                    $("#joinDay").html(ex.calcDays(customerDetail.timeJoin.time)+"天");
                    if(ex.judgeEmpty(customerDetail.totalCar)){
                        $("#totalCar").html(customerDetail.totalCar);
                        $("#totalCarNum").html("添加的车辆（"+ customerDetail.totalCarNum +"）：");
                    }else{
                        $("#totalCar").html("暂无车辆");
                        $("#totalCarNum").html("添加的车辆（0）：");
                    }
                    if(ex.judgeEmpty(customerDetail.guaranteeCar)){
                        $("#guaranteeCar").html(customerDetail.guaranteeCar);
                        $("#guaranteeCarNum").html("保障中车辆（"+ customerDetail.guaranteeCarNum +"）：");
                    }else {
                        $("#guaranteeCar").html("暂无车辆");
                        $("#guaranteeCarNum").html("保障中车辆（0）：");
                    }
                    $("#timeJoin").html(ex.judgeEmpty(customerDetail.timeJoin) ? ex.reTime("s", customerDetail.timeJoin.time) : "");
                    if(customerDetail.status ==1){
                        layer.open({
                            type: 1,
                            title: '用户详情',
                            closeBtn: 1,
                            area: '450px;',
                            shade: 0.5,
                            btn: ['冻结','取消'],
                            btnAlign: 'c',
                            moveType: 1, //拖拽模式，0或者1
                            content: $("#defeated-layer"),
                            yes:function(index, layero){
                                $.ajax({
                                    type : "post",
                                    dataType : "json",
                                    headers: {rqSide: ex.pc()},
                                    data : JSON.stringify({"id":customerId}),
                                    contentType : "application/json;charset=utf-8",
                                    url: "${ctx}/customer/freeze",
                                    success : function(datas){
                                        if(datas.code != "0"){
                                            alert(datas.message)
                                        }else{
                                            location.reload();
                                        }
                                    }
                                });
                            }
                        })
                    }else{
                        layer.open({
                            type: 1,
                            title: '用户详情',
                            closeBtn: 1,
                            area: '450px;',
                            shade: 0.5,
                            btn: ['解冻','取消'],
                            btnAlign: 'c',
                            moveType: 1, //拖拽模式，0或者1
                            content: $("#defeated-layer"),
                            yes:function(index, layero){
                                $.ajax({
                                    type : "post",
                                    dataType : "json",
                                    headers: {rqSide: ex.pc()},
                                    data : JSON.stringify({"id":customerId}),
                                    contentType : "application/json;charset=utf-8",
                                    url: "${ctx}/customer/unfreeze",
                                    success : function(datas){
                                        if(datas.code != "0"){
                                            alert(datas.message)
                                        }else{
                                            location.reload();
                                        }
                                    }
                                });
                            }
                        })
                    }
                } else {
                    alertBtn(datas.message);
                }
            }
        });
    }

    function search() {
        $("li[class=active][role=presentation]").children().click();
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