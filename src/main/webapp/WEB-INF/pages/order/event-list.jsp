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
            </ul>
        </div>
        <div class="row margin-row-max">
            <div class="col-sm-4 new-search-btn">
                <input id="searchInfo" type="text" class="form-control" aria-describedby="inputGroupSuccess1Status" onkeyup="value=value.replace(/\s/g,'')" placeholder="请输入车主姓名、手机号、车牌号进行搜索订单">
                <button type="button" class="btn def-btn btn-success" onclick="eventListData(1)">搜索</button>
            </div>
            <div class="col-sm-4">
                <button type="button" class="btn def-btn btn-success pull-left" onclick="exportData()" style="margin-left:6px;">导出</button>
            </div>
        </div>
       <div class="row">
            <div class="col-lg-12 col-xs-12">
                <div>
                    <!-- Nav tabs -->
                    <ul class="nav nav-tabs nav-bac" role="tablist">
                        <li role="presentation" class="active"><a id="totalNum" href="#home_1" aria-controls="home_1" role="tab" data-toggle="tab" onclick="changeTab('','','')">全部订单</a></li>
                        <li role="presentation" id="flag1"><a id="applyNum" href="#home_1" aria-controls="home_1" role="tab" data-toggle="tab" onclick="changeTab(1,'1','1')">待审核</a></li>
                        <li role="presentation" id="flag2"><a id="notPassNum" href="#home_1" aria-controls="home_1" role="tab" data-toggle="tab" onclick="changeTab(2,'1','2')">未通过</a></li>
                        <li role="presentation" id="flag3"><a id="passNum" href="#home_1" aria-controls="home_1" role="tab" data-toggle="tab" onclick="changeTab(3,'1','3')">已通过</a></li>
                        <li role="presentation" id="flag4"><a id="distributionNum" href="#home_1" aria-controls="home_1" role="tab" data-toggle="tab" onclick="changeTab(4,'1','10')">待分单</a></li>
                        <li role="presentation" id="flag5"><a id="receiveNum" href="#home_1" aria-controls="home_1" role="tab" data-toggle="tab" onclick="changeTab(5,'1','11')">待接单</a></li>
                        <li role="presentation" id="flag6"><a id="failReceiveNum" href="#home_1" aria-controls="home_1" role="tab" data-toggle="tab" onclick="changeTab(6,'1','12')">放弃接单</a></li>
                        <li role="presentation" id="flag7"><a id="waitAssertNum" href="#home_1" aria-controls="home_1" role="tab" data-toggle="tab" onclick="changeTab(7,'1','21')">待定损</a></li>
                        <li role="presentation" id="flag8"><a id="assertNum" href="#home_1" aria-controls="home_1" role="tab" data-toggle="tab" onclick="changeTab(8,'1','22')">定损待确认</a></li>
                        <li role="presentation" id="flag9"><a id="waitReceiveNum" href="#home_1" aria-controls="home_1" role="tab" data-toggle="tab" onclick="changeTab(9,'1','31')">待接车</a></li>
                        <li role="presentation" id="flag10"><a id="waitRepairNum" href="#home_1" aria-controls="home_1" role="tab" data-toggle="tab" onclick="changeTab(10,'1','51')">待维修</a></li>
                        <li role="presentation" id="flag11"><a id="repairNum" href="#home_1" aria-controls="home_1" role="tab" data-toggle="tab" onclick="changeTab(11,'1','52')">维修中</a></li>
                        <li role="presentation" id="flag12"><a id="receiveCarNum" href="#home_1" aria-controls="home_1" role="tab" data-toggle="tab" onclick="changeTab(12,'1','61')">待交车</a></li>
                        <li role="presentation" id="flag13"><a id="commentNum" href="#home_1" aria-controls="home_1" role="tab" data-toggle="tab" onclick="changeTab(13,'1','71')">待评价</a></li>
                        <li role="presentation" id="flag14"><a id="complaintNum" href="#home_1" aria-controls="home_1" role="tab" data-toggle="tab" onclick="changeTab(14,'1','81')">投诉中</a></li>
                        <li role="presentation" id="flag15"><a id="completeNum" href="#home_1" aria-controls="home_1" role="tab" data-toggle="tab" onclick="changeTab(15,'1','100')">已完成</a></li>
                        <li role="presentation" id="flag16"><a id="invalidNum" href="#home_1" aria-controls="home_1" role="tab" data-toggle="tab" onclick="changeTab(16,'10','')">已废弃</a></li>
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
<form class="form-horizontal margin-top-ele lay-form-time" action="${ctx}/excel/event" method="post" id="excelExport" style="display: none"></form>
<script type="text/javascript">
    var pageSize=10;
    var flag="${flag}";
    var searchInfo="${searchInfo}";
    if(flag){
        $("#flag"+flag).addClass("active").siblings("li").removeClass("active");
    }
    if(searchInfo){
        searchInfo = $("#searchInfo").val(searchInfo);
    }
    eventListData(1);

    //加载订单统计
    function eventCount(searchInfo) {
        $.ajax({
        type : "post",
        dataType : "json",
        headers: {rqSide: ex.pc()},
        data : JSON.stringify({"searchInfo":searchInfo}),
        contentType : "application/json;charset=utf-8",
        url: "${ctx}/event/count",
        success : function(datas){
            if(datas.code == "0"){
                var result = datas.data;
                $("#totalNum").html("全部订单("+result.totalNum+")");
                $("#applyNum").html("待审核("+result.applyNum+")");
                $("#notPassNum").html("未通过("+result.notPassNum+")");
                $("#passNum").html("已通过("+result.passNum+")");
                $("#distributionNum").html("待分单("+result.distributionNum+")");
                $("#receiveNum").html("待接单("+result.receiveNum+")");
                $("#failReceiveNum").html("放弃接单("+result.failReceiveNum+")");
                $("#waitAssertNum").html("待定损("+result.waitAssertNum+")");
                $("#assertNum").html("定损待确认("+result.assertNum+")");
                $("#waitReceiveNum").html("待接车("+result.waitReceiveNum+")");
                $("#waitRepairNum").html("待维修("+result.waitRepairNum+")");
                $("#repairNum").html("维修中("+result.repairNum+")");
                $("#receiveCarNum").html("待交车("+result.receiveCarNum+")");
                $("#commentNum").html("待评价("+result.commentNum+")");
                $("#complaintNum").html("投诉中("+result.complaintNum+")");
                $("#completeNum").html("已完成("+result.completeNum+")");
                $("#invalidNum").html("已废弃("+result.invalidNum+")");
            }
        }
    });
    }

    //加载页面数据
    function eventListData(pageNo){
        var isInvalid="${isInvalid}";
        var status="${status}";
        var searchInfo = $("#searchInfo").val();
        eventCount(searchInfo);
        var strJson = {"pageNo":pageNo,"pageSize":pageSize,"searchInfo":searchInfo,"isInvalid":isInvalid,"statusEvent":status};
        $.ajax({
            type : "post",
            dataType : "json",
            headers: {rqSide: ex.pc()},
            data : JSON.stringify(strJson),
            contentType : "application/json;charset=utf-8",
            url: "${ctx}/event/list",
            success : function(datas){
                if(datas.code == "0"){
                    var repData = datas.data.list;
                    var dataHtml = "<thead><tr><th>车主姓名</th><th>手机号</th><th>车牌号</th><th>车型品牌</th><th>加入时间</th><th>互助金余额</th><th>理赔余额</th><th>理赔次数</th><th>渠道来源</th><th>维修厂</th><th>状态</th><th>时间</th><th>操作</th></tr></thead>";
                    dataHtml += "<tbody>";

                    if (0 == repData.length) {
                        dataHtml += "</tbody>";
                        $("#mainDataTable").html(dataHtml);
                        $("#blankMessage").text("暂无订单信息");
                    } else {
                        for (var i = 0; i < repData.length; i++) {
                            var repDateChild = repData[i];
                            var statusEvent = ex.statusEvent(repDateChild.statusEvent);
                            var sourceName = ex.judgeEmpty(repDateChild.sourceName) ? repDateChild.sourceName : "自然用户";
                            dataHtml += "<tr>" +
                                "<td>" + ex.judgeEmpty(repDateChild.nameCarOwner) + "</td>" +
                                "<td>" + ex.judgeEmpty(repDateChild.customerPN) + "</td>" +
                                "<td>" + ex.judgeEmpty(repDateChild.licensePlateNumber) + "</td>" +
                                "<td>" + ex.judgeEmpty(repDateChild.model) + "</td>" +
                                "<td>" + (repDateChild.timeBegin ? ex.calcDays(repDateChild.timeBegin.time) : 0) + "天</td>" +
                                "<td>" + ex.judgeEmptyOr0(repDateChild.carAmtCooperation) + "</td>" +
                                "<td>" + ex.judgeEmptyOr0(repDateChild.amtCompensation) + "</td>" +
                                "<td>" + ex.judgeEmptyOr0(repDateChild.compensateNum) + "</td>" +
                                "<td>" + sourceName + "</td>" +
                                "<td>" + ex.judgeEmpty(repDateChild.shopName) + "</td>" +
                                "<td>" + statusEvent + "</td>" +
                                "<td>" + ex.calcDays(repDateChild.createAt.time) + "天</td>" +
                                "<td><button type='button' class='btn btn-info'onclick=showDetail(" + repDateChild.statusEvent + ",'" + ex.judgeEmpty(repDateChild.eventNo) + "')>查看</button></td>" +
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
                                eventListData(n,status);
                                window.location.href="#top";
                            }
                        });
                        kkpager.generPageHtml();
                    }
                }
            }
        });
    }

    function changeTab(flag,isInvalid,status) {
        var searchInfo = $("#searchInfo").val();
        self.location.href = "${ctx}/event/list.html?flag=" + flag + "&isInvalid=" + isInvalid + "&status=" + status + "&searchInfo=" + searchInfo;
    }

    //查看互助事件详情
    function showDetail(statusEvent,eventNo) {
        self.location.href = "${ctx}/event/detail.html?statusEvent=" + statusEvent + "&eventNo=" + eventNo;
    }

    function exportData() {
        var isInvalid="${isInvalid}";
        var status="${status}";
        var searchInfo = $("#searchInfo").val();
        $("#excelExport").empty().append("<input name='statusEvent' value="+ status +">");
        $("#excelExport").append("<input name='searchInfo' value="+ searchInfo +">");
        $("#excelExport").append("<input name='isInvalid' value="+ isInvalid +">");
        $("#excelExport").submit();
    }
</script>
</body>
</html>