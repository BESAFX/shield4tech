app.factory("OrderSellService",
    ['$http', '$log', function ($http, $log) {
        return {
            findAll: function () {
                return $http.get("/api/orderSell/findAll").then(function (response) {
                    return response.data;
                });
            },
            findAllCombo: function () {
                return $http.get("/api/orderSell/findAllCombo").then(function (response) {
                    return response.data;
                });
            },
            findOne: function (id) {
                return $http.get("/api/orderSell/findOne/" + id).then(function (response) {
                    return response.data;
                });
            },
            create: function (orderSell) {
                return $http.post("/api/orderSell/create", orderSell).then(function (response) {
                    return response.data;
                });
            },
            remove: function (id) {
                return $http.delete("/api/orderSell/delete/" + id);
            },
            filter: function (search) {
                return $http.get("/api/orderSell/filter?" + search).then(function (response) {
                    return response.data;
                });
            },
            findByToday: function (filter) {
                return $http.get(filter ? "/api/orderSell/findByToday?filter=" + filter : "/api/orderSell/findByToday").then(function (response) {
                    return response.data;
                });
            },
            findByWeek: function (filter) {
                return $http.get(filter ? "/api/orderSell/findByWeek?filter=" + filter : "/api/orderSell/findByWeek").then(function (response) {
                    return response.data;
                });
            },
            findByMonth: function (filter) {
                return $http.get(filter ? "/api/orderSell/findByMonth?filter=" + filter : "/api/orderSell/findByMonth").then(function (response) {
                    return response.data;
                });
            },
            findByYear: function (filter) {
                return $http.get(filter ? "/api/orderSell/findByYear?filter=" + filter : "/api/orderSell/findByYear").then(function (response) {
                    return response.data;
                });
            }
        };
    }]);