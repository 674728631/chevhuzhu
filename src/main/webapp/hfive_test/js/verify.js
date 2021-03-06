'use strict';

(function () {
  var login_loading = false; //登录提交中
  var vali_loading = false; //验证码获取中
  var vali_colding = false; //验证码冷却中
  var phone = getUrlPar('phone');
  //var redirct_url = decodeURIComponent(getUrlPar('redir'));
  var from_url = loadStorage('from_url');
  var to_url = loadStorage('to_url');
  var code_data = ['', '', '', ''];
  var code_index = 0;
  var user = checkLogin();

  if (user) {
    delStorage('from_url');
    delStorage('to_url');
    if (from_url && new RegExp('^http://test.chevhuzhu.com/hfive', 'i').test(from_url)) {
      window.location.href = from_url;
    } else if (to_url && new RegExp('^http://test.chevhuzhu.com/hfive', 'i').test(to_url)) {
      window.location.href = to_url;
    } else {
      window.location.href = '/hfive/view/profile.html';
    }
  }

  if (_config.is_wx) {
    wxOauth(function () {
      configWXJSSDK(_config.wx_share, {
        success: function success() {
          shareWX({
            title: _share_title,
            desc: _share_desc,
            link: _share_link
          });
        }
      });
    });
  }

  if (!validatePhone(phone, true)) {
    showConfirm({
      str: '手机号码有误，请重新填写',
      btn_yes: {
        href: '/hfive/view/login.html?redir=' + redirct_url
      }
    });
    return;
  } else {
    $('.container').html(juicer($('#main-tpl').html(), { phone: phone }));
  }
  var $btn_code = $('#btn-code');
  var $btn_confirm = $('#btn-confirm');
  var vkey = vkeyboard({
    exception_class: 'code-item',
    event_show: function event_show() {
      if ($(window).height() - $btn_confirm.offset().top < $('#vkeyboard').height()) {
        $('.container').addClass('spec');
        $(window).scrollTop($('#t2').offset().top);
      }
    },
    event_close: function event_close() {
      $('.container').removeClass('spec');
    },
    event_input: function event_input(val, name) {
      if (val === 'confirm') {
        if (code_index >= 3) {
          vkey.hide();
          submitData();
        }
      } else if (val === 'delete') {
        if (code_data[code_index] === '') {
          code_index -= 1;
          if (code_index < 0) {
            code_index = 0;
          }
        }
        code_data[code_index] = '';
        $('.code-item').eq(code_index).text('').removeClass('active');
        vkey.confirmDisable();
        $btn_confirm.addClass('weak').removeClass('strong');
      } else {
        code_data[code_index] = val;
        $('.code-item').eq(code_index).text(val).addClass('active');
        code_index += 1;
        if (code_index > 3) {
          code_index = 3;
          vkey.confirmEnable();
          $btn_confirm.addClass('strong').removeClass('weak');
          submitData();
        }
      }
    }
  });
  vkey.confirmDisable();
  vkey.show();
  coldValiCode(60);

  // 重新发送验证码
  $btn_code.on('click', sendValiCode);

  // 发送验证码
  function sendValiCode() {
    if (vali_loading || vali_colding) return;
    showLoad({ str: '验证码发送中' });
    vali_loading = true;
    goAPI({
      url: _api.code,
      type: 'get',
      data: {
        mobileNumber: phone + '_1'
      },
      success: function success(data) {
        showAlert('验证码已发送，请注意查收');
        coldValiCode(60);
      },
      error: function error(data) {
        $btn_code.text('发送验证码');
        showConfirm({
          str: data
        });
      },
      complete: function complete() {
        hideLoad();
        vali_loading = false;
      }
    });
  }

  // 验证码冷却
  function coldValiCode(timer) {
    if (timer > 0) {
      vali_colding = true;
      $btn_code.text(timer + 's');
      setTimeout(function () {
        coldValiCode(timer - 1);
      }, 1000);
    } else {
      vali_colding = false;
      $btn_code.text('发送验证码').removeClass('disabled');
    }
  }

  // 填写验证码
  $('#code-box').on('click', '.code-item', function () {
    vkey.show();
  });

  // 协议
  // $('#btn-protocol').on('click', function(){
  // showConfirm({
  // title: '车V店用户使用协议',
  // str: user_protocol,
  // full: true
  // });
  // })

  // 提交
  $btn_confirm.on('click', function () {
    if (code_index >= 3) {
      submitData();
    }
  });

  function submitData() {
    if (login_loading) return;
    var code_temp = code_data.join('');
    if (code_temp.length === 0) {
      showAlert('请输入验证码');
      return;
    } else if (!/^[0-9]{4}$/.test(code_temp)) {
      showAlert('请输入4位纯数字的验证码');
      return;
    }
    login(phone, code_temp);
  }

  // 登录
  function login(phone, code) {
    if (login_loading) return;
    login_loading = true;
    showLoad({
      str: '登录中'
    });
    goAPI({
      url: _api.login,
      data: {
        phoneAndCode: phone + '_' + code,
        fromId: loadStorage('from_id', true) || 'null'
      },
      success: function success(data) {
        data = data.data;
        if (!data.token) {
          showConfirm({
            str: '获取用户信息失败，请重新登录'
          });
          return;
        }
        data['phone'] = phone;
        saveStorage('user', data, true);
        // 如果是首次登录且来自车妈妈渠道，则将状态存入session，页面跳转之后即显示对应提示
        if ($.isArray(data.chemamaHint) && data.chemamaHint.length > 0) {
          saveStorage('login_chemama', data.chemamaHint);
          // 如果是首次登录且有优惠券，则将状态存入session，页面跳转之后即显示对应提示
        } else if (data.firstLogin == 1) {
          saveStorage('login_coupon', data.coupon);
        }
        delStorage('to_url');
        delStorage('from_id', true);
        // 如果是从加入互助按钮过来的，需要判断用户的车辆状态来进行跳转
        if (new RegExp('^http://test.chevhuzhu.com/hfive', 'i').test(to_url)) {
          if (/\/car_add.html/.test(to_url) && getUrlPar('from', to_url) === 'index') {
            var car_list = data.carList;
            if ($.isArray(car_list) && car_list.length > 0) {
              if (car_list.length === 1) {
                var car = car_list[0];
                if (car.status == 1) {
                  window.location.href = '/hfive/view/car_add_pay.html?id=' + car.id;
                } else if (car.status == 2) {
                  window.location.href = '/hfive/view/car_add2.html?id=' + car.id;
                } else if (car.status == 10 || car.status == 13 || car.status == 31) {
                  window.location.href = '/hfive/view/car.html?type=2';
                } else if (car.status == 12) {
                  window.location.href = '/hfive/view/car_add.html?id=' + car.id;
                } else if (car.status == 20) {
                  window.location.href = '/hfive/view/car.html';
                } else if (car.status == 30) {
                  window.location.href = '/hfive/view/car_add.html?rejoin=1&id=' + car.id;
                } else {
                  window.location.href = to_url;
                }
              } else {
                var all_clear = false;
                for (var i = 0, j = car_list.length; i < j; i++) {
                  if (car_list[i].status == 20) {
                    all_clear = true;
                    break;
                  }
                }
                if (all_clear) {
                  window.location.href = '/hfive/view/car.html';
                } else {
                  window.location.href = '/hfive/view/car.html?type=2';
                }
              }
            } else {
              window.location.href = to_url;
            }
          } else {
            window.location.href = to_url;
          }
        } else {
          window.location.href = '/hfive/view/profile.html';
        }
      },
      error: function error(data) {
        showConfirm({
          str: data
        });
      },
      complete: function complete() {
        login_loading = false;
        hideLoad();
      }
    });
  }
})();