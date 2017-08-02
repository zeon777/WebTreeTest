<%--
  Created by IntelliJ IDEA.
  User: zeon
  Date: 25.07.2017
  Time: 21:43
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head><TITLE>Tree Example</TITLE>
    <link rel="stylesheet" href="tree.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
    <script src="script.js"></script>
    <script>
        onload = function() { tree("root", "/ajaxList") }

    </script>
</head>
<body>

WebTree:
<p></p>
<p></p>
<div id="aboveRoot">
    <button id='AddNode'>Add node</button><button id='DeleteNode'>Delete node</button></div>
<ul class="Container" id="root">
    <li id="0" class="Node IsRoot IsLast ExpandClosed noSelect">
        <div class="Expand"></div>
        <div class="Content"><span class="NameText">Каталог</span></div>
        <ul class="Container">
        </ul>
    </li>
</ul>



</body>
</html>