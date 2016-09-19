/**
 * 组织机构会议室树模块
 * @type {{}}
 */
var OrgBoardroomTree = {
    default_config: {}
};
/**
 * 初始化
 * @param config
 */
OrgBoardroomTree.loadOrgBoardroomTree = function (config) {
    config = config || OrgBoardroomTree.default_config;
    var node = $(config.el);
    node.tree({
        url: URI + '/rest/organization/subs',
        onExpand: function (obj) {
            OrgBoardroomTree.expandFun(node, obj);
        },
        onLoadSuccess: function () {
            var root = node.tree('getRoot');
            if (root != null) {
                node.tree('expand', root.target);
            }
            if (config.initFun != null) {
                config.initFun();
            }
        },
        onClick: function (obj) {
            var attrs = obj.attributes;
            var type = attrs != null ? attrs.nodeType : '';
            if (type == 'boardroom' && config.onClickDeviceFun != null) {
                config.onClickDeviceFun(obj);
            }
        }
    });
};
/**
 * 展开树节点执行的方法
 * @param node
 * @param obj
 */
OrgBoardroomTree.expandFun = function (node, obj) {
    $(obj.target).siblings('ul').remove();

    var attrs = obj.attributes;
    var type = attrs != null ? attrs.nodeType : '';
    var id = obj.id;
    //组织机构没有下级，则加载设备数据
    if (type == 'org_leaf') {
        Public.getRest('/boardroom/query?limit=999999&orgCode=' + id, function (resp) {
            var devices = resp.items;
            if (devices != null && devices.length > 0) {
                for (var i = 0, j = devices.length; i < j; i++) {
                    var device = devices[i];
                    device.state = 'open';
                    device.text = device.name;
                    device.attributes = {'nodeType': 'boardroom', 'ip': device.ip};
                    devices[i] = device;
                }
            }
            node.tree('append', {
                parent: obj.target,
                data: devices
            });
        });
    } else {
        //有下级组织机构，加载下级数据
        $.get(URI + '/rest/organization/subs?id=' + id, function (subs) {
            if (subs != null && subs.length > 0) {
                for (var i = 0, j = subs.length; i < j; i++) {
                    var sub = subs[i];
                    var type = sub.count == 0 ? 'org_leaf' : null;
                    sub.state = 'closed';
                    sub.attributes = {'nodeType': type};
                    subs[i] = sub;
                }
            }
            node.tree('append', {
                parent: obj.target,
                data: subs
            });
        });
    }
};

