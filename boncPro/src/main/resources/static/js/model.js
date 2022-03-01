/**
 * 接口列表操作
 */
var dataModelModel = {
	//初始页面内容显示
	init: function(idx){
		$("#checkedAll").prop("checked", false );
		var obj = {}
		obj['dataSrcAbbr'] = idx;
		initDataModelTable(obj);
		//按条件查询用
		$("#model_dataSrcAbbr").val(idx);
	},
    //按条件查询
    search: function(){
    	var inputs = $('#dataModelContent  .input-group .form-control');
        var obj = {};
        for (var i = 0; i < inputs.length; i++) {
            if ($.trim($(inputs[i]).val()) == '') {
                continue;
            }
            obj[$(inputs[i]).attr('data-col')] = $.trim($(inputs[i]).val());
        }
        initDataModelTable(obj);
    },
    //查看详情
    /*detail: function(dataSrcAbbr,dataInterfaceNo){
    	$('#columnQueryAlert').modal({'show': 'center', "backdrop": "static"});
    	detailModel.init(dataSrcAbbr,dataInterfaceNo);
    },*/
    detail: function(dataSrcAbbr,dataInterfaceNo){
    	$('#detailAlert').modal({'show': 'center', "backdrop": "static"});
    	detailModel.init(dataSrcAbbr,dataInterfaceNo);
    },
    common:function(url,type){
    	var chkBoxes = $('#dataModelTable').find('input:checked');
        if (chkBoxes.length == 0) {
            zUI.dialog.alert('<pre>请至少选择一条记录</pre>');
            return false;
        }
		var dataSrcAbbr = $("#model_dataSrcAbbr").val();
		var arr = [];
		$(chkBoxes).each(function() {
	    	arr.push($(this).attr('id'));
	    });
		var param = {};
		param.tables = arr;
		param.dataSrcAbbr = dataSrcAbbr;
		param.dbType = '1';
		var json = JSON.stringify(param);
		console.log(json);
		$('#loadAlert').modal({'show': 'center', "backdrop": "static"});
		$.ajax({
			url:url,
			type:"post",
			data:json,
			contentType:"application/json;charset=UTF-8",
			success:function(data){
				$('#loadAlert').modal('hide');
				console.log(data);
				if(data.msgCode=='1111'){
					zUI.dialog.alert('<pre>'+data.msgData+'</pre>');
				}else{
					zUI.dialog.alert_max('<pre>'+data.msgData+'</pre>');
					dataModelModel.init(data.idx);
				}
			}
		}) 
	},
	createSql:function(){
		
		var url = "/model/createSql";
		var param = {};
		var arr = [];
		arr.push($("#model_inter").val());
		param.tables = arr;
		param.dataSrcAbbr = $("#model_ds").val();
		param.dbType = $("#model_dbType").val();
		var json = JSON.stringify(param);
		console.log(json);
		$('#loadAlert').modal({'show': 'center', "backdrop": "static"});
		$.ajax({
			url:url,
			type:"post",
			data:json,
			contentType:"application/json;charset=UTF-8",
			success:function(data){
				$('#loadAlert').modal('hide');
				console.log(data);
				if(data.msgCode=='1111'){
					zUI.dialog.alert('<pre>'+data.msgData+'</pre>');
				}else{
					zUI.dialog.alert_max('<pre>'+data.msgData+'</pre>');
				}
				
			}
		}) 
	},
	createFile:function(){
		var url = "/model/createFile";
		dataModelModel.common(url,1);
		//alert("建表语句生成文件成功");
	},
	insertDb:function(){
		var url = "/model/insertDb";
		dataModelModel.common(url,2);
	},
	viewSqlBak:function(ds,inter){
		$('#modelAlert').modal({'show': 'center', "backdrop": "static"});
		$("#model_inter").val(inter);
		$("#model_ds").val(ds);
	},
	viewSql:function(ds,inter){
		var url = "/model/createSql";
		var param = {};
		var arr = [];
		arr.push(inter);
		param.tables = arr;
		param.dataSrcAbbr = ds;
		param.dbType = '0';
		var json = JSON.stringify(param);
		console.log(json);
		$('#loadAlert').modal({'show': 'center', "backdrop": "static"});
		$.ajax({
			url:url,
			type:"post",
			data:json,
			contentType:"application/json;charset=UTF-8",
			success:function(data){
				$('#loadAlert').modal('hide');
				console.log(data);
				if(data.msgCode=='1111'){
					zUI.dialog.alert('<pre>'+data.msgData+'</pre>');
				}else{
					zUI.dialog.alert_max('<pre>'+data.msgData+'</pre>');
				}
				
			}
		})
	},
	delModel:function(ds,outer,inner){
		$('#delModelAlert').modal({'show': 'center', "backdrop": "static"});
		$("#delModel_outer").val(outer);
		$("#delModel_ds").val(ds);
		$("#delModel_inner").val(inner);
	},
	delModelFunc:function(){
		
		var url = "/interface/delTable";
		var param = {};
		var arr = [];
		var ft = [];
		if($("#delModel_dbType").val()=='2'){
			arr.push($("#delModel_inner").val());
			ft.push("odata");
		}else if($("#delModel_dbType").val()=='1'){
			arr.push($("#delModel_outer").val());
			ft.push("sdata_oltp");
		}else if($("#delModel_dbType").val()=='0'){
			arr.push($("#delModel_inner").val());
			arr.push($("#delModel_outer").val());
			ft.push("odata");
			ft.push("sdata_oltp");
		}
		param.tables = arr;
		param.dataSrcAbbr = $("#delModel_ds").val();
		param.funcType = ft;
		var json = JSON.stringify(param);
		console.log(json);
		/*$('#loadAlert').modal({'show': 'center', "backdrop": "static"});*/
		$.ajax({
			url:url,
			type:"post",
			data:json,
			contentType:"application/json;charset=UTF-8",
			success:function(data){
				/*$('#loadAlert').modal('hide');*/
				console.log(data);
				zUI.dialog.alert('<pre>'+data.msgData+'</pre>');
				if(data.msgCode=="0000"){
					dataModelModel.init(data.idx);
					$('#delModelAlert').modal('hide');
				}
			}
		}) 
	},
    // 关闭弹框
    hidden: function () {
        $('#modelBaseGroupAlert').modal('toggle', 'center');
    },
}


