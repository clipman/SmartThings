/**
 *  VirtualSwitch v2022-04-25
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
	definition (name: "VirtualSwitch", namespace: "clipman", author: "clipman", vid: "generic-switch") {
		capability "Switch"
	}
}

def on() {
	sendEvent(name: "switch", value: "on", displayed: true)
}

def off() {
	sendEvent(name: "switch", value: "off", displayed: true)
}

def installed() {
    off()
}