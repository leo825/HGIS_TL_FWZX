/**
 * 应用管理
 * @type {{}}
 */
var ServiceTracking = {

};
/**
 * 初始化
 */
ServiceTracking.init = function () {
    $('#main-view').load('partials/monitor/service_tracking.html', function(){
        ServiceTracking.render();
    });
};
/**
 * 渲染页面
 */
ServiceTracking.render = function(){
	ServiceTracking.loadDataTime();
    ServiceTracking.loadData();
    ServiceTracking.loadAllProjectForm();
};


/**
 * 加载日历时间
 * */
ServiceTracking.loadDataTime=function(){
	var start = {
		    elem: '#start',
		    format: 'YYYY-MM-DD hh:mm:ss',
		    max: laydate.now(),
		    festival:true,//显示节日
		    istime: true,
		    istoday: false,
		    choose: function(datas){
		         end.min = datas; //开始日选好后，重置结束日的最小日期
		         end.start = datas; //将结束日的初始值设定为开始日
		    }
		};
		var end = {
		    elem: '#end',
		    format: 'YYYY-MM-DD hh:mm:ss',
		    max: laydate.now(),
		    istime: true,
		    istoday: false,
		    choose: function(datas){
		        start.max = datas; //结束日选好后，重置开始日的最大日期
		    }
		};
		laydate(start);
		laydate(end);
};

/**
 * 加载数据
 */
ServiceTracking.loadData = function () {
    var params = ServiceTracking.getSearchParams();
    var url = URI+'/rest/operation/easyui_query';
    if(params.length > 0){
        url += '?' + params.join('&');
    }
    //加载应用表格数据
    $('.js-monitor-table').datagrid({
        //url:'json/service-tracking-datagrid.json',
        url: url,
        singleSelect: true,
        pagination:true,
        rownumbers:true,
        pageSize: Public.LIMIT,
        singleSelect:true,
        remoteSort:false,
        columns:[[
                  {field:'id',title:'ID',hidden:true},
                  {field:'ip',title:'用户ip',width:100},
                  {field:'user',title:'IP归属',width:100, formatter: function(value, row){
                      var user = row.user;
                      return user != null ? user : '';
                  }},
                  {field:'server',title:'服务名称',width:100},
                  {field:'project',title:'应用名称',width:100},
                  {field:'result',title:'访问结果',width:100},
                  {field:'date',title:'访问时间',width:200,sortable:true},
                  {field:'description',title:'操作描述',width:200},
                  {field:'operate',title:'操作',width:200, formatter: function(value, row){
                	  var id = row.id;
                	  var html = [];
                	  html.push('<div class="a-btn-group">');
                	  //html.push('<a href="javascript:void(0)" class="a-btn" onclick="ServiceTracking.show(\''+id+'\');">查看</a>');
                	  html.push('<a href="javascript:void(0)" class="a-btn" onclick="ServiceTracking.deletes(\''+id+'\',\'' + row.ip + '\');">删除</a>');
                	  html.push('</div>');
                	  return html.join('');
                  }}
        ]],
        sort:'date',
        order:'desc',
        onLoadSuccess: function (data) {
            if (data.total == 0) {
                //添加一个新数据行，第一列的值为你需要的提示信息
                $(this).datagrid('appendRow', { ip: '<div style="text-align:center;color:red">没有相关记录！</div>' }).datagrid('mergeCells', { index: 0, field: 'ip', colspan: 8 });
                $(this).closest('div.datagrid-wrap').find('div.datagrid-pager').hide();
            }
        }
    });
    $('.js-monitor-table').datagrid({loadFilter:ServiceTracking.pagerFilter});
};

/**
 * 分页过滤器
 * */
ServiceTracking.pagerFilter = function(data){
    var dg = $('.js-monitor-table');
    var opts = dg.datagrid('options');
    var pager = dg.datagrid('getPager');
    pager.pagination({
        onSelectPage:function(pageNum, pageSize){
            opts.pageNumber = pageNum;
            opts.pageSize = pageSize;
            pager.pagination('refresh',{
                pageNumber:pageNum,
                pageSize:pageSize
            });
            dg.datagrid('loadData',data);
        }
    });
    if (!data.originalRows){
        data.originalRows = (data.rows);
    }
    var start = (opts.pageNumber-1)*parseInt(opts.pageSize);
    var end = start + parseInt(opts.pageSize);
    data.rows = (data.originalRows.slice(start, end));
    return data;
};



