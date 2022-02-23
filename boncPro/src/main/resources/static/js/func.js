/**
 * 函数登记
 */
var funcModel = {
	//初始页面内容显示
	init: function(obj){
		obj['useType']=localStorage.getItem("funcTab");
		initFuncTable(obj);
		//按条件查询用
		//$("#inter_dataSrcAbbr").val(idx);
	},
	initProc: function(obj){
		obj['useType']='3';
		initRegProcTable(obj);
		//按条件查询用
		//$("#inter_dataSrcAbbr").val(idx);
	},
	//弹出新增/修改弹窗
    addProc: function (saveType,$this) {
        var detail = {};
        if ($this) {
            var curRow = $this.parentNode.parentNode;
            detail = $('#regProcTable').DataTable().row(curRow).data();
        }
        $('#procRegisterAlert form')[0].reset();
        $('#procRegisterAlert').attr('saveType', saveType).modal({'show': 'center', "backdrop": "static"});
        if(saveType=='1'){
        	$("#procRegisterAlert .form-control[col-name='funcName']").attr("readOnly",false);
        }else if(saveType=='2'){
        	$("#procRegisterAlert .form-control[col-name='funcName']").attr("readOnly",true);
        	funcModel.showDataProc(detail); // 数据回显
        }
    },
 // 回显数据
    showDataProc: function (detail) {
        if (detail) {
            var data = detail ? detail : {};
            for (var key in data) {
                var target = $("#procRegisterAlert .form-control[col-name='" + key + "']");
                if (target.length > 0) {
                    $(target).val(data[key]);
                }
            }
        }
    },
    // 获取组表单数据
    getDataProc: function () {
        var obj = {};
        var inputs = $('#procRegisterAlert form .form-control');
        for (var i = 0; i < inputs.length; i++) {
            obj[$(inputs[i]).attr('col-name')] = $(inputs[i]).val();
        }
        var useType = '3';
        obj['useType'] = useType;
        return obj;
    },
    // 新增/修改数据
    saveProc: function () {
    	debugger;
        // 表单验证
        if (!$('#procRegisterAlert form').isValid()) {
            return;
        }
    	var saveType = $('#procRegisterAlert').attr('saveType'); // 1新增 2	修改
    	var url = '/func/createFunc';
    	if(saveType==2){
    		url = '/func/editFunc';
    	}
    	var obj = funcModel.getDataProc();
        $.ajax({
            url: url,
            type: 'POST',
            data: obj,
            success: function (data) {
            	if(data.message=='保存成功'){
            		successMessager.show('保存成功');
                	$('#procRegisterAlert').modal('toggle', 'center');
                	var obj = {};
                    obj['useType']=data.useType;
                    if(data.useType=='3'){
                    	funcModel.initProc(obj);
                    }else{
                    	funcModel.init(obj);
                    }
            	}else{
            		zUI.dialog.alert('<pre>'+data.message+'</pre>');
            	}
            },
            error: function (data) {
                failedMessager.show('保存失败');
            }
        });
    },
	//弹出新增/修改弹窗
    add: function (saveType,$this) {
        var detail = {};
        if ($this) {
            var curRow = $this.parentNode.parentNode;
            detail = $('#funcTable').DataTable().row(curRow).data();
        }
        $('#funcRegisterAlert form')[0].reset();
        $('#funcRegisterAlert').attr('saveType', saveType).modal({'show': 'center', "backdrop": "static"});
        if(saveType=='1'){
        	$("#funcRegisterAlert .form-control[col-name='funcName']").attr("readOnly",false);
        }else if(saveType=='2'){
        	$("#funcRegisterAlert .form-control[col-name='funcName']").attr("readOnly",true);
        	funcModel.showData(detail); // 数据回显
        }
    },
    // 回显数据
    showData: function (detail) {
        if (detail) {
            var data = detail ? detail : {};
            for (var key in data) {
                var target = $("#funcRegisterAlert .form-control[col-name='" + key + "']");
                if (target.length > 0) {
                    $(target).val(data[key]);
                }
            }
        }
    },
    // 获取组表单数据
    getData: function () {
        var obj = {};
        var inputs = $('#funcRegisterAlert form .form-control');
        for (var i = 0; i < inputs.length; i++) {
            obj[$(inputs[i]).attr('col-name')] = $(inputs[i]).val();
        }
        var useType = localStorage.getItem("funcTab");
        obj['useType'] = useType;
        return obj;
    },
    // 新增/修改数据
    save: function () {
    	debugger;
        // 表单验证
        if (!$('#funcRegisterAlert form').isValid()) {
            return;
        }
    	var saveType = $('#funcRegisterAlert').attr('saveType'); // 1新增 2	修改
    	var url = '/func/createFunc';
    	if(saveType==2){
    		url = '/func/editFunc';
    	}
    	var obj = funcModel.getData();
        $.ajax({
            url: url,
            type: 'POST',
            data: obj,
            success: function (data) {
            	if(data.message=='保存成功'){
					successMessager.show(data.message);
					funcModel.hidden();
	            	var obj = {};
	                obj['useType']=data.useType;
	            	funcModel.init(obj);
            	}else{
            		zUI.dialog.alert('<pre>'+data.message+'</pre>');
            	}
            },
            error: function (data) {
                failedMessager.show('保存失败');
            }
        });
    },
    // 删除
    del: function (funcName,useType) {
        /*confirmAlert.show('是否确认删除？', function () {
        });*/
    	if(useType=='3'){
    		confirmAlert.show('是否继续删除？', function () {
    	        $.ajax({
    	            url: '/func/deleteFunc',
    	            type: 'POST',
    	            data: {'funcParam': funcName,'useType':useType},
    	            success: function (data) {
    	                successMessager.show('删除成功');
    	                var obj = {};
    	                obj['useType']=data.useType;
	                	funcModel.initProc(obj);
    	            },
    	            error: function (data) {
    	                failedMessager.show('删除成功');
    	            }
    	        });
        	});
    	}else{
    		confirmAlert.show('是否继续删除？', function () {
    	        $.ajax({
    	            url: '/func/deleteFunc',
    	            type: 'POST',
    	            data: {'funcName': funcName,'useType':useType},
    	            success: function (data) {
    	                successMessager.show('删除成功');
    	                var obj = {};
    	                obj['useType']=data.useType;
	                	funcModel.init(obj);
    	            },
    	            error: function (data) {
    	                failedMessager.show('删除成功');
    	            }
    	        });
        	});
    	}
    	
    },
    //按条件查询
    search: function(){
    	var inputs = $('#funcContent  .input-group .form-control');
        var obj = {};
        for (var i = 0; i < inputs.length; i++) {
            if ($.trim($(inputs[i]).val()) == '') {
                continue;
            }
            obj[$(inputs[i]).attr('data-col')] = $.trim($(inputs[i]).val());
        }
        funcModel.init(obj);
    },
    // 关闭弹框
    hidden: function () {
        $('#funcRegisterAlert').modal('toggle', 'center');
    },
    check:function(arr){
    	//alert(arr);
    	var check = true;
    	//debugger;
    	for(var i=arr.length-1;i>=0;i--){
    		console.log(arr[i]);
    		for(j=0;j<i;j++){
    			console.log(arr[j]);
    			if(arr[i]==arr[j]){
    				check = false;
    				break;
    			}
    		}
    	}
    	//alert(check)
    	return check;
    },
    getResult:function(flag){
//		zUI.dialog.alert('<pre>'+JSON.stringify(zUI.select.getListTranData('#tran1'),null,2)+'</pre>');
		debugger;
//		console.log(zUI.select.getListTranData('#tran1'));
		var obj = zUI.dom.get("#tran1");
		var condition = zUI.dom.hasClass(obj,'zUI-list-tran');
		if(flag==2){
			obj = zUI.dom.get("#tran2");
			condition = zUI.dom.hasClass(obj,'zUI-list-tran2');
		}
	    if(condition){
	        var lis = obj.querySelectorAll('.list-box .left li');
	        var ris = obj.querySelectorAll('.list-box .right li');
	        var l = [],r=[],p=[],d=[],t=[];
	        zUI.util.each(lis,function(i,o){
	            l.push(o.getAttribute('list-tran-data'));
	        });
	        zUI.util.each(ris,function(i,o){
	            r.push(o.getAttribute('list-tran-data'));
	        });
	        zUI.util.each(ris,function(i,o){
	            p.push(o.getAttribute('list-param'));
	        });
	        zUI.util.each(ris,function(i,o){
	            d.push(o.getAttribute('list-desc'));
	        });
	        zUI.util.each(ris,function(i,o){
	            t.push(o.getAttribute('list-reg-desc'));
	        });
	        console.log(r);
	        /*if(!funcModel.check(r)){
	        	failedMessager.show('配置函数不能重复');
	        	return false;
	        }*/
	        var url = "/func/saveConfig";
			var param = {};
			param.tables = r;
			var idx = localStorage.getItem("idx");
			var use_type = localStorage.getItem("use_type");
			param.dataSrcAbbr = idx;
			param.dbType = use_type;
			param.param=p;
			param.desc=d;
			param.funcType=t;
			var json = JSON.stringify(param);
			console.log(json);
			$('#loadAlert').modal({'show': 'center', "backdrop": "static"});
			$.ajax({
				url:url,
				type:"post",
				data:json,
				contentType:"application/json;charset=UTF-8",
				success:function(data){
					$('#loadAlert').modal('hide');
					/*if(flag==1){
						$("#pText").html(data.word);
					}else if(flag==2){
						$("#pText2").html(data.word);
					}*/
					successMessager.show('保存成功');
					console.log(data);
				},
				error: function (data) {
	            	failedMessager.show('保存失败');
	            }
			})
	    }
	},
	clear:function(flag){
		if(flag==1){
			$("#configList").html("");
		}else if(flag==2){
			$("#configList2").html("");
		}
		//history.go(0);
	},
	queryConfig: function(idx,type){
		var obj ={};
		obj["dataSrcAbbr"]=idx;
		obj["useType"]=type;
		$.ajax({
	        url: '/func/queryConfig',
	        type: 'POST',
	        data: obj,
	        success: function (res) {
	        	console.log(res);
	        	var data = res.tables;
	        	var htmlstr ='';
	        	var funcName_tmp = '';
	    		var funcParam_tmp = '';
	    		var funcParamDesc_tmp = '';
	    		var funcDesc_tmp = '';
	    		if(data.length>0){
		        	for(var i in data){
		    			var funcName = data[i].funcName;
		    			var funcParam = data[i].funcParam;
		    			var funcParamDesc = data[i].funcParamDesc;
		    			var funcDesc = data[i].funcType;
		    			var active ="";
		    			if(i==0){
		    				active ="active";
		    				var funcName_tmp = funcName;
				    		var funcParam_tmp = funcParam;
				    		var funcParamDesc_tmp = funcParamDesc;
				    		var funcDesc_tmp = funcDesc;
		    			}
		    			htmlstr=htmlstr+'<li list-tran-data="'+funcName+'" list-param="'+funcParam+'" list-desc="'+funcParamDesc+'" list-reg-desc="'+funcDesc+'" class="'+active+'">'+funcName+'</li>';
		    			/*onclick=funcModel.queryParam("'+funcParam+'","'+funcParamDesc+'")*/
		        	}
	    		}
	    		if(type==1){
	        		$("#configList").html(htmlstr);
	        		//$("#pText").html(res.word);
	        		$("#name").val(funcName_tmp);
					$("#param").val(funcParam_tmp);
					$("#desc").val(funcParamDesc_tmp);
					$("#regDesc").val(funcDesc_tmp);
	        	}else if(type==2){
	        		$("#configList2").html(htmlstr);
	        		//$("#pText2").html(res.word);
	        		$("#name2").val(funcName_tmp);
					$("#param2").val(funcParam_tmp);
					$("#desc2").val(funcParamDesc_tmp);
					$("#regDesc2").val(funcDesc_tmp);
	        	}
	        },
	        error: function (data) {
	        }
	    });
	},
	editParam:function($this,type){
		if(type==1){
			var funcName =$("#name").val();
			var funcParam =$("#param").val();
			var funcParamDesc =$("#desc").val();
			var funcDesc = $("#regDesc").val();
			$("#configList li[list-tran-data='"+funcName+"']").attr("list-param",funcParam);
			$("#configList li[list-tran-data='"+funcName+"']").attr("list-desc",funcParamDesc);
			$("#configList li[list-tran-data='"+funcName+"']").attr("list-reg-desc",funcDesc);
		}else if(type==2){
			var funcName =$("#name2").val();
			var funcParam =$("#param2").val();
			var funcParamDesc =$("#desc2").val();
			var funcDesc = $("#regDesc2").val();
			$("#configList2 li[list-tran-data='"+funcName+"']").attr("list-param",funcParam);
			$("#configList2 li[list-tran-data='"+funcName+"']").attr("list-desc",funcParamDesc);
			$("#configList2 li[list-tran-data='"+funcName+"']").attr("list-reg-desc",funcDesc);
		}
		
		/*var funcName = $("#name").val();
		var funcParam = $("#param").val();*/
		/*alert(funcParam);
		var map = new Map();
		map[funcName]=funcParam;*/
		//localStorage.setItem("monitor_"+funcName,funcParam);
		/*$("#param").val(funcParam);
		$("#desc").val(funcParamDesc);*/
	},
	getContent(type){
		var obj ={};
		obj["dataSrcAbbr"]=localStorage.getItem("idx");
		obj["useType"]=type;
		$.ajax({
	        url: '/func/queryContent',
	        type: 'POST',
	        data: obj,
	        success: function (res) {
	        	console.log(res);
        		zUI.dialog.alert_max('<pre>'+res.word+'</pre>');
    			/*zUI.dialog.alert('<pre>'+$("#pText").html()+'</pre>');*/
	        },
	        error: function (data) {
	        }
	    });
	}
}


