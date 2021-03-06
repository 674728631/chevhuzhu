'use strict';

(function () {
  var loading = false;
  var user = checkLogin(true);
  var user_data;
  var user_profile;
  var money_base = 1000;
  var money_max = 1500;
  var money_reward = 125;

  if (user) {
    loadData();
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
        user_profile = data.data;
        goAPI({
          url: _api.share,
          data: {
            token: user.token
          },
          success: function success(data) {
            user_data = data.data;
            $('#container').html(juicer($('#main-tpl').html(), user_data));
            if (_config.is_wx && !_config.wx_config) {
              configWXJSSDK(_config.wx_share, {
                success: function success() {
                  shareWX({
                    imgUrl: 'http://test.chevhuzhu.com/hfive/img/icon_ca.png',
                    title: user_profile.userName + '邀请你免费领取1000元擦挂补贴',
                    desc: '小擦刮还在报保险？这样处理省一大笔',
                    link: 'http://test.chevhuzhu.com/hfive/view/event_join2.html?id=' + user_profile.id
                  });
                }
              });
            }
            $('#btn-rule').on('click', function () {
              showConfirm({
                class: 'tl',
                title: '会员特权',
                str: '分享海报邀请朋友扫码免费领取' + money_base + '元擦挂维修金，并关注公号添加车牌；平台自动为Ta充值基础互助金，30天观察期后可开始限时体验擦挂维修。\n\n福利一：\n每邀请一位朋友加入，自己的维修额度提升' + money_reward + '元，最多增加' + money_reward * 4 + '提升至' + (money_base + money_reward * 4) + '元；\n\n福利二：\n每邀请一位朋友加入，平台为你自己的账户充值3元互助金。',
                cover_close: true
              });
            });
            // $('#btn-confirm').on('click', function(){
            // if (user_data.list.length > 3) {
            // window.location.href = '/hfive/view/car.html';
            // } else {
            // var $node = $('<div class="share-layer"></div>').on('click', function(){
            // $(this).remove();
            // });
            // $('body').append($node);
            // $node = null;
            // }
            // })
            if (user_data.amtCompensation === null || user_data.amtCompensation === undefined) {
              user_data.amtCompensation = money_base;
            }
            if (user_data.list.length === 0) {
              $('#car').on('click', function () {
                var $node = $('<div class="share-layer"></div>').on('click', function () {
                  $(this).remove();
                });
                $('body').append($node);
                $node = null;
              });
            } else {
              if (user_data.licensePlateNumber && user_data.id) {
                if (user_data.status == 1) {
                  $('#car').attr('href', '/hfive/view/car_add_pay.html?id=' + user_data.id);
                } else if (user_data.status == 2) {
                  $('#car').attr('href', '/hfive/view/car_add2.html?id=' + user_data.id);
                } else if (user_data.status == 12) {
                  $('#car').attr('href', '/hfive/view/car_add.html?id=' + user_data.id);
                } else if (user_data.status == 30) {
                  $('#car').attr('href', '/hfive/view/car_add.html?rejoin=1&id=' + user_data.id);
                } else {
                  $('#car').attr('href', '/hfive/view/car.html');
                }
              } else {
                $('#car').attr('href', '/hfive/view/car_add.html');
              }
              for (var i = 0, j = user_data.list.length; i < j; i++) {
                if (user_data.list[i].status == 2) {
                  user_data.amtCompensation += money_reward;
                }
              }
            }
            if (user_data.licensePlateNumber && user_data.id) {
              $('#desc').text(user_data.licensePlateNumber);
            }

            if (user_data.status == 1) {
              var base = money_base;
              for (var i = 0, j = user_data.list.length; i < j; i++) {
                if (user_data.list[i].status == 1) {
                  base += money_reward;
                }
              }
              setCar(money_max, base);
            } else {
              setCar(money_max, user_data.amtCompensation);
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
      },
      error: function error(data) {
        hideLoad();
        loading = false;
        showConfirm({
          str: data,
          btn_yes: {
            str: '重新加载',
            event_click: loadData
          }
        });
      }
    });
  }

  // 初始化圆形进度图示
  function setCar(max, cur) {
    var per = cur / max * 100; // 进度比例
    var dis = Math.round(16 * deviceWidth / 75); //容器宽高的一半，整数，数值越大容器越大
    var dis_shadow = Math.round(deviceWidth / 75); //基本进度条的粗细程度，整数
    var offset = -1; //实际进度条相对于基本进度条的粗细偏差，整数，数值越小实际进度条越粗
    var dis_rect = dis - offset; //实际进度条的宽高的一半，整数
    $('#car').css({ 'width': dis * 2, 'height': dis * 2 }).removeClass('hide');
    $('#spinner').css({ 'box-shadow': 'inset 0 0 0 ' + dis_shadow + 'px #ffc900' });
    $('#sp-left').css({ 'clip': 'rect(0,' + dis_rect + 'px,' + dis_rect * 2 + 'px,0)', 'top': offset, 'right': offset, 'bottom': offset, 'left': offset });
    $('#sp-left i').css({ 'clip': 'rect(0,' + dis_rect + 'px,' + dis_rect * 2 + 'px,0)', 'box-shadow': 'inset 0 0 0 ' + (dis_shadow - offset * 2) + 'px #ff6127' });
    $('#sp-right').css({ 'clip': 'rect(0,' + dis_rect * 2 + 'px,' + dis_rect * 2 + 'px,' + dis_rect + 'px)', 'top': offset, 'right': offset, 'bottom': offset, 'left': offset });
    $('#sp-right i').css({ 'clip': 'rect(0,' + dis_rect * 2 + 'px,' + dis_rect * 2 + 'px,' + dis_rect + 'px)', 'box-shadow': 'inset 0 0 0 ' + (dis_shadow - offset * 2) + 'px #ff6127' });
    setTimeout(function () {
      if (per > 50) {
        if (per > 100) {
          per = 100;
        }
        var dur = Math.round(per - 50) / 100;
        $('#sp-left i').css({ 'transition-duration': dur + 's', 'transform': 'rotate(' + Math.floor(-180 + 360 * (per - 50) / 100) + 'deg) translateZ(0)' });
        $('#sp-right i').css({ 'transform': 'rotate(0deg) translateZ(0)' });
        setMoney(cur, 500 + dur * 1000);
      } else {
        if (per < 0) {
          per = 0;
        }
        $('#sp-right i').css({ 'transform': 'rotate(' + Math.floor(-180 + 360 * per / 100) + 'deg) translateZ(0)' });
        setMoney(cur, 500);
      }
    }, 100);
  }

  // 动态改变金额
  function setMoney(num, duration) {
    var step = Math.ceil(num / duration * 20);
    var money = step;
    var func = function func() {
      if (money > num) {
        $('#money').text(num);
        return;
      }
      $('#money').text(money);
      money += step;
      setTimeout(func, 20);
    };
    func();
  }
})();