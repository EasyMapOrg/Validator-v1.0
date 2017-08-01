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

package com.git.opengds.editor.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.test.context.ContextConfiguration;

import com.git.gdsbuilder.edit.qa10.EditQA10Collection;
import com.git.gdsbuilder.edit.qa20.EditQA20Collection;
import com.git.gdsbuilder.type.qa10.feature.QA10Feature;
import com.git.gdsbuilder.type.qa10.layer.QA10Layer;
import com.git.gdsbuilder.type.qa10.structure.QA10Tables;
import com.git.gdsbuilder.type.qa20.collection.QA20LayerCollection;
import com.git.gdsbuilder.type.qa20.feature.QA20Feature;
import com.git.gdsbuilder.type.qa20.header.NDAField;
import com.git.gdsbuilder.type.qa20.header.NDAHeader;
import com.git.gdsbuilder.type.qa20.header.NGIHeader;
import com.git.gdsbuilder.type.qa20.layer.QA20Layer;
import com.git.gdsbuilder.type.qa20.layer.QA20LayerList;
import com.git.opengds.file.dxf.dbManager.QA10DBQueryManager;
import com.git.opengds.file.dxf.persistence.QA10LayerCollectionDAO;
import com.git.opengds.file.ngi.dbManager.QA20DBQueryManager;
import com.git.opengds.file.ngi.persistence.QA20LayerCollectionDAO;
import com.git.opengds.geoserver.service.GeoserverService;
import com.git.opengds.user.domain.UserVO;
import com.git.opengds.validator.persistence.ValidateProgressDAO;

@Service
@ContextConfiguration(locations = { "file:src/main/webapp/WEB-INF/spring/**/*.xml" })
public class EditDBManagerServiceImpl implements EditDBManagerService {

	/*@Inject
	private DataSourceTransactionManager txManager;*/

	@Inject
	private QA20LayerCollectionDAO qa20DAO;

	@Inject
	private QA10LayerCollectionDAO qa10DAO;	

	@Inject
	private ValidateProgressDAO progressDAO;

	@Autowired
	private GeoserverService geoserverService;
	
/*	public EditDBManagerServiceImpl(UserVO userVO) {
		// TODO Auto-generated constructor stub
		qa20DAO = new QA20LayerCollectionDAOImpl(userVO);
		qa10DAO = new QA10LayerCollectionDAOImpl(userVO);
		qa20DBManager = new QA20DBManagerServiceImpl(userVO);
		qa10DBManager = new QA10DBManagerServiceImpl(userVO);
		geoserverService = new GeoserverServiceImpl(userVO);
	}*/

	public Integer checkQA20LayerCollectionName(UserVO userVO, String collectionName) throws RuntimeException{

		QA20DBQueryManager queryManager = new QA20DBQueryManager();
		HashMap<String, Object> queryMap = queryManager.getSelectQA20LayerCollectionIdx(collectionName);
		Integer cIdx = qa20DAO.selectQA20LayerCollectionIdx(userVO,queryMap);
		if (cIdx == null) {
			return null;
		} else {
			return cIdx;
		}
	}

	@Override
	public Integer createQA20LayerCollection(UserVO userVO, String type, EditQA20Collection editCollection) throws RuntimeException {

		String collectionName = editCollection.getCollectionName();

		QA20LayerList createLayerList = editCollection.getCreateLayerList();
		QA20LayerCollection createCollection = new QA20LayerCollection();
		createCollection.setFileName(collectionName);
		createCollection.setQa20LayerList(createLayerList);

		QA20DBQueryManager queryManager = new QA20DBQueryManager();
		HashMap<String, Object> insertCollectionQuery = queryManager.getInsertQA20LayerCollectionQuery(collectionName);
		int cIdx = qa20DAO.insertQA20LayerCollection(userVO,insertCollectionQuery);

		return cIdx;
	}

