app.controller("menuCtrl", [
    '$scope',
    '$rootScope',
    '$state',
    '$timeout',
    '$uibModal',
    'ModalProvider',
    'CompanyService',
    'CustomerService',
    'SupplierService',
    'ProductCategoryService',
    'TeamService',
    'PersonService',
    function (
        $scope,
        $rootScope,
        $state,
        $timeout,
        $uibModal,
        ModalProvider,
        CompanyService,
        CustomerService,
        SupplierService,
        ProductCategoryService,
        TeamService,
        PersonService
    ) {

    /**************************************************************************************************************
     *                                                                                                            *
     * General                                                                                                    *
     *                                                                                                            *
     **************************************************************************************************************/
    $timeout(function () {
        CompanyService.get().then(function (data) {
            $scope.selectedCompany = data;
        });
        window.componentHandler.upgradeAllRegistered();
    }, 600);
    $scope.$watch('toggleState', function (newValue, oldValue) {
        switch (newValue) {
            case 'menu': {
                $scope.pageTitle = $rootScope.lang==='AR' ? 'القائمة' : 'Menu';
                $scope.MDLIcon = 'widgets';
                break;
            }
            case 'company': {
                $scope.pageTitle = $rootScope.lang==='AR' ? 'الشركة' : 'Company';
                $scope.MDLIcon = 'store';
                break;
            }
            case 'customer': {
                $scope.pageTitle = $rootScope.lang==='AR' ? 'العملاء' : 'Customers';
                $scope.MDLIcon = 'account_circle';
                break;
            }
            case 'supplier': {
                $scope.pageTitle = $rootScope.lang==='AR' ? 'الموردين' : 'Suppliers';
                $scope.MDLIcon = 'account_box';
                break;
            }
            case 'productCategory': {
                $scope.pageTitle = $rootScope.lang==='AR' ? 'التصنيفات' : 'Categories';
                $scope.MDLIcon = 'stars';
                break;
            }
            case 'product': {
                $scope.pageTitle = $rootScope.lang==='AR' ? 'الاصناف' : 'Products';
                $scope.MDLIcon = 'archive';
                break;
            }
            case 'warehouse': {
                $scope.pageTitle = $rootScope.lang==='AR' ? 'المخازن' : 'Warehouses';
                $scope.MDLIcon = 'store';
                break;
            }
            case 'team': {
                $scope.pageTitle = $rootScope.lang==='AR' ? 'الصلاحيات' : 'Privileges';
                $scope.MDLIcon = 'security';
                break;
            }
            case 'profile': {
                $scope.pageTitle = $rootScope.lang==='AR' ? 'الملف الشخصي' : 'Profile';
                $scope.MDLIcon = 'account_circle';
                break;
            }
            case 'help': {
                $scope.pageTitle = $rootScope.lang==='AR' ? 'مساعدة' : 'Help';
                $scope.MDLIcon = 'help';
                break;
            }
            case 'about': {
                $scope.pageTitle = $rootScope.lang==='AR' ? 'عن البرنامج' : 'About';
                $scope.MDLIcon = 'info';
                break;
            }
            case 'report': {
                $scope.pageTitle = $rootScope.lang==='AR' ? 'التقارير' : 'Reports';
                $scope.MDLIcon = 'print';
                break;
            }
        }
        $timeout(function () {
            window.componentHandler.upgradeAllRegistered();
        }, 500);
    }, true);
    $scope.toggleState = 'menu';
    $scope.openStateMenu = function () {
        $scope.toggleState = 'menu';
    };
    $scope.openStateCompany = function () {
        $scope.toggleState = 'company';
    };
    $scope.openStateCustomer = function () {
        $scope.toggleState = 'customer';
    };
    $scope.openStateSupplier = function () {
        $scope.toggleState = 'supplier';
    };
    $scope.openStateProductCategory = function () {
        $scope.toggleState = 'productCategory';
        $timeout(function () {
            $scope.fetchProductCategoryTableData();
        }, 500);
    };
    $scope.openStateProduct = function () {
        $scope.toggleState = 'product';
    };
    $scope.openStateWarehouse = function () {
        $scope.toggleState = 'warehouse';
    };
    $scope.openStateEmployee = function () {
        $scope.toggleState = 'employee';
    };
    $scope.openStateTeam = function () {
        $scope.toggleState = 'team';
        $timeout(function () {
            $scope.fetchTeamTableData();
        }, 500);
    };
    $scope.openStateProfile = function () {
        $scope.toggleState = 'profile';
    };
    $scope.openStateHelp = function () {
        $scope.toggleState = 'help';
    };
    $scope.openStateAbout = function () {
        $scope.toggleState = 'about';
    };
    $scope.openStateReport = function () {
        $scope.toggleState = 'report';
    };

    /**************************************************************************************************************
     *                                                                                                            *
     * Company                                                                                                    *
     *                                                                                                            *
     **************************************************************************************************************/
    $scope.selectedCompany = {};
    $scope.submitCompany = function () {
        CompanyService.update($scope.selectedCompany).then(function (data) {
            $scope.selectedCompany = data;
        });
    };
    $scope.browseCompanyLogo = function () {
        document.getElementById('uploader-company').click();
    };
    $scope.uploadCompanyLogo = function (files) {
        CompanyService.uploadCompanyLogo(files[0]).then(function (data) {
            $scope.selectedCompany.logo = data;
        });
    };

    /**************************************************************************************************************
     *                                                                                                            *
     * Customer                                                                                                   *
     *                                                                                                            *
     **************************************************************************************************************/
    $scope.customers = [];
    $scope.paramCustomer = {};
    $scope.findAllCustomers = function () {
        CustomerService.findAll().then(function (data) {
            $scope.customers = data;
        });
    };
    $scope.filterCustomers = function () {
        var search = [];
        if ($scope.paramCustomer.code) {
            search.push('code=');
            search.push($scope.paramCustomer.code);
            search.push('&');
        }
        if ($scope.paramCustomer.name) {
            search.push('name=');
            search.push($scope.paramCustomer.name);
            search.push('&');
        }
        if ($scope.paramCustomer.mobile) {
            search.push('mobile=');
            search.push($scope.paramCustomer.mobile);
            search.push('&');
        }
        if ($scope.paramCustomer.identityNumber) {
            search.push('identityNumber=');
            search.push($scope.paramCustomer.identityNumber);
            search.push('&');
        }
        if ($scope.paramCustomer.email) {
            search.push('email=');
            search.push($scope.paramCustomer.email);
            search.push('&');
        }
        CustomerService.filter(search.join("")).then(function (data) {
            $scope.customers = data;
        });
    };
    $scope.openFilterCustomers = function () {
        var modalInstance = $uibModal.open({
            animation: true,
            ariaLabelledBy: 'modal-title',
            ariaDescribedBy: 'modal-body',
            templateUrl: '/ui/partials/customer/customerFilter.html',
            controller: 'customerFilterCtrl',
            scope: $scope,
            backdrop: 'static',
            keyboard: false
        });

        modalInstance.result.then(function (paramCustomer) {

            $scope.paramCustomer = paramCustomer;

            $scope.filterCustomers();

        }, function () {});
    };
    $scope.deleteCustomer = function (customer) {
        ModalProvider.openConfirmModel('العملاء', 'account_circle', 'هل تود حذف العميل وكل ما يتعلق به من حسابات فعلاً؟').result.then(function (response) {
            if(response){
                CustomerService.remove(customer.id).then(function () {
                    var index = $scope.customers.indexOf(customer);
                    return $scope.customers.splice(index, 1);
                });
            }
        });
    };
    $scope.newCustomer = function () {
        ModalProvider.openCustomerCreateModel().result.then(function (data) {
            return $scope.customers.splice(0, 0, data);
        }, function () {});
    };
    $scope.enableCustomer = function (customer) {
        CustomerService.enable(customer).then(function () {
            customer.enabledInArabic = data.enabledInArabic;
            customer.enabledInEnglish = data.enabledInEnglish;
            return customer;
        });
    };
    $scope.disableCustomer = function (customer) {
        CustomerService.disable(customer).then(function () {
            customer.enabledInArabic = data.enabledInArabic;
            customer.enabledInEnglish = data.enabledInEnglish;
            return customer;
        });
    };
    $scope.rowMenuCustomers = [
        {
            html: '<div class="drop-menu">انشاء عميل جديد<span class="fa fa-pencil fa-lg"></span></div>',
            enabled: function () {
                return $rootScope.contains($rootScope.me.team.authorities, ['ROLE_CUSTOMER_CREATE']);
            },
            click: function ($itemScope, $event, value) {
                $scope.newCustomer();
            }
        },
        {
            html: '<div class="drop-menu">تعديل بيانات العميل<span class="fa fa-edit fa-lg"></span></div>',
            enabled: function () {
                return $rootScope.contains($rootScope.me.team.authorities, ['ROLE_CUSTOMER_UPDATE']);
            },
            click: function ($itemScope, $event, value) {
                ModalProvider.openCustomerUpdateModel($itemScope.customer);
            }
        },
        {
            html: '<div class="drop-menu">تفعيل العميل<span class="fa fa-eye fa-lg"></span></div>',
            enabled: function () {
                return $rootScope.contains($rootScope.me.team.authorities, ['ROLE_CUSTOMER_ENABLE']);
            },
            click: function ($itemScope, $event, value) {
                $scope.enableCustomer($itemScope.customer);
            }
        },
        {
            html: '<div class="drop-menu">تعطيل العميل<span class="fa fa-eye-slash fa-lg"></span></div>',
            enabled: function () {
                return $rootScope.contains($rootScope.me.team.authorities, ['ROLE_CUSTOMER_DISABLE']);
            },
            click: function ($itemScope, $event, value) {
                $scope.disableCustomer($itemScope.customer);
            }
        },
        {
            html: '<div class="drop-menu">حذف العميل<span class="fa fa-trash fa-lg"></span></div>',
            enabled: function () {
                return $rootScope.contains($rootScope.me.team.authorities, ['ROLE_CUSTOMER_DELETE']);
            },
            click: function ($itemScope, $event, value) {
                $scope.deleteCustomer($itemScope.customer);
            }
        },
        {
            html: '<div class="drop-menu">التفاصيل<span class="fa fa-info fa-lg"></span></div>',
            enabled: function () {
                return true;
            },
            click: function ($itemScope, $event, value) {
                ModalProvider.openCustomerDetailsModel($itemScope.customer);
            }
        }
    ];

    /**************************************************************************************************************
     *                                                                                                            *
     * Supplier                                                                                                   *
     *                                                                                                            *
     **************************************************************************************************************/
    $scope.suppliers = [];
    $scope.paramSupplier = {};
    $scope.findAllSuppliers = function () {
        SupplierService.findAll().then(function (data) {
            $scope.suppliers = data;
        });
    };
    $scope.filterSuppliers = function () {
        var search = [];
        if ($scope.paramSupplier.code) {
            search.push('code=');
            search.push($scope.paramSupplier.code);
            search.push('&');
        }
        if ($scope.paramSupplier.name) {
            search.push('name=');
            search.push($scope.paramSupplier.name);
            search.push('&');
        }
        if ($scope.paramSupplier.mobile) {
            search.push('mobile=');
            search.push($scope.paramSupplier.mobile);
            search.push('&');
        }
        if ($scope.paramSupplier.identityNumber) {
            search.push('identityNumber=');
            search.push($scope.paramSupplier.identityNumber);
            search.push('&');
        }
        if ($scope.paramSupplier.email) {
            search.push('email=');
            search.push($scope.paramSupplier.email);
            search.push('&');
        }
        SupplierService.filter(search.join("")).then(function (data) {
            $scope.suppliers = data;
        });
    };
    $scope.openFilterSuppliers = function () {
        var modalInstance = $uibModal.open({
            animation: true,
            ariaLabelledBy: 'modal-title',
            ariaDescribedBy: 'modal-body',
            templateUrl: '/ui/partials/supplier/supplierFilter.html',
            controller: 'supplierFilterCtrl',
            scope: $scope,
            backdrop: 'static',
            keyboard: false
        });

        modalInstance.result.then(function (paramSupplier) {

            $scope.paramSupplier = paramSupplier;

            $scope.filterSuppliers();

        }, function () {});
    };
    $scope.deleteSupplier = function (supplier) {
        ModalProvider.openConfirmModel('الموردين', 'store', 'هل تود حذف المورد وكل ما يتعلق به من حسابات فعلاً؟').result.then(function (response) {
            if(response){
                SupplierService.remove(supplier.id).then(function () {
                    var index = $scope.suppliers.indexOf(supplier);
                    return $scope.suppliers.splice(index, 1);
                });
            }
        });
    };
    $scope.newSupplier = function () {
        ModalProvider.openSupplierCreateModel().result.then(function (data) {
            return $scope.suppliers.splice(0, 0, data);
        }, function () {});
    };
    $scope.enableSupplier = function (supplier) {
        SupplierService.enable(supplier).then(function () {
            supplier.enabledInArabic = data.enabledInArabic;
            supplier.enabledInEnglish = data.enabledInEnglish;
            return supplier;
        });
    };
    $scope.disableSupplier = function (supplier) {
        SupplierService.disable(supplier).then(function () {
            supplier.enabledInArabic = data.enabledInArabic;
            supplier.enabledInEnglish = data.enabledInEnglish;
            return supplier;
        });
    };
    $scope.rowMenuSuppliers = [
        {
            html: '<div class="drop-menu">انشاء مورد جديد<span class="fa fa-pencil fa-lg"></span></div>',
            enabled: function () {
                return $rootScope.contains($rootScope.me.team.authorities, ['ROLE_SUPPLIER_CREATE']);
            },
            click: function ($itemScope, $event, value) {
                $scope.newSupplier();
            }
        },
        {
            html: '<div class="drop-menu">تعديل بيانات المورد<span class="fa fa-edit fa-lg"></span></div>',
            enabled: function () {
                return $rootScope.contains($rootScope.me.team.authorities, ['ROLE_SUPPLIER_UPDATE']);
            },
            click: function ($itemScope, $event, value) {
                ModalProvider.openSupplierUpdateModel($itemScope.supplier);
            }
        },
        {
            html: '<div class="drop-menu">تفعيل المورد<span class="fa fa-eye fa-lg"></span></div>',
            enabled: function () {
                return $rootScope.contains($rootScope.me.team.authorities, ['ROLE_SUPPLIER_ENABLE']);
            },
            click: function ($itemScope, $event, value) {
                $scope.enableSupplier($itemScope.supplier);
            }
        },
        {
            html: '<div class="drop-menu">تعطيل المورد<span class="fa fa-eye-slash fa-lg"></span></div>',
            enabled: function () {
                return $rootScope.contains($rootScope.me.team.authorities, ['ROLE_SUPPLIER_DISABLE']);
            },
            click: function ($itemScope, $event, value) {
                $scope.disableSupplier($itemScope.supplier);
            }
        },
        {
            html: '<div class="drop-menu">حذف المورد<span class="fa fa-trash fa-lg"></span></div>',
            enabled: function () {
                return $rootScope.contains($rootScope.me.team.authorities, ['ROLE_SUPPLIER_DELETE']);
            },
            click: function ($itemScope, $event, value) {
                $scope.deleteSupplier($itemScope.supplier);
            }
        },
        {
            html: '<div class="drop-menu">التفاصيل<span class="fa fa-info fa-lg"></span></div>',
            enabled: function () {
                return true;
            },
            click: function ($itemScope, $event, value) {
                ModalProvider.openSupplierDetailsModel($itemScope.supplier);
            }
        }
    ];

    /**************************************************************************************************************
     *                                                                                                            *
     * ProductCategory                                                                                            *
     *                                                                                                            *
     **************************************************************************************************************/
    $scope.productCategories = [];
    $scope.productCategories.checkAll = false;
    $scope.fetchProductCategoryTableData = function () {
        ProductCategoryService.findAll().then(function (data) {
            $scope.productCategories = data;
            $timeout(function () {
                window.componentHandler.upgradeAllRegistered();
            }, 500);
        });
    };
    $scope.newProductCategory = function () {
        ModalProvider.openProductCategoryCreateModel().result.then(function (data) {
            $scope.productCategories.splice(0, 0, data);
        }, function () {
        });
    };
    $scope.deleteProductCategory = function (team) {
        ModalProvider.openConfirmModel(
            $rootScope.lang==='AR' ? "التصنيفات" : "Categories",
            "stars",
            $rootScope.lang==='AR' ? "هل تود حذف المجموعة فعلاً؟" : "Are your really want to delete this item ?").result.then(function (data) {
            if(data){
                ProductCategoryService.remove(team.id).then(function () {
                    var index = $scope.productCategories.indexOf(team);
                    $scope.productCategories.splice(index, 1);
                });
            }
        });
    };
    $scope.rowMenuProductCategory = [
        {
            html: '<div class="drop-menu">' +
            '<img src="/ui/img/add.png" width="24" height="24">' +
            '<span>' + ($rootScope.lang=== 'AR' ? 'جديد...' : 'New...') + '</span>' +
            '</div>',
            enabled: function () {
                return $rootScope.contains($rootScope.me.team.authorities, ['ROLE_PRODUCT_CATEGORY_CREATE']);
            },
            click: function ($itemScope, $event, value) {
                $scope.newProductCategory();
            }
        },
        {
            html: '<div class="drop-menu">' +
            '<img src="/ui/img/edit.png" width="24" height="24">' +
            '<span>' + ($rootScope.lang=== 'AR' ? 'تعديل...' : 'Edit...') + '</span>' +
            '</div>',
            enabled: function () {
                return $rootScope.contains($rootScope.me.team.authorities, ['ROLE_PRODUCT_CATEGORY_UPDATE']);
            },
            click: function ($itemScope, $event, value) {
                ModalProvider.openProductCategoryUpdateModel($itemScope.productCategory);
            }
        },
        {
            html: '<div class="drop-menu">' +
            '<img src="/ui/img/delete.png" width="24" height="24">' +
            '<span>' + ($rootScope.lang=== 'AR' ? 'حذف...' : 'Delete...') + '</span>' +
            '</div>',
            enabled: function () {
                return $rootScope.contains($rootScope.me.team.authorities, ['ROLE_PRODUCT_CATEGORY_DELETE']);
            },
            click: function ($itemScope, $event, value) {
                $scope.deleteProductCategory($itemScope.productCategory);
            }
        }
    ];
    $scope.checkAllProductCategories = function () {
        var elements = document.querySelectorAll('.check');
        for (var i = 0, n = elements.length; i < n; i++) {
            var element = elements[i];
            if ($('#checkAllProductCategories input').is(":checked")) {
                element.MaterialCheckbox.check();
            }
            else {
                element.MaterialCheckbox.uncheck();
            }
        }
        angular.forEach($scope.productCategories, function (productCategory) {
            productCategory.isSelected = $scope.productCategories.checkAll;
        });
    };
    $scope.checkProductCategory = function () {
            var elements = document.querySelectorAll('.check');
            for (var i = 0, n = elements.length; i < n; i++) {
                var element = elements[i];
                if ($('.check input:checked').length == $('.check input').length) {
                    document.querySelector('#checkAllProductCategories').MaterialCheckbox.check();
                } else {
                    document.querySelector('#checkAllProductCategories').MaterialCheckbox.uncheck();
                }
            }
        };

    /**************************************************************************************************************
     *                                                                                                            *
     * Team                                                                                                       *
     *                                                                                                            *
     **************************************************************************************************************/
    $scope.teams = [];
    $scope.fetchTeamTableData = function () {
        TeamService.findAll().then(function (data) {
            $scope.teams = data;
        });
    };
    $scope.newTeam = function () {
        ModalProvider.openTeamCreateModel().result.then(function (data) {
            $scope.teams.splice(0, 0, data);
        }, function () {
        });
    };
    $scope.deleteTeam = function (team) {
        $rootScope.showConfirmNotify("حذف البيانات", "هل تود حذف المجموعة فعلاً؟", "error", "fa-trash", function () {
            TeamService.remove(team.id).then(function () {
                var index = $scope.teams.indexOf(team);
                $scope.teams.splice(index, 1);
            });
        });
    };
    $scope.rowMenuTeam = [
        {
            html: '<div class="drop-menu">انشاء مجموعة جديدة<span class="fa fa-pencil fa-lg"></span></div>',
            enabled: function () {
                return $rootScope.contains($rootScope.me.team.authorities, ['ROLE_TEAM_CREATE']);
            },
            click: function ($itemScope, $event, value) {
                $scope.newTeam();
            }
        },
        {
            html: '<div class="drop-menu">تعديل بيانات المجموعة<span class="fa fa-edit fa-lg"></span></div>',
            enabled: function () {
                return $rootScope.contains($rootScope.me.team.authorities, ['ROLE_TEAM_UPDATE']);
            },
            click: function ($itemScope, $event, value) {
                ModalProvider.openTeamUpdateModel($itemScope.team);
            }
        },
        {
            html: '<div class="drop-menu">حذف المجموعة<span class="fa fa-trash fa-lg"></span></div>',
            enabled: function () {
                return $rootScope.contains($rootScope.me.team.authorities, ['ROLE_TEAM_DELETE']);
            },
            click: function ($itemScope, $event, value) {
                $scope.deleteTeam($itemScope.team);
            }
        }
    ];

    /**************************************************************************************************************
     *                                                                                                            *
     * Profile                                                                                                    *
     *                                                                                                            *
     **************************************************************************************************************/
    $scope.submitProfile = function () {
        PersonService.update($scope.me).then(function (data) {
            $scope.me = data;
        });
    };
    $scope.browseProfilePhoto = function () {
        document.getElementById('uploader-profile').click();
    };
    $scope.uploadPersonPhoto = function (files) {
        PersonService.uploadPersonPhoto(files[0]).then(function (data) {
            $scope.me.photo = data;
        });
    };

}]);