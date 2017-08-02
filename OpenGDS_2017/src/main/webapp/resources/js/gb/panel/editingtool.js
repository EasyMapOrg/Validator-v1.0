/**
 * 드래그 패널 객체를 정의한다.
 * 
 * @author yijun.so
 * @date 2017. 07.26
 * @version 0.01
 * @class gb.panel.EditingTool
 * @constructor
 */
var gb;
if (!gb)
	gb = {};
if (!gb.panel)
	gb.panel = {};
gb.panel.EditingTool = function(obj) {
	gb.panel.Base.call(this, obj);
	var options = obj ? obj : {};
	this.map = options.map ? options.map : undefined;
	this.featureRecord = options.featureRecord ? options.featureRecord : undefined;
	this.treeInstance = options.treeInstance ? options.treeInstance : undefined;
	this.selected = options.selected ? options.selected : undefined;
	this.infoURL = options.infoURL ? options.infoURL : undefined;
	this.wmsURL = options.wmsURL ? options.wmsURL : undefined;
	this.wfsURL = options.wfsURL ? options.wfsURL : undefined;

	this.layers = undefined;
	this.layer = undefined;

	this.features = new ol.Collection();
	this.tempSource = new ol.source.Vector({
		features : this.features
	});
	this.tempVector = new ol.layer.Vector({
		map : this.map,
		source : this.tempSource,
		style : new ol.style.Style({
			stroke : new ol.style.Stroke({
				color : 'rgba(0,0,0,0)',
				width : 1
			}),
			fill : new ol.style.Fill({
				color : 'rgba(0, 0, 0, 0)'
			}),
			image : new ol.style.Circle({
				radius : 10,
				fill : new ol.style.Fill({
					color : 'rgba(0,0,0,0)'
				})
			})
		})
	});

	this.btn = {
		selectBtn : undefined,
		drawBtn : undefined,
		moveBtn : undefined,
		rotateBtn : undefined,
		modiBtn : undefined,
		delBtn : undefined
	};
	this.isOn = {
		select : false,
		draw : false,
		move : false,
		remove : false,
		modify : false,
		rotate : false
	};
	this.interaction = {
		select : undefined,
		selectWMS : undefined,
		dragbox : undefined,
		draw : undefined,
		updateDraw : undefined,
		move : undefined,
		rotate : undefined,
		modify : undefined,
		remove : undefined
	};

	var that = this;
	var i1 = $("<i>").addClass("fa").addClass("fa-mouse-pointer").attr("aria-hidden", true);
	this.btn.selectBtn = $("<button>").addClass("gb-panel-editingtool-btn").click(function() {
		console.log("click select");
		that.select(that.updateSelected());
	}).append(i1);
	var float1 = $("<div>").css({
		"float" : "left"
	}).append(this.btn.selectBtn);

	var i2 = $("<i>").addClass("fa").addClass("fa-pencil").attr("aria-hidden", true);
	this.btn.drawBtn = $("<button>").addClass("gb-panel-editingtool-btn").click(function() {
		console.log("click draw");
		that.draw(that.updateSelected());
	}).append(i2);
	var float2 = $("<div>").css({
		"float" : "left"
	}).append(this.btn.drawBtn);

	var i3 = $("<i>").addClass("fa").addClass("fa-arrows").attr("aria-hidden", true);
	this.btn.moveBtn = $("<button>").addClass("gb-panel-editingtool-btn").click(function() {
		console.log("click move");
		that.move(that.updateSelected());
	}).append(i3);
	var float3 = $("<div>").css({
		"float" : "left"
	}).append(this.btn.moveBtn);

	var i4 = $("<i>").addClass("fa").addClass("fa-wrench").attr("aria-hidden", true);
	this.btn.modiBtn = $("<button>").addClass("gb-panel-editingtool-btn").click(function() {
		console.log("click modify");
		that.modify(that.updateSelected());
	}).append(i4);
	var float4 = $("<div>").css({
		"float" : "left"
	}).append(this.btn.modiBtn);

	var i5 = $("<i>").addClass("fa").addClass("fa-repeat").attr("aria-hidden", true);
	this.btn.rotateBtn = $("<button>").addClass("gb-panel-editingtool-btn").click(function() {
		console.log("click rotate");
		that.rotate(that.updateSelected());
	}).append(i5);
	var float5 = $("<div>").css({
		"float" : "left"
	}).append(this.btn.rotateBtn);

	var i6 = $("<i>").addClass("fa").addClass("fa-eraser").attr("aria-hidden", true);
	this.btn.delBtn = $("<button>").addClass("gb-panel-editingtool-btn").click(function() {
		console.log("click remove");
		that.remove(that.updateSelected());
	}).append(i6);
	var float6 = $("<div>").css({
		"float" : "left"
	}).append(this.btn.delBtn);

	$(this.panelBody).append(float1).append(float2).append(float3).append(float4).append(float5).append(float6);

	if (this.autoOpen) {
		this.open();
	} else {
		this.close();
	}
	$("body").append(this.panel);

	var fth1 = $("<th>").text("Index");
	var fth2 = $("<th>").text("Description");
	var ftr = $("<tr>").append(fth1).append(fth2);
	var fhd = $("<thead>").append(ftr);
	this.featureTB = $("<tbody>").mouseleave(function() {
		that.map.getView().fit(that.tempSelectSource.getExtent(), that.map.getSize());
	});
	var ftb = $("<table>").addClass("gb-table").append(fhd).append(this.featureTB);

	this.featurePop = new gb.panel.Base({
		"width" : "240px",
		"positionX" : 0,
		"positionY" : 0,
		"autoOpen" : false,
		"body" : ftb
	});

	$(this.featurePop.getPanel()).css({
		"max-height" : "300px",
		"overflow-y" : "auto"
	});
	var ath1 = $("<th>").text("Key");
	var ath2 = $("<th>").text("Value");
	var atr = $("<tr>").append(ath1).append(ath2);
	var ahd = $("<thead>").append(atr);
	this.attrTB = $("<tbody>");
	var atb = $("<table>").addClass("gb-table").append(ahd).append(this.attrTB);
	this.attrPop = new gb.panel.Base({
		"width" : "300px",
		"positionX" : 0,
		"positionY" : 0,
		"autoOpen" : false,
		"body" : atb
	});
	// var ahead = $("<div>").addClass("panel-heading").css({
	// "padding" : 0
	// }).append(" ");
	// var alist =
	// $("<div>").addClass("panel-body").addClass("gb-edit-sel-apan").css({
	// "max-height" : "300px",
	// "overflow-y" : "auto"
	// }).append(this.attrTB);
	// this.attrPop = $("<div>").css({
	// "width" : "250px",
	// // "max-height" : "300px",
	// "top" : 0,
	// "right" : 0,
	// "position" : "absolute",
	// "z-Index" : "999",
	// "margin-bottom" : 0
	// }).addClass("panel").addClass("panel-default").append(ahead).append(alist);
	// $("body").append(this.attrPop);
	// $(this.attrPop).hide();
	// $(this.attrPop).draggable({
	// appendTo : "body"
	// });

	this.map.on('postcompose', function(evt) {
		that.map.getInteractions().forEach(function(interaction) {
			if (interaction instanceof gb.interaction.MultiTransform) {
				if (interaction.getFeatures().getLength()) {
					interaction.drawMbr(evt);
				}
			}
		});
	});
};
gb.panel.EditingTool.prototype = Object.create(gb.panel.Base.prototype);
gb.panel.EditingTool.prototype.constructor = gb.panel.EditingTool;
/**
 * 피처목록을 생성한다.
 * 
 */
