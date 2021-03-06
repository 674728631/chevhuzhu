'use strict';

(function () {
  var user = checkLogin(true);

  if (user) {
    loadData();
  }

  function loadData() {
    showLoad();
    $.when(loadList(), loadStatus(), loadUser()).done(function (list, status, userdata) {
      hideLoad();

      // 已经注册过熊猫车服了
      if (status.result == 203) {
        showConfirm({
          str: status.message,
          btn_yes: {
            str: '回到首页',
            href: '/hfive/view/index.html'
          }
        });
        return;
      }

      if (!$.isArray(list)) {
        list = [];
      }
      var car = null;
      if ($.isArray(userdata.carList)) {
        var carList = userdata.carList;
        var hasCar = false;
        var firstCar = null;
        for (var i = 0, j = carList.length; i < j; i++) {
          if (carList[i].status == 1 && !firstCar) {
            firstCar = carList[i];
          }
          if (carList[i].status > 1) {
            hasCar = true;
            car = carList[i];
            break;
          }
        }
        if (!hasCar) {
          car = firstCar;
        }
      }
      $('.container').html(juicer($('#main-tpl').html(), { list: list, number: status.number, car: car }));
      $('.container').on('click', '.btn-use', function () {
        showConfirm({
          str: '请识别二维码前往【懒人洗车】平台激活',
          node: '<img src="/hfive/img/xiaochengxu_xiongbao.jpg" style="width:3rem;">'
        });
      }).on('click', '.btn-rule', function () {
        showConfirm({
          title: '使用说明',
          class: 'tl',
          str: '1、补贴活动为车V互动V2会员专属特权；\n2、使用有效期截至2019-02-01；\n3、成都绕城内可预约。'
        });
      });
    }).fail(function (msg) {
      hideLoad();
      showConfirm({
        str: msg,
        btn_yes: {
          str: '重新加载',
          event_click: loadData
        }
      });
    });
  }

  // 获取优惠券列表
  function loadList() {
    var dtd = $.Deferred();
    goAPI({
      url: _api.carwash_coupon,
      data: {
        token: user.token
      },
      success: function success(data) {
        dtd.resolve(data.data);
      },
      error: function error(msg) {
        dtd.reject(msg);
      }
    });
    return dtd.promise();
  }

  // 获取用户的购买情况
  function loadStatus() {
    var dtd = $.Deferred();
    goAPI({
      url: _api.carwash_check,
      data: {
        token: user.token
      },
      success: function success(data) {
        dtd.resolve(data.data);
      },
      error: function error(data) {
        dtd.reject(msg);
      }
    });
    return dtd.promise();
  }

  // 获取用户的车辆情况
  function loadUser() {
    var dtd = $.Deferred();
    goAPI({
      url: _api.user,
      data: {
        token: user.token
      },
      success: function success(data) {
        dtd.resolve(data.data);
      },
      error: function error(data) {
        dtd.reject(msg);
      }
    });
    return dtd.promise();
  }
})();