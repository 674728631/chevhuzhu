'use strict';

(function () {
  var loading = false;
  var phone = null;
  var $phone = $('#phone');
  var $btn_confirm = $('#btn-confirm');
  var redirct_url = decodeURIComponent(getUrlPar('redir'));
  var from_url = loadStorage('from_url');
  var to_url = loadStorage('to_url');
  var user = checkLogin();

  if (user) {
    delStorage('from_url');
    delStorage('to_url');
    if (from_url && new RegExp('^http://test.chevhuzhu.com/hfive', 'i').test(from_url)) {
      window.location.href = from_url;
    } else if (to_url && new RegExp('^http://test.chevhuzhu.com/hfive', 'i').test(to_url)) {
      window.location.href = to_url;
    } else {
      window.location.href = '/hfive/view/index.html';
    }
  }

  if (_config.is_wx) {
    configWXJSSDK(_config.wx_share, {
      success: function success() {
        shareWX({
          title: _share_title,
          desc: _share_desc,
          link: _share_link
        });
      }
    });
  }

  if ($phone.val().trim().length >= 11) {
    $('#btn-confirm').removeClass('weak').addClass('strong');
    $('#btn-cancel').removeClass('hide');
  }

  $phone.on('keydown', function (e) {
    if (e.keyCode === 13) sendValiCode();
  }).on('input', function () {
    var val = $phone.val().trim();
    if (val.length > 0) {
      $('#btn-cancel').removeClass('hide');
      if (val.length >= 11) {
        $('#btn-confirm').removeClass('weak').addClass('strong');
      } else {
        $('#btn-confirm').removeClass('strong').addClass('weak');
      }
    } else {
      $('#btn-cancel').addClass('hide');
    }
  });

  // 提交
  $btn_confirm.on('click', sendValiCode);

  $('#btn-cancel').on('click', function () {
    $phone.val('');
    $('#btn-cancel').addClass('hide');
    $('#btn-confirm').removeClass('strong').addClass('weak');
  });

  // 发送验证码
  function sendValiCode() {
    if (loading) return;
    phone = $phone.val().trim();
    if (phone.length < 11) {
      return;
    }
    if (!validatePhone(phone, true)) {
      showAlert('手机号码有误');
      return;
    }
    showLoad({ str: '验证码发送中' });
    loading = true;
    goAPI({
      url: _api.code,
      type: 'get',
      data: {
        mobileNumber: phone + '_1'
      },
      success: function success(data) {
        //window.location.href = '/hfive/view/verify.html?phone=' + phone + '&redir=' + redirct_url;
        saveStorage('to_url', redirct_url);
        window.location.href = '/hfive/view/verify.html?phone=' + phone;
      },
      error: function error(data) {
        showConfirm({
          str: data
        });
      },
      complete: function complete() {
        hideLoad();
        loading = false;
      }
    });
  }
})();