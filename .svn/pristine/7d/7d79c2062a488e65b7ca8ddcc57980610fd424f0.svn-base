'use strict';

(function () {
  loadData();
  function loadData() {
    showLoad();
    goAPI({
      url: _api.index,
      success: function success(data) {
        $('.container').html(juicer($('#main-tpl').html(), data.data));
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