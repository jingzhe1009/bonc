var createSqlModel = {
	//初始页面内容显示
	init: function(idx){
		var obj = {}
		//initCreateSqlTable(obj);
	},
	setDbType:function(flag){
		$("#dbType").val(flag);
	},
	common:function(url){
		var chkBoxes = $('#dataModelTable').find('input:checked');
        if (chkBoxes.length == 0) {
            zUI.dialog.alert('<pre>请至少选择一条记录</pre>');
            return false;
        }
		//detail = $('#procTable').DataTable().
		/*var table = $('#procTable').dataTable();
		debugger;
		var trList = table.fnGetNodes();
		for(i=0;i<trList.length;i++){
		    var trObj = trList[i]; 
		    console.log(trObj.firstElementChild.firstElementChild);
		}*/
		//console.log(obj);
		/*var obj = {};
        var inputs = $('#createSqlAlert form .form-control');
        for (var i = 0; i < inputs.length; i++) {
            obj[$(inputs[i]).attr('col-name')] = $(inputs[i]).val();
        }*/
		var dataSrcAbbr = $("#inter_dataSrcAbbr").val();
		var arr = [];
		/*$("input[name='items']").each(function(index,item){
	        var id = $(this).attr("id");
			arr.push(id);
	    });*/
		$(chkBoxes).each(function() {
	    	arr.push($(this).attr('id'));
	    });
		var param = {};
		param.tables = arr;
		param.dataSrcAbbr = dataSrcAbbr;
		param.dbType = 'tdh';
		var json = JSON.stringify(param);
		console.log(json);
		$.ajax({
			url:url,
			type:"post",
			data:json,
			contentType:"application/json;charset=UTF-8",
			success:function(data){
				console.log(data);
				zUI.dialog.alert('<pre>'+data+'</pre>');
				//alert(data);
			}
		}) 
	},
	createSql:function(ds,inter,i){
		var arr = [];
		arr.push(inter);
		var url = "/bonc/createSql";
		var param = {};
		param.tables = arr;
		param.dataSrcAbbr = ds;
		param.dbType = 'tdh';
		param.flag=i;
		var json = JSON.stringify(param);
		console.log(json);
		$.ajax({
			url:url,
			type:"post",
			data:json,
			contentType:"application/json;charset=UTF-8",
			success:function(data){
				console.log(data);
				//alert(data);
				zUI.dialog.alert('<pre>'+data+'</pre>');
			}
		}) 
		//alert("入库成功");
	},
	createFile:function(){
		var url = "/bonc/createFile";
		createSqlModel.common(url);
		//alert("建表语句生成文件成功");
	},
	insertDb:function(){
		var url = "/bonc/insertDb";
		createSqlModel.common(url);
	},
	createSqlSave:function(){
		$("#pText").text('');
		$("#pText").append('<font color=red>create</font> table xxx () values ();');
	}
}

//物化
function initCreateSqlTable(obj) {
    $('#createSqlTable').width('100%').dataTable({
    	//默认搜索组件
        "searching": false,
        //排序功能
        "ordering": false,
        "destroy": true,
        // 自动列宽
        "autoWidth": true,
        //滚动条
        "scrollX":true,
        //控制每页显示条数
        "lengthChange": false,
        "pagingType": "full_numbers",
        //翻页功能
        "paging": true,
        //控制总数显示
        "info": true,
        //列表的过滤,搜索和排序信息会传递到Server端进行处理
        "serverSide": false,
        "pageLength": 10,
        "columns": [
        	
            {"title": "数据源缩写", "data": "dataSrcAbbr"},
            {"title": "数据接口编号", "data": "dataInterfaceNo"},
            {"title": "数据接口名", "data": "dataInterfaceName"},
            {"title": "数据接口描述", "data": "dataInterfaceDesc"}
            ],
        ajax: {
            url: '/bonc/queryInterface',
            "type": 'GET',
            "data": function (d) { // 查询参数
            	debugger;
                return $.extend({}, d, obj);
            }
        },
        "fnDrawCallback": function (oSettings, json) {
            
        }
    });
}


