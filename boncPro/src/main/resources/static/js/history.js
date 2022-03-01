/**
 * 接口列表操作
 */
var historyModel = {
	//初始页面内容显示
	init: function(idx){
		var obj = {}
		obj['dataSrcAbbr'] = idx;
		initHistoryTable(obj);
		//按条件查询用
		$("#inter_dataSrcAbbr").val(idx);
	},
	//弹出新增/修改弹窗
    add: function (saveType,$this) {
        var detail = {};
        if ($this) {
            var curRow = $this.parentNode.parentNode;
            detail = $('#interfaceTable').DataTable().row(curRow).data();
        }
        $('#interfaceAlert form')[0].reset();
        $('#interfaceAlert').attr('saveType', saveType).modal({'show': 'center', "backdrop": "static"});
        if(saveType=='1'){
        	$("#interfaceAlert .form-control[col-name='dataSrcAbbr']").val($("#inter_dataSrcAbbr").val());
        	$("#interfaceAlert .form-control[col-name='dataSrcAbbr']").attr("readOnly",true);
        	$("#interfaceAlert .form-control[col-name='dataInterfaceNo']").attr("readOnly",false);
        	$("#interfaceAlert .form-control[col-name='dataInterfaceName']").attr("readOnly",false);
        }else if(saveType=='2'){
        	$("#interfaceAlert .form-control[col-name='dataSrcAbbr']").attr("readOnly",true);
        	$("#interfaceAlert .form-control[col-name='dataInterfaceNo']").attr("readOnly",true);
        	$("#interfaceAlert .form-control[col-name='dataInterfaceName']").attr("readOnly",true);
        	historyModel.showData(detail); // 数据回显
        }
    },
    // 回显数据
    showData: function (detail) {
        if (detail) {
            var data = detail ? detail : {};
            for (var key in data) {
                var target = $("#interfaceAlert .form-control[col-name='" + key + "']");
                if (target.length > 0) {
                    $(target).val(data[key]);
                }
            }
        }
    },
    // 获取组表单数据
    getData: function () {
        var obj = {};
        var inputs = $('#interfaceAlert form .form-control');
        for (var i = 0; i < inputs.length; i++) {
            obj[$(inputs[i]).attr('col-name')] = $(inputs[i]).val();
        }
        return obj;
    },
    // 保存数据
    save: function () {
    	debugger;
        // 表单验证
        if (!$('#interfaceAlert form').isValid()) {
            return;
        }
    	var saveType = $('#interfaceAlert').attr('saveType'); // 1新增 2	修改
    	var url = '/interface/createInterface';
    	if(saveType==2){
    		url = '/interface/editInterface';
    	}
    	var obj = historyModel.getData();
    	$('#loadAlert').modal({'show': 'center', "backdrop": "static"});
        $.ajax({
            url: url,
            type: 'POST',
            data: obj,
            success: function (data) {
                $('#loadAlert').modal('hide');
                if(data.message=='保存成功'){
	                historyModel.init(data.idx);
	            	successMessager.show('保存成功');
	            	historyModel.hidden();
                }else{
                	zUI.dialog.alert('<pre>'+data.message+'<pre>');
                }
//            	historyModel.init(data);
            	
            },
            error: function (data) {
                failedMessager.show(data.message);
            }
        });
    },
    // 删除
    del: function (dataSrcAbbr,dataInterfaceNo,dataInterfaceName) {
    	confirmAlert.show('是否继续删除？', function () {
	        $.ajax({
	            url: '/interface/deleteInterface',
	            type: 'POST',
	            data: {'dataSrcAbbr': dataSrcAbbr,'dataInterfaceNo': dataInterfaceNo,'dataInterfaceName': dataInterfaceName},
	            success: function (data) {
	                successMessager.show('删除成功');
	            	historyModel.init(data);
	            },
	            error: function (data) {
	                failedMessager.show('删除成功');
	            }
	        });
    	});
    },
    //按条件查询
    search: function(){
    	var inputs = $('#interfaceContent .input-group .form-control');
        var obj = {};
        for (var i = 0; i < inputs.length; i++) {
            if ($.trim($(inputs[i]).val()) == '') {
                continue;
            }
            obj[$(inputs[i]).attr('data-col')] = $.trim($(inputs[i]).val());
        }
        initInterfaceTable(obj);
    },
    //查看版本
    version: function(dataSrcAbbr,dataInterfaceNo,dataInterfaceName){
    	$('#versionAlert').modal({'show': 'center', "backdrop": "static"});
		var obj ={};
		obj['dataSrcAbbr']=dataSrcAbbr;
		obj['dataInterfaceNo']=dataInterfaceNo;
		obj['dataInterfaceName']=dataInterfaceName;
		initVersionTable(obj);
    	//interfaceVersionTableModal.initPage(versionType,key);
    },
    //查看详情
    detail: function(dataSrcAbbr,dataInterfaceNo,dataInterfaceName){
    	$('#detailAlert').modal({'show': 'center', "backdrop": "static"});
    	detailModel.init(dataSrcAbbr,dataInterfaceNo,dataInterfaceName);
    },
    //导入表结构
    importTable: function(){
    	window.open("/interface/importTable",'newwindow','width='+(window.screen.availWidth)+',height='+(window.screen.availHeight-20)+',top=0,left=0,toolbar=no,menubar=no,scrollbars=no, resizable=no,location=no, status=no'); 
    },
    // 关闭弹框
    hidden: function () {
        $('#interfaceAlert').modal('toggle', 'center');
    },
}


