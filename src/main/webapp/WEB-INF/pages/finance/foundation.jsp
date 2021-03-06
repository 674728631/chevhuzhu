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
                <li>互助金管理</li>
            </ul>
        </div>
       
        <div class="row" >
            <div class="col-lg-12 col-xs-12">
                <div class="index-wrap row" style="margin-top:60px">
                	<p class="pull-left" style="line-height:34px;">起止时间</p>
                	<input name="beginTime" id="beginTime" value="" class="form-control pull-left" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd',maxDate:'#F{$dp.$D(\'endTime\')||\'%y-%M-%d\'}'});" placeholder="选择开始时间" style="width:20%;margin-left:6px;">
                	<p class="pull-left" style="line-height:34px;margin-left:6px;">-</p>
                	<input name="endTime" id="endTime" value="" class="form-control pull-left" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd',minDate:'#F{$dp.$D(\'beginTime\')||\'%y-%M-%d\'}'});" placeholder="选择结束时间" style="width:20%;margin-left:6px;">
               		<button type="button" class="btn def-btn btn-success pull-left" onclick="search()" style="margin-left:12px;">搜索</button>
               		<button type="button" class="btn def-btn btn-success pull-left" onclick="reset()" style="margin-left:15px;">清空</button>
                    <button type="button" class="btn def-btn btn-info" onclick="exportData()" style="margin-left: 15px;">导出</button>
                 </div>
                    <!-- Nav tabs -->
           <div class="index-wrap row" style="margin-top:-40px">
             <ul class="col-lg-12 col-sm-12">
                <li><p>充值用户</p><span id="carNum"></span></li>
                <li><p>充值笔数</p><span id="payNum"></span></li>
                <li onclick="changeAmt()"><p>充值金额</p><span id="amtTotal"></span></li>
                <li ><p>理赔支出</p><span id="amtPaid"></span></li>
                <li ><p>实际支出</p><span id="realPaid"></span></li>
                <li ><p>余额</p><span id="amtBalance"></span></li>
                <li ><p>理赔率</p><span id="eventRadio"></span></li>
                <li ><p>虚拟补贴</p><span id="allowance"></span></li>
                <li ><p>本月实际支出</p><span id="currentMonthRealPaid"></span></li>
            </ul>
          </div>
          <div class="index-wrap row" style="margin-top:-20px">
		    <ul  style="margin: 2px;">
		        <li><p>互助金总额</p><span id="rechargeAmt"></span></li>
		        <li><p>总笔数</p><span id="rechargeNum"></span></li>
		        <li><p>1.2元充值笔数</p><span id="1amtTotal"></span></li>
		        <li><p>3元充值笔数</p><span id="3amtTotal"></span></li>
		        <li><p>9元充值笔数</p><span id="9amtTotal"></span></li>
		        <li><p>99元充值笔数</p><span id="99amtTotal"></span></li>
		        <li><p>救助支付笔数</p><span id="rescueTotal"></span></li>
		     </ul>
		  </div> 
              <ul class="nav nav-tabs nav-bac" role="tablist">
                 <li role="presentation" class="active"><a href="#home_1" aria-controls="home_1" role="tab" data-toggle="tab" onclick="customerRecord(1,'1,2,3,10')" >用户充值</a></li>
                 <li role="presentation"><a href="#home_2" aria-controls="home_2" role="tab" data-toggle="tab" onclick="customerRecord(1,'4')">系统充值</a></li>
                 <li role="presentation"><a href="#home_3" aria-controls="home_3" role="tab" data-toggle="tab" onclick="expensesRecord(1)">支出明细</a></li>
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
<!--修改互助余额弹窗-->
<div class="update-lay-box" id="addLay">
    <form class="form-horizontal lay-form-update">
        <div class="form-group">
            <label class="col-sm-4 control-label">类目：</label>
            <div class="col-sm-8">
                <input type="text" class="form-control in_1" placeholder="" maxlength="100" onkeyup="value=value.replace(/\s/g,'')">
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-4 control-label">金额：</label>
            <div class="col-sm-8">
                <input type="text" class="form-control in_2" placeholder="" maxlength="100" onkeyup="value=value.replace(/\s/g,'')">
            </div>
        </div>
    </form>
</div>

