'use strict';

(function () {
	var $lottery;
	var $units;
	var loading = false;
	var urls = {
		'001': '/hfive/view/event_join.html?sid=163&cid=201809280939198B3p3P',
		'002': '/hfive/view/event_join.html?sid=167&cid=20181023164903l10cRX',
		'003': '/hfive/view/event_join.html?sid=100001&cid=2018103010150664WQD6'
	};
	var event_id = getUrlPar('id') || '001';
	var prize_url = urls[event_id] ? urls[event_id] : '/hfive/view/index.html';

	if (_config.is_wx) {
		// configWXJSSDK(_config.wx_share, {
		// success: function() {
		// shareWX({
		// title: shop_test[sid].name + '邀请你免费领取1000元小擦刮维修补贴金',
		// desc: '时间过了就木有了哈',
		// imgUrl: 'http://test.chevhuzhu.com/hfive/img/share_event_cdcyh.jpg',
		// link: window.location.href.replace(/#.*$/, '')
		// })
		// }
		// })
		wxOauth();
	} else {
		showConfirm({
			str: '请在微信客户端中打开此页面',
			btn_yes: {
				str: null
			}
		});
		return;
	}

	var lottery = {
		index: -1, //当前转动到哪个位置，起点位置
		count: 8, //总共有多少个位置
		timer: 0, //setTimeout的ID，用clearTimeout清除
		speed: 20, //初始转动速度
		times: 0, //转动次数
		cycle: 15, //转动基本次数：即至少需要转动多少次再进入抽奖环节
		prize: -1, //中奖位置
		init: function init(id) {
			if ($('#' + id).find('.lottery-unit').length > 0) {
				$lottery = $('#' + id);
				$units = $lottery.find('.lottery-unit');
				this.obj = $lottery;
				this.count = $units.length;
				$lottery.find('.lottery-unit.lottery-unit-' + this.index).addClass('active');
			};
		},
		roll: function roll() {
			var index = this.index;
			var count = this.count;
			var lottery = this.obj;
			$(lottery).find('.lottery-unit.lottery-unit-' + index).removeClass('active');
			index += 1;
			if (index > count - 1) {
				index = 0;
			};
			$(lottery).find('.lottery-unit.lottery-unit-' + index).addClass('active');
			this.index = index;
			return false;
		},
		stop: function stop(index) {
			this.prize = index;
			return false;
		}
	};

	function rollStart() {
		lottery.times += 1;
		lottery.roll();
		if (lottery.times > lottery.cycle + 10 && lottery.prize == lottery.index) {
			setTimeout(function () {
				$('#result').removeClass('hide').find('a').attr('href', prize_url);
				setTimeout(function () {
					$('#result .main').addClass('active');
				}, 10);
			}, lottery.speed);
			lottery.speed = 20;
			lottery.prize = -1;
			lottery.times = 0;
		} else {
			if (lottery.times < lottery.cycle) {
				lottery.speed -= 10;
			} else {
				if (lottery.times > lottery.cycle + 10 && (lottery.prize == 0 && lottery.index == 7 || lottery.prize == lottery.index + 1)) {
					lottery.speed += 20;
				} else {
					lottery.speed += 40;
				}
			}
			if (lottery.speed < 90) {
				lottery.speed = 90;
			};
			lottery.timer = setTimeout(rollStart, lottery.speed);
		}
		return false;
	}

	function getLottery() {
		// rollStart();
		// lottery.stop(2);
		// return;
		if (loading) return;
		loading = true;
		goAPI({
			url: _api.lottery,
			data: {
				drawNum: event_id
			},
			success: function success(data) {
				rollStart();
				lottery.stop(2);
			},
			error: function error(data, code) {
				showConfirm({
					str: data
				});
			},
			complete: function complete() {
				loading = false;
			}
		});
	}

	lottery.init('lottery');

	$('.draw-btn').on('click', getLottery);
})();