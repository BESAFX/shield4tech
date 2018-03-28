app.factory("ProductService",
    ['$http', '$log', function ($http, $log) {
        return {
            findAll: function () {
                return $http.get("/api/product/findAll").then(function (response) {
                    return response.data;
                });
            },
            findAllCombo: function () {
                return $http.get("/api/product/findAllCombo").then(function (response) {
                    return response.data;
                });
            },
            findOne: function (id) {
                return $http.get("/api/product/findOne/" + id).then(function (response) {
                    return response.data;
                });
            },
            create: function (product) {
                return $http.post("/api/product/create", product).then(function (response) {
                    return response.data;
                });
            },
            remove: function (id) {
                return $http.delete("/api/product/delete/" + id);
            },
            update: function (product) {
                return $http.put("/api/product/update", product).then(function (response) {
                    return response.data;
                });
            },
            filter: function (search) {
                return $http.get("/api/product/filter?" + search).then(function (response) {
                    return response.data;
                });
            }
        };
    }]);