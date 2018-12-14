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
                <li><a href="${ctx}/main.html"><i class="fa fa-dashboard"></i> 首页</a></li>
                <li>成员</li>
            </ul>
        </div>
        <div class="row margin-row-max">
            <div class="col-sm-8 new-search-btn">
                <input style="width: 40%" id="searchInfo" type="text" class="form-control" aria-describedby="inputGroupSuccess1Status" onkeyup="value=value.replace(/\s/g,'')" placeholder="请输入手机号、车牌号搜索信息">
                <button style="width: 10%;margin-right: 20px" type="button" class="btn def-btn btn-success" onclick="search()">搜索</button>
                <%--<button style="width: 10%" type="button" class="btn def-btn btn-info" onclick="exportData()">导出</button>--%>
                <div class="" style="position:relative;float:left;display: inline-block;margin-left:30px;">
                    <button id="importDataBtn" type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false" value="-1">导出 <span class="caret"></span></button>
                    <ul class="dropdown-menu dropdown-menu-right">
                        <li><a href="#" onclick="improtExcelData(-1)">全部</a></li>
                        <li><a href="#" onclick="improtExcelData(13)">观察期</a></li>
                        <li><a href="#" onclick="improtExcelData(20)">保障中</a></li>
                    </ul>
                </div>
            </div>
            <div class="time-layer" style="display: none;">
    <form class="form-horizontal margin-top-ele lay-form-time" action="${ctx}/excel/doExcelInvitation2" method="post" id="excelExport" style="padding-top:4px;margin-top:4px;">
	<input type="hidden" name="customerPN" id="customerPN" />
	<input type="hidden" name="customerId" id="customerId" />
    </form>
</div>
        </div>
        <div class="row" style="padding-left: 15px;padding-bottom: 5px">
            <div id="timeShowDiv">时间：<span id="timeShowSpan">全部</span></div>
        </div>
        <div class="row">
            <div class="col-lg-12 col-xs-12">
                <div>
                    <ul class="nav nav-tabs nav-bac" role="tablist">
                        <li role="presentation" class="active"><a id="totalNum" href="#home_1" aria-controls="home_1" role="tab" data-toggle="tab" onclick="setTotalData(1)">全部(0)</a></li>
                        <li role="presentation" class="flag1"><a id="guanchaNum" href="#home_1" aria-controls="home_1" role="tab" data-toggle="tab" onclick="setGuanchaData(1,1)">观察期(0)</a></li>
                        <li role="presentation" class="flag1"><a id="baozhangNum" href="#home_1" aria-controls="home_1" role="tab" data-toggle="tab" onclick="setBaozhangData(1,1)">保障中(0)</a></li>
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
            <li style="float:left;">用户来源：<span id="attentionShop">自然用户</span></li>
        </div>
        <li style="margin-top:6px;"><span id="totalCarNum">添加的车辆（0）：</span><span id="totalCar">暂无车辆</span></li>
        <li style="margin-top:6px;"><span id="guaranteeCarNum">保障中车辆（0）：</span><span id="guaranteeCar">暂无车辆</span></li>
        <li style="margin:6px 0;">用户注册时间：<span id="timeJoin"></span></li>
    </div>
