/**
 * 字段明细
 */
var detailModel = {
	init: function(dataInterfaceName){
		debugger;
		var obj = {}
    	obj['dataInterfaceName'] = dataInterfaceName;
    	obj['batchNo'] = localStorage.getItem("batchNo");
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
			{"title": "版本", "data": "flag","render":function(data,type,row){
				if(data=='0'){
					return '上一版本';
				}else{
					return '本次导入版本';
				}
            }},
			{"title": "状态", "data": "flag","width":"10%","render":function(data,type,row){
				if(data=='2'){
					return '<font color="red">修改</font>';
				}else if(data=='3'){
					return '<font color="blue">新增</font>';
				}else if(data=='1'){
					return '无变化';
				}else if(data=='4'){
					return '<font color="red">缺失</font>';
				}else{
					return '-';
				}
            }},
            {"title": "数据源缩写", "data": "dataSrcAbbr","width":"10%"},
            {"title": "接口编号", "data": "dataInterfaceNo","width":"10%" },
            {"title": "接口名", "data": "dataInterfaceName" },
            {"title": "字段编号", "data": "columnNo","width":"10%" },
            {"title": "字段名", "data": "columnName","render":function(data,type,row){
				return compareModel.getData(row,data);
            } },
            {"title": "数据类型", "data": "dataType","width":"10%","render":function(data,type,row){
				return compareModel.getData(row,data);
            } },
            {"title": "格式", "data": "dataFormat","render":function(data,type,row){
				return compareModel.getData(row,data);
            } },
            {"title": "是否非空", "data": "nullable","render":function(data,type,row){
				return compareModel.getData(row,data);
            }},
            {"title": "分隔符", "data": "comma","render":function(data,type,row){
				return compareModel.getData(row,data);
            } },
            {"title": "字段描述", "data": "columnComment","render":function(data,type,row){
				return compareModel.getData(row,data);
            }},
            {"title": "分桶字段", "data": "isbucket","width":"10%","render":function(data,type,row){
				return compareModel.getData(row,data);
            } }
            /*{"title": "生效日期", "data": "sData"}*/
            /*{"title": "失效日期", "data": "eDate" },*/
            ],
        ajax: {
            url: '/col/queryColumnCompare',
            "type": 'GET',
            "data": function (d) { // 查询参数
                return $.extend({}, d, obj);
            }
        },
        "fnDrawCallback": function (oSettings, json) {
            
        }
    });
}