$(document).ready(function() {

    $('#registrationForm').on('submit', function(e) {
        e.preventDefault();
        var registerUrl = $(this).attr('action');
        var csrfToken = $("meta[name='_csrf']").attr("content");
        var csrfParameter = $("meta[name='_csrf_parameter']").attr("content");
        var csrfHeader = $("meta[name='_csrf_header']").attr("content");

        var data = {
            "firstName": $('#firstName').val(),
            "lastName": $('#lastName').val(),
            "email": $('#email').val(),
        };
        data[csrfParameter] = csrfToken;

        var headers = {};
        headers[csrfHeader] = csrfToken;

        $.ajax({
            type: 'POST',
            contentType: 'application/json',
            url: registerUrl,
            headers: headers,
            data: JSON.stringify(data),
            success: function () {
                $('.js-error').html('');
            },
            error: function (response) {
                $('.js-error').html('');
                $.each(response.responseJSON, function (field, errors) {
                    var errorContainer = $('.js-errors-' + field);
                    for (i = 0; i < errors.length; i++) {
                        errorContainer.append('<span>' + errors[i] + '</span>');
                    }
                });
            }
        });
    });

});