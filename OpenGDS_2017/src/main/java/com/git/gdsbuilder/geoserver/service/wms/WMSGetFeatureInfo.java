package com.git.gdsbuilder.geoserver.service.wms;

/**
 * WMS 서버가 제공하는 작업, 서비스, 데이터에 대한 메타데이터 목록을 해당 서버에 요청
 * @author SG.Lee
 * @Date 2017. 5. 29. 오후 2:14:37
 * */
public class WMSGetFeatureInfo {
	private final static String SERVICE = "WMS";
	private final static String REQUEST = "GetFeatureInfo";
	
	private String serverURL ="";
	private String version="1.0.0";
	private String layers="";
	private String styles="";
	private String srs="";
	private String bbox="";
	private int width=0;
	private int height=0;
	private String query_layers="";
	private String info_format="application/json";
	private int feature_count=1;
	private int x=0;
	private int y=0;
	private String exceptions = "application/vnd.ogc.se_xml"; 
	
	public WMSGetFeatureInfo(String serverURL, String version, String layers, String styles, String srs, String bbox, int width,
			int height, String query_layers, String info_format, int feature_count, int x, int y, String exceptions) {
		super();
		this.serverURL=serverURL;
		this.version = version;
		this.layers = layers;
		this.styles = styles;
		this.srs = srs;
		this.bbox = bbox;
		this.width = width;
		this.height = height;
		this.query_layers = query_layers;
		this.info_format = info_format;
		this.feature_count = feature_count;
		this.x = x;
		this.y = y;
		this.exceptions = exceptions;
	}
	
	public WMSGetFeatureInfo(String serverURL, String version, String layers, String styles, String srs, String bbox, int width,
			int height, String query_layers, int x, int y) {
		super();
		this.serverURL=serverURL;
		this.version = version;
		this.layers = layers;
		this.styles = styles;
		this.srs = srs;
		this.bbox = bbox;
		this.width = width;
		this.height = height;
		this.query_layers = query_layers;
		this.x = x;
		this.y = y;
	}

	public static String getService() {
		return SERVICE;
	}

	public static String getRequest() {
		return REQUEST;
	}
	public String getServerURL() {
		return serverURL;
	}
	public void setServerURL(String serverURL) {
		this.serverURL = serverURL;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getLayers() {
		return layers;
	}
	public void setLayers(String layers) {
		this.layers = layers;
	}
	public String getStyles() {
		return styles;
	}
	public void setStyles(String styles) {
		this.styles = styles;
	}
	public String getSrs() {
		return srs;
	}
	public void setSrs(String srs) {
		this.srs = srs;
	}
	public String getBbox() {
		return bbox;
	}
	public void setBbox(String bbox) {
		this.bbox = bbox;
	}
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = height;
	}
	public String getQuery_layers() {
		return query_layers;
	}
	public void setQuery_layers(String query_layers) {
		this.query_layers = query_layers;
	}
	public String getInfo_format() {
		return info_format;
	}
	public void setInfo_format(String info_format) {
		this.info_format = info_format;
	}
	public int getFeature_count() {
		return feature_count;
	}
	public void setFeature_count(int feature_count) {
		this.feature_count = feature_count;
	}
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	public String getExceptions() {
		return exceptions;
	}
	public void setExceptions(String exceptions) {
		this.exceptions = exceptions;
	}
	
	
	public String getWMSGetFeatureInfoURL(){
		StringBuffer urlBuffer = new StringBuffer();
		if(!this.serverURL.equals("")){
			urlBuffer.append(serverURL);
			urlBuffer.append("?");
			urlBuffer.append("request="+REQUEST);
			urlBuffer.append("&");
			urlBuffer.append("service="+SERVICE);
			if(!this.version.equals("")){
				urlBuffer.append("&");
				urlBuffer.append("version="+version);
			}
			if(!this.layers.equals("")){
				urlBuffer.append("&");
				urlBuffer.append("layers="+layers);
			}
			if(!this.styles.equals("")){
				urlBuffer.append("&");
				urlBuffer.append("styles="+styles);
			}
			if(!this.srs.equals("")){
				urlBuffer.append("&");
				urlBuffer.append("srs="+srs);
			}
			if(!this.info_format.equals("")){
				urlBuffer.append("&");
				urlBuffer.append("info_format="+info_format);
			}
			if(!this.bbox.equals("")){
				urlBuffer.append("&");
				urlBuffer.append("bbox="+bbox);
			}
			if(!this.query_layers.equals("")){
				urlBuffer.append("&");
				urlBuffer.append("query_layers="+query_layers);
			}
			if(!this.exceptions.equals("")){
				urlBuffer.append("&");
				urlBuffer.append("exceptions="+exceptions);
			}
			urlBuffer.append("&");
			urlBuffer.append("width="+String.valueOf(this.width));
			urlBuffer.append("&");
			urlBuffer.append("height="+String.valueOf(this.height));
			urlBuffer.append("&");
			urlBuffer.append("feature_count="+String.valueOf(this.feature_count));
			urlBuffer.append("&");
			urlBuffer.append("x="+String.valueOf(this.x));
			urlBuffer.append("&");
			urlBuffer.append("y="+String.valueOf(this.y));
		}
		else
			return "";
		return urlBuffer.toString();
	}
}
