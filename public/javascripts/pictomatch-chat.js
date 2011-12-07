$(document).ready(function(){
	
	var chatContainer = $('#pm_chat_container');
	
	var Model = chatContainer.getModel();
	
	
	console.log("socket", Model.url.subscribeSocket);

	// Create a socket
    var socket;
    
    if(window.WebSocket) { // Chrome
  		console.log("Using WebSocket");
 		socket  = new WebSocket(Model.url.subscribeSocket);
 	} else if(window.MozWebSocket) { // Firefox
  		console.log("Using MozWebSocket");
 		socket = new MozWebSocket(Model.url.subscribeSocket);
 	}
    
    if(socket) {
    	// Message received on the socket
    	socket.onmessage = function(event) {
        	var parts = /^([^:]+):([^:]+)(:(.*))?$/.exec(event.data);
        	display({
            	type: 		parts[1],
            	userLogin: 	parts[2],
            	text: 		parts[4]
        	})
    	}
    
    	$('#pm_chat_cmd_send').click(function(e) {
        	var message = $('#pm_chat_message').val();
        	$('#pm_chat_message').val('');
        	socket.send(message);
    	});

    } else {
    	// No WebSocket !!!!
    	
    	var lastReceivedMsgId = 0;
		
		$.get(Model.url.subscribeAjax);
		
		// Retrieve new messages
	    var getMessages = function() {
	        $.ajax({
	            url: Model.url.listen,
	            data: {lastReceivedMsgId: lastReceivedMsgId},
	            success: function(events) {
	                $(events).each(function() {
	                    display(this.data);
	                    lastReceivedMsgId = this.id;
	                });
	                getMessages();
	            },
	            dataType: 'json'
	        });
	    }
	    getMessages();
	    
		
	    $('#pm_chat_cmd_send').click(function(e) {
	        var message = $('#pm_chat_message').val();
	        $('#pm_chat_message').val('');
	
	        $.post(Model.url.write, {message: message});
	    });
    }
    
	
	
	var display = function(event) {
       	$('#pm_chat_thread').append(tmpl('pm_chat_message_tmpl', {event: event}));
    	$('#pm_chat_thread').scrollTo('max')
   	}
   	
   	
   	
   	$('#pm_chat_message').keypress(function(e) {
	    if(e.charCode == 13 || e.keyCode == 13) {
	        $('#pm_chat_cmd_send').click()
	    	e.preventDefault()
		}
	})
   	
   	
   	
    $('#pm_chat_cmd_unsubscribe').click(function() {
    	var trigger = $(this);
    	
    	if(window.WebSocket|| "MozWebSocket" in window) { // Chrome / Firefox
	  		trigger.attr('href', Model.url.home);
	 	} else {
	 		trigger.attr('href', Model.url.unsubscribe);
	 	}
	 	
	 	return true;
	});
});