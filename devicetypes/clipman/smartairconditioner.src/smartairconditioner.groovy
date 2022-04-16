/**
 *	SmartAirConditioner v2021-08-01
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
	definition (name: "SmartAirConditioner", namespace: "clipman", author: "clipman", ocfDeviceType: "oic.d.airconditioner",
		mnmn: "SmartThingsCommunity", vid: "dd5b2015-0da4-31f5-aacb-fecccad67bc6") {

		capability "Switch"								//on, off
		capability "Air Conditioner Mode"				//airConditionerMode, supportedAcModes=[auto, wind, cool, dry, coolClean, dryClean], setAirConditionerMode(mode)
		capability "Air Conditioner Fan Mode"			//fanMode, supportedAcFanModes=[auto, medium, high, turbo], setFanMode(fanMode)
		capability "Fan Speed"							//fanSpeed
		capability "Thermostat Cooling Setpoint"		//coolingSetpoint, setCoolingSetpoint(setpoint)
		capability "Temperature Measurement"			//temperature
		capability "Relative Humidity Measurement"		//humidity
		capability "Dust Sensor"						//dustLevel, fineDustLevel
		capability "Odor Sensor"						//odorLevel
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
		command "setOdorLevel"

		command "sendEvent"
		command "setToken"
	}

	preferences {
		input "status_1", "enum", title: "Select a status1", required: true, options: ["전원", "모드", "팬모드", "온도", "설정온도", "습도", "미세먼지", "미세먼지등급", "초미세먼지", "초미세먼지등급", "공기질", "갱신시간"], defaultValue: "온도"
		input "status_2", "enum", title: "Select a status2", required: true, options: ["전원", "모드", "팬모드", "온도", "설정온도", "습도", "미세먼지", "미세먼지등급", "초미세먼지", "초미세먼지등급", "공기질", "갱신시간", "표시안함"], defaultValue: "설정온도"
		input "status_3", "enum", title: "Select a status3", required: true, options: ["전원", "모드", "팬모드", "온도", "설정온도", "습도", "미세먼지", "미세먼지등급", "초미세먼지", "초미세먼지등급", "공기질", "갱신시간", "표시안함"], defaultValue: "모드"
		input "status_4", "enum", title: "Select a status4", required: true, options: ["전원", "모드", "팬모드", "온도", "설정온도", "습도", "미세먼지", "미세먼지등급", "초미세먼지", "초미세먼지등급", "공기질", "갱신시간", "표시안함"], defaultValue: "전원"
		input "status_5", "enum", title: "Select a status5", required: true, options: ["전원", "모드", "팬모드", "온도", "설정온도", "습도", "미세먼지", "미세먼지등급", "초미세먼지", "초미세먼지등급", "공기질", "갱신시간", "표시안함"], defaultValue: "표시안함"

		input type: "paragraph", element: "paragraph", title: "만든이", description: "김민수 clipman@naver.com [날자]<br>네이버카페: Smartthings & IoT home Community", displayDuringSetup: false
		input type: "paragraph", element: "paragraph", title: "SmartAirConditioner v2021-08-01", description: "", displayDuringSetup: false
	}
}

def setToken(stToken, deviceID) {
	state.stToken = stToken
	state.deviceID = deviceID
}

def statusbar() {
	def statusMap = ["전원":"switch", "모드":"airConditionerMode", "팬모드":"fanMode", "온도":"temperature", "설정온도":"coolingSetpoint", "습도":"humidity", "미세먼지":"dustLevel", "미세먼지등급":"dustClass", "초미세먼지":"fineDustLevel", "초미세먼지등급":"fineDustClass", "공기질":"airClass", "갱신시간":"status"]
	if(settings.status_1 == null || settings.status_1 == "") settings.status_1 = "온도"
	if(settings.status_2 == null || settings.status_2 == "") settings.status_2 = "설정온도"
	if(settings.status_3 == null || settings.status_3 == "") settings.status_3 = "모드"
	if(settings.status_4 == null || settings.status_4 == "") settings.status_4 = "전원"
	if(settings.status_5 == null || settings.status_5 == "") settings.status_5 = "표시안함"

	def status = device.currentValue(statusMap[settings.status_1])+getUnit(settings.status_1)
	if(settings.status_2 != "표시안함") status = status + " " + device.currentValue(statusMap[settings.status_2])+getUnit(settings.status_2)
	if(settings.status_3 != "표시안함") status = status + " " + device.currentValue(statusMap[settings.status_3])+getUnit(settings.status_3)
	if(settings.status_4 != "표시안함") status = status + " " + device.currentValue(statusMap[settings.status_4])+getUnit(settings.status_4)
	if(settings.status_5 != "표시안함") status = status + " " + device.currentValue(statusMap[settings.status_5])+getUnit(settings.status_5)

	sendEvent(name: "statusbar", value: status.replace("auto","스마트쾌적").replace("cool","냉방").replace("dry","제습").replace("Clean","청정").replace("wind","공기청정").replace("on","켜짐").replace("off","꺼짐"), displayed: false)
}

def getUnit(attributes) {
	if(attributes == "온도") return "°C"
	if(attributes == "설정온도") return "°C"
	if(attributes == "습도") return "%"
	if(attributes == "미세먼지") return "㎍"
	if(attributes == "초미세먼지") return "㎍"
	return ""
}

def setStatusbar(String status) {
	sendEvent(name: "statusbar", value: status, displayed: false)
}

def installed() {
	setDustLevel(0)
	setFineDustLevel(0)
	setOdorLevel(0)
	off()
}

def updated() {
	refresh()
}

def refresh() {
	get()
	//log.info "stToken: ${state.stToken}, deviceID: ${state.deviceID}, "
}

def setSwitch(onOff) {
	if(onOff == "on") {
		sendEvent(name: "switch", value: "on")
	} else {
		sendEvent(name: "switch", value: "off")
	}
}

/*
def setAirConditionerMode(airConditionerMode, state = "action") {
	if(state == "action") {
		def params = buildPost("airConditionerMode", "setAirConditionerMode", airConditionerMode)
		post(params)
	}
	sendEvent(name: "airConditionerMode", value: airConditionerMode)
}
*/
def setAirConditionerMode(airConditionerMode, state = "action") {
	if(state == "action") {
		if(airConditionerMode == "fanOnly") {
			airConditionerMode = "wind"
		}
		def params = buildPost("airConditionerMode", "setAirConditionerMode", airConditionerMode)
		post(params)
	} else {
		if(airConditionerMode == "wind") {
			airConditionerMode = "fanOnly"
		}
	}
	sendEvent(name: "airConditionerMode", value: airConditionerMode)
}

