<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>   
<!DOCTYPE html>
<html>
<head>
<meta charset="EUC-KR">
<title>Insert title here</title>
<script src="http://code.jquery.com/jquery-1.10.2.js"></script>
<style>
/**Global CSS**/
*{
	margin : 0;
	pading : 0;
}
li{
 	list-style : none;
}
a{
	text-decoration: none;
}
table {
	margin : 0 auto;
	text-align : center;
}
fieldset {
    border: 5px solid #f0c413;
    background: white;
    border-radius: 5px;
    padding: 15px;
    margin : 0 auto;
    width : 160px;
}


/** Header CSS**/
header{
	width : 900px;
	height : 300px;
	margin : 0 auto;
	background : #f0c413;  /*  #EB613B; */
	position : relative;
	/* border-left : solid #f0c413 1px;
	border-right : solid #f0c413 1px; */
}

/** Nav CSS**/
nav{
	width : 900px;
	height : 40px;
	margin : 0 auto;
	background : black;
	/* border : dashed #f0c413 2px; */
	line-height : 40px;
	color : white;
}

/** Section CSS**/
section {
	width : 900px;
/* 	height : 600px; */
	margin : 0 auto;
	background : gray;
}
#left{
	width : 200px;
/* 	height : 600px; */
	float : left;
	background : #fae6c3; /*  #f5e9dc; */
}
#left>div{
	margin : 10px 10px;
}

#center{
	width : 700px;
/* 	height : 600px; */
	float : left;
	
	background : white;
}

/** Footer CSS**/
footer{
	width : 900px;
	height : 50px;
	margin : 0 auto;
	background : black;
	color : white;
	line-height : 50px;
}


</style>
<script>
function makeHeight(){
	var section = document.querySelector("#section");
	var left = document.querySelector("#left");
	var center = document.querySelector("#center");
	
	var mHeight = center.clientHeight;
	/* ClientHeight를 써서 표 높이만큼 늘리게 함. */
	if(mHeight < 400){
		mHeight = 400;
	}
	center.style.height = mHeight + 'px';
	section.style.height = mHeight + 'px';
	left.style.height = mHeight + 'px';
}
window.onload = function(){
	makeHeight();
};

</script>
</head>
<body>
<header>
<jsp:include page="${top}"/>
</header>
<nav>${nav}</nav>
<section id="section">
<aside id="left">
<jsp:include page="${left}"/>
</aside>
<aside id="center">
<jsp:include page="${center}"/>
</aside>
</section>
<footer>
</footer>
</body>
</html>