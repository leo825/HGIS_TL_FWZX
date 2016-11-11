/**
 * 实时监控
 * 
 * @type {{}}
 */

var RealtimeMonitoring = {

	chatOptions : {
		chart : {
			renderTo : 'container',
			type : 'spline',
			animation : Highcharts.svg, // don't animate in old IE
			marginRight : 10,
			events : {
				load : function() {
					// set up the updating of the chart each second
					// var series = this.series[0];
					// setInterval(function() {
					// var x = (new Date()).getTime(), // current time
					// y = Math.random();
					// series.addPoint([x, y], true, true);
					// }, 1000);
					var series = this.series[0];

					RealtimeMonitoring.pageTimer["timer1"]  = setInterval(
							function() {
								var projectId = $(
										'.js-monitor-manage .js-projects')
										.val();
								projectId = Public.isNull(projectId) ? ""
										: projectId;
								Public
										.getRest(
												'/operation/get_coordinate?projectId='
														+ projectId,
												function(dicts) {
													var projectName = dicts.projectName;
													if (!Public
															.isNull(projectName)) {
														RealtimeMonitoring.chatOptions.title.text = projectName
																+ "实时访问数据";
													} else {
														RealtimeMonitoring.chatOptions.title.text = "所有应用访问数据";
													}
													var x = dicts.x;
													var y = dicts.y;
													series.addPoint([
															(new Date())
																	.getTime(),
															y ], true, true);
												});
							}, 2000);
				}
			}
		},
		title : {
			text : '所有应用访问数据'
		},
		xAxis : {
			type : 'datetime'
		},
		yAxis : {
			title : {
				text : '次数'
			},
			allowDecimals : false,
			plotLines : [ {
				value : 0,
				width : 1,
				color : '#808080'
			} ]
		},
		tooltip : {
			formatter : function() {
				return '<b>' + this.series.name + '</b><br/>'
						+ Highcharts.dateFormat('%Y-%m-%d %H:%M:%S', this.x)
						+ '<br/>' + Highcharts.numberFormat(this.y, 1);
			}
		},
		legend : {
			enabled : false
		},
		exporting : {
			enabled : false
		},
		series : [ {
			name : '访问次数',
			data : (function() {
				// generate an array of random data
				var data = [], time = (new Date()).getTime(), i;

				for (i = -9; i <= 0; i += 1) {
					data.push({
						x : time + i * 2000,
						y : 0
					});
				}
				return data;
			}())
		} ],
		exporting : {
			enabled : true
		},
		credits : {
			enabled : false
		}
	},
	chart : {},
	pageTimer : {}

};
/**
 * 初始化
 */
RealtimeMonitoring.init = function() {
	$('#main-view').load('partials/monitor/realtime_monitoring.html',
			function() {
				RealtimeMonitoring.render();
			});
};
/**
 * 渲染页面
 */
RealtimeMonitoring.render = function() {
	RealtimeMonitoring.loadData();
	RealtimeMonitoring.loadProjects();
};
/**
 * 加载数据
 */
RealtimeMonitoring.loadData = function() {
	Highcharts.setOptions({
		global : {
			useUTC : false
		}
	});
	
	for(var timer in RealtimeMonitoring.pageTimer){
	    clearInterval(RealtimeMonitoring.pageTimer[timer]);
	}
	
	RealtimeMonitoring.chart = new Highcharts.Chart(
			RealtimeMonitoring.chatOptions);
};

/**
 * 查看应用信息
 */
RealtimeMonitoring.show = function() {
	var message = 'message';
	return message;
};

/**
 * 获取查询参数列表
 */
RealtimeMonitoring.getSearchParams = function() {
	var name = $('.js-monitor-manage .js-name').val();
	var type = $('.js-monitor-manage .js-type').val();
	var params = [];
	if (!Public.isNull(name)) {
		params.push('name=' + encodeURI(name));
	}
	if (!Public.isNull(type)) {
		params.push('type=' + type);
	}
	return params;
};

/**
 * 检查应用表单，并获取数据
 */
RealtimeMonitoring.checkBoardroomForm = function() {
	var name = $('.p-add-project input[name="name"]').val();
	var ip = $('.p-add-project input[name="ip"]').val();
	var port = $('.p-add-project input[name="port"]').val();
	var description = $('.p-add-project input[name="description"]').val();
	var provider = $('.p-add-project input[name="provider"]').val();
	if (Public.isNull(name)) {
		Public.alert('应用名称不能为空');
		return null;
	} else if (Public.isIp(ip)) {
		Public.alert('应用ip不合法');
		return null;
	} else if (Public.isInt(port)) {
		Public.alert("端口应该是数字");
		return null;
	} else if (Public.isNull(provider)) {
		Public.alert("提供者不能为空");
		return null;
	}
	var obj = {};
	obj.name = name;
	obj.ip = ip;
	obj.port = port;
	obj.description = Public.isNull(description) ? '' : description;
	obj.provider = provider;
	return obj;
};

/**
 * 初始化表单数据
 */
var timer;
RealtimeMonitoring.initFormData = function() {
	var series = RealtimeMonitoring.chart.series[0];
	clearInterval("RealtimeMonitoring.getCoordinate()");
	if (timer != null) {
		clearTimeout(timer);
	}
	timer = setInterval("RealtimeMonitoring.getCoordinate()", 2000);
};

/**
 * 根据应用projectId来获取访问量
 * 
 * 
 */
RealtimeMonitoring.getCoordinate = function() {
	var projectId = $('.js-monitor-manage .js-projects').val();
	projectId = Public.isNull(projectId) ? "" : projectId;
	Public.getRest('/operation/get_coordinate?projectId=' + projectId,
			function(dicts) {
				var projectName = dicts.projectName;
				if (!Public.isNull(projectName)) {
					RealtimeMonitoring.chatOptions.title.text = projectName
							+ "实时访问数据";
				} else {
					RealtimeMonitoring.chatOptions.title.text = "所有应用访问数据";
				}
				var x = dicts.x;
				var y = dicts.y;
				RealtimeMonitoring.chart.series[0].addPoint([ x, y ], true,
						true);
			});
};

/**
 * 加载所有应用
 * 
 */
RealtimeMonitoring.loadProjects = function() {
	Public.getRest('/project', function(dicts) {
		if (dicts != null && dicts.length > 0) {
			var html = [];
			html.push('<option value="">全部应用</option>');
			for ( var i = 0, j = dicts.length; i < j; i++) {
				var dict = dicts[i];
				html.push('<option value="' + dict.id + '">' + dict.name
						+ '</option>');
			}
			$('.js-projects').html(html.join(""));
		} else {
			$('.js-projects').html('');
		}
	});

};