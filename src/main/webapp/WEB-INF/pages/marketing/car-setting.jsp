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
                <li><i class="fa fa-dashboard"></i> 营销</li>
                <li>车辆补贴</li>
            </ul>
        </div>
        <div class="row margin-row-max">
            <div class="col-sm-3">
                <input id="searchInfo" type="text" class="form-control" aria-describedby="inputGroupSuccess1Status" onkeyup="value=value.replace(/\s/g,'')" placeholder="请输入车主姓名、手机号、车牌号、渠道进行搜索订单">
            </div>
            <div class="col-sm-9">
                <p class="pull-left" style="line-height:34px;">起止时间</p>
                <input id="beginTime" value="" class="form-control pull-left" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd'});" placeholder="选择开始时间" style="width:20%;margin-left:6px;">
                <p class="pull-left" style="line-height:34px;margin-left:6px;">-</p>
                <input id="endTime" value="" class="form-control pull-left" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd'});" placeholder="选择结束时间" style="width:20%;margin-left:6px;">
                <button type="button" class="btn def-btn btn-success pull-left" onclick="carListData(1)" style="margin-left:12px;">搜索</button>
                <button type="button" class="btn def-btn btn-success pull-left" onclick="exportData()" style="margin-left:6px;">导出</button>
            </div>
        </div>
        <div class="row">
            <div class="col-lg-12 col-xs-12">
                <div>
                    <!-- Nav tabs -->
                    <ul class="nav nav-tabs nav-bac" role="tablist">
                        <li role="presentation" class="active"><a id="totalNum" href="#home_1" aria-controls="home_1" role="tab" data-toggle="tab" onclick="changeTab('','')" >全部</a></li>
                        <li role="presentation" id="flag1"><a id="initNum" href="#home_1" aria-controls="home_1" role="tab" data-toggle="tab" onclick="changeTab(1,'1')">待支付</a></li>
                        <li role="presentation" id="flag2"><a id="observationNum" href="#home_1" aria-controls="home_1" role="tab" data-toggle="tab" onclick="changeTab(2,'13')">观察期</a></li>
                        <li role="presentation" id="flag3"><a id="guaranteeNum" href="#home_1" aria-controls="home_1" role="tab" data-toggle="tab" onclick="changeTab(3,'20')">保障中</a></li>
                        <li role="presentation" id="flag4"><a id="outNum" href="#home_1" aria-controls="home_1" role="tab" data-toggle="tab" onclick="changeTab(4,'30')">退出计划</a></li>
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
<!--修改互助余额弹窗-->
<div class="update-lay-box" id="addLay">
    <form class="form-horizontal lay-form-update">
        <div class="form-group">
            <label class="col-sm-4 control-label">补贴金额：</label>
            <div class="col-sm-8">
                <input type="text" class="form-control in_1" placeholder="" maxlength="100" onkeyup="value=value.replace(/\s/g,'')">
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-4 control-label">保障类型：</label>
            <div class="col-sm-8">
                <input type="radio" class="in_2" placeholder="" name="ac" value="1">扣完即止
                <input type="radio" class="in_2" placeholder="" name="ac" value="2">保障一年
            </div>
        </div>
    </form>
