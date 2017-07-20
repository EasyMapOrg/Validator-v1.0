package com.git.opengds.validator.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Service;
import org.springframework.test.context.ContextConfiguration;

import com.git.gdsbuilder.FileRead.dxf.writer.QA10FileWriter;
import com.git.gdsbuilder.FileRead.ngi.writer.QA20FileWriter;
import com.git.gdsbuilder.type.qa10.collection.QA10LayerCollection;
import com.git.gdsbuilder.type.qa10.layer.QA10Layer;
import com.git.gdsbuilder.type.qa10.structure.QA10Blocks;
import com.git.gdsbuilder.type.qa10.structure.QA10Header;
import com.git.gdsbuilder.type.qa10.structure.QA10Tables;
import com.git.gdsbuilder.type.qa20.collection.QA20LayerCollection;
import com.git.gdsbuilder.type.qa20.layer.QA20Layer;
import com.git.opengds.file.dxf.dbManager.QA10DBQueryManager;
import com.git.opengds.file.dxf.persistence.QA10LayerCollectionDAO;
import com.git.opengds.file.ngi.dbManager.QA20DBQueryManager;
import com.git.opengds.file.ngi.persistence.QA20LayerCollectionDAO;
import com.git.opengds.parser.error.ErrorLayerDXFExportParser;
import com.git.opengds.parser.error.ErrorLayerNGIExportParser;
import com.git.opengds.validator.dbManager.ErrorLayerDBQueryManager;
import com.git.opengds.validator.persistence.ErrorLayerDAO;
import com.vividsolutions.jts.io.ParseException;

@Service
@ContextConfiguration(locations = { "file:src/main/webapp/WEB-INF/spring/**/*.xml" })
public class ErrorLayerExportServiceImpl implements ErrorLayerExportService {

	@Inject
	private ErrorLayerDAO errLayerDAO;

	@Inject
	private QA20LayerCollectionDAO qa20LayerCollectionDAO;

	@Inject
	private QA10LayerCollectionDAO qa10LayerCollectionDAO;

