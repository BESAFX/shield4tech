app.controller('productCategoryCreateUpdateCtrl', ['ProductCategoryService', '$scope', '$rootScope', '$timeout', '$log', '$uibModalInstance', 'title', 'action', 'productCategory',
    function (ProductCategoryService, $scope, $rootScope, $timeout, $log, $uibModalInstance, title, action, productCategory) {

        $scope.productCategory = productCategory;

        $scope.title = title;

        $scope.action = action;

        $scope.submit = function () {
            switch ($scope.action) {
                case 'create' :
                    ProductCategoryService.create($scope.productCategory).then(function (data) {
                        $uibModalInstance.close(data);
                    });
                    break;
                case 'update' :
                    ProductCategoryService.update($scope.productCategory).then(function (data) {
                        $scope.productCategory = data;
                    });
                    break;
            }
        };

        $scope.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };

    }]);