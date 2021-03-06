/*
 * Copyright (c) 2015, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

var operationModule = function () {
    var log = new Log("modules/operation.js");

    var constants = require("constants.js");
    var utility = require("utility.js").utility;

    var hostname = utility.getIoTServerConfig("IoTMgtHost");
    var carbonHttpsServletTransport = "https://" + hostname + ":9443";

    var server = require('store').server;
    var user = server.current(session);

    var publicMethods = {};
    var privateMethods = {};

    publicMethods.getControlOperations = function (deviceType) {
        switch (deviceType) {
            case "virtual_firealarm":
                return [{name: "Alarm Status", description: "0:off 1:on", operation: "bulb"}];
            default:
                return [];
        }
    };

    publicMethods.getMonitorOperations = function (deviceType) {
        switch (deviceType) {
            case "virtual_firealarm":
                return [{name: "Temperature", operation: "readtemperature"}];
            case "android_sense":
                return [
                    {name: "Temperature", operation: "readtemperature"},
                    {name: "Battery", operation: "readbattery"},
                    {name: "gps", operation: "readgps"},
                    {name: "Light", operation: "readlight"},
                    {name: "Magnetic", operation: "readmagnetic"}
                ];
            default:
                return [];
        }
    };

    publicMethods.handlePOSTOperation = function (deviceType, operation, deviceId, value) {
        var endPoint = carbonHttpsServletTransport + '/' + deviceType + "/controller/" + operation + "/" + ((value == 1) ? "ON" : "OFF");
        var header = '{"owner":"' + user.username + '","deviceId":"' + deviceId + '","protocol":"mqtt"}';
        return post(endPoint, {}, JSON.parse(header), "json");
    };

    publicMethods.handleGETOperation = function (deviceType, operation, operationName, deviceId) {

        if (operation == "readgps") {
            var result = {};
            result.data = {
                map: {
                    lat: Math.random() * (6.9 - 6.1) + 6.1,
                    lng: Math.random() * (80 - 78) + 79
                }
            };
            return result;
        }
        var endPoint = carbonHttpsServletTransport + '/' + deviceType + "/controller/" + operation;
        var header = '{"owner":"' + user.username + '","deviceId":"' + deviceId + '","protocol":"mqtt"}';
        result = get(endPoint, {}, JSON.parse(header), "json");
        if (result.data) {
            result.data = JSON.parse('{"' + operationName + '":' + result.data.sensorValue + ', "time":' + result.data.time + '}');
        }
        return result;
    };

    return publicMethods;
}();
