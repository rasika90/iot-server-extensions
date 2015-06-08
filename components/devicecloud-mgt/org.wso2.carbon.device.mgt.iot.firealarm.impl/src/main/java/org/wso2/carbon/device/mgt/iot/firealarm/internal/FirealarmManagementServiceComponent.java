/*
 * Copyright (c) 2015, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * you may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.wso2.carbon.device.mgt.iot.firealarm.internal;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.wso2.carbon.device.mgt.iot.firealarm.impl.FireAlarmManager;
import org.wso2.carbon.ndatasource.core.DataSourceService;
import org.wso2.carbon.device.mgt.common.spi.DeviceMgtService;

///**
// * @scr.component name="org.wso2.carbon.device.mgt.iot.firealarm.internal.FirealarmManagementServiceComponent"
// * immediate="true"
// * @scr.reference name="org.wso2.carbon.ndatasource"
// * interface="org.wso2.carbon.ndatasource.core.DataSourceService"
// * cardinality="1..1"
// * policy="dynamic"
// * bind="setDataSourceService"
// * unbind="unsetDataSourceService"
// */

@Component(name="org.wso2.carbon.device.mgt.iot.firealarm.internal.FirealarmManagementServiceComponent",
         immediate=true)
public class FirealarmManagementServiceComponent {
	

    private ServiceRegistration firealarmServiceRegRef;



    private static final Log log = LogFactory.getLog(FirealarmManagementServiceComponent.class);

    @Activate
    protected void activate(ComponentContext ctx) {
    	if (log.isDebugEnabled()) {
            log.debug("Activating Firealarm Device Management Service Component");
        }
        try {
            BundleContext bundleContext = ctx.getBundleContext();


            firealarmServiceRegRef =
                    bundleContext.registerService(DeviceMgtService.class.getName(), new
					FireAlarmManager(),
												  null);



            if (log.isDebugEnabled()) {
                log.debug("Firealarm Device Management Service Component has been successfully activated");
            }
        } catch (Throwable e) {
            log.error("Error occurred while activating Firealarm Device Management Service Component", e);
        }
    }

    @Deactivate
    protected void deactivate(ComponentContext ctx) {
        if (log.isDebugEnabled()) {
            log.debug("De-activating Firealarm Device Management Service Component");
        }
        try {
            if (firealarmServiceRegRef != null) {
                firealarmServiceRegRef.unregister();
            }

            if (log.isDebugEnabled()) {
                log.debug(
                        "Firealarm Device Management Service Component has been successfully de-activated");
            }
        } catch (Throwable e) {
            log.error("Error occurred while de-activating Firealarm Device Management bundle", e);
        }
    }


    
}