
function tree(id, url) {
    var element = document.getElementById(id)
    var selectedID = null;  //выбранный узел
    var editName= "";       //имя узла до редактирования, для проверки были ли изменения если он редактировался

    function hasClass(elem, className) {
        return new RegExp("(^|\\s)"+className+"(\\s|$)").test(elem.className)
    }

    function selectNode(node,on) {
        //Если надо снять значение о выдилении с редактируемого нода то проверяем не изменился ли он
        if(!on && node.getElementsByTagName('span')[0].hasAttribute('contenteditable')){
            var nametext = node.getElementsByTagName('span')[0];
                nametext.removeAttribute('contenteditable');
            //Сравниваем имена до и после редактирования
            if(editName!=nametext.innerHTML){
                //Отправляем новое имя на сервер
                ajaxUpdate(nametext.innerHTML,node.id,"/ajaxUpdate")
            }
        }

        var newClass = on ? 'select' : 'noSelect'
        // регексп находит отдельно стоящий select|noSelect и меняет на newClass
        var re =  /(^|\s)(select|noSelect)(\s|$)/
        node.className = node.className.replace(re, '$1'+newClass+'$3')
    }


    function toggleNode(node) {
        function showLoading(on) {

            var expand = node.getElementsByTagName('DIV')[0]
            expand.className = on ? 'ExpandLoading' : 'Expand'
        }
        // определить новый класс для узла


        if(hasClass(node, 'ExpandClosed')) {showLoading(true);setTimeout(toggleNodeContin,2000)}
        //если узел закрыт то вставляем задержку в 2 секунды перед его открытием
        else toggleNodeContin();
        function toggleNodeContin() {  //непосредственное открытие|закрытие узла
            showLoading(false)
            var newClass = hasClass(node, 'ExpandOpen') ? 'ExpandClosed' : 'ExpandOpen'
            // заменить текущий класс на newClass
            // регексп находит отдельно стоящий open|close и меняет на newClass
            var re =  /(^|\s)(ExpandOpen|ExpandClosed)(\s|$)/
            node.className = node.className.replace(re, '$1'+newClass+'$3')
        }

    }
//Загрузка узла с сервера и отрисовка на странице
    function load(node,reload,id) {

        function onSuccess(data) {
            if (!data.errcode) {
                onLoaded(data)
            } else {
                onLoadError(data)
            }
        }


        function onAjaxError(xhr, status){
            showLoading(false)
            var errinfo = { errcode: status }
            if (xhr.status != 200) {
                // может быть статус 200, а ошибка
                // из-за некорректного JSON
                errinfo.message = xhr.statusText
            } else {
                errinfo.message = 'Некорректные данные с сервера'
            }
            onLoadError(errinfo)
        }


        function onLoaded(data) {

            for(var i=0; i<data.length; i++) {    //Создание новых узлов на основе даных от AJAX
                var child = data[i];
                var li = document.createElement('LI');
                li.id = child.id;

                li.className = "Node Expand" + (child.leaf ? 'Closed' : 'Leaf')+" noSelect"
                if (i == data.length-1) li.className += ' IsLast'

                li.innerHTML = '<div class="Expand"></div><div class="Content"><span class="NameText">'+child.name+'</span></div>'
                if (child.leaf) {
                    li.innerHTML += '<ul class="Container"></ul>'
                }
                node.getElementsByTagName('UL')[0].appendChild(li)
            }

            if(data.length==0 && selectedID!=0)
            {
                var re =  /(^|\s)(ExpandOpen|ExpandClosed)(\s|$)/
                node.className = node.className.replace(re, '$1'+'ExpandLeaf'+'$3');

            }

            node.isLoaded = true;
            if(!reload)
            { toggleNode(node);}
            if(id!=null)
            { var nodetoopen =document.getElementById(id);
                load(nodetoopen);
            }
        }

        function onLoadError(error) {
            var msg = "Ошибка "+error.errcode
            if (error.message) msg = msg + ' :'+error.message
            alert(msg)
        }


        $.ajax({
            url: url,
            type: 'GET',
            data: ({id:node.id}),
            dataType: "json",
            success: onSuccess,
            error: onAjaxError,
            cache: false
        })

    }




       //Обновление имени узла на сервере
    function ajaxUpdate(NodeName,NodeId,url) {
        $.ajax({
            url: url,
            type: 'POST',
            data: ({id:NodeId,name:NodeName}),
            dataType: "json",
            success: onSuccess,
            error: onAjaxError,
            cache: false

        });
        function onSuccess() {
            return true;
        }
        function onAjaxError() {
            alert("AjaxError");
            return false;
        }
    }




    //Добавление или удаление узла
    function addOrDeleteNode(url,id,name) {


        if(id==0)
        {var deletedNode= document.getElementById(id).parentNode.getElementsByTagName('UL')[0];
        }
        else
        {var deletedNode =document.getElementById(id).parentNode;
         selectedID=null;}
        var reloadnode = deletedNode.parentNode;
        deletedNode.innerHTML = '';

        $.ajax({
            url: url,
            type: 'POST',
            data: ({id:id,name:name}),
            dataType: "json",
            success: onSuccess,
            error: onAjaxError,
            cache: false

        });
        function onSuccess() {
            if(name!=null&&id!=0)
            {load(reloadnode,true,id);}
            else
            {load(reloadnode,true);}
        }
        function onAjaxError() {
            alert("AjaxError");
            return false;
        }


    }



        // определение действия при клике на элементе дерева:
        //переключение выбранного узла если клик по тексту или разворачивание узла если клик по иконке

    element.onclick = function(event) {
        event = event || window.event;
        var clickedElem = event.target || event.srcElement;



        if (!(hasClass(clickedElem, 'Expand')||hasClass(clickedElem, 'NameText'))) {
            return // клик не там
        }

        // Node, на который кликнули
        var node;
        if (hasClass(clickedElem, 'NameText')) {
            node = clickedElem.parentNode.parentNode;
        }
        else {
            node = clickedElem.parentNode;
        }


        if(selectedID!=node.id ){  //Установка нового выбранного узла если он уже не выбран
        if(selectedID!=null){selectNode(document.getElementById(selectedID),false);}
        selectNode(node,true);
            selectedID=node.id;
        }



        if (hasClass(clickedElem, 'NameText')) {
            return //клик на тексте ничего кроме выделения не нужно
        }


        if (hasClass(node, 'ExpandLeaf')) {

            return // клик на листе
        }

        if (node.isLoaded || node.getElementsByTagName('LI').length) {
            // Узел уже загружен через AJAX(возможно он пуст)
            toggleNode(node);
            return
        }


        if (node.getElementsByTagName('LI').length) {
            // Узел не был загружен при помощи AJAX, но у него почему-то есть потомки
            // Например, эти узлы были в DOM дерева до вызова tree()
            // ничего подгружать не надо
            toggleNode(node);
            return
        }


        // загрузить узел
        load(node)
    }




    element.ondblclick = function (event) {     //Проверка ДВОЙНОГО клика для включения редактирования имени нода
        event = event || window.event;
        var clickedElem = event.target || event.srcElement;
         if (!hasClass(clickedElem, 'NameText')) {

        return // клик не там
    }

        var node = clickedElem.parentNode.parentNode; // Node, на который кликнули


        if(selectedID!=node.id ){  //Установка нового выбранного узла если он уже не выбран
            if(selectedID!=null){selectNode(document.getElementById(selectedID),false);}
            selectNode(node,true);
            selectedID=node.id;
        }


        var nametext = node.getElementsByTagName('span')[0];
        if(!nametext.hasAttribute('contenteditable')){     //добавляем атрибут для возможности ввода текста
           editName= nametext.innerHTML;
        nametext.setAttribute('contenteditable',"true");}

    }


    document.onclick = function (event) { //проверка нажатия кнопок находящихся вне элемента tree
        event = event || window.event;
        var clickedElem = event.target || event.srcElement;
        if(clickedElem.id=='AddNode'){
            clickedElem.parentNode.innerHTML="<form><input id='input' type=\"text\" size=\"20\"><input id='formOk' type=\"submit\" value=\"Ok\"></form>";
            ; return}
        if(clickedElem.id=='DeleteNode' && selectedID!=null) {addOrDeleteNode('/ajaxAddDelete',selectedID); return}
        if(clickedElem.id=='formOk' && selectedID!=null){
            var newName=document.getElementById('input').value;
            document.getElementById('aboveRoot').innerHTML="<button id='AddNode'>Add node</button><button id='DeleteNode'>Delete node</button>";
            addOrDeleteNode('/ajaxAddDelete',selectedID,newName);
        }
    }
}
