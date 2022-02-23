/**
 * 字段操作
 */
var columnModel = {
	init: function(dataSrcAbbr,dataInterfaceNo,columnNo){
		debugger;
		var obj = {}
		obj['dataSrcAbbr'] = dataSrcAbbr;
    	obj['dataInterfaceNo'] = dataInterfaceNo;
    	obj['columnNo'] = columnNo;
		initColumnTable(obj);
		
		$("#column_dataSrcAbbr").val(dataSrcAbbr);
		$("#column_dataInterfaceNo").val(dataInterfaceNo);
	},
	//弹出新增/修改弹窗
    add: function (saveType,$this) {
        var detail = {};
        if ($this) {
            var curRow = $this.parentNode.parentNode;
            detail = $('#columnTable').DataTable().row(curRow).data();
        }
        $('#columnAlert form')[0].reset();
        $('#columnAlert').attr('saveType', saveType).modal({'show': 'center', "backdrop": "static"});
        
        if(saveType=='1'){
        	$("#columnAlert .form-control[col-name='dataSrcAbbr']").val($("#column_dataSrcAbbr").val());
        	$("#columnAlert .form-control[col-name='dataSrcAbbr']").attr("readOnly",true);
        	$("#columnAlert .form-control[col-name='dataInterfaceNo']").attr("readOnly",false);
        	$("#columnAlert .form-control[col-name='columnNo']").attr("readOnly",false);
        	$("#columnAlert .form-control[col-name='dataInterfaceName']").attr("readOnly",false);
        	//$("#columnAlert .form-control[col-name='dataInterfaceNo']").val($("#column_dataInterfaceNo").val());
        }else if(saveType=='2'){
        	$("#columnAlert .form-control[col-name='dataSrcAbbr']").attr("readOnly",true);
        	$("#columnAlert .form-control[col-name='dataInterfaceNo']").attr("readOnly",true);
        	$("#columnAlert .form-control[col-name='columnNo']").attr("readOnly",true);
        	$("#columnAlert .form-control[col-name='dataInterfaceName']").attr("readOnly",true);
        	columnModel.showData(detail); // 数据回显
        }
    },
    // 关闭添加参数组弹框
    hiddenAddGroupAlert: function () {
        $('#columnAlert').modal('toggle', 'center');
    },
    // 回显组数据
    showData: function (detail) {
        if (detail) {
            var data = detail ? detail : {};
            for (var key in data) {
                var target = $("#columnAlert .form-control[col-name='" + key + "']");
                if (target.length > 0) {
                    $(target).val(data[key]);
                }
            }
        }
    },
    // 新增/修改数据
    save: function () {
        // 表单验证
        if (!$('#columnAlert form').isValid()) {
            return;
        }
    	var saveType = $('#columnAlert').attr('saveType'); // 1新增 2	修改
    	var url = '/col/createColumn';
    	if(saveType==2){
    		url = '/col/editColumn';
    	}
    	var obj = columnModel.getData();
    	$('#loadAlert').modal({'show': 'center', "backdrop": "static"});
        $.ajax({
            url: url,
            type: 'POST',
            data: obj,
            success: function (data) {
            	$('#loadAlert').modal('hide');
            	debugger;
            	if(data.message=='保存成功'){
            		columnModel.hiddenAddGroupAlert();
            		columnModel.init(data.idx,data.interNo,data.colNo);
            		successMessager.show('保存成功');
            	}else{
            		zUI.dialog.alert('<pre>'+data.message+'<pre>');
            	}
                // successMessager.show('保存成功');
                
            },
            error: function (data) {
            	failedMessager.show(data.message);
            }
        });
    },
    // 获取组表单数据
    getData: function () {
        var obj = {};
        var inputs = $('#columnAlert form .form-control');
        for (var i = 0; i < inputs.length; i++) {
            obj[$(inputs[i]).attr('col-name')] = $(inputs[i]).val();
        }
        return obj;
    },
    // 删除
    del: function (dataSrcAbbr,dataInterfaceNo,columnNo) {
        confirmAlert.show('是否确认删除？', function () {
        	$.ajax({
                url: '/col/deleteColumn',
                type: 'POST',
                data: {'dataSrcAbbr': dataSrcAbbr,'dataInterfaceNo': dataInterfaceNo,'columnNo':columnNo,'dataInterfaceName': dataInterfaceName},
                success: function (data) {
                	columnModel.init(data.idx);
                	successMessager.show('删除成功');
                },
                error: function (data) {
                	failedMessager.show('删除成功');
                }
            });
        });
    },
    //按条件查询
    search: function(){
    	var inputs = $('#colContent .input-group .form-control');
        var obj = {};
        for (var i = 0; i < inputs.length; i++) {
            if ($.trim($(inputs[i]).val()) == '') {
                continue;
            }
            obj[$(inputs[i]).attr('data-col')] = $.trim($(inputs[i]).val());
        }
        console.log(obj);
        initColumnTable(obj);
    },
    //导入字段
    importCol: function(){
    	window.open("/col/importCol",'newwindow','width='+(window.screen.availWidth)+',height='+(window.screen.availHeight-20)+',top=0,left=0,toolbar=no,menubar=no,scrollbars=no, resizable=no,location=no, status=no'); 
    },
    //查看版本
    version: function(dataSrcAbbr,dataInterfaceNo,columnNo,dataInterfaceName){
    	$('#colVersionAlert').modal({'show': 'center', "backdrop": "static"});
		var obj ={};
		obj['dataSrcAbbr']=dataSrcAbbr;
		obj['dataInterfaceNo']=dataInterfaceNo;
		obj['columnNo']=columnNo;
		obj['dataInterfaceName']=dataInterfaceName;
		initColVersionTable(obj);
    	//interfaceVersionTableModal.initPage(versionType,key);
    },
    
}


