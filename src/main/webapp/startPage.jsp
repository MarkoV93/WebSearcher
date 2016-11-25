<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%--
  Created by IntelliJ IDEA.
  User: Marko
  Date: 22.11.2016
  Time: 14:13
  To change this template use File | Settings | File Templates.
--%>


<html>
<head>
    <title>Title</title>
    <script src="<c:url value="/resource/js/ajaxForController.js" />"></script>
    <script src="<c:url value="/resource/js/jquery.min.js" />"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
</head>
<body>
<div class="col-sm-12 col-md-12 col-lg-12 ">
    <div class="container">
        <h2>Simple web searcher</h2>
        <p>Test task in developEx company</p>
        <div class="progress">
            <div class="bar progress-bar progress-bar-striped active" id="progress-bar" role="progressbar"
                 aria-valuenow="1" aria-valuemin="0" aria-valuemax="100" style="width:1%">
                <p style="margin:0px" id="resul_load">&#160</p>
            </div>
        </div>
    </div>
    <br>
    <div class="col-sm-4 col-md-4 col-lg-4 ">
        URL
        <br>
        <input type="text" id="url">
        <br>
        word
        <br>
        <input type="text" id="word">
        <br>
        Count of threads
        <br>
        <input type="text" id="countOfThreads">
        <br>
        Count of pages
        <br>
        <input type="text" id="countOfLinks">

        <br>
        <input type="button" value="start" onclick="start()">
        <input type="button" value="stop" onclick="stop()">
        <input type="button" value="pause" onclick="pause()">
        <p style="color: red ;margin:0px" id="warning_text">&#160</p>
    </div>
    <div style="font-weight: bold;" class="col-sm-5 col-md-5 col-lg-5 ">
        <p style="color: green ;margin:0px" id="result_text">&#160</p>
    </div>
</div>
</body>
</html>
