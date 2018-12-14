'use strict';

(function () {
  var loading = false;
  var checkall = false;
  var money_pay = 0;
  var level_active;
  var data_level = {};
  var data_car = {};
  var cid = getUrlPar('id');
  var user = checkLogin(true);
  var pay_wait = null;

  var juicer_getPayText = function juicer_getPayText(car) {
    if (car.status == 13 || car.status == 20) {
      if (car.observationEndTime) {
        var dt = new Date(car.observationEndTime.time);
        return '保障至' + new Date(dt.getFullYear(), dt.getMonth(), dt.getDate() + 365).formatDate('yyyy-MM-dd');
      } else {
        return '分摊全年12个月';
      }
    } else {
      var dt = new Date();
      return '保障至' + new Date(dt.getFullYear(), dt.getMonth(), dt.getDate() + 395).formatDate('yyyy-MM-dd');
    }
  };
  juicer.register('build_payText', juicer_getPayText);

  if (user) {
    if (!cid) {
      showConfirm({
        str: '缺少车辆id',
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
  }

  function resetMoney() {
    var money_temp = data_level[level_active] ? data_level[level_active] : data_level['1'];
    //var cars = $('#car-list .car-item.active');
    if (money_temp == 99) {
      // money_pay = 0;
      // cars.each(function(){
      // var car = data_car[$(this).data('id')];
      // if (car) {
      // money_pay += car.yearPayAmount;
      // }
      // });
      money_pay = data_car[cid].yearPayAmount;
    } else {
      money_pay = money_temp;
    }
    if (money_pay > 0) {
      $('#btn-pay').text('确认支付（' + money_pay + '元）');
    } else {
      $('#btn-pay').text('确认支付');
    }
  }

  function wxPay(dt) {
    if (loading) return;
    showLoad();
    loading = true;
    goAPI({
      url: _api.car_pay,
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
              str: '支付成功，即将返回上一个页面',
              class: 'shadow-blue',
              cover_background: 'rgba(255,255,255,0.5)',
              btn_yes: {
                str: null
              }
            });
            setTimeout(function () {
              window.history.go(-1);
            }, 2000);
            //window.location.href = '/hfive/view/car_pay_success.html';
          }
        });
      },
      error: function error(msg) {
        showConfirm({
          str: msg
        });
        //window.location.href = '/hfive/view/car_pay_fail.html?failres=' + ($.type(msg) === 'string' ? encodeURIComponent(msg) : '');
      },
      complete: function complete() {
        hideLoad();
        loading = false;
      }
    });
  }

  function wxPay_test(dt) {
    pay_test(dt);
  }

  function pay(dt) {
    if (loading) return;
    if (!_config.is_wx_mobile) {
      showConfirm({
        str: '请使用微信移动客户端打开该页面再进行支付'
      });
      return;
    }
    if (!_config.wx_config) {
      pay_wait = dt;
    } else {
      wxPay(dt);
    }
  }

  function pay_test(dt) {
    if (loading) return;
    showLoad();
    loading = true;
    goAPI({
      url: _api.car_pay_test,
      data: dt,
      traditional: true,
      success: function success(data) {
        showConfirm({
          str: '支付成功，即将返回上一个页面',
          class: 'shadow-blue',
          cover_background: 'rgba(255,255,255,0.5)',
          btn_yes: {
            str: null
          }
        });
        setTimeout(function () {
          window.history.go(-1);
        }, 2000);
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

  function loadData() {
    if (loading) return;
    showLoad();
    loading = true;
    goAPI({
      url: _api.pay_cars,
      data: {
        token: user.token
      },
      success: function success(data) {
        data = data.data;

        if (_config.is_wx_mobile && !_config.wx_config) {
          configWXJSSDK(['chooseWXPay'].concat(_config.wx_share), {
            success: function success() {
              // shareWX({
              // title: _share_title,
              // desc: _share_desc,
              // link: _share_link
              // })
              if (pay_wait) wxPay_test(pay_wait);
            }
          });
        }

        if (!$.isArray(data.carList) || data.carList.length === 0) {
          showConfirm({
            title: '暂无车辆信息',
            str: '如果您已添加车辆，请等待车辆通过审核',
            btn_yes: {
              str: '去添加车辆',
              href: '/hfive/view/car_add.html'
            },
            btn_no: {
              str: '回到主页',
              href: '/hfive/view/index.html'
            }
          });
          return;
        }
        for (var i = 0, j = data.amountList.length; i < j; i++) {
          data_level[data.amountList[i].id] = data.amountList[i].amount;
          if (i === 0) {
            level_active = data.amountList[i].id;
          }
        }
        if ($.isArray(data.carList)) {
          for (var i = 0, j = data.carList.length; i < j; i++) {
            data_car[data.carList[i].id] = data.carList[i];
          }
        }
        if (!data_car[cid]) {
          showConfirm({
            str: '该车辆不存在',
            btn_yes: {
              str: '返回上一页',
              href: 'javascript:history.go(-1);'
            },
            btn_no: {
              str: '回到首页',
              href: '/hfive/view/index.html'
            }
          });
          return;
        }

        $('.container').html(juicer($('#main-tpl').html(), $.extend(data, { car: data_car[cid] })));

        resetMoney();
        // var car_selected = $('#car-list .car-item.car-' + cid);
        // if (car_selected.length === 1) {
        // car_selected.addClass('active');
        // $('#btn-pay').removeClass('weak').addClass('strong');
        // resetMoney();
        // if ($('#car-list .car-item').not('.active').length === 0) {
        // $('#btn-checkall').addClass('done');
        // checkall = true;
        // } else {
        // $('#btn-checkall').removeClass('done');
        // checkall = false;
        // }
        // }

        // $('#car-list').on('click', '.car-item', function(){
        // var $this = $(this);
        // $this.toggleClass('active');
        // if ($('#car-list .car-item.active').length === 0) {
        // $('#btn-pay').removeClass('strong').addClass('weak');
        // } else {
        // $('#btn-pay').removeClass('weak').addClass('strong');
        // }
        // if ($('#car-list .car-item').not('.active').length === 0) {
        // $('#btn-checkall').addClass('done');
        // checkall = true;
        // } else {
        // $('#btn-checkall').removeClass('done');
        // checkall = false;
        // }
        // resetMoney();
        // })

        // $('#btn-checkall').on('click', function(){
        // if (checkall) {
        // $('#btn-pay').removeClass('strong').addClass('weak');
        // $('#car-list .car-item').removeClass('active');
        // $('#btn-checkall').removeClass('done');
        // checkall = false;
        // } else {
        // $('#btn-pay').removeClass('weak').addClass('strong');
        // $('#car-list .car-item').addClass('active');
        // $('#btn-checkall').addClass('done');
        // checkall = true;
        // }
        // resetMoney();
        // })

        $('#level-box').on('click', '.level-item', function () {
          var $this = $(this);
          if (!$this.hasClass('active')) {
            $this.addClass('active').siblings().removeClass('active');
            level_active = $this.data('value');
            resetMoney();
          }
        });

        $('#btn-pay').on('click', function () {
          if (money_pay > 0) {
            // var cars = [];
            // $('#car-list .car-item.active').each(function(){
            // cars.push($(this).data('id'));
            // });
            pay_test({
              token: user.token,
              CarId: [cid],
              amountId: level_active
            });
          }
        });
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