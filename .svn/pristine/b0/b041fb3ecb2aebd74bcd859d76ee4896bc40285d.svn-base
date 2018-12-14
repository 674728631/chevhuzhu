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
    <script src="${ctx}/cite/js/extends/SimpleTree.js"></script>
    <!--页面 权限设置css-->
    <style type="text/css">
        .st_tree {
            margin: 10px -20px;
        }
        /* 超链接 */
        .st_tree a {
            text-decoration: none;
        }
        /* 鼠标经过的超链接 */
        .st_tree a:hover {
            color: #f33;
            text-decoration: underline;
        }
        /* 菜单 */
        .st_tree ul {
            padding: 0 18px;
            margin: 0;
        }
        /* 菜单项 */
        .st_tree ul li {
            font-size: 13px;
            color: #222;
            line-height: 18px;
            cursor: pointer;
            list-style: none;
            background: url(${ctx}/cite/images/st_node.gif);
            background-repeat: no-repeat;
            padding: 0 0 3px 20px;
            margin-left: 16px;
        }
        /* 子菜单 */
        .st_tree ul li ul {
        }
        /* 子菜单项 */
        .st_tree ul li ul li {
        }
        /* 子菜单的父节点 */
        .st_tree .folder {
            /*list-style-image: url(imgs/st_icon.png);*/
            background: url(${ctx}/cite/images/st_folder.gif);
            background-repeat: no-repeat;
            padding: 0 0 0 20px;
        }
        /* 展开的父节点 */
        .st_tree .open {
            /*list-style-image: url(imgs/st_icon_open.png);*/
            background: url(${ctx}/cite/images/st_folder_open.gif);
            background-repeat: no-repeat;
            padding: 0 0 0 20px;
        }
    </style>
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
                <li>权限管理</li>
            </ul>
        </div>
        <div class="row margin-top-ele">
            <div class="col-lg-12 col-xs-12">
                <div class="panel panel-info">
                    <div class="panel-heading">
                        <h3 class="panel-title">权限管理</h3>
                    </div>
                    <div class="panel-de-button">
                        <button type="button" class="btn btn-info def-btn" onclick="addRole('添加角色')">添加</button>
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

<!--添加角色 弹窗-->
<div id="add-layer" class="update-lay-box">
        <div class="form-group">
            <label class="col-sm-4 control-label">角色名称：</label>
            <div class="col-sm-8">
                <input type="text" class="ad-input in_1 form-control" placeholder="" maxlength="20" onkeyup="value=value.replace(/\s/g,'')">
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-4 control-label">拥有菜单：</label>
            <div class="col-sm-8">
                <div class="st_tree"><ul name="menuData" class="stree-ul"></ul></div>
            </div>
        </div>
</div>
<!--修改角色 弹窗-->
<div class="update-lay-box" id="updateLay">
    <form class="form-horizontal lay-form-update">
        <div class="form-group">
            <label class="col-sm-4 control-label">角色名称：</label>
            <div class="col-sm-8">
                <input type="text" class="form-control info_1" placeholder="" maxlength="20" onkeyup="value=value.replace(/\s/g,'')">
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-4 control-label">拥有菜单：</label>
            <div class="col-sm-8">
            <div class="st_tree"><ul name="menuData2"></ul></div>
            </div>
        </div>
    </form>
