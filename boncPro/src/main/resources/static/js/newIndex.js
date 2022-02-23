zUI.select.init();
$(function () {
	initMenu();
	$(".tab-content").hide();
});
function getResult(type){
//	zUI.dialog.alert('<pre>'+JSON.stringify(zUI.select.getListTranData('#tran1'),null,2)+'</pre>');
	debugger;
//	console.log(zUI.select.getListTranData('#tran1'));
	var obj = zUI.dom.get("#tran1");
    if(zUI.dom.hasClass(obj,'zUI-list-tran')){
        var lis = obj.querySelectorAll('.list-box .left li');
        var ris = obj.querySelectorAll('.list-box .right li');
        var l = [],r=[];
        zUI.util.each(lis,function(i,o){
            l.push(o.getAttribute('list-tran-data'));
        });
        zUI.util.each(ris,function(i,o){
            r.push(o.getAttribute('list-tran-data'));
        });
        console.log(r);
		var url = "/bonc/saveFunc";
		var param = {};
		param.tables = r;
		ds=localStorage.getItem("idx");
		param.dataSrcAbbr = ds;
		param.dbType = type;
		var json = JSON.stringify(param);
		console.log(json);
		$.ajax({
			url:url,
			type:"post",
			data:json,
			contentType:"application/json;charset=UTF-8",
			success:function(data){
				console.log(data);
				//alert(data);
				/*zUI.dialog.alert('<pre>'+data+'</pre>')*/
			}
		})
    }
}

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
    			var dataSrcDesc = data[i].dataSrcDesc;
    			var active = "";
		    	if(i==0){
		    		localStorage.setItem("idx",idx);
		    		changeTab(0,idx);//0:目录扫描函数配置
		    		active = "active";
		    	}
		    	htmlstr=htmlstr+'<li idx="'+idx+'" link="'+basePath+'/newIndex?idx='+idx+'" class="'+active+'"><a href="#"><i class="icon icon-circle-blank"></i>'+dataSrcDesc+'</></a></li>';
    		}
			$("#menusWrap").append(htmlstr);
		}
	});
	
	
	
	//监听切换菜单数据源列表
	$('#menusWrap').on('click','li',function () {
		var idx = $(this).attr("idx");
		//$(this).addClass('active').siblings().removeClass('active');
		$("#leftMenu li").removeClass('active');
		$(this).addClass('active');
		localStorage.setItem("idx",idx);
		changeTab(0,idx);//0:数据源
	});
	//监听词根管理
	$('#dicWrap').on('click','li',function () {
		$(".tab-content").hide();
		$("#tabContainer").hide();
		var idx = $(this).attr("idx");
		//$(this).addClass('active').siblings().removeClass('active');
		$("#leftMenu li").removeClass('active');
		$(this).addClass('active');
		$("#pageHeader").html('<p>当前位置：<span>词根管理</span></p>');
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
		$("#pageHeader").html('<p>当前位置：<span>基本信息</span></p>');
		if(idx=='ds'){
			dataSrcModel.init();
			$("#configDataSrc").show();
		}else if(idx=='func'){
			changeRegisterTab("1");
			$("#funcContent").show();
		}else if(idx=='log'){
			logModel.init();
			$("#logContent").show();
		}
	});
	
	// 监听切换tab选项卡
    $('#authorityTab li').click(function () {
        var tabId = $(this).attr('tab-id');
        var idx = localStorage.getItem("idx");
        changeTab(tabId,idx);
    });
    
    // 监听切换tab选项卡
    $('#funcTab li').click(function () {
        var tabId = $(this).attr('tab-id');
        changeRegisterTab(tabId);
    });
	
	//监听右上角菜单点击事件
    $('#sysMenuList').on('click','li',function () {
    	//alert(1);
    });
    
}

function changeRegisterTab(tabId){
	localStorage.setItem("funcTab",tabId);
	$('#funcTab li').removeClass('active');
	$("#funcTab li[tab-id='" + tabId + "']").addClass('active');
	var obj = {};
	obj['useType'] = tabId;
	funcModel.init(obj);
}