gb.panel.EditingTool.prototype.setFeatureList_ = function() {
	return this.interaction;
};
/**
 * 내부 인터랙션 구조를 반환한다.
 * 
 * @return {Mixed Obj} {select : ol.interaction.Select..}
 */
gb.panel.EditingTool.prototype.getInteractions_ = function() {
	return this.interaction;
};
/**
 * 내부 인터랙션 하나를 반환한다.
 * 
 * @return {Mixed Obj} {select : ol.interaction.Select..}
 */
gb.panel.EditingTool.prototype.getInteraction_ = function(key) {
	return this.interaction[key];
};
/**
 * 내부 인터랙션 구조를 설정한다.
 * 
 * @param {String}
 *            key - interaction name
 * @param {ol.interaction.Interaction}
 *            val - interaction
 */
gb.panel.EditingTool.prototype.setInteraction_ = function(key, val) {
	this.interaction[key] = val;
};
/**
 * 편집할 레이어를 설정한다.
 * 
 * @param {ol.layer.Base}
 *            layer - layer to edit
 */
gb.panel.EditingTool.prototype.setLayer = function(layer) {
	this.layer = layer;
};
/**
 * 편집중인 레이어를 반환한다.
 * 
 * @return {ol.layer.Base}
 */
gb.panel.EditingTool.prototype.getLayer = function() {
	return this.layer;
};

/**
 * 해당 인터랙션을 활성화 시킨다.
 * 
 * @param {String ||
 *            Array<String>} 활성화 시킬 인터랙션 이름
 */
gb.panel.EditingTool.prototype.activeIntrct_ = function(intrct) {
	// var that = this;
	var keys = Object.keys(this.getInteractions_());
	for (var i = 0; i < keys.length; i++) {
		if (this.getInteraction_(keys[i])) {
			this.getInteraction_(keys[i]).setActive(false);
		}
	}
	if (Array.isArray(intrct)) {
		for (var j = 0; j < intrct.length; j++) {
			this.getInteraction_(intrct[j]).setActive(true);
			if (intrct[j] === "select" || intrct[j] === "selectWMS" || intrct[j] === "dragbox") {
				this.isOn["select"] = true;
			} else {
				this.isOn[intrct[j]] = true;
			}
		}
	} else if (typeof intrct === "string") {
		this.getInteraction_(intrct).setActive(true);
		if (intrct === "select" || intrct === "selectWMS" || intrct[j] === "dragbox") {
			this.isOn["select"] = true;
		} else {
			this.isOn[intrct] = true;
		}
	}
};
/**
 * 해당 인터랙션을 비활성화 시킨다.
 * 
 * @param {String ||
 *            Array<String>} 인터랙션의 이름 또는 인터랙션 이름의 배열
 */
