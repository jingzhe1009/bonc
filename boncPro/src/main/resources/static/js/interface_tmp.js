//版本
function version(dataSrcAbbr,dataInterfaceNo,dataInterfaceName){
	$('#versionAlert').modal({'show': 'center', "backdrop": "static"});
	var obj ={};
	obj['dataSrcAbbr']=dataSrcAbbr;
	obj['dataInterfaceNo']=dataInterfaceNo;
	obj['dataInterfaceName']=dataInterfaceName;
	initVersionTable(obj);
}
/**
 * 初始化 接口列表
 */
function initInterfaceTable(obj) {
	
    $('#interfaceTable').width('100%').dataTable({
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
                return '<input type="checkbox" id="'+row.dataSrcAbbr+'-'+row.dataInterfaceNo+'-'+row.importType+'-'+row.batchNo+'-'+row.dataInterfaceName+'" name="items">';
            }},
            {"title":"操作" ,"data": null,"width":"5%","render": function(data, type, row) {
             	var html = '<div><span onclick="version(\''+row.dataSrcAbbr+'\',\''+row.dataInterfaceNo+'\',\''+row.dataInterfaceName+'\')" class="btn-sm cm-tblA">版本</span></div>';
 				return html;
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
            {"title": "数据接口名", "data": "dataInterfaceName"},
            {"title": "数据接口描述", "data": "dataInterfaceDesc"},
            {"title": "数据加载频率", "data": "dataLoadFreq"},
            {"title": "数据加载方式", "data": "dataLoadMthd"},
            {"title": "字段分割符", "data": "filedDelim"},
            {"title": "行分隔符", "data": "lineDelim"},
            {"title": "外表数据库", "data": "extrnlDatabaseName"},
            {"title": "内表数据库", "data": "intrnlDatabaseName"},
            {"title": "外表表名", "data": "extrnlTableName"},
            {"title": "内表表名", "data": "intrnlTableName"},
            {"title": "表类型", "data": "tableType"},
            {"title": "分桶数", "data": "bucketNumber"}
            ],
        ajax: {
            url: '/interface/queryInterfaceTmp',
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