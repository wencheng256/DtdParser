<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
request.setAttribute("path", path);
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

	</head>
	<body>
		<div class="home">
			<div class="container">			
				<header>
					<h1 style="font-family:微软雅黑;">DTD结构图生成器
						<span style="line-height:40px;">www.mario256.cn</span>
					</h1>	
					<br>
					<div>
					<i style="color:#666;font-weight:normal;"> 宽: </i> <input type="text" name="width" id="width"/>
					<i style="color:#666;font-weight:normal;"> 高: </i> <input type="text" name="height" id="height"/>
					<button onclick="go()">设置</button>
					</div>
				</header> 
			</div>
		</div>
		
		<div>
			
		</div>
		
<!-- 为ECharts准备一个具备大小（宽高）的Dom -->
    <div id="main" style="height:800px"></div>
    <!-- ECharts单文件引入 -->
    <script src="${path}resources/js/echarts-all.js"></script>

    <script type="text/javascript">
        // 路径配置
		var myChart = echarts.init(document.getElementById('main'));
		var option = {
			title : {
				text: 'DTD展示'
			},
			toolbox: {
				show : true,
				feature : {
					mark : {show: true},
					dataView : {show: false, readOnly: true},
					restore : {show: true},
					saveAsImage : {show: true}
				}
			},
			series : [
				{
					name:'树图',
					type:'tree',
					orient: 'horizontal',  // vertical horizontal
					rootLocation: {x: 100,y: 230}, // 根节点位置  {x: 100, y: 'center'}
					nodePadding: ${height},
					layerPadding: ${width},
					hoverable: true,
					roam: true,
					symbolSize: 10,
					itemStyle: {
						normal: {
							color: '#4883b4',
							label: {
								show: true,
								position: 'right',
								formatter: "{b}",
								textStyle: {
									color: '#000',
									fontSize: 10
								}
							},
							lineStyle: {
								color: '#ccc',
								type: 'broken' // 'curve'|'broken'|'solid'|'dotted'|'dashed'

							}
						},
						emphasis: {
							color: '#4883b4',
							label: {
								show: true
							},
							borderWidth: 0
						}
					},

					data: [
						${json}
					]
				}
			]
		};
		myChart.setOption(option);

        function go(){
        	var lo = window.location.origin+window.location.pathname+window.location.search.split("&")[0];
        	var width = document.getElementById("width");
        	var height = document.getElementById("height");
        	if(width.value != ""){
        		lo+="&width="+width.value;
        	}
        	if(height.value!=""){
        		lo+="&height="+height.value;
        	}
        	//console.info(lo);
        	window.location.href= lo;
        }
    </script>
	
	</body>
</html>