gb.panel.EditingTool.prototype.deactiveIntrct_ = function(intrct) {
	if (Array.isArray(intrct)) {
		for (var j = 0; j < intrct.length; j++) {
			if (!!this.interaction[intrct[j]]) {
				this.interaction[intrct[j]].setActive(false);
			}
			if (intrct[j] === "select" || intrct[j] === "selectWMS") {
				this.isOn["select"] = false;
				this.featurePop.close();
			} else {
				this.isOn[intrct[j]] = false;
				this.tempVector.setMap(this.map);
				this.map.removeLayer(this.managed);
			}
		}
	} else if (typeof intrct === "string") {
		if (!!this.interaction[intrct]) {
			this.interaction[intrct].setActive(false);
		}
		if (intrct === "select" || intrct === "selectWMS") {
			this.isOn["select"] = false;
		} else {
			this.isOn[intrct] = false;
			this.tempVector.setMap(this.map);
			this.map.removeLayer(this.managed);
		}
	}
	// this.map.removeLayer(this.managed);
};
/**
 * 버튼을 누른상태로 만든다
 * 
 * @param {String}
 *            button name
 */
gb.panel.EditingTool.prototype.activeBtn_ = function(btn) {
	if (!this.btn[btn].hasClass("active")) {
		this.btn[btn].addClass("active");
	}
	var keys = Object.keys(this.btn);
	for (var i = 0; i < keys.length; i++) {
		if (keys[i] !== btn) {
			if (this.btn[keys[i]].hasClass("active")) {
				this.btn[keys[i]].removeClass("active");
			}
		}
	}
};
/**
 * 버튼을 안 누른 상태로 만든다
 * 
 * @param {String}
 *            button name
 */
gb.panel.EditingTool.prototype.deactiveBtn_ = function(btn) {
	if (this.btn[btn].hasClass("active")) {
		this.btn[btn].removeClass("active");
	}
};
/**
 * 피처 선택을 활성화 한다
 * 
 * @param {ol.layer.Base}
 *            layer - 편집할 레이어
 */
