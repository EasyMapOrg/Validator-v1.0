package com.git.opengds.file.dxf.persistence;

import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import org.apache.ibatis.session.SqlSession;
import org.postgresql.util.PSQLException;
import org.springframework.stereotype.Repository;

@Repository
public class QA10LayerCollectionDAOImpl implements QA10LayerCollectionDAO {

	@Inject
	private SqlSession sqlSession;

	private static final String namespace = "com.git.mappers.qa10Mappers.QA10LayerCollectionMapper";

	@Override
	public void createQA10LayerTb(HashMap<String, Object> qa10Layertb) throws PSQLException {
		sqlSession.update(namespace + ".createQA10LayerTb", qa10Layertb);
	}

	@Override
	public void insertQA10Layer(HashMap<String, Object> qa10Layer) {
		sqlSession.insert(namespace + ".insertQA10Layer", qa10Layer);
	}

	@Override
	public int insertQA10LayerCollection(HashMap<String, Object> insertCollectionQuery) {
		sqlSession.insert(namespace + ".insertQA10LayerCollection", insertCollectionQuery);
		return (Integer) insertCollectionQuery.get("c_idx");
	}

	@Override
	public void insertQA10LayerMetadata(HashMap<String, Object> insertQueryMap) {
		sqlSession.insert(namespace + ".insertQA10LayerMetadata", insertQueryMap);
	}

	@Override
	public void insertQA10Feature(HashMap<String, Object> insertQuertMap) {
		sqlSession.insert(namespace + ".insertQA10Feature", insertQuertMap);
	}

	@Override
	public HashMap<String, Object> selectQA10FeatureIdx(HashMap<String, Object> selectIdxqueryMap) {
		return sqlSession.selectOne(namespace + ".selectFeatureIdx", selectIdxqueryMap);
	}

	@Override
	public int deleteQA10Feature(HashMap<String, Object> deleteQuery) {
		return sqlSession.delete(namespace + ".deleteFeature", deleteQuery);
	}

	@Override
	public int insertQA10LayerCollectionBlocksCommon(HashMap<String, Object> blocksQuery) {
		sqlSession.insert(namespace + ".insertQA10LayerCollectionBlockCommon", blocksQuery);
		return (Integer) blocksQuery.get("bc_idx");
	}

	@Override
	public void insertQA10LayercollectionBlockEntity(HashMap<String, Object> entitiesQuery) {
		sqlSession.insert(namespace + ".insertQA10LayerCollectionBlockEntity", entitiesQuery);
	}

	@Override
	public int insertQA10LayerCollectionTableCommon(HashMap<String, Object> tablesQuery) {
		sqlSession.insert(namespace + ".insertQA10LayerCollectionTableCommon", tablesQuery);
		return (Integer) tablesQuery.get("tc_idx");
	}

	@Override
	public void insertQA10LayerCollectionTableLayers(HashMap<String, Object> layersQuery) {
		sqlSession.insert(namespace + ".insertQA10LayerCollectionTableLayer", layersQuery);
	}

	@Override
	public Integer selectQA10LayerCollectionIdx(HashMap<String, Object> selectLayerCollectionIdxQuery) {
		HashMap<String, Object> idxMap = sqlSession.selectOne(namespace + ".selectLayerCollectionIdx",
				selectLayerCollectionIdxQuery);
		return (Integer) idxMap.get("c_idx");
	}

	@Override
	public List<HashMap<String, Object>> selectQA10LayerMetadataIdxs(HashMap<String, Object> metadataIdxQuery) {
		return sqlSession.selectList(namespace + ".selectQA10LayerMetadataIdxs", metadataIdxQuery);
	}

	@Override
	public HashMap<String, Object> selectQA10LayerTableName(HashMap<String, Object> layerTbNameQuery) {
		return sqlSession.selectOne(namespace + ".selectQA10LayerTableName", layerTbNameQuery);
	}

	@Override
	public int dropLayer(HashMap<String, Object> dropLayerTbQuery) {
		return sqlSession.update(namespace + ".dropQA10Layer", dropLayerTbQuery);
	}

	@Override
	public void deleteField(HashMap<String, Object> deleteFieldQuery) {
		sqlSession.delete(namespace + ".deleteField", deleteFieldQuery);
	}

