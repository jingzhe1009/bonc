
	//分页
	function setPage(result){
		var html = '';
		html += '<li><a href="#" onclick="pre()"><上一页</a></li>';
		var num = result.totalPages;
		var cnum = parseInt($("#pageNum").val());
		if(num>=10){
			if(cnum>num/2){
				for(var i=cnum-5;i<=num;i++){
					if(cnum==i){
						html += '<li><a href="#" style="background-color:#337ab7" onclick="goPage('+(i)+')"><font color=white >'+(i)+'</font></a></li>';
					}else{
						html += '<li><a href="#" onclick="goPage('+(i)+')">'+(i)+'</a></li>';
					}
				}
			}else{
				for(var i=1;i<=10;i++){
					if(cnum==i){
						html += '<li><a href="#" style="background-color:#337ab7" onclick="goPage('+(i)+')"><font color=white >'+(i)+'</font></a></li>';
					}else{
						html += '<li><a href="#" onclick="goPage('+(i)+')">'+(i)+'</a></li>';
					}
				}
			}
		}else{
			for(var i=1;i<=num;i++){
				if(cnum==i){
					html += '<li><a href="#" style="background-color:#337ab7" onclick="goPage('+(i)+')"><font color=white >'+(i)+'</font></a></li>';
				}else{
					html += '<li><a href="#" onclick="goPage('+(i)+')">'+(i)+'</a></li>';
				}
			}
		}
		html += '<li><a href="#" onclick="next()">下一页></a></li>';
		html += '<li><a href="#" ><select id="choose" onChange="choose(this.value)"><option value="1">1</option><option value="5">5</option><option value="10">10</option><option value="20">20</option><option value="50">50</option><option value="100">100</option></select></a></li>';
		html += '<li><a href="#" >共'+result.totalPages+'页,'+result.totalSize+'条记录</a></li>';
		$("#page1").html(html);
		$("#totalSize").val(result.totalSize);
		$("#choose").val(result.pageSize);
	}
	function goPage(num){
		$("#pageNum").val(num);
		query();
	}
	function next(){
		var pageSize  = $("#pageSize").val();
		var pageNum =$("#pageNum").val();
		var totalSize =$("#totalSize").val();
		if((parseInt(pageNum)-1)*parseInt(pageSize)<parseInt(totalSize)){
			$("#pageNum").val(parseInt(pageNum)+1);
		}else{
			$("#pageNum").val(pageNum);
		}
		query();
	}
	function pre(){
		var pageSize  = $("#pageSize").val();
		var pageNum =$("#pageNum").val();
		if(parseInt(pageNum)==1){
			$("#pageNum").val(1);
		}else{
			$("#pageNum").val(parseInt(pageNum)-1);
		}
		query();	
	}
	function choose(v){
		$("#pageNum").val(1);
		$("#pageSize").val(v);
		query();
	}
	
	//导入词根
	function importDictionary(){
		window.open("/dic/dictionary",'newwindow','height=400,width=800,top=400,left=400,toolbar=no,menubar=no,scrollbars=no, resizable=no,location=no, status=no'); 
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
	
	/**
	 * 全选
	 * @returns
	 */
	function checkedAll() {
		var checked =$("#checkedAll").prop("checked");
		console.log(checked)
		$('[name=items]:checkbox').prop("checked", checked ); //所有checkbox跟着全选的checkbox走。
	}
	