</div>
<script type="text/javascript">
    var pageSize=10;
	$("#guanchaNum").html("观察期(${guanchaCount})");
    $("#baozhangNum").html("保障中(${baozhangCount})");

    // 选择的tab标签，默认是用户(1:全部，2：观察期，3：保障中)
    var selectTabType = 1;
    // 存储页面传过来的值
    var customerId = '${customerId}';
    var tabType = '${tabType}';
    var beginTime = '${beginTime}';
    var endTime = '${endTime}';

    if (beginTime != '' && endTime != ''){
        $("#timeShowDiv").html("时间：" + beginTime + "至" + endTime);
    } else {
        $("#timeShowDiv").html("截止时间：" + getDateStr3(new Date()));
    }

    setTotalData(1);
    //加载全部的数据
    function setTotalData(pageNo,status){
        selectTabType = 1;
        var searchInfo = $("#searchInfo").val();
        var strJson = {"pageNo":pageNo,"pageSize":pageSize,"customerPN":searchInfo,"status":status,"tabType":tabType,"beginTime":beginTime,"endTime":endTime};
        $.ajax({
            type : "post",
            dataType : "json",
            headers: {rqSide: ex.pc()},
            data : JSON.stringify(strJson),
            contentType : "application/json;charset=utf-8",
            url: "${ctx}/invitation/detailList/"+customerId,
            success : function(datas){
                if(datas.code == "0"){
                    $("#totalNum").html("全部(" + datas.data.total + ")");
                    var repData = datas.data.list;
                    var dataHtml = "<thead><tr><th>头像</th><th>昵称</th><th>手机号</th><th>车牌号</th><th>车辆状态</th><th>充值金额</th><th>余额</th></tr></thead>";
                    dataHtml += "<tbody>";
                    for(var i=0;i < repData.length;i++){
                        var repDateChild = repData[i];
                        var carStatus = ex.carStatus(repDateChild.status);
                        dataHtml += "<tr>";
                        if(ex.judgeEmpty(repDateChild.portrait)){
                            dataHtml += "<td><img src='"+ repDateChild.portrait +"' style='width:32px;height:32px;border-radius:50%;' alt=''></td>";
                        }else{
                            dataHtml += "<td><img src='${ctx}/cite/images/no_pic.png' style='width:32px;height:32px;border-radius:50%;' alt=''></td>";
                        }
                        dataHtml += "<td>" + ex.judgeEmpty(repDateChild.nickname) + "</td>" +
                            "<td>" + ex.judgeEmpty(repDateChild.customerPN) + "</td>" +
                            "<td>" + ex.judgeEmptyOr0(repDateChild.licensePlateNumber) + "</td>" +
                            "<td>" + carStatus + "</td>" +
                            "<td>" + ex.judgeEmptyOr0(repDateChild.rechargeAmount) + "</td>" +
                            "<td>" + ex.judgeEmptyOr0(repDateChild.amtCooperation) + "</td>" +
                           /*  "<td>" +
                            "<button type='button' class='btn btn-info' onclick='showDetail(" + repDateChild.invitedCustomerId + ")'>查看</button>&nbsp;" +
                            "</td>" + */
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
                                setTotalData(n,status);
                                window.location.href="#top";
                            }
                        });
                        kkpager.generPageHtml();
                    }
                }
            }
        });
    }

    //加载观察期的数据
    function setGuanchaData(pageNo,status){
        selectTabType = 2;
        var searchInfo = $("#searchInfo").val();
        var strJson = {"pageNo":pageNo,"pageSize":pageSize,"customerPN":searchInfo,"status":"13","tabType":tabType,"beginTime":beginTime,"endTime":endTime};
        $.ajax({
            type : "post",
            dataType : "json",
            headers: {rqSide: ex.pc()},
            data : JSON.stringify(strJson),
            contentType : "application/json;charset=utf-8",
            url: "${ctx}/invitation/detailList/"+customerId,
            success : function(datas){
                if(datas.code == "0"){
                    $("#guanchaNum").html("观察期(" + datas.data.total + ")");
                    var repData = datas.data.list;
                    var dataHtml = "<thead><tr><th>头像</th><th>昵称</th><th>手机号</th><th>车牌号</th><th>车辆状态</th><th>充值金额</th><th>余额</th></tr></thead>";
                    dataHtml += "<tbody>";
                    for(var i=0;i < repData.length;i++){
                        var repDateChild = repData[i];
                        var carStatus = ex.carStatus(repDateChild.status);
                        dataHtml += "<tr>";
                        if(ex.judgeEmpty(repDateChild.portrait)){
                            dataHtml += "<td><img src='"+ repDateChild.portrait +"' style='width:32px;height:32px;border-radius:50%;' alt=''></td>";
                        }else{
                            dataHtml += "<td><img src='${ctx}/cite/images/no_pic.png' style='width:32px;height:32px;border-radius:50%;' alt=''></td>";
                        }
                        dataHtml += "<td>" + ex.judgeEmpty(repDateChild.nickname) + "</td>" +
                            "<td>" + ex.judgeEmpty(repDateChild.customerPN) + "</td>" +
                            "<td>" + ex.judgeEmptyOr0(repDateChild.licensePlateNumber) + "</td>" +
                            "<td>" + carStatus + "</td>" +
                            "<td>" + ex.judgeEmptyOr0(repDateChild.rechargeAmount) + "</td>" +
                            "<td>" + ex.judgeEmptyOr0(repDateChild.amtCooperation) + "</td>" +
                            /*  "<td>" +
                             "<button type='button' class='btn btn-info' onclick='showDetail(" + repDateChild.invitedCustomerId + ")'>查看</button>&nbsp;" +
                             "</td>" + */
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
                                setGuanchaData(n,status);
                                window.location.href="#top";
                            }
                        });
                        kkpager.generPageHtml();
                    }
                }
            }
        });
    }

    //加载保障中的数据
    function setBaozhangData(pageNo,status){
        selectTabType = 3;
        var searchInfo = $("#searchInfo").val();
        var strJson = {"pageNo":pageNo,"pageSize":pageSize,"customerPN":searchInfo,"status":"20","tabType":tabType,"beginTime":beginTime,"endTime":endTime};
        $.ajax({
            type : "post",
            dataType : "json",
            headers: {rqSide: ex.pc()},
            data : JSON.stringify(strJson),
            contentType : "application/json;charset=utf-8",
            url: "${ctx}/invitation/detailList/"+customerId,
            success : function(datas){
                if(datas.code == "0"){
                    $("#baozhangNum").html("保障中(" + datas.data.total + ")");
                    var repData = datas.data.list;
                    var dataHtml = "<thead><tr><th>头像</th><th>昵称</th><th>手机号</th><th>车牌号</th><th>车辆状态</th><th>充值金额</th><th>余额</th></tr></thead>";
                    dataHtml += "<tbody>";
                    for(var i=0;i < repData.length;i++){
                        var repDateChild = repData[i];
                        var carStatus = ex.carStatus(repDateChild.status);
                        dataHtml += "<tr>";
                        if(ex.judgeEmpty(repDateChild.portrait)){
                            dataHtml += "<td><img src='"+ repDateChild.portrait +"' style='width:32px;height:32px;border-radius:50%;' alt=''></td>";
                        }else{
                            dataHtml += "<td><img src='${ctx}/cite/images/no_pic.png' style='width:32px;height:32px;border-radius:50%;' alt=''></td>";
                        }
                        dataHtml += "<td>" + ex.judgeEmpty(repDateChild.nickname) + "</td>" +
                            "<td>" + ex.judgeEmpty(repDateChild.customerPN) + "</td>" +
                            "<td>" + ex.judgeEmptyOr0(repDateChild.licensePlateNumber) + "</td>" +
                            "<td>" + carStatus + "</td>" +
                            "<td>" + ex.judgeEmptyOr0(repDateChild.rechargeAmount) + "</td>" +
                            "<td>" + ex.judgeEmptyOr0(repDateChild.amtCooperation) + "</td>" +
                            /*  "<td>" +
                             "<button type='button' class='btn btn-info' onclick='showDetail(" + repDateChild.invitedCustomerId + ")'>查看</button>&nbsp;" +
                             "</td>" + */
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
                                setBaozhangData(n,status);
                                window.location.href="#top";
                            }
                        });
                        kkpager.generPageHtml();
                    }
                }
            }
        });
    }

    // 下载文件
    function improtExcelData(status) {
        var url = "${ctx}/invitation/importExcelForPullNewDetail";
        var form = $("<form></form>").attr("action", url).attr("method", "get");
        form.append($("<input></input>").attr("type", "hidden").attr("name", "pageNo").attr("value", 1));
        form.append($("<input></input>").attr("type", "hidden").attr("name", "pageSize").attr("value", 10000));
        form.append($("<input></input>").attr("type", "hidden").attr("name", "customerPN").attr("value", $("#searchInfo").val()));
        if (status != -1){
            form.append($("<input></input>").attr("type", "hidden").attr("name", "status").attr("value", status));
        }
        form.append($("<input></input>").attr("type", "hidden").attr("name", "tabType").attr("value", tabType));
        form.append($("<input></input>").attr("type", "hidden").attr("name", "customerId").attr("value", customerId));
        form.append($("<input></input>").attr("type", "hidden").attr("name", "beginTime").attr("value", beginTime));
        form.append($("<input></input>").attr("type", "hidden").attr("name", "endTime").attr("value", endTime));
        form.appendTo('body').submit().remove();
    }

    //用户详情弹窗
    function showDetail(customerId){
            self.location.href = "${ctx}/invitation/detailList.html/" + customerId;
    }

    function search() {
        if (selectTabType === 1){
            setTotalData(1);
        }else if (selectTabType === 2){
            setGuanchaData(1);
        }else if (selectTabType === 3){
            setBaozhangData(1);
        }
    }

    //消息提示 弹窗
    function alertBtn(info){
        layer.alert(info, function(index){
            layer.close(index);
        });
    }
    function exportData() {
    	$("#customerPN").val($("#searchInfo").val());
    	$("#customerId").val(customerId);
        $("#excelExport").submit();
    }

    //获取当前日期yy-mm-dd
    //date 为时间对象
    function getDateStr3(date) {
        var year = "";
        var month = "";
        var day = "";
        var now = date;
        year = ""+now.getFullYear();
        if((now.getMonth()+1)<10){
            month = "0"+(now.getMonth()+1);
        }else{
            month = ""+(now.getMonth()+1);
        }
        if((now.getDate())<10){
            day = "0"+(now.getDate());
        }else{
            day = ""+(now.getDate());
        }
        return year+"-"+month+"-"+day;
    }
</script>
</body>
</html>