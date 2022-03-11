/**
 * 接口列表操作
 */
var procModel = {
	//初始页面内容显示
	init: function(idx){
		var obj = {}
		obj['dataSrcAbbr'] = idx;
		initProcTable(obj);
		//按条件查询用
		$("#proc_dataSrcAbbr").val(idx);
	},
	//弹出新增/修改弹窗
    add: function (saveType,$this) {
        var url = '/model/procInfoList';
        $.ajax({
            url: url,
            type: 'GET',
            success: function (data) {
            	var detail = {};
                if ($this) {
                    var curRow = $this.parentNode.parentNode;
                    detail = $('#procTable').DataTable().row(curRow).data();
                }
                $('#procAlert form')[0].reset();
                $('#procAlert').attr('saveType', saveType).modal({'show': 'center', "backdrop": "static"});
                if(saveType=='1'){
                	$("#procAlert .form-control[col-name='dataSrcAbbr']").val($("#proc_dataSrcAbbr").val());
                	$("#procAlert .form-control[col-name='dataSrcAbbr']").attr("readOnly",true);
                	$("#procAlert .form-control[col-name='dataInterfaceNo']").attr("readOnly",false);
                }else if(saveType=='2'){
                	procModel.showData(detail); // 数据回显
                	$("#procAlert .form-control[col-name='dataSrcAbbr']").attr("readOnly",true);
                	$("#procAlert .form-control[col-name='dataInterfaceNo']").attr("readOnly",true);
                }
            	console.info(detail);
            	console.info(data.dbList);
            	console.info(data.procList);
            	var dbList = data.dbList;
            	var procList = data.procList;
            	var dbOption = "";
            	var dbKey = '';
            	var procKey = '';
            	for(var i in dbList){
            		var db = dbList[i];
            		if(saveType=='2'&&detail.procDatabaseName==db.func_name){
            			dbKey=db.func_name;
            		}else{
            			//dbOption = dbOption + '<option>'+db.func_name+'</option>';
            		}
            		dbOption = dbOption + '<option value="'+db.func_name+'">'+db.func_desc+'('+db.func_name+')</option>';
            	}
            	$("#proc_procDatabaseName").html(dbOption);
            	$("#proc_procDatabaseName").val(dbKey);
            	var procOption = "";
            	for(var i in procList){
            		var proc = procList[i];
            		if(saveType=='2'&&detail.procName==proc.func_param){
            			procKey = proc.func_param;
            			//procOption = procOption + "<option selected>"+proc.func_param+"</option>";
            		}else{
            			//procOption = procOption + "<option>"+proc.func_param+"</option>";
            		}
            		procOption = procOption + '<option value="'+proc.func_param+'">'+proc.func_param_desc+'('+proc.func_param+')</option>';
            	}
            	$("#proc_procName").html(procOption);
            	$("#proc_procName").val(procKey);
            	//successMessager.show('成功');
            },
            error: function (data) {
                failedMessager.show('加载列表失败');
            }
        });
    },
    // 回显数据
    showData: function (detail) {
        if (detail) {
            var data = detail ? detail : {};
            for (var key in data) {
                var target = $("#procAlert .form-control[col-name='" + key + "']");
                if (target.length > 0) {
                    $(target).val(data[key]);
                }
            }
        }
    },
    // 获取组表单数据
    getData: function () {
        var obj = {};
        var inputs = $('#procAlert form .form-control');
        for (var i = 0; i < inputs.length; i++) {
            obj[$(inputs[i]).attr('col-name')] = $(inputs[i]).val();
        }
        return obj;
    },
    //导入表结构
    importProc: function(){
    	window.open("/model/importProc",'newwindow','width='+(window.screen.availWidth)+',height='+(window.screen.availHeight)+',top=0,left=0,toolbar=no,menubar=no,scrollbars=no, resizable=no,location=no, status=no'); 
    },
    // 新增/修改数据
    save: function () {
    	debugger;
        // 表单验证
        if (!$('#procAlert form').isValid()) {
            return;
        }
    	var saveType = $('#procAlert').attr('saveType'); // 1新增 2	修改
    	var url = '/model/createProc';
    	if(saveType==2){
    		url = '/model/editProc';
    	}
    	var obj = procModel.getData();
    	console.log(obj);
    	$('#loadAlert').modal({'show': 'center', "backdrop": "static"});
        $.ajax({
            url: url,
            type: 'POST',
            data: obj,
            success: function (data) {
            	$('#loadAlert').modal('hide');
            	successMessager.show('保存成功');
            	procModel.hidden();
            	procModel.init(data);
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
	            url: '/model/deleteProc',
	            type: 'POST',
	            data: {'dataSrcAbbr': dataSrcAbbr,'dataInterfaceNo': dataInterfaceNo},
	            success: function (data) {
	                successMessager.show('删除成功');
	            	procModel.init(data);
	            },
	            error: function (data) {
	                failedMessager.show('删除成功');
	            }
	        });
    	});
    },
    //按条件查询
    search: function(){
    	var inputs = $('#procContent .input-group .form-control');
        var obj = {};
        for (var i = 0; i < inputs.length; i++) {
            if ($.trim($(inputs[i]).val()) == '') {
                continue;
            }
            obj[$(inputs[i]).attr('data-col')] = $.trim($(inputs[i]).val());
        }
        initProcTable(obj);
    },
    //查看版本
    version: function(dataSrcAbbr,dataInterfaceNo){
    	$('#procVersionAlert').modal({'show': 'center', "backdrop": "static"});
		var obj ={};
		obj['dataSrcAbbr']=dataSrcAbbr;
		obj['dataInterfaceNo']=dataInterfaceNo;
		initProcVersionTable(obj);
    },
    // 关闭弹框
    hidden: function () {
        $('#procAlert').modal('toggle', 'center');
    },
}


/**
 * 初始化 接口列表
 */
function initProcTable(obj) {
	
    $('#procTable').width('100%').dataTable({
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
        	/*{"title": "全选<input type=\"checkbox\" id=\"checkedAll\" onclick=\"checkedAll()\">", "data": null, "width": "5%","render": function (data, type, row) {
               return '<input type="checkbox" id="'+row.dataInterfaceNo+'" name="items">';
            }},*/
        	{"title":"操作" ,"data": null,"render": function(data, type, row) {
            	var html = '<div>';
            		html += '<span onclick="procModel.version(\''+row.dataSrcAbbr+'\',\''+row.dataInterfaceNo+'\')" class="btn-sm cm-tblA">版本</span>';
            		html += '<span onclick="procModel.add(2,this)" class="btn-sm cm-tblA">修改</span>';
            		html += '<span onclick="procModel.del(\''+row.dataSrcAbbr+'\',\''+row.dataInterfaceNo+'\');" class="btn-sm cm-tblA">删除</span>';
            		html += '</div>';
            	$("#row_"+row.functionId).data("rowData",row);
				return html;
			}},
            {"title": "数据源缩写", "data": "dataSrcAbbr"},
            {"title": "数据接口编号", "data": "dataInterfaceNo"},
            /*{"title": "数据接口名", "data": "dataInterfaceName"},*/
            /*{"title": "数据接口描述", "data": "dataInterfaceDesc"},*/
            {"title": "存储过程数据库名", "data": "procDatabaseName"},
            {"title": "存储过程", "data": "procName"}
            /*{"title": "起效日期", "data": "sDate"},
            {"title": "失效日期", "data": "eDate"}*/
            
            ],
        ajax: {
            url: '/model/queryProc',
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