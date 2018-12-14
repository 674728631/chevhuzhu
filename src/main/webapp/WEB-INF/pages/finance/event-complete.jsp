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
                <li><i class="fa fa-dashboard"></i> 财务</li>
                <li>商家费用</li>
            </ul>
        </div>
       <div class="row  margin-top-ele">
            <div class="col-lg-12 col-xs-12">
                <div class="panel panel-info">
                    <div class="panel-heading">
                        <h3 class="panel-title">商家费用</h3>
                    </div>
                    <div class="col-sm-3 col-xs-3 input-search">
                        <div class="form-group has-success has-feedback">
                            <div class="input-group">
                                <span class="input-group-addon">商家名称</span>
                                <input id="shopName" type="text" class="form-control" aria-describedby="inputGroupSuccess1Status" onkeyup="value=value.replace(/\s/g,'')">
                            </div>
                        </div>
                    </div>
                    <div class="panel-de-button">
                        <button type="button" class="btn btn-success def-btn" onclick="eventListData(1)">搜索</button>
                    </div>
                    <div class="panel-body">
                        <table class="table table-bordered table-hover" id="mainDataTable"></table>
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

<script type="text/javascript">
    var pageSize=10;

    eventListData(1);

    //加载页面数据
    function eventListData(pageNo){
        var shopName = $("#shopName").val();
        var strJson = {"pageNo":pageNo,"pageSize":pageSize,"statusEvent":'71,100',"shopName":shopName};
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
                    var dataHtml = "<thead><tr><th>互助事件号</th><th>车主名字</th><th>车牌号</th><th>修理厂</th><th>车主支付金额</th><th>分摊总额</th><th>车主分摊金额</th><th>商家得到金额</th></tr></thead>";
                    dataHtml += "<tbody>";
                    for(var i=0;i < repData.length;i++){
                        var repDateChild = repData[i];
                        dataHtml += "<tr>" +
                            "<td>" + ex.judgeEmpty(repDateChild.eventNo) + "</td>" +
                            "<td>" + ex.judgeEmpty(repDateChild.nameCarOwner) + "</td>" +
                            "<td>" + ex.judgeEmpty(repDateChild.licensePlateNumber) + "</td>" +
                            "<td>" + ex.judgeEmpty(repDateChild.shopName) + "</td>" +
                            "<td>" + ex.judgeEmptyOr0(repDateChild.amtPay) + "</td>" +
                            "<td>" + ex.judgeEmptyOr0(repDateChild.amtCooperation) + "</td>" +
                            "<td>" + ex.judgeEmptyOr0(repDateChild.amtShare) + "</td>" +
                            "<td>" + ex.judgeEmptyOr0(repDateChild.amtBusiness) + "</td>" +
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
                                eventListData(n);
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