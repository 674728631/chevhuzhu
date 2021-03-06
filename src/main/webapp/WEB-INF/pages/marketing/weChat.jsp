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
    <link rel="stylesheet" href="${ctx}/cite/css/index_1.css" type="text/css">
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
        <c:choose>
            <c:when test="${userInfo.hasMainHtml}">
                <div class="position-row">
                    <ul class="breadcrumb index-bread">
                        <li><a href="${ctx}/main.html"><i class="fa fa-dashboard"></i> 首页</a></li>
                    </ul>
                </div>
                <!--待处理订单-->
                <%-- <div class="row margin-105">
                    <div class="col-lg-12 order-box">
                        <h3 class="border-bot">待处理订单</h3>
                        <ul class="order-deal max-wid">
                            <li>
                                <a href="${ctx}/event/list.html?flag=1&isInvalid=1&status=1">
                                    <strong>申请理赔</strong>
                                    <p id="applyNum"></p>
                                </a>
                            </li>
                            <li>
                                <a href="${ctx}/event/list.html?flag=4&isInvalid=1&status=10">
                                    <strong>待分单</strong>
                                    <p id="distributionNum"></p>
                                </a>
                            </li>
                            <li>
                                <a href="${ctx}/event/list.html?flag=8&isInvalid=1&status=22">
                                    <strong>定损待审核</strong>
                                    <p id="assertNum"></p>
                                </a>
                            </li>
                            <li>
                                <a href="${ctx}/event/list.html?flag=14&isInvalid=1&status=81">
                                    <strong>投诉中</strong>
                                    <p id="complaintNum"></p>
                                </a>
                            </li>
                        </ul>
                    </div>
                </div> --%>
                <!--数据统计-->
                <div class="row" style="margin-top: 100px;">
                    <div class="col-lg-12 data-total clear-padding">
                        <h3 class="">数据统计</h3>
                        <div class="data-tab border-bot" style="height:auto;overflow:hidden;">
                            <ul class="data-tab-btn">
                                <li><button id="tag1" class="" onclick="chart('register','人')">注册用户数</button></li>
                                <li><button id="tag2" class="" onclick="chart('view','次')">微信访问数</button></li>
                                <%--<li><button onclick="chart('guarantee','辆')">保障中车辆</button></li>--%>
                                <li><button id="tag3" onclick="chart('shop','家')">商家数</button></li>
                                <li><button id="tag4" onclick="chart('channel','家')">其他渠道数</button></li>
                                <li><button id="tag5" onclick="chart('event','单')">申请理赔</button></li>
                                <li><button id="tag6" onclick="chart('foundation','元')">互助金</button></li>
                                <li><button id="tag7" onclick="chart('initNum','辆')">待支付</button></li>
                                <li><button id="tag8" onclick="chart('observationNum','辆')">观察期</button></li>
                                <li><button id="tag9" onclick="chart('guaranteeNum','辆')">保障中</button></li>
                                <li><button id="tag10" onclick="chart('outNum','辆')">退出</button></li>
                                <li><button id="tag11" onclick="chart('twiceRecharge','次')">二次充值</button></li>
                            </ul>
                            <ul class="data-tab-line">
                                <li onclick="timeLay()">自定义</li>
                                <li onclick="monthDate()">本月</li>
                                <li class="add-tab-sty" onclick="weekDate()">本周</li>
                            </ul>
                        </div>
                        <!--折线图-->
                        <div class="echarts-des">
                            <p>时间：<span id="beginTime"></span> —— <span id="endTime"></span></p>
                            <p>总计：<span id="countChart"></span></p>
                        </div>
                        <div id="highEchart" style="width:100%;height: 200px;margin-top: 60px;"></div>
                    </div>
                </div>
            </c:when>
            <c:otherwise>
                欢迎进入车V互助后台
            </c:otherwise>
        </c:choose>
    </div>
    <!--时间弹窗-->
    <div class="time-layer">
        <form class="form-horizontal margin-top-ele lay-form-time" style="padding-top:4px;margin-top:4px;">
            <div class="form-group">
                <label class="col-sm-4 control-label">开始时间：</label>
                <div class="col-sm-7"><input id="begin" value="" class="form-control" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd'});" ></div>
            </div>
            <div class="form-group">
                <label class="col-sm-4 control-label">结束时间：</label>
                <div class="col-sm-7"><input id="end" value="" class="form-control" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd'});" ></div>
            </div>
        </form>
    </div>
