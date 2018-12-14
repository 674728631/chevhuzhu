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
                <li>修理厂结算</li>
            </ul>
        </div>
        <div class="row margin-row-max">
            <div class="col-sm-3">
                <input id="searchInfo" type="text" class="form-control" aria-describedby="inputGroupSuccess1Status" onkeyup="value=value.replace(/\s/g,'')" placeholder="请输入维修厂名字">
            </div>
            <div class="col-sm-9">
                <button type="button" class="btn def-btn btn-success pull-left" onclick="customerRecord(1)" style="margin-left:12px;">搜索</button>
            </div>
        </div>
        <div class="row">
            <div class="col-lg-12 col-xs-12">
                <div style="margin-top:10px;">
                    <ul class="nav nav-tabs nav-bac" role="tablist">
                        <li role="presentation" class="active"><a href="#home_1" aria-controls="home_1" role="tab" data-toggle="tab" onclick="customerRecord(1,0)" >修理厂结算</a></li>
                    </ul>
					<!-- Tab panes -->
                    <div class="tab-content index-tab-content">
                        <div role="tabpanel" class="tab-pane active white-table" id="home_1">
                            <table class="table table-bordered table-hover" id="recordDataTable"></table>
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
    var searchInfo="${searchInfo}";
    customerRecord(1);
    //加载用户充值数据
    function customerRecord(pageNo){
    	var searchInfo = $("#searchInfo").val();
        var strJson = {"pageNo":pageNo,"pageSize":pageSize,"searchInfo":searchInfo};
        $.ajax({
            type : "post",
            dataType : "json",
            headers: {rqSide: ex.pc()},
            data : JSON.stringify(strJson),
            contentType : "application/json;charset=utf-8",
            url: "${ctx}/foundation/maintenanceshopBill",
            success : function(datas){
                if(datas.code == "200"){
                    var repData = datas.data.list;
                    var dataHtml = "<thead><tr><th>排名</th><th>昵称/名称</th><th>待结算金额</th><th>可用金额</th><th>总金额</th><th>已提金额</th></tr></thead>";
                    dataHtml += "<tbody>";
                    for(var i=0;i < repData.length;i++){
                        var repDateChild = repData[i];
                        dataHtml += "<tr>" +
                       		"<td>" + (i+1) + "</td>" +
                            "<td>" + ex.judgeEmpty(repDateChild.shopName) + "</td>" +
                            "<td>" + ex.judgeEmptyOr0(repDateChild.amtFreeze) + " 元</td>" +
                            "<td>" + ex.judgeEmptyOr0(repDateChild.amtUnfreeze) + "  元</td>" +
                            "<td>" + ex.judgeEmptyOr0(repDateChild.amtTotal) + "  元</td>" +
                            "<td>" + ex.judgeEmptyOr0(repDateChild.amtPaid) + "  元</td>" +
                            "</tr>";
                    }
                    dataHtml += "</tbody>";
                    $("#recordDataTable").html(dataHtml);
                    if(datas.data.total > -1){
                        kkpager.init({
                            pagerid : "kkpager",
                            pno : datas.data.pageNum,//当前页
                            total : datas.data.pages,//总页码
                            totalRecords : datas.data.total,//总数据条数
                            mode : 'click',//默认值是link，可选link或者click
                            click : function(n){
                            	customerRecord(n);
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