'use strict';

(function () {
  var loading = false;
  var oid = getUrlPar('id');
  var type = getUrlPar('type');
  var data_order;
  var data_imgs = [];
  var preview_imgs = preview({
    fixed: true,
    background: '#000000',
    number: true,
    point: true,
    fill: 'fix'
  });
  if (type != 1 && type != 2) {
    type = 1;
  }

  if (!oid) {
    showConfirm({
      str: '缺少订单ID',
      btn_yes: {
        str: '返回上一页',
        href: 'javascript:history.go(-1);'
      },
      btn_no: {
        str: '回到首页',
        href: '/hfive/view/index.html'
      }
    });
  } else {
    loadData();
  }

  // 加载页面
  function loadData() {
    showLoad();
    goAPI({
      url: _api.baojia,
      data: {
        eventNo: oid,
        type: type
      },
      success: function success(data) {
        if (_config.is_wx_mobile && !_config.wx_config) {
          configWXJSSDK(['previewImage'].concat(_config.wx_share), {
            success: function success() {
              shareWX({
                title: '预估定损价',
                desc: '车V互助邀请您预估车辆定损价，点击开始报价',
                link: 'http://test.chevhuzhu.com/hfive/view/baojia.html?id=' + oid + '&type=' + type
              });
            }
          });
        }
        data_order = data.data;
        if (data_order.commentLabelContent) {
          data_order.commentLabelContent = data_order.commentLabelContent.split('_');
        } else {
          data_order.commentLabelContent = null;
        }
        if ($.type(data_order.accidentImg) === 'string') {
          data_order.accidentImg = data_order.accidentImg.split('_');
        } else if (!data_order.accidentImg) {
          data_order.accidentImg = [];
        }
        if ($.type(data_order.assertImg) === 'string') {
          data_order.assertImg = data_order.assertImg.split('_');
        } else if (!data_order.assertImg) {
          data_order.assertImg = [];
        }
        if ($.type(data_order.repairImg) === 'string') {
          data_order.repairImg = data_order.repairImg.split('_');
        } else if (!data_order.repairImg) {
          data_order.repairImg = [];
        }
        $('.container').html(juicer($('#main-tpl').html(), data_order));

        // 浏览大图
        $('.photo-box').on('click', '.photo-item', function () {
          var $this = $(this);
          var index = $this.index();
          var imgs = [];
          $this.closest('.photo-box').find('.photo-img img').each(function () {
            imgs.push($(this).attr('src'));
          });
          if (_config.is_wx) {
            if (_config.wx_config) {
              wx.previewImage({
                current: imgs[index],
                urls: imgs
              });
            }
          } else {
            preview_imgs.show({
              imgs: imgs,
              index: index + 1
            });
          }
        });
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
      }
    });
  }
})();