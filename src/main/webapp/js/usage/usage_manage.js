/**
 * 应用管理
 * @type {{}}
 */
var UsageManage = {

};
/**
 * 初始化
 */
UsageManage.init = function () {
    $('#main-view').load('partials/usage/usage_manage.html', function(){
        UsageManage.render();
    });
};
/**
 * 渲染页面
 */
UsageManage.render = function(){
    UsageManage.loadData();
};
/**
 * 加载数据
 */
UsageManage.loadData = function () {
    var params = UsageManage.getSearchParams();
    var url = URI+'/rest/usage/usages_easyui_query';
    if(params.length > 0){
        url += '?' + params.join('&');
    }
    //加载应用表格数据
    $('.js-usage-table').datagrid({
        //url:'json/usage-manage-datagrid.json',
        url: url,
        rownumbers:true,
        singleSelect: true,
        pageSize: Public.LIMIT,
        pageList:[5,10,15,20],
        columns:[[
            {field:'id',title:'ID',hidden:true},
            {field:'ip',title:'用户ip',width:100},
            {field:'user',title:'IP归属',width:100, formatter: function(value, row){
                var user = row.user;
                return user != null ? user : '';
            }},
            {field:'description',title:'用户描述',width:200, formatter: function(value, row){
                var description = row.description;
                return description != null ? description : '';
            }},
            {field:'operate',title:'操作',width:200, formatter: function(value, row){
                var id = row.id;
                var html = [];
                html.push('<div class="a-btn-group">');
                //html.push('<a href="javascript:void(0)" class="a-btn" onclick="UsageManage.show(\''+id+'\');">查看</a>');
                html.push('<a href="javascript:void(0)" class="a-btn" onclick="UsageManage.update(\''+id+'\');">修改</a>');
                html.push('<a href="javascript:void(0)" class="a-btn" onclick="UsageManage.deletes(\''+id+'\',\'' + row.ip + '\');">删除</a>');
                html.push('</div>');
                return html.join('');
            }}
        ]],
        pagination:true,
        onLoadSuccess: function (data) {
            if (data.total == 0) {
                //添加一个新数据行，第一列的值为你需要的提示信息
                $(this).datagrid('appendRow', { ip: '<div style="text-align:center;color:red">没有相关记录！</div>' }).datagrid('mergeCells', { index: 0, field: 'ip', colspan: 4 });
                $(this).closest('div.datagrid-wrap').find('div.datagrid-pager').hide();
            }
        }
    });
    $('.js-usage-table').datagrid({loadFilter:UsageManage.pagerFilter});
};

/**
 * 分页过滤器
 * */
