"""
프로젝트: 모바일을 활용한 산림조사 AI 자동분석

연구책임자: 김동현
연구원: 김선재, 성은지, 이종서

연구내용: 모바일 기기인 스마트폰으로 매목조사를 수행하고 컴퓨터 비전기술 및 클라우드 컴퓨팅 기술을 활용하여 실시간으로 임목에 대한 정보를
분석하는 프로그램을 개발함

연구에 활용된 기술: AR, Mobile, Deep Learning(Mask R-CNN), Cloud Computing, Motion Sensor
"""
import cv2
import numpy as np
import os
import argparse
import math

import requests
import json

import sharedVariables as sv
import rcnnModel
import merchantableTreeVolumeFunc as mTV
import imageProcessing as imgPs
from decimal import Decimal, ROUND_HALF_UP, ROUND_HALF_EVEN

import tensorflow as tf
os.environ['TF_CPP_MIN_LOG_LEVEL'] = '2'
tf.compat.v1.logging.set_verbosity(tf.compat.v1.logging.ERROR)

img = '92.jpg'

parser = argparse.ArgumentParser(description="Add path of image")
parser.add_argument("-f", "--file", dest="argPath", action="store")
parser.add_argument("-m", "--method", dest="argMethod", action="store")  
args = parser.parse_args()
p=args.argPath
imgPath=p
method = args.argMethod    


    
# 모델 생성
rcnnModel.createRCNN_Model(imgPath)

# 좌측점,우측점,센터점, 가장원거리 점 추출
imgPs.getImageInfo(sv.ori_img.copy(),sv.mask2.copy())
# AR객체 찾아내고 1px당  몇cm인지 파악
imgPs.figureOutARObject(sv.ori_img.copy())


if method == "inter":
    overlayImg,result= mTV.useInternationalFormula()
else:
    overlayImg,result=mTV.useFormula(method)




for log in result:
    print(log)

alpha = 0.5
image_new = cv2.addWeighted(overlayImg, alpha, sv.ori_img2, 1 - alpha, 0)

fileName = imgPath[imgPath.rfind('\\')+1:]
treeId = fileName[fileName.find("_")+1:fileName.rfind(".")]

returnData = {
    "tid":treeId,
    "method":method,
    "totalVolume":0,
    "logsVolume":"",
    "logsLength":"",
    "logsEdia":"",
    "logsSweep":"",
    "deduction":0
}

for info in result:
    returnData["totalVolume"]+=info["mTreeVolume"] 
    returnData["logsVolume"] += str(info["mTreeVolume"])+","
    returnData["logsLength"] += str(info["logLength"])+","
    returnData["logsEdia"] += str(info["eDiameter"])+","
    returnData["logsSweep"] += str(info["logSweep"])+","
    returnData["deduction"]+=info["deduction"]

returnData["totalVolume"]=round(returnData["totalVolume"],4)  
returnData["logsVolume"]=returnData["logsVolume"][:-1] 
returnData["logsLength"]=returnData["logsLength"][:-1] 
returnData["logsEdia"] = returnData["logsEdia"][:-1]
returnData["logsSweep"]=returnData["logsSweep"][:-1]


for key,val in returnData.items():
    returnData[key]=str(val)
        

print(result)
print(returnData)

sendResult = {"json":returnData}

fileName = imgPath[imgPath.rfind('\\')+1:]
fileFullName= "../../tree_analyzed_image/"+fileName

sendResult["imgName"]=fileName

#cv2.imwrite(fileFullName,image_new)
extension = os.path.splitext(fileFullName)[1]
result, encoded_img = cv2.imencode(extension, image_new)
if result:
    with open(fileFullName, mode='w+b') as f:
        encoded_img.tofile(f)


rr=requests.post('http://192.168.0.17:8080/analysis/analyzeddata', json.dumps(sendResult))



# im = cv2.imread(fileFullName)
# cv2.imshow("img",im)
# cv2.waitKey()
# cv2.destroyAllWindows()