	@Override
	public Integer createQA10LayerCollection(UserVO userVO, String type, EditQA10Collection editCollection) throws RuntimeException {

		String collectionName = editCollection.getCollectionName();

		QA10DBQueryManager queryManager = new QA10DBQueryManager();
		HashMap<String, Object> insertCollectionQuery = queryManager.getInsertLayerCollection(collectionName);
		int cIdx = qa10DAO.insertQA10LayerCollection(userVO,insertCollectionQuery);

		return cIdx;
	}

	public void deleteQA10LayerCollection(UserVO userVO, int cIdx) throws RuntimeException{

		QA10DBQueryManager queryManager = new QA10DBQueryManager();

		HashMap<String, Object> deleteTableCommonsQuery = queryManager.getDeleteTables(cIdx);
		qa10DAO.deleteField(userVO, deleteTableCommonsQuery);

		HashMap<String, Object> deleteValidateProgressQuery = queryManager.getDeleteQA10ProgressQuery(cIdx);
		progressDAO.deleteQA10Progress(deleteValidateProgressQuery);

		HashMap<String, Object> deleteLayerCollectionQuery = queryManager.getDeleteLayerCollection(cIdx);
		qa10DAO.deleteField(userVO, deleteLayerCollectionQuery);
	}

	public void deleteQA20LayerCollection(UserVO userVO, int cIdx) throws RuntimeException{

		QA20DBQueryManager queryManager = new QA20DBQueryManager();

		HashMap<String, Object> deleteValidateProgressQuery = queryManager.getDeleteQA10ProgressQuery(cIdx);
		progressDAO.deleteQA20Progress(deleteValidateProgressQuery);

		HashMap<String, Object> deleteLayerCollectionQuery = queryManager.getDeleteLayerCollection(cIdx);
		qa20DAO.deleteField(userVO, deleteLayerCollectionQuery);
	}

	@Override
	public boolean createQA20Layer(UserVO userVO, String type, Integer idx, String collectionName, QA20Layer qa20Layer, String src)
			throws RuntimeException {

		/*DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		TransactionStatus status = txManager.getTransaction(def);*/

		try {
			QA20DBQueryManager queryManager = new QA20DBQueryManager();

			// createQA20Layer
			HashMap<String, Object> createQuery = queryManager.getQA20LayerTbCreateQuery(type, collectionName,
					qa20Layer, src);
			qa20DAO.createQA20LayerTb(userVO,createQuery);

			// insertLayerMedata
			HashMap<String, Object> insertQueryMap = queryManager.getInsertQA20LayerMeataData(type, collectionName, idx,
					qa20Layer);
			int lmIdx = qa20DAO.insertQA20LayerMetadata(userVO,insertQueryMap);

			NDAHeader ndaHeader = qa20Layer.getNdaHeader();
			// aspatial_field_def
			List<HashMap<String, Object>> fieldDefs = queryManager.getAspatialFieldDefsInsertQuery(lmIdx, ndaHeader);
			if (fieldDefs != null) {
				for (int j = 0; j < fieldDefs.size(); j++) {
					qa20DAO.insertNdaAspatialFieldDefs(userVO,fieldDefs.get(j));
				}
			}
			NGIHeader ngiHeader = qa20Layer.getNgiHeader();
			// point_represent
			List<HashMap<String, Object>> ptReps = queryManager.getPtRepresentInsertQuery(lmIdx,
					ngiHeader.getPoint_represent());
			if (ptReps != null) {
				for (int j = 0; j < ptReps.size(); j++) {
					qa20DAO.insertPointRepresent(userVO,ptReps.get(j));
				}
			}
			// lineString_represent
			List<HashMap<String, Object>> lnReps = queryManager.getLnRepresentInsertQuery(lmIdx,
					ngiHeader.getLine_represent());
			if (lnReps != null) {
				for (int j = 0; j < lnReps.size(); j++) {
					qa20DAO.insertLineStringRepresent(userVO,lnReps.get(j));
				}
			}
			// region_represent
			List<HashMap<String, Object>> rgReps = queryManager.getRgRepresentInsertQuery(lmIdx,
					ngiHeader.getRegion_represent());
			if (rgReps != null) {
				for (int j = 0; j < rgReps.size(); j++) {
					qa20DAO.insertRegionRepresent(userVO,rgReps.get(j));
				}
			}
			// text_represent
			List<HashMap<String, Object>> txtReps = queryManager.getTxtRepresentInsertQuery(lmIdx,
					ngiHeader.getText_represent());
			if (txtReps != null) {
				for (int j = 0; j < txtReps.size(); j++) {
					qa20DAO.insertTextRepresent(userVO,txtReps.get(j));
				}
			}
		} catch (Exception e) {
			throw new RuntimeException();
		}
		return true;
	}

