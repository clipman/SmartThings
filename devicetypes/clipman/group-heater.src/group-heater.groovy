/**
*  Group Heater (v.0.0.1)
*
*  Authors
*   - clipman@naver.com
*  Copyright 2021
*
*  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
*  in compliance with the License. You may obtain a copy of the License at:
*
*  http://www.apache.org/licenses/LICENSE-2.0
*
*  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
*  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
*  for the specific language governing permissions and limitations under the License.
*/

metadata {
	definition (name: "Group Heater", namespace: "clipman", author: "clipman", ocfDeviceType: "oic.d.thermostat",
		mnmn: "SmartThingsCommunity", vid: "c228c587-7524-33a3-9dd9-78124e29c28c") {

		capability "Switch"
		capability "circlecircle06391.switch"
	}
}

def setSwitchStatus(String status) {
	sendEvent(name: "switchStatus", value: status, displayed: "false")
}

def setSwitchDisplay(String display) {
	log.debug "setSwitchDisplay($display)"
	switch(display) {
	case "on":
		sendEvent(name: "switchTotal", value: display, displayed: "true")
		break;
	case "off":
		sendEvent(name: "switchTotal", value: display, displayed: "true")
		break;
	default:
		sendEvent(name: "switchDisplay", value: display, displayed: "true")
		break;
	}
}

def setSwitchTotal(String onoff) {
	sendEvent(name: "switchTotal", value: onoff, displayed: "true")
}

def on() {
	sendEvent(name: "switch", value: "on")
}

def off() {
	sendEvent(name: "switch", value: "off")
}

def installed() {
	sendEvent(name: "switchStatus", value: "꺼짐", displayed: "false")
	sendEvent(name: "switchDisplay", value: "꺼짐", displayed: "false")
	off()
}