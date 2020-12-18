


// 지도 관련 자바스크립트
let container = document.getElementById('map'); //지도를 담을 영역의 DOM 레퍼런스
let options = { //지도를 생성할 때 필요한 기본 옵션
    center: new kakao.maps.LatLng(36.377938, 128.145146), //지도의 중심좌표.
    level: 3 //지도의 레벨(확대, 축소 정도)
};


let map = new kakao.maps.Map(container, options); //지도 생성 및 객체 리턴

let positions = new Array();

for(var i=0; i<treePosArray.length; i++)
    positions.push({
        title: treePosArray[i][0],
        latlng: new kakao.maps.LatLng(treePosArray[i][1], treePosArray[i][2])
    }); //마커표시 위치 지정
// 마커를 생성합니다
var imageSrc = "https://www.flaticon.com/svg/static/icons/svg/1497/1497192.svg";

for (var i = 0; i < positions.length; i ++) {

    // 마커 이미지의 이미지 크기 입니다
    var imageSize = new kakao.maps.Size(24, 35);

    // 마커 이미지를 생성합니다
    var markerImage = new kakao.maps.MarkerImage(imageSrc, imageSize);

    // 마커를 생성합니다
    var marker = new kakao.maps.Marker({
        map: map, // 마커를 표시할 지도
        position: positions[i].latlng, // 마커를 표시할 위치
        title : positions[i].title, // 마커의 타이틀, 마커에 마우스를 올리면 타이틀이 표시됩니다
        image : markerImage // 마커 이미지
    });
}


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
polygon.setMap(map);