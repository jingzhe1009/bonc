/**
 * 初始化临时表
 */

function initInterfaceTable(obj) {

$('#interfaceTable').width('100%').dataTable({
    //默认搜索组件
    "searching": true,
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
    "pageLength": 10,
    "columns": [
        /*{"title": "<input type=\"checkbox\" id=\"checkedAll\" >", "data": null, "width": "5%","render": function (data, type, row) {
                return '<input type="checkbox" id="'+row.dataSrcAbbr+'-'+row.dataInterfaceNo+'-'+row.importType+'-'+row.batchNo+'-'+row.dataInterfaceName+'" name="items">';
            }},
        {"title":"操作" ,"data": null,"width":"5%","render": function(data, type, row) {
                var html = '<div><span onclick="version(\''+row.dataSrcAbbr+'\',\''+row.dataInterfaceNo+'\',\''+row.dataInterfaceName+'\')" class="btn-sm cm-tblA">版本</span></div>';
                return html;
            }},*/
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

function initColumnTable(obj) {
$('#columnTable').width('100%').dataTable({
    //默认搜索组件
    "searching": true,
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
    "pageLength": 10,
    "columns": [
        /*{"title": "<input type=\"checkbox\" id=\"checkedAll\"> ", "data": null, "width": "5%","render": function (data, type, row) {
                return '<input type="checkbox" id="'+row.dataSrcAbbr+'-'+row.dataInterfaceNo+'-'+row.importType+'-'+row.batchNo+'-'+row.columnNo+'-'+row.dataInterfaceName+'" name="itemsColumn">';
            }},
        {"title":"操作" ,"data": null,"render": function(data, type, row) {
                var html = '<div>';
                html += '<span onclick="version(\''+row.dataSrcAbbr+'\',\''+row.dataInterfaceNo+'\',\''+row.columnNo+'\',\''+row.dataInterfaceName+'\')" class="btn-sm cm-tblA">版本</span>';
                html += '</div>';
                $("#row_"+row.functionId).data("rowData",row);
                return html;
            }},*/
        {"title": "导入类型", "data": "importType","render":function(data,type,row){
                if(data=='1'){
                    return '<font color="blue">新增</font>';
                }else if(data=='2'){
                    return '<font color="red">修改</font>';
                }
                return '<font color="black">未变化</font>';
            }},
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
        {"title": "是否为主键", "data": "iskey" },
        {"title": "是否有效字段", "data": "isvalid" },
        {"title": "增量字段", "data": "incrementfield" },
        /*{"title": "生效日期", "data": "sDate"}*/
        /*{"title": "失效日期", "data": "eDate" },*/

    ],
    ajax: {
        url: '/col/queryColumnTmp',
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

function initProcTable(obj) {

$('#procTable').width('100%').dataTable({
    //默认搜索组件
    "searching": true,
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
    "pageLength": 10,
    "columns": [
        /*{"title": "<input type=\"checkbox\" id=\"checkedAll\" >", "data": null, "width": "5%","render": function (data, type, row) {
                return '<input type="checkbox" id="'+row.dataSrcAbbr+'-'+row.dataInterfaceNo+'-'+row.importType+'-'+row.batchNo+'" name="itemsProc">';
            }},*/
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
        {"title": "存储过程", "data": "procName"}
        /*{"title": "起效日期", "data": "sDate"},
        {"title": "失效日期", "data": "eDate"},*/
       /* {"title":"操作" ,"data": null,"render": function(data, type, row) {
                var html = '<div>';
                html += '<span onclick="version(\''+row.dataSrcAbbr+'\',\''+row.dataInterfaceNo+'\')" class="btn-sm cm-tblA">版本</span>';
                html += '</div>';
                $("#row_"+row.functionId).data("rowData",row);
                return html;
            }}*/
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