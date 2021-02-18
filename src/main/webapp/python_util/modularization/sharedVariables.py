import os
# 각 모듈간 공유되는 변수 
"""
공유 항목

# 파일 경로

# 원본 이미지1 (800x400사이즈)  ori_img
# 이미지2 (800x400사이즈)   oriimg
# 마스크  mask2
# 흉고높이(120cm)에 해당하는 픽셀수 => ( 1픽셀당 cm  : 가로, 세로),  breast_h1px, dbh1px
# 흉고AR y좌표점  p_cy
# 좌측점 리스트, 우측점리스트, 센터점 리스트, leftPoints, rightPoints, centerPoints
# 원거리지점 ,사이즈(센터점리스트 길이) distantPoint, size(len(centerPoints))
# 다각형 좌표 리스트  pointsList 
"""


# 기본 루트 디렉토리 경로
ROOT_DIR = os.path.abspath('/Users/himuc/Downloads/Mask_RCNN-20210108T021008Z-001/Mask_RCNN')
# 기본 루트 디렉토리 경로 내의 logs 폴더
logPath = ROOT_DIR + '/logs'
# 학습된 mask_rcnn_fists_0030.h5가 있는 경로
hdfPath = '/Users/himuc/Desktop/mask_rcnn_fists_0030/mask_rcnn_fists_0030.h5'
# mask_rcnn_coco.h5 (기본 coo모델)경로
COCO_WEIGHTS_PATH = os.path.join(ROOT_DIR, 'logs')


config = None
model = None
weight_path=None

ori_img = None
ori_img2 = None
oriimg = None
mask2 = None
splash2 = None


btContour = None
stemContour = None
p_cy = None

breast_h1px = 0
dbh1px = 0
leftPoints = None
rightPoints = None
centerPoints = None
distantPoint = None
size = 0

logInfo={
    "mTreeVolume":0, #이용재적  mTreeVolume_list
    "logLength":0, #통나무높이 logLength_list
    "logSweep":0, #굽음도  logSweep_list
    "eDiameter":0, # 말구직경  dia_list
    "deduction":0 #공제량  
}
def resetLogInfo():
    global logInfo
    logInfo={
    "mTreeVolume":0, #이용재적  mTreeVolume_list
    "logLength":0, #통나무높이 logLength_list
    "logSweep":0, #굽음도  logSweep_list
    "eDiameter":0, # 말구직경  dia_list
    "deduction":0 #공제량  
    }

pointsList_fom = None
pointsList_int = None

def getPointsList_form():
    return pointsList_fom