/**
 * 初始化 接口列表
 */
function initHistoryTable(obj) {
	
    $('#historyTable').width('100%').dataTable({
    	//默认搜索组件
        "searching": false,
        //排序功能
        "ordering": false,
        "destroy": true,
        // 自动列宽
        "autoWidth": false,
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
        	{"title":"操作" ,"data": null,"render": function(data, type, row) {
            	var html = '<div>';
            		html += '<span onclick="historyModel.detail(\''+row.dataSrcAbbr+'\',\''+row.dataInterfaceNo+'\',\''+row.dataInterfaceName+'\');" class="btn-sm cm-tblA">字段</span>';
            		/*html += '<span onclick="historyModel.version(1,\''+row.dataSrcAbbr+'|'+row.dataInterfaceNo+'\');" class="btn-sm cm-tblA">版本</span>';*/
            		html += '<span onclick="historyModel.version(\''+row.dataSrcAbbr+'\',\''+row.dataInterfaceNo+'\',\''+row.dataInterfaceName+'\')" class="btn-sm cm-tblA">版本</span>';
            		html += '<span onclick="historyModel.add(2,this)" class="btn-sm cm-tblA">修改</span>';
            		html += '<span onclick="historyModel.del(\''+row.dataSrcAbbr+'\',\''+row.dataInterfaceNo+'\',\''+row.dataInterfaceName+'\');" class="btn-sm cm-tblA">删除</span>';
            		html += '</div>';
            	//$("#row_"+row.functionId).data("rowData",row);
				return html;
			}},
            {"title": "数据源缩写", "data": "dataSrcAbbr"},
            {"title": "数据接口编号", "data": "dataInterfaceNo"},
            {"title": "数据接口名", "data": "dataInterfaceName"},
            {"title": "数据接口描述", "data": "dataInterfaceDesc","render":function(data,type,row){
            	return '<p style="word-wrap:break-word;">' + data + '</p>';
            }},
            {"title": "数据加载频率", "data": "dataLoadFreq"},
            {"title": "数据加载方式", "data": "dataLoadMthd"},
            {"title": "字段分割符", "data": "filedDelim"},
            {"title": "行分隔符", "data": "lineDelim"},
            {"title": "外表数据库", "data": "extrnlDatabaseName"},
            {"title": "内表数据库", "data": "intrnlDatabaseName"},
            {"title": "外表表名", "data": "extrnlTableName"},
            {"title": "内表表名", "data": "intrnlTableName"},
            {"title": "表类型", "data": "tableType"},
            {"title": "分桶数", "data": "bucketNumber"},
            {"title": "起效日期", "data": "sDate","render": function(data, type, row) {
            	var oDate = new Date(data);
            	var oYear = oDate.getFullYear();
            	var oMonth = oDate.getMonth()+1;
            	var oDay = oDate.getDate();
            	return oYear+"-"+oMonth+"-"+oDay;
            }}
            /*{"title": "失效日期", "data": "eDate","hidden":"true"},*/
            
            ],
        ajax: {
            url: '/interface/queryInterface',
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