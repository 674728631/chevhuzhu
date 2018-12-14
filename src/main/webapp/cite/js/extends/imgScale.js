// 图片自适应居中
function resetImg(obj,judge) {
        var img = $(obj);
        var target_w = '100%';
        var target_h = '100%';
        var target_l = 0;
        var target_t = 0;
        var img_w = img.width();
        var img_h = img.height();
        var con_w = img.parent().width();
        var con_h = img.parent().height();
        var scale_x = con_w / img_w;
        var scale_y = con_h / img_h;
        if(judge){
            if (scale_x <= scale_y) {
                target_h = img_h * scale_x / con_h;
                target_t = (1 - target_h) / 2;
                target_h = (Math.round(target_h * 10000) / 100) + '%';
                target_t = (Math.round(target_t * 10000) / 100) + '%';
            } else {
                target_w = img_w * scale_y / con_w;
                target_l = (1 - target_w) / 2;
                target_w = (Math.round(target_w * 10000) / 100) + '%';
                target_l = (Math.round(target_l * 10000) / 100) + '%';
            }
        }else{
            if (scale_x >= scale_y) {
                target_h = img_h * scale_x / con_h;
                target_t = (1 - target_h) / 2;
                target_h = (Math.round(target_h * 10000) / 100) + '%';
                target_t = (Math.round(target_t * 10000) / 100) + '%';
            } else {
                target_w = img_w * scale_y / con_w;
                target_l = (1 - target_w) / 2;
                target_w = (Math.round(target_w * 10000) / 100) + '%';
                target_l = (Math.round(target_l * 10000) / 100) + '%';
            }
        }
    img.css({
        'position':'absolute',
        'width': target_w,
        'height': target_h,
        'left': target_l,
        'top': target_t,
        'opacity':'1'
    });
}

//点击放大图片
var timer = null;
//点击放大图片
/*function scaleImg(obj){
    clearTimeout(timer); //在双击事件中，先清除前面click事件的时间处理
    //点击放大图片
    $(".bigImg").show();
    /!*获取浏览器窗口宽高*!/
    var window_height = parseInt($(window).height());
    var window_width = parseInt($(window).width());
    /!*获取 图片路径*!/
    var img_src = $(obj).find("img").get(0).src;
    $(".bigImg").css({"width":window_width + "px","height":window_height + "px"});

    $(".bigImg .bigImgShow").attr("src",img_src);
    var img_width = $(".bigImgShow").width();
    var img_height = $(".bigImgShow").height();
    /!*判断图片原图是否高于浏览器*!/
   /!* if(img_height > window_height){
        $(".bigImgShow").css({"transform":"scale(1)","marginLeft" : - (img_width/2) +"px","marginTop" : "0","top":"0"});
    }else{
        $(".bigImgShow").css({"transform":"scale(1)","marginLeft" : - (img_width/2) +"px","marginTop" : - (img_height/2) +"px","top":"50%"});
    }*!/

    $("#closeBtn").click(function(){
        $(".bigImg").hide();
    })
}*/

//查看大图 轮播 修改版
function scaleImg(obj){
    //动态添加 图片
    var localImgNum;
    var mainImg = $(obj).closest(".img-info").find('li');
    var imgNumHj = mainImg.length;
    //存储 图片名、图片地址
    var addressObj ={
        "default":[]
    };
    var imgArr = [];
    for(var a = 0;a<imgNumHj;a++){
        addressObj.default.push({"title": mainImg.eq(a).find("p").text(),"src": mainImg.eq(a).find("a").find("img").get(0).src})
        imgArr.push(mainImg.eq(a).find("a").find("img").get(0).src);
    }
$("#swiperSon").html("");
var html = '';
for(var n = 0;n < imgNumHj;n++){
html += '<div class="swiper-slide swiper-no-swiping zoomableContainer'+ n +'" >';
html += '<h3 class="xs-title">'+ addressObj.default[n].title +'<i class="fa fa-repeat rote-btn" aria-hidden="true" onclick="roteBen(this)"></i></h3>';
html += '<img src="'+ addressObj.default[n].src +'" alt="" class="imageFullScreen'+ n +'"/>';
html += '</div>';
}
$("#swiperSon").html(html);
//获取当前点击的图片src
var img_src_one = $(obj).find("img").get(0).src;
for(var j = 0;j<imgArr.length;j++){
if(imgArr[j] ==img_src_one ){
    //以当前点击的图片 为第一张轮播图
    localImgNum = j;
}
}
//所有照片 轮播 滑动
layer.open({
title:'照片查看',
closeBtn:'2',
type:1,
area:['60%','70%'],
scrollbar: false,
content: $('#swiperLay'),
success:function () {
    //轮播 swiper配置
    var swiper = new Swiper('.swiper-container', {
        pagination: '.swiper-pagination',
        paginationClickable: '.swiper-pagination',
        nextButton: '.swiper-button-next',
        prevButton: '.swiper-button-prev',
        spaceBetween: 30,
        effect: 'fade',
        noSwiping : true,
        initialSlide :localImgNum,
    });
    //鼠标中心缩放 拖拽
    var swiperSonNum = $("#swiperSon div").length;
    for(var i = 0;i<swiperSonNum;i++){
        $('.imageFullScreen'+ i +'').smartZoom({'containerClass': 'zoomableContainer'+ i+''});
    }
}
});

}
//图片旋转
var current = 0;
function roteBen(imgS){
current = (current+90)%360;
$(imgS).parent("h3").next("div").children("img").css({"transform":"rotate("+current+"deg)","transform-origin":"center center 0"});
}