/**
 * 详情列表
 * @param obj
 * @returns
 */
function initColumnTable(obj) {
    $('#columnTable').width('100%').dataTable({
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
            		html += '<span onclick="columnModel.version(\''+row.dataSrcAbbr+'\',\''+row.dataInterfaceNo+'\',\''+row.columnNo+'\',\''+row.dataInterfaceName+'\');" class="btn-sm cm-tblA">版本</span>';
            		html += '<span onclick="columnModel.add(2,this)" class="btn-sm cm-tblA">修改</span>';
            		html += '<span onclick="columnModel.del(\''+row.dataSrcAbbr+'\',\''+row.dataInterfaceNo+'\',\''+row.columnNo+'\',\''+row.dataInterfaceName+'\');" class="btn-sm cm-tblA">删除</span>';
            		html += '</div>';
            	$("#row_"+row.functionId).data("rowData",row);
				return html;
			}},
            {"title": "数据源缩写", "data": "dataSrcAbbr"},
            {"title": "接口编号", "data": "dataInterfaceNo" },
            {"title": "接口名", "data": "dataInterfaceName" },
            {"title": "字段编号", "data": "columnNo" },
            {"title": "字段名", "data": "columnName" },
            {"title": "数据类型", "data": "dataType" },
            {"title": "格式", "data": "dataFormat" },
            {"title": "是否非空", "data": "nullable"},
            {"title": "空值替代值", "data": "replacenull"},
            {"title": "分隔符", "data": "comma" },
            {"title": "字段描述", "data": "columnComment" },
            {"title": "分桶字段", "data": "isbucket" },
            {"title": "起效日期", "data": "sDate","render": function(data, type, row) {
            	var oDate = new Date(data);
            	var oYear = oDate.getFullYear();
            	var oMonth = oDate.getMonth()+1;
            	var oDay = oDate.getDate();
            	return oYear+"-"+oMonth+"-"+oDay;
            }}
            /*{"title": "失效日期", "data": "eDate" },*/
            
            ],
        ajax: {
            url: '/col/queryColumn',
            "type": 'GET',
            "data": function (d) { // 查询参数
                return $.extend({}, d, obj);
            }
        },
        "fnDrawCallback": function (oSettings, json) {
            
        }
    });
}