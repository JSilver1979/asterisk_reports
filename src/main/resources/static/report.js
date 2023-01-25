angular.module('app',[]).controller('indexController', function ($scope, $http) {
    const contextPath = 'http://localhost:8080'
    // const contextPath = 'http://10.1.5.105:8080'

    $scope.search = function () {
        $http.post(contextPath + '/report/calls_search', $scope.newDate)
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
        $http.post(contextPath + '/stats/v2', $scope.newDate)
            .then(function (response){
                $scope.Stats = response.data;
            });
    };

    $scope.downloadReports = function () {
        $http.post(contextPath + '/file/get_report', $scope.newReport)
            .then(function (response) {
                let file = new Blob([new Uint8Array([0xEF, 0xBB, 0xBF]),response.data], {type: 'text/csv'});
                let url = window.URL || window.webkitURL;
                let downloadLink = angular.element('<a></a>');
                let filename = $scope.newReport.dateFrom + '_' + $scope.newReport.dateTo + '_' + $scope.newReport.group + '.csv';
                downloadLink.attr('href', url.createObjectURL(file));
                downloadLink.attr('target','_self');
                downloadLink.attr('download', filename);
                downloadLink[0].click();
            });
    };
});