	@Override
	public boolean exportErrorLayer(String format, String type, String name, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ParseException {

		Map<String, Object> fileMap = null;
		ErrorLayerDBQueryManager dbManager = new ErrorLayerDBQueryManager();

		try {
			// fileWrite
			HashMap<String, Object> selectQuery = dbManager.selectAllErrorFeaturesQuery(name);
			List<HashMap<String, Object>> errAllFeatures = errLayerDAO.selectAllErrorFeatures(selectQuery);
			String[] nameSplit = name.split("_");
			String collectionName = nameSplit[2];
			if (format.equals("ngi")) {
				QA20LayerCollection qa20LayerCollection = new QA20LayerCollection();
				// 기존 파일 layer 합쳐합쳐
				QA20DBQueryManager qa20dbManager = new QA20DBQueryManager();
				// 기존 도엽 Collection 가져오기
				qa20LayerCollection.setFileName(collectionName);
				HashMap<String, Object> selectLayerCollectionIdxQuery = qa20dbManager
						.getSelectQA20LayerCollectionIdx(collectionName);
				int cIdx = qa20LayerCollectionDAO.selectQA20LayerCollectionIdx(selectLayerCollectionIdxQuery);
				HashMap<String, Object> selectAllMetaIdxQuery = qa20dbManager.getSelectQA20LayerMetaDataIdxQuery(cIdx);
				List<HashMap<String, Object>> mIdxMapList = qa20LayerCollectionDAO
						.selectQA20LayerMetadataIdxs(selectAllMetaIdxQuery);
				// errlayer 합쳐합쳐
				QA20Layer errQA20Layer = ErrorLayerNGIExportParser.parseQA20ErrorLayer(name, errAllFeatures);
				for (int i = 0; i < mIdxMapList.size(); i++) {
					HashMap<String, Object> mIdxMap = mIdxMapList.get(i);
					int lmIdx = (Integer) mIdxMap.get("lm_idx");
					// layerMeata
					HashMap<String, Object> selectAllMetaQuery = qa20dbManager
							.getSelectAllQA20LayerMetaDataQuery(lmIdx);
					HashMap<String, Object> metaMap = qa20LayerCollectionDAO.selectQA20LayerMeata(selectAllMetaQuery);

					List<HashMap<String, Object>> textRepresenets = null;
					List<HashMap<String, Object>> regionRepresenets = null;
					List<HashMap<String, Object>> pointRepresenets = null;
					List<HashMap<String, Object>> lineRepresenets = null;
					List<HashMap<String, Object>> aspatialField = null;
					// tRepresent
					if ((Boolean) metaMap.get("ngi_mask_text")) {
						HashMap<String, Object> selectTextRepresentQuery = qa20dbManager
								.getSelectTextRepresentQuery(lmIdx);
						textRepresenets = qa20LayerCollectionDAO.selectTextRepresent(selectTextRepresentQuery);
					}
					// rRepresent
					if ((Boolean) metaMap.get("ngi_mask_region")) {
						HashMap<String, Object> selectRegionRepresentQuery = qa20dbManager
								.getSelectResionRepresentQuery(lmIdx);
						regionRepresenets = qa20LayerCollectionDAO.selectResionRepresent(selectRegionRepresentQuery);
					}
					// pRepresent
					if ((Boolean) metaMap.get("ngi_mask_point")) {
						HashMap<String, Object> selectPointRepresentQuery = qa20dbManager
								.getSelectPointRepresentQuery(lmIdx);
						pointRepresenets = qa20LayerCollectionDAO.selectPointRepresent(selectPointRepresentQuery);
					}
					// lRepresent
					if ((Boolean) metaMap.get("ngi_mask_linestring")) {
						HashMap<String, Object> selectLineRepresentQuery = qa20dbManager
								.getSelectLineRepresentQuery(lmIdx);
						lineRepresenets = qa20LayerCollectionDAO.selectLineStringRepresent(selectLineRepresentQuery);
					}
					HashMap<String, Object> selectNdaAspatialFieldQuery = qa20dbManager
							.getSelectNadAspatialFieldQuery(lmIdx);
					aspatialField = qa20LayerCollectionDAO.selectNdaAspatialField(selectNdaAspatialFieldQuery);
					// layerTB
					String layerTbName = (String) metaMap.get("layer_t_name");
					HashMap<String, Object> selectAllFeaturesQuery = qa20dbManager
							.getSelectAllFeaturesQuery(layerTbName, aspatialField);
					List<HashMap<String, Object>> featuresMapList = qa20LayerCollectionDAO
							.selectAllQA20Features(selectAllFeaturesQuery);

					QA20Layer qa20Layer = ErrorLayerNGIExportParser.parseQA20Layer(metaMap, featuresMapList,
							pointRepresenets, lineRepresenets, regionRepresenets, textRepresenets, aspatialField);
					qa20LayerCollection.addQA20Layer(qa20Layer);
				}
				QA20FileWriter qa20Writer = new QA20FileWriter();
				fileMap = qa20Writer.writeNGIFile(qa20LayerCollection, errQA20Layer);
				String fileName = (String) fileMap.get("fileName");
				// ngi
				String ngiFileDir = (String) fileMap.get("NgifileDir");
				layerFileOutputStream(fileName, ngiFileDir, response);
				// nda
				String ndaFileDir = (String) fileMap.get("NdafileDir");
				layerFileOutputStream(fileName, ndaFileDir, response);
			} else if (format.equals("dxf")) {
				QA10LayerCollection qa10LayerCollection = new QA10LayerCollection();
				qa10LayerCollection.setCollectionName(collectionName);

				QA10DBQueryManager qa10dbQueryManager = new QA10DBQueryManager();

				// collectionIdx
				HashMap<String, Object> selectLayerCollectionIdxQuery = qa10dbQueryManager
						.getSelectLayerCollectionIdx(collectionName);
				int cIdx = qa10LayerCollectionDAO.selectQA10LayerCollectionIdx(selectLayerCollectionIdxQuery);

				HashMap<String, Object> selectAllMetaIdxQuery = qa10dbQueryManager.getSelectLayerMetaDataIdx(cIdx);
				List<HashMap<String, Object>> mIdxMapList = qa10LayerCollectionDAO
						.selectQA10LayerMetadataIdxs(selectAllMetaIdxQuery);

				List<LinkedHashMap<String, Object>> blocks = new ArrayList<LinkedHashMap<String, Object>>();
				for (int i = 0; i < mIdxMapList.size(); i++) {
					HashMap<String, Object> mIdxMap = mIdxMapList.get(i);
					int lmIdx = (Integer) mIdxMap.get("lm_idx");
					// layerMeata
					HashMap<String, Object> selectMetaQuery = qa10dbQueryManager.getSelectQA10LayerMetaDataQuery(lmIdx);
					HashMap<String, Object> metaMap = qa10LayerCollectionDAO.selectQA10LayerMeata(selectMetaQuery);

					String layerId = (String) metaMap.get("layer_id");
					String[] typeSplit = layerId.split("_");
					String layerType = typeSplit[1];
					String layerTbName = (String) metaMap.get("layer_t_name");

					// blockCommons
					String id = typeSplit[0];
					HashMap<String, Object> selectBlockCommonQuery = qa10dbQueryManager.getSelectBlockCommon(cIdx, id);
					HashMap<String, Object> blockCommonMap = qa10LayerCollectionDAO
							.selectQA10layerBlocksCommon(selectBlockCommonQuery);
					if (blockCommonMap != null) {
						LinkedHashMap<String, Object> block = new LinkedHashMap<String, Object>();
						LinkedHashMap<String, Object> blockCommons = new LinkedHashMap<String, Object>(blockCommonMap);
						block.put("block", blockCommons);

						int bcIdx = (Integer) blockCommonMap.get("bc_idx");
						// blockEntities
						List<LinkedHashMap<String, Object>> entities = new ArrayList<LinkedHashMap<String, Object>>();

						// arc
						HashMap<String, Object> selectBlockArcList = qa10dbQueryManager.getSelectBlockArc(bcIdx);
						List<HashMap<String, Object>> blockArcMapList = qa10LayerCollectionDAO
								.selectBlockEntities(selectBlockArcList);
						if (blockArcMapList != null) {
							for (int j = 0; j < blockArcMapList.size(); j++) {
								HashMap<String, Object> blockArcMap = blockArcMapList.get(j);
								LinkedHashMap<String, Object> entity = new LinkedHashMap<String, Object>(blockArcMap);
								entities.add(entity);
							}
						}
						// circle
						HashMap<String, Object> selectBlockCircleList = qa10dbQueryManager.getSelectBlockCircle(bcIdx);
						List<HashMap<String, Object>> blockCircleMapList = qa10LayerCollectionDAO
								.selectBlockEntities(selectBlockCircleList);
						if (blockCircleMapList != null) {
							for (int j = 0; j < blockCircleMapList.size(); j++) {
								HashMap<String, Object> blockCircleMap = blockCircleMapList.get(j);
								LinkedHashMap<String, Object> entity = new LinkedHashMap<String, Object>(
										blockCircleMap);
								entities.add(entity);
							}
						}
						// polyline
						HashMap<String, Object> selectBlockPolylineList = qa10dbQueryManager
								.getSelectBlockPolyline(bcIdx);
						List<HashMap<String, Object>> blockPolylineMapList = qa10LayerCollectionDAO
								.selectBlockEntities(selectBlockPolylineList);
						if (blockPolylineMapList != null) {
							for (int j = 0; j < blockPolylineMapList.size(); j++) {
								HashMap<String, Object> blockPolylineMap = blockPolylineMapList.get(j);
								LinkedHashMap<String, Object> polylineEntity = new LinkedHashMap<String, Object>(
										blockPolylineMap);
								int bpIdx = (Integer) blockPolylineMap.get("bp_idx");
								// vertext
								HashMap<String, Object> selectBlockVertexList = qa10dbQueryManager
										.getSelectBlockVertex(bpIdx);
								List<HashMap<String, Object>> blockVertexMapList = qa10LayerCollectionDAO
										.selectBlockEntities(selectBlockVertexList);
								if (blockVertexMapList != null) {
									List<LinkedHashMap<String, Object>> vertexEntityList = new ArrayList<LinkedHashMap<String, Object>>();
									for (int k = 0; k < blockVertexMapList.size(); k++) {
										LinkedHashMap<String, Object> vertextEntity = new LinkedHashMap<String, Object>(
												blockVertexMapList.get(k));
										vertexEntityList.add(vertextEntity);
									}
									polylineEntity.put("vertexs", vertexEntityList);
								}
							}
						}
						// text
						HashMap<String, Object> selectBlockTextList = qa10dbQueryManager.getSelectBlockText(bcIdx);
						List<HashMap<String, Object>> blockTextMapList = qa10LayerCollectionDAO
								.selectBlockEntities(selectBlockTextList);
						if (blockTextMapList != null) {
							for (int j = 0; j < blockTextMapList.size(); j++) {
								HashMap<String, Object> blockTextMap = blockTextMapList.get(j);
								LinkedHashMap<String, Object> entity = new LinkedHashMap<String, Object>(blockTextMap);
								entities.add(entity);
							}
						}
						block.put("entities", entities);
						blocks.add(block);
					}

					// Entities
					HashMap<String, Object> selectFeaturesQuery = qa10dbQueryManager.getSelectFeatureQuery(layerTbName,
							layerType);
					List<HashMap<String, Object>> featuresMapList = qa10LayerCollectionDAO
							.selectQA10Features(selectFeaturesQuery);

					QA10Layer qa10Layer = ErrorLayerDXFExportParser.parseQA10Layer(layerId, featuresMapList);
					qa10Layer.setLayerType(layerType);
					qa10LayerCollection.addQA10Layer(qa10Layer);
				}

				// setDefaultHeaderValues
				QA10Header header = new QA10Header();
				header.setDefaultHeaderValues();
				qa10LayerCollection.setHeader(header);

				// tableCommonValue
				HashMap<String, Object> selectTablesCommonsQuery = qa10dbQueryManager.getSelectTableCommon(cIdx);
				HashMap<String, Object> tablesCommonMap = qa10LayerCollectionDAO
						.selectTablesCommon(selectTablesCommonsQuery);
				int tcIdx = (Integer) tablesCommonMap.get("tc_idx");

				// tableLayerValue
				HashMap<String, Object> selectTablesLayerQuery = qa10dbQueryManager.getSelectTableLayer(tcIdx);
				List<HashMap<String, Object>> tablesLayerMap = qa10LayerCollectionDAO
						.selectTablesLayer(selectTablesLayerQuery);

				// setDefaultTableLtype
				QA10Tables tables = new QA10Tables();
				tables.setDefaultLineTypeValues();
				tables.setLineTypes(true);
				tables.setDefaultStyleValues();
				tables.setStyles(true);
				tables.setLayerValues(tablesCommonMap, tablesLayerMap);
				tables.setLayers(true);

				QA10FileWriter qa10Writer = new QA10FileWriter();
				fileMap = qa10Writer.writeDxfFile(qa10LayerCollection);
				String fileName = (String) fileMap.get("fileName");
				String dxfDir = (String) fileMap.get("fileDxfDir");
				layerFileOutputStream(fileName, dxfDir, response);
			}
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	private boolean layerFileOutputStream(String fileName, String fileDir, HttpServletResponse response) {

		try {
			// fileOutput
			response.setContentType("application/octet-stream");
			response.setHeader("Content-Disposition", "attachment;filename=" + fileName);

			File file = new File(fileDir);
			FileInputStream fileIn = new FileInputStream(file);
			ServletOutputStream out = response.getOutputStream();

			byte[] outputByte = new byte[4096];
			while (fileIn.read(outputByte, 0, 4096) != -1) {
				out.write(outputByte, 0, 4096);
			}
			fileIn.close();
			out.flush();
			out.close();
		} catch (Exception e) {
			return false;
		}
		return true;
	}
}
