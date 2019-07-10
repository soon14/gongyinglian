$(function () {
    vm.initTable();
    //表单提交
    $("form").FM({
        fields: vm.fields,
        success: vm.saveOrUpdate

    })

});

//判断是否禁用
function change2(th) {
    if (th.value == '1') {
        $('#commodityCode').attr("disabled", "disabled");
    } else {
        $('#commodityCode').removeAttr("disabled");
    }
}

//判断物流编码是否重复
function checkCode(value) {
    if (value == '' || value == null) {
        return;
    }
    $.ajax({
        url: baseURL + "logistics/logisticsoffer/checkCode",
        data: JSON.stringify({commodityCode: value}),
        type: "POST",
        contentType: "application/JSON",
        success: function (r) {
            if (r.code == 0) {
                if (r.commodityCode != null) {
                    alert("商品编码已存在！");
                    vm.logisticsoffer.commodityCode = '';
                }
            } else {
                alert(r.msg);
            }
        }
    })
}

//运输线路
function typeFmt(value, rows, index) {
    var pyy = rows.type;
    if (pyy == '0') {
        return '国内';
    } else if (pyy == '1') {
        return '国际';
    }
}

//单位
function unitFmt(value, rows, index) {
    var pyy = rows.unit;
    if (pyy == '0') {
        return '元/m³';
    } else if (pyy == '1') {
        return '美元/m³';
    } else if (pyy == '2') {
        return '元/柜';
    } else if (pyy == '3') {
        return '元/车';
    } else if (pyy == '4') {
        return '美元/票';
    }
}

//运输分类
function categoryIdFmt(value, rows, index) {
    var py = rows.categoryId;
    if (py == '0') {
        return '船舶';
    } else if (py == '1') {
        return '车辆';
    } else if (py == '2') {
        return '航空';
    } else if (py == '3') {
        return '火车';
    }
}

//状态
function statusFmt(value, rows, index) {
    var py1 = rows.status;
    if (py1 == '0') {
        return '上架';
    } else if (py1 == '1') {
        return '下架';
    }
}

function operFormatter(value, row, index) {
    var op = '';
    op += '<a id="btn_detail">查看详情</a>';
    return op;
}

window.operateEvents = {
    'click #btn_detail': function (event, value, row, index) {
        vm.showList = false;
        vm.backShow = false;
        vm.showBtn = false;
        vm.getInfo(row.id);
    }
};

