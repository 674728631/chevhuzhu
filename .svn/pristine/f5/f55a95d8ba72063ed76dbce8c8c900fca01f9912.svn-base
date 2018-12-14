'use strict';

(function () {
  var cid = getUrlPar('id');
  var oid = getUrlPar('oid');
  var loading = false;
  var user = checkLogin(true);
  var previewImg;
  var photo_min = 3;
  var photo_max = 6;
  var photo_uploaded = 0;
  var plate = '';
  var rule = '一、追尾事故，变道碰擦事故：\n\n这类型的事故需要至少拍三张照片，一张车头，一张车尾，一张碰撞处局部照片，车头的那张需要把两车全貌，车辆牌照之类的拍好，地上的交通线都拍出来（用以界定责任），车尾一样。\n\n二、发生在十字路口，转盘，高架之类的事故：\n\n需要最少拍五张照片，分别是一张车头，一张车尾，一张碰撞处局部照片加上路口两车的位置情况四个方向拍上几张，如果来得及，能把红绿灯情况能涵盖进去最好。';

  if (user) {
    if (!cid && !oid) {
      showConfirm({
        str: '缺少车辆ID',
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
  }

  function confirmData(dt) {
    if (loading) return;
    showLoad();
    loading = true;
    goAPI({
      url: _api.add_baoxian_order,
      data: dt,
      traditional: true,
      success: function success(data) {
        window.location.href = '/hfive/view/baoxian_order_add2.html?id=' + cid + '&oid=' + (data.data ? data.data : oid);
      },
      error: function error(msg, code, data) {
        if (code == 488) {
          // 该车辆已经申请了理赔，引导用户跳转到对应的理赔订单页面
          showConfirm({
            str: plate + '的车辆已经有个理赔订单，请不要重复申请，谢谢。',
            btn_yes: {
              str: '查看订单',
              href: '/hfive/view/baoxian_order_detail.html?id=' + data.data
            },
            btn_no: {
              str: '我知道了'
            }
          });
        } else {
          showConfirm({
            class: 'fail',
            full: true,
            title: '您的' + plate + '车辆信息提交失败！\n请重新提交',
            str: '失败原因：' + msg,
            btn_yes: {
              str: '我知道了'
            }
          });
        }
      },
      complete: function complete() {
        hideLoad();
        loading = false;
      }
    });
  }

  // 获取车辆详情
  function getCarDetail(dt, cbk) {
    if (loading) return;
    showLoad();
    loading = true;
    goAPI({
      url: _api.car_detail,
      data: dt,
      success: function success(data) {
        if ($.isFunction(cbk)) {
          cbk(data.data);
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

  // 获取订单详情
  function getOrderDetail(dt, cbk) {
    if (loading) return;
    showLoad();
    loading = true;
    goAPI({
      url: _api.baoxian_order_detail,
      data: dt,
      success: function success(data) {
        if ($.isFunction(cbk)) {
          cbk(data.data);
        }
      },
      error: function error(data) {
        showConfirm({
          str: data,
          btn_yes: {
            str: '重新加载',
            event_click: function event_click() {
              getOrderDetail(dt, cbk);
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

  // 初始化页面
  function initData(data) {
    if (_config.is_wx && !_config.wx_config) {
      //configWXJSSDK(['previewImage', 'chooseImage', 'uploadImage'].concat(_config.wx_share), {
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
    if (data.statusOrder && data.statusOrder != 2) {
      showConfirm({
        str: '只有未通过审核的理赔申请才可以进行修改',
        btn_yes: {
          str: '返回上一页',
          href: 'javascript:history.go(-1);'
        },
        btn_no: {
          str: '回到首页',
          href: '/hfive/view/index.html'
        }
      });
      return;
    }
    if (!cid) {
      cid = data.carId;
    }
    plate = data.licensePlateNumber;
    $('.container').html(juicer($('#main-tpl').html(), $.extend(data, { cid: cid, oid: oid })));
    $('#desc-status').text($('#desc').val().length + '/200');

    if (data.accidentImg) {
      var imgs;
      if ($.isArray(data.accidentImg)) {
        imgs = data.accidentImg;
      } else {
        imgs = data.accidentImg.split('_');
      }
      for (var i = 0, j = imgs.length; i < j; i++) {
        if (i > 2) {
          break;
        }
        $('#photo' + (i + 1)).removeClass('default').html('<img src="' + imgs[i] + '" onload="resetImg(this, \'fix\')">');
      }
      //handleImg(imgs, 0);
      // for (var i = 0, j = imgs.length; i < j; i++) {
      // $('#btn-upload').before($('<div class="btn-upload item" href="javascript:;"><a class="upload-img hover" href="javascript:;"><img src="' + imgs[i] + '" onload="resetImg(this)"></a><a class="upload-del hover" href="javascript:;"><span></span></a></div>'));
      // photo_uploaded += 1;
      // if (photo_uploaded >= photo_max) {
      // $('#btn-upload').addClass('hide');
      // break;
      // }
      // }
    }

    // previewImg = preview({
    // background: '#000',
    // fill: 'fix'
    // });

    // $('#btn-rule').on('click', function(){
    // showConfirm({
    // title: '拍照规则',
    // str: rule,
    // full: true,
    // cover_click: true
    // });
    // })

    // $('#btn-upload').on('click', function(){            
    // if (!_config.is_wx_mobile) {
    // showAlert('请在手机微信客户端中上传照片');
    // return;
    // }
    // if (!_config.wx_config) {
    // showAlert('正在调用摄像头...请稍后');
    // return;
    // }
    // wx.chooseImage({
    // count: 1,
    // sizeType: ['compressed'], // 可以指定是原图还是压缩图，默认二者都有
    // sourceType: ['camera'], // 可以指定来源是相册还是相机，默认二者都有
    // success: function (res) {
    // var ids = res.localIds; // 返回选定照片的本地ID列表，localId可以作为img标签的src属性显示图片
    // wxUploadPhoto({
    // localId: ids[0],
    // success: function(res){
    // var serverId = res.serverId;
    // $('#btn-upload').before($('<div class="btn-upload item" href="javascript:;"><a class="upload-img hover" href="javascript:;"><img src="' + ids[0] + '" onload="resetImg(this)" data-value="' + serverId + '"></a><a class="upload-del hover" href="javascript:;"><span></span></a></div>'));
    // photo_uploaded += 1;
    // if (photo_uploaded >= photo_max) {
    // $('#btn-upload').addClass('hide');
    // }
    // }
    // })
    // }
    // });
    // })

    $('.btn-upload2').on('click', function () {
      if (!_config.is_wx_mobile) {
        showAlert('请在手机微信客户端中上传照片');
        return;
      }
      if (!_config.wx_config) {
        showAlert('正在调用摄像头...请稍后');
        return;
      }
      var $photo = $(this);
      wx.chooseImage({
        count: 1,
        sizeType: ['compressed'], // 可以指定是原图还是压缩图，默认二者都有
        sourceType: ['album', 'camera'], // 可以指定来源是相册还是相机，默认二者都有
        success: function success(res) {
          var ids = res.localIds; // 返回选定照片的本地ID列表，localId可以作为img标签的src属性显示图片
          wxUploadPhoto({
            localId: ids[0],
            success: function success(res) {
              var serverId = res.serverId;
              $photo.removeClass('default').html('<img src="' + ids[0] + '" onload="resetImg(this, \'fix\')" data-value="' + serverId + '">');
            }
          });
        }
      });
    });

    // $('#photo-upload').on('change', function(){
    // handleImg(this.files, 0);
    // })
    // function handleImg(file, index) {
    // if (index >= file.length) return;
    // if (photo_uploaded < photo_max) {
    // if (photo_uploaded + 1 >= photo_max) {
    // $('#btn-upload').addClass('hide');
    // }
    // var photo = $('<div class="btn-upload item wait" href="javascript:;"><a class="upload-img hover" href="javascript:;"></a><a class="upload-del hover" href="javascript:;"><span></span></a></div>');
    // $('#btn-upload').before(photo);
    // resizeImg({
    // file: file[index],
    // width: 1200,
    // success: function(rst){
    // photo_uploaded += 1;
    // photo.removeClass('wait').find('.upload-img').insertImg(rst.base64);
    // photo = null;
    // if (photo_uploaded >= photo_max) {
    // $('#btn-upload').addClass('hide');
    // }
    // },
    // fail: function(){
    // photo.remove();
    // photo = null;
    // if (photo_uploaded < photo_max) {
    // $('#btn-upload').removeClass('hide');
    // }
    // showAlert('无法处理该图片\n请选择其它图片');
    // },
    // always: function(){
    // if (index + 1 < file.length) {
    // handleImg(file, index + 1);
    // } else {
    // $('#btn-upload').val('');
    // }
    // }
    // });
    // } 
    // }

    // $('#upload-area').on('click', 'a', function(){
    // var $item = $(this);
    // if ($item.hasClass('upload-del')) {
    // showConfirm({
    // str: '是否删除这张照片',
    // btn_yes: {
    // event_click: function(){
    // $item.closest('.btn-upload').remove();
    // photo_uploaded -= 1;
    // if (photo_uploaded < photo_max) {
    // $('#btn-upload').removeClass('hide');
    // }
    // }
    // },
    // btn_cancel: {
    // str: '取消'
    // }
    // });
    // } else if ($item.hasClass('upload-img') && !$item.closest('.btn-upload.item').hasClass('wait')) {
    // var img_current = $item.find('img').eq(0).attr('src');
    // if (_config.is_wx) {
    // if (_config.wx_config) {
    // wx.previewImage({
    // current: img_current,
    // urls: [img_current]
    // });
    // }
    // } else {
    // previewImg.show({
    // imgs: [img_current]
    // });
    // }
    // }
    // })

    $('#btn-confirm').on('click', function () {
      var desc = $('#desc').val().trim();
      //var imgs = $('#upload-area .btn-upload.item');
      var base = [];
      if (desc.length === 0) {
        showAlert('请描述事故信息');
        return;
      }
      // if (imgs.filter('.wait').length > 0) {
      // showAlert('请等待所有照片都上传完毕');
      // return;
      // }
      var img1 = $('#photo1 img').eq(0);
      var img2 = $('#photo2 img').eq(0);
      var img3 = $('#photo3 img').eq(0);
      // imgs.each(function(){
      // var img = $(this).find('.upload-img img').eq(0);
      // var src = img.attr('src');
      // if (/^http/.test(src)) {
      // base.push(src.match(/.+\/(.+\.jpe?g|.+\.png)/)[1]);
      // } else {
      // base.push(img.data('value'));
      // }
      // })
      // if (base.length < photo_min) {
      // showAlert('请至少上传' + photo_min + '张事故照片');
      // return;
      // }
      if (img1.length === 0) {
        showAlert('请上传45°角带车牌拍摄整车的照片');
        return;
      }
      if (img2.length === 0) {
        showAlert('请上传含被撞物体受损的照片');
        return;
      }
      if (img3.length === 0) {
        showAlert('请上传受损处细节的照片');
        return;
      }
      $([img1, img2, img3]).each(function (index, item) {
        var img = $(item);
        var src = img.attr('src');
        if (/^http/.test(src)) {
          base.push(src.match(/.+\/(.+\.jpe?g|.+\.png)/)[1]);
        } else {
          base.push(img.data('value'));
        }
      });
      confirmData({
        token: user.token,
        orderNo: oid,
        carId: cid,
        description: desc,
        base: base
      });
    });

    $('#desc').on('input', function () {
      $('#desc-status').text($('#desc').val().length + '/200');
    });
  }

  function loadData() {
    // 如果有订单id，说明是编辑订单内容，否则是新建订单
    if (oid) {
      getOrderDetail({
        token: user.token,
        orderNo: oid
      }, initData);
    } else {
      getCarDetail({
        token: user.token,
        id: cid
      }, initData);
    }
  }
})();