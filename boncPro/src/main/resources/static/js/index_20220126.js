zUI.select.init(0);
$(function () {
	initMenu();
	$(".tab-content").hide();
});

/**
 * 加载数据源菜单
 * @returns
 */
function initMenu(){
	var localObj = window.location;
	var contextPath = localObj.pathname.split("/")[1];
	var basePath = localObj.protocol + "//" + localObj.host + "/"+ contextPath;
	$.ajax({
		url:"/bonc/queryDataSrc",
		type:"GET",
		data:{},
		contentType:"application/json;charset=UTF-8",
		success: function(result){
			var htmlstr ='';
			var data = result.data;
			for(var i in data){
    			var idx = data[i].dataSrcAbbr;
    			var desc = data[i].dataSrcDesc;
    			var active = "";
		    	if(i==0){
		    		localStorage.setItem("idx",idx);
		    		localStorage.setItem("desc",desc);
		    		changeTab(8);//0:目录扫描函数配置
		    		active = "active";
		    	}
		    	htmlstr=htmlstr+'<li idx="'+idx+'" desc="'+desc+'" link="'+basePath+'/newIndex?idx='+idx+'" class="'+active+'"><a href=#>'+desc+'('+idx+')'+'</a></li>';
    		}
			$("#menusWrap").html(htmlstr);
		}
	});
	
	//监听切换菜单数据源列表
	$('#menusWrap').on('click','li',function () {
		var idx = $(this).attr("idx");
		var desc = $(this).attr("desc");
		//$(this).addClass('active').siblings().removeClass('active');
		$("#leftMenu li").removeClass('active');
		$(this).addClass('active');
		localStorage.setItem("idx",idx);
		localStorage.setItem("desc",desc);
		changeTab(8);//0:数据源
	});
	
	//监听目录扫描函数配置
	$('#configList').on('click','li',function () {
		$("#funcList li").removeClass('active');
		$("#configList li").removeClass('active');
		$(this).addClass('active');
		var id = $(this).attr("id");
		var desc = $(this).attr("desc");
		var param = $(this).attr("param");
		$("#funcParam").html(param);
		$("#funcParamDesc").html(desc);
		$("#funcCode").val(id);
	});
	//监听文件预处理函数配置
	$('#configList2').on('click','li',function () {
		//alert(1);
		$("#funcList2 li").removeClass('active');
		$("#configList2 li").removeClass('active');
		$(this).addClass('active');
		var funcName = $(this).attr("list-tran-data");
		var funcParam = $(this).attr("list-param");
		var funcParamDesc = $(this).attr("list-desc");
		var funcDesc = $(this).attr("list-reg-desc");
		$("#name2").val(funcName);
		$("#param2").val(funcParam);
		$("#desc2").val(funcParamDesc);
		$("#regDesc2").val(funcDesc);
	});
	
	//监听接口双击
	/*$("#interfaceTable").on('dblclick','tr',function(){
		var table=$("#interfaceTable").DataTable();
		var data=table.row(this).data();
		console.log(data);
		interfaceModel.detail(data.dataSrcAbbr,data.dataInterfaceNo);
	});*/
	
	//监听词根管理
	$('#dicWrap').on('click','li',function () {
		$(".tab-content").hide();
		$("#tabContainer").hide();
		var idx = $(this).attr("idx");
		//$(this).addClass('active').siblings().removeClass('active');
		$("#leftMenu li").removeClass('active');
		$(this).addClass('active');
		if(idx=='table'){
			dicTableModel.init();
			$("#dicTableContent").show();
		}else if(idx=='col'){
			dicColModel.init();
			$("#dicColContent").show();
		}
	});
	
	//监听基本信息
	$('#baseWrap').on('click','li',function () {
		$(".tab-content").hide();
		$("#tabContainer").hide();
		var idx = $(this).attr("idx");
		//$(this).addClass('active').siblings().removeClass('active');
		$("#leftMenu li").removeClass('active');
		$(this).addClass('active');
		if(idx=='ds'){
			dataSrcModel.init();
			$("#configDataSrc").show();
		}else if(idx=='func'){
			changeRegisterTab("1");
			$("#funcTab li[tab-id='1']").attr("style","display:block");
			$("#funcTab li[tab-id='2']").attr("style","display:block");
			$("#funcTab li[tab-id='3']").attr("style","display:none");
			$("#funcContent").show();
			$("#addFunc").show();
			$("#addProc").hide();
		}else if(idx=='log'){
			logModel.init();
			$("#logContent").show();
		}else if(idx=='proc'){
			var obj = {};
			obj['useType'] = 3;
			funcModel.initProc(obj);
			$("#regProcContent").show();
		}else if(idx=='excel'){
			$("#excelContent").show();
		}else if(idx=='info'){
			$("#dataSrcInfoContent").show();
			var obj={}
			$('#dataSrcInfoTable').width('100%').dataTable({
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
		            {"title": "文件名", "data": "fileName"},
		            {"title": "文件作用", "data": "fileFunc"},
		            {"title": "文件存放位置", "data": "fileLocal"},
		            {"title": "执行命令", "data": "cmd"}
		            ],
		        ajax: {
		            url: '/bonc/queryDataSrcInfo',
		            "type": 'GET',
		            "data": function (d) { // 查询参数
		            	//debugger;
		                return $.extend({}, d, obj);
		            }
		        },
		        "fnDrawCallback": function (oSettings, json) {
		            
		        }
		    });
			
		}
	});
	
	// 监听切换数据源工作流程tab选项卡
    $('#authorityTab li').click(function () {
        var tabId = $(this).attr('tab-id');
        var idx = localStorage.getItem("idx");
        changeTab(tabId,idx);
    });
    
    // 监听切换函数登记tab选项卡
    $('#funcTab li').click(function () {
        var tabId = $(this).attr('tab-id');
        changeRegisterTab(tabId);
    });
	
	//监听右上角菜单点击事件
    $('#sysMenuList').on('click','li',function () {
    	//alert(1);
    });
    
}
//切换函数登记标签
function changeRegisterTab(tabId){
	localStorage.setItem("funcTab",tabId);
	$('#funcTab li').removeClass('active');
	$("#funcTab li[tab-id='" + tabId + "']").addClass('active');
	var obj = {};
	obj['useType'] = tabId;
	funcModel.init(obj);
}
//切换数据源工作流程标签
function changeTab(tabId){
	var idx = localStorage.getItem("idx");
	var desc = localStorage.getItem("desc");
	$("#checkedAll").prop("checked", false );
    $(".tab-content").hide();
    $("#tabContainer").show();
	//标签页选中样式
    $('#authorityTab li').removeClass('active');
    $("#authorityTab li[tab-id='" + tabId + "']").addClass('active');
    
	if (tabId == '0') {
		$("#regDesc").val('');
		$("#desc").val('');
		$("#param").val('');
		$("#configFuncContent").show();
    	$("#pageHeader").html('<p>当前位置：<span>目录扫描函数配置</span></p>');
    	localStorage.setItem("use_type","1");
    	localStorage.setItem("idx",idx);
    	funcModel.queryConfig(idx,1);
    } else if (tabId == '1') {
    	$("#desc").val('');
		$("#param").val('');
    	$("#configFuncContent2").show();
    	$("#pageHeader").html('<p>当前位置：<span>文件预处理函数配置</span></p>');
    	localStorage.setItem("use_type","2");
    	localStorage.setItem("idx",idx);
    	funcModel.queryConfig(idx,2);
    } else if (tabId == '2') {
    	$("#interfaceContent").show(idx);
    	interfaceModel.init(idx);
    	$("#pageHeader").html('<p>当前位置：<span>数据接口配置</span></p>');
    } else if (tabId == '3') {
    	$("#colContent").show();
    	columnModel.init(idx);
    	$("#pageHeader").html('<p>当前位置：<span>数据接口字段配置</span></p>');
    } else if (tabId == '4') {
    	
    	$("#pageHeader").html('<p>当前位置：<span>数据建模</span></p>');
    	dataModelModel.init(idx);
    	$("#dataModelContent").show(idx);
    } else if (tabId == '5') {
    	$("#pageHeader").html('<p>当前位置：<span>数据算法加载</span></p>');
    	procModel.init(idx);
    	$("#procContent").show();
    } else if(tabId == '8'){
		$("#baseContent").show();
		$('.base_title').html('当前数据源:'+desc+"("+idx+")");
    	$("#pageHeader").html('<p>当前位置：<span>配置信息</span></p>');
	} else if(tabId == '9'){
		$("#configFuncContent3").show();
		/*$('.base_title').html('当前数据源:'+localStorage.getItem("dataSrcDesc")+"("+localStorage.getItem("idx")+")");*/
    	$("#pageHeader").html('<p>当前位置：<span>预处理函数配置</span></p>');
	}
}

