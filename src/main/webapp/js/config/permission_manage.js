/**
 * 权限管理
 * @type {{}}
 */
var PermissionManage = {
    /**
     * 权限树模块
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
PermissionManage.init = function () {
    $('#main-view').load('partials/config/permission_manage.html', function () {
        PermissionManage.loadData();
        PermissionManage.loadAllRoles();
    });
};
/**
 * 加载数据
 */
PermissionManage.loadData = function () {
    var params = PermissionManage.getSearchParams();
    var url = URI + '/rest/role/easyui_query';
    if (params.length > 0) {
        url += '?' + params.join('&');
    }
    //加载用户角色表格数据
    $('.js-user-role-table').datagrid({
        //url:'json/permission-datagrid.json',
        url: url,
        pagination: true,
        rownumbers: true,
        singleSelect: true,
        pageSize: Public.LIMIT,
        columns: [[
            {field: 'id', title: 'ID', hidden: true},
            {field: 'name', title: '角色', width: 200},
            {field: 'description', title: '角色描述', width: 200},
            {
                field: 'operate', title: '操作', width: 300, formatter: function (value, row) {
                var id = row.id;
                var name = row.name;
                var description = row.description;
                var html = [];
                html.push('<div class="a-btn-group">');
                if (name != 'superAdministrator') {
                    html.push('<a href="javascript:void(0)" class="a-btn" onclick="PermissionManage.update(\'' + id + '\',\'' + name + '\',\'' + description + '\');">修改</a>');
                    html.push('<a href="javascript:void(0)" class="a-btn" onclick="PermissionManage.deletes(\'' + id + '\',\'' + name + '\');">删除</a>');
                    html.push('<a href="javascript:void(0)" class="a-btn" onclick="PermissionManage.addPermission(\'' + id + '\',\'' + name + '\');">授权</a>');
                } else {
                    html.push('<a href="javascript:void(0)" class="a-btn">修改</a>');
                    html.push('<a href="javascript:void(0)" class="a-btn">删除</a>');
                    html.push('<a href="javascript:void(0)" class="a-btn">授权</a>');
                }
                html.push('</div>');
                return html.join('');
            }
            }
        ]],
        onLoadSuccess: function (data) {
            if (data.total == 0) {
                //添加一个新数据行，第一列的值为你需要的提示信息
                $(this).datagrid('appendRow', {name: '<div style="text-align:center;color:red">没有相关记录！</div>'}).datagrid('mergeCells', {
                    index: 0,
                    field: 'name',
                    colspan: 3
                });
            }
        }
    });
};
/**
 * 添加角色
 */
PermissionManage.add = function () {
    Public.createDialog('添加角色', '', 'p-add-role', 350, 150);
    $('.p-add-role .p-dialog-content').load('partials/config/form_role.html', function () {

    });

    //保存角色
    $('.p-add-role .js-ok').unbind('click').bind('click', function () {
        var device = PermissionManage.checkForm();
        if (device != null) {
            Public.postRest('/role', device, function (resp) {
                Public.msg('添加成功');
                $('.p-add-role').dialog('destroy');
                //重新加载数据
                PermissionManage.loadData();
            });
        }
    });
};
/**
 * 更新角色
 */
PermissionManage.update = function (id, name, description, e) {
    Public.stopPropagation(e);
    Public.createDialog('修改角色', '', 'p-add-role', 350, 150);
    $('.p-add-role .p-dialog-content').load('partials/config/form_role.html', function () {
        $('.p-add-role .js-name').val(name);
        $('.p-add-role .js-description').val(description);
    });

    //保存角色
    $('.p-add-role .js-ok').unbind('click').bind('click', function () {
        var device = PermissionManage.checkForm();
        if (device != null) {
            device.id = id;
            Public.putRest('/role', device, function (resp) {
                Public.msg('修改成功');
                $('.p-add-role').dialog('destroy');
                //重新加载数据
                PermissionManage.loadData();
            });
        }
    });
};
/**
 * 删除角色
 */
PermissionManage.deletes = function (id, name, e) {
    Public.stopPropagation(e);
    Public.comfirm('确定要删除' + name + '吗', function () {
        Public.deleteRest('/role/' + id, null, function () {
            Public.msg('删除成功');
            PermissionManage.loadData();
        });
    });
};

/**
 * 检查表单，并获取数据
 */
PermissionManage.checkForm = function () {
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
 * 检查权限
 * */
PermissionManage.checkPremissions = function (id) {
    var nodes = $('.js-qx-tree').tree("getChecked");
    var resourceNames = [];

    for (var i = 0; i < nodes.length; i++) {
        resourceNames.push(nodes[i].text);
    }

    if (resourceNames.length == 0) {
        Public.alert('所选权限不能为空');
        return null;
    }

    var obj = {};
    obj.resourceNames = resourceNames.toString();
    obj.roleId = id;
    return obj;
};


/**
 * 加载权限树
 * by liux
 * */
PermissionManage.loadPermissionTree = function (config, id) {
    config = config || PermissionManage.default_config;
    var node = $(config.el);
    node.tree({
        //url:'json/config-permission-tree.json',
        url: URI + '/rest/resource/easyui_query?id=' + id,
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
PermissionManage.loadPermissionData = function (id) {
    //加载会场数据
    PermissionManage.loadPermissionTree({
        el: '.js-qx-tree',
        checkbox: true,
        onlyLeafCheck: true,
        initFun: function () {
            $('.js-qx-tree').tree('expandAll');
        }
    }, id);
};


/**
 * 角色授权
 */
PermissionManage.addPermission = function (id, name, e) {
    Public.createDialog('权限配置', '', 'p-add-permission', 350, 300);
    $('.p-add-permission .p-dialog-content').load('partials/config/permission_add.html', function () {
        PermissionManage.loadPermissionData(id);
    });

    //保存权限
    $('.p-add-permission .js-ok').unbind('click').bind('click', function () {
        var obj = PermissionManage.checkPremissions(id);
        if (obj != null) {
            Public.postRest('/resource/add_premission', obj, function (resp) {
                Public.msg('权限更改成功,现在一共' + resp.total + '个权限');
                $('.p-add-permission').dialog('destroy');
                //重新加载数据
                PermissionManage.loadData();
            });
        }
    });
};


/**
 * 获取查询参数列表
 */
PermissionManage.getSearchParams = function () {
    var description = $('.js-p-permission .js-description').val();
    var name = $('.js-p-permission .js-name').val();
    var params = [];
    if (!Public.isNull(description)) {
        params.push('description=' + encodeURI(description));
    }
    if (!Public.isNull(name)) {
        params.push('name=' + encodeURI(name));
    }
    return params;
};

/**
 * 获取所有的角色
 */
PermissionManage.loadAllRoles = function () {
    Public.getRest('/role', function (rows) {
        if (rows != null && rows.length > 0) {
            var html = [];
            html.push('<option value="">全部</option>');
            for (var i = 0, j = rows.length; i < j; i++) {
                html.push('<option value="' + rows[i].name + '">' + rows[i].name + '</option>');
            }
            $('.js-p-permission .js-name').html(html.join(""));
        } else {
            $('.js-p-permission .js-name').html('');
        }
    });
};
