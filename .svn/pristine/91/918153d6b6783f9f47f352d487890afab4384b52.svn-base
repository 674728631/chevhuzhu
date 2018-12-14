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
                <li>理赔代办管理</li>
            </ul>
        </div>
        <div class="index-wrap w5 row">
            <ul class="col-lg-12 col-sm-12">
                <li class='go-quote'><p>订单数</p><span id="totalOrder"></span></li>
                <li class='go-quote'><p>成交率</p><span id="orderRadio"></span></li>
                <li ><p>交易金额</p><span id="totalOrderAmt"></span></li>
                <li ><p>结算金额</p><span id="totalBusinessAmt"></span></li>
                <li ><p>利润</p><span id="profit"></span></li>
            </ul>
        </div>
        <div class="row">
            <div class="col-lg-12 col-xs-12">
                <div>
                    <button type="button" class="btn def-btn btn-info" onclick="exportData()">导出</button>
                    <!-- Tab panes -->
                    <div class="tab-content index-tab-content">
                        <div role="tabpanel" class="tab-pane active white-table" id="home_1">
                            <table class="table table-bordered table-hover" id="resultTable"></table>
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

<!--结算弹窗-->
<div id="settle-layer" class="ad-layer">
    <ul>
        <li>
            <span>结算价</span>
            <input type="text" class="ad-input in_1" maxlength="20" onkeyup="value=value.replace(/\s/g,'')">
        </li>
    </ul>
</div>
<!--导出弹窗-->
<div class="time-layer" style="display: none;">
    <form class="form-horizontal margin-top-ele lay-form-time" action="${ctx}/excel/orderStatistical" method="post" id="excelExport" style="padding-top:4px;margin-top:4px;">

    </form>
</div>
<script type="text/javascript">
    var pageSize=10;

    foundationData();
    //加载互助总额数据
    function foundationData(){
        $.ajax({
            type : "post",
            dataType : "json",
            headers: {rqSide: ex.pc()},
            contentType : "application/json;charset=utf-8",
            url: "${ctx}/orderStatistical/sumData",
            success : function(datas){
                if(datas.code == "0"){
                    var repData = datas.data;
                    $("#totalOrder").html(ex.judgeEmptyOr0(repData.totalOrder));
                    $("#orderRadio").html(ex.judgeEmptyOr0(repData.orderRadio)+"%");
                    $("#totalOrderAmt").html(ex.judgeEmptyOr0(repData.totalOrderAmt));
                    $("#totalBusinessAmt").html(ex.judgeEmptyOr0(repData.totalBusinessAmt));
                    $("#profit").html(ex.judgeEmptyOr0(repData.profit));
                }
            }
        });
    }

    record(1)
    //加载保险理赔收支明细数据
    function record(pageNo){
        var strJson = {"pageNo":pageNo,"pageSize":pageSize};
        $.ajax({
            type : "post",
            dataType : "json",
            headers: {rqSide: ex.pc()},
            data : JSON.stringify(strJson),
            contentType : "application/json;charset=utf-8",
            url: "${ctx}/orderStatistical/orderList",
            success : function(datas){
                if(datas.code == "0"){
                    var repData = datas.data.list;
                    var dataHtml = "<thead><tr><th>账户</th><th>车牌</th><th>渠道</th><th>交易金额（元）</th><th>维修厂结算（元）</th><th>渠道结算（元）</th><th>车主交易时间</th><th>渠道结算操作</th></tr></thead>";
                    dataHtml += "<tbody>";
                    for(var i=0;i < repData.length;i++){
                        var repDateChild = repData[i];
                        var tradeTime = ex.judgeEmpty(repDateChild.tradeTime)?ex.reTime("s",repDateChild.tradeTime.time):"";
                        dataHtml += "<tr>" +
                            "<td>" + ex.judgeEmpty(repDateChild.customerPN) + "</td>" +
                            "<td>" + ex.judgeEmpty(repDateChild.licensePlateNumber) + "</td>" +
                            "<td>" + ex.judgeEmpty(repDateChild.channelName) + "</td>" +
                            "<td>" + ex.judgeEmptyOr0(repDateChild.amtOrder) + "</td>" +
                            "<td>" + ex.judgeEmptyOr0(repDateChild.amtBusiness) + "</td>" +
                            "<td>" + ex.judgeEmptyOr0(repDateChild.amtChannel) + "</td>" +
                            "<td>" + tradeTime + "</td>" +
                            "<td><button type='button' class='btn btn-info'onclick=settle(" + repDateChild.id +")>结算</button></td>" +
                            "</tr>";
                    }
                    dataHtml += "</tbody>";
                    $("#resultTable").html(dataHtml);
                    if(datas.data.total > -1){
                        kkpager.init({
                            pagerid : "kkpager",
                            pno : datas.data.pageNum,//当前页
                            total : datas.data.pages,//总页码
                            totalRecords : datas.data.total,//总数据条数
                            mode : 'click',//默认值是link，可选link或者click
                            click : function(n){
                                record(n);
                                window.location.href="#top";
                            }
                        });
                        kkpager.generPageHtml();
                    }
                }
            }
        });
    }

    //结算弹窗
    function settle(id){
        layer.open({
            title:"结算",
            type: 1,
            btn:['确认','取消'],
            btnAlign: 'r',
            area: ['300px','175px'],
            offset: 'auto',
            shadeClose: false, //点击遮罩关闭
            content: $('#settle-layer'),
            yes:function(index, layero){
                var amtChannel = $(".in_1").val();
                if (!amtChannel) {
                    alertBtn("请填写结算金额");
                    return;
                }
                var strJson = {"id":id,"amtChannel":amtChannel};
                $.ajax({
                    type : "post",
                    dataType : "json",
                    headers: {rqSide: ex.pc()},
                    data : JSON.stringify(strJson),
                    contentType : "application/json;charset=utf-8",
                    url: "${ctx}/orderStatistical/settle",
                    success : function(datas){
                        if(datas.code != "0"){
                            alert(datas.message)
                        }else{
                            location.reload();
                        }
                    }
                });
                layer.close(index);
            }
        });
    }

    function exportData() {
        $("#excelExport").submit();
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