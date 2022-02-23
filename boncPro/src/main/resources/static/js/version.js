var modelVersionTableModal = {
    // 初始化模型版本列表页面
    initPage: function (versionType,key) {
    	var obj = {}
    	obj['key'] = key;
    	obj['versionType'] = versionType;
        initVersionTable(obj); // 根据模型名称初始化列表
        modelVersionTableModal.show();
    },
    // 展开弹框
    show: function () {
        $('#modelVersionTableAlertModal').modal({'show': 'center', "backdrop": "static"});
    },
    // 关闭弹框
    hidden: function () {
        $('#modelVersionTableAlertModal').modal('hide');
    }
}

/**
 * 初始化 版本列表
 */
function initVersionTable(obj) {
    $('#modelVersionTable').width('100%').dataTable({
        "searching": false,
        "ordering": false,
        "destroy": true,
        "bLengthChange": false,
        "pagingType": "full_numbers",
        "paging": true,
        "info": true,
        "serverSide": false,
        "pageLength": 2,
        "columns": [
            {"title": "版本号", "data": "versionId"},
            {"title": "版本描述", "data": "versionDesc"},
            {"title": "创建人", "data": "createUser"},
            {"title": "创建时间", "data": "createDate"}
            ],
        ajax: {
            url: '/bonc/version',
            "type": 'GET',
            "data": function (d) { // 查询参数
                return $.extend({}, d, obj);
            }
        },
        "fnDrawCallback": function (oSettings, json) {
        }
    });
}