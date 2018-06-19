/**
 * 레이어 스타일 패널 객체를 정의한다.
 * 
 * @author yijun.so
 * @date 2018. 06.04
 * @version 0.01
 * @class gb.panel.LayerStyle
 * @constructor
 */
var gb;
if (!gb)
	gb = {};
if (!gb.panel)
	gb.panel = {};
gb.panel.LayerStyle = function(obj) {
	obj.width = 247;
	obj.height = 491;
	obj.positionX = 380;
	obj.positionY = 466;
	gb.panel.Base.call(this, obj);
	var options = obj ? obj : {};
	this.layer = options.layer instanceof ol.layer.Base ? options.layer : undefined;
	this.layerName = $("<div>");
	this.linePicker = $("<div>");
	var lineStyle = $("<div>").append(this.linePicker);
	this.polyPicker = $("<div>");
	var polyStyle = $("<div>").append(this.polyPicker);
	$(this.panelBody).append();
	$("body").append(this.panel);
};
gb.panel.LayerStyle.prototype = Object.create(gb.panel.Base.prototype);
gb.panel.LayerStyle.prototype.constructor = gb.panel.LayerStyle;
/**
 * 스타일을 변경할 레이어를 설정한다.
 * 
 * @method setLayer
 */
gb.panel.LayerStyle.prototype.setLayer = function(layer) {

};
/**
 * 내부 인터랙션 구조를 반환한다.
 * 
 * @method getInteractions_
 * @return {Mixed Obj} {select : ol.interaction.Select..}
 */
gb.panel.LayerStyle.prototype.get = function() {
	return null;
};