/**
 * 页面主模块
 * @type {{}}
 */
var Index = {
};
/**
 * 初始化
 */
Index.init = function(){
	Index.render();
    Router.run();
};
/**
 * 渲染页面
 */
Index.render = function(){
    $('.easyui-layout').layout();
};
/**
 * 设置左边栏的标题文字
 */
Index.setSideTitle = function(title){
    $('.layout-panel-west .panel-title').first().html(title);
};
/**
 * 渲染标题菜单
 */
Index.activeTopMenu = function(menu){
    var href = window.location.href;
    var menu = href.split('#/')[1].split('/')[0];
    var node = $('.header-menu a[href="#/'+menu+'"]');
    var current = node.parents('li');
    $('.header-menu li').not(current).removeClass('active');
    current.addClass('active');

    //设置左边栏标题
    Index.setSideTitle(node.html());
};
/**
 * 设置导航标题
 * @param nav
 */
Index.renderMainTitle = function () {
    var nav = $('.side-menu li.active span:last').html();
    $('.navigation .nav').html(nav);
};
/**
 * 渲染左边栏菜单
 */
Index.activeSideMenu = function(){
    var hash = window.location.hash;
    var node = $('#side-left .side-menu a[href="'+hash+'"]');
    if(node == null || node.length == 0){
        node = $('#side-left .side-menu a:first');
    }
    var current = node.parents('li');
    $('#side-left .side-menu li').not(current).removeClass('active');
    current.addClass('active');

    Index.renderMainTitle();
};