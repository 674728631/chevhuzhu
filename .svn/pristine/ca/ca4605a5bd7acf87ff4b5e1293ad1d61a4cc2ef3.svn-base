'use strict';

(function () {
  var page_data = {
    '1': {
      'loading': false,
      'inited': false,
      'nomore': false,
      'page': 1
    },
    '2': {
      'loading': false,
      'inited': false,
      'nomore': false,
      'page': 1
    }
  };
  var page_type = 1;
  var top_type = 1;
  var list_tpl = juicer($('#list-tpl').html());
  var tbox;

  loadData();
  function loadData() {
    showLoad();
    goAPI({
      url: _api.publicity,
      success: function success(data) {
        data = data.data;
        $('#container').html(juicer($('#main-tpl').html(), data));
        var top_height = $('#top').height();
        var type_height = $('#type-box2').height();
        var top_limit = type_height - top_height;
        var top_triger = top_height - type_height;
        $('#container').css({ 'padding-top': top_height });
        $('#type-box2').on('click', 'a', function () {
          var $this = $(this);
          if (!$this.hasClass('active')) {
            $this.addClass('active').siblings().removeClass('active');
            tbox.switchTo($this.index());
          }
        });
        if (loadStorage('publicity_type')) {
          page_type = loadStorage('publicity_type');
        } else {
          if (data.publicityCount == 0 && data.finishCount > 0) {
            $('#type-box2 a[data-value="' + 2 + '"]').addClass('active');
            page_type = 2;
          } else {
            $('#type-box2 a[data-value="' + 1 + '"]').addClass('active');
            page_type = 1;
          }
          saveStorage('publicity_type', page_type);
        }

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
          event_scroll: function event_scroll(index, dis, dir) {
            //if (dir === -1 && dis >= top_height) {
            if (dir === -1) {
              $('#top').css({
                'webkit-transform': 'translateZ(0) translateY(' + top_limit + 'px)',
                'transform': 'translateZ(0) translateY(' + top_limit + 'px)'
              });
              //} else if (dir === 1 && dis < top_height) {
            } else {
              $('#top').css({
                'webkit-transform': 'translateZ(0) translateY(0)',
                'transform': 'translateZ(0) translateY(0)'
              });
            }
            //if (dir === -1 && dis >= top_height && top_type == 1) {
            if (dir === -1 && top_type == 1) {
              top_type = -1;
              $('#container').css({ 'padding-top': type_height });
              //tbox.scrollTo(index, dis - top_triger);
              //} else if (dir === 1 && dis < top_height - 150 && top_type === -1) {
            } else if (dir === 1 && top_type === -1) {
              top_type = 1;
              $('#container').css({ 'padding-top': top_height });
            }
          },
          event_scrollBottom: function event_scrollBottom(index, dis) {
            if (page_data[page_type].loading || page_data[page_type].nomore) return;
            loadList();
          },
          event_switch: function event_switch(index, lastIndex) {
            var $item = $('#type-box2 a').eq(index);
            $item.addClass('active').siblings().removeClass('active');
            page_type = $item.data('value');
            saveStorage('publicity_type', page_type);
            if (!page_data[page_type].inited) {
              loadList();
            }
          }
        });
        tbox.switchTo($('#type-box2 a[data-value="' + page_type + '"]').index(), { animation: false, event: true });
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
      }
    });
  }

  function loadList(emptyAll) {
    if (page_data[page_type].loading) return;
    showLoad();
    page_data[page_type].loading = true;
    if (emptyAll) {
      page_data[page_type].page = 1;
      page_data[page_type].nomore = false;
    }
    goAPI({
      url: _api.publicity_list,
      data: {
        flag: page_type,
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
          // 老板说把这个事件挪到列表最后去
          $('.e-20180508095042617326').appendTo(tbox.activeTab().find('.list-box'));
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