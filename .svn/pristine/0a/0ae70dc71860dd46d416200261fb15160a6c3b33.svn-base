<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"></c:set>

<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <title>车V互助后台管理系统</title>
        <meta name="keywords" content="车V互助后台管理系统"/>
        <meta name="description" content="车V互助后台管理系统"/>
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <meta name="author" content="车V互助">
        <link rel="shortcut icon" href="${ctx}/cite/images/vlogo.png" type="image/x-icon">
        <link rel="stylesheet" href="${ctx}/cite/css/bootstrap.css">
        <link rel="stylesheet" href="${ctx}/cite/plugins/font-awesome/css/font-awesome.min.css">
        <link rel="stylesheet" href="${ctx}/cite/css/login.css">
        <link rel="stylesheet" href="${ctx}/cite/css/animate.min.css">
        <link rel="stylesheet" href="${ctx}/cite/css/index.css">
    </head>

    <body>
        <header>
            <a href="javascript:void(0)">
                <img src="${ctx}/cite/images/logo_icon.png" alt="" style="width: 100%">
            </a>
            <span>车V互助后台管理系统</span>
        </header>
        <div class="login-box">
            <h4>账号登录</h4>
            <form id="lForm" action="${ctx}/boss/" method="post">
                <div class="user-input user-account">
                    <input type="text" placeholder="请输入账号" id="managername" name="username" autocomplete="off">
                    <span></span>
                </div>
                <div class="user-input user-password">
                    <input type="password" placeholder="请输入密码" id="managerpassword" name="password" autocomplete="off" >
                    <span></span>
                </div>
                <!--用户名或密码错误提示-->
                <p class="erro-tips animated flash"></p>
                <div class="common-div text-right"><a href="javascript:void (0);" onclick="forget()">忘记密码？修改密码</a></div>
                <div class="common-div login-btn" onclick="loginBoss();">登录</div>
            </form>
        </div>

        <!--找回密码 弹窗-->
        <div class="update-lay-box" id="addLay">
            <form class="form-horizontal lay-form-update">
                <div class="form-group">
                    <label class="col-sm-4 control-label">手机号：</label>
                    <div class="col-sm-8">
                        <input id="adminPN" type="text" class="form-control" placeholder="" maxlength="11" onkeyup="value=value.replace(/\s/g,'')">
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-4 control-label">验证码：</label>
                    <div class="col-sm-8">
                        <input id="verifyCode" type="text" class="form-control" placeholder="" maxlength="4" onkeyup="value=value.replace(/\s/g,'')">
                        <button type='button' class='btn btn-info' onclick=getCode()>获取验证码</button>
                        <button type='button' class='btn btn-info' onclick=phoneVerification()>下一步</button>
                    </div>
                </div>
            </form>
        </div>

        <!--找回密码 弹窗-->
        <div class="update-lay-box" id="updateLay">
            <form class="form-horizontal lay-form-update">
                <div class="form-group">
                    <label class="col-sm-4 control-label">密码：</label>
                    <div class="col-sm-8">
                        <input id="password" type="text" class="form-control" placeholder="" maxlength="20" onkeyup="value=value.replace(/\s/g,'')">
                        <button type='button' class='btn btn-info' onclick=resetPassword()>提交</button>
                    </div>
                </div>
            </form>
        </div>

        <script type="text/javascript" src="${ctx}/cite/js/extends/jquery-1.10.2.js"></script>
        <script type="text/javascript" src="${ctx}/cite/plugins/layer/layer.js"></script>
        <script type="text/javascript" src="${ctx}/cite/js/module/extend.js"></script>
        <script type="text/javascript">
            $("body").bind("keydown",function(e){
                e = window.event || e;
                if(event.keyCode==116){//屏蔽F5刷新键
                    e.keyCode = 0; //IE下需要设置为keyCode为false
                    return false;
                }
            });

            if(${(rl.code == '4001' || rl.code == '4002' || rl .code == '4000') && (userInfo == null || userInfo =='')}){
                $(".erro-tips").text("用户名或密码填写有误!");
                $(".erro-tips").show();
            }

            $("#managername").focus(function(){$(".erro-tips").hide();})
            $("#managerpassword").focus(function(){$(".erro-tips").hide();});
            function login() {
                var username = $("#managername").val();
                var password = $("#managerpassword").val();
                if (username != null && username != "" && password != null && password != null) {
                    $("#lForm").submit();
                } else {
                    $(".erro-tips").text("用户名或密码不能为空!");
                    $(".erro-tips").show();
                }
            }

            // 登录后台系统
            function loginBoss() {
                var username = $("#managername").val();
                var password = $("#managerpassword").val();
                if (username != null && username != "" && password != null && password != null) {
                    $.ajax({
                        type: "post",
                        dataType: "json",
                        headers: {rqSide: ex.pc()},
                        data: {"username": username,"password": password},
                        contentType: "application/x-www-form-urlencoded;charset=utf-8",
                        url: "${ctx}/boss/login",
                        success : function(data){
                            if (data.code == "501"){
                                // 用户名或者密码不能为空
                                $(".erro-tips").text(data.message);
                                $(".erro-tips").show();
                            }else if(data.code == "502"){
                                // 您输入的账号不存在，请核对后重新输入
                                $(".erro-tips").text(data.message);
                                $(".erro-tips").show();
                                $("#managerpassword").val("")
                            }else if(data.code == "503"){
                                // 您输入的密码有误，请核对后重新输入
                                $(".erro-tips").text(data.message);
                                $(".erro-tips").show();
                                $("#managerpassword").val("")
                            }else if(data.code == "500"){
                                // 服务器出现异常,请稍后再试
                                $(".erro-tips").text(data.message);
                                $(".erro-tips").show();
                            }else if(data.code == "200"){
                                // 登录成功
                                $(".erro-tips").hide();
                                location.href = "${ctx}/main.html";
                            }
                        }
                    });
                } else {
                    $(".erro-tips").text("用户名或密码不能为空!");
                    $(".erro-tips").show();
                }
            }

            //找回密码弹窗
            function  forget() {
                layer.open({
                    title:'手机号码验证',
                    closeBtn:'1',
                    type:1,
                    anim: 5,
                    area:['400px','200px'],
                    shadeClose: false, //点击遮罩关闭
                    content: $('#addLay'),
                });
            }

            //获取验证码
            function  getCode() {
                var adminPN = $("#adminPN").val();
                $.ajax({
                    type : "get",
                    headers: {rqSide: ex.pc()},
                    contentType : "application/json;charset=utf-8",
                    url: "${ctx}/msm/verificationCode?mobileNumber="+ adminPN +"_2"
                });
            }

            //重置密码弹窗
            function  phoneVerification() {
                var adminPN = $("#adminPN").val();
                var verifyCode = $("#verifyCode").val();
                $.ajax({
                    type : "post",
                    dataType : "json",
                    headers: {rqSide: ex.pc()},
                    data : JSON.stringify({"adminPN":adminPN,"verifyCode":verifyCode}),
                    contentType : "application/json;charset=utf-8",
                    url: "${ctx}/phoneVerification",
                    success : function(datas){
                        if(datas.code != "0"){
                            alertBtn(datas.message)
                        }else{
                            layer.open({
                                title:'设置新密码',
                                closeBtn:'1',
                                type:1,
                                anim: 5,
                                area:['400px','200px'],
                                shadeClose: false, //点击遮罩关闭
                                content: $('#updateLay'),
                            });
                        }
                    }
                });
            }

            //重置密码
            function  resetPassword() {
                var adminPN = $("#adminPN").val();
                var adminPW = $("#password").val();
                $.ajax({
                    type: "post",
                    dataType: "json",
                    headers: {rqSide: ex.pc()},
                    data: JSON.stringify({"adminPN": adminPN,"adminPW": adminPW}),
                    contentType: "application/json;charset=utf-8",
                    url: "${ctx}/resetPassword",
                    success : function(datas){
                        if(datas.code != "0"){
                            alertBtn(datas.message)
                        }else{
                            layer.closeAll();
                            alertBtn("重置密码成功！")
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