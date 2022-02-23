var localObj = window.location;
var contextPath = localObj.pathname.split("/")[1];
var basePath = localObj.protocol + "//" + localObj.host + "/"+ contextPath;
(function (window) {
	window.creCommon = _common = {};
    
	function loadHtml(url) {
        $('#pageContent').html('');
        $.pjax({
            url: url,
            type: 'post',
            dataType: 'html',
            container: 'body'
        });
    }
    _common.loadHtml = loadHtml;
    
})(window);

(function (jQuery) {
	
})(jQuery);
$(function () {
	ajax_method();
	$('#menusWrap').on('click','li',function () {
		var idx = $(this).attr("idx");
		$(this).addClass('active').siblings().removeClass('active');
		//_common.loadHtml(basePath+'/newIndex?idx='+idx);
		initInterfaceTable(idx);
	});
    //initPage();
	//initMenu();
	//initModelBaseTable(); // 初始化table
	//initInterfaceTable();
	/*$('#menusWrap > li').click(function () {
		initInterfaceTable();
		//creCommon.loadHtml("http://localhost:8055/bonc/newIndex?idx=POF");
	})*/
});
/* js获取个人中心数据      */
function ajax_method() {
    //创建异步对象
    var xhr = new XMLHttpRequest();
    //设置请求的类型及url
    //post请求一定要添加请求头才行不然会报错        CONTENT-TYPE:application/x-www-form-urlencoded含义是表示客户端提交给服务器文本内容的编码方式 是URL编码，即除了标准字符外，每字节以双字节16进制前加个“%”表示
    xhr.open('get', '/bonc/findDataSource');
    xhr.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
    //发送请求
    xhr.send();
    xhr.onreadystatechange = function () {
        // 这步为判断服务器是否正确响应
    	if (xhr.readyState == 4 && xhr.status == 200) {
    		var result = xhr.responseText;
    		var result = JSON.parse(xhr.responseText);
    		var htmlstr ='';
    		for(var i in result){
    			var idxValue = result[i].dsEname;
		    	if(0==i){
		    		//_common.loadHtml(basePath+'/newIndex?idx='+idxValue);
		    		initInterfaceTable(idxValue);
		    		htmlstr=htmlstr+'<li idx="'+idxValue+'" link="'+basePath+'/newIndex?idx='+idxValue+'" class="active"><div class="menuImg"><img src="/imgs/mainNew/icon_sy_normal.png"/></div><span>数据源'+result[i].dsCname+'</></span></li>';
		    	}else{
		    		htmlstr=htmlstr+'<li idx="'+idxValue+'" link="'+basePath+'/newIndex?idx='+idxValue+'" ><div class="menuImg"><img src="/imgs/mainNew/icon_sy_normal.png"/></div><span>数据源'+result[i].dsCname+'</span></li>';
		    	}
    		}
    		$("#menusWrap").html(htmlstr);
    	}
        
    }
}
function initMenu(){
	$.ajax({
		url:"/bonc/findDataSource",
		type:"GET",
		data:{},
		contentType:"application/json;charset=UTF-8",
		success: function(result){
			var htmlstr ='';
		    for(var i in result){
		    	var idxValue = result[i].dsEname;
		    	if(0==i){
		    		htmlstr=htmlstr+'<li idx="'+idxValue+'" link="'+basePath+'/newIndex?idx='+idxValue+'" class="active"><div class="menuImg"><img src="/imgs/mainNew/icon_sy_normal.png"/></div><span>数据源'+result[i].dsCname+'</span></li>';
		    	}else{
		    		htmlstr=htmlstr+'<li idx="'+idxValue+'" link="'+basePath+'/newIndex?idx='+idxValue+'" ><div class="menuImg"><img src="/imgs/mainNew/icon_sy_normal.png"/></div><span>数据源'+result[i].dsCname+'</span></li>';
		    	}
		    }
		    $("#menusWrap").html(htmlstr);
		}
	});
}

/**
 * 初始化 接口信息
 */
