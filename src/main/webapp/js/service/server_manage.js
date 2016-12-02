/**
 * 应用管理
 * @type {{}}
 */
var ServerManage = {
    oldService: {}
};
/**
 * 初始化
 */
ServerManage.init = function () {
    $('#main-view').load('partials/service/server_manage.html', function () {
        ServerManage.render();
    });
};
/**
 * 渲染页面
 */
ServerManage.render = function () {
    ServerManage.loadData();
    ServerManage.loadTypeDataOfForm();
};
/**
 * 加载数据
 */
ServerManage.loadData = function () {
    var params = ServerManage.getSearchParams();
    var url = URI + '/rest/service/easyui_query';
    if (params.length > 0) {
        url += '?' + params.join('&');
    }
    //加载应用表格数据
    $('.js-service-table').datagrid({
        //url:'json/service-server-datagrid.json',
        url: url,
        pagination: true,
        rownumbers: true,
        singleSelect: true,
        pageSize: Public.LIMIT,
        columns: [[
            {field: 'id', title: 'ID', hidden: true},
            {field: 'name', title: '接口名称', width: 200, sortable: true, sorter: 'mysort'},
            {
                field: 'serverAddress', title: '接口地址', width: 300, formatter: function (value, row) {
                var serverAddress = row.serverAddress;
                return serverAddress != null ? serverAddress : '';
            }
            },
            {field: 'project', title: '应用名称', width: 100},
            {field: 'dataReturnType', title: '返回数据类型', width: 100},
            {field: 'description', title: '接口描述', width: 200},
            {
                field: 'operate', title: '操作', width: 200, formatter: function (value, row) {
                if (Public.isInArray("add", RESOURCES)) {
                    $("#addBtn").show();
                }
                var id = row.id;
                var html = [];
                html.push('<div class="a-btn-group">');
                //html.push('<a href="javascript:void(0)" class="a-btn" onclick="ServerManage.show(\'' + id + '\');">查看</a>');
                html.push('<a href="javascript:void(0)" class="a-btn" onclick="ServerManage.update(\'' + id + '\');">修改</a>');
                html.push('<a href="javascript:void(0)" class="a-btn" onclick="ServerManage.deletes(\'' + id + '\',\'' + row.name + '\');">删除</a>');
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
                    colspan: 6
                });
                $(this).closest('div.datagrid-wrap').find('div.datagrid-pager').hide();
            }
        }
    });
    $('.js-service-table').datagrid({loadFilter: ServerManage.pagerFilter});
};


function mysort(a, b) {
    return (a > b ? 1 : -1);
}

/**
 * 分页过滤器
 * */
