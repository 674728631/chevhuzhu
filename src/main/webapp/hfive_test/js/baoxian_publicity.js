'use strict';

(function () {
  var page_data = {
    '1': {
      'loading': false,
      'inited': false,
      'nomore': false,
      'page': 1
    }
  };
  var page_type = 1;
  var list_tpl = juicer($('#list-tpl').html());
  var tbox;

  $('#container').html(juicer($('#main-tpl').html(), null));

  tbox = tabbox({
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
    }
  });

  loadList(true);
  function loadList(emptyAll) {
    if (page_data[page_type].loading) return;
    showLoad();
    page_data[page_type].loading = true;
    if (emptyAll) {
      page_data[page_type].page = 1;
      page_data[page_type].nomore = false;
    }
    goAPI({
      url: _api.baoxian_publicity_list,
      data: {
        //flag: page_type,
        pageNo: page_data[page_type].page
      },
      success: function success(data) {
        data = data.data;
        page_data[page_type].inited = true;
        if (emptyAll) {
          tbox.activeTab().find('.list-box').empty();
        }
        if (data.total == 0) {
          page_data[page_type].nomore = true;
          tbox.activeTab().addClass('full');
          tbox.activeTab().find('.null-info').removeClass('hide');
        } else {
          page_data[page_type].page += 1;
          tbox.activeTab().removeClass('full');
          tbox.activeTab().find('.null-info').addClass('hide');
          tbox.activeTab().find('.list-box').append(list_tpl.render(data));
          if (!data.hasNextPage) {
            page_data[page_type].nomore = true;
          }
        }
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
      error: function error(msg) {
        showConfirm({
          str: msg
        });
      },
      complete: function complete() {
        hideLoad();
        page_data[page_type].loading = false;
      }
    });
  }
})();