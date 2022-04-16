/**
 *	SmartAirMonitor v2021-08-01
 *	clipman@naver.com
 *  날자
 *
 *	Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 *	in compliance with the License. You may obtain a copy of the License at:
 *
 *	http://www.apache.org/licenses/LICENSE-2.0
 *
 *	Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 *	on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
 *	for the specific language governing permissions and limitations under the License.
 *
 */

import groovy.json.JsonSlurper

metadata {
	definition (name: "SmartAirMonitor ", namespace: "clipman", author: "clipman", ocfDeviceType: "x.com.st.d.airqualitysensor",
		mnmn: "SmartThingsCommunity", vid: "a5fbcb73-6516-35e4-8165-7eaef2387ad4") {

		capability "Dust Sensor"						//dustLevel, fineDustLevel
		capability "Very Fine Dust Sensor"				//veryFineDustLevel
		capability "Carbon Dioxide Measurement"			//carbonDioxide
		//capability "Tvoc Measurement"					//tvocLevel
		//capability "Radon Measurement"				//radonLevel
		capability "Temperature Measurement"			//temperature
		capability "Relative Humidity Measurement"		//humidity
		capability "Air Quality Sensor"					//airQuality
		capability "Odor Sensor"						//odorLevel
		capability "Dust Health Concern"				//dustHealthConcern
		capability "Fine Dust Health Concern"			//fineDustHealthConcern
		capability "Very Fine Dust Health Concern"		//veryFineDustHealthConcern
		capability "Carbon Dioxide Health Concern"		//carbonDioxideHealthConcern
		//capability "Tvoc Health Concern"				//tvocHealthConcern
		//capability "Radon Health Concern"				//radonHealthConcern
		capability "circlecircle06391.dustClass"		//dustClass
		capability "circlecircle06391.fineDustClass"	//fineDustClass
		capability "circlecircle06391.airClass"			//airClass
		capability "circlecircle06391.status"			//statusbar
		capability "circlecircle06391.statusBar"		//status
		capability "Refresh"

		command "setTemperature"
		command "setHumidity"
		command "setDustLevel"
		command "setFineDustLevel"
		command "setVeryFineDustLevel"
		command "setOdorLevel"
		command "setCarbonDioxide"
		command "setTvocLevel"
		command "setRadonLevel"

		command "sendEvent"
		command "setToken"
	}

	preferences {
		input "status_1", "enum", title: "Select a status1", required: true, options: ["온도", "습도", "미세먼지", "미세먼지등급", "초미세먼지", "초미세먼지등급", "극초미세먼지", "이산화탄소", "공기질", "갱신시간"], defaultValue: "온도"
		input "status_2", "enum", title: "Select a status2", required: true, options: ["온도", "습도", "미세먼지", "미세먼지등급", "초미세먼지", "초미세먼지등급", "극초미세먼지", "이산화탄소", "공기질", "갱신시간", "표시안함"], defaultValue: "습도"
		input "status_3", "enum", title: "Select a status3", required: true, options: ["온도", "습도", "미세먼지", "미세먼지등급", "초미세먼지", "초미세먼지등급", "극초미세먼지", "이산화탄소", "공기질", "갱신시간", "표시안함"], defaultValue: "미세먼지"
		input "status_4", "enum", title: "Select a status4", required: true, options: ["온도", "습도", "미세먼지", "미세먼지등급", "초미세먼지", "초미세먼지등급", "극초미세먼지", "이산화탄소", "공기질", "갱신시간", "표시안함"], defaultValue: "초미세먼지"
		input "status_5", "enum", title: "Select a status5", required: true, options: ["온도", "습도", "미세먼지", "미세먼지등급", "초미세먼지", "초미세먼지등급", "극초미세먼지", "이산화탄소", "공기질", "갱신시간", "표시안함"], defaultValue: "표시안함"

		input type: "paragraph", element: "paragraph", title: "만든이", description: "김민수 clipman@naver.com [날자]<br>네이버카페: Smartthings & IoT home Community", displayDuringSetup: false
		input type: "paragraph", element: "paragraph", title: "SmartAirMonitor v2021-08-01", description: "", displayDuringSetup: false
	}
}

def setToken(stToken, deviceID) {
	state.stToken = stToken
	state.deviceID = deviceID
}

