
var dicColModel = {
	init: function(cname){
		var obj = {}
		obj['cname'] = cname;
    	dictionaryColumn(obj);
	},
	//弹出新增/修改弹窗
    show: function (saveType,$this) {
        var detail = {};
        if ($this) {
            var curRow = $this.parentNode.parentNode;
            detail = $('#dicColTable').DataTable().row(curRow).data();
        }
        //console.log(detail);
        $('#dicColumn form')[0].reset();
        $('#dicColumn').attr('saveType', saveType).modal({'show': 'center', "backdrop": "static"});
        dicColModel.echoGroupData(detail); // 数据回显
    },
    // 关闭添加参数组弹框
    hiddenAddGroupAlert: function () {
        $('#dicColumn').modal('toggle', 'center');
    },
    // 回显组数据
    echoGroupData: function (detail) {
        if (detail) {
            var data = detail ? detail : {};
            for (var key in data) {
                var target = $("#dicColumn .form-control[col-name='" + key + "']");
                if (target.length > 0) {
                    $(target).val(data[key]);
                }
            }
        }
    },
    // 新增/修改数据
    saveRuleSetGourp: function () {
        // 表单验证
        /*if (!$('#columnAlert form').isValid()) {
            return;
        }*/
    	var saveType = $('#dicColumn').attr('saveType'); // 1新增 2	修改
    	var url = '/dic/createDicColumn';
    	if(saveType==2){
    		url = '/dic/editDicColumn';
    	}
    	var obj = dicColModel.getGroupData();
        $.ajax({
            url: url,
            type: 'POST',
            data: obj,
            success: function (data) {
            	dicColModel.hiddenAddGroupAlert();
            	dicColModel.init();
            	successMessager.show('保存成功');
            },
            error: function (data) {
            	failedMessager.show('保存失败');
            }
        });
    },
    // 获取组表单数据
    getGroupData: function () {
        var obj = {};
        var inputs = $('#dicColumn form .form-control');
        for (var i = 0; i < inputs.length; i++) {
            obj[$(inputs[i]).attr('col-name')] = $(inputs[i]).val();
        }
        return obj;
    },
    // 删除
    deleteGroup: function (cname) {
        confirmAlert.show('是否确认删除？', function () {
	        $.ajax({
	            url: '/dic/deleteDicColumn',
	            type: 'POST',
	            data: {'cname': cname},
	            success: function (data) {
	            	successMessager.show('删除成功');
	            	dicColModel.init();
	            },
	            error: function (data) {
	            	failedMessager.show('删除失败');
	            }
	        });
        });
    },
    //按条件查询
    search: function(){
    	var inputs = $('#dicColContent .input-group .form-control');
    	debugger;
        var obj = {};
        for (var i = 0; i < inputs.length; i++) {
            if ($.trim($(inputs[i]).val()) == '') {
                continue;
            }
            obj[$(inputs[i]).attr('data-col')] = $.trim($(inputs[i]).val());
        }
        dictionaryColumn(obj);
    },
    //查看版本
    version: function(versionType,key){
    	modelVersionTableModal.initPage(versionType,key);
    }
    
}

/**
 * 词根表
 */
var dicTableModel = {
	init: function(cname){
		var obj = {}
		obj['cname'] = cname;
    	dictionaryTable(obj);
	},
	//弹出新增/修改弹窗
    show: function (saveType,$this) {
        var detail = {};
        if ($this) {
            var curRow = $this.parentNode.parentNode;
            detail = $('#dicTableTable').DataTable().row(curRow).data();
        }
        //console.log(detail);
        $('#dicTable form')[0].reset();
        $('#dicTable').attr('saveType', saveType).modal({'show': 'center', "backdrop": "static"});
        dicTableModel.echoGroupData(detail); // 数据回显
    },
    // 关闭添加参数组弹框
    hiddenAddGroupAlert: function () {
        $('#dicTable').modal('toggle', 'center');
    },
    // 回显组数据
    echoGroupData: function (detail) {
        if (detail) {
            var data = detail ? detail : {};
            for (var key in data) {
                var target = $("#dicTable .form-control[col-name='" + key + "']");
                if (target.length > 0) {
                    $(target).val(data[key]);
                }
            }
        }
    },
    // 新增/修改数据
    saveRuleSetGourp: function () {
        // 表单验证
        /*if (!$('#columnAlert form').isValid()) {
            return;
        }*/
    	var saveType = $('#dicTable').attr('saveType'); // 1新增 2	修改
    	var url = '/dic/createDicTable';
    	if(saveType==2){
    		url = '/dic/editDicTable';
    	}
    	var obj = dicTableModel.getGroupData();
        $.ajax({
            url: url,
            type: 'POST',
            data: obj,
            success: function (data) {
            	dicTableModel.hiddenAddGroupAlert();
            	dicTableModel.init();
            	successMessager.show('保存成功');
            },
            error: function (data) {
            	failedMessager.show('保存失败');
            }
        });
    },
    // 获取组表单数据
    getGroupData: function () {
        var obj = {};
        var inputs = $('#dicTable form .form-control');
        for (var i = 0; i < inputs.length; i++) {
            obj[$(inputs[i]).attr('col-name')] = $(inputs[i]).val();
        }
        return obj;
    },
    // 删除
    deleteGroup: function (cname) {
        confirmAlert.show('是否确认删除？', function () {
        	$.ajax({
                url: '/dic/deleteDicTable',
                type: 'POST',
                data: {'cname': cname},
                success: function (data) {
                	successMessager.show('删除成功');
                	dicTableModel.init();
                },
                error: function (data) {
                	failedMessager.show('删除失败');
                }
            });
        });
    },
    //按条件查询
    search: function(){
    	var inputs = $('#dicTableContent .input-group .form-control');
    	debugger;
        var obj = {};
        for (var i = 0; i < inputs.length; i++) {
            if ($.trim($(inputs[i]).val()) == '') {
                continue;
            }
            obj[$(inputs[i]).attr('data-col')] = $.trim($(inputs[i]).val());
        }
        dictionaryTable(obj);
    },
    //导入词根
    importDictionary: function(){
    	window.open("/dic/dictionary",'newwindow','height=400,width=800,top=400,left=400,toolbar=no,menubar=no,scrollbars=no, resizable=no,location=no, status=no'); 
    },
    //查看版本
    version: function(versionType,key){
    	modelVersionTableModal.initPage(versionType,key);
    }
    
}
/*var localObj = window.location;
var contextPath = localObj.pathname.split("/")[1];
var basePath = localObj.protocol + "//" + localObj.host + "/"+ contextPath;

$(function () {
	var id = $("#flag").val();
	if(id=='1'){
		dictionaryTable();
	}else{
		dictionaryColumn();
	}
	
});

*/
/**
 * 初始化 接口信息
 */
