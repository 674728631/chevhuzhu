"use strict";!function(){var t,e,i,n,s=checkLogin(),r=0,a=[],o=[],c=!1,l=null,m=juicer($("#main-tpl").html()),f=getUrlPar("fromid");function h(t){if(t)try{e&&(e.close(),l=null),(e=new EventSource(_api.newmsg+"?token="+t)).onmessage=function(t){if("null"===t.data.trim())e.close();else{var i=t.data.trim().split("|");if(i.length>0){var n=i.length-1;if(""!==i[n]){var s=i[n].split("_");o.push({content:s[0],type:s[1],val:s[2],id:s[3]})}!function(){if(0===o.length)return;var t=o.splice(0,1);if(l){if(l.id===t[0].id)return;$("#msg-item").removeClass("active"),setTimeout(function(){31==t[0].type?$("#msg-item").attr("href","/hfive/view/car.html?mid="+t[0].id):41==t[0].type?t[0].val?$("#msg-item").attr("href","/hfive/view/order_detail.html?mid="+t[0].id+"&id="+t[0].val):$("#msg-item").attr("href","/hfive/view/order.html"):$("#msg-item").attr("href","javascript:;"),$("#msg-content").text(t[0].content),$("#msg-item").addClass("active"),l=t[0]},500)}else 31==t[0].type?$("#msg-item").attr("href","/hfive/view/car.html?mid="+t[0].id):41==t[0].type?t[0].val?$("#msg-item").attr("href","/hfive/view/order_detail.html?mid="+t[0].id+"&id="+t[0].val):$("#msg-item").attr("href","/hfive/view/order.html"):$("#msg-item").attr("href","javascript:;"),$("#msg-content").text(t[0].content),$("#msg-item").addClass("active"),l=t[0]}()}}}}catch(t){}}function d(){if(!c&&0!==a.length){c=!0;var t=a.splice(0,1);$("#newer-avatar").css({width:"auto",height:"auto"}).attr("src",t[0].avatar),$("#newer-desc").text(t[0].desc),$("#newer-item").addClass("active"),setTimeout(function(){$("#newer-item").removeClass("active"),setTimeout(function(){c=!1,a.length>0&&d()},500)},3500)}}function v(){showLoad(),goAPI({url:_api.index,data:{token:s?s.token:""},success:function(e){if(e=e.data,$.isArray(e.carList)&&e.carList.length>0)if(r=0,t={num:e.carList.length},1===e.carList.length)t.car=e.carList[0];else{for(var o=!0,l=0,u=e.carList.length;l<u;l++)if(13!==e.carList[l].status&&20!==e.carList[l].status){o=!1;break}t.all_clear=o}else t={num:0};if(e.btn_join=t,$(".container").html(m.render(e)),loadStorage("index_welcome",!0)||(saveStorage("index_welcome",1,!0),loginCoupinEvent(function(){showConfirm({class:"tl",str:"我只想问你们一个问题！请你们认真回答我。如果遇到一个不要房子、不要车、不要存款、不要钻戒，不要你请吃饭、看电影、买东西、不会骚扰你！只想在你的车子擦刮的时候帮你免费修车，还上门接车，让你省下一年1000多的保险费。这样的理赔管家，让你给9元你觉得过分吗？",btn_yes:{str:"不过分"},btn_no:{str:"过分"}})})),t.num>0&&function t(e){n&&clearTimeout(n),n=setTimeout(function(){(r-=1.4)<1.4*(1-e)&&(r=0),$("#car-content").css({"webkit-transform":"translateY("+r+"rem) translateZ(0)",transform:"translateY("+r+"rem) translateZ(0)"}),t(e)},4500)}($("#car-box .car-item").length),e.randomCar&&!loadStorage("index_newer")){var g=e.randomCar.split("_");a.push({desc:g[0],avatar:g[1]?g[1]:"/hfive/img/default_avatar_1.png"}),d(),saveStorage("index_newer",1)}$(".intro-list2").on("click",".intro-btn",function(){$(this).closest(".intro-item").toggleClass("active")}),tabbox({container:".tabbox",height:"100%",pull:!0,exception_class:".no-scroll",event_pullMove:function(t,e){t>=40?($(".tabbox-refresh .txt").text("松开刷新"),$(".tabbox-refresh .arr3").addClass("uturn")):($(".tabbox-refresh .txt").text("下拉刷新"),$(".tabbox-refresh .arr3").removeClass("uturn"))},event_pullEnd:function(t,e){t>=40&&($(".tabbox-refresh .txt").text("下拉刷新"),$(".tabbox-refresh .arr3").removeClass("uturn"),v())},event_scroll:function(t,e,i){-1===i?($(".navbar-bottom").addClass("close"),$("#btn-join").removeClass("close"),$("#btn-manager").removeClass("close")):1===i&&($(".navbar-bottom").removeClass("close"),$("#btn-join").addClass("close"),$("#btn-manager").addClass("close"))}}),function(){try{i&&(i.close(),c=!1),(i=new EventSource(_api.newer)).onmessage=function(t){var e=t.data.trim().split("|");if(e.length>0){for(var i=0,n=e.length;i<n;i++)if(""!==e[i]){var s=e[i].split("_"),r=s[0],o=s[1]?s[1]:"/hfive/img/default_avatar_1.png";a.push({desc:r,avatar:o})}d()}}}catch(t){}}(),h(s.token),function(){var t=$("#scroll-panel"),e=t.closest(".intro-fbox").siblings(".intro-desc"),i=(t.find(".intro-item").length,!1),n=4,s=0,r=0;function a(i){(r=i-s)<0||r>n-1?r<0?r=0:r>n-1&&(r=n-1):(e.removeClass("s1 s2 s3 s4").addClass("s"+(r+1)).find("p").removeClass("show").filter(".p"+(s+r+1)).addClass("show"),t.find(".intro-item").removeClass("active").eq(s+r).addClass("active"))}$("#scroll-box").on("swipeLeft",function(){a(r+1)}).on("swipeRight",function(){a(r-1)}),t.on("click",".intro-item",function(){var t=$(this);t.hasClass("active")||i||a(t.index())})}(),_config.is_wx&&(_config.wx_config||configWXJSSDK(_config.wx_share,{success:function(){shareWX({title:_share_title,desc:_share_desc,link:_share_link})}}),checkFollow({success:function(t){1==t.data?$(".btn-join").off("click"):($(".btn-join").off("click").on("click",function(t){t.preventDefault(),showConfirm({str:"扫码认识我",cover_close:!0,node:'<img src="/hfive/img/chevhuzhu_qr_s.jpg" style="width:3rem;height:3rem;">',btn_close:!0,btn_yes:{str:null}})}),f&&showConfirm({str:"关注公众号<br>我们一起领取500元额度",cover_close:!0,node:'<img src="/hfive/img/chevhuzhu_qr_s.jpg" style="width:3rem;height:3rem;">',btn_close:!0,btn_yes:{str:null}}))}}))},error:function(t){showConfirm({str:t,btn_yes:{str:"重新加载",event_click:v}})},complete:function(){hideLoad()}})}f&&saveStorage("from_id",f,!0),v()}();