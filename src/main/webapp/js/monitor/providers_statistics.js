/**
 * 提供者统计
 * @type {{}}
 */
var ProvidersStatistics = {
	    chatOptions1 : {
	            chart: {
	            	renderTo: 'container1',
	                zoomType: 'xy'
	            },
	            title: {
	                text: '2016年内提供应用统计'
	            },
	            xAxis: [{
	                categories: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun',
	                    'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'],
	                crosshair: false
	            }],
	            yAxis: [{ // Primary yAxis
	            	allowDecimals: false,
	            	tickInterval: 1,
	                title: {
	                    text: '提供者',
	                    style: {
	                        color: Highcharts.getOptions().colors[1]
	                    }
	                },
	                labels: {
	                    format: '{value}',
	                    style: {
	                        color: Highcharts.getOptions().colors[1]
	                    }
	                }
	            }, { // Secondary yAxis
	            	tickInterval: 1,
	                title: {
	                    text: '提供数量',
	                    style: {
	                        color: Highcharts.getOptions().colors[0]
	                    }
	                },
	                labels: {
	                    format: '{value}',
	                    style: {
	                        color: Highcharts.getOptions().colors[0]
	                    }
	                }
	            }],
	            tooltip: {
	                shared: true
	            },
	            legend: {
	                layout: 'vertical',
	                align: 'left',
	                x: 120,
	                verticalAlign: 'top',
	                y: 100,
	                floating: true,
	                backgroundColor: (Highcharts.theme && Highcharts.theme.legendBackgroundColor) || '#FFFFFF'
	            },
	            series: [{
	                name: '提供数量',
	                type: 'column',
	                //yAxis: 1,
	                data: [1, 2, 3, 4, 5, 1, 0, 0, 0, 0, 0, 0],
	                tooltip: {
	                    valueSuffix: ' 位'
	                }

	            }, {
	                name: '提供者',
	                type: 'spline',
	                data: [1, 1, 3, 4, 2, 1, 0, 0, 0, 0, 0, 0],
	                tooltip: {
	                    valueSuffix: ' 个'
	                }
	            }],
	            exporting:{
                    enabled:true
                },
                credits: {
                    enabled: false
                }
	        },
	    chatOptions2:{
	        chart: {
	        	renderTo: 'container2',
	            plotBackgroundColor: null,
	            plotBorderWidth: null,
	            plotShadow: false,
	            type: 'pie'
	        },
	        title: {
	            text: '2015提供应用所占比例'
	        },
	        tooltip: {
	            pointFormat: '{series.name}: <b>{point.percentage:.1f}%</b>'
	        },
	        plotOptions: {
	            pie: {
	                allowPointSelect: true,
	                cursor: 'pointer',
	                dataLabels: {
	                    enabled: true,
	                    format: '<b>{point.name}</b>: {point.percentage:.1f} %',
	                    style: {
	                        color: (Highcharts.theme && Highcharts.theme.contrastTextColor) || 'black'
	                    }
	                }
	            }
	        },
	        series: [{
	            name: 'Brands',
	            colorByPoint: true,
	            data: [{
	                name: 'company1',
	                y: 56.33
	            }, {
	                name: 'company2',
	                y: 24.03,
	                sliced: true,
	                selected: true
	            }, {
	                name: 'company3',
	                y: 10.38
	            }, {
	                name: 'company4',
	                y: 4.77
	            }, {
	                name: 'company5',
	                y: 0.91
	            }, {
	                name: '其他',
	                y: 0.2
	            }]
	        }],
            exporting:{
                enabled:true
            },
            credits: {
                enabled: false
            }
	    }
};
/**
 * 初始化
 */
ProvidersStatistics.init = function () {
    $('#main-view').load('partials/monitor/providers_statistics.html', function(){
        ProvidersStatistics.render();
    });
};
/**
 * 渲染页面
 */
ProvidersStatistics.render = function(){
    ProvidersStatistics.loadData();
};
/**
 * 加载数据
 */
ProvidersStatistics.loadData = function () {
    Public.getRest('/project/get_providers_statistics?year=2016', function(chats){
        if(chats != null){
			ProvidersStatistics.chatOptions1.title.text = chats.chat1.title;
			ProvidersStatistics.chatOptions1.series[0].data = chats.chat1.data0;
			ProvidersStatistics.chatOptions1.series[1].data = chats.chat1.data1;
			ProvidersStatistics.chatOptions2.title.text = chats.chat2.title;
			ProvidersStatistics.chatOptions2.series[0].data = chats.chat2.data;
            var chart1 = new Highcharts.Chart(ProvidersStatistics.chatOptions1);
            var chart2 = new Highcharts.Chart(ProvidersStatistics.chatOptions2);
        } else {
            $('.js-project-manage .js-type').html('');
        }
    });
};

/**
 * 修改应用信息
 */
