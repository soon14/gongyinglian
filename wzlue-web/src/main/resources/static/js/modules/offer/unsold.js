$(window).resize(function () {
    $('#table').bootstrapTable('resetView');
    $('#commodityTable').bootstrapTable('resetView');
    $('#categoryData').bootstrapTable('resetView');
});
$(function () {
    if (vm.$i18n.locale == 'en') {
        $("#table").bootstrapTable.defaults.locale = "en-US";
    } else {
        $("#table").bootstrapTable.defaults.locale = "zh-CN";
    }
    vm.initTable();
    // vm.initDateTime();
    //列表
    //表单提交
    $("form").FM({
        fields: vm.fields,
        success: vm.saveOrUpdate
    });

    $("#datetimepicker1").datetimepicker().on('hide', function (ev) {
        vm.goodsOffer.time = $("#datetimepicker1").val();

    });
    $("#datetimepicker2").datetimepicker().on('hide', function (ev) {
        vm.goodsOffer.time = $("#datetimepicker2").val();
    });
    $("#datetimepicker3").datetimepicker().on('hide', function (ev) {
        vm.goodsOffer.productionDate = $("#datetimepicker3").val();
    });

});
var vm = new Vue({
        el: '#rrapp',
        i18n,
        data: {
            goodsStatus: [],
            showList: true,
            //提交按钮
            showSure: true,
            title: null,
            commodity: [],
            goodsOffer: {
                images: []
            },
            unitPrice: 0,
            ue: UE.getEditor('myEditor', {
                initialFrameHeight: 440,
            }),
            images: [],
            goodsCount: null,
            params: {
                name: '',
                categoryId: null,
                purchasePlanNo: ""
            },
            categorys: [],
            bang: [],
            goodsUnits: [],
            provinces: [],
            cities: [],
            promotions: [],
            transactionManners: ['CFR', 'CIF', 'CPT', 'CIP', 'DAF', 'DES', 'DEQ', 'DDU', 'DDP', 'FOB', 'FCA', 'FAS', 'EXW'],
            events: {
                'click .showInfo': function (event, value, row, index) {
                    vm.showList = false;
                    vm.showSure = false;
                    vm.getInfo(value);
                }
            },
//验证字段
            fields: {
                goodsName: {
                    validators: {
                        notEmpty: {
                            message: '报盘名称不能为空'
                        }, stringLength: {max: 29, message: '报盘名称须少于30个字符'}
                    },
                }, goodsType: {
                    validators: {
                        notEmpty: {
                            message: '货物类型不能为空'
                        },
                    },
                }, goodsPrice: {
                    validators: {
                        notEmpty: {
                            message: '报盘单价不能为空'
                        }, regexp: {
                            //|(^0$)
                            regexp: /(^[1-9](\d{1,8})?(\.\d{1,2})?$)|(^\d\.\d{1,2}$)/,
                            message: '报盘单价为整数最多9位，小数最多2位的正数'
                        }
                        //   /^[0-9]{1,9}([.][0-9]{1,2})?$/
                    },
                }, priceUnit: {
                    validators: {
                        notEmpty: {
                            message: '价格单位不能为空'
                        },
                    },
                }, goodsCount: {
                    validators: {
                        notEmpty: {
                            message: '报盘数量不能为空'
                        }, regexp: {
                            regexp: /^[1-9]\d{0,9}$/,
                            message: '报盘数量为小于11位的正整数'
                        }

                    },
                }, goodsUnit: {
                    validators: {
                        notEmpty: {
                            message: '报盘单位不能为空'
                        },
                    },
                }, time1: {
                    trigger: 'change',
                    validators: {
                        notEmpty: {
                            message: '装运时间不能为空'
                        },
                        callback: {
                            message: '装运时间不能小于当前时间',
                            callback: function (value, validator, $field) {
                                let now = new Date().Format("yyyy-MM");
                                // let start = new Date(value.replace("-", "/").replace("-", "/"));
                                if (value >= now) {
                                    return true;
                                }
                                return false;
                            }
                        }
                    },
                }, time2: {
                    trigger: 'change',
                    validators: {
                        notEmpty: {
                            message: '提货时间不能为空'
                        },
                        callback: {
                            message: '提货时间不能小于当前时间',
                            callback: function (value, validator, $field) {
                                let now = new Date().Format("yyyy-MM");
                                // let start = new Date(value.replace("-", "/").replace("-", "/"));
                                if (value >= now) {
                                    return true;
                                }
                                return false;
                            }
                        }
                    },
                }, place1: {
                    validators: {
                        notEmpty: {
                            message: '装运口岸不能为空'
                        }, stringLength: {
                            max: 29,
                            message: '装运口岸须少于30个字符'
                        }
                    },
                }, place2: {
                    validators: {
                        notEmpty: {
                            message: '交货地点不能为空'
                        }, stringLength: {
                            max: 29,
                            message: '交货地点须少于30个字符'
                        }
                    },
                }, paymentTerm: {
                    validators: {
                        notEmpty: {
                            message: '付款条件不能为空'
                        }, stringLength: {
                            max: 19,
                            message: '付款条件须少于20个字符'
                        }
                    },
                }, productionDate: {
                    trigger: 'change',
                    validators: {
                        notEmpty: {
                            message: '生产日期不能为空'
                        },
                        callback: {
                            message: '生产日期不能大于当前时间',
                            callback: function (value, validator, $field) {
                                let now = new Date().Format("yyyy-MM");
                                // let start = new Date(value.replace("-", "/").replace("-", "/"));
                                if (value <= now) {
                                    return true;
                                }
                                return false;
                            }
                        }
                    },
                }, shelfLife: {
                    validators: {
                        notEmpty: {
                            message: '保质期不能为空'
                        }, regexp: {
                            regexp: /^[1-9]\d{0,9}$/,
                            message: '保质期为小于11位的正整数'
                        }
                    },
                }, clause: {
                    validators: {
                        /*notEmpty: {
                            message: '溢短装条款不能为空'
                        }, */
                        stringLength: {
                            max: 29,
                            message: '溢短装条款须少于30个字符'
                        }
                    },
                }, transactionManner: {
                    validators: {
                        notEmpty: {
                            message: '成交方式不能为空'
                        },
                    },
                },
                /*goodsPodProvince: {
                    message: '货物提货地址省验证失败',
                    validators: {
                        notEmpty: {
                            message: '货物提货地址省不能为空'
                        },
                    },
                }, goodsPodCity: {
                    message: '货物提货地址城市验证失败',
                    validators: {
                        notEmpty: {
                            message: '货物提货地址城市不能为空'
                        },
                    },
                }, goodsSname: {
                    message: '货物仓库名称验证失败',
                    validators: {
                        notEmpty: {
                            message: '货物仓库名称不能为空'
                        }, stringLength: {min: 1, max: 30, message: '货物仓库名称小于30'}
                    },
                }*/
                // , goodsSailingTime: {
                //     message: '货物船期验证失败',
                //     trigger: 'change',
                //     validators: {
                //         notEmpty: {
                //             message: '货物船期不能为空'
                //         },
                //     },
                // }
                /* , goodsCsc: {
                     message: '可否拼柜验证失败',
                     validators: {
                         notEmpty: {
                             message: '可否拼柜不能为空'
                         },
                         callback: function (value, v) {
                             if (value == '' || value == null) {
                                 return false;
                             } else {
                                 return true;
                             }
                         }
                     },
                 },*/ goodsDescribe: {
                    message: '商品描述验证失败',
                    validators: {
                        callback: function (value, v) {
                            if (vm.ue.getContent() == null || vm.ue.getContent() == "") {
                                return false;
                            } else {
                                return true;
                            }
                        }
                    }
                }, goodsDescribe: {
                    message: '货物描述验证失败',
                    validators: {
                        notEmpty: {
                            message: '货物描述不能为空'
                        },
                    },
                }, /*goodsCompanyId: {
                message: '货物企业id验证失败',
                validators: {
                    notEmpty: {
                        message: '货物企业id不能为空'
                    },
                },
            }, goodsTransactionCount: {
                message: '货物历史交易数量验证失败',
                validators: {
                    notEmpty: {
                        message: '货物历史交易数量不能为空'
                    },
                },
            }, goodsState: {
                message: '货物状态0：下架1：上架验证失败',
                validators: {
                    notEmpty: {
                        message: '货物状态0：下架1：上架不能为空'
                    },
                },
            }, commodityCountry: {
                message: '产地验证失败',
                validators: {
                    notEmpty: {
                        message: '产地不能为空'
                    },
                },
            }, commodityFactoryNumber: {
                message: '厂号验证失败',
                validators: {
                    notEmpty: {
                        message: '厂号不能为空'
                    },
                },
            }, commodityPacking: {
                message: '包装验证失败',
                validators: {
                    notEmpty: {
                        message: '包装不能为空'
                    },
                },
            }, commodityClassification: {
                message: '商品分类验证失败',
                validators: {
                    notEmpty: {
                        message: '商品分类不能为空'
                    },
                },
            }, createBy: {
                message: '创建人验证失败',
                validators: {
                    notEmpty: {
                        message: '创建人不能为空'
                    },
                },
            }, *//* createTime: {
                message: '创建时间验证失败',
                validators: {
                    notEmpty: {
                        message: '创建时间不能为空'
                    },
                },
            },*//* modifyBy: {
                message: '修改人验证失败',
                validators: {
                    notEmpty: {
                        message: '修改人不能为空'
                    },
                },
            }, *//* modifyTime: {
                message: '修改时间验证失败',
                validators: {
                    notEmpty: {
                        message: '修改时间不能为空'
                    },
                },
            },*//* remarks: {
                message: '备注验证失败',
                validators: {
                    notEmpty: {
                        message: '备注不能为空'
                    },
                },
            }, currency: {
                message: '币种验证失败',
                validators: {
                    notEmpty: {
                        message: '币种不能为空'
                    },
                },
            }*/
            }
        },
        methods: {
            goodsTypeChange: function () {
                // if (vm.goodsOffer.goodsType == 0)
                //     vm.goodsOffer.goodsCsc = 0;
                // else
                //     vm.goodsOffer.goodsCsc = null;
                if (vm.goodsOffer.goodsType == 0) {
                    vm.goodsOffer.goodsUnit = 1;    //柜
                    $("#goodsUnit").attr('disabled', 'disabled');
                } else {
                    $('#goodsUnit').removeAttr('disabled');
                }
                //清除货物信息
                $("#commodityTable").bootstrapTable('removeAll');
            },
            //改变报盘单价
            changePrice: function () {
                var commoditys = $("#commodityTable").bootstrapTable("getData");
                commoditys.forEach(function (item) {
                    item.commodityPrice = vm.goodsOffer.goodsPrice;
                });
                // $("#commodityTable").bootstrapTable("removeAll");
                $("#commodityTable").bootstrapTable("load", {
                    rows: commoditys,
                    total: commoditys == null ? 0 : commoditys.length
                });
            },
            //改变价格单位
            changePriceUnit: function () {
                if (vm.goodsOffer.goodsUnit == 3 && (vm.goodsOffer.priceUnit == 1 || vm.goodsOffer.priceUnit == 3)) {
                    $("#commodityTable").bootstrapTable("removeAll");
                    alert("报盘数量单位请与价格单位保持一致");
                    vm.goodsOffer.goodsUnit = 2;
                } else if (vm.goodsOffer.goodsUnit == 2 && (vm.goodsOffer.priceUnit == 2 || vm.goodsOffer.priceUnit == 4)) {
                    $("#commodityTable").bootstrapTable("removeAll");
                    alert("报盘数量单位请与价格单位保持一致");
                    vm.goodsOffer.goodsUnit = 3;
                }
                var commoditys = $("#commodityTable").bootstrapTable("getData");
                commoditys.forEach(function (item) {
                    item.priceUnit = vm.goodsOffer.priceUnit;
                    if (vm.goodsOffer.priceUnit == 1 || vm.goodsOffer.priceUnit == 3) {
                        item.commodityUnit = 1;
                    } else if (vm.goodsOffer.priceUnit == 2 || vm.goodsOffer.priceUnit == 4) {
                        item.commodityUnit = 2;
                    }
                });
                /*     $("#commodityTable").bootstrapTable("removeAll");*/
                $("#commodityTable").bootstrapTable("load", {
                    rows: commoditys,
                    total: commoditys == null ? 0 : commoditys.length
                });
            },
            countPrice: function () {
                if (isBlank(vm.goodsOffer.goodsPrice)) {
                    alert("请先输入报盘单价");
                    vm.goodsOffer.goodsPromotionId = '';
                    return;
                }
                $.get(baseURL + 'promotion/promotion/info/' + vm.goodsOffer.goodsPromotionId, function (r) {
                    var promotion = r.promotion;
                    var goodsPromotionPrice = 0;
                    if (promotion.ruleType == 0) {//打折
                        goodsPromotionPrice = vm.goodsOffer.goodsPrice * (1 -(promotion.discountRate / 100));
                    }else if (promotion.ruleType == 1) {//折价
                        goodsPromotionPrice = vm.goodsOffer.goodsPrice - promotion.discountPrice;
                    }
                    if (goodsPromotionPrice < 0 || goodsPromotionPrice == 0) {
                        alert("促销后的报盘单价须大于0");
                        vm.goodsOffer.goodsPromotionId = '';
                        return;
                    }
                })
            },
            //改变报盘数量
            changeCount: function () {
                if (vm.goodsOffer.goodsType == 1 && (vm.goodsOffer.goodsUnit == 2 || vm.goodsOffer.goodsUnit == 3)) { //现货
                    var commoditys = $("#commodityTable").bootstrapTable("getData");
                    commoditys.forEach(function (item) {
                        item.commodityCount = vm.goodsOffer.goodsCount;
                    });
                    // $("#commodityTable").bootstrapTable("removeAll");
                    $("#commodityTable").bootstrapTable("load", {
                        rows: commoditys,
                        total: commoditys == null ? 0 : commoditys.length
                    });
                }
            },
            //改变数量单位
            changeCountUnit: function () {
                if (vm.goodsOffer.goodsUnit == 3 && (vm.goodsOffer.priceUnit == 1 || vm.goodsOffer.priceUnit == 3)) {
                    $("#commodityTable").bootstrapTable("removeAll");
                    alert("报盘数量单位请与价格单位保持一致");
                    vm.goodsOffer.goodsUnit = 2;
                } else if (vm.goodsOffer.goodsUnit == 2 && (vm.goodsOffer.priceUnit == 2 || vm.goodsOffer.priceUnit == 4)) {
                    $("#commodityTable").bootstrapTable("removeAll");
                    alert("报盘数量单位请与价格单位保持一致");
                    vm.goodsOffer.goodsUnit = 3;
                } else if (vm.goodsOffer.goodsUnit == 2 || vm.goodsOffer.goodsUnit == 3) {
                    var commoditys = $("#commodityTable").bootstrapTable("getData");
                    commoditys.forEach(function (item) {
                        item.commodityCount = vm.goodsOffer.goodsCount;
                    });
                    // $("#commodityTable").bootstrapTable("removeAll");
                    $("#commodityTable").bootstrapTable("load", {
                        rows: commoditys,
                        total: commoditys == null ? 0 : commoditys.length
                    });
                } else if (vm.goodsOffer.goodsUnit == 1) {
                    var commoditys = $("#commodityTable").bootstrapTable("getData");
                    commoditys.forEach(function (item) {
                        item.commodityCount = null;
                    });
                    // $("#commodityTable").bootstrapTable("removeAll");
                    $("#commodityTable").bootstrapTable("load", {
                        rows: commoditys,
                        total: commoditys == null ? 0 : commoditys.length
                    });
                }
            },
            getPromotions: function () {
                $.get(baseURL + 'promotion/promotion/list', {'status': 1, 'termValidate': true}, function (r) {
                    var rows = r.data;
                    rows = JSON.stringify(rows);
                    rows = rows.replace(/id/g, "value").replace(/title/g, "label");
                    vm.promotions = JSON.parse(rows);
                })
            },
            getCommodityWeightUnit: function () {
                $.get({
                    url: baseURL + 'sys/dict/getByType',
                    dataType: 'JSON',
                    data: {type: '货物重量单位'},
                    success: function (r) {
                        if (r.code == 0) {
                            var rows = r.data;
                            rows = JSON.stringify(rows);
                            rows = rows.replace(/code/g, "value").replace(/name/g, "label");
                            vm.goodsUnits = JSON.parse(rows);
                        }
                    }
                })
            },
            initTable: function () {
                var vm = this;
                //列表
                $("#table").bootstrapTable("destroy")
                $("#table").BT({
                    url: baseURL + 'offer/goodsoffer/list',
                    columns: [
                        {checkbox: true},
                        {title: vm.$t('name'), field: 'goodsName'},
                        {title: vm.$t('offerCode'), field: 'goodsNumber'},
                        {
                            title: vm.$t('TypeOfGoods'), field: 'goodsType', formatter: function (value, row, index) {
                                if (value == 0) {
                                    return "期货"
                                } else if (value == 1) {
                                    return "现货"
                                }
                            }
                        },
                        {
                            title: vm.$t('num'), field: 'goodsCount', formatter: function (value, row, index) {
                                var str;
                                if (row.goodsUnit==1) {
                                    str = "柜";
                                }else if (row.goodsUnit == 2) {
                                    str = "吨";
                                } else if (row.goodsUnit == 3) {
                                    str =  "千克";
                                }
                                return value + "&nbsp;&nbsp;" + str;
                            }
                        },
                        /*{
                            title: vm.$t('unit'), field: 'goodsUnit', formatter: function (value, row, index) {
                                if (value == 1) {
                                    return "柜"
                                } else if (value == 2) {
                                    return "吨"
                                } else if (value == 3) {
                                    return "千克"
                                }
                            }
                        },*/
                        {title: vm.$t('UnitPrice'), field: 'goodsPrice', formatter: function (value, row, index) {
                                var str;
                                if (row.priceUnit == 1) {
                                    str = "元/吨";
                                } else if (row.priceUnit == 2) {
                                    str = "元/千克";
                                } else if (row.priceUnit == 3) {
                                    str = "美元/吨";
                                } else if (row.priceUnit == 4) {
                                    str = "美元/千克";
                                }
                                return value + "&nbsp;&nbsp;" + str;
                            }
                         },
                       /* {
                            title: vm.$t('unit'), field: 'priceUnit', formatter: function (value, row, index) {
                                if (value == 1) {
                                    return "元/吨"
                                } else if (value == 2) {
                                    return "元/千克"
                                } else if (value == 3) {
                                    return "美元/吨"
                                } else if (value == 4) {
                                    return "美元/千克"
                                }
                            }
                        },*/
                        /* {
                             title: vm.$t('productTypes'), formatter: function (value, row, index) {
                             return row.goodsPodProvince + row.goodsPodCity;
                         }
                         },*/
                        // {title: vm.$t('warehouseName'), field: 'goodsSname'},
                        // {title: vm.$t('Schedule'), field: 'goodsSailingTime'},
                        {title: vm.$t('place'), field: 'countryName'},
                        {title: vm.$t('Producer') + "/" + vm.$t('FactoryNumber'), field: 'commodityFactoryNumber'},
                        // {
                        //     title: vm.$t("cabinet"), field: 'goodsCsc', formatter: function (value, row, index) {
                        //         if (value == 0) {
                        //             return "不可拼"
                        //         } else if (value == 1) {
                        //             return "拼柜"
                        //         }
                        //     }
                        // },
                        {
                            title: vm.$t('NumberTransactions'), field: 'goodsTransactionCount', formatter: function (value, row, index) {
                                var str;
                                if (row.goodsUnit==1) {
                                    str = "柜";
                                }else if (row.goodsUnit == 2) {
                                    str = "吨";
                                } else if (row.goodsUnit == 3) {
                                    str =  "千克";
                                }
                                return value + "&nbsp;&nbsp;" + str;
                            }
                        },
                        {
                            title: vm.$t('Stock'), field: 'stock', formatter: function (value, row, index) {
                                var str;
                                if (row.goodsUnit==1) {
                                    str = "柜";
                                }else if (row.goodsUnit == 2) {
                                    str = "吨";
                                } else if (row.goodsUnit == 3) {
                                    str =  "千克";
                                }
                                return value + "&nbsp;&nbsp;" + str;
                            }
                        },
                        {title: vm.$t('LPTime'), field: 'modifyTime'},
                        {
                            title: vm.$t("status"), field: 'goodsState',
                            formatter: function (value, row, index) {
                                if (value == 0) {
                                    return '下架';
                                } else if (value == 1) {
                                    return '上架';
                                }
                            }
                        },
                        {
                            title: vm.$t("chaozuo"), field: 'id',
                            //查看详情
                            formatter: function (value, row, index) {
                                return "<a href='javascript:void(0)' class='showInfo'>" + vm.$t('Details') + "</a>";
                            }, events: vm.events
                        },
                    ],
                    //条件查询
                    queryParams: vm.params
                });


                $("#commodityTable").bootstrapTable("destroy")
                $("#commodityTable").BT({
                    columns: [
                        {checkbox: true},
                        {title: vm.$t("commodityCode"), field: 'itemCode', visible: false},
                        {title: vm.$t('ProductName'), field: 'itemName'},
                        {title: vm.$t('Categories') + "Id", field: 'categoryId', visible: false},
                        {title: vm.$t('Categories'), field: 'categoryName', visible: false},
                        {
                            title: vm.$t('place'), field: 'commodityCountry',
                            formatter: function (value, row, index) {
                                if (row.commodityCountryName != null) {
                                    return row.commodityCountryName;
                                } else {
                                    return row.commodityCountry;
                                }

                            }
                        },
                        {title: vm.$t('Producer') + "/" + vm.$t('FactoryNumber'), field: 'factoryNo'},
                        {
                            title: vm.$t('UnitPrice'),
                            // valign: 'middle',
                            // align: 'center',
                            field: 'commodityPrice',
                            formatter: function (value, row, index) {
                                if (row.priceUnit == 1) {
                                    return value + '&nbsp;&nbsp;元/吨';
                                } else if (row.priceUnit == 2) {
                                    return value + '&nbsp;&nbsp;元/千克';
                                } else if (row.priceUnit == 3) {
                                    return value + '&nbsp;&nbsp;美元/吨';
                                } else if (row.priceUnit == 4) {
                                    return value + '&nbsp;&nbsp;美元/千克';
                                }
                            }
                            // editable: {
                            //     type: 'text',
                            //     width: '10%',
                            //     title: vm.$t('price'),
                            //     emptytext: vm.$t('price'),
                            //     defaultValue: 1,
                            //     validate: function (v) {
                            //         if (!v) return vm.$t('price');
                            //         if (isNaN(v)) return '请输入正确的报价';
                            //         // if (parseInt(v) < 0) return '报价必须大于0';
                            //         // if (parseInt(v) > 9999999999) return '报价不能大于9999999999';
                            //         if (!/(^[1-9](\d{1,8})?(\.\d{1,2})?$)|(^0$)|(^\d\.\d{1,2}$)/.test(v)) return '商品报价整数最多9位，小数最多2位';
                            //         vm.unitPrice = v;
                            //         vm.$nextTick(function () {
                            //             vm.unitPrice = JSON.parse(JSON.stringify(vm.unitPrice));
                            //         })
                            //         $('#commodityTable').bootstrapTable('resetView');
                            //     },
                            // }
                        },
                        {
                            title: vm.$t('Weight'),
                            // valign: 'middle',
                            // align: 'center',
                            field: 'commodityCount',
                            editable: {
                                type: 'text',
                                title: vm.$t('InputWeight'),
                                emptytext: vm.$t('InputWeight'),
                                // defaultValue: 1,
                                validate: function (v) {
                                    if (!v) return vm.$t('InputWeight');
                                    if (isNaN(v)) return '请输入正确的重量';
                                    // if (parseInt(v) < 0) return '重量必须大于0';
                                    // if (parseInt(v) > 9999999999) return '重量不能大于9999999999';
                                    if (!/(^[1-9](\d{1,8})?(\.\d{1,2})?$)|(^0$)|(^\d\.\d{1,2}$)/.test(v)) return '商品重量整数最多9位，小数最多2位';
                                    // vm.unitPrice.commodityCount = v;
                                    // vm.$nextTick(function () {
                                    //     vm.unitPrice = JSON.parse(JSON.stringify(vm.unitPrice));
                                    // })
                                    $('#commodityTable').bootstrapTable('resetView');
                                },
                            }
                        },
                        {   //valign: 'middle', align: 'center',
                            title: vm.$t('unit'), field: 'commodityUnit',
                            formatter: function (value, row, index) {
                                if (value == 1) {
                                    return "吨"
                                } else if (value == 2) {
                                    return "千克"
                                }
                            }
                            // editable: {
                            //     type: 'select',
                            //     width: '10%',
                            //     title: vm.$t("SelectUnit"),
                            //     emptytext: vm.$t("SelectUnit"),
                            //     source: [{value: 1, text: "吨"}, {value: 2, text: "千克"}],
                            //     defaultValue: 1,
                            //     validate: function (v) {
                            //         if (!v) return vm.$t("SelectUnit");
                            //         $('#commodityTable').bootstrapTable('resetView');
                            //     }
                            // }
                        },
                        {
                            title: vm.$t("pack"),
                            // valign: 'middle',
                            // align: 'center',
                            field: 'commodityPacking',
                            editable: {
                                type: 'text',
                                emptytext: vm.$t("InputPack"),
                                title: vm.$t("InputPack"),
                                // defaultValue: 1,
                                validate: function (v) {
                                    // if (!v) return vm.$t("InputPack");
                                    if (v && v.length > 20) return '包装长度须小于20';
                                    $('#commodityTable').bootstrapTable('resetView');
                                }
                            }
                        },
                    ],

                    onLoadSuccess: function () {
                        $('#commodityTable').bootstrapTable('resetView');
                    }
                })
                this.goodsCount = 0;
                $("#categoryData").bootstrapTable("destroy")
                $("#categoryData").BT({
                    url: baseURL + 'goods/info/list',
                    columns: [
                        {checkbox: true},
                        {title: vm.$t("commodityCode"), field: 'itemCode'},
                        {title: vm.$t('productName'), field: 'itemName'},
                        {title: vm.$t('Categories') + "Id", field: 'categoryId', visible: false},
                        //{title: '产地ID', field: 'places', visible: false},
                        {title: vm.$t('Categories'), field: 'categoryName'},
                        {
                            title: vm.$t('place'),
                            formatter: function (value, row, index) {
                                var places = row.places;
                                if (places.length < 1) {
                                    return "";
                                } else {
                                    return places[0].placeName;
                                }

                            }

                        },
                        {title: vm.$t('FactoryNumber'), field: 'factoryNo'},
                        {title: vm.$t('TaxNumber'), field: 'taxNumber'},
                        {
                            title: vm.$t('TariffPricing'),
                            formatter: function (value, row, index) {
                                var pricingMethod = row.pricingMethod;
                                if (pricingMethod == '1') {
                                    return '从价';
                                } else {
                                    return '从量';
                                }
                            }
                        },
                        {
                            title: vm.$t('numerical'),
                            formatter: function (value, row, index) {
                                var places = row.places;
                                if (places.length < 1) {
                                    return "";
                                } else {
                                    return places[0].numerical;
                                }

                            }

                        },
                    ]
                })
            },
            initDateTime: function () {
                var d, s;
                d = new Date();
                s = d.getFullYear() + "-";             //取年份
                s = s + (d.getMonth() + 1) + "-";//取月份
                s += d.getDate() + " ";         //取日期
                s += d.getHours() + ":";       //取小时
                s += d.getMinutes() + ":";    //取分
                s += d.getSeconds();         //取秒
                var language;
                if (vm.$i18n.locale == 'en') {
                    language = 'en'
                } else {
                    language = 'zh-CN'
                }
                $("#datetimepicker").datetimepicker({
                    startDate: s,
                    language: language,
                }).on('hide', function (ev) {
                    var value = $("#datetimepicker").val();
                    vm.goodsOffer.goodsSailingTime = value;
                });
            },
            statusChange: function (value) {
                $("#commodityTable").bootstrapTable('removeAll');
            },

            addCategory: function (event) {
                var guid = $("#categoryData").bootstrapTable("getSelections");
                if (!guid.length) {
                    alert("请选择一条商品信息");
                    return;
                }
                // var flag = false;
                if (guid.length > 1) {
                    // if (this.goodsOffer.goodsType == 0) {
                    //     //期货
                    //     if (this.goodsOffer.goodsCsc == 0) {
                    //         if (guid.length >= 1) {
                    //             alert("您选择的是不可拼柜，只能添加一条商品");
                    //             return;
                    //         }
                    //     }
                    // } else {
                    //     //现货
                    //     if (guid.length >= 1) {
                    //         alert("您选择的商品类型是现货,只能添加一条商品信息");
                    //         return;
                    //     }
                    // }
                    alert("只能选择一条商品信息");
                    return;
                }
                var commoditys = $("#commodityTable").bootstrapTable("getData");
                // if (this.goodsOffer.goodsType == 0) {
                //     //期货
                //     if (this.goodsOffer.goodsCsc == 0) {
                //         if (commoditys.length >= 1) {
                //             alert("您选择的是不可拼柜，只能添加一条商品");
                //             return;
                //         }
                //     }
                // } else {
                //     //现货
                //     if (commoditys.length >= 1) {
                //         alert("您选择的商品类型是现货,只能添加一条商品信息");
                //         return;
                //     }
                // }
                if (commoditys.length >= 1) {
                    alert("只能添加一条商品信息");
                    return;
                }

                guid.forEach(function (item) {
                    // if (flag) {
                    //     return;
                    // }
                    // commoditys.forEach(function (c) {
                    //     if (c.itemCode == item.itemCode) {
                    //         alert("编码" + item.itemCode + "商品已添加，请重新选择");
                    //         flag = true;
                    //         return;
                    //     }
                    // });
                    // item.commodityPrice = null;
                    // item.commodityCount = null;
                    // item.commodityUnit = null;
                    // item.commodityPacking = null;

                    item.commodityPrice = vm.goodsOffer.goodsPrice;
                    item.priceUnit = vm.goodsOffer.priceUnit;
                    // if (vm.goodsOffer.priceUnit == 1 || vm.goodsOffer.priceUnit == 2) { //元
                    //     item.priceUnit = 1;
                    // } else if (vm.goodsOffer.priceUnit == 3 || vm.goodsOffer.priceUnit == 4) { //美元
                    //     item.priceUnit = 2;
                    // }
                    if (vm.goodsOffer.goodsType == 1 && (vm.goodsOffer.goodsUnit == 2 || vm.goodsOffer.goodsUnit == 3)) { //现货
                        item.commodityCount = vm.goodsOffer.goodsCount;
                    } else {
                        item.commodityCount = null;
                    }
                    if (vm.goodsOffer.priceUnit == 1 || vm.goodsOffer.priceUnit == 3) { //吨
                        item.commodityUnit = 1;
                    } else if (vm.goodsOffer.priceUnit == 2 || vm.goodsOffer.priceUnit == 4) { //千克
                        item.commodityUnit = 2;
                    }
                    item.commodityPacking = null;
                });
                // if (flag) {
                //     return;
                // }
                // commoditys = commoditys.concat(guid);
                var temp = guid;
                for (var i = 0; i < temp.length; i++) {
                    temp[i].commodityCountry = temp[i].places[0].placeName;
                    temp[i].countryCode = temp[i].places[0].place;
                }
                commoditys = commoditys.concat(temp);

                //commoditys[0].commodityCountry = commoditys[0].places[0].placeName;
                $("#commodityTable").bootstrapTable("removeAll");
                this.goodsCount = commoditys.length;
                // $("#commodityTable").bootstrapTable("append", JSON.parse(JSON.stringify(commoditys)));
                $("#commodityTable").bootstrapTable("load", {
                    rows: commoditys,
                    total: commoditys == null ? 0 : commoditys.length
                });
                $("#myModal").modal('hide');
                window.setTimeout(function () {
                    $('#commodityTable').bootstrapTable('resetView');
                }, 500)
            },
            query: function () {
                vm.reload();
            },
            add: function () {
                vm.showList = false;
                vm.showSure = true;
                vm.title = "新增";

                vm.goodsOffer = {
                    // currency: 1,
                    images: []
                };
                vm.images.length = 0;
                // vm.goodsOffer.goodsType = 0;
                vm.ue.setContent('');

                // vm.goodsOffer.goodsUnit = 'CTN';
            },
            update: function (event) {
                var id = getSelectedRowId("id");
                if (id == null) {
                    return;
                }
                vm.showList = false;
                vm.showSure = true;
                vm.title = "修改";
                vm.getInfo(id)
            },
            updateStatueUnsold: function (event) {

                var select = $("#table").bootstrapTable("getSelections");
                if (!select.length) {
                    alert("请选择一条记录");
                    return;
                }
                var ids = [];
                $.each(select, function (idx, item) {
                    ids[idx] = item["id"];
                });
                var url = "offer/goodsoffer/unsold";
                confirm('确定变更报盘状态吗？', function () {
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
            updateStatue: function (event) {
                var select = $("#table").bootstrapTable("getSelections");
                if (!select.length) {
                    alert("请选择一条记录");
                    return;
                }
                var ids = [];
                $.each(select, function (idx, item) {
                    ids[idx] = item["id"];
                });
                var url = "offer/goodsoffer/onsale";
                confirm('确定变更报盘状态吗？', function () {
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
            validate: function () {
                var bl = $('form').VF();//启用验证
                if (!bl) {
                    return;
                }
            },
            saveOrUpdate: function (event) {
                layer.load();
                if (vm.goodsOffer.goodsType == 1) {
                    var date = new Date(vm.goodsOffer.productionDate);
                    /* vm.goodsOffer.expirationDate = formatDate(new Date(date.getTime() + 1000 * 60 * 60 * 24 * vm.goodsOffer.shelfLife), "YY-MM-DD");
                    let now = new Date().Format("yyyy-MM-dd");*/
                    vm.goodsOffer.expirationDate = formatDate(new Date(date.getTime() + 1000 * 60 * 60 * 24 * vm.goodsOffer.shelfLife * 30), "YY-MM");
                    let now = new Date().Format("yyyy-MM");
                    if (vm.goodsOffer.expirationDate < now) {
                        alert("商品已过期");
                        layer.closeAll();
                        return;
                    }


                }
                var a = $('#bjEdit').editable('validate');
                if (a.bjEdit != null) {
                    alert(a.bjEdit);
                    layer.closeAll();
                    return;
                }
// var as = $(".editable").editable('validate');
// if (as.commodityPrice != null) {
//     alert(as.commodityPrice);
//     layer.closeAll();
//     return;
// }
                var as = $(".editable").editable('validate');
                if (as.commodityCount != null) {
                    alert(as.commodityCount);
                    layer.closeAll();
                    return;
                }
// var as = $(".editable").editable('validate');
// if (as.commodityUnit != null) {
//     alert(as.commodityUnit);
//     layer.closeAll();
//     return;
// }
                var as = $(".editable").editable('validate');
                if (as.commodityPacking != null) {
                    alert(as.commodityPacking);
                    layer.closeAll();
                    return;
                }
                /*            if (vm.images != null && vm.images.length > 0) {
                                vm.goodsOffer.images = vm.images;
                            } else {
                                console.log(vm.goodsOffer.images == null || JSON.stringify(vm.goodsOffer.images) == '{}')
                                if (vm.goodsOffer.images == null || JSON.stringify(vm.goodsOffer.images) == '{}') {
                                    alert("请上传合商品图片");
                                    return;
                                } else {
                                    console.log(vm.goodsOffer.images)
                                }
                            }*/
                if (vm.images.length == 0 && (vm.goodsOffer.images == null || vm.goodsOffer.images.length == 0)) {
                    alert("请上传图片");
                    layer.closeAll();
                    return;
                }
                if (vm.goodsOffer.images.length>5) {
                    alert("商品图片或凭证最多五张");
                    layer.closeAll();
                    return;
                }

                if (vm.goodsOffer.images == null) {
                    vm.goodsOffer.images = [];
                }
                // vm.goodsOffer.images = vm.images;
                vm.goodsOffer.images = vm.goodsOffer.images.concat(vm.images);
                vm.goodsOffer.goodsDescribe = UE.utils.unhtml(this.ue.getContent());
                let ceiShi=this.ue.getContentTxt();//获取纯文本内容
                let photos=UE.utils.unhtml(this.ue.getContent());
                var imgReg = /img.*?(?:>|\/)/gi;
                var arr = photos.match(imgReg);//筛选出图片个数
                if(arr!=null){
                    if (arr.length>5){
                        alert("商品描述上传的图片数量请少于6张")
                        layer.closeAll();
                        return
                    }
                }
                ceiShi=ceiShi.replace(/(^\s+)|(\s+$)/g,"").replace(/(\n)/g, "").replace(/(\t)/g, "").replace(/(\r)/g, "").replace(/<\/?[^>]*>/g, "").replace(/\s*/g, "");//去除空格等筛选

                if (vm.goodsOffer.goodsDescribe == "" || vm.goodsOffer.goodsDescribe == null) {
                    alert("请填写商品描述");
                    layer.closeAll();
                    return;
                }else if (ceiShi.length > 1000) {
                     alert("商品描述不可超出1000字");
                     layer.closeAll();
                    return;
                }
                vm.goodsOffer.commodityPrice = $("#bjEdit").text().trim();
                var commoditys = $("#commodityTable").bootstrapTable("getData")
                commoditys.forEach(function (item) {
                    item.commodityName = item.itemName;
                    item.commodityId = item.id;
                    item.commodityNumber = item.itemCode;
                    item.commodityFactoryNumber = item.factoryNo;
                    if (item.countryCode != null) { //报盘修改时 产地丢失
                        item.commodityCountry = item.countryCode;
                    }
                });
                if (!commoditys.length) {
                    alert("请添加商品");
                    layer.closeAll();
                    return;
                }
                //按 吨/千克 报盘，报盘重量 ！= 商品重量
                if ((vm.goodsOffer.goodsUnit == 2 || vm.goodsOffer.goodsUnit == 3) && vm.goodsOffer.goodsCount != commoditys[0].commodityCount) {
                    alert("重量请与报盘重量保持一致");
                    commoditys[0].commodityCount = vm.goodsOffer.goodsCount;
                    $("#commodityTable").bootstrapTable("load", {
                        rows: commoditys,
                        total: commoditys == null ? 0 : commoditys.length
                    });
                    layer.closeAll();
                    return;
                }
// for (let obj of commoditys){
//     if (!/(^[1-9](\d{1,8})?(\.\d{1,2})?$)|(^0$)|(^\d\.\d{1,2}$)/.test(obj.commodityPrice)) {
//         alert("商品报价整数最多9位，小数最多2位");
//         layer.closeAll();
//         return;
//     }
//     if (!/(^[1-9](\d{1,8})?(\.\d{1,2})?$)|(^0$)|(^\d\.\d{1,2}$)/.test(obj.commodityCount)) {
//         alert("商品重量整数最多9位，小数最多2位");
//         layer.closeAll();
//         return;
//     }
// }

//截止日期计算
                vm.goodsOffer.commoditys = commoditys;
                vm.goodsOffer.commodityCountry = commoditys[0].commodityCountry;
                vm.goodsOffer.commodityClassification = commoditys[0].categoryId;
                vm.goodsOffer.commodityFactoryNumber = commoditys[0].commodityFactoryNumber;
                vm.goodsOffer.commodityPacking = commoditys[0].commodityPacking;
                var url = vm.goodsOffer.id == null ? "offer/goodsoffer/save" : "offer/goodsoffer/update";
                $.ajax({
                    type: "POST",
                    url: baseURL + url,
                    contentType: "application/json",
                    data: JSON.stringify(vm.goodsOffer),
                    success: function (r) {
                        layer.closeAll();
                        if (r.code === 0) {
                            alert('操作成功', function (index) {
                                location.reload();
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
                        url: baseURL + "offer/goodsoffer/delete",
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
            }
            ,
            getInfo: function (id) {
                vm.goodsOffer = {};
                // vm.images = [];
                vm.ue.setContent('');
                $("#bjEdit").editable("destroy")
                $.get(baseURL + "offer/goodsoffer/info/" + id, function (r) {
                    vm.goodsOffer = r.goodsOffer;
                    // vm.images = vm.goodsOffer.images;
                    // vm.goodsOffer.goodsPodProvince = r.goodsOffer.provinceCode;
                    // vm.goodsOffer.goodsPodCity = r.goodsOffer.cityCode;
                    vm.ue.setContent(UE.utils.html(vm.goodsOffer.goodsDescribe));
                    var row = r.goodsOffer.commoditys;
                    row.forEach(function (item) {
                        item.itemName = item.commodityName;
                        item.id = item.commodityId;
                        item.itemCode = item.commodityNumber;
                        item.factoryNo = item.commodityFactoryNumber;
                    })
                    var goodsPodCity = vm.goodsOffer.goodsPodCity;
                    vm.goodsOffer.goodsPodCity = null;
                    vm.provincesChange(null, null, vm.goodsOffer.goodsPodProvince);
                    setTimeout(function () {
                        vm.goodsOffer.goodsPodCity = goodsPodCity;
                        console.log('goodsPodCity', goodsPodCity)
                    }, 0)
                    $("#commodityTable").bootstrapTable("removeAll");
                    // $("#commodityTable").bootstrapTable("append", row)
                    $("#commodityTable").bootstrapTable("load", {
                        rows: row,
                        total: row == null ? 0 : row.length
                    });
                    if (vm.goodsCount == row.length) {
                        vm.goodsCount = row.length + 1;
                    } else {
                        vm.goodsCount = row.length;
                    }
                    vm.showList = false;
                });
            }
            ,
            reload: function (event) {
                vm.showList = true;
                vm.title = "";
                vm.goodsOffer = {images: []};
                vm.images.length = 0;
                vm.goodsOffer.goodsType = 0;
                vm.ue.setContent('');
                $("#bjEdit").editable("destroy")
                $("#commodityTable").bootstrapTable("removeAll");
                //刷新 如需条件查询common.js
                $("#table").BTF5(vm.params);
                $("form").RF();
            }
            ,
            provincesChange: function (index, child, value) {
                var str = JSON.stringify(window.getCities(value));
                str = str.replace(/id/g, "value").replace(/city/g, "label");
                console.log("加载数据")
                this.cities = JSON.parse(str);
            }
            ,
            showCommodity: function () {
                $(".form").data("bootstrapValidator").validateField("goodsType");
                var type = $(".form").data("bootstrapValidator").isValidField("goodsType");
                if (!type) {
                    alert("请选择商品类型");
                    return;
                }
                $(".form").data("bootstrapValidator").validateField("goodsPrice");
                var price = $(".form").data("bootstrapValidator").isValidField("goodsPrice");
                if (!price) {
                    alert("请填写单价");
                    return;
                }
                $(".form").data("bootstrapValidator").validateField("priceUnit");
                var unitP = $(".form").data("bootstrapValidator").isValidField("priceUnit");
                if (!unitP) {
                    alert("请选择价格单位");
                    return;
                }
                if (vm.goodsOffer.goodsType == 1) {    //现货
                    $(".form").data("bootstrapValidator").validateField("goodsCount");
                    var count = $(".form").data("bootstrapValidator").isValidField("goodsCount");
                    if (!count) {
                        alert("请填写报盘数量");
                        return;
                    }
                    $(".form").data("bootstrapValidator").validateField("goodsUnit");
                    var unitC = $(".form").data("bootstrapValidator").isValidField("goodsUnit");
                    if (!unitC) {
                        alert("请选择数量单位");
                        return;
                    }
                    if (vm.goodsOffer.goodsUnit == 3 && (vm.goodsOffer.priceUnit == 1 || vm.goodsOffer.priceUnit == 3)) {
                        alert("报盘数量单位请与价格单位保持一致");
                        vm.goodsOffer.goodsUnit = 2;
                        return;
                    } else if (vm.goodsOffer.goodsUnit == 2 && (vm.goodsOffer.priceUnit == 2 || vm.goodsOffer.priceUnit == 4)) {
                        alert("报盘数量单位请与价格单位保持一致");
                        vm.goodsOffer.goodsUnit = 3;
                        return;
                    }
                }
                // if (this.goodsOffer.goodsType == 0) {
                //     $(".form").data("bootstrapValidator").validateField("goodsCsc");
                //     str = $(".form").data("bootstrapValidator").isValidField("goodsCsc");
                //     if (!str) {
                //         alert("请选择可否拼柜");
                //         return;
                //     }
                // }
                var commoditys = $("#commodityTable").bootstrapTable("getData");
                // if (this.goodsOffer.goodsType == 0) {
                //     //期货
                //     if (this.goodsOffer.goodsCsc == 0) {
                //         if (commoditys.length >= 1) {
                //             alert("您选择的是不可拼柜，只能添加一条商品");
                //             return;
                //         }
                //     }
                // } else {
                //     //现货
                //     if (commoditys.length >= 1) {
                //         alert("您选择的商品类型是现货,只能添加一条商品信息");
                //         return;
                //     }
                // }
                if (commoditys.length >= 1) {
                    alert("只能添加一条商品信息");
                    return;
                }
                $("#categoryData").bootstrapTable("uncheckAll");
                $("#myModal").modal("show")
            }
            ,
            removeCommodity: function () {
                var commoditys = $("#commodityTable").bootstrapTable("getData");
                var select = $("#commodityTable").bootstrapTable("getSelections");
                if (!select.length) {
                    alert("请选择一条记录");
                    return;
                }
                select.forEach(function (item) {
                    commoditys.splice(commoditys.indexOf(item), 1);
                });
                // $("#commodityTable").bootstrapTable("load", commoditys);
                $("#commodityTable").bootstrapTable("load", {
                    rows: commoditys,
                    total: commoditys == null ? 0 : commoditys.length
                })
            }
            ,
//添加商品查询

            goodsQuery: function () {
                $("#categoryData").bootstrapTable("destroy")
                $("#categoryData").BT({
                    url: baseURL + 'goods/info/list',
                    columns: [
                        {checkbox: true},
                        {title: vm.$t("commodityCode"), field: 'itemCode'},
                        {title: vm.$t('productName'), field: 'itemName'},
                        {title: vm.$t('Categories') + "Id", field: 'categoryId', visible: false},
                        //{title: '产地ID', field: 'places', visible: false},
                        {title: vm.$t('Categories'), field: 'categoryName'},
                        {title: vm.$t('place'), field: 'goodsCount'},
                        {title: vm.$t('FactoryNumber'), field: 'factoryNo'},
                        {title: vm.$t('TaxNumber'), field: 'goodsCount'},
                        {title: vm.$t('TariffPricing'), field: 'pricingMethod'},
                        {title: vm.$t('numerical'), field: 'goodsCount'},
                    ],
                    //条件查询
                    queryParams: vm.params,
                    onLoadSuccess: function () {
                        setTimeout(function () {
                            $('#categoryData').bootstrapTable('resetView');
                        }, 0)
                    }
                });
            }
            ,
            getCategorys: function () {
                $.ajax({
                    url: '/goods/category/list',
                    success: function (r) {
                        if (r.code == 0) {
                            var str = JSON.stringify(r.rows);
                            str = str.replace(/id/g, "value").replace(/categoryName/g, "label");
                            vm.categorys = JSON.parse(str);
                        }
                    }
                })
            }
        },
        created: function () {
            var str = JSON.stringify(window.getProvinces());
            str = str.replace(/provinceid/g, "value").replace(/province/g, "label");
            this.provinces = JSON.parse(str);
            //this.goodsUnits.push(this.bang)
            //this.getCategorys();
            this.getPromotions();
            // this.getCommodityWeightUnit();
            this.goodsStatus.push({value: 0, label: this.$t('LowerShelves')})
            this.goodsStatus.push({value: 1, label: this.$t('UpperShelves')});
        }
        ,
        watch: {
            "$i18n.locale":

                function (value) {
                    if (value == 'en') {
                        $("#table").bootstrapTable.defaults.locale = "en-US";
                    } else {
                        $("#table").bootstrapTable.defaults.locale = "zh-CN";
                    }
                    //this.initTable();
                    var options = $("#table").bootstrapTable('getOptions');
                    options.columns = options.columns;
                    $("#table").bootstrapTable('refreshOptions', {'options': options})
                    // this.initDateTime();
                }

            ,
            "getBj":

                function () {
                    $("#bjEdit").editable({
                        emptytext: '请输入报价',
                        defaultValue: 1,
                        validate: function (v) {
                            if (!v) return '报价不能为空';
                            if (isNaN(v)) return '请输入正确的报价';
                            if (parseInt(v) < 0) return '报价不能小于0';

                        }
                    })
                }

            ,
            'unitPrice':

                function (value) {
                    // vm.goodsOffer.goodsPrice = value.commodityPrice * value.commodityCount;
                    console.log(vm.goodsOffer.goodsPrice)
                }

            ,
            "goodsOffer.goodsType":

                function (value, old) {
                    if (value == 1) {
                        //现货
                        $("form").bootstrapValidator('enableFieldValidators', 'goodsPodProvince', true);
                        $("form").bootstrapValidator('enableFieldValidators', 'goodsPodCity', true);
                        $("form").bootstrapValidator('enableFieldValidators', 'goodsSname', true);
                        $("form").bootstrapValidator('enableFieldValidators', 'goodsSailingTime', false);
                        $("form").bootstrapValidator('enableFieldValidators', 'goodsCsc', false);
                        //vm.goodsUnits.splice(vm.goodsUnits.indexOf(vm.bang), 1);
                    } else {
                        //期货
                        $("form").bootstrapValidator('enableFieldValidators', 'goodsPodProvince', false);
                        $("form").bootstrapValidator('enableFieldValidators', 'goodsPodCity', false);
                        $("form").bootstrapValidator('enableFieldValidators', 'goodsSname', false);
                        $("form").bootstrapValidator('enableFieldValidators', 'goodsSailingTime', true);
                        $("form").bootstrapValidator('enableFieldValidators', 'goodsCsc', true);
                        /* if (vm.goodsUnits.indexOf(vm.bang) == -1) {
                             vm.goodsUnits.push(vm.bang);
                         }*/
                    }
                    // $(vm.$refs.goodsUnits.$el).selectpicker('val', vm.goodsOffer.goodsUnit)
                    $(".form").RF();
                }
        }
        ,
        watch: {
            "$i18n.locale":

                function (value) {
                    if (value == 'en') {
                        $("#table1").bootstrapTable.defaults.locale = "en-US";
                        $("#table2").bootstrapTable.defaults.locale = "en-US";
                    } else {
                        $("#table1").bootstrapTable.defaults.locale = "zh-CN";
                        $("#table2").bootstrapTable.defaults.locale = "zh-CN";
                    }
                    $("#table1").bootstrapTable("destroy");
                    $("#table2").bootstrapTable("destroy");
                    this.initTable();
                }

            ,
        }
    })
;