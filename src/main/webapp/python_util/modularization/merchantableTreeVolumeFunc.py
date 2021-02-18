import cv2
import math
import numpy as np

import sharedVariables as sv

"----------------------------------------------------------------------------------------------------------------------"
""" ##### 국외 이용재적 함수 #####"""
#Doyle: 가장 많이 사용되는 식 -> 과대치 발생
einch =0
m3 = 0.00235974

"이용재적식 적용하고 가장 높은 이용재적을 출력하는 높이를 반환"


toggle=1  # 밑변 줄일때 좌,우 번갈아가기위한 토글

mTreeVolume_list = list()
deduction_list=list()

inter4_list = list()
inter8_list = list()
inter12_list = list()
inter16_list = list()
inter20_list = list()

pinus_kw_list = list()
larix_kw_list = list()
pinus_km_list = list()
larix_km_list = list()
pinus_merRatio_list = list()
larix_merRatio_list = list()
dia_list = list()
stemVolume = 0


# 식 적용 최소 조건 말구직경: 6cm / 재장: 1.8m

def Doyle(edia, length):
    global einch, m3
    einch = 0.393701 * edia
    doyle = math.pow(einch - 4, 2) * (length / 16)
    doyle = round(doyle / m3, 4)
    return doyle

#Hanna: Scribner와 매우 비슷
def Hanna(edia, length):
    global einch, m3
    einch = 0.393701 * edia
    hanna = (0.61 * math.pow(einch, 2)) - (1.7 * einch) - 6
    hanna = round(hanna / m3, 4)
    return hanna

#Mississippi: Mississipi주에서 생육하는 소나무에 한정되서 개발된 식
def Mississipi(edia, length):
    global einch, m3
    einch = 0.393701 * edia
    misp = ((0.891 * math.pow(einch, 2)) - (5.503 * einch) + 22.198) * (length / 16)
    misp = round(misp / m3, 4)
    return misp

#Scribner: 무조건 통직한 통나무라는 가정하에 계산되는 식
def Scribner(edia, length):
    global einch, m3
    einch = 0.393701 * edia
    scrib = ((0.79 * math.pow(einch, 2)) - (2 * einch) - 4) * (length / 16)
    scrib = round(scrib / m3, 4)
    return scrib

#International 1/4-inch rule: 가장 정확하나 사용이 매우 저조
def International4(edia, length):
    global einch, m3
    einch = 0.393701 * edia
    inter4 = round(((0.199 * math.pow(einch, 2)) - (0.642 * einch)) / m3, 4)
    return inter4
def International8(edia, length):
    global einch, m3
    einch = 0.393701 * edia
    inter8 = round(((0.398 * math.pow(einch, 2)) - (1.086 * einch) - 0.27) / m3, 4)
    return inter8
def International12(edia, length):
    global einch, m3
    einch = 0.393701 * edia
    inter12 = round(((0.597 * math.pow(einch, 2)) - (1.330 * einch) - 0.72) / m3, 4)
    return inter12
def International16(edia, length):
    global einch, m3
    einch = 0.393701 * edia
    inter16 = round(((0.796 * math.pow(einch, 2)) - (1.375 * einch) - 1.23) / m3, 4)
    return inter16
def International20(edia, length):
    global einch, m3
    einch = 0.393701 * edia
    inter20 = round(((0.995 * math.pow(einch, 2)) - (1.221 * einch) - 1.72) / m3, 4)
    return inter20
"----------------------------------------------------------------------------------------------------------------------"


"----------------------------------------------------------------------------------------------------------------------"
""" ##### 국내 이용재적 함수 ##### """
#목질부 재적
def WoodVolume(dia):
    global pinus_kw, larix_kw
    pinus_kw = round((0.900 / (1 + (0.5585 / dia))) + (0.0566 / (1 + (17.7718 / dia))), 4)
    larix_kw = round((0.8833 / (1 + (0.5383 / dia))) + (0.0708 / (1 + (13.1094 / dia))), 4)
    return pinus_kw, larix_kw

#원목이용률 및 조재율
def StemVol(edia, dia):
    global pinus_kw, larix_kw
    pinus_km = -(0.0406 + (-0.0002 * dia) + (0.000005 * math.pow(dia, 2)) + (-0.00000009 * math.pow(dia, 3))) + \
         math.e ** ((-0.8081 * (edia / dia)) ** 3.7825)
    larix_km = -(0.0390 + (-0.0003 * dia) + (0.000005 * math.pow(dia, 2)) + (-0.00000005 * math.pow(dia, 3))) + \
         math.e ** ((-1.0204 * (edia / dia)) ** 4.1059)
    pinus_merRatio = int((pinus_kw / pinus_km) * 100)
    larix_merRatio = int((larix_kw / larix_km) * 100)
    return pinus_km, larix_km, pinus_merRatio, larix_merRatio