</div>
<script type="text/javascript">
    var pageSize=10;
    roleListData(1);

    //加载页面数据
    function roleListData(pageNo){
        var strJson = {"pageNo":pageNo,"pageSize":pageSize};
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
                    var dataHtml = "<thead><tr><th>名称</th><th>操作</th></tr></thead>";
                    dataHtml += "<tbody>";
                    for(var i=0;i < repData.length;i++){
                        var repDateChild = repData[i];
                        dataHtml += "<tr>" +
                            "<td>" + ex.judgeEmpty(repDateChild.name) + "</td>" +
                            "<td><button type='button' class='btn btn-primary def-btn' onclick='updateRole(this," + repDateChild.id + ")'>修改</button>&nbsp;" +
                            "<button type='button' class='btn btn-danger def-btn' onclick='deleteRole(" + repDateChild.id + ")'>删除</button></td>" +
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
                                roleListData(n);
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
    function addRole(title){
        menuData();
        layer.open({
            title:title,
            type: 1,
            btn:['确定','取消'],
            btnAlign: 'r',
            area: ['400px','500px'],
            offset: 'auto',
            shadeClose: false, //点击遮罩关闭
            content: $('#add-layer'),
            yes:function(index, layero){
                var name = $(".in_1").val();
                if (!name) {
                    alert("请填写角色名称");
                    return;
                }
                var rightsMenu = new Array();
                $.each($('input:checkbox:checked[attr1="save"]'),function(){
                    rightsMenu. push($(this).val());
                });
                var strJson = {"name":name,"rightsMenu":rightsMenu.toString()};
                $.ajax({
                    type : "post",
                    dataType : "json",
                    headers: {rqSide: ex.pc()},
                    data : JSON.stringify(strJson),
                    contentType : "application/json;charset=utf-8",
                    url: "${ctx}/role/saveRole",
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

    //修改
    function updateRole(value,roleId){
        var parents = $(value).parents("tr").find("td");
        var info1 = parents.eq(0).text();
        $(".info_1").val(info1);
        menuData2(roleId);
        layer.open({
            title:'修改',
            type: 1,
            btn:['确定','取消'],
            btnAlign: 'r',
            area: ['400px','500px'],
            shadeClose: false, //点击遮罩关闭
            content: $('#updateLay'),
            success:function () {
                $(value).prop('selectedIndex', 0);
            },
            yes:function(index, layero){
                var name = $(".info_1").val();
                if (!name) {
                    alert("请填写角色名称");
                    return;
                }
                var rightsMenu = new Array();
                $.each($('input:checkbox:checked[attr1="update"]'),function(){
                    rightsMenu. push($(this).val());
                });
                var strJson = {"name":name,"id":roleId,"rightsMenu":rightsMenu.toString()};
                $.ajax({
                    type : "post",
                    dataType : "json",
                    headers: {rqSide: ex.pc()},
                    data : JSON.stringify(strJson),
                    contentType : "application/json;charset=utf-8",
                    url: "${ctx}/role/updateRole",
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

    //删除
    function deleteRole(roleId){
        layer.open({
            title:'信息提示',
            type: 1,
            btn:['确定','取消'],
            btnAlign: 'r',
            area: ['280px'],
            shadeClose: false, //点击遮罩关闭
            content: '<p class="is-layer">您确定要删除该角色吗？</p>',
            yes:function(index, layero){
                //  操作
                var strJson = {"id":roleId};
                $.ajax({
                    type : "post",
                    dataType : "json",
                    headers: {rqSide: ex.pc()},
                    data : JSON.stringify(strJson),
                    contentType : "application/json;charset=utf-8",
                    url: "${ctx}/role/deleteRole",
                    success : function(datas){
                        if(datas.code != "0"){
                            alert(datas.message)
                        }else{
                            location.reload();
                        }
                    }
                });
                layer.close();
            }
        });
    }

    //载入所有菜单权限
    function menuData(){
        var strJson = {};
        $.ajax({
            type : "post",
            dataType : "json",
            headers: {rqSide: ex.pc()},
            data : JSON.stringify(strJson),
            contentType : "application/json;charset=utf-8",
            url: "${ctx}/role/loadMenuData",
            success : function(datas){
                if(datas.code == "0"){
                    var allMenus = datas.data.allMenus;
                    var dataHtml = "";
                    for(var i=0;i < allMenus.length;i++){
                        var menuSpecies = allMenus[i];
                        var parentMenu = menuSpecies.parentMenu;
                        var childMenus = menuSpecies.childMenu;
                        dataHtml += "<input attr1='save' type='checkbox' class='check_1' value='" + parentMenu.id + "'>";
                        dataHtml += "<li><a href='#'>"+parentMenu.name+"</a></li>";
                        dataHtml += "<ul show='true'>";
                        for(var j=0;j < childMenus.length;j++){
                            var childMenu = childMenus[j];
                            dataHtml += "<input attr1='save' type='checkbox' class='check_1' value='" + childMenu.id + "'>";
                            dataHtml += "<li><a href='#'>"+childMenu.name+"</a></li>";
                        }
                        dataHtml += "</ul>";
                    }
                    $("[name='menuData']").html(dataHtml);
                }
            }
        });
    }
    //回显所有菜单权限
    function menuData2(roleId){
        var strJson = {"roleId":roleId};
        $.ajax({
            type : "post",
            dataType : "json",
            headers: {rqSide: ex.pc()},
            data : JSON.stringify(strJson),
            contentType : "application/json;charset=utf-8",
            url: "${ctx}/role/loadMenuData",
            success : function(datas){
                if(datas.code == "0"){
                    var allMenus = datas.data.allMenus;
                    var userMenus = datas.data.userMenus;
                    var dataHtml = "";
                    for(var i=0;i < allMenus.length;i++){
                        var menuSpecies = allMenus[i];
                        var parentMenu = menuSpecies.parentMenu;
                        var childMenus = menuSpecies.childMenu;
                        var flag = true;
                        if(userMenus){
                            var menuArr = userMenus.split(",");
                            for(var x=0;x < menuArr.length;x++){
                                if(parentMenu.id == menuArr[x]){
                                    flag = false;
                                    break;
                                }
                            }
                        }
                        if(flag){
                            dataHtml += "<input attr1='update' type='checkbox' class='check_1' value='" + parentMenu.id + "'>";
                        }else {
                            dataHtml += "<input attr1='update' type='checkbox' class='check_1' checked='checked' value='" + parentMenu.id + "'>";
                        }
                        dataHtml += "<li><a href='#'>"+parentMenu.name+"</a></li>";
                        dataHtml += "<ul show='true'>";
                        for(var j=0;j < childMenus.length;j++){
                            var childMenu = childMenus[j];
                            var flag2 = true;
                            if(userMenus){
                                var menuArr = userMenus.split(",");
                                for(var x=0;x < menuArr.length;x++){
                                    if(childMenu.id == menuArr[x]){
                                        flag2 = false;
                                        break;
                                    }
                                }
                            }
                            if(flag2){
                                dataHtml += "<input attr1='update' type='checkbox' class='check_1' value='" + childMenu.id + "'>";
                            }else {
                                dataHtml += "<input attr1='update' type='checkbox' class='check_1' checked='checked' value='" + childMenu.id + "'>";
                            }
                            dataHtml += "<li><a href='#'>"+childMenu.name+"</a></li>";
                        }
                        dataHtml += "</ul>";
                    }
                    $("[name='menuData2']").html(dataHtml);
                }
            }
        });
    }
</script>
</body>
</html>