gb.panel.EditingTool.prototype.select = function(layer) {
	var that = this;
	if (this.isOn.select) {
		if (!!this.interaction.selectWMS || !!this.interaction.select) {
			this.interaction.select.getFeatures().clear();
			this.deactiveIntrct_([ "dragbox", "select", "selectWMS" ]);
		}
		this.deactiveBtn_("selectBtn");
		this.isOn.select = false;
		return;
	}
	var sourceLayer;
	if (Array.isArray(layer)) {
		if (layer.length > 1) {
			console.error("please, select 1 layer");
			return;
		} else if (layer.length < 1) {
			console.error("no selected layer");
			return;
		} else {
			sourceLayer = layer[0];
		}
	} else if (layer instanceof ol.layer.Base) {
		sourceLayer = layer;
	} else {
		return;
	}

	if (sourceLayer instanceof ol.layer.Vector) {
		console.log("vector-select");
		if (!this.tempSelectSource) {
			this.tempSelectSource = new ol.source.Vector();
		} else {
			this.tempSelectSource.clear();
		}
		if (this.interaction.select instanceof ol.interaction.Select) {
			this.interaction.select.getFeatures().clear();
		}
		this.map.removeInteraction(this.interaction.select);
		var styles = [ new ol.style.Style({
			stroke : new ol.style.Stroke({
				color : 'rgba(0,153,255,1)',
				width : 2
			}),
			fill : new ol.style.Fill({
				color : 'rgba(255, 255, 255, 0.5)'
			})
		}), new ol.style.Style({
			image : new ol.style.Circle({
				radius : 10,
				fill : new ol.style.Fill({
					color : 'rgba(0,153,255,0.4)'
				})
			}),
			geometry : function(feature) {

				var coordinates;
				var geom;

				if (feature.getGeometry() instanceof ol.geom.MultiPolygon) {
					coordinates = feature.getGeometry().getCoordinates()[0][0];
					geom = new ol.geom.MultiPoint(coordinates);
				} else if (feature.getGeometry() instanceof ol.geom.Polygon) {
					coordinates = feature.getGeometry().getCoordinates()[0];
					geom = new ol.geom.MultiPoint(coordinates);
				} else if (feature.getGeometry() instanceof ol.geom.MultiLineString) {
					coordinates = feature.getGeometry().getCoordinates()[0];
					geom = new ol.geom.MultiPoint(coordinates);
				} else if (feature.getGeometry() instanceof ol.geom.LineString) {
					coordinates = feature.getGeometry().getCoordinates();
					geom = new ol.geom.MultiPoint(coordinates);
				} else if (feature.getGeometry() instanceof ol.geom.MultiPoint) {
					coordinates = feature.getGeometry().getCoordinates();
					geom = new ol.geom.MultiPoint(coordinates);
				} else if (feature.getGeometry() instanceof ol.geom.Point) {
					coordinates = [ feature.getGeometry().getCoordinates() ];
					geom = new ol.geom.MultiPoint(coordinates);
				}

				return geom;
			}
		}) ];
		this.interaction.select = new ol.interaction.Select({
			layers : [ sourceLayer ],
			toggleCondition : ol.events.condition.platformModifierKeyOnly,
			style : styles
		});
		this.map.addInteraction(this.interaction.select);
		this.map.removeInteraction(this.interaction.dragbox);
		this.interaction.dragbox = new ol.interaction.DragBox({
			condition : ol.events.condition.shiftKeyOnly
		});
		this.map.addInteraction(this.interaction.dragbox);
		this.interaction.dragbox.on('boxend', function() {
			that.interaction.select.getFeatures().clear();
			sourceLayer.getSource().forEachFeatureIntersectingExtent(this.getGeometry().getExtent(), function(feature) {
				that.interaction.select.getFeatures().push(feature);
			});
			// that.interaction.selectWMS.setExtent(this.getGeometry().getExtent());
		});
		this.interaction.select.getFeatures().on("change:length", function(evt) {
			that.features = that.interaction.select.getFeatures();
			$(that.featureTB).empty();
			that.tempSelectSource.clear();
			that.tempSelectSource.addFeatures(that.features.getArray());
			if (that.features.getLength() > 1) {
				that.featurePop.close();
				for (var i = 0; i < that.features.getLength(); i++) {
					var idx = that.features.item(i).getId().substring(that.features.item(i).getId().indexOf(".") + 1);
					var td1 = $("<td>").text(idx);
					var anc = $("<a>").addClass("gb-edit-sel-flist").css("cursor", "pointer").attr({
						"value" : that.features.item(i).getId()
					}).text("Selecting feature").click(function() {
						var feature = that.tempSelectSource.getFeatureById($(this).attr("value"));
						that.interaction.select.getFeatures().clear();
						that.interaction.select.getFeatures().push(feature);
						that.featurePop.close();
						console.log(feature);
					});
					var td2 = $("<td>").append(anc).mouseover(function() {
						var fid = $(this).find("a").attr("value");
						var feature = that.tempSelectSource.getFeatureById(fid);
						that.map.getView().fit(feature.getGeometry().getExtent(), that.map.getSize());
					});
					var tr = $("<tr>").append(td1).append(td2);
					$(that.featureTB).append(tr);
				}

				that.featurePop.open();
				that.featurePop.getPanel().position({
					"my" : "left top",
					"at" : "right top",
					"of" : that.getPanel(),
					"collision" : "fit"
				});
				that.attrPop.close();
			} else if (that.features.getLength() === 1) {
				that.featurePop.close();
				$(that.attrTB).empty();
				that.setLayer(that.updateSelected());
				var attrInfo = that.getLayer().get("git").attribute;
				that.feature = that.features.item(0);
				if (!!attrInfo) {
					var attr = that.features.item(0).getProperties();
					var keys = Object.keys(attrInfo);
					for (var i = 0; i < keys.length; i++) {
						if (keys[i] === "geometry") {
							continue;
						}
						var td1 = $("<td>").text(keys[i]);
						var tform = $("<input>").addClass("gb-edit-sel-alist").attr({
							"type" : "text"
						}).css({
							"width" : "100%",
							"border" : "none"
						}).val(attr[keys[i]]).on("input", function() {
							var obj = {};
							obj[$(this).parent().prev().text()] = $(this).val();
							that.feature.setProperties(obj);
							that.featureRecord.update(that.getLayer(), that.feature);
						});
						var td2 = $("<td>").append(tform);
						var tr = $("<tr>").append(td1).append(td2);
						that.attrTB.append(tr);
					}
					that.attrPop.open();
					that.attrPop.getPanel().position({
						"my" : "left top",
						"at" : "right top",
						"of" : that.getPanel(),
						"collision" : "fit"
					});
				} else {
					// $(that.featurePop).hide();
					that.attrPop.close();
				}
			} else {
				that.featurePop.close();
				that.attrPop.close();
			}

		});
		// this.map.addInteraction(this.interaction.selectWMS);
		this.activeIntrct_([ "select", "dragbox" ]);
		this.isOn.select = true;
		this.activeBtn_("selectBtn");
		this.deactiveIntrct_([ "move", "rotate" ]);
	} else if (sourceLayer instanceof ol.layer.Base) {
		// && (sourceLayer.get("git").geometry === "Point" ||
		// sourceLayer.get("git").geometry === "LineString"
		// || sourceLayer.get("git").geometry === "Polygon" ||
		// sourceLayer.get("git").geometry === "MultiPoint"
		// || sourceLayer.get("git").geometry === "MultiLineString" ||
		// sourceLayer.get("git").geometry === "MultiPolygon")
		if (!sourceLayer instanceof ol.layer.Tile && !sourceLayer.get("git").hasOwnProperty("fake")) {
			return;
		}
		if (sourceLayer instanceof ol.layer.Base && sourceLayer.get("git").hasOwnProperty("fake")) {
			if (sourceLayer.get("git").fake === "child") {
				var arr = {
					"geoLayerList" : [ sourceLayer.get("id") ]
				}
				var names = [];
				// console.log(JSON.stringify(arr));

				$.ajax({
					url : "geoserver/getGeoLayerInfoList.ajax",
					method : "POST",
					contentType : "application/json; charset=UTF-8",
					cache : false,
					data : JSON.stringify(arr),
					beforeSend : function() { // 호출전실행
						$("body").css("cursor", "wait");
					},
					traditional : true,
					success : function(data2, textStatus, jqXHR) {
						console.log(data2);
						if (Array.isArray(data2)) {
							for (var i = 0; i < data2.length; i++) {
								var source = new ol.source.TileWMS({
									url : "geoserver/geoserverWMSLayerLoad.do",
									params : {
										'LAYERS' : data2[i].lName,
										'TILED' : true,
										'FORMAT' : 'image/png8',
										'VERSION' : '1.1.0',
										'CRS' : 'EPSG:5186',
										'SRS' : 'EPSG:5186',
										'BBOX' : data2[i].nbBox.minx.toString() + "," + data2[i].nbBox.miny.toString() + ","
												+ data2[i].nbBox.maxx.toString() + "," + data2[i].nbBox.maxy.toString()
									},
									serverType : 'geoserver'
								});
								sourceLayer.setSource(source);
								var ogit = sourceLayer.get("git");
								ogit["attribute"] = data2[i].attInfo;
								ogit["geometry"] = data2[i].geomType;
								var getPosition = function(str, subString, index) {
									return str.split(subString, index).join(subString).length;
								};
								var id = sourceLayer.get("id");
								var format = id.substring((getPosition(id, "_", 1) + 1), getPosition(id, "_", 2));
								var layer;
								if (format === "ngi") {
									layer = new gb.layer.LayerInfo({
										name : sourceLayer.get("name"),
										id : id,
										format : format,
										epsg : "5186",
										mbound : [ [ data2[i].nbBox.minx.toString(), data2[i].nbBox.miny.toString() ],
												[ data2[i].nbBox.maxx.toString(), data2[i].nbBox.maxy.toString() ] ],
										lbound : [ [ 122.71, 28.6 ], [ 134.28, 40.27 ] ],
										isNew : false,
										geometry : id.substring(getPosition(id, "_", 4) + 1),
										sheetNum : id.substring((getPosition(id, "_", 2) + 1), getPosition(id, "_", 3))
									});
								} else if (format === "dxf") {
									layer = new gb.layer.LayerInfo({
										name : sourceLayer.get("name"),
										id : id,
										format : format,
										epsg : "5186",
										mbound : [ [ data2[i].nbBox.minx.toString(), data2[i].nbBox.miny.toString() ],
												[ data2[i].nbBox.maxx.toString(), data2[i].nbBox.maxy.toString() ] ],
										isNew : false,
										lbound : [ [ 122.71, 28.6 ], [ 134.28, 40.27 ] ],
										isNew : false,
										geometry : id.substring(getPosition(id, "_", 4) + 1),
										sheetNum : id.substring((getPosition(id, "_", 2) + 1), getPosition(id, "_", 3))
									});
								}
								ogit["information"] = layer;
								// var git = {
								// "validation" : false,
								// "geometry" : data2[i].geomType,
								// "editable" : true,
								// "attribute" : data2[i].attInfo,
								// "fake" : "child"
								// }
								// wms.set("name",
								// obj.refer.get_node(data2[i].lName).text);
								// wms.set("id", data2[i].lName);
								// wms.setVisible(false);
								// console.log(wms.get("id"));
								// wms.set("type", "ImageTile");
								// wms.set("git", git);
								// console.log(wms);
							}

							$("body").css("cursor", "default");
						}
					}
				});
			}
		}
		console.log("image-select");
		if (!this.tempSelectSource) {
			this.tempSelectSource = new ol.source.Vector();
		} else {
			this.tempSelectSource.clear();
		}
		if (this.interaction.select instanceof ol.interaction.Select) {
			this.interaction.select.getFeatures().clear();
		}
		this.map.removeInteraction(this.interaction.select);
		var styles = [ new ol.style.Style({
			stroke : new ol.style.Stroke({
				color : 'rgba(0,153,255,1)',
				width : 2
			}),
			fill : new ol.style.Fill({
				color : 'rgba(255, 255, 255, 0.5)'
			})
		}), new ol.style.Style({
			image : new ol.style.Circle({
				radius : 10,
				fill : new ol.style.Fill({
					color : 'rgba(0,153,255,0.4)'
				})
			}),
			geometry : function(feature) {

				var coordinates;
				var geom;

				if (feature.getGeometry() instanceof ol.geom.MultiPolygon) {
					coordinates = feature.getGeometry().getCoordinates()[0][0];
					geom = new ol.geom.MultiPoint(coordinates);
				} else if (feature.getGeometry() instanceof ol.geom.Polygon) {
					coordinates = feature.getGeometry().getCoordinates()[0];
					geom = new ol.geom.MultiPoint(coordinates);
				} else if (feature.getGeometry() instanceof ol.geom.MultiLineString) {
					coordinates = feature.getGeometry().getCoordinates()[0];
					geom = new ol.geom.MultiPoint(coordinates);
				} else if (feature.getGeometry() instanceof ol.geom.LineString) {
					coordinates = feature.getGeometry().getCoordinates();
					geom = new ol.geom.MultiPoint(coordinates);
				} else if (feature.getGeometry() instanceof ol.geom.MultiPoint) {
					coordinates = feature.getGeometry().getCoordinates();
					geom = new ol.geom.MultiPoint(coordinates);
				} else if (feature.getGeometry() instanceof ol.geom.Point) {
					coordinates = [ feature.getGeometry().getCoordinates() ];
					geom = new ol.geom.MultiPoint(coordinates);
				}

				return geom;
			}
		}) ];
		this.interaction.select = new ol.interaction.Select({
			layers : [ this.tempVector ],
			toggleCondition : ol.events.condition.platformModifierKeyOnly,
			style : styles
		});
		this.map.addInteraction(this.interaction.select);
		this.map.removeInteraction(this.interaction.dragbox);
		this.interaction.dragbox = new ol.interaction.DragBox({
			condition : ol.events.condition.shiftKeyOnly
		});
		this.map.addInteraction(this.interaction.dragbox);
		this.interaction.dragbox.on('boxend', function() {
			that.interaction.select.getFeatures().clear();
			that.tempSource.forEachFeatureIntersectingExtent(this.getGeometry().getExtent(), function(feature) {
				that.interaction.select.getFeatures().push(feature);
			});
			that.interaction.selectWMS.setExtent(this.getGeometry().getExtent());
		});
		this.interaction.selectWMS = new gb.interaction.SelectWMS({
			url : this.wfsURL,
			select : that.interaction.select,
			destination : that.tempVector,
			record : this.featureRecord,
			layer : function() {
				return that.updateSelected();
			}
		});
		this.interaction.select.getFeatures().on("change:length", function(evt) {
			that.features = that.interaction.select.getFeatures();
			$(that.featureTB).empty();
			that.tempSelectSource.clear();
			that.tempSelectSource.addFeatures(that.features.getArray());
			if (that.features.getLength() > 1) {
				that.featurePop.close();
				for (var i = 0; i < that.features.getLength(); i++) {
					var idx = that.features.item(i).getId().substring(that.features.item(i).getId().indexOf(".") + 1);
					var td1 = $("<td>").text(idx);
					var anc = $("<a>").addClass("gb-edit-sel-flist").css("cursor", "pointer").attr({
						"value" : that.features.item(i).getId()
					}).text("Selecting feature").click(function() {
						var feature = that.tempSelectSource.getFeatureById($(this).attr("value"));
						that.interaction.select.getFeatures().clear();
						that.interaction.select.getFeatures().push(feature);
						that.featurePop.close();
						console.log(feature);
					});
					var td2 = $("<td>").append(anc).mouseover(function() {
						var fid = $(this).find("a").attr("value");
						var feature = that.tempSelectSource.getFeatureById(fid);
						that.map.getView().fit(feature.getGeometry().getExtent(), that.map.getSize());
					});
					var tr = $("<tr>").append(td1).append(td2);
					$(that.featureTB).append(tr);
				}

				that.featurePop.open();
				that.featurePop.getPanel().position({
					"my" : "left top",
					"at" : "right top",
					"of" : that.getPanel(),
					"collision" : "fit"
				});
				that.attrPop.close();
			} else if (that.features.getLength() === 1) {
				that.featurePop.close();
				$(that.attrTB).empty();
				that.setLayer(that.updateSelected());
				var attrInfo = that.getLayer().get("git").attribute;
				that.feature = that.features.item(0);
				if (!!attrInfo) {
					var attr = that.features.item(0).getProperties();
					var keys = Object.keys(attrInfo);

					for (var i = 0; i < keys.length; i++) {
						if (keys[i] === "geometry") {
							continue;
						}
						var td1 = $("<td>").text(keys[i]);
						var tform = $("<input>").addClass("gb-edit-sel-alist").attr({
							"type" : "text"
						}).css({
							"width" : "100%",
							"border" : "none"
						}).val(attr[keys[i]]).on("input", function() {
							var obj = {};
							obj[$(this).parent().prev().text()] = $(this).val();
							that.feature.setProperties(obj);
							that.featureRecord.update(that.getLayer(), that.feature);
						});

						var td2 = $("<td>").append(tform);
						var tr = $("<tr>").append(td1).append(td2);
						that.attrTB.append(tr);
					}
					that.attrPop.open();
					that.attrPop.getPanel().position({
						"my" : "left top",
						"at" : "right top",
						"of" : that.getPanel(),
						"collision" : "fit"
					});
				}
			} else {
				that.featurePop.close();
				that.attrPop.close();
			}

		});
		this.map.addInteraction(this.interaction.selectWMS);
		this.activeIntrct_([ "select", "selectWMS", "dragbox" ]);
		this.isOn.select = true;
		this.activeBtn_("selectBtn");
		this.deactiveIntrct_([ "move", "rotate" ]);
	}
};
/**
 * 피처 그리기를 활성화 한다
 * 
 * @param {ol.layer.Base}
 *            layer - 편집할 레이어
 */