function dictionaryTable(obj) {
    $('#dicTableTable').width('100%').dataTable({
        "searching": false,
        "ordering": false,
        "destroy": true,
        "bLengthChange": false,
        // 自动列宽
        "autoWidth": true,
        "pagingType": "full_numbers",
        "paging": true,
        "info": true,
        "serverSide": false,
        "pageLength": 10,
        "columns": [
        	/*{"title": "全选<input type=\"checkbox\" id=\"checkedAll\" onclick=\"checkedAll()\">", "data": "dataInterfaceName", "width": "5%","render": function (data, type, row) {
               return '<input type="checkbox" id="'+data+'" name="items">';
            }},*/
        	/**
            {"title": "序号", "data": "tableEname", "width": "5%","render": function (data, type, row) {
               return '';
            }},*/
            {"title": "实体名", "data": "cname", "width": "10%"},
            /*{"title": "版本", "data": "version", "width": "8%"},*/
            {"title": "表名", "data": "ename", "width": "8%"},
            /*{"title": "实体名长度", "data": "tLenb", "width": "8%"},
            {"title": "表名长度", "data": "tLen", "width": "7%"},*/
            {"title": "添加日期", "data": "createDate", "width": "7%"},
            {"title":"操作" ,"data": null,"width": "30%","render": function(data, type, row) {
            	var html = '<div>';
            		/*html += '<span onclick="dicTableModel.version(3,\''+row.cname+'\');" class="btn-sm cm-tblA">版本</span>';*/
            		html += '<span onclick="dicTableModel.show(2,this)" class="btn-sm cm-tblB">修改</span>';
            		html += '<span onclick="dicTableModel.deleteGroup(\''+row.cname+'\');" class="btn-sm cm-tblC">删除</span>';
            		html += '</div>';
            	$("#row_"+row.functionId).data("rowData",row);
				return html;
			}}
            ],
        ajax: {
            url: '/dic/queryDicTable',
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

function dictionaryColumn(obj) {
    $('#dicColTable').width('100%').dataTable({
        "searching": false,
        "ordering": false,
        "destroy": true,
        // 自动列宽
        "autoWidth": true,
        "bLengthChange": false,
        "pagingType": "full_numbers",
        "paging": true,
        "info": true,
        "serverSide": false,
        "pageLength": 10,
        "columns": [
        	/*{"title": "全选<input type=\"checkbox\" id=\"checkedAll\" onclick=\"checkedAll()\">", "data": "dataInterfaceName", "width": "5%","render": function (data, type, row) {
               return '<input type="checkbox" id="'+data+'" name="items">';
            }},*/
        	/**
            {"title": "序号", "data": "tableEname", "width": "5%","render": function (data, type, row) {
               return '';
            }},*/
            {"title": "中文词根", "data": "cname", "width": "10%"},
            /*{"title": "版本", "data": "version", "width": "8%"},*/
            {"title": "英文缩写", "data": "ename", "width": "8%"},
            {"title": "英文全拼", "data": "fullEname", "width": "8%"},
            /*{"title": "字符数统计", "data": "wordNum", "width": "7%"},*/
            {"title": "添加日期", "data": "createDate", "width": "7%"},
            {"title":"操作" ,"data": null,"width": "30%","render": function(data, type, row) {
            	var html = '<div>';
            		/*html += '<span onclick="dicColModel.version(4,\''+row.cname+'\');" class="btn-sm cm-tblA">版本</span>';*/
            		html += '<span onclick="dicColModel.show(2,this)" class="btn-sm cm-tblB">修改</span>';
            		html += '<span onclick="dicColModel.deleteGroup(\''+row.cname+'\');" class="btn-sm cm-tblC">删除</span>';
            		html += '</div>';
            	$("#row_"+row.functionId).data("rowData",row);
				return html;
			}}
            ],
        ajax: {
            url: '/dic/queryDicColumn',
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

function add(saveType,$this){
	debugger;
	var menu = localStorage.getItem("dataSrcAbbr");
	if(menu=='col'){
		dicColModel.show(saveType,$this);
	}else if(menu=='table'){
		dicTableModel.show(saveType,$this);
	}
}
function find(){
	var menu = localStorage.getItem("dataSrcAbbr");
	if(menu=='col'){
		dicColModel.search();
	}else if(menu=='table'){
		dicTableModel.search();
	}
}

