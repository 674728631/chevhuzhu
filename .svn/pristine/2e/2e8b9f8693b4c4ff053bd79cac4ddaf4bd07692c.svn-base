'use strict';

(function () {
  var loading = false;
  var user = checkLogin(true);
  var user_data;

  if (user) {
    loadData();
  }

  function editSex() {
    if (loading) return;
    pushMenu({
      title: '设置性别',
      data: [{ value: 1, name: '男' }, { value: 0, name: '女' }],
      event_click: function event_click(data) {
        submitData({
          sex: data.value
        }, function () {
          showAlert('性别设置成功');
          user_data.gender = data.value;
          $('#sex-txt').text(data.name);
        });
      }
    });
  }

  function editNickname() {
    if (loading) return;
    showConfirm({
      str: '设置姓名',
      class: 'edit2',
      node: $('<input id="nickname" type="text" maxlength="50" placeholder="请输入您的姓名">').val(user_data.userName),
      btn_yes: {
        click_close: false,
        event_click: function event_click() {
          if (loading) return;
          var nickname = $('#nickname').val().trim();
          if (nickname.length === 0) {
            showAlert('姓名不能为空');
            return;
          }
          submitData({
            userName: nickname
          }, function () {
            hideConfirm();
            showAlert('姓名设置成功');
            user_data.userName = nickname;
            $('#nickname-txt').text(nickname);
          });
        }
      },
      btn_cancel: {
        str: '取消'
      }
    });
  }

  function editAvatar() {
    if (loading) return;
    loading = true;
    showLoad();
    var $this = $(this);
    var file = this.files[0];
    resizeImg({
      file: file,
      width: 400,
      success: function success(rst) {
        $this.val('');
        submitData({
          base: rst.base64
        }, function () {
          showAlert('头像设置成功');
          $('.avatar').empty().insertImg(rst.base64);
          $('#avatar-txt').text('修改');
        });
      },
      fail: function fail() {
        showAlert('无法处理该图片\n请选择其它图片');
        hideLoad();
      },
      complete: function complete() {
        loading = false;
      }
    });
  }

  function logout() {
    if (loading) return;
    showConfirm({
      str: '是否退出登录',
      cover_close: true,
      btn_yes: {
        event_click: function event_click() {
          delStorage('user', true);
          window.location.href = '/hfive/view/profile.html';
        }
      },
      btn_cancel: {
        str: '取消'
      }
    });
  }

  // 提交修改的信息
  function submitData(opt, cbk) {
    if (loading) return;
    showLoad();
    loading = true;
    goAPI({
      url: _api.user_edit,
      data: $.extend({
        token: user.token
      }, opt),
      success: function success(data) {
        if ($.isFunction(cbk)) {
          cbk();
        }
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

  function loadData() {
    if (loading) return;
    showLoad();
    loading = true;
    goAPI({
      url: _api.user,
      data: {
        token: user.token
      },
      success: function success(data) {
        user_data = data.data;
        $('.container').html(juicer($('#main-tpl').html(), user_data));
        $('#avatar-upload').on('change', editAvatar);
        $('#btn-nickname').on('click', editNickname);
        $('#btn-sex').on('click', editSex);
        $('#btn-logout').on('click', logout);
        if (_config.is_wx && !_config.wx_config) {
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