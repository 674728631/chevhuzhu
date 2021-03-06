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
                <li>活动模板</li>
            </ul>
        </div>
        <div class="row margin-top-ele">
            <div class="col-lg-12 col-xs-12 col-md-12">
                <div class="panel panel-info">
                    <div class="panel-heading">
                        <h3 class="panel-title">模板列表</h3>
                    </div>
                    <div class="panel-body col-xs-12" id="home_1">
                        <div class="col-sm-3 col-xs-12 input-search">
                            <div class="form-group has-success has-feedback">
                                <div class="input-group">
                                    <span class="input-group-addon">活动名称</span>
                                    <input id="searchInfo" type="text" class="form-control" aria-describedby="inputGroupSuccess1Status" onkeyup="value=value.replace(/\s/g,'')" placeholder="请输入活动名称进行搜索">
                                </div>
                            </div>
                        </div>
                        <div class="panel-de-button">
                            <button type="button" class="btn btn-success def-btn" onclick="modelListData(1)">搜索</button>
                        </div>
                        <div class="panel-de-button">
                            <button type="button" class="btn btn-info def-btn" onclick="addModel('新建')">新建模板</button>
                        </div>
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
<!--新建活动模板 弹窗-->
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
        <!-- 2018.9.17 -->
        <div class="form-group">
            <label class="col-sm-4 control-label">邀请者奖励面额：</label>
            <div class="col-sm-8">
                <input type="text" class="form-control in_4" placeholder="" maxlength="100" onkeyup="value=value.replace(/\s/g,'')">
            </div>
        </div>
                <div class="form-group">
            <label class="col-sm-4 control-label">邀请者奖励额度：</label>
            <div class="col-sm-8">
                <input type="text" class="form-control in_5" placeholder="" maxlength="100" onkeyup="value=value.replace(/\s/g,'')">
            </div>
        </div> 
        <!-- 2018.9.17 -->
        <div class="form-group">
            <label class="col-sm-4 control-label">使用条件：</label>
            <div class="col-sm-8">
                <input type="text" class="form-control in_6" placeholder="支付金额满多少可以使用" onkeyup="value=value.replace(/\s/g,'')">
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-4 control-label">奖励发放量：</label>
            <div class="col-sm-8">
                <input type="number" class="form-control in_7" placeholder="">
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-4 control-label">活动说明：</label>
            <div class="col-sm-8">
                <textarea type="text" class="form-control in_8" placeholder="" style="height: 100px" maxlength="200" onkeyup="value=value.replace(/\s/g,'')"></textarea>
            </div>
        </div>
    </form>
