;(function(){
    zUI.select = {
    	init:function(code){
    		$.ajax({
				url:"/func/queryFunc",
				type:"GET",
				data:{"useType":code},
				contentType:"application/json;charset=UTF-8",
				success: function(result){
					var htmlstr ='';
					var data = result.list1;
					console.log(data);
					for(var i in data){
						var funcId = data[i].funcId;
		    			var funcName = data[i].funcName;
		    			var funcDesc = data[i].funcDesc;
		    			var funcParam = data[i].funcParam;
		    			var funcParamDesc = data[i].funcParamDesc;
				    	htmlstr=htmlstr+'<li id="'+funcId+'" desc="'+funcParamDesc+'" param="'+funcParam+'" >'+funcName+'('+funcDesc+')</li>';
		    		}
					$("#funcList").html(htmlstr);
					zUI.select.loadListTran({
					    elem:'.zUI-list-tran'
					})
					
					
					
					/*var htmlstr2 ='';
					var data2 = result.list2;
					console.log(data2);
					for(var i in data2){
		    			var funcName = data2[i].funcName;
		    			var funcDesc = data2[i].funcDesc;
		    			var funcParam = data2[i].funcParam;
		    			var funcParamDesc = data2[i].funcParamDesc;
		    			var active = "";
				    	if(i==0){
				    		active = "active";
				    	}
				    	htmlstr2=htmlstr2+'<li list-tran-data="'+funcName+'" list-param="'+funcParam+'" list-desc="'+funcParamDesc+'" list-reg-desc="'+funcDesc+'">'+funcName+'</li>';
		    		}
					debugger;
					$("#funcList2").html(htmlstr2);
					zUI.select.loadListTran({
					    elem:'.zUI-list-tran2'
					})*/
				}
			});
    	},
        loadListTran:function(set){
            var dom = zUI.dom;
            var util = zUI.util;
            var elem = set.elem||'';
            var $obj = dom.gets(elem);
            var loadOne = function(tab){
                var left = tab.querySelector('.list-box');
                var right = tab.querySelectorAll('.list-box')[1];
                var leftUl = left.querySelector('.left');
                var rightUl = right.querySelector('.right');
                var optTran = dom.createElem({name:'div',clazz:'list-tool'});
                dom.insertAfter(optTran,left);
                var optTranDiv = dom.createElem({name:'div',clazz:'list-tool-indiv'});
                optTran.appendChild(optTranDiv);
                var optTranDivs = dom.createElem({name:'div'});
                optTranDiv.appendChild(optTranDivs);
                var ir = dom.createElem({name:'i',clazz:'zUI-icon icon-arrow-right2'});
                var iu = dom.createElem({name:'i',clazz:'zUI-icon icon-arrow-up2'});
                var id = dom.createElem({name:'i',clazz:'zUI-icon icon-arrow-down2'});
                var il = dom.createElem({name:'i',clazz:'zUI-icon icon-arrow-left2'});
                dom.removeClass(ir,'zUI-gray');
                dom.removeClass(iu,'zUI-gray');
                dom.removeClass(id,'zUI-gray');
                dom.removeClass(il,'zUI-gray');
                optTranDivs.appendChild(ir);
                optTranDivs.appendChild(iu);
                optTranDivs.appendChild(id);
                optTranDivs.appendChild(il);
                var goRight,goLeft,goUp,goDown;
                var click = function(){
                    if(dom.hasClass(this.parentNode,'left')){
                        dom.removeClass(rightUl.querySelectorAll('li.active'),'active');
                    }else{
                        dom.removeClass(leftUl.querySelectorAll('li.active'),'active');
                    }
                    if(dom.hasClass(this,'active')){
                        dom.removeClass(this,'active');
                    }else{
                        dom.addClass(this,'active');
                    }
                    //setColor();
                    //setColor2();
                }
                var setColor = function(){
                    var hasL = leftUl.querySelectorAll('li.active').length > 0;
                    var hasR = rightUl.querySelectorAll('li.active').length > 0;
                    if(!hasL){
                        dom.addClass(ir,'zUI-gray');
                    }else{
                        dom.removeClass(ir,'zUI-gray');
                    }
                    if(!hasR){
                        dom.addClass(il,'zUI-gray');
                    }else{
                        dom.removeClass(il,'zUI-gray');
                    }
                }
                var setColor2 = function(){
                    var lis = tab.querySelectorAll('li.active');
                    if(lis.length == 0){
                        dom.addClass(iu,'zUI-gray');
                        dom.addClass(id,'zUI-gray');
                    }else{
                        if(lis.length == 1 && lis[0].previousElementSibling == null){
                            dom.addClass(iu,'zUI-gray');
                        }else{
                            dom.removeClass(iu,'zUI-gray');
                        }
                        if(lis.length == 1 && lis[0].nextElementSibling == null){
                            dom.addClass(id,'zUI-gray');
                        }else{
                            dom.removeClass(id,'zUI-gray');
                        }
                    }

                }
                goRight =  function () {
                    var lis = leftUl.querySelectorAll('li.active');
                    if(lis.length > 0){
                        util.each(lis,function(i,o){
                            var newLi = o.cloneNode(true);
                            console.log(newLi);
                            //dom.remove(o);
                            rightUl.appendChild(newLi);
                            newLi.onclick = click;
                        });
                        rightUl.scrollTop = rightUl.offsetHeight;
                        $("#funcList li").removeClass('active');
                        //$("#funcList2 li").removeClass('active');
                    }
                    /*setColor();
                    setColor2();*/
                }
                goLeft =  function () {
                    var lis = rightUl.querySelectorAll('li.active');
                    if(lis.length > 0){
                        util.each(lis,function(i,o){
                            var newLi = o.cloneNode(true);
                            dom.remove(o);
                            //leftUl.appendChild(newLi);
                            newLi.onclick = click;
                        });
                        leftUl.scrollTop = leftUl.offsetHeight;
                        $("#configList li").removeClass('active');
                        //$("#configList2 li").removeClass('active');
                        $("#name").val('');
            			$("#param").val('');
            			$("#desc").val('');
            			$("#regDesc").val('');
            			$("#name2").val('');
            			$("#param2").val('');
            			$("#desc2").val('');
            			$("#regDesc2").val('');
                    }
                    /*setColor();
                    setColor2();*/
                }
                goUp =  function () {
                    var lis = tab.querySelectorAll('li.active');
                    var hasL = leftUl.querySelectorAll('li.active').length > 0;
                    var hasR = rightUl.querySelectorAll('li.active').length > 0;
                    var lm = false,rm=false;
                    util.each(lis,function(i,o){
                        var pre = o.previousElementSibling;

                        if(pre != null){
                            var no = dom.swapNode(pre,o);
                            o.onclick = click;
                            no.onclick = click;
                            if(!lm&& hasL){
                                leftUl.scrollTop = leftUl.scrollTop-dom.getH(o);
                                lm = !lm;
                            }
                            if(!rm&& hasR){
                                rightUl.scrollTop = leftUl.scrollTop-dom.getH(o);
                                rm = !rm;
                            }
                        }
                    });
                    /*setColor2();*/
                }
                goDown =  function () {
                    var lis = tab.querySelectorAll('li.active');
                    var lm = false,rm=false;
                    var hasL = leftUl.querySelectorAll('li.active').length > 0;
                    var hasR = rightUl.querySelectorAll('li.active').length > 0;
                    for (var i = lis.length -1; i >= 0 ; i--) {
                        var nex = lis[i].nextElementSibling;
                        if(nex != null){
                            var o = dom.swapNode(nex,lis[i]);
                            lis[i].onclick = click;
                            nex.onclick = click;
                            o.onclick = click;
                            if(!lm&& hasL){
                                leftUl.scrollTop = leftUl.scrollTop+dom.getH(o);
                                lm = !lm;
                            }
                            if(!rm&& hasR){
                                rightUl.scrollTop = leftUl.scrollTop+dom.getH(o);
                                rm = !rm;
                            }
                        }
                    }
                    /*setColor2();*/
                }
                ir.onclick = goRight;
                il.onclick = goLeft;
                iu.onclick = goUp;
                id.onclick = goDown;
                var liList = tab.querySelectorAll('li');
                util.each(liList,function(i,o){
                    o.onclick = click;
                });
                /*setColor();
                setColor2();*/
            }
            util.each($obj,function(i,o){
                loadOne(o);
            });
        },
        getListTranData: function(elem){
            var obj = zUI.dom.get(elem);
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
                return r;
            }
            return null;
        },
        loadSlider: function (set) {
            var elem = set.elem||'';
            var format = set.format||'';
            var dom = zUI.dom;
            var items = dom.gets(elem);
            var loadOne = function(one,o){
                //console.log(o);
                var disabled = dom.hasClass(o,'disabled');
                var maxWidth = parseInt(dom.getW(o));
                var nowV = parseInt(o.getAttribute('zUI-slider-data'))||0;
                var now = o.querySelector('.zUI-slider-now');
                var bar = now.querySelector('.zUI-slider-bar');
                dom.addClass(now,'no-select');
                var tip = dom.createElem({name:'div',clazz:'zUI-slider-tip zUI-hide'});
                var tn = nowV;
                if(zUI.util.isF(format)){
                    tn = format(nowV);
                }
                tip.appendChild(document.createTextNode(tn));
                document.body.appendChild(tip);
                var showTip = function(x){
                    //console.log(x);
                    var r1 = o.offsetTop - dom.getH(tip)-14;
                    tip.style.left = x+'px';
                    tip.style.top = r1+'px';
                }
                now.style.width = maxWidth* nowV/100 +'px';
                if(!disabled){
                    bar.onmousedown=function(e){
                        dom.addClass(tip,'zUI-show');
                        var e = e || window.event;
                        var state = 1;
                        var x0 = e.clientX;
                        var y0 = e.clientY;
                        var startX = o.offsetLeft+ parseInt(dom.getW(now)) - parseInt(dom.getW(tip))/2;

                        showTip(startX);
                        var w = parseInt(dom.getW(now));
                        var f1 = function(e1){
                            e1 = e1 || window.event;
                            if(state == 1){
                                var moveX = w + e1.clientX - x0;
                                var mark = true;
                                if(moveX < 0){
                                    moveX = 0
                                    mark = false;
                                }
                                if(moveX > maxWidth){
                                    mark = false;
                                    moveX = maxWidth
                                }
                                if(mark){
                                    showTip(startX+e1.clientX - x0);
                                }

                                var per = parseInt(moveX*100/maxWidth);
                                now.style.width = moveX + 'px';
                                o.setAttribute('zUI-slider-data',per);
                                var html = per;
                                if(zUI.util.isF(format)){
                                    html = format(per);
                                }
                                tip.innerHTML = html;
                            }
                        }
                        var f2 = function(e2){
                            state = 0;
                            dom.addClass(tip,'zUI-hide');
                            dom.removeClass(tip,'zUI-show');
                            dom.removeEvent(document,"mousemove",f1);
                            dom.removeEvent(document,"mouseup",f2);
                        }
                        document.onmousemove = f1;
                        document.onmouseup = f2;

                    };

                }
                bar.onmouseover = function(){
                    if(!dom.hasClass(tip,'zUI-show')){
                        dom.removeClass(tip,'zUI-hide');
                    }
                    var startX = o.offsetLeft+ parseInt(dom.getW(now)) - parseInt(dom.getW(tip))/2;
                    showTip(startX);
                };
                bar.onmouseout = function(){
                    if(!dom.hasClass(tip,'zUI-show')){
                        dom.addClass(tip,'zUI-hide');
                    }
                };


            }
            zUI.util.each(items,function(i,o){
                loadOne(i,o);
            });
        }
    }
}());