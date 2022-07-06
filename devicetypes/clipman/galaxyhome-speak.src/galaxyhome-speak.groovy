/**
 *  GalaxyHome Speak v2022-05-15
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
	definition (name: "GalaxyHome Speak", namespace: "clipman", author: "clipman", mnmn:"SmartThingsCommunity", vid: "339b1d41-c3fd-3475-9fb1-4985168ba900", ocfDeviceType: "x.com.st.d.voiceassistance") {
		capability "Speech Synthesis"
		capability "circlecircle06391.kakaoTalk"
	}
	preferences {
		input "stGalaxyHomeID", "text", title: "GalaxyHome Device ID", required: true
		input "stToken", "text", title: "SmartThings Token", required: true
	}
}

def speak(message) {
	services('[{"component": "main","capability": "speechSynthesis","command": "speak","arguments": ["' + message + '"]}]')
}

def sendMessage(message, image = "") {
	speak(message)
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