</div>
<script type="text/javascript">
    var pageSize=10;

    modelListData(1);

    //加载页面数据
    function modelListData(pageNo){
        var searchInfo = $("#searchInfo").val();
        var strJson = {"pageNo":pageNo,"pageSize":pageSize,"searchInfo":searchInfo};
        $.ajax({
            type : "post",
            dataType : "json",
            headers: {rqSide: ex.pc()},
            data : JSON.stringify(strJson),
            contentType : "application/json;charset=utf-8",
            url: "${ctx}/activities/modelList",
            success : function(datas){
                if(datas.code == "0"){
                    var repData = datas.data.list;
                    var dataHtml = "<thead><tr><th>活动名称</th><th>活动类型</th><th>被邀请者奖励面额</th><th>邀请者奖励面额</th><th>邀请者奖励额度</th><th>操作</th></tr></thead>";
                    dataHtml += "<tbody>";
                    for(var i=0;i < repData.length;i++){
                        var repDateChild = repData[i];
                        var couponType = ex.judgeEmpty(repDateChild.type)==1?"充值奖励":"未知";
                        dataHtml += "<tr>" +
                            "<td>" + ex.judgeEmpty(repDateChild.name) + "</td>" +
                            "<td>" + couponType + "</td>" +
                            "<td>" + ex.judgeEmptyOr0(repDateChild.amount) + "</td>" +
                            "<td>" + ex.judgeEmptyOr0(repDateChild.inviterAmount) + "</td>" +
                            "<td>" + ex.judgeEmptyOr0(repDateChild.inviterCompensation) + "</td>" +
                            "<td>" +
                            "<button type='button' class='btn btn-info' onclick=addModel('编辑'," + repDateChild.id + ")>编辑模板</button>&nbsp;" +
                            "<button type='button' class='btn btn-success' onclick=createCoupon(" + repDateChild.id + ")>发起活动</button>&nbsp;" +
                            "<button type='button' class='btn btn-warning' onclick=del('" + repDateChild.id + "')>删除模板</button>&nbsp;" +
                            "<button type='button' class='btn btn-success' onclick=startCoupon(" + repDateChild.id + ")>开启活动</button>&nbsp;" +
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
                                modelListData(n);
                                window.location.href="#top";
                            }
                        });
                        kkpager.generPageHtml();
                    }
                }
            }
        });
    }

    //新建活动模板
    function  addModel(title,modelId) {
        $(".in_1").val("");
        $("input:radio[name='ac']").prop("checked",false);
        $(".in_3").val("");
        $(".in_4").val("");
        $(".in_5").val("");
        $(".in_6").val("");
        $(".in_7").val("");
        $(".in_8").val("");
        //如果modelId不为空，表示编辑，回显数据
        if(modelId){
            $.ajax({
                type : "post",
                dataType : "json",
                headers: {rqSide: ex.pc()},
                data : JSON.stringify({"modelId":modelId}),
                contentType : "application/json;charset=utf-8",
                url: "${ctx}/activities/loadModel",
                success : function(datas){
                    if(datas.code == "0"){
                        var model = datas.data
                        $(".in_1").val(model.name);
                        $("input:radio[name='ac'][value="+ model.type +"]").prop("checked",true);
                        $(".in_3").val(model.amount);
                        $(".in_4").val(model.inviterAmount);
                        $(".in_5").val(model.inviterCompensation);
                        $(".in_6").val(model.meetPrice);
                        $(".in_7").val(model.num);
                        $(".in_8").val(model.description);
                    }else{
                        alertBtn(datas.message);
                    }
                },
                error:function (e) {
                    alertBtn("请求失败!请检查网络或联系管理员!");
                }
            });
        }
        layer.open({
            title:title+'活动模板',
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
                var inviterAmount = $(".in_4").val();
                var inviterCompensation = $(".in_5").val();
                var meetPrice = $(".in_4").val();
                var num = $(".in_5").val();
                var description = $(".in_6").val();
                if (!(name&&type&&amount&&meetPrice&&num&&description)) {
                    alert("请正确填写所有选项");
                    return;
                }
                if (num<0) {
                    alert("奖励发行量必须大于0");
                    return;
                }
                if(modelId){
                    var strJson = {"name":name,"type":type,"amount":amount,"inviterAmount":inviterAmount,"inviterCompensation":inviterCompensation,"meetPrice":meetPrice,"num":num,"description":description,"modelId":modelId};
                    $.ajax({
                        type : "post",
                        dataType : "json",
                        headers: {rqSide: ex.pc()},
                        data : JSON.stringify(strJson),
                        contentType : "application/json;charset=utf-8",
                        url: "${ctx}/activities/updateCouponModel",
                        success : function(datas){
                            if(datas.code != "0"){
                                alertBtn(datas.message)
                            }else{
                                location.reload();
                            }
                        }
                    });
                    layer.close(index);
                }else {
                    var strJson = {"name":name,"type":type,"amount":amount,"inviterAmount":inviterAmount,"inviterCompensation":inviterCompensation,"meetPrice":meetPrice,"num":num,"description":description};
                    $.ajax({
                        type : "post",
                        dataType : "json",
                        headers: {rqSide: ex.pc()},
                        data : JSON.stringify(strJson),
                        contentType : "application/json;charset=utf-8",
                        url: "${ctx}/activities/saveCouponModel",
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
            }
        });
    }

    //删除活动模板
    function del(modelId){
        var strJson = {"modelId":modelId};
        $.ajax({
            type : "post",
            dataType : "json",
            headers: {rqSide: ex.pc()},
            data : JSON.stringify(strJson),
            contentType : "application/json;charset=utf-8",
            url: "${ctx}/activities/deleteCouponModel",
            success : function(datas){
                if(datas.code != "0"){
                    alert(datas.message)
                }else{
                    location.reload();
                }
            }
        });
    }

    //跳转发起活动页面
    function createCoupon(modelId) {
        self.location.href = "${ctx}/activities/launchCoupon.html?modelId=" + modelId;
    }
    
    //开启活动
    function startCoupon(modelId){
    	console.log("进入");
    	var strJson = {"modelId":modelId};
    	$.ajax({
    		type:"post",
    		dataType : "json",
            headers: {rqSide: ex.pc()},
            data : JSON.stringify(strJson),
            contentType : "application/json;charset=utf-8",
            url: "${ctx}/activities/startActivity",
            success:function(datas){
            	if(datas.code != "0"){
            		alert(datas.message)
            	}else{
            		/* alert(datas.message); */
             		location.reload(); 
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