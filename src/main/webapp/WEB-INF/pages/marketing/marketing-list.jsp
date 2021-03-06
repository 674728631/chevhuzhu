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
                <li><i class="fa fa-dashboard"></i> 营销</li>
                <li>营销统计</li>
            </ul>
        </div>
        <div class="row margin-row-max">
            <div class="col-sm-12 new-search-btn">
                <!-- <input id="searchInfo" type="text" class="form-control" aria-describedby="inputGroupSuccess1Status" onkeyup="value=value.replace(/\s/g,'')" placeholder="请输入手机号、昵称 / 名称搜索信息" style="width:30%;">
                <button type="button" class="btn def-btn btn-success" onclick="search()" style="width:auto;">搜索</button> -->
                <div class="" style="position:relative;float:left;display: inline-block;margin-left:0px;">
                  	<button id="selectTimeBtn" type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false" value="-1">时间筛选 <span class="caret"></span></button>
                    <ul class="dropdown-menu dropdown-menu-right">
                        <li><a href="#" onclick="selectTime(1)">今天</a></li>
                        <li><a href="#" onclick="selectTime(2)">本周</a></li>
                        <li><a href="#" onclick="selectTime(3)">本月</a></li>
                        <li><a href="#" onclick="selectTime(4)">自定义</a></li>
                    </ul>
                </div>
                <button type="button" class="btn def-btn btn-info" onclick="clearSelectData()" style="width:auto;margin-left:30px;">清除筛选条件</button>
                <!-- <div class="" style="position:relative;float:left;display: inline-block;margin-left:30px;">
                    <button id="importDataBtn" type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false" value="-1">导出 <span class="caret"></span></button>
                    <ul class="dropdown-menu dropdown-menu-right">
                        <li><a href="#" onclick="importData(1)">用户</a></li>
                        <li><a href="#" onclick="importData(2)">商家</a></li>
                        <li><a href="#" onclick="importData(3)">其他渠道</a></li>
                    </ul>
                </div> -->
            </div>
	        <div class="time-layer" style="display: none;">
	            <form class="form-horizontal margin-top-ele lay-form-time" action="${ctx}/excel/doExcelInvitation1" method="post" id="excelExport" style="padding-top:4px;margin-top:4px;">
	                <input type="hidden" name="customerPN" id="customerPN" />
	            </form>
	        </div>
        </div>
        <div class="row" style="padding-left: 15px;padding-bottom: 5px">
            <div id="timeShowDiv">时间：<span id="timeShowSpan">全部</span></div>
        </div>
        <div class="index-wrap row" style="margin-top: -45px;">
            <ul class="col-lg-12 col-sm-12">
                <li><p>微信关注</p><span id="weChatConcernNum"></span></li>
                <li><p>注册</p><span id="registerNum"></span></li>
                <li><p>观察期</p><span id="observationNum"></span></li>
                <li ><p>保障中</p><span id="guaranteeNum"></span></li>
                <li ><p>充值笔数</p><span id="rechargeNum"></span></li>
                <li ><p>充值金额</p><span id="amtRcharge"></span></li>
                <li ><p>理赔通过数</p><span id="eventNum"></span></li>
                <li ><p>理赔金额</p><span id="eventMoney"></span></li>
            </ul>
        </div>
        <div class="row">
            <div class="col-lg-12 col-xs-12">
                <div>
                    <ul class="nav nav-tabs nav-bac" role="tablist">
                        <li role="presentation" class="active"><a id="userNum" href="#home_1" aria-controls="home_1" role="tab" data-toggle="tab" onclick="setData(1,1)">用户(0)</a></li>
                        <li role="presentation" class="flag1"><a id="businessNum" href="#home_1" aria-controls="home_1" role="tab" data-toggle="tab" onclick="setBusinessData(1)">商家(0)</a></li>
                        <li role="presentation" class="flag1"><a id="channelNum" href="#home_1" aria-controls="home_1" role="tab" data-toggle="tab" onclick="setChannelData(1)">其他渠道(0)</a></li>
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
<div id="customTimeLayer" style="padding: 10px;">
    <p class="pull-left" style="line-height:34px;">起止时间</p>
    <input id="beginTime" value="" class="form-control pull-left" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd'});" placeholder="选择开始时间" style="width:120px;margin-left:6px;">
    <p class="pull-left" style="line-height:34px;margin-left:6px;">-</p>
    <input id="endTime" value="" class="form-control pull-left" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd'});" placeholder="选择结束时间" style="width:120px;margin-left:6px;">
