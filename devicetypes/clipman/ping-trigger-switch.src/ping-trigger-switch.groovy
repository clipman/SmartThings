/**
 *	Ping Trigger Switch 0.0.1
 *	Copyright 2021 Minsoo Kim (clipman)
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

metadata {
	definition (name: "Ping Trigger Switch", namespace: "clipman", author: "clipman", ocfDeviceType: "oic.d.switch") {
		capability "Actuator"
		capability "Button"
		capability "Refresh"
		capability "Configuration"
		capability "Switch"
	}

	preferences {
        input name: "deviceIP", type: "text", required: true, title: "Device IP Address"
        input name: "devicePort", type: "number", required: true, title: "Device port", defaultValue: 80, range: "1..99999"
        input name: "eventOptionValue", type: "enum", title: "(Optional) When to fire events that are triggered by On/Off commands?", options:["0": "Only for state changes (Default)" , "1": "Always fire events for every command"], defaultValue: "0"
	}
}

def on() {
	sendEvent(name: "switch", value: state.switch)
	if (eventOption != "0" || state.switch=="off") {
		sendEvent(name: "button", value: "pushed", displayed: false, isStateChange: true)
		log.debug "Setting button status is pushed"
	}
}

def off() {
	sendEvent(name: "switch", value: state.switch)
	if (eventOption != "0" || state.switch=="on") {
		sendEvent(name: "button", value: "held", displayed: false, isStateChange: true)
		log.debug "Setting button status to held"
	}
}

def installed() {
	state.switch="off"
	sendEvent(name: "switch", value: "off", displayed: false)
	sendEvent(name: "supportedButtonValues", value: ["pushed", "held"].encodeAsJSON(), displayed: false)
	sendEvent(name: "numberOfButtons", value: 1, displayed: false)
	sendEvent(name: "button", value: "pushed", displayed: false, isStateChange: false)

	initialize()
}

def initialize() {
	sendEvent(name: "checkInterval", value: 30 * 60 + 2 * 60, displayed: false, data: [protocol: "lan", hubHardwareId: device.hub.hardwareID])
	runEvery1Minute(ping)
	refresh()
}

def refresh() {
	ping()
}

def ping() {
	def egHostIP = deviceIP ?: "192.168.219.130"
	def egHostPort = devicePort ?: 80
	def egHost = egHostIP + ":" + egHostPort

	log.debug "ping($egHost)"
	unschedule(pingTimeout)
	runIn(2, pingTimeout)
	sendHubCommand(new physicalgraph.device.HubAction("""GET / HTTP/1.1\r\nHOST: $egHost\r\n\r\n""",physicalgraph.device.Protocol.LAN,null,[callback:pingCallback]))
}

def pingCallback(physicalgraph.device.HubResponse hubResponse) {
	log.debug("Device is now online")
	unschedule(pingTimeout)
    state.switch = "on"
	sendEvent(name: "switch", value: "on", displayed: true)
}

def pingTimeout() {
	log.debug("Device is now offline")
   	state.switch = "off"
	sendEvent(name: "switch", value: "off", displayed: true)
}

private getEventOption() {
	return eventOptionValue ?: "0"
}
