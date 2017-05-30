package com.git.gdsbuilder.geoserver.service;

import com.git.gdsbuilder.geoserver.service.wfs.WFSGetFeature;
import com.git.gdsbuilder.geoserver.service.wms.WMSGetFeatureInfo;
import com.git.gdsbuilder.geoserver.service.wms.WMSGetMap;

public interface GeoserverServiceManager {
	public void requestWFSGetFeature(WFSGetFeature feature);
	public void requestWMSFeatureInfo(WMSGetFeatureInfo feature);
	public void requestWMSGetMap(WMSGetMap feature);
}
