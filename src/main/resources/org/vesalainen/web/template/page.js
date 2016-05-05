

$("div[data-role='page']").on("swipeleft", function(event){
    $("body").pagecontainer("change", "#"+prevPage($(this)));
});

$("div[data-role='page']").on("swiperight", function(event){
    $("body").pagecontainer("change", "#"+nextPage($(this)));
});