	@Override
	public void insertQA20CreateFeature(UserVO userVO, String tableName, QA20Feature createFeature, String src) throws RuntimeException{

		QA20DBQueryManager queryManager = new QA20DBQueryManager();
		HashMap<String, Object> insertQuertMap = queryManager.getInertQA20FeatureQuery(tableName, createFeature, src);
		qa20DAO.insertQA20Feature(userVO,insertQuertMap);
	}

	@Override
	public void updateQA20ModifyFeature(UserVO userVO, String tableName, QA20Feature modifyFeature, String src) throws RuntimeException{

		QA20DBQueryManager queryManager = new QA20DBQueryManager();

		/*DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		TransactionStatus status = txManager.getTransaction(def);*/

		try {
			// 1. featureID 조회
			String featureID = modifyFeature.getFeatureID();
			HashMap<String, Object> selectIdxqueryMap = queryManager.getSelectQA20FeatureIdxQuery(tableName, featureID);
			HashMap<String, Object> idxMap = qa20DAO.selectQA20FeatureIdx(userVO,selectIdxqueryMap);
			int idx = (Integer) idxMap.get("f_idx");

			// 2. 해당 feature 삭제
			HashMap<String, Object> deleteFeatureMap = queryManager.getDeleteQA20FeatureQuery(tableName, idx);
			qa20DAO.deleteQA20Feature(userVO,deleteFeatureMap);

			// 3. 다시 insert
			HashMap<String, Object> insertFeatureMap = queryManager.getInertQA20FeatureQuery(tableName, modifyFeature,
					src);
			qa20DAO.insertQA20Feature(userVO,insertFeatureMap);
		} catch (Exception e) {
//			txManager.rollback(status);
		}
//		txManager.commit(status);
	}