def statusbar() {
	def statusMap = ["온도":"temperature", "습도":"humidity", "미세먼지":"dustLevel", "미세먼지등급":"dustClass", "초미세먼지":"fineDustLevel", "초미세먼지등급":"fineDustClass", "극초미세먼지":"veryFineDustLevel", "이산화탄소":"carbonDioxide", "공기질":"airClass", "갱신시간":"status"]
	if(settings.status_1 == null || settings.status_1 == "") settings.status_1 = "온도"
	if(settings.status_2 == null || settings.status_2 == "") settings.status_2 = "습도"
	if(settings.status_3 == null || settings.status_3 == "") settings.status_3 = "미세먼지"
	if(settings.status_4 == null || settings.status_4 == "") settings.status_4 = "초미세먼지"
	if(settings.status_5 == null || settings.status_5 == "") settings.status_5 = "표시안함"

	def status = device.currentValue(statusMap[settings.status_1])+getUnit(settings.status_1)
	if(settings.status_2 != "표시안함") status = status + " " + device.currentValue(statusMap[settings.status_2])+getUnit(settings.status_2)
	if(settings.status_3 != "표시안함") status = status + " " + device.currentValue(statusMap[settings.status_3])+getUnit(settings.status_3)
	if(settings.status_4 != "표시안함") status = status + " " + device.currentValue(statusMap[settings.status_4])+getUnit(settings.status_4)
	if(settings.status_5 != "표시안함") status = status + " " + device.currentValue(statusMap[settings.status_5])+getUnit(settings.status_5)

	sendEvent(name: "statusbar", value: status, displayed: false)
}

def getUnit(attributes) {
	if(attributes == "온도") return "°C"
	if(attributes == "습도") return "%"
	if(attributes == "미세먼지") return "㎍"
	if(attributes == "초미세먼지") return "㎍"
	if(attributes == "극초미세먼지") return "㎍"
	if(attributes == "이산화탄소") return "ppm"
	return ""
}

def setStatusbar(String status) {
	sendEvent(name: "statusbar", value: status, displayed: false)
}

def installed() {
}

def updated() {
	refresh()
}

def refresh() {
	get()
	//log.info "stToken: ${state.stToken}, deviceID: ${state.deviceID}, "
}

def setTemperature(temperature) {
	sendEvent(name: "temperature", value: temperature, unit: "C")
}

def setHumidity(humidity) {
	sendEvent(name: "humidity", value: humidity, unit: "%")
}

def setDustLevel(dustLevel) {
	def dustClass
	if (dustLevel > 150) dustClass = "최악"
	else if (dustLevel > 80) dustClass = "나쁨"
	else if (dustLevel > 30) dustClass = "보통"
	else if (dustLevel >= 0) dustClass = "좋음"

	sendEvent(name: "dustLevel", value: dustLevel, unit: "㎍/㎥")
	sendEvent(name: "dustClass", value: dustClass)
}

def setFineDustLevel(fineDustLevel) {
	def fineDustClass
	if (fineDustLevel > 75) fineDustClass = "최악"
	else if (fineDustLevel > 35) fineDustClass = "나쁨"
	else if (fineDustLevel > 15) fineDustClass = "보통"
	else if (fineDustLevel >= 0) fineDustClass = "좋음"

	sendEvent(name: "fineDustLevel", value: fineDustLevel, unit: "㎍/㎥")
	sendEvent(name: "fineDustClass", value: fineDustClass)
}

def setVeryFineDustLevel(veryFineDustLevel) {
	sendEvent(name: "veryFineDustLevel", value: veryFineDustLevel, unit: "㎍/㎥")
}

def setCarbonDioxide(carbonDioxide) {
	sendEvent(name: "carbonDioxide", value: carbonDioxide, unit: "ppm")
}

def setTvocLevel(tvocLevel) {
	sendEvent(name: "tvocLevel", value: tvocLevel, unit: "ppm")
}

def setRadonLevel(radonLevel) {
	sendEvent(name: "radonLevel", value: radonLevel, unit: "ppm")
}

def setOdorLevel(odorLevel) {
	def airClass
	if (odorLevel > 3) airClass = "최악"
	else if (odorLevel > 2) airClass = "나쁨"
	else if (odorLevel > 1) airClass = "보통"
	else if (odorLevel >= 0) airClass = "좋음"

	sendEvent(name: "odorLevel", value: odorLevel)
	sendEvent(name: "airClass", value: airClass)
}

def setDustHealthConcern(dustHealthConcern) {
	sendEvent(name: "dustHealthConcern", value: dustHealthConcern)
}

def setFineDustHealthConcern(fineDustHealthConcern) {
	sendEvent(name: "fineDustHealthConcern", value: fineDustHealthConcern)
}

