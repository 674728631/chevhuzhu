'use strict';

(function () {
  var loading = false;
  var user = checkLogin(true);
  var cid = getUrlPar('id');
  var oid = getUrlPar('oid');
  var car_data;
  var dri_photo;
  var plate = '';

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

  // 识别行驶证图片
  function getDriverData(img) {
    if (loading) return;
    showLoad({ str: '图片识别中' });
    loading = true;
    goAPI({
      url: _api.recognition_driver,
      data: {
        image: img.id,
        side: 'face'
      },
      success: function success(data) {
        dri_photo = img;
        setDriverData(data.data);
        $('.reupload-box').removeClass('hide');
        $window.scrollTop(0);
      },
      error: function error(data) {
        showConfirm({
          str: data,
          btn_yes: {
            event_click: function event_click() {
              if (dri_photo) {
                //$('#img1').empty().insertImg(dri_photo, 'full');
                $('#img1').html('<img src="' + dri_photo.src + '" onload="resetImg(this, \'full\')" data-value="' + dri_photo.id + '">');
              } else {
                $('#img1').empty();
                $('.btn-upload').addClass('default');
              }
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

  // 根据行驶证数据设置页面
  function setDriverData(data) {
    $('#d-owner').val(data.owner);
    //$('#d-plate').val(data.plate_num);
    $('#d-vin').val(data.vin);
    $('#d-model').val(data.model);
    $('#d-engine').val(data.engine_num);
    var dt_register = toDate(data.register_date);
    if (dt_register) {
      $('#d-register').val(dt_register.formatDate('yyyy-MM-dd'));
    } else {
      $('#d-register').val('');
    }
    var dt_issue = toDate(data.issue_date);
    if (dt_issue) {
      $('#d-issue').val(dt_issue.formatDate('yyyy-MM-dd'));
    } else {
      $('#d-issue').val('');
    }
    //if (data.owner && validateCar(data.plate_num) && data.model) {
    if (data.owner && data.model) {
      $('#d-owner2').text(data.owner);
      //$('#d-plate2').text(data.plate_num);
      $('#d-model2').text(data.model);
      $('#driver-info').addClass('hide');
      $('#driver-info2').removeClass('hide');
    } else {
      $('#driver-info').removeClass('hide');
      $('#driver-info2').addClass('hide');
    }
  }

  function confirmData(dt) {
    if (loading) return;
    showLoad();
    loading = true;
    goAPI({
      url: _api.add_license,
      data: dt,
      success: function success(data) {
        showConfirm({
          class: 'success',
          full: true,
          title: '您的' + plate + '车辆信息\n提交成功！',
          str: '我们将在一个工作日内完成理赔审核，请留意信息，车V互助为您的爱车保驾护航！',
          btn_yes: {
            str: '我知道了',
            href: '/hfive/view/baoxian_order.html'
          }
        });
      },
      error: function error(msg, code, data) {
        showConfirm({
          class: 'fail',
          full: true,
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
          cbk(data.data);
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

  function initData(data) {
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

    if (data) {
      car_data = data;
      plate = car_data.licensePlateNumber;
      $('#d-plate2').text(plate);
      if (!cid && car_data.carId) {
        cid = car_data.carId;
      }
      if (data.drivingLicense) {
        dri_photo = { src: data.drivingLicense, id: '' };
        $('#img1').insertImg(data.drivingLicense, 'full').closest('.btn-upload').removeClass('default');
        $('.reupload-box').removeClass('hide');
        setDriverData({
          owner: data.nameCarOwner,
          model: data.model,
          plate_num: data.licensePlateNumber,
          vin: data.VIN,
          engine_num: data.engineNum,
          register_date: data.registerDate,
          issue_date: data.issueDate
        });
      }
    }

    $('#btn-edit').on('click', function () {
      $('#driver-info').removeClass('hide');
      $('#driver-info2').addClass('hide');
    });

    // $('.btn-upload input').on('change', function(){
    // if (loading) return;
    // loading = true;
    // showLoad();
    // var $this = $(this);
    // var file = this.files[0];
    // resizeImg({
    // file: file,
    // width: 1200,
    // success: function(rst){
    // loading = false;
    // $this.val('').siblings('.upload-img').empty().insertImg(rst.base64, 'full').closest('.btn-upload').removeClass('default');
    // getDriverData(rst.base64);
    // $('.reupload-box').removeClass('hide');
    // },
    // fail: function(){
    // loading = false;
    // hideLoad();
    // showAlert('无法处理该图片\n请选择其它图片');
    // }
    // });
    // })

    $('#upload-car').on('click', function () {
      if (!_config.is_wx_mobile) {
        showAlert('请在手机微信客户端中上传照片');
        return;
      }
      if (!_config.wx_config) {
        showAlert('正在调用摄像头...请稍后再点击');
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
              $photo.removeClass('default').find('.upload-img').html('<img src="' + ids[0] + '" onload="resetImg(this, \'full\')" data-value="' + serverId + '">');
              getDriverData({ src: ids[0], id: serverId });
              $('.reupload-box').removeClass('hide');
            }
          });
        }
      });
    });

    $('#btn-pay').on('click', function () {
      var img = $('#img1 img');
      var owner = $('#d-owner').val().trim();
      //var plate = $('#d-plate').val().trim();
      var model = $('#d-model').val().trim();
      var vin = $('#d-vin').val().trim();
      var engine = $('#d-engine').val().trim();
      var register = $('#d-register').val().trim();
      var issue = $('#d-issue').val().trim();
      if (img.length === 0) {
        showAlert('请上传行驶证照片');
        return;
      }
      if (owner.length === 0) {
        showAlert('行驶证所有人不能为空');
        return;
      }
      // if (plate.length === 0) {
      // showAlert('行驶证车牌号不能为空');
      // return;
      // }
      // if (!validateCar(plate)) {
      // showAlert('车牌号格式不正确');
      // return;
      // }
      if (model.length === 0) {
        showAlert('行驶证品牌型号不能为空');
        return;
      }
      // if (vin.length === 0) {
      // showAlert('行驶证车辆识别代码不能为空');
      // return;
      // }
      // if (vin.length !== 17 && vin.length !== 18) {
      // showAlert('行驶证车辆识别代码必须为17-18位字符');
      // return;
      // }
      // if (engine.length === 0) {
      // showAlert('行驶证发动机号不能为空');
      // return;
      // }
      // if (register.length === 0) {
      // showAlert('行驶证注册日期不能为空');
      // return;
      // }
      // if (issue.length === 0) {
      // showAlert('行驶证发证日期不能为空');
      // return;
      // }
      confirmData({
        carId: cid,
        orderNo: oid,
        token: user.token,
        base: /^http/.test(img.eq(0).attr('src')) ? '' : img.eq(0).data('value'),
        nameCarOwner: owner,
        //licensePlateNumber: plate.toUpperCase(),
        // brand: brand,
        // carType: type,
        VIN: vin,
        model: model,
        engineNum: engine,
        registerDate: register ? toDate(register).formatDate('yyyy/MM/dd') : '',
        issueDate: issue ? toDate(issue).formatDate('yyyy/MM/dd') : ''
      });
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