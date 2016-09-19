/**
 * 设置应用路由
 */
var Router = {
    app : null
};

Router.run = function() {
    /**
     * 1、路由设置
     * 2、初始路由至 #resource
     */
    Router.app = $.sammy(function() {
        //在所有路由之前运行
        this.before({}, function() {
            Index.activeTopMenu();
        });
        //服务管理
        this.get(/\#\/service/, function() {
            Service.init(this);
        });
        //运行监控
        this.get(/\#\/monitor/, function() {
        	Monitor.init(this);
        });
        //调用管理
        this.get(/\#\/usage/, function() {
            Usage.init(this);
        });
        //系统管理
        this.get(/\#\/system/, function() {
            System.init(this);
        });
        
        //配置管理
        this.get(/\#\/config/, function() {
            Config.init(this);
        });
    });
    this.app.run('#/service');
};

