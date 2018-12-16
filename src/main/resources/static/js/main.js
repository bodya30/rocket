$(document).ready(function () {

    $('#registrationForm').on('submit', function (e) {
        e.preventDefault();
        var registerUrl = $(this).attr('action');
        var csrfToken = $("meta[name='_csrf']").attr("content");
        var csrfParameter = $("meta[name='_csrf_parameter']").attr("content");
        var csrfHeader = $("meta[name='_csrf_header']").attr("content");

        var data = {
            "firstName": $('#firstName').val(),
            "lastName": $('#lastName').val(),
            "email": $('#email').val(),
            "password": $('#password').val(),
            "confirmPassword": $('#confirmPassword').val()
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
                $('.js-register-message').show();
            },
            error: function (response) {
                $('.js-error').html('');
                $('.js-register-message').hide();
                if (response.status === 500) {
                    $('.js-errors-message').append('<span>Something went wrong, please contact us +380 095 111 22 33</span>')
                } else {
                    $.each(response.responseJSON, function (field, errors) {
                        var fieldErrorContainer = $('.js-errors-' + field);
                        var errorContainer = fieldErrorContainer.length ? fieldErrorContainer : $('.js-errors-message');
                        for (i = 0; i < errors.length; i++) {
                            errorContainer.append('<span>' + errors[i] + '</span>');
                        }
                    });
                }
            }
        });
    });

});