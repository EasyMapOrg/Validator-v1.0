package com.git.opengds.file.dxf.persistence;

import java.util.HashMap;
import java.util.List;

import org.postgresql.util.PSQLException;

public interface QA10LayerCollectionDAO {

	public void createQA10LayerTb(HashMap<String, Object> qa20Layertb) throws PSQLException;

	public void insertQA10Layer(HashMap<String, Object> qa10Layer);

	public int insertQA10LayerCollection(HashMap<String, Object> insertCollectionQuery);

	public void insertQA10LayerMetadata(HashMap<String, Object> insertQueryMap);

	public void insertQA10Feature(HashMap<String, Object> insertQuertMap);

	public HashMap<String, Object> selectQA10FeatureIdx(HashMap<String, Object> selectIdxqueryMap);

	public int deleteQA10Feature(HashMap<String, Object> deleteFeatureMap);

	public int insertQA10LayerCollectionBlocksCommon(HashMap<String, Object> blocksQuery);

	public void insertQA10LayercollectionBlockEntity(HashMap<String, Object> entitiesQuery);

	public int insertQA10LayerCollectionTableCommon(HashMap<String, Object> tablesQuery);

	public void insertQA10LayerCollectionTableLayers(HashMap<String, Object> hashMap);

	public Integer selectQA10LayerCollectionIdx(HashMap<String, Object> selectLayerCollectionIdxQuery);

	public List<HashMap<String, Object>> selectQA10LayerMetadataIdxs(HashMap<String, Object> metadataIdxQuery);

	public int selectQA10LayerMetadataIdx(HashMap<String, Object> metadataIdxQuery);

	public HashMap<String, Object> selectQA10LayerTableName(HashMap<String, Object> layerTbNameQuery);

	public int dropLayer(HashMap<String, Object> dropLayerTbQuery);

	public void deleteField(HashMap<String, Object> deleteLayerMetaQuery);

	public Integer selectTableCommonIdx(HashMap<String, Object> tableIdxQuery);

	public Integer selectBlockCommonIdx(HashMap<String, Object> tableIdxQuery);

	public List<HashMap<String, Object>> selectBlockCommonIdxs(HashMap<String, Object> tableIdxQuery);

	public void updateQA10LayerMetadataLayerID(HashMap<String, Object> updateLayerNameQuery);

	public int selectTableLayerIdx(HashMap<String, Object> selectTlIdxQuery);

	public void updateTableLayerId(HashMap<String, Object> updateTlIdQuery);

	public HashMap<String, Object> selectQA10LayerMeata(HashMap<String, Object> selectAllMetaQuery);

	public List<HashMap<String, Object>> selectQA10Features(HashMap<String, Object> selectFeaturesQuery);

	public HashMap<String, Object> selectTablesCommon(HashMap<String, Object> selectTablesCommonsQuery);

	public List<HashMap<String, Object>> selectTablesLayer(HashMap<String, Object> selectTablesLayerQuery);

	public int insertQA10LayercollectionBlockPolyline(HashMap<String, Object> polylineQuery);

	public void insertQA10LayercollectionBlockVertex(HashMap<String, Object> vertextInsertQuery);

	public HashMap<String, Object> selectQA10layerBlocksCommon(HashMap<String, Object> selectBlockCommonQuery);

	public List<HashMap<String, Object>> selectBlockEntities(HashMap<String, Object> selectBlockArcList);

	public int insertQA10LayercollectionBlockLWPolyline(HashMap<String, Object> polylineQuery);

}