</div>
<form class="form-horizontal margin-top-ele lay-form-time" action="${ctx}/excel/car" method="post" id="excelExport" style="display: none"></form>
<script type="text/javascript" src="${ctx}/cite/plugins/MyDatePicker/WdatePicker.js"></script>
<script type="text/javascript">
    var pageSize=10;
    var flag="${flag}";
    var searchInfo="${searchInfo}";
    var beginTime="${beginTime}";
    var endTime="${endTime}";
    if(flag){
        $("#flag"+flag).addClass("active").siblings("li").removeClass("active");
    }
    if(searchInfo){
        $("#searchInfo").val(searchInfo);
    }
    if(beginTime && endTime){
        $("#beginTime").val(beginTime);
        $("#endTime").val(endTime);
    }
    carListData(1);

    //加载车辆统计
    function carCount(searchInfo,beginTime,endTime) {
        $.ajax({
            type : "post",
            dataType : "json",
            headers: {rqSide: ex.pc()},
            data : JSON.stringify({"searchInfo":searchInfo,'beginTime':beginTime,'endTime':endTime}),
            contentType : "application/json;charset=utf-8",
            url: "${ctx}/car/count",
            success : function(datas){
                if(datas.code == "0"){
                    var result = datas.data;
                    $("#totalNum").html("全部("+result.totalNum+")");
                    $("#initNum").html("待支付("+result.initNum+")");
                    $("#observationNum").html("观察期("+result.observationNum+")");
                    $("#guaranteeNum").html("保障中("+result.guaranteeNum+")");
                    $("#outNum").html("退出计划("+result.outNum+")");
                }
            }
        });
    }

    //加载页面数据
    function carListData(pageNo){
        var status="${status}";
        var searchInfo = $("#searchInfo").val();
        var beginTime = $("#beginTime").val();
        var endTime = $("#endTime").val();
        if(beginTime && endTime){
            endTime = ex.getDateString(ex.plusDateOneDay(new Date(endTime)));
        }
        carCount(searchInfo,beginTime,endTime);
        var strJson = {"pageNo":pageNo,"pageSize":pageSize,"searchInfo":searchInfo,"status":status,'beginTime':beginTime,'endTime':endTime};
        $.ajax({
            type : "post",
            dataType : "json",
            headers: {rqSide: ex.pc()},
            data : JSON.stringify(strJson),
            contentType : "application/json;charset=utf-8",
            url: "${ctx}/car/list",
            success : function(datas){
                if(datas.code == "0"){
                    var repData = datas.data.list;
                    var dataHtml = "<thead><tr><th>头像</th><th>手机号</th><th>车牌号</th><th>来源</th><th>余额</th><th>理赔余额</th><th>加入时长</th><th>状态</th><th>操作</th></tr></thead>";
                    dataHtml += "<tbody>";
                    for(var i=0;i < repData.length;i++){
                        var repDateChild = repData[i];
                        var carStatus = ex.carStatus(repDateChild.status);
                        var joinDay = 0;
                        if(repDateChild.status==30){
                            joinDay = ex.calcDayBeginToEnd(repDateChild.timeBegin.time,repDateChild.timeSignout.time)
                        }
                        if(repDateChild.status==20){
                            joinDay = ex.calcDays(repDateChild.timeBegin.time);
                        }
                        var shopName = ex.judgeEmpty(repDateChild.shopName)?repDateChild.shopName:"自然用户";
                        dataHtml += "<tr>" +
                            "<td><img src='" + ex.judgeEmpty(repDateChild.portrait) + "' style='width:32px;height:32px;border-radius:50%;vertical-align: top;margin-right:4px;' alt=''></td>" +
                            "<td>" + ex.judgeEmpty(repDateChild.customerPN) + "</td>" +
                            "<td>" + ex.judgeEmpty(repDateChild.licensePlateNumber) + "</td>" +
                            "<td>" + shopName + "</td>" +
                            "<td>" + ex.judgeEmptyOr0(repDateChild.amtCooperation) + "元</td>" +
                            "<td>" + ex.judgeEmptyOr0(repDateChild.amtCompensation) + "元</td>" +
                            "<td>" + joinDay + "天</td>" +
                            "<td>" + carStatus + "</td>" +
                            "<td>" +
                            "<button type='button' class='btn btn-info' onclick='showDetail(" + repDateChild.id + ","+ repDateChild.status +")'>查看</button>&nbsp;" +
                            "<button type='button' class='btn btn-info' onclick=addAmtCooperation('" + repDateChild.licensePlateNumber +"','"+ repDateChild.status +"')>修改</button>&nbsp;" +
                            "<button type='button' class='btn btn-danger' onclick=modifyCarUnavailable('" + repDateChild.licensePlateNumber +"')>不可用</button>&nbsp;" +
                            "</td>" +
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
                                carListData(n,status);
                                window.location.href="#top";
                            }
                        });
                        kkpager.generPageHtml();
                    }
                }
            }
        });
    }

    //查看车辆详情
    function showDetail(carId,status) {
        self.location.href = "${ctx}/car/carDetail.html?carId=" + carId + "&status=" + status;
    }

    function changeTab(flag,status) {
        var searchInfo = $("#searchInfo").val();
        var beginTime = $("#beginTime").val();
        var endTime = $("#endTime").val();
        self.location.href = "${ctx}/car/setting.html?flag=" + flag + "&status=" + status + "&searchInfo=" + searchInfo + "&beginTime=" + beginTime + "&endTime=" + endTime;
    }

    //导出数据
    function exportData() {
        var status="${status}";
        var searchInfo = $("#searchInfo").val();
        var beginTime = $("#beginTime").val();
        var endTime = $("#endTime").val();
        if(beginTime && endTime){
            endTime = ex.getDateString(ex.plusDateOneDay(new Date(endTime)));
        }
        $("#excelExport").empty().append("<input name='status' value="+ status +">");
        $("#excelExport").append("<input name='searchInfo' value="+ searchInfo +">");
        $("#excelExport").append("<input name='beginTime' value="+ beginTime +">");
        $("#excelExport").append("<input name='endTime' value="+ endTime +">");
        $("#excelExport").submit();
    }

    //修改金额
    function  addAmtCooperation(licensePlateNumber, status) {
    	console.log(status != 13 && status != 20);
    	if(status != 13 && status != 20){
    		layer.alert("不能修改金额！");
    	}else{
    		layer.open({
                title:'修改'+ licensePlateNumber +'金额',
                closeBtn:'1',
                type:1,
                btn:['确定','取消'],
                anim: 5,
                shadeClose: false, //点击遮罩关闭
                content: $('#addLay'),
                yes:function(index, layero){
                    var addAmt = $(".in_1").val();
                    var type = $("input:radio[name='ac']:checked").val();
                    if (!(addAmt&&type)) {
                        alert("请正确填写所有选项");
                        return;
                    }
                    var strJson = {"licensePlateNumber":licensePlateNumber,"type":type,"addAmt":addAmt};
                    $.ajax({
                        type : "post",
                        dataType : "json",
                        headers: {rqSide: ex.pc()},
                        data : JSON.stringify(strJson),
                        contentType : "application/json;charset=utf-8",
                        url: "${ctx}/car/updateAmt",
                        success : function(datas){
                            if(datas.code != "0"){
                                alertBtn(datas.message)
                            }else{
                                location.reload();
                            }
                        }
                    });
                    layer.close(index);
                }
            });
    	}        
    }

    // 将车辆设置为不可用
    function modifyCarUnavailable(licensePlateNumber) {
        layer.open({
            title:'确认',
            closeBtn:'1',
            type:1,
            btn:['确定','取消'],
            anim: 5,
            shadeClose: false, //点击遮罩关闭
            content: '<div style="margin: 20px;">确定要把车辆:' + licensePlateNumber + ',设置为不可用吗?</div>',
            yes:function(index, layero){
                var strJson = {"licensePlateNumber":licensePlateNumber};
                $.ajax({
                    type : "post",
                    dataType : "json",
                    headers: {rqSide: ex.pc()},
                    data : JSON.stringify(strJson),
                    contentType : "application/json;charset=utf-8",
                    url: "${ctx}/car/modifyCarUnavailable",
                    success : function(datas){
                        if(datas.code != "0"){
                            alertBtn(datas.message)
                        }else{
                            location.reload();
                        }
                    }
                });
                layer.close(index);
            }
        });
    }
</script>
</body>
</html>