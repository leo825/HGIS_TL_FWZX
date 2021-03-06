/**
 * 应用管理
 * @type {{}}
 */
var UsageProjectManage = {};
/**
 * 初始化
 */
UsageProjectManage.init = function () {
    $('#main-view').load('partials/usage/usage_project_manage.html', function () {
        UsageProjectManage.render();
    });
};
/**
 * 渲染页面
 */
UsageProjectManage.render = function () {
    UsageProjectManage.loadData();
    UsageProjectManage.loadProjects();
};
/**
 * 加载数据
 */
UsageProjectManage.loadData = function () {
    var params = UsageProjectManage.getSearchParams();
    var url = URI + '/rest/usage/easyui_query';
    if (params.length > 0) {
        url += '?' + params.join('&');
    }
    //加载应用表格数据
    $('.js-usage-table').datagrid({
        //url:'json/usage-manage-datagrid.json',
        url: url,
        rownumbers: true,
        singleSelect: true,
        pageSize: Public.LIMIT,
        pageList: [5, 10, 15, 20],
        columns: [[
            {field: 'id', title: 'ID', hidden: true},
            {field: 'ip', title: '用户ip', width: 100},
            {
                field: 'user', title: 'IP归属', width: 100, formatter: function (value, row) {
                var user = row.user;
                return user != null ? user : '';
            }
            },
            {
                field: 'description', title: '用户描述', width: 200, formatter: function (value, row) {
                var description = row.description;
                return description != null ? description : '';
            }
            },
            {field: 'projectId', title: 'projectId', hidden: true},
            {field: 'projectName', title: '应用名称', width: 200},
            {
                field: 'provider', title: '应用提供者', width: 100, formatter: function (value, row) {
                var provider = row.provider;
                return provider != null ? provider : '';
            }
            },
            {
                field: 'operate', title: '操作', width: 200, formatter: function (value, row) {
                var id = row.id;
                var projectId = row.projectId;
                var html = [];
                html.push('<div class="a-btn-group">');
                //html.push('<a href="javascript:void(0)" class="a-btn" onclick="UsageProjectManage.show(\''+id+'\');">查看</a>');
                html.push('<a href="javascript:void(0)" class="a-btn" onclick="UsageProjectManage.update(\'' + id + '\',\'' + projectId + '\');">修改</a>');
                html.push('<a href="javascript:void(0)" class="a-btn" onclick="UsageProjectManage.deletes(\'' + id + '\',\'' + row.ip + '\',\'' + row.projectId + '\');">删除</a>');
                html.push('</div>');
                return html.join('');
            }
            }
        ]],
        pagination: true,
        onLoadSuccess: function (data) {
            if (data.total == 0) {
                //添加一个新数据行，第一列的值为你需要的提示信息
                $(this).datagrid('appendRow', {ip: '<div style="text-align:center;color:red">没有相关记录！</div>'}).datagrid('mergeCells', {
                    index: 0,
                    field: 'ip',
                    colspan: 7
                });
                $(this).closest('div.datagrid-wrap').find('div.datagrid-pager').hide();
            }
        }
    });
    $('.js-usage-table').datagrid({loadFilter: UsageProjectManage.pagerFilter});
};

/**
 * 分页过滤器
 * */
UsageProjectManage.pagerFilter = function (data) {
    var dg = $('.js-usage-table');
    var opts = dg.datagrid('options');
    var pager = dg.datagrid('getPager');
    pager.pagination({
        onSelectPage: function (pageNum, pageSize) {
            opts.pageNumber = pageNum;
            opts.pageSize = pageSize;
            pager.pagination('refresh', {
                pageNumber: pageNum,
                pageSize: pageSize
            });
            dg.datagrid('loadData', data);
        }
    });
    if (!data.originalRows) {
        data.originalRows = (data.rows);
    }
    var start = (opts.pageNumber - 1) * parseInt(opts.pageSize);
    var end = start + parseInt(opts.pageSize);
    data.rows = (data.originalRows.slice(start, end));
    return data;
};

/**
 * 增加使用者使用信息
 */
UsageProjectManage.add = function () {
    Public.createDialog('添加使用者', '', 'p-add-usage', 320, 250);
    $('.p-add-usage .p-dialog-content').load('partials/usage/add_usage_manage.html', function () {
        UsageProjectManage.loadProjectsCheckData();
    });

    //保存应用信息
    $('.p-add-usage .js-ok').unbind('click').bind('click', function () {
        var project = UsageProjectManage.checkProjectForm();
        if (project != null) {
            Public.postRest('/usage', project, function (resp) {
                Public.msg('添加成功');
                $('.p-update-usage').dialog('destroy');
                //重新加载数据
                UsageProjectManage.loadData();
            });
        }
    });
};
/**
 * 修改应用信息
 */
UsageProjectManage.update = function (id, projectId, e) {
    Public.stopPropagation(e);
    Public.createDialog('修改应用', '', 'p-update-usage', 300, 300);
    $('.p-update-usage .p-dialog-content').load('partials/usage/update_usage_projects.html', function () {
        UsageProjectManage.showUsageProjectInfo(id, projectId);
    });

    //修改应用信息
    $('.p-update-usage .js-ok').unbind('click').bind('click', function () {
        var newProjectId = $('.p-update-usage select[name="projectName"]').val();
        if (newProjectId != projectId) {
            Public.getRest('/usage/update_usage_project?usageId=' + id + '&projectId=' + projectId+'&newProjectId='+newProjectId, function (resp) {

                Public.msg('修改成功');
                $('.p-update-usage').dialog('destroy');
                //重新加载数据
                UsageProjectManage.loadData();
            });
        }else{
            Public.alert("没有做任何修改");
            return null;
        }

    });
};
/**
 * 查看应用信息
 */
