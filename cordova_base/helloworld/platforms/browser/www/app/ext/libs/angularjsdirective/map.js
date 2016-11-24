define(["app", "ol"], function (app, ol) {

    app.directive('map', function() {
        var directive = {};

        directive.restrict = 'E';
        directive.template = "<div> \
            <div id='map' class='map'></div> \
            <div id='popup' class='ol-popup'> \
            <b id='popup-buildName'></b> \
            </div> \
            </div>";

        directive.link = function($scope, element, attributes) {
            var center = [parseFloat(attributes.longitude), parseFloat(attributes.latitude)];
            var coor = ol.proj.transform(center, 'EPSG:4326', 'EPSG:3857');
            var view = new ol.View({
                center : coor,
                zoom : 15,
                maxZoom : 18,
                minZoom : 1
            });
            var mousePositionControl = new ol.control.MousePosition({
                coordinateFormat : ol.coordinate.createStringXY(4),
                projection : 'EPSG:4326',
                // comment the following two lines to have the mouse position
                // be placed within the map.
                className : 'custom-mouse-position',
                target : document.getElementById('mouse-position'),
                undefinedHTML : '&nbsp;'
            });
            var container = document.getElementById('popup');
            /**
             * 浮动层，显示建筑基本信息
             */
            var overlay = new ol.Overlay(/** @type {olx.OverlayOptions} */( {
                element : container
            }));

            //矢量图数据源
            var normalVectorSource = new ol.source.Vector({});
            var unNormalVectorSource = new ol.source.Vector({});
            var map = new ol.Map({
                target : 'map',
                controls : ol.control.defaults({
                    attributionOptions : ( {
                        collapsible : false
                    })
                }).extend([new ol.control.FullScreen(), mousePositionControl, new ol.control.OverviewMap()]),
                overlays : [overlay],
                layers : [new ol.layer.Tile({
                    title : "地图",
                    type : 'base',
                    source : new ol.source.XYZ({
                        attributions : [],
                        url : "http://webrd01.is.autonavi.com/appmaptile?lang=zh_cn&size=1&scale=1&style=7&x={x}&y={y}&z={z}"
                    })
                }), new ol.layer.Tile({
                    title : '卫星图',
                    visible : false,
                    source : new ol.source.XYZ({
                        attributions : [],
                        url : "http://webst01.is.autonavi.com/appmaptile?style=6&x={x}&y={y}&z={z}"
                    })
                }), new ol.layer.Tile({
                    title : '卫星图地址',
                    visible : false,
                    source : new ol.source.XYZ({
                        attributions : [],
                        url : "http://webst01.is.autonavi.com/appmaptile?x={x}&y={y}&z={z}&lang=zh_cn&size=1&scale=1&style=8"
                    })
                }), new ol.layer.Vector({
                    source : normalVectorSource,
                    style : new ol.style.Style({
                        image : new ol.style.Icon({
                            anchor : [0.5, 0.5],
                            anchorXUnits : 'fraction',
                            anchorYUnits : 'fraction',
                            opacity : 0.95,
                            src : '/ec4/pages/map/img/normal.png'
                        })
                    })
                }), new ol.layer.Vector({
                    source : unNormalVectorSource,
                    style : new ol.style.Style({
                        image : new ol.style.Icon({
                            anchor : [0.5, 0.5],
                            anchorXUnits : 'fraction',
                            anchorYUnits : 'fraction',
                            opacity : 0.95,
                            src : '/ec4/pages/map/img/unnormal.png'
                        })
                    })
                })],
                view : view
            });
            //默认位置
            view.setCenter([13376882.795496527, 3941536.9804111]);

            console.info("map....",map)
        };

        return directive;
    });

    return {};
});