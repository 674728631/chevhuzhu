"use strict";!function(){var t,e={1:{loading:!1,inited:!1,nomore:!1,page:1},2:{loading:!1,inited:!1,nomore:!1,page:1}},a=1,i=1,n=juicer($("#list-tpl").html());function o(i){e[a].loading||(showLoad(),e[a].loading=!0,i&&(e[a].page=1,e[a].nomore=!1),goAPI({url:_api.publicity_list,data:{flag:a,pageNo:e[a].page},success:function(o){o=o.data,e[a].inited=!0,i&&t.activeTab().find(".list-box").empty(),0==o.total?(e[a].nomore=!0,t.activeTab().addClass("full"),t.activeTab().find(".null-info").removeClass("hide")):(e[a].page+=1,t.activeTab().removeClass("full"),t.activeTab().find(".null-info").addClass("hide"),t.activeTab().find(".list-box").append(n.render(o)),o.hasNextPage||(e[a].nomore=!0),$(".e-20180508095042617326").appendTo(t.activeTab().find(".list-box"))),_config.is_wx&&!_config.wx_config&&configWXJSSDK(_config.wx_share,{success:function(){shareWX({title:_share_title,desc:_share_desc,link:_share_link})}})},error:function(t){showConfirm({str:t})},complete:function(){hideLoad(),e[a].loading=!1}}))}!function n(){showLoad();goAPI({url:_api.publicity,success:function(n){n=n.data,$("#container").html(juicer($("#main-tpl").html(),n));var s=$("#top").height(),r=$("#type-box2").height(),l=r-s;$("#container").css({"padding-top":s}),$("#type-box2").on("click","a",function(){var e=$(this);e.hasClass("active")||(e.addClass("active").siblings().removeClass("active"),t.switchTo(e.index()))}),loadStorage("publicity_type")?a=loadStorage("publicity_type"):(0==n.publicityCount&&n.finishCount>0?($('#type-box2 a[data-value="2"]').addClass("active"),a=2):($('#type-box2 a[data-value="1"]').addClass("active"),a=1),saveStorage("publicity_type",a)),(t=tabbox({container:".tabbox",height:"100%",pull:!0,wheel_self:!1,event_pullMove:function(t,e){t>=40?($(".tabbox-refresh").eq(e).find(".txt").text("松开刷新"),$(".tabbox-refresh").eq(e).find(".arr3").addClass("uturn")):($(".tabbox-refresh").eq(e).find(".txt").text("下拉刷新"),$(".tabbox-refresh").eq(e).find(".arr3").removeClass("uturn"))},event_pullEnd:function(t,e){t>=40&&($(".tabbox-refresh").eq(e).find(".txt").text("下拉刷新"),$(".tabbox-refresh").eq(e).find(".arr3").removeClass("uturn"),o(!0))},event_scroll:function(t,e,a){-1===a?$("#top").css({"webkit-transform":"translateZ(0) translateY("+l+"px)",transform:"translateZ(0) translateY("+l+"px)"}):$("#top").css({"webkit-transform":"translateZ(0) translateY(0)",transform:"translateZ(0) translateY(0)"}),-1===a&&1==i?(i=-1,$("#container").css({"padding-top":r})):1===a&&-1===i&&(i=1,$("#container").css({"padding-top":s}))},event_scrollBottom:function(t,i){e[a].loading||e[a].nomore||o()},event_switch:function(t,i){var n=$("#type-box2 a").eq(t);n.addClass("active").siblings().removeClass("active"),a=n.data("value"),saveStorage("publicity_type",a),e[a].inited||o()}})).switchTo($('#type-box2 a[data-value="'+a+'"]').index(),{animation:!1,event:!0})},error:function(t){showConfirm({str:t,btn_yes:{str:"重新加载",event_click:n}})},complete:function(){hideLoad()}})}()}();