ProvidersStatistics.update = function (id, e) {
    Public.stopPropagation(e);
    Public.createDialog('修改应用', '', 'p-add-project', 300, 300);
    $('.p-add-project .p-dialog-content').load('partials/system/form_boardroom.html', function () {
        ProvidersStatistics.showBoardroomInfo(id);
    });

    //修改应用信息
    $('.p-add-project .js-ok').unbind('click').bind('click', function(){
        var boardroom = ProvidersStatistics.checkBoardroomForm();
        if(boardroom != null){
            boardroom.id = id;
            Public.putRest('/boardroom', boardroom, function (resp) {
                Public.msg('修改成功');
                $('.p-add-project').dialog('destroy');
                //重新加载数据
                ProvidersStatistics.loadData();
            });
        }
    });
};

/**
 * 获取查询参数列表
 */
ProvidersStatistics.getSearchParams = function () {
    var name = $('.js-project-manage .js-name').val();
    var type = $('.js-project-manage .js-type').val();
    var params = [];
    if(!Public.isNull(name)){
        params.push('name='+encodeURI(name));
    }
    if(!Public.isNull(type)){
        params.push('type='+type);
    }
    return params;
};

/**
 * 检查应用表单，并获取数据
 */
ProvidersStatistics.checkBoardroomForm = function () {
    var name = $('.p-add-project input[name="name"]').val();
    var ip = $('.p-add-project input[name="ip"]').val();
    var port = $('.p-add-project input[name="port"]').val();
    var description =  $('.p-add-project input[name="description"]').val();
    var provider = $('.p-add-project input[name="provider"]').val();
    if(Public.isNull(name)){
        Public.alert('应用名称不能为空');
        return null;
    }else if(Public.isIp(ip)){
    	Public.alert('应用ip不合法');
    	return null;
    }else if(Public.isInt(port)){
    	Public.alert("端口应该是数字");
    	return null;
    }else if(Public.isNull(provider)){
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
ProvidersStatistics.initFormData = function(initBoardroomTypeValue, initNetworkValue, initOrgValue){
    //加载应用类型数据
//    ProjectManage.loadBoardroomTypeData('.p-add-project select[name="type"]', initBoardroomTypeValue);
//    //加载网络拓扑数据
//    ProjectManage.loadNetworkData('.p-add-project select[name="network"]', initNetworkValue);
//    //加载组织机构数据
//    ProjectManage.loadOrgTreeData('.p-add-project .js-org-tree', initOrgValue);
};

/**
 * 加载应用类型数据
 */
ProvidersStatistics.loadBoardroomTypeData = function(el, initValue){
    Public.getRest('/dictionary/query?group=BOARDROOM_TYPE', function(dicts){
        if(dicts != null && dicts.length > 0){
            var html = [];
            for(var i= 0,j=dicts.length; i<j; i++){
                var dict = dicts[i];
                html.push('<option value="'+dict.id+'">'+dict.name+'</option>');
            }
            $(el).html(html.join(""));
        } else {
            $(el).html('');
        }
        if(initValue != null){
            $(el).val(initValue);
        }
    });
};

/**
 * 加载网络拓扑数据
 */
ProvidersStatistics.loadNetworkData = function(el, initValue){
    //Public.getRest('/dictionary/query?group=BOARDROOM_TYPE', function(dicts){
    $.getJSON('json/network_list.json', function(nets){
        if(nets != null && nets.length > 0){
            var html = [];
            for(var i= 0,j=nets.length; i<j; i++){
                var net = nets[i];
                html.push('<option value="'+net.id+'">'+net.name+'</option>');
            }
            $(el).html(html.join(""));
        } else {
            $(el).html('');
        }
        if(initValue != null){
            $(el).val(initValue);
        }
    });
};

/**
 * 加载组织机构树数据
 */
ProvidersStatistics.loadOrgTreeData = function(el, initValue){
    var node = $(el);
    node.tree({
        //url:'json/sys-org-tree.json',
        url: URI+'/rest/organization/subs',
        onLoadSuccess: function(){
            var root = node.tree('getRoot');
            if (root != null) {
                node.tree('expand', root.target);
                node.tree('select', root.target);
            }
            if(initValue != null){
                var initNode = node.tree('find', initValue);
                if(initNode != null){
                    node.tree('select', initNode.target);
                }
            }
        }
    });
};

/**
 * 显示应用信息
 * @param id
 */
ProvidersStatistics.showBoardroomInfo = function (id) {
    Public.getRest('/boardroom/'+id, function(room){
        var type = room.type;
        var org = room.organization;
        var net = room.network;
        var typeId = type != null ? type.id : '';
        var orgCode = org != null ? org.code : '';
        ProvidersStatistics.initFormData(typeId, net, orgCode);
        $('.p-add-project input[name="name"]').val(room.name);
    });
};
