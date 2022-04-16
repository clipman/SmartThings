/**
 *	SmartAirConditionerWall v2021-08-01
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
	definition (name: "SmartAirConditionerWall", namespace: "clipman", author: "clipman", ocfDeviceType: "oic.d.airconditioner",
		mnmn: "SmartThingsCommunity", vid: "dfd19641-1641-3f39-b396-a93d2832fd41") {

		capability "Switch"								//on, off
		capability "Air Conditioner Mode"				//airConditionerMode, supportedAcModes=[auto, cool, dry, fanOnly], setAirConditionerMode(mode)
		capability "Air Conditioner Fan Mode"			//fanMode, supportedAcFanModes=[auto, medium, high, turbo], setFanMode(fanMode)
		capability "Fan Speed"							//fanSpeed
		capability "Thermostat Cooling Setpoint"		//coolingSetpoint, setCoolingSetpoint(setpoint)
		capability "Temperature Measurement"			//temperature
		capability "circlecircle06391.status"			//statusbar
		capability "circlecircle06391.statusBar"		//status
		capability "Refresh"

		command "setTemperature"

		command "sendEvent"
		command "setToken"
	}

	preferences {
		input "status_1", "enum", title: "Select a status1", required: true, options: ["전원", "모드", "팬모드", "온도", "설정온도", "갱신시간"], defaultValue: "설정온도"
		input "status_2", "enum", title: "Select a status2", required: true, options: ["전원", "모드", "팬모드", "온도", "설정온도", "표시안함"], defaultValue: "모드"
		input "status_3", "enum", title: "Select a status3", required: true, options: ["전원", "모드", "팬모드", "온도", "설정온도", "표시안함"], defaultValue: "팬모드"
		input "status_4", "enum", title: "Select a status4", required: true, options: ["전원", "모드", "팬모드", "온도", "설정온도", "표시안함"], defaultValue: "전원"
		input "status_5", "enum", title: "Select a status5", required: true, options: ["전원", "모드", "팬모드", "온도", "설정온도", "표시안함"], defaultValue: "표시안함"

		input type: "paragraph", element: "paragraph", title: "만든이", description: "김민수 clipman@naver.com [날자]<br>네이버카페: Smartthings & IoT home Community", displayDuringSetup: false
		input type: "paragraph", element: "paragraph", title: "SmartAirConditioner v2021-08-01", description: "", displayDuringSetup: false
	}
}

def setToken(stToken, deviceID) {
	state.stToken = stToken
	state.deviceID = deviceID
}

def statusbar() {
	def statusMap = ["전원":"switch", "모드":"airConditionerMode", "팬모드":"fanMode", "온도":"temperature", "설정온도":"coolingSetpoint", "갱신시간":"status"]
	if(settings.status_1 == null || settings.status_1 == "") settings.status_1 = "설정온도"
	if(settings.status_2 == null || settings.status_2 == "") settings.status_2 = "모드"
	if(settings.status_3 == null || settings.status_3 == "") settings.status_3 = "팬모드"
	if(settings.status_4 == null || settings.status_4 == "") settings.status_4 = "전원"
	if(settings.status_5 == null || settings.status_5 == "") settings.status_5 = "표시안함"

	def status = device.currentValue(statusMap[settings.status_1])+getUnit(settings.status_1)
	if(settings.status_2 != "표시안함") status = status + " " + device.currentValue(statusMap[settings.status_2])+getUnit(settings.status_2)
	if(settings.status_3 != "표시안함") status = status + " " + device.currentValue(statusMap[settings.status_3])+getUnit(settings.status_3)
	if(settings.status_4 != "표시안함") status = status + " " + device.currentValue(statusMap[settings.status_4])+getUnit(settings.status_4)
	if(settings.status_5 != "표시안함") status = status + " " + device.currentValue(statusMap[settings.status_5])+getUnit(settings.status_5)

	sendEvent(name: "statusbar", value: status.replace("cool","자동").replace("cool","냉방").replace("dry","제습").replace("fanonly","송풍").replace("on","켜짐").replace("off","꺼짐"), displayed: false)
}

def getUnit(attributes) {
	if(attributes == "온도") return "°C"
	if(attributes == "설정온도") return "°C"
	return ""
}

def setStatusbar(String status) {
	sendEvent(name: "statusbar", value: status, displayed: false)
}

def installed() {
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

def setAirConditionerMode(airConditionerMode, state = "action") {
	if(state == "action") {
		def params = buildPost("airConditionerMode", "setAirConditionerMode", airConditionerMode)
		post(params)
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
					sendEvent(name: "supportedAcModes", value: resp.data.airConditionerMode.supportedAcModes.value)
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
					sendEvent(name: "supportedAcFanModes", value: resp.data.airConditionerFanMode.supportedAcFanModes.value)
					setFanMode(resp.data.airConditionerFanMode.fanMode.value, "state")
				} catch (Exception e) {
					log.info "airConditionerFanMode: ${e.message}"
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