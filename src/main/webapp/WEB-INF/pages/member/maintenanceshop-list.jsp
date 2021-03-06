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
<div id="wrapper" class="container-fluid">
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
                <li><i class="fa fa-dashboard"></i> 成员</li>
                <li>渠道名单</li>
            </ul>
        </div>
        <div class="row margin-row-max">
            <div class="col-sm-4 new-search-btn">
                <input id="searchInfo" type="text" class="form-control" aria-describedby="inputGroupSuccess1Status" onkeyup="value=value.replace(/\s/g,'')" placeholder="请输入名称、手机号进行搜索信息">
                <button type="button" class="btn def-btn btn-success" onclick="search()">搜索</button>
            </div>
            <div class="col-sm-8">
                <button type="button" class="btn btn-info def-btn" onclick="addMaintenanceshop()">添加商家</button>
                <div class="" style="display: inline-block;" id="shopType">
                    <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">新建 <span class="caret"></span></button>
                    <ul class="dropdown-menu dropdown-menu-right">
                        <li ><a href="#" onclick="addShopPage()">商家</a></li>
                        <li ><a href="#" onclick="addChannel()">其他</a></li>
                    </ul>
                </div>
                <div class="" style="display: inline-block;">
                    <button id="searchStatus" type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false" value="-1">状态 <span class="caret"></span></button>
                    <ul class="dropdown-menu dropdown-menu-right">
                        <li><a href="#" onclick="changeSearchStatus(-1,'全部')">全部</a></li>
                        <li><a href="#" onclick="changeSearchStatus(1,'正常')">正常</a></li>
                        <li><a href="#" onclick="changeSearchStatus(2,'冻结')">冻结</a></li>
                    </ul>
                </div>
            </div>
        </div>
        <div class="row">
            <div class="col-lg-12 col-xs-12">
                <div>
                    <ul class="nav nav-tabs nav-bac" role="tablist">
                        <li role="presentation" id="flag2" class="active"><a id="shopNum" href="#home_1" aria-controls="home_1" role="tab" data-toggle="tab" onclick="maintenanceshopListData(1)">商家</a></li>
                        <li role="presentation" id="flag1"><a id="channelNum" href="#home_1" aria-controls="home_1" role="tab" data-toggle="tab" onclick="channelListData(1)">其他</a></li>
                    </ul>
                    <!-- Tab panes -->
                    <div class="tab-content index-tab-content">
                        <div role="tabpanel" class="tab-pane active white-table" id="home_1">
                            <table class="table table-bordered table-hover clear-border" id="mainDataTable"></table>
                            <%-- 数据分页(会显示所有数据所分页数) --%>
                            <div id="blankMessage" style="text-align:center;line-height:160px;font-size:18px;font-weight: bold;"></div>
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

<!--添加商家 弹窗-->
<div class="update-lay-box" id="addShopLay">
    <form class="form-horizontal lay-form-update">
        <div class="form-group">
            <label class="col-sm-4 control-label">商家名称：</label>
            <div class="col-sm-8">
                <input type="text" class="form-control in_1" placeholder="" maxlength="20" onkeyup="value=value.replace(/\s/g,'')">
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-4 control-label">联系人：</label>
            <div class="col-sm-8">
                <input type="text" class="form-control in_2" placeholder="" maxlength="20" onkeyup="value=value.replace(/\s/g,'')">
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-4 control-label">联系电话：</label>
            <div class="col-sm-8">
                <input type="text" class="form-control in_3" placeholder="" maxlength="11">
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-4 control-label">地址：</label>
            <div class="col-sm-8">
                <input type="text" class="form-control in_4" placeholder="" maxlength="100" onkeyup="value=value.replace(/\s/g,'')">
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-4 control-label">收款比率：</label>
            <div class="col-sm-8">
                <input type="text" class="form-control in_5" placeholder="" maxlength="20" onkeyup="value=value.replace(/\s/g,'')">
            </div>
        </div>
    </form>
