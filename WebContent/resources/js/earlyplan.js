/**
 * <pre>
 * 格式化日期物件。
 * y:年
 * M:月
 * d:日
 * H:時
 * m:分
 * s:秒
 * </pre>
 * @param d
 * @param fmt
 * @returns {String}
 */
function formatJsonDate(d, fmt) {
	var s = "";
	if(fmt==undefined || fmt==null) {
		fmt = "y/M/d H:m";
	}
	if(d) {
		var c;
		for(var i=0, len=fmt.length;i<len;++i) {
			c = fmt[i];
			switch (c) {
			case "y":
				s += (d.year + 1900);
				break;
			case "M":
				s += to0NStr(d.month + 1);				
				break;
			case "d":
				s += to0NStr(d.date);
				break;
			case "H":
				s += to0NStr(d.hours);
				break;
			case "m":
				s += to0NStr(d.minutes);
				break;
			case "s":
				s += to0NStr(d.seconds);
				break;
			default:
				s += c;
				break;
			}
		}
	}
	return s;
}

function to0NStr(n) {
	return n<10?"0"+n:""+n;
}

function getFormattedDate(d, fmt) {
	var s = "";
	if(fmt==undefined || fmt==null) {
		fmt = "y/M/d H:m";
	}
	if(d) {
		var c;
		for(var i=0, len=fmt.length;i<len;++i) {
			c = fmt[i];
			switch (c) {
			case "y":
				s += d.getFullYear();
				break;
			case "M":
				s += to0NStr(d.getMonth() + 1);				
				break;
			case "d":
				s += to0NStr(d.getDate());
				break;
			case "H":
				s += to0NStr(d.getHours());
				break;
			case "m":
				s += to0NStr(d.getMinutes());
				break;
			case "s":
				s += to0NStr(d.getSeconds());
				break;
			default:
				s += c;
				break;
			}
		}
	}
	return s;
	
}

function getLastMonth(){
	var now = new Date();
	var lastMonth = new Date();
	if (now.getMonth() == 0) {
	    lastMonth = new Date(now.getFullYear() - 1, 11, 1);
	} else {
	    lastMonth = new Date(now.getFullYear(), now.getMonth() - 1, 1);
	}
	return lastMonth;
}

function getCurrentFirstDay(){
	var now = new Date();
	var current = new Date(now.getFullYear(), now.getMonth(), 1);
	return current;
}

function getNextFirstDay(){
	var now = new Date();
	var nextMonth = new Date(now.getFullYear(), now.getMonth() + 1, 1);
	return nextMonth;
}