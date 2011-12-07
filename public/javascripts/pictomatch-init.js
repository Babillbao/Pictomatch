$(document).ready(function(){
	
	// Model introspection
    $.fn.getModel = function(){
    	var target = this;
    	var expando = target.data('model');
    	var ret = {}, inputs, model; 
    	
    	if(expando) {
    		return expando;
    	}
    
    	// Walk through hidden input and init model
    	inputs = target.find("input[type='hidden'][class='pm-data']");
    	model = inputs.serializeArray();
    	
		$.each(model, function(i, data){
			var name = data.name;
			var value = data.value;
			ns.call(ret, name, value);
		});
		
		// Remove model elements to prevent any submit form.
		inputs.remove();
		
		// Then still hold it as expando for further use
		this.data('model', ret);
	
		return ret;
    };
    
    /**
	* Based on DUI's one. <br />
	*
	* Make a namespace within a class.
	* Usage 1: MyClass.ns('foo.bar');
	* Usage 2: MyClass.ns('foo.bar', 'baz');
	*
	* @param {String} name Period separated list of namespaces to nest. MyClass.ns('foo.bar') makes MyClass['foo']['bar'].
	* @param {optional mixed} value Set the contents of the deepest specified namespace to this value.
	*
	*/
    function ns() {
        if (arguments.length == 0) throw new Error('ns should probably have some arguments passed to it.');
        
        var arg = arguments[0];
        var levels = null;
        var get = (arguments.length == 1 || arguments[1] === undefined) && arg.constructor != Object ? true : false;
        
        if (arg.constructor == String) {
            var dummy = {};
            dummy[arg] = arguments[1] ? arguments[1] : undefined;
            
            arg = dummy;
        }
        
        if (arg.constructor == Object) {
            var _class = this, miss = false, last = this;
            
            $.each(arg, function(nsName, nsValue){
                //Reset nsobj back to the top each time
                var nsobj = _class;
                var levels = nsName.split('.');
                
                $.each(levels, function(i, level){
                    //First, are we using ns as a getter? Also, did our get attempt fail?
                    if (get && typeof nsobj[level] == 'undefined') {
                        //Dave's not here, man
                        miss = true;
                        
                        //Break out of each
                        return false;
                    } else if (i == levels.length - 1 && nsValue) {
                        //Ok, so we're setting. Is it time to set yet or do we move on?
                        nsobj[level] = nsValue;
                    } else if (typeof nsobj[level] == 'undefined') {
                        //...nope, not yet. Check to see if the ns doesn't already exist in our class...
                        //...and make it a new static class
                        nsobj[level] = {};
                    }
                    
                    //Move one level deeper for the next iteration
                    last = nsobj = nsobj[level];
                });
            });
            
            return miss ? undefined : last;
        }
    };
});