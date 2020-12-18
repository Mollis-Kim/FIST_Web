
// 지도 관련 자바스크립트
let container = document.getElementById('mapJs'); //지도를 담을 영역의 DOM 레퍼런스
let options = { //지도를 생성할 때 필요한 기본 옵션
    center: new kakao.maps.LatLng(36.377938, 128.145146), //지도의 중심좌표.
    level: 3 //지도의 레벨(확대, 축소 정도)
};


let mapJs = new kakao.maps.Map(container, options); //지도 생성 및 객체 리턴


/*마커생성 부분 */
let markerPosition  = new kakao.maps.LatLng(36.377938, 128.145146); //마커표시 위치 지정
// 마커를 생성합니다
var marker = new kakao.maps.Marker({
    position: markerPosition
});
marker.setMap(mapJs);


/*다각형 생성부분*/
var polygonPath = [
    new kakao.maps.LatLng(36.380184, 128.150071),
    new kakao.maps.LatLng(36.375995, 128.147132),
    new kakao.maps.LatLng(36.374630, 128.143591),
    new kakao.maps.LatLng(36.375856, 128.144535),
    new kakao.maps.LatLng(36.376202, 128.141574),
    new kakao.maps.LatLng(36.378914, 128.142883),
    new kakao.maps.LatLng(36.378828, 128.139879),
    new kakao.maps.LatLng(36.380106, 128.142132),
    new kakao.maps.LatLng(36.379605, 128.143076),
    new kakao.maps.LatLng(36.381177, 128.144728),
    new kakao.maps.LatLng(36.379968, 128.147861),
    new kakao.maps.LatLng(36.380815, 128.148441)
];

// 지도에 표시할 다각형을 생성합니다
var polygon = new kakao.maps.Polygon({
    path:polygonPath, // 그려질 다각형의 좌표 배열입니다
    strokeWeight: 3, // 선의 두께입니다
    strokeColor: '#39DE2A', // 선의 색깔입니다
    strokeOpacity: 0.8, // 선의 불투명도 입니다 1에서 0 사이의 값이며 0에 가까울수록 투명합니다
    strokeStyle: 'longdash', // 선의 스타일입니다
    fillColor: '#A2FF99', // 채우기 색깔입니다
    fillOpacity: 0.7 // 채우기 불투명도 입니다
});

// 지도에 다각형을 표시합니다
polygon.setMap(mapJs);