function changeTab(tabId,idx){
	$("#checkedAll").prop("checked", false );
    $(".tab-content").hide();
    $("#tabContainer").show();
	//标签页选中样式
    $('#authorityTab li').removeClass('active');
    $("#authorityTab li[tab-id='" + tabId + "']").addClass('active');
	if (tabId == '0') {
		$("#configFuncContent").show();
    	$("#pageHeader").html('<p>当前位置：<span>目录扫描函数配置</span></p>');
    	/*$("#pageHeader").html('<p>当前位置：<span>数据源管理</span></p>');
    	dataSrcModel.initShow(idx);
    	$("#dataSrcContent").show();*/
    	var obj ={};
    	obj['dataSrcAbbr']=idx;
    	obj['useType']=1;
    	$.ajax({
            url: '/bonc/queryConfig',
            type: 'POST',
            data: obj,
            success: function (data) {
                //successMessager.show('保存成功');
            	var htmlstr ='';
            	var list = data.list;
				for(var i in list){
	    			var idx = list[i].funcName;
			    	htmlstr=htmlstr+'<li list-tran-data="'+idx+'" >'+idx+'</li>';
	    		}
				//$("#funcList").append(htmlstr);
            	$("#configList").html(htmlstr);
            	$("#pText").html(data.word);
            	/*$.ajax({
    				url:"/bonc/queryFunc",
    				type:"GET",
    				data:{"useType":"1"},
    				contentType:"application/json;charset=UTF-8",
    				success: function(result){
    					var htmlstr ='';
    					var data = result.data;
    					for(var i in data){
    		    			var idx = data[i].funcName;
    		    			var dataSrcDesc = data[i].funcDesc;
    		    			var active = "";
    				    	if(i==0){
    				    		active = "active";
    				    	}
    				    	htmlstr=htmlstr+'<li list-tran-data="'+idx+'" >'+idx+'</li>';
    		    		}
    					$("#funcList").html(htmlstr);
    					
    				}
    			});*/
            },
            error: function (data) {
            	//failedMessager.show('保存失败');
            }
        });
    } else if (tabId == '1') {
    	$("#configFuncContent").show();
    	$("#pageHeader").html('<p>当前位置：<span>文件预处理函数配置</span></p>');
    	//$("#pageHeader").html('<p>当前位置：<span>数据采集</span></p>');
    	var obj ={};
    	obj['dataSrcAbbr']=idx;
    	obj['useType']=2;
    	$.ajax({
    		url: '/bonc/queryConfig',
            type: 'POST',
            data: obj,
            success: function (data) {
                //successMessager.show('保存成功');
            	$("#configList").html(data.list);
            	$("#pText").html(data.word);
            	$.ajax({
					url:"/bonc/queryFunc",
					type:"GET",
					data:{"useType":"1"},
					contentType:"application/json;charset=UTF-8",
					success: function(result){
						var htmlstr ='';
						var data = result.data;
						for(var i in data){
			    			var idx = data[i].funcName;
			    			var dataSrcDesc = data[i].funcDesc;
			    			var active = "";
					    	if(i==0){
					    		active = "active";
					    	}
					    	htmlstr=htmlstr+'<li list-tran-data="'+idx+'" >'+idx+'</li>';
			    		}
						$("#funcList").html(htmlstr);
						
					}
            	});
            },
            error: function (data) {
            	//failedMessager.show('保存失败');
            }
        });
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
    }
}

/**
 * 全选
 * @returns
 */
function checkedAll() {
	var checked =$("#checkedAll").prop("checked");
	console.log(checked)
	$('[name=items]:checkbox').prop("checked", checked ); //所有checkbox跟着全选的checkbox走。
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
//导入词根
function importDictionary(){
	window.open("/bonc/dictionary",'newwindow','height=400,width=800,top=400,left=400,toolbar=no,menubar=no,scrollbars=no, resizable=no,location=no, status=no'); 
}

//将时间戳格式化 
function getMyDate(time){  
	if(typeof(time)=="undefined"){
	    return "";
	}
	var oDate = new Date(time),  
	oYear = oDate.getFullYear(),  
	oMonth = oDate.getMonth()+1,  
	oDay = oDate.getDate(),  
	/*oHour = oDate.getHours(),  
	oMin = oDate.getMinutes(),  
	oSen = oDate.getSeconds(), */ 
	oTime = oYear +'-'+ getzf(oMonth) +'-'+ getzf(oDay) +' ';
	/*+ getzf(oHour) +':'+ getzf(oMin) +':'+getzf(oSen);//最后拼接时间  
*/	return oTime;  
};

 //补0操作,当时间数据小于10的时候，给该数据前面加一个0  
function getzf(num){  
    if(parseInt(num) < 10){  
        num = '0'+num;  
    }  
    return num;  
}
