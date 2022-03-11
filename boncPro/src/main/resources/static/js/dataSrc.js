
//数据源配置
var dataSrcModel = {
	//初始页面内容显示
	init: function(obj){
		dataSrcTable(obj);
	},
	initShow: function(dataSrcAbbr){
		var obj = {}
		obj['dataSrcAbbr'] = dataSrcAbbr;
		dataSrcShowTable(obj);
		dataSrcModel.show(1);
	},
	show:function(flag){
		if(flag==1){
			$("#configDataSrc").hide();
			localStorage.setItem("flag",2);
		}else{
			$("#configDataSrc").show();
			localStorage.setItem("flag",1);
		}
	},
	//弹出新增1/修改2弹窗
    add: function (saveType,$this) {
    	debugger;
        var detail = {};
        if ($this) {
            var curRow = $this.parentNode.parentNode;
            detail = $('#dataSrcTable').DataTable().row(curRow).data();
        }
        console.log($('#dataSourceAlert form'));
        $('#dataSourceAlert form')[0].reset();
        //弹窗
        $('#dataSourceAlert').attr('saveType', saveType).modal({'show': 'center', "backdrop": "static"});
        if(saveType=='1'){
        	$("#dataSourceAlert .form-control[col-name='dataSrcAbbr']").attr("readOnly",false);
        }else if(saveType=='2'){
        	$("#dataSourceAlert .form-control[col-name='dataSrcAbbr']").attr("readOnly",true);
        	dataSrcModel.showData(detail); // 数据回显
        }
    },
    // 回显数据
    showData: function (detail) {
        if (detail) {
            var data = detail ? detail : {};
            for (var key in data) {
                var target = $("#dataSourceAlert .form-control[col-name='" + key + "']");
                if (target.length > 0) {
                    $(target).val(data[key]);
                }
            }
        }
    },
    // 获取组表单数据
    getData: function () {
        var obj = {};
        var inputs = $('#dataSourceAlert form .form-control');
        for (var i = 0; i < inputs.length; i++) {
            obj[$(inputs[i]).attr('col-name')] = $(inputs[i]).val();
        }
        return obj;
    },
    // 新增/修改数据
    save: function () {
    	debugger;
        // 表单验证
        if (!$('#dataSourceAlert form').isValid()) {
            return;
        }
    	var saveType = $('#dataSourceAlert').attr('saveType'); // 1新增 2	修改
    	var url = '/bonc/createDataSrc';
    	if(saveType==2){
    		url = '/bonc/editDataSrc';
    	}
    	var obj = dataSrcModel.getData();
        $.ajax({
            url: url,
            type: 'POST',
            data: obj,
            success: function (data) {
            	// successMessager.show('保存成功');
            	if(data.message=='保存成功'){
            		successMessager.show('保存成功');
            		history.go(0);
            	}else{
            		zUI.dialog.alert('<pre>'+data.message+'</pre>');
                    //dataSrcModel.init();
            	}
            },
            error: function (data) {
                failedMessager.show('保存失败');
            }
        });
    },
    // 删除
    del: function (dataSrcAbbr) {
        /*confirmAlert.show('是否确认删除？', function () {
        });*/
    	confirmAlert.show('是否继续删除？', function () {
	        $.ajax({
	            url: '/bonc/deleteDataSrc',
	            type: 'POST',
	            data: {'dataSrcAbbr': dataSrcAbbr},
	            success: function (data) {
	                successMessager.show('删除成功');
	                dataSrcModel.init();
	                history.go(0);
	            },
	            error: function (data) {
	                failedMessager.show('删除成功');
	            }
	        });
    	});
    },
    //按条件查询
    search: function(){
    	var inputs = $('#configDataSrc .input-group .form-control');
        var obj = {};
        for (var i = 0; i < inputs.length; i++) {
            if ($.trim($(inputs[i]).val()) == '') {
                continue;
            }
            obj[$(inputs[i]).attr('data-col')] = $.trim($(inputs[i]).val());
        }
        dataSrcModel.init(obj);
    },
    // 关闭弹框
    hidden: function () {
        $('#dataSourceAlert').modal('toggle', 'center');
    }
}


/**
 * 初始化 接口列表
 */
function dataSrcShowTable(obj) {
	
    $('#dataSrcShowTable').width('100%').dataTable({
    	//默认搜索组件
        "searching": false,
        //排序功能
        "ordering": false,
        "destroy": true,
        //滚动条
        "scrollX":false,
        //控制每页显示条数
        "lengthChange": false,
        //翻页功能
        "paging": false,
        //列表的过滤,搜索和排序信息会传递到Server端进行处理
        "serverSide": false,
        "info": false,
        "columns": [
            {"title": "数据源缩写", "data": "dataSrcAbbr"},
            {"title": "数据接口描述", "data": "dataSrcDesc"},
            {"title": "起效日期", "data": "sDate"},
            {"title": "失效日期", "data": "eDate"}
            ],
        ajax: {
            url: '/bonc/datasource',
            "type": 'GET',
            "data": function (d) { // 查询参数
            	debugger;
                return $.extend({}, d, obj);
            }
        }
    });
}
function dataSrcTable(obj) {
	
    $('#dataSrcTable').width('100%').dataTable({
    	//默认搜索组件
        "searching": true,
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
            {"title": "数据源缩写", "data": "dataSrcAbbr"},
            {"title": "数据源描述", "data": "dataSrcDesc"},
            /*{"title": "起效日期", "data": "sDate","render": function(data, type, row){  
            	console.log(data);
                return getMyDate(data);
            }},
            {"title": "失效日期", "data": "eDate","render": function(data, type, row){  
                return getMyDate(data);
            }},*/
            {"title":"操作" ,"data": null,"render": function(data, type, row) {
            	var html = '<div>';
            		html += '<span onclick="dataSrcModel.add(2,this)" class="btn-sm cm-tblA">修改</span>';//
            		html += '<span onclick="dataSrcModel.del(\''+row.dataSrcAbbr+'\');" class="btn-sm cm-tblC">删除</span>';
            		html += '</div>';
            	$("#row_"+row.functionId).data("rowData",row);
				return html;
			}}
            ],
        ajax: {
            url: '/bonc/queryDataSrc',
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

