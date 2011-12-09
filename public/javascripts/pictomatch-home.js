$(document).ready(function(){
	
	var container = $('#pm_home_container'),
		form = $('#pm_home_register_form', container),
		formBody = $("#pm_home_register_form_body", form),
	    rError = /alert-message error/,
	    
	    Model = container.getModel();

	// Capture Form Submit Event
    form.submit(function() {
    	
		var formdata = form.serialize();
		
		$.post(Model.url.register, formdata, function(response) {
			var hasError = response.match(rError);
			
			if(hasError) {
				formBody.html(response);
			} else {
				location.reload();
			}
		}); 

    	// Don’t let the browser redirect
    	return false;
    });
});