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
                <li>一元洗车</li>
            </ul>
        </div>
        <div class="row">
            <div class="col-lg-12 col-xs-12">
                <div style="margin-top: 104px;">
                	<p class="pull-left" style="line-height:34px;">起止时间</p>
                	<input name="beginTime" id="beginTime" value="" class="form-control pull-left" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd',maxDate:'#F{$dp.$D(\'endTime\')||\'%y-%M-%d\'}'});" placeholder="选择开始时间" style="width:20%;margin-left:6px;">
                	<p class="pull-left" style="line-height:34px;margin-left:6px;">-</p>
                	<input name="endTime" id="endTime" value="" class="form-control pull-left" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd',minDate:'#F{$dp.$D(\'beginTime\')||\'%y-%M-%d\'}'});" placeholder="选择结束时间" style="width:20%;margin-left:6px;">
               		<button type="button" class="btn def-btn btn-success pull-left" onclick="search()" style="margin-left:12px;">搜索</button>
               		<button type="button" class="btn def-btn btn-success pull-left" onclick="reset()" style="margin-left:15px;">清空</button>
                    <button type="button" class="btn def-btn btn-info" onclick="exportData()" style="margin-left: 15px;">导出</button>

                    <div class="index-wrap row">
                        <ul  style="margin: 2px;">
                            <li><p>总笔数</p><span id="rechargeNum"></span></li>
                            <li><p>总金额</p><span id="rechargeAmt"></span></li>
                        </ul>
                    </div>

                    <ul class="nav nav-tabs nav-bac" role="tablist">
                        <li role="presentation" class="active"><a href="#home_1" aria-controls="home_1" role="tab" data-toggle="tab" onclick="customerRecord(1,0)" >全部</a></li>
                        <li role="presentation"><a href="#home_2" aria-controls="home_2" role="tab" data-toggle="tab" onclick="customerRecord(1,1)">一元明细</a></li>
                        <li role="presentation"><a href="#home_3" aria-controls="home_3" role="tab" data-toggle="tab" onclick="customerRecord(1,2)">二元明细</a></li>
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

<!--导出弹窗-->
<div class="time-layer" style="display: none;">
    <form class="form-horizontal margin-top-ele lay-form-time" action="${ctx}/activities/exportExcel" method="post" id="excelExport" style="padding-top:4px;margin-top:4px;"></form>
</div>
<script type="text/javascript" src="${ctx}/cite/plugins/MyDatePicker/WdatePicker.js"></script>
<script type="text/javascript">

    $.get("${ctx}/activities/statCarWashTotal", function(datas) {
        // console.log(datas);
        if (datas.code == "200") {
            var repData = datas.data;
            $("#rechargeNum").html(ex.judgeEmptyOr0(repData.count));
            $("#rechargeAmt").html(ex.judgeEmptyOr0(repData.totalFee)+" 元");
        }
    });


   //查询
    function search() {
        customerRecord(1,0);
    }
   //清空查询条件   
    function reset() {
        var beginTime = $("#beginTime").val("");
        var endTime = $("#endTime").val("");
        // customerRecord(1);
    }

    customerRecord(1,0);
    //加载用户充值数据
    function customerRecord(pageNo,type){
    	var beginTime = $("#beginTime").val();
    	var endTime = $("#endTime").val();
    	if(beginTime && endTime){
           endTime = ex.getDateString(ex.plusDateOneDay(new Date(endTime)));
        }
        var strJson = {"currentPage":pageNo,"beginTime":beginTime,"endTime":endTime,"type":type};
        $.ajax({
            type : "post",
            dataType : "json",
            headers: {rqSide: ex.pc()},
            data : JSON.stringify(strJson),
            contentType : "application/json;charset=utf-8",
            url: "${ctx}/activities/statCarWashActivity",
            success : function(datas){
                if(datas.code == "200"){
                    var repData = datas.data.pageList;
                    var dataHtml = "<thead><tr><th>用户手机</th><th>购买数量</th><th>支付金额</th><th>支付时间</th><th>微信订单号</th><th>未使用数量</th><th>已使用数量</th></tr></thead>";
                    dataHtml += "<tbody>";
                    for(var i=0;i < repData.length;i++){
                        var repDateChild = repData[i];
                        var timeEnd = ex.judgeEmpty(repDateChild.timeEnd)?ex.reTime("s",repDateChild.timeEnd.time):"";
                        dataHtml += "<tr>" +
                            "<td>" + ex.judgeEmpty(repDateChild.customerPN) + "</td>" +
                            "<td>" + ex.judgeEmpty(repDateChild.buyNum) + "</td>" +
                            "<td>" + ex.judgeEmpty(repDateChild.totalFee) + "  元</td>" +
                            "<td>" + timeEnd + "</td>" +
                            "<td>" + ex.judgeEmpty(repDateChild.transactionId) + "</td>" +
                            "<td>" + repDateChild.notUseNum + "</td>" +
                            "<td>" + repDateChild.useNum + "</td>" +
                            "</tr>";
                    }
                    dataHtml += "</tbody>";
                    $("#recordDataTable").html(dataHtml);
                    if(datas.data.allRow > -1){
                        kkpager.init({
                            pagerid : "kkpager",
                            pno : datas.data.currentPage,//当前页
                            total : datas.data.totalPage,//总页码
                            totalRecords : datas.data.allRow,//总数据条数
                            mode : 'click',//默认值是link，可选link或者click
                            click : function(n){
                                customerRecord(n,type);
                                window.location.href="#top";
                            }
                        });
                        kkpager.generPageHtml();
                    }
                }
            }
        });
    }
    // 导出Excel
    function exportData() {
          var beginTime = $("#beginTime").val();
          var endTime = $("#endTime").val();
          if(beginTime && endTime){
              endTime = ex.getDateString(ex.plusDateOneDay(new Date(endTime)));
          }
          $("#excelExport").empty().append("<input name='beginTime' value="+ beginTime +">");
          $("#excelExport").append("<input name='endTime' value="+ endTime +">");
          $("#excelExport").submit();
    }
</script>
</body>
</html>