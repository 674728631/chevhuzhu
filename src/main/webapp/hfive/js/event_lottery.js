"use strict";!function(){var e,t,i=!1,n={"001":"/hfive/view/event_join.html?sid=163&cid=201809280939198B3p3P","002":"/hfive/view/event_join.html?sid=167&cid=20181023164903l10cRX","003":"/hfive/view/event_join.html?sid=100001&cid=2018103010150664WQD6"},s=getUrlPar("id")||"001",r=n[s]?n[s]:"/hfive/view/index.html";if(_config.is_wx){wxOauth();var o={index:-1,count:8,timer:0,speed:20,times:0,cycle:15,prize:-1,init:function(i){$("#"+i).find(".lottery-unit").length>0&&(e=$("#"+i),t=e.find(".lottery-unit"),this.obj=e,this.count=t.length,e.find(".lottery-unit.lottery-unit-"+this.index).addClass("active"))},roll:function(){var e=this.index,t=this.count,i=this.obj;return $(i).find(".lottery-unit.lottery-unit-"+e).removeClass("active"),(e+=1)>t-1&&(e=0),$(i).find(".lottery-unit.lottery-unit-"+e).addClass("active"),this.index=e,!1},stop:function(e){return this.prize=e,!1}};o.init("lottery"),$(".draw-btn").on("click",function(){i||(i=!0,goAPI({url:_api.lottery,data:{drawNum:s},success:function(e){d(),o.stop(2)},error:function(e,t){showConfirm({str:e})},complete:function(){i=!1}}))})}else showConfirm({str:"请在微信客户端中打开此页面",btn_yes:{str:null}});function d(){return o.times+=1,o.roll(),o.times>o.cycle+10&&o.prize==o.index?(setTimeout(function(){$("#result").removeClass("hide").find("a").attr("href",r),setTimeout(function(){$("#result .main").addClass("active")},10)},o.speed),o.speed=20,o.prize=-1,o.times=0):(o.times<o.cycle?o.speed-=10:o.times>o.cycle+10&&(0==o.prize&&7==o.index||o.prize==o.index+1)?o.speed+=20:o.speed+=40,o.speed<90&&(o.speed=90),o.timer=setTimeout(d,o.speed)),!1}}();