/**
 * 初始化 接口列表
 */
function initDataModelTable(obj) {
	
    $('#dataModelTable').width('100%').dataTable({
    	//默认搜索组件
        "searching": false,
        //排序功能
        "ordering": false,
        "destroy": true,
        // 自动列宽
        "autoWidth": false,
        //滚动条
        "scrollX":false,
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
        	/*{"title": "全选<input type=\"checkbox\" id=\"checkedAll\" onclick=\"checkedAll()\">", "data": null, "width": "5%","render": function (data, type, row) {
               return '<input type="checkbox" id="'+row.dataInterfaceNo+'" name="items">';
            }},*/
        	/*{"title": "<input type=\"checkbox\" id=\"checkedAll\" onclick=\"checkedAll()\">全选", "data": null, "width": "5%","render": function (data, type, row) {
                return '<input type="checkbox" id="'+row.dataInterfaceName+'-'+row.condition+'-'+row.num+'" name="items">';
             }},*/
             {"title":"操作" ,"data": null,"width":"15%","render": function(data, type, row) {
             	var html = '<div>';
             		html += '<span onclick="dataModelModel.detail(\''+row.dataSrcAbbr+'\',\''+row.dataInterfaceNo+'\');" class="btn-sm cm-tblA">字段</span>';
             		html += '<span onclick="dataModelModel.viewSql(\''+row.dataSrcAbbr+'\',\''+row.dataInterfaceName+'\')" class="btn-sm cm-tblA">查看建表语句</span>';
             		html += '<span onclick="dataModelModel.delModel(\''+row.dataSrcAbbr+'\',\''+row.dataInterfaceName+'\',\''+row.intrnlTableName+'\')" class="btn-sm cm-tblA">删除表</span>';
             		/*html += '<span onclick="dataModelModel.createSql(\''+row.dataSrcAbbr+'\',\''+row.dataInterfaceName+'\',1);" class="btn-sm cm-tblA">查看外表</span>';*/
             		/*html += '<span onclick="dataModelModel.createSql(\''+row.dataSrcAbbr+'\',\''+row.dataInterfaceName+'\',2);" class="btn-sm cm-tblA">查看内表</span>';*/
             		/*html += '<span onclick="interfaceModel.version(1,\''+row.dataSrcAbbr+'|'+row.dataInterfaceNo+'\');" class="btn-sm cm-tblA">版本</span>';
             		html += '<span onclick="interfaceModel.add(2,this)" class="btn-sm cm-tblA">修改</span>';
             		html += '<span onclick="interfaceModel.del(\''+row.dataSrcAbbr+'\',\''+row.dataInterfaceNo+'\');" class="btn-sm cm-tblC">删除</span>';*/
             		html += '</div>';
             	$("#row_"+row.functionId).data("rowData",row);
 				return html;
 			}},
            {"title": "数据源缩写", "data": "dataSrcAbbr"},
            {"title": "数据接口编号", "data": "dataInterfaceNo"},
            {"title": "数据接口名", "data": "dataInterfaceName"},
            {"title": "数据接口描述", "data": "dataInterfaceDesc"},
            {"title": "字段个数", "data": "num"},
            {"title": "表创建状态", "data": "condition","render": function(data, type, row) {
            	/*if(data.indexOf("未")!=-1){
            		var str =data.split(",");
            		var str1 = str[0];
            		var str2 = str[1];
            		if(str1.indexOf("未")!=-1){
            			str1 = '<font color="blue">'+str1+'</font>';
            		}
            		if(str2.indexOf("未")!=-1){
            			str2 = '<font color="blue">'+str2+'</font>';
            		}
            		data = str1+","+str2;
            	}*/
            	return data;
            }}
            /*{"title": "数据加载频率", "data": "dataLoadFreq"},
            {"title": "数据加载方式", "data": "dataLoadMthd"},
            {"title": "字段分割符", "data": "filedDelim"},
            {"title": "行分隔符", "data": "lineDelim"},
            {"title": "外表数据库", "data": "extrnlDatabaseName"},
            {"title": "内表数据库", "data": "intrnlDatabaseName"},
            {"title": "外表表名", "data": "extrnlTableName"},
            {"title": "内表表名", "data": "intrnlTableName"},
            {"title": "表类型", "data": "tableType"},
            {"title": "分桶数", "data": "bucketNumber"},
            {"title": "起效日期", "data": "sDate"}*/
            /*{"title": "失效日期", "data": "eDate"}*/
            
            ],
        ajax: {
            url: '/interface/queryModel',
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