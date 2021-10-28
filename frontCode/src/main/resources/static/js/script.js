$(function() {
  $('.tab_menu').on('click', function() {
    var tabWrap = $(this).parents('.content_frame');
    var tabBtn = tabWrap.find(".tab_menu");
    var tabContents = tabWrap.find('.content_area');
    tabBtn.removeClass('active');
    $(this).addClass('active');
    var elmIndex = tabBtn.index(this);
    tabContents.removeClass('show');
    tabContents.eq(elmIndex).addClass('show');
  });
});
$(function() {
  $('.side_menu').on('click', function() {
    var tabWrap = $(this).parents('.content_wrapper');
    var tabBtn = tabWrap.find(".side_menu");
    var tabContents = tabWrap.find('.main_content');
    tabBtn.removeClass('side_active');
    $(this).addClass('side_active');
    var elmIndex = tabBtn.index(this);
    tabContents.removeClass('main_show');
    tabContents.eq(elmIndex).addClass('main_show');
  });
});


$(function(){
  $("#howto_download_toggle").on("click", function() {
    $(this).next().slideToggle();
  });
});
$(function(){
  $("#howto_upload_toggle").on("click", function() {
    $(this).next().slideToggle();
  });
});
$(function(){
  $("#howto_future_toggle").on("click", function() {
    $(this).next().slideToggle();
  });
});
$(function(){
  $("#howto_contact_toggle").on("click", function() {
    $(this).next().slideToggle();
  });
});


window.onload = function () {
  getFormValue();
  // UploadFormを取得
  var $formObject = document.getElementById( "addItems_form" );
  // UploadFormの入力オブジェクトの数だけループ
  for( var $i = 0; $i < $formObject.length; $i++ ) {
    $formObject.elements[$i].onkeyup = function(){
        getFormValue();
    };
  }
}
function getFormValue() {
  const firstHtml = '<!DOCTYPE html><html lang="ja"><head><meta charset="UTF-8"><meta name="viewport" content="width=device-width, initial-scale=1.0"><meta http-equiv="X-UA-Compatible" content="ie=edge"></head><body>';
  const endHtml = '</body></html>';
  const styleStart = '<style>';
  const styleEnd = '</style>';
  // UploadFormを取得
  var $formObject = document.getElementById("addItems_form");
  // iframeのsrcdocにデータ入力
  document.getElementById( "addItems_iframe_content" ).srcdoc = firstHtml + $formObject.htmlInputText.value + styleStart + $formObject.cssInputText.value + styleEnd + endHtml;
}
