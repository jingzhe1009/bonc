/**
 * 字段明细
 */
var detailModel = {
	init: function(dataSrcAbbr,dataInterfaceNo,dataInterfaceName){
		debugger;
		var obj = {}
		obj['dataSrcAbbr'] = dataSrcAbbr;
    	obj['dataInterfaceNo'] = dataInterfaceNo;
    	obj['dataInterfaceName'] = dataInterfaceName;
		initDetailTable(obj);
	}
}


/**
 * 详情列表
 * @param obj
 * @returns
 */
function initDetailTable(obj) {
    $('#detailTable').width('100%').dataTable({
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
            {"title": "数据源缩写", "data": "dataSrcAbbr","width":"10%"},
            {"title": "接口编号", "data": "dataInterfaceNo","width":"10%" },
            /*{"title": "接口名", "data": "dataInterfaceName" },*/
            {"title": "字段编号", "data": "columnNo","width":"10%" },
            {"title": "字段名", "data": "columnName" },
            {"title": "数据类型", "data": "dataType","width":"10%" },
            /*{"title": "格式", "data": "dataFormat" },*/
            /*{"title": "是否非空", "data": "nullable"},*/
            /*{"title": "分隔符", "data": "comma" },*/
            {"title": "字段描述", "data": "columnComment"},
            {"title": "分桶字段", "data": "isbucket","width":"10%" },
            /*{"title": "生效日期", "data": "sData"}*/
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