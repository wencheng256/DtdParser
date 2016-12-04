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
		<link href="${path}css/bootstrap.css" rel="stylesheet">
		<link href="${path}css/font-awesome.css" rel="stylesheet">
		<link href="${path}css/style.css" rel="stylesheet">

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
    <script src="${path }js/echarts.js"></script>
    <script type="text/javascript">
        // 路径配置
        require.config({
            paths: {
                echarts: '${path }js'
            }
        });
        
        // 使用
        require(
            [
                'echarts',
                'echarts/chart/tree' // 使用柱状图就加载bar模块，按需加载
            ],
            function (ec) {
                // 基于准备好的dom，初始化echarts图表
                var myChart = ec.init(document.getElementById('main')); 
                
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
        
                // 为echarts对象加载数据 
                myChart.setOption(option); 
            }
        );
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