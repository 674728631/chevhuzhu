'use strict';

(function () {
  var user = checkLogin();
  var car_trans = 0;
  var newer = [];
  var newmsg = [];
  var newer_showing = false; // 当前是否正在展示新用户加入提示
  var newmsg_now = null; // 当前正在展示的消息体
  var btn_join; // 加入按钮
  var tpl = juicer($('#main-tpl').html());
  var sse_msg;
  var sse_join;
  var car_st;
  var top_height;
  var from_id = getUrlPar('fromid');

  if (from_id) {
    saveStorage('from_id', from_id, true);
  }

  // 获取已登录用户的新消息提示
  function getNewMsg(token) {
    if (!token) {
      return;
    }
    try {
      if (sse_msg) {
        sse_msg.close();
        newmsg_now = null;
      }
      sse_msg = new EventSource(_api.newmsg + '?token=' + token);
      sse_msg.onmessage = function (event) {
        if (event.data.trim() === 'null') {
          sse_msg.close();
        } else {
          var data = event.data.trim().split('|');
          if (data.length > 0) {
            var i = data.length - 1;
            if (data[i] !== '') {
              var item = data[i].split('_');
              newmsg.push({
                content: item[0],
                type: item[1],
                val: item[2],
                id: item[3]
              });
            }
            showNewMsg();
          }
        }
      };
    } catch (e) {}
  }

  // 获取新加入用户提示
  function getNewer() {
    try {
      if (sse_join) {
        sse_join.close();
        newer_showing = false;
      }
      sse_join = new EventSource(_api.newer);
      sse_join.onmessage = function (event) {
        var data = event.data.trim().split('|');
        if (data.length > 0) {
          for (var i = 0, j = data.length; i < j; i++) {
            if (data[i] !== '') {
              var item = data[i].split('_');
              var desc = item[0];
              var avatar = item[1] ? item[1] : '/hfive/img/default_avatar_1.png';
              newer.push({
                desc: desc,
                avatar: avatar
              });
            }
          }
          showNewer();
        }
      };
    } catch (e) {}
  }

  // 弹出已登录用户的新消息提示
  function showNewMsg() {
    if (newmsg.length === 0) {
      return;
    }
    var data = newmsg.splice(0, 1);
    if (newmsg_now) {
      if (newmsg_now.id === data[0].id) {
        return;
      }
      $('#msg-item').removeClass('active');
      setTimeout(function () {
        if (data[0].type == 31) {
          $('#msg-item').attr('href', '/hfive/view/car.html?mid=' + data[0].id);
        } else if (data[0].type == 41) {
          if (data[0].val) {
            $('#msg-item').attr('href', '/hfive/view/order_detail.html?mid=' + data[0].id + '&id=' + data[0].val);
          } else {
            $('#msg-item').attr('href', '/hfive/view/order.html');
          }
        } else {
          $('#msg-item').attr('href', 'javascript:;');
        }
        $('#msg-content').text(data[0].content);
        $('#msg-item').addClass('active');
        newmsg_now = data[0];
      }, 500);
    } else {
      if (data[0].type == 31) {
        $('#msg-item').attr('href', '/hfive/view/car.html?mid=' + data[0].id);
      } else if (data[0].type == 41) {
        if (data[0].val) {
          $('#msg-item').attr('href', '/hfive/view/order_detail.html?mid=' + data[0].id + '&id=' + data[0].val);
        } else {
          $('#msg-item').attr('href', '/hfive/view/order.html');
        }
      } else {
        $('#msg-item').attr('href', 'javascript:;');
      }
      $('#msg-content').text(data[0].content);
      $('#msg-item').addClass('active');
      newmsg_now = data[0];
    }
  }

  // 弹出新加入用户提示
  function showNewer() {
    if (newer_showing || newer.length === 0) {
      return;
    }
    newer_showing = true;
    var data = newer.splice(0, 1);
    $('#newer-avatar').css({ 'width': 'auto', 'height': 'auto' }).attr('src', data[0].avatar);
    $('#newer-desc').text(data[0].desc);
    $('#newer-item').addClass('active');
    setTimeout(function () {
      $('#newer-item').removeClass('active');
      setTimeout(function () {
        newer_showing = false;
        if (newer.length > 0) {
          showNewer();
        }
      }, 500);
    }, 3500);
  }

  // 滚动车主信息
  function transCar(len) {
    var dis = 1.4;
    if (car_st) {
      clearTimeout(car_st);
    }
    car_st = setTimeout(function () {
      car_trans -= dis;
      if (car_trans < (1 - len) * dis) {
        car_trans = 0;
      }
      $('#car-content').css({ 'webkit-transform': 'translateY(' + car_trans + 'rem) translateZ(0)', 'transform': 'translateY(' + car_trans + 'rem) translateZ(0)' });
      transCar(len);
    }, 4500);
  }

  // 滑动板块
  function scrollPanel() {
    var $scroll = $('#scroll-panel');
    var $desc = $scroll.closest('.intro-fbox').siblings('.intro-desc');
    var _length = $scroll.find('.intro-item').length;
    var _moving = false;
    var _touching = false;
    var _scroll_length = 4;
    var _scroll_width;
    var _scroll_left;
    var _scroll_index = 0;
    var _scroll_index_active = 0;
    var _scroll_index_start = 0;
    var _scroll_index_end = _length - _scroll_length;
    var _startX;
    var _lastX;
    var _moveX;
    var _disX;
    var _tempX;
    var _dis = 0;
    $('#scroll-box').on('swipeLeft', function () {
      moveTo(_scroll_index_active + 1);
    }).on('swipeRight', function () {
      moveTo(_scroll_index_active - 1);
    });
    //$scroll.on({'touchstart': scrollstart, 'mousedown': scrollstart}).on({'touchmove': scrollmove, 'mousemove': scrollmove}).on({'touchend': scrollend, 'mouseup': scrollend})
    $scroll.on('click', '.intro-item', function () {
      var $this = $(this);
      if (!$this.hasClass('active') && !_moving) {
        moveTo($this.index());
      }
    });
    function scrollstart(e) {
      if (_moving) return;
      e.preventDefault();
      _touching = true;
      _scroll_width = $scroll.find('.intro-item').eq(0).width();
      _scroll_left = _scroll_width * (_scroll_index_start - _scroll_index);
      _startX = e.touches ? e.touches[0].pageX : e.pageX - $scroll.offset().left;
      _moveX = _startX;
      _lastX = _moveX;
    }

    function scrollmove(e) {
      if (!_touching) return;
      _lastX = _moveX;
      _moveX = e.touches ? e.touches[0].pageX : e.pageX - $scroll.offset().left;
      _disX = _moveX - _startX;
      _tempX = _scroll_left + _disX;
      if (_tempX > 0) {
        _tempX = 0;
      } else if (_tempX < _scroll_width * (_scroll_length - _length)) {
        _tempX = _scroll_width * (_scroll_length - _length);
      }
      $scroll.css('left', _tempX);
    }

    function scrollend(e) {
      if (!_touching) {
        _touching = false;
        return;
      }
      _touching = false;
      _dis = _moveX - _startX;
      if (Math.abs(_dis) < 5) {
        var ta = $(e.target);
        if (!ta.hasClass('intro-item')) {
          ta = ta.parent();
        }
        if (ta.hasClass('intro-item')) {
          moveTo(ta.index());
        }
        return;
      }
      if (Math.abs(_dis) > 50) {
        if (_dis > 0 && _moveX - _lastX >= 0 && _scroll_index > _scroll_index_start) {
          _scroll_index -= Math.round(_dis / _scroll_width);
          if (_scroll_index < _scroll_index_start) {
            _scroll_index = _scroll_index_start;
          }
        } else if (_dis <= 0 && _moveX - _lastX <= 0 && _scroll_index < _scroll_index_end) {
          _scroll_index -= Math.round(_dis / _scroll_width);
          if (_scroll_index > _scroll_index_end) {
            _scroll_index = _scroll_index_end;
          }
        }
      }
      _scroll_left = _scroll_width * (_scroll_index_start - _scroll_index);
      _moving = true;
      $scroll.animate({ "left": _scroll_left }, 100);
      setTimeout(function () {
        $desc.find('p').removeClass('show').filter('.p' + (_scroll_index + _scroll_index_active + 1)).addClass('show');
        $scroll.find('.intro-item').removeClass('active').eq(_scroll_index + _scroll_index_active).addClass('active');
        _moving = false;
      }, 110);
    }

    function moveTo(index) {
      _scroll_index_active = index - _scroll_index;
      if (_scroll_index_active < 0 || _scroll_index_active > _scroll_length - 1) {
        if (_scroll_index_active < 0) {
          _scroll_index_active = 0;
        } else if (_scroll_index_active > _scroll_length - 1) {
          _scroll_index_active = _scroll_length - 1;
        }
        // _scroll_index += index - _scroll_index - _scroll_index_active;
        // _scroll_left = _scroll_width * (_scroll_index_start - _scroll_index);
        // _moving = true;
        // $scroll.animate({"left": _scroll_left}, 100);
        // setTimeout(function() {
        // $desc.removeClass('s1 s2 s3 s4').addClass('s' + (_scroll_index_active + 1)).find('p').removeClass('show').filter('.p' + (_scroll_index + _scroll_index_active + 1)).addClass('show');
        // $scroll.find('.intro-item').removeClass('active').eq(_scroll_index + _scroll_index_active).addClass('active');
        // _moving = false;
        // }, 110);
      } else {
        $desc.removeClass('s1 s2 s3 s4').addClass('s' + (_scroll_index_active + 1)).find('p').removeClass('show').filter('.p' + (_scroll_index + _scroll_index_active + 1)).addClass('show');
        $scroll.find('.intro-item').removeClass('active').eq(_scroll_index + _scroll_index_active).addClass('active');
      }
    }
  }

  loadData();
  function loadData() {
    showLoad();
    goAPI({
      url: _api.index,
      data: {
        token: user ? user.token : ''
      },
      success: function success(data) {
        data = data.data;
        if ($.isArray(data.carList) && data.carList.length > 0) {
          car_trans = 0;
          btn_join = { num: data.carList.length };
          if (data.carList.length === 1) {
            btn_join['car'] = data.carList[0];
          } else {
            var all_clear = true;
            for (var i = 0, j = data.carList.length; i < j; i++) {
              if (data.carList[i].status !== 13 && data.carList[i].status !== 20) {
                all_clear = false;
                break;
              }
            }
            btn_join['all_clear'] = all_clear;
          }
        } else {
          btn_join = {
            num: 0
          };
        }
        data['btn_join'] = btn_join;
        $('.container').html(tpl.render(data));
        //top_height = Math.ceil($('#chev').offset().top);
        // var banners = [];
        // for (var i = 0, j = data.imgList.length; i < j; i++) {
        // banners.push({
        // src: data.imgList[i].linkImg,
        // href: data.imgList[i].linkTitle
        // });
        // }
        // var previewImg = preview({
        // type: 'loop',
        // fixed: false,
        // auto: true,
        // dely: 3000,
        // height: '3.2rem',
        // point: true,
        // container: $('#banner-box'),
        // imgs: banners
        // });
        // previewImg.show();

        if (!loadStorage('index_welcome', true)) {
          saveStorage('index_welcome', 1, true);
          loginCoupinEvent(function () {
            showConfirm({
              class: 'tl',
              str: '我只想问你们一个问题！请你们认真回答我。如果遇到一个不要房子、不要车、不要存款、不要钻戒，不要你请吃饭、看电影、买东西、不会骚扰你！只想在你的车子擦刮的时候帮你免费修车，还上门接车，让你省下一年1000多的保险费。这样的理赔管家，让你给9元你觉得过分吗？',
              btn_yes: {
                str: '不过分'
              },
              btn_no: {
                str: '过分'
              }
            });
          });
        }

        if (btn_join.num > 0) {
          transCar($('#car-box .car-item').length);
        }

        if (data.randomCar && !loadStorage('index_newer')) {
          var item = data.randomCar.split('_');
          newer.push({
            desc: item[0],
            avatar: item[1] ? item[1] : '/hfive/img/default_avatar_1.png'
          });
          showNewer();
          saveStorage('index_newer', 1);
        }

        $('.intro-list2').on('click', '.intro-btn', function () {
          $(this).closest('.intro-item').toggleClass('active');
        });
        // $('.intro-fbox').on('click', '.intro-item', function(){
        // var $this = $(this);
        // if (!$this.hasClass('active')) {
        // var $fbox = $this.closest('.intro-fbox');
        // var $desc = $fbox.siblings('.intro-desc');
        // var index = $fbox.data('index');
        // var item_index = $this.index();
        // data_intros[index].index = item_index;
        // $this.addClass('active').siblings().removeClass('active');
        // $desc.removeClass('s1 s2 s3 s4').addClass('s' + (item_index + data_intros[index].offset + 1)).find('p').removeClass('show').filter('.p' + (item_index + 1)).addClass('show');
        // }
        // })
        // $('.intro-fbox').on('click', '.btn-scroll', function(){
        // var $arr = $(this);
        // var $fbox = $arr.parent();
        // var $list = $fbox.find('.intro-list');
        // var $desc = $fbox.siblings('.intro-desc');
        // var index = $fbox.data('index');
        // var dis = 1.6;
        // var max = 4;
        // if ($arr.hasClass('scroll-left')) {
        // var offset_temp = data_intros[index].offset += 1;
        // var index_temp = data_intros[index].index -= 1;
        // data_intros[index].offset = offset_temp > 0 ? 0 : offset_temp;
        // data_intros[index].index = index_temp < 0 ? 0 : index_temp;
        // } else if ($arr.hasClass('scroll-right')) {
        // var offset_temp = data_intros[index].offset -= 1;
        // var index_temp = data_intros[index].index += 1;
        // data_intros[index].offset = offset_temp < (max - data_intros[index].length) ? (max - data_intros[index].length) : offset_temp;
        // data_intros[index].index = index_temp > (data_intros[index].length - 1) ? (data_intros[index].length - 1) : index_temp;
        // }
        // $list.css({'webkit-transform':'translateX(' + data_intros[index].offset * dis + 'rem) translateZ(0)', 'transform':'translateX(' + data_intros[index].offset * dis + 'rem) translateZ(0)'});
        // $list.find('.intro-item').eq(data_intros[index].index).addClass('active').siblings().removeClass('active');
        // $desc.removeClass('s1 s2 s3 s4').addClass('s' + (data_intros[index].index + data_intros[index].offset + 1)).find('p').removeClass('show').filter('.p' + (data_intros[index].index + 1)).addClass('show');
        // })

        initTabBox(true);
        getNewer();
        getNewMsg(user.token);
        scrollPanel();
        if (_config.is_wx) {
          if (!_config.wx_config) {
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
          checkFollow({
            success: function success(data) {
              if (data.data == 1) {
                $('.btn-join').off('click');
              } else {
                $('.btn-join').off('click').on('click', function (e) {
                  e.preventDefault();
                  showConfirm({
                    str: '扫码认识我',
                    cover_close: true,
                    node: '<img src="/hfive/img/chevhuzhu_qr_s_test.jpg" style="width:3rem;height:3rem;">',
                    btn_close: true,
                    btn_yes: {
                      str: null
                    }
                  });
                });
                if (from_id) {
                  showConfirm({
                    str: '关注公众号<br>我们一起领取500元额度',
                    cover_close: true,
                    node: '<img src="/hfive/img/chevhuzhu_qr_s_test.jpg" style="width:3rem;height:3rem;">',
                    btn_close: true,
                    btn_yes: {
                      str: null
                    }
                  });
                }
              }
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
      }
    });
  }

  function initTabBox(pull) {
    tabbox({
      container: '.tabbox',
      height: '100%',
      pull: pull,
      exception_class: '.no-scroll',
      event_pullMove: function event_pullMove(dis, index) {
        if (dis >= 40) {
          $('.tabbox-refresh .txt').text('松开刷新');
          $('.tabbox-refresh .arr3').addClass('uturn');
        } else {
          $('.tabbox-refresh .txt').text('下拉刷新');
          $('.tabbox-refresh .arr3').removeClass('uturn');
        }
      },
      event_pullEnd: function event_pullEnd(dis, index) {
        if (dis >= 40) {
          $('.tabbox-refresh .txt').text('下拉刷新');
          $('.tabbox-refresh .arr3').removeClass('uturn');
          loadData();
        }
      },
      event_scroll: function event_scroll(index, dis, dir) {
        //if (dir === -1 && dis >= top_height) {
        if (dir === -1) {
          $('.navbar-bottom').addClass('close');
          $('#btn-join').removeClass('close');
          $('#btn-manager').removeClass('close');
          //} else if (dir === 1 && dis < top_height) {
        } else if (dir === 1) {
          $('.navbar-bottom').removeClass('close');
          $('#btn-join').addClass('close');
          $('#btn-manager').addClass('close');
        }
      }
    });
  }
})();