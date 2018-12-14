'use strict';

(function () {
  var loading = false;
  var id = getUrlPar('id');
  var rejoin = getUrlPar('rejoin');
  var user = checkLogin(true);
  var plate = '';
  var flag = 1; // 1为添加，2为编辑
  var photo_upload = {};

  if (user) {
    showConfirm({
      str: '该页面已失效',
      btn_yes: {
        str: '返回上一页',
        href: 'javascript:history.go(-1);'
      },
      btn_no: {
        str: '回到首页',
        href: '/hfive/view/index.html'
      }
    });
  }

  // if (!id) {
  // showConfirm({
  // str: '缺少车辆id',
  // btn_yes: {
  // str: '返回上一页',
  // href: 'javascript:history.go(-1);'
  // },
  // btn_no: {
  // str: '回到首页',
  // href: '/hfive/view/index.html'
  // }
  // });
  // } else {
  // checkToken(user, loadData);
  // }

  function confirmData(dt) {
    if (loading) return;
    showLoad();
    loading = true;
    goAPI({
      url: _api.add_car2,
      data: dt,
      traditional: true,
      success: function success(data) {
        showConfirm({
          class: 'success',
          title: '您的' + plate + '车辆信息\n提交成功！',
          str: '我们将在一个工作日内完成车辆审核，请留意信息，车V互助因为您的支持将更加强大',
          btn_yes: {
            str: '我知道了',
            href: '/hfive/view/car.html'
          }
        });
      },
      error: function error(msg) {
        showConfirm({
          class: 'fail',
          title: '您的' + plate + '车辆信息提交失败！\n请重新提交',
          str: '失败原因：' + msg,
          btn_yes: {
            str: '我知道了'
          }
        });
      },
      complete: function complete() {
        hideLoad();
        loading = false;
      }
    });
  }

  function getCarDetail(dt, cbk) {
    if (loading) return;
    showLoad();
    loading = true;
    goAPI({
      url: _api.car_detail,
      data: dt,
      success: function success(data) {
        if ($.isFunction(cbk)) {
          cbk(data);
        }
      },
      error: function error(data) {
        showConfirm({
          str: data,
          btn_yes: {
            str: '重新加载',
            event_click: function event_click() {
              getCarDetail(dt, cbk);
            }
          }
        });
      },
      complete: function complete() {
        hideLoad();
        loading = false;
      }
    });
  }

  function setImg(file, index) {
    resizeImg({
      file: file,
      width: 640,
      success: function success(rst) {
        var item = $('#upload-box2 .btn-upload[data-value="' + index + '"]');
        item.removeClass('default').find('input').val('').siblings('.upload-img').empty().insertImg(rst.base64, 'fix');
      },
      fail: function fail() {
        showAlert('无法处理该图片\n请选择其它图片');
      },
      complete: function complete() {
        loading = false;
        hideLoad();
      }
    });
  }

  function loadData() {
    getCarDetail({
      token: user.token,
      id: id
    }, function (data) {
      data = data.data;
      plate = data.licensePlateNumber;
      $('.container').html(juicer($('#main-tpl').html(), data));

      if (_config.is_wx && !_config.wx_config) {
        configWXJSSDK(['chooseImage', 'uploadImage'].concat(_config.wx_share), {
          success: function success() {
            // shareWX({
            // title: _share_title,
            // desc: _share_desc,
            // link: _share_link
            // })
          }
        });
      }

      if (data.carPhotos) {
        flag = 2;
        if (rejoin != 1) {
          var photoes = JSON.parse(data.carPhotos);
          if (photoes.qd) {
            $('#img-qd').insertImg(photoes.qd, 'fix').closest('.btn-upload').removeClass('default');
          }
          if (photoes.zh) {
            $('#img-zh').insertImg(photoes.zh, 'fix').closest('.btn-upload').removeClass('default');
          }
          if (photoes.yh) {
            $('#img-yh').insertImg(photoes.yh, 'fix').closest('.btn-upload').removeClass('default');
          }
          if (photoes.zq) {
            $('#img-zq').insertImg(photoes.zq, 'fix').closest('.btn-upload').removeClass('default');
          }
          if (photoes.yq) {
            $('#img-yq').insertImg(photoes.yq, 'fix').closest('.btn-upload').removeClass('default');
          }
          if (photoes.zc) {
            $('#img-zc').insertImg(photoes.zc, 'fix').closest('.btn-upload').removeClass('default');
          }
          if (photoes.yc) {
            $('#img-yc').insertImg(photoes.yc, 'fix').closest('.btn-upload').removeClass('default');
          }
        }
      }

      // $('.btn-upload input').on('change', function(){
      // if (loading) return;
      // loading = true;
      // showLoad();
      // var $this = $(this);
      // var index = $this.closest('.btn-upload').data('value');
      // var files = this.files;
      // for (var i = 0, j = files.length; i < j; i++) {
      // if (i + index < 8) {
      // setImg(files[i], i + index);
      // } else {
      // break;
      // }
      // }
      // })

      $('.btn-upload').on('click', function () {
        if (!_config.is_wx_mobile) {
          showAlert('请在微信客户端中上传照片');
          return;
        }
        if (!_config.wx_config) {
          showAlert('正在调用摄像头...请稍后再点击');
          return;
        }
        var $this = $(this);
        wx.chooseImage({
          count: 1,
          sizeType: ['compressed'], // 可以指定是原图还是压缩图，默认二者都有
          sourceType: ['camera'], // 可以指定来源是相册还是相机，默认二者都有
          success: function success(res) {
            var ids = res.localIds; // 返回选定照片的本地ID列表，localId可以作为img标签的src属性显示图片
            wxUploadPhoto({
              localId: ids[0],
              success: function success(res) {
                var serverId = res.serverId;
                $this.removeClass('default').find('.upload-img').empty().insertImg(ids[0], 'fix');
                photo_upload[$this.data('value')] = serverId;
              }
            });
          }
        });
      });

      $('#btn-confirm').on('click', function () {
        var img_qd = $('#img-qd img');
        var img_yq = $('#img-yq img');
        var img_yc = $('#img-yc img');
        var img_yh = $('#img-yh img');
        var img_zh = $('#img-zh img');
        var img_zc = $('#img-zc img');
        var img_zq = $('#img-zq img');
        if (img_qd.length === 0) {
          showAlert('请上传车辆前挡的照片');
          return;
        }
        if (img_yq.length === 0) {
          showAlert('请上传车辆右前45度的照片');
          return;
        }
        if (img_yc.length === 0) {
          showAlert('请上传车辆右侧的照片');
          return;
        }
        if (img_yh.length === 0) {
          showAlert('请上传车辆右后45度的照片');
          return;
        }
        if (img_zh.length === 0) {
          showAlert('请上传车辆左后45度的照片');
          return;
        }
        if (img_zc.length === 0) {
          showAlert('请上传车辆左侧的照片');
          return;
        }
        if (img_zq.length === 0) {
          showAlert('请上传车辆左前45度的照片');
          return;
        }
        $('.btn-upload').each(function () {
          var $this = $(this);
          var img = $this.find('.upload-img img').eq(0).attr('src');
          if (/^http/.test(img)) {
            photo_upload[$this.data('value')] = 'null';
          }
        });
        confirmData({
          token: user.token,
          flag: flag,
          id: id,
          base: [photo_upload.zh, photo_upload.yh, photo_upload.zq, photo_upload.yq, photo_upload.qd, photo_upload.zc, photo_upload.yc]
        });
      });
    });
  }
})();