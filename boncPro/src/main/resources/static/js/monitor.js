zUI.select = {
    	init:function(idx,type){
    		debugger;
    		$.ajax({
				url:"/bonc/queryFunc",
				type:"GET",
				data:{"dataSrcAbbr":idx},
				contentType:"application/json;charset=UTF-8",
				success: function(result){
					debugger;
					var htmlstr ='';
					var data = result.data;
					for(var i in data){
		    			var funcName = data[i].funcName;
		    			var dataSrcDesc = data[i].funcDesc;
		    			var active = "";
				    	if(i==0){
				    		active = "active";
				    	}
				    	htmlstr=htmlstr+'<li list-tran-data="'+funcName+'" >'+funcName+'</li>';
		    		}
					$("#funcList").html(htmlstr);
					
					var obj ={};
					obj["dataSrcAbbr"]=idx;
		        	obj["funcType"]=type;
		        	$.ajax({
		                url: '/bonc/queryConfig',
		                type: 'POST',
		                data: obj,
		                success: function (res) {
		                	console.log(res);
		                	$("#pText").html(res.word);
		                	var data = res.tables;
		                	var htmlstr ='';
		                	for(var i in data){
		    	    			var idx = data[i].funcName;
		    	    			htmlstr=htmlstr+'<li list-tran-data="'+idx+'" >'+idx+'</li>';
		                	}
		                	$("#configList").html(htmlstr);
		                	zUI.select.loadListTran({
							    elem:'.zUI-list-tran'
							})
		                },
		                error: function (data) {
		                }
		            });
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
                    setColor();
                    setColor2();
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
                            dom.remove(o);
                            rightUl.appendChild(newLi);
                            newLi.onclick = click;
                        });
                        rightUl.scrollTop = rightUl.offsetHeight;
                    }
                    setColor();
                    setColor2();
                }
                goLeft =  function () {
                    var lis = rightUl.querySelectorAll('li.active');
                    if(lis.length > 0){
                        util.each(lis,function(i,o){
                            var newLi = o.cloneNode(true);
                            dom.remove(o);
                            leftUl.appendChild(newLi);
                            newLi.onclick = click;
                        });
                        leftUl.scrollTop = leftUl.offsetHeight;
                    }
                    setColor();
                    setColor2();
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
                    setColor2();
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
                    setColor2();
                }
                ir.onclick = goRight;
                il.onclick = goLeft;
                iu.onclick = goUp;
                id.onclick = goDown;
                var liList = tab.querySelectorAll('li');
                util.each(liList,function(i,o){
                    o.onclick = click;
                });
                setColor();
                setColor2();
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
zUI.tab = {
        loadTab:function(set){
            var dom = zUI.dom;
            var elem = set.elem||'';
            var type = set.type||'1';
            var $obj = document.querySelectorAll(elem);
            var clicks = set.clicks||[];
            var that = this;
            var loadOne = function(tab,index){
                var navBox = tab.querySelector('.bar-box');
                var navs = navBox.querySelectorAll('.nav');
                var wraps = tab.querySelectorAll('.content .wrap');
                var minW = 0;
                for(var i=0;i<navs.length;i++){
                    var nav = navs[i];
                    minW += parseInt(dom.getW(nav));
                    var wrap = wraps.length > i ? wraps[i]:null;
                    var disable = dom.hasClass(nav,'disable');
                    if(!disable){
                        //闭包
                        nav.onclick = function(wp,nv,ind){
                            return function(e){
                                that.showTab(nv,wp,wraps,navs);
                                var myClick = clicks.length > ind ? clicks[ind]:null;
                                if(zUI.util.isF(myClick)){
                                    try{
                                        myClick(index,nv,wp);
                                    }catch (e) {
                                        console.error(e);
                                    }
                                }
                            }
                        }(wrap,nav,i);
                    }

                }
                tab.style.minWidth = ((navs.length+2)*5+minW+0.5)+'px';
            }
            for(var i=0;i<$obj.length;i++){
                loadOne($obj[i],i);
            }
        },
        switchTab: function (elem,index) {
            var dom = zUI.dom;
            var obj = document.querySelectorAll(elem);
            for (var i = 0; i < obj.length; i++) {
                if(dom.hasClass(obj[i],'zUI-bar')){
                    var navs = obj[i].querySelectorAll('.bar-box .nav');
                    var wraps = obj[i].querySelectorAll('.content .wrap');
                    var nav = navs.length > index ? navs[index] : null;
                    var wrap = wraps.length > index ? wraps[index]:null;
                    this.showTab(nav,wrap,wraps,navs);
                }
            }
        },
        showTab: function (nav,wrap,wraps,navs) {
            var dom = zUI.dom;
            if(nav != null){
                dom.removeClass(wraps,'zUI-show');
                dom.removeClass(navs,'active');
                dom.addClass(nav,'active');
                if(wrap != null){
                    dom.addClass(wrap,'zUI-show');
                }
            }
        },
        //加载一个面板
        loadPanel: function (set) {
            var dom = zUI.dom;
            var elem = set.elem||'';
            var $obj = document.querySelectorAll(elem);
            var close = set.close;
            var that = this;
            var loadOne = function(tab,index){
                var group = tab.getAttribute('zUI-card-group');
                var folder = tab.getAttribute('zUI-folder')||(group ? 'hide':'');

                var close = tab.hasAttribute('zUI-close');
                var title = tab.querySelector('.zUI-card-title');
                var bns = dom.createElem({name:'div',clazz:'btns'});
                title.appendChild(bns);
                var wrap = tab.querySelector('.zUI-card-wrap');
                var folderBtn = dom.createElem({name:'i',clazz:'zUI-icon icon-cancel-circle'});
                var show = folder === 'show';
                if(folder){
                    bns.appendChild(folderBtn);
                    dom.addClass(folderBtn,'zUI-icon ' + (show ? 'icon-circle-down':'icon-circle-left'));
                    dom.addClass(wrap,show?'zUI-show':'zUI-hide');
                    dom.removeClass(wrap,show?'zUI-hide':'zUI-show');
                    var hideCard = function(w,btn){
                        dom.removeClass(w,'zUI-show');
                        dom.addClass(btn,'icon-circle-left');
                        dom.addClass(w,'zUI-hide');
                    }
                    var showCard = function(w,btn){
                        dom.removeClass(w,'zUI-hide');
                        dom.addClass(btn,'icon-circle-down');
                        dom.addClass(w,'zUI-show');
                    }
                    var fod = function(e){
                        dom.removeClass(folderBtn,'icon-circle-down icon-circle-left');
                        var type = 'unknow';
                        if(dom.isVisable(wrap)){
                            hideCard(wrap,folderBtn);
                            type='hide';
                        }else{
                            showCard(wrap,folderBtn);
                            type='show';
                            if(group){
                                var others = tab.parentNode.querySelectorAll('.zUI-card[zUI-card-group=\''+group+'\']');
                                for (var i = 0; i < others.length; i++) {
                                    if(others[i] !== tab){
                                        var wrap2 = others[i].querySelector('.zUI-card-wrap');
                                        var folderBtn2 = others[i].querySelector('.zUI-card-title .btns .icon-circle-left,.icon-circle-down');
                                        hideCard(wrap2,folderBtn2);
                                    }
                                }

                            }

                        }
                        if(zUI.util.isF(set.change)){
                            try{
                                (set.change)(type);
                            }catch (e) {
                                console.error(e);
                            }
                        }
                    };
                    title.onclick = fod;

                }
                if(close){
                    var ico = dom.createElem({name:'i',clazz:'zUI-icon icon-cancel-circle'});
                    bns.appendChild(ico);
                    ico.onclick = function(){
                        var r = true;
                        if(zUI.util.isF(set.close)){
                            try{
                                r = (set.close)();
                            }catch (e) {
                                console.error(e);
                            }
                        }
                        if(r!== false){
                            dom.remove(this.parentNode.parentNode.parentNode);
                        }
                    }
                }
            }
            for(var i=0;i<$obj.length;i++){
                loadOne($obj[i],i);
            }
        }
    }
zUI.dialog={
        maskCount:0,
        alertUI:function(set){
            var dom = zUI.dom;
            var title = set.title||'提示';
            var width = set.width;
            var height = set.height;
            var close = set.close;
            var draggable = set.draggable !== false;
            var resizable = set.resizable !== false;
            var shadow = set.shadow !== false;
            var btns = set.btns||null;
            var id = set.id||('zUI_'+zUI.util.UUID());
            if(dom.get('#'+id)!== null){
                return false;
            }
            var shadowDiv = null;
            if(shadow){
                shadowDiv = dom.createElem({name:'div',clazz:'zUI-shadow',id:'mark_'+id});
                document.body.appendChild(shadowDiv);
            }
            var content = set.content||'';
            var obj = dom.createElem({name:'div',clazz:'zUI-box',id:id});

            var tit = dom.createElem({name:'div',clazz:'zUI-title no-select'+ (draggable?' draggable':'')});

            obj.appendChild(tit);
            var h1 = dom.createElem({name:'h1',clazz:'zUI-title',text:title});

            tit.appendChild(h1);

            var dv2 = document.createElement('div');
            var clo = dom.createElem({name:'a',text:'×'});
            dv2.appendChild(clo);
            tit.appendChild(h1);
            tit.appendChild(dv2);

            var cnt = dom.createElem({name:'div',clazz:'zUI-content',html:content});

            obj.appendChild(cnt);
            var btnBox = dom.createElem({name:'div',clazz:'zUI-btns'});
            if(btns !== null){
                obj.appendChild(btnBox);
            }else{
                dom.addClass(cnt,'not-btn');
            }

            document.body.appendChild(obj);
            if(height){
                obj.style.height=height;
            }
            if(width){
                obj.style.width=width;
            }
            function isF(f){
                return typeof f === 'function';
            }
            var btnArr = [];
            if(btns !== null){
                for(var bn in btns){
                    var f = btns[bn];
                    //防止循环结束后，回调时bn永远是最后一个值
                    var b = dom.createElem({name:'a',clazz:'zUI-btn',text:bn});
                    b.setAttribute('href','javascript:;');
                    btnBox.appendChild(b);
                    if(isF(f)){
                        b.onclick = function(ff){
                            return function () {
                                if(ff() !== false){
                                    dom.remove(shadowDiv);
                                    dom.remove(obj);
                                    if(zUI.util.isF(close)){
                                        close();
                                    }
                                }
                            }
                        }(f);
                    }
                    btnArr.push(b);
                }
            }

            if(draggable){
                tit.onmousedown=function(e){
                    var e = e || window.event;
                    var state = 1;
                    var x0 = e.clientX - obj.offsetLeft;
                    var y0 = e.clientY - obj.offsetTop;
                    /*if(typeof tit[0].setCapture !== 'undefined'){
                        tit[0].setCapture();
                    }*/
                    var f1 = function(e1){
                        e1 = e1 || window.event;
                        if(state == 1){
                            var moveX = e1.clientX - x0;
                            var moveY = e1.clientY - y0;
                            if(moveX < 0){
                                moveX = 0
                            }else if(moveX > window.innerWidth - tit.offsetWidth){
                                moveX = window.innerWidth - tit.offsetWidth
                            }
                            if(moveY < 0){
                                moveY = 0
                            }else if(moveY > window.innerHeight - tit.offsetHeight){
                                moveY =  window.innerHeight - tit.offsetHeight
                            }
                            obj.style.left = moveX + 'px';
                            obj.style.top = moveY + 'px'
                        }
                    }
                    var f2 = function(e2){
                        state = 0;
                        dom.removeEvent(document,"mousemove",f1);
                        dom.removeEvent(document,"mouseup",f2);
                    }
                    document.onmousemove = f1;
                    document.onmouseup = f2;

                };
            }
            if(resizable){
                var tool = dom.createElem({name:'span',clazz:'zUI-resize no-select'});
                obj.appendChild(tool);
                tool.onmousedown=function(e){
                    var e = e || window.event;
                    var state = 1;
                    var ww = parseInt(obj.style.width||obj.offsetWidth);
                    var hh = parseInt(obj.style.height||obj.offsetHeight);
                    var ccnntt = parseInt(cnt.style.height||cnt.offsetHeight);
                    var x0 = e.clientX;
                    var y0 = e.clientY;
                    var f1 = function(e1){
                        e1 = e1 || window.event;
                        if(state == 1){
                            e.preventDefault();
                            var x1 = e1.clientX;
                            var y1 = e1.clientY;
                            if(x1 < 0){
                                x1 = 0;
                            }else if(x1 > window.innerWidth){
                                x1 = window.innerWidth;
                            }
                            if(y1 < 0){
                                y1 = 0;
                            }else if(y1 > window.innerHeight){
                                y1 =  window.innerHeight;
                            }

                            obj.style.width=cnt.style.width=(ww+x1-x0)+'px';
                            obj.style.height=(hh+y1-y0)+'px';
                            cnt.style.height=(ccnntt+y1-y0)+'px';
                        }
                    }
                    var f2 = function(e2){
                        state = 0;
                        dom.removeEvent(document,"mousemove",f1);
                        dom.removeEvent(document,"mouseup",f2);
                    }
                    document.onmousemove = f1;
                    document.onmouseup = f2;
                }
            }
            obj.style.display = 'block';
            if(btnArr.length > 0){
                var minWidth = 0;
                for (var i = 0; i < btnArr.length; i++) {
                    minWidth += dom.getW(btnArr[i]);
                }
                var p1 = parseInt(dom.css(btnBox,'paddingLeft'));
                var p2 = parseInt(dom.css(btnBox,'paddingRight'));
                // var wm = dom.getW(btnBox);
                obj.style.minWidth = (minWidth+p1+p2 + btnArr.length*8+5)+'px';
                cnt.style.minWidth = (minWidth+p1+p2 + btnArr.length*8+5)+'px';
            }
            function setPos(){
                var winW = document.documentElement.clientWidth;
                var winH = document.documentElement.clientHeight;
                //offsetWidth获取只能是显示的元素
                var rw = parseInt(dom.getW(obj));
                var rh = (parseInt(dom.getH(obj)))+42+45;
                obj.style.top = (winH/2-rh/2)+'px';
                obj.style.left = (winW/2-rw/2)+'px';
            }
            setPos();
            clo.onclick=function(){
                dom.remove(shadowDiv);
                dom.remove(obj);
                if(zUI.util.isF(close)){
                    close();
                }
            };
            window.onresize=setPos;
        },
        alert:function(content,title){
            var set = {
                content: content,
                title: title,
                resizable: false,
                width: '300px',
                btns: {
                    '确定':function(){}
                },
            }
            this.alertUI(set);
        },
        alertDom: function (set) {
            var dom = zUI.dom;
            var target = dom.get(set.target);
            if(target){
                var parentNode = target.parentNode;
                var id = "_" + new Date().getTime()+"_"+zUI.util.UUID();
                var temp = dom.createElem({name:'div',id:id,clazz:'zUI-hide'});
                var newTar = target.cloneNode(true);
                try {
                    var oldClose = set.close;
                    set.close = function(){
                        dom.addClass(newTar,'zUI-hide');
                        parentNode.replaceChild(newTar,temp);
                    }
                    if(zUI.util.isF(oldClose)){
                        oldClose();
                    }
                    parentNode.replaceChild(temp,target);
                    dom.removeClass(newTar,'zUI-hide');
                    set.content = newTar.outerHTML;

                    this.alertUI(set);
                }catch (e) {
                    console.error(e);
                    parentNode.replaceChild(target,temp);
                    dom.addClass(target,'zUI-hide');
                }

            }
        },
        hoverTip: function (set) {
            var dom = zUI.dom;
            var dir = set.dir || 'left';
            var items = dom.gets(set.elem);
            if(items){
                var loadOne = function(index,obj){
                    var title = obj.getAttribute('zUI-title');
                    var stay = obj.getAttribute('zUI-title-stay')||'false'+'';
                    if(title){
                        var s1 = '#function:';
                        if(title.indexOf(s1)===0 && title.lastIndexOf('#')===title.length - 1){
                            var f = title.substring(10,title.length - 1);
                            title = eval(f+'('+obj+')');
                            if(typeof title !== 'string'){
                                title = title.outerHTML;
                            }
                        }
                        var id = 'zUI_tip_' + zUI.util.UUID();
                        obj.onmouseover = function(e){
                            var tip = dom.get('#'+id);
                            if(tip != null){
                                dom.remove(tip);
                                return;
                            }
                            tip = dom.createElem({name:'div',clazz:'zUI-tip',html:title,id:id});
                            var x,y,af;
                            dom.addClass(tip,dir);
                            var doLeft,doRight,doUp,doDown;
                            doLeft = function(){
                                x = e.target.offsetLeft;
                                y = e.target.offsetTop + dom.getH(e.target)/2;
                                af = function(){
                                    var dx = x-dom.getW(tip)-2;
                                    tip.style.top = (y-dom.getH(tip)/2) + 'px';
                                    tip.style.left = (dx) + 'px';
                                    if(dx < 0){
                                        tip.style.left = '0px';
                                        tip.style.width = x+'px';
                                        tip.style.top = (y-dom.getH(tip)/2) + 'px';

                                    }
                                }
                            }
                            doRight = function(){
                                x = e.target.offsetLeft + dom.getW(e.target)+2;
                                y = e.target.offsetTop + dom.getH(e.target)/2;
                                af = function(){
                                    tip.style.top = (y-dom.getH(tip)/2) + 'px';
                                    tip.style.left = (x-2) + 'px';
                                    var x1 = x + dom.getW(tip);
                                    var dx = dom.getW(document.body) - x1;
                                    if(dx < 0){
                                    }
                                }
                            }
                            doUp = function(){
                                x = e.target.offsetLeft + dom.getW(e.target)/2;
                                y = e.target.offsetTop - 2;
                                af = function(){
                                    tip.style.top = (y - dom.getH(tip)) + 'px';
                                    tip.style.left = (x-dom.getW(tip)/2-2) + 'px';
                                }
                            }
                            doDown = function(){
                                x = e.target.offsetLeft + dom.getW(e.target)/2;
                                y = e.target.offsetTop + dom.getH(e.target);
                                af = function(){
                                    tip.style.top = (y+ 2) + 'px';
                                    tip.style.left = (x-dom.getW(tip)/2-2) + 'px';
                                }
                            }
                            if(dir === 'left'){
                                doLeft();
                            }else if(dir === 'up'){
                                doUp();
                            }else if(dir === 'down'){
                                doDown();
                            }else{
                                doRight();
                            }
                            tip.style.left = x + 'px';
                            tip.style.top = y + 'px';
                            document.body.appendChild(tip);
                            af();
                            var key = zUI.util.UUID();
                            var clf = function(ee){
                                if(ee.target!== tip && !dom.isChild(ee.target,tip)){
                                    dom.remove(tip);
                                    zUI.event.unbind(key);
                                }

                            };
                            zUI.event.bind(key,document,'click',clf);
                            document.onclick = clf;

                        }
                    }
                }
                zUI.util.each(items,loadOne);
            }
        },
        showMask: function (set) {
            var delay = set && set.delay || '';
            var maskCount = this.maskCount;
            this.maskCount = ++maskCount;
            var dom = zUI.dom;
            var mask = dom.get('.zUI-mask');
            if(mask == null){
                mask = dom.createElem({name:'div',clazz:'zUI-shadow zUI-mask'});
                mask.appendChild(dom.createElem({name:'img'}));
                document.body.appendChild(mask);
            }
            var that = this;
            if(delay && !isNaN(delay = parseInt(delay))){
                setTimeout(function () {
                    that.closeMask();
                },delay);
            }
        },
        closeMask: function (force) {
            var maskCount = this.maskCount;
            if(force === true){
                this.maskCount = 0;
            }else{
                this.maskCount = --maskCount;
            }
            if(maskCount <= 0){
                zUI.dom.remove('.zUI-mask');
            }
        },
        message: function (set) {
            var dom = zUI.dom;
            var content = set.content||'';
            var dir = set.dir||'center';
            var ifClose = set.close !== false;
            var timer = set.timer;
            var delay = set.delay||'5000';
            if(delay < 1000){
                delay = 1000;
            }
            var level = set.level||'success';
            var queue = dom.get('.zUI-message-queue.'+dir);
            if(queue == null){
                queue = dom.createElem({name:'div',clazz:'zUI-message-queue '+dir});
                document.body.appendChild(queue);
            }
            queue.style.width = 'auto';
            var msg = dom.createElem({name:'div',clazz:'zUI-message-wrap '+level});
            var iconName = {info:'icon-info',warn:'icon-notification',error:'icon-warning'}[level]||'icon-checkmark';

            var icon1 = dom.createElem({name:'i',clazz:'zUI-icon '+iconName});

            msg.appendChild(icon1);
            var textDiv = dom.createElem({name:'div',text:content});
            msg.appendChild(textDiv);
            queue.appendChild(msg);
            if(dir === 'center'){
                msg.style.left = '-'+dom.getW(msg)/2+'px';
            }else if(dir === 'right'){
                var www = parseInt(dom.getW(msg)) + 6;
                msg.style.left = '-'+www+'px';
            }

            var ff = null;
            if(timer !== false){
                var icon2 = dom.createElem({name:'div',clazz:'zUI-message-timer'});
                var div = dom.createElem({name:'div'});
                icon2.appendChild(div);
                msg.appendChild(icon2);
                var ct = 60,stop = false;
                var divM = dom.getW(icon2);
                var divW = divM/(delay/ct-1);
                ff = setInterval(function () {
                    if(!stop){
                        var realW = dom.getW(div);
                        var nw = parseFloat(realW) + divW;
                        if(nw > divM){
                            nw = divM;
                        }
                        div.style.width = nw + 'px';
                    }
                },ct);
            }
            var closeTip = function(){
                if(ff != null){
                    clearInterval(ff);
                }
                var doF;
                var dy = dom.getH(msg)/4;
                var dxx = parseInt(dom.getW(msg))/4;
                var opc = 1;
                if(dir ==='right'){
                    doF = function () {
                        var h1 = parseFloat(dom.css(msg,'left'));
                        msg.style.left = (h1 + dxx)+'px';
                    };
                }else{
                    doF = function () {
                        var h1= 0;
                        msg.style.top = (h1 - dy)+'px';
                        msg.style.opacity = opc-=0.2;
                    };
                }

                var dh = setInterval(doF,50);
                setTimeout(function () {
                    clearInterval(dh);
                    dom.remove(msg);
                },200);
            }
            var zmo = zUI.util.setTimeout(closeTip,delay);

            msg.onmouseover = function () {
                stop = true;
                zmo.pause();
            };
            msg.onmouseout = function () {
                stop = false;
                zmo.play();
            };
            if(ifClose){
                var close = dom.createElem({name:'span',clazz:'close',text:'×'});
                msg.appendChild(close);
                close.onclick = closeTip;
            }
            msg.style.width = parseInt(dom.getW(msg)) + 'px';
            var ms = queue.querySelectorAll('.zUI-message-wrap');
            var h = 0;
            var winH = document.documentElement.clientHeight - 20;
            zUI.util.each(ms,function (i,o) {
                h+= dom.getH(o)+5;
            });
            if(h > winH){
                dom.remove(ms[0]);
            }
            queue.style.width = '0px';
        }
    }