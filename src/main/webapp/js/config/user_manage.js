/**
 * 用户管理
 * @type {{}}
 */
var UserManage = {
    /**
     * 用户树模块
     * */
    default_config: {
        el: null,
        checkbox: false,
        onlyLeafCheck: false,
        onClickDeviceFun: null,
        initFun: null
    }

};
/**
 * 初始化
 */
UserManage.init = function(){
    $('#main-view').load('partials/config/user_manage.html', function(){
        UserManage.loadData();
        UserManage.loadAllRoles();
    });
};
/**
 * 加载数据
 */
UserManage.loadData = function () {
    var params = UserManage.getSearchParams();
    var url = URI + '/rest/user/get_users_easyui_query';
    if(params.length > 0){
        url += '?' + params.join('&');
    }
    //加载用户用户表格数据
    $('.js-user-role-table').datagrid({
        //url:'json/permission-datagrid.json',
        url: url,
        pagination:true,
        rownumbers:true,
        singleSelect: false,
        checkOnSelect : true,
        pageSize: Public.LIMIT,
        columns:[[
            {field:'id', title:'ID',hidden:true},
            {field:'checkbox' , checkbox: true },
            {field:'account', title: '用户名', width: 200},
            {field:'nickname',title:'用户昵称',width:200},
            {field:'telephone',title:'联系电话',width:200},
            {
                field: 'operate', title: '操作', width: 300, formatter: function (value, row) {
                var id = row.id;
                var html = [];
                html.push('<div class="a-btn-group">');
                html.push('<a href="javascript:void(0)" class="a-btn" onclick="UserManage.addRole(\'' + id + '\');">授权角色 </a>');
                html.push('</div>');
                return html.join('');
            }}
        ]],
        onLoadSuccess: function (data) {
            if (data.total == 0) {
                //添加一个新数据行，第一列的值为你需要的提示信息
                $(this).datagrid('appendRow', {checkbox: '<div style="text-align:center;color:red">没有相关记录！</div>'}).datagrid('mergeCells', {
                    index: 0,
                    field: 'checkbox',
                    colspan: 5
                });
            }
        }
    });
    
    $('.js-user-role-table').datagrid({loadFilter:UserManage.pagerFilter});

};

/**
 * 分页过滤器
 * */
UserManage.pagerFilter = function(data){
    var dg = $('.js-user-role-table');
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
 * 添加用户
 */
UserManage.add = function () {
    Public.createDialog('添加用户', '', 'p-add-role', 350, 150);
    $('.p-add-role .p-dialog-content').load('partials/config/form_role.html', function () {

    });

    //保存用户
    $('.p-add-role .js-ok').unbind('click').bind('click', function () {
        var device = UserManage.checkForm();
        if (device != null) {
            Public.postRest('/role', device, function (resp) {
                Public.msg('添加成功');
                $('.p-add-role').dialog('destroy');
                //重新加载数据
                UserManage.loadData();
            });
        }
    });
};
/**
 * 更新用户
 */
UserManage.update = function (id, name, description, e) {
    Public.stopPropagation(e);
    Public.createDialog('修改用户', '', 'p-add-role', 350, 150);
    $('.p-add-role .p-dialog-content').load('partials/config/form_role.html', function () {
        $('.p-add-role .js-name').val(name);
        $('.p-add-role .js-description').val(description);
    });

    //保存用户
    $('.p-add-role .js-ok').unbind('click').bind('click', function () {
        var device = UserManage.checkForm();
        if (device != null) {
            device.id = id;
            Public.putRest('/role', device, function (resp) {
                Public.msg('修改成功');
                $('.p-add-role').dialog('destroy');
                //重新加载数据
                UserManage.loadData();
            });
        }
    });
};
/**
 * 删除用户
 */
UserManage.deletes = function (id, name, e) {
    Public.stopPropagation(e);
    Public.comfirm('确定要删除' + name + '吗', function () {
        Public.deleteRest('/role/' + id, null, function () {
            Public.msg('删除成功');
            UserManage.loadData();
        });
    });
};

/**
 * 检查表单，并获取数据
 */
UserManage.checkForm = function () {
    var name = $('.p-add-role .js-name').val();
    var description = $('.p-add-role .js-description').val();
    if (Public.isNull(name)) {
        Public.alert('名称不能为空');
        return null;
    }
    var obj = {};
     obj.name = name;
     obj.description = description; 
    return obj;
};

/**
 * 检查用户
 * */
UserManage.checkRoles = function(id){
	var nodes = $('.js-qx-tree').tree("getChecked");
    var roleNames = [];
    
    for(var i = 0; i < nodes.length; i++){
    	roleNames.push(nodes[i].text);
    }
    
    if(roleNames.length == 0){
        Public.alert('所选用户不能为空');
        return null;
    }
    
    var obj = {};
    obj.roleNames = roleNames.toString();
    obj.userId = id;
    return obj;
};


/**
 * 加载用户树
 * by liux
 * */
 UserManage.loadRoleTree = function (config,id) {
     config = config || UserManage.default_config;
     var node = $(config.el);
     node.tree({
         //url:'json/config-permission-tree.json',
         url: URI + '/rest/user/get_role_easyui_query?id='+id,
         checkbox: config.checkbox,
         onlyLeafCheck: config.onlyLeafCheck,
         onLoadSuccess: function () {
             var root = node.tree('getRoot');
             if (root != null) {
                 node.tree('expand', root.target);
             }
         }
     });
};

/**
 * 加载数据
 * by liux
 */
UserManage.loadRoleData = function (id) {
    //加载会场数据
    UserManage.loadRoleTree({
        el: '.js-qx-tree',
        checkbox: true,
        onlyLeafCheck: true,
        initFun: function () {
            $('.js-qx-tree').tree('expandAll');
        }
    },id);
};


/**
 * 用户授予角色
 */
UserManage.addRole = function (id,e) {
    Public.createDialog('用户用户配置', '', 'p-add-role', 350, 300);
    $('.p-add-role .p-dialog-content').load('partials/config/role_add.html', function () {
        UserManage.loadRoleData(id);
    });

    //保存用户
    $('.p-add-role .js-ok').unbind('click').bind('click', function () {
        var obj = UserManage.checkRoles(id);
        console.log(obj);
        if (obj != null) {
            Public.postRest('/user/add_role', obj, function (resp) {
                Public.msg('用户更改成功,现在一共'+resp.total+'个用户');
                $('.p-add-role').dialog('destroy');
                //重新加载数据
                UserManage.loadData();
            });
        }
    });
};



/**
 * 获取查询参数列表
 */
UserManage.getSearchParams = function () {
    var description = $('.p-add-role .js-description').val();
    var name = $('.p-add-role .js-name').val();
    var params = [];
    if(!Public.isNull(name)){
        params.push('name='+encodeURI(name));
    }
    if(!Public.isNull(description)){
        params.push('description='+encodeURI(description));
    }
    return params;
};

/**
 * 获取所有的角色
 */
UserManage.loadAllRoles = function(){
    Public.getRest('/role', function(rows){
        if(rows != null && rows.length > 0){
            var html = [];
            html.push('<option value="">全部</option>');
            for(var i= 0,j=rows.length; i<j; i++){
                html.push('<option value="'+rows[i].name+'">'+rows[i].name+'</option>');
            }
            $('.p-add-role .js-name').html(html.join(""));
        } else {
            $('.p-add-role .js-name').html('');
        }
    });
};