</div>

<!--修改商家 弹窗-->
<div class="update-lay-box" id="updateLay">
    <form class="form-horizontal lay-form-update">
        <div class="form-group">
            <label class="col-sm-4 control-label">商家名称：</label>
            <div class="col-sm-8">
                <input type="text" class="form-control info_1" placeholder="" maxlength="20" onkeyup="value=value.replace(/\s/g,'')">
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-4 control-label">联系人：</label>
            <div class="col-sm-8">
                <input type="text" class="form-control info_2" placeholder="" maxlength="20">
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-4 control-label">联系电话：</label>
            <div class="col-sm-8">
                <input type="text" class="form-control info_3" placeholder="" maxlength="11">
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-4 control-label">地址：</label>
            <div class="col-sm-8">
                <input type="text" class="form-control info_4" placeholder="" maxlength="100" onkeyup="value=value.replace(/\s/g,'')">
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-4 control-label">收款比率：</label>
            <div class="col-sm-8">
                <input type="text" class="form-control info_5" placeholder="" maxlength="20" onkeyup="value=value.replace(/\s/g,'')">
            </div>
        </div>
    </form>
</div>
<!--添加其他渠道 弹窗-->
<div class="update-lay-box" id="addLay">
    <form class="form-horizontal lay-form-update">
        <div class="form-group">
            <label class="col-sm-4 control-label">渠道名称：</label>
            <div class="col-sm-8">
                <input id="nameNew" type="text" class="form-control" placeholder="" maxlength="50" onkeyup="value=value.replace(/\s/g,'')">
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-4 control-label">手机号：</label>
            <div class="col-sm-8">
                <input id="telNew" type="text" class="form-control" placeholder="" maxlength="11" onkeyup="value=value.replace(/\s/g,'')">
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-4 control-label">地址（选填）：</label>
            <div class="col-sm-8">
                <input id="addressNew" type="text" class="form-control" placeholder="" maxlength="100" onkeyup="value=value.replace(/\s/g,'')">
            </div>
        </div>
    </form>
</div>
<!--查看其他渠道 弹窗-->
<div id="defeated-layer">
    <div class="input-reason">
        <div style="margin-top:6px;overflow: hidden;line-height:32px;">
            <li style="float:left;">渠道名称：<span id="name"></span></li>
            <li style="float:right;"><span id="status"></span></li>
        </div>
        <div style="margin-top:6px;overflow: hidden;">
            <li style="float:left;">地址：<span id="address"></span></li>
            <li style="float:right;">手机号：<span id="tel"></span></li>
        </div>
        <div style="margin-top:6px;overflow: hidden;">
            <li style="float:left;">微信关注数：<span id="attentionNum"></span></li>
            <li style="float:right;">注册数：<span id="registerNum"></span></li>
        </div>
        <img id="qrcode" src='${ctx}/cite/images/no_pic.png' style="height: 150px;width: 150px">
        <li style="margin:6px 0;">新建渠道时间：<span id="createAt"></span></li>
    </div>