""" ##### 커스텀 함수 ##### """
"round 함수 커스텀"
def my_round(num, ndig = 0, rd = 1):
    """rd==1: ROUND_HALF_UP, Round to nearest, ties away from zero
       rd==2: ROUND_HALF_EVEN, Round to nearest, ties to even """
    #make float expression
    if ndig > 0:
        expression = '0.' + '0' * ndig
        number = num
    else:  # 0 or negative
        expression = '0'
        number = num / (10 ** (-ndig))

    # round by rounding rule
    if rd == 2:
        rd_num = Decimal(number).quantize(Decimal(expression), rounding=ROUND_HALF_EVEN)
    else:
        rd_num = Decimal(number).quantize(Decimal(expression), rounding=ROUND_HALF_UP)

    # return number
    if ndig > 0:
        return float(rd_num)
    else:  # 0 or negative
        return int(rd_num * (10 ** (-ndig)))
"----------------------------------------------------------------------------------------------------------------------"


def getLength(line):
    return abs(line[0][0]-line[1][0])

def amend(sLine1, eLine1):
    global toggle
    length = getLength(eLine1)
    # sLine의 좌,우 점의 x좌표를 번갈아가면서 1씩 출이기
    while getLength(sLine1) > length:
        if toggle==1:
            sLine1[0][0]+=1
        else:
            sLine1[1][0]-=1
        toggle*=-1

def getDeductionPixel(pts1, pts2):
    img1 = np.zeros((800,400,3), np.uint8)
    img2 = np.zeros((800,400,3), np.uint8)

    cv2.fillPoly(img1, [pts1],(255,255,255))
    cv2.fillPoly(img2, [pts2],(255,255,255))

    gray1 = cv2.cvtColor(img1, cv2.COLOR_BGR2GRAY)
    gray2 = cv2.cvtColor(img2, cv2.COLOR_BGR2GRAY)

    gray2 = cv2.bitwise_not(gray2) 
    area3 = cv2.bitwise_and(gray1,gray2)

    return np.sum(area3)//255        
        
