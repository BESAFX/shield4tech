app.controller('customerDetailsCtrl', [
    'CustomerService',
    'ModalProvider',
    '$scope',
    '$rootScope',
    '$timeout',
    '$log',
    '$uibModalInstance',
    '$uibModal',
    'customer',
    function (
        CustomerService,
        ModalProvider,
        $scope,
        $rootScope,
        $timeout,
        $log,
        $uibModalInstance,
        $uibModal,
        customer
    ) {

        $scope.customer = customer;

        $scope.refreshCustomer = function () {
            CustomerService.findOne($scope.customer.id).then(function (data) {
                $scope.customer = data;
            })
        };

        $scope.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };

        $timeout(function () {
            $scope.refreshCustomer();
            window.componentHandler.upgradeAllRegistered();
        }, 600);

    }]);