UsageManage.pagerFilter = function(data){
    var dg = $('.js-usage-table');
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
 * 增加应用信息
 */
UsageManage.add = function () {
    Public.createDialog('添加使用者', '', 'p-add-usage', 320, 250);
    $('.p-add-usage .p-dialog-content').load('partials/usage/add_usage.html', function () {

    });

    //保存应用信息
    $('.p-add-usage .js-ok').unbind('click').bind('click', function(){
        var usages = UsageManage.checkUsageFormToAdd();
        if(usages != null){
            Public.postRest('/usage/batch_add', usages, function (resp) {
                Public.msg('一共添加'+resp.total+'使用者,其中'+resp.successed+'添加成功');
                $('.p-add-usage').dialog('destroy');
                //重新加载数据
                UsageManage.loadData();
            });
        }
    });
};
/**
 * 修改应用信息
 */
UsageManage.update = function (id, e) {
    Public.stopPropagation(e);
    Public.createDialog('修改应用', '', 'p-add-usage', 300, 300);
    $('.p-add-usage .p-dialog-content').load('partials/usage/update_usage.html', function () {
    	UsageManage.showUsageInfo(id);
    });

    //修改应用信息
    $('.p-add-usage .js-ok').unbind('click').bind('click', function(){
        var usage = UsageManage.checkUsageForm();
        if(usage != null){
            usage.id = id;
            Public.putRest('/usage', usage, function (resp) {
                Public.msg('修改成功');
                $('.p-add-usage').dialog('destroy');
                //重新加载数据
                UsageManage.loadData();
            });
        }
    });
};
/**
 * 查看应用信息
 */
UsageManage.show = function(){

};
/**
 * 删除使用者信息
 */
UsageManage.deletes = function (id,ip,e) {
    Public.stopPropagation(e);
    Public.comfirm('确定要删除用户ip为' + ip + ',的信息吗', function () {
        Public.deleteRest('/usage/' + id, null, function () {
            Public.msg('删除成功');
            UsageManage.loadData();
        });
    });

};


/**
 * 获取查询参数列表
 */
UsageManage.getSearchParams = function () {
	var ip = $('.js-usage-manage .js-ip').val();
    var name = $('.js-usage-manage .js-name').val();
    var params = [];
    if(!Public.isNull(ip)){
    	if(!Public.isIp(ip)){
        	Public.alert(ip+"不是正确的ip格式");
    	}else{
            params.push('ip='+ip);
    	}
    }
    if(!Public.isNull(name)){
        params.push('name='+encodeURI(name));
    }
    return params;
};

/**
 * 检查应用表单，并获取数据
 */
UsageManage.checkUsageFormToAdd = function () {
    var ipStart = $('.p-add-usage input[name="ipStart"]').val();
    var ipEnd = $('.p-add-usage input[name="ipEnd"]').val();
    var name = $('.p-add-usage input[name="name"]').val();
    var description =  $('.p-add-usage input[name="description"]').val();
    if(Public.isNull(name)){
        Public.alert('用户ip归属不能为空');
        return null;
    }
    if(!Public.isIp(ipStart)){
    	Public.alert('开始ip不合法');
    	return null;
    }
    if(!Public.isNull(ipEnd)){
    	if(!Public.isIp(ipEnd)){
        	Public.alert('结束ip不合法');
        	return null;	
    	}
    	var mask = "255.255.255.0";
    	if(!Public.isEqualIPAddress(ipStart, ipEnd, mask)){
        	Public.alert('开始ip和结束ip不在一个网段');
        	return null;	
    	}
    }else{
    	ipEnd = ipStart;
    }
    var ipPrefix = ipStart.substr(0,ipStart.lastIndexOf(".")+1);
    var start = ipStart.substr(ipStart.lastIndexOf(".")+1);
    var end = ipEnd.substr(ipEnd.lastIndexOf(".")+1);
    
    var obj = {};
    obj.ipPrefix = ipPrefix;
    obj.start = start;
    obj.end = end;
    obj.name = name;
    obj.description = Public.isNull(description) ? '' : description;
    return obj;
};


/**
 * 修改单个使用者
 * 
 * */
UsageManage.checkUsageForm = function () {
    var ip = $('.p-add-usage input[name="ip"]').val();
    var name = $('.p-add-usage input[name="name"]').val();
    var description =  $('.p-add-usage input[name="description"]').val();
    if(Public.isNull(name)){
        Public.alert('用户ip归属不能为空');
        return null;
    }
    if(!Public.isIp(ip)){
    	Public.alert('ip不合法');
    	return null;
    }
    
    var obj = {};
    obj.ip = ip;
    obj.name = name;
    obj.description = Public.isNull(description) ? '' : description;
    return obj;
};

/**
 * 初始化表单数据
 */
UsageManage.initFormData = function(initusageTypeValue, initNetworkValue, initOrgValue){
    //加载所有的应用名称
	//UsageManage.loadProjectNameData('.p-add-usage .js-org-tree', initOrgValue);
};

/**
 * 加载应用类型数据
 */
UsageManage.loadProjectsCheckData = function(){
    Public.getRest('/project', function(dicts){
        if(dicts != null && dicts.length > 0){
            var html = [];
            for(var i= 0,j=dicts.length; i<j; i++){
                var dict = dicts[i];
                html.push('<input name="projectList" type="checkbox" value="'+dict.id+'">'+dict.name+'</input>');
            }
            
            $(".projectsCheck").html(html.join(""));
        } else {
            $(el).html('');
        }
    });
};

/**
 * 显示使用者信息
 * @param id
 */
UsageManage.showUsageInfo = function (id) {
    Public.getRest('/usage/'+id, function(usage){
        var id = usage.id;
        var ip = usage.ip;
        var name = usage.name;
        var description = usage.description;
        $('.p-add-usage input[name="ip"]').val(ip);
        $('.p-add-usage input[name="name"]').val(name);
        $('.p-add-usage input[name="description"]').val(description);
    });
};

/**
 *批量给使用者赋权应用
 * 
 * */
UsageManage.empowermentProjects = function(){

    Public.createDialog('给使用者授权应用', '', 'p-add-projects', 320, 350);
    $('.p-add-projects .p-dialog-content').load('partials/usage/empowerment_projects.html', function () {
    	UsageManage.loadProjectTree();
    });

    //保存应用信息
    $('.p-add-projects .js-ok').unbind('click').bind('click', function(){
        var usages = UsageManage.checkProjectTree();
        if(usages != null){
            Public.postRest('/usage/batch_add_project', usages, function (resp) {
                Public.msg('给'+resp.total+'使用者添加应用,其中'+resp.successed+'应用添加成功');
                $('.p-add-projects').dialog('destroy');
                //重新加载数据
                UsageManage.loadData();
            });
        }
    });

};

/**
 * 加载应用树
 * 
 * */
UsageManage.loadProjectTree = function(){
    var node = $('.js-project-tree');
    node.tree({
        //url:'json/sys-org-tree.json',
        checkbox:true,
        url: URI+'/rest/project/get_project_tree',
        onLoadSuccess: function(){
            var root = node.tree('getRoot');
            if (root != null) {
                node.tree('expand', root.target);
                node.tree('select', root.target);
            }
        }
    });
};


/**
 * 检查授权应用表单
 */
UsageManage.checkProjectTree = function () {
    var ipStart = $('.p-add-projects input[name="ipStart"]').val();
    var ipEnd = $('.p-add-projects input[name="ipEnd"]').val();
    var projects = $('.js-project-tree').tree("getChecked");
    var projectNames = [];
    
    for(var i = 0; i < projects.length; i++){
    	projectNames.push(projects[i].text);
    }
    
    if(!Public.isIp(ipStart)){
    	Public.alert('开始ip不合法');
    	return null;
    }
    if(!Public.isNull(ipEnd)){
    	if(!Public.isIp(ipEnd)){
        	Public.alert('结束ip不合法');
        	return null;	
    	}
    	var mask = "255.255.255.0";
    	if(!Public.isEqualIPAddress(ipStart, ipEnd, mask)){
        	Public.alert('开始ip和结束ip不在一个网段');
        	return null;	
    	}
    }else{
    	ipEnd = ipStart;
    }
    
    if(projectNames.length == 0){
        Public.alert('所选应用不能为空');
        return null;
    }
    
    var ipPrefix = ipStart.substr(0,ipStart.lastIndexOf(".")+1);
    var start = ipStart.substr(ipStart.lastIndexOf(".")+1);
    var end = ipEnd.substr(ipEnd.lastIndexOf(".")+1);
    
    var obj = {};
    obj.ipPrefix = ipPrefix;
    obj.start = start;
    obj.end = end;
    obj.projectNames = projectNames.toString();
    return obj;
};

