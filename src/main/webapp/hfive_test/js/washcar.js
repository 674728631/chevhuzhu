'use strict';

(function () {
  var loading = false;
  var money_pay = 0;
  var user = checkLogin(true);
  var pay_wait = null;
  var pay_number = 1; // 购买数量
  var time_end = '2019-01-01'; // 活动截止时间
  var date_end = new Date(time_end + ' 00:00:00');

  if (user) {
    if (_config.is_wx_mobile) {
      configWXJSSDK(['chooseWXPay', 'hideAllNonBaseMenuItem'], {
        success: function success() {
          wx.hideAllNonBaseMenuItem();
          if (pay_wait) wxPay_test(pay_wait);
        }
      });
    }

    if (Date.now() >= date_end) {
      showConfirm({
        str: '购买截止有效期已过，下次再来吧',
        btn_yes: {
          str: '回到首页',
          href: '/hfive/view/index.html'
        }
      });
      return;
    }

    loadData();
  }

  function wxPay(dt) {
    if (loading) return;
    showLoad();
    loading = true;
    goAPI({
      url: _api.carwash_pay,
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
                str: '确定',
                href: '/hfive/view/coupon.html'
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
      url: _api.carwash_pay,
      data: dt,
      traditional: true,
      success: function success(data) {
        showConfirm({
          str: '支付成功',
          btn_yes: {
            str: '确定',
            href: '/hfive/view/coupon.html'
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

  function loadData() {
    if (loading) return;
    showLoad();
    loading = true;
    goAPI({
      url: _api.carwash_check,
      data: {
        token: user.token
      },
      success: function success(data) {
        data = data.data;

        if (data.result == 200) {
          $('.container').html(juicer($('#main-tpl').html(), $.extend(data, { time_end: time_end })));
          $('.num-type').initNumberBox({
            max: Number(data.number),
            value: 1,
            input: false,
            event_change: function event_change(val) {
              pay_number = val;
            }
          });
          $('#btn-pay').on('click', function () {
            pay_test({
              token: user.token,
              number: pay_number
            });
          });
        } else if (data.result == 201) {
          showConfirm({
            str: '您邀请的好友还未满2人，无法购买',
            btn_yes: {
              str: '去邀请好友',
              href: '/hfive/view/share.html'
            }
          });
        } else {
          showConfirm({
            str: data.message,
            btn_yes: {
              str: '回到首页',
              href: '/hfive/view/index.html'
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
})();