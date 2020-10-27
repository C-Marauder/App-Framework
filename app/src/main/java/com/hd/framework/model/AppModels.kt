package com.hd.framework.model

/**
 *   @Author:小灰灰
 *   @Time:2020/10/26
 *   @Desc:
 */

data class House(
    var buildingName: String? = null,
    var cellName: String? = null,
    var checkStatus: Int? = null,
    var checkStatusName: String? = null,
    var houseId: Int? = null,
    var houseNum: String? = null,
    var householdType: Int? = null,
    var householdTypeName: String? = null,
    var lastCheckerType: Int? = null,
    var notpassNum: Int? = null,
    var passedNum: Int? = null,
    var projectId: Int? = null,
    var projectName: String? = null,
    var relationId: Int? = null
)
data class City(
    var areaId: Int? = null,
    var areaName: String? = null
)