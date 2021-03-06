'use strict';

(function () {
  var loading = false;
  var user = checkLogin();
  var oid = getUrlPar('id');
  var data_order;
  var data_car;
  var data_imgs = [];
  var btn_join;
  var preview_imgs = preview({
    fixed: true,
    background: '#000000',
    number: true,
    point: true,
    fill: 'fix'
  });

  if (!oid) {
    showConfirm({
      str: '缺少事件ID',
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

  // 加载页面
  function loadData() {
    showLoad();
    goAPI({
      url: _api.publicity_detail,
      data: {
        eventNo: oid
      },
      success: function success(data) {
        data_order = data.data;
        if (user) {
          loadCar(function (dt) {
            data_car = dt.list;
            initData();
          });
        } else {
          initData();
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
      }
    });
  }

  // 初始化页面数据
  function initData() {
    if (data_order.commentLabelContent) {
      data_order.commentLabelContent = data_order.commentLabelContent.split('_');
    } else {
      data_order.commentLabelContent = null;
    }
    if ($.type(data_order.accidentImg) === 'string') {
      data_order.accidentImg = data_order.accidentImg.split('_');
    } else if (!data_order.accidentImg) {
      data_order.accidentImg = [];
    }
    if ($.type(data_order.assertImg) === 'string') {
      data_order.assertImg = data_order.assertImg.split('_');
    } else if (!data_order.assertImg) {
      data_order.assertImg = [];
    }
    if ($.type(data_order.repairImg) === 'string') {
      data_order.repairImg = data_order.repairImg.split('_');
    } else if (!data_order.repairImg) {
      data_order.repairImg = [];
    }
    if ($.type(data_order.carPhotos) === 'string') {
      data_order.carPhotos = data_order.carPhotos.split('_');
    } else if (!data_order.carPhotos) {
      data_order.carPhotos = [];
    }
    if ($.isArray(data_car) && data_car.length > 0) {
      btn_join = { num: data_car.length };
      if (data_car.length === 1) {
        btn_join['car'] = data_car[0];
      } else {
        var all_clear = false;
        for (var i = 0, j = data_car.length; i < j; i++) {
          if (data_car[i].status == 20) {
            all_clear = true;
            break;
          }
        }
        btn_join['all_clear'] = all_clear;
      }
    } else {
      btn_join = {
        num: 0
      };
    }
    data_order['btn_join'] = btn_join;

    $('.container').html(juicer($('#main-tpl').html(), data_order));

    // 浏览大图
    $('.photo-box').on('click', '.photo-item', function () {
      var $this = $(this);
      var index = $this.index();
      var imgs = [];
      $this.closest('.photo-box').find('.photo-img img').each(function () {
        imgs.push($(this).attr('src'));
      });
      if (_config.is_wx) {
        if (_config.wx_config) {
          wx.previewImage({
            current: imgs[index],
            urls: imgs
          });
        }
      } else {
        preview_imgs.show({
          imgs: imgs,
          index: index + 1
        });
      }
    });

    tabbox({
      container: '.tabbox',
      height: '100%',
      pull: false,
      wheel_self: false,
      event_scroll: function event_scroll(index, dis, dir) {
        if (dir === -1) {
          $('#btn-join').removeClass('close');
        } else if (dir === 1) {
          $('#btn-join').addClass('close');
        }
      }
    });

    if (_config.is_wx) {
      if (!_config.wx_config) {
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

      checkFollow({
        success: function success(data) {
          if (data.data == 1) {
            $('.btn-join').off('click');
          } else {
            $('.btn-join').off('click').on('click', function (e) {
              e.preventDefault();
              showConfirm({
                str: '扫码认识我',
                cover_close: true,
                node: '<img src="/hfive/img/chevhuzhu_qr_s_test.jpg" style="width:3rem;height:3rem;">',
                btn_close: true,
                btn_yes: {
                  str: null
                }
              });
            });
          }
        }
      });
    }
  }

  // 获取车辆列表
  function loadCar(cbk) {
    goAPI({
      url: _api.car_list,
      data: {
        token: user.token,
        pageSize: 1000
      },
      success: function success(data) {
        if ($.isFunction(cbk)) {
          cbk(data.data);
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
      }
    });
  }
})();