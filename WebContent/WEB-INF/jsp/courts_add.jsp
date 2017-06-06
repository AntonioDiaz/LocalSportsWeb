<%@include file="taglibs.jsp"%>
<script>
	$(document).ready(function() {
		$('#btnBack').on('click', function(event) {
			window.location.href = "/center/listCourts?idSportCenter="+ ${sportCenter.id};
		});
		
		
	});
</script>
<h2 style="color: #0061a8">
	${sportCenter.name} - Nueva pista 
</h2>
<hr>
<form:form method="post" action="doAddCourt" commandName="my_form" name="my_form" cssClass="form-horizontal">
	<%@ include file="/WEB-INF/jsp/courts_form.jsp"%>
	<div class="form-group">
		<label class="control-label col-sm-4">&nbsp;</label>
		<div class="col-sm-2">
			<button id="btnBack" type="button" class="btn btn-default btn-block">cancelar</button>
		</div>		
		<div class="col-sm-2">
			<button type="submit" class="btn btn-default btn-block">crear</button>
		</div>
		<div class="col-sm-4">&nbsp;</div>
	</div>	
</form:form>