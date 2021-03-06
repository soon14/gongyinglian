$(function () {
    vm.initTable();
    //表单提交
    $("form").FM({
        fields: vm.fields,
        success: vm.saveOrUpdate

    });

});

function opFormatter(value, row, index) {
    var op = '';
    op += '<a id="btn_detail">查看详情</a>';
    return op;
}

window.opEvent = {
    'click #btn_detail': function (event, value, row, index) {
        vm.showList = false;
        vm.getInfo(row.id);
    }
};

var vm = new Vue({
    el: '#rrapp',
    i18n,
    data: {
        showList: true,
        title: null,
        complaint: {},
        images: [],
        params: {
            status: '',
        },
        statusList: ['待处理', '处理中', '完结'],
        resultList: ['通过', '不通过'],
//验证字段
        fields: {
            complaintFrom: {
                message: '投诉用户验证失败',
                validators: {
                    notEmpty: {
                        message: '投诉用户不能为空'
                    },
                },
            }, complaintTo: {
                message: '被投诉用户验证失败',
                validators: {
                    notEmpty: {
                        message: '被投诉用户不能为空'
                    },
                },
            }, orderId: {
                message: '订单号验证失败',
                validators: {
                    notEmpty: {
                        message: '订单号不能为空'
                    },
                },
            }, complaintTime: {
                message: '投诉时间验证失败',
                validators: {
                    notEmpty: {
                        message: '投诉时间不能为空'
                    },
                },
            }, reason: {
                message: '投诉原因验证失败',
                validators: {
                    notEmpty: {
                        message: '投诉原因不能为空'
                    },
                },
            }, status: {
                message: '处理状态 0 : 待处理 1 : 处理中 2 : 完结验证失败',
                validators: {
                    notEmpty: {
                        message: '处理状态 0 : 待处理 1 : 处理中 2 : 完结不能为空'
                    },
                },
            }, result: {
                message: '处理结果 0 : 通过 1 : 不通过验证失败',
                validators: {
                    notEmpty: {
                        message: '处理结果 0 : 通过 1 : 不通过不能为空'
                    },
                },
            }, suggestion: {
                message: '处理意见验证失败',
                validators: {
                    notEmpty: {
                        message: '处理意见不能为空'
                    },
                },
            }, deptId: {
                message: '权限部门id验证失败',
                validators: {
                    notEmpty: {
                        message: '权限部门id不能为空'
                    },
                },
            }, createdBy: {
                message: '创建人验证失败',
                validators: {
                    notEmpty: {
                        message: '创建人不能为空'
                    },
                },
            }, createdTime: {
                message: '创建时间验证失败',
                validators: {
                    notEmpty: {
                        message: '创建时间不能为空'
                    },
                },
            }, updateBy: {
                message: '修改人验证失败',
                validators: {
                    notEmpty: {
                        message: '修改人不能为空'
                    },
                },
            }, updateTime: {
                message: '修改时间验证失败',
                validators: {
                    notEmpty: {
                        message: '修改时间不能为空'
                    },
                },
            }
        },
    },
    methods: {
        initTable: function () {
            //列表
            $("#table").bootstrapTable("destroy");      //**********对于查询必不可少
            $("#table").BT({
                url: baseURL + 'complaint/complaint/list',
                columns: [
                    {checkbox: true},
                    {title: vm.$t("ComplaintFrom"), field: 'complaintFrom'},
                    {title: vm.$t("ComplaintTo"), field: 'complaintTo'},
                    {title: vm.$t("OrderId"), field: 'orderId'},
                    {title: vm.$t("ComplaintTime"), field: 'complaintTime'},
                    {title: vm.$t("ComplaintReason"), field: 'reason'},
                    {
                        title: vm.$t("status"), field: 'status',
                        formatter: function (value) {
                            if (value == 0) {
                                return "待处理"
                            } else if (value == 1) {
                                return "处理中"
                            } else if (value == 2) {
                                return "完结"
                            }
                        }
                    },
                    // {title: '处理结果', field: 'result'},
                    // {title: '处理意见', field: 'suggestion'},
                    {title: vm.$t('chaozuo'), formatter: opFormatter, events: opEvent}
                ],
                //条件查询
                queryParams: vm.params
            });
        },


        query: function () {
            vm.reload();
        },
        add: function () {
            vm.showList = false;
            vm.title = "新增";
            vm.complaint = {};
            vm.images.length = 0;
            vm.$refs.file.destroy();
            vm.$refs.file.initFileInput();
        },
        update: function (event) {
            var id = getSelectedRowId("id");
            if (id == null) {
                return;
            }
            vm.showList = false;
            vm.title = "修改";

            vm.getInfo(id)
        },
        //表单验证
        validate: function () {
            var bl = $('form').VF();//启用验证
            if (!bl) {
                return;
            }
        },
        saveOrUpdate: function (event) {
            if (vm.images.length == 0 && (vm.complaint.images == null || vm.complaint.images == 0)) {
                alert("请上传图片");
                return;
            }
            if (vm.complaint.images == null) {
                vm.complaint.images = [];
            }
            ;
            vm.complaint.images = vm.complaint.images.concat(vm.images)

            var url = vm.complaint.id == null ? "complaint/complaint/save" : "complaint/complaint/update";
            $.ajax({
                type: "POST",
                url: baseURL + url,
                contentType: "application/json",
                data: JSON.stringify(vm.complaint),
                success: function (r) {
                    if (r.code === 0) {
                        alert('操作成功', function (index) {
                            vm.reload();
                        });
                    } else {
                        alert(r.msg);
                    }
                }
            });
        },
        del: function (event) {
            var ids = getSelectedRowsId("id");
            if (ids == null) {
                return;
            }

            confirm('确定要删除选中的记录？', function () {
                $.ajax({
                    type: "POST",
                    url: baseURL + "complaint/complaint/delete",
                    contentType: "application/json",
                    data: JSON.stringify(ids),
                    success: function (r) {
                        if (r.code == 0) {
                            alert('操作成功', function (index) {
                                vm.reload();
                            });
                        } else {
                            alert(r.msg);
                        }
                    }
                });
            });
        },
        getInfo: function (id) {
            vm.complaint = {};
            vm.images.length = 0;
            $.get(baseURL + "complaint/complaint/info/" + id, function (r) {
                vm.complaint = r.complaint;
                var images = vm.complaint.images;

            });
        },
        reload: function (event) {
            vm.showList = true;
            vm.title = "";
            vm.complaint = {};
            vm.images.length = 0;
            //刷新 如需条件查询common.js
            // $("#table").BTF5(vm.params);
            //解决table,分页问题（当点击完新增以后，第二页的内容和第一页的内容一样）
            vm.initTable();
            $("form").RF();
        },
        handle: function (event) {
            if (vm.complaint.result == null) {
                alert("处理结果不能为空");
            } else if (isBlank(vm.complaint.suggestion)) {
                alert("处理意见不能为空")
            } else if (vm.complaint.suggestion.length >= 200) {
                alert("处理意见长度要小于200个字符")
            } else {
                $.ajax({
                    type: "POST",
                    url: baseURL + "complaint/complaint/handle",
                    contentType: "application/json",
                    data: JSON.stringify(vm.complaint),
                    success: function (r) {
                        if (r.code == 0) {
                            alert('操作成功', function (index) {
                                vm.reload();
                            });
                        } else {
                            alert(r.msg);
                        }
                    }
                });
            }
        }
    },
    watch: {
        "$i18n.locale": function (value) {
            if (value == 'en') {
                $("#table").bootstrapTable.defaults.locale = "en-US";
            } else {
                $("#table").bootstrapTable.defaults.locale = "zh-CN";
            }
            $("#table").bootstrapTable("destroy");
            this.initTable();
        },
    }
});