/**
 * 修改应用信息
 */
ServiceTracking.update = function (id, e) {
    Public.stopPropagation(e);
    Public.createDialog('修改应用', '', 'p-add-project', 300, 300);
    $('.p-add-project .p-dialog-content').load('partials/system/form_boardroom.html', function () {
        ServiceTracking.showBoardroomInfo(id);
    });

    //修改应用信息
    $('.p-add-project .js-ok').unbind('click').bind('click', function(){
        var boardroom = ServiceTracking.checkBoardroomForm();
        if(boardroom != null){
            boardroom.id = id;
            Public.putRest('/boardroom', boardroom, function (resp) {
                Public.msg('修改成功');
                $('.p-add-project').dialog('destroy');
                //重新加载数据
                ServiceTracking.loadData();
            });
        }
    });
};
/**
 * 查看应用信息
 */
ServiceTracking.show = function(){

};

/**
 * 删除日志信息
 */
ServiceTracking.deletes = function (id, ip, e) {
    Public.stopPropagation(e);
    Public.comfirm('确定要删除日志信息' + ip + '吗', function () {
        Public.deleteRest('/operation/' + id, null, function () {
            Public.msg('删除成功');
            ServiceTracking.loadData();
        });
    });
};


/**
 * 获取查询参数列表
 */
ServiceTracking.getSearchParams = function() {
	var result = $('.js-service-tracking .js-result').val();
	var projectName = $('.js-service-tracking .js-project').val();
	var startTime = $('#start').val();
	var endTime = $('#end').val();
	var params = [];
	
	if (!Public.isNull(result)) {
		params.push('result=' + encodeURI(result));
	}
	if (!Public.isNull(projectName)) {
		params.push('projectName=' +encodeURI(projectName));
	}
	if (!Public.isNull(startTime)) {
		params.push('startTime=' + startTime);
	}
	if (!Public.isNull(endTime)) {
		params.push('endTime=' + endTime);
		if (Public.isNull(startTime)) {
			Public.alert('开始时间不能为空');
			return null;
		}
	}
	return params;
};

/**
 * 检查应用表单，并获取数据
 */
ServiceTracking.checkBoardroomForm = function () {
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
ServiceTracking.initFormData = function(initBoardroomTypeValue, initNetworkValue, initOrgValue){
    //加载应用类型数据
//    ServiceTracking.loadBoardroomTypeData('.p-add-project select[name="type"]', initBoardroomTypeValue);
//    //加载网络拓扑数据
//    ServiceTracking.loadNetworkData('.p-add-project select[name="network"]', initNetworkValue);
//    //加载组织机构数据
//    ServiceTracking.loadOrgTreeData('.p-add-project .js-org-tree', initOrgValue);
};

/**
 * 加载应用类型数据
 */
ServiceTracking.loadBoardroomTypeData = function(el, initValue){
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
ServiceTracking.loadNetworkData = function(el, initValue){
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
ServiceTracking.loadOrgTreeData = function(el, initValue){
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
ServiceTracking.showBoardroomInfo = function (id) {
    Public.getRest('/boardroom/'+id, function(room){
        var type = room.type;
        var org = room.organization;
        var net = room.network;
        var typeId = type != null ? type.id : '';
        var orgCode = org != null ? org.code : '';
        ServiceTracking.initFormData(typeId, net, orgCode);
        $('.p-add-project input[name="name"]').val(room.name);
    });
};

/**
 * 获取查询表单中的应用类型
 */
ServiceTracking.loadAllProjectForm = function(){
    Public.getRest('/project', function(dicts){
        if(dicts != null && dicts.length > 0){
            var html = [];
            html.push('<option value="">全部</option>');
            for(var i= 0,j=dicts.length; i<j; i++){
                var dict = dicts[i];
                html.push('<option value="'+dict.name+'">'+dict.name+'</option>');
            }
            $('.js-service-tracking .js-project').html(html.join(""));
        } else {
            $('.js-service-tracking .js-project').html('');
        }
    });
};