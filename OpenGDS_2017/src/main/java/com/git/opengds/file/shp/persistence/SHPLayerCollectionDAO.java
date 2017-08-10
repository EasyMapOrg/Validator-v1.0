package com.git.opengds.file.shp.persistence;

import java.util.HashMap;

import com.git.opengds.user.domain.UserVO;

public interface SHPLayerCollectionDAO {

	public int insertSHPLayerCollection(UserVO userVO, HashMap<String, Object> insertCollectionQuery);

	public void createSHPLayerTb(UserVO userVO, HashMap<String, Object> createLayerQuery);

	public void insertSHPLayer(UserVO userVO, HashMap<String, Object> insertLayerQuery);

	public void insertSHPLayerMetadata(UserVO userVO, HashMap<String, Object> insertLayerMeteQuery);

}
