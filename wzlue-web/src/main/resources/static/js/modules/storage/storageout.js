$(function () {

    vm.initTable();

    //表单提交
    $("#costForm").FM({
        fields: vm.costFields,
        success: vm.saveOrUpdate
    });

    vm.initGoodTabele();
});

function arrival(id, companyName) {
    confirm(companyName + '确定要出库了吗？', function () {
        $.ajax({
            type: "POST",
            url: baseURL + "storage/storageout/update2",
            contentType: "application/json",
            data: JSON.stringify({id: id}),
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
}

function detail(id, companyName) {
    vm.showList = false;
    vm.title = "详情";
    vm.companyName = companyName;
    vm.orderCost = {};
    vm.feeList = [];
    vm.getInfo(id);
    vm.initDateTime();
    vm.getCompanys();
}

var vm = new Vue({
    el: '#rrapp',
    i18n,
    data: {
        showList: true,
        title: null,
        out: {},
        params: {
            status: '',
        },
        feeList: [],    //其他
        step: 0,    //其他 子项个数
//验证字段
        fields: {
            code: {
                message: '出库单号验证失败',
                validators: {
                    notEmpty: {
                        message: '出库单号不能为空'
                    },
                },
            }, orderNumber: {
                message: '订单号验证失败',
                validators: {
                    notEmpty: {
                        message: '订单号不能为空'
                    },
                },
            }, contact: {
                message: '提货司机姓名验证失败',
                validators: {
                    notEmpty: {
                        message: '提货司机姓名不能为空'
                    },
                },
            }, phone: {
                message: '提货司机电话验证失败',
                validators: {
                    notEmpty: {
                        message: '提货司机电话不能为空'
                    },
                },
            }, card: {
                message: '身份证号验证失败',
                validators: {
                    notEmpty: {
                        message: '身份证号不能为空'
                    },
                },
            }, vehicleNumber: {
                message: '车牌号验证失败',
                validators: {
                    notEmpty: {
                        message: '车牌号不能为空'
                    },
                },
            }, takeTime: {
                message: '预计提货时间验证失败',
                validators: {
                    notEmpty: {
                        message: '预计提货时间不能为空'
                    },
                },
            }, escort: {
                message: '0:押车；1:不押车验证失败',
                validators: {
                    notEmpty: {
                        message: '0:押车；1:不押车不能为空'
                    },
                },
            }, copeCode: {
                message: '0：要抄码；1：不抄码验证失败',
                validators: {
                    notEmpty: {
                        message: '0：要抄码；1：不抄码不能为空'
                    },
                },
            }, remark: {
                message: '备注验证失败',
                validators: {
                    //长度校验
                    stringLength: {
                        max: 200,
                        message: '备注不能超过200个字'
                    },
                },
            },
        },
        costFields: {
            money1: {
                message: '装卸费验证失败',
                validators: {
                    notEmpty: {
                        message: '装卸费不能为空'
                    },
                    //正则校验
                    regexp: {
                        regexp: /(^[1-9](\d{1,8})?(\.\d{1,2})?$)|(^0$)|(^\d\.\d{1,2}$)/,
                        message: '装卸费是整数位小于10位，小数位小于2位数的数字'
                    }
                },
            }, money2: {
                message: '处置费验证失败',
                validators: {
                    notEmpty: {
                        message: '处置费不能为空'
                    },
                    regexp: {
                        regexp: /(^[1-9](\d{1,8})?(\.\d{1,2})?$)|(^0$)|(^\d\.\d{1,2}$)/,
                        message: '处置费是整数位小于10位，小数位小于2位数的数字'
                    }
                },
            }, money3: {
                message: '重复上下架验证失败',
                validators: {
                    notEmpty: {
                        message: '重复上下架不能为空'
                    },
                    regexp: {
                        regexp: /(^[1-9](\d{1,8})?(\.\d{1,2})?$)|(^0$)|(^\d\.\d{1,2}$)/,
                        message: '重复上下架费是整数位小于10位，小数位小于2位数的数字'
                    }
                },
            }, money4: {
                message: '分拣费验证失败',
                validators: {
                    notEmpty: {
                        message: '分拣费不能为空'
                    },
                    regexp: {
                        regexp: /(^[1-9](\d{1,8})?(\.\d{1,2})?$)|(^0$)|(^\d\.\d{1,2}$)/,
                        message: '分拣费是整数位小于10位，小数位小于2位数的数字'
                    }
                },
            }, money5: {
                message: '理货费验证失败',
                validators: {
                    notEmpty: {
                        message: '理货费不能为空'
                    },
                    regexp: {
                        regexp: /(^[1-9](\d{1,8})?(\.\d{1,2})?$)|(^0$)|(^\d\.\d{1,2}$)/,
                        message: '理货费是整数位小于10位，小数位小于2位数的数字'
                    }
                },
            }, money6: {
                message: '抄码费验证失败',
                validators: {
                    /*notEmpty: {
                        message: '抄码费不能为空'
                    },*/
                    regexp: {
                        regexp: /(^[1-9](\d{1,8})?(\.\d{1,2})?$)|(^0$)|(^\d\.\d{1,2}$)/,
                        message: '抄码费是整数位小于10位，小数位小于2位数的数字'
                    }
                },
            }, money7: {
                message: '复冻费验证失败',
                validators: {
                    notEmpty: {
                        message: '复冻费不能为空'
                    },
                    regexp: {
                        regexp: /(^[1-9](\d{1,8})?(\.\d{1,2})?$)|(^0$)|(^\d\.\d{1,2}$)/,
                        message: '复冻费是整数位小于10位，小数位小于2位数的数字'
                    }
                },
            }, money8: {
                message: '查验移箱费验证失败',
                validators: {
                    notEmpty: {
                        message: '查验移箱费不能为空'
                    },
                    regexp: {
                        regexp: /(^[1-9](\d{1,8})?(\.\d{1,2})?$)|(^0$)|(^\d\.\d{1,2}$)/,
                        message: '查验移箱费是整数位小于10位，小数位小于2位数的数字'
                    }
                },
            }, money9: {
                message: '查验掏箱费验证失败',
                validators: {
                    notEmpty: {
                        message: '查验掏箱费不能为空'
                    },
                    regexp: {
                        regexp: /(^[1-9](\d{1,8})?(\.\d{1,2})?$)|(^0$)|(^\d\.\d{1,2}$)/,
                        message: '查验掏箱费是整数位小于10位，小数位小于2位数的数字'
                    }
                },
            }, money10: {
                message: '查验开关箱门费及铅封验证失败',
                validators: {
                    notEmpty: {
                        message: '查验开关箱门费及铅封不能为空'
                    },
                    regexp: {
                        regexp: /(^[1-9](\d{1,8})?(\.\d{1,2})?$)|(^0$)|(^\d\.\d{1,2}$)/,
                        message: '查验开关箱门费及铅封费是整数位小于10位，小数位小于2位数的数字'
                    }
                },
            }, money11: {
                message: '门到门查验费验证失败',
                validators: {
                    notEmpty: {
                        message: '门到门查验费不能为空'
                    },
                    regexp: {
                        regexp: /(^[1-9](\d{1,8})?(\.\d{1,2})?$)|(^0$)|(^\d\.\d{1,2}$)/,
                        message: '门到门查验费是整数位小于10位，小数位小于2位数的数字'
                    }
                },
            }, money12: {
                message: '出入库费验证失败',
                validators: {
                    notEmpty: {
                        message: '出入库费不能为空'
                    },
                    regexp: {
                        regexp: /(^[1-9](\d{1,8)?(\.\d{1,2})?$)|(^0$)|(^\d\.\d{1,2}$)/,
                        message: '出入库费是整数位小于10位，小数位小于2位数的数字'
                    }
                },
            }, money13: {
                message: '缠绕膜验证失败',
                validators: {
                    notEmpty: {
                        message: '缠绕膜不能为空'
                    },
                    regexp: {
                        regexp: /(^[1-9](\d{1,8})?(\.\d{1,2})?$)|(^0$)|(^\d\.\d{1,2}$)/,
                        message: '缠绕膜费是整数位小于10位，小数位小于2位数的数字'
                    }
                },
            }, money14: {
                message: '过车费验证失败',
                validators: {
                    notEmpty: {
                        message: '过车费不能为空'
                    },
                    regexp: {
                        regexp: /(^[1-9](\d{1,8})?(\.\d{1,2})?$)|(^0$)|(^\d\.\d{1,2}$)/,
                        message: '过车费是整数位小于10位，小数位小于2位数的数字'
                    }
                },
            }, money15: {
                message: '预冷费验证失败',
                validators: {
                    notEmpty: {
                        message: '预冷费不能为空'
                    },
                    regexp: {
                        regexp: /(^[1-9](\d{1,8})?(\.\d{1,2})?$)|(^0$)|(^\d\.\d{1,2}$)/,
                        message: '预冷费是整数位小于10位，小数位小于2位数的数字'
                    }
                },
            }

        },
        companyList: [],
        tableList: [],
        commodityEntityList: [],
        orderCost: {},
        opt: {},
        companyName: '',
    },
    methods: {

        initTable: function () {

            //列表
            $("#table").bootstrapTable("destroy");     //**********对于查询必不可少

            $("#table").BT({
                url: baseURL + 'storage/storageout/list2',
                columns: [
                    {checkbox: true},
                    {title: vm.$t("OutOfWarehouseSingleNumber"), field: 'code'},
                    {title: vm.$t("NameOfBuyerCompany"), field: 'merchantName'},
                    // {title: vm.$t("WarehouseCompany"), field: 'companyName'},
                    {title: vm.$t("NameOfPickUpDriver"), field: 'contact'},
                    {title: vm.$t("PickUpDriversCall"), field: 'phone'},
                    {title: vm.$t("PrePickUpTime"), field: 'takeTime'},
                    {
                        title: vm.$t("WhetherOrNotToEscortTheCar"),
                        field: 'escort',
                        formatter: function (value, row, index) {
                            var ss = '';
                            if (row.escort == 0) {
                                ss += '是';
                            } else if (row.escort == 1) {
                                ss += '否';
                            }
                            return ss;
                        }
                    },
                    {
                        title: vm.$t("WhetherOrNotToCopyTheCode"),
                        field: 'copeCode',
                        formatter: function (value, row, index) {
                            var ss = '';
                            if (row.copeCode == 0) {
                                ss += '是';
                            } else if (row.copeCode == 1) {
                                ss += '否';
                            }
                            return ss;
                        }
                    },
                    /*{title: '是否发送邮件', field: 'email',formatter:function(value,row,index){
                     var ss='';
                     if(row.email==0){
                     ss+='发送邮件';
                     }else if(row.email==1){
                     ss+='不发送邮件';
                     }
                     return ss;
                     }},*/
                    {
                        title: vm.$t("status"), field: 'status', formatter: function (value, row, index) {
                            var ss = '';
                            if (row.status == 0) {
                                ss += '待出库';
                            } else if (row.status == 1) {
                                ss += '已出库';
                            }
                            return ss;
                        }
                    },
                    {
                        title: vm.$t("chaozuo"), formatter: function (value, row, index) {
                            var ss = '';
                            /*if(row.status==0){
                                ss+='<button class="btn btn-primary btn-sm" onclick="arrival('+row.id+',\''+row.companyName+'\')">确认出库</button>&nbsp;';
                            }*/
                            ss += '<button class="btn btn-primary btn-sm" onclick="detail(' + row.id + ',\'' + row.companyName + '\')">详情</button>&nbsp;';
                            return ss;
                        }
                    }
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
            vm.out = {};
            vm.initDateTime();
            vm.getCompanys();
        },
        getCompanys: function () {
            $.get(baseURL + "company/merchantusers/getCompanys", function (r) {
                var str = JSON.stringify(r.list);
                str = str.replace(/id/g, "value").replace(/companyName/g, "label");
                vm.companyList = JSON.parse(str);
            });
        },
        //显示选择货物列表modal
        showMModal: function () {
            if (vm.out.companyId == null || vm.out.companyId == '') {
                alert('请先选择出库仓库公司');
                return;
            }
            vm.tableList = [];
            if (vm.out.companyId != '' && vm.out.companyId != null) {
                $.get(baseURL + "storage/ordercommodity/getOutCommoditys", {companyId: vm.out.companyId}, function (r) {
                    vm.tableList = r.list;
                    $("#addGoodTable").bootstrapTable('removeAll');
                    $("#addGoodTable").bootstrapTable('append', vm.tableList);
                    $('#myModal').modal('show');
                });
            }
        },
        removeGoods: function (index) {
            vm.commodityEntityList.splice(index, 1);
            vm.tableList.remove(vm.commodityEntityList);
        },
        initGoodTabele: function () {
            vm.opt = {
                pagination: false,
                columns: [
                    {checkbox: true},
                    {title: '集装箱号', field: 'containerNo'},
                    {title: '报检号', field: 'inspectionNo'},
                    {title: '订单号', field: 'orderNumber'},
                    {title: '商品名称', field: 'commodityName'},
                    {title: '商品价格', field: 'commodityPrice'},
                    {title: '商品单位', field: 'commodityUnit'},
                    {title: '产地', field: 'commodityCountry'},
                    {title: '生产日期', field: 'productionDate'},
                    {title: '有效期截止日期', field: 'expirationDate'},
                    {title: '剩余重量', field: 'weight'},
                    {title: '单位', field: 'unitName'}

                ]
            }
            $("#addGoodTable").BT2(vm.opt);
        },
        //添加货物事件
        addGood: function () {
            $('#myModal').modal('hide');
            var grid = $('#addGoodTable').bootstrapTable('getSelections');
            if (!grid.length) {
                alert("请选择一条记录");
                return;
            }
            if (vm.commodityEntityList == null) {
                vm.commodityEntityList = [];
            }
            grid.forEach(function (item) {
                if (vm.commodityEntityList.length == 0) {
                    vm.commodityEntityList.push(item);
                } else {
                    for (let obj of vm.commodityEntityList) {
                        if (obj.orderCommodityId != item.orderCommodityId) {
                            vm.commodityEntityList.push(item);
                        }
                    }
                }
            });
        },
        update: function (event) {
            var id = getSelectedRowId("id");
            if (id == null) {
                return;
            }
            vm.showList = false;
            vm.title = "修改";
            vm.initDateTime();
            vm.getCompanys();
            vm.getInfo(id)
        },
        //表单验证
        validate: function () {
            var bl = $('#costForm').VF();//启用验证
            if (!bl) {
                return;
            }
        },
        saveOrUpdate: function (event) {
            if (vm.commodityEntityList == null || vm.commodityEntityList.length == 0) {
                alert("请添加货物信息");
                return;
            }
            for (let obj of vm.commodityEntityList) {
                if (!/(^[1-9](\d{1,8})?(\.\d{1,2})?$)|(^0$)|(^\d\.\d{1,2}$)/.test(obj.outWeight)) {
                    alert("出库重量整数最多9位，小数最多2位");
                    return;
                }
                if (obj.outWeight > obj.weight) {
                    alert("出库重量不能超过库存重量");
                    return;
                }
                // if (obj.commodityUnit == 1 && obj.outUnit == 0) {   //吨计量
                //     obj.weightNum = obj.weight;
                // }
                // if (!/(^[1-9](\d{1,10})?(\.\d{1,2})?$)|(^0$)|(^\d\.\d{1,2}$)/.test(obj.outWeightNum)) {
                //     alert("出库数量整数最多11位，小数最多2位");
                //     return;
                // }
                // if (obj.outWeightNum > obj.weightNum) {
                //     alert("出库数量不能超过库存数量");
                //     return;
                // }
                // if (!(obj.commodityUnit == 1 && obj.outUnit == 0) && obj.outWeightNum == obj.weightNum && obj.outWeight != obj.weight) {
                //     alert("托盘数量全部出库时重量也应该全部出库");
                //     return;
                // }

            }
            if (vm.feeList != null && vm.feeList.length > 0) {
                for (let obj of vm.feeList) {
                    if (isBlank(obj.name) || obj.name.length > 10) {
                        alert("其他费用的费用名称不能为空且长度要小于10");
                        return;
                    }
                    if (!/(^[1-9](\d{1,8})?(\.\d{1,2})?$)|(^0$)|(^\d\.\d{1,2}$)/.test(obj.fee)) {
                        alert("其他费用子项的整数最多9位，小数最多2位");
                        return;
                    }
                }
                vm.orderCost.money16 = JSON.stringify(vm.feeList);
            }

            if (isBlank(vm.orderCost.money6)) {
                vm.orderCost.money6 = 0;
            }
            vm.out.commodityEntityList = vm.commodityEntityList;
            vm.out.orderCost = vm.orderCost;
            $.ajax({
                type: "POST",
                url: baseURL + "storage/storageout/update2",
                contentType: "application/json",
                data: JSON.stringify(vm.out),
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
                    url: baseURL + "storage/storageout/delete",
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
            $.get(baseURL + "storage/storageout/info/" + id, function (r) {
                vm.out = r.storageOut;
                vm.commodityEntityList = r.commodityEntityList;
                if (r.orderCost != null) {
                    vm.orderCost = r.orderCost;
                    if (r.orderCost.feeList != null) {
                        vm.feeList = r.orderCost.feeList;
                    }
                }else {        //vm.out.status == 0 待出库
                    vm.orderCost = {
                        money1: 0,
                        money2: 0,
                        money3: 0,
                        money4: 0,
                        money5: 0,
                        money6: 0,
                        money7: 0,
                        money9: 0,
                        money13: 0,
                        money14: 0,
                        money15: 0
                    }
                    if (vm.out.copeCode == 1) { //不抄码
                        // vm.orderCost.money6 = 0;    //抄码费=0
                        $('#money6').attr('disabled', 'disabled');
                    } else {
                        $('#money6').removeAttr('disabled');
                    }

                }

            });
        },
        reload: function (event) {
            vm.showList = true;
            vm.title = "";
            vm.out = {};
            vm.tableList = [];
            vm.commodityEntityList = [];
            //刷新 如需条件查询common.js
            $("#table").BTF5(vm.params);
            $("form").RF();
        },
        initDateTime: function () {
            $('#datetimepicker').datetimepicker('remove');
            var language;
            if (vm.$i18n.locale == 'en') {
                language = 'en'
            } else {
                language = 'zh-CN'
            }
            $.fn.datetimepicker.dates['en'] = {
                days: ["Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"],
                daysShort: ["Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"],
                daysMin: ["Su", "Mo", "Tu", "We", "Th", "Fr", "Sa", "Su"],
                months: ["January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"],
                monthsShort: ["Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"],
                today: "Today",
                meridiem: ["AM", "PM"]
            };
            $("#datetimepicker").datetimepicker({
                startDate: new Date(),
                language: language,
                autoclose: true,
                startView: 2,
                minView: 2,
                todayBtn: true,
                todayHighlight: true,
                forceParse: true,

            }).on('hide', function (ev) {
                var value = $("#datetimepicker").val();
                vm.out.takeTime = value;
                $("form").data('bootstrapValidator')
                    .updateStatus('takeTime', 'NOT_VALIDATED', null)
                    .validateField('takeTime');

            });
        },
        addFee: function () {
            if (vm.step == 5) {
                alert("最多添加5个子项");
                return;
            }
            vm.feeList.push({
                name: '',
                fee: 0
            });
            vm.step = vm.step + 1;
        },
        remove: function (index) {
            vm.feeList.splice(index, 1);
            vm.step = vm.step - 1;
        }
    },
    created: function () {

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
    },
});


$.fn.BT2 = function (obj) {
    return $(this).bootstrapTable({
        url: obj.url,                           //请求后台的URL（*）
        data: obj.data || [],
        method: obj.method ? obj.method : 'get',//请求方式（*）
        // toolbar: '#toolbar',                    //工具按钮用哪个容器
        striped: true,                          //是否显示行间隔色
        cache: obj.cache ? obj.cache : true,    //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
        sortable: false,                         //是否启用排序
        sortOrder: obj.sortOrder ? obj.sortOrder : "desc",//排序方式
        sidePagination: 'server',               //分页方式：client客户端分页，server服务端分页（*）
        queryParams: function (params) {
            var temp = {   //这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
            };
            console.log(obj.queryParams);
            //对象合并
            temp = Object.assign(temp, (obj.queryParams || {}));
            console.log(temp);
            return temp;
        },
        search: false,                      //是否显示表格搜索，此搜索是客户端搜索
        showColumns: false,                      //是否显示所有的列
        showRefresh: false,                      //是否显示刷新按钮
        clickToSelect: true,                    //是否启用点击选中行
        height: obj.height ? obj.height : 650,  //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
        uniqueId: obj.uniqueId ? obj.uniqueId : "ID",                         //每一行的唯一标识，一般为主键列
        showToggle: false,                       //是否显示详细视图和列表视图的切换按钮
        cardView: false,                        //是否显示详细视图
        showExport: false,                       //是否显示导出
        onLoadSuccess: obj.onLoadSuccess,         //数据加载完成以后事件
        onClickRow: obj.onClickRow,                  //单击row事件
        onEditableSave: obj.onEditableSave,//保存可编辑单元格时触发，参数field, row, oldValue, $el
        columns: function () {                  //列表数据
            //获取列名
            var array = obj.columns;
            return array;
        }
        (),
    });
}

