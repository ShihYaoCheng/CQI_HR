<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<div class="modal fade" id="deleteModal" tabindex="-1" role="dialog" aria-labelledby="basicModal" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
				<h4 class="modal-title" id="myModalLabel">刪除功能</h4>
			</div>
			<div class="modal-body">
				<input type="hidden" id="deleteId"/>
				<span>確定要刪除嗎?</span>
			</div>
			<div style="clear:both;"></div>
			<div class="modal-footer" style="margin-top:20px">
				<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
				<button type="button" class="btn btn-primary" onclick="deleteData()">Delete</button>
			</div>
		</div>
	</div>
</div>
<script type="text/javascript">
function deleteData(){
	var id = $("#deleteId").val();
	var targetURL= "<c:url value='/security/"+url+"/"+id+"'/>";
	
	$("body").css("cursor", "progress");
	$.ajax({
		type : "DELETE",
		url : targetURL,
		data : {
			
		},
		dataType: "json",
		success : function(data) {
			$("body").css("cursor", "auto");
			alert(data.message);
			if (data.success) {
				queryData(1);
				$('#deleteModal').modal('hide');
			}
			
		}
	});
}

function deleteData(){
	var id = $('#deleteId').val();
	var targetURL= "<c:url value='/security/"+url+"/"+id+"'/>";
	
	$("body").css("cursor", "progress");
	$.ajax({
		type : "DELETE",
		url : targetURL,
		data : {
			
		},
		dataType: "json",
		success : function(data) {
			$("body").css("cursor", "auto");
			if (data.success) {
				queryData(1);
				$('#deleteModal').modal('hide');
			}
			
		}
	});
}

</script>