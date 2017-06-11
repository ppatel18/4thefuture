/**
 * Created by Aanya on 6/11/2017.
 */
// Items Magic [Preview-1]

//your code here

$('.paper.preview-1 .item').click(function () {
    $('.paper.preview-1 .item').not(this).removeClass('active');
    $(this).toggleClass('active');
    if ($('.paper.preview-1 .item').hasClass('active')) {
        $('.paper.preview-1').addClass('item-active');
    } else {
        $('.paper.preview-1').removeClass('item-active')
    };
});
// Preventing Closing when Click inside counter
$('.minus,.plus').click(function (a) {
    a.stopPropagation();
});

// Adding Counter [Preview-1]
$('.paper.preview-1 .plus').click(function () {
    $(this).parent().find('.number').html(function (y, val) {
        return val * 1 + 1
    });
});

$('.paper.preview-1 .minus').click(function () {
    var increased = parseInt($(this).parent().find('.number').text());
    if (isNaN(increased) || increased > 0) {
        $(this).parent().find('.number').html(function (t, val) {
            return val * 1 - 1
        });
    } else {
        return false;
    }
});
$('.paper.preview-1 .plus, .paper.preview-1 .minus').click(function () {
    var increased = parseInt($(this).parent().find('.number').text());

    var itemsTotal = 0;
    $('.paper.preview-1 .number').each(function () {
        itemsTotal += ($(this).html() * 1);
    });
    $('.paper.preview-1 .items-total').html(itemsTotal);

    var itemsTotalAll = parseInt($('.paper.preview-1 .items-total').text());
    if (isNaN(itemsTotalAll) || itemsTotalAll <= 0) {
        $('.paper.preview-1 .action').hide();
    } else {
        $('.paper.preview-1 .action').show();
    }

    if (isNaN(increased) || increased <= 0) {
        $(this).parent().find('.number').hide();
    } else {
        $(this).parent().find('.number').show();
    }
});


