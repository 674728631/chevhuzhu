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
                <li><i class="fa fa-dashboard"></i>管理</li>
                <li>用户管理</li>
            </ul>
        </div>
        <div class="row margin-top-ele">
            <div class="col-lg-12 col-xs-12">
                <div class="panel panel-info">
                    <div class="panel-heading">
                        <h3 class="panel-title">用户管理</h3>
                    </div>
                    <div class="panel-de-button">
                        <button type="button" class="btn btn-info def-btn" onclick="addAdmin('添加管理员')">添加</button>
                    </div>
                    <div class="panel-body" id="home_1">
                        <table class="table table-bordered table-hover" id="mainDataTable"></table>
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

<!--添加管理员 弹窗-->
<div id="ad-layer" class="update-lay-box">
    <ul>
        <li>
            <span>账号：</span>
            <%--<input type="text" class="ad-input in_1 form-control" maxlength="20" onkeyup="value=value.replace(/\s/g,'')">--%>
            <input type="text" class="ad-input in_1 form-control" maxlength="20">
        </li>
        <li>
            <span>电话：</span>
            <input type="text" class="ad-input in_2 form-control" maxlength="11" onkeyup="value=value.replace(/\s/g,'')">
        </li>
        <li>
            <span>密码：</span>
            <input type="password" class="ad-input in_3 form-control" onkeyup="value=value.replace(/\s/g,'')">
        </li>
        <li>
            <span>再次输入密码：</span>
            <input type="password" class="ad-input in_4 form-control" onkeyup="value=value.replace(/\s/g,'')">
        </li>
        <li>
            <span>角色：</span>
            <select class="ad-input in_5 form-control" name="roleData"></select>
        </li>
    </ul>
</div>

<!--修改管理员 弹窗-->
<div class="update-lay-box" id="updateLay">
    <form class="form-horizontal lay-form-update">
        <div class="form-group">
            <label class="col-sm-4 control-label">账号：</label>
            <div class="col-sm-8">
                <%--<input type="text" class="form-control info_1" placeholder="" maxlength="20" onkeyup="value=value.replace(/\s/g,'')">--%>
                <input type="text" class="form-control info_1" placeholder="" maxlength="20">
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-4 control-label">电话：</label>
            <div class="col-sm-8">
                <input type="text" class="form-control info_2" placeholder="" maxlength="11" onkeyup="value=value.replace(/\s/g,'')">
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-4 control-label">角色：</label>
            <div class="col-sm-8">
                <select name="roleData" id="" class="form-control info_3"></select>
            </div>
        </div>
    </form>
</div>
<!--修改密码 弹窗-->
<div class="update-lay-box" id="updatePassword">
    <form class="form-horizontal lay-form-update">
        <div class="form-group">
            <label class="col-sm-4 control-label">密码：</label>
            <div class="col-sm-8">
                <input type="password" class="form-control password" minlength="6" onkeyup="value=value.replace(/\s/g,'')">
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-4 control-label">确认密码：</label>
            <div class="col-sm-8">
                <input type="password" class="form-control confirmPassword" minlength="6" onkeyup="value=value.replace(/\s/g,'')">
            </div>
        </div>
    </form>