	@Override
	public boolean modifyQA20Layer(UserVO userVO, String type, Integer collectionIdx, String collectionName, QA20Layer qa20Layer,
			Map<String, Object> geoLayer) throws RuntimeException {

		/*DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		TransactionStatus status = txManager.getTransaction(def);*/

		QA20DBQueryManager queryManager = new QA20DBQueryManager();
		try {
			String orignName = qa20Layer.getOriginLayerName();
			HashMap<String, Object> queryMap = queryManager.getSelectQA20LayerMetaDataIdxQuery(collectionIdx,
					orignName);
			Integer lmIdx = qa20DAO.selectQA20LayerMetadataIdx(userVO,queryMap);

			// meta Tb - layerName update
			String currentName = qa20Layer.getLayerName();
			if (!currentName.equals(orignName)) {
				HashMap<String, Object> updateLayerNameQuery = queryManager.getUpdateQA20LayerMeataLayerNameQuery(lmIdx,
						currentName);
				qa20DAO.updateQA20LayerMetadataLayerName(userVO,updateLayerNameQuery);
			}

			NGIHeader ngiHeader = qa20Layer.getNgiHeader();
			// meta Tb - boundary update
			String boundary = ngiHeader.getBound();
			HashMap<String, Object> updateBoundaryQuery = queryManager.getUpdateQA20LayerMeataBoundaryQuery(lmIdx,
					boundary);
			qa20DAO.updateQA20LayerMetadataBoundary(userVO,updateBoundaryQuery);

			// ngi_point_rep Tb update

			// ngi_linestring_rep Tb update

			// ngi_region_rep Tb update

			// ngi_text_rep Tb update

			// nda_aspatial Tb update
			NDAHeader ndaHeader = qa20Layer.getNdaHeader();
			List<NDAField> fields = ndaHeader.getAspatial_field_def();
			for (int j = 0; j < fields.size(); j++) {
				// updated
				NDAField modifiedfield = fields.get(j);
				String originFieldName = modifiedfield.getOriginFieldName();

				// origin
				HashMap<String, Object> selectNadFieldsQuery = queryManager.getNdaAspatialFieldFidxQuery(lmIdx,
						originFieldName);
				HashMap<String, Object> fIdxMap = qa20DAO.selectNdaAspatialFieldFidxs(userVO,selectNadFieldsQuery);
				if (fIdxMap != null) {
					// update
					int fIdx = (Integer) fIdxMap.get("f_idx");
					HashMap<String, Object> updateFieldQuery = queryManager.updateNdaAspatialFieldQuery(fIdx,
							modifiedfield);
					qa20DAO.updateNdaAspatialField(userVO,updateFieldQuery);
				} else {
					// insert
					HashMap<String, Object> insertFieldQuery = queryManager.getAspatialFieldDefsInsertQuery(lmIdx,
							modifiedfield);
					qa20DAO.insertNdaAspatialFieldDefs(userVO,insertFieldQuery);
				}
			}
		} catch (Exception e) {
//			txManager.rollback(status);
			return false;
		}
		// update Geoserver
		String originalName = (String) geoLayer.get("orignalName");
		String name = (String) geoLayer.get("name");
		String title = (String) geoLayer.get("title");
		String summary = (String) geoLayer.get("summary");
		boolean attChangeFlag = (Boolean) geoLayer.get("attChangeFlag");
		String tableName = "geo_" + type + "_" + collectionName + "_" + originalName;
		boolean isSuccessed = geoserverService.updateFeatureType(userVO,tableName, name, title, summary, "", attChangeFlag);
		if (isSuccessed) {
//			txManager.commit(status);
			return true;
		} else {
			return false;
		}
	}

	@Override
	public List<HashMap<String, Object>> getQA20LayerMetadataIdx(UserVO userVO, Integer collectionIdx)throws RuntimeException {

		QA20DBQueryManager queryManager = new QA20DBQueryManager();
		HashMap<String, Object> queryMap = queryManager.getSelectQA20LayerMetaDataIdxQuery(collectionIdx);
		List<HashMap<String, Object>> cIdx = qa20DAO.selectQA20LayerMetadataIdxs(userVO,queryMap);
		if (cIdx == null) {
			return null;
		} else {
			return cIdx;
		}
	}

	@Override
	public void deleteQA20RemovedFeature(UserVO userVO, String tableName, String featureId) throws RuntimeException{

		QA20DBQueryManager queryManager = new QA20DBQueryManager();

		/*DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		TransactionStatus status = txManager.getTransaction(def);*/
		try {
			HashMap<String, Object> selectIdxqueryMap = queryManager.getSelectQA20FeatureIdxQuery(tableName, featureId);
			HashMap<String, Object> idxMap = qa20DAO.selectQA20FeatureIdx(userVO,selectIdxqueryMap);
			int idx = (Integer) idxMap.get("f_idx");

			HashMap<String, Object> deleteFeatureMap = queryManager.getDeleteQA20FeatureQuery(tableName, idx);
			qa20DAO.deleteQA20Feature(userVO,deleteFeatureMap);
		} catch (Exception e) {
//			txManager.rollback(status);
		}
//		txManager.commit(status);
	}

