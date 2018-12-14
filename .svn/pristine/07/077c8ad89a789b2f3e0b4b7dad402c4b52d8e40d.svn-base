'use strict';

(function () {
  var loading = false;
  var oid = getUrlPar('id');
  var mid = getUrlPar('mid');
  var user = checkLogin(true);
  var data_order;
  var data_imgs = [];
  var preview_imgs = preview({
    fixed: true,
    background: '#000000',
    number: true,
    point: true,
    fill: 'fix'
  });
  var pay_wait = null;
  var position = { ads: '' };
  var geolocation;
  var geocoder;
  var marker;
  var map;

  var juicer_stime = function juicer_stime(time) {
    var h = Math.floor(time / 3600);
    if (h > 0) {
      var s = Math.ceil(time % 3600 / 60);
      if (s >= 60) {
        return h + 1 + '小时';
      } else if (s === 0) {
        return h + '小时';
      } else {
        return h + '小时' + s + '分';
      }
    } else {
      return Math.ceil(time / 60) + '分';
    }
  };
  juicer.register('build_stime', juicer_stime);

  if (user) {
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
  }

  // 设置步骤图标
  function setOrderStep(status) {
    var step = 0;
    if (status == 1 || status == 2) {
      step = 1;
    } else if (status == 3 || status == 10 || status == 11 || status == 12 || status == 21) {
      step = 2;
    } else if (status == 31 || status == 32) {
      step = 3;
    } else if (status == 41 || status == 42) {
      step = 4;
    } else {
      step = 5;
    }
    $('.step-box .point').each(function (index) {
      if (index < step) {
        $(this).addClass('active');
      }
    });
  }

  // 设置倒计时
  function setTimer(time) {
    if (time >= 0) {
      var h = Math.floor(time / 3600);
      var m = Math.floor(time % 3600 / 60);
      var s = Math.floor(time % 60);
      var str;
      if (h > 0) {
        str = h + '时';
        if (m > 9) {
          str += m + '分';
        } else {
          str += '0' + m + '分';
        }
        if (s > 9) {
          str += s + '秒';
        } else {
          str += '0' + s + '秒';
        }
      } else if (m > 0) {
        str = m + '分';
        if (s > 9) {
          str += s + '秒';
        } else {
          str += '0' + s + '秒';
        }
      } else {
        str = s + '秒';
      }
      $('#time').text(str);
      setTimeout(function () {
        setTimer(time - 1);
      }, 1000);
    } else {
      $('#time').text('已超时');
    }
  }

  // 设置高德地图
  function setMap() {
    if (map) {
      return;
    }
    map = new AMap.Map('map-main', {
      center: [104.065764, 30.657462],
      zoom: 14
    });
    // 点击地图上的某个点
    map.on('click', function (e) {
      map.setCenter(e.lnglat);
      if (geocoder) {
        geocoder.getAddress(e.lnglat, function (status, result) {
          if (status === 'complete') {
            position = {
              lat: e.lnglat.lat,
              lng: e.lnglat.lng,
              ads: result.regeocode.formattedAddress
            };
            $('#map-address').text(position.ads);
          }
        });
      }
      if (marker) {
        marker.setPosition(e.lnglat);
      } else {
        marker = new AMap.Marker({
          position: e.lnglat,
          map: map
        });
      }
    });
    map.plugin(['AMap.Geolocation', 'AMap.Geocoder', 'AMap.ToolBar', 'AMap.Scale'], function () {
      geolocation = new AMap.Geolocation({
        enableHighAccuracy: true, //是否使用高精度定位，默认:true
        timeout: 5000, //超过5秒后停止定位，默认：无穷大
        maximumAge: 0, //定位结果缓存0毫秒，默认：0
        convert: true, //自动偏移坐标，偏移后的坐标为高德坐标，默认：true
        showButton: true, //显示定位按钮，默认：true
        buttonPosition: 'LB', //定位按钮停靠位置，默认：'LB'，左下角
        buttonOffset: new AMap.Pixel(10, 20), //定位按钮与设置的停靠位置的偏移量，默认：Pixel(10, 20)
        showMarker: true, //定位成功后在定位到的位置显示点标记，默认：true
        showCircle: true, //定位成功后用圆圈表示定位精度范围，默认：true
        panToLocation: true, //定位成功后将定位到的位置作为地图中心点，默认：true
        zoomToAccuracy: true //定位成功后调整地图视野范围使定位位置及精度范围视野内可见，默认：false
      });
      geocoder = new AMap.Geocoder({
        batch: false
      });
      map.addControl(new AMap.ToolBar());
      map.addControl(new AMap.Scale());
      map.addControl(geolocation);
      geolocation.getCurrentPosition();
      $('#map-address').text('自动定位中');
      $('#caddress').val('自动定位中');
      //定位成功
      AMap.event.addListener(geolocation, 'complete', function (result) {
        if (marker) {
          marker.setPosition(result.position);
        } else {
          marker = new AMap.Marker({
            position: result.position,
            map: map
          });
        }
        position = {
          lat: result.position.lat,
          lng: result.position.lng,
          ads: result.formattedAddress
        };
        $('#map-address').text(position.ads);
        $('#caddress').val(position.ads);
      });
      //定位失败
      AMap.event.addListener(geolocation, 'error', function (err) {
        showAlert('定位失败，错误原因：' + err.message);
        if ($('#map-address').text() === '自动定位中') {
          $('#map-address').text('请选择当前位置');
        }
        $('#caddress').val('');
      });
    });
  }

  // 提交联系人信息
  function submitContacer(dt) {
    if (loading) return;
    showLoad();
    loading = true;
    goAPI({
      url: _api.baoxian_submit_contacter,
      data: dt,
      success: function success(data) {
        showConfirm({
          str: '提交成功',
          btn_yes: {
            event_click: function event_click() {
              window.location.reload();
            }
          }
        });
      },
      error: function error(msg) {
        showAlert(msg);
      },
      complete: function complete() {
        hideLoad();
        loading = false;
      }
    });
  }

  function wxPay(dt) {
    if (loading) return;
    showLoad();
    loading = true;
    goAPI({
      url: _api.baoxian_order_pay,
      data: dt,
      traditional: true,
      success: function success(data) {
        wx.chooseWXPay({
          appId: data.data.appId,
          timestamp: data.data.timeStamp.toString(),
          nonceStr: data.data.nonceStr,
          package: data.data.package,
          signType: data.data.signType,
          paySign: data.data.paySign,
          success: function success(res) {
            showConfirm({
              str: '支付成功',
              btn_yes: {
                event_click: function event_click() {
                  window.location.reload();
                }
              }
            });
          }
        });
      },
      error: function error(msg) {
        showConfirm({
          str: msg
        });
      },
      complete: function complete() {
        hideLoad();
        loading = false;
      }
    });
  }

  // 支付
  function pay(dt) {
    if (loading) return;
    if (!_config.is_wx_mobile) {
      showConfirm({
        str: '请使用微信移动客户端打开该页面再进行支付'
      });
      return;
    }
    if (!_config.wx_config) {
      pay_wait = dt;
    } else {
      wxPay(dt);
    }
  }

  // 支付（测试环境用）
  function pay_test(dt) {
    if (loading) return;
    showLoad();
    loading = true;
    goAPI({
      url: _api.baoxian_order_pay_test,
      data: dt,
      traditional: true,
      success: function success(data) {
        showConfirm({
          str: '支付成功',
          btn_yes: {
            event_click: function event_click() {
              window.location.reload();
            }
          }
        });
      },
      error: function error(msg) {
        showConfirm({
          str: msg
        });
      },
      complete: function complete() {
        hideLoad();
        loading = false;
      }
    });
  }

  // 确认维修中心来接车
  function confirmOrder(dt) {
    if (loading) return;
    showLoad();
    loading = true;
    goAPI({
      url: _api.baoxian_order_confirm,
      data: dt,
      success: function success(data) {
        showConfirm({
          str: '操作成功',
          btn_yes: {
            event_click: function event_click() {
              window.location.reload();
            }
          }
        });
      },
      error: function error(msg) {
        showConfirm({
          str: msg
        });
      },
      complete: function complete() {
        hideLoad();
        loading = false;
      }
    });
  }

  // 投诉
  function reportOrder(dt, cbk) {
    if (loading) return;
    showLoad();
    loading = true;
    goAPI({
      url: _api.baoxian_order_report,
      data: dt,
      success: function success(data) {
        if ($.isFunction(cbk)) {
          cbk();
        }
      },
      error: function error(msg) {
        showAlert(msg);
      },
      complete: function complete() {
        hideLoad();
        loading = false;
      }
    });
  }

  // 加载页面
  function loadData() {
    showLoad();
    goAPI({
      url: _api.baoxian_order_detail,
      data: {
        token: user.token,
        orderNo: oid,
        messageId: mid
      },
      success: function success(data) {
        data_order = data.data;
        if (data_order.status == 4 && data_order.isInvalid != 10) {
          window.location.href = '/hfive/view/baoxian_order_add2.html?oid=' + oid;
          return;
        }
        if (_config.is_wx_mobile && !_config.wx_config) {
          configWXJSSDK(['previewImage', 'chooseWXPay'].concat(_config.wx_share), {
            success: function success() {
              // shareWX({
              // title: _share_title,
              // desc: _share_desc,
              // link: _share_link
              // })
              if (pay_wait) wxPay(pay_wait);
            }
          });
        }
        if (data_order.labelContent) {
          data_order.labelContent = data_order.labelContent.split('_');
        } else {
          data_order.labelContent = null;
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

        if (data_order.isInvalid != 10) {
          setOrderStep(data_order.status);
        }
        if ($('#time').length > 0) {
          setTimer(data_order.interval);
        }

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

        // 地图选点后点击确认
        $('#map-confirm').on('click', function () {
          if (position.ads) {
            $('#caddress').val(position.ads);
          }
          $('#map-layer').addClass('hide');
        });

        // 支付
        $('#btn-confirm').on('click', function () {
          pay_test({
            token: user.token,
            orderNo: data_order.orderNo
          });
        });

        // 投诉
        $('#btn-report').on('click', function () {
          showConfirm({
            class: 'shadow-blue',
            cover_background: 'rgba(255,255,255,0.5)',
            cover_close: true,
            str: '亲，维修中我们有很多不足地方需要改进，感谢对车V互助的支持，请认真填写您的投诉内容，方便后续工作人员与您联系。',
            node: '<div id="report-box"><textarea id="report" class="fsize-14" maxlength="200" placeholder="亲，请填写您的投诉内容，方便我们工作人员核实处理！"></textarea><p id="report-status" class="tr">0/200</p></div>',
            event_init: function event_init() {
              $('#report').off().on('input', function () {
                $('#report-status').text($('#report').val().length + '/200');
              });
            },
            btn_yes: {
              click_close: false,
              event_click: function event_click() {
                var content = $('#report').val().trim();
                if (content.length === 0) {
                  showAlert('请填写投诉内容');
                  return;
                }
                reportOrder({
                  token: user.token,
                  orderNo: data_order.orderNo,
                  content: content
                }, function () {
                  showConfirm({
                    str: '操作成功',
                    btn_yes: {
                      event_click: function event_click() {
                        window.location.reload();
                      }
                    }
                  });
                });
              }
            },
            btn_cancel: {
              str: '取消'
            }
          });
        });

        // 撤销投诉并且确认维修中心已交车
        $('#btn-resolve').on('click', function () {
          showConfirm({
            str: '您确定投诉问题已经解决并且确认维修中心已经交车给您了？',
            class: 'shadow-blue',
            cover_background: 'rgba(255,255,255,0.5)',
            cover_close: true,
            btn_yes: {
              event_click: function event_click() {
                reportOrder({
                  token: user.token,
                  orderNo: data_order.orderNo
                }, function () {
                  loading = false;
                  pay_test({
                    token: user.token,
                    orderNo: data_order.orderNo
                  });
                });
              }
            },
            btn_cancel: {
              str: '取消'
            }
          });
        });

        // 确认维修中心来接车
        $('#btn-pay').on('click', function () {
          showConfirm({
            title: '亲，您确定维修中心已接车？',
            str: '维修中心人员来接车，请先核对下其身份',
            color: '#3196fe',
            class: 'shadow-blue',
            cover_background: 'rgba(255,255,255,0.5)',
            cover_close: true,
            btn_yes: {
              event_click: function event_click() {
                confirmOrder({
                  token: user.token,
                  orderNo: data_order.orderNo
                });
              }
            },
            btn_cancel: {
              str: '取消'
            }
          });
        });

        // 申请维修中心来接车
        $('#btn-apply').on('click', function () {
          showConfirm({
            class: 'shadow-blue contact',
            cover_background: 'rgba(255,255,255,0.5)',
            node: '<div class="citem fbox-ac"><p>接车地址</p><div id="btn-address" class="ibox fbox-ac fbox-f1 hover"><input id="caddress" type="text" maxlength="100" value="' + position.ads + '" placeholder="点击打开地图" readonly></div></div><div class="citem fbox-ac"><p>联系人　</p><div class="ibox fbox-ac fbox-f1"><input id="cname" type="text" maxlength="50" placeholder="请输入联系人姓名" value="' + data_order.nameCarOwner + '"><a class="fbox-cc cdel hover hide" href="javascript:;"><span></span></a></div></div><div class="citem fbox-ac"><p>手机号　</p><div class="ibox fbox-ac fbox-f1"><input id="cphone" type="tel" maxlength="11" placeholder="请输入联系人手机号" value="' + user.phone + '"><a class="fbox-cc cdel hover hide" href="javascript:;"><span></span></a></div></div><div class="citem fbox-ac"><p>接车时间</p><div class="ibox fbox-ac fbox-f1"><input id="cdate" class="hover" type="text" maxlength="50" placeholder="请选择接车时间" value="' + '' + '" readonly><a class="fbox-cc cdel hover hide" href="javascript:;"><span></span></a></div></div>',
            btn_yes: {
              click_close: false,
              event_click: function event_click() {
                var cname = $('#cname').val().trim();
                var cphone = $('#cphone').val().trim();
                var cdate = $('#cdate').val().trim();
                if (!position.ads) {
                  showAlert('请选择接车地址');
                  return;
                }
                if (cname.length === 0) {
                  showAlert('请填写联系人');
                  return;
                }
                if (cphone.length === 0) {
                  showAlert('请填写手机号');
                  return;
                }
                if (!validatePhone(cphone, true)) {
                  showAlert('手机号格式有误');
                  return;
                }
                if (cdate.length === 0) {
                  showAlert('请选择接车时间');
                  return;
                }
                submitContacer({
                  token: user.token,
                  orderNo: oid,
                  nameCarOwner: cname,
                  telCarOwner: cphone,
                  reciveCarTime: cdate,
                  place: position.ads,
                  longitude: position.lng * 1000000,
                  latitude: position.lat * 1000000
                });
              }
            },
            btn_cancel: {
              str: '取消'
            },
            event_init: function event_init() {
              setMap();
              $('#btn-address').off().on('click', function () {
                $('#map-layer').removeClass('hide');
              });
              $('.contact input').off().on('input', function () {
                var $this = $(this);
                var val = $this.val().trim();
                if (val.length > 0) {
                  $this.siblings('.cdel').removeClass('hide');
                } else {
                  $this.siblings('.cdel').addClass('hide');
                }
              });
              $('.contact .cdel').off().on('click', function () {
                var $this = $(this);
                $this.addClass('hide').siblings('input').val('');
              });
              $('#cdate').off().on('click', function () {
                var cdate = {};
                var chour = {};
                var cmin = {};
                var data_time = [];
                var data_time_date = {};
                var data_time_hour = {};
                var data_time_min = {};
                var data_time_now = new Date();
                var data_first_day_start = new Date(data_time_now.getFullYear(), data_time_now.getMonth(), data_time_now.getDate(), data_time_now.getHours() + 3, data_time_now.getMinutes(), data_time_now.getSeconds());
                var data_first_day_begin = new Date(data_time_now.getFullYear(), data_time_now.getMonth(), data_time_now.getDate(), 12, 0, 0);
                var data_first_day_end = new Date(data_time_now.getFullYear(), data_time_now.getMonth(), data_time_now.getDate(), 17, 59, 59);
                var data_first_day_hour = {};
                var data_first_day_min = {};
                if (data_first_day_start < data_first_day_begin) {
                  data_first_day_start = data_first_day_begin;
                } else if (data_first_day_start > data_first_day_end) {
                  data_first_day_start = null;
                }
                for (var i = 0; i < 7; i++) {
                  if (i === 0 && data_first_day_start || i > 0) {
                    var item = new Date(data_time_now.getFullYear(), data_time_now.getMonth(), data_time_now.getDate() + i).formatDate('yyyy-MM-dd');
                    data_time_date[i] = { name: item };
                  }
                }
                for (var i = 12; i < 18; i++) {
                  data_time_hour[i] = { name: '' + i };
                }
                for (var i = 0; i < 60; i++) {
                  data_time_min[i] = { name: i < 10 ? '0' + i : '' + i };
                }
                if (data_first_day_start) {
                  for (var i = data_first_day_start.getHours(); i < 18; i++) {
                    data_first_day_hour[i] = { name: '' + i };
                  }
                  for (var i = data_first_day_start.getMinutes(); i < 60; i++) {
                    data_first_day_min[i] = { name: i < 10 ? '0' + i : '' + i };
                  }
                  data_time.push(data_time_date, data_first_day_hour, data_first_day_min);
                } else {
                  data_time.push(data_time_date, data_time_hour, data_time_min);
                }
                var datapick_time = datapicker({
                  class: 'node',
                  node: '<p class="time-title fbox-cc">预计接车时间</p><p class="time-txt fbox-cc"></p><div class="time-header fbox-ac"><p class="fbox-f1">日期<p/><p class="fbox-f1">时<p/><p class="fbox-f1">分</p></div>',
                  event_scroll: function event_scroll(data, index) {
                    if (data_first_day_start && data[0].name === data_first_day_start.formatDate('yyyy-MM-dd')) {
                      if (index === 0) {
                        data_time[1] = data_first_day_hour;
                        if (!data_first_day_hour[data[1].name] || data_first_day_start.getHours() == data[1].name) {
                          data_time[2] = data_first_day_min;
                        } else {
                          data_time[2] = data_time_min;
                        }
                      } else if (index === 1) {
                        if (data[1].name == data_first_day_start.getHours()) {
                          data_time[2] = data_first_day_min;
                        } else {
                          data_time[2] = data_time_min;
                        }
                      }
                      datapick_time.show([data[0].value, data[1].value, data[2].value], data_time);
                    } else if (index === 0) {
                      data_time = [data_time_date, data_time_hour, data_time_min];
                      datapick_time.show([data[0].value, data[1].value, data[2].value], data_time);
                    } else {
                      $('.time-txt').text(data[0].name + '　' + data[1].name + ':' + data[2].name);
                    }
                  },
                  event_show: function event_show(data) {
                    cdate = data[0];
                    chour = data[1];
                    cmin = data[2];
                    $('.time-txt').text(data[0].name + '　' + data[1].name + ':' + data[2].name);
                  },
                  event_ok: function event_ok(data) {
                    cdate = data[0];
                    chour = data[1];
                    cmin = data[2];
                    $('#cdate').val(cdate.name + ' ' + chour.name + ':' + cmin.name + ':00');
                  }
                });
                datapick_time.show([cdate.value, chour.value, cmin.value], data_time);
              });
            }
          });
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