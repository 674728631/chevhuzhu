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
                <li>活动列表</li>
            </ul>
        </div>
        <div class="row margin-row-max">
            <div class="col-sm-6 new-search-btn">
                <input id="searchInfo" type="text" class="form-control" aria-describedby="inputGroupSuccess1Status" onkeyup="value=value.replace(/\s/g,'')" placeholder="请输入活动名称进行搜索">
                <button type="button" class="btn def-btn btn-success" onclick="search()">搜索</button>
            </div>
        </div>
        <div class="row">
            <div class="col-lg-12 col-xs-12">
                <div>
                    <ul class="nav nav-tabs nav-bac" role="tablist">
                        <li role="presentation" class="active"><a href="#home_1" aria-controls="home_1" role="tab" data-toggle="tab" onclick="couponListData(1)">全部</a></li>
                        <li role="presentation" id="flag1"><a href="#home_1" aria-controls="home_1" role="tab" data-toggle="tab" onclick="couponListData(1,'0')">未开始</a></li>
                        <li role="presentation" id="flag2"><a href="#home_1" aria-controls="home_1" role="tab" data-toggle="tab" onclick="couponListData(1,'1')">进行中</a></li>
                        <li role="presentation" id="flag3"><a href="#home_1" aria-controls="home_1" role="tab" data-toggle="tab" onclick="couponListData(1,'2')">已结束</a></li>
                        <li role="presentation" id="flag4"><a href="#home_1" aria-controls="home_1" role="tab" data-toggle="tab" onclick="couponListData(1,'100')">已领完</a></li>
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
<!--编辑活动弹窗-->
<div class="update-lay-box" id="addLay">
    <form class="form-horizontal lay-form-update">
        <div class="form-group">
            <label class="col-sm-4 control-label">活动名称：</label>
            <div class="col-sm-8">
                <input type="text" class="form-control in_1" placeholder="" maxlength="20" onkeyup="value=value.replace(/\s/g,'')">
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-4 control-label">奖励类型：</label>
            <div class="col-sm-8">
                <input type="radio" class="in_2" placeholder="" name="ac" value="1">充值奖励
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-4 control-label">被邀请者奖励面额：</label>
            <div class="col-sm-8">
                <input type="text" class="form-control in_3" placeholder="" maxlength="100" onkeyup="value=value.replace(/\s/g,'')">
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-4 control-label">使用条件：</label>
            <div class="col-sm-8">
                <input type="text" class="form-control in_4" placeholder="支付金额满多少可以使用" onkeyup="value=value.replace(/\s/g,'')">
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-4 control-label">奖励发放量：</label>
            <div class="col-sm-8">
                <input type="number" class="form-control in_5" placeholder="">
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-4 control-label">生效时间：</label>
            <div class="col-sm-8">
                <input type="text" class="form-control in_7" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'});">
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-4 control-label">过期时间：</label>
            <div class="col-sm-8">
                <input type="text" class="form-control in_8" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'});">
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-4 control-label">活动说明：</label>
            <div class="col-sm-8">
                <textarea type="text" class="form-control in_6" placeholder="" style="height: 100px" maxlength="200" onkeyup="value=value.replace(/\s/g,'')"></textarea>
            </div>
        </div>
    </form>
