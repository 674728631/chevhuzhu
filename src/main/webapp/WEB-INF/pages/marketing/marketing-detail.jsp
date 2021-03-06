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
        <div class="datebox-pick clearfix" style="margin-top: 55px;">
            <p class="datebox-title">时间：</p>
            <div class="datebox-btn" style="position:relative;float:left;display: inline-block;margin-left:0px;">
                <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false" value="-1"><span id="btn-title" class="btn-title">今日</span> <span class="caret"></span></button>
                <ul class="dropdown-menu dropdown-menu-right">
                    <li><a href="javascript:;" onclick="selectTime(1)">今日</a></li>
                    <li><a href="javascript:;" onclick="selectTime(2)">本周</a></li>
                    <li><a href="javascript:;" onclick="selectTime(3)">本月</a></li>
                    <li><a href="javascript:;" onclick="selectTime(4)">自定义</a></li>
                </ul>
            </div>
        </div>
        <div class="base-count">
            <ul class="base-list clearfix">
                <li><a href="${ctx}/marketing/weChat.html?1"><p class="base-num num1">${weChatConcernNum}</p></a><p class="base-desc">微信访问数</p></li>
                <li><a href="${ctx}/marketing/weChat.html?2"><p class="base-num num2">${registerNum}</p></a><p class="base-desc">注册</p></li>
                <li><a href="${ctx}/marketing/weChat.html?3"><p class="base-num num3">${observationNum}</p></a><p class="base-desc">观察期</p></li>
                <li><a href="${ctx}/marketing/weChat.html?4"><p class="base-num num4">${guaranteeNum}</p></a><p class="base-desc">保障中</p></li>
                <li><p class="base-num num5">${rechargeNum}</p><p class="base-desc">充值笔数</p></li>
                <li><p class="base-num num6">${eventNum}</p><p class="base-desc">理赔通过数</p></li>
                <li><p class="base-num num7">${eventMoney}</p><p class="base-desc">理赔金额</p></li>
            </ul>
        </div>
        <div class="user-count" style="margin-top: 26px;">
            <p class="user-type" style="font-size: 15px"><b>拉新数据（总会员 <span class="p2 base-num2 num12"></span>）</b></p>
        </div>
        <div class="user-table">
            <table class="table1"></table>
        </div>
        <p class="ttile" style="margin-top: 30px;"><strong>全局数据</strong></p>
        <ul class="base-data clearfix">
            <li><p class="p1">微信关注</p><p class="p2"><span class="p1"></span>　</p><p class="p2 base-num2 num10">-</p></li>
            <li><p class="p1">注册</p><p class="p2"><span class="p1"></span>　</p><p class="p2 base-num2 num11">-</p></li>
            <li><p class="p1">总会员</p><p class="p2"><span class="p1"></span>　</p><p class="p2 base-num2 num12">-</p></li>
            <li><p class="p1">观察期</p><p class="p2"><span class="p1"></span>　</p><p class="p2 base-num2 num13">-</p></li>
            <li><p class="p1">保障中</p><p class="p2"><span class="p1"></span>　</p><p class="p2 base-num2 num14">-</p></li>
            <li><p class="p1">退出计划</p><p class="p2"><span class="p1"></span>　</p><p class="p2 base-num2 num15">-</p></li>
            <li><p class="p1">退出再加入</p><p class="p2"><span class="p1"></span>　</p><p class="p2 base-num2 num16">-</p></li>

            <li><p class="p1">渠道</p><p class="p2"><span class="p1">数量　</span><span class="base-num2 num17">-</span></p><p class="p2"><span class="p1">占比　</span><span class="base-num2 num18">-</span></p></li>
            <li><p class="p1">自然用户</p><p class="p2"><span class="p1">数量　</span><span class="base-num2 num19">-</span></p><p class="p2"><span class="p1">占比　</span><span class="base-num2 num20">-</span></p></li>
            <li><p class="p1">邀请</p><p class="p2"><span class="p1">数量　</span><span class="base-num2 num21">-</span></p><p class="p2"><span class="p1">占比　</span><span class="base-num2 num22">-</span></p></li>

            <li><p class="p1">用户成本</p><p class="p2"><span class="base-num2 num1"></span>　</p><p class="p2 base-num2 clazz1"></p></li>
            <li><p class="p1">平均充值金额</p><p class="p2"><span class="base-num2 num2"></span>　</p><p class="p2 base-num2 clazz2"></p></li>
            <%--<li><p class="p1">平均保障中天数</p><p class="p2"><span class="p1"></span>　</p><p class="p2 base-num2 num3">-</p></li>--%>
            <li><p class="p1">平均理赔金额</p><p class="p2"><span class="base-num2 num4"></span>　</p><p class="p2 base-num2 clazz4"></p></li>
            <%--<li><p class="p1">待支付</p><p class="p2"><span class="p1">数量　</span><span class="base-num2 num5">-</span></p><p class="p2"><span class="p1">占比　</span><span class="base-num2 num6">-</span></p></li>--%>
            <li><p class="p1">余额不足数</p><p class="p2"><span class="p1">数量　</span><span class="base-num2 num7">-</span></p><p class="p2"><span class="p1">占比　</span><span class="base-num2 num8">-</span></p></li>
            <li><p class="p1">申请理赔次数</p><p class="p2"><span class="p1">数量　</span><span class="base-num2 num9">-</span></p><p class="p2"><span class="p1">占比　</span><span class="base-num2 num30">-</span></p></li>
            <li><p class="p1">申请理赔通过次数</p><p class="p2"><span class="p1">数量　</span><span class="base-num2 num31">-</span></p><p class="p2"><span class="p1">占比　</span><span class="base-num2 num32">-</span></p></li>
        </ul>
        <div class="chart-box">
            <p class="chart-title"><strong>全局理赔概况</strong></p>
            <div class="chart-legend">
                <p><i class="chart-legend-icon i1"></i>理赔率</p>
                <p><i class="chart-legend-icon i2"></i>保障中车辆</p>
                <p><i class="chart-legend-icon i3"></i>理赔车辆</p>
            </div>
            <div class="chart-body chart1">
                <div class="chart-content"></div>
            </div>
        </div>
        <div class="chart-box b2 clearfix">
            <div class="chart-body chart2">
                <p class="chart-title-sub s2"><strong>受损部位</strong></p>
                <div class="chart-content">
                    <canvas height="250"></canvas>
                </div>
            </div>
            <div class="chart-body chart3">
                <p class="chart-title-sub s2"><strong>理赔金额</strong></p>
                <div class="chart-content">
                    <canvas height="250"></canvas>
                </div>
            </div>
            <div class="chart-body chart4">
                <p class="chart-title-sub s2"><strong>虚拟补贴</strong></p>
                <div class="chart-content">
                    <canvas height="250"></canvas>
                </div>
            </div>
        </div>
        <div class="user-count">
            <p class="user-type" style="font-size: 15px"><b>虚拟补贴</b></p>
        </div>
        <div class="user-table">
            <table class="table2"></table>
        </div>
        <div class="chart-box clearfix">
            <p class="chart-title"><strong>购买行为概况</strong></p>
            <div class="chart-body chart5">
            	<div class="chart-legend-sub">
                    <p>总计：<span id="rechargeNum">-</span>笔</p>
                </div>
                <div class="chart-content"></div>
            </div>
            <div class="chart-body chart6">
                <div class="chart-legend-sub">
                    <p style="float:left;">总复购率：<span id="rechargeNum3"></span>%</p>
                    <p style="float:left;">&nbsp;总计：<span id="rechargeNum2">-</span>笔</p>
                </div>
                <div class="chart-content"></div>
            </div>
        </div>
        <div class="user-count">
            <p class="user-type" style="font-size: 15px"><b>维修厂结算</b></p>
            <p class="user-arr"><a href="${ctx}/foundation/shopBill.html"><span class="a1"></span><span class="a2"></span></a></p>
        </div>
        <div class="user-table">
            <table class="table3"></table>
        </div>
    </div>
