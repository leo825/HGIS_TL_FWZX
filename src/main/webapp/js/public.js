/**
 * 通用类
 * @type {{}}
 */
var Public = {
    //页容量
    LIMIT: 10
};
/**
 * 发送POST方式的REST请求
 * @param url 请求路径
 * @param data 数据
 * @param successFun 成功时的回调函数
 * @param errMsg 失败时提示信息
 */
Public.postRest = function (url, data, successFun, errMsg) {
    var reqUrl = URI + '/rest' + url;
    $.ajax({
        type: 'POST',
        url: reqUrl,
        data: JSON.stringify(data),
        contentType: 'application/json',
        success: function (resp) {
            if (resp != null && resp.status == 'success') {
                successFun(resp.data);
            } else {
                var msg = errMsg != null ? errMsg : '数据保存失败';
                Public.alert(msg);
                Public.debug('保存失败：' + reqUrl);
            }
        }
    });
};
/**
 * 发送DELETE方式的REST请求
 * @param url 请求路径
 * @param data 数据
 * @param successFun 成功时的回调函数
 * @param errMsg 失败时提示信息
 */
Public.deleteRest = function (url, data, successFun, errMsg) {
    var reqUrl = URI + '/rest' + url;
    $.ajax({
        type: 'DELETE',
        url: reqUrl,
        data: JSON.stringify(data),
        contentType: 'application/json',
        success: function (resp) {
            if (resp != null && resp.status == 'success') {
                successFun(resp.data);
            } else {
                var msg = errMsg != null ? errMsg : '数据删除失败';
                Public.alert(msg);
                Public.debug('删除失败：' + reqUrl);
            }
        }
    });
};
/**
 * 发送PUT方式的REST请求
 * @param url 请求路径
 * @param data 数据
 * @param successFun 成功时的回调函数
 * @param errMsg 失败时提示信息
 */
Public.putRest = function (url, data, successFun, errMsg) {
    var reqUrl = URI + '/rest' + url;
    $.ajax({
        type: 'PUT',
        url: reqUrl,
        data: JSON.stringify(data),
        contentType: 'application/json',
        success: function (resp) {
            if (resp != null && resp.status == 'success') {
                successFun(resp.data);
            } else {
                var msg = errMsg != null ? errMsg : '数据更新失败';
                Public.alert(msg);
                Public.debug('更新失败：' + reqUrl);
            }
        }
    });
};
/**
 * 发送GET方式的REST请求
 * @param url 请求路径
 * @param successFun 成功时的回调函数
 * @param errMsg 失败时提示信息
 */
Public.getRest = function (url, successFun, errMsg) {
    var reqUrl = URI + '/rest' + url;
    $.get(reqUrl, function (resp) {
        if (resp != null && resp.status == 'success') {
            successFun(resp.data);
        } else {
            var msg = errMsg != null ? errMsg : '获取数据为空';
            Public.alert(msg);
            Public.debug('获取为空：' + reqUrl);
        }
    });
};
/**
 * 发送GET方式的REST请求
 * @param url 请求路径
 * @param successFun 成功时的回调函数
 * @param errMsg 失败时提示信息
 */
Public.getSyncRest = function (url, successFun, errMsg) {
    var reqUrl = URI + '/rest' + url;
    $.ajax({
        type: 'GET',
        async: false,
        url: reqUrl,
        contentType: 'application/json',
        success: function (resp) {
            if (resp != null && resp.status == 'success') {
                successFun(resp.data);
            } else {
                var msg = errMsg != null ? errMsg : '获取数据失败';
                Public.alert(msg);
                Public.debug('获取失败：' + reqUrl);
            }
        }
    });
};

/**
 * 显示提示信息
 * @param msg
 */
Public.msg = function (msg) {
    var delay = arguments[1] ? arguments[1] : 5000;//延时使消息消失
    setTimeout(function () {
        $.messager.show({
            title: '消息',
            msg: msg,
            timeout: delay
        });
    }, 100);
};
/**
 * 显示警告信息
 */
Public.alert = function (msg) {
    $.messager.alert('警告', msg, 'warnimg');
};

/**
 * 显示提示信息
 * */
Public.infoMsg = function (msg) {
    $.messager.alert('信息', msg, 'message');
};

/**
 * 显示操作确认框
 * @param msg
 * @param callback
 */
Public.comfirm = function (msg, callback) {
    $.messager.confirm('确认', msg, function (r) {
        if (r) {
            callback();
        }
    });
};

/**
 * 判断是否为空或者空字符串
 */
Public.isNull = function (text) {
    return text == null || text == '';
};
/**
 * 处理text为空的情况，当text为空时用cover替换
 * @param text 要判断是否为空的字符串
 * @param cover 替换的字符串
 * @returns {*}
 */
