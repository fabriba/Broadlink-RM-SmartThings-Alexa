/**
 *  Tasker-Tnes Switch
 *
 *  Copyright 2016 BeckyR
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License. You may obtain a copy of the License at:
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 *  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
 *  for the specific language governing permissions and limitations under the License.
 *
 *  1/7/17 - updated with better switch displays and use of device ID from user itsamti
 *   // 09/01/2016 - itsamti - Added new switch definition below 
  * 2017-09-05 - Fa: this was partly rewritten to integrate with TNES instead of RMBridge. 
  *          It will send a message as follows:
  *                title   = smartthings_command            //exactly that string, always the same)
  *                command = $deviceNetworkId_$toggle       //eg: HomeTheatre_on
  *  2017-10-28 - FA: added project on github
 */
 
 
metadata {
	
    definition (name: "Tasker-TNES Bridge title=smartthings_command, command=deviceNetworkId_toggle", namespace: "beckyricha - Fa", author: "BeckyR - Fa") {
		capability "Switch"

		command "onPhysical"
		command "offPhysical"
	}

	tiles {
		standardTile("switch", "device.switch", width: 2, height: 2, canChangeIcon: true) {
			state "off", label: '${currentValue}', action: "switch.on", icon: "st.switches.switch.off", backgroundColor: "#ffffff"
			state "on", label: '${currentValue}', action: "switch.off", icon: "st.switches.switch.on", backgroundColor: "#79b821"
		}
		standardTile("on", "device.switch", decoration: "flat") {
			state "default", label: 'On', action: "onPhysical", backgroundColor: "#ffffff"
		}
		standardTile("off", "device.switch", decoration: "flat") {
			state "default", label: 'Off', action: "offPhysical", backgroundColor: "#ffffff"
		}
        main "switch"
		details(["switch","on","off"])
	}
}

def parse(String description) {
	def pair = description.split(":")
	createEvent(name: pair[0].trim(), value: pair[1].trim())
}

def on() {
	sendEvent(name: "switch", value: "on")
	put_on('on')
}

def off() {
	sendEvent(name: "switch", value: "off")
	put_off('off')
}

def onPhysical() {
	sendEvent(name: "switch", value: "on", type: "physical")
	put_on('on')
}

def offPhysical() {
	sendEvent(name: "switch", value: "off", type: "physical")
	put_off('off')
}
 
private put_on(toggle) {
    def toReplace = device.deviceNetworkId
	def replaced = toReplace.replaceAll(' ', '_')
    
def params = [
    uri: "https://thacthab.herokuapp.com/broadcast/",
    body: "{\"login\":\"*****\",\"pass\":\"******\" ,\"title\":\"smartthings_command\" ,\"message\":\"{\\\"command\\\":\\\"${replaced}_${toggle}\\\"}\"}"
  ]

try {
    httpPostJson(params) { resp ->
        resp.headers.each {
            log.debug "${it.name} : ${it.value}"
        }
        log.debug "response contentType: ${resp.    contentType}"
    }
} catch (e) {
    log.debug "something went wrong: $e"
}
	
}

private put_off(toggle) {
    def toReplace = device.deviceNetworkId
	def replaced = toReplace.replaceAll(' ', '_')
   
def params = [
    uri: "https://thacthab.herokuapp.com/broadcast/",
    body: "{\"login\":\"***\",\"pass\":\"*****\" ,\"title\":\"smartthings_command\" ,\"message\":\"{\\\"command\\\":\\\"${replaced}_${toggle}\\\"}\"}"
  ]

try {
    httpPostJson(params) { resp ->
        resp.headers.each {
            log.debug "${it.name} : ${it.value}"
        }
        log.debug "response contentType: ${resp.    contentType}"
    }
} catch (e) {
    log.debug "something went wrong: $e"
}
	
}
