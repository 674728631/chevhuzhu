'use strict';

(function () {
  var loading = false;
  var id = getUrlPar('id');
  var eid = 5;
  var eid_test = 3;

  if (!id) {
    showConfirm({
      str: '参数错误'
    });
    return;
  }

  if (_config.is_wx) {
    wxOauth(loadData);
  } else {
    loadData();
  }

  // 初始化页面
  function loadData() {
    showLoad();
    goAPI({
      url: _api.user_other,
      data: {
        customerId: id
      },
      success: function success(data) {
        data = data.data;
        $('#container').html(juicer($('#main-tpl').html(), data));
        setTitle(data.nickname + '邀你免费领取1000元小擦刮维修补贴金');

        if (_config.is_wx) {
          configWXJSSDK(_config.wx_share, {
            success: function success() {
              shareWX({
                imgUrl: 'http://test.chevhuzhu.com/hfive/img/icon_ca.png',
                title: data.nickname + '邀请你免费领取1000元擦挂补贴',
                desc: '小擦刮还在报保险？这样处理省一大笔',
                link: window.location.href.replace(/#.*$/, '')
              });
            }
          });
        }

        //data.qrcode

        // $('#btn-confirm').on('click', function(){
        // var phone = $('#phone').val().trim();
        // if (phone.length === 0) {
        // showAlert('请输入手机号码');
        // return;
        // }
        // if (!validatePhone(phone, true)) {
        // showAlert('手机号码有误');
        // return;
        // }
        // showLoad();
        // loading = true;
        // goAPI({
        // url: _api.share2,
        // data: {
        // customerId: id,
        // newCustomerPN: phone,
        // modelId: eid_test
        // },
        // success: function(data) {
        // showConfirm({
        // str: '<font class="txt-red">恭喜您！获得1000元擦刮维修补贴！</font><br>请长按识别二维码关注<br>完成最后一步注册即可认领成功',
        // cover_close: true,
        // node: '<img src="/hfive/img/chevhuzhu_qr_s_test.jpg" style="width:3rem;height:3rem;">',
        // btn_close: true,
        // btn_yes: {
        // str: null
        // }
        // });
        // },
        // error: function(data,code) {
        // if (code == 501) {
        // showConfirm({
        // str: '<font class="txt-red">您已领取过啦~</font><br>识别二维码关注 get车险技能',
        // cover_close: true,
        // node: '<img src="/hfive/img/chevhuzhu_qr_s_test.jpg" style="width:3rem;height:3rem;">',
        // btn_close: true,
        // btn_yes: {
        // str: null
        // }
        // });
        // } else if (code == 502) {
        // showConfirm({
        // str: '亲，您已加入了保障计划，请下次再来~'
        // });
        // } else {
        // showConfirm({
        // str: data
        // });
        // }
        // },
        // complete: function() {
        // hideLoad();
        // loading = false;
        // }
        // })
        // })
      },
      error: function error(data, code) {
        showConfirm({
          str: data
        });
      },
      complete: function complete() {
        hideLoad();
      }
    });
  }
})();