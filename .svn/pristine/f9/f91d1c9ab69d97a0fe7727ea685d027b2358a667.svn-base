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
                <li>营销</li>
            </ul>
        </div>

        <div class="row margin-t-60 new-ad-box">
            <div class="col-xs-12 col-md-12 tk-mav-tabs">
                <!-- Nav tabs -->
                <ul class="nav nav-tabs" role="tablist">
                    <li role="presentation" class="active"><a href="#home_1" aria-controls="home_1" role="tab" data-toggle="tab">新增奖励活动</a></li>
                </ul>
                <!-- Tab panes -->
                <div class="tab-content">
                    <div role="tabpanel" class="tab-pane active" id="home_1">
                        <table class="table table-bordered add-award-table">
                            <tbody>
                            <tr>
                                <td class="font-we">*活动名称：</td>
                                <td><input id="name" type="text" class="form-control" onkeyup="value=value.replace(/\s/g,'')"></td>
                                <td class="font-we">*奖励发行数量：</td>
                                <td><input id="num" type="number" class="form-control" onkeyup="value=value.replace(/\s/g,'')"></td>
                                <td class="font-we">*奖励面额：</td>
                                <td><input id="amount" type="text" class="form-control" onkeyup="value=value.replace(/\s/g,'')"></td>
                            </tr>

                            <tr>
                                <td class="font-we">*使用条件：</td>
                                <td><input id="meetPrice" type="text" class="form-control" placeholder="支付金额满多少可以使用" onkeyup="value=value.replace(/\s/g,'')"></td>
                                <td class="font-we">生效时间：</td>
                                <td><input id="beginTime" type="text" class="form-control" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'});"></td>
                                <td class="font-we">过期时间：</td>
                                <td><input id="endTime" type="text" class="form-control" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'});"></td>
                            </tr>
                            <tr>
                                <td class="font-we">*奖励类型：</td>
                                <td style="text-align: left"><input type="radio" class="in_2" placeholder="" name="ac" value="1">充值奖励</td>
                            </tr>
                            <tr>
                                <td class="font-we">*活动说明：</td>
                                <td colspan="3"><textarea id="description" type="text" class="form-control" onkeyup="value=value.replace(/\s/g,'')"></textarea></td>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>

        <!--奖励列表-->
        <div class="row">
            <div class="col-xs-12 col-md-12 tk-mav-tabs">
                <!-- Tab panes -->
                <div class="tab-content jl-tab-content">
                    <div role="tabpanel" class="tab-pane active white-table">
                        <!--搜索选项-->
                        <div class="col-md-3">
                            <div class="form-group has-success has-feedback">
                                <div class="input-group">
                                    <span class="input-group-addon">商家名称</span>
                                    <input id="searchInfo" type="text" class="form-control" aria-describedby="inputGroupSuccess1Status">
                                </div>
                            </div>
                        </div>
                        <div class="col-md-3">
                            <div class="form-group has-success has-feedback">
                                <div class="input-group">
                                    <button class="btn def-btn btn-primary" onclick="maintenanceshopListData(1)">搜索</button>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="row">
            <div class="col-lg-12 col-xs-12">
                <div class="panel panel-info">
                    <div class="panel-heading">
                        <h3 class="panel-title">商家列表</h3>
                    </div>
                    <div class="panel-body">
                        <table class="table table-bordered table-hover col-xs-12" id="mainDataTable"></table>
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
<script type="text/javascript" src="${ctx}/cite/plugins/MyDatePicker/WdatePicker.js"></script>
<script type="text/javascript">
    $(document).ready(function (){
        $.ajax({
            type : "post",
            dataType : "json",
            headers: {rqSide: ex.pc()},
            data : JSON.stringify({"modelId":"${modelId}"}),
            contentType : "application/json;charset=utf-8",
            url: "${ctx}/activities/loadModel",
            success : function(datas){
                if(datas.code == "0"){
                    var model = datas.data
                    $("#name").val(model.name);
                    $("#num").val(model.num);
                    $("#amount").val(model.amount);
                    $("#meetPrice").val(model.meetPrice);
                    $("#description").val(model.description);
                    $("input:radio[name='ac'][value="+ model.type +"]").prop("checked",true);
                }else{
                    alertBtn(datas.message);
                }
            },
            error:function (e) {
                alertBtn("请求失败!请检查网络或联系管理员!");
            }
        });
    });

    maintenanceshopListData(1);
    //加载页面数据
    function maintenanceshopListData(pageNo){
        var searchInfo = $("#searchInfo").val();
        var strJson = {"pageNo":pageNo,"pageSize":10,"searchInfo":searchInfo};
        $.ajax({
            type : "post",
            dataType : "json",
            headers: {rqSide: ex.pc()},
            data : JSON.stringify(strJson),
            contentType : "application/json;charset=utf-8",
            url: "${ctx}/maintenanceshop/activityShop",
            success : function(datas){
                if(datas.code == "0"){
                    var repData = datas.data.list;
                    var dataHtml = "<thead><tr><th class='col-xs-1'>商家名称</th><th class='col-xs-1'>联系电话</th><th class='col-xs-1'>地址</th><th class='col-xs-1'>操作</th></tr></thead>";
                    dataHtml += "<tbody>";
                    for(var i=0;i < repData.length;i++){
                        var repDateChild = repData[i];
                        dataHtml += "<tr>" +
                            "<td>" + ex.judgeEmpty(repDateChild.name) + "</td>" +
                            "<td>" + ex.judgeEmpty(repDateChild.tel) + "</td>" +
                            "<td>" + ex.judgeEmpty(repDateChild.address) + "</td>" +
                            "<td>" +
                            "<button type='button' class='btn btn-success' onclick='createCoupon(" + repDateChild.id + ")'>参加活动</button>&nbsp;" +
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
                                maintenanceshopListData(n);
                                window.location.href="#top";
                            }
                        });
                        kkpager.generPageHtml();
                    }
                }
            }
        });
    }

    //创建一次活动
    function createCoupon(shopId) {
        var name = $("#name").val();
        var num = $("#num").val();
        var amount = $("#amount").val();
        var meetPrice = $("#meetPrice").val();
        var type = $("input:radio[name='ac']:checked").val();
        var description = $("#description").val();
        var beginTime = $("#beginTime").val();
        var endTime = $("#endTime").val();
        if (!(name&&type&&amount&&meetPrice&&num&&description)) {
            alertBtn("请正确填写带*选项");
            return;
        }
        if (num<0) {
            alertBtn("奖励发行量必须大于0");
            return;
        }
        var strJson = {"name":name,"type":type,"amount":amount,"meetPrice":meetPrice,"num":num,"description":description,"shopId":shopId,"beginTime":beginTime,"endTime":endTime};
        $.ajax({
            type : "post",
            dataType : "json",
            headers: {rqSide: ex.pc()},
            data : JSON.stringify(strJson),
            contentType : "application/json;charset=utf-8",
            url: "${ctx}/activities/saveCoupon",
            success : function(datas){
                if(datas.code != "0"){
                    alertBtn(datas.message)
                }else {
                    alertBtn("发起活动成功，请前往活动列表查看！")
                }
            }
        });
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