</div>
<script type="text/javascript">
    $("#searchInfo").val(decodeURIComponent(window.location.search).slice(1, decodeURIComponent(window.location.search).length));
    var pageSize=10;
    
    //加载固定数据
    if($("#searchInfo").val() == null || $("#searchInfo").val() == ""){
    	maintenanceshopListData(1);
    }

    //全局搜索
    if($("#searchInfo").val() != null && $("#searchInfo").val() != ""){
    	console.log("默认查询");
    	addClass();
    	type = 20;
    	search();
    	
    }
    
    //加载商家数据
    function maintenanceshopListData(pageNo){
    	console.log('test')
        var searchInfo = $("#searchInfo").val();
        var searchStatus = $("#searchStatus").val();
        if(searchStatus == -1){
            searchStatus = "";
        }
        count(searchInfo,searchStatus);
        var strJson = {"pageNo":pageNo,"pageSize":pageSize,"type":"10","searchInfo":searchInfo,"status":searchStatus};
        $.ajax({
            type : "post",
            dataType : "json",
            headers: {rqSide: ex.pc()},
            data : JSON.stringify(strJson),
            contentType : "application/json;charset=utf-8",
            url: "${ctx}/maintenanceshop/list",
            success : function(datas){
                if(datas.code == "0"){
                    var repData = datas.data.list;
                    var dataHtml = "<thead><tr><th class='col-xs-1' style='width:13%'>名称</th><th class='col-xs-1' style='width:6%'>服务分</th><th class='col-xs-1' style='width:6%'>关注数</th><th class='col-xs-1' style='width:6%'>注册数</th><th class='col-xs-1' style='width:6%'>保障数</th><th class='col-xs-1' style='width:6%'>申请理赔数量</th><th class='col-xs-1' style='width:6%'>申请率</th><th class='col-xs-1' style='width:6%'>理赔数量</th><th class='col-xs-1' style='width:6%'>申请通过率</th><th class='col-xs-1' style='width:6%'>理赔支出</th><th class='col-xs-1' style='width:6%'>理赔率</th><th class='col-xs-1' style='width:6%'>状态</th><th class='col-xs-1' style='width:10%'>时间</th><th class='col-xs-4' style='width:8%'>操作</th></tr></thead>";
                    dataHtml += "<tbody>";
                       if (0 == repData.length) {
                        dataHtml += "</tbody>";
                        $("#mainDataTable").html(dataHtml);
                        $("#blankMessage").text("暂无名单信息");
                    } else {
                    for(var i=0;i < repData.length;i++){
                        var repDateChild = repData[i];
                        var shopStatus = ex.judgeEmpty(repDateChild.status)==1?"正常":ex.judgeEmpty(repDateChild.status)==2?"冻结":"未知";
                        var timeJoin = ex.judgeEmpty(repDateChild.timeJoin)?ex.reTime("s",repDateChild.timeJoin.time):"";
                        dataHtml += "<tr>" +
                            "<td>" + ex.judgeEmpty(repDateChild.name) + "</td>" +                            
                            "<td>" + ex.judgeEmpty(repDateChild.servicePoints) + "</td>" +
                            "<td>" + ex.judgeEmptyOr0(repDateChild.attentionNum) + "</td>" +
                            "<td>" + ex.judgeEmptyOr0(repDateChild.registerNum) + "</td>" +
                            "<td>" + ex.judgeEmptyOr0(repDateChild.carNum) + "</td>" +
                            "<td>" + ex.judgeEmptyOr0(repDateChild.eventApplyNum) + "</td>" +
                            "<td>" + ex.judgeEmptyOr0(repDateChild.applyRate) + "%</td>" +
                            "<td>" + ex.judgeEmptyOr0(repDateChild.eventNum) + "</td>" +
                            "<td>" + ex.judgeEmptyOr0(repDateChild.passRate) + "%</td>" +
                            "<td>" + ex.judgeEmptyOr0(repDateChild.expenses) + "</td>" +
                            "<td>" + ex.judgeEmptyOr0(repDateChild.eventRate) + "%</td>" +
                            "<td>" + shopStatus + "</td>" +
                            "<td>" + timeJoin + "</td>" +
                            "<td><button type='button' class='btn btn-info' onclick=showDetail(" + repDateChild.id + ","+ repDateChild.status +")>查看</button>&nbsp;" +
                            "<button type='button' class='btn btn-primary'onclick=updateMaintenanceshop(" + repDateChild.id + ")>修改</button>&nbsp;" +
                            "</tr>";
                    }
                    dataHtml += "</tbody>";
                    $("#mainDataTable").html(dataHtml);
                    }
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

    //加载渠道数据
    function channelListData(pageNo){
    	console.log('test2')
        var searchInfo = $("#searchInfo").val();
        var searchStatus = $("#searchStatus").val();
        if(searchStatus == -1){
            searchStatus = "";
        }
        count(searchInfo,searchStatus);
        var strJson = {"pageNo":pageNo,"pageSize":pageSize,"type":"20","searchInfo":searchInfo,"status":searchStatus};
        $.ajax({
            type : "post",
            dataType : "json",
            headers: {rqSide: ex.pc()},
            data : JSON.stringify(strJson),
            contentType : "application/json;charset=utf-8",
            url: "${ctx}/maintenanceshop/list",
            success : function(datas){
                if(datas.code == "0"){
                    var repData = datas.data.list;
                    var dataHtml = "<thead><tr><th class='col-xs-1' style='width:13%'>名称</th><th class='col-xs-1' style='width:6%'>关注数</th><th class='col-xs-1' style='width:6%'>注册数</th><th class='col-xs-1' style='width:6%'>保障数</th><th class='col-xs-1' style='width:6%'>申请理赔数量</th><th class='col-xs-1' style='width:6%'>申请率</th><th class='col-xs-1' style='width:6%'>理赔数量</th><th class='col-xs-1' style='width:6%'>申请通过率</th><th class='col-xs-1' style='width:6%'>理赔支出</th><th class='col-xs-1' style='width:6%'>理赔率</th><th class='col-xs-1' style='width:6%'>状态</th><th class='col-xs-1' style='width:10%'>时间</th><th class='col-xs-4' style='width:8%'>操作</th></tr></thead>";
                    dataHtml += "<tbody>";
                     if (0 == repData.length) {
                        dataHtml += "</tbody>";
                        $("#mainDataTable").html(dataHtml);
                        $("#blankMessage").text("暂无名单信息");
                    } else {
                    for(var i=0;i < repData.length;i++){
                        var repDateChild = repData[i];
                        var shopStatus = ex.judgeEmpty(repDateChild.status)==1?"正常":ex.judgeEmpty(repDateChild.status)==2?"冻结":"未知";
                        var timeJoin = ex.judgeEmpty(repDateChild.timeJoin)?ex.reTime("s",repDateChild.timeJoin.time):"";
                        dataHtml += "<tr>" +
                            "<td>" + ex.judgeEmpty(repDateChild.name) + "</td>" +
                            "<td>" + ex.judgeEmptyOr0(repDateChild.attentionNum) + "</td>" +
                            "<td>" + ex.judgeEmptyOr0(repDateChild.registerNum) + "</td>" +
                            "<td>" + ex.judgeEmptyOr0(repDateChild.carNum) + "</td>" +
                            "<td>" + ex.judgeEmptyOr0(repDateChild.eventApplyNum) + "</td>" +
                            "<td>" + ex.judgeEmptyOr0(repDateChild.applyRate) + "%</td>" +
                            "<td>" + ex.judgeEmptyOr0(repDateChild.eventNum) + "</td>" +
                            "<td>" + ex.judgeEmptyOr0(repDateChild.passRate) + "%</td>" +
                            "<td>" + ex.judgeEmptyOr0(repDateChild.expenses) + "</td>" +
                            "<td>" + ex.judgeEmptyOr0(repDateChild.eventRate) + "%</td>" +
                            "<td>" + shopStatus + "</td>" +
                            "<td>" + timeJoin + "</td>" +
                            "<td><button type='button' class='btn btn-info' onclick=showChannel(" + repDateChild.id + ")>查看</button>&nbsp;" +
                            "</tr>";
                    }
                    dataHtml += "</tbody>";
                    $("#mainDataTable").html(dataHtml);
                    }
                    if(datas.data.total > -1){
                        kkpager.init({
                            pagerid : "kkpager",
                            pno : datas.data.pageNum,//当前页
                            total : datas.data.pages,//总页码
                            totalRecords : datas.data.total,//总数据条数
                            mode : 'click',//默认值是link，可选link或者click
                            click : function(n){
                                channelListData(n);
                                window.location.href="#top";
                            }
                        });
                        kkpager.generPageHtml();
                    }
                }
            }
        });
    }

    //加载商家统计
    function count(searchInfo,searchStatus) {
        $.ajax({
            type : "post",
            dataType : "json",
            headers: {rqSide: ex.pc()},
            data : JSON.stringify({"searchInfo":searchInfo,"status":searchStatus}),
            contentType : "application/json;charset=utf-8",
            url: "${ctx}/maintenanceshop/count",
            success : function(datas){
                if(datas.code == "0"){
                    var result = datas.data;
                    $("#shopNum").html("商家("+result.shopNum+")");
                    $("#channelNum").html("其他("+result.channelNum+")");
                }
            }
        });
    }

    //添加其他渠道
    function  addChannel() {
        layer.open({
            title:'新建其他渠道',
            closeBtn:'1',
            type:1,
            btn:['提交','取消'],
            anim: 5,
            shadeClose: false, //点击遮罩关闭
            content: $('#addLay'),
            yes:function(index, layero){
                var name = $("#nameNew").val();
                var tel = $("#telNew").val();
                var address = $("#addressNew").val();
                if (!(name&&tel)) {
                    alertBtn("请填写必填选项");
                    return;
                }
                if (ex.checkPhone(tel)) {
                    alertBtn("请输入正确的电话号码");
                    return;
                }
                var strJson = {"name":name,"tel":tel,"address":address,"type":20};
                $.ajax({
                    type : "post",
                    dataType : "json",
                    headers: {rqSide: ex.pc()},
                    data : JSON.stringify(strJson),
                    contentType : "application/json;charset=utf-8",
                    url: "${ctx}/maintenanceshop/saveChannel",
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

    //编辑渠道
    function  updateChannel(shopId) {
        $("#nameNew").val("");
        $("#telNew").val("");
        $("#addressNew").val("");
        $.ajax({
            type : "post",
            dataType : "json",
            headers: {rqSide: ex.pc()},
            data : JSON.stringify({"shopId":shopId}),
            contentType : "application/json;charset=utf-8",
            url: "${ctx}/maintenanceshop/channelDetail",
            success : function(datas){
                if(datas.code == "0"){
                    var detail = datas.data
                    $("#nameNew").val(ex.judgeEmpty(detail.name));
                    $("#telNew").val(ex.judgeEmpty(detail.tel));
                    $("#addressNew").val(ex.judgeEmpty(detail.address));
                }else{
                    alertBtn(datas.message);
                }
            },
            error:function (e) {
                alertBtn("请求失败!请检查网络或联系管理员!");
            }
        });
        layer.open({
            title:'编辑其他渠道',
            closeBtn:'1',
            type:1,
            btn:['提交','取消'],
            anim: 5,
            shadeClose: false, //点击遮罩关闭
            content: $('#addLay'),
            yes:function(index, layero){
                var name = $("#nameNew").val();
                var tel = $("#telNew").val();
                var address = $("#addressNew").val();
                if (!(name&&tel)) {
                    alertBtn("请填写必填选项");
                    return;
                }
                if (ex.checkPhone(tel)) {
                    alertBtn("请输入正确的电话号码");
                    return;
                }
                var strJson = {"id":shopId,"name":name,"tel":tel,"address":address};
                $.ajax({
                    type : "post",
                    dataType : "json",
                    headers: {rqSide: ex.pc()},
                    data : JSON.stringify(strJson),
                    contentType : "application/json;charset=utf-8",
                    url: "${ctx}/maintenanceshop/updateChannel",
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

    //其他渠道详情弹窗
    function showChannel(shopId){
        $.ajax({
            type : "post",
            dataType : "json",
            headers: {rqSide: ex.pc()},
            data : JSON.stringify({"shopId":shopId}),
            contentType : "application/json;charset=utf-8",
            url: "${ctx}/maintenanceshop/channelDetail",
            success : function(datas) {
                if (datas.code == "0") {
                    var detail = datas.data;
                    $("#name").html(ex.judgeEmpty(detail.name));
                    $("#status").html(ex.judgeEmpty(detail.status)==1?"正常":ex.judgeEmpty(detail.status)==2?"冻结":"未知");
                    $("#address").html(ex.judgeEmpty(detail.address));
                    $("#tel").html(ex.judgeEmpty(detail.tel));
                    $("#attentionNum").html(ex.judgeEmptyOr0(detail.attentionNum));
                    $("#registerNum").html(ex.judgeEmptyOr0(detail.registerNum));
                    $("#qrcode").attr('src',ex.judgeEmpty(detail.qrcode));
                    $("#createAt").html(ex.judgeEmpty(detail.createAt) ? ex.reTime("s", detail.createAt.time) : "");
                    if(detail.status ==1){
                        layer.open({
                            type: 1,
                            title: '其他渠道详情',
                            closeBtn: 1,
                            btn: ['冻结','编辑'],
                            btn1: function(){
                                $.ajax({
                                    type : "post",
                                    dataType : "json",
                                    headers: {rqSide: ex.pc()},
                                    data : JSON.stringify({"id":shopId}),
                                    contentType : "application/json;charset=utf-8",
                                    url: "${ctx}/maintenanceshop/freeze",
                                    success : function(datas){
                                        if(datas.code != "0"){
                                            alertBtn(datas.message)
                                        }else{
                                            location.reload();
                                        }
                                    }
                                });
                            },
                            btn2: function(){
                                updateChannel(shopId);
                                return false;
                            },
                            area: '450px;',
                            shade: 0.5,
                            btnAlign: 'c',
                            moveType: 1, //拖拽模式，0或者1
                            content: $("#defeated-layer")
                        })
                    }else{
                        layer.open({
                            type: 1,
                            title: '其他渠道详情',
                            closeBtn: 1,
                            btn: ['解冻','编辑'],
                            btn1: function(){
                                $.ajax({
                                    type : "post",
                                    dataType : "json",
                                    headers: {rqSide: ex.pc()},
                                    data : JSON.stringify({"id":shopId}),
                                    contentType : "application/json;charset=utf-8",
                                    url: "${ctx}/maintenanceshop/unfreeze",
                                    success : function(datas){
                                        if(datas.code != "0"){
                                            alertBtn(datas.message)
                                        }else{
                                            location.reload();
                                        }
                                    }
                                });
                            },
                            btn2: function(){
                                updateChannel(shopId);
                                return false;
                            },
                            area: '450px;',
                            shade: 0.5,
                            btnAlign: 'c',
                            moveType: 1, //拖拽模式，0或者1
                            content: $("#defeated-layer")
                        })
                    }

                } else {
                    alertBtn(datas.message);
                }
            }
        });
    }

    //添加商家页面
    function addShopPage() {
        self.location.href = "${ctx}/maintenanceshop/editShop.html?maintenanceshopId=-1";
    }

    //添加商家
    function  addMaintenanceshop() {
        layer.open({
            title:'添加商家信息',
            closeBtn:'1',
            type:1,
            btn:['确定','取消'],
            anim: 5,
            shadeClose: false, //点击遮罩关闭
            content: $('#addShopLay'),
            yes:function(index, layero){
                var name = $(".in_1").val();
                var linkman = $(".in_2").val();
                var tel = $(".in_3").val();
                var address = $(".in_4").val();
                var ratio = $(".in_5").val();
                if (!(name&&linkman&&tel&&address&&ratio)) {
                    alertBtn("请填写所有选项");
                    return;
                }
                if (ex.checkPhone(tel)) {
                    alertBtn("请输入正确的电话号码");
                    return;
                }
                var strJson = {"name":name,"linkman":linkman,"tel":tel,"address":address,"ratio":ratio};
                $.ajax({
                    type : "post",
                    dataType : "json",
                    headers: {rqSide: ex.pc()},
                    data : JSON.stringify(strJson),
                    contentType : "application/json;charset=utf-8",
                    url: "${ctx}/maintenanceshop/saveMaintenanceshop",
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

    //修改商家
    function updateMaintenanceshop(maintenanceshopId){
        $.ajax({
            type : "post",
            dataType : "json",
            headers: {rqSide: ex.pc()},
            data : JSON.stringify({"shopId":maintenanceshopId}),
            contentType : "application/json;charset=utf-8",
            url: "${ctx}/maintenanceshop/shopDetail",
            success : function(datas){
                if(datas.code == "0"){
                    var shopDetail = datas.data
                    $(".info_1").val(shopDetail.name);
                    $(".info_2").val(shopDetail.linkman);
                    $(".info_3").val(shopDetail.tel);
                    $(".info_4").val(shopDetail.address);
                    $(".info_5").val(shopDetail.ratio);
                }else{
                    alertBtn(datas.message);
                }
            },
            error:function (e) {
                alertBtn("请求失败!请检查网络或联系管理员!");
            }
        });
        layer.open({
            title:'修改',
            type: 1,
            btn:['确定','取消'],
            btnAlign: 'r',
            shadeClose: false, //点击遮罩关闭
            content: $('#updateLay'),
            yes:function(index, layero){
                var name = $(".info_1").val();
                var linkman = $(".info_2").val();
                var tel = $(".info_3").val();
                var address = $(".info_4").val();
                var ratio = $(".info_5").val();
                if (!(name&&linkman&&tel&&address&&ratio)) {
                    alertBtn("请填写所有选项");
                    return;
                }
                if (ex.checkPhone(tel)) {
                    alertBtn("请输入正确的电话号码");
                    return;
                }
                var strJson = {"id":maintenanceshopId,"name":name,"linkman":linkman,"tel":tel,"address":address,"ratio":ratio};
                $.ajax({
                    type : "post",
                    dataType : "json",
                    headers: {rqSide: ex.pc()},
                    data : JSON.stringify(strJson),
                    contentType : "application/json;charset=utf-8",
                    url: "${ctx}/maintenanceshop/updateMaintenanceshop",
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

    //删除商家
    function deleteMaintenanceshop(maintenanceshopId){
        layer.open({
            title:'信息提示',
            type: 1,
            btn:['确定','取消'],
            btnAlign: 'r',
            area: ['280px'],
            shadeClose: false, //点击遮罩关闭
            content: '<p class="is-layer">您确定要删除该商家吗？</p>',
            yes:function(index, layero){
                //  操作
                var strJson = {"id":maintenanceshopId};
                $.ajax({
                    type : "post",
                    dataType : "json",
                    headers: {rqSide: ex.pc()},
                    data : JSON.stringify(strJson),
                    contentType : "application/json;charset=utf-8",
                    url: "${ctx}/maintenanceshop/deleteMaintenanceshop",
                    success : function(datas){
                        if(datas.code != "0"){
                            alertBtn(datas.message)
                        }else{
                            location.reload();
                        }
                    }
                });
                layer.close();
            }
        });
    }

    //查看维修厂详情
    function showDetail(maintenanceshopId,status) {
        self.location.href = "${ctx}/maintenanceshop/detail.html?maintenanceshopId=" + maintenanceshopId + "&status=" + status;
    }

    function search() {
        $("li[class=active][role=presentation]").children().click();
    }

    function changeSearchStatus(status,htmlContent) {
        $("#searchStatus").val(status);
        $("#searchStatus").html(htmlContent);
        $("li[class=active][role=presentation]").children().click();
    }

    //消息提示 弹窗
    function alertBtn(info) {
        layer.alert(info, function (index) {
            layer.close(index);
        });
    }
    
    //
    function addClass(){
    	$("#flag1").addClass("active");
    	$("#flag2").removeClass("active");
    }
    
</script>
</body>
</html>