function initInterfaceTable(idx) {
	$("#pageHeader").html('<p>当前位置：<span>接口管理</span></p>');
	localStorage.setItem("idx",idx);
	var obj = {}
	obj['idx'] = idx;
    $('#modelBaseTable').width('100%').dataTable({
        "searching": false,
        "ordering": false,
        "destroy": true,
        "bLengthChange": false,
        "pagingType": "full_numbers",
        "paging": true,
        "info": true,
        "serverSide": true,
        "pageLength": 10,
        "columns": [
        	{"title": "全选<input type=\"checkbox\" id=\"checkedAll\" onclick=\"checkedAll()\">", "data": "dataInterfaceName", "width": "5%","render": function (data, type, row) {
               return '<input type="checkbox" id="'+data+'" name="items">';
            }},
        	/**
            {"title": "序号", "data": "tableEname", "width": "5%","render": function (data, type, row) {
               return '';
            }},*/
            {"title": "数据源缩写", "data": "dataSrcAbbr", "width": "6%"},
            {"title": "数据接口编号", "data": "dataInterfaceNo", "width": "8%"},
            {"title": "数据接口名", "data": "dataInterfaceName", "width": "8%"},
            {"title": "数据接口描述", "data": "dataInterfaceDesc", "width": "8%"},
            {"title": "数据加载频率", "data": "dataLoadFreq", "width": "7%"},
            {"title": "数据加载方式", "data": "dataLoadMthd", "width": "7%"},
            {"title": "字段分割符", "data": "filedDelim", "width": "5%"},
            {"title": "行分隔符", "data": "lineDelim", "width": "5%"},
            {"title": "外表数据库", "data": "extrnlDatabaseName", "width": "5%"},
            {"title": "内表数据库", "data": "intrnlDatabaseName", "width": "5%"},
            {"title": "外表表名", "data": "extrnlTableName", "width": "5%"},
            {"title": "内表表名", "data": "intrnlTableName", "width": "5%"},
            /*{"title": "表类型", "data": "tableType", "width": "5%"},
            {"title": "分桶数", "data": "bucketNumber", "width": "5%"},
            {"title": "起效日期", "data": "sData", "width": "5%"},
            {"title": "失效日期", "data": "eDate", "width": "5%"},*/
            {"title":"操作" ,"data": null,"render": function(data, type, row) {
            	var html = '<div>';
            		html += '<span onclick="detail(\''+row.dataSrcAbbr+'\',\''+row.dataInterfaceNo+'\');" class="btn-sm cm-tblB">查看表结构</span>';
            		html += '</div>';
            	$("#row_"+row.functionId).data("rowData",row);
				return html;
			}}
            ],
        ajax: {
            url: '/bonc/findInterface',
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

function detail(ds,inter){
	//_common.loadHtml(basePath+'/interface?ds='+ds+'&inter='+inter);
	//$('#modelBaseTable').html('');
	//initModelBaseTable();
	$('#modelBaseTable').html("");
	initColumnTable(ds,inter)
}

function initColumnTable(ds,inter) {
	$("#pageHeader").html('<p>当前位置：<span>字段管理</span></p>');
	var obj = {}
	obj['ds'] = ds;
	obj['inter'] = inter;
    $('#modelBaseTable').width('100%').dataTable({
        "searching": false,
        "ordering": false,
        "destroy": true,
        "bLengthChange": false,
        "pagingType": "full_numbers",
        "paging": true,
        "info": true,
        "serverSide": true,
        "pageLength": 10,
        "columns": [
            {"title": "数据源缩写", "data": "dataSrcAbbr", "width": "10%"},
            {"title": "接口编号", "data": "dataInterfaceNo", "width": "10%"},
            {"title": "接口名", "data": "dataInterfaceName", "width": "10%"},
            {"title": "序号", "data": "columnNo", "width": "10%"},
            {"title": "字段名", "data": "columnName", "width": "10%"},
            {"title": "格式", "data": "dataFormat", "width": "10%"},
            {"title": "是否可控", "data": "nullable", "width": "10%"},
            {"title": "空值替代值", "data": "replacenull", "width": "10%"}
            ],
        ajax: {
            url: '/bonc/findColumn',
            "type": 'GET',
            "data": function (d) { // 查询参数
                return $.extend({}, d, obj);
            }
        },
        "fnDrawCallback": function (oSettings, json) {
            
        }
    });
}

function importTable(){
	window.open("/bonc/table",'newwindow','height=400,width=800,top=400,left=400,toolbar=no,menubar=no,scrollbars=no, resizable=no,location=no, status=no'); 
}

function importDictionary(){
	window.open("/bonc/dictionary",'newwindow','height=400,width=800,top=400,left=400,toolbar=no,menubar=no,scrollbars=no, resizable=no,location=no, status=no'); 
}

function create(){
	//$('#modelBaseGroupAlert').modal({'show': 'center', "backdrop": "static"});
	var ids = [ ];
    var chkBoxes = $('#modelBaseTable').find('input:checked');
    if (chkBoxes.length == 0) {
        alert('请至少选择一条记录');
        return false;
    }
    $(chkBoxes).each(function() {
      ids.push($(this).attr('id'));
    });
    console.log(ids);
    var json =JSON.stringify(ids);
    //window.open("/bonc/createSqlPage?ids="+json,'newwindow','height=600,width=1200,top=400,left=400,toolbar=no,menubar=no,scrollbars=no, resizable=no,location=no, status=no');
    $("#ids").val(json);
    $("#ds").val(localStorage.getItem("idx"));
    form.action = '/bonc/createSqlPage';
	form.submit();
}
function checkedAll() {
	console.log($('#modelBaseTable').DataTable().rows);
	//.row(curRow).data();
	var checked =$("#checkedAll").prop("checked");
	console.log(checked)
	$('[name=items]:checkbox').prop("checked", checked ); //所有checkbox跟着全选的checkbox走。
}



