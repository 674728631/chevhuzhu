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
                <li><i class="fa fa-dashboard"></i> 订单</li>
                <li>投诉订单</li>
            </ul>
        </div>
        <div class="row margin-row-max">
            <div class="col-sm-4 new-search-btn">
                <input id="searchInfo" type="text" class="form-control" aria-describedby="inputGroupSuccess1Status" onkeyup="value=value.replace(/\s/g,'')" placeholder="请输入姓名、手机号进行搜索订单">
                <button type="button" class="btn def-btn btn-success" onclick="search()">搜索</button>
            </div>
            <div class="col-sm-4">
                <button type="button" class="btn btn-info def-btn" onclick="addNew()">新建</button>
            </div>
        </div>
        <div class="row">
            <div class="col-lg-12 col-xs-12">
                <div>
                    <!-- Nav tabs -->
                    <ul class="nav nav-tabs nav-bac" role="tablist">
                        <li role="presentation" class="active"><a id="totalNum" href="#home_1" aria-controls="home_1" role="tab" data-toggle="tab" onclick="complaintListData(1)">全部</a></li>
                        <li role="presentation" id="flag1"><a id="unSolveNum" href="#home_1" aria-controls="home_1" role="tab" data-toggle="tab" onclick="complaintListData(1,'1')">待处理</a></li>
                        <li role="presentation" id="flag2"><a id="solveNum" href="#home_1" aria-controls="home_1" role="tab" data-toggle="tab" onclick="complaintListData(1,'3')">已处理</a></li>
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
<!--新建投诉订单 弹窗-->
<div class="update-lay-box" id="addLay" style="width:640px">
    <form class="form-horizontal lay-form-update" style="width:100%">
        <div class="form-group">
            <label class="col-sm-2 control-label">姓名：</label>
            <div class="col-sm-4">
                <input id="nameNew" type="text" class="form-control" placeholder="" maxlength="20" onkeyup="value=value.replace(/\s/g,'')">
            </div>
            <label class="col-sm-2 control-label">手机号：</label>
            <div class="col-sm-4">
                <input id="telNew" type="text" class="form-control" placeholder="" maxlength="11">
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-2 control-label">用户类型：</label>
            <div class="col-sm-4">
                <select id="typeUserNew" class="form-control">
                    <option value=0>请选择</option>
                    <option value=1>商家</option>
                    <option value=2>车主</option>
                    <option value=3>其他</option>
                </select>
            </div>
            <label class="col-sm-2 control-label">问题类型：</label>
            <div class="col-sm-4">
                <select id="typeQuestionNew" class="form-control">
                    <option value=0>请选择</option>
                    <option value=1>加入互助问题</option>
                    <option value=10>理赔订单</option>
                    <option value=20>扣款有误</option>
                    <option value=30>保障期</option>
                    <option value=40>充值</option>
                    <option value=100>其他</option>
                </select>
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-2 control-label">问题描述：</label>
            <div class="col-sm-10">
                <textarea id="contentNew" onkeyup="value=value.replace(/\s/g,'')" style="border:1px solid #ccc;border-radius:6px;width:100%;"></textarea>
            </div>
        </div>
    </form>
</div>
<!-- 查看投诉订单 -->
<div id="defeated-layer">
    <div class="input-reason">
        <li style="float:left;width:50%;margin-top:6px;">姓名：<span id="name"></span></li>
        <li style="float:left;width:50%;margin-top:6px;">手机号：<span id="tel"></span></li>
        <li style="float:left;width:50%;margin-top:6px;">用户类型：<span id="typeUser"></span></li>
        <li style="float:left;width:50%;margin-top:6px;">问题类型：<span id="typeQuestion"></span></li>
        <li style="float:left;width:100%;margin-top:6px;">问题描述：<span id="content"></span></li>
        <li style="float:left;width:100%;margin:6px 0;" id="time"></li>
    </div>
