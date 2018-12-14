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
                <li><i class="fa fa-dashboard"></i>管理</li>
                <li>操作日志</li>
            </ul>
        </div>
        <div class="row margin-row-max"></div>
        <div class="row">
            <div class="col-lg-12 col-xs-12">
                <div>
                    <ul class="nav nav-tabs nav-bac" role="tablist">
                        <li role="presentation" class="active"><a href="#home_1" aria-controls="home_1" role="tab" data-toggle="tab" onclick="foundationDetailData(1)">操作日志</a></li>
                        <li role="presentation" id="flag1"><a href="#home_1" aria-controls="home_1" role="tab" data-toggle="tab" onclick="logListData(1)">系统日志</a></li>
                    </ul>
                    <!-- Tab panes -->
                    <div class="tab-content index-tab-content">
                        <div role="tabpanel" class="tab-pane active white-table" id="home_1">
                            <table class="table table-bordered table-hover clear-border" id="mainDataTable"></table>
                            <%-- 数据分页(会显示所有数据所分页数) --%>
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
<script type="text/javascript">
    var pageSize=10;

    foundationDetailData(1);

    //加载页面数据
    function foundationDetailData(pageNo){
        var strJson = {"pageNo":pageNo,"pageSize":pageSize};
        $.ajax({
            type : "post",
            dataType : "json",
            headers: {rqSide: ex.pc()},
            data : JSON.stringify(strJson),
            contentType : "application/json;charset=utf-8",
            url: "${ctx}/foundation/detailList",
            success : function(datas){
                if(datas.code == "0"){
                    var repData = datas.data.list;
                    var dataHtml = "<thead><tr><th>类目</th><th>增加充值总额（元）</th><th>操作人</th><th>时间</th></tr></thead>";
                    dataHtml += "<tbody>";
                    for(var i=0;i < repData.length;i++){
                        var repDateChild = repData[i];
                        var createAt = ex.judgeEmpty(repDateChild.createAt)?ex.reTime("s",repDateChild.createAt.time):"";
                        dataHtml += "<tr>" +
                            "<td>" + ex.judgeEmpty(repDateChild.category) + "</td>" +
                            "<td>" + ex.judgeEmptyOr0(repDateChild.amt) + "</td>" +
                            "<td>" + ex.judgeEmpty(repDateChild.practitioner) + "</td>" +
                            "<td>" + createAt + "</td>" +
                            "</tr>";
                    }
                    dataHtml += "</tbody>";
                    $("#mainDataTable").html(dataHtml);
                    if(datas.data.total > -1){
                        kkpager.init({
                            pagerid : "kkpager",
                            pno : datas.data.pageNum,//当前页
                            total : datas.data.pages,//总页码
                            totalRecords : datas.data.total,//总数据条数
                            mode : 'click',//默认值是link，可选link或者click
                            click : function(n){
                            	foundationDetailData(n);
                                window.location.href="#top";
                            }
                        });
                        kkpager.generPageHtml();
                    }
                }
            }
        });
    }

    //加载页面数据
    function logListData(pageNo){
        var strJson = {"pageNo":pageNo,"pageSize":pageSize};
        $.ajax({
            type : "post",
            dataType : "json",
            headers: {rqSide: ex.pc()},
            data : JSON.stringify(strJson),
            contentType : "application/json;charset=utf-8",
            url: "${ctx}/log/list",
            success : function(datas){
                if(datas.code == "0"){
                    var repData = datas.data.list;
                    var dataHtml = "<thead><tr><th>操作人</th><th>操作内容</th><th>操作时间</th><th>IP地址</th></tr></thead>";
                    dataHtml += "<tbody>";
                    for(var i=0;i < repData.length;i++){
                        var repDateChild = repData[i];
                        var createAt = ex.judgeEmpty(repDateChild.createAt)?ex.reTime("s",repDateChild.createAt.time):"";
                        dataHtml += "<tr>" +
                            "<td>" + ex.judgeEmpty(repDateChild.practitioner) + "</td>" +
                            "<td>" + ex.judgeEmpty(repDateChild.perform) + "</td>" +
                            "<td>" + createAt + "</td>" +
                            "<td>" + ex.judgeEmpty(repDateChild.ip) + "</td>" +
                            "</tr>";
                    }
                    dataHtml += "</tbody>";
                    $("#mainDataTable").html(dataHtml);
                    if(datas.data.total > -1){
                        kkpager.init({
                            pagerid : "kkpager",
                            pno : datas.data.pageNum,//当前页
                            total : datas.data.pages,//总页码
                            totalRecords : datas.data.total,//总数据条数
                            mode : 'click',//默认值是link，可选link或者click
                            click : function(n){
                                logListData(n);
                                window.location.href="#top";
                            }
                        });
                        kkpager.generPageHtml();
                    }
                }
            }
        });
    }
</script>
</body>
</html>