<!-- javascript -->		
<script src="<c:url value='/resources/js/jquery-1.8.3.min.js'/>" type="text/javascript"></script>
<script src="<c:url value='/resources/js/ie10-viewport-bug-workaround.js'/>" type="text/javascript"></script>
<script src="<c:url value='/resources/js/bootstrap.min.js'/>" type="text/javascript"></script>
<script src="<c:url value='/resources/js/docs.min.js'/>" type="text/javascript"></script>
<script src="<c:url value='/resources/js/bs.pagination.js'/>" type="text/javascript"></script>
<script src="<c:url value='/resources/js/bootstrap.file-input.js'/>" type="text/javascript"></script>
<script src="<c:url value='/resources/js/ie-emulation-modes-warning.js'/>" type="text/javascript"></script>
<script src="<c:url value='/resources/js/stringBuilder.js'/>" type="text/javascript"></script>
<script src="<c:url value='/resources/js/ajaxfileupload.js'/>" type="text/javascript"></script>
<script src="<c:url value='/resources/js/earlyplan.js?version=2'/>" type="text/javascript"></script>
<script src="<c:url value='/resources/js/md5.min.js'/>" type="text/javascript"></script>
<script src="<c:url value='/resources/plugin-js/moment.js'/>" type="text/javascript"></script>
<script src="<c:url value='/resources/plugin-js/bootstrap/js/transition.js'/>" type="text/javascript"></script>
<script src="<c:url value='/resources/plugin-js/bootstrap/js/collapse.js'/>" type="text/javascript"></script>
<script src="<c:url value='/resources/plugin-js/bootstrap-datetimepicker.min.js'/>" type="text/javascript"></script>

<div class="modal fade" id="confirmModal" tabindex="-1" role="dialog" aria-labelledby="confirmModal" aria-hidden="true">
	<div class="modal-dialog">
		<div id="confirmContent" class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-hidden="true" style="margin-top:-8px;">&times;</button>
			</div>
			
			<div class="modal-body">
					<span id="confirmMessage"></span>
					<input type="hidden" id="comfirmDeleteId"/>
			</div>
			<div style="clear:both;"></div>
			<div class="modal-footer" style="margin-top:20px;">
				<button type="button" class="btn btn-default btn-sm" data-dismiss="modal">Cancel</button>
				<button type="button" class="btn btn-primary btn-sm" onclick="confirmResult()">Yes</button>
			</div>
		</div>
		
	</div>
</div>

<div class="modal fade" id="alertModal" tabindex="-1" role="dialog" aria-labelledby="alertModal" aria-hidden="true">
	<div class="modal-dialog modal-sm">
		<div id="alertContent" class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-hidden="true" style="margin-top:-8px;">&times;</button>
			</div>
			
			<div class="modal-body">
					<span id="alertMessage"></span>
			</div>
			<div style="clear:both;"></div>
			<div class="modal-footer" style="text-align:center; padding:10px;">
				<button type="button" class="btn btn-primary btn-sm" data-dismiss="modal">OK</button>
			</div>
		</div>
		
	</div>
</div>

<div class="modal fade" id="sessionOvertimeModal" tabindex="-1" role="dialog" aria-labelledby="sessionOvertimeModal" aria-hidden="true">
	<div class="modal-dialog modal-sm">
		<div id="sessionOvertimeContent" class="modal-content">
			<div class="modal-header">
			</div>
			
			<div class="modal-body">
				<span id="sessionOvertimeMessage"></span>
			</div>
			<div style="clear:both;"></div>
			<div class="modal-footer" style="text-align:center; padding:10px;">
				<button type="button" class="btn btn-primary btn-sm" data-dismiss="modal" onclick="sessionOvertimeButtonClick()">OK</button>
			</div>
		</div>
		
	</div>
</div>
<script type="text/javascript">
    $('input[type=file]').bootstrapFileInput();
    function viewUserProfile(id){
    	location.href="<c:url value='/u/"+id+"'/>";
    }
    
    function comfirmMessage(message, id){
    	$("#confirmMessage").text(message);
    	$("#comfirmDeleteId").val(id);
    	$("#confirmModal").modal('toggle');
    }
    
    function closeConfirmMessage(){
    	$("#confirmModal").modal('toggle');
    }
    
    function alertMessage(message){
    	$("#alertMessage").text(message);
    	$("#alertModal").modal('toggle');
    }
    
    function sessionOvertimeMessage(message){
    	$("#sessionOvertimeMessage").text(message);
    	$("#sessionOvertimeModal").modal('toggle');
    }
    
    function sessionOvertimeButtonClick(){
    	location.href="<c:url value='/'/>";
    }
    
    function resetInput(id){
		$("#"+id+"-error").text('');
		$("#"+id+"-error").hide();		
	}
    function searchSwitch(){
		if($("#searchItemsDiv").is(":visible")){
			$('#searchSwitch').removeClass("glyphicon-minus");
			$('#searchSwitch').addClass("glyphicon-plus");
			$('#searchItemsDiv').slideUp( "slow" );
			
		}else{
			$('#searchSwitch').removeClass("glyphicon-plus");
			$('#searchSwitch').addClass("glyphicon-minus");
			$('#searchItemsDiv').slideDown( "slow" );
		}
	}
    
    function clearFilter(){
    	$("#searchItemsDiv :input").val();
    }

    function imageResizingToParent(object){
    	$("<img/>")
	    .attr("src", $(object).attr("src"))
	    .load(function() {
	        if(this.width < this.height){
	        	$(object).css({'height':'100%'});
    		}else{
	        	$(object).css({'width':'100%'});
    		}
	    });
    }
</script>