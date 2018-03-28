app.controller('productCreateUpdateCtrl', ['ProductService', '$scope', '$rootScope', '$timeout', '$log', '$uibModalInstance', 'title', 'action', 'product',
    function (ProductService, $scope, $rootScope, $timeout, $log, $uibModalInstance, title, action, product) {

        $scope.product = product;

        $scope.title = title;

        $scope.action = action;

        $scope.submit = function () {
            switch ($scope.action) {
                case 'create' :
                    ProductService.create($scope.product).then(function (data) {
                        $uibModalInstance.close(data);
                    });
                    break;
                case 'update' :
                    ProductService.update($scope.product).then(function (data) {
                        $scope.product = data;
                    });
                    break;
            }
        };

        $scope.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };

    }]);