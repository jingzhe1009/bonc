/**
 * 接口列表操作
 */
var compareModel = {
	//初始页面内容显示
	init: function(idx){
		debugger;
		var obj = {}
		obj['dataSrcAbbr'] = idx;
		initCompareTable(obj);
		//按条件查询用
	},
    //按条件查询
    search: function(){
    	var inputs = $('#interfaceContent .input-group .form-control');
        var obj = {};
        for (var i = 0; i < inputs.length; i++) {
            if ($.trim($(inputs[i]).val()) == '') {
                continue;
            }
            obj[$(inputs[i]).attr('data-col')] = $.trim($(inputs[i]).val());
        }
        initCompareTable(obj);
    },
    //查看版本
    version: function(dataSrcAbbr,dataInterfaceNo,dataInterfaceName){
    	$('#versionAlert').modal({'show': 'center', "backdrop": "static"});
		var obj ={};
		obj['dataSrcAbbr']=dataSrcAbbr;
		obj['dataInterfaceNo']=dataInterfaceNo;
		obj['dataInterfaceName']=dataInterfaceName;
		initVersionTable(obj);
    },
    //查看详情
    detail: function(dataSrcAbbr,dataInterfaceNo,dataInterfaceName){
    	$('#detailAlert').modal({'show': 'center', "backdrop": "static"});
    	detailModel.init(dataSrcAbbr,dataInterfaceNo,dataInterfaceName);
    }
}


/**
 * 初始化 接口列表
 */
function initCompareTable(obj) {
	
    $('#compareTable').width('100%').dataTable({
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
            		html += '<span onclick="compareModel.detail(\''+row.dataSrcAbbr+'\',\''+row.dataInterfaceNo+'\',\''+row.dataInterfaceName+'\');" class="btn-sm cm-tblA">字段</span>';
            		html += '</div>';
				return html;
			}},
            {"title": "数据源缩写", "data": "dataSrcAbbr"},
            {"title": "数据接口编号", "data": "dataInterfaceNo"},
            {"title": "数据接口名", "data": "dataInterfaceName"},
            {"title": "数据接口描述", "data": "dataInterfaceDesc","render":function(data,type,row){
            	return '<p style="word-wrap:break-word;">' + data + '</p>';
            }},
            {"title": "数据加载频率", "data": "dataLoadFreq"},
            {"title": "数据加载方式", "data": "dataLoadMthd"},
            {"title": "字段分割符", "data": "filedDelim"},
            {"title": "行分隔符", "data": "lineDelim"},
            {"title": "外表数据库", "data": "extrnlDatabaseName"},
            {"title": "内表数据库", "data": "intrnlDatabaseName"},
            {"title": "外表表名", "data": "extrnlTableName"},
            {"title": "内表表名", "data": "intrnlTableName"},
            {"title": "表类型", "data": "tableType"},
            {"title": "分桶数", "data": "bucketNumber"},
            {"title": "起效日期", "data": "sDate","render": function(data, type, row) {
            	var oDate = new Date(data);
            	var oYear = oDate.getFullYear();
            	var oMonth = oDate.getMonth()+1;
            	var oDay = oDate.getDate();
            	return oYear+"-"+oMonth+"-"+oDay;
            }}
            ],
        ajax: {
            url: '/interface/queryInterface',
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