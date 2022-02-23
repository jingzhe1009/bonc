/**
 * 函数登记
 */
var logModel = {
	//初始页面内容显示
	init: function(idx){
		var obj = {}
		obj['dataSrcAbbr'] = idx;
		initLogTable(obj);
		//按条件查询用
		//$("#inter_dataSrcAbbr").val(idx);
	},
	//弹出新增/修改弹窗
    add: function (saveType,$this) {
        var detail = {};
        if ($this) {
            var curRow = $this.parentNode.parentNode;
            detail = $('#procTable').DataTable().row(curRow).data();
        }
        $('#modelBaseGroupAlert form')[0].reset();
        $('#modelBaseGroupAlert').attr('saveType', saveType).modal({'show': 'center', "backdrop": "static"});
        if(saveType=='1'){
        	$("#modelBaseGroupAlert .form-control[col-name='dataSrcAbbr']").val($("#dataSrcAbbr").val());
        }else if(saveType=='2'){
        	funcModel.showData(detail); // 数据回显
        }
    },
    // 回显数据
    showData: function (detail) {
        if (detail) {
            var data = detail ? detail : {};
            for (var key in data) {
                var target = $("#modelBaseGroupAlert .form-control[col-name='" + key + "']");
                if (target.length > 0) {
                    $(target).val(data[key]);
                }
            }
        }
    },
    // 获取组表单数据
    getData: function () {
        var obj = {};
        var inputs = $('#modelBaseGroupAlert form .form-control');
        for (var i = 0; i < inputs.length; i++) {
            obj[$(inputs[i]).attr('col-name')] = $(inputs[i]).val();
        }
        return obj;
    },
    // 新增/修改数据
    save: function () {
    	debugger;
        // 表单验证
        if (!$('#modelBaseGroupAlert form').isValid()) {
            return;
        }
    	var saveType = $('#modelBaseGroupAlert').attr('saveType'); // 1新增 2	修改
    	var url = '/bonc/createInterface';
    	if(saveType==2){
    		url = '/bonc/editInterface';
    	}
    	var obj = funcModel.getData();
        $.ajax({
            url: url,
            type: 'POST',
            data: obj,
            success: function (data) {
            	successMessager.show('保存成功');
            	funcModel.hidden();
            	funcModel.init(data);
            },
            error: function (data) {
                failedMessager.show('保存失败');
            }
        });
    },
    // 删除
    del: function (dataSrcAbbr,dataInterfaceNo) {
        /*confirmAlert.show('是否确认删除？', function () {
        });*/
    	confirmAlert.show('是否继续删除？', function () {
	        $.ajax({
	            url: '/bonc/deleteInterface',
	            type: 'POST',
	            data: {'dataSrcAbbr': dataSrcAbbr,'dataInterfaceNo': dataInterfaceNo},
	            success: function (data) {
	                successMessager.show('删除成功');
	            	funcModel.init(data);
	            },
	            error: function (data) {
	                failedMessager.show('删除成功');
	            }
	        });
    	});
    },
    //按条件查询
    search: function(){
    	var inputs = $('#modelBasePageContent .modelBaseWrapper .input-group .form-control');
        var obj = {};
        for (var i = 0; i < inputs.length; i++) {
            if ($.trim($(inputs[i]).val()) == '') {
                continue;
            }
            obj[$(inputs[i]).attr('data-col')] = $.trim($(inputs[i]).val());
        }
        initFuncTable(obj);
    },
    //查看版本
    version: function(versionType,key){
    	modelVersionTableModal.initPage(versionType,key);
    },
    //查看详情
    detail: function(dataSrcAbbr,dataInterfaceNo){
    	$('#columnQueryAlert').modal({'show': 'center', "backdrop": "static"});
    	columnModel.init(dataSrcAbbr,dataInterfaceNo);
    },
    // 关闭弹框
    hidden: function () {
        $('#modelBaseGroupAlert').modal('toggle', 'center');
    },
}


/**
 * 初始化 接口列表
 */
function initLogTable(obj) {
	
    $('#logTable').width('100%').dataTable({
    	//默认搜索组件
        "searching": false,
        //排序功能
        "ordering": false,
        "destroy": true,
        // 自动列宽
        "autoWidth": true,
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
            {"title": "id", "data": "actionId"},
            {"title": "执行用户", "data": "actionUser"},
            {"title": "执行步骤描述", "data": "actionDesc"},
            {"title": "执行时间", "data": "actionTime"}
            ],
        ajax: {
            url: '/bonc/queryLog',
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