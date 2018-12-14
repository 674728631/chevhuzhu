'use strict';

(function () {
  var loading = false;
  var user = checkLogin(true);
  var cid = getUrlPar('id');
  var car_data;
  var dri_photo;
  var cnum1;
  var cnum2;
  var cnum_last = '';
  var vkey;
  var province_id = '51';
  var province_name = area_json[province_id].name;
  var city_id = '510100';
  var city_name = '成都市';
  var city_fullname = province_name + city_name;
  var area_cichuan = area_json[province_id].value;
  var data_area = {};
  var data_cnum = { '川A': { name: '川A' }, '川B': { name: '川B' }, '川C': { name: '川C' }, '川D': { name: '川D' }, '川E': { name: '川E' }, '川F': { name: '川F' }, '川G': { name: '川G' }, '川H': { name: '川H' }, '川J': { name: '川J' }, '川K': { name: '川K' }, '川L': { name: '川L' }, '川M': { name: '川M' }, '川N': { name: '川N' }, '川O': { name: '川O' }, '川P': { name: '川P' }, '川Q': { name: '川Q' }, '川R': { name: '川R' }, '川S': { name: '川S' }, '川T': { name: '川T' }, '川U': { name: '川U' }, '川V': { name: '川V' }, '川W': { name: '川W' }, '川X': { name: '川X' }, '川Y': { name: '川Y' }, '川Z': { name: '川Z' } };
  data_cnum = car_number;
  var datapick_area;
  var datapick_cnum;

  if (user) {
    checkToken(user, function () {
      if (cid) {
        getCarDetail({
          token: user.token,
          id: cid
        }, function (data) {
          loadData(data.data);
        });
      } else {
        loadData();
      }
    });
  }

  // for (var key in area_cichuan) {
  // data_area[key] = {
  // name: area_cichuan[key].name,
  // value: null
  // };
  // }
  data_area['510100'] = {
    name: area_cichuan['510100'].name,
    value: null
  };

  datapick_area = datapicker2({
    data: data_area,
    event_ok: function event_ok(data) {
      city_id = data[0].value;
      city_name = data[0].name;
      city_fullname = province_name + data[0].name;
      $('#city').val(city_fullname);
    }
  });

  datapick_cnum = datapicker2({
    data: data_cnum,
    active_level: 2,
    class: 'carpick',
    event_ok: function event_ok(data) {
      cnum1 = data[0].value;
      cnum2 = data[1].value;
      $('#cnum1').val(cnum1 + cnum2);
    }
  });

  // 根据行驶证数据设置页面
  function setDriverData(data) {
    $('#d-plate').val(data.plate_num);
  }

  function confirmData(dt) {
    if (loading) return;
    showLoad();
    loading = true;
    goAPI({
      url: _api.add_car1,
      data: dt,
      success: function success(data) {
        if (data.data.noNeedPay == 1) {
          //window.location.href = '/hfive/view/car_add2.html?id=' + data.data.id + '&rejoin=' + getUrlPar('rejoin');
          showConfirm({
            class: 'success',
            title: '您的' + (cnum1 + cnum2 + cnum_last) + '车辆信息\n提交成功！',
            str: '恭喜您！您的爱车进入30天观察期，观察期后正式生效',
            btn_yes: {
              str: '我知道了',
              href: '/hfive/view/car.html'
            }
          });
          // } else if (car_data) {
          // if (car_data.status == 1 || car_data.status == 30) {
          // window.location.href = '/hfive/view/car_add_pay.html?id=' + data.data.id + '&rejoin=' + getUrlPar('rejoin');
          // } else {
          // window.location.href = '/hfive/view/car_add2.html?id=' + data.data.id + '&rejoin=' + getUrlPar('rejoin');
          // }
        } else {
          window.location.href = '/hfive/view/car_add_pay.html?id=' + data.data.id + '&rejoin=' + getUrlPar('rejoin');
        }
      },
      error: function error(msg, code, data) {
        if (code == 489) {
          // 该车辆已存在，将引导用户跳转到车辆列表页面
          showConfirm({
            class: 'fail',
            title: '您的' + (cnum1 + cnum2 + cnum_last) + '车辆信息提交失败！\n请重新提交',
            str: '失败原因：' + msg,
            btn_yes: {
              str: '我知道了',
              href: '/hfive/view/car.html'
            }
          });
        } else {
          showConfirm({
            class: 'fail',
            title: '您的' + (cnum1 + cnum2 + cnum_last) + '车辆信息提交失败！\n请重新提交',
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
          str: data
        });
      },
      complete: function complete() {
        hideLoad();
        loading = false;
      }
    });
  }

  function setVkey(val) {}

  function loadData(data) {
    $('.container').html(juicer($('#main-tpl').html(), data));

    if (data) {
      car_data = data;
      if (data.drvingCity) {
        city_fullname = data.drvingCity;
      }
      cnum1 = data.licensePlateNumber.substr(0, 1).toUpperCase();
      cnum2 = data.licensePlateNumber.substr(1, 1).toUpperCase();
      cnum_last = data.licensePlateNumber.substr(2).toUpperCase();
    } else {
      cnum1 = '川';
      cnum2 = 'A';
    }

    vkey = vkeyboard({
      type: 'keyboard',
      init: cnum_last,
      input: $('#cnum-keyboard'),
      max_length: 10
    });

    $('#city').val(city_fullname);
    $('#cnum1').val(cnum1 + cnum2);

    $('#btn-help').on('click', function () {
      showConfirm({
        str: '因维修覆盖范围，目前互助只针对大成都（都江堰 、简阳 、眉山 、资阳除外）范围车主',
        btn_yes: {
          str: '我知道了'
        }
      });
    });

    $('#city-picker').on('click', function () {
      datapick_area.show([city_id]);
    });

    $('#cnum-picker').on('click', function () {
      datapick_cnum.show([cnum1, cnum2]);
    });

    $('#btn-pay').on('click', function () {
      cnum_last = vkey.val().trim();
      if (city_fullname.length === 0) {
        showAlert('请选择城市');
        return;
      }
      if (cnum_last.length === 0) {
        showAlert('请输入车牌号其余位数');
        return;
      }
      if (!validateCar(cnum1 + cnum2 + cnum_last)) {
        showAlert('车牌号格式不正确');
        return;
      }
      showConfirm({
        title: '重要提示',
        str: '请再次确认您的车牌是否为' + cnum1 + cnum2 + cnum_last,
        btn_close: true,
        btn_cancel: {
          str: '重填'
        },
        btn_yes: {
          str: '下一步',
          event_click: function event_click() {
            showConfirm({
              title: '重要提示',
              str: '<div class="fsize-14"><span class="txt-lv1" style="line-height:2">A 加入条件</span><br>（1）长期行驶地在大成都范围私家车<br>（2）8年以内私家车<br>（3）上年度保险理赔不超过3次<br>（4）面包车不能加入<br><br><span class="txt-lv1" style="line-height:2">B 擦刮救助范围</span><br>（1）碰撞、擦刮、玻璃破碎、爆胎、倒车镜损坏均可互助<br>（2）旧伤车可加入但旧伤不赔，划痕不赔<br><br><span class="txt-lv1" style="line-height:2">C 会员账户</span><br>（1）首充9元起成为会员，享1000元/年擦刮救助额度<br>（2）每次事故分摊不超过0.1元<br>（3）账户余额不低于0元</duv>',
              color: '#999',
              class: 'type3 tl',
              btn_close: true,
              btn_yes: {
                str: '下一步'
              },
              event_close: function event_close() {
                confirmData({
                  id: cid,
                  token: user.token,
                  licensePlateNumber: (cnum1 + cnum2 + cnum_last).toUpperCase(),
                  drvingCity: city_fullname
                });
              }
            });
          }
        }
      });
    });
  }
})();