def setFanMode(fanMode, state = "action") {
	if(state == "action") {
		def params = buildPost("airConditionerFanMode", "setFanMode", fanMode)
		post(params)
	} else {
		try {
			def supportedAcFanModes = evaluate(device.currentValue("supportedAcFanModes"))
			if(supportedAcFanModes[0] == "auto") {
				if(fanMode == supportedAcFanModes[0]) {
					sendEvent(name: "fanSpeed", value: 0)
				} else if(fanMode == supportedAcFanModes[1]){
					sendEvent(name: "fanSpeed", value: 1)
				} else if(fanMode == supportedAcFanModes[2]){
					sendEvent(name: "fanSpeed", value: 3)
				} else {
					sendEvent(name: "fanSpeed", value: 4)
				}
			} else {
				if(fanMode == supportedAcFanModes[0]) {
					sendEvent(name: "fanSpeed", value: 1)
				} else if(fanMode == supportedAcFanModes[1]){
					sendEvent(name: "fanSpeed", value: 2)
				} else {
					sendEvent(name: "fanSpeed", value: 4)
				}
			}
		} catch (Exception e) {
			log.warn "supportedAcFanModes: ${e.message}"
		}
	}
	sendEvent(name: "fanMode", value: fanMode)
}

def setFanSpeed(fanSpeed){
	try {
		def supportedAcFanModes = evaluate(device.currentValue("supportedAcFanModes"))
		if(supportedAcFanModes[0] == "auto") {
			if(fanSpeed == 0) {
				setFanMode(supportedAcFanModes[0])
			} else if(fanSpeed == 1) {
				setFanMode(supportedAcFanModes[1])
			} else if(fanSpeed == 2) {
				setFanMode(supportedAcFanModes[1])
			} else if(fanSpeed == 3) {
				setFanMode(supportedAcFanModes[2])
			} else{
				setFanMode(supportedAcFanModes[3])
			}
		} else {
			if(fanSpeed == 0) {
				//
			} else if(fanSpeed == 1) {
				setFanMode(supportedAcFanModes[0])
			} else if(fanSpeed == 2) {
				setFanMode(supportedAcFanModes[1])
			} else if(fanSpeed == 3) {
				setFanMode(supportedAcFanModes[1])
			} else{
				setFanMode(supportedAcFanModes[2])
			}
		}
	} catch (Exception e) {
		log.warn "supportedAcFanModes: ${e.message}"
	}
	sendEvent(name: "fanSpeed", value: fanSpeed)
}

