function initProcTable(obj) {
	
    $('#procTable').width('100%').dataTable({
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
        "lengthChange": true,
        "pagingType": "full_numbers",
        //翻页功能
        "paging": true,
        //控制总数显示
        "info": true,
        //列表的过滤,搜索和排序信息会传递到Server端进行处理
        "serverSide": false,
        "pageLength": 10,
        "columns": [
        	{"title": "<input type=\"checkbox\" id=\"checkedAll\" onclick=\"checkedAll()\">全选", "data": null, "width": "5%","render": function (data, type, row) {
               return '<input type="checkbox" id="'+row.dataSrcAbbr+'-'+row.dataInterfaceNo+'-'+row.importType+'-'+row.batchNo+'" name="items">';
            }},
            {"title": "导入类型", "data": "importType","render":function(data,type,row){
            	if(data=='1'){
             		return '<font color="blue">新增</font>';
             	}else if(data=='2'){
             		return '<font color="red">修改</font>';
             	}
             	return '<font color="black">未变化</font>';
            }},
            {"title": "数据源缩写", "data": "dataSrcAbbr"},
            {"title": "数据接口编号", "data": "dataInterfaceNo"},
            /*{"title": "数据接口名", "data": "dataInterfaceName"},*/
            /*{"title": "数据接口描述", "data": "dataInterfaceDesc"},*/
            {"title": "存储过程数据库名", "data": "procDatabaseName"},
            {"title": "存储过程", "data": "procName"},
            /*{"title": "起效日期", "data": "sDate"},
            {"title": "失效日期", "data": "eDate"},*/
            {"title":"操作" ,"data": null,"render": function(data, type, row) {
            	var html = '<div>';
            		html += '<span onclick="version(\''+row.dataSrcAbbr+'\',\''+row.dataInterfaceNo+'\')" class="btn-sm cm-tblA">版本</span>';
            		html += '</div>';
            	$("#row_"+row.functionId).data("rowData",row);
				return html;
			}}
            ],
        ajax: {
            url: '/model/queryProcTmp',
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