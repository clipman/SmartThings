/**
 *  WakeOnLanButton v2022-06-14
 *  clipman@naver.com
 *  날자
 *  ST허브가 있어야 동작됩니다.
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
	definition (name: "WakeOnLanButton", namespace: "clipman", author: "clipman", ocfDeviceType: "x.com.st.d.remotecontroller", mnmn: "SmartThingsCommunity", vid: "05d8f755-c62d-376f-ad5c-54eb57befb7c") {
		capability "Button"
		capability 'Momentary'
	}
	preferences {
		input type: "paragraph", element: "paragraph", title: "만든이", description: "김민수 clipman@naver.com [날자]", displayDuringSetup: false
		input "macAddress", "text", title: "MAC Address", required: true
	}
}

def installed() {
	sendEvent(name: "supportedButtonValues", value: ['pushed'], displayed: false)
	sendEvent(name: "numberOfButtons", value: 1, displayed: false)
}

def push() {
	sendEvent(name: 'button', value: 'pushed', isStateChange: true)
    def result = new physicalgraph.device.HubAction (
       	"wake on lan $macAddress",
       	physicalgraph.device.Protocol.LAN,
       	null
    )    
    return result
}