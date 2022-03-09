function importExcel() {
	debugger
	var d = new Date();
	var batchNo = d.getTime();
	$("#batchNo").val(batchNo);
	var fileInput = $('#filename').get(0).files[0];
	if (fileInput) {
		var formData = new FormData($("#form")[0]);
		//$('#loadAlert').modal({'show': 'center', "backdrop": "static"});
		$.ajax({
			//几个参数需要注意一下
			type: 'POST',//方法类型
			url: "/info/importExcel",//url
			data: formData,
			async: false,
			cache: false,
			contentType: false,
			processData: false,
			success: function (result) {
				console.log(result);//打印服务端返回的数据(调试用)
				localStorage.setItem("batchNo",$("#batchNo").val());
				localStorage.setItem("dataSrcTmp",result.dataSrcAbbr);
				zUI.dialog.alert('<pre>' + result.msgData + '</pre>');
			},
			error: function (error) {
				debugger
				console.log(error);
				zUI.dialog.alert('<pre>异常</pre>');
			}
		});
	} else {
		zUI.dialog.alert('<pre>请上传文件</pre>');
	}
}

function alert(content){
	var set = {
		content: content,
		// title: title,
		resizable: false,
		width: '300px',
		btns: {				//校验通过，点击确定，进入信息核对界面
			'确定':function(){
				if (content.search("成功") != -1) {
					window.location.href = '/info/infoCheck';
				}
			}
		},
	};
	zUI.dialog.alertUI(set);
}
//导入
function tmpToSave(flag) {

	var paramInface = {};
	var paramColumn = {};
	var paramProc = {};

	var arr = [];
	var arrColumn = [];
	var arrProc = [];

	// obj['batchNo'] = localStorage.getItem("batchNo");
	// obj['dataSrcAbbr'] =  localStorage.getItem("dataSrcTmp");
	// var batchNo = $("#batchNo").val();
	var batchNo = localStorage.getItem("batchNo");
	if (batchNo == '') {
		zUI.dialog.alert('<pre>请先导入校验</pre>');
		return false;
	} else {
		debugger
		$("input[name='items']").each(function (index, item) {
			var id = $(this).attr("id");
			arr.push(id);
		});
		$("input[name='itemsColumn']").each(function (index, item) {
			var id = $(this).attr("id");
			arrColumn.push(id);
		});
		$("input[name='itemsProc']").each(function (index, item) {
			var id = $(this).attr("id");
			arrProc.push(id);
		});
		paramInface.dbType = '2';
		paramColumn.dbType = '2';
		paramProc.dbType = '2';
	}


	var url = "/info/tmpToSaveAll";
	// param.tables = arr;
	paramInface.tables = arr;
	paramColumn.tables = arrColumn;
	paramProc.tables = arrProc;

	var jsonInface = JSON.stringify(paramInface);
	var jsonColumn = JSON.stringify(paramColumn);
	var jsonProc = JSON.stringify(paramProc);
	debugger
	var str = {"paramInface": jsonInface, "paramColumn": jsonColumn, "paramProc": jsonProc};
	$('#loadAlert').modal({'show': 'center', "backdrop": "static"});
	$.ajax({
		url: url,
		type: "post",
		data: JSON.stringify(str),
		contentType: "application/json;charset=UTF-8",
		success: function (data) {
			$('#loadAlert').modal('hide');
			//$("#loadShow").hide();
			console.log(jsonInface);
			console.log(jsonColumn);
			console.log(jsonProc);
			zUI.dialog.alert('<pre>' + data.msgData + '</pre>');
			var obj = {};
			// obj['batchNo'] = $("#batchNo").val();
			// obj['dataSrcAbbr'] = data.dataSrcAbbr;
			localStorage.getItem("batchNo");
			localStorage.getItem("dataSrcTmp");
			initInterfaceTable(obj);
			initColumnTable(obj);
			initProcTable(obj);
			$("#checkedAll").prop("checked", false);

		}
	})
}