</div>
<script type="text/javascript" src="${ctx}/cite/plugins/MyDatePicker/WdatePicker.js"></script>
<script type="text/javascript">
    var pageSize=10;

    couponListData(1);

    //加载页面数据
    function couponListData(pageNo,status){
        var searchInfo = $("#searchInfo").val();
        var strJson = {"pageNo":pageNo,"pageSize":pageSize,"searchInfo":searchInfo,"status":status};
        $.ajax({
            type : "post",
            dataType : "json",
            headers: {rqSide: ex.pc()},
            data : JSON.stringify(strJson),
            contentType : "application/json;charset=utf-8",
            url: "${ctx}/activities/list",
            success : function(datas){
                if(datas.code == "0"){
                    var repData = datas.data.list;
                    var dataHtml = "<thead><tr><th>活动名称</th><th>活动商家</th><th>活动类型</th><th>奖励面额</th><th>状态</th><th>操作</th></tr></thead>";
                    dataHtml += "<tbody>";
                    for(var i=0;i < repData.length;i++){
                        var repDateChild = repData[i];
                        var couponStatus = ex.judgeEmpty(repDateChild.status)==0?"未开始":ex.judgeEmpty(repDateChild.status)==1?"进行中"
                            :ex.judgeEmpty(repDateChild.status)==2?"已结束":ex.judgeEmpty(repDateChild.status)==100?"已领完":"未知";

                        var couponType = ex.judgeEmpty(repDateChild.type)==1?"充值奖励":"未知";

                        var btnName = ex.judgeEmpty(repDateChild.status)==0?"启用":ex.judgeEmpty(repDateChild.status)==1?"停用"
                            :ex.judgeEmpty(repDateChild.status)==2?"启用":ex.judgeEmpty(repDateChild.status)==100?"启用":"停用";

                        var btnClass = ex.judgeEmpty(repDateChild.status)==0?"btn btn-success":ex.judgeEmpty(repDateChild.status)==1?"btn btn-danger"
                            :ex.judgeEmpty(repDateChild.status)==2?"btn btn-success":ex.judgeEmpty(repDateChild.status)==100?"btn btn-success":"btn btn-danger";
                        dataHtml += "<tr>" +
                            "<td>" + ex.judgeEmpty(repDateChild.name) + "</td>" +
                            "<td>" + ex.judgeEmpty(repDateChild.shopName) + "</td>" +
                            "<td>" + couponType + "</td>" +
                            "<td>" + ex.judgeEmpty(repDateChild.amount) + "</td>" +
                            "<td>" + couponStatus + "</td>" +
                            "<td>" +
                            "<button type='button' class='btn btn-info' onclick=updateActivity('" + repDateChild.couponNo + "')>编辑</button>&nbsp;" +
                            "<button type='button' class='"+ btnClass +"' onclick=startOrEnd('" + repDateChild.couponNo + "',"+ repDateChild.status +")>"+ btnName +"</button>&nbsp;" +
                            "<button type='button' class='btn btn-warning' onclick=del('" + repDateChild.couponNo + "')>删除</button>&nbsp;" +
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
                                couponListData(n,status);
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

    //启用或停用活动
    function startOrEnd(couponNo,status){
        var strJson = {"couponNo":couponNo};
        if(status==0 || status==2 || status==100){
            $.ajax({
                type : "post",
                dataType : "json",
                headers: {rqSide: ex.pc()},
                data : JSON.stringify(strJson),
                contentType : "application/json;charset=utf-8",
                url: "${ctx}/activities/start",
                success : function(datas){
                    if(datas.code != "0"){
                        alert(datas.message)
                    }else{
                        location.reload();
                    }
                }
            });
        }else {
            $.ajax({
                type : "post",
                dataType : "json",
                headers: {rqSide: ex.pc()},
                data : JSON.stringify(strJson),
                contentType : "application/json;charset=utf-8",
                url: "${ctx}/activities/end",
                success : function(datas){
                    if(datas.code != "0"){
                        alert(datas.message)
                    }else{
                        location.reload();
                    }
                }
            });
        }

    }

    //删除活动
    function del(couponNo){
        var strJson = {"couponNo":couponNo};
        $.ajax({
            type : "post",
            dataType : "json",
            headers: {rqSide: ex.pc()},
            data : JSON.stringify(strJson),
            contentType : "application/json;charset=utf-8",
            url: "${ctx}/activities/deleteCoupon",
            success : function(datas){
                if(datas.code != "0"){
                    alert(datas.message)
                }else{
                    location.reload();
                }
            }
        });
    }

    //编辑活动
    function  updateActivity(couponNo) {
        $(".in_1").val("");
        $("input:radio[name='ac']").prop("checked",false);
        $(".in_3").val("");
        $(".in_4").val("");
        $(".in_5").val("");
        $(".in_6").val("");
        $(".in_7").val(null);
        $(".in_8").val(null);
        $.ajax({
            type : "post",
            dataType : "json",
            headers: {rqSide: ex.pc()},
            data : JSON.stringify({"couponNo":couponNo}),
            contentType : "application/json;charset=utf-8",
            url: "${ctx}/activities/loadCoupon",
            success : function(datas){
                if(datas.code == "0"){
                    var model = datas.data
                    $(".in_1").val(model.name);
                    $("input:radio[name='ac'][value="+ model.type +"]").prop("checked",true);
                    $(".in_3").val(model.amount);
                    $(".in_4").val(model.meetPrice);
                    $(".in_5").val(model.num);
                    $(".in_6").val(model.description);
                    $(".in_7").val(ex.judgeEmpty(model.beginTime)?ex.reTime("s",model.beginTime.time):"");
                    $(".in_8").val(ex.judgeEmpty(model.endTime)?ex.reTime("s",model.endTime.time):"");
                }else{
                    alertBtn(datas.message);
                }
            },
            error:function (e) {
                alertBtn("请求失败!请检查网络或联系管理员!");
            }
        });
        layer.open({
            title:'编辑活动',
            closeBtn:'1',
            type:1,
            btn:['确定','取消'],
            anim: 5,
            shadeClose: false, //点击遮罩关闭
            content: $('#addLay'),
            yes:function(index, layero){
                var name = $(".in_1").val();
                var type = $("input:radio[name='ac']:checked").val();
                var amount = $(".in_3").val();
                var meetPrice = $(".in_4").val();
                var num = $(".in_5").val();
                var description = $(".in_6").val();
                var beginTime = $(".in_7").val();
                var endTime = $(".in_8").val();
                if (!(name&&type&&amount&&meetPrice&&num&&description)) {
                    alert("请正确填写所有选项");
                    return;
                }
                if (num<0) {
                    alert("奖励发行量必须大于0");
                    return;
                }
                var strJson = {"name":name,"type":type,"amount":amount,"meetPrice":meetPrice,"num":num,"description":description,"couponNo":couponNo,"beginTime":beginTime,"endTime":endTime};
                $.ajax({
                    type : "post",
                    dataType : "json",
                    headers: {rqSide: ex.pc()},
                    data : JSON.stringify(strJson),
                    contentType : "application/json;charset=utf-8",
                    url: "${ctx}/activities/updateCoupon",
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

    //消息提示 弹窗
    function alertBtn(info){
        layer.alert(info, function(index){
            layer.close(index);
        });
    }
</script>
</body>
</html>