var vm = new Vue({
    el: '#rrapp',
    i18n,
    data: {
        logisticsOfferAddressList: [],
        logisticsOfferAddressList1: [],
        showList: true,
        title: null,
        logisticsOffer: {},
        disenbled: false,
        params: {
            categoryId: '',
            title: '',
            status: ''
        },
        categoryOptions: [
            {value: 0, label: '船舶'},
            {value: 1, label: '车辆'},
            {value: 2, label: '航空'},
            {value: 3, label: '火车'}
        ],
        ue: UE.getEditor('myEditor', {
            initialFrameHeight: 440,
        }),
        provinces: [],
        cities: [],
        cities1: [],
        showBtn: true,
        logisticsOfferAddressList: [],
        backShow: true,
        //验证字段
        fields: {
            title: {
                message: '标题验证失败',
                validators: {
                    notEmpty: {
                        message: '标题不能为空'
                    },
                    stringLength: {
                        min: 1,
                        max: 20,
                        message: '标题长度必须在1到20之间'
                    },
                    // regexp: {
                    //     regexp: /[\u4e00-\u9fa5]/,
                    //     message: '标题名称必须为字符串'
                    // }
                },
            },
            phone: {
                message: '联系方式验证失败',
                validators: {
                    notEmpty: {
                        message: '联系方式不能为空'
                    },
                    regexp: {
                        regexp: /^1[3456789]\d{9}$/,
                        message: '请正确输入手机号码'
                    }
                },
            },
            contact: {
                message: '联系人验证失败',
                validators: {
                    notEmpty: {
                        message: '联系人不能为空'
                    },
                    stringLength: {
                        max: 20,
                        message: '联系人长度小于20个字符'
                    }
                    // regexp: {
                    //     regexp: /[\u4e00-\u9fa5]/,
                    //     message: '联系人必须为字符串'
                    // }
                },
            },
            price: {
                message: '报价验证失败',
                validators: {
                    notEmpty: {
                        message: '报价不能为空'
                    },
                    regexp: {
                        //|(^0$)
                        regexp: /(^[1-9](\d{1,8})?(\.\d{1,2})?$)|(^\d\.\d{1,2}$)/,
                        message: '报价为整数最多9位，小数最多2位的正数'
                    }
                //    /^[0-9]{1,10}([.][0-9]{1,2})?$/
                },
            },
            categoryId: {
                message: '运输分类',
                validators: {
                    notEmpty: {
                        message: '请选择运输分类'
                    },
                },
            },
            type: {
                message: '物流类型',
                validators: {
                    notEmpty: {
                        message: '请选择物流类型'
                    },
                },
            },
            businessIntroduction: {
                message: '业务介绍验证失败',
                validators: {
                    notEmpty: {
                        message: '业务介绍不能为空'
                    },
                },
            },
            commodityCode: {
                message: '商品编码验证失败',
                validators: {
                    notEmpty: {
                        message: '商品编码不能为空'
                    },
                    regexp: {
                        regexp: /^[^\u4e00-\u9fa5]+$/,
                        message: '商品编码不予许出现中文'
                    }
                },
            },
            status: {
                message: '状态 0:在售,1:下架验证失败',
                validators: {
                    notEmpty: {
                        message: '状态不能为空'
                    },
                },
            },
            num: {
                message: '成交数量验证失败',
                validators: {
                    notEmpty: {
                        message: '数量不能为空'
                    },
                    regexp: {
                        regexp: /^[1-9]\d*$/,
                        message: '请正确输入数量'
                    }, stringLength: {
                        min: 1, max: 11, message: '输入小于11位的整数'
                    }
                },
            },
            unit: {
                message: '单位验证失败',
                validators: {
                    notEmpty: {
                        message: '单位不能为空'
                    },
                },
            }
        }
    },
    methods: {
        initTable: function () {
            //列表
            $("#table").BT({
                url: baseURL + 'logistics/logisticsoffer/list',
                columns: [
                    {checkbox: true},
                    {title: vm.$t('categoryId'), field: 'categoryId', align: 'center', formatter: categoryIdFmt},
                    {title: vm.$t('offerCode'), field: 'commodityCode', align: 'center'},
                    {title: vm.$t('title'), field: 'title', align: 'center'},
                    {title: vm.$t('price'), field: 'price', align: 'center'},
                    {title: vm.$t('unit'), align: 'center', formatter: unitFmt},
                    {title: vm.$t('type1'), align: 'center', formatter: typeFmt},
                    {title: vm.$t('contact'), field: 'contact', align: 'center'},
                    {title: vm.$t('phone'), field: 'phone', align: 'center'},
                    {title: vm.$t('status'), field: 'status', align: 'center', formatter: statusFmt},
                    {title: vm.$t('chaozuo'), align: 'center', formatter: operFormatter, events: operateEvents}
                ],
                //条件查询
                queryParams: vm.params
            });
        },
        addRow: function () {
            if (vm.logisticsOffer.type == null) {
                alert("请先选择运输线路");
                return;
            }
            if (this.logisticsOfferAddressList.length > 4) {
                alert("不能超过5条线路");
                return;
            }
            var data = {province: null, city: null};
            this.logisticsOfferAddressList.push(data)
        },
        removeRow: function (item, index, e) {
            var div = JSON.parse(JSON.stringify(this.logisticsOfferAddressList));
            div.splice(index, 1)
            this.logisticsOfferAddressList = [];
            this.$nextTick(function () {
                this.logisticsOfferAddressList = div;
            })
            console.log("移除知乎", JSON.stringify(this.logisticsOfferAddressList))

        },
        query: function () {
            vm.reload();
        },
        //增加国际线路
        addRow1: function () {
            if (this.logisticsOfferAddressList1.length > 4) {
                alert("不能超过5条线路");
                return;
            }
            var data = {province: null, city: null};
            this.logisticsOfferAddressList1.push(data)
        },
        //移除国际线路
        removeRow1: function (index, e) {
            this.logisticsOfferAddressList1.splice(index, 1)
        },
        // query: function () {
        //     $("#table").bootstrapTable("refreshOptions", {
        //         queryParams: function (param) {
        //             var query = $.extend(true, param, vm.params);
        //             return query;
        //         },
        //         pageNumber: 1
        //     });
        //     vm.reload();
        // },
        add: function () {
            vm.showList = false;
            vm.showBtn = true;
            vm.title = "新增";
            vm.logisticsOffer = {};
            vm.logisticsOffer.status = 0;
            vm.ue.setContent("");
            //新增成功后再进新增页时清空之前的地址数据
            vm.logisticsOfferAddressList = [];
            vm.logisticsOfferAddressList1 = [];
        },
        update: function (event) {
            var id = getSelectedRowId("id");
            if (id == null) {
                return;
            }
            vm.showList = false;
            vm.showBtn = true;
            vm.title = "修改";
            // vm.getInfoLogisticsId();
            vm.logisticsOfferAddressList = [];
            vm.logisticsOfferAddressList1 = [];
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
            vm.logisticsOffer.businessIntroduction = UE.utils.unhtml(this.ue.getContent());
            var url = vm.logisticsOffer.id == null ? "logistics/logisticsoffer/save" : "logistics/logisticsoffer/update";
            if (vm.logisticsOffer.type == 0) {
                vm.logisticsOffer.logisticsOfferAddressList = vm.logisticsOfferAddressList;
            } else {
                vm.logisticsOffer.logisticsOfferAddressList = vm.logisticsOfferAddressList1;
            }
            if (vm.logisticsOffer.logisticsOfferAddressList.length < 1) {
                alert("请添加线路！");
                return
            }
            var flag = true;
            var flag1 = true;
            if (vm.logisticsOffer.type == 0) {
                vm.logisticsOfferAddressList.forEach(function (item, value) {
                    if (item == null || item == '') {
                        alert("请添加国内运输线路");
                        flag = false;
                    } else {
                        if (item.province == null || item.city == null || item.provinceEnd == null || item.cityEnd == null) {
                            alert("请补全运输线路");
                            flag = false;
                        }
                    }
                });
            }
            if (!flag) {
                return;
            }
            if (vm.logisticsOffer.type == 1) {
                vm.logisticsOfferAddressList1.forEach(function (item, value) {
                    if (item == null || item == '') {
                        alert("请添国际运输线路");
                        flag1 = false;
                    } else {
                        if (item.province == null || item.provinceEnd == null || item.city == null || item.cityEnd == null) {
                            alert("请补全运输线路");
                           flag1 = false;
                        }
                    }
                });
            }
            if (!flag1) {
                return;
            }
            let ceiShi=this.ue.getContentTxt();//获取纯文本内容
            let photos=UE.utils.unhtml(this.ue.getContent());
            var imgReg = /img.*?(?:>|\/)/gi;
            var arr = photos.match(imgReg);//筛选出图片个数
            if (arr!=null){
                if (arr.length>5){
                    alert("业务介绍上传的图片数量请少于6张")
                    return
                }
            }

            ceiShi=ceiShi.replace(/(^\s+)|(\s+$)/g,"").replace(/(\n)/g, "").replace(/(\t)/g, "").replace(/(\r)/g, "").replace(/<\/?[^>]*>/g, "").replace(/\s*/g, "");//去除空格等筛选

            if (vm.logisticsOffer.businessIntroduction == null || vm.logisticsOffer.businessIntroduction == '') {
                alert("请添加业务介绍");
                return;
            }
            if (ceiShi.length >1000) {
                alert("业务介绍不可超过1000字");
                return;
            }

            layer.load();
            $.ajax({
                type: "POST",
                url: baseURL + url,
                contentType: "application/json",
                data: JSON.stringify(vm.logisticsOffer),
                success: function (r) {
                    if (r.code === 0) {
                        alert('操作成功', function (index) {
                            vm.reload();
                        });
                    } else {
                        alert(r.msg);
                    }
                    layer.closeAll();
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
                    url: baseURL + "logistics/logisticsoffer/delete",
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
            vm.title = "详情";
            vm.logisticsOfferAddressList = [];
            vm.logisticsOfferAddressList1 = [];
            $.get(baseURL + "logistics/logisticsoffer/info/" + id, function (r) {
                vm.logisticsOffer = r.logisticsOffer;
                vm.ue.setContent(UE.utils.html(vm.logisticsOffer.businessIntroduction));
                if (vm.logisticsOffer.type == 0) {
                    // vm.showBtn = true;
                    //回显物流线路信息
                    vm.logisticsOffer.logisticsOfferAddressList.forEach(function (item) {
                        var str = JSON.stringify(window.getCities(item.province));
                        str = str.replace(/cityid/g, "value").replace(/city/g, "label");
                        item.cities = JSON.parse(str);
                        var strEnd = JSON.stringify(window.getCities(item.provinceEnd));
                        strEnd = strEnd.replace(/cityid/g, "value").replace(/city/g, "label");
                        item.cities1 = JSON.parse(strEnd);
                        vm.logisticsOfferAddressList = vm.logisticsOffer.logisticsOfferAddressList;
                    })
                } else if (vm.logisticsOffer.type == 1) {
                    // vm.showBtn = false;
                    // vm.logisticsOfferAddressList1 = r.logisticsOfferAddressList;
                    vm.logisticsOfferAddressList1 = vm.logisticsOffer.logisticsOfferAddressList;
                }
            });
        },
        reload: function (event) {
            vm.showList = true;
            vm.title = "";
            vm.backShow = true;
            vm.showBtn = true;
            $("#table").bootstrapTable("destroy");
            vm.initTable();
            //刷新 如需条件查询common.js
            // $("#table").BTF5(vm.params);
            $("form").RF();
        },
        //选择出发地 sheng
        provincesChange: function (value, data, e) {
            var str = JSON.stringify(window.getCities(value));
            str = str.replace(/cityid/g, "value").replace(/city/g, "label");
            //this.cities
            data.cities = JSON.parse(str);

        },

        //选择目的地
        provincesChange1: function (value, data, e) {
            console.log(value);
            var str = JSON.stringify(window.getCities(value));
            console.log(str);
            str = str.replace(/cityid/g, "value").replace(/city/g, "label");
            //this.cities
            data.cities1 = JSON.parse(str);
        },
        //改变物流类型
        changeType: function () {
            if (vm.logisticsOffer.type == '1') {
                if (vm.logisticsOfferAddressList1 == null || vm.logisticsOfferAddressList1.length == 0) {
                    vm.logisticsOfferAddressList1.push({})
                }
                vm.showBtn = true;
            } else {
                if (vm.logisticsOfferAddressList == null || vm.logisticsOfferAddressList.length == 0) {
                    vm.logisticsOfferAddressList.push({})
                }
                vm.showBtn = true;
            }
        },
        //上架
        updateStateUp: function (event) {
            var select = $("#table").bootstrapTable("getSelections");
            if (!select.length) {
                alert("请选择一条记录");
                return;
            }
            var ids = [];
            $.each(select, function (idx, item) {
                ids[idx] = item["id"];
            });
            confirm('确定要上架选中的记录？', function () {
                var url = "logistics/logisticsoffer/up";
                $.ajax({
                    type: "POST",
                    url: baseURL + url,
                    contentType: "application/json",
                    data: JSON.stringify(ids),
                    success: function (r) {
                        if (r.code === 0) {
                            alert('上架成功', function (index) {
                                vm.reload();
                            });
                        } else {
                            alert(r.msg);
                        }
                    }
                });
            });
        },
        updateStateDown: function (event) {
            var select = $("#table").bootstrapTable("getSelections");
            if (!select.length) {
                alert("请选择一条记录");
                return;
            }
            var ids = [];
            $.each(select, function (idx, item) {
                ids[idx] = item["id"];
            });
            confirm('确定要下架选中的记录？', function () {
                var url = "logistics/logisticsoffer/down";
                $.ajax({
                    type: "POST",
                    url: baseURL + url,
                    contentType: "application/json",
                    data: JSON.stringify(ids),
                    success: function (r) {
                        if (r.code === 0) {
                            alert('下架成功', function (index) {
                                vm.reload();
                            });
                        } else {
                            alert(r.msg);
                        }
                    }
                });
            });
        },


    },
    created: function () {
        var str = JSON.stringify(window.getProvinces());
        str = str.replace(/provinceid/g, "value").replace(/province/g, "label");
        this.provinces = JSON.parse(str);
        this.logisticsOfferAddressList.push({province: null, city: null})
        // this.logisticsOfferAddressList.push({province: 120000, city: null})
        // this.logisticsOfferAddressList.push({province: 130000, city: null})
    },
    watch: {
        "$i18n.locale": function (value, old) {
            if (value == 'en') {
                $("#table").bootstrapTable.defaults.locale = "en-US";
            } else {
                $("#table").bootstrapTable.defaults.locale = "zh-CN";
            }
            $("#table").bootstrapTable("destroy")
            this.initTable();
        }
    }
});