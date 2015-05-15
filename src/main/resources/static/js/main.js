$(document).ready(function () {
    var pageTitle = $(document).find('title').text();

    if (pageTitle === 'Multilayer Perceptron Global Convergence Graph For All Data Sets') {
        $('#graph-menu').addClass('active');
        $('#graph-all').addClass('active');
    } else if (pageTitle === 'Multilayer Perceptron Convergence Graph') {
        $('#graph-menu').addClass('active');
        $('#graph-menu-heading').addClass('active');
    } else if (pageTitle === 'Multilayer Perceptron Dashboard') {
		$('#mlp-dashboard-actions').addClass('active');
        $('#mlp-dashboard').addClass('active');
    } else if (pageTitle === 'List Multilayer Perceptrons'){
		$('#mlp-list').addClass('active');
        $('#mlp-dashboard-actions').addClass('active');		
	} else if (pageTitle === 'Create Multilayer Perceptron'){
		$('#mlp-create').addClass('active');
        $('#mlp-dashboard-actions').addClass('active');			
	} else if (pageTitle === 'Download Data Sets') {
		$('#dataset-menu').addClass('active');
		$('#dataset-download').addClass('active');
	}
});