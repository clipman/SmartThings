/**
 *  GalaxyHome Button v2022-07-05
 *  clipman@naver.com
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License. You may obtain a copy of the License at:
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 *  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
 *  for the specific language governing permissions and limitations under the License.
 *
 */

metadata {
	definition (name: "GalaxyHome Button", namespace: "clipman", author: "clipman", ocfDeviceType: "x.com.st.d.remotecontroller", mnmn: "SmartThingsCommunity", vid: "05d8f755-c62d-376f-ad5c-54eb57befb7c") {
		capability "Button"
		capability 'Momentary'
	}
	preferences {
		input type: "paragraph", element: "paragraph", title: "만든이", description: "김민수 clipman@naver.com [날자]", displayDuringSetup: false
		input "stGalaxyHomeID", "text", title: "GalaxyHome Device ID", required: true
		input "stToken", "text", title: "SmartThings Token", required: true
		input "stGalaxyOrder", "text", title: "빅스비 명령", required: true
	}
}

def installed() {
	sendEvent(name: "supportedButtonValues", value: ['pushed'], displayed: false)
	sendEvent(name: "numberOfButtons", value: 1, displayed: false)
}

def push() {
	sendEvent(name: 'button', value: 'pushed', isStateChange: true)
	if(stGalaxyHomeID && stToken && stGalaxyOrder) {
		services('[{"component": "main","capability": "samsungim.bixbyContent","command": "bixbyCommand","arguments": ["search_all", "' + settings.stGalaxyOrder + '"]}]')
	}
}

def services(data) {
	def params = [
		uri: "https://api.smartthings.com/v1/devices/$settings.stGalaxyHomeID/commands",
		headers: ["Authorization": "Bearer $settings.stToken"],
		requestContentType: "application/json",
		body: data
	]
	try {
		httpPost(params) { resp ->
			return true
		}
	} catch (e) {
		log.error "SmartThings Services({$service}) Error: $e"
		return false
	}
}