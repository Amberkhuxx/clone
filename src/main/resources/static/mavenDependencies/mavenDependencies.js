(function () {
  'use strict';

  var app = angular.module('sqlc.maven-dependencies', ['ngMaterial', 'sqlc.common']);
  app.controller('listMavenDependenciesCtrl', function ($scope, $http, $mdDialog) {

    var checkOutLogString = 'Please check out the log file for more information.';

    var base = window.baseUrl + '/static/licensecheck/mavenDependencies/';

    var loadMavenDependencies = function () {
      $http.get(window.baseUrl + '/api/mavenDependencies/show').then(function (response) {
        $scope.mavenDependencies = response.data.mavenDependencies;
      });
    };

    var loadLicenses = function () {
      $http.get(window.baseUrl + '/api/licenses/show').then(function (response) {
        $scope.licenses = response.data;
      });
    };

    loadMavenDependencies();
    loadLicenses();

    $scope.editMavenDependency = function (ev, mavenDependency) {

      $scope.mavenDependencyKeyEdit = mavenDependency.key;
      $scope.mavenDependencyLicenseEdit = mavenDependency.license;

      $mdDialog.show({
        templateUrl: base + 'mavenDependenciesEditModal.html',
        targetEvent: ev,
        scope: $scope,
        clickOutsideToClose: true,
        preserveScope: true,
        controller: 'DialogController'
      })
        .then(function (answer) {
          var newMavenDependency = {
            oldKey: mavenDependency.key,
            newKey:  $scope.mavenDependencyKeyEdit,
            newLicense: $scope.mavenDependencyLicenseEdit
          };
          $http.post(window.baseUrl + '/api/mavenDependencies/edit?mavenDependency=' + JSON.stringify(newMavenDependency))
            .then(
            function (response) {
              loadMavenDependencies();
            },
            function (response) {
              alert('Failed to edit maven dependency. ' + checkOutLogString);
            });
        }, function () {
          // console.log('edit maven dependency canceled');
        });
    };

    $scope.addMavenDependency = function (ev) {

      $mdDialog.show({
        templateUrl: base + 'mavenDependenciesAddModal.html',
        targetEvent: ev,
        scope: $scope,
        clickOutsideToClose: true,
        preserveScope: true,
        controller: 'DialogController'
      })
        .then(function (answer) {
          var mavenDependency = {
            key: $scope.mavenDependencyKeyAdd,
            license: $scope.mavenDependencyLicenseAdd
          };
          $http.post(window.baseUrl + '/api/mavenDependencies/add?mavenDependency=' + JSON.stringify(mavenDependency))
            .then(
            function (response) {
              loadMavenDependencies();
            },
            function (response) {
              alert('Failed to add maven dependency. ' + checkOutLogString);
            })
        }, function () {
          // console.log('add maven dependency canceled');
        });

      $scope.mavenDependencyKeyAdd = '';
      $scope.mavenDependencyLicenseAdd = '';
    };

    $scope.deleteMavenDependency = function (ev, mavenDependency) {

      $scope.mavenDependencyKeyDelete = mavenDependency.key;
      $scope.mavenDependencyLicenseDelete = mavenDependency.license;

      $mdDialog.show({
        templateUrl: base + 'mavenDependenciesDeleteModal.html',
        targetEvent: ev,
        scope: $scope,
        clickOutsideToClose: true,
        preserveScope: true,
        controller: 'DialogController'
      }).then(function () {
        $http.post(window.baseUrl + '/api/mavenDependencies/delete?mavenDependency=' + JSON.stringify(mavenDependency))
          .then(
          function (response) {
            loadMavenDependencies();
          },
          function (response) {
            alert('Failed to delete maven dependency. ' + checkOutLogString);
          });
      },
        function () {
          // console.log('deletion aborted');
        });
    };
  });
} ());
