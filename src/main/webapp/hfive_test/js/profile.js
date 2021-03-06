'use strict';

(function () {
  var loading = false;
  var car_v2 = false;
  var user = checkLogin();
  var user_data;
  var tpl = juicer($('#main-tpl').html());
  if (user) {
    loadData();
  } else {
    $('.container').html(tpl.render({ login: false }));
    initTabBox(false);
  }

  function loadData() {
    if (loading) return;
    showLoad();
    loading = true;
    goAPI({
      url: _api.user,
      data: {
        token: user.token
      },
      success: function success(data) {
        user_data = data.data;
        if (!$.isArray(user_data.carList)) {
          user_data.carList = [];
        }
        // $.each(user_data.carList, function(index, item){
        // if (item.levela == 2) {
        // car_v2 = true;
        // return false;
        // }
        // });
        user_data.carList.reverse();
        var carFirst = user_data.carList[0];
        var carCharge = null;
        for (var i = 0, j = user_data.carList.length; i < j; i++) {
          if (user_data.carList[i].id < carFirst.id) {
            carFirst = user_data.carList[i];
          }
          if (!carCharge && user_data.carList[i].status == 20 && user_data.carList[i].typeGuarantee != 2 && user_data.carList[i].amtCooperation < 1) {
            carCharge = user_data.carList[i];
          }
        }
        $.extend(user_data, { associatorId: user.associatorId, phone: user.phone, login: true, car_v2: car_v2, carCharge: carCharge, carFirst: carFirst });
        $('.container').html(tpl.render(user_data));
        var car_length = $('#card-content .car-item, #card-content .car-card').length;
        if (car_length > 1) {
          $('#card-content').css('width', car_length * 5.2 + (car_length - 1) * 0.4 + 0.6 + 'rem');
          initCardBox();
        } else {
          $('#card-content').css('width', '100%');
        }
        initTabBox(true);

        if (_config.is_wx && !_config.wx_config) {
          configWXJSSDK(_config.wx_share, {
            success: function success() {
              shareWX({
                title: _share_title,
                desc: _share_desc,
                link: _share_link
              });
            }
          });
        }
      },
      error: function error(data) {
        showConfirm({
          str: data,
          btn_yes: {
            str: '重新加载',
            event_click: loadData
          }
        });
      },
      complete: function complete() {
        hideLoad();
        loading = false;
      }
    });
  }

  function initTabBox(pull) {
    tabbox({
      container: '.tabbox',
      height: '100%',
      pull: pull,
      wheel_self: false,
      exception_class: $('#card-content .car-item, #card-content .car-card').length > 1 ? '#card-box' : '',
      event_pullMove: function event_pullMove(dis, index) {
        if (dis >= 40) {
          $('.tabbox-refresh .txt').text('松开刷新');
          $('.tabbox-refresh .arr3').addClass('uturn');
        } else {
          $('.tabbox-refresh .txt').text('下拉刷新');
          $('.tabbox-refresh .arr3').removeClass('uturn');
        }
      },
      event_pullEnd: function event_pullEnd(dis, index) {
        if (dis >= 40) {
          $('.tabbox-refresh .txt').text('下拉刷新');
          $('.tabbox-refresh .arr3').removeClass('uturn');
          loadData();
        }
      }
    });
  }

  function initCardBox() {
    var startX = 0;
    var tempX = 0;
    var disX = 0;
    var baseX = 0;
    var width = $window.width() - $('#card-content').width();
    if ($('#card-content .car-card').length > 1) {
      $('#card-content').on('swipeLeft', function () {
        $('#card-content .car-card').eq(0).removeClass('active');
        $('#card-content .car-card').eq(1).addClass('active');
        $('#card-content').css({
          'webkit-transform': 'translateX(' + width + 'px) translateY(0) translateZ(0)',
          'transform': 'translateX(' + width + 'px) translateY(0) translateZ(0)'
        });
      }).on('swipeRight', function () {
        $('#card-content .car-card').eq(0).addClass('active');
        $('#card-content .car-card').eq(1).removeClass('active');
        $('#card-content').css({
          'webkit-transform': 'translateX(0px) translateY(0) translateZ(0)',
          'transform': 'translateX(0px) translateY(0) translateZ(0)'
        });
      });
      // $('#card-content').on('touchstart', function(e){
      // startX = e.touches ? e.touches[0].pageX : e.pageX;
      // $('#card-content .car-card').removeClass('active');
      // }).on('touchmove', function(e){
      // e.preventDefault();
      // tempX = e.touches ? e.touches[0].pageX : e.pageX;
      // disX = baseX + (tempX - startX);
      // if (disX > 0) {
      // disX = 0;
      // } else if (disX < width) {
      // disX = width;
      // }
      // $('#card-content').css({
      // 'webkit-transform': 'translateX(' + disX + 'px) translateY(0) translateZ(0)',
      // 'transform': 'translateX(' + disX + 'px) translateY(0) translateZ(0)'
      // });
      // }).on('touchend', function(){
      // baseX = disX;
      // console.log($('#card-content').width() - $window.width());
      // console.log(-disX * 2);
      // if ($('#card-content').width() - $window.width() < -disX * 2) {
      // $('#card-content .car-card').eq(1).addClass('active');
      // } else {
      // $('#card-content .car-card').eq(0).addClass('active');
      // }
      // });
    }
  }
})();