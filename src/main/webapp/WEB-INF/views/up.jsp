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
		<link href="${path}/resources/css/bootstrap.css" rel="stylesheet">
		<link href="${path}/resources/css/font-awesome.css" rel="stylesheet">
		<link href="${path}/resources/css/style.css" rel="stylesheet">
		<link rel="stylesheet" href="${path}/resources/control/css/zyUpload.css" type="text/css">
		
		<script type="text/javascript" src="${path}/resources/js/jquery-1.7.2.js"></script>
		<script type="text/javascript" src="${path}/resources/core/zyFile.js"></script>
		<script type="text/javascript" src="${path}/resources/control/js/zyUpload.js"></script>

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
						<span style="line-height:40px;">www.mario256.cn</span>
					</h1>	
				</header> 
			</div>
		</div>

		<div id="demo" class="demo"></div>

		<script>
			$(function(){
				// 初始化插件
				$("#demo").zyUpload({
					width            :   "650px",                 // 宽度
					height           :   "400px",                 // 宽度
					itemWidth        :   "120px",                 // 文件项的宽度
					itemHeight       :   "100px",                 // 文件项的高度
					url              :   "${path}/to_upload",  // 上传文件的路径
					multiple         :   true,                    // 是否可以多个文件上传
					dragDrop         :   true,                    // 是否可以拖动上传文件
					del              :   true,                    // 是否可以删除文件
					finishDel        :   false,  				  // 是否在上传文件完成后删除预览
					/* 外部获得的回调接口 */
					onSelect: function(files, allFiles){                    // 选择文件的回调方法
						console.info("当前选择了以下文件：");
						console.info(files);
						console.info("之前没上传的文件：");
						console.info(allFiles);
					},
					onDelete: function(file, surplusFiles){                     // 删除一个文件的回调方法
						console.info("当前删除了此文件：");
						console.info(file);
						console.info("当前剩余的文件：");
						console.info(surplusFiles);
					},
					onSuccess: function(file){                    // 文件上传成功的回调方法
						console.info("此文件上传成功：");
						console.info(file);
					},
					onFailure: function(file){                    // 文件上传失败的回调方法
						console.info("此文件上传失败：");
						console.info(file);
					},
					onComplete: function(responseInfo){           // 上传完成的回调方法
						console.info("文件上传完成");
						console.info(responseInfo);
					}
				});
			});
		</script>
	</body>
</html>