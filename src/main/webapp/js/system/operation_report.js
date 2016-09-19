/**
 * 运行报告控制
 * @type {{}}
 */
var OperationReport = {
    //文件共享的数量
    COUNT: 0
};
/**
 * 初始化
 */
OperationReport.init = function () {
    $('#main-view').load('partials/system/report_control.html', function(){
        OperationReport.loadData();
    });
};
/**
 * 加载数据
 */
OperationReport.loadData = function () {
    var url = URI + '/rest/operation/get_opreation_report';
    //加载设备表格数据
    $('.js-file-table').datagrid({
        //url:'json/report-datagrid.json',
        url: url,
        pagination:true,
        rownumbers:true,
        singleSelect: true,
        pageSize: Public.LIMIT,
        columns:[[
            {field:'id',title:'ID',hidden:true},
            {field:'name',title:'报告名称',width:200},
            {field: 'date', title: '报告时间', width: 200},
            {field: 'type', title: '类型', width: 100},
            {
                field: 'operate', title: '操作', width: 200, formatter: function (value, row) {
                var fileName = encodeURI(row.name);
                var url = URI + '/rest/operation/download_report?fileName=' + fileName;
                var html = [];
                html.push('<div class="a-btn-group">');
                html.push('<a href="' + url + '" class="a-btn">下载</a>');
                html.push('</div>');
                return html.join('');
            }}
        ]],
        onLoadSuccess: function (data) {
            var total = data.total;
            if (total == 0) {
                //添加一个新数据行，第一列的值为你需要的提示信息
                $(this).datagrid('appendRow', {name: '<div style="text-align:center;color:red">没有相关记录！</div>'}).datagrid('mergeCells', {
                    index: 0,
                    field: 'name',
                    colspan: 5
                });
            }
            OperationReport.COUNT = total;
            //开启文件定时器
            if (Index.open_file_interval) {
                Index.file_interval = setInterval("FileControl.getNewest()", Index.interval_file_time);
            }
        }
    });
};
/**
 * 添加文件
 */
OperationReport.add = function () {
    Public.createDialog('分享文件', '', 'p-add-file', 400, 200);
    $('.p-add-file .p-dialog-content').load('partials/device/form_file.html', function () {
        $('#uploadForm').attr('action', URI + '/rest/attachment');
    });

    //上传文件
    $('.p-add-file .js-ok').unbind('click').bind('click', function () {
        $('#uploadForm').ajaxSubmit(function (resp) {
            if (resp != null && resp.status == 'success') {
                Public.msg('文件分享成功');
                $('.p-add-file').dialog('destroy');
                OperationReport.loadData();
            } else {
                Public.alert('文件上传失败');
                Public.debug('获取失败：' + reqUrl);
            }
        });
    });
};
/**
 * 删除文件
 * @param id
 * @param name
 * @param e
 */
OperationReport.deletes = function (id, name, e) {
    Public.stopPropagation(e);
    Public.comfirm('确定要删除文件' + name + '吗', function () {
        Public.deleteRest('/attachment/' + id, null, function () {
            Public.msg('删除成功');
            OperationReport.loadData();
        });
    });
};
/**
 * 获取最新文件
 */
OperationReport.getNewest = function () {
    Public.getRest('/attachment/newest', function (count) {
        if (count > OperationReport.COUNT) {
            var add = count - OperationReport.COUNT;
            OperationReport.COUNT = count;
            Public.msg('新增分享文件' + add + '个，请<a href="#/device/file_control" style="color: #0000ff;">前往</a>查看!');
            //TODO:如果当前页面在文件控制页，则将新文件记录加入表格中
            OperationReport.loadData();
        }
    });
};
/**
 * 选择用户
 */
OperationReport.chooseUser = function () {
    ChooseUserModule.init(null, function (users) {
        if (users.length != 0) {
            var userIds = [];
            var usernames = [];
            for (var i = 0, j = users.length; i < j; i++) {
                var user = users[i];
                userIds.push(user.id);
                usernames.push(user.name);
            }
            $('#uploadForm input[name="user"]').val(userIds);
            $('#uploadForm .js-name-view').val(usernames);
        } else {
            $('#uploadForm input[name="user"]').val('全部');
            $('#uploadForm .js-name-view').val('全部');
        }

    });
};