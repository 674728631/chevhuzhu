'use strict';

(function () {
  showConfirm({
    str: '非常抱歉，该页面已停止访问',
    btn_yes: {
      str: '回到首页',
      href: '/hfive/view/index.html'
    }
  });
  var page_data = {
    '1': {
      'loading': false,
      'inited': false,
      'nomore': false,
      'page': 1
    },
    '2': {
      'loading': false,
      'inited': false,
      'nomore': false,
      'page': 1
    },
    '3': {
      'loading': false,
      'inited': false,
      'nomore': false,
      'page': 1
    }
  };
  var list_tpl = juicer($('#list-tpl').html());
  var view_type = '2'; //page_data[getUrlPar('type')] ? getUrlPar('type') : '1';
  var loading = false;
  var tbox;
  var geolocation;
  var geocoder;
  var map;
  var position;

  var juicer_distance = function juicer_distance(dis) {
    if (dis < 1000) {
      return dis + 'm';
    } else {
      return Math.round(dis / 100) / 10 + 'km';
    }
  };
  juicer.register('build_distance', juicer_distance);

  //setMap();
  //loadData();
  function loadData() {
    $('#container').html(juicer($('#main-tpl').html(), {}));
    $('#menu-box').on('click', 'a', function () {
      var $this = $(this);
      if (!$this.hasClass('active')) {
        $this.addClass('active').siblings().removeClass('active');
        tbox.switchTo($this.index());
      }
    });
    tbox = tabbox({
      container: '.tabbox',
      height: '100%',
      pull: true,
      event_pullMove: function event_pullMove(dis, index) {
        if (dis >= 40) {
          $('.tabbox-refresh').eq(index).find('.txt').text('松开刷新');
          $('.tabbox-refresh').eq(index).find('.arr3').addClass('uturn');
        } else {
          $('.tabbox-refresh').eq(index).find('.txt').text('下拉刷新');
          $('.tabbox-refresh').eq(index).find('.arr3').removeClass('uturn');
        }
      },
      event_pullEnd: function event_pullEnd(dis, index) {
        if (dis >= 40) {
          $('.tabbox-refresh').eq(index).find('.txt').text('下拉刷新');
          $('.tabbox-refresh').eq(index).find('.arr3').removeClass('uturn');
          getList(true);
        }
      },
      event_scrollBottom: function event_scrollBottom(index, dis) {
        if (page_data[view_type].loading || page_data[view_type].nomore) return;
        getList();
      },
      event_switch: function event_switch(index, lastIndex) {
        //var $item = $('#menu-box a').eq(index);
        //$item.addClass('active').siblings().removeClass('active');
        //view_type = $item.data('value');
        if (!page_data[view_type].inited) {
          getList(true);
        }
      }
    });
    //tbox.switchTo($('#menu-box a[data-value="' + view_type + '"]').index(), {animation:false, event:true});
    tbox.switchTo(0, { animation: false, event: true });
  }

  // 设置高德地图
  function setMap() {
    map = new AMap.Map('map-main');
    map.plugin(['AMap.Geolocation'], function () {
      geolocation = new AMap.Geolocation({
        enableHighAccuracy: true, //是否使用高精度定位，默认:true
        timeout: 6000, //超过6秒后停止定位，默认：无穷大
        maximumAge: 0, //定位结果缓存0毫秒，默认：0
        convert: true, //自动偏移坐标，偏移后的坐标为高德坐标，默认：true
        showButton: false, //显示定位按钮，默认：true
        buttonPosition: 'LB', //定位按钮停靠位置，默认：'LB'，左下角
        buttonOffset: new AMap.Pixel(10, 20), //定位按钮与设置的停靠位置的偏移量，默认：Pixel(10, 20)
        showMarker: false, //定位成功后在定位到的位置显示点标记，默认：true
        showCircle: false, //定位成功后用圆圈表示定位精度范围，默认：true
        panToLocation: false, //定位成功后将定位到的位置作为地图中心点，默认：true
        zoomToAccuracy: false //定位成功后调整地图视野范围使定位位置及精度范围视野内可见，默认：false
      });
      map.addControl(geolocation);
    });
  }

  // 获取商家列表前先判断是否已定位
  function getList(emptyAll) {
    if (page_data[view_type].loading) return;
    showLoad();
    page_data[view_type].loading = true;
    if (emptyAll) {
      page_data[view_type].page = 1;
      page_data[view_type].nomore = false;
    }
    loadList(emptyAll);
    // if (position && (position.time - Date().now < 60000)) {
    // loadList(emptyAll);
    // } else if (geolocation) {
    // geolocation.getCurrentPosition(function(status, result){
    // if (status === 'complete') {
    // position = {
    // lat: result.position.lat,
    // lng: result.position.lng,
    // time: Date().now
    // };
    // }
    // loadList(emptyAll);
    // });
    // } else {
    // loadList(emptyAll);
    // }
  }

  // 获取商家列表
  function loadList(emptyAll) {
    goAPI({
      url: _api.shop_list,
      data: {
        sortFlag: view_type,
        longitude: position ? position.lng * 1000000 : 'null',
        latitude: position ? position.lat * 1000000 : 'null',
        pageNo: page_data[view_type].page
      },
      success: function success(data) {
        data = data.data;
        page_data[view_type].inited = true;
        if (emptyAll) {
          tbox.activeTab().find('.list-box').empty();
        }
        if (data.total == 0) {
          page_data[view_type].nomore = true;
          tbox.activeTab().addClass('full');
          tbox.activeTab().find('.null-info').removeClass('hide');
        } else {
          page_data[view_type].page += 1;
          tbox.activeTab().removeClass('full');
          tbox.activeTab().find('.null-info').addClass('hide');
          tbox.activeTab().find('.list-box').append(list_tpl.render(data));
          if (!data.hasNextPage) {
            page_data[view_type].nomore = true;
          }
        }
        // if (_config.is_wx && !_config.wx_config) {
        // configWXJSSDK(_config.wx_share, {
        // success: function() {
        // shareWX({
        // title: _share_title,
        // desc: _share_desc,
        // link: _share_link
        // })
        // }
        // })
        // }
      },
      error: function error(msg) {
        if (emptyAll) {
          showConfirm({
            str: msg,
            btn_yes: {
              str: '重新加载',
              event_click: function event_click() {
                getList(emptyAll);
              }
            }
          });
        } else {
          showConfirm({
            str: msg
          });
        }
      },
      complete: function complete() {
        hideLoad();
        page_data[view_type].loading = false;
      }
    });
  }
})();