gb.panel.EditingTool.prototype.draw = function(layer) {
	var that = this;
	if (this.isOn.draw) {
		if (!!this.interaction.draw || !!this.interaction.updateDraw) {
			this.deactiveIntrct_("draw");
			this.deactiveBtn_("drawBtn");
		}
		return;
	}
	if (!!this.interaction.select) {
		this.interaction.select.getFeatures().clear();
		this.deactiveIntrct_([ "dragbox", "select", "selectWMS" ]);
	}
	var sourceLayer;
	if (Array.isArray(layer)) {
		if (layer.length > 1) {
			console.error("please, select 1 layer");
			return;
		} else if (layer.length < 1) {
			console.error("no selected layer");
			return;
		} else {
			sourceLayer = layer[0];
		}
	} else if (layer instanceof ol.layer.Base) {
		sourceLayer = layer;
	} else {
		return;
	}

	var git = sourceLayer.get("git");
	if (git.editable === true && sourceLayer instanceof ol.layer.Vector) {
		this.interaction.draw = new ol.interaction.Draw({
			source : sourceLayer.getSource(),
			type : git.geometry
		});

		this.interaction.draw.selectedType = function() {
			var irreGeom = that.updateSelected().get("git").geometry;
			var geom;
			switch (irreGeom) {
			case "Polyline":
				geom = "LineString";
				break;
			case "LWPolyline":
				geom = "LineString";
				break;
			case "Insert":
				geom = "Point";
				break;
			case "Text":
				geom = "Point";
				break;
			default:
				geom = that.updateSelected().get("git").geometry;
				break;
			}
			return geom;
		};

		this.interaction.draw.on("drawend", function(evt) {
			console.log(evt);
			var layers = that.selected();
			if (layers.length !== 1) {
				return;
			}
			if (that.getLayer().get("id") === layers[0].get("id")) {
				var feature = evt.feature;
				var c = that.featureRecord.getCreated();
				var l = c[that.getLayer().get("id")];
				if (!l) {
					var fid = that.getLayer().get("id") + ".new0";
					feature.setId(fid);
					that.featureRecord.create(layers[0], feature);
				} else {
					var keys = Object.keys(l);
					var count;
					if (keys.length === 0) {
						count = 0;
					} else {
						var id = keys[keys.length - 1];
						var nposit = (id.search(".new")) + 4;
						count = (parseInt(id.substr(nposit, id.length)) + 1);
					}
					var fid = that.getLayer().get("id") + ".new" + count;
					feature.setId(fid);
					that.featureRecord.create(layers[0], feature);
				}
			}
		});
		this.map.addInteraction(this.interaction.draw);
		this.deactiveIntrct_([ "select", "selectWMS", "move", "modify", "rotate" ]);
		this.activeIntrct_("draw");
		this.activeBtn_("drawBtn");
	} else if (git.editable === true && sourceLayer instanceof ol.layer.Base) {

		if (!this.managed) {
			this.managed = new ol.layer.Vector({
				source : this.tempSource
			});
			this.managed.set("name", "temp_vector");
			this.managed.set("id", "temp_vector");
			this.map.addLayer(this.managed);
		}

		this.interaction.draw = new ol.interaction.Draw({
			source : this.tempSource,
			type : git.geometry
		});
		this.interaction.draw.selectedType = function() {
			return that.updateSelected().get("git").geometry;
		};
		this.interaction.draw.on("drawend", function(evt) {
			console.log(evt);
			var layers = that.selected();
			if (layers.length !== 1) {
				return;
			}
			if (that.getLayer().get("id") === layers[0].get("id")) {
				var feature = evt.feature;
				var c = that.featureRecord.getCreated();
				var l = c[that.getLayer().get("id")];
				if (!l) {
					var fid = that.getLayer().get("id") + ".new0";
					feature.setId(fid);
					that.featureRecord.create(layers[0], feature);
				} else {
					var keys = Object.keys(l);
					var count;
					if (keys.length === 0) {
						count = 0;
					} else {
						var id = keys[keys.length - 1];
						var nposit = (id.search(".new")) + 4;
						count = (parseInt(id.substr(nposit, id.length)) + 1);
					}
					var fid = that.getLayer().get("id") + ".new" + count;
					feature.setId(fid);
					that.featureRecord.create(layers[0], feature);
				}
			}
		});
		this.map.addInteraction(this.interaction.draw);
		this.deactiveIntrct_([ "select", "selectWMS", "move", "modify", "rotate" ]);
		this.activeIntrct_("draw");
		this.activeBtn_("drawBtn");
	}

};
/**
 * 피처 이동을 활성화 한다
 * 
 * @param {ol.layer.Base}
 *            layer - 편집할 레이어
 */
