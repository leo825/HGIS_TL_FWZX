/**
 * 应用管理
 * @type {{}}
 */
var ProjectManage = {};
/**
 * 初始化
 */
ProjectManage.init = function () {
    $('#main-view').load('partials/service/project_manage.html', function () {
        ProjectManage.render();
    });
};
/**
 * 渲染页面
 */
ProjectManage.render = function () {
    ProjectManage.loadData();
    ProjectManage.loadAllProvidersForm();

//    ProjectManage.getState();
};
/**
 * 加载数据
 */
ProjectManage.loadData = function () {
    var params = ProjectManage.getSearchParams();
    var url = URI + '/rest/project/easyui_query';
    if (params.length > 0) {
        url += '?' + params.join('&');
    }
    //加载应用表格数据
    $('.js-project-table').datagrid({
        //url:'json/service-project-datagrid.json',
        url: url,
        singleSelect: true,
        pagination: true,
        rownumbers: true,
        pageSize: Public.LIMIT,
        columns: [[
            {field: 'id', title: 'ID', hidden: true},
            {field: 'name', title: '应用名称', width: 100},
            {
                field: 'ip', title: '应用IP', width: 100, formatter: function (value, row) {
                var ip = row.ip;
                return ip != null ? ip : '';
            }
            },
            {field: 'port', title: '应用端口', width: 100},
            {
                field: 'state', title: '应用状态', width: 100, formatter: function (value, row) {
                if (value == '断开') {
                    return "<font color='red'>" + value + "</font>";
                } else {
                    return value;
                }
            }
            },
            {field: 'description', title: '应用描述', width: 100},
            {
                field: 'provider', title: '应用提供者', width: 100, formatter: function (value, row) {
                var provider = row.provider;
                return provider != null ? provider : '';
            }
            },
            {field: 'fileName', title: '审核状态', hidden: true},
            {field: 'testUrl', title: '测试页面', width: 200},
            {
                field: 'checkState', title: '审核状态', width: 100, formatter: function (value, row) {
                if (value == '未审核') {
                    return "<font color='red'>" + value + "</font>";
                } else {
                    return value;
                }
            }
            },
            {
                field: 'operate', title: '操作', width: 200, formatter: function (value, row) {

                var html = [];
                html.push('<div class="a-btn-group">');
                html.push('<a href="javascript:void(0)" class="a-btn" onclick="ProjectManage.test(\'' + row.ip + '\',\'' + row.port + '\');">测试</a>');

                if (Public.isInArray("add", RESOURCES)) {
                    $("#addBtn").show();
                }

                if (Public.isInArray("update", RESOURCES)) {
                    html.push('<a href="javascript:void(0)" class="a-btn" onclick="ProjectManage.update(\'' + row.id + '\');">修改</a>');
                }

                if (Public.isInArray("delete", RESOURCES)) {
                    html.push('<a href="javascript:void(0)" class="a-btn" onclick="ProjectManage.deletes(\'' + row.id + '\',\'' + row.name + '\');">删除</a>');
                }

                if (Public.isInArray("check", RESOURCES)) {
                    html.push('<a href="javascript:void(0)" class="a-btn" onclick="ProjectManage.check(\'' + row.id + '\',\'' + row.name + '\');">审核</a>');
                }

                if (!Public.isNull(row.fileName)) {
                    var url = URI + '/rest/project/download_file?id=' + row.id;
                    html.push('<a href="' + url + '" class="a-btn">文档资料</a>');
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
                    colspan: 10
                });
                $(this).closest('div.datagrid-wrap').find('div.datagrid-pager').hide();
            }
        }
    });
    $('.js-project-table').datagrid({loadFilter: ProjectManage.pagerFilter});
};

/**
 * 分页过滤器
 * */
ProjectManage.pagerFilter = function (data) {
    var dg = $('.js-project-table');
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
 * 增加应用信息
 */
ProjectManage.add = function () {
    Public.createDialog('添加应用', '', 'p-add-project', 360, 300);
    $('.p-add-project .p-dialog-content').load('partials/service/add_project.html', function () {
        ProjectManage.initFormData();
    });

    //保存应用信息
    $('.p-add-project .js-ok').unbind('click').bind('click', function () {
        var project = ProjectManage.checkProjectForm();
        if (project != null) {
            Public.postRest('/project', project, function (resp) {
                ProjectManage.addFile(resp);
                Public.msg('添加成功,等待审核');
                $('.p-add-project').dialog('destroy');
            });
        }
    });
};

ProjectManage.addFile = function (project) {
    var url = URI + '/rest/project/add_file';
    $.ajaxFileUpload({
        url: url, //用于文件上传的服务器端请求地址
        secureuri: false, //是否需要安全协议，一般设置为false
        data: project,
        fileElementId: 'fileToUpload', //文件上传域的ID
        dataType: 'json', //返回值类型 一般设置为json
        success: function (data) //服务器成功响应处理函数
        {
            ProjectManage.loadData();
        }
    });
};


/**
 * 修改应用信息
 */
ProjectManage.update = function (id, e) {
    Public.stopPropagation(e);
    Public.createDialog('修改应用', '', 'p-add-project', 300, 300);
    $('.p-add-project .p-dialog-content').load('partials/service/update_project.html', function () {
        ProjectManage.showProjectInfo(id);
    });

    //修改应用信息
    $('.p-add-project .js-ok').unbind('click').bind('click', function () {
        var project = ProjectManage.checkProjectForm();
        if (project != null) {
            project.id = id;
            Public.putRest('/project', project, function (data) {
                Public.msg('修改成功');
                $('.p-add-project').dialog('destroy');
                //重新加载数据
                ProjectManage.loadData();
                ProjectManage.loadAllProvidersForm();
            });
        }
    });
};

/**
 * 定时器
 * */

ProjectManage.getState = function () {
    setInterval('ProjectManage.loadData()', 2000);
};


/**
 * 删除应用信息
 */
ProjectManage.deletes = function (id, name, e) {
    Public.stopPropagation(e);
    Public.comfirm('确定要删除应用信息' + name + '吗', function () {
        Public.deleteRest('/project/' + id, null, function () {
            Public.msg('删除成功');
            ProjectManage.loadData();
        });
    });
};
/**
 * 测试应用
 */
ProjectManage.test = function (ip, port, e) {
    Public.stopPropagation(e);
    Public.getRest('/project/get_state?ip=' + ip + "&port=" + port, function (data) {
        if (data) {
            Public.infoMsg("服务已连接");
        } else {
            Public.infoMsg("服务未连接");
        }
    });
};


/**
 * 审核应用
 * */
ProjectManage.check = function (id, name, e) {
    Public.stopPropagation(e);
    Public.comfirm('确定要审核应用信息' + name + '吗', function () {
        Public.getRest('/project/check_project/' + id, function (data) {
            if (data) {
                Public.msg('审核成功');
                ProjectManage.loadData();
            } else {
                Public.infoMsg("审核失败");
            }
        });
    });

};

/**
 * 获取查询参数列表
 */
ProjectManage.getSearchParams = function () {
    var name = $('.js-project-manage .js-name').val();
    var provider = $('.js-project-manage .js-provider').val();
    var userId = $(".user-info").find("span:first-child").attr("id");
    var params = [];
    if (!Public.isNull(name)) {
        params.push('name=' + encodeURI(name));
    }
    if (!Public.isNull(provider)) {
        params.push('provider=' + encodeURI(provider));
    }
    if (!Public.isNull(userId)) {
        params.push('userId=' + userId);
    }
    return params;
};

/**
 * 检查应用表单，并获取数据
 */
ProjectManage.checkProjectForm = function () {
    var name = $('.p-add-project input[name="name"]').val();
    var ip = $('.p-add-project input[name="ip"]').val();
    var port = $('.p-add-project input[name="port"]').val();
    var description = $('.p-add-project input[name="description"]').val();
    var provider = $('.p-add-project input[name="provider"]').val();
    var testUrl = $('.p-add-project input[name="testUrl"]').val();
    var checkState = $('.p-add-project input[name="checkState"]').val();

    if (Public.isNull(name)) {
        Public.alert('应用名称不能为空');
        return null;
    }
    if (!Public.isIp(ip)) {
        Public.alert('应用ip不合法');
        return null;
    }
    if (!Public.isInt(port)) {
        Public.alert("端口应该是数字");
        return null;
    }
    if (Public.isNull(provider)) {
        Public.alert("提供者不能为空");
        return null;
    }
    if (Public.isNull(testUrl)) {
        Public.alert("测试页面不能为空");
        return null;
    }

    var obj = {};
    obj.name = name;
    obj.ip = ip;
    obj.port = port;
    obj.description = Public.isNull(description) ? '' : description;
    obj.provider = provider;
    obj.testUrl = testUrl;
    obj.checkState = Public.isNull(checkState) ? '未审核' : checkState;
    return obj;
};

/**
 * 初始化表单数据
 */
ProjectManage.initFormData = function (initBoardroomTypeValue, initNetworkValue, initOrgValue) {
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
ProjectManage.showProjectInfo = function (id) {
    Public.getRest('/project/' + id, function (project) {
        var id = project.id;
        var name = project.name;
        var ip = project.ip;
        var port = project.port;
        var state = project.state;
        var description = project.description != null ? project.description : '';
        var provider = project.provider != null ? project.provider : '';

        $('.p-add-project input[name="name"]').val(project.name);
        $('.p-add-project input[name="ip"]').val(project.ip);
        $('.p-add-project input[name="port"]').val(project.port);
        $('.p-add-project input[name="checkState"]').val(project.checkState);
        $('.p-add-project input[name="description"]').val(project.description);
        $('.p-add-project input[name="provider"]').val(project.provider);
        $('.p-add-project input[name="testUrl"]').val(project.testUrl);
    });
};

/**
 * 获取查询表单中的应用提供者
 */
ProjectManage.loadAllProvidersForm = function () {
    Public.getRest('/project/get_all_providers?', function (rows) {
        if (rows != null && rows.length > 0) {
            var html = [];
            html.push('<option value="">全部</option>');
            for (var i = 0, j = rows.length; i < j; i++) {
                html.push('<option value="' + rows[i] + '">' + rows[i] + '</option>');
            }
            $('.js-project-manage .js-provider').html(html.join(""));
        } else {
            $('.js-project-manage .js-provider').html('');
        }
    });
};