package com.git.opengds.geoserver.service;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface GeoserverLayerProxyService {
	public void requestWMSLayer(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException;
	public void requestGetFeature(HttpServletRequest request, HttpServletResponse response);
	public void requestGeoserverDataOutput(HttpServletRequest request, HttpServletResponse response);
}
