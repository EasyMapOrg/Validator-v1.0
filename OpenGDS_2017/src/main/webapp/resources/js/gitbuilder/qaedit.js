/**
 * 검수편집 객체를 정의한다.
 * 
 * @author yijun.so
 * @date 2017. 04
 * @version 0.01
 */
var gitbuilder;
if (!gitbuilder)
	gitbuilder = {};
if (!gitbuilder.ui)
	gitbuilder.ui = {};
gitbuilder.ui.QAEdit = $
		.widget("gitbuilder.qaedit",
				{
					window : undefined,
					naviWindow : undefined,
					options : {
						geoserverURL : undefined,
						map : undefined,
						user : undefined,
						appendTo : "body",
						layersURL : undefined,
						featureWMSURL : undefined,
						featureWFSURL : undefined,
						groupURL : undefined,
						editingTool : undefined,
						linkKey : undefined
					},
					map : undefined,
					mapsheet : undefined,
					upbtn : undefined,
					leftbtn : undefined,
					centerbtn : undefined,
					rightbtn : undefined,
					downbtn : undefined,
					errbtn : undefined,
					td2 : undefined,
					tbody : undefined,
					error : undefined,
					source : undefined,
					features : undefined,
					count : undefined,
					feature : undefined,
					lid : undefined,
					list : undefined,
					_import_fake_group : function(){
						// // =======================================
						
					},
					_create : function() {
						var that = this;
						console.log(this.options.editingTool);
						this._on(false, this.element, {
							click : function(event) {
								if (event.target === that.element[0]) {
									that.open();
								}
							}
						});
						this.mapsheet = {};
						this.map = this.options.map;
						var xSpan = $("<span>").attr({
							"aria-hidden" : true
						}).html("&times;");
						var xButton = $("<button>").attr({
							"type" : "button",
							"data-dismiss" : "modal",
							"aria-label" : "Close"
						}).html(xSpan);
						$(xButton).addClass("close");

						var htag = $("<h4>");
						htag.text("QA Edit");
						$(htag).addClass("modal-title");

						var header = $("<div>").append(xButton).append(htag);
						$(header).addClass("modal-header");
						/*
						 * 
						 * 
						 * header end
						 * 
						 * 
						 */
						var serverTitle = $("<h3>").addClass("panel-title").text("GeoServer");
						var serverHead = $("<div>").addClass("panel-heading").append(serverTitle);
						this.list = $("<div>").attr({
							"id" : "geoserverList"
						});
						var serverBody = $("<div>").css({
							"padding" : "0",
							"overflow" : "auto",
							"height" : "300px"
						}).addClass("panel-body").append(this.list);
						var panel = $("<div>").addClass("panel").addClass("panel-default").append(serverHead).append(serverBody);
						var left = $("<div>").append(panel);

						$(this.list).jstree({
							"core" : {
								"animation" : 0,
								"check_callback" : true,
								"themes" : {
									"stripes" : true
								},
								'data' : {
									'url' : function() {
										return that.options.layersURL;
									}
								}
							},
							"contextmenu" : {
								select_node : true,
								show_at_node : true,
								items : function(o, cb) { // Could be an
									// object
									// directly
									return {
										"center" : {
											"separator_before" : false,
											"icon" : "fa fa-dot-circle-o",
											"separator_after" : false,
											"_disabled" : false, // (this.check("rename_node",
											// data.reference,
											// this.get_parent(data.reference),
											// "")),
											"label" : "Center",
											/*
											 * ! "shortcut" : 113,
											 * "shortcut_label" : 'F2', "icon" :
											 * "glyphicon glyphicon-leaf",
											 */
											"action" : function(data) {
												var inst = $.jstree.reference(data.reference), obj = inst.get_node(data.reference);
												if (obj.type === "n_ngi_group" || obj.type === "n_dxf_group") {
													that.assign_center(obj.id);
												}
											}
										},
										"up" : {
											"separator_before" : false,
											"icon" : "fa fa-chevron-circle-up",
											"separator_after" : false,
											"_disabled" : false, // (this.check("rename_node",
											// data.reference,
											// this.get_parent(data.reference),
											// "")),
											"label" : "Up",
											/*
											 * ! "shortcut" : 113,
											 * "shortcut_label" : 'F2', "icon" :
											 * "glyphicon glyphicon-leaf",
											 */
											"action" : function(data) {
												var inst = $.jstree.reference(data.reference), obj = inst.get_node(data.reference);
												if (obj.type === "n_ngi_group" || obj.type === "n_dxf_group") {
													that.assign_up(obj.id);
												}
											}
										},
										"down" : {
											"separator_before" : false,
											"icon" : "fa fa-chevron-circle-down",
											"separator_after" : false,
											"_disabled" : false, // (this.check("rename_node",
											// data.reference,
											// this.get_parent(data.reference),
											// "")),
											"label" : "Down",
											/*
											 * ! "shortcut" : 113,
											 * "shortcut_label" : 'F2', "icon" :
											 * "glyphicon glyphicon-leaf",
											 */
											"action" : function(data) {
												var inst = $.jstree.reference(data.reference), obj = inst.get_node(data.reference);
												if (obj.type === "n_ngi_group" || obj.type === "n_dxf_group") {
													that.assign_down(obj.id);
												}
											}
										},
										"left" : {
											"separator_before" : false,
											"icon" : "fa fa-chevron-circle-left",
											"separator_after" : false,
											"_disabled" : false, // (this.check("rename_node",
											// data.reference,
											// this.get_parent(data.reference),
											// "")),
											"label" : "Left",
											/*
											 * ! "shortcut" : 113,
											 * "shortcut_label" : 'F2', "icon" :
											 * "glyphicon glyphicon-leaf",
											 */
											"action" : function(data) {
												var inst = $.jstree.reference(data.reference), obj = inst.get_node(data.reference);
												if (obj.type === "n_ngi_group" || obj.type === "n_dxf_group") {
													that.assign_left(obj.id);
												}
											}
										},
										"right" : {
											"separator_before" : false,
											"icon" : "fa fa-chevron-circle-right",
											"separator_after" : false,
											"_disabled" : false, // (this.check("rename_node",
											// data.reference,
											// this.get_parent(data.reference),
											// "")),
											"label" : "Right",
											/*
											 * ! "shortcut" : 113,
											 * "shortcut_label" : 'F2', "icon" :
											 * "glyphicon glyphicon-leaf",
											 */
											"action" : function(data) {
												var inst = $.jstree.reference(data.reference), obj = inst.get_node(data.reference);
												if (obj.type === "n_ngi_group" || obj.type === "n_dxf_group") {
													that.assign_right(obj.id);
												}
											}
										},
										"error" : {
											"separator_before" : false,
											"icon" : "fa fa-info-circle",
											"separator_after" : false,
											"_disabled" : false, // (this.check("rename_node",
											// data.reference,
											// this.get_parent(data.reference),
											// "")),
											"label" : "Error",
											/*
											 * ! "shortcut" : 113,
											 * "shortcut_label" : 'F2', "icon" :
											 * "glyphicon glyphicon-leaf",
											 */
											"action" : function(data) {
												var inst = $.jstree.reference(data.reference), obj = inst.get_node(data.reference);
												if (obj.type === "e_ngi_layer" || obj.type === "e_dxf_layer") {
													that.assign_error(obj.id);
												}
											}
										}
									};
								}
							},
							"plugins" : [ "contextmenu", "search", "state", "types" ]
						});

						$("body").on("contextmenu", function() {
							$("body").find(".vakata-context").css("z-index", "1100");
						});

						this.upbtn = $("<td>").text("Up : Not assigned");
						this.leftbtn = $("<td>").text("Left : Not assigned");
						this.centerbtn = $("<td>").text("Center : Not assigned");
						this.rightbtn = $("<td>").text("Right : Not assigned");
						this.downbtn = $("<td>").text("Down : Not assigned");
						this.errbtn = $("<td>").attr({
							"colspan" : 3
						}).text("Error : Not assigned");

						var td1 = $("<td>"), td2 = $("<td>").append(this.upbtn), td3 = $("<td>");
						var tr1 = $("<tr>").append(td1).append(this.upbtn).append(td3);

						var td11 = $("<td>").append(this.leftbtn), td22 = $("<td>").append(this.centerbtn), td33 = $("<td>").append(this.rightbtn);
						var tr11 = $("<tr>").append(this.leftbtn).append(this.centerbtn).append(this.rightbtn);

						var td111 = $("<td>"), td222 = $("<td>").append(this.downbtn), td333 = $("<td>");
						var tr111 = $("<tr>").append(td111).append(this.downbtn).append(td333);

						var td1111 = $("<td>"), td2222 = $("<td>").append(this.errbtn), td3333 = $("<td>");
						var tr1111 = $("<tr>").append(this.errbtn);

						var tb = $("<table>").addClass("table").addClass("table-bordered").addClass("text-center").append(tr1).append(tr11).append(
								tr111).append(tr1111);

						var sixLayers = $("<div>").append(tb);

						var outerRow = $("<div>").addClass("row").append(sixLayers).append(left);
						var body = $("<div>").append(outerRow);
						$(body).addClass("modal-body");
						/*
						 * 
						 * 
						 * body end
						 * 
						 * 
						 */
						var closeBtn = $("<button>").attr({
							"type" : "button",
							"data-dismiss" : "modal"
						});
						$(closeBtn).addClass("btn");
						$(closeBtn).addClass("btn-default");
						$(closeBtn).text("Close");

						var okBtn = $("<button>").attr({
							"type" : "button",
							"data-dismiss" : "modal"
						});
						$(okBtn).addClass("btn");
						$(okBtn).addClass("btn-primary");
						$(okBtn).addClass("gitbuilder-jstree-geoserver-qa-ok");
						$(okBtn).text("OK");
						$(document).on("click", ".gitbuilder-jstree-geoserver-qa-ok", function() {
							that.start();
						});
						var footer = $("<div>").append(closeBtn).append(okBtn);
						$(footer).addClass("modal-footer");
						/*
						 * 
						 * 
						 * footer end
						 * 
						 * 
						 */
						var content = $("<div>").append(header).append(body).append(footer);
						$(content).addClass("modal-content");

						var dialog = $("<div>").html(content);
						$(dialog).addClass("modal-dialog");
						$(dialog).addClass("modal-lg");
						this.window = $("<div>").hide().attr({
							// Setting tabIndex makes the div focusable
							tabIndex : -1,
							role : "dialog",
						}).html(dialog);
						$(this.window).addClass("modal");
						$(this.window).addClass("fade");

						$(this.window).appendTo("body");
						this.window.modal({
							backdrop : "static",
							keyboard : true,
							show : false,
						});

						$(document).on("click", ".gitbuilder-qaedit-navigator-link", function() {
							console.log($(this).text());
							var fid = $(this).text();
							var lid = fid.substring(0, fid.indexOf("."));
							if (lid.indexOf(".") !== -1) {
								console.error("layer name error");
								return;
							}
							that.selectOriginalFeature(lid, fid);
						});

						this._create_navigator();
					},
					_init : function() {
						var that = this;
						this.mapsheet = {};
					},

					/**
					 * 오류 레이어 할당
					 */
					assign_error : function(name) {
						this.mapsheet.error = name;
						$(this.errbtn).text(name);
						console.log(this.mapsheet);
					},

					/**
					 * 가운데 레이어 할당
					 */
					assign_center : function(name) {
						this.mapsheet.center = name;
						$(this.centerbtn).text(name);
						console.log(this.mapsheet);
					},

					/**
					 * 위 레이어 할당
					 */
					assign_up : function(name) {
						this.mapsheet.up = name;
						$(this.upbtn).text(name);
						console.log(this.mapsheet);
					},

					/**
					 * 아래 레이어 할당
					 */
					assign_down : function(name) {
						this.mapsheet.down = name;
						$(this.downbtn).text(name);
						console.log(this.mapsheet);
					},

					/**
					 * 왼 레이어 할당
					 */
					assign_left : function(name) {
						this.mapsheet.left = name;
						$(this.leftbtn).text(name);
						console.log(this.mapsheet);
					},

					/**
					 * 오른 레이어 할당
					 */
					assign_right : function(name) {
						this.mapsheet.right = name;
						$(this.rightbtn).text(name);
						console.log(this.mapsheet);
					},

					start : function() {
						var that = this;
						console.log(this.mapsheet);
						var mapsheet = this.mapsheet;
						var arr = {
							"geoLayerList" : []
						}

						if (mapsheet.center !== undefined && mapsheet.center !== null && mapsheet.center !== "") {
							arr.geoLayerList.push(mapsheet.center);
						}
						if (mapsheet.up !== undefined && mapsheet.up !== null && mapsheet.up !== "") {
							arr.geoLayerList.push(mapsheet.up);
						}
						if (mapsheet.down !== undefined && mapsheet.down !== null && mapsheet.down !== "") {
							arr.geoLayerList.push(mapsheet.down);
						}
						if (mapsheet.left !== undefined && mapsheet.left !== null && mapsheet.left !== "") {
							arr.geoLayerList.push(mapsheet.left);
						}
						if (mapsheet.right !== undefined && mapsheet.right !== null && mapsheet.right !== "") {
							arr.geoLayerList.push(mapsheet.right);
						}
						var error;
						if (mapsheet.error !== undefined && mapsheet.error !== null && mapsheet.error !== "") {
							error = mapsheet.error;
						}

//						var that = this;
						var parentLayer;
//						var farr = {
//							"geoLayerList" : obj.parent
//						}
						var layers = new ol.Collection();
						
						console.log(JSON.stringify(arr));
						var parentParam;
						$.ajax({
							url : that.options.groupInfoURL,
							method : "POST",
							contentType : "application/json; charset=UTF-8",
							cache : false,
							// async : false,
							data : JSON.stringify(arr),
							beforeSend : function() { // 호출전실행
								$("body").css("cursor", "wait");
							},
							traditional : true,
							success : function(data, textStatus, jqXHR) {
								console.log(data);
								// parentParam = data;
								if (Array.isArray(data)) {
									for (var i = 0; i < data.length; i++) {
										var wms = new ol.layer.Tile({
											source : new ol.source.TileWMS({
												// url : CONTEXT +
												// "/geoserver2/geoserverWMSLayerLoad.do",
												// url :
												// "http://175.116.181.34:8080/opengds/geoserver2/geoserverWMSLayerLoad.do",
												url : "geoserver2/geoserverWMSLayerLoad.do",
												params : {
													'LAYERS' : $(that.list).jstree(true).get_node(data[i].name).children.toString(),
													// 'LAYERS' :
													// that._data.geoserver.user +
													// ":" + data[i].name,
													'TILED' : true,
													// 'FORMAT' : 'image/png8',
													'VERSION' : '1.1.0',
													'CRS' : 'EPSG:5186',
													'SRS' : 'EPSG:5186',
													'BBOX' : data[i].bbox.minx.toString() + "," + data[i].bbox.miny.toString() + ","
															+ data[i].bbox.maxx.toString() + "," + data[i].bbox.maxy.toString()
												},
												serverType : 'geoserver'
											})
										});
										wms.set("name", $(that.list).jstree(true).get_node(data[i].name).text);
										wms.set("id", data[i].name);
										var git = {
											"validation" : true,
											"geometry" : data[i].geomType,
											"editable" : true,
											"fake" : "parent"
										}
										wms.set("git", git);
										parentLayer = wms;
										layers.push(wms);
										console.log(wms);
										// wms.set("type", "ImageTile");
										// that._data.geoserver.map.addLayer(wms);
										//=============================================================
										for (var m = 0; m < data.length; m++) {
											var arr2 = {
												"geoLayerList" : $(that.list).jstree(true).get_node(data[m].name).children
											}
											var parent = $(that.list).jstree(true).get_node(data[m].name);
											var names = [];
											// console.log(JSON.stringify(arr));
											$.ajax({
												url : "geoserver2/getGeoLayerInfoList.ajax",
												method : "POST",
												contentType : "application/json; charset=UTF-8",
												cache : false,
												// async : false,
												data : JSON.stringify(arr2),
												beforeSend : function() { // 호출전실행
													// loadImageShow();
												},
												traditional : true,
												success : function(data2, textStatus, jqXHR) {
													console.log(data2);
													if (Array.isArray(data2)) {
														var arra = [];
														for (var i = 0; i < data2.length; i++) {
															var wms = new ol.layer.Tile({
																source : new ol.source.TileWMS({
																	url : "geoserver2/geoserverWMSLayerLoad.do",
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
																})
															});
															var git = {
																"validation" : true,
																"geometry" : data2[i].geomType,
																"editable" : true,
																"attribute" : data2[i].attInfo,
																"fake" : "child"
															}
															wms.set("name", $(that.list).jstree(true).get_node(data2[i].lName).text);
															wms.set("id", data2[i].lName);
															// wms.setVisible(false);
															console.log(wms.get("id"));
															// wms.set("type", "ImageTile");
															wms.set("git", git);
															arra.push(wms);
															console.log(wms);
														}
														var mapLayers = that.map.getLayers();
														var flag = true;
														var newCollection = [];
														// 현재 맵에 같은 아이디의 타일레이어가 있는지
														for (var j = 0; j < mapLayers.getLength(); j++) {
															if (mapLayers.item(j).get("id") === parent.id && mapLayers.item(j) instanceof ol.layer.Tile) {
																var befParams = mapLayers.item(j).getSource().getParams();
																var git = mapLayers.item(j).get("git");
																var lid = mapLayers.item(j).get("id");
																var lname = mapLayers.item(j).get("name");
																// 있다면 구 그룹의 콜렉션과 신 그룹의 콜렉션을
																// 비교
																var befCollection = mapLayers.item(j).get("git").layers;
																for (var l = 0; l < arra.length; l++) {
																	var dupl = false;
																	for (var k = 0; k < befCollection.getLength(); k++) {
																		if (arra[l].get("id") === befCollection.item(k).get("id")) {
																			dupl = true;
																		}
																	}
																	if (!dupl) {
																		newCollection.push(arra[l]);
																	}
																}
																befCollection.extend(newCollection);
																var names = [];
																for (var i = 0; i < befCollection.getLength(); i++) {
																	names.push(befCollection.item(i).get("id"));
																}
																befParams["LAYERS"] = names.toString();
																// var group = new
																// ol.layer.Group({
																// layers : befCollection
																// });
																var wms2 = new ol.layer.Tile({
																	source : new ol.source.TileWMS({
																		url : "geoserver2/geoserverWMSLayerLoad.do",
																		params : befParams,
																		serverType : 'geoserver'
																	})
																});
																wms2.set("name", lname);
																wms2.set("id", lid);
																wms2.set("git", git);
																layers.push(wms2);
																// wms.set("type", "Group");
//																that._data.geoserver.map.removeLayer(mapLayers.item(j));
//																that._data.geoserver.map.addLayer(wms2);
																flag = false;
																break;
															}
														}
														if (flag) {
															// var group = new
															// ol.layer.Group({
															// layers : arra
															// });
															var info = parentLayer.get("git");
															info["layers"] = new ol.Collection().extend(arra);
															layers.push(parentLayer);
															console.log(parentLayer);
//															that._data.geoserver.map.addLayer(parentLayer);
															// group.set("name",
															// obj.refer.get_node(obj.parent).text);
															// group.set("id", obj.parent);
															// group.set("type", "Group");
															// that._data.geoserver.map.addLayer(group);
														}
														$("body").css("cursor", "default");
													}
												}
											});
										}
										//=============================================================
										if (error !== undefined) {
											var defaultParameters = {
												service : 'WFS',
												version : '1.0.0',
												request : 'GetFeature',
												typeName : error,
												// typeName : that.options.user +
												// ":" + error,
												// maxFeatures : 10000,
												outputFormat : 'text/javascript',
												format_options : 'callback:getJson'
											};

											console.log(defaultParameters.typeName);
											var handleJson = function(data2) {
												console.log(data2);

												var format = new ol.format.GeoJSON().readFeatures(JSON.stringify(data2));

												var source = new ol.source.Vector({
													features : format
												});

												var fill = new ol.style.Fill({
													color : "rgba(0,0,0,0)"
												});
												var stroke = new ol.style.Stroke({
													color : "rgba(255,0,0,1)",
													width : 2.7
												});
												var text = new ol.style.Text({});
												var styles = new ol.style.Style({
													image : new ol.style.Circle({
														fill : fill,
														stroke : stroke,
														radius : 40
													}),
													fill : fill,
													stroke : stroke,
													text : text
												});

												var layer = new ol.layer.Vector({
													source : source,
													style : styles
												});
												layer.set("name", error);
												layer.set("id", error);
												layer.set("type", "Point");

												that.error = layer;

												layers.push(layer);

												var group = new ol.layer.Group({
													layers : layers
												});
												group.set("name", "Validator Layers");
												group.set("type", "Group");

												if (that.map instanceof ol.Map) {
													var coll = that.map.getLayers();
													for (var i = 0; i < coll.getLength(); i++) {
														if (coll.item(i).get("name") === "Validator Layers") {
															that.map.removeLayer(coll.item(i));
														}
													}
													that.map.addLayer(group);
													that.openNavigator();
													that._initNavigator();
												}
											}

											$.ajax({
												url : that.options.featureWFSURL,
												data : defaultParameters,
												dataType : 'jsonp',
												jsonpCallback : 'getJson',
												success : handleJson
											});
										}
									}
								}
								//===========================================================
							}
						});
						
//						$.ajax({
//							url : that.options.groupInfoURL,
//							method : "POST",
//							contentType : "application/json; charset=UTF-8",
//							cache : false,
//							// async : false,
//							data : JSON.stringify(arr),
//							beforeSend : function() { // 호출전실행
//								// loadImageShow();
//							},
//							traditional : true,
//							success : function(data, textStatus, jqXHR) {
//								console.log(data);
//								if (Array.isArray(data)) {
//									var layers = [];
//									for (var i = 0; i < data.length; i++) {
//										var wms = new ol.layer.Tile({
//											source : new ol.source.TileWMS({
//												url : that.options.featureWMSURL,
//												params : {
//													'LAYERS' : data[i].name,
//													// 'LAYERS' :
//													// that.options.user + ":" +
//													// data[i].name,
//													'TILED' : true,
//													// 'FORMAT' : 'image/png8',
//													'VERSION' : '1.1.0',
//													'CRS' : 'EPSG:5186',
//													'SRS' : 'EPSG:5186',
//													'BBOX' : data[i].bbox.minx.toString() + "," + data[i].bbox.miny.toString() + ","
//															+ data[i].bbox.maxx.toString() + "," + data[i].bbox.maxy.toString()
//												},
//												serverType : 'geoserver'
//											})
//										});
//										wms.set("name", data[i].name);
//										wms.set("id", data[i].name);
//										wms.set("type", "ImageTile");
//										layers.push(wms);
//										// that.map.addLayer(wms);
//									}
//								}
//							}
//						});
					},
					_create_navigator : function() {
						var that = this;
						var prevIcon = $("<span>").addClass("glyphicon").addClass("glyphicon-backward"), nextIcon = $("<span>").addClass("glyphicon")
								.addClass("glyphicon-forward");
						var btnPrev = $("<button>").addClass("gitbuilder-qaedit-navigator-prev").addClass("btn").addClass("btn-default").append(
								prevIcon), btnNext = $("<button>").addClass("gitbuilder-qaedit-navigator-next").addClass("btn").addClass(
								"btn-default").append(nextIcon);
						$(document).on("click", ".gitbuilder-qaedit-navigator-prev", function() {
							that.prevError();
						});
						$(document).on("click", ".gitbuilder-qaedit-navigator-next", function() {
							that.nextError();
						});
						var td1 = $("<td>").append(btnPrev), td3 = $("<td>").append(btnNext);
						this.td2 = $("<td>");
						var tr1 = $("<tr>").addClass("text-center").append(td1).append(this.td2).append(td3);
						var thead = $("<thead>").append(tr1);
						this.tbody = $("<tbody>");
						var tb = $("<table>").addClass("table").append(thead).append(this.tbody);
						var pbd = $("<div>").addClass("panel-body").append(tb);
						var phd = $("<div>").addClass("panel-heading").text("Error Navigator");
						var pdf = $("<div>").addClass("panel").addClass("panel-default").append(phd).append(pbd);
						this.naviWindow = $("<div>").css({
							"max-width" : "500px",
							"top" : "100px",
							"right" : 0,
							"position" : "absolute",
							"z-Index" : "999",
						}).append(pdf);

						$("body").append(this.naviWindow);
						$(this.naviWindow).hide();
						$(this.naviWindow).draggable({
							appendTo : "body",
						});
					},
					_initNavigator : function() {
						this.source = this.error.getSource();
						this.features = this.source.getFeatures();
						this.count = 1;
						this.lid = this.error.get("id");
						this.showFeatureInfo(this.source.getFeatureById(this.lid + "." + this.count));
					},
					prevError : function() {
						if (this.count > 1 && this.count < this.features.length + 1) {
							this.count--;
						}
						var feature = this.source.getFeatureById(this.lid + "." + this.count);
						if (feature) {
							this.showFeatureInfo(feature);
						}
					},
					nextError : function() {
						if (this.count > 0 && this.count < this.features.length) {
							this.count++;
						}
						var feature = this.source.getFeatureById(this.lid + "." + this.count);
						if (feature) {
							this.showFeatureInfo(feature);
						}
					},
					showFeatureInfo : function(feature) {
						if (!feature) {
							console.log("no feature maybe there is no error");
							return;
						}
						var fid = feature.getId();
						$(this.td2).text(fid);
						var prop = feature.getProperties();
						var keys = Object.keys(prop);
						$(this.tbody).empty();
						for (var i = 0; i < keys.length; i++) {
							var td1 = $("<td>").text(keys[i]);
							if (keys[i] === this.options.linkKey) {
								var anc = $("<a>").addClass("gitbuilder-qaedit-navigator-link").attr({
									"href" : "#"
								}).text(prop[keys[i]]);
								var td2 = $("<td>").attr("colspan", 2).append(anc);
							} else {
								var td2 = $("<td>").attr("colspan", 2).text(prop[keys[i]]);
							}
							var tr1 = $("<tr>").append(td1).append(td2);
							$(this.tbody).append(tr1);
						}
						var geom = feature.getGeometry();
						this.map.getView().fit(geom.getExtent(), this.map.getSize());
						this.map.getView().setZoom(16);
					},
					selectOriginalFeature : function(layer, fid) {
						var that = this;
						var params = {
							"service" : "WFS",
							"version" : "1.0.0",
							"request" : "GetFeature",
							"typeName" : layer,
							"outputformat" : "text/javascript",
							"featureID" : fid,
							"format_options" : "callback:getJson"
						};
						var addr = this.options.featureWFSURL;

						$.ajax({
							url : addr,
							data : params,
							dataType : 'jsonp',
							jsonpCallback : 'getJson',
							beforeSend : function() {
								$("body").css("cursor", "wait");
							},
							complete : function() {
								$("body").css("cursor", "default");
							},
							success : function(data) {
								console.log(data);
								var features = new ol.format.GeoJSON().readFeatures(JSON.stringify(data));
								// that.options.editingTool.open();
								that.options.editingTool.setFeatures(features);
							}
						});
					},
					getErrorLayer : function(map) {
						return this.error;
					},
					open : function() {
						this.window.modal('show');
					},
					openNavigator : function() {
						$(this.naviWindow).show();
					},
					close : function() {
						this.window.modal('hide');
					},
					closeNavigator : function() {
						$(this.naviWindow).hide();
					},
					destroy : function() {
						this.element.off("click");
						$(this.window).find("button").off("click");
						$(this.window).find("input").off("change").off("load");
						this.window = undefined;
					},
					_appendTo : function() {
						var element = this.options.appendTo;
						if (element && (element.jquery || element.nodeType)) {
							return $(element);
						}
						return this.document.find(element || "body").eq(0);
					},
					_removeClass : function(element, keys, extra) {
						return this._toggleClass(element, keys, extra, false);
					},

					_addClass : function(element, keys, extra) {
						return this._toggleClass(element, keys, extra, true);
					},

					_toggleClass : function(element, keys, extra, add) {
						add = (typeof add === "boolean") ? add : extra;
						var shift = (typeof element === "string" || element === null), options = {
							extra : shift ? keys : extra,
							keys : shift ? element : keys,
							element : shift ? this.element : element,
							add : add
						};
						options.element.toggleClass(this._classes(options), add);
						return this;
					},

					_on : function(suppressDisabledCheck, element, handlers) {
						var delegateElement;
						var instance = this;

						// No suppressDisabledCheck flag, shuffle arguments
						if (typeof suppressDisabledCheck !== "boolean") {
							handlers = element;
							element = suppressDisabledCheck;
							suppressDisabledCheck = false;
						}

						// No element argument, shuffle and use this.element
						if (!handlers) {
							handlers = element;
							element = this.element;
							delegateElement = this.widget();
						} else {
							element = delegateElement = $(element);
							this.bindings = this.bindings.add(element);
						}

						$.each(handlers, function(event, handler) {
							function handlerProxy() {

								// Allow widgets to customize the disabled
								// handling
								// - disabled as an array instead of boolean
								// - disabled class as method for disabling
								// individual parts
								if (!suppressDisabledCheck && (instance.options.disabled === true || $(this).hasClass("ui-state-disabled"))) {
									return;
								}
								return (typeof handler === "string" ? instance[handler] : handler).apply(instance, arguments);
							}

							// Copy the guid so direct unbinding works
							if (typeof handler !== "string") {
								handlerProxy.guid = handler.guid = handler.guid || handlerProxy.guid || $.guid++;
							}

							var match = event.match(/^([\w:-]*)\s*(.*)$/);
							var eventName = match[1] + instance.eventNamespace;
							var selector = match[2];

							if (selector) {
								delegateElement.on(eventName, selector, handlerProxy);
							} else {
								element.on(eventName, handlerProxy);
							}
						});
					},

					_off : function(element, eventName) {
						eventName = (eventName || "").split(" ").join(this.eventNamespace + " ") + this.eventNamespace;
						element.off(eventName).off(eventName);

						// Clear the stack to avoid memory leaks (#10056)
						this.bindings = $(this.bindings.not(element).get());
						this.focusable = $(this.focusable.not(element).get());
						this.hoverable = $(this.hoverable.not(element).get());
					}
				});