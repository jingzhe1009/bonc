function initProcVersionTable(obj) {
	
    $('#procVersionTable').width('100%').dataTable({
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
        "pageLength": 5,
        "columns": [
        	/*{"title": "全选<input type=\"checkbox\" id=\"checkedAll\" onclick=\"checkedAll()\">", "data": null, "width": "5%","render": function (data, type, row) {
               return '<input type="checkbox" id="'+row.dataSrcAbbr+'-'+row.dataInterfaceNo+'-'+row.importType+'-'+row.batchNo+'" name="items">';
            }},*/
        	{"title": "版本号", "data": "num"},
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
            url: '/model/queryProcVersion',
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