gb.panel.EditingTool.prototype.move = function(layer) {
	var that = this;
	if (this.interaction.select.getFeatures().getLength() > 0) {
		if (this.isOn.move) {
			if (!!this.interaction.move) {
				this.interaction.select.getFeatures().clear();
				this.deactiveIntrct_("move");
				this.deactiveBtn("moveBtn");
				this.map.removeLayer(this.managed);
			}
			return;
		}
		if (!this.managed) {
			this.managed = new ol.layer.Vector({
				source : this.tempSource
			});
			this.managed.set("name", "temp_vector");
			this.managed.set("id", "temp_vector");
			this.map.addLayer(this.managed);
		}
		this.interaction.move = new ol.interaction.Translate({
			features : this.interaction.select.getFeatures()
		});
		this.interaction.move.on("translateend", function(evt) {
			console.log(evt);
			var layers = that.selected();
			if (layers.length !== 1) {
				return;
			}
			if (that.getLayer().get("id") === layers[0].get("id")) {
				var features = evt.features;
				for (var i = 0; i < features.getLength(); i++) {
					that.featureRecord.update(layers[0], features.item(i));
				}
			}
		});
		this.map.addInteraction(this.interaction.move);
		this.deactiveIntrct_([ "select", "selectWMS", "modify", "rotate" ]);
		this.activeIntrct_("move");
		this.activeBtn_("moveBtn");
	} else {
		console.error("select features");
	}
};
/**
 * 피처 멀티편집을 활성화 한다
 * 
 * @param {ol.layer.Base}
 *            layer - 편집할 레이어
 */
