package com.git.opengds.file.dxf.persistence;

import java.util.HashMap;
import java.util.List;

import com.git.opengds.user.domain.UserVO;

public interface QA10LayerCollectionDAO {

	public void createQA10LayerTb(UserVO userVO, HashMap<String, Object> qa20Layertb) throws RuntimeException;

	public void insertQA10Layer(UserVO userVO, HashMap<String, Object> qa10Layer) throws RuntimeException;

	public int insertQA10LayerCollection(UserVO userVO, HashMap<String, Object> insertCollectionQuery) throws RuntimeException;

	public void insertQA10LayerMetadata(UserVO userVO, HashMap<String, Object> insertQueryMap) throws RuntimeException;

	public void insertQA10Feature(UserVO userVO, HashMap<String, Object> insertQuertMap) throws RuntimeException;

	public HashMap<String, Object> selectQA10FeatureIdx(UserVO userVO, HashMap<String, Object> selectIdxqueryMap) throws RuntimeException;

	public int deleteQA10Feature(UserVO userVO, HashMap<String, Object> deleteFeatureMap) throws RuntimeException;

	public int insertQA10LayerCollectionBlocks(UserVO userVO, HashMap<String, Object> blocksQuery) throws RuntimeException;

	public void insertQA10LayercollectionBlockEntity(UserVO userVO, HashMap<String, Object> entitiesQuery) throws RuntimeException;

	public int insertQA10LayerCollectionTableCommon(UserVO userVO, HashMap<String, Object> tablesQuery) throws RuntimeException;

	public void insertQA10LayerCollectionTableLayers(UserVO userVO, HashMap<String, Object> hashMap) throws RuntimeException;

	public Integer selectQA10LayerCollectionIdx(UserVO userVO, HashMap<String, Object> selectLayerCollectionIdxQuery) throws RuntimeException;

	public List<HashMap<String, Object>> selectQA10LayerMetadataIdxs(UserVO userVO, HashMap<String, Object> metadataIdxQuery) throws RuntimeException;

	public int selectQA10LayerMetadataIdx(UserVO userVO, HashMap<String, Object> metadataIdxQuery) throws RuntimeException;
	
	public HashMap<String, Object> selectQA10LayerTableName(UserVO userVO, HashMap<String, Object> layerTbNameQuery) throws RuntimeException;

	public int dropLayer(UserVO userVO, HashMap<String, Object> dropLayerTbQuery) throws RuntimeException;

	public void deleteField(UserVO userVO, HashMap<String, Object> deleteLayerMetaQuery) throws RuntimeException;

	public Integer selectTableCommonIdx(UserVO userVO, HashMap<String, Object> tableIdxQuery) throws RuntimeException;

	public List<HashMap<String, Object>> selectBlockCommonIdx(UserVO userVO, HashMap<String, Object> tableIdxQuery) throws RuntimeException;

	public void updateQA10LayerMetadataLayerID(UserVO userVO, HashMap<String, Object> updateLayerNameQuery) throws RuntimeException;

	public int selectTableLayerIdx(UserVO userVO, HashMap<String, Object> selectTlIdxQuery) throws RuntimeException;

	public void updateTableLayerId(UserVO userVO, HashMap<String, Object> updateTlIdQuery) throws RuntimeException;

}
