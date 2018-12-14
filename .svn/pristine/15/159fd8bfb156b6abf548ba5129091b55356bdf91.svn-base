<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"></c:set>

<div class="collapse navbar-collapse navbar-ex1-collapse">
    <div class="panel-group nav navbar-nav side-nav" id="leftNav"></div>

    <!--顶部 右侧信息提示-->
    <ul class="ht-nav">
        <li class="ht-nav-item" id="exitBtn">
            <span class="user-logo"><img src="${ctx}/cite/images/logoo.jpg" alt="" class="img-responsive"></span>
            <a href="javascript:void(0)" class="user-nc">
                ${userInfo.adminUN == null?userInfo.adminPN:userInfo.adminUN}
                <i class="fa fa-angle-down hover-icon" style="font-size: 22px; color: grey;margin-left: 8px;margin-top: 20px;"></i>
            </a>
            <!--退出登录-->
            <div id="exitBox">
                <ul>
                    <li>
                        <a href="#" onclick="clearUserInfo();" target="_self">
                            退出登录
                        </a>
                    </li>
                </ul>
            </div>
        </li>
    </ul>
    <script type="text/javascript">
        function clearUserInfo() {
            layer.open({
                title:'信息提示',
                type: 1,
                btn:['确定','取消'],
                btnAlign: 'r',
                area: ['280px'],
                shadeClose: false, //点击遮罩关闭
                content: '<p class="is-layer">您确定要退出吗？</p>',
                yes:function(layero){
                    $.ajax({
                        type : "post",
                        url: "${ctx}/exBypass/exitLogin",
                        contentType : "application/x-www-form-urlencoded; charset=UTF-8",
                        async : false,
                        dataType : "json",
                        headers: {rqSide: 'Q0JILVBD'},
                        success: function (datas) {}
                    });
                    self.location.href = "${ctx}/boss";
                    layer.close();
                }
            });
        }

        var roteNumber = 0,oneMenuNuber = 0;
        function msClick(id){
            sessionStorage.setItem("demokey",id);
            $(".panel-collapse").removeClass("in");
        }

        function rote(labelVal){
            var i = $(labelVal).attr("class").match(/(\d)+/g);
            if(roteNumber == 0){
                roteNumber = 1;
                $(labelVal).parents(".panel-heading").addClass("openNav");
                $(labelVal).find(".nav-icon").addClass("change-bac-icon" + i);
            }else{
                roteNumber = 0;
                $(".panel-heading").removeClass("openNav");
                for(var j = 0;j < oneMenuNuber;j++){
                    $("a .nav-icon").removeClass("change-bac-icon" + j);
                }
            }
            $(labelVal).find(".hover-icon").toggleClass("rote");
        }

        $("#exitBtn").on({
            mouseover: function () {
                $("#exitBox").show();
                $(".hover-icon").css({
                    "-webkit-transform": "rotate(180deg)",
                    "-webkit-transition": "transform 0.2s ease-out;"
                })
            },
            mouseout: function () {
                $("#exitBox").hide();
                $(".hover-icon").css({
                    "-webkit-transform": "rotate(0deg)",
                    "-webkit-transition": "transform 0.2s ease-out;"
                })
            }
        });

        loadMenu();
        function loadMenu(){
            var strJson = {};
            $.ajax({
                type : "post",
                dataType : "json",
                headers: {rqSide: ex.pc()},
                data : JSON.stringify(strJson),
                contentType : "application/json;charset=utf-8",
                url: "${ctx}/loadLeftMenu",
                success : function(datas){
                    if(datas.code == "0"){
                        var allMenus = datas.data.leftMenu;
                        var dataHtml = "";
                        for(var i=0;i < allMenus.length;i++){
                            var menuSpecies = allMenus[i];
                            var parentMenu = menuSpecies.parentMenu;
                            var childMenus = menuSpecies.childMenu;
                            if(!ex.judgeEmpty(childMenus)){
                                dataHtml += "<div class='panel panel-default'>" +
                                    "<div class='panel-heading'>" +
                                    "<h4 class='panel-title'>" +
                                    "<a id='M-" + parentMenu.id + "' href='${ctx}" + parentMenu.url + "' onclick='msClick(this.id);'>" +
                                    "<span class='"+ parentMenu.icon +"'></span>" +
                                    "<span class='nav-title'>" + ex.judgeEmpty(parentMenu.name) + "</span>" +"</a></h4></div></div>";
                            }else {
                                var demokey = sessionStorage.getItem("demokey");
                                dataHtml += "<div class='panel panel-default'>" +
                                    "<div class='panel-heading "+ (!demokey && parentMenu.id =="1"?"openNav":"") +"'>" +
                                    "<h4 class='panel-title'>" +
                                    "<a class='pa-a" + (i + 1) + "' data-toggle='collapse' href='#left" + (i+1) + "' onclick='rote(this)'>" +
                                    "<span class='"+ parentMenu.icon +"'></span>" +
                                    "<span class='nav-title'>" + ex.judgeEmpty(parentMenu.name) + "</span>" +
                                    "<i class='fa fa-angle-down hover-icon rote'></i></a></h4></div>";
                                dataHtml +="<div id='left" + (i+1) + "' class='panel-collapse collapse "+ (!demokey && parentMenu.id =="1"? "in" : "") +"'>" +
                                    "<div class='panel-body'>" +
                                    "<ul class='son-nav'>";
                                for(var j=0;j < childMenus.length;j++){
                                    var childMenu = childMenus[j];
                                    dataHtml += "<li data-id='" + (i + 1) + "'  id='M-" + childMenu.parentId+ "-" +childMenu.id + "' class='m_s_click "+ (!demokey?"activeLi":"") +"' onclick='msClick(this.id);'><a href='${ctx}"+ childMenu.url + "'>" + childMenu.name +"</a></li>";
                                }
                                dataHtml += "</ul></div></div></div>";
                            }
                        }
                        $("#leftNav").html(dataHtml);
                        if(demokey){
                            if(demokey.split("-").length > 2){
                                //导航点击样式
                                $("#" + demokey).parents(".panel-collapse").addClass("in");
                                $("#" + demokey).parents(".panel-collapse").siblings(".panel-heading").addClass("openNav");
                                $("#" + demokey).addClass("activeLi");
                                var dataId = $("#" + demokey).attr("data-id");
                                $(".pa-a"+dataId).find(".hover-icon").toggleClass("rote");
                                $(".pa-a"+dataId).find(".nav-icon").toggleClass("change-bac-icon" + dataId);
                                roteNumber = 1;
                            }else{
                                $(".panel-collapse").removeClass("in");
                                $("#" + demokey).removeClass("activeLi");
                            }
                        }
                    }
                }
            });
        }
    </script>
</div>