	@Override
	public boolean dropQA20Layer(UserVO userVO, String type, Integer collectionIdx, String collectionName, QA20Layer layer) throws RuntimeException{

		/*DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		TransactionStatus status = txManager.getTransaction(def);*/

		String layerName = layer.getLayerName();
		QA20DBQueryManager queryManager = new QA20DBQueryManager();

		try {
			HashMap<String, Object> metadataIdxQuery = queryManager.getSelectQA20LayerMetaDataIdxQuery(collectionIdx,
					layerName);
			Integer mIdx = qa20DAO.selectQA20LayerMetadataIdx(userVO,metadataIdxQuery);

			// ngi_text_represent 삭제
			HashMap<String, Object> deleteTextRepQuery = queryManager.getDeleteTextRepresentQuery(mIdx);
			qa20DAO.deleteField(userVO,deleteTextRepQuery);
			// ngi_point_represent 삭제
			HashMap<String, Object> deletePointRepQuery = queryManager.getDeletePointRepresentQuery(mIdx);
			qa20DAO.deleteField(userVO,deletePointRepQuery);
			// ngi_lineString_represent 삭제
			HashMap<String, Object> deleteLineStringRepQuery = queryManager.getDeleteLineStringRepresentQuery(mIdx);
			qa20DAO.deleteField(userVO,deleteLineStringRepQuery);
			// ngi_polygon_represent 삭제
			HashMap<String, Object> deleteRegionRepQuery = queryManager.getDeleteRegionRepresentQuery(mIdx);
			qa20DAO.deleteField(userVO,deleteRegionRepQuery);
			// nda_aspatial_field_def 삭제
			HashMap<String, Object> deleteAspatialFieldQuery = queryManager.getDeleteAsptialFieldQuery(mIdx);
			qa20DAO.deleteField(userVO,deleteAspatialFieldQuery);
			// layerMetadata 삭제
			HashMap<String, Object> deleteLayerMetaQuery = queryManager.getDeleteQA20LayerMetaQuery(mIdx);
			qa20DAO.deleteField(userVO,deleteLayerMetaQuery);

			HashMap<String, Object> dropQuery = queryManager.getQA20DropLayerQuery(type, collectionName, layerName);
			qa20DAO.dropLayer(userVO,dropQuery);

			// HashMap<String, Object> deleteLayerCollectionQuery = queryManager
			// .getDeleteQA20LayerCollectionQuery(collectionIdx);
			// qa20DAO.deleteField(deleteLayerCollectionQuery);
		} catch (Exception e) {
//			txManager.rollback(status);
			return false;
		}
		String layerTableName = "geo" + "_" + type + "_" + collectionName + "_" + layerName;
		String groupName = "gro" + "_" + type + "_" + collectionName;
		boolean isSuccessed = geoserverService.removeGeoserverLayer(userVO,groupName, layerTableName);
		if (isSuccessed) {
//			txManager.commit(status);
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void insertQA10CreateFeature(UserVO userVO, String tableName, QA10Feature createFeature) throws RuntimeException{

		QA10DBQueryManager queryManager = new QA10DBQueryManager();
		HashMap<String, Object> insertQuertMap = queryManager.getInertFeatureQuery(tableName, createFeature);
		qa10DAO.insertQA10Feature(userVO,insertQuertMap);
	}

	@Override
	public void updateQA10ModifyFeature(UserVO userVO, String tableName, QA10Feature modifyFeature) throws RuntimeException{

		QA10DBQueryManager queryManager = new QA10DBQueryManager();

		/*DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		TransactionStatus status = txManager.getTransaction(def);
*/
		try {
			// 1. featureID 조회
			String featureID = modifyFeature.getFeatureID();
			HashMap<String, Object> selectIdxqueryMap = queryManager.getSelectFeatureIdx(tableName, featureID);
			HashMap<String, Object> idxMap = qa10DAO.selectQA10FeatureIdx(userVO,selectIdxqueryMap);
			int idx = (Integer) idxMap.get("f_idx");

			// 2. 해당 feature 삭제
			HashMap<String, Object> deleteFeatureMap = queryManager.getDeleteFeature(tableName, idx);
			qa10DAO.deleteQA10Feature(userVO,deleteFeatureMap);

			// 3. 다시 insert
			HashMap<String, Object> insertFeatureMap = queryManager.getInertFeatureQuery(tableName, modifyFeature);
			qa10DAO.insertQA10Feature(userVO,insertFeatureMap);
		} catch (Exception e) {
//			txManager.rollback(status);
		}
//		txManager.commit(status);
	}

	@Override
	public void deleteQA10RemovedFeature(UserVO userVO, String tableName, String featureId) throws RuntimeException{

		QA10DBQueryManager queryManager = new QA10DBQueryManager();

		/*DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		TransactionStatus status = txManager.getTransaction(def);*/
		try {
			HashMap<String, Object> selectIdxqueryMap = queryManager.getSelectFeatureIdx(tableName, featureId);
			HashMap<String, Object> idxMap = qa10DAO.selectQA10FeatureIdx(userVO,selectIdxqueryMap);
			int idx = (Integer) idxMap.get("f_idx");

			HashMap<String, Object> deleteFeatureMap = queryManager.getDeleteFeature(tableName, idx);
			qa10DAO.deleteQA10Feature(userVO,deleteFeatureMap);
		} catch (Exception e) {
//			txManager.rollback(status);
		}
//		txManager.commit(status);

	}

	@Override
	public Integer checkQA10LayerCollectionName(UserVO userVO, String collectionName) throws RuntimeException{

		QA10DBQueryManager queryManager = new QA10DBQueryManager();
		HashMap<String, Object> queryMap = queryManager.getSelectLayerCollectionIdx(collectionName);
		Integer cIdx = qa20DAO.selectQA20LayerCollectionIdx(userVO,queryMap);
		if (cIdx == null) {
			return null;
		} else {
			return cIdx;
		}
	}

	@Override
	public boolean createQA10Layer(UserVO userVO, String type, Integer collectionIdx, String collectionName, QA10Layer createLayer,
			String src) throws RuntimeException{

		QA10DBQueryManager queryManager = new QA10DBQueryManager();

		/*DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		TransactionStatus status = txManager.getTransaction(def);*/

		try {
			HashMap<String, Object> createQuery = queryManager.qa10LayerTbCreateQuery(type, collectionName, createLayer,
					src);
			qa10DAO.createQA10LayerTb(userVO,createQuery);

			// insertQA10Layer
/*			List<HashMap<String, Object>> inertLayerQuerys = queryManager.qa10LayerTbInsertQuery(type, collectionName,
					createLayer, src);
			for (int j = 0; j < inertLayerQuerys.size(); j++) {
				HashMap<String, Object> insertLayerQuery = inertLayerQuerys.get(j);
				qa10DAO.insertQA10Layer(insertLayerQuery);
			}*/

			// insertLayerMetadata
			HashMap<String, Object> insertQueryMap = queryManager.getInsertLayerMeataData(type, collectionName,
					collectionIdx, createLayer);
			qa10DAO.insertQA10LayerMetadata(userVO,insertQueryMap);

			// tablesLayer
			QA10Tables tables = new QA10Tables();
			Map<String, Object> tbLayers = tables.getLayerValues(createLayer);
			HashMap<String, Object> tablesQuery = queryManager.getInsertTables(collectionIdx, tbLayers);
			int tbIdx = qa10DAO.insertQA10LayerCollectionTableCommon(userVO,tablesQuery);
			if (tables.isLayers()) {
				List<HashMap<String, Object>> layersQuery = queryManager.getInsertTablesLayers(tbIdx, tbLayers);
				for (int i = 0; i < layersQuery.size(); i++) {
					qa10DAO.insertQA10LayerCollectionTableLayers(userVO,layersQuery.get(i));
				}
			}
		} catch (Exception e) {
//			txManager.rollback(status);
			return false;
		}
//		txManager.commit(status);
		return true;
	}

	@Override
	public boolean dropQA10Layer(UserVO userVO, String type, Integer collectionIdx, String collectionName, QA10Layer layer) throws RuntimeException{

		/*DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		TransactionStatus status = txManager.getTransaction(def);*/

		String layerId = layer.getLayerID();
		String[] typeSplit = layerId.split("_");
		String id = typeSplit[0] + "_" + typeSplit[1];
	//	String id = typeSplit[0];

		try {
			QA10DBQueryManager dbManager = new QA10DBQueryManager();
			HashMap<String, Object> selectLayerCollectionIdxQuery = dbManager
					.getSelectLayerCollectionIdx(collectionName);
			Integer cIdx = qa10DAO.selectQA10LayerCollectionIdx(userVO,selectLayerCollectionIdxQuery);
			if (cIdx != null) {
				HashMap<String, Object> metadataIdxQuery = dbManager.getSelectQA10LayerMetaDataIdxQuery(cIdx, layerId);
				int mIdx = qa10DAO.selectQA10LayerMetadataIdx(userVO, metadataIdxQuery);
				// get layerTb name
				HashMap<String, Object> layerTbNameQuery = dbManager.getSelectLayerTableNameQuery(mIdx);
				HashMap<String, Object> layerTbNameMap = qa10DAO.selectQA10LayerTableName(userVO, layerTbNameQuery);
				// layerTb drop
				String layerTbName = (String) layerTbNameMap.get("layer_t_name");
				HashMap<String, Object> dropLayerTbQuery = dbManager.getDropLayer(layerTbName);
				qa10DAO.dropLayer(userVO,dropLayerTbQuery);
				// tables
				HashMap<String, Object> tableIdxQuery = dbManager.getSelectTableCommonIdx(cIdx);
				Integer tcIdx = qa10DAO.selectTableCommonIdx(userVO,tableIdxQuery);
				if (tcIdx != null) {
					// tables - layer
					HashMap<String, Object> deleteTableLayersQuery = dbManager.getDeleteTableLayers(tcIdx, id);
					qa10DAO.deleteField(userVO,deleteTableLayersQuery);
					// blocks - commonIdx
					HashMap<String, Object> blocksIdxQuery = dbManager.getSelectBlockCommonIdx(cIdx, id);
					Integer bcIdx = qa10DAO.selectBlockCommonIdx(userVO,blocksIdxQuery);
					if (bcIdx != null) {
						// blocks - vertex
						HashMap<String, Object> deleteBlocksVertexQuery = dbManager.getDeleteBlockVertex(bcIdx);
						qa10DAO.deleteField(userVO,deleteBlocksVertexQuery);
						// blocks - polyline
						HashMap<String, Object> deleteBlocksPolylineQuery = dbManager.getDeleteBlockPolyline(bcIdx);
						qa10DAO.deleteField(userVO,deleteBlocksPolylineQuery);
						// blocks - text
						HashMap<String, Object> deleteBlocksTextQuery = dbManager.getDeleteBlockText(bcIdx);
						qa10DAO.deleteField(userVO,deleteBlocksTextQuery);
						// blocks - circle
						HashMap<String, Object> deleteBlocksCircleQuery = dbManager.getDeleteBlockCircle(bcIdx);
						qa10DAO.deleteField(userVO,deleteBlocksCircleQuery);
						// blocks - arc
						HashMap<String, Object> deleteBlocksArcQuery = dbManager.getDeleteBlockArc(bcIdx);
						qa10DAO.deleteField(userVO,deleteBlocksArcQuery);
						// blocks - commons
						HashMap<String, Object> deleteBlocksCommonsQuery = dbManager.getDeleteBlocks(bcIdx);
						qa10DAO.deleteField(userVO,deleteBlocksCommonsQuery);
					}
				}
				// layerMetadata 삭제
				HashMap<String, Object> deleteLayerMetaQuery = dbManager.getDeleteLayerMeta(mIdx);
				qa10DAO.deleteField(userVO,deleteLayerMetaQuery);
			}
		} catch (Exception e) {
//			txManager.rollback(status);
			return false;
		}
		String layerTableName = "geo" + "_" + type + "_" + collectionName + "_" + layerId;
		String groupName = "gro" + "_" + type + "_" + collectionName;
		boolean isSuccessed = geoserverService.removeGeoserverLayer(userVO,groupName, layerTableName);
		if (isSuccessed) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean modifyQA10Layer(UserVO userVO, String type, Integer collectionIdx, String collectionName, QA10Layer qa10Layer,
			Map<String, Object> geoLayer) throws RuntimeException{

	/*	DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		TransactionStatus status = txManager.getTransaction(def);*/

		QA10DBQueryManager queryManager = new QA10DBQueryManager();

		String orignId = qa10Layer.getOriginLayerID();
		String currentId = qa10Layer.getLayerID();
		try {
			HashMap<String, Object> queryMap = queryManager.getSelectQA10LayerMetaDataIdxQuery(collectionIdx, orignId);
			Integer lmIdx = qa10DAO.selectQA10LayerMetadataIdx(userVO,queryMap);

			// meta Tb - layerName update
			if (!currentId.equals(orignId)) {
				HashMap<String, Object> updateLayerNameQuery = queryManager.getUpdateQA10LayerMeataLayerIDQuery(lmIdx,
						currentId);
				qa10DAO.updateQA10LayerMetadataLayerID(userVO,updateLayerNameQuery);
			}

			// layerCollection_table_common
			HashMap<String, Object> selectTcIdxQuery = queryManager.getSelectTableCommonIdx(collectionIdx);
			int tcIdx = qa10DAO.selectTableCommonIdx(userVO,selectTcIdxQuery);

			// layerCollection_table_layer
			HashMap<String, Object> selectTlIdxQuery = queryManager.getSelectTableLayerIdx(tcIdx, orignId);
			int tlIdx = qa10DAO.selectTableLayerIdx(userVO,selectTlIdxQuery);

			// layerCollection_table_layer - layerId update
			HashMap<String, Object> updateTlIdQuery = queryManager.getUpdateTableLayerId(tlIdx, currentId);
			qa10DAO.updateTableLayerId(userVO,updateTlIdQuery);
		} catch (Exception e) {
//			txManager.rollback(status);
			return false;
		}
//		txManager.commit(status);
		// update Geoserver
		String originalName = (String) geoLayer.get("orignalName");
		String name = (String) geoLayer.get("name");
		String title = (String) geoLayer.get("title");
		String summary = (String) geoLayer.get("summary");
		boolean attChangeFlag = (Boolean) geoLayer.get("attChangeFlag");
		String tableName = "geo_" + type + "_" + collectionName + "_" + originalName;
		String tableNameCurrent = "geo_" + type + "_" + collectionName + "_" + currentId;
		boolean isSuccessed = geoserverService.updateFeatureType(userVO,tableName, tableNameCurrent, title, summary, "", attChangeFlag);
		if (isSuccessed) {
//			txManager.commit(status);
			return true;
		} else {
			return false;
		}
		// return true;
	}

	@Override
	public void deleteQA10LayerCollectionTablesCommon(UserVO userVO, Integer cIdx) throws RuntimeException{
		
		QA10DBQueryManager queryManager = new QA10DBQueryManager();
		HashMap<String, Object> deleteQuery = queryManager.getDeleteTables(cIdx); 
		qa10DAO.deleteField(userVO,deleteQuery);
	}
}
