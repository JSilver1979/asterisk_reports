angular.module('app',[]).controller('indexController', function ($scope, $http) {
    const contextPath = 'http://localhost:8080/report'

    $scope.search = function () {
        $http.post(contextPath + '/calls_search', $scope.newDate)
            .then(function (response){
                $scope.CallsList = response.data;
            });
    };
    $scope.isAnswered = function(test) {
        if (test.includes('ANSWERED')) {
            return true;
        } else {
            return false;
        }
    };

    $scope.searchStats = function (){
        $http.post('http://localhost:8080/stats', $scope.newDate)
            .then(function (response){
                $scope.Stats = response.data;
            });
    };
});