$(function(){
  $("#lovepiano-ql li label").on("click",function(){
    $(this).next().stop().slideToggle(300);
  });
});


$(function(){
  $("#setting-condition-open").click(function(){
    $(this).next().stop().slideToggle();
    return false;
  });
});

$(function(){
  $("#setting-condition-close").click(function(){
    $(this).parent().parent().stop().slideToggle();
    return false;
  });
});
