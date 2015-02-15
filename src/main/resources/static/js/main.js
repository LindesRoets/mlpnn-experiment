$(document).ready(function () {
    var pageTitle = $(document).find('title').text();

    if (pageTitle === 'hl7-example') {
        $('#example-menu').addClass('active');
        $('#hl7-example-menu').addClass('active');
    } else if (pageTitle === 'hl7-example-validator') {
        $('#example-menu').addClass('active');
        $('#hl7-example-validator-menu').addClass('active');
    } else if (pageTitle === 'PipeSmasher') {
        $('#pipe-smasher-menu').addClass('active');
    } else if (pageTitle === 'adt-pid-form-validator') {
        $('#adt-menu').addClass('active');
        $('#adt-pid-form-validator-menu').addClass('active');
    } else if (pageTitle === 'adt-pid-hl7') {
        $('#adt-menu').addClass('active');
        $('#adt-hl7-pid-menu').addClass('active');
    } else if (pageTitle === 'MultiLayer Perceptron Dashboard') {
        $('#mlp-dashboard').addClass('active');
    } else if (pageTitle === 'Data Set Dashboard'){
        $('#dataset-dashboard').addClass('active');
    }


    $('div.has-error input.form-control').on('click', function () {
        $('.help-block').parent().toggle('slide');
    });
});