'use strict';

(function () {
  var $document = $(document);
  var sid = getUrlPar('id');
  var list_tpl = juicer($('#list-tpl').html());
  var list_nomore = false;
  var list_page = 1;
  var list_data = {};
  var loading = false;

  if (!sid) {
    showConfirm({
      str: '缺少商家id',
      btn_yes: {
        str: '返回上一页',
        href: 'javascript:history.go(-1);'
      },
      btn_no: {
        str: '回到首页',
        href: '/hfive/view/index.html'
      }
    });
  } else {
    loadData();
  }

  function loadData() {
    showLoad();
    goAPI({
      url: _api.shop_detail,
      data: {
        id: sid
      },
      success: function success(data) {
        data = data.data;
        $('.container').html(juicer($('#main-tpl').html(), data));
        var imgs = data.img.split('_');
        var imgs_wx = $.map(imgs, function (item, index) {
          return (/null\?/.test(item) ? 'http://test.chevhuzhu.com/hfive/img/default_shop_2.png' : item
          );
        });
        imgs = $.map(imgs, function (item, index) {
          return {
            src: item,
            default: '/hfive/img/default_shop_2.png'
          };
        });
        if (imgs.length > 0) {
          var previewImgFixed = preview({
            background: '#000',
            fill: 'fix',
            point: true,
            number: true,
            imgs: imgs
          });
          var previewImg = preview({
            fixed: false,
            auto: true,
            dely: 3000,
            height: '100%',
            point: true,
            container: $('#img-box'),
            imgs: imgs,
            event_click: function event_click(index) {
              if (_config.is_wx) {
                if (_config.wx_config) {
                  var top_imgs = [];
                  $('#img-box .preview-img img').each(function () {
                    top_imgs.push($(this).attr('src'));
                  });
                  if (top_imgs.length > 2) {
                    top_imgs = top_imgs.slice(1, top_imgs.length - 2);
                  }
                  wx.previewImage({
                    current: imgs_wx[index],
                    urls: imgs_wx
                  });
                }
              } else {
                previewImgFixed.show({ index: index });
              }
            }
          });
          previewImg.show();
        }
        $window.on('scroll', function () {
          if (loading || list_nomore || $window.height() + $window.scrollTop() + 30 < $document.height()) return;
          loadList();
        });
        loadList(true);
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

  // 获取评价列表
  function loadList(emptyAll) {
    if (loading) return;
    showLoad();
    loading = true;
    if (emptyAll) {
      list_page = 1;
      list_nomore = false;
    }
    goAPI({
      url: _api.comment_list,
      data: {
        id: sid,
        pageNo: list_page
      },
      success: function success(data) {
        data = data.data;
        if (emptyAll) {
          $('#list-box').empty();
        }
        if (data.total == 0) {
          list_nomore = true;
        } else {
          list_page += 1;
          $('#list-box').append(list_tpl.render(data));
          if (!data.hasNextPage) {
            list_nomore = true;
          }
        }
        if (_config.is_wx && !_config.wx_config) {
          configWXJSSDK(['previewImage'].concat(_config.wx_share), {
            success: function success() {
              // shareWX({
              // title: _share_title,
              // desc: _share_desc,
              // link: _share_link
              // })
            }
          });
        }
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
        loading = false;
      }
    });
  }
})();