</div>
<script src="${ctx}/cite/js/extends/highcharts.js"></script>
<script type="text/javascript" src="${ctx}/cite/plugins/MyDatePicker/WdatePicker.js"></script>
<script type="text/javascript">
	var tabType = decodeURIComponent(window.location.search).slice(1, decodeURIComponent(window.location.search).length);
    var dt = new Date();
    // 本周
   /*  var benzhou_start = ex.getDateString(new Date(dt.getFullYear(), dt.getMonth(), dt.getDate() - dt.getDay() + 1));
    var benzhou_end = ex.getDateString(new Date(dt.getFullYear(), dt.getMonth(), dt.getDate() - dt.getDay() + 7));  */
   
  	var date = new Date();
    var today = date.getDay(); 
    var stepSunDay = -today + 1;
    var stepMonday = 7 - today; 
    if (today == 0) {  
        stepSunDay = -6;
        stepMonday = 0;
    }
    var time = date.getTime();
    var monday = new Date(time + stepSunDay * 24 * 3600 * 1000);
    var sunday = new Date(time + stepMonday * 24 * 3600 * 1000);
    var benzhou_start = ex.getDateString(new Date(monday.getFullYear(), monday.getMonth(), monday.getDate()));
    var benzhou_end = ex.getDateString(new Date(sunday.getFullYear(), sunday.getMonth(), sunday.getDate()));
    
    // 本月
    var benyue_start = ex.getDateString(new Date(dt.getFullYear(), dt.getMonth(), 1));
    var benyue_end = ex.getDateString(new Date(dt.getFullYear(), dt.getMonth() + 1, 0));

    //加载订单统计
    $.ajax({
        type : "post",
        dataType : "json",
        headers: {rqSide: ex.pc()},
        data : JSON.stringify({}),
        contentType : "application/json;charset=utf-8",
        url: "${ctx}/event/count",
        success : function(datas){
            if(datas.code == "0"){
                var result = datas.data;
                $("#applyNum").html(result.applyNum);
                $("#distributionNum").html(result.distributionNum);
                $("#assertNum").html(result.assertNum);
                $("#complaintNum").html(result.complaintNum);
            }
        }
    });

    //加载车辆统计
    $.ajax({
        type : "post",
        dataType : "json",
        headers: {rqSide: ex.pc()},
        data : JSON.stringify({}),
        contentType : "application/json;charset=utf-8",
        url: "${ctx}/car/count",
        success : function(datas){
            if(datas.code == "0"){
                var result = datas.data;
                $("#examineNum").html(result.examineNum);
            }
        }
    });

    //第一次进来加载本周注册人数
    $("#beginTime").html(benzhou_start);
    $("#endTime").html(benzhou_end);
    //chart('register','人');

    if(tabType == 1){ //访问
    	console.log(tabType);
    	chart('view','次');   
    	document.getElementById('tag2').classList.add('blue-bag');
    }else if(tabType == 2){ //注册
    	console.log(tabType);
    	chart('register','人')
    	document.getElementById('tag1').classList.add('blue-bag');
    }else if(tabType == 3){ //观察期
    	console.log(tabType);
    	chart('observationNum','辆');
    	document.getElementById('tag8').classList.add('blue-bag');
    }else if(tabType == 4){ //保障中
    	console.log(tabType);
    	chart('guaranteeNum','辆');
    	document.getElementById('tag9').classList.add('blue-bag');
    }
    
    function chart(chartType,unit){
        var beginTime = $("#beginTime").html();
        var endTime = ex.getDateString(ex.plusDateOneDay(new Date($("#endTime").html())));
        var jsonStr = {'chartType':chartType,'beginTime':beginTime,'endTime':endTime};
        $.ajax({
            type:"post",
            dataType:"json",
            headers:{rqSide:ex.pc()},
            data:JSON.stringify(jsonStr),
            contentType:"appliaction/json;charset=utf-8",
            url:"${ctx}/chart/count",
            success:function(datas){
                if(datas.code == "0"){
                    var repData = datas.data;
                    var xArr = repData.datelist;
                    var yArr = repData.numlist;
                    $('#highEchart').highcharts({
                        chart: {type: 'area'},
                        title: {text: ''},
                        credits: {enabled: false},
                        xAxis: {allowDecimals: false, labels: {formatter: function () {return xArr[this.value];}}},
                        yAxis: {title: {text: ''}, labels: {formatter: function () {return this.value;}}},
                        tooltip: {formatter: function () {return xArr[this.x] + '<br/>共计' + yArr[this.x] + unit;}},
                        plotOptions: {area:{marker:{enabled:false,symbol:'circle',radius: 2,states:{hover:{enabled:true}}}}},
                        legend: {enabled: false},
                        series: [{data: yArr}]
                    });
                    $("#countChart").html(repData.countNumber + unit);
                }
            }
        });
    }

    function weekDate() {
        $("#beginTime").html(benzhou_start);
        $("#endTime").html(benzhou_end);
        $(".blue-bag").click();

    }

    function monthDate() {
        $("#beginTime").html(benyue_start);
        $("#endTime").html(benyue_end);
        $(".blue-bag").click();
    }

    //时间弹窗
    function timeLay(){
        layer.open({
            type: 1
            ,title: '时间选择'
            ,closeBtn: 1
            ,area: '360px;'
            ,shade: 0.5
            ,btn: ['确定','取消']
            ,btnAlign: 'r'
            ,moveType: 1 //拖拽模式，0或者1
            ,content: $(".time-layer")
            ,yes: function(index, layero){
                $("#beginTime").html($("#begin").val());
                $("#endTime").html($("#end").val());
                $(".blue-bag").click();
                layer.close(index); //如果设定了yes回调，需进行手工关闭
            }
        });
    }

    /*data-tab-line 切换样式*/
    $(".data-tab-line li").click(function () {
        $(this).addClass("add-tab-sty").siblings().removeClass("add-tab-sty");
    });
    $(".data-tab-btn li button").click(function () {
        $(this).addClass("blue-bag").parent("li").siblings().children().removeClass("blue-bag");
    });
</script>
</body>
</html>