<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"></c:set>

<meta name="keywords" content="车V互助后台管理系统"/>
<meta name="description" content="车V互助后台管理系统"/>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="author" content="车V互助">
<link rel="shortcut icon" href="${ctx}/cite/images/vlogo.png" type="image/x-icon">
<link rel="stylesheet" href="${ctx}/cite/css/bootstrap.css">
<link rel="stylesheet" href="${ctx}/cite/css/sb-admin.css" >
<link rel="stylesheet" href="${ctx}/cite/plugins/font-awesome/css/font-awesome.min.css">
<link rel="stylesheet" href="${ctx}/cite/css/index.css">
<link rel="stylesheet" href="${ctx}/cite/css/paging.css">
<%--<link rel="stylesheet" href="${ctx}/cite/css/ft-carousel.css">--%>
<link rel="stylesheet" href="${ctx}/cite/css/iconfont.css" type="text/css"/>
<link rel="stylesheet" href="${ctx}/cite/css/newSettle.css" type="text/css"/>
<link rel="stylesheet" href="${ctx}/cite/css/fileUpload.css" type="text/css">
<link rel="stylesheet" href="${ctx}/cite/css/swiper-3.4.2.min.css">
<link rel="stylesheet" href="${ctx}/cite/css/chart.css" type="text/css">
<!-- JavaScript -->
<script type="text/javascript" src="${ctx}/cite/js/extends/jquery-1.10.2.js"></script>
<script type="text/javascript" src="${ctx}/cite/js/extends/bootstrap.js"></script>
<script type="text/javascript" src="${ctx}/cite/plugins/layer/layer.js"></script>
<script type="text/javascript" src="${ctx}/cite/js/extends/paging.js"></script>
<script type="text/javascript" src="${ctx}/cite/js/extends/ft-carousel.min.js"></script>
<script type="application/javascript" src="${ctx}/cite/plugins/swiper-3.4.2.jquery.min.js"></script>
<script type="application/javascript" src="${ctx}/cite/plugins/e-smart-zoom-jquery.min.js"></script>
<script type="text/javascript" src="${ctx}/cite/js/extends/fileUpload.js"></script>
<script type="text/javascript" src="${ctx}/cite/js/extends/imgScale.js"></script>
<script type="text/javascript" src="${ctx}/cite/js/module/extend.js"></script>
<script type="text/javascript" src="${ctx}/cite/js/extends/echarts.js"></script>
<script type="text/javascript">
    exC();
    function exC(){
        $.ajax({
            type : "post",
            url: "${ctx}/exBypass/isSession",
            contentType : "application/x-www-form-urlencoded; charset=UTF-8",
            async : false,
            dataType : "json",
            headers: {rqSide: 'Q0JILVBD'},
            success: function (datas) {
                if(datas == 0 || datas == "0"){
                    ex.loginUrl("");
                    return false;
                }else{
                    return true;
                }
            }
        });
    }
</script>
