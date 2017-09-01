/*
 *    OpenGDS/Builder
 *    http://git.co.kr
 *
 *    (C) 2014-2017, GeoSpatial Information Technology(GIT)
 *    
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 3 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */

package com.git.opengds.parser.edit.layer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.git.gdsbuilder.edit.dxf.EditDXFLayer;
import com.git.gdsbuilder.edit.ngi.EditNGILayer;
import com.git.gdsbuilder.edit.shp.EditSHPLayer;
import com.git.gdsbuilder.type.ngi.header.NDAField;
import com.git.gdsbuilder.type.ngi.header.NDAHeader;
import com.git.gdsbuilder.type.ngi.header.NGIHeader;
import com.vividsolutions.jts.io.ParseException;

/**
 * JSONObject를 QA20Layer 객체로 파싱하는 클래스
 * 
 * @author DY.Oh
 * @Date 2017. 3. 11. 오후 2:14:38
 */
public class EditDTLayerParser {

	protected static String create = "create";
	protected static String modify = "modify";
	protected static String delete = "delete";

	JSONObject layerObj;
	String originLayerType;
	EditNGILayer editNGILayer;
	EditDXFLayer editDXFLayer;
	EditSHPLayer editSHPLayer;

	public static String getCreate() {
		return create;
	}

	public static void setCreate(String create) {
		EditDTLayerParser.create = create;
	}

	public static String getModify() {
		return modify;
	}

	public static void setModify(String modify) {
		EditDTLayerParser.modify = modify;
	}

	public static String getDelete() {
		return delete;
	}

	public static void setDelete(String delete) {
		EditDTLayerParser.delete = delete;
	}

	public JSONObject getLayerObj() {
		return layerObj;
	}

	public void setLayerObj(JSONObject layerObj) {
		this.layerObj = layerObj;
	}

	public String getType() {
		return originLayerType;
	}

	public void setType(String type) {
		this.originLayerType = type;
	}

	public EditNGILayer getEditNGILayer() {
		return editNGILayer;
	}

	public void setEditNGILayer(EditNGILayer editQA20Layer) {
		this.editNGILayer = editQA20Layer;
	}

	public EditDXFLayer getEditDXFLayer() {
		return editDXFLayer;
	}

	public void setEditDXFLayer(EditDXFLayer editQA10Layer) {
		this.editDXFLayer = editQA10Layer;
	}

	public EditSHPLayer getEditSHPLayer() {
		return editSHPLayer;
	}

	public void setEditSHPLayer(EditSHPLayer editSHPLayer) {
		this.editSHPLayer = editSHPLayer;
	}

	/**
	 * EditLayerParser 생성자
	 * 
	 * @param layerName
	 * 
	 * @param layerObj
	 * @param state
	 * @throws ParseException
	 */
	public EditDTLayerParser(String type, JSONObject layerObj, String state) throws ParseException {
		this.originLayerType = type;
		this.layerObj = layerObj;
		if (type.equals("ngi")) {
			if (state.equals(create)) {
				ngiCreatedLayerParse();
			} else if (state.equals(modify)) {
				ngiModifiedLayerParse();
			}
		} else if (type.equals("dxf")) {
			if (state.equals(create)) {
				dxfCreatedLayerParse();
			} else if (state.equals(modify)) {
				dxfModifiedLayerParse();
			}
		} else if (type.equals("shp")) {
			if (state.equals(create)) {
				shpCreatedLayerParse();
			} else if (state.equals(modify)) {
				shpModifiedLayerParse();
			}
		}
	}

	public void modifiedGeoserverLayerParse(String type) {

		JSONObject geoLayerObj = (JSONObject) layerObj.get("geoserver");
		// geoLayerObj.get("lbound");
		// geoLayerObj.get("style");
		String orignalName = (String) layerObj.get("originLayerName");
		String name = (String) layerObj.get("currentLayerName");
		String title = (String) geoLayerObj.get("title");
		String abstractContent = (String) geoLayerObj.get("summary");

		Map<String, Object> geoLayer = new HashMap<String, Object>();
		geoLayer.put("orignalName", orignalName);
		geoLayer.put("name", name);
		geoLayer.put("title", title);
		geoLayer.put("summary", abstractContent);
		geoLayer.put("attChangeFlag", true);

		if (type.equals("ngi")) {
			this.editNGILayer.setGeoServerLayer(geoLayer);
		} else if (type.equals("dxf")) {
			this.editDXFLayer.setGeoServerLayer(geoLayer);
		}
	}

	public void ngiModifiedLayerParse() {

		editNGILayer = new EditNGILayer();
		// name
		String orignName = (String) layerObj.get("originLayerName");
		String currentName = (String) layerObj.get("currentLayerName");
		editNGILayer.setOrignName(orignName);
		editNGILayer.setLayerName(currentName);

		// attr
		JSONArray updateAttrArray = (JSONArray) layerObj.get("updateAttr");

		// ndaHeader
		List<NDAField> updateAttr = parseAttrNGILayer(currentName, updateAttrArray);
		NDAHeader ndaHeader = new NDAHeader("1", updateAttr);
		editNGILayer.setNdaHeader(ndaHeader);

		// // bound
		// JSONArray boundArry = (JSONArray) layerObj.get("bound");
		// JSONArray minArry = (JSONArray) boundArry.get(0);
		// double minX = (Double) minArry.get(0);
		// double minY = (Double) minArry.get(1);
		//

		// JSONArray maxArry = (JSONArray) boundArry.get(1);
		// double maxX = (Double) maxArry.get(0);
		// double maxY = (Double) maxArry.get(1);
		// String boundStr = "BOUND(" + minX + ", " + minY + ", " + maxX + ", "
		// + maxY + ")";
		//
		// NGIHeader ngiHeader = new NGIHeader();
		// ngiHeader.setBound(boundStr);
		//
		// // represent
		// String represent = (String) layerObj.get("represent");
		//
		// // ngiHeader
		// editQA20Layer.setNgiHeader(ngiHeader);
		modifiedGeoserverLayerParse("ngi");
		editNGILayer.setModified(true);
	}

	private List<NDAField> parseAttrNGILayer(String layerName, JSONArray attrArry) {

		List<NDAField> fieldList = new ArrayList<NDAField>();
		for (int i = 0; i < attrArry.size(); i++) {
			JSONObject attrObj = (JSONObject) attrArry.get(i);
			String originFieldName = (String) attrObj.get("originFieldName");
			String fieldName = (String) attrObj.get("fieldName");
			String type = (String) attrObj.get("type");
			String decimalStr = "";
			if (type.equals("Double")) {
				Object decimalObj = attrObj.get("decimal");
				if (decimalObj != null) {
					long decimal = (long) decimalObj;
					decimalStr = String.valueOf(decimal);
				}
			}
			long size = (long) attrObj.get("size");
			String sizeStr = String.valueOf(size);
			boolean isUnique = (boolean) attrObj.get("isUnique");
			boolean isNotNull = (boolean) attrObj.get("nullable");
			NDAField fied = new NDAField(originFieldName, fieldName, type, sizeStr, decimalStr, isUnique, isNotNull);
			fieldList.add(fied);
		}
		return fieldList;
	}

	public void ngiCreatedLayerParse() throws ParseException {

		editNGILayer = new EditNGILayer();

		String layerName = (String) layerObj.get("layerName");
		editNGILayer.setLayerName(layerName);
		editNGILayer.setOrignName(layerName);

		String layerType = (String) layerObj.get("layerType");
		editNGILayer.setLayerType(layerType);

		NGIHeader ngiHeader = new NGIHeader();
		String mask = "MASK(" + layerType + ")";
		ngiHeader.setGeometric_metadata(mask);

		String version = layerObj.get("version").toString();
		ngiHeader.setVersion(version);

		String dim = layerObj.get("dim").toString();
		ngiHeader.setDim("DIM(" + dim + ")");

		JSONArray boundArr = (JSONArray) layerObj.get("bound");
		JSONArray minXY = (JSONArray) boundArr.get(0);
		JSONArray maxXY = (JSONArray) boundArr.get(1);
		String bound = "BOUND(" + minXY.get(0).toString() + ", " + minXY.get(1).toString() + ", "
				+ maxXY.get(0).toString() + ", " + maxXY.get(1).toString() + ")";
		ngiHeader.setBound(bound);
		String test = "1 REGIONATTR(SOLID, 1, 14606014, SOLID100, 14606014, 14606014)";
		ngiHeader.addRegion_represent(test);

		NDAHeader ndaHeader = new NDAHeader();
		ndaHeader.setVersion(version);
		List<NDAField> fieldList = new ArrayList<NDAField>();
		JSONArray attrArray = (JSONArray) layerObj.get("attr");
		for (int i = 0; i < attrArray.size(); i++) {
			JSONObject attrObj = (JSONObject) attrArray.get(i);
			String fieldName = (String) attrObj.get("fieldName");
			String type = (String) attrObj.get("type");
			String decimalStr = "";
			if (type.equals("Double")) {
				long decimal = (long) attrObj.get("decimal");
				decimalStr = String.valueOf(decimal);
			}
			long size = (long) attrObj.get("size");
			String sizeStr = String.valueOf(size);
			boolean isUnique = (boolean) attrObj.get("isUnique");
			boolean isNotNull = (boolean) attrObj.get("nullable");
			NDAField fied = new NDAField(fieldName, type, sizeStr, decimalStr, isUnique, isNotNull);
			fieldList.add(fied);
		}
		ndaHeader.setAspatial_field_def(fieldList);

		editNGILayer.setNgiHeader(ngiHeader);
		editNGILayer.setNdaHeader(ndaHeader);
	}

	public void dxfModifiedLayerParse() {

		editDXFLayer = new EditDXFLayer();

		String originLayerName = (String) layerObj.get("originLayerName");
		String currentLayerName = (String) layerObj.get("currentLayerName");
		editDXFLayer.setLayerName(currentLayerName);
		editDXFLayer.setOrignName(originLayerName);

		modifiedGeoserverLayerParse("dxf");
	}

	public void dxfCreatedLayerParse() {

		editDXFLayer = new EditDXFLayer();

		String layerName = (String) layerObj.get("layerName");
		editDXFLayer.setLayerName(layerName);
		editDXFLayer.setOrignName(layerName);

		String originLayerType = (String) layerObj.get("layerType");
		editDXFLayer.setOriginLayerType(originLayerType.toUpperCase());

		String layerType = "";
		if (originLayerType.equals("LineString")) {
			layerType = "LineString";
		} else if (originLayerType.equals("Polyline")) {
			layerType = "LineString";
		} else if (originLayerType.equals("LWPolyline")) {
			layerType = "LineString";
		} else if (originLayerType.equals("Insert")) {
			layerType = "Point";
		} else if (originLayerType.equals("Text")) {
			layerType = "Point";
		} else if (originLayerType.equals("Solid")) {
			layerType = "Polygon";
		} else if (originLayerType.equals("Circle")) {
			layerType = "Polygon";
		} else if (originLayerType.equals("Arc")) {
			layerType = "LineString";
		}
		editDXFLayer.setLayerType(layerType.toUpperCase());
		System.out.println("");
	}

	public void shpModifiedLayerParse() {

	}

	public void shpCreatedLayerParse() {

	}
}
