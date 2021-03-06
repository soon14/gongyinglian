$(function () {

    //表单提交
    $("#addCardForm").FM({fields: vm.cardfields, success: vm.saveCard});

    $('#myModal').on('hide.bs.modal', function () {
        $("#addCardForm").RF();
        vm.bankCard = {};
    })
});

var vm = new Vue({
    el: '#rrapp',
    i18n,
    data: {
        showList: 4,
        title: '银行卡管理',
        add: false,
        detail: false,
        myAccount: {},
        cardList: [],
        bankCard: {},
        transfer: {
            type: 0
        },
        params: {
            name: '',
        },
        //验证字段
        cardfields: {
            name: {
                message: '姓名验证失败',
                validators: {
                    notEmpty: {
                        message: '姓名不能为空'
                    }, stringLength: {
                        max: 20,
                        message: '长度小于20'
                    },
                },
            },
            idcard: {
                message: '身份证验证失败', validators: {
                    notEmpty: {message: '身份证不能为空'},
                    regexp: {
                        regexp: /^(^[1-9]\d{7}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])\d{3}$)|(^[1-9]\d{5}[1-9]\d{3}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])((\d{4})|\d{3}[Xx])$)$/,
                        message: '身份证号码格式不正确，为15位和18位身份证号码！'
                    }
                },
            },
            bankCardNumber: {
                message: '银行卡号验证失败',
                validators: {
                    notEmpty: {message: '银行卡号不能为空'},
                    stringLength: {//检测长度
                        min: 16,
                        max: 19,
                        message: '长度必须在16-19之间'
                    },
                    regexp: {
                        regexp: /([\d]{4})([\d]{4})([\d]{4})([\d]{4})([\d]{0,})?/,
                        message: '银行卡号不正确'
                    }
                },
            },
            phone: {
                message: '手机号码验证失败',
                validators: {
                    notEmpty: {message: '手机号码不能为空'},
                    regexp: {regexp: /^1[3456789]\d{9}$/, message: '手机号码不正确'}
                },
            },
            defaultCard: {message: '默认卡片  1:默认验证失败', validators: {notEmpty: {message: '默认卡片  1:默认不能为空'},},},
        }
    },
    methods: {
        getCardList: function () {
            $.get(baseURL + "bill/bankcard/list/", function (r) {
                if (r.code == '0')
                    vm.cardList = r.data;
                else
                    alert(r.msg);
            });
        },
        addCard: function () {
            if (vm.cardList.length === 8 || vm.cardList.length > 8) {
                alert("已达银行卡上限8张");
                return;
            }
            vm.bankCard = {};
            vm.add = true;
            vm.detail = false;
            $('#myModal').modal('show');
        },
        //表单验证
        cardvalidate: function () {
            var bl = $('#addCardForm').VF();//启用验证
            if (!bl) {
                return;
            }
        },
        saveCard: function () {
            var url = vm.bankCard.id == null ? "bill/bankcard/save" : "bill/bankcard/update";
            $.ajax({
                type: "POST",
                url: baseURL + url,
                contentType: "application/json",
                data: JSON.stringify(vm.bankCard),
                success: function (r) {
                    if (r.code === 0) {
                        alert('操作成功', function (index) {
                            $('#myModal').modal('hide');
                            vm.reload();
                        });
                    } else {
                        alert(r.msg);
                    }
                }
            });
        },
        delCard: function (id) {
            // confirm('确定要删除选中的记录？', function () {
            alert('确定要删除选中的银行卡？', function () {
                $.ajax({
                    type: "POST",
                    url: baseURL + "bill/bankcard/delete",
                    contentType: "application/json",
                    data: JSON.stringify([id]),
                    success: function (r) {
                        if (r.code == 0) {
                            // alert('操作成功', function (index) {
                                vm.reload();
                            // });
                        } else {
                            alert(r.msg);
                        }
                    }
                });
            });
        },
        cardDetail: function (id) {
            vm.add = false;
            vm.detail = true;
            vm.getInfo(id);
            $('#myModal').modal('show');
        },
        getInfo: function (id) {
            $.ajaxSettings.async = false;
            $.get(baseURL + "bill/bankcard/info/" + id, function (r) {
                vm.bankCard = r.bankCard;
            });
            $.ajaxSettings.async = true;
        },
        showDiv: function (idx) {

            vm.title = '我的银行卡';
            vm.getCardList();

            vm.showList = idx;
        },
        query: function () {
            vm.reload();
        },
        add: function () {
            vm.showList = false;
            vm.title = "新增";
            vm.myAccount = {};
        },
        update: function (event) {
            var id = getSelectedRowId("id");
            if (id == null) {
                return;
            }
            vm.showList = 1;
            vm.title = "修改";

            vm.getInfo(id)
        },

        saveOrUpdate: function (event) {
            var url = vm.myAccount.id == null ? "bill/myaccount/save" : "bill/myaccount/update";
            $.ajax({
                type: "POST",
                url: baseURL + url,
                contentType: "application/json",
                data: JSON.stringify(vm.myAccount),
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
                    url: baseURL + "bill/myaccount/delete",
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
        reload: function (event) {
            vm.showList = 4;
            vm.title = "银行卡管理";
            //刷新 如需条件查询common.js
            // $("#table").BTF5(vm.params);
            $("#addCardForm").RF();
            $('#myModal').modal('hide');
            vm.getCardList();
        }
    },
    created: function () {
        this.getCardList();
    },
    watch: {
        /*"transfer.type": function (newValue, oldValue) {

        }*/

        "$i18n.locale": function (value) {
            if (value == 'en') {
                $("#table").bootstrapTable.defaults.locale = "en-US";
            } else {
                $("#table").bootstrapTable.defaults.locale = "zh-CN";
            }
            $("#table").bootstrapTable("destroy");
            this.initTable();
        }
    }
});