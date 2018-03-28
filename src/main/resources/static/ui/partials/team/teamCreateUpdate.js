app.controller('teamCreateUpdateCtrl', [
    'TeamService',
    '$scope',
    '$rootScope',
    '$timeout',
    '$log',
    '$uibModalInstance',
    'title',
    'action',
    'team',
    function (TeamService, $scope, $rootScope, $timeout, $log, $uibModalInstance, title, action, team) {

        $scope.title = title;

        $scope.action = action;

        $scope.roles = [];

        //////////////////////////Company////////////////////////////////
        $scope.roles.push({
            nameArabic: 'تعديل بيانات الشركة',
            nameEnglish: 'Update Company Information',
            value: 'ROLE_COMPANY_UPDATE',
            selected: false,
            category: $rootScope.lang === 'AR' ? 'الإدارة' : 'Administrator'
        });
        //////////////////////////Employee////////////////////////////////
        $scope.roles.push({
            nameArabic: 'إنشاء حسابات الموظفين',
            nameEnglish: 'Create Employees Account',
            value: 'ROLE_EMPLOYEE_CREATE',
            selected: false,
            category: $rootScope.lang === 'AR' ? 'الموارد البشرية' : 'HR'
        });
        $scope.roles.push({
            nameArabic: 'تعديل حسابات الموظفين',
            nameEnglish: 'Update Employees Account',
            value: 'ROLE_EMPLOYEE_UPDATE',
            selected: false,
            category: $rootScope.lang === 'AR' ? 'الموارد البشرية' : 'HR'
        });
        $scope.roles.push({
            nameArabic: 'حذف حسابات الموظفين',
            nameEnglish: 'Delete Employees Account',
            value: 'ROLE_EMPLOYEE_DELETE',
            selected: false,
            category: $rootScope.lang === 'AR' ? 'الموارد البشرية' : 'HR'
        });
        //////////////////////////Vacation Type////////////////////////////////
        $scope.roles.push({
            nameArabic: 'إنشاء بنود الاجازات',
            nameEnglish: 'Create Vacation Types',
            value: 'ROLE_VACATION_TYPE_CREATE',
            selected: false,
            category: $rootScope.lang === 'AR' ? 'الموارد البشرية' : 'HR'
        });
        $scope.roles.push({
            nameArabic: 'تعديل بنود الاجازات',
            nameEnglish: 'Update Vacation Types',
            value: 'ROLE_VACATION_TYPE_UPDATE',
            selected: false,
            category: $rootScope.lang === 'AR' ? 'الموارد البشرية' : 'HR'
        });
        $scope.roles.push({
            nameArabic: 'حذف بنود الاجازات',
            nameEnglish: 'Delete Vacation Types',
            value: 'ROLE_VACATION_TYPE_DELETE',
            selected: false,
            category: $rootScope.lang === 'AR' ? 'الموارد البشرية' : 'HR'
        });
        //////////////////////////Vacation////////////////////////////////
        $scope.roles.push({
            nameArabic: 'إنشاء الاجازات',
            nameEnglish: 'Create Vacations',
            value: 'ROLE_VACATION_CREATE',
            selected: false,
            category: $rootScope.lang === 'AR' ? 'الموارد البشرية' : 'HR'
        });
        $scope.roles.push({
            nameArabic: 'تعديل الاجازات',
            nameEnglish: 'Update Vacations',
            value: 'ROLE_VACATION_UPDATE',
            selected: false,
            category: $rootScope.lang === 'AR' ? 'الموارد البشرية' : 'HR'
        });
        $scope.roles.push({
            nameArabic: 'حذف الاجازات',
            nameEnglish: 'Delete Vacations',
            value: 'ROLE_VACATION_DELETE',
            selected: false,
            category: $rootScope.lang === 'AR' ? 'الموارد البشرية' : 'HR'
        });
        //////////////////////////Deduction Type////////////////////////////////
        $scope.roles.push({
            nameArabic: 'إنشاء بنود الاستقطاعات',
            nameEnglish: 'Create Deduction Types',
            value: 'ROLE_DEDUCTION_TYPE_CREATE',
            selected: false,
            category: $rootScope.lang === 'AR' ? 'الموارد البشرية' : 'HR'
        });
        $scope.roles.push({
            nameArabic: 'تعديل بنود الاستقطاعات',
            nameEnglish: 'Update Deduction Types',
            value: 'ROLE_DEDUCTION_TYPE_UPDATE',
            selected: false,
            category: $rootScope.lang === 'AR' ? 'الموارد البشرية' : 'HR'
        });
        $scope.roles.push({
            nameArabic: 'حذف بنود الاستقطاعات',
            nameEnglish: 'Delete Deduction Types',
            value: 'ROLE_DEDUCTION_TYPE_DELETE',
            selected: false,
            category: $rootScope.lang === 'AR' ? 'الموارد البشرية' : 'HR'
        });
        //////////////////////////Deduction////////////////////////////////
        $scope.roles.push({
            nameArabic: 'إنشاء الاستقطاعات',
            nameEnglish: 'Create Deductions',
            value: 'ROLE_DEDUCTION_CREATE',
            selected: false,
            category: $rootScope.lang === 'AR' ? 'الموارد البشرية' : 'HR'
        });
        $scope.roles.push({
            nameArabic: 'تعديل الاستقطاعات',
            nameEnglish: 'Update Deductions',
            value: 'ROLE_DEDUCTION_UPDATE',
            selected: false,
            category: $rootScope.lang === 'AR' ? 'الموارد البشرية' : 'HR'
        });
        $scope.roles.push({
            nameArabic: 'حذف الاستقطاعات',
            nameEnglish: 'Delete Deductions',
            value: 'ROLE_DEDUCTION_DELETE',
            selected: false,
            category: $rootScope.lang === 'AR' ? 'الموارد البشرية' : 'HR'
        });
        ///////////////////////// Salary////////////////////////////////
        $scope.roles.push({
            nameArabic: 'إنشاء الرواتب',
            nameEnglish: 'Create Salaries',
            value: 'ROLE_SALARY_CREATE',
            selected: false,
            category: $rootScope.lang === 'AR' ? 'الموارد البشرية' : 'HR'
        });
        $scope.roles.push({
            nameArabic: 'تعديل الرواتب',
            nameEnglish: 'Update Salaries',
            value: 'ROLE_SALARY_UPDATE',
            selected: false,
            category: $rootScope.lang === 'AR' ? 'الموارد البشرية' : 'HR'
        });
        $scope.roles.push({
            nameArabic: 'حذف الرواتب',
            nameEnglish: 'Delete Salaries',
            value: 'ROLE_SALARY_DELETE',
            selected: false,
            category: $rootScope.lang === 'AR' ? 'الموارد البشرية' : 'HR'
        });
        //////////////////////////Customer////////////////////////////////
        $scope.roles.push({
            nameArabic: 'إنشاء حسابات العملاء',
            nameEnglish: 'Create Customers Account',
            value: 'ROLE_CUSTOMER_CREATE',
            selected: false,
            category: $rootScope.lang === 'AR' ? 'الإدارة' : 'Administrator'
        });
        $scope.roles.push({
            nameArabic: 'تعديل حسابات العملاء',
            nameEnglish: 'Update Customers Account',
            value: 'ROLE_CUSTOMER_UPDATE',
            selected: false,
            category: $rootScope.lang === 'AR' ? 'الإدارة' : 'Administrator'
        });
        $scope.roles.push({
            nameArabic: 'تفعيل حسابات العملاء',
            nameEnglish: 'Enable Customers Account',
            value: 'ROLE_CUSTOMER_ENABLE',
            selected: false,
            category: $rootScope.lang === 'AR' ? 'الإدارة' : 'Administrator'
        });
        $scope.roles.push({
            nameArabic: 'تعطيل حسابات العملاء',
            nameEnglish: 'Disable Customers Account',
            value: 'ROLE_CUSTOMER_DISABLE',
            selected: false,
            category: $rootScope.lang === 'AR' ? 'الإدارة' : 'Administrator'
        });
        $scope.roles.push({
            nameArabic: 'حذف حسابات العملاء',
            nameEnglish: 'Delete Customers Account',
            value: 'ROLE_CUSTOMER_DELETE',
            selected: false,
            category: $rootScope.lang === 'AR' ? 'الإدارة' : 'Administrator'
        });
        //////////////////////////Supplier////////////////////////////////
        $scope.roles.push({
            nameArabic: 'إنشاء حسابات الموردين',
            nameEnglish: 'Create Suppliers Account',
            value: 'ROLE_SUPPLIER_CREATE',
            selected: false,
            category: $rootScope.lang === 'AR' ? 'الإدارة' : 'Administrator'
        });
        $scope.roles.push({
            nameArabic: 'تعديل حسابات الموردين',
            nameEnglish: 'Update Suppliers Account',
            value: 'ROLE_SUPPLIER_UPDATE',
            selected: false,
            category: $rootScope.lang === 'AR' ? 'الإدارة' : 'Administrator'
        });
        $scope.roles.push({
            nameArabic: 'تفعيل حسابات الموردين',
            nameEnglish: 'Enable Supplier Account',
            value: 'ROLE_SUPPLIER_ENABLE',
            selected: false,
            category: $rootScope.lang === 'AR' ? 'الإدارة' : 'Administrator'
        });
        $scope.roles.push({
            nameArabic: 'تعطيل حسابات الموردين',
            nameEnglish: 'Disable Supplier Account',
            value: 'ROLE_SUPPLIER_DISABLE',
            selected: false,
            category: $rootScope.lang === 'AR' ? 'الإدارة' : 'Administrator'
        });
        $scope.roles.push({
            nameArabic: 'حذف حسابات الموردين',
            nameEnglish: 'Delete Suppliers Account',
            value: 'ROLE_SUPPLIER_DELETE',
            selected: false,
            category: $rootScope.lang === 'AR' ? 'الإدارة' : 'Administrator'
        });
        //////////////////////////Product////////////////////////////////
        $scope.roles.push({
            nameArabic: 'إنشاء الاصناف',
            nameEnglish: 'Create Products',
            value: 'ROLE_PRODUCT_CREATE',
            selected: false,
            category: $rootScope.lang === 'AR' ? 'المخزون' : 'Stock'
        });
        $scope.roles.push({
            nameArabic: 'تعديل بيانات الاصناف',
            nameEnglish: 'Update Products Info.',
            value: 'ROLE_PRODUCT_UPDATE',
            selected: false,
            category: $rootScope.lang === 'AR' ? 'المخزون' : 'Stock'
        });
        $scope.roles.push({
            nameArabic: 'حذف الاصناف',
            nameEnglish: 'Delete Products',
            value: 'ROLE_PRODUCT_DELETE',
            selected: false,
            category: $rootScope.lang === 'AR' ? 'المخزون' : 'Stock'
        });
        //////////////////////////Product Category////////////////////////////////
        $scope.roles.push({
            nameArabic: 'إنشاء أنواع الاصناف',
            nameEnglish: 'Create Product Categories',
            value: 'ROLE_PRODUCT_CATEGORY_CREATE',
            selected: false,
            category: $rootScope.lang === 'AR' ? 'المخزون' : 'Stock'
        });
        $scope.roles.push({
            nameArabic: 'تعديل بيانات أنواع الاصناف',
            nameEnglish: 'Update Product Categories Info.',
            value: 'ROLE_PRODUCT_CATEGORY_UPDATE',
            selected: false,
            category: $rootScope.lang === 'AR' ? 'المخزون' : 'Stock'
        });
        $scope.roles.push({
            nameArabic: 'حذف أنواع الاصناف',
            nameEnglish: 'Delete Product Categories',
            value: 'ROLE_PRODUCT_CATEGORY_DELETE',
            selected: false,
            category: $rootScope.lang === 'AR' ? 'المخزون' : 'Stock'
        });
        //////////////////////////Warehouse////////////////////////////////
        $scope.roles.push({
            nameArabic: 'إنشاء المخازن',
            nameEnglish: 'Create Warehouses',
            value: 'ROLE_WAREHOUSE_CREATE',
            selected: false,
            category: $rootScope.lang === 'AR' ? 'المخزون' : 'Stock'
        });
        $scope.roles.push({
            nameArabic: 'تعديل بيانات المخازن',
            nameEnglish: 'Update Warehouses Info.',
            value: 'ROLE_WAREHOUSE_UPDATE',
            selected: false,
            category: $rootScope.lang === 'AR' ? 'المخزون' : 'Stock'
        });
        $scope.roles.push({
            nameArabic: 'حذف المخازن',
            nameEnglish: 'Delete Warehouses',
            value: 'ROLE_WAREHOUSE_DELETE',
            selected: false,
            category: $rootScope.lang === 'AR' ? 'المخزون' : 'Stock'
        });
        //////////////////////////Order Buy////////////////////////////////
        $scope.roles.push({
            nameArabic: 'إنشاء طلبات الشراء',
            nameEnglish: 'Create Order Purchases',
            value: 'ROLE_ORDER_BUY_CREATE',
            selected: false,
            category: $rootScope.lang === 'AR' ? 'المشتريات' : 'Purchases'
        });
        $scope.roles.push({
            nameArabic: 'حذف طلبات الشراء',
            nameEnglish: 'Delete Order Purchases',
            value: 'ROLE_ORDER_BUY_DELETE',
            selected: false,
            category: $rootScope.lang === 'AR' ? 'المشتريات' : 'Purchases'
        });
        //////////////////////////Order Sell////////////////////////////////
        $scope.roles.push({
            nameArabic: 'إنشاء طلبات البيع',
            nameEnglish: 'Create Order Sell',
            value: 'ROLE_ORDER_SELL_CREATE',
            selected: false,
            category: $rootScope.lang === 'AR' ? 'المبيعات' : 'Sales'
        });
        $scope.roles.push({
            nameArabic: 'حذف طلبات البيع',
            nameEnglish: 'Delete Order Sell',
            value: 'ROLE_ORDER_SELL_DELETE',
            selected: false,
            category: $rootScope.lang === 'AR' ? 'المبيعات' : 'Sales'
        });
        //////////////////////////Bill Buy////////////////////////////////
        $scope.roles.push({
            nameArabic: 'إنشاء فواتير الشراء',
            nameEnglish: 'Create Bill Purchases',
            value: 'ROLE_BILL_BUY_CREATE',
            selected: false,
            category: $rootScope.lang === 'AR' ? 'المشتريات' : 'Purchases'
        });
        $scope.roles.push({
            nameArabic: 'حذف فواتير الشراء',
            nameEnglish: 'Delete Bill Purchases',
            value: 'ROLE_BILL_BUY_DELETE',
            selected: false,
            category: $rootScope.lang === 'AR' ? 'المشتريات' : 'Purchases'
        });
        //////////////////////////Bill Sell////////////////////////////////
        $scope.roles.push({
            nameArabic: 'إنشاء فواتير البيع',
            nameEnglish: 'Create Bill Sell',
            value: 'ROLE_BILL_SELL_CREATE',
            selected: false,
            category: $rootScope.lang === 'AR' ? 'المبيعات' : 'Sales'
        });
        $scope.roles.push({
            nameArabic: 'حذف فواتير البيع',
            nameEnglish: 'Delete Bill Sell',
            value: 'ROLE_BILL_SELL_DELETE',
            selected: false,
            category: $rootScope.lang === 'AR' ? 'المبيعات' : 'Sales'
        });
        //////////////////////////Team////////////////////////////////
        $scope.roles.push({
            nameArabic: 'إنشاء الصلاحيات',
            nameEnglish: 'Create Privileges',
            value: 'ROLE_TEAM_CREATE',
            selected: false,
            category: $rootScope.lang === 'AR' ? 'الإدارة' : 'Administrator'
        });
        $scope.roles.push({
            nameArabic: 'تعديل الصلاحيات',
            nameEnglish: 'Update Privileges',
            value: 'ROLE_TEAM_UPDATE',
            selected: false,
            category: $rootScope.lang === 'AR' ? 'الإدارة' : 'Administrator'
        });
        $scope.roles.push({
            nameArabic: 'حذف الصلاحيات',
            nameEnglish: 'Delete Privileges',
            value: 'ROLE_TEAM_DELETE',
            selected: false,
            category: $rootScope.lang === 'AR' ? 'الإدارة' : 'Administrator'
        });

        if (team) {
            $scope.team = team;
            if (typeof team.authorities === 'string') {
                $scope.team.authorities = team.authorities.split(',');
            }
            //
            angular.forEach($scope.team.authorities, function (auth) {
                angular.forEach($scope.roles, function (role) {
                    if (role.value === auth) {
                        return role.selected = true;
                    }
                })
            });
        } else {
            $scope.team = {};
        }

        $scope.submit = function () {
            $scope.team.authorities = [];
            angular.forEach($scope.roles, function (role) {
                if (role.selected) {
                    $scope.team.authorities.push(role.value);
                }
            });
            $scope.team.authorities = $scope.team.authorities.join();
            switch ($scope.action) {
                case 'create' :
                    TeamService.create($scope.team).then(function (data) {
                        $uibModalInstance.close(data);
                    });
                    break;
                case 'update' :
                    TeamService.update($scope.team).then(function (data) {
                        $scope.team = data;
                        $scope.team.authorities = team.authorities.split(',');
                    });
                    break;
            }
        };

        $scope.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };

        $timeout(function () {
            window.componentHandler.upgradeAllRegistered();
        }, 600);

    }]);