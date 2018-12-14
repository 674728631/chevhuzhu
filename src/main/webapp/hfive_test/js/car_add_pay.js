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
    money_pay = data_level[level_active] ? data_level[level_active] : data_level['1'];
    if (money_pay == 99) {
      var car = data_car[cid];
      if (car) {
        money_pay = car.yearPayAmount;
      }
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
            //支付成功后跳转到上传车辆各个角度的照片页面（已废弃）
            //window.location.href = '/hfive/view/car_add2.html?id=' + cid + '&rejoin=' + getUrlPar('rejoin');
            showConfirm({
              class: 'success',
              title: '您的' + data_car[cid].licensePlateNumber + '车辆信息\n提交成功！',
              str: '恭喜您！您的爱车进入30天观察期，观察期后正式生效',
              btn_yes: {
                str: '我知道了',
                href: '/hfive/view/car.html'
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
        //window.location.href = '/hfive/view/car_add2.html?id=' + cid + '&rejoin=' + getUrlPar('rejoin');
        showConfirm({
          class: 'success',
          title: '您的' + data_car[cid].licensePlateNumber + '车辆信息\n提交成功！',
          str: '车V互助因为您的支持将更加强大',
          btn_yes: {
            str: '我知道了',
            href: '/hfive/view/car.html'
          }
        });
      },
      error: function error(msg) {
        showConfirm({
          class: 'fail',
          title: '您的' + data_car[cid].licensePlateNumber + '车辆信息提交失败！\n请重新提交',
          str: '失败原因：' + msg,
          btn_yes: {
            str: '我知道了'
          }
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
              if (pay_wait) wxPay_test(pay_wait);
            }
          });
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