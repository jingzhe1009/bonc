/**
 * 字段列表
 * @param obj
 * @returns
 */
function initColVersionTable(obj) {
    $('#colVersionTable').width('100%').dataTable({
    	//默认搜索组件
        "searching": false,
        //排序功能
        "ordering": false,
        "destroy": true,
        // 自动列宽
        "autoWidth": false,
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
        	{"title": "版本号", "data": "num"},
            {"title": "数据源缩写", "data": "dataSrcAbbr"},
            {"title": "接口编号", "data": "dataInterfaceNo" },
            /*{"title": "接口名", "data": "dataInterfaceName" },*/
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
            }},
            {"title": "失效日期", "data": "eDate","render": function(data, type, row) {
            	var oDate = new Date(data);
            	var oYear = oDate.getFullYear();
            	var oMonth = oDate.getMonth()+1;
            	var oDay = oDate.getDate();
            	return oYear+"-"+oMonth+"-"+oDay;
            }}
            /*{"title": "生效日期", "data": "sDate"}*/
            /*{"title": "失效日期", "data": "eDate" },*/
            /*{"title":"操作" ,"data": null,"render": function(data, type, row) {
            	var html = '<div>';
            		html += '<span onclick="columnModel.version(2,\''+row.dataSrcAbbr+'|'+row.dataInterfaceNo+'|'+row.columnNo+'\');" class="btn-sm cm-tblA">版本</span>';
            		html += '<span onclick="columnModel.add(2,this)" class="btn-sm cm-tblA">修改</span>';
            		html += '<span onclick="columnModel.del(\''+row.dataSrcAbbr+'\',\''+row.dataInterfaceNo+'\',\''+row.columnNo+'\');" class="btn-sm cm-tblC">删除</span>';
            		html += '</div>';
            	$("#row_"+row.functionId).data("rowData",row);
				return html;
			}}*/
            ],
        ajax: {
            url: '/col/queryColumnVersion',
            "type": 'GET',
            "data": function (d) { // 查询参数
                return $.extend({}, d, obj);
            }
        },
        "fnDrawCallback": function (oSettings, json) {
            
        }
    });
}