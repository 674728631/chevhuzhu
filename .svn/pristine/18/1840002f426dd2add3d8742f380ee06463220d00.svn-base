function vkeyboard(data) {
  var default_options = {
    type: '',  // 键盘类型，默认为数字键盘，"keyboard"为英文加数字键盘
    init: '',  // 初始值，默认为空
    input: null,  // 输入框，默认为空，可提供一个dom元素作为输入框的容器
    max_length: -1,  // 输入框可输入的最大长度，默认无限制
    exception_class: '',  // 过滤触发关闭键盘的元素class
    enable_point: false,  // 是否允许输入小数点
    enable_dash: false,  // 是否允许输入横杠
    event_init: null,  // 初始化回调函数
    event_show: null,  // 显示回调函数
    event_hide: null,  // 隐藏回调函数
    event_input: null  // 输入回调函数
  };
  var options = $.extend({}, default_options, data);
  var vkeyboard_show = false;
  var vkeyboard_input = null;
  var vkeyboard_cursor_index = -1;
  var vkeyboard_value = '';
  var vkeyboard;
  if (options.type === 'keyboard') {
    vkeyboard = $('<div id="vkeyboard" class="vkeyboard-node vkeyboard-keyboard"><div id="vkeyboard-main"><div class="vkeyboard-key-line fbox-cc"><span class="vkeyboard-key" data-value="1">1</span><span class="vkeyboard-key" data-value="2">2</span><span class="vkeyboard-key" data-value="3">3</span><span class="vkeyboard-key" data-value="4">4</span><span class="vkeyboard-key" data-value="5">5</span><span class="vkeyboard-key" data-value="6">6</span><span class="vkeyboard-key" data-value="7">7</span><span class="vkeyboard-key" data-value="8">8</span><span class="vkeyboard-key" data-value="9">9</span><span class="vkeyboard-key" data-value="0">0</span></div><div class="vkeyboard-key-line fbox-cc"><span class="vkeyboard-key" data-value="Q">Q</span><span class="vkeyboard-key" data-value="W">W</span><span class="vkeyboard-key" data-value="E">E</span><span class="vkeyboard-key" data-value="R">R</span><span class="vkeyboard-key" data-value="T">T</span><span class="vkeyboard-key" data-value="Y">Y</span><span class="vkeyboard-key" data-value="U">U</span><span class="vkeyboard-key" data-value="I">I</span><span class="vkeyboard-key" data-value="O">O</span><span class="vkeyboard-key" data-value="P">P</span></div><div class="vkeyboard-key-line fbox-cc"><span class="vkeyboard-key" data-value="A">A</span><span class="vkeyboard-key" data-value="S">S</span><span class="vkeyboard-key" data-value="D">D</span><span class="vkeyboard-key" data-value="F">F</span><span class="vkeyboard-key" data-value="G">G</span><span class="vkeyboard-key" data-value="H">H</span><span class="vkeyboard-key" data-value="J">J</span><span class="vkeyboard-key" data-value="K">K</span><span class="vkeyboard-key" data-value="L">L</span></div><div class="vkeyboard-key-line fbox-cc"><span class="vkeyboard-key" data-value="Z">Z</span><span class="vkeyboard-key" data-value="X">X</span><span class="vkeyboard-key" data-value="C">C</span><span class="vkeyboard-key" data-value="V">V</span><span class="vkeyboard-key" data-value="B">B</span><span class="vkeyboard-key" data-value="N">N</span><span class="vkeyboard-key" data-value="M">M</span><p id="vkeyboard-del" class="vkeyboard-key" data-value="delete"><span>Ｘ</span></p></div></div></div>');
  } else {
    vkeyboard = $('<div id="vkeyboard" class="vkeyboard-node"><table id="vkeyboard-main"><tr><td class="vkeyboard-key" data-value="1">1</td><td class="vkeyboard-key" data-value="2">2</td><td class="vkeyboard-key" data-value="3">3</td><td class="vkeyboard-key" id="vkeyboard-del" data-value="delete" rowspan="2"><span>Ｘ</span></td></tr><tr><td class="vkeyboard-key" data-value="4">4</td><td class="vkeyboard-key" data-value="5">5</td><td class="vkeyboard-key" data-value="6">6</td></tr><tr><td class="vkeyboard-key" data-value="7">7</td><td class="vkeyboard-key" data-value="8">8</td><td class="vkeyboard-key" data-value="9">9</td><td class="vkeyboard-key" id="vkeyboard-confirm" data-value="confirm" rowspan="2">确定</td></tr><tr><td class="vkeyboard-key" id="vkeyboard-dash"></td><td class="vkeyboard-key" data-value="0">0</td><td class="vkeyboard-key" id="vkeyboard-point"></td></tr></table></div>');
  }
  
  initKeyboard();
  
  $('body').append(vkeyboard);
  
  vkeyboard.on('click', '.vkeyboard-key', function(){
    var $item = $(this);
    if (!$item.hasClass('disabled')) {
      if ($item.data('value') === 'delete' && vkeyboard_value.length > 0 && vkeyboard_cursor_index >= 0) {
        var arr = vkeyboard_value.split('');
        arr.splice(vkeyboard_cursor_index, 1);
        vkeyboard_value = arr.join('');
        vkeyboard_cursor_index--;
        arr = null;
      } else if ($item.data('value') !== 'delete' && $item.data('value') !== 'confirm' && (options.max_length < 0 || options.max_length > 0 && vkeyboard_value.length < options.max_length)) {
        var arr = vkeyboard_value.split('');
        arr.insertAt(vkeyboard_cursor_index + 1, $item.text());
        vkeyboard_value = arr.join('');
        vkeyboard_cursor_index++;
        arr = null;
      }
      updateInputValue();
      if (options.event_input) {
        options.event_input($item.data('value'), $item.text());
      }
    }
  })
  
  if (vkeyboard_input) {
    vkeyboard_input
    // .on('touchstart mousedown', function(e){
      // var spans = vkeyboard_input.find('span');
      // if (spans.length > 0) {
        // var $this = $(this);
        // var $tar = $(e.target);
        // var $inner = vkeyboard_input.find('.vkeyboard-input-inner');
        // var x = e.touches ? e.touches[0].pageX - $this.offset().left : e.pageX - $this.offset().left;
        // if (e.target.nodeName.toUpperCase() === 'SPAN') {
          // var index = $tar.index();
          // if (index === 0) {
            // spans.removeClass('first-active active').eq(0).addClass('first-active');
          // } else {
            // spans.removeClass('first-active active').eq(index - 1).addClass('active');
          // }
          // vkeyboard_cursor_index = index - 1;
        // } else if (x > $inner.width()) {
          // spans.removeClass('first-active active').last().addClass('active');
          // vkeyboard_cursor_index = spans.length - 1;
        // } else {
          // spans.removeClass('first-active active').first().addClass('first-active');
          // vkeyboard_cursor_index = -1;
        // }
      // }
    // })
    .on('touchstart mousedown touchmove', function(e){
      var spans = vkeyboard_input.find('span');
      if (spans.length > 0) {
        var $this = $(this);
        var $inner = vkeyboard_input.find('.vkeyboard-input-inner');
        var x = e.touches ? e.touches[0].pageX - $this.offset().left : e.pageX - $this.offset().left;
        var left = vkeyboard_input.offset().left;
        if (x <= spans.first().offset().left - left) {
          spans.removeClass('first-active active').first().addClass('first-active');
          vkeyboard_cursor_index = -1;
        } else if (x >= spans.last().offset().left - left + spans.last().width()) {
          spans.removeClass('first-active active').last().addClass('active');
          vkeyboard_cursor_index = spans.length - 1;
        } else {
          var is_end = true;
          spans.each(function(index, span){
            if (x <= $(span).offset().left - left) {
              if (index === 0) {
                spans.removeClass('first-active active').eq(0).addClass('first-active');
              } else {
                spans.removeClass('first-active active').eq(index - 1).addClass('active');
              }
              vkeyboard_cursor_index = index - 1;
              is_end = false;
              return false;
            }
          })
          if (is_end) {
            spans.removeClass('first-active active').last().addClass('active');
            vkeyboard_cursor_index = spans.length - 1;
          }
        }
      }
    })
  }
  
  $('body').on('click', function(e){
    var tar = $(e.target);
    if (vkeyboard_show && !tar.hasClass(options.exception_class) && tar.closest('.vkeyboard-input').length === 0 && tar.closest('#vkeyboard').length === 0) {
      result.hide();
      if (vkeyboard_input) {
        vkeyboard_input.removeClass('active');
      }
    } else if (vkeyboard_input) {
      if (tar.closest('.vkeyboard-input').length > 0) {
        vkeyboard_input.addClass('active');
        result.show();
      } else if (tar.closest('#vkeyboard').length > 0) {
        vkeyboard_input.addClass('active');
      }
    }
  })
  
  // 更新输入框
  function updateInputValue() {
    if (!vkeyboard_input) {
      return;
    }
    if (vkeyboard_value.length === 0) {
      vkeyboard_input.addClass('vkeyboard-default').empty();
    } else {
      var str = '<div class="vkeyboard-input-inner">';
      for (var i = 0, j = vkeyboard_value.length; i < j; i++) {
        str += '<span>' + vkeyboard_value.substr(i, 1) + '</span>';
      }
      str += '</div>';
      vkeyboard_input.removeClass('vkeyboard-default').html(str).find('span').removeClass('first-active active');
      if (vkeyboard_cursor_index === -1) {
        vkeyboard_input.find('span').eq(0).addClass('first-active');
      } else {
        vkeyboard_input.find('span').eq(vkeyboard_cursor_index).addClass('active');
      }
    }
  }
  
  // 初始化参数
  function initKeyboard() {
    if (options.enable_point) {
      vkeyboard.find('#vkeyboard-point').text('.').data('value', '.').removeClass('disabled');
    } else {
      vkeyboard.find('#vkeyboard-point').text('').addClass('disabled');
    }
    if (options.enable_dash) {
      vkeyboard.find('#vkeyboard-dash').text('-').data('value', '-').removeClass('disabled');
    } else {
      vkeyboard.find('#vkeyboard-dash').text('').addClass('disabled');
    }
    if (!$.isFunction(options.event_init)) {
      options.event_init = null;
    }
    if (!$.isFunction(options.event_input)) {
      options.event_input = null;
    }
    if (!$.isFunction(options.event_show)) {
      options.event_show = null;
    }
    if (!$.isFunction(options.event_hide)) {
      options.event_hide = null;
    }
    if (options.input !== null) {
      vkeyboard_input = $(options.input).addClass('vkeyboard-input vkeyboard-default');
    }
    if (options.init) {
      vkeyboard_value = options.init.toString();
      updateInputValue();
    }
  }
  
  var result = {
    show: function() {
      if (vkeyboard_show) {
        return;
      }
      vkeyboard_show = true;
      vkeyboard.css('display', 'block');
      setTimeout(function(){
        vkeyboard.addClass('open');
        if (options.event_show) {
          options.event_show();
        }
      }, 100);
    },
    hide: function() {
      vkeyboard.removeClass('open');
      setTimeout(function(){
        vkeyboard.css('display', 'none');
        vkeyboard_show = false;
        if (options.event_close) {
          options.event_close();
        }
      }, 300);
    },
    val: function() {
      return vkeyboard_value;
    },
    confirmDisable: function() {
      $('#vkeyboard-confirm').addClass('disabled');
    },
    confirmEnable: function() {
      $('#vkeyboard-confirm').removeClass('disabled');
    }
  }
  return result;
};