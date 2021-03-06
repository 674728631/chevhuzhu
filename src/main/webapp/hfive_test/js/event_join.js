'use strict';

(function () {
  var loading = false;
  var sid = getUrlPar('sid'); // 商家id
  var cid = getUrlPar('cid'); // 优惠券id
  var qrcode = '/hfive/img/chevhuzhu_qr_s_test.jpg';
  var qrcode_shop = qrcode;
  var shop = {
    '15': {
      name: '锋锐汽修',
      logo: '/hfive/img/event_join_rfqx.png',
      width: '1rem'
    },
    '54': {
      name: '红泰汽贸',
      logo: '/hfive/img/event_join_htqm.png',
      width: '1rem'
    },
    '57': {
      name: '正能量汽修',
      logo: '/hfive/img/event_join_znlqx.png',
      width: '1rem'
    },
    '77': {
      name: '三合源汽修',
      logo: '/hfive/img/event_join_shyqx.png',
      width: '1rem'
    },
    '131': {
      name: '成都车友会',
      logo: '/hfive/img/event_join_cdcyh.png',
      width: '1.8rem',
      left: '.3rem',
      top: '1.4rem'
    },
    '132': {
      name: '汽车保险部',
      logo: '/hfive/img/event_join_qcbxb.png',
      width: '1rem'
    },
    '135': {
      name: '宏金保',
      logo: '/hfive/img/event_join_hjb.png',
      width: '1rem'
    },
    '136': {
      name: '爱车购',
      logo: '/hfive/img/event_join_acg.png',
      width: '1rem'
    },
    '137': {
      name: '车险服务部',
      logo: '/hfive/img/event_join_cxfwb.png',
      width: '1rem'
    },
    '138': {
      name: '020LED大灯',
      logo: '/hfive/img/event_join_020led.png',
      width: '1rem',
      top: '1.4rem'
    },
    '139': {
      name: '万车达',
      logo: '/hfive/img/event_join_wcd.png',
      width: '1rem'
    },
    '140': {
      name: '蜀车网',
      logo: '/hfive/img/event_join_scw.png',
      width: '2rem',
      left: '0',
      top: '1.45rem'
    },
    '141': {
      name: 'FM92.5私家车广播',
      logo: '/hfive/img/event_join_pm925.png',
      width: '1.42rem',
      left: '.6rem',
      top: '1.4rem'
    },
    '142': {
      name: '丰融保险部',
      logo: '/hfive/img/event_join_frbxb.png',
      width: '1rem'
    },
    '143': {
      name: '四川希望保险代理有限公司',
      logo: '/hfive/img/event_join_scxwbxdl.png',
      width: '1.6rem',
      top: '1.5rem',
      left: '.5rem'
    },
    '146': {
      name: '四川鑫茂益汽车',
      logo: '/hfive/img/event_join_scxmyqc.png',
      width: '1rem'
    },
    '151': {
      name: '四川欣立保险公估有限公司',
      logo: '/hfive/img/event_join_scxl.png',
      width: '1rem',
      top: '1.4rem'
    },
    '153': {
      name: '佳兴保险',
      logo: '/hfive/img/event_join_jxbx.png',
      width: '1rem',
      top: '1.3rem'
    },
    '154': {
      name: '车V互助福利一群',
      logo: '/hfive/img/event_join_cvhzflyq.png',
      width: '1rem'
    },
    '155': {
      name: '匠车坊花郡店',
      logo: '/hfive/img/event_join_jcfhjd.png',
      width: '1rem'
    },
    '156': {
      name: '匠车坊',
      logo: '/hfive/img/event_join_jcfhjd.png',
      width: '1rem'
    },
    '157': {
      name: '车管家',
      logo: '/hfive/img/event_join_cgj.png',
      width: '1rem'
    },
    '159': {
      name: '阳光保险集团',
      logo: '/hfive/img/event_join_ygbx.png',
      width: '1rem'
    },
    '161': {
      name: '太平洋保险',
      logo: '/hfive/img/event_join_tpybx.jpg',
      width: '1.2rem',
      left: '.8rem',
      top: '1.3rem',
      allow: true
    },
    '162': {
      name: '中泰保险',
      logo: '/hfive/img/event_join_ztbx.jpg',
      width: '1rem'
    },
    '163': {
      name: 'e泊车',
      logo: '/hfive/img/event_join_ybc.png',
      width: '.94rem',
      allow: true
    },
    '166': {
      name: '永丰加油站',
      logo: '/hfive/img/event_join_yfjyz.png',
      width: '.9rem'
    },
    '167': {
      name: '成都周边游',
      logo: '/hfive/img/event_join_cdzby.png',
      width: '1rem'
    },
    '169': {
      name: '四川交通广播',
      logo: '/hfive/img/event_join_scjtgb.png?1',
      width: '1.6rem',
      left: '.4rem',
      top: '1.5rem'
    },
    '171': {
      name: '车险管家',
      logo: '/hfive/img/event_join_cgj.png',
      width: '1rem'
    },
    '173': {
      name: '四川省机动车年检平台',
      logo: '/hfive/img/event_join_scsjdcnjpt.jpg',
      width: '1rem'
    },
    '174': {
      name: '熊猫驾信',
      logo: '/hfive/img/event_join_xmjx.jpg',
      width: '1.8rem',
      top: '1.4rem',
      left: '.2rem'
    },
    '100001': {
      name: '车妈妈',
      logo: '/hfive/img/event_join_cmm.jpg',
      width: '.9rem'
    }
  };
  var shop_test = $.extend(shop, {
    '177': {
      name: '测试渠道'
    },
    '178': {
      name: 'e泊车测试',
      logo: '/hfive/img/event_join_ybc.png',
      width: '.94rem',
      allow: true
    }
  });

  if (!sid || !cid || !shop[sid]) {
    showConfirm({
      str: '参数错误'
    });
    return;
  }

  setTitle(shop_test[sid].name + '邀你免费领取1000元小擦刮维修补贴金');

  if (_config.is_wx) {
    //if (!_config.is_wx) {
    wxOauth(function () {
      loadShop(function () {
        //$('#container').html(juicer($('#main-tpl').html(), {sid: sid, data: shop_test[sid]}));
        configWXJSSDK(_config.wx_share, {
          success: function success() {
            shareWX({
              title: shop_test[sid].name + '邀请你免费领取1000元小擦刮维修补贴金',
              desc: '时间过了就木有了哈',
              imgUrl: 'http://test.chevhuzhu.com/hfive/img/hongbao.jpg',
              link: window.location.href.replace(/#.*$/, '')
            });
          }
        });
        showConfirm({
          str: '<font class="txt-red">恭喜您！获得1000元擦刮维修补贴！</font><br>请长按识别二维码关注<br>完成最后一步注册即可认领成功',
          node: '<img src="' + qrcode_shop + '" style="width:3rem;height:3rem;">',
          btn_yes: {
            str: null
          }
        });
        // $('#btn-confirm').on('click', function(){
        // var phone = $('#phone').val().trim();
        // if (phone.length === 0) {
        // showAlert('请输入手机号码');
        // return;
        // }
        // if (!validatePhone(phone, true)) {
        // showAlert('手机号码有误');
        // return;
        // }
        // showLoad();
        // loading = true;
        // goAPI({
        // url: _api.event,
        // data: {
        // shopId: sid,
        // couponNo: cid,
        // mobileNumber: phone
        // },
        // success: function(data) {
        // showConfirm({
        // str: '<font class="txt-red">恭喜您！获得1000元擦刮维修补贴！</font><br>请长按识别二维码关注<br>完成最后一步注册即可认领成功',
        // cover_close: true,
        // node: '<img src="' + qrcode_shop + '" style="width:3rem;height:3rem;">',
        // btn_close: true,
        // btn_yes: {
        // str: null
        // }
        // });
        // },
        // error: function(data,code) {
        // if (code == 501) {
        // showConfirm({
        // str: '<font class="txt-red">您已领取过啦~</font><br>识别二维码关注 get车险技能',
        // cover_close: true,
        // node: '<img src="' + qrcode + '" style="width:3rem;height:3rem;">',
        // btn_close: true,
        // btn_yes: {
        // str: null
        // }
        // });
        // } else if (code == 502) {
        // showConfirm({
        // str: '亲，您已加入了保障计划，请下次再来~'
        // });
        // } else {
        // showConfirm({
        // str: data
        // });
        // }
        // },
        // complete: function() {
        // hideLoad();
        // loading = false;
        // }
        // })
        // })
      });
    });
  } else {
    // if (shop_test[sid].allow) {
    // $('#container').html(juicer($('#main-tpl').html(), {sid: sid, data: shop_test[sid]}));
    // } else {
    showConfirm({
      str: '请在微信客户端中打开此页面',
      btn_yes: {
        str: null
      }
    });
    // }
  }

  function loadShop(cbk) {
    goAPI({
      url: _api.shop_detail_simple,
      data: {
        shopId: sid
      },
      success: function success(data) {
        data = data.data;
        if (data) {
          qrcode_shop = data.qrcode;
        }
        if ($.isFunction(cbk)) {
          cbk();
        }
      },
      error: function error(data) {
        hideLoad();
        showConfirm({
          str: data,
          btn_yes: {
            str: '重新加载',
            event_click: function event_click() {
              loadShop(cbk);
            }
          }
        });
      }
    });
  }
})();