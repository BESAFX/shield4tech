app.controller('supplierDetailsCtrl', [
    'SupplierService',
    'ModalProvider',
    '$scope',
    '$rootScope',
    '$timeout',
    '$log',
    '$uibModalInstance',
    '$uibModal',
    'supplier',
    function (
        SupplierService,
        ModalProvider,
        $scope,
        $rootScope,
        $timeout,
        $log,
        $uibModalInstance,
        $uibModal,
        supplier
    ) {

        $scope.supplier = supplier;

        $scope.refreshSupplier = function () {
            SupplierService.findOne($scope.supplier.id).then(function (data) {
                $scope.supplier = data;
            })
        };

        $scope.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };

        $timeout(function () {
            $scope.refreshSupplier();
            window.componentHandler.upgradeAllRegistered();
        }, 600);

    }]);