def useFormula(cmd):
    global stemVolume
    cp = sv.ori_img2.copy() 
    LP = sv.leftPoints.copy()
    RP = sv.rightPoints.copy()
    polygonsList = list()
    height = 0
    sLine = [LP[0], RP[0]]  # 사각형(평행사변형)의 밑변- 시작라인
    idxArr=list() #커팅되는 부분 인덱스를 담아두는 리스트 
    idxArr.append(0)
    areaOfLayer = getLength(sLine) # 각 층별 원뿔대 높이??
    size = len(sv.centerPoints)
    for i in range(1, size-1): #1번부터 사이즈-1번인덱스까지.....
        edia = abs(RP[i][0] - LP[i][0]) * sv.breast_h1px # 각지점별 말구직경을 구하고....
        
        if edia >= 6: # 말구직경이 6cm보다 크다면... 
            height+= sv.breast_h1px  # 1픽셀에 해당하는 높이값(cm)을 늘려주고...
            eLine =[LP[i], RP[i]]  # 사각형(평행사변형)의 윗변 - 끝라인을 i번째로 지정
            
            next_eLine=[LP[i+1], RP[i+1]] # i+1번째를 다음끝라인으로 미리 지정해놓음
            next_edia = abs(RP[i+1][0] - LP[i+1][0]) * sv.breast_h1px # i+1번째 말구직경도 미리 구하기
            next_height = height+sv.breast_h1px # i+1번째 높이도 미리 구해놓기
            
            currVal=0  # 현재(i번까지)의 공식값
            nextVal=0 # 다음번(i+1번째까지)의 공식값
            areaOfLayer += getLength(eLine)
            if height >=180:  #높이가 180cm이상에서 현재값,다음값 비교
                
                if cmd=="doyle":
                    currVal = Doyle(edia, height)
                    nextVal = Doyle(next_edia, next_height)
                elif cmd =="hanna":
                    currVal = Hanna(edia, height)
                    nextVal = Hanna(next_edia, next_height)
                elif cmd== "misp":
                    currVal = Mississipi(edia, height)
                    nextVal = Mississipi(next_edia, next_height)
                elif cmd== "scrib":
                    currVal = Scribner(edia, height)
                    nextVal = Scribner(next_edia, next_height)
                    
                if currVal > nextVal: # i번째 값이 i+1번째보다 더 크다면 i번째에서 커팅!
                    dia_list.append(edia) # edia 값 넣기
                    idxArr.append(i)  # 커팅부분인 i값을 리스트에 넣기
                    mTreeVolume_list.append(currVal) # 커팅 당시의 공식값도 리스트에 넣기
                    deduction_list.append(areaOfLayer) # 통나무 높이만큼의 전체 픽셀수
                    areaOfLayer=0
                    height=0 # 높이값을 0으로 초기화
                    sLine = eLine # 현재 끝라인을 시작라인으로 바꾸기
        else: #만약 6cm가 되지 않는다면....
            stemVolume += round(((((edia**2) * math.pi) + ((next_edia**2) * math.pi)) * sv.breast_h1px) / 2, 4)
            # stemVolume에 (현재 말구직경 넓이+ 다음번말구직경 넓이)*높이cm/2  가산
    rtn =list()        
    # 사각형 그리기 위한 부분
    for idx in range(len(idxArr)-1):  
        sv.resetLogInfo();
        i = idxArr[idx] #idxArr[idx]에는 실제 인덱스값이 들어가있음
        i2 = idxArr[idx+1] 
        
        srcPts = list()
        for index in range(i, i2+1):
            srcPts.append(LP[index])
        for index in range(i2, i-1, -1):
            srcPts.append(RP[index])
        srcPts = np.array(srcPts, np.int32)
        
        sLine=[LP[i],RP[i]]  #시작 라인
        eLine=[LP[i2],RP[i2]] # 끝라인
        
        
        eMidPoint = [(LP[i2][0]+RP[i2][0])//2,LP[i2][1]] # 끝라인 중점
        hh = max(abs(LP[i][1]-LP[i2][1]),abs(RP[i][1]-RP[i2][1])) # 사각형 높이 
        
        if getLength(sLine) > getLength(eLine): #밑변이 더 길다면, 윗변에 맞춰서 사이즈조정
            amend(sLine, eLine)
        sMidPoint = [(sLine[0][0]+sLine[1][0])//2, sLine[0][1]] # 시작라인 중점
        pts = np.array([eLine[0],eLine[1],sLine[1],sLine[0]], np.int32)
        sv.logInfo["deduction"]= getDeductionPixel(srcPts,pts)
        polygonsList.append(pts)
                                     
        cv2.fillPoly(cp, [pts],(240,120,100)) # 사각형
        cv2.line(cp, tuple(sMidPoint), tuple(eMidPoint),(255,255,255),2) # 중심선
        cv2.line(cp, tuple(LP[i2]), tuple(RP[i2]), (255,255,255),2)
        
        
        sv.logInfo["mTreeVolume"] = round(mTreeVolume_list[idx],4)  # idx번째 이용재적
        sv.logInfo["logLength"] = round(hh* sv.breast_h1px,1)         # idx번째 통나무 높이(cm)   
        
        sweepDegree = round((eMidPoint[0]-sMidPoint[0])/hh*100,1) # idx번째 굽음도 
        sv.logInfo["logSweep"] = str(sweepDegree)+" (Left)" if sweepDegree <0 else str(sweepDegree)+" (Right)"
        sv.logInfo["eDiameter"] = round(dia_list[idx],1) # idx번째 말구직경
        
        rtn.append(sv.logInfo.copy())
    sv.pointsList_fom = polygonsList
    return cp, rtn



def useInternationalFormula():
    cp = sv.ori_img2.copy() 
    LP = sv.leftPoints.copy()
    RP = sv.rightPoints.copy()
    checkIdx =0 #체크포인트
    polygonsList = list()
    interList=list() #지점별 인터값들이 들어갈 리스트
    rtn =list()    
    h=0 #시작을 120cm부터 시작하기 위해(흉고직경측정AR 위치부터 시작)
    
    for i in range(len(RP)):
        if RP[i][1] == sv.p_cy:
            h=abs(i)
            break

    pre_checkIdx=checkIdx
    sLine=[LP[0],RP[0]]  #시작 라인
    areaOfLayer = getLength(sLine)
    while checkIdx <= len(RP):
        sv.resetLogInfo()
        _120idx = checkIdx+h # 120cm에 해당하는 인덱스
        _240idx = checkIdx+_120idx*2  # 240cm에 해당하는 인덱스
        _360idx = checkIdx+_120idx*3   # 360cm에 해당하는 인덱스
        _480idx = checkIdx+_120idx*4  # 480cm에 해당하는 인덱스
        _600idx = checkIdx+_120idx*5  # 600cm에 해당하는 인덱스
        _maxVal = -1

        if _120idx<len(RP): #_120idx가 rightPoints범위 내에 있다면,,,,
            edia = abs(LP[_120idx][0]-RP[_120idx][0])* sv.breast_h1px #말구 직경 측정
            inter4 = International4(edia, 120) 
            interList.append(inter4) 
            
            if inter4>_maxVal: #inter4 값이 최대값보다 크다면 최대값으로 설정
                _maxVal = inter4
                checkIdx = _120idx # 체크포인트를 _120idx지점으로 지정
        if _240idx<len(RP): # _240idx가 rightPoints범위 내에 있다면,,,,
            edia = abs(LP[_240idx][0]-RP[_240idx][0])* sv.breast_h1px # 말구직경 측정
            inter8 = International8(edia, 240)
            interList.append(inter8)
            
            if inter8>_maxVal:  # inter8값이 최대값보다 크다면 최대값으로 설정
                _maxVal = inter8
                checkIdx = _240idx # 체크포인트를 _240idx지점으로 지정
        if _360idx<len(RP):
            edia = abs(LP[_360idx][0]-RP[_360idx][0])* sv.breast_h1px
            inter12 = International12(edia, 360)
            interList.append(inter12)
            
            if inter12>_maxVal:
                _maxVal = inter12
                checkIdx = _360idx
        if _480idx<len(RP):
            edia = abs(LP[_480idx][0]-RP[_480idx][0])* sv.breast_h1px
            inter16 = International16(edia, 480)
            interList.append(inter16)
            
            if inter16>_maxVal:
                _maxVal = inter16
                checkIdx = _480idx
        if _600idx<len(RP):
            edia = abs(LP[_600idx][0]-RP[_600idx][0])* sv.breast_h1px
            inter20 = International20(edia, 600)
            interList.append(inter20)
            if inter20>_maxVal:
                _maxVal = inter20
                checkIdx = _600idx

        if pre_checkIdx == checkIdx: # 이전루프때의 체크포인트와 현재 체크포인트가 같다면
            break                    # 최대값 변화가 없거나, 120cm가 안되는경우이니 while문 벗어나기
        
        
        eLine=[LP[checkIdx],RP[checkIdx]] # 끝라인
        if getLength(sLine) > getLength(eLine): #밑변이 더 길다면, 윗변에 맞춰서 사이즈조정
            amend(sLine, eLine)
        
        sv.logInfo["mTreeVolume"] = round(_maxVal,4)  # idx번째 이용재적
        hh = max(abs(LP[checkIdx][1]-LP[pre_checkIdx][1]),abs(RP[checkIdx][1]-RP[pre_checkIdx][1])) 
                            # 사각형 높이 
        sv.logInfo["logLength"] = round(hh* sv.breast_h1px,1)         # idx번째 통나무 높이(cm)   
        
        eMidPoint = [(LP[checkIdx][0]+RP[checkIdx][0])//2,LP[checkIdx][1]] # 끝라인 중점
        sMidPoint = [(sLine[0][0]+sLine[1][0])//2, sLine[0][1]] # 시작라인 중점
        
        
        sweepDegree = round((eMidPoint[0]-sMidPoint[0])/hh*100,1) # idx번째 굽음도 
        sv.logInfo["logSweep"] = str(sweepDegree)+" (Left)" if sweepDegree <0 else str(sweepDegree)+" (Right)"
        
        sv.logInfo["eDiameter"] = round(getLength(eLine),1) # idx번째 말구직경
        
        srcPts = list()
        for index in range(pre_checkIdx+1,checkIdx+1):
            srcPts.append(LP[index])
        for index in range(checkIdx, pre_checkIdx-1, -1):
            srcPts.append(RP[index])
        srcPts = np.array(srcPts, np.int32)
        
        
        pts = np.array([eLine[0],eLine[1],sLine[1],sLine[0]], np.int32)
        sv.logInfo["deduction"]= getDeductionPixel(srcPts,pts)
        polygonsList.append(pts)
        
        rtn.append(sv.logInfo.copy())
        
        cv2.fillPoly(cp, [pts],(100,120,240))
        cv2.line(cp, tuple(LP[pre_checkIdx]), tuple(RP[pre_checkIdx]), (255,255,255),2)
        pre_checkIdx = checkIdx # 현재 체크포인트를 이전체크포인트로 변경
        sLine = eLine
        interList.clear()
    sv.pointsList_int = polygonsList 
    return cp, rtn