def setVeryFineDustHealthConcern(veryFineDustHealthConcern) {
	sendEvent(name: "veryFineDustHealthConcern", value: veryFineDustHealthConcern)
}

def setCarbonDioxideHealthConcern(carbonDioxideHealthConcern) {
	sendEvent(name: "carbonDioxideHealthConcern", value: carbonDioxideHealthConcern)
}

def setTvocHealthConcern(tvocHealthConcern) {
	sendEvent(name: "tvocHealthConcern", value: tvocHealthConcern)
}

def setRadonHealthConcern(radonHealthConcern) {
	sendEvent(name: "radonHealthConcern", value: radonHealthConcern)
}

def get() {
	def params = [
		uri: "https://api.smartthings.com/v1/devices/" + state.deviceID + "/components/main/status",
		headers: ['Authorization' : "Bearer " + state.stToken]
	]
	//log.debug "params: ${params}"
	try {
		httpGet(params) { resp ->
			if (resp.status == 200) {
				//log.debug "response data: ${resp.data}"
				try {
					setDustLevel(resp.data.dustSensor.dustLevel.value as Integer)
					setFineDustLevel(resp.data.dustSensor.fineDustLevel.value as Integer)
				} catch (Exception e) {
					sendEvent(name: "dustClass", value: "모름")
					sendEvent(name: "fineDustClass", value: "모름")
					log.info "dustSensor: ${e.message}"
				}
				try {
					setVeryFineDustLevel(resp.data.veryFineDustSensor.veryFineDustLevel.value as Integer)
				} catch (Exception e) {
					log.info "veryFineDustSensor: ${e.message}"
				}
				try {
					setCarbonDioxide(resp.data.carbonDioxideMeasurement.carbonDioxide.value as Integer)
				} catch (Exception e) {
					log.info "carbonDioxideMeasurement: ${e.message}"
				}
				/*
				try {
					setTvocLevel(resp.data.tvocMeasurement.tvocLevel.value as Integer)
				} catch (Exception e) {
					log.info "tvocMeasurement: ${e.message}"
				}
				try {
					setRadonLevel(resp.data.radonMeasurement.radonLevel.value as Integer)
				} catch (Exception e) {
					log.info "radonMeasurement: ${e.message}"
				}
				*/
				try {
					setOdorLevel(resp.data.odorSensor.odorLevel.value as Integer)
				} catch (Exception e) {
					sendEvent(name: "airClass", value: "모름")
					log.info "odorSensor: ${e.message}"
				}
				try {
					setTemperature(resp.data.temperatureMeasurement.temperature.value as Integer)
				} catch (Exception e) {
					log.info "temperatureMeasurement: ${e.message}"
				}
				try {
					setHumidity(resp.data.relativeHumidityMeasurement.humidity.value as Integer)
				} catch (Exception e) {
					log.info "relativeHumidityMeasurement: ${e.message}"
				}
				try {
					setDustHealthConcern(resp.data.dustHealthConcern.dustHealthConcern.value)
				} catch (Exception e) {
					log.info "dustHealthConcern: ${e.message}"
				}
				try {
					setFineDustHealthConcern(resp.data.fineDustHealthConcern.fineDustHealthConcern.value)
				} catch (Exception e) {
					log.info "fineDustHealthConcern: ${e.message}"
				}
				try {
					setVeryFineDustHealthConcern(resp.data.veryFineDustHealthConcern.veryFineDustHealthConcern.value)
				} catch (Exception e) {
					log.info "veryFineDustHealthConcern: ${e.message}"
				}
				try {
					setCarbonDioxideHealthConcern(resp.data.carbonDioxideHealthConcern.carbonDioxideHealthConcern.value)
				} catch (Exception e) {
					log.info "carbonDioxideHealthConcern: ${e.message}"
				}
				/*
				try {
					setTvocHealthConcern(resp.data.tvocHealthConcern.tvocHealthConcern.value)
				} catch (Exception e) {
					log.info "tvocHealthConcern: ${e.message}"
				}
				try {
					setRadonHealthConcern(resp.data.radonHealthConcern.radonHealthConcern.value)
				} catch (Exception e) {
					log.info "radonHealthConcern: ${e.message}"
				}
				*/
			}
		}
	} catch (Exception e) {
		log.warn "Call to on failed: ${e.message}"
	}
	updateTime()
	statusbar()
}

def updateTime() {
	def timeStaus = new Date().format("HH:mm:ss", location.timeZone)
	sendEvent(name: "status", value: timeStaus, displayed: false)
}