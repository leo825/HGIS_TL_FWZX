/**
 * 会议控制
 * @type {{}}
 */
var MeetControl = {
    //当前选择的会议ID
    MID: null
};
/**
 * 会议控制
 */
MeetControl.init = function () {
    $('#main-view').load('partials/meet/meet_control.html', function(){
        MeetControl.MID = null;
        MeetControl.render();
        MeetControl.loadData();
    });
};
/**
 * 渲染页面
 */
MeetControl.render = function () {
    $('input.js-search').searchbox({
        width:180,
        searcher:function(value,name){
            MeetControl.loadDeviceData(MeetControl.MID, value);
        },
        prompt:'请输入关键字'
    });
};
/**
 * 加载数据
 */
MeetControl.loadData = function () {
    //加载会议列表数据
    MeetControl.loadMeetList();
    MeetControl.loadMeetTreegrid();
    MeetControl.loadOrgDeviceDropTree();
};
/**
 * 加载会议列表数据
 */
MeetControl.loadMeetList = function () {
    Public.getRest('/conference', function (ms) {
        if (ms == null || ms.length == 0) {
            $('.js-meet-list').html('<option value="-1">没有会议</option>');
            //MeetControl.loadDeviceData('-1');
            return null;
        }
        var html = [];
        for (var i = 0, j = ms.length; i < j; i++) {
            var m = ms[i];
            html.push('<option value="' + m.id + '">' + m.name + '</option>');
        }
        $('.js-meet-list').html(html.join(''));
        $('.js-meet-list').unbind('change').bind('change', function () {
            var mid = $(this).val();
            MeetControl.MID = mid;
            //MeetControl.loadDeviceData(mid);
            $('.js-device-table').treegrid('expand', mid);
        });
        var mid = $('.js-meet-list').val();
        MeetControl.MID = mid;
        //MeetControl.loadDeviceData(mid);
        $('.js-device-table').treegrid('expand', mid);
    });
};
/**
 * 加载会场连接终端
 */
/*MeetControl.loadDeviceData = function (mid, searchWord) {
    var url = URI + '/rest/conference/' + mid + '/device/easyui_query';
    if (!Public.isNull(searchWord)) {
        url += '?searchWord=' + searchWord;
    }
    //加载会场连接终端
    $('.js-device-table').datagrid({
        //url:'json/meet-device-datagrid.json',
        url: url,
        pagination:true,
        rownumbers:true,
        singleSelect: true,
        pageSize: Public.LIMIT,
        columns:[[
            {field:'id',title:'ID',hidden:true},
            {field:'name',title:'会场名',width:200},
            {field:'remark',title:'备注',width:100},
            {field:'ip',title:'IP',width:100},
            {field:'status',title:'状态',width:100},
            {field:'audio',title:'音频',width:100},
            {field:'video',title:'视频',width:100},
            {field:'role',title:'角色',width:100},
            {field:'content',title:'内容',width:100},
            {field:'mcu',title:'所属MCU',width:100}
        ]],
        onLoadSuccess: function (data) {
            var total = data.total;
            $('.js-device-total').html(total);
            if (total == 0) {
                Public.showNoResult('.js-device-table', 'name', 9);
            } else {
                $('.js-device-table').datagrid('enableDnd');
            }
        },
        onStopDrag: function (target) {
            $('.js-device-tree').hover(function () {
                $('.js-device-tree').unbind('hover');
                //向会场列表加入该对象
                var mid = MeetControl.MID;
                if (mid != null) {
                    Public.deleteRest('/conference/' + mid + '/device?deviceId=' + target.id, null, function () {
                        Public.msg('删除成功');
                        MeetControl.loadDeviceData(mid);
                    });
                }
            });
        }
    });
 };*/
/**
 * 加载机构设备树，可以拖拽
 * @param config
 */
