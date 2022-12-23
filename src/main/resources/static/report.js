angular.module('app',[]).controller('indexController', function ($scope, $http) {
    const contextPath = 'http://localhost:8080/report'

    // $scope.loadStats = function() {
    //     $http.get(contextPath + '/call')
    //         .then(function (response) {
    //             $scope.CallsList = response.data;
    //         });
    // };

    // $scope.search = function (dateSearch) {
    //     $http.post(contextPath + '/date_search', $scope.newDate)
    //         .then(function (response){
    //         $scope.Stats = response.data;
    //     });
    // };

    $scope.search = function () {
        $http.post(contextPath + '/calls_search', $scope.newDate)
            .then(function (response){
                $scope.CallsList = response.data;
            });
    };

    // $scope.loadStats();
});