'use strict';

(function () {
  var loading = false;
  var cid = getUrlPar('id');
  var user = checkLogin(true);

  if (user) {
    loadData();
  }

  function loadData() {
    showLoad();
    $.when(loadPublicity(), loadUser()).done(function (publicity, user) {
      hideLoad();
      var carlist = user.carList;
      var car = null;
      if (cid && $.isArray(carlist) && carlist.length > 0) {
        for (var i = 0, j = carlist.length; i < j; i++) {
          if (cid == carlist[i].id) {
            car = carlist[i];
            break;
          }
        }
      }
      $('#container').html(juicer($('#main-tpl').html(), { publicity: publicity, user: user, car: car })).removeClass('hide');
    }).fail(function (msg) {
      hideLoad();
      showConfirm({
        str: msg,
        btn_yes: {
          str: '重新加载',
          event_click: loadData
        }
      });
    });
  }

  function loadPublicity() {
    var dtd = $.Deferred();
    goAPI({
      url: _api.publicity,
      success: function success(data) {
        dtd.resolve(data.data);
      },
      error: function error(msg) {
        dtd.reject(msg);
      }
    });
    return dtd.promise();
  }

  function loadUser() {
    var dtd = $.Deferred();
    goAPI({
      url: _api.user,
      data: {
        token: user.token
      },
      success: function success(data) {
        dtd.resolve(data.data);
      },
      error: function error(msg) {
        dtd.reject(msg);
      }
    });
    return dtd.promise();
  }
})();