</div>
<script type="text/javascript" src="${ctx}/cite/plugins/MyDatePicker/WdatePicker.js"></script>
<script type="text/javascript">
    var pageSize=10;
    $("#businessNum").html("商家(${businessCount})");
    $("#channelNum").html("其他渠道(${channelCount})");
    // 选择的tab标签，默认是用户(1:用户，2：商家，3：其他渠道)
    var tabType = 1;
    // 开始时间的条件
    var beginTimeCondition = "";
    // 结束的时间条件
    var endTimeCondition = "";
    // 初始化时间的显示
    $("#timeShowDiv").html("截止时间：" + getDateStr3(new Date()));
	
    localShowData(beginTimeCondition,endTimeCondition);
    setData(1,1);
    //加载页面数据
    function setData(pageNo,status){
        tabType = 1;
        var searchInfo = $("#searchInfo").val();
        var strJson = {"pageNo":pageNo,"pageSize":pageSize,"customerPN":searchInfo,"status":status,"beginTime":beginTimeCondition,"endTime":endTimeCondition};
        $.ajax({
            type : "post",
            dataType : "json",
            headers: {rqSide: ex.pc()},
            data : JSON.stringify(strJson),
            contentType : "application/json;charset=utf-8",
            url: "${ctx}/invitation/list",
            success : function(datas){
                if(datas.code == "0"){
                    $("#userNum").html("用户("+ datas.data.total +")");
                    var repData = datas.data.list;
                    var dataHtml = "<thead><tr><th>排名</th><th>昵称/名称</th><th>手机号</th><th>关注数</th><th>注册数</th><th>观察期车辆</th><th>保障中车辆</th><th>占比（观察期+保障中）</th><th>注册时间<th>操作</th></tr></thead>";
                    dataHtml += "<tbody>";
                    for(var i=0;i < repData.length;i++){
                        var repDateChild = repData[i];
                        dataHtml += "<tr>";
                        dataHtml += "<td>"+ ((pageNo -1) * pageSize + (i + 1)) +"</td>";
                        dataHtml += "<td>" + ex.judgeEmpty(repDateChild.nickname) + "</td>" +
                            "<td>" + ex.judgeEmpty(repDateChild.customerPN) + "</td>" +
                            "<td>" + ex.judgeEmptyOr0(repDateChild.carNum) + "</td>" +
                            "<td>" + ex.judgeEmptyOr0(repDateChild.count) + "</td>" +
                            "<td>" + ex.judgeEmpty(repDateChild.guancha) + "</td>" +
                            "<td>" + ex.judgeEmpty(repDateChild.baozhang) + "</td>" +
                            "<td>" + repDateChild.ratio + "%</td>" +
                            "<td>" + repDateChild.createAt + "</td>" +
                            "<td>" +
                            "<button type='button' class='btn btn-info' onclick='switchToDetail(" + repDateChild.customerId + ")'>查看</button>&nbsp;" +
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
                                setData(n,status);
                                window.location.href="#top";
                            }
                        });
                        kkpager.generPageHtml();
                    }
                }
            }
        });
    }

    // 加载商家的数据
    function setBusinessData(pageNo){
        tabType = 2;
        var searchInfo = $("#searchInfo").val();
        var strJson = {"pageNo":pageNo,"pageSize":pageSize,"customerPN":searchInfo,"beginTime":beginTimeCondition,"endTime":endTimeCondition};
        $.ajax({
            type : "post",
            dataType : "json",
            headers: {rqSide: ex.pc()},
            data : JSON.stringify(strJson),
            contentType : "application/json;charset=utf-8",
            url: "${ctx}/invitation/businessList",
            success : function(datas){
                if(datas.code == "0"){
                    $("#businessNum").html("商家("+ datas.data.total +")");
                    var repData = datas.data.list;
                    var dataHtml = "<thead><tr><th>排名</th><th>昵称/名称</th><th>手机号</th><th>关注数</th><th>注册数</th><th>观察期车辆</th><th>保障中车辆</th><th>占比（观察期+保障中）</th><th>注册时间<th>操作</th></tr></thead>";
                    dataHtml += "<tbody>";
                    for(var i=0;i < repData.length;i++){
                        var repDateChild = repData[i];
                        dataHtml += "<tr>";
                        dataHtml += "<td>"+ ((pageNo -1) * pageSize + (i + 1)) +"</td>";
                        dataHtml += "<td>" + repDateChild.name + "</td>" +
                            "<td>" + ex.judgeEmpty(repDateChild.tel) + "</td>" +
                            "<td>" + repDateChild.followNum + "</td>" +
                            "<td>" + repDateChild.registerNum + "</td>" +
                            "<td>" + repDateChild.guancha + "</td>" +
                            "<td>" + repDateChild.baozhang + "</td>" +
                            "<td>" + repDateChild.ratio + "%</td>" +
                            "<td>" + repDateChild.createAt + "</td>" +
                            "<td>" +
                            "<button type='button' class='btn btn-info' onclick='switchToDetail(" + repDateChild.maintenanceshopId + ")'>查看</button>&nbsp;" +
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
                                setBusinessData(n);
                                window.location.href="#top";
                            }
                        });
                        kkpager.generPageHtml();
                    }
                }
            }
        });
    }

    // 加载其他渠道的数据
    function setChannelData(pageNo){
        tabType = 3;
        var searchInfo = $("#searchInfo").val();
        var strJson = {"pageNo":pageNo,"pageSize":pageSize,"customerPN":searchInfo,"beginTime":beginTimeCondition,"endTime":endTimeCondition};
        $.ajax({
            type : "post",
            dataType : "json",
            headers: {rqSide: ex.pc()},
            data : JSON.stringify(strJson),
            contentType : "application/json;charset=utf-8",
            url: "${ctx}/invitation/channelList",
            success : function(datas){
                if(datas.code == "0"){
                    $("#channelNum").html("其他渠道("+ datas.data.total +")");
                    var repData = datas.data.list;
                    var dataHtml = "<thead><tr><th>排名</th><th>昵称/名称</th><th>手机号</th><th>关注数</th><th>注册数</th><th>观察期车辆</th><th>保障中车辆</th><th>占比（观察期+保障中）</th><th>注册时间<th>操作</th></tr></thead>";
                    dataHtml += "<tbody>";
                    for(var i=0;i < repData.length;i++){
                        var repDateChild = repData[i];
                        dataHtml += "<tr>";
                        dataHtml += "<td>"+ ((pageNo -1) * pageSize + (i + 1)) +"</td>";
                        dataHtml += "<td>" + repDateChild.name + "</td>" +
                            "<td>" + ex.judgeEmpty(repDateChild.tel) + "</td>" +
                            "<td>" + repDateChild.followNum + "</td>" +
                            "<td>" + repDateChild.registerNum + "</td>" +
                            "<td>" + repDateChild.guancha + "</td>" +
                            "<td>" + repDateChild.baozhang + "</td>" +
                            "<td>" + repDateChild.ratio + "%</td>" +
                            "<td>" + repDateChild.createAt + "</td>" +
                            "<td>" +
                            "<button type='button' class='btn btn-info' onclick='switchToDetail(" + repDateChild.maintenanceshopId + ")'>查看</button>&nbsp;" +
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
                                setChannelData(n);
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
            self.location.href = "${ctx}/invitation/detailList.html/" + customerId;
    }

    // 点击搜索按钮
    function search() {
        if (tabType === 1){
            setData(1,1);
        }else if (tabType === 2){
            setBusinessData(1);
        }else if (tabType === 3){
            setChannelData(1);
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
        $("#excelExport").submit();
    }

    // 选择时间
    function selectTime(type) {
        if (type === 1){
            // 今天
            $("#selectTimeBtn").html("今天");
            var currentDayStr = getDateStr3(new Date());
            $("#timeShowDiv").html("时间："+ currentDayStr + "至" + currentDayStr);
            beginTimeCondition = currentDayStr;
            endTimeCondition = currentDayStr;
            search();
        } else if (type === 2){
            // 本周
            $("#selectTimeBtn").html("本周");
            var weekArr = getWeekStartAndEnd(0);
            $("#timeShowDiv").html("时间："+ weekArr[0] + "至" + weekArr[1]);
            beginTimeCondition = weekArr[0];
            endTimeCondition = weekArr[1];
            search();
        } else if (type === 3){
            // 本月
            $("#selectTimeBtn").html("本月");
            var monthArr = getMonthStartAndEnd(0);
            $("#timeShowDiv").html("时间："+ monthArr[0] + "至" + monthArr[1]);
            beginTimeCondition = monthArr[0];
            endTimeCondition = monthArr[1];
            search();
        } else if (type === 4) {
            // 自定义
            $("#beginTime").val("");
            $("#endTime").val("");
            // 弹出框,选择时间
            layer.open({
                title:'自定义时间',
                closeBtn:'1',
                type:1,
                btn:['确定','取消'],
                anim: 5,
                shadeClose: false, //点击遮罩关闭
                content: $('#customTimeLayer'),
                yes:function(index, layero){
                    var bgTime = $("#beginTime").val();
                    var edTime = $("#endTime").val();
                    // 判断开始和结束时间
                    if (bgTime == null || undefined == bgTime || "" == bgTime){
                        alert("开始时间不能为空");
                        return;
                    }
                    if (edTime == null || undefined == edTime || "" == edTime){
                        alert("结束时间不能为空");
                        return;
                    }
                    // 结束时间不能大于开始时间
                    var starttime = new Date(Date.parse(bgTime));
                    var endtime = new Date(Date.parse(edTime));
                    if (endtime < starttime){
                        alert("结束时间不能小于开始时间");
                        return;
                    }
                    $("#selectTimeBtn").html("自定义");
                    $("#timeShowDiv").html("时间："+ $("#beginTime").val() + "至" + $("#endTime").val());
                    beginTimeCondition = $("#beginTime").val();
                    endTimeCondition = $("#endTime").val();
                    layer.close(index);
                    search();
                }
            });
        }
    }
    
    // 清除按钮
    function clearSelectData() {
        $("#searchInfo").val("");
        $("#selectTimeBtn").html("时间筛选");
        beginTimeCondition = "";
        endTimeCondition = "";
        $("#timeShowDiv").html("截止时间：" + getDateStr3(new Date()));
        setData(1,1);
    }
    // 导出
    function importData(type) {
        var importUrl = "";
        if (type === 1){
            // 用户
            importUrl = "${ctx}/invitation/importExcelForUser";
        } else if (type === 2){
            // 商家
            importUrl = "${ctx}/invitation/importExcelForBusiness";
        } else if (type === 3){
            importUrl = "${ctx}/invitation/importExcelForChannel";
        }
        if (importUrl != ""){
            downloadFileByForm(importUrl);
        }
    }

    // 下载文件
    function switchToDetail(id) {
        var url = "${ctx}/invitation/detailList.html";
        var form = $("<form></form>").attr("action", url).attr("method", "get");
        form.append($("<input></input>").attr("type", "hidden").attr("name", "customerId").attr("value", id));
        form.append($("<input></input>").attr("type", "hidden").attr("name", "tabType").attr("value", tabType));
        form.append($("<input></input>").attr("type", "hidden").attr("name", "beginTime").attr("value", beginTimeCondition));
        form.append($("<input></input>").attr("type", "hidden").attr("name", "endTime").attr("value", endTimeCondition));
        form.appendTo('body').submit().remove();
    }

    // 下载文件
    function downloadFileByForm(url) {
        var form = $("<form></form>").attr("action", url).attr("method", "get");
        form.append($("<input></input>").attr("type", "hidden").attr("name", "pageNo").attr("value", 1));
        form.append($("<input></input>").attr("type", "hidden").attr("name", "pageSize").attr("value", 10000));
        form.append($("<input></input>").attr("type", "hidden").attr("name", "customerPN").attr("value", $("#searchInfo").val()));
        form.append($("<input></input>").attr("type", "hidden").attr("name", "beginTime").attr("value", beginTimeCondition));
        form.append($("<input></input>").attr("type", "hidden").attr("name", "endTime").attr("value", endTimeCondition));
        form.appendTo('body').submit().remove();
    }

    // 获取本周的其实日期
    // AddWeekCount为0代表当前周   为-1代表上一个周   为1代表下一个周以此类推
    function getWeekStartAndEnd(AddWeekCount) {
        var startStop = new Array();
        var millisecond = 1000 * 60 * 60 * 24;
        var currentDate = new Date();
        currentDate = new Date(currentDate.getTime() + (millisecond * 7*AddWeekCount));
        var week = currentDate.getDay();
        var month = currentDate.getDate();
        var minusDay = week != 0 ? week - 1 : 6;
        var currentWeekFirstDay = new Date(currentDate.getTime() - (millisecond * minusDay));
        var currentWeekLastDay = new Date(currentWeekFirstDay.getTime() + (millisecond * 6));
        startStop.push(getDateStr3(currentWeekFirstDay));
        startStop.push(getDateStr3(currentWeekLastDay));
        return startStop;
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

    /**
     * 获得相对当月AddMonthCount个月的起止日期
     * AddMonthCount为0 代表当月 为-1代表上一个月  为1代表下一个月 以此类推
     * ***/
    function getMonthStartAndEnd(AddMonthCount) {
        var startStop = new Array();
        var currentDate = new Date();
        var month=currentDate.getMonth()+AddMonthCount;
        if(month<0){
            var n = parseInt((-month)/12);
            month += n*12;
            currentDate.setFullYear(currentDate.getFullYear()-n);
        }
        currentDate = new Date(currentDate.setMonth(month));
        var currentMonth = currentDate.getMonth();
        var currentYear = currentDate.getFullYear();
        var currentMonthFirstDay = new Date(currentYear, currentMonth,1);
        var currentMonthLastDay = new Date(currentYear, currentMonth+1, 0);
        startStop.push(getDateStr3(currentMonthFirstDay));
        startStop.push(getDateStr3(currentMonthLastDay));
        return startStop;
    }
    
    //加载局部数据--概况
    function localShowData(beginTime,endTime){
        var strJson = {"beginTime":beginTime,"endTime":endTime};
        $.ajax({
            type : "post",
            dataType : "json",
            headers: {rqSide: ex.pc()},
            data : JSON.stringify(strJson),
            contentType : "application/json;charset=utf-8",
            url: "${ctx}/marketing/localShow",
            success : function(datas){
                if(datas.code == "0"){
                    var repData = datas.data;
                    $("#weChatConcernNum").html(ex.judgeEmptyOr0(repData.weChatConcernNum));
                    $("#registerNum").html(ex.judgeEmptyOr0(repData.registerNum));
                    $("#observationNum").html(ex.judgeEmptyOr0(repData.observationNum));
                    $("#guaranteeNum").html(ex.judgeEmptyOr0(repData.guaranteeNum));
                    $("#amtRcharge").html(ex.judgeEmptyOr0(repData.amtRcharge));
                    $("#rechargeNum").html(ex.judgeEmptyOr0(repData.rechargeNum));
                    $("#eventMoney").html(ex.judgeEmptyOr0(repData.eventMoney));
                    $("#eventNum").html(ex.judgeEmptyOr0(repData.eventNum));
                }
            }
        });
    }
</script>
</body>
</html>