Public.dealNull = function (text, cover) {
    if (cover == null) {
        cover = '';
    }
    text = Public.isNull(text) ? cover : text;
    return text;
};

/**
 * 判断是否为正整数
 * @num
 * */
Public.isInt = function (num) {
    var reg = /^\+?[1-9][0-9]*$/;
    return reg.test(num);
};

/**
 * 判断是否为正小数
 * @str
 * */
Public.isDouble = function (str) {
    if (str.match(/^(:?(:?\d+.\d+)|(:?\d+))$/)) {
        return true;
    }
    return false;
};

/**
 * 判断字符串是否为ip地址
 * @param str
 */
Public.isIp = function (str) {
    var re = /^([0-9]|[1-9]\d|1\d\d|2[0-4]\d|25[0-5])\.([0-9]|[1-9]\d|1\d\d|2[0-4]\d|25[0-5])\.([0-9]|[1-9]\d|1\d\d|2[0-4]\d|25[0-5])\.([0-9]|[1-9]\d|1\d\d|2[0-4]\d|25[0-5])$/;
    if (re.test(str)) {
        return true;
    }
    return false;
};

/**
 * 判断字符串是否符合接口格式。例如：http://www.runoob.com:80/html/html-tutorial.html
 *@param str
 * */
Public.isApi = function (str) {
    var re = /(\w+):\/\/([^/:]+)(:\d*)?([^# ]*)/;
    if (re.test(str)) {
        return true;
    }
    return false;
}


/**
 *创建弹出对话框
 * @param {Object} title    对话框标题
 * @param {Object} content  对话框内容
 * @param {Object} classStr 对话框的class
 * @param {Object} width 宽度
 * @param {Object} height 高度
 * @param {Object} isClose  是表示关闭，否表示销毁
 */
Public.createDialog = function (title, content, classStr, width, height,
                                isClose, isModal) {
    var styleStr = Public.getDialogStyle(width, height);
    isModal = isModal == null ? true : isModal;
    content = '<div class="p-dialog-content">'
    + content
    + '</div><div class="messager-button"><a href="javascript:void(0)" class="l-btn js-ok" style="margin-left: 10px;"><span class="l-btn-left"><span class="l-btn-text">确定</span></span></a><a href="javascript:void(0)" class="l-btn js-cancel" style="margin-left: 10px;"><span class="l-btn-left"><span class="l-btn-text">取消</span></span></a></div>';
    $('<div class="' + classStr + '" style="' + styleStr + '"/>').dialog({
        title: title,
        modal: isModal,
        resizable: true,
        collapsible: true,
        maximizable: true,
        content: content,
        onClose: function () {
            if (!isClose) {
                $(this).dialog('destroy');
            }
        }
    });
    //解决IE6、IE7下，弹出框滚动条无效的bug,因被easyui重置样式，故直接用js设置
    //if(IE6 || IE7){
    $('.dialog-content').css('left', '0');
    $('.dialog-content').css('top', '0');
    //}
    $('.' + classStr + ' .js-cancel').unbind('click').bind('click', function () {
        var node = $('.' + classStr);
        if (!isClose) {
            node.dialog('destroy');
        } else {
            node.dialog('close');
        }
    });
};
/**
 *创建弹出对话框
 * @param {Object} title    对话框标题
 * @param {Object} content  对话框内容
 * @param {Object} classStr 对话框的class
 * @param {Object} styleStr 对话框样式字符串
 * @param {Object} isClose  是表示关闭，否表示销毁
 */
Public.createSimpleDialog = function (title, content, classStr, styleStr,
                                      isClose, isModal) {
    isModal = isModal == null ? true : isModal;
    $('<div class="' + classStr + '" style="' + styleStr + '"/>').dialog({
        title: title,
        modal: isModal,
        resizable: true,
        collapsible: true,
        maximizable: true,
        content: content,
        onClose: function () {
            if (!isClose) {
                $(this).dialog('destroy');
            }
        }
    });
    //解决IE6、IE7下，弹出框滚动条无效的bug,因被easyui重置样式，故直接用js设置
    //if(IE6 || IE7){
    $('.dialog-content').css('left', '0');
    $('.dialog-content').css('top', '0');
    //}
};
/**
 * 设置弹出框的位置及宽高
 * @param {Object} percent 页面的百分比，如0.8
 */
Public.getDialogStyle = function (width, height) {
    var d_w = document.body.clientWidth; //页面宽度
    var d_h = document.body.clientHeight;//页面高度
    width = width > d_w ? d_w * 0.9 : width;
    height = height > d_h ? d_h * 0.9 : height;
    var left = parseInt((d_w - width) / 2);
    var top = parseInt((d_h - height) / 2);
    var style = 'left:' + left + 'px;top:' + top + 'px;width:' + width
        + 'px;height:' + height + 'px;';
    return style;
};

/**
 * 去重复的合并2个数组
 * @arr1 数组1
 * @arr2 数组2
 */
Public.mergeArray = function (arr1, arr2) {
    var _arr = [];
    for (var i = 0; i < arr1.length; i++) {
        _arr.push(arr1[i]);
    }
    var _dup;
    for (var i = 0; i < arr2.length; i++) {
        _dup = false;
        for (var _i = 0; _i < arr1.length; _i++) {
            if (arr2[i] === arr1[_i]) {
                _dup = true;
                break;
            }
        }
        if (!_dup) {
            _arr.push(arr2[i]);
        }
    }
    return _arr;
};

//阻止事件冒泡函数
Public.stopPropagation = function (e) {
    if (e && e.stopPropagation) {
        e.stopPropagation();
    } else {
        window.event.cancelBubble = true;
    }
};
/**
 * 计算所给时间hours小时后的时间
 * @param beginTime
 * @param hours
 */
Public.addTime = function (beginTime, hours) {
    beginTime = beginTime.replace(/-/g, "/");
    var date = new Date(beginTime);
    date.setHours(date.getHours() + hours);
    var str = Public.getTimeStr(date);
    return str;
};
/**
 * 格式化时间
 * @param date
 */
Public.getTimeStr = function (date) {
    var y = date.getFullYear();
    var m = date.getMonth() + 1;
    var d = date.getDate();
    var h = date.getHours();
    var mm = date.getMinutes();
    var s = date.getSeconds();
    m = m > 9 ? m : '0' + m;
    d = d > 9 ? d : '0' + d;
    h = h > 9 ? h : '0' + h;
    mm = mm > 9 ? mm : '0' + mm;
    s = s > 9 ? s : '0' + s;
    return y + '-' + m + '-' + d + ' ' + h + ':' + mm + ':' + s;
};

/**
 * 获取当前时间字符串
 */
Public.getNowStr = function () {
    var date = new Date();
    var str = Public.getTimeStr(date);
    return str;
};

/**
 * 打印debug信息
 */
Public.debug = function (msg) {
    //谷歌
    if (console != null) {
        console.log(msg);
    } else {
        //非谷歌
    }
};

/**
 * 显示没有查询结果
 */
Public.showNoResult = function (el, field, colspan) {
    //添加一个新数据行，第一列的值为你需要的提示信息
    var row = {};
    row[field] = '<div style="text-align:center;color:red">没有相关记录！</div>';
    $(el).datagrid('appendRow', row).datagrid('mergeCells', {
        index: 0,
        field: field,
        colspan: colspan
    });
};

/**
 * 判断字符串是否在数组中
 * @str 字符串
 * @array 数组
 * */
Public.isInArray = function (str, array) {
    var result = $.inArray(str, array);
    if (result > -1) {
        return true;
    }
    return false;
};

/**
 * 判断ip是否在一个段
 * @addr1 ip地址1
 * @addr2 ip地址2
 * @mask 子网掩码
 * */
Public.isEqualIPAddress = function (addr1, addr2, mask) {
    if (!addr1 || !addr2 || !mask) {
        return false;
    }
    var res1 = [], res2 = [];
    addr1 = addr1.split(".");
    addr2 = addr2.split(".");
    mask = mask.split(".");
    for (var i = 0, ilen = addr1.length; i < ilen; i += 1) {
        res1.push(parseInt(addr1[i]) & parseInt(mask[i]));
        res2.push(parseInt(addr2[i]) & parseInt(mask[i]));
    }
    if (res1.join(".") == res2.join(".")) {
        return true;
    } else {
        return false;
    }
};

/**
 * 前端分页器
 * @data json数组
 * */
Public.pagerFilter = function (data) {
    if (typeof(data) == 'undefined') {
        data = {total: 0, rows: []};
    }
    var dg = $('.js-monitor-table');
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
 * 获取字符串的长度，包括空格
 * @str
 * */
Public.getStrLength = function (str) {
    //获得字符串实际长度，中文2，英文1
    var realLength = 0, len = str.length, charCode = -1;
    for (var i = 0; i < len; i++) {
        charCode = str.charCodeAt(i);
        if (charCode >= 0 && charCode <= 128) realLength += 1;
        else realLength += 2;
    }
    return realLength;
};

/**
 * 截取字符串(汉字按照2字节算)
 * @str 要截取的字符串
 * @len 要截取的字节数
 * */
Public.cutStr = function (str, len) {
    var temp,
        icount = 0,
        patrn = /[^\x00-\xff]/;
    strre = "";
    for (var i = 0; i < str.length; i++) {
        if (icount <= len - 1) {
            temp = str.substr(i, 1);
            if (patrn.exec(temp) == null) {
                icount = icount + 1
            } else {
                icount = icount + 2
            }
            strre += temp
        } else {
            break;
        }
    }
    return strre;
};

/**
 * 全部替换
 * @s1
 * @s2
 * */
Public.replaceAll = function (s1, s2) {
    return this.replace(new RegExp(s1, "gm"), s2)
}

/**
 * 清除字符串两边的空格
 * @str
 * */
Public.trim = function (str) {
    return str.replace(/(^\s*)|(\s*$)/g, "");
};

/**
 * 清除字符串中的所有空格
 * @str
 * */
Public.trimAll = function (str) {
    return str.replace(/(^\s+)|(\s+$)/g, "").replace(/\s/g, "");
};

/**
 * 清除字符串左边的空格
 * @str
 * */
Public.ltrim = function (str) {
    return str.replace(/^(\s*|　*)/, "");
};

/**
 * 清除字符串右边的空格
 * @str
 * */
Public.rtrim = function (str) {
    return str.replace(/(\s*|　*)$/, "");
};

/**
 * 判断字符串是否以某个字符开头
 * @s
 * */
Public.startWith = function (s) {
    return this.indexOf(s) == 0;
};

/**
 * 判断字符串是否以某个字符结束
 * @s
 * */
Public.endWith = function (s) {
    var d = this.length - s.length;
    return (d >= 0 && this.lastIndexOf(s) == d);
};

/**
 * 转义htmlbiaoqian
 * @text 文本
 * */
Public.htmlEncode = function (text) {
    return text.replace(/&/g, '&').replace(/\"/g, '"').replace(/</g, '<').replace(/>/g, '>');
};

/**
 * 加入收藏夹
 * @sURL
 * @sTitle
 * */
Public.AddFavorite = function (sURL, sTitle) {
    try {
        window.external.addFavorite(sURL, sTitle)
    } catch (e) {
        try {
            window.sidebar.addPanel(sTitle, sURL, "");
        } catch (e) {
            alert("加入收藏失败，请使用Ctrl+D进行添加");
        }
    }
};

/**
 * 获取指定名称的cookie的值
 * @objName
 * */
Public.getCookie = function (objName) {
    var arrStr = document.cookie.split("; ");
    for (var i = 0; i < arrStr.length; i++) {
        //获取单个cookies
        var temp = arrStr[i].split("=");
        if (temp[0] == objName) {
            if (temp.length > 1) {
                return unescape(temp[1]);
            } else {
                return "";
            }
        }
    }
    return "";
};

/**
 * 清除所有的cookie
 * */
Public.clearCookie = function () {
    var keys = document.cookie.match(/[^ =;]+(?=\=)/g);
    if (keys) {
        for (var i = keys.length; i--;)
            document.cookie = keys[i] + '=0;expires=' + new Date(0).toUTCString();
    }
};

/**
 * 是否未obj对象
 * @object
 * */
Public.isObj = function (object) {
    return object && typeof (object) == 'object' && Object.prototype.toString.call(object).toLowerCase() == "[object object]";
};

/**
 * 是否是数组
 * @object
 * */
Public.isArray = function (object) {
    return object && typeof (object) == 'object' && object.constructor == Array;
};

/**
 * 获取对象属性个数
 * @object
 * */
Public.getLength = function (object) {
    var count = 0;
    for (var i in object) count++;
    return count;
};

/**
 * 比较两个对象是否相同
 * @objA
 * @objB
 * */
Public.Compare = function (objA, objB) {
    if (!Public.isObj(objA) || !Public.isObj(objB)) return false; //判断类型是否正确
    if (Public.getLength(objA) != Public.getLength(objB)) return false; //判断长度是否一致
    return Public.CompareObj(objA, objB, true);//默认为true
};

/**
 * 比较两个对象是否相同
 * @objA
 * @objB
 * @flag
 * */
Public.CompareObj = function (objA, objB, flag) {
    for (var key in objA) {
        if (!flag) //跳出整个循环
            break;
        if (!objB.hasOwnProperty(key)) {
            flag = false;
            break;
        }
        if (!Public.isArray(objA[key])) { //子级不是数组时,比较属性值
            if (objB[key] != objA[key]) {
                flag = false;
                break;
            }
        } else {
            if (!Public.isArray(objB[key])) {
                flag = false;
                break;
            }
            var oA = objA[key], oB = objB[key];
            if (oA.length != oB.length) {
                flag = false;
                break;
            }
            for (var k in oA) {
                if (!flag) //这里跳出循环是为了不让递归继续
                    break;
                flag = Public.CompareObj(oA[k], oB[k], flag);
            }
        }
    }
    return flag;
};