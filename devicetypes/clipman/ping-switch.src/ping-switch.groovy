/**
 *  Ping Switch v2022-04-25
 *  clipman@naver.com
 *  날자
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License. You may obtain a copy of the License at:
 *
 *	  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 *  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
 *  for the specific language governing permissions and limitations under the License.
 *
 */

metadata {
	definition (name: "Ping Switch", namespace: "clipman", author: "clipman", ocfDeviceType: "oic.d.switch") {
		capability "Switch"
		capability "Refresh"
	}
	preferences {
		input type: "paragraph", element: "paragraph", title: "서버", description: "https://xxx.duckdns.org 또는 http://xxx.duckdns.org:8123"
		input "haURL", "text", title: "HomeAssistant URL", required: true
	}
}

def on() {
	sendEvent(name: "switch", value: state.switch)
}

def off() {
	sendEvent(name: "switch", value: state.switch)
}

def installed() {
	state.switch = "off"
	sendEvent(name: "switch", value: "off", displayed: false)
	runEvery1Minute(ping)
	refresh()
}

def refresh() {
	ping()
}

def ping() {
	unschedule(pingTimeout)
	runIn(2, pingTimeout)

	def params = [
		uri: settings.haURL,
		path: "/",
		requestContentType: "application/json"
	]
	try {
		httpGet(params) { resp ->
			if (resp.status == 200) {
				log.debug("온라인입니다.")
				unschedule(pingTimeout)
				state.switch = "on"
				sendEvent(name: "switch", value: "on", displayed: true)
			}
		}
	} catch (e) {
	}
}

def pingTimeout() {
	log.debug("오프라인입니다.")
   	state.switch = "off"
	sendEvent(name: "switch", value: "off", displayed: true)
}