</div>
<script type="text/javascript">
    var pageSize=10;

    adminListData(1);

    //加载页面数据
    function adminListData(pageNo){
        var strJson = {"pageNo":pageNo,"pageSize":pageSize};
        $.ajax({
            type : "post",
            dataType : "json",
            headers: {rqSide: ex.pc()},
            data : JSON.stringify(strJson),
            contentType : "application/json;charset=utf-8",
            url: "${ctx}/admin/list",
            success : function(datas){
                if(datas.code == "0"){
                    var repData = datas.data.list;
                    var dataHtml = "<thead><tr><th>账号</th><th>电话</th><th>角色</th><th>状态</th><th>操作</th></tr></thead>";
                    dataHtml += "<tbody>";
                    for(var i=0;i < repData.length;i++){
                        var repDateChild = repData[i];
                        var status = ex.judgeEmpty(repDateChild.status)==1?"正常":ex.judgeEmpty(repDateChild.status)==2?"冻结":"未知";
                        dataHtml += "<tr>" +
                            "<td>" + ex.judgeEmpty(repDateChild.adminUN) + "</td>" +
                            "<td>" + ex.judgeEmpty(repDateChild.adminPN) + "</td>" +
                            "<td>" + ex.judgeEmpty(repDateChild.name) + "</td>" +
                            "<td>" + status + "</td>" +
                            "<td><select class='op-select form-control' onclick='gradeChange(this," + repDateChild.id + "," + repDateChild.roleId +")'>\n" +
                            "    <option value='0' selected='selected'>请选择</option>\n" +
                            "    <option value='1'>修改</option>\n" +
                            "    <option value='2'>冻结</option>\n" +
                            "    <option value='3'>解冻</option>\n" +
                            "    <option value='4'>删除</option>\n" +
                            "    <option value='5'>修改密码</option>\n" +
                            "</select></td>" +
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
                                adminListData(n);
                                window.location.href="#top";
                            }
                        });
                        kkpager.generPageHtml();
                    }
                }
            }
        });
    }

    //添加
    function addAdmin(title){
        roleData();
        layer.open({
            title:title,
            type: 1,
            btn:['确定','取消'],
            btnAlign: 'r',
            offset: 'auto',
            shadeClose: false, //点击遮罩关闭
            content: $('#ad-layer'),
            yes:function(index, layero){
                var adminUN = $(".in_1").val();
                var adminPN = $(".in_2").val();
                var adminPW = $(".in_3").val();
                var adminPW2 = $(".in_4").val();
                var roleId = $(".in_5").val();
                if (!(adminUN&&adminPN&&adminPW&&roleId)) {
                    alert("请填写所有选项");
                    return;
                }
                if (!(adminPW==adminPW2)) {
                    alert("两次输入的密码不相同");
                    return;
                }
                if (ex.checkPhone(adminPN)) {
                    alert("请输入正确的电话号码");
                    return;
                }
                if (roleId==0) {
                    alert("请选择角色");
                    return;
                }
                var strJson = {"adminUN":adminUN,"adminPN":adminPN,"adminPW":adminPW,"roleId":roleId};
                $.ajax({
                    type : "post",
                    dataType : "json",
                    headers: {rqSide: ex.pc()},
                    data : JSON.stringify(strJson),
                    contentType : "application/json;charset=utf-8",
                    url: "${ctx}/admin/saveAdmin",
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

    //‘操作’下拉菜单
    function gradeChange(value,adminId,roleId){
        var parents = $(value).parents("tr").find("td");
        var info1 = parents.eq(0).text(),
            info2 = parents.eq(1).text();
        $(".info_1").val(info1);
        $(".info_2").val(info2);
        var opt= $(value).val();
        if(opt == 1){
            roleData(roleId)
            layer.open({
                title:'修改',
                type: 1,
                btn:['确定','取消'],
                btnAlign: 'r',
                shadeClose: false, //点击遮罩关闭
                content: $('#updateLay'),
                success:function () {
                    $(value).prop('selectedIndex', 0);
                },
                yes:function(index, layero){
                    var adminUN = $(".info_1").val();
                    var adminPN = $(".info_2").val();
                    var roleId = $(".info_3").val();
                    if (!(adminUN&&adminPN&&roleId)) {
                        alert("请填写所有选项");
                        return;
                    }
                    if (ex.checkPhone(adminPN)) {
                        alert("请输入正确的电话号码");
                        return;
                    }
                    if (roleId==0) {
                        alert("请选择角色");
                        return;
                    }
                    var strJson = {"adminUN":adminUN,"adminPN":adminPN,"roleId":roleId,"id":adminId};
                    $.ajax({
                        type : "post",
                        dataType : "json",
                        headers: {rqSide: ex.pc()},
                        data : JSON.stringify(strJson),
                        contentType : "application/json;charset=utf-8",
                        url: "${ctx}/admin/updateAdmin",
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
        if(opt == 2){
            layer.open({
                title:'信息提示',
                type: 1,
                btn:['确定','取消'],
                btnAlign: 'r',
                area: ['280px'],
                shadeClose: false, //点击遮罩关闭
                content: '<p class="is-layer">您确定要冻结该用户吗？</p>',
                success:function () {
                    $(value).prop('selectedIndex', 0);
                },
                yes:function(index, layero){
                    //  操作
                    var strJson = {"id":adminId,"status":2};
                    $.ajax({
                        type : "post",
                        dataType : "json",
                        headers: {rqSide: ex.pc()},
                        data : JSON.stringify(strJson),
                        contentType : "application/json;charset=utf-8",
                        url: "${ctx}/admin/updateAdmin",
                        success : function(datas){
                            location.reload();
                        }
                    });
                    layer.close(index);
                }
            });
        }
        if(opt == 3){
            layer.open({
                title:'信息提示',
                type: 1,
                btn:['确定','取消'],
                btnAlign: 'r',
                area: ['280px'],
                shadeClose: false, //点击遮罩关闭
                content: '<p class="is-layer">您确定要解冻该用户吗？</p>',
                success:function () {
                    $(value).prop('selectedIndex', 0);
                },
                yes:function(index, layero){
                    //  操作
                    var strJson = {"id":adminId,"status":1};
                    $.ajax({
                        type : "post",
                        dataType : "json",
                        headers: {rqSide: ex.pc()},
                        data : JSON.stringify(strJson),
                        contentType : "application/json;charset=utf-8",
                        url: "${ctx}/admin/updateAdmin",
                        success : function(datas){
                            location.reload();
                        }
                    });
                    layer.close(index);
                }
            });
        }
        if(opt == 4){
            layer.open({
                title:'信息提示',
                type: 1,
                btn:['确定','取消'],
                btnAlign: 'r',
                area: ['280px'],
                shadeClose: false, //点击遮罩关闭
                content: '<p class="is-layer">您确定要删除该用户吗？</p>',
                success:function () {
                    $(value).prop('selectedIndex', 0);
                },
                yes:function(index, layero){
                    //  操作
                    var strJson = {"id":adminId};
                    $.ajax({
                        type : "post",
                        dataType : "json",
                        headers: {rqSide: ex.pc()},
                        data : JSON.stringify(strJson),
                        contentType : "application/json;charset=utf-8",
                        url: "${ctx}/admin/deleteAdmin",
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
        if(opt == 5){
            layer.open({
                title:'修改密码',
                type: 1,
                btn:['确定','取消'],
                btnAlign: 'r',
                shadeClose: false, //点击遮罩关闭
                content: $('#updatePassword'),
                success:function () {
                    $(value).prop('selectedIndex', 0);
                },
                yes:function(index, layero){
                    var password = $("#updatePassword .password ").val();
                    var comfirmPassword= $("#updatePassword .confirmPassword").val();
                    if (!password) {
                        alert("请输入密码");
                        return;
                    }
                    if (!comfirmPassword) {
                        alert("请输入确认密码");
                        return;
                    }
                    if (!(password == comfirmPassword)) {
                        alert("密码与确认密码不一致");
                        return;
                    }
                    if (password.length < 6){
                        alert("密码的长度不能小于6位");
                        return;
                    }
                    var strJson = {"adminPW":password,"adminPN":info2};
                    $.ajax({
                        type : "post",
                        dataType : "json",
                        headers: {rqSide: ex.pc()},
                        data : JSON.stringify(strJson),
                        contentType : "application/json;charset=utf-8",
                        url: "${ctx}/admin/updatePassword",
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
    }

    //载入所有角色数据
    function roleData(roleId){
        var strJson = {"pageNo":1,"pageSize":2147483647};
        $.ajax({
            type : "post",
            dataType : "json",
            headers: {rqSide: ex.pc()},
            data : JSON.stringify(strJson),
            contentType : "application/json;charset=utf-8",
            url: "${ctx}/role/list",
            success : function(datas){
                if(datas.code == "0"){
                    var repData = datas.data.list;
                    var dataHtml = "<option value='0'>请选择</option>";
                    for(var i=0;i < repData.length;i++){
                        var repDateChild = repData[i];
                        if(roleId == repDateChild.id){
                            dataHtml += "<option value='" + repDateChild.id + "' selected = 'selected'>" + repDateChild.name + "</option>";
                        }else {
                            dataHtml += "<option value='" + repDateChild.id + "'>" + repDateChild.name + "</option>";
                        }
                    }
                    $("[name='roleData']").html(dataHtml);
                }
            }
        });
    }
</script>
</body>
</html>