'use strict';

var loading = false;
var user = checkLogin(true);
var user_data;
var share_url = '';
var avatar_loaded = false;
var qrcode_loaded = false;

if (user) {
  loadData();
}

function loadData() {
  showLoad();
  loading = true;
  goAPI({
    url: _api.user,
    data: {
      token: user.token
    },
    success: function success(data) {
      user_data = data.data;
      goAPI({
        url: _api.user_other,
        data: {
          customerId: user_data.id
        },
        success: function success(data) {
          loading = false;
          var dt = data.data;
          share_url = 'http://test.chevhuzhu.com/hfive/view/event_join2.html?id=' + user_data.id;
          if (_config.is_wx) {
            configWXJSSDK(_config.wx_share, {
              success: function success() {
                shareWX({
                  imgUrl: 'http://test.chevhuzhu.com/hfive/img/icon_ca.png',
                  title: user_data.userName + '邀请你免费领取1000元擦挂补贴',
                  desc: '小擦刮还在报保险？这样处理省一大笔',
                  link: share_url
                });
              }
            });
          }
          // dt.portrait = dt.portrait.replace(/http:\/\/thirdwx.qlogo.cn\//, '/wechat_image/');
          if (!dt.qrcode) {
            qrcode_loaded = true;
          }
          $('#container').html(juicer($('#main-tpl').html(), dt));
          $('#btn-rule').on('click', function () {
            showConfirm({
              class: 'tl',
              title: '会员特权',
              str: '分享海报邀请朋友扫码免费领取1000元擦挂维修金，并关注公号添加车牌；平台自动为Ta充值基础互助金，30天观察期后可开始限时体验擦挂维修。\n\n福利一：\n邀请好友，好友免费领取1000元擦刮维修金;\n\n福利二：\n邀请满2名好友即可获得1元购买洗车活动(限购2次)；\n\n福利三：\n每邀请一位好友加入，自己的擦刮额度就提升125元最多增额500提升至1500元；\n\n福利四：\n每邀请一位好友加入，平台即为你的账户余额充值3元互助金（用于分摊）。',
              cover_close: true
            });
          });
        },
        error: function error(data) {
          loading = false;
          hideLoad();
          showConfirm({
            str: data,
            btn_yes: {
              str: '重新加载',
              event_click: loadData
            }
          });
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

// 等用户头像和二维码都加载完毕后再触发生成图片
function showAvatar(obj) {
  resetImg(obj);
  avatar_loaded = true;
  showImg();
}

function showQrcode() {
  qrcode_loaded = true;
  showImg();
}

function showImg() {
  if (avatar_loaded && qrcode_loaded) {
    createImg(function () {
      $('#text1').addClass('hide');
      $('#source').addClass('upper');
      $('#container').addClass('red');
      hideLoad();
    });
  }
}

// 生成图片
function createImg(cbk) {
  setTimeout(function () {
    $('#main').height($('#main').height());
    $('#source').height($('#main').height());
    $('.avatar img').removeAttr('onload');
    $('#qr img').removeAttr('onload');
    var dom = $('#main');
    var width = dom.width();
    var height = dom.height();
    var type = "png";
    var scaleBy = 1;
    var canvas = document.createElement('canvas');
    var rect = dom.get(0).getBoundingClientRect();
    canvas.width = width * scaleBy;
    canvas.height = height * scaleBy;
    canvas.style.width = width * scaleBy + 'px';
    canvas.style.height = height * scaleBy + 'px';
    var context = canvas.getContext('2d');
    context.scale(scaleBy, scaleBy);
    context.translate(-rect.left, -rect.top - $('#container').scrollTop());
    html2canvas(dom[0], {
      canvas: canvas,
      useCORS: true,
      onrendered: function onrendered(canvas) {
        $('#source').append($('<img>', { 'class': 'view', 'src': canvas.toDataURL('image/png') }));
        if ($('#source .view').length > 1) {
          $('#source .view:eq(0)').remove();
        }
        if ($.isFunction(cbk)) {
          cbk();
        }
      }
    });
  }, 300);
}