//定义success主题提示消息
var successMessager = new $.zui.Messager({
    type: 'success', // 定义颜色主题
    placement: 'center', // 位置
    icon: 'ok-sign',
    time: 2000
});

// 定义danger主题提示消息
var failedMessager = new $.zui.Messager({
    type: 'danger',
    placement: 'center',
    icon: 'exclamation-sign',
    time: 2000
});

// 定义warning主题提示消息
var warningMessager = new $.zui.Messager({
    type: 'warning', // 定义颜色主题
    placement: 'center', // 位置
    icon: 'info-sign',
    time: 2000
});

//确认弹框
var confirmAlert = {
    /**
     * @param msg: 弹框提示信息
     * @param confirmHandler: 确认handler
     * @param cancelHandler: 取消handler
     */
    show: function (msg, confirmHandler, cancelHandler) {
        $('#msgText').text(msg ? msg : '');
        if (confirmHandler) {
            $('#msgConfirm').unbind().on('click', function () {
                confirmHandler();
            });
        }
        if (cancelHandler) {
            $('#msgCancel').unbind().on('click', function () {
                cancelHandler();
            });
        }
        $('#msgAlertModal').modal({'show': 'center', "backdrop": "static"});
    },

    hide: function () {
        $('#msgAlertModal').modal('hide');
    }
}

function showTrain(){
	$('#trainAlert').modal({'show': 'center', "backdrop": "static"});
}

function showParam(){
	
	key = '原系统数据文件名';
	value='yss';
	var html = '';
	var param = $("#funcParam").text();
	var p = param.substring(1,param.length-1);
	var ps = p.split(',');
	for(var i in ps){
		console.log(ps[i]);
		var t =ps[i];
		var pattern = new RegExp("[']");
	    if(pattern.test(t))
	    	continue;
		html += getInput(t,t);
	}
	$("#paramList").html("");
	$("#paramList").html(html);
	$('#paramAlert').modal({'show': 'center', "backdrop": "static"});
	
}
function getInput(key,value){
	
	var text ='<div class="form-group"><label class="col-xs-3 col-sm-3 col-md-3 col-lg-3"><i class="mustIcon">*</i>'+key+'</label><div class="col-xs-6 col-sm-6 col-md-6 col-lg-6"><input name="'+value+'param" type="text" class="form-control" ></div></div>';
	return text;
}