def setCoolingSetpoint(coolingSetpoint, state = "action") {
	if(state == "action") {
		def params = buildPost("thermostatCoolingSetpoint", "setCoolingSetpoint", coolingSetpoint)
		post(params)
	}
	sendEvent(name: "coolingSetpoint", value: coolingSetpoint, unit: "C")
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

def setOdorLevel(odorLevel) {
	def airClass
	if (odorLevel > 3) airClass = "최악"
	else if (odorLevel > 2) airClass = "나쁨"
	else if (odorLevel > 1) airClass = "보통"
	else if (odorLevel >= 0) airClass = "좋음"

	sendEvent(name: "odorLevel", value: odorLevel)
	sendEvent(name: "airClass", value: airClass)
}

def on() {
	def params = buildPost("switch", "on")
	post(params)
	sendEvent(name: "switch", value: "on")
}

def off() {
	def params = buildPost("switch", "off")
	post(params)
	sendEvent(name: "switch", value: "off")
}

def post(params) {
	log.trace "post($params)"
	try {
		httpPostJson(params) { resp ->
			if (resp.success) {
			}
			if (resp.data) {
				//log.debug "${resp.data}"
			}
		}
	} catch (Exception e) {
		log.warn "Call to on failed: ${e.message}"
	}
}

def buildPost(String capability_, String command_, arg_ = "") {
	log.trace "buildPost($capability_, $command_, $arg_)"
	def builder = new groovy.json.JsonBuilder()
	def c

	if (arg_ instanceof List) {
		c = [component:"main", capability:capability_, command:command_, arguments:arg_.collect()]
	} else if(arg_ != "") {
		def d = [arg_]
		c = [component:"main", capability:capability_, command:command_, arguments:d.collect()]
	} else {
		c = [component:"main", capability:capability_, command:command_]
	}

	builder commands:[c]

	def params = [
		uri: "https://api.smartthings.com/v1/devices/" + state.deviceID + "/commands",
		headers: ['Authorization' : "Bearer " + state.stToken],
		body: builder.toString()
	]
	//log.debug(builder.toString())
	return params
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
				setSwitch(resp.data.switch.switch.value)
				try {
					//sendEvent(name: "supportedAcModes", value: resp.data.airConditionerMode.supportedAcModes.value)
					sendEvent(name: "supportedAcModes", value: ["auto", "cool", "dry", "fanOnly", "coolClean", "dryClean"], displayed: false)
					setAirConditionerMode(resp.data.airConditionerMode.airConditionerMode.value, "state")
				} catch (Exception e) {
					log.info "airConditionerMode: ${e.message}"
				}
				try {
					setCoolingSetpoint(resp.data.thermostatCoolingSetpoint.coolingSetpoint.value as Integer, "state")
				} catch (Exception e) {
					log.info "thermostatCoolingSetpoint: ${e.message}"
				}
				try {
					setTemperature(resp.data.temperatureMeasurement.temperature.value as Integer)
				} catch (Exception e) {
					log.info "temperatureMeasurement: ${e.message}"
				}
				try {
					setDustLevel(resp.data.dustSensor.dustLevel.value as Integer)
					setFineDustLevel(resp.data.dustSensor.fineDustLevel.value as Integer)
				} catch (Exception e) {
					sendEvent(name: "dustClass", value: "모름")
					sendEvent(name: "fineDustClass", value: "모름")
					log.info "dustSensor: ${e.message}"
				}
				try {
					sendEvent(name: "supportedAcFanModes", value: resp.data.airConditionerFanMode.supportedAcFanModes.value)
					setFanMode(resp.data.airConditionerFanMode.fanMode.value, "state")
				} catch (Exception e) {
					log.info "airConditionerFanMode: ${e.message}"
				}
				try {
					setOdorLevel(resp.data.odorSensor.odorLevel.value as Integer)
				} catch (Exception e) {
					sendEvent(name: "airClass", value: "모름")
					log.info "odorSensor: ${e.message}"
				}
				try {
					setHumidity(resp.data.relativeHumidityMeasurement.humidity.value)
				} catch (Exception e) {
					log.info "relativeHumidityMeasurement: ${e.message}"
				}
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