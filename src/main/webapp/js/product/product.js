/**
 * 产品发布
 * @type {{}}
 */
var Product = {

};
/**
 * 初始化
 */
Product.init = function () {
    $('#side-left').load('partials/product/product_side.html', function(){
        $('#main-view').load('partials/product/product.html', function(){
            Product.render();
        });
    });
};
/**
 * 渲染页面
 */
Product.render = function () {
    Index.renderMainTitle();
};