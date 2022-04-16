/**
 *	SmartIP v2021-09-01
 *	clipman@naver.com
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
	definition (name: "SmartIP", namespace: "clipman", author: "clipman", ocfDeviceType: "oic.d.wirelessrouter",
		mnmn: "SmartThingsCommunity", vid: "eaa47a7a-5a7d-3ac3-806f-df51f112f21b") {
		capability "circlecircle06391.status"				//statusbar
		capability "Refresh"
	}
}

def setStatusbar(String status){
	sendEvent(name: "statusbar", value: status, displayed: true)
}

def installed() {
	refresh()
}

def uninstalled() {
	unschedule()
}

def updated() {
	refresh()
}

def refresh() {
	unschedule()
	getIPAddress()
	runEvery10Minutes(getIPAddress)
}

def getIPAddress() {
	def params = [
        uri: "https://api.ipify.org?format=json"
	]
	try {
		httpGet(params) {resp ->
			log.debug "response data: ${resp.data}"
			if (resp.status == 200) {
				if(resp.data.ip != null) {
					setStatusbar("IP: " + resp.data.ip)
				}
			}
		}
	} catch (e) {
		log.debug "Error: ${e}, uri: ${params.uri}"
	}
}