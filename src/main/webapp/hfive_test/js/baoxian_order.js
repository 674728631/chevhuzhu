'use strict';

(function () {
  var loading = false;
  var user = checkLogin(true);
  var list_tpl = juicer($('#list-tpl').html());
  var list_nomore = false;
  var list_page = 1;
  var list_data = {};
  var car_list = null;
  var car_loading = false;
  var car_date = Date.now();

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
      if (loading || list_nomore) return;
      loadList();
    }
  });

  function wxPay(dt, cbk) {
    if (loading) return;
    showLoad();
    loading = true;
    goAPI({
      url: _api.baoxian_order_pay,
      data: dt,
      traditional: true,
      success: function success(data) {
        wx.chooseWXPay({
          appId: data.data.appId,
          timestamp: data.data.timeStamp.toString(),
          nonceStr: data.data.nonceStr,
          package: data.data.package,
          signType: data.data.signType,
          paySign: data.data.paySign,
          success: function success(res) {
            showConfirm({
              str: '支付成功',
              btn_yes: {
                event_click: cbk
              }
            });
          }
        });
      },
      error: function error(msg) {
        showConfirm({
          str: msg
        });
      },
      complete: function complete() {
        hideLoad();
        loading = false;
      }
    });
  }

  // 支付
  function pay(dt, cbk) {
    if (loading) return;
    if (!_config.is_wx_mobile) {
      showConfirm({
        str: '请使用微信移动客户端打开该页面再进行支付'
      });
      return;
    }
    if (_config.wx_config) {
      wxPay(dt, cbk);
    }
  }

  // 支付（测试环境用）
  function pay_test(dt, cbk) {
    if (loading) return;
    showLoad();
    loading = true;
    goAPI({
      url: _api.baoxian_order_pay_test,
      data: dt,
      traditional: true,
      success: function success(data) {
        showConfirm({
          str: '支付成功',
          btn_yes: {
            event_click: cbk
          }
        });
      },
      error: function error(msg) {
        showConfirm({
          str: msg
        });
      },
      complete: function complete() {
        hideLoad();
        loading = false;
      }
    });
  }

  // 确认维修中心来接车
  function confirmOrder(dt, cbk) {
    if (loading) return;
    showLoad();
    loading = true;
    goAPI({
      url: _api.baoxian_order_confirm,
      data: dt,
      success: function success(data) {
        showConfirm({
          str: '操作成功',
          btn_yes: {
            event_click: cbk
          }
        });
      },
      error: function error(msg) {
        showConfirm({
          str: msg
        });
      },
      complete: function complete() {
        hideLoad();
        loading = false;
      }
    });
  }

  // 投诉
  function reportOrder(dt, cbk) {
    if (loading) return;
    showLoad();
    loading = true;
    goAPI({
      url: _api.baoxian_order_report,
      data: dt,
      success: function success(data) {
        showConfirm({
          str: '操作成功',
          btn_yes: {
            event_click: cbk
          }
        });
      },
      error: function error(msg) {
        showAlert(msg);
      },
      complete: function complete() {
        hideLoad();
        loading = false;
      }
    });
  }

  // 获取可申请理赔的车辆列表
  function getCarList(cbk) {
    if (car_loading) return;
    car_loading = true;
    goAPI({
      url: _api.car_list,
      data: {
        status: '13,20',
        flag: 1,
        token: user.token,
        pageSize: 100
      },
      success: cbk,
      error: function error(data) {
        showConfirm({
          str: data,
          btn_yes: {
            str: '重新加载',
            event_click: getCarList
          }
        });
      },
      complete: function complete() {
        car_loading = false;
      }
    });
  }

  // 获取订单列表
  function loadList(emptyAll) {
    if (loading) return;
    showLoad();
    loading = true;
    if (emptyAll) {
      list_page = 1;
      list_nomore = false;
    }
    goAPI({
      url: _api.baoxian_order_list,
      data: {
        token: user.token,
        pageNo: list_page
      },
      success: function success(data) {
        data = data.data;
        if (emptyAll) {
          $('#list-area').empty();
          list_data = {};
        }
        if (data.total == 0) {
          list_nomore = true;
          $('.tabbox-tab').addClass('full');
          $('.null-info').removeClass('hide');
        } else {
          list_page += 1;
          var list = data.list;
          for (var i = 0, j = list.length; i < j; i++) {
            list_data[list[i].orderNo] = list[i];
          }
          data['lipei_max'] = _config.lipei_max;
          $('.tabbox-tab').removeClass('full');
          $('.null-info').addClass('hide');
          $('#list-area').append(list_tpl.render(data));
          if (!data.hasNextPage) {
            list_nomore = true;
          }
        }
        if (_config.is_wx_mobile && !_config.wx_config) {
          configWXJSSDK(['previewImage', 'chooseWXPay'].concat(_config.wx_share), {
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

  $('#btn-add').on('click', function () {
    if (Date.now() - car_date > 600000) {
      car_list = null;
    }
    if (car_list) {
      var node;
      if (car_list.list.length == 0) {
        node = '<div id="add-list"><div class="loading-box default"><p class="loading-text">正在获取可申请理赔的车辆</p><p class="default-text">暂无可申请理赔的车辆 <a class="hover txt-blue" href="/hfive/view/car_add.html">立即加入互助</a></p></div></div>';
      } else {
        node = '<div id="add-list">' + juicer($('#add-tpl').html(), car_list) + '</div>';
      }
      showConfirm({
        title: '选择理赔车辆',
        class: 'add-list shadow-blue',
        full: true,
        btn_close: true,
        cover_background: 'rgba(255,255,255,0.5)',
        node: node,
        btn_yes: {
          event_click: function event_click() {
            var item = $('#add-list .item.active');
            if (item.length > 0) {
              window.location.href = '/hfive/view/baoxian_order_add.html?id=' + item.eq(0).data('value');
            }
          }
        },
        btn_cancel: {
          str: '取消'
        }
      });
    } else {
      showConfirm({
        title: '选择理赔车辆',
        class: 'add-list shadow-blue',
        full: true,
        btn_close: true,
        cover_background: 'rgba(255,255,255,0.5)',
        node: '<div id="add-list"><div class="loading-box loading"><p class="loading-text">正在获取可申请理赔的车辆</p><p class="default-text">暂无可申请理赔的车辆 <a class="hover txt-blue" href="/hfive/view/car_add.html">立即加入互助</a></p></div></div>',
        btn_yes: {
          event_click: function event_click() {
            var item = $('#add-list .item.active');
            if (item.length > 0) {
              window.location.href = '/hfive/view/baoxian_order_add.html?id=' + item.eq(0).data('value');
            }
          }
        },
        btn_cancel: {
          str: '取消'
        }
      });
      getCarList(function (data) {
        car_date = Date.now();
        car_list = data.data;
        if (car_list.total == 0) {
          $('#add-list .loading-box').removeClass('hide loading').addClass('default');
        } else {
          car_list.list = $.map(car_list.list, function (item, index) {
            if (item.orderNo && item.statusOrder != 100 && item.orderIsInvalid != 10 || item.eventNo && item.statusEvent != 100 && item.eventIsInvalid != 10) {
              return null;
            } else {
              return item;
            }
          });
          if (car_list.list.length === 0) {
            $('#add-list .loading-box').removeClass('hide loading').addClass('default');
          } else {
            $('#add-list .loading-box').addClass('hide');
            $('#add-list').html(juicer($('#add-tpl').html(), car_list));
          }
        }
      });
    }
    $('.add-list').off('click').on('click', '.item', function () {
      $(this).addClass('active').siblings().removeClass('active');
    });
  });

  $('#list-area').on('click', '.btn', function () {
    var $btn = $(this);
    if ($btn.hasClass('btn-confirm')) {
      var item = list_data[$btn.data('value')];
      if (!item) {
        showConfirm({
          str: '未找到该订单的信息，无法完成操作'
        });
        return;
      }
      showConfirm({
        title: '亲，您确定维修中心已接车？',
        str: '维修中心人员来接车，请先核对下其身份',
        color: '#4895e5',
        cover_close: true,
        btn_yes: {
          str: '确定',
          event_click: function event_click() {
            confirmOrder({
              token: user.token,
              orderNo: item.orderNo
            }, function () {
              item.status = 31;
              var $node_old = $('.item.o' + item.orderNo);
              if ($node_old.length > 0) {
                var $node_new = $(list_tpl.render({ list: [item] }));
                $node_old.html($node_new.html());
                $node_new = null;
              }
            });
          }
        },
        btn_cancel: {
          str: '取消'
        }
      });
    } else if ($btn.hasClass('btn-pay')) {
      var item = list_data[$btn.data('value')];
      if (!item) {
        showConfirm({
          str: '未找到该订单的信息，无法完成操作'
        });
        return;
      }
      pay_test({
        token: user.token,
        orderNo: item.orderNo
      }, function () {
        item.status = 61;
        var $node_old = $('.item.o' + item.orderNo);
        if ($node_old.length > 0) {
          var $node_new = $(list_tpl.render({ list: [item] }));
          $node_old.html($node_new.html());
          $node_new = null;
        }
      });
    } else if ($btn.hasClass('btn-resolve')) {
      var item = list_data[$btn.data('value')];
      if (!item) {
        showConfirm({
          str: '未找到该订单的信息，无法完成操作'
        });
        return;
      }
      showConfirm({
        title: '您确定投诉问题已经解决\n并且确认已经接到您的爱车了？',
        cover_close: true,
        btn_yes: {
          str: '确定',
          event_click: function event_click() {
            reportOrder({
              token: user.token,
              orderNo: item.orderNo
            }, function () {
              loading = false;
              pay_test({
                token: user.token,
                orderNo: item.orderNo
              }, function () {
                item.status = 61;
                var $node_old = $('.item.o' + item.orderNo);
                if ($node_old.length > 0) {
                  var $node_new = $(list_tpl.render({ list: [item] }));
                  $node_old.html($node_new.html());
                  $node_new = null;
                }
              });
            });
          }
        },
        btn_cancel: {
          str: '取消'
        }
      });
    }
  });
})();