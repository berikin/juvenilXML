function detectmob(){if(navigator.userAgent.match(/Android/i)||navigator.userAgent.match(/webOS/i)||navigator.userAgent.match(/iPhone/i)||navigator.userAgent.match(/iPad/i)||navigator.userAgent.match(/iPod/i)||navigator.userAgent.match(/BlackBerry/i)||navigator.userAgent.match(/Windows Phone/i)){return true}else{return false}}(function(a){a.fn.drags=function(c){c=a.extend({handle:"",cursor:"move"},c);if(c.handle===""){var b=this}else{var b=this.find(c.handle)}return b.css("cursor",c.cursor).on("mousedown",function(k){if(c.handle===""){var d=a(this).addClass("draggable")}else{var d=a(this).addClass("active-handle").parent().addClass("draggable")}var i=d.css("z-index"),j=d.outerHeight(),h=d.outerWidth(),f=d.offset().top+j-k.pageY,g=d.offset().left+h-k.pageX;d.css("z-index",1000).parents().on("mousemove",function(l){a(".draggable").offset({top:l.pageY+f-j,left:l.pageX+g-h}).on("mouseup",function(){a(this).removeClass("draggable").css("z-index",i)})});k.preventDefault()}).on("mouseup",function(){if(c.handle===""){a(this).removeClass("draggable")}else{a(this).removeClass("active-handle").parent().removeClass("draggable")}})}})(jQuery);function getInn(a){$.ajax({type:"GET",url:"db/inns.xml",dataType:"xml",success:function(b){$(b).find("element").each(function(){if($(this).find('attribute[name="Identificador"]').text()==a){$(".infotop").remove();$(".infodown").remove();$('<div class="infotop"></div>').html("").appendTo("body");$('<p id="closeinfo"></p>').html('<img id="closedetails" src="img/cerrar.png" class="fade" alt="cerrar"></img>').appendTo(".infotop");$("#closedetails").bind({click:function(j){$(".infodown").fadeOut(800,function(){$(".infodown").remove()});$(".infotop").fadeOut(800,function(){$(".infotop").remove()})},mouseenter:function(){$(this).fadeOut("fast");$(this).attr("src","img/cerrarhover.png");$(this).fadeIn("fast")},mouseleave:function(){$(this).fadeOut("fast");$(this).attr("src","img/cerrar.png");$(this).fadeIn("fast")}});if($(this).find('attribute[name="Imagen"]').find("link").find("reference").text()==""){$(".infotop").append('<img src="img/albergue_sin_imagen.png" alt="foto del albergue" id="picture">')}else{$(".infotop").append('<img src="'+$(this).find('attribute[name="Imagen"]').find("link").find("reference").text()+'" alt="foto del albergue" id="picture">')}$('<h1 id="infoname"></h1>').html($(this).find('attribute[name="Titulo_es"]').find("string").text()).appendTo(".infotop");$('<p id="address"></p>').html($(this).find('attribute[name="Localizacion"]').find("text").text()).appendTo(".infotop");$(".infotop").append('<div class="contact"></div>');var f="";$(this).find('attribute[name="Telefono"]').find("array").find("string").each(function(){f+=$(this).text()+" /"});f=f.substring(0,f.length-1);$('<p id="phone"></p>').html('<span><img src="img/tfno.png" alt="tfno" /></span>'+f).appendTo(".contact");var g="";$(this).find('attribute[name="Fax"]').find("array").find("string").each(function(){g+=$(this).text()+" /"});g=g.substring(0,g.length-1);$('<p id="fax"></p>').html('<span><img src="img/fax.png" alt="fax" /></span>'+g).appendTo(".contact");var i="";$(this).find('attribute[name="Email"]').find("array").find("string").each(function(){i+='<a href="mailto:'+$(this).text()+'">'+$(this).text()+"</a> /"});i=i.substring(0,i.length-1);$('<p id="mail"></p>').html('<span><img src="img/mail.png" alt="eMail" /></span>'+i).appendTo(".contact");var e="";$(this).find('attribute[name="Web"]').find("array").find("string").each(function(){e+='<a href="http://'+$(this).text()+'" target="_blank">'+$(this).text()+"</a> /"});e+=' <a target="_blank" href="http://www.juvecyl.tuars.com/?id='+a+'">Enlace permanente</a>';$('<p id="web"></p>').html('<span><img src="img/web.png" alt="Webs" /></span>'+e).appendTo(".contact");$('<nav id="navigation"></nav>').html("").appendTo(".infotop");$("<ul></ul>").html('<li><img class="navlink fade" id="nav_activity" src="img/boton_actividades.png" alt="Actividades" /></li><li><img class="navlink fade" id="nav_howtoget" src="img/boton_como_llegar.png" alt="Cómo llegar" /></li><li><img class="navlink fade" id="nav_description" src="img/boton_descripcion.png" alt="Descripción" /></li><li><img class="navlink fade" id="nav_equipment" src="img/boton_equipamiento.png" alt="Equipamiento" /></li><li><img class="navlink fade" id="nav_services" src="img/boton_servicios.png" alt="Servicios" /></li>').appendTo("#navigation");$(".navlink").bind({click:function(j){$("#sec_description").fadeOut(500);$("#sec_activity").fadeOut(500);$("#sec_howtoget").fadeOut(500);$("#sec_equipment").fadeOut(500);$("#sec_services").fadeOut(500);switch($(this).attr("id")){case"nav_activity":$("#sec_activity").delay(505).fadeIn(500);break;case"nav_description":$("#sec_description").delay(505).fadeIn(500);break;case"nav_howtoget":$("#sec_howtoget").delay(505).fadeIn(500);break;case"nav_equipment":$("#sec_equipment").delay(505).fadeIn(500);break;case"nav_services":$("#sec_services").delay(505).fadeIn(500);break}},mouseenter:function(j){var k=$(this).attr("src");k=k.substring(0,(k.length-4));$(this).fadeOut("fast");$(this).attr("src",k+"hover.png");$(this).fadeIn("fast");$(".info_nav").fadeIn(200);$(".info_nav").html($(this).attr("alt"));$(".info_nav").css("left",j.clientX-30);$(".info_nav").css("top",j.clientY+40)},mouseleave:function(j){var k=$(this).attr("src");k=k.substring(0,(k.length-9));$(this).fadeOut("fast");$(this).attr("src",k+".png");$(this).fadeIn("fast");$(".info_nav").fadeOut(200)}});$('<div class="infodown"></div>').html("").appendTo("body");$('<section id="sec_description"></section>').html("<h3>Descripción</h3><p>"+$(this).find('attribute[name="Descripcion_es"]').find("text").text()+"</p>").appendTo(".infodown");var d="<ul>";$(this).find('attribute[name="Actividades"]').find("array").find("ActividadesAlojamiento").each(function(){d+="<li>"+$(this).text()+"</li>"});d+="</ul>";$('<section id="sec_activity"></section>').html("<h3>Actividades</h3><p>"+d+"</p>").appendTo(".infodown");$("#sec_activity").hide();$('<section id="sec_howtoget"></section>').html("<h3>Cómo llegar</h3><p>"+$(this).find('attribute[name="ComoLlegar"]').find("text").text()+"</p>").appendTo(".infodown");$("#sec_howtoget").hide();var c="<ul>";$(this).find('attribute[name="Equipamiento"]').find("array").find("Equipamiento").each(function(){c+="<li>"+$(this).text()+"</li>"});c+="</ul>";$('<section id="sec_equipment"></section>').html("<h3>Equipamiento</h3><p>"+c+"</p>").appendTo(".infodown");$("#sec_equipment").hide();var h="<ul>";$(this).find('attribute[name="Equipamiento"]').find("array").find("Equipamiento").each(function(){h+="<li>"+$(this).text()+"</li>"});h+="</ul>";$('<section id="sec_services"></section>').html("<h3>Servicios</h3><p>"+h+"</p>").appendTo(".infodown");$("#sec_services").hide();$(".infotop").fadeIn(800);$(".infodown").fadeIn(800);$(".infotop").bind("click",function(j){j.stopPropagation()});$(".infodown").bind("click",function(j){j.stopPropagation()});$(".infotop").css("left",($(window).width()/2)-400+"px");$(".infodown").css("left",($(window).width()/2)-400+"px");$(".infotop").drags();$(".infodown").drags();if(/Android|webOS|iPhone|iPad|iPod|BlackBerry/i.test(navigator.userAgent)){$(".infotop").css("left","5px");$(".infodown").css("left","5px");$(".infotop").css("width",$(window).width()-10+"px");$(".infodown").css("width",$(window).width()-10+"px")}return false}})}})}function showInn(){$(".linked").click(function(a){a.preventDefault();var b=a.target.id;getInn(b)})}function searchProvinces(){$(".linkedprov").click(function(a){a.preventDefault();$("#textbox").val($(this).text());var b=$("#textbox").val();searchc(b)})}function searchc(a){$(".result").remove();$('<div id="loadingGif"></div>').html('<img src="img/ajax-loader.gif" alt="Cargando..." />').appendTo("#maindiv");searching=1;$.ajax({type:"GET",url:"db/inns.xml",dataType:"xml",success:function(c){$("#nullresult").remove();$("#resultnames").remove();$("#resultprovinces").remove();$('<div id="resultnames"></div>').html("<h2>Resultados por nombre</h2>").appendTo("#resultsdiv");$('<div id="resultprovinces"></div>').html("<h2>Resultados por provincia</h2>").appendTo("#resultsdiv");var b=false;var d=false;$(c).find("element").each(function(){var e=$(this).find('attribute[name="Titulo_es"]').find("string").text().toLowerCase();var f=$(this).find('attribute[name="Provincia"]').find("string").text().toLowerCase();var g=$(this).find('attribute[name="Identificador"]').text();if(e.search(a)!=-1){b=true;switch(f){case"salamanca":$('<div class="result sal"></div>').html('<p><span class="resultname"><a href="#" id="'+g+'" class="linked">'+e+'</a></span> <span class="provname"><a href="#" class="linkedprov">'+f+"</a></span></p>").appendTo("#resultnames");break;case"ávila":$('<div class="result avi"></div>').html('<p><span class="resultname"><a href="#" id="'+g+'" class="linked">'+e+'</a></span> <span class="provname"><a href="#" class="linkedprov">'+f+"</a></span></p>").appendTo("#resultnames");break;case"burgos":$('<div class="result bur"></div>').html('<p><span class="resultname"><a href="#" id="'+g+'" class="linked">'+e+'</a></span> <span class="provname"><a href="#" class="linkedprov">'+f+"</a></span></p>").appendTo("#resultnames");break;case"segovia":$('<div class="result seg"></div>').html('<p><span class="resultname"><a href="#" id="'+g+'" class="linked">'+e+'</a></span> <span class="provname"><a href="#" class="linkedprov">'+f+"</a></span></p>").appendTo("#resultnames");break;case"soria":$('<div class="result sor"></div>').html('<p><span class="resultname"><a href="#" id="'+g+'" class="linked">'+e+'</a></span> <span class="provname"><a href="#" class="linkedprov">'+f+"</a></span></p>").appendTo("#resultnames");break;case"valladolid":$('<div class="result val"></div>').html('<p><span class="resultname"><a href="#" id="'+g+'" class="linked">'+e+'</a></span> <span class="provname"><a href="#" class="linkedprov">'+f+"</a></span></p>").appendTo("#resultnames");break;case"palencia":$('<div class="result pal"></div>').html('<p><span class="resultname"><a href="#" id="'+g+'" class="linked">'+e+'</a></span> <span class="provname"><a href="#" class="linkedprov">'+f+"</a></span></p>").appendTo("#resultnames");break;case"zamora":$('<div class="result zam"></div>').html('<p><span class="resultname"><a href="#" id="'+g+'" class="linked">'+e+'</a></span> <span class="provname"><a href="#" class="linkedprov">'+f+"</a></span></p>").appendTo("#resultnames");break;case"león":$('<div class="result leo"></div>').html('<p><span class="resultname"><a href="#" id="'+g+'" class="linked">'+e+'</a></span> <span class="provname"><a href="#" class="linkedprov">'+f+"</a></span></p>").appendTo("#resultnames");break;default:$('<div class="result"></div>').html('<p><span class="resultname"><a href="#" id="'+g+'" class="linked">'+e+'</a></span> <span class="provname"><a href="#" class="linkedprov">'+f+"</a></span></p>").appendTo("#resultnames");break}}if(f.search(a)!=-1){d=true;switch(f){case"salamanca":$('<div class="result sal"></div>').html('<p><span class="provname"><a href="#" class="linkedprov">'+f+'</a></span><span class="resultname"><a href="#" id="'+g+'" class="linked">'+e+"</a></span></p>").appendTo("#resultprovinces");break;case"ávila":$('<div class="result avi"></div>').html('<p><span class="resultname"><a href="#" id="'+g+'" class="linked">'+e+'</a></span> <span class="provname"><a href="#" class="linkedprov">'+f+"</a></span></p>").appendTo("#resultprovinces");break;case"burgos":$('<div class="result bur"></div>').html('<p><span class="resultname"><a href="#" id="'+g+'" class="linked">'+e+'</a></span> <span class="provname"><a href="#" class="linkedprov">'+f+"</a></span></p>").appendTo("#resultprovinces");break;case"segovia":$('<div class="result seg"></div>').html('<p><span class="resultname"><a href="#" id="'+g+'" class="linked">'+e+'</a></span> <span class="provname"><a href="#" class="linkedprov">'+f+"</a></span></p>").appendTo("#resultprovinces");break;case"soria":$('<div class="result sor"></div>').html('<p><span class="resultname"><a href="#" id="'+g+'" class="linked">'+e+'</a></span> <span class="provname"><a href="#" class="linkedprov">'+f+"</a></span></p>").appendTo("#resultprovinces");break;case"valladolid":$('<div class="result val"></div>').html('<p><span class="resultname"><a href="#" id="'+g+'" class="linked">'+e+'</a></span> <span class="provname"><a href="#" class="linkedprov">'+f+"</a></span></p>").appendTo("#resultprovinces");break;case"palencia":$('<div class="result pal"></div>').html('<p><span class="resultname"><a href="#" id="'+g+'" class="linked">'+e+'</a></span> <span class="provname"><a href="#" class="linkedprov">'+f+"</a></span></p>").appendTo("#resultprovinces");break;case"zamora":$('<div class="result zam"></div>').html('<p><span class="resultname"><a href="#" id="'+g+'" class="linked">'+e+'</a></span> <span class="provname"><a href="#" class="linkedprov">'+f+"</a></span></p>").appendTo("#resultprovinces");break;case"león":$('<div class="result leo"></div>').html('<p><span class="resultname"><a href="#" id="'+g+'" class="linked">'+e+'</a></span> <span class="provname"><a href="#" class="linkedprov">'+f+"</a></span></p>").appendTo("#resultprovinces");break;default:$('<div class="result"></div>').html('<p><span class="resultname"><a href="#" id="'+g+'" class="linked">'+e+'</a></span> <span class="provname"><a href="#" class="linkedprov">'+f+"</a></span></p>").appendTo("#resultprovinces");break}}});if(d==false){$("#resultprovinces").remove()}if(b==false){$("#resultnames").remove()}if(d==false&&b==false){$('<div id="nullresult"></div>').html("No se encontraron resultados").appendTo("#resultsdiv")}$("#loadingGif").remove();searching=0;showInn();searchProvinces();$("#footerimg").removeAttr("style");$("#footerimg").css("top",$(".main-container").height()+100);if($(".main-container").height()+100<($(window).height()-$("#footerimg").height())){$("#footerimg").removeAttr("style")}}})}function searchInnByKey(){var b=null;var a=0;$("#textbox").keydown(function(c){if(c.keyCode==10||c.keyCode==13){c.preventDefault()}clearTimeout(b);b=setTimeout(function(){box=jQuery("#textbox").val().toLowerCase();if(box.length>2){if(a!=1){searchc(box)}else{$('<div id="loadingGif"></div>').html('<div class="center"><p>¡Espera a que termine la búsqueda actual!</p><img src="img/ajax-loader.gif" alt="Cargando..." /></div>').appendTo("#maindiv")}}else{$("#footerimg").removeAttr("style");$("#nullresult").html("Búsqueda demasiado corta");$("#resultprovinces").remove();$("#resultnames").remove();$(".result").remove()}},800)});$("#finderb").click(function(c){c.preventDefault()})}function getParameterByName(a){a=a.replace(/[\[]/,"\\[").replace(/[\]]/,"\\]");var c=new RegExp("[\\?&]"+a+"=([^&#]*)"),b=c.exec(location.search);return b==null?"":decodeURIComponent(b[1].replace(/\+/g," "))}$(document).ready(function(){if(detectmob()){$("#apk").css("display","block");$("#closeapk").click(function(b){b.preventDefault();$("#apk").css("display","none")})}$("body").click(function(){if($(".infodown").length){$(".infodown").fadeOut(800,function(){$(".infodown").remove()});$(".infotop").fadeOut(800,function(){$(".infotop").remove()})}});$("img[usemap]").rwdImageMaps();searchInnByKey();var a=getParameterByName("id");if(a!=null){getInn(a)}});