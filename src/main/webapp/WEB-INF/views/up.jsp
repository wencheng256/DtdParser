<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
request.setAttribute("path", basePath);
%>
<!DOCTYPE html>
<html lang="en">
	<head>
		<meta charset="utf-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge">
		<meta name="viewport" content="width=device-width, initial-scale=1">
		<title>Dtd结构图</title>

		<!-- Bootstrap -->
		<link href="css/bootstrap.css" rel="stylesheet">
		<link href="css/font-awesome.css" rel="stylesheet">
		<link href="css/style.css" rel="stylesheet">
		<link rel="stylesheet" href="control/css/zyUpload.css" type="text/css">
		
		<script type="text/javascript" src="${path}js/jquery-1.7.2.js"></script>
		<!-- å¼ç¨æ ¸å¿å±æä»¶ -->
		<script type="text/javascript" src="${path}core/zyFile.js"></script>
		<!-- å¼ç¨æ§å¶å±æä»¶ -->
		<script type="text/javascript" src="${path}control/js/zyUpload.js"></script>
		<!-- å¼ç¨åå§åJS -->
		<script type="text/javascript" src="${path}js/demo.js"></script>

		<!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
		<!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
		<!--[if lt IE 9]>
		  <script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
		  <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
		<![endif]-->
	</head>
	<body>
		<div class="home">
			<div class="container">			
				<header>
					<h1 style="font-family:微软雅黑;">DTD结构图生成器
						<span style="line-height:40px;">www.wencheng.win/dtd</span>
					</h1>	
				</header> 
			</div>
		</div>

		<div id="demo" class="demo"></div>  
	
	
	</body>
</html>