<!--导出弹窗-->
<div class="time-layer" style="display: none;">
    <form class="form-horizontal margin-top-ele lay-form-time" action="${ctx}/excel/recharge" method="post" id="excelExport" style="padding-top:4px;margin-top:4px;"></form>
</div>
<script type="text/javascript" src="${ctx}/cite/plugins/MyDatePicker/WdatePicker.js"></script>
<script type="text/javascript">
    var pageSize=10;
    var beginTime="${beginTime}";
    var endTime="${endTime}";
	var choice = 1;
    foundationData();
    //加载互助总额数据
    function foundationData(){
        var strJson = {};
        $.ajax({
            type : "post",
            dataType : "json",
            headers: {rqSide: ex.pc()},
            data : JSON.stringify(strJson),
            contentType : "application/json;charset=utf-8",
            url: "${ctx}/foundation/foundationData",
            success : function(datas){
                if(datas.code == "0"){
                    var repData = datas.data;
                    $("#carNum").html(ex.judgeEmptyOr0(repData.carNum));
                    $("#payNum").html(ex.judgeEmptyOr0(repData.payNum));
                    $("#amtBalance").html(ex.judgeEmptyOr0(repData.amtBalance));
                    $("#amtTotal").html(ex.judgeEmptyOr0(repData.amtTotal));
                    $("#amtPaid").html(ex.judgeEmptyOr0(repData.amtPaid));
                    $("#realPaid").html(ex.judgeEmptyOr0(repData.realPaid));
                    $("#eventRadio").html(ex.judgeEmptyOr0(repData.eventRadio)+"%");
                    $("#allowance").html(ex.judgeEmptyOr0(repData.allowance));
                    $("#currentMonthRealPaid").html(ex.judgeEmptyOr0(repData.currentMonthRealPaid));
                }
            }
        });
    }
    
    //加载统计数据
    function rechargeData(beginTime,endTime){
    	var strJson = {"beginTime":beginTime,"endTime":endTime};
    	$.ajax({    		
    		type :"post",
    		dataType : "json",
            headers: {rqSide: ex.pc()},
            data : JSON.stringify(strJson),
            contentType : "application/json;charset=utf-8",
            url : "${ctx}/foundation/rechargeDate",
            success : function(datas){
            	if(datas.code == "0"){
            		var repData = datas.data;
            		$("#rechargeAmt").html(ex.judgeEmptyOr0(repData.rechargeAmt));
            		$("#rechargeNum").html(ex.judgeEmptyOr0(repData.rechargeNum));
            		$("#1amtTotal").html(ex.judgeEmptyOr0(repData.amt1Total));
            		$("#3amtTotal").html(ex.judgeEmptyOr0(repData.amt3Total));
            		$("#9amtTotal").html(ex.judgeEmptyOr0(repData.amt9Total));
            		$("#99amtTotal").html(ex.judgeEmptyOr0(repData.amt99Total));
            		$("#rescueTotal").html(ex.judgeEmptyOr0(repData.rescueTotal));            		
            	}
            }
    	});
    }
   //查询
   function search(){
	  if(choice == 1){
		  customerRecord(1,'1,2,3,10');
	  }else if(choice == 2){
		  customerRecord(1,'4');
	  }else{
		  expensesRecord(1);
	  }
   }
   //清空查询条件   
   function reset(){
   	var beginTime = $("#beginTime").val("");
	var endTime = $("#endTime").val("");
	  if(choice == 1){
		  customerRecord(1,'1,2,3,10');
	  }else if(choice == 2){
		  customerRecord(1,'4');
	  }else{
		  expensesRecord(1);
	  }
   }

    customerRecord(1,'1,2,3,10')
    //加载用户充值数据
    function customerRecord(pageNo,type){
    	if(type == 4){
    		choice = 2;
    	}else{
    		choice = 1;
    	}
    	console.log(choice);
    	var beginTime = $("#beginTime").val();
    	var endTime = $("#endTime").val();
    	if(beginTime && endTime){
           endTime = ex.getDateString(ex.plusDateOneDay(new Date(endTime)));
        }
        var strJson = {"pageNo":pageNo,"pageSize":pageSize,"type":type,"beginTime":beginTime,"endTime":endTime};
        rechargeData(beginTime,endTime);
        $.ajax({
            type : "post",
            dataType : "json",
            headers: {rqSide: ex.pc()},
            data : JSON.stringify(strJson),
            contentType : "application/json;charset=utf-8",
            url: "${ctx}/foundation/customerRecord",
            success : function(datas){
                if(datas.code == "0"){
                    var repData = datas.data.list;
                    var dataHtml = "<thead><tr><th>充值描述</th><th>车牌号</th><th>渠道</th><th>充值金额</th><th>充值方式</th><th>充值时间</th></tr></thead>";
                    dataHtml += "<tbody>";
                    for(var i=0;i < repData.length;i++){
                        var repDateChild = repData[i];
                        var timeRecharge = ex.judgeEmpty(repDateChild.timeRecharge)?ex.reTime("s",repDateChild.timeRecharge.time):"";
                        dataHtml += "<tr>" +
                            "<td>" + ex.judgeEmpty(repDateChild.description) + "</td>" +
                            "<td>" + ex.judgeEmpty(repDateChild.licensePlateNumber) + "</td>" +
                            "<td>" + ex.judgeEmpty(repDateChild.shopName) + "</td>" +
                            "<td>" + ex.judgeEmptyOr0(repDateChild.amt) + "</td>" +
                            "<td>" + ex.rechargeType(repDateChild.type) + "</td>" +
                            "<td>" + timeRecharge + "</td>" +
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

    //加载支出明细数据
    function expensesRecord(pageNo){
    	choice = 3;
    	console.log(choice);
    	var beginTime = $("#beginTime").val();
    	var endTime = $("#endTime").val();
    	if(beginTime && endTime){
            endTime = ex.getDateString(ex.plusDateOneDay(new Date(endTime)));
         }
        var strJson = {"pageNo":pageNo,"pageSize":pageSize,"beginTime":beginTime,"endTime":endTime};
        $.ajax({
            type : "post",
            dataType : "json",
            headers: {rqSide: ex.pc()},
            data : JSON.stringify(strJson),
            contentType : "application/json;charset=utf-8",
            url: "${ctx}/foundation/expensesRecord",
            success : function(datas){
                if(datas.code == "0"){
                    var repData = datas.data.list;
                    var dataHtml = "<thead><tr><th>支出描述</th><th>支出金额</th><th>支出时间</th></tr></thead>";
                    dataHtml += "<tbody>";
                    for(var i=0;i < repData.length;i++){
                        var repDateChild = repData[i];
                        var createAt = ex.judgeEmpty(repDateChild.createAt)?ex.reTime("s",repDateChild.createAt.time):"";
                        dataHtml += "<tr>" +
                            "<td>" + ex.judgeEmpty(repDateChild.description) + "</td>" +
                            "<td>" + ex.judgeEmptyOr0(repDateChild.amtCooperation) + "</td>" +
                            "<td>" + createAt + "</td>" +
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
                                expensesRecord(n);
                                window.location.href="#top";
                            }
                        });
                        kkpager.generPageHtml();
                    }
                }
            }
        });
    }

    function changeAmt() {
        layer.open({
            title:'修改充值金额',
            closeBtn:'1',
            type:1,
            btn:['确定','取消'],
            anim: 5,
            shadeClose: false, //点击遮罩关闭
            content: $('#addLay'),
            yes:function(index, layero){
                var category = $(".in_1").val();
                var amt = $(".in_2").val();
                if (!(category&&amt)) {
                    alert("请填写所有选项");
                    return;
                }
                var strJson = {"category":category,"amt":amt};
                $.ajax({
                    type : "post",
                    dataType : "json",
                    headers: {rqSide: ex.pc()},
                    data : JSON.stringify(strJson),
                    contentType : "application/json;charset=utf-8",
                    url: "${ctx}/foundation/updateAmt",
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

    function exportData() {
    	  var type="${choice}";
          var beginTime = $("#beginTime").val();
          var endTime = $("#endTime").val();
          if(beginTime && endTime){
              endTime = ex.getDateString(ex.plusDateOneDay(new Date(endTime)));
          }
          $("#excelExport").empty().append("<input name='choice' value="+ choice +">");
          $("#excelExport").append("<input name='beginTime' value="+ beginTime +">");
          $("#excelExport").append("<input name='endTime' value="+ endTime +">");
        $("#excelExport").submit();
    }
    
    function getBeginTime(){
    	var time = $(this).val();
    	$("")
    }
</script>
</body>
</html>