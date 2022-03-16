/**
 * 接口列表操作
 */
var historyModel = {
	//初始页面内容显示
	init: function(idx){
		var obj = {}
		obj['dataSrcAbbr'] = idx;
		initHistoryTable(obj);
		//按条件查询用
		//$("#inter_dataSrcAbbr").val(idx);
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
        initInterfaceTable(obj);
    },
    //查看版本
    version: function(dataSrcAbbr,dataInterfaceNo,dataInterfaceName){
    	$('#versionAlert').modal({'show': 'center', "backdrop": "static"});
		var obj ={};
		obj['dataSrcAbbr']=dataSrcAbbr;
		obj['dataInterfaceNo']=dataInterfaceNo;
		obj['dataInterfaceName']=dataInterfaceName;
		initVersionTable(obj);
    	//interfaceVersionTableModal.initPage(versionType,key);
    },
    //查看详情
    detail: function(dataSrcAbbr,dataInterfaceNo,dataInterfaceName){
    	$('#detailAlert').modal({'show': 'center', "backdrop": "static"});
    	detailModel.init(dataSrcAbbr,dataInterfaceNo,dataInterfaceName);
    },
    setIndexParam:function(idx){
		var param = {};
		param.dataSrcAbbr = idx;
		var json = JSON.stringify(param);
		console.log(json);
		$.ajax({
			url:"/interface/queryLastRecord",
			type:"POST",
			data:json,
			contentType:"application/json;charset=UTF-8",
			success: function(result){
				console.log(result);
				if(result.state=='success'){
					var data = result.data;
					$("#shouye_version").text(data.needVrsnNbr);
					$("#shouye_sum").text(data.intfTot);
					$("#shouye_add").text(data.intfNew);
					$("#shouye_edit").text(data.intfAlt);
					$("#shouye_date").text(data.exptDate);
				}else{
					$("#shouye_version").text('v1.0');
					$("#shouye_sum").text('0');
					$("#shouye_add").text('0');
					$("#shouye_edit").text('0');
					$("#shouye_date").text('2022/01/11 11:11:11');
				}
			}
		});
	},
	//一键导入
	yjdr:function() {
		$("#syContent").hide();
		$("#importContent").show();
	}
}


/**
 * 初始化 接口列表
 */
function initHistoryTable(obj) {
	
    $('#historyTable').width('100%').dataTable({
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
            {"title": "版本", "data": "needVrsnNbr"},
            {"title": "流水号", "data": "exptSeqNbr"},
            {"title": "导入文件名称", "data": "exptFileName"},
            {"title": "接口总数", "data": "intfTot"},
            {"title": "新增", "data": "intfNew"},
            {"title": "修改", "data": "intfAlt"},
            {"title": "导入时间", "data": "exptDate"},
            /*{"title": "失效日期", "data": "eDate","hidden":"true"},*/
            {"title":"操作" ,"data": null,"render": function(data, type, row) {
            	var html = '<div>';
            		/*html += '<span onclick="" class="btn-sm cm-tblA"><img src="imgs/index/history.png">历史信息</span>';*/
            		html += '<span onclick="" class="btn-sm cm-tblA"><img src="imgs/index/down.png">生成上线脚本</span>';
            		html += '</div>';
            	//$("#row_"+row.functionId).data("rowData",row);
				return html;
			}}
            ],
        ajax: {
            url: '/interface/queryRecord',
            "type": 'GET',
            "data": function (d) { // 查询参数
                return $.extend({}, d, obj);
            }
        },
        "fnDrawCallback": function (oSettings, json) {
            
        }
    });
}