</div>
<div id="customTimeLayer" style="padding: 10px;display:none;">
    <p class="pull-left" style="line-height:34px;">起止时间</p>
    <input id="beginTime" value="" class="form-control pull-left" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd'});" placeholder="选择开始时间" style="width:120px;margin-left:6px;">
    <p class="pull-left" style="line-height:34px;margin-left:6px;">-</p>
    <input id="endTime" value="" class="form-control pull-left" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd'});" placeholder="选择结束时间" style="width:120px;margin-left:6px;">
</div>
<script type="text/javascript" src="${ctx}/cite/plugins/MyDatePicker/WdatePicker.js"></script>
<script type="text/javascript">

    // 选择的tab标签，默认是用户(1:用户，2：商家，3：其他渠道)
    var tabType = 1;
    selectTime(1);
    // 开始时间的条件
    var beginTimeCondition = "";
    // 结束的时间条件
    var endTimeCondition = "";

    //localShowData(beginTimeCondition, endTimeCondition);
    //setInviationData(beginTimeCondition, endTimeCondition);
    //选择时间
    function selectTime(type) {
        if (type === 1){
            // 今天
            $("#btn-title").html("今天");
            var currentDayStr = getDateStr3(new Date());
            beginTimeCondition = currentDayStr;
            endTimeCondition = getDateStr3(getNextDay());
            /*        console.log(beginTimeCondition+"+++++++"+endTimeCondition); */
            search();
        } else if (type === 2){
            // 昨天
            $("#btn-title").html("本周");
            var weekArr = getWeekStartAndEnd(0);
            beginTimeCondition = weekArr[0];
            endTimeCondition = weekArr[1];
            search();
        } else if (type === 3){
            // 本月
            $("#btn-title").html("本月");
            var monthArr = getMonthStartAndEnd(0);
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
                	console.log(1111);
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


    // 根据tab查询数据
    function search() {
        localShowData(beginTimeCondition, endTimeCondition);
        setInviationData(beginTimeCondition, endTimeCondition);
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
                    // 渲染部分数据
                    /* for (var i = 1; i < 8; i++) {
                      $('.base-num.num' + i).text(1000);  // 顶部7个数字
                    } */
                    var repData = datas.data;
                    $('.base-num.num1').text(ex.judgeEmptyOr0(repData.weChatConcernNum));
                    $('.base-num.num2').text(ex.judgeEmptyOr0(repData.registerNum));
                    $('.base-num.num3').text(ex.judgeEmptyOr0(repData.observationNum));
                    $('.base-num.num4').text(ex.judgeEmptyOr0(repData.guaranteeNum));
                    $('.base-num.num5').text(ex.judgeEmptyOr0(repData.rechargeNum));
                    $('.base-num.num6').text(ex.judgeEmptyOr0(repData.eventNum));
                    $('.base-num.num7').text(ex.judgeEmptyOr0(repData.eventMoney));
                }
            }
        });
    }

    //加载拉新数据
    function setInviationData(beginTime,endTime){
        var strJson = {"beginTime":beginTime,"endTime":endTime};
        $.ajax({
            type : "post",
            dataType : "json",
            headers: {rqSide: ex.pc()},
            data : JSON.stringify(strJson),
            contentType : "application/json;charset=utf-8",
            url: "${ctx}/invitation/inviationDate",
            success : function(datas){
                if(datas.code == "0"){
                    /*  $("#userNum").html("用户("+ datas.data.total +")"); */
                    var repData = datas.data.list;
                    table1_data = [];
                    for(var i=0;i < repData.length;i++){
                        var repDateChild = repData[i];
                        table1_data.push([i+1,ex.judgeEmpty(repDateChild.name),ex.judgeEmptyOr0(repDateChild.followNum),ex.judgeEmptyOr0(repDateChild.registerNum),ex.judgeEmptyOr0(repDateChild.numbers),repDateChild.ratio + "%",])
                    }
                    $('.table1').html(createTable(table1_titles_per, table1_titles, table1_data));
                    $('.table1').on('click', 'tr', function(){
                    	var name = $(this).find('td:eq(1)').text();
                    	if(name != "邀请活动" && name != "自然用户"){
                    		name = decodeURI(name);
                        	//alert(name);
                        	window.location.href="../maintenanceshop/list.html?"+name;
                    	}
                    })
                }
            }
        });
    }

    // 拉新排行榜表格
    var table1_titles_per = [5, 30, 15, 15, 15, 20];  // 每列宽度占比
    var table1_titles = ['排名', '渠道', '关注', '注册', '观察期',	'拉新占比'];
    var table1_data = [];

    //加载用户数据
    function setData(){
        tabType = 1;
        var strJson = {"status":status,"beginTime":beginTimeCondition,"endTime":endTimeCondition};
        $.ajax({
            type : "post",
            dataType : "json",
            headers: {rqSide: ex.pc()},
            data : JSON.stringify(strJson),
            contentType : "application/json;charset=utf-8",
            url: "${ctx}/invitation/userDate",
            success : function(datas){
                if(datas.code == "0"){
                    /*  $("#userNum").html("用户("+ datas.data.total +")"); */
                    var repData = datas.data.list;
                    table1_data = [];
                    for(var i=0;i < repData.length;i++){
                        var repDateChild = repData[i];
                        table1_data.push([i+1,ex.judgeEmpty(repDateChild.name),ex.judgeEmptyOr0(repDateChild.followNum),ex.judgeEmptyOr0(repDateChild.registerNum),ex.judgeEmptyOr0(repDateChild.numbers),repDateChild.ratio + "%",])
                    }
                    $('.table1').html(createTable(table1_titles_per, table1_titles, table1_data));
                }
            }
        });
    }

    //加载商家的数据
    function setBusinessData(){
        tabType = 2;
        var strJson = {"beginTime":beginTimeCondition,"endTime":endTimeCondition};
        $.ajax({
            type : "post",
            dataType : "json",
            headers: {rqSide: ex.pc()},
            data : JSON.stringify(strJson),
            contentType : "application/json;charset=utf-8",
            url: "${ctx}/invitation/businessDate",
            success : function(datas){
                if(datas.code == "0"){
                    /* $("#businessNum").html("商家("+ datas.data.total +")"); */
                    var repData = datas.data.list;
                    table1_data = [];
                    for(var i=0;i < repData.length;i++){
                        var repDateChild = repData[i];
                        table1_data.push([i+1,ex.judgeEmpty(repDateChild.name),ex.judgeEmptyOr0(repDateChild.followNum),ex.judgeEmptyOr0(repDateChild.registerNum),ex.judgeEmptyOr0(repDateChild.guancha),ex.judgeEmptyOr0(repDateChild.baozhang),repDateChild.ratio + "%",])
                    }
                    $('.table1').html(createTable(table1_titles_per, table1_titles, table1_data));
                }
            }
        });
    }

    // 加载其他渠道的数据
    function setChannelData(){
        tabType = 3;
        var strJson = {"beginTime":beginTimeCondition,"endTime":endTimeCondition};
        $.ajax({
            type : "post",
            dataType : "json",
            headers: {rqSide: ex.pc()},
            data : JSON.stringify(strJson),
            contentType : "application/json;charset=utf-8",
            url: "${ctx}/invitation/channelDate",
            success : function(datas){
                if(datas.code == "0"){
                    /* $("#channelNum").html("其他渠道("+ datas.data.total +")"); */
                    var repData = datas.data.list;
                    table1_data = [];
                    for(var i=0;i < repData.length;i++){
                        var repDateChild = repData[i];
                        table1_data.push([i+1,ex.judgeEmpty(repDateChild.name),ex.judgeEmptyOr0(repDateChild.followNum),ex.judgeEmptyOr0(repDateChild.registerNum),ex.judgeEmptyOr0(repDateChild.guancha),repDateChild.ratio + "%",])
                    }
                    $('.table1').html(createTable(table1_titles_per, table1_titles, table1_data));
                }
            }
        });
    }

    loadRecharge();
    //加载充值数据
    function loadRecharge() {
        var strJson = {};
        $.ajax({
            type: "post",
            dataType: "json",
            headers: {rqSide: ex.pc()},
            data: JSON.stringify(strJson),
            contentType: "application/json;charset=utf-8",
            url: "${ctx}/marketing/getRechargeInfo",
            success: function (datas) {
                if (datas.code == "0") {
                    var repData = datas.data;
                    chart5_data = [];
                    var total = 0;
                    for (var i = 0; i < repData.length; i++) {
                        var repDateChild = repData[i];
                        chart5_data.push({"name": repDateChild.amt, "value": repDateChild.num});
                        total += repDateChild.num;
                    }
                    chart5.setOption({series: {data: chart5_data}});
                    $('#rechargeNum').text(total); // 总笔数
                }
            }
        });
    }

    loadReEnter();
    //复购
    function loadReEnter(){
        $.ajax({
            type: "post",
            dataType: "json",
            headers: {rqSide: ex.pc()},
            contentType: "application/json;charset=utf-8",
            url: "${ctx}/marketing/getReEnter",
            success:function(datas){
                if (datas.code == "0") {
                    var repData = datas.data;
                    chart6_data1 = [repData.userNum12, repData.userNum3, repData.userNum9x, repData.userNum9z]; //用户数
                    chart6_data2 = [repData.rechargeNum12, repData.rechargeNum3, repData.rechargeNum9x, repData.rechargeNum9z]; //复购次数
                    chart6_labels = ['1.2元\n\n复购率: '+repData.per12+'%',
                        '3元\n\n复购率: '+repData.per3+'%',
                        '9元(虚拟补贴)\n\n复购率: '+repData.per9x+'%',
                        '9元(自然用户)\n\n复购率: '+repData.per9z+'%'];
                    chart6.setOption({series: [{data: chart6_data1}, {data: chart6_data2}],xAxis:{data:chart6_labels}});
                    var total = repData.rechargeNum12 + repData.rechargeNum3 + repData.rechargeNum9x + repData.rechargeNum9z;
                    $('#rechargeNum2').text(total); // 总笔数
                    $('#rechargeNum3').append(repData.totalPer); // 总笔数
                }
            }
        });
    }

    loadShopList();
    function loadShopList(){
        $.ajax({
            type: "post",
            dataType: "json",
            headers: {rqSide: ex.pc()},
            contentType: "application/json;charset=utf-8",
            url: "${ctx}/marketing/getShopList",
            success: function (datas) {
                if(datas.code == "0"){
                    /* $("#channelNum").html("其他渠道("+ datas.data.total +")"); */
                    var repData = datas.data;
                    table2_data = [];
                    for(var i=0;i < repData.length;i++){
                        var repDateChild = repData[i];
                        table2_data.push([i+1,ex.judgeEmpty(repDateChild.shopName),ex.judgeEmptyOr0(repDateChild.shopTotal),ex.judgeEmptyOr0(repDateChild.shopSubsidy)+ "%",ex.judgeEmptyOr0(repDateChild.shopEventTotal),ex.judgeEmptyOr0(repDateChild.shopEventRatio)+ "%"])
                    }
                    $('.table2').html(createTable(table2_titles_per, table2_titles, table2_data));
                }
            }
        });
    }
    // *************************************************************************** //
    var chart1;
    var chart2;
    var chart3;
    var chart4;
    // 页面加载事件
    $(function($){

        // 获取全局数据
        $.getJSON("${ctx}/marketing/getGlobalDate", function(datas){
            if(datas.code == "200"){
                var result = datas.data;
                $('.base-num2.num10').text(result.subscribeCount); // 微信关注
                $('.base-num2.num11').text(result.signupCount); // 注册
                $('.base-num2.num12').text(result.totalNum); // 总会员
                $('.base-num2.num13').text(result.observationNum); // 观察期
                $('.base-num2.num14').text(result.guaranteeNum); // 保障中
                $('.base-num2.num15').text(result.outNum); // 退出计划
                $('.base-num2.num16').text(result.reJoinNum); // 退出再加入

                $('.base-num2.num17').text(result.channelUserCount); // 渠道
                $('.base-num2.num18').text(result.channelPer + '%'); // 渠道占比
                $('.base-num2.num19').text(result.natureUserCount); // 自然用户
                $('.base-num2.num20').text(result.naturePer + '%'); // 自然占比
                $('.base-num2.num21').text(result.invitationUserCount); // 邀请
                $('.base-num2.num22').text(result.invitePer + '%'); // 邀请占比


                var text ="";
                if (result.userCost$ < 0) {
                    text = "<font style='color: blue'>↓</font><font style='color: blue;font-size: 12px'> " + result.userCost$ + "%</font>";
                } else if (result.userCost$ > 0) {
                    text = "<font style='color: red'>↑</font><font style='color: red;font-size: 12px'> " + result.userCost$ + "%</font>";
                } else {
                    text = "<font style='color: black'>-</font><font style='color: black;font-size: 12px'> " + result.userCost$ + "%</font>";
                }
                $('.base-num2.num1').append(result.userCost + ' 元'); // 用户成本
                $('.base-num2.clazz1').append(text);

                text ="";
                if (result.avgRecharge$ < 0) {
                    text = "<font style='color: blue'>↓</font><font style='color: blue;font-size: 12px'> " + result.avgRecharge$ + "%</font>";
                } else if (result.avgRecharge$ > 0) {
                    text = "<font style='color: red'>↑</font><font style='color: red;font-size: 12px'> " + result.avgRecharge$ + "%</font>";
                } else {
                    text = "<font style='color: black'>-</font><font style='color: black;font-size: 12px'> " + result.avgRecharge$ + "%</font>";
                }
                $('.base-num2.num2').append(result.avgRecharge + ' 元'); // 平均充值金额
                $('.base-num2.clazz2').append(text);
                
                text = "";
                if (result.avgEventAmt$ < 0) {
                    text = "<font style='color: blue'>↓</font><font style='color: blue;font-size: 12px'> " + result.avgEventAmt$ + "%</font>";
                } else if (result.avgEventAmt$ > 0) {
                    text = "<font style='color: red'>↑</font><font style='color: red;font-size: 12px'> " + result.avgEventAmt$ + "%</font>";
                } else {
                    text = "<font style='color: black'>-</font><font style='color: black;font-size: 12px'> " + result.avgEventAmt$ + "%</font>";
                }
                $('.base-num2.num4').append(result.avgEventAmt + ' 元'); // 平均理赔金额
                $('.base-num2.clazz4').append(text);
                
                /* $('.base-num2.num5').text(result.toPay); // 待支付数量
                /*$('.base-num2.num3').text(result.avgGuaranteeNum); // 平均保障中天数*/
                $('.base-num2.num6').text(result.toPayPer); // 待支付占比*/
                $('.base-num2.num7').text(result.noBanlance); // 余额不足数量
                $('.base-num2.num8').text(result.noBanlancePer + '%'); // 余额不足占比
                $('.base-num2.num9').text(result.allEventNum); // 申请理赔次数
                $('.base-num2.num30').text(result.eventPer + '%'); // 申请理赔占比
                $('.base-num2.num31').text(result.eventSuccessNum); // 申请理赔通过次数
                $('.base-num2.num32').text(result.successEventPer + '%'); // 申请理赔通过占比
            }
        });

        // 全局理赔概况统计表
        $.getJSON("${ctx}/marketing/globalClaimsAnalysis", function(datas){
            if(datas.code == "200"){
                var result = datas.data;
                // 统计的月份
                var chart1_labels = result.month;
                // 理赔率
                var chart1_data1 = result.eventPer;
                // 保障中车辆
                var chart1_data2 = result.guaranteeNum;
                // 理赔车辆
                var chart1_data3 = result.eventNum;

                // 设置第一个统计图表的缩放起止范围
                var chart1_data_num = 6;  // 一屏显示的数据条数
                var chart1_start = null;
                var chart1_end = null;
                if (chart1_labels.length > chart1_data_num) {
                    chart1_start = chart1_labels.length - chart1_data_num;
                    chart1_end = chart1_labels.length - 1;
                }

                // 渲染第一个统计图表(全局理赔概况)
                chart1 = echarts.init($('.chart1 .chart-content')[0]);
                chart1.setOption({
                    grid: {
                        left: 20,
                        right: 20,
                        top: 50,
                        bottom: 60
                    },
                    tooltip: {
                        show: true
                    },
                    dataZoom: [{
                        id: 'dataZoomX',
                        xAxisIndex: 0,
                        filterMode: 'filter',
                        startValue: chart1_start,
                        endValue: chart1_end
                    }],
                    xAxis: {
                        data: chart1_labels,
                        axisLine: {
                            lineStyle: {
                                type: 'dashed',
                                color: '#b5b5b5'
                            }
                        },
                        axisLabel: {
                            color: '#434141'
                        },
                        axisTick: {
                            show: false
                        }
                    },
                    yAxis: [{
                        show: false
                    }, {
                        splitLine: {
                            lineStyle: {
                                color: '#d7d5d5',
                                type: 'dashed'
                            }
                        },
                        axisLine: {
                            show: false
                        },
                        axisTick: {
                            show: false
                        },
                        axisLabel: {
                            show: false
                        }
                    }],
                    series: [{
                        name: '理赔率',
                        type: 'line',
                        data: chart1_data1,
                        itemStyle: {
                            color: '#fdab22'
                        },
                        label: {
                            show: true,
                            fontSize: 14,
                            formatter: '{c}%'
                        },
                        tooltip: {
                            formatter: '{b}{a}:{c}%'
                        },
                        z: 11
                    }, {
                        name: '保障中车辆',
                        type: 'bar',
                        data: chart1_data2,
                        barWidth: '30%',
                        barGap: '-100%',
                        itemStyle: {
                            color: '#eef3f9'
                        },
                        label: {
                            show: true,
                            color: '#434141',
                            fontSize: 14,
                            position: 'top',
                            formatter: '{c}辆'
                        },
                        tooltip: {
                            formatter: '{b}{a}:{c}辆'
                        },
                        emphasis: {
                            itemStyle: {
                                color: '#cfd6de'
                            }
                        },
                        yAxisIndex: 1
                    }, {
                        name: '理赔车辆',
                        type: 'bar',
                        data: chart1_data3,
                        barWidth: '30%',
                        itemStyle: {
                            color: '#5799f6'
                        },
                        label: {
                            show: true,
                            fontSize: 14,
                            position: 'top',
                            formatter: '{c}辆'
                        },
                        tooltip: {
                            formatter: '{b}{a}:{c}辆'
                        },
                        z: 10,
                        yAxisIndex: 1
                    }]
                });
            }
        });


        // 受损部位统计图
        $.getJSON("${ctx}/marketing/getDamagePosition", function(datas){
            if(datas.code == "200"){
                // 渲染第二个统计图表(受损部位)
                var chart2_data = datas.data;
                chart2 = echarts.init($('.chart2 .chart-content')[0]);
                chart2.setOption({
                    color: [ '#8de1f4'],
                    tooltip: {
                        formatter: '{b}:{c}起 {d}%'
                    },
                    series: [{
                        name: '受损部位',
                        type: 'pie',
                        data: chart2_data,
                        minAngle: 15,
                        radius: ['40%', '90%'],
                        itemStyle: {
                            borderWidth: 3,
                            borderColor: '#fff'
                        },
                        label: {
                            show: false,
                            position: 'outside',
                            fontSize: 14,
                            color: '#434141'
                            // formatter: '{b} {c}起'
                        }
                    }]

                });
            }

        });

        // 渲染第三个统计图表(理赔金额)
        $.getJSON("${ctx}/marketing/eventAmt", function(datas){
            if(datas.code == "200"){
                // 理赔金额统计图
                var chart3_data = datas.data;
                chart3 = echarts.init($('.chart3 .chart-content')[0]);
                chart3.setOption({
                    color: ['#8de1f4'],
                    tooltip: {
                        formatter: '{b}:{c}笔 {d}%'
                    },
                    series: [{
                        name: '理赔金额',
                        type: 'pie',
                        data: chart3_data,
                        minAngle: 15,
                        radius: ['40%', '90%'],
                        itemStyle: {
                            borderWidth: 3,
                            borderColor: '#fff'
                        },
                        label: {
                            show: false,
                            position: 'inside',
                            fontSize: 14,
                            color: '#434141',
                            formatter: '{b} {c}笔'
                        }
                    }]
                });
            }
        });

        // 渲染第四个统计图表(虚拟补贴)
        $.getJSON("${ctx}/marketing/rechargeNum", function(datas){
            if(datas.code == "200") {
                // 虚拟补贴统计图
                var chart4_data = datas.data;
                chart4 = echarts.init($('.chart4 .chart-content')[0]);
                chart4.setOption({
                    // color: ['#1ad1fa', '#65d6f0', '#8de1f4', '#7de7fe'],
                    color: ['#16d4cd', '#fba801', '#fec904', '#7f7ce8', '#21b8ea'],
                    tooltip: {
                        formatter: '{b}:{c}笔 {d}%'
                    },
                    series: [{
                        name: '虚拟补贴',
                        type: 'pie',
                        data: chart4_data,
                        minAngle: 15,
                        radius: ['40%', '90%'],
                        itemStyle: {
                            borderWidth: 3,
                            borderColor: '#fff'
                        },
                        label: {
                            show: false,
                            position: 'inside',
                            fontSize: 14,
                            color: '#434141',
                            formatter: '{b} {c}笔'
                        }
                    }]
                });
            }
        });

        // 服务商结算排行榜表格
        var strJson = {"pageNo":1,"pageSize":10};
        $.ajax({
            type: "post",
            dataType: "json",
            contentType: "application/json;charset=utf-8",
            url: "${ctx}/marketing/maintenanceshopAmt",
            data:JSON.stringify(strJson),
            success:function(datas){
                var chart4_data = datas.data.list;
                // console.log(chart4_data);
                var table3_titles_per = [10, 30, 15, 15, 15, 15];
                var table3_titles = ['排名', '昵称/名称', '待结算金额', '可用金额', '总金额', '已提现金'];
                var table3_data =  new Array();
                $.each(chart4_data,function(i,v){
                    table3_data[i] =  [i+1,v.shopName,v.amtFreeze + ' 元',v.amtUnfreeze + ' 元',v.amtTotal + ' 元',v.amtPaid + ' 元'];
                });
                $('.table3').html(createTable(table3_titles_per, table3_titles, table3_data));
            }
        });
    });

    // *************************************************************************** //

    //----------------------------------------------------------------------------------------------------------------------------
    // 虚拟补贴排行榜表格
    var table2_titles_per = [5, 30, 15, 15, 15, 20];
    var table2_titles = ['排名', '渠道', '补贴金额', '占比', '理赔金额', '理赔率(理赔金额/补贴+充值)'];
    var table2_data = [];


    // 充值统计图
    var chart5_data = [{name: '9元', value: 245}, {name: '99元', value: 129}];
    // 复购统计图
    var chart6_labels = [];
    var chart6_data1 = [];  // 用户数
    var chart6_data2 = [];  // 复购次数
    /* var chart6_sum = ;  // 复购次数 */
    //----------------------------------------------------------------------------------------------------------------------------
    // 构造表格的方法
    var createTable = function(titles_per, titles, data) {
        var html = '';
        for (var i = 0, j = titles.length; i < j; i++) {
            if (i === 0) {
                html += '<tr class="user-table-title">';
            }
            html += '<th' + (titles_per[i] ? ' width="' + titles_per[i]  + '%"' : '') + '>' + titles[i] + '</th>';
            if (i + 1 === j) {
                html += '</tr>';
            }
        }
        for (var i = 0, j = data.length; i < j; i++) {
            var item = data[i];
            html += '<tr>';
            for (var m = 0, n = item.length; m < n; m++) {
                html += '<td>' + item[m] + '</td>';
            }
            html += '</tr>';
        }
        return html;
    }

    // 渲染第五个统计图表(充值概况)
    var chart5 = echarts.init($('.chart5 .chart-content')[0]);
    chart5.setOption({
        title: {
            text: '充值',
            padding: [10, 5, 5, 20],
            textStyle: {
                fontSize: 16
            }
        },
        color: ['#8b85e0', '#feca03'],
        tooltip: {
            formatter: '{b}元充值:{c}笔 {d}%'
        },
        series: [{
            name: '充值',
            type: 'pie',
            data: chart5_data,
            minAngle: 15,
            itemStyle: {
                borderWidth: 3,
                borderColor: '#fff'
            },
            label: {
                show: true,
                position: 'inside',
                fontSize: 14,
                color: '#434141',
                formatter: ' {b} '
            }
        }]
    });

    // 渲染第六个统计图表(复购率)
    var chart6 = echarts.init($('.chart6 .chart-content')[0]);
    chart6.setOption({
        title: {
            text: '复购情况',
            padding: [10, 5, 5, 20],
            textStyle: {
                fontSize: 16
            }
        },
        legend: {
            show: true,
            left: 'center',
            top: 10
        },
        tooltip: {
            formatter: '{b}{a}:{c}'
        },
        grid: {
            left: 20,
            right: 20,
            top: 50,
            bottom: 60
        },
        xAxis: {
            data: chart6_labels,
            axisLine: {
                lineStyle: {
                    type: 'dashed',
                    color: '#b5b5b5'
                }
            },
            axisLabel: {
                color: '#434141'
            },
            axisTick: {
                show: false
            }
        },
        yAxis: {
            splitLine: {
                lineStyle: {
                    color: '#d7d5d5',
                    type: 'dashed'
                }
            },
            axisLine: {
                show: false
            },
            axisTick: {
                show: false
            },
            axisLabel: {
                show: false
            }
        },
        series: [{
            name: '用户数',
            type: 'bar',
            data: chart6_data1,
            barWidth: '25%',
            itemStyle: {
                color: '#90bbf9'
            },
            label: {
                show: true,
                fontSize: 14,
                position: 'top',
                formatter: '{c}'
            }
        },
            {
                name: '复购次数',
                type: 'bar',
                data: chart6_data2,
                barWidth: '25%',
                itemStyle: {
                    color: '#feca03'
                },
                label: {
                    show: true,
                    fontSize: 14,
                    position: 'top',
                    formatter: '{c}'
                }
            }]
    });

    // 给所有统计图表添加自适应性
    window.addEventListener("resize", function() {
        this.chart1.resize();
        this.chart2.resize();
        this.chart3.resize();
        this.chart4.resize();
        this.chart5.resize();
        this.chart6.resize();
    });

    // 渲染部分数据
    /* for (var i = 1; i < 8; i++) {
      $('.base-num.num' + i).text(1000);  // 顶部7个数字
    } */


    // 切换拉新统计的对象（用户/商家/渠道）
    $('.user-count').on('click', 'a', function(){
        $(this).addClass('active').siblings().removeClass('active');
        tabType = $(this).data('value');
        //-----ajax请求数据后重新对table1_data赋值-----
        //table1_data = [...];
        //$('.table1').html(createTable(table1_titles_per, table1_titles, table1_data));
    })

    // 切换日期
    $('.datebox-btn').on('click', 'a', function(){
        var $this = $(this);
        var name = $this.text();
        $this.closest('.datebox-btn').find('.btn-title').text(name);
    })

    //获取点前时间的后一天
    function getNextDay() {
        var date = new Date();
        var nextDay = new Date(date.getTime() + 24*60*60*1000);
        return nextDay;
    }

    // 获取本周的其实日期
    // AddWeekCount为0代表当前周   为-1代表上一个周   为1代表下一个周以此类推
    function getWeekStartAndEnd(AddWeekCount) {
        var startStop = new Array();
        var millisecond = 1000 * 60 * 60 * 24;
        var currentDate = new Date();
        currentDate = new Date(currentDate.getTime()
            + (millisecond * 7 * AddWeekCount));
        var week = currentDate.getDay();
        var month = currentDate.getDate();
        var minusDay = week != 0 ? week - 1 : 6;
        var currentWeekFirstDay = new Date(currentDate.getTime()
            - (millisecond * minusDay));
        var currentWeekLastDay = new Date(currentWeekFirstDay.getTime()
            + (millisecond * 6));
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
        year = "" + now.getFullYear();
        if ((now.getMonth() + 1) < 10) {
            month = "0" + (now.getMonth() + 1);
        } else {
            month = "" + (now.getMonth() + 1);
        }
        if ((now.getDate()) < 10) {
            day = "0" + (now.getDate());
        } else {
            day = "" + (now.getDate());
        }
        return year + "-" + month + "-" + day;
    }

    /**
     * 获得相对当月AddMonthCount个月的起止日期
     * AddMonthCount为0 代表当月 为-1代表上一个月  为1代表下一个月 以此类推
     * ***/
    function getMonthStartAndEnd(AddMonthCount) {
        var startStop = new Array();
        var currentDate = new Date();
        var month = currentDate.getMonth() + AddMonthCount;
        if (month < 0) {
            var n = parseInt((-month) / 12);
            month += n * 12;
            currentDate.setFullYear(currentDate.getFullYear() - n);
        }
        currentDate = new Date(currentDate.setMonth(month));
        var currentMonth = currentDate.getMonth();
        var currentYear = currentDate.getFullYear();
        var currentMonthFirstDay = new Date(currentYear, currentMonth, 1);
        var currentMonthLastDay = new Date(currentYear, currentMonth + 1, 0);
        startStop.push(getDateStr3(currentMonthFirstDay));
        startStop.push(getDateStr3(currentMonthLastDay));
        return startStop;
    }
</script>
</body>
</html>