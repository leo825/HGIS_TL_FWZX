/**
 * 使用者统计
 * @type {{}}
 */
var UsagesStatistics = {
		chatOptions : {
		        chart: {
		        	renderTo: 'container',
		            type: 'pie',
		            options3d: {
		                enabled: true,
		                alpha: 45,
		                beta: 0
		            }
		        },
		        title: {
		            text: '2016年调用者统计'
		        },
		        tooltip: {
		            pointFormat: '{series.name}: <b>{point.percentage:.1f}%</b>'
		        },
		        plotOptions: {
		            pie: {
		                allowPointSelect: true,
		                cursor: 'pointer',
		                depth: 35,
		                dataLabels: {
		                    enabled: true,
		                    format: '{point.name}'
		                }
		            }
		        },
		        series: [{
		            type: 'pie',
		            name: '应用使用比例',
		            data: [
		                ['Firefox', 45.0],
		                ['IE', 26.8],
		                {
		                    name: 'Chrome',
		                    y: 12.8,
		                    sliced: true,
		                    selected: true
		                },
		                ['Safari', 8.5],
		                ['Opera', 6.2],
		                ['Others', 0.7]
		            ]
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
UsagesStatistics.init = function () {
    $('#main-view').load('partials/monitor/usage_statistics.html', function(){
        UsagesStatistics.render();
    });
};
/**
 * 渲染页面
 */
UsagesStatistics.render = function(){
    UsagesStatistics.loadData();
};
/**
 * 加载数据
 */
UsagesStatistics.loadData = function () {
    Public.getRest('/project/get_usages_statistics', function(chat){
    	console.log(chat);
        if(chat != null){
        	UsagesStatistics.chatOptions.title.text = chat.title;
        	UsagesStatistics.chatOptions.series[0].data = chat.data;
            var chart = new Highcharts.Chart(UsagesStatistics.chatOptions);
        } else {
            $('.js-project-manage .js-type').html('');
        }
    });

};
/**
 * 增加应用信息
 */
UsagesStatistics.add = function () {
    Public.createDialog('添加应用', '', 'p-add-project', 320, 250);
    $('.p-add-project .p-dialog-content').load('partials/service/form_project.html', function () {
        UsagesStatistics.initFormData();
    });

    //保存应用信息
    $('.p-add-project .js-ok').unbind('click').bind('click', function(){
        var project = UsagesStatistics.checkBoardroomForm();
        if(project != null){
            Public.postRest('/boardroom', project, function (resp) {
                Public.msg('添加成功');
                $('.p-add-project').dialog('destroy');
                //重新加载数据
                UsagesStatistics.loadData();
            });
        }
    });
};
/**
 * 修改应用信息
 */
UsagesStatistics.update = function (id, e) {
    Public.stopPropagation(e);
    Public.createDialog('修改应用', '', 'p-add-project', 300, 300);
    $('.p-add-project .p-dialog-content').load('partials/system/form_boardroom.html', function () {
        UsagesStatistics.showBoardroomInfo(id);
    });

    //修改应用信息
    $('.p-add-project .js-ok').unbind('click').bind('click', function(){
        var boardroom = UsagesStatistics.checkBoardroomForm();
        if(boardroom != null){
            boardroom.id = id;
            Public.putRest('/boardroom', boardroom, function (resp) {
                Public.msg('修改成功');
                $('.p-add-project').dialog('destroy');
                //重新加载数据
                UsagesStatistics.loadData();
            });
        }
    });
};
/**
 * 查看应用信息
 */
UsagesStatistics.show = function(){
	var message = 'message';
	return message;
};
/**
 * 复制应用信息
 */
UsagesStatistics.copy = function () {
	var message = 'message';
	return message;
};


/**
 * 获取查询参数列表
 */
UsagesStatistics.getSearchParams = function () {
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
UsagesStatistics.checkBoardroomForm = function () {
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
UsagesStatistics.initFormData = function(initBoardroomTypeValue, initNetworkValue, initOrgValue){
    //加载应用类型数据
//    ProjectManage.loadBoardroomTypeData('.p-add-project select[name="type"]', initBoardroomTypeValue);
//    //加载网络拓扑数据
//    ProjectManage.loadNetworkData('.p-add-project select[name="network"]', initNetworkValue);
//    //加载组织机构数据
//    ProjectManage.loadOrgTreeData('.p-add-project .js-org-tree', initOrgValue);
};

/**
 * 显示应用信息
 * @param id
 */
UsagesStatistics.showBoardroomInfo = function (id) {
    Public.getRest('/boardroom/'+id, function(room){
        var type = room.type;
        var org = room.organization;
        var net = room.network;
        var typeId = type != null ? type.id : '';
        var orgCode = org != null ? org.code : '';
        UsagesStatistics.initFormData(typeId, net, orgCode);
        $('.p-add-project input[name="name"]').val(room.name);
    });
};

/**
 * 获取查询表单中的应用类型
 */
UsagesStatistics.loadTypeDataOfForm = function(){
    Public.getRest('/dictionary/query?group=BOARDROOM_TYPE', function(dicts){
        if(dicts != null && dicts.length > 0){
            var html = [];
            html.push('<option value="">全部</option>');
            for(var i= 0,j=dicts.length; i<j; i++){
                var dict = dicts[i];
                html.push('<option value="'+dict.id+'">'+dict.name+'</option>');
            }
            $('.js-project-manage .js-type').html(html.join(""));
        } else {
            $('.js-project-manage .js-type').html('');
        }
    });
};