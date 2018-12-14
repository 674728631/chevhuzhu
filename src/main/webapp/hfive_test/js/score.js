'use strict';

(function () {
  var loading = false;
  var oid = getUrlPar('id');
  var user = checkLogin(true);
  var data_order;
  var score = 0;

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

  // 提交评价
  function submitScore(dt) {
    if (loading) return;
    showLoad();
    loading = true;
    goAPI({
      url: _api.submit_score,
      data: dt,
      success: function success(data) {
        showConfirm({
          str: '您的评价已经提交成功，感谢您对车V互助的支持！车V互助因您而更加强大',
          class: 'shadow-blue',
          cover_background: 'rgba(255,255,255,0.5)',
          btn_yes: {
            str: null
          }
        });
        setTimeout(function () {
          window.history.go(-1);
        }, 2000);
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

  function loadData() {
    showLoad();
    goAPI({
      url: _api.order_detail,
      data: {
        token: user.token,
        eventNo: oid
      },
      success: function success(data) {
        data_order = data.data;
        if (data_order.statusEvent != 71) {
          showConfirm({
            str: '该订单无法进行评价',
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
        $('.container').html(juicer($('#main-tpl').html(), data_order));

        $('#star-box').on('click', '.i-star', function () {
          var $this = $(this).removeClass('empty');
          var stars = $this.siblings('.i-star');
          score = $this.index();
          $('#star').text(score);
          stars.each(function (i) {
            if (i < score - 1) {
              $(this).removeClass('empty');
            } else {
              $(this).addClass('empty');
            }
          });
        });

        $('#tag-box').on('click', 'a', function () {
          $(this).toggleClass('active');
        });

        $('#desc').on('input', function () {
          $('#desc-status').text($('#desc').val().length + '/150');
        });

        $('#btn-confirm').on('click', function () {
          if (score < 1) {
            showAlert('请先评分');
            return;
          }
          var tags = [];
          $('#tag-box a.active').each(function () {
            tags.push($(this).text());
          });
          submitScore({
            token: user.token,
            eventNo: data_order.eventNo,
            score: score,
            content: $('#desc').val().trim(),
            labelContent: tags.join('_')
          });
        });

        if (_config.is_wx && !_config.wx_config) {
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