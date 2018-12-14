'use strict';

(function () {
  var mid = getUrlPar('id');
  var loading = false;
  //var user = checkLogin(true);
  var userdata;

  // if (!mid) {
  // showConfirm({
  // str: '缺少明细id',
  // btn_yes: {
  // href: '/hfive/view/wallet_history.html'
  // }
  // });
  // return;
  // }

  // if (_config.is_wx) {
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

  // if (user) {
  //   loadData();
  // }

  function loadData() {
    showLoad();
    getUserInfo(user.token).done(function (data) {
      userdata = data.data;
      loadRecord();
    }).fail(function () {
      hideLoad();
      showConfirm({
        str: '出错了，请稍候重试',
        btn_yes: {
          str: '重新加载',
          event_click: loadData
        }
      });
    });
  }

  function loadRecord() {
    if (loading) return;
    showLoad();
    loading = true;
    goAPI({
      url: _api.wallet_record,
      data: {
        token: user.token,
        messageId: mid
      },
      success: function success(data) {
        $('.container').html(juicer($('#main-tpl').html(), $.extend(data.data, { userdata: userdata, user: user })));
      },
      error: function error(data) {
        showConfirm({
          str: data,
          btn_yes: {
            str: '重新加载',
            event_click: loadRecord
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