app.factory("OrderBuyService",
    ['$http', '$log', function ($http, $log) {
        return {
            findAll: function () {
                return $http.get("/api/orderBuy/findAll").then(function (response) {
                    return response.data;
                });
            },
            findAllCombo: function () {
                return $http.get("/api/orderBuy/findAllCombo").then(function (response) {
                    return response.data;
                });
            },
            findOne: function (id) {
                return $http.get("/api/orderBuy/findOne/" + id).then(function (response) {
                    return response.data;
                });
            },
            create: function (orderBuy) {
                return $http.post("/api/orderBuy/create", orderBuy).then(function (response) {
                    return response.data;
                });
            },
            remove: function (id) {
                return $http.delete("/api/orderBuy/delete/" + id);
            },
            filter: function (search) {
                return $http.get("/api/orderBuy/filter?" + search).then(function (response) {
                    return response.data;
                });
            },
            findByToday: function (filter) {
                return $http.get(filter ? "/api/orderBuy/findByToday?filter=" + filter : "/api/orderBuy/findByToday").then(function (response) {
                    return response.data;
                });
            },
            findByWeek: function (filter) {
                return $http.get(filter ? "/api/orderBuy/findByWeek?filter=" + filter : "/api/orderBuy/findByWeek").then(function (response) {
                    return response.data;
                });
            },
            findByMonth: function (filter) {
                return $http.get(filter ? "/api/orderBuy/findByMonth?filter=" + filter : "/api/orderBuy/findByMonth").then(function (response) {
                    return response.data;
                });
            },
            findByYear: function (filter) {
                return $http.get(filter ? "/api/orderBuy/findByYear?filter=" + filter : "/api/orderBuy/findByYear").then(function (response) {
                    return response.data;
                });
            }
        };
    }]);