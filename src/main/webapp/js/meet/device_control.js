/**
 * 终端控制
 * @type {{}}
 */
var DeviceControl = {

};
/**
 * 初始化
 */
DeviceControl.init = function () {
    $('#main-view').load('partials/meet/device_control.html', function(){
        DeviceControl.loadData();
        DeviceControl.initEvent();
    });
};

/**
 * 加载数据
 */
DeviceControl.loadData = function () {
    //加载会场数据
    //$('.js-device-tree').tree({
    //    url:'json/device-tree.json'
    //});
    OrgDeviceTree.loadOrgDeviceTree({
        el: '.js-device-tree',
        onClickDeviceFun: function (node) {
            var title = node.text + '(' + node.attributes.ip + ')';
            $('.js-device-name').html(title);
        },
        initFun: function () {
            $('.js-device-tree').tree('expandAll');
            //点击第一个设备
            var roots = $('.js-device-tree').tree('getRoots');
            if (roots != null && roots.length > 0) {
                var leaf = null;
                for (var i = 0, j = roots.length; i < j; i++) {
                    leaf = CreateMeet.getDeviceNode('.js-device-tree', roots[i]);
                    if (leaf != null) {
                        break;
                    }
                }
                if (leaf != null) {
                    $(leaf.target).click();
                }
            }
        }
    });
};
/**
 * 初始化事件
 */
DeviceControl.initEvent = function () {
    //号码点击事件
    $('.tel-book-table .js-no').unbind('click').bind('click', function () {
        var no = $(this).html();
        var iptObj = $('.tel-book-table .js-phone');
        var tel = iptObj.val();
        iptObj.val(tel + no);
    });
};
/**
 * 获取设备节点
 */
CreateMeet.getDeviceNode = function (el, node) {
    var attrs = node.attributes;
    var type = attrs != null ? attrs.nodeType : '';
    //组织机构没有下级，则加载设备数据
    if (type == 'device') {
        return node;
    }
    var subs = $(el).tree('getChildren', node.target);
    if (subs == null || subs.length == 0) {
        return null;
    }
    for (var i = 0, j = subs.length; i < j; i++) {
        var leaf = CreateMeet.getDeviceNode(el, subs[i]);
        if (leaf != null) {
            return leaf;
        }
    }
};