/**
 * 初始化 接口列表
 */
function initFuncTable(obj) {
	
    $('#funcTable').width('100%').dataTable({
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
            {"title": "函数名", "data": "funcName"},
            {"title": "函数描述", "data": "funcDesc"},
            {"title": "参数", "data": "funcParam"},
            /*{"title": "参数描述", "data": "funcParamDesc","width":"7%"},*/
            /*{"title": "函数类型", "data": "useType"},*/
            {"title":"操作" ,"data": null,"render": function(data, type, row) {
            	var html = '<div>';
            		html += '<span onclick="funcModel.add(2,this)" class="btn-sm cm-tblA">修改</span>';//
            		html += '<span onclick="funcModel.del(\''+row.funcName+'\',\''+row.useType+'\');" class="btn-sm cm-tblC">删除</span>';
            		html += '</div>';
            	$("#row_"+row.functionId).data("rowData",row);
				return html;
			}}
            ],
        ajax: {
            url: '/func/queryFunc',
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
function initRegProcTable(obj) {
	
    $('#regProcTable').width('100%').dataTable({
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
            {"title": "存储过程数据库名", "data": "funcName"},
            {"title": "存储过程数据库中文名称", "data": "funcDesc"},
            {"title": "存储过程", "data": "funcParam"},
            {"title": "存储过程名称", "data": "funcParamDesc"},
            /*{"title": "函数类型", "data": "useType"},*/
            {"title":"操作" ,"data": null,"render": function(data, type, row) {
            	var html = '<div>';
            		/*html += '<span onclick="funcModel.addProc(2,this)" class="btn-sm cm-tblA">修改</span>';//
*/            		html += '<span onclick="funcModel.del(\''+row.funcParam+'\',\'3\');" class="btn-sm cm-tblC">删除</span>';
            		html += '</div>';
            	$("#row_"+row.functionId).data("rowData",row);
				return html;
			}}
            ],
        ajax: {
            url: '/func/queryFunc',
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