gb.panel.EditingTool.prototype.rotate = function(layer) {
	var that = this;
	if (this.interaction.select.getFeatures().getLength() > 0) {
		if (this.isOn.rotate) {
			if (!!this.interaction.rotate) {
				this.interaction.select.getFeatures().clear();
				this.deactiveIntrct_("rotate");
				this.deactiveBtn("rotateBtn");
			}
			return;
		}
		if (this.interaction.select.getFeatures().getLength() !== 1) {
			console.error("select 1 feature");
			return;
		}
		if (!this.managed) {
			this.managed = new ol.layer.Vector({
				source : this.tempSource
			});
			this.managed.set("name", "temp_vector");
			this.managed.set("id", "temp_vector");
			this.map.addLayer(this.managed);
		}
		this.interaction.rotate = new gb.interaction.MultiTransform({
			features : this.interaction.select.getFeatures()
		});
		this.interaction.rotate.on("transformend", function(evt) {
			console.log(evt);
			var layers = that.selected();
			if (layers.length !== 1) {
				return;
			}
			if (that.getLayer().get("id") === layers[0].get("id")) {
				var feature = evt.feature;
				that.featureRecord.update(layers[0], feature);
			}
		});
		this.map.addInteraction(this.interaction.rotate);
		this.deactiveIntrct_([ "select", "selectWMS", "move", "modify" ]);
		this.activeIntrct_("rotate");
		this.activeBtn_("rotateBtn");
	} else {
		console.error("select features");
	}
};
/**
 * 피처 수정을 활성화 한다
 * 
 * @param {ol.layer.Base}
 *            layer - 편집할 레이어
 */
gb.panel.EditingTool.prototype.modify = function(layer) {
	var that = this;
	if (this.interaction.select.getFeatures().getLength() > 0) {
		if (this.isOn.modify) {
			if (!!this.interaction.modify) {
				this.interaction.select.getFeatures().clear();
				this.deactiveIntrct_("modify");
				this.deactiveBtn("modiBtn");
				this.map.removeLayer(this.managed);
			}
			return;
		}
		if (!this.managed) {
			this.managed = new ol.layer.Vector({
				source : this.tempSource
			});
			this.managed.set("name", "temp_vector");
			this.managed.set("id", "temp_vector");
			this.map.addLayer(this.managed);
		}
		this.interaction.modify = new ol.interaction.Modify({
			features : this.interaction.select.getFeatures()
		});
		this.interaction.modify.on("modifyend", function(evt) {
			console.log(evt);
			var layers = that.selected();
			if (layers.length !== 1) {
				return;
			}
			if (that.getLayer().get("id") === layers[0].get("id")) {
				var features = evt.features;
				for (var i = 0; i < features.getLength(); i++) {
					that.featureRecord.update(layers[0], features.item(i));
				}
			}
		});
		this.map.addInteraction(this.interaction.modify);
		this.deactiveIntrct_([ "select", "selectWMS", "move", "rotate" ]);
		this.activeIntrct_("modify");
		this.activeBtn_("modiBtn");
	} else {
		console.error("select features");
	}
};
/**
 * 피처를 삭제한다
 * 
 * @param {ol.layer.Base}
 *            layer - 편집할 레이어
 */
gb.panel.EditingTool.prototype.remove = function(layer) {
	var that = this;
	if (this.interaction.select.getFeatures().getLength() > 0) {
		if (this.isOn.remove) {
			if (!!this.interaction.remove) {
				this.interaction.select.getFeatures().clear();
				this.deactiveBtn("removeBtn");
				this.map.removeLayer(this.managed);
			}
			return;
		}
		// if (!this.managed) {
		// this.managed = new ol.layer.Vector({
		// source : this.tempSource
		// });
		// this.managed.set("name", "temp_vector");
		// this.managed.set("id", "temp_vector");
		// this.map.addLayer(this.managed);
		// }
		var layers = that.selected();
		if (layers.length !== 1) {
			return;
		}
		if (that.getLayer().get("id") === layers[0].get("id")) {
			var features = this.interaction.select.getFeatures();
			var fill = new ol.style.Fill({
				color : "rgba(255,0,0,0.01)"
			});

			var stroke = new ol.style.Stroke({
				color : "rgba(255,0,0,0.7)",
				width : 1.25
			});

			var text = new ol.style.Text({});
			var style = new ol.style.Style({
				image : new ol.style.Circle({
					fill : fill,
					stroke : stroke,
					radius : 5
				}),
				fill : fill,
				stroke : stroke
			});
			if (layer instanceof ol.layer.Vector) {
				for (var i = 0; i < features.getLength(); i++) {
					if (features.item(i).getId().search(".new") !== -1) {
						layer.getSource().removeFeature(features.item(i));
					} else {
						features.item(i).setStyle(style);
					}
					that.featureRecord.remove(layers[0], features.item(i));
				}
			} else if (layer instanceof ol.layer.Base) {
				for (var i = 0; i < features.getLength(); i++) {
					if (features.item(i).getId().search(".new") !== -1) {
						this.managed.getSource().removeFeature(features.item(i));
					} else {
						features.item(i).setStyle(style);
					}
					that.featureRecord.remove(layers[0], features.item(i));
				}
			}

		}
		this.interaction.select.getFeatures().clear();

	} else {
		console.error("select features");
	}
};
/**
 * 선택한 레이어를 업데이트한다
 * 
 * @return {ol.layer.Base}
 */
gb.panel.EditingTool.prototype.updateSelected = function() {
	var result;
	if (typeof this.selected === "function") {
		this.layers = this.selected();
		if (Array.isArray(this.layers)) {
			this.layer = this.layers[0];
			result = this.layer;
		}
	}
	return result;
};