ServerManage.pagerFilter = function (data) {
    var dg = $('.js-service-table');
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
 * 增加接口信息
 */
ServerManage.add = function () {
    Public.createDialog('添加接口', '', 'p-add-service', 400, 300);
    $('.p-add-service .p-dialog-content').load('partials/service/add_service.html', function () {
        ServerManage.initFormData();
    });

    //保存应用信息
    $('.p-add-service .js-ok').unbind('click').bind('click', function () {
        var service = ServerManage.checkServiceForm();
        if (service != null) {
            Public.getRest('/service/get_route?serverName=' + service.serverName + '&projectId=' + service.projectId, function (dicts) {
                if (dicts != null && dicts.length > 0) {
                    Public.alert(service.serverName+'已经存在于应用中，同一应用中接口名称不可重复');
                    return null;
                } else {
                    Public.postRest('/service', service, function (resp) {
                        Public.msg('添加成功');
                        $('.p-add-service').dialog('destroy');
                        //重新加载数据
                        ServerManage.loadData();
                    });
                }
            });
        }
    });
};

/**
 * 批量导入接口信息
 *
 * */
ServerManage.batchAdd = function () {
    Public.createDialog('批量添加接口', '', 'p-add-service', 300, 200);
    $('.p-add-service .p-dialog-content').load('partials/service/batch_add_service.html', function () {
        ServerManage.initFormData();
    });
    //保存应用信息
    $('.p-add-service .js-ok').unbind('click').bind('click', function () {
        var obj = {};
        var projectId = $('.p-add-service select[name="projectName"]').val();
        if (!Public.isNull(projectId)) {
            obj.projectId = projectId;
            var url = URI + '/rest/service/batch_add';
            if ($("#excelToUpload").val().length > 0 && projectId != null) {
                $.ajaxFileUpload({
                    fileElementId: 'excelToUpload', //文件上传域的ID
                    url: url, //用于文件上传的服务器端请求地址
                    secureuri: false, //是否需要安全协议，一般设置为false
                    data: obj,
                    async: true,
                    dataType: 'json', //返回值类型 一般设置为text,如果是json，会有问题"<pre style="word-wrap: break-word; white-space: pre-wrap;"></pre>"
                    success: function (resp) //服务器成功响应处理函数
                    {
                        $('.p-add-service').dialog('destroy');
                        if (resp != null) {
                            var data = JSON.parse(resp);
                            Public.msg('一共需要添加' + data.total + '个接口,其中' + data.failed + '个接口添加失败！！');
                        } else {
                            Public.msg('批量添加接口失败,文件解析失败,文件类型应该为excel');
                        }
                        ServerManage.loadData();
                    }
                });
            }
        } else {
            Public.alert("应用名称不能为空");
            return null;
        }
    });

};


/**
 * 修改接口
 */
ServerManage.update = function (id, e) {
    var oldService;
    Public.stopPropagation(e);
    Public.createDialog('修改接口', '', 'p-add-service', 400, 300);
    $('.p-add-service .p-dialog-content').load('partials/service/update_service.html', function () {
        ServerManage.showServiceInfo(id);
    });

    //修改应用信息
    $('.p-add-service .js-ok').unbind('click').bind('click', function () {
        var service = ServerManage.checkServiceForm();
        delete ServerManage.oldService.regTime;
        var oldService = ServerManage.oldService;
        if (service != null) {
            service.id = id;
            if (Public.Compare(service, oldService)) {
                Public.alert('没有做任何修改');
                return null;
            }
            Public.putRest('/service', service, function (resp) {
                Public.msg('修改成功');
                $('.p-add-service').dialog('destroy');
                //重新加载数据
                ServerManage.loadData();
            });
            ServerManage.oldService = {};
        }
    });
};
/**
 * 查看接口信息
 */
ServerManage.show = function () {
    var message = 'message';
    return message;
};
/**
 * 删除接口信息
 */
ServerManage.deletes = function (id, name, e) {
    Public.stopPropagation(e);
    Public.comfirm('确定要删除接口信息' + name + '吗', function () {
        Public.deleteRest('/service/' + id, null, function () {
            Public.msg('删除成功');
            ServerManage.loadData();
        });
    });
};

/**
 * 获取查询参数列表
 */
ServerManage.getSearchParams = function () {
    var name = $('.js-server-manage .js-name').val();
    var projectId = $('.js-server-manage .js-project').val();
    var params = [];
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
ServerManage.checkServiceForm = function () {
    var serverName = $('.p-add-service input[name="serverName"]').val();
    var projectId = $('.p-add-service select[name="projectName"]').val();
    var serverAddr = $('.p-add-service input[name="serverAddr"]').val();
    var dataReturnType = $('.p-add-service input[name="dataReturnType"]').val();
    var description = $('.p-add-service input[name="description"]').val();
    if (Public.isNull(serverName)) {
        Public.alert('接口名称不能为空');
        return null;
    } else if (Public.isNull(projectId)) {
        Public.alert('应用不能为空');
        return null;
    } else if (Public.isNull(serverAddr)) {
        Public.alert("接口地址不能为空");
        return null;
    } else if (Public.isNull(dataReturnType)) {
        Public.alert("接口返回类型不能为空");
        return null;
    } else {
        if (!Public.isApi(serverAddr)) {
            Public.alert("接口地址的格式应如下所示：http://192.168.0.101:7001/PGIS/XMLPort");
            return null;
        }
        var obj = {};
        obj.serverName = serverName;
        obj.projectId = projectId;
        obj.serverAddr = serverAddr;
        obj.dataReturnType = dataReturnType;
        obj.description = description;
        return obj;
    }
};

/**
 * 初始化表单数据
 */
ServerManage.initFormData = function (initBoardroomTypeValue, initNetworkValue, initOrgValue) {
//    //加载应用类型数据
//    ServerManage.loadServerTypeData('.p-add-service select[name="type"]', initBoardroomTypeValue);
//    //加载网络拓扑数据
//    ServerManage.loadNetworkData('.p-add-service select[name="network"]', initNetworkValue);
//    //加载组织机构数据
//    ServerManage.loadOrgTreeData('.p-add-service .js-org-tree', initOrgValue);
    //加载所有的应用
    ServerManage.loadProjects();
};

/**
 * 加载应用类型数据
 */
ServerManage.loadServerTypeData = function (el, initValue) {
    Public.getRest('/dictionary/query?group=BOARDROOM_TYPE', function (dicts) {
        if (dicts != null && dicts.length > 0) {
            var html = [];
            for (var i = 0, j = dicts.length; i < j; i++) {
                var dict = dicts[i];
                html.push('<option value="' + dict.id + '">' + dict.name + '</option>');
            }
            $(el).html(html.join(""));
        } else {
            $(el).html('');
        }
        if (initValue != null) {
            $(el).val(initValue);
        }
    });
};

/**
 * 加载网络拓扑数据
 */
ServerManage.loadNetworkData = function (el, initValue) {
    //Public.getRest('/dictionary/query?group=BOARDROOM_TYPE', function(dicts){
    $.getJSON('json/network_list.json', function (nets) {
        if (nets != null && nets.length > 0) {
            var html = [];
            for (var i = 0, j = nets.length; i < j; i++) {
                var net = nets[i];
                html.push('<option value="' + net.id + '">' + net.name + '</option>');
            }
            $(el).html(html.join(""));
        } else {
            $(el).html('');
        }
        if (initValue != null) {
            $(el).val(initValue);
        }
    });
};

/**
 * 加载所有应用
 *
 * */
ServerManage.loadProjects = function (projectId) {
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


/**
 * 显示应用信息
 * @param id
 */
ServerManage.showServiceInfo = function (id) {
    Public.getRest('/service/' + id, function (service) {
        var serverName = service.serverName;
        var projectId = service.projectId;
        var serverAddr = service.serverAddr;
        var dataReturnType = service.dataReturnType;
        var description = service.description;
        $('.p-add-service input[name="serverName"]').val(service.serverName);
        $('.p-add-service input[name="serverAddr"]').val(service.serverAddr);
        $('.p-add-service input[name="dataReturnType"]').val(service.dataReturnType);
        $('.p-add-service input[name="description"]').val(service.description);
        ServerManage.loadProjects(projectId);
        ServerManage.oldService = service;
    });
};

/**
 * 获取查询表单中的应用类型
 */
ServerManage.loadTypeDataOfForm = function () {
    Public.getRest('/project', function (dicts) {
        if (dicts != null && dicts.length > 0) {
            var html = [];
            html.push('<option value="">全部</option>');
            for (var i = 0, j = dicts.length; i < j; i++) {
                var dict = dicts[i];
                html.push('<option value="' + dict.id + '">' + dict.name + '</option>');
            }
            $('.js-server-manage .js-project').html(html.join(""));
        } else {
            $('.js-server-manage .js-project').html('');
        }
    });
};