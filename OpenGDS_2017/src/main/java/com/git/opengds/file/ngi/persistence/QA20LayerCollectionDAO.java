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

package com.git.opengds.file.ngi.persistence;

import java.util.HashMap;
import java.util.Map;

import org.postgresql.util.PSQLException;

public interface QA20LayerCollectionDAO {

	public int insertQA20LayerCollection(Map<String, Object> collection);

	public HashMap<String, Object> selectQA20LayerCollectionIdx(HashMap<String, Object> selectLayerCollectionIdxQuery);

	public void createQA20LayerTb(HashMap<String, Object> hashMap) throws PSQLException;

	public void insertQA20Layer(HashMap<String, Object> layer) throws PSQLException;

	public int dropLayer(HashMap<String, Object> dropQuery);

	public void insertQA20Feature(HashMap<String, Object> insertQuertMap);

	public int insertQA20LayerMetadata(HashMap<String, Object> metadata) throws PSQLException;

	public void insertNdaAspatialFieldDefs(HashMap<String, Object> fieldDef) throws PSQLException;

	public void insertPointRepresent(HashMap<String, Object> hashMap) throws PSQLException;

	public void insertLineStringRepresent(HashMap<String, Object> hashMap) throws PSQLException;

	public void insertRegionRepresent(HashMap<String, Object> hashMap) throws PSQLException;

	public void insertTextRepresent(HashMap<String, Object> hashMap) throws PSQLException;

	public HashMap<String, Object> selectCountAllFeatures(HashMap<String, Object> countquery);

	public HashMap<String, Object> selectFeatureIdx(HashMap<String, Object> selectQuery);

	public int deleteFeature(HashMap<String, Object> deleteQuery);

	public void insertFeature(HashMap<String, Object> insertQuery);

}
