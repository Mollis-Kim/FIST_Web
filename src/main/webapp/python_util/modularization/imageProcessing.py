"----------------------------------------------------------------------------------------------------------------------"
""" ##### 추출된 수간에서 임목 정보 분석 알고리즘 ##### """

import cv2
import math
import numpy as np
import skimage.draw

import sharedVariables as sv

"근사화 컨투어 생성 및 이미지 출력"

"수간 굽음도 함수"
a = b = c = 0
maxDistance = -1
maxDistanceIdx = 0
#기울기
def getSlope(x1, y1, x2, y2):
    if x1 != x2:
        return ((y2 - y1) / (x2 - x1))
    else:
        return float('inf')

#직선 방정식
def LinearEq(x1, y1, x2, y2):
    global a, b, c
    a = getSlope(x1, y1, x2, y2)
    b = -1
    if not math.isinf(a):
        c = y1 - (a * x1)
        return a, b, c
    else:
        return float('inf'), b, float('inf')

def DisOfLinear(x, y):
    global a, b, c
    if not math.isinf(a):
        d = abs((a * x) + (b * y) + c) / (math.sqrt(math.pow(a, 2) + math.pow(b, 2)))
        return d
    else:
        return float('inf')


def getImageInfo(img, mask):
    global maxDistance, maxDistanceIdx
    ori_img = img
    black = skimage.color.gray2rgb(skimage.color.rgb2gray(ori_img)) * 0
    gray = skimage.color.gray2rgb(skimage.color.rgb2gray(ori_img)) * 255
    white = skimage.util.invert(black) * 255
    contours_arr = list()
    for i in range(mask.shape[2]):
        reshapedMask = np.reshape(mask[:, :, i], (ori_img.shape[0], ori_img.shape[1], 1))
        whiteMask = np.where(reshapedMask, white, black).astype(np.uint8)
        whiteMask = cv2.resize(whiteMask, dsize=(400, 800), interpolation=cv2.INTER_AREA)
        cvtedWhiteMask = cv2.cvtColor(whiteMask, cv2.COLOR_BGR2GRAY)
        ret, thr = cv2.threshold(cvtedWhiteMask, 127, 255, 0)
        contours , hierarchy = cv2.findContours(thr, cv2.RETR_LIST, cv2.CHAIN_APPROX_NONE)
        contours_arr.append(contours)

    #전체 컨투어 그림
    sv.ori_img = cv2.resize(ori_img, dsize=(400, 800), interpolation=cv2.INTER_AREA)
    sv.oriimg = sv.ori_img.copy()
    
    

    #좌측, 우측, 중앙 컨투어 구분된 그림
    ori_img2 = cv2.resize(ori_img, dsize=(400, 800), interpolation=cv2.INTER_AREA)

    "BT 컨투어와 Stem 컨투어 구분(좌표개수가 많은 것이 Stem이라고 가정)"
    if len(contours_arr[0]) == 1 and len(contours_arr[1]) == 1:
        if len(contours_arr[0][0]) < len(contours_arr[1][0]):
            sv.btContour = contours_arr[0][0]
            sv.stemContour = contours_arr[1][0]
        else:
            sv.btContour = contours_arr[1][0]
            sv.stemContour = contours_arr[0][0]
    else:
        if len(contours_arr[0]) == 1:
            sv.btContour = contours_arr[0][0]
            maxIdx = 0
            for i in range(len(contours_arr[1])):
                if len(contours_arr[1][i]) > len(contours_arr[1][maxIdx]):
                    maxIdx = i
            sv.stemContour = contours_arr[1][maxIdx]
        else:
            sv.btContour = contours_arr[1][0]
            maxIdx = 0
            for i in range(len(contours_arr[0])):
                if len(contours_arr[0][i]) > len(contours_arr[0][maxIdx]):
                    maxIdx = i
            sv.stemContour = contours_arr[0][maxIdx]

    " Stem과 BT 컨투어 그리기"
    for cnt in sv.stemContour:
        cv2.drawContours(ori_img, [cnt], 0, (255, 100, 0), 2)
    for cnt in sv.btContour:
        cv2.drawContours(ori_img, [cnt], 0, (0, 30, 255), 2)

    "컨투어 좌표들 좌, 우측으로 나누기"
    p = sv.stemContour[:, 0, :].tolist()

    " y좌표 기준으로 컨투어 정렬(위쪽에서 아래쪽 방향으로)"
    sortedPoints = sorted(p, key=lambda x: x[1])

    leftPoints = list()
    rightPoints = list()
    centerPoints = list()

    bt = sv.btContour[:, 0:].tolist()
    btTop = sorted(bt, key=lambda x: x[0][1])
    bottom = btTop[0][0][1]

    " 윤곽선 시작 위치를 bottom이상으로 잡고, 뒷부분을 지운후 좌표 2개만 남겨놓기"
    i = len(sortedPoints) - 1
    while i != 1:
        if abs(sortedPoints[i][0] - sortedPoints[i-1][0]) == 1 or sortedPoints[i][1] > bottom:
            del sortedPoints[i]
        i -= 1

    # 동일 y지점엔 x값에 따라 좌,우포인트 리스트에 넣어주기 (짝수인덱스는 좌측, 홀수는 우측)
    for i in range(len(sortedPoints)):
        if i % 2 == 0:
            leftPoints.append(sortedPoints[i])
        else:
            rightPoints.append(sortedPoints[i])

    #간혹 좌,우 쌍이 안맞고 한쪽이 더 많은 경우가 있음. 그런경우 점개수가 적은쪽 기준을 사이즈로잡음.
    size = len(leftPoints) if len(leftPoints) < len(rightPoints) else len(rightPoints)
    i = size - 1
    while i != 0:
        if abs(leftPoints[i][0] - leftPoints[i-1][0]) > 1 or abs(rightPoints[i][0] - rightPoints[i-1][0]) > 1:
            del leftPoints[i]
            del rightPoints[i]
        i -= 1

    if abs(leftPoints[1][0] - leftPoints[0][0]) > 1 or abs(rightPoints[1][0] - rightPoints[0][0]) > 1:
        del leftPoints[0]
        del rightPoints[0]

    size = len(leftPoints) if len(leftPoints) < len(rightPoints) else len(rightPoints)

    i=0
    while i!=len(rightPoints)-1:
        if rightPoints[i+1][1]-rightPoints[i][1] != 1:
            ra = rightPoints[i][1]
            rb = rightPoints[i+1][1]
            la = rightPoints[i][0]
            lb = rightPoints[i+1][0]
            for y in range(ra+1,rb):
                rightPoints.insert(i+1, (rightPoints[i][0]+(y-ra+1)//5,y))
                leftPoints.insert(i+1, (leftPoints[i][0]+(y-ra+1)//5,y))
                i+=1
        i+=1
    size = len(rightPoints)        


    # 중점 설정
    for i in range(size):
        p = list(((leftPoints[i][0]+rightPoints[i][0])//2, leftPoints[i][1]))
        centerPoints.append(p)

    # 좌,우, 중앙 지점들에 대해 원그려주기
    # for i in range(size):
    #     ori_img2 = cv2.circle(ori_img2, tuple(leftPoints[i]), 1, (0, 200, 255), -1)
    #     ori_img2 = cv2.circle(ori_img2, tuple(rightPoints[i]), 1, (40, 100, 255), -1)
    #     ori_img2 = cv2.circle(ori_img2, tuple(centerPoints[i]), 1, (200, 200, 25), -1)
    
    
    "수간 굽음도 계산"
    a, b, c = LinearEq(centerPoints[0][0], centerPoints[0][1], centerPoints[-1][0], centerPoints[-1][1])

    for i in range(len(centerPoints)):
        d = DisOfLinear(centerPoints[i][0], centerPoints[i][1])
        if d != float('inf') and d > maxDistance:
            maxDistance = d
            maxDistanceIdx = i
            # 여기에 굽음도 할 수 있게 d/L * 100해서 굽음도 구하기

    #직선그리기
    cv2.circle(ori_img2, (centerPoints[0][0], centerPoints[0][1]), 3, (0, 255, 0), -1)
    cv2.circle(ori_img2, (centerPoints[-1][0], centerPoints[-1][1]), 3, (0, 255, 0), -1)
    cv2.line(ori_img2, (centerPoints[0][0], centerPoints[0][1]),
             (centerPoints[-1][0], centerPoints[-1][1]), (0, 255, 30), 1, cv2.LINE_AA)

    # 가장 먼점 표시
    (mpx, mpy) = (centerPoints[maxDistanceIdx][0], centerPoints[maxDistanceIdx][1])
    
    # 포인트 리스트 순서를 아래에서부터 위로 보게끔 재정렬
    rightPoints.reverse()
    leftPoints.reverse()
    centerPoints.reverse()

    tmp1 = leftPoints.copy()
    tmp2 = rightPoints.copy()
    # 간혹 이미지상 좌,우 점들이 서로 바껴서 그려지는경우가 있기에, 재조정
    if leftPoints[0][0] > rightPoints[0][0]:
        leftPoints = tmp2
        rightPoints = tmp1
    
    leftPoints= leftPoints[1:]
    rightPoints= rightPoints[1:]
    centerPoints=centerPoints[1:]
    
    for i in range(len(leftPoints)):
        if str(type(leftPoints[i])) != "<class 'list'>":
            leftPoints[i]=list(leftPoints[i])
    
    for i in range(len(rightPoints)):
        if str(type(rightPoints[i])) != "<class 'list'>":
            rightPoints[i]=list(rightPoints[i])

    for i in range(len(centerPoints)):
        if str(type(centerPoints[i])) != "<class 'list'>":
            centerPoints[i]=list(centerPoints[i])
            
    size = len(centerPoints)

    sv.leftPoints = leftPoints
    sv.rightPoints = rightPoints
    sv.centerPoints = centerPoints
    sv.distantPoint = (mpx, mpy)
    sv.size = len(centerPoints)

def figureOutARObject(img):
    ori_img = cv2.resize(img, dsize=(400, 800), interpolation=cv2.INTER_AREA)
    oriimg = ori_img.copy()
    ori_img2 = ori_img.copy()
    " AR 객체 출력 "
    #마스크 생성
    purple1 = np.array([153, 0, 153])
    purple2 = np.array([255, 102, 255])
    mPurple = cv2.inRange(oriimg, purple1, purple2)
    purple_obj = cv2.bitwise_and(oriimg, oriimg, mask=mPurple)

    #이진화
    gPur = cv2.cvtColor(purple_obj, cv2.COLOR_BGR2GRAY)
    ret, pThes = cv2.threshold(gPur, 0, 255, cv2.THRESH_BINARY)
    k = cv2.getStructuringElement(cv2.MORPH_RECT, (2, 2))
    p_open = cv2.morphologyEx(pThes, cv2.MORPH_OPEN, k)

    #컨투어 생성
    p_contour, hr = cv2.findContours(p_open, cv2.RETR_EXTERNAL, cv2.CHAIN_APPROX_SIMPLE)
    p_contr = p_contour[0]
    p_rect = cv2.minAreaRect(p_contr)
    p_box = cv2.boxPoints(p_rect)
    p_box = np.int0(p_box)
    p_rect = sorted(p_box.tolist())
    cv2.drawContours(purple_obj, [p_box], -1, (255, 102, 255), 2)

    #중심좌표 계산
    p_mmt = cv2.moments(p_contr)
    p_cx = int(p_mmt['m10'] / p_mmt['m00'])
    sv.p_cy = int(p_mmt['m01'] / p_mmt['m00'])
    cv2.circle(ori_img2, (p_cx, sv.p_cy), 3, (0, 255, 0), -1)
    b_mmt = cv2.moments(sv.btContour)
    b_cx = int(b_mmt['m10'] / b_mmt['m00'])
    b_cy = int(b_mmt['m01'] / b_mmt['m00'])
    cv2.circle(ori_img2, (b_cx, b_cy), 3, (0, 255, 0), -1)

    "픽셀별 직경 및 흉고높이 정의"
    a = abs(p_cx - b_cx)
    b = abs(sv.p_cy - b_cy)
    # (c,d) == (좌x, 좌y)
    # (e,f) == (우x, 우y)
    c, d, e, f = p_rect[1][0], p_rect[1][1], p_rect[3][0], p_rect[3][1]
    g = abs(c - e)
    h = abs(d - f)
    cv2.circle(ori_img2, (c, d), 3, (0, 255, 0), -1)
    cv2.circle(ori_img2, (e, f), 3, (0, 255, 0), -1)
    breast_h = math.trunc(math.sqrt(math.pow(a, 2) + math.pow(b, 2)))
    breast_h1px = round(abs(120 / breast_h), 1)
    dbh = g
    dbh1px = round((abs(40 / dbh)), 1)
    print("breast_h : ",breast_h," dbh:",dbh)
    print('breast_h에서의 1픽셀의 cm: ', breast_h1px, "cm", '\nDBH에서의 1픽셀의 cm: ', dbh1px, "cm")
    
    sv.ori_img2 = ori_img2
    sv.breast_h1px = breast_h1px
    sv.dbh1px = dbh1px