	@Override
	public Integer selectTableCommonIdx(HashMap<String, Object> tableIdxQuery) {
		HashMap<String, Object> idxMap = sqlSession.selectOne(namespace + ".selectTableCommonIdx", tableIdxQuery);
		return (Integer) idxMap.get("tc_idx");
	}

	@Override
	public Integer selectBlockCommonIdx(HashMap<String, Object> blockIdxQuery) {
		HashMap<String, Object> idxMap = sqlSession.selectOne(namespace + ".selectBlockCommonIdx", blockIdxQuery);
		if (idxMap != null) {
			return (Integer) idxMap.get("bc_idx");
		} else {
			return null;
		}
	}

	@Override
	public List<HashMap<String, Object>> selectBlockCommonIdxs(HashMap<String, Object> blockIdxQuery) {
		return sqlSession.selectList(namespace + ".selectBlockCommonIdxs", blockIdxQuery);
	}

	@Override
	public int selectQA10LayerMetadataIdx(HashMap<String, Object> metadataIdxQuery) {
		HashMap<String, Object> idxMap = sqlSession.selectOne(namespace + ".selectQA10LayerMetadataIdx",
				metadataIdxQuery);
		return (Integer) idxMap.get("lm_idx");
	}

	@Override
	public void updateQA10LayerMetadataLayerID(HashMap<String, Object> updateLayerNameQuery) {
		sqlSession.update(namespace + ".updateQA10LayerMetadataLayerID", updateLayerNameQuery);
	}

	@Override
	public int selectTableLayerIdx(HashMap<String, Object> selectTlIdxQuery) {
		HashMap<String, Object> idxMap = sqlSession.selectOne(namespace + ".selectTableLayerIdx", selectTlIdxQuery);
		return (Integer) idxMap.get("tl_idx");
	}

	@Override
	public void updateTableLayerId(HashMap<String, Object> updateTlIdQuery) {
		sqlSession.update(namespace + ".updateTableLayerID", updateTlIdQuery);
	}

	@Override
	public HashMap<String, Object> selectQA10LayerMeata(HashMap<String, Object> selectMetaQuery) {
		return sqlSession.selectOne(namespace + ".selectQA10LayerMeata", selectMetaQuery);
	}

	@Override
	public List<HashMap<String, Object>> selectQA10Features(HashMap<String, Object> selectFeaturesQuery) {
		return sqlSession.selectList(namespace + ".selectQA10Features", selectFeaturesQuery);
	}

	@Override
	public HashMap<String, Object> selectTablesCommon(HashMap<String, Object> selectTablesCommonsQuery) {
		return sqlSession.selectOne(namespace + ".selectTablesCommon", selectTablesCommonsQuery);
	}

	@Override
	public List<HashMap<String, Object>> selectTablesLayer(HashMap<String, Object> selectTablesLayerQuery) {
		return sqlSession.selectList(namespace + ".selectTablesLayer", selectTablesLayerQuery);
	}

	@Override
	public int insertQA10LayercollectionBlockPolyline(HashMap<String, Object> polylineQuery) {
		sqlSession.insert(namespace + ".insertQA10LayercollectionBlockPolyline", polylineQuery);
		return (Integer) polylineQuery.get("bp_idx");
	}

	@Override
	public void insertQA10LayercollectionBlockVertex(HashMap<String, Object> vertextInsertQuery) {
		sqlSession.insert(namespace + ".insertQA10LayercollectionBlockVertex", vertextInsertQuery);
	}

	@Override
	public HashMap<String, Object> selectQA10layerBlocksCommon(HashMap<String, Object> selectBlockCommonQuery) {
		return sqlSession.selectOne(namespace + ".selectQA10layerBlocksCommon", selectBlockCommonQuery);
	}

	@Override
	public List<HashMap<String, Object>> selectBlockEntities(HashMap<String, Object> selectBlockArcList) {
		List<HashMap<String, Object>> selectList = sqlSession.selectList(namespace + ".selectBlockEntities",
				selectBlockArcList);
		if (selectList.size() > 0) {
			return selectList;
		} else {
			return null;
		}
	}

	@Override
	public int insertQA10LayercollectionBlockLWPolyline(HashMap<String, Object> polylineQuery) {
		sqlSession.insert(namespace + ".insertQA10LayercollectionBlockLWPolyline", polylineQuery);
		return (Integer) polylineQuery.get("blp_idx");
	}
}
