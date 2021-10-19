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
