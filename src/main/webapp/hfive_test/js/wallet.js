'use strict';

(function () {
  var pay_url;
  var loading = false;
  var user = checkLogin(true);
  var cid = getUrlPar('id');

  if (user) {
    loadData();
  }

  function loadData() {
    if (loading) return;
    showLoad();
    loading = true;
    goAPI({
      url: _api.wallet,
      data: {
        token: user.token
      },
      success: function success(data) {
        data = data.data;
        if ($.isArray(data.carList)) {
          var arr1 = [];
          var arr2 = [];
          for (var i = 0, j = data.carList.length; i < j; i++) {
            if (data.carList[i].status == 20) {
              arr1.push(data.carList[i]);
            } else {
              arr2.push(data.carList[i]);
            }
          }
          data.carList = arr1.concat(arr2);
        } else {
          data.carList = [];
        }
        $('.container').html(juicer($('#main-tpl').html(), data));
        if (data.carList.length > 0) {
          if (cid) {
            var selected_car = $('#car-list').find('.car-item[data-id="' + cid + '"]');
            if (selected_car.length > 0) {
              selected_car.eq(0).addClass('active').siblings().removeClass('active');
              pay_url = '/hfive/view/car_pay.html?id=' + cid;
            } else {
              pay_url = '/hfive/view/car_pay.html?id=' + data.carList[0].id;
            }
          } else {
            pay_url = '/hfive/view/car_pay.html?id=' + data.carList[0].id;
          }
        }
        $('.desc-close').on('click', function () {
          $('.desc-wrap').remove();
        });
        $('#car-list').on('click', '.car-item', function () {
          var $this = $(this);
          $this.addClass('active').siblings('.car-item').removeClass('active');
          //$('#btn-pay').removeClass('weak').addClass('strong');
          pay_url = '/hfive/view/car_pay.html?id=' + $this.data('id');
        });
        $('#btn-pay').on('click', function () {
          if (pay_url) {
            window.location.href = pay_url;
          }
        });
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