/**
 *用户选择模块
 */
var ChooseUserModule = {
    //默认搜索机构ID
    searchJgid: "",
    //选择的机构对象
    crtOrg: null,
    //用于返回记录的ID数组
    backArr: ['-1'],
    //选中的用户的id数组
    sidArr: [],
    //选中的用户的手机号码数组
    mobileArr: [],
    //回调函数
    crtCallback: null,
    FLAG_LOADED: false,
    //要获取的用户信息的属性名
    TYPE: null,
    USER: {
        property: {ID: 'id', MOBILE: 'mobile'}
    }
};

//初始化
ChooseUserModule.init = function (type, callback) {
    ChooseUserModule.TYPE = type;
    ChooseUserModule.sidArr = [];
    ChooseUserModule.mobileArr = [];
    ChooseUserModule.FLAG_LOADED = false;
    ChooseUserModule.crtCallback = callback;
    ChooseUserModule.render();
    ChooseUserModule.initEvent();

};

//渲染
ChooseUserModule.render = function () {
    var content = ChooseUserModule.getUserChooseHtml();
    var styleStr = Public.getDialogStyle(1024, 720);
    Public.createSimpleDialog('用户选择', content, 'dp-choose', styleStr);
    $('.dp-choose .easyui-layout').layout();
    //$('.dp-choose #easyui-layout').layout({
    //    height: 600
    //});
    $('.dp-choose .easyui-searchbox').searchbox({
        searcher: ChooseUserModule.search
    });
    $('.dp-choose .dp-ok').linkbutton();
    $('.dp-choose .org-back').linkbutton();


    ChooseUserModule.createTree();
};

//初始化数据
ChooseUserModule.createTree = function () {
    var node = $('.dp-org');
    node.tree({
        url: URI + '/rest/organization/subs',
        onLoadSuccess: function () {
            var root = node.tree('getRoot');
            if (root != null) {
                node.tree('expand', root.target);
                $(root.target).click();
            }
            ChooseUserModule.FLAG_LOADED = true;
        },
        onClick: function (obj) {
            //单击事件：选择机构
            var orgId = obj.id;
            ChooseUserModule.searchJgid = orgId;
            ChooseUserModule.search();   //查询
        }
    });
};

//初始化事件
ChooseUserModule.initEvent = function () {
    //返回按钮
    $('.dp-side-left .org-back').unbind('click').bind('click', function () {
        ChooseUserModule.callbackTreeBack();
    });
    //确定按钮
    $('.dp-choose .dp-ok').unbind('click').bind('click', function () {
        ChooseUserModule.callbackFun();
    });
};


//获取用户选择的页面内容
ChooseUserModule.getUserChooseHtml = function () {
    var html = [];
    html.push('<div id="easyui-layout" class="easyui-layout" fit="true">');

    html.push('<div region="west" title="机构选择" class="dp-side-left" style="width:260px;height: 600px;">');//height:600px;
    html.push('<div class="dp-org"></div>');
    html.push('</div>');

    html.push('<div region="center" border="false">');

    html.push('<div class="dp-content easyui-layout" fit="true">');
    html.push('<div region="north" style="height:45px;padding:10px 0 0 10px;">');
    html.push('<input class="dp-keyword easyui-searchbox" prompt="请输入用户名称..." style="width:300px"/>');
    html.push('<a href="javascript:void(0)" class="dp-ok">确认</a>');
    html.push('</div>');
    html.push('<div region="center">');
    html.push('<table class="user-result easyui-datagrid"></table>');
    html.push('</div></div>');

    html.push('</div>');

    html.push('</div>');
    return html.join('');
};

//查询用户信息
ChooseUserModule.search = function () {

    var searchKey = $('.dp-choose .easyui-searchbox').searchbox('getValue');
    var url = URI + '/rest/user/easyui_query?orgCode=' + ChooseUserModule.searchJgid + '&name=' + encodeURI(searchKey);
    $('.user-result').datagrid({
        url: url,
        pagination: true,
        rownumbers: true,
        pageSize: Public.LIMIT,
        fit: true,
        fitColumns: true,
        resizable: true,
        columns: [[
            {field: 'ck', checkbox: "true", width: 30},
            {field: 'id', title: 'id', align: 'center', width: 80},
            {field: 'name', title: '姓名', align: 'center', width: 80},
            {
                field: 'organization', title: '所属机构', align: 'center', width: 140, formatter: function (value, row) {
                var org = row.organization;
                return org != null ? org.name : '';
            }
            }
        ]],
        onLoadSuccess: function (data) {
            var rows = data.rows;
            for (var i = 0; i < rows.length; i++) {
                var rowData = rows[i];
                if (ChooseUserModule.TYPE === ChooseUserModule.USER.property.MOBILE) {
                    var mobile = rowData.mobile;
                    for (var j = 0; j < ChooseUserModule.mobileArr.length; j++) {
                        if (mobile == ChooseUserModule.mobileArr[j])
                            $('.user-result').datagrid('selectRow', i);
                    }
                } else {
                    var id = rowData.id;
                    for (var j = 0; j < ChooseUserModule.sidArr.length; j++) {
                        if (id == ChooseUserModule.sidArr[j])
                            $('.user-result').datagrid('selectRow', i);
                    }
                }
            }
        },
        onSelect: function (rowIndex, rowData) {
            if (ChooseUserModule.TYPE === ChooseUserModule.USER.property.MOBILE) {
                var mobile = rowData.mobile;
                ChooseUserModule.mobileArr = Public.mergeArray(ChooseUserModule.mobileArr, [mobile]);
            } else {
                var id = rowData.id;
                ChooseUserModule.sidArr = Public.mergeArray(ChooseUserModule.sidArr, [rowData]);
            }
        },
        onUnselect: function (rowIndex, rowData) {
            if (ChooseUserModule.TYPE === ChooseUserModule.USER.property.MOBILE) {
                var mobile = rowData.mobile;
                for (var i = ChooseUserModule.mobileArr.length - 1; i > -1; i--) {
                    if (mobile == ChooseUserModule.mobileArr[i]) {
                        ChooseUserModule.mobileArr.splice(i, 1);
                    }
                }
            } else {
                var id = rowData.id;
                for (var i = ChooseUserModule.sidArr.length - 1; i > -1; i--) {
                    if (rowData == ChooseUserModule.sidArr[i]) {
                        ChooseUserModule.sidArr.splice(i, 1);
                    }
                }
            }
        },
        onSelectAll: function (rows) {
            if (rows != null) {
                if (ChooseUserModule.TYPE === ChooseUserModule.USER.property.MOBILE) {
                    var _mobileArr = [];
                    for (var i = 0; i < rows.length; i++) {
                        var rowData = rows[i];
                        _mobileArr.push(rowData.mobile);
                    }
                    ChooseUserModule.mobileArr = Public.mergeArray(ChooseUserModule.mobileArr, _mobileArr);
                } else {
                    var idArr = [];
                    for (var i = 0; i < rows.length; i++) {
                        var rowData = rows[i];
                        idArr.push(rowData);
                    }
                    ChooseUserModule.sidArr = Public.mergeArray(ChooseUserModule.sidArr, idArr);
                }
            }
        },
        onUnselectAll: function (rows) {
            if (rows != null) {
                for (var i = 0; i < rows.length; i++) {
                    var rowData = rows[i];
                    if (ChooseUserModule.TYPE === ChooseUserModule.USER.property.MOBILE) {
                        var mobile = rowData.mobile;
                        for (var j = ChooseUserModule.mobileArr.length - 1; j > -1; j--) {
                            if (mobile == ChooseUserModule.mobileArr[j]) {
                                ChooseUserModule.mobileArr.splice(j, 1);
                            }
                        }
                    } else {
                        var id = rowData.id;
                        for (var j = ChooseUserModule.sidArr.length - 1; j > -1; j--) {
                            if (rowData == ChooseUserModule.sidArr[j]) {
                                ChooseUserModule.sidArr.splice(j, 1);
                            }
                        }
                    }
                }
            }
        }
    });
};

//用户选择完成后的回调函数
ChooseUserModule.callbackFun = function () {
    if (ChooseUserModule.TYPE === ChooseUserModule.USER.property.MOBILE) {
        ChooseUserModule.crtCallback(ChooseUserModule.mobileArr);
    } else {
        ChooseUserModule.crtCallback(ChooseUserModule.sidArr);
    }
    $('.dp-choose').dialog('destroy');
};

/**
 * 机构加载后执行参数中的回掉函数
 * @param {Object} callback
 */
ChooseUserModule.loaded = function (callback) {
    setTimeout(function () {
        if (ChooseUserModule.FLAG_LOADED) {
            callback();
        } else {
            ChooseUserModule.loaded(callback);
        }
    }, 600);
};