MeetControl.loadOrgDeviceDropTree = function () {
    var node = $('.js-device-tree');
    node.tree({
        dnd: true,
        url: URI + '/rest/organization/subs',
        onExpand: function (obj) {
            OrgDeviceTree.expandFun(node, obj);
        },
        onLoadSuccess: function () {
            var root = node.tree('getRoot');
            if (root != null) {
                node.tree('expand', root.target);
            }
        },
        onBeforeDrag: function (target) {
            var attrs = target.attributes;
            var type = attrs != null ? attrs.nodeType : '';
            if (type != 'device') {
                return false;
            }
        },
        onStopDrag: function (target) {
            $('.js-device-table').parents('.datagrid').mouseup(function () {
                //向会场列表加入该对象
                var mid = MeetControl.MID;
                if (mid != null) {
                    Public.postRest('/conference/' + mid + '/device?deviceId=' + target.id, null, function () {
                        Public.msg('添加成功');
                        //MeetControl.loadDeviceData(mid);
                        $('.js-device-table').treegrid('reload', mid);
                    });
                }
                $('.js-device-table').parents('.datagrid').unbind('mouseup');
            });
        },
        onBeforeDrop: function () {
            return false;
        },
        onDrop: function () {
            return false;
        }
    });
};
/**
 * 加载会议的树网格，第二级为会议的设备
 */
MeetControl.loadMeetTreegrid = function () {
    var url = URI + '/rest/conference/easyui_query';
    //加载会场连接终端
    $('.js-device-table').treegrid({
        //url:'json/meet-device-datagrid.json',
        idField: 'id',
        treeField: 'name',
        url: url,
        pagination: true,
        rownumbers: true,
        singleSelect: true,
        pageSize: Public.LIMIT,
        columns: [[
            {field: 'id', title: 'ID', hidden: true},
            {field: 'name', title: '会场名', width: 200},
            {field: 'remark', title: '备注', width: 100},
            {field: 'ip', title: 'IP', width: 100},
            {field: 'status', title: '状态', width: 100},
            {field: 'audio', title: '音频', width: 100},
            {field: 'video', title: '视频', width: 100},
            {field: 'role', title: '角色', width: 100},
            {field: 'content', title: '内容', width: 100},
            {field: 'mcu', title: '所属MCU', width: 100}
        ]],
        loadFilter: function (data) {
            var rows = data.rows;
            if (rows != null && rows.length > 0) {
                for (var i = 0, j = rows.length; i < j; i++) {
                    rows[i].state = 'closed';
                    rows[i].nodeType = 'c';
                }
            }
            data.rows = rows;
            return data;
        },
        onLoadSuccess: function (row, data) {
            var total = data.total;
            $('.js-device-total').html(total);
            if (total == 0) {
                Public.showNoResult('.js-device-table', 'name', 9);
            } else {
                $('.js-device-table').treegrid('enableDnd', row ? row.id : null);
            }
        },
        onExpand: function (row) {
            var mid = row.id;
            MeetControl.MID = mid;
            $('.js-device-table').treegrid('select', mid);
            var cs = $('.js-device-table').treegrid('getChildren', mid);
            if (cs != null && cs.length > 0) {
                for (var i = 0, j = cs.length; i < j; i++) {
                    $('.js-device-table').treegrid('remove', cs[i].id);
                }
            }
            Public.getRest('/conference/' + mid + '/device', function (devices) {
                //因为设备有可能重复，所以加上会议ID前缀，防止onExpand时将别的会议的设备移除
                if (devices != null && devices.length > 0) {
                    for (var i = 0, j = devices.length; i < j; i++) {
                        devices[i].id = mid + '-' + devices[i].id;
                    }
                }
                $('.js-device-table').treegrid('append', {
                    parent: mid,
                    data: devices
                });
            });
            $('.js-meet-list').val(mid);
        },
        onSelect: function (row) {
            if (row.nodeType == 'c') {
                var mid = row.id;
                MeetControl.MID = mid;
                $('.js-meet-list').val(mid);
            }
        },
        onBeforeDrag: function (target) {
            if (target != null && target.nodeType == 'c') {
                return false;
            }
            return true;
        },
        onStopDrag: function (target) {
            $('.js-device-tree').mouseup(function () {
                var mid = MeetControl.MID;
                if (mid != null) {
                    var deviceId = target.id.split('-')[1];
                    Public.deleteRest('/conference/' + mid + '/device?deviceId=' + deviceId, null, function () {
                        Public.msg('删除成功');
                        //MeetControl.loadDeviceData(mid);
                        $('.js-device-table').treegrid('reload', mid);
                    });
                }
                $('.js-device-tree').unbind('mouseup');
            });
        },
        onBeforeDrop: function () {
            return false;
        }
    });
};