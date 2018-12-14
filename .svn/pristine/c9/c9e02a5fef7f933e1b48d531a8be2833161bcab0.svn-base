'use strict';

(function () {
  var loading = false;
  var sid = getUrlPar('sid') || '140'; // 商家id
  var cid = getUrlPar('cid') || '201805171649320Qdss2'; // 优惠券id

  if (_config.is_wx) {
    $('#container').html(juicer($('#main-tpl').html(), {}));
    configWXJSSDK(_config.wx_share, {
      success: function success() {
        shareWX({
          title: '@老司机 1000元私家车维修金请及时查收',
          desc: '4.20-4.30，时间过了就么得了哈',
          imgUrl: 'http://test.chevhuzhu.com/hfive/img/share_event_cdcyh.jpg',
          link: window.location.href.replace(/#.*$/, '')
        });
      }
    });
  } else {
    //$('#container').html(juicer($('#main-tpl').html(), {}));
    showConfirm({
      str: '请在微信客户端中打开此页面',
      btn_yes: {
        str: null
      }
    });
  }

  $('#btn-confirm').on('click', function () {
    var phone = $('#phone').val().trim();
    if (phone.length === 0) {
      showAlert('请输入手机号码');
      return;
    }
    if (!validatePhone(phone, true)) {
      showAlert('手机号码有误');
      return;
    }
    showLoad();
    loading = true;
    goAPI({
      url: _api.event,
      data: {
        shopId: sid,
        couponNo: cid,
        mobileNumber: phone
      },
      success: function success(data) {
        showConfirm({
          str: '<font class="txt-red">恭喜您！获得1000元擦刮维修补贴！</font><br>请长按识别二维码关注<br>完成最后一步注册即可认领成功',
          cover_close: true,
          node: '<img src="/hfive/img/chevhuzhu_qr_s_test.jpg" style="width:3rem;height:3rem;">',
          btn_close: true,
          btn_yes: {
            str: null
          }
        });
      },
      error: function error(data, code) {
        if (code == 501) {
          showConfirm({
            str: '您已领取过啦~<br>请识别二维码关注<br>补充车辆信息',
            cover_close: true,
            node: '<img src="/hfive/img/chevhuzhu_qr_s_test.jpg" style="width:3rem;height:3rem;">',
            btn_close: true,
            btn_yes: {
              str: null
            }
          });
        } else if (code == 502) {
          showConfirm({
            str: '亲，您已加入了保障计划，请下次再来~'
          });
        } else {
          showConfirm({
            str: data
          });
        }
      },
      complete: function complete() {
        hideLoad();
        loading = false;
      }
    });
  });
})();