'use strict';

(function () {
  var loading = false;
  var user = checkLogin(true);
  var juicer_txt = function juicer_txt(str) {
    if (/-/.test(str)) {
      return '支';
    } else {
      return '充';
    }
  };
  juicer.register('build_txt', juicer_txt);
  var juicer_color = function juicer_color(str) {
    if (/-/.test(str)) {
      return 'red';
    } else {
      return 'green';
    }
  };
  juicer.register('build_color', juicer_color);

  var tbox = tabbox({
    container: '.tabbox',
    height: '100%',
    pull: true,
    wheel_self: false,
    event_pullMove: function event_pullMove(dis, index) {
      if (dis >= 40) {
        $('.tabbox-refresh').eq(index).find('.txt').text('松开刷新');
        $('.tabbox-refresh').eq(index).find('.arr3').addClass('uturn');
      } else {
        $('.tabbox-refresh').eq(index).find('.txt').text('下拉刷新');
        $('.tabbox-refresh').eq(index).find('.arr3').removeClass('uturn');
      }
    },
    event_pullEnd: function event_pullEnd(dis, index) {
      if (dis >= 40) {
        $('.tabbox-refresh').eq(index).find('.txt').text('下拉刷新');
        $('.tabbox-refresh').eq(index).find('.arr3').removeClass('uturn');
        loadData(true);
      }
    }
  });

  if (user) {
    loadData();
  }

  function loadData() {
    if (loading) return;
    showLoad();
    loading = true;
    goAPI({
      url: _api.wallet_history,
      data: {
        token: user.token
      },
      success: function success(data) {
        data = data.data;
        if ($.isArray(data) && data.length > 0) {
          $('#list-area').html(juicer($('#list-tpl').html(), { list: data }));
        } else {
          $('.tabbox-tab').addClass('full');
          $('.null-info').removeClass('hide');
        }
        // if (_config.is_wx && !_config.wx_config) {
        // configWXJSSDK(_config.wx_share, {
        // success: function() {
        // shareWX({
        // title: _share_title,
        // desc: _share_desc,
        // link: _share_link
        // })
        // }
        // })
        // }
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
})();