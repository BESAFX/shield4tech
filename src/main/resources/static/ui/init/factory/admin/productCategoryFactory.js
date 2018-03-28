app.factory("ProductCategoryService",
    ['$http', '$log', function ($http, $log) {
        return {
            findAll: function () {
                return $http.get("/api/productCategory/findAll").then(function (response) {
                    return response.data;
                });
            },
            findAllCombo: function () {
                return $http.get("/api/productCategory/findAllCombo").then(function (response) {
                    return response.data;
                });
            },
            findOne: function (id) {
                return $http.get("/api/productCategory/findOne/" + id).then(function (response) {
                    return response.data;
                });
            },
            create: function (productCategory) {
                return $http.post("/api/productCategory/create", productCategory).then(function (response) {
                    return response.data;
                });
            },
            remove: function (id) {
                return $http.delete("/api/productCategory/delete/" + id);
            },
            update: function (productCategory) {
                return $http.put("/api/productCategory/update", productCategory).then(function (response) {
                    return response.data;
                });
            }
        };
    }]);