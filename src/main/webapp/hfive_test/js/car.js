'use strict';

(function () {
  var loading = false;
  var mid = getUrlPar('mid');
  var user = checkLogin(true);
  var list_tpl = juicer($('#list-tpl').html());
  var page_data = {
    '1': {
      'value': '20',
      'loading': false,
      'inited': false,
      'nomore': false,
      'page': 1
    },
    '2': {
      'value': '1,2,10,11,12,13,30,31',
      'loading': false,
      'inited': false,
      'nomore': false,
      'page': 1
    }
  };
  var page_type = page_data[getUrlPar('type')] ? getUrlPar('type') : '1';

  if (user) {
    loadList(true);
  }

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
        loadList(true);
      }
    },
    event_scrollBottom: function event_scrollBottom(index, dis) {
      if (page_data[page_type].loading || page_data[page_type].nomore) return;
      loadList();
    },
    event_switch: function event_switch(index, lastIndex) {
      var $item = $('#menu-box a').eq(index);
      $item.addClass('active').siblings().removeClass('active');
      page_type = $item.data('value');
      if (!page_data[page_type].inited) {
        loadList(true);
      }
    }
  });
  tbox.switchTo($('#menu-box a[data-value="' + page_type + '"]').index(), { animation: false, event: true });

  $('#menu-box').on('click', 'a', function () {
    var $this = $(this);
    if (!$this.hasClass('active')) {
      $this.addClass('active').siblings().removeClass('active');
      tbox.switchTo($this.index());
    }
  });

  function loadList(emptyAll) {
    if (page_data[page_type].loading) return;
    showLoad();
    page_data[page_type].loading = true;
    if (emptyAll) {
      page_data[page_type].page = 1;
      page_data[page_type].nomore = false;
    }
    goAPI({
      url: _api.car_list,
      data: {
        token: user.token,
        status: page_data[page_type].value,
        messageId: mid,
        pageNo: page_data[page_type].page
      },
      success: function success(data) {
        data = data.data;
        page_data[page_type].inited = true;
        if (emptyAll) {
          tbox.activeTab().find('.list-area').empty();
        }
        if (data.total == 0) {
          page_data[page_type].nomore = true;
          tbox.activeTab().addClass('full');
          tbox.activeTab().find('.null-info').removeClass('hide');
        } else {
          page_data[page_type].page += 1;
          data['lipei_max'] = _config.lipei_max;
          tbox.activeTab().removeClass('full');
          tbox.activeTab().find('.null-info').addClass('hide');
          tbox.activeTab().find('.list-area').append(list_tpl.render(data));
          if (!data.hasNextPage) {
            page_data[page_type].nomore = true;
          }
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
      error: function error(msg) {
        if (emptyAll) {
          showConfirm({
            str: msg,
            btn_yes: {
              str: '重新加载',
              event_click: function event_click() {
                loadList(emptyAll);
              }
            }
          });
        } else {
          showConfirm({
            str: msg
          });
        }
      },
      complete: function complete() {
        hideLoad();
        page_data[page_type].loading = false;
      }
    });
  }
})();