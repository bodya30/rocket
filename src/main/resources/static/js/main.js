$(document).ready(function() {

    $('#registrationForm').on('submit', function(e) {
        e.preventDefault();
        var form = $(this);
        var registerUrl = form.attr('action');

        $.ajax({
            type: 'POST',
            url: registerUrl,
            data: form.serialize(),
            success: function () {

            },
            error: function (response) {
                $.each(response.responseJSON, function (field, errors) {
                    var errorContainer = $('.js-errors-' + field);
                    errorContainer.html('');
                    for (i = 0; i < errors.length; i++) {
                        errorContainer.append('<span>' + errors[i] + '</span>');
                    }
                });
            }
        });
    });

});