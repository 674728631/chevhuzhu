var ex={
    pc:function(){//标记为pc端
        return "Q0JILVBD";
    },
    isPC:function(){//判断是否为pc端
        // var userAgentInfo = navigator.userAgent;
        // var Agents = ["Android", "iPhone", "SymbianOS", "Windows Phone", "iPad", "iPod"];
        // var flag = true;
        // for (var v = 0; v < Agents.length; v++) {
        //     if (userAgentInfo.indexOf(Agents[v]) > 0) {
        //         flag = false;
        //         break;
        //     }
        // }
        // if(flag){
        //     window.location.href= "/login/";
        // }else{
        window.location.href= "/hfive/view/index.html";
        // }
    },
    loginUrl:function(value){
        if(value == null || value == "" || value == "[]" || value == "{}"){
            window.top.location = "/boss";
            return false;
        }
    },
    judgeEmptyOr0:function(value){//判断是否为空
        return null != value && "" != value && undefined != value?value:0;
    },
    judgeEmpty:function(value){//判断是否为空
        return null != value && undefined != value && "" != value?value:"";
    },
    reTime:function(type,date){//页面日期显示格式
        var d = new Date();
        if(ex.judgeEmpty(date))
            d.setTime(date);
        var month = d.getMonth()+1;
        var day = d.getDate();
        var hours = d.getHours();
        var minutes = d.getMinutes();
        var seconds = d.getSeconds();
        var output = d.getFullYear()+'-'+(month<10?'0':'')+month+'-'+(day<10?'0':'')+day+" "+(hours<10?'0':'')+hours+":"+(minutes<10?'0':'')+minutes+":"+(seconds<10?'0':'')+seconds;
        if(type == "i"){
            output = d.getFullYear()+'-'+(month<10?'0':'')+month+'-'+(day<10?'0':'')+day+" "+(hours<10?'0':'')+hours+":"+(minutes<10?'0':'')+minutes;
        }else if(type == "h"){
            output = d.getFullYear()+'-'+(month<10?'0':'')+month+'-'+(day<10?'0':'')+day+" "+(hours<10?'0':'')+hours;
        }else if(type == "d"){
            output = d.getFullYear()+'-'+(month<10?'0':'')+month+'-'+(day<10?'0':'')+day;
        }else if(type == "m"){
            output = d.getFullYear()+'-'+(month<10?'0':'')+month;
        }else if(type == "y"){
            output = d.getFullYear();
        }
        return output;
    },
    checkPhone:function(phone){//验证是否为正确的手机号格式
        if(!(/^1[345789]\d{9}$/.test(phone)))
            return true;
    },
    lpnv:function(value){//车牌号验证
        return !value.match("^[京,津,渝,沪,冀,晋,辽,吉,黑,苏,浙,皖,闽,赣,鲁,豫,鄂,湘,粤,琼,川,贵,云,陕,秦,甘,陇,青,台,内蒙古,桂,宁,新,藏,澳,军,海,航,警][A-Z][0-9,A-Z]{5}$");
    },
    getDays:function(dt, days) {
        if ($.type(dt) === 'string' && /^\d+$/.test(dt) || $.type(dt) === 'number') {
            dt = new Date(dt);
        } else if ($.type(dt) !== 'date') {
            return '0';
        }
        if ($.type(days) !== 'number') {
            days = 0;
        } else {
            days = Math.floor(days);
        }
        dt = new Date(dt.getFullYear(), dt.getMonth(), dt.getDate() + days, dt.getHours(), dt.getMinutes(), dt.getSeconds());
        var dt_now = new Date();
        var dt_left = dt - dt_now;
        if (dt_left <= 0) return 0;
        return Math.ceil(dt_left / 86400000);
    },
    calcDays:function(begin){
        var now = new Date();
        var days = now.getTime() - begin;
        var day = parseInt(days / (1000 * 60 * 60 * 24));
        return day;
    },
    calcDayBeginToEnd:function(begin,end){
        var days = end - begin;
        var day = parseInt(days / (1000 * 60 * 60 * 24));
        return day;
    },
    plusDateOneDay:function(dt){ //当前日期加一天 传入日期形式的时间
        try {
            return new Date(dt.getFullYear(), dt.getMonth(), dt.getDate() + 1);
        } catch(e) {
            return '';
        }
    },
    getDateString:function(dt){ //拿到字符串形式的时间
        try {
            return dt.getFullYear() + '-' + ((dt.getMonth() + 1) < 10 ? ('0' + (dt.getMonth() + 1)) : (dt.getMonth() + 1)) + '-' + (dt.getDate() < 10 ? ('0' + dt.getDate()) : dt.getDate());
        } catch(e) {
            return '';
        }
    },
    statusEvent:function(statusEvent){//将互助事件状态由int转为描述
        var result = statusEvent==1?"待审核":statusEvent==2?"未通过":statusEvent==3?"已通过":statusEvent==4?"待完善":
            statusEvent==10?"待分单":statusEvent==11?"待接单":statusEvent==12?"放弃接单":
                    statusEvent==21?"待定损": statusEvent==22?"定损待确认": statusEvent==31?"待接车":
                        statusEvent==51?"待维修":statusEvent==52?"维修中": statusEvent==61?"待交车":
                            statusEvent==71?"待评价":statusEvent==100?"已完成":
                                statusEvent==81?"投诉中":"未知";
        return result;
    },
    orderStatus:function(orderStatus){//将互助事件状态由int转为描述
        var result = orderStatus==1?"申请代办":orderStatus==2?"未通过":orderStatus==3?"已通过":orderStatus==4?"待完善":
            orderStatus==10?"待分单":orderStatus==11?"待接单":orderStatus==12?"放弃接单":
                    orderStatus==21?"待接车":orderStatus==31?"待定损": orderStatus==32?"待确认":
                        orderStatus==41?"待维修":orderStatus==42?"维修中":orderStatus==51?"待交车":
                            orderStatus==61?"待评价":orderStatus==71?"投诉中":orderStatus==100?"已完成":"未知";
        return result;
    },
    carStatus:function(carStatus){//将车辆状态状态由int转为描述
        var result = carStatus==1?"待支付":carStatus==2?"待添加照片":carStatus==10?"待审核":carStatus==12?"未通过":carStatus==13?"观察期":
            carStatus==20?"保障中":carStatus==30?"退出计划":carStatus==31?"不可用":"未知";
        return result;
    },
    rechargeType:function(type){//将车辆状态状态由int转为描述
        var result = type==1?"微信":type==2?"支付宝":type==3?"活动赠送":type==4?"后台充值":type==10?"车妈妈充值":"未知";
        return result;
    },
    complaintTypeUser:function(typeUser){//将车辆状态状态由int转为描述
        var result = typeUser==1?"商家":typeUser==2?"车主":typeUser==3?"其他":"未知";
        return result;
    },
    complaintTypeQuestion:function(typeQuestion){//将车辆状态状态由int转为描述
        var result = typeQuestion==1?"加入互助问题":typeQuestion==10?"理赔订单":typeQuestion==20?"扣款有误":typeQuestion==30?"保障期":
                typeQuestion==40?"充值":typeQuestion==100?"其他":"未知";
        return result;
    }
}