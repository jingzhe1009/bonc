function tmpInfoCheck_bak(){
	$("#syContent").hide();
	$("#importContent").show();
	var obj ={};
	obj['batchNo'] = localStorage.getItem("batchNo");
	obj['dataSrcAbbr'] =  localStorage.getItem("idx");
	//for (var i=1;i<=3;i++) {
		// debugger
		//changeTab2(i, obj);
	//}
	changeTab2(1,obj);	 //默认显示第一个tab页
	// 监听切换数据源工作流程tab选项卡
	$('#authorityTab2 li').click(function () {
		var tabId = $(this).attr('tab-id');
		changeTab2(tabId,obj);
	});
	//加载信息确认table
	//var idx = localStorage.getItem("idx");
	//compareModel.init(idx);
	//dataModelModel.init(idx);
}

//切换数据源工作流程标签
function changeTab2_bak(tabId,obj){
	$("#checkedAll").prop("checked", false );
    $(".tab-content2").hide();
    $("#tabContainer2").show();
	//标签页选中样式
    $('#authorityTab2 li').removeClass('active');
    $("#authorityTab2 li[tab-id='" + tabId + "']").addClass('active');

	if (tabId == '1') {
		$("#interfaceContent").show(obj);
		// interfaceModel.init(idx);
		debugger
		initInterfaceTable(obj);
		// $("#inter_dataSrcAbbr").val(idx);
		$("#pageHeader").html('<p>当前位置：<span>数据接口配置</span></p>');
	} else if (tabId == '2') {
		$("#colContent").show(obj);
		// columnModel.init(idx);
		initColumnTable(obj);
		// $("#column_dataSrcAbbr").val(idx);
		$("#pageHeader").html('<p>当前位置：<span>数据接口字段配置</span></p>');
	} else if (tabId == '3') {
		$("#procContent").show(obj);
		// procModel.init(idx);
		initProcTable(obj);
		// $("#proc_dataSrcAbbr").val(idx);
		$("#pageHeader").html('<p>当前位置：<span>数据算法加载</span></p>');
	}

}
