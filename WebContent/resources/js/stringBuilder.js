/**
 * @author Siolk
 * StringBuilder 2.5
 */
function StringBuilder(str) {
	this.Class = "StringBuilder";
	this.val = new String();
	if(typeof(str) != "undefined"){
		this.val += str;
	}
	
	this.append = function(str){
		with(this){val += str;return this;}
	};
	this.toString = function(){
		with(this){return val;}
	};
	this.indexOf = function(str, position) {
		with(this){
			if(isNaN(position)){
				return val.indexOf(str);
			}else{
				return val.indexOf(str, position);	
			}
			
		}
	};
	this.lastIndexOf = function(str, position) {
		with(this){
			if(isNaN(position)){
				return val.lastIndexOf(str);
			}else{
				return val.lastIndexOf(str, position);
			}
			
		}
	};
	this.replace = function(str, repStr) {
		with(this){
			val = val.replace(str, repStr);
			return val;
		}
	};
	this.replaceAll = function(str, repStr){
		var start = 0;
		with (this) {
			while((start = val.indexOf(str, start)) != -1){
				val = val.replace(str, repStr);
			}
			return val;
		}
	};
	this.replaceAllSelf = function(str, repStr){
		with(this){val = replaceAll(str, repStr);return this;}
	};
	this.replaceSelf = function(str, repStr) {
		with(this){val = replace(str, repStr);return this;}
	};
};