UsageProjectManage.show = function () {
    var message = 'message';
    return message;
};
/**
 * 删除使用者应用信息
 */
UsageProjectManage.deletes = function (id, ip, projectId, e) {
    Public.stopPropagation(e);
    Public.comfirm('确定要删除用户ip为' + ip + ',的信息吗', function () {
        Public.deleteRest('/usage/' + id + '?projectId=' + projectId, null, function () {
            Public.msg('删除成功');
            UsageProjectManage.loadData();
        });
    });

};

/**
 * 获取查询参数列表
 */
UsageProjectManage.getSearchParams = function () {
    var ip = $('.js-usage-manage .js-ip').val();
    var name = $('.js-usage-manage .js-name').val();
    var projectId = $('.js-usage-manage .js-project').val();
    var params = [];
    if (!Public.isNull(ip)) {
        if (!Public.isIp(ip)) {
            Public.alert(ip + "不合法");
            return null;
        } else {
            params.push('ip=' + ip);
        }
    }
    if (!Public.isNull(name)) {
        params.push('name=' + encodeURI(name));
    }
    if (!Public.isNull(projectId)) {
        params.push('projectId=' + projectId);
    }
    return params;
};

/**
 * 检查应用表单，并获取数据
 */
UsageProjectManage.checkProjectForm = function () {
    var name = $('.p-update-usage input[name="name"]').val();
    var ip = $('.p-update-usage input[name="ip"]').val();
    var port = $('.p-update-usage input[name="port"]').val();
    var description = $('.p-update-usage input[name="description"]').val();
    var provider = $('.p-update-usage input[name="provider"]').val();

    if (Public.isNull(name)) {
        Public.alert('应用名称不能为空');
        return null;
    } else if (!Public.isIp(ip)) {
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
UsageProjectManage.initFormData = function (initBoardroomTypeValue, initNetworkValue, initOrgValue) {
    //加载所有的应用名称
    //UsageProjectManage.loadProjectNameData('.p-update-usage .js-org-tree', initOrgValue);
};

/**
 * 加载应用类型数据
 */
UsageProjectManage.loadProjectsCheckData = function () {
    Public.getRest('/project', function (dicts) {
        if (dicts != null && dicts.length > 0) {
            var html = [];
            for (var i = 0, j = dicts.length; i < j; i++) {
                var dict = dicts[i];
                html.push('<input name="projectList" type="checkbox" value="' + dict.id + '">' + dict.name + '</input>');
            }

            $(".projectsCheck").html(html.join(""));
        } else {
            $(el).html('');
        }
    });
};


/**
 * 加载组织机构树数据
 */
UsageProjectManage.loadOrgTreeData = function (el, initValue) {
    var node = $(el);
    node.tree({
        //url:'json/sys-org-tree.json',
        url: URI + '/rest/organization/subs',
        onLoadSuccess: function () {
            var root = node.tree('getRoot');
            if (root != null) {
                node.tree('expand', root.target);
                node.tree('select', root.target);
            }
            if (initValue != null) {
                var initNode = node.tree('find', initValue);
                if (initNode != null) {
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
UsageProjectManage.showUsageProjectInfo = function (id, projectId) {
    Public.getRest('/usage/' + id, function (usage) {
        var ip = usage.ip;
        var name = usage.name;
        var description = usage.description;
        var projectName;
        var projectList = usage.projectList;

        for (var i = 0; i < projectList.length; i++) {
            if (projectId == projectList[i].id) {
                projectName = projectList[i].name;
                break;
            }
        }
        $('.p-update-usage input[name="ip"]').val(ip);
        $('.p-update-usage input[name="name"]').val(name);
        $('.p-update-usage input[name="description"]').val(description);
        UsageProjectManage.loadProjects(projectId);
    });
};

/**
 * 获取查询表单中的应用类型
 */
UsageProjectManage.loadProjects = function () {
    Public.getRest('/project', function (dicts) {
        if (dicts != null && dicts.length > 0) {
            var html = [];
            html.push('<option value="">全部</option>');
            for (var i = 0, j = dicts.length; i < j; i++) {
                var dict = dicts[i];
                html.push('<option value="' + dict.id + '">' + dict.name + '</option>');
            }
            $('.js-usage-manage .js-project').html(html.join(""));
        } else {
            $('.js-usage-manage .js-project').html('');
        }
    });
};

/**
 * 获取查询表单中的应用提供者
 */
UsageProjectManage.loadAllProvidersForm = function () {
    Public.getRest('/project/get_all_providers?', function (rows) {
        if (rows != null && rows.length > 0) {
            var html = [];
            html.push('<option value="">全部</option>');
            for (var i = 0, j = rows.length; i < j; i++) {
                html.push('<option value="' + rows[i] + '">' + rows[i] + '</option>');
            }
            $('.js-usage-manage .js-provider').html(html.join(""));
        } else {
            $('.js-usage-manage .js-provider').html('');
        }
    });
};


/**
 * 加载所有应用
 *
 * */
UsageProjectManage.loadProjects = function (projectId) {
    Public.getRest('/project', function (dicts) {
        if (dicts != null && dicts.length > 0) {
            var html = [];
            if (Public.isNull(projectId)) {
                html.push('<option value="">空</option>');
            } else {
                for (var i = 0, j = dicts.length; i < j; i++) {
                    var dict = dicts[i];
                    if (projectId == dict.id) {
                        html.push('<option value="' + dict.id + '">' + dict.name + '</option>');
                    }
                }
            }
            for (var i = 0, j = dicts.length; i < j; i++) {
                var dict = dicts[i];
                html.push('<option value="' + dict.id + '">' + dict.name + '</option>');
            }
            $('.js-projects').html(html.join(""));
        } else {
            $('.js-projects').html('');
        }
    });
};



