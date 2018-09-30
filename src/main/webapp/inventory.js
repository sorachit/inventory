var table;
$(document).ready(function() {
	table = $('#int_inventory').dataTable({
		"searching" : false,
		"ordering" : false,
		"processing" : true,
		"columns" : [ {
			"data" : "id"
		}, {
			"data" : "userName"
		}, {
			"data" : "inventoryName"
		} ]
	});

	$('#int_inventory tbody').on('click', 'tr', function() {
		if ($(this).hasClass('selected')) {
			$(this).removeClass('selected');
		} else {
			$('#int_inventory tr.selected').removeClass('selected');
			$(this).addClass('selected');
		}
	});

});

$("#button-search").click(function() {
	$.ajax({
		url : "./api/inventory-api/search",
		type : "POST",
		contentType : 'application/json',
		data : JSON.stringify({
			"id" : $('#id').val(),
			"userName" : $('#userName').val(),
			"inventoryName" : $('#inventoryName').val()
		}),
		success : function(result) {
			$('#int_inventory').dataTable().fnClearTable();
			$('#int_inventory').dataTable().fnAddData(result);
		}
	});
});

$("#button-clear").click(function() {
	$('#id').val("");
	$('#userName').val("");
	$('#inventoryName').val("");
	$('#int_inventory').dataTable().fnClearTable();
});

$("#button-add").click(function() {
	$('#idSave').val("");
	$('#userNameSave').val("");
	$('#inventoryNameSave').val("");
});

$("#button-edit").click(function() {
	var row = $('#int_inventory').dataTable().api().row('.selected').data();
	$('#idSave').val(row.id);
	$('#userNameSave').val(row.userName);
	$('#inventoryNameSave').val(row.inventoryName);
});

$('#button-delete').click(function() {
	var row = $('#int_inventory').dataTable().api().row('.selected').data();
	$.ajax({
		url : "./api/inventory-api/delete",
		type : "POST",
		contentType : 'application/json',
		data : JSON.stringify({
			"id" : row.id
		}),
		success : function(result) {
			$('#int_inventory').dataTable().api().row('.selected').remove().draw( false );
		},
		error : function(result){
			
		}
	});
	
	
	
});

$("#button-save").click(function() {
	$.ajax({
		url : "./api/inventory-api/save",
		type : "POST",
		contentType : 'application/json',
		data : JSON.stringify({
			"id" : $('#idSave').val(),
			"userName" : $('#userNameSave').val(),
			"inventoryName" : $('#inventoryNameSave').val()
		}),
		success : function(result) {
			$('#int_inventory').dataTable().fnUpdate(result);
			$('#saveModal').modal('hide');
		}
	});
});
