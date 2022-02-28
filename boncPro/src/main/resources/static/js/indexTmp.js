

//切换数据源工作流程标签
function changeTab2(tabId,obj){
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