</div>
<script type="text/javascript">
    //加载投诉统计
    function complaintCount(searchInfo) {
        $.ajax({
            type : "post",
            dataType : "json",
            headers: {rqSide: ex.pc()},
            data : JSON.stringify({"searchInfo":searchInfo}),
            contentType : "application/json;charset=utf-8",
            url: "${ctx}/complaint/count",
            success : function(datas){
                if(datas.code == "0"){
                    var result = datas.data;
                    $("#totalNum").html("全部("+result.totalNum+")");
                    $("#unSolveNum").html("待处理("+result.unSolveNum+")");
                    $("#solveNum").html("已处理("+result.solveNum+")");
                }
            }
        });
    }

    var pageSize=10;

    complaintListData(1);

    //加载页面数据
    function complaintListData(pageNo,status){
        var searchInfo = $("#searchInfo").val();
        complaintCount(searchInfo);
        var strJson = {"pageNo":pageNo,"pageSize":pageSize,"searchInfo":searchInfo,"status":status};
        $.ajax({
            type : "post",
            dataType : "json",
            headers: {rqSide: ex.pc()},
            data : JSON.stringify(strJson),
            contentType : "application/json;charset=utf-8",
            url: "${ctx}/complaint/list",
            success : function(datas){
                if(datas.code == "0"){
                    var repData = datas.data.list;
                    var dataHtml = "<thead><tr><th>姓名</th><th>手机号</th><th>用户类型</th><th>问题类型</th><th>状态</th><th>时间</th><th>操作</th></tr></thead>";
                    dataHtml += "<tbody>";
                    if (0 == repData.length) {
                        dataHtml += "</tbody>";
                        $("#mainDataTable").html(dataHtml);
                        $("#blankMessage").text("暂无订单信息");
                    } else {
                        for (var i = 0; i < repData.length; i++) {
                            var repDateChild = repData[i];
                            var complaintStatus = ex.judgeEmpty(repDateChild.status) == 1 ? "待处理" : ex.judgeEmpty(repDateChild.status) == 3 ? "已处理" : "未知";
                            var typeUser = ex.complaintTypeUser(repDateChild.typeUser);
                            var typeQuestion = ex.complaintTypeQuestion(repDateChild.typeQuestion);
                            var createAt = ex.judgeEmpty(repDateChild.createAt) ? ex.reTime("s", repDateChild.createAt.time) : "";
                            dataHtml += "<tr>" +
                                "<td>" + ex.judgeEmpty(repDateChild.name) + "</td>" +
                                "<td>" + ex.judgeEmpty(repDateChild.tel) + "</td>" +
                                "<td>" + typeUser + "</td>" +
                                "<td>" + typeQuestion + "</td>" +
                                "<td>" + complaintStatus + "</td>" +
                                "<td>" + createAt + "</td>" +
                                "<td><button type='button' class='btn btn-info' onclick='showDetail(" + repDateChild.id + ")'>查看</button>&nbsp;</td>" +
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
                                complaintListData(n,status);
                                window.location.href="#top";
                            }
                        });
                        kkpager.generPageHtml();
                    }
                }
            }
        });
    }

    function search() {
        $("li[class=active][role=presentation]").children().click();
    }

    //新建投诉订单
    function  addNew() {
        layer.open({
            title:'新建投诉订单',
            closeBtn:'1',
            type:1,
            area: '640px',
            btn:['提交','取消'],
            anim: 5,
            btnAlign: 'c',
            shadeClose: false, //点击遮罩关闭
            content: $('#addLay'),
            yes:function(index, layero){
                var name = $("#nameNew").val();
                var tel = $("#telNew").val();
                var typeUser = $("#typeUserNew").val();
                var typeQuestion = $("#typeQuestionNew").val();
                var content = $("#contentNew").val();
                if (!(name&&tel&&content)) {
                    alertBtn("请填写所有选项");
                    return;
                }
                if (typeUser==0 || typeQuestion==0) {
                    alertBtn("请填写所有选项");
                    return;
                }
                if (ex.checkPhone(tel)) {
                    alertBtn("请输入正确的电话号码");
                    return;
                }
                var strJson = {"name":name,"tel":tel,"typeUser":typeUser,"typeQuestion":typeQuestion,"content":content};
                $.ajax({
                    type : "post",
                    dataType : "json",
                    headers: {rqSide: ex.pc()},
                    data : JSON.stringify(strJson),
                    contentType : "application/json;charset=utf-8",
                    url: "${ctx}/complaint/create",
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

    //投诉详情弹窗
    function showDetail(complaintId){
        $.ajax({
            type : "post",
            dataType : "json",
            headers: {rqSide: ex.pc()},
            data : JSON.stringify({"complaintId":complaintId}),
            contentType : "application/json;charset=utf-8",
            url: "${ctx}/complaint/detail",
            success : function(datas) {
                if (datas.code == "0") {
                    var complaintDetail = datas.data;
                    $("#name").html(ex.judgeEmpty(complaintDetail.name));
                    $("#tel").html(ex.judgeEmpty(complaintDetail.tel));
                    $("#typeUser").html(ex.complaintTypeUser(complaintDetail.typeUser));
                    $("#typeQuestion").html(ex.complaintTypeQuestion(complaintDetail.typeQuestion));
                    $("#content").html(ex.judgeEmpty(complaintDetail.content));
                    if(complaintDetail.status ==1){
                        var createAt = ex.judgeEmpty(complaintDetail.createAt) ? ex.reTime("s", complaintDetail.createAt.time) : "";
                        var timeHtml = "投诉时间：<span>"+ createAt +"</span>"
                        $("#time").html(timeHtml);
                        layer.open({
                            type: 1,
                            title: '待处理订单详情',
                            closeBtn: 1,
                            area: '450px;',
                            btn:['已处理','取消'],
                            shade: 0.5,
                            btnAlign: 'c',
                            moveType: 1, //拖拽模式，0或者1
                            content: $("#defeated-layer"),
                            yes:function(index, layero){
                                $.ajax({
                                    type : "post",
                                    dataType : "json",
                                    headers: {rqSide: ex.pc()},
                                    data : JSON.stringify({"complaintId":complaintId}),
                                    contentType : "application/json;charset=utf-8",
                                    url: "${ctx}/complaint/success",
                                    success : function(datas){
                                        if(datas.code != "0"){
                                            alert(datas.message)
                                        }else{
                                            location.reload();
                                        }
                                    }
                                });
                            }
                        })
                    }else{
                        var solveAt = ex.judgeEmpty(complaintDetail.solveAt) ? ex.reTime("s", complaintDetail.solveAt.time) : "";
                        var timeHtml = "处理时间：<span>"+ solveAt +"</span>"
                        $("#time").html(timeHtml);
                        layer.open({
                            type: 1,
                            title: '已处理订单详情',
                            closeBtn: 1,
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

    //消息提示 弹窗
    function alertBtn(info) {
        layer.alert(info, function (index) {
            layer.close(index);
        });
    }
</script>
</body>
</html>