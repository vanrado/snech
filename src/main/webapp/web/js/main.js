function markForDelete() {
    $(".remove").click(function () {
        if ($(this).parent(this).hasClass("for-delete")) {
            $(this).parent(this).removeClass("for-delete");
        } else {
            $(this).parent(this).addClass("for-delete");
        }
    });
}
;

function initMyDatePicker() {
    $('#datetimepicker2').datetimepicker({
        locale: 'sk'
    });
}

$(document).ready(function () {
    initMyDatePicker();
    markForDelete();
    $("#filter").hide();
    $("#filterShowLink").click(function () {
        if ($("#filter").css('display') === 'none') {
            $("#filterShowLink span").removeClass("glyphicon-plus");
            $("#filterShowLink span").addClass("glyphicon-minus");
        } else {
            $("#filterShowLink span").removeClass("glyphicon-minus");
            $("#filterShowLink span").addClass("glyphicon